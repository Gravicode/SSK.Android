package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class NotificationAndIndicationManager_Factory implements Factory<NotificationAndIndicationManager> {
    private final Provider<BluetoothGatt> bluetoothGattProvider;
    private final Provider<byte[]> configDisableProvider;
    private final Provider<byte[]> configEnableIndicationProvider;
    private final Provider<byte[]> configEnableNotificationProvider;
    private final Provider<DescriptorWriter> descriptorWriterProvider;
    private final Provider<RxBleGattCallback> gattCallbackProvider;

    public NotificationAndIndicationManager_Factory(Provider<byte[]> configEnableNotificationProvider2, Provider<byte[]> configEnableIndicationProvider2, Provider<byte[]> configDisableProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<RxBleGattCallback> gattCallbackProvider2, Provider<DescriptorWriter> descriptorWriterProvider2) {
        this.configEnableNotificationProvider = configEnableNotificationProvider2;
        this.configEnableIndicationProvider = configEnableIndicationProvider2;
        this.configDisableProvider = configDisableProvider2;
        this.bluetoothGattProvider = bluetoothGattProvider2;
        this.gattCallbackProvider = gattCallbackProvider2;
        this.descriptorWriterProvider = descriptorWriterProvider2;
    }

    public NotificationAndIndicationManager get() {
        NotificationAndIndicationManager notificationAndIndicationManager = new NotificationAndIndicationManager((byte[]) this.configEnableNotificationProvider.get(), (byte[]) this.configEnableIndicationProvider.get(), (byte[]) this.configDisableProvider.get(), (BluetoothGatt) this.bluetoothGattProvider.get(), (RxBleGattCallback) this.gattCallbackProvider.get(), (DescriptorWriter) this.descriptorWriterProvider.get());
        return notificationAndIndicationManager;
    }

    public static NotificationAndIndicationManager_Factory create(Provider<byte[]> configEnableNotificationProvider2, Provider<byte[]> configEnableIndicationProvider2, Provider<byte[]> configDisableProvider2, Provider<BluetoothGatt> bluetoothGattProvider2, Provider<RxBleGattCallback> gattCallbackProvider2, Provider<DescriptorWriter> descriptorWriterProvider2) {
        NotificationAndIndicationManager_Factory notificationAndIndicationManager_Factory = new NotificationAndIndicationManager_Factory(configEnableNotificationProvider2, configEnableIndicationProvider2, configDisableProvider2, bluetoothGattProvider2, gattCallbackProvider2, descriptorWriterProvider2);
        return notificationAndIndicationManager_Factory;
    }

    public static NotificationAndIndicationManager newNotificationAndIndicationManager(byte[] configEnableNotification, byte[] configEnableIndication, byte[] configDisable, BluetoothGatt bluetoothGatt, RxBleGattCallback gattCallback, Object descriptorWriter) {
        NotificationAndIndicationManager notificationAndIndicationManager = new NotificationAndIndicationManager(configEnableNotification, configEnableIndication, configDisable, bluetoothGatt, gattCallback, (DescriptorWriter) descriptorWriter);
        return notificationAndIndicationManager;
    }
}
