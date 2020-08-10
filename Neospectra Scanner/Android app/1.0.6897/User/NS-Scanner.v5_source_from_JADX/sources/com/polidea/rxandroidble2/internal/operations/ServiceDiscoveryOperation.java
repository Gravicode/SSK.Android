package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleGattCallbackTimeoutException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.SingleResponseOperation;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.util.RxBleServicesLogger;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;
import p005io.reactivex.SingleSource;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;

public class ServiceDiscoveryOperation extends SingleResponseOperation<RxBleDeviceServices> {
    /* access modifiers changed from: private */
    public final RxBleServicesLogger bleServicesLogger;
    /* access modifiers changed from: private */
    public final BluetoothGatt bluetoothGatt;

    ServiceDiscoveryOperation(RxBleGattCallback rxBleGattCallback, BluetoothGatt bluetoothGatt2, RxBleServicesLogger bleServicesLogger2, TimeoutConfiguration timeoutConfiguration) {
        super(bluetoothGatt2, rxBleGattCallback, BleGattOperationType.SERVICE_DISCOVERY, timeoutConfiguration);
        this.bluetoothGatt = bluetoothGatt2;
        this.bleServicesLogger = bleServicesLogger2;
    }

    /* access modifiers changed from: protected */
    public Single<RxBleDeviceServices> getCallback(RxBleGattCallback rxBleGattCallback) {
        return rxBleGattCallback.getOnServicesDiscovered().firstOrError().doOnSuccess(new Consumer<RxBleDeviceServices>() {
            public void accept(RxBleDeviceServices rxBleDeviceServices) throws Exception {
                ServiceDiscoveryOperation.this.bleServicesLogger.log(rxBleDeviceServices, ServiceDiscoveryOperation.this.bluetoothGatt.getDevice());
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean startOperation(BluetoothGatt bluetoothGatt2) {
        return bluetoothGatt2.discoverServices();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public Single<RxBleDeviceServices> timeoutFallbackProcedure(final BluetoothGatt bluetoothGatt2, RxBleGattCallback rxBleGattCallback, final Scheduler timeoutScheduler) {
        return Single.defer(new Callable<SingleSource<? extends RxBleDeviceServices>>() {
            public SingleSource<? extends RxBleDeviceServices> call() throws Exception {
                if (bluetoothGatt2.getServices().size() == 0) {
                    return Single.error((Throwable) new BleGattCallbackTimeoutException(bluetoothGatt2, BleGattOperationType.SERVICE_DISCOVERY));
                }
                return Single.timer(5, TimeUnit.SECONDS, timeoutScheduler).flatMap(new Function<Long, Single<RxBleDeviceServices>>() {
                    public Single<RxBleDeviceServices> apply(Long delayedSeconds) {
                        return Single.fromCallable(new Callable<RxBleDeviceServices>() {
                            public RxBleDeviceServices call() throws Exception {
                                return new RxBleDeviceServices(bluetoothGatt2.getServices());
                            }
                        });
                    }
                });
            }
        });
    }
}
