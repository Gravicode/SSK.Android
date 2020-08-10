package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleGattCharacteristicException;
import com.polidea.rxandroidble2.exceptions.BleGattDescriptorException;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.util.ByteAssociation;
import com.polidea.rxandroidble2.internal.util.CharacteristicChangedEvent;
import java.util.UUID;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Function;

@ConnectionScope
public class RxBleGattCallback {
    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            RxBleLog.m17d("onConnectionStateChange newState=%d status=%d", Integer.valueOf(newState), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeConnectionStateCallback(gatt, status, newState);
            super.onConnectionStateChange(gatt, status, newState);
            RxBleGattCallback.this.bluetoothGattProvider.updateBluetoothGatt(gatt);
            if (isDisconnectedOrDisconnecting(newState)) {
                RxBleGattCallback.this.disconnectionRouter.onDisconnectedException(new BleDisconnectedException(gatt.getDevice().getAddress()));
            } else if (status != 0) {
                RxBleGattCallback.this.disconnectionRouter.onGattConnectionStateException(new BleGattException(gatt, status, BleGattOperationType.CONNECTION_STATE));
            }
            RxBleGattCallback.this.connectionStatePublishRelay.accept(RxBleGattCallback.this.mapConnectionStateToRxBleConnectionStatus(newState));
        }

