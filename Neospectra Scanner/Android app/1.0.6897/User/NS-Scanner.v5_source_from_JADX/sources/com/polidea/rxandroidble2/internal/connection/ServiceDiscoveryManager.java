package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Maybe;
import p005io.reactivex.Single;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.schedulers.Schedulers;
import p005io.reactivex.subjects.BehaviorSubject;
import p005io.reactivex.subjects.Subject;

@ConnectionScope
class ServiceDiscoveryManager {
    /* access modifiers changed from: private */
    public final BluetoothGatt bluetoothGatt;
    private Single<RxBleDeviceServices> deviceServicesObservable;
    /* access modifiers changed from: private */
    public boolean hasCachedResults = false;
    /* access modifiers changed from: private */
    public final OperationsProvider operationProvider;
    /* access modifiers changed from: private */
    public final ConnectionOperationQueue operationQueue;
    /* access modifiers changed from: private */
    public Subject<TimeoutConfiguration> timeoutBehaviorSubject = BehaviorSubject.create().toSerialized();

    @Inject
    ServiceDiscoveryManager(ConnectionOperationQueue operationQueue2, BluetoothGatt bluetoothGatt2, OperationsProvider operationProvider2) {
        this.operationQueue = operationQueue2;
        this.bluetoothGatt = bluetoothGatt2;
        this.operationProvider = operationProvider2;
        reset();
    }

    /* access modifiers changed from: 0000 */
    public Single<RxBleDeviceServices> getDiscoverServicesSingle(final long timeout, final TimeUnit timeoutTimeUnit) {
        if (this.hasCachedResults) {
            return this.deviceServicesObservable;
        }
        return this.deviceServicesObservable.doOnSubscribe(new Consumer<Disposable>() {
            public void accept(Disposable disposable) throws Exception {
                ServiceDiscoveryManager.this.timeoutBehaviorSubject.onNext(new TimeoutConfiguration(timeout, timeoutTimeUnit, Schedulers.computation()));
            }
        });
    }

    /* access modifiers changed from: private */
    public void reset() {
        this.hasCachedResults = false;
        this.deviceServicesObservable = getListOfServicesFromGatt().map(wrapIntoRxBleDeviceServices()).switchIfEmpty((SingleSource<? extends T>) getTimeoutConfiguration().flatMap(scheduleActualDiscoveryWithTimeout())).doOnSuccess(Functions.actionConsumer(new Action() {
            public void run() throws Exception {
                ServiceDiscoveryManager.this.hasCachedResults = true;
            }
        })).doOnError(Functions.actionConsumer(new Action() {
            public void run() {
                ServiceDiscoveryManager.this.reset();
            }
        })).cache();
    }

    @NonNull
    private Function<List<BluetoothGattService>, RxBleDeviceServices> wrapIntoRxBleDeviceServices() {
        return new Function<List<BluetoothGattService>, RxBleDeviceServices>() {
            public RxBleDeviceServices apply(List<BluetoothGattService> bluetoothGattServices) {
                return new RxBleDeviceServices(bluetoothGattServices);
            }
        };
    }

    private Maybe<List<BluetoothGattService>> getListOfServicesFromGatt() {
        return Single.fromCallable(new Callable<List<BluetoothGattService>>() {
            public List<BluetoothGattService> call() {
                return ServiceDiscoveryManager.this.bluetoothGatt.getServices();
            }
        }).filter(new Predicate<List<BluetoothGattService>>() {
            public boolean test(List<BluetoothGattService> bluetoothGattServices) {
                return bluetoothGattServices.size() > 0;
            }
        });
    }

    @NonNull
    private Single<TimeoutConfiguration> getTimeoutConfiguration() {
        return this.timeoutBehaviorSubject.firstOrError();
    }

    @NonNull
    private Function<TimeoutConfiguration, Single<RxBleDeviceServices>> scheduleActualDiscoveryWithTimeout() {
        return new Function<TimeoutConfiguration, Single<RxBleDeviceServices>>() {
            public Single<RxBleDeviceServices> apply(TimeoutConfiguration timeoutConf) {
                return ServiceDiscoveryManager.this.operationQueue.queue(ServiceDiscoveryManager.this.operationProvider.provideServiceDiscoveryOperation(timeoutConf.timeout, timeoutConf.timeoutTimeUnit)).firstOrError();
            }
        };
    }
}
