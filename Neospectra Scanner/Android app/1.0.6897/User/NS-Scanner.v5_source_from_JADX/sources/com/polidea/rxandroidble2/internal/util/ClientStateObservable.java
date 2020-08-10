package com.polidea.rxandroidble2.internal.util;

import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleAdapterStateObservable.BleAdapterState;
import com.polidea.rxandroidble2.RxBleClient.State;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

public class ClientStateObservable extends Observable<State> {
    /* access modifiers changed from: private */
    public final Observable<BleAdapterState> bleAdapterStateObservable;
    /* access modifiers changed from: private */
    public final Observable<Boolean> locationServicesOkObservable;
    private final LocationServicesStatus locationServicesStatus;
    /* access modifiers changed from: private */
    public final RxBleAdapterWrapper rxBleAdapterWrapper;
    private final Scheduler timerScheduler;

    @Inject
    protected ClientStateObservable(RxBleAdapterWrapper rxBleAdapterWrapper2, Observable<BleAdapterState> bleAdapterStateObservable2, @Named("location-ok-boolean-observable") Observable<Boolean> locationServicesOkObservable2, LocationServicesStatus locationServicesStatus2, @Named("timeout") Scheduler timerScheduler2) {
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
        this.bleAdapterStateObservable = bleAdapterStateObservable2;
        this.locationServicesOkObservable = locationServicesOkObservable2;
        this.locationServicesStatus = locationServicesStatus2;
        this.timerScheduler = timerScheduler2;
    }

    @NonNull
    private static Single<Boolean> checkPermissionUntilGranted(final LocationServicesStatus locationServicesStatus2, Scheduler timerScheduler2) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, timerScheduler2).takeWhile(new Predicate<Long>() {
            public boolean test(Long timer) {
                return !locationServicesStatus2.isLocationPermissionOk();
            }
        }).count().map(new Function<Long, Boolean>() {
            public Boolean apply(Long count) throws Exception {
                return Boolean.valueOf(count.longValue() == 0);
            }
        });
    }

    /* access modifiers changed from: private */
    @NonNull
    public static Observable<State> checkAdapterAndServicesState(Boolean permissionWasInitiallyGranted, RxBleAdapterWrapper rxBleAdapterWrapper2, Observable<BleAdapterState> rxBleAdapterStateObservable, final Observable<Boolean> locationServicesOkObservable2) {
        Observable<State> stateObservable = rxBleAdapterStateObservable.startWith(rxBleAdapterWrapper2.isBluetoothEnabled() ? BleAdapterState.STATE_ON : BleAdapterState.STATE_OFF).switchMap(new Function<BleAdapterState, Observable<State>>() {
            public Observable<State> apply(BleAdapterState bleAdapterState) {
                if (bleAdapterState != BleAdapterState.STATE_ON) {
                    return Observable.just(State.BLUETOOTH_NOT_ENABLED);
                }
                return locationServicesOkObservable2.map(new Function<Boolean, State>() {
                    public State apply(Boolean locationServicesOk) {
                        return locationServicesOk.booleanValue() ? State.READY : State.LOCATION_SERVICES_NOT_ENABLED;
                    }
                });
            }
        });
        return permissionWasInitiallyGranted.booleanValue() ? stateObservable.skip(1) : stateObservable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super State> observer) {
        if (!this.rxBleAdapterWrapper.hasBluetoothAdapter()) {
            observer.onSubscribe(Disposables.empty());
            observer.onComplete();
            return;
        }
        checkPermissionUntilGranted(this.locationServicesStatus, this.timerScheduler).flatMapObservable(new Function<Boolean, Observable<State>>() {
            public Observable<State> apply(Boolean permissionWasInitiallyGranted) {
                return ClientStateObservable.checkAdapterAndServicesState(permissionWasInitiallyGranted, ClientStateObservable.this.rxBleAdapterWrapper, ClientStateObservable.this.bleAdapterStateObservable, ClientStateObservable.this.locationServicesOkObservable);
            }
        }).distinctUntilChanged().subscribe(observer);
    }
}
