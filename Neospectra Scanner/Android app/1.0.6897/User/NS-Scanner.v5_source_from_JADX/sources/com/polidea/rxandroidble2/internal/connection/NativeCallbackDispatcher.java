package com.polidea.rxandroidble2.internal.connection;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import bleshadow.javax.inject.Inject;

class NativeCallbackDispatcher {
    private BluetoothGattCallback nativeCallback;

    @Inject
    NativeCallbackDispatcher() {
    }

    public void notifyNativeChangedCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onCharacteristicChanged(gatt, characteristic);
        }
    }

    public void notifyNativeConnectionStateCallback(BluetoothGatt gatt, int status, int newState) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onConnectionStateChange(gatt, status, newState);
        }
    }

    public void notifyNativeDescriptorReadCallback(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onDescriptorRead(gatt, descriptor, status);
        }
    }

    public void notifyNativeDescriptorWriteCallback(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onDescriptorWrite(gatt, descriptor, status);
        }
    }

    @TargetApi(21)
    public void notifyNativeMtuChangedCallback(BluetoothGatt gatt, int mtu, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onMtuChanged(gatt, mtu, status);
        }
    }

    public void notifyNativeReadRssiCallback(BluetoothGatt gatt, int rssi, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onReadRemoteRssi(gatt, rssi, status);
        }
    }

    public void notifyNativeReliableWriteCallback(BluetoothGatt gatt, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onReliableWriteCompleted(gatt, status);
        }
    }

    public void notifyNativeServicesDiscoveredCallback(BluetoothGatt gatt, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onServicesDiscovered(gatt, status);
        }
    }

    public void notifyNativeWriteCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onCharacteristicWrite(gatt, characteristic, status);
        }
    }

    /* access modifiers changed from: 0000 */
    public void setNativeCallback(BluetoothGattCallback callback) {
        this.nativeCallback = callback;
    }

    /* access modifiers changed from: 0000 */
    public void notifyNativeReadCallback(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (this.nativeCallback != null) {
            this.nativeCallback.onCharacteristicRead(gatt, characteristic, status);
        }
    }
}
