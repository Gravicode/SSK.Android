package com.polidea.rxandroidble2.internal.connection;

import android.util.Log;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import java.util.LinkedList;
import java.util.Queue;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableOnSubscribe;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Cancellable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

@ConnectionScope
class DisconnectionRouter implements DisconnectionRouterInput, DisconnectionRouterOutput {
    private static final String TAG = "DisconnectionRouter";
    private Disposable adapterMonitoringDisposable;
    /* access modifiers changed from: private */
    public final Queue<ObservableEmitter<BleException>> exceptionEmitters = new LinkedList();
    /* access modifiers changed from: private */
    public BleException exceptionOccurred;

    @Inject
    DisconnectionRouter(@Named("mac-address") final String macAddress, RxBleAdapterWrapper adapterWrapper, Observable<BleAdapterState> adapterStateObservable) {
        this.adapterMonitoringDisposable = awaitAdapterNotUsable(adapterWrapper, adapterStateObservable).map(new Function<Boolean, BleException>() {
            public BleException apply(Boolean isAdapterUsable) {
                return new BleDisconnectedException(macAddress);
            }
        }).firstElement().subscribe(new Consumer<BleException>() {
            public void accept(BleException exception) throws Exception {
                Log.d(DisconnectionRouter.TAG, "An exception received, indicating that the adapter has became unusable.");
                DisconnectionRouter.this.exceptionOccurred = exception;
                DisconnectionRouter.this.notifySubscribersAboutException();
            }
        }, new Consumer<Throwable>() {
            public void accept(Throwable throwable) throws Exception {
                Log.w(DisconnectionRouter.TAG, "Failed to monitor adapter state.", throwable);
            }
        });
    }

    private static Observable<Boolean> awaitAdapterNotUsable(RxBleAdapterWrapper adapterWrapper, Observable<BleAdapterState> stateChanges) {
        return stateChanges.map(new Function<BleAdapterState, Boolean>() {
            public Boolean apply(BleAdapterState bleAdapterState) {
                return Boolean.valueOf(bleAdapterState.isUsable());
            }
        }).startWith(Boolean.valueOf(adapterWrapper.isBluetoothEnabled())).filter(new Predicate<Boolean>() {
            public boolean test(Boolean isAdapterUsable) {
                return !isAdapterUsable.booleanValue();
            }
        });
    }

    public void onDisconnectedException(BleDisconnectedException disconnectedException) {
        onExceptionOccurred(disconnectedException);
    }

    public void onGattConnectionStateException(BleGattException disconnectedGattException) {
        onExceptionOccurred(disconnectedGattException);
    }

    private void onExceptionOccurred(BleException exception) {
        if (this.exceptionOccurred == null) {
            this.exceptionOccurred = exception;
            notifySubscribersAboutException();
        }
    }

    /* access modifiers changed from: private */
    public void notifySubscribersAboutException() {
        if (this.adapterMonitoringDisposable != null) {
            this.adapterMonitoringDisposable.dispose();
        }
        while (!this.exceptionEmitters.isEmpty()) {
            ObservableEmitter<BleException> exceptionEmitter = (ObservableEmitter) this.exceptionEmitters.poll();
            exceptionEmitter.onNext(this.exceptionOccurred);
            exceptionEmitter.onComplete();
        }
    }

    public Observable<BleException> asValueOnlyObservable() {
        return Observable.create(new ObservableOnSubscribe<BleException>() {
            public void subscribe(ObservableEmitter<BleException> emitter) throws Exception {
                if (DisconnectionRouter.this.exceptionOccurred != null) {
                    emitter.onNext(DisconnectionRouter.this.exceptionOccurred);
                    emitter.onComplete();
                    return;
                }
                DisconnectionRouter.this.storeEmitterToBeNotifiedInTheFuture(emitter);
            }
        });
    }

    /* access modifiers changed from: private */
    public void storeEmitterToBeNotifiedInTheFuture(final ObservableEmitter<BleException> emitter) {
        this.exceptionEmitters.add(emitter);
        emitter.setCancellable(new Cancellable() {
            public void cancel() throws Exception {
                DisconnectionRouter.this.exceptionEmitters.remove(emitter);
            }
        });
    }

    public <T> Observable<T> asErrorOnlyObservable() {
        return asValueOnlyObservable().flatMap(new Function<BleException, Observable<T>>() {
            public Observable<T> apply(BleException e) {
                return Observable.error((Throwable) e);
            }
        });
    }
}