        private boolean isDisconnectedOrDisconnecting(int newState) {
            return newState == 0 || newState == 3;
        }

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            RxBleLog.m17d("onServicesDiscovered status=%d", Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeServicesDiscoveredCallback(gatt, status);
            super.onServicesDiscovered(gatt, status);
            if (RxBleGattCallback.this.servicesDiscoveredOutput.hasObservers() && !RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.servicesDiscoveredOutput, gatt, status, BleGattOperationType.SERVICE_DISCOVERY)) {
                RxBleGattCallback.this.servicesDiscoveredOutput.valueRelay.accept(new RxBleDeviceServices(gatt.getServices()));
            }
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            RxBleLog.m17d("onCharacteristicRead characteristic=%s status=%d", characteristic.getUuid(), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeReadCallback(gatt, characteristic, status);
            super.onCharacteristicRead(gatt, characteristic, status);
            if (RxBleGattCallback.this.readCharacteristicOutput.hasObservers()) {
                if (!RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.readCharacteristicOutput, gatt, characteristic, status, BleGattOperationType.CHARACTERISTIC_READ)) {
                    RxBleGattCallback.this.readCharacteristicOutput.valueRelay.accept(new ByteAssociation(characteristic.getUuid(), characteristic.getValue()));
                }
            }
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            RxBleLog.m17d("onCharacteristicWrite characteristic=%s status=%d", characteristic.getUuid(), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeWriteCallback(gatt, characteristic, status);
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (RxBleGattCallback.this.writeCharacteristicOutput.hasObservers()) {
                if (!RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.writeCharacteristicOutput, gatt, characteristic, status, BleGattOperationType.CHARACTERISTIC_WRITE)) {
                    RxBleGattCallback.this.writeCharacteristicOutput.valueRelay.accept(new ByteAssociation(characteristic.getUuid(), characteristic.getValue()));
                }
            }
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            RxBleLog.m17d("onCharacteristicChanged characteristic=%s", characteristic.getUuid());
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeChangedCallback(gatt, characteristic);
            super.onCharacteristicChanged(gatt, characteristic);
            if (RxBleGattCallback.this.changedCharacteristicSerializedPublishRelay.hasObservers()) {
                RxBleGattCallback.this.changedCharacteristicSerializedPublishRelay.accept(new CharacteristicChangedEvent(characteristic.getUuid(), Integer.valueOf(characteristic.getInstanceId()), characteristic.getValue()));
            }
        }

        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            RxBleLog.m17d("onCharacteristicRead descriptor=%s status=%d", descriptor.getUuid(), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeDescriptorReadCallback(gatt, descriptor, status);
            super.onDescriptorRead(gatt, descriptor, status);
            if (RxBleGattCallback.this.readDescriptorOutput.hasObservers()) {
                if (!RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.readDescriptorOutput, gatt, descriptor, status, BleGattOperationType.DESCRIPTOR_READ)) {
                    RxBleGattCallback.this.readDescriptorOutput.valueRelay.accept(new ByteAssociation(descriptor, descriptor.getValue()));
                }
            }
        }

        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            RxBleLog.m17d("onDescriptorWrite descriptor=%s status=%d", descriptor.getUuid(), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeDescriptorWriteCallback(gatt, descriptor, status);
            super.onDescriptorWrite(gatt, descriptor, status);
            if (RxBleGattCallback.this.writeDescriptorOutput.hasObservers()) {
                if (!RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.writeDescriptorOutput, gatt, descriptor, status, BleGattOperationType.DESCRIPTOR_WRITE)) {
                    RxBleGattCallback.this.writeDescriptorOutput.valueRelay.accept(new ByteAssociation(descriptor, descriptor.getValue()));
                }
            }
        }

        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            RxBleLog.m17d("onReliableWriteCompleted status=%d", Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeReliableWriteCallback(gatt, status);
            super.onReliableWriteCompleted(gatt, status);
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            RxBleLog.m17d("onReadRemoteRssi rssi=%d status=%d", Integer.valueOf(rssi), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeReadRssiCallback(gatt, rssi, status);
            super.onReadRemoteRssi(gatt, rssi, status);
            if (RxBleGattCallback.this.readRssiOutput.hasObservers() && !RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.readRssiOutput, gatt, status, BleGattOperationType.READ_RSSI)) {
                RxBleGattCallback.this.readRssiOutput.valueRelay.accept(Integer.valueOf(rssi));
            }
        }

        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            RxBleLog.m17d("onMtuChanged mtu=%d status=%d", Integer.valueOf(mtu), Integer.valueOf(status));
            RxBleGattCallback.this.nativeCallbackDispatcher.notifyNativeMtuChangedCallback(gatt, mtu, status);
            super.onMtuChanged(gatt, mtu, status);
            if (RxBleGattCallback.this.changedMtuOutput.hasObservers() && !RxBleGattCallback.this.propagateErrorIfOccurred(RxBleGattCallback.this.changedMtuOutput, gatt, status, BleGattOperationType.ON_MTU_CHANGED)) {
                RxBleGattCallback.this.changedMtuOutput.valueRelay.accept(Integer.valueOf(mtu));
            }
        }
    };
    /* access modifiers changed from: private */
    public final BluetoothGattProvider bluetoothGattProvider;
    private final Scheduler callbackScheduler;
    /* access modifiers changed from: private */
    public final Relay<CharacteristicChangedEvent> changedCharacteristicSerializedPublishRelay = PublishRelay.create().toSerialized();
    /* access modifiers changed from: private */
    public final Output<Integer> changedMtuOutput = new Output<>();
    /* access modifiers changed from: private */
    public final PublishRelay<RxBleConnectionState> connectionStatePublishRelay = PublishRelay.create();
    /* access modifiers changed from: private */
    public final DisconnectionRouter disconnectionRouter;
    private final Function<BleGattException, Observable<?>> errorMapper = new Function<BleGattException, Observable<?>>() {
        public Observable<?> apply(BleGattException bleGattException) {
            return Observable.error((Throwable) bleGattException);
        }
    };
    /* access modifiers changed from: private */
    public final NativeCallbackDispatcher nativeCallbackDispatcher;
    /* access modifiers changed from: private */
    public final Output<ByteAssociation<UUID>> readCharacteristicOutput = new Output<>();
    /* access modifiers changed from: private */
    public final Output<ByteAssociation<BluetoothGattDescriptor>> readDescriptorOutput = new Output<>();
    /* access modifiers changed from: private */
    public final Output<Integer> readRssiOutput = new Output<>();
    /* access modifiers changed from: private */
    public final Output<RxBleDeviceServices> servicesDiscoveredOutput = new Output<>();
    /* access modifiers changed from: private */
    public final Output<ByteAssociation<UUID>> writeCharacteristicOutput = new Output<>();
    /* access modifiers changed from: private */
    public final Output<ByteAssociation<BluetoothGattDescriptor>> writeDescriptorOutput = new Output<>();

    private static class Output<T> {
        final PublishRelay<BleGattException> errorRelay = PublishRelay.create();
        final PublishRelay<T> valueRelay = PublishRelay.create();

        Output() {
        }

        /* access modifiers changed from: 0000 */
        public boolean hasObservers() {
            return this.valueRelay.hasObservers() || this.errorRelay.hasObservers();
        }
    }

    @Inject
    public RxBleGattCallback(@Named("bluetooth_callbacks") Scheduler callbackScheduler2, BluetoothGattProvider bluetoothGattProvider2, DisconnectionRouter disconnectionRouter2, NativeCallbackDispatcher nativeCallbackDispatcher2) {
        this.callbackScheduler = callbackScheduler2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.disconnectionRouter = disconnectionRouter2;
        this.nativeCallbackDispatcher = nativeCallbackDispatcher2;
    }

    /* access modifiers changed from: private */
    public RxBleConnectionState mapConnectionStateToRxBleConnectionStatus(int newState) {
        switch (newState) {
            case 1:
                return RxBleConnectionState.CONNECTING;
            case 2:
                return RxBleConnectionState.CONNECTED;
            case 3:
                return RxBleConnectionState.DISCONNECTING;
            default:
                return RxBleConnectionState.DISCONNECTED;
        }
    }

    /* access modifiers changed from: private */
    public boolean propagateErrorIfOccurred(Output output, BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, BleGattOperationType operationType) {
        return isException(status) && propagateStatusError(output, new BleGattCharacteristicException(gatt, characteristic, status, operationType));
    }

    /* access modifiers changed from: private */
    public boolean propagateErrorIfOccurred(Output output, BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status, BleGattOperationType operationType) {
        return isException(status) && propagateStatusError(output, new BleGattDescriptorException(gatt, descriptor, status, operationType));
    }

    /* access modifiers changed from: private */
    public boolean propagateErrorIfOccurred(Output output, BluetoothGatt gatt, int status, BleGattOperationType operationType) {
        return isException(status) && propagateStatusError(output, new BleGattException(gatt, status, operationType));
    }

    private boolean isException(int status) {
        return status != 0;
    }

    private boolean propagateStatusError(Output output, BleGattException exception) {
        output.errorRelay.accept(exception);
        return true;
    }

    private <T> Observable<T> withDisconnectionHandling(Output<T> output) {
        return Observable.merge((ObservableSource<? extends T>) this.disconnectionRouter.asErrorOnlyObservable(), (ObservableSource<? extends T>) output.valueRelay, (ObservableSource<? extends T>) output.errorRelay.flatMap(this.errorMapper));
    }

    public BluetoothGattCallback getBluetoothGattCallback() {
        return this.bluetoothGattCallback;
    }

    public <T> Observable<T> observeDisconnect() {
        return this.disconnectionRouter.asErrorOnlyObservable();
    }

    public Observable<RxBleConnectionState> getOnConnectionStateChange() {
        return this.connectionStatePublishRelay.observeOn(this.callbackScheduler);
    }

    public Observable<RxBleDeviceServices> getOnServicesDiscovered() {
        return withDisconnectionHandling(this.servicesDiscoveredOutput).observeOn(this.callbackScheduler);
    }

    public Observable<Integer> getOnMtuChanged() {
        return withDisconnectionHandling(this.changedMtuOutput).observeOn(this.callbackScheduler);
    }

    public Observable<ByteAssociation<UUID>> getOnCharacteristicRead() {
        return withDisconnectionHandling(this.readCharacteristicOutput).observeOn(this.callbackScheduler);
    }

    public Observable<ByteAssociation<UUID>> getOnCharacteristicWrite() {
        return withDisconnectionHandling(this.writeCharacteristicOutput).observeOn(this.callbackScheduler);
    }

    public Observable<CharacteristicChangedEvent> getOnCharacteristicChanged() {
        return Observable.merge((ObservableSource<? extends T>) this.disconnectionRouter.asErrorOnlyObservable(), (ObservableSource<? extends T>) this.changedCharacteristicSerializedPublishRelay).observeOn(this.callbackScheduler);
    }

    public Observable<ByteAssociation<BluetoothGattDescriptor>> getOnDescriptorRead() {
        return withDisconnectionHandling(this.readDescriptorOutput).observeOn(this.callbackScheduler);
    }

    public Observable<ByteAssociation<BluetoothGattDescriptor>> getOnDescriptorWrite() {
        return withDisconnectionHandling(this.writeDescriptorOutput).observeOn(this.callbackScheduler);
    }

    public Observable<Integer> getOnRssiRead() {
        return withDisconnectionHandling(this.readRssiOutput).observeOn(this.callbackScheduler);
    }

    public void setNativeCallback(BluetoothGattCallback callback) {
        this.nativeCallbackDispatcher.setNativeCallback(callback);
    }
}
