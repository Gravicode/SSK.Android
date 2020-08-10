package com.polidea.rxandroidble2;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.exceptions.BleCharacteristicNotFoundException;
import com.polidea.rxandroidble2.exceptions.BleDescriptorNotFoundException;
import com.polidea.rxandroidble2.exceptions.BleServiceNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import p005io.reactivex.Observable;
import p005io.reactivex.Single;
import p005io.reactivex.SingleSource;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

public class RxBleDeviceServices {
    /* access modifiers changed from: private */
    public final List<BluetoothGattService> bluetoothGattServices;

    public RxBleDeviceServices(List<BluetoothGattService> bluetoothGattServices2) {
        this.bluetoothGattServices = bluetoothGattServices2;
    }

    public List<BluetoothGattService> getBluetoothGattServices() {
        return this.bluetoothGattServices;
    }

    public Single<BluetoothGattService> getService(@NonNull final UUID serviceUuid) {
        return Observable.fromIterable(this.bluetoothGattServices).filter(new Predicate<BluetoothGattService>() {
            public boolean test(BluetoothGattService bluetoothGattService) throws Exception {
                return bluetoothGattService.getUuid().equals(serviceUuid);
            }
        }).firstElement().switchIfEmpty((SingleSource<? extends T>) Single.error((Throwable) new BleServiceNotFoundException(serviceUuid)));
    }

    public Single<BluetoothGattCharacteristic> getCharacteristic(@NonNull final UUID characteristicUuid) {
        return Single.fromCallable(new Callable<BluetoothGattCharacteristic>() {
            public BluetoothGattCharacteristic call() throws Exception {
                for (BluetoothGattService service : RxBleDeviceServices.this.bluetoothGattServices) {
                    BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicUuid);
                    if (characteristic != null) {
                        return characteristic;
                    }
                }
                throw new BleCharacteristicNotFoundException(characteristicUuid);
            }
        });
    }

    public Single<BluetoothGattCharacteristic> getCharacteristic(@NonNull UUID serviceUuid, @NonNull final UUID characteristicUuid) {
        return getService(serviceUuid).map(new Function<BluetoothGattService, BluetoothGattCharacteristic>() {
            public BluetoothGattCharacteristic apply(BluetoothGattService bluetoothGattService) {
                BluetoothGattCharacteristic characteristic = bluetoothGattService.getCharacteristic(characteristicUuid);
                if (characteristic != null) {
                    return characteristic;
                }
                throw new BleCharacteristicNotFoundException(characteristicUuid);
            }
        });
    }

    public Single<BluetoothGattDescriptor> getDescriptor(UUID characteristicUuid, final UUID descriptorUuid) {
        return getCharacteristic(characteristicUuid).map(new Function<BluetoothGattCharacteristic, BluetoothGattDescriptor>() {
            public BluetoothGattDescriptor apply(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(descriptorUuid);
                if (descriptor != null) {
                    return descriptor;
                }
                throw new BleDescriptorNotFoundException(descriptorUuid);
            }
        });
    }

    public Single<BluetoothGattDescriptor> getDescriptor(UUID serviceUuid, final UUID characteristicUuid, final UUID descriptorUuid) {
        return getService(serviceUuid).map(new Function<BluetoothGattService, BluetoothGattCharacteristic>() {
            public BluetoothGattCharacteristic apply(BluetoothGattService bluetoothGattService) {
                return bluetoothGattService.getCharacteristic(characteristicUuid);
            }
        }).map(new Function<BluetoothGattCharacteristic, BluetoothGattDescriptor>() {
            public BluetoothGattDescriptor apply(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(descriptorUuid);
                if (descriptor != null) {
                    return descriptor;
                }
                throw new BleDescriptorNotFoundException(descriptorUuid);
            }
        });
    }
}
