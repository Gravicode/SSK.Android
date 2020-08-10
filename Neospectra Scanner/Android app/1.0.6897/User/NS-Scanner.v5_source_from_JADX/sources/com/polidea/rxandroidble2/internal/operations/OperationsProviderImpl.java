package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.RequiresApi;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationAckStrategy;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy;
import com.polidea.rxandroidble2.internal.connection.PayloadSizeLimitProvider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.util.RxBleServicesLogger;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;

public class OperationsProviderImpl implements OperationsProvider {
    private final RxBleServicesLogger bleServicesLogger;
    private final BluetoothGatt bluetoothGatt;
    private final Scheduler bluetoothInteractionScheduler;
    private final Provider<ReadRssiOperation> rssiReadOperationProvider;
    private final RxBleGattCallback rxBleGattCallback;
    private final TimeoutConfiguration timeoutConfiguration;
    private final Scheduler timeoutScheduler;

    @Inject
    OperationsProviderImpl(RxBleGattCallback rxBleGattCallback2, BluetoothGatt bluetoothGatt2, RxBleServicesLogger bleServicesLogger2, @Named("operation-timeout") TimeoutConfiguration timeoutConfiguration2, @Named("bluetooth_interaction") Scheduler bluetoothInteractionScheduler2, @Named("timeout") Scheduler timeoutScheduler2, Provider<ReadRssiOperation> rssiReadOperationProvider2) {
        this.rxBleGattCallback = rxBleGattCallback2;
        this.bluetoothGatt = bluetoothGatt2;
        this.bleServicesLogger = bleServicesLogger2;
        this.timeoutConfiguration = timeoutConfiguration2;
        this.bluetoothInteractionScheduler = bluetoothInteractionScheduler2;
        this.timeoutScheduler = timeoutScheduler2;
        this.rssiReadOperationProvider = rssiReadOperationProvider2;
    }

    public CharacteristicLongWriteOperation provideLongWriteOperation(BluetoothGattCharacteristic bluetoothGattCharacteristic, WriteOperationAckStrategy writeOperationAckStrategy, WriteOperationRetryStrategy writeOperationRetryStrategy, PayloadSizeLimitProvider maxBatchSizeProvider, byte[] bytes) {
        CharacteristicLongWriteOperation characteristicLongWriteOperation = new CharacteristicLongWriteOperation(this.bluetoothGatt, this.rxBleGattCallback, this.bluetoothInteractionScheduler, this.timeoutConfiguration, bluetoothGattCharacteristic, maxBatchSizeProvider, writeOperationAckStrategy, writeOperationRetryStrategy, bytes);
        return characteristicLongWriteOperation;
    }

    @RequiresApi(api = 21)
    public MtuRequestOperation provideMtuChangeOperation(int requestedMtu) {
        return new MtuRequestOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, requestedMtu);
    }

    public CharacteristicReadOperation provideReadCharacteristic(BluetoothGattCharacteristic characteristic) {
        return new CharacteristicReadOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, characteristic);
    }

    public DescriptorReadOperation provideReadDescriptor(BluetoothGattDescriptor descriptor) {
        return new DescriptorReadOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, descriptor);
    }

    public ReadRssiOperation provideRssiReadOperation() {
        return (ReadRssiOperation) this.rssiReadOperationProvider.get();
    }

    public ServiceDiscoveryOperation provideServiceDiscoveryOperation(long timeout, TimeUnit timeUnit) {
        return new ServiceDiscoveryOperation(this.rxBleGattCallback, this.bluetoothGatt, this.bleServicesLogger, new TimeoutConfiguration(timeout, timeUnit, this.timeoutScheduler));
    }

    public CharacteristicWriteOperation provideWriteCharacteristic(BluetoothGattCharacteristic characteristic, byte[] data) {
        CharacteristicWriteOperation characteristicWriteOperation = new CharacteristicWriteOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, characteristic, data);
        return characteristicWriteOperation;
    }

    public DescriptorWriteOperation provideWriteDescriptor(BluetoothGattDescriptor bluetoothGattDescriptor, byte[] data) {
        DescriptorWriteOperation descriptorWriteOperation = new DescriptorWriteOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, 2, bluetoothGattDescriptor, data);
        return descriptorWriteOperation;
    }

    @RequiresApi(api = 21)
    public ConnectionPriorityChangeOperation provideConnectionPriorityChangeOperation(int connectionPriority, long delay, TimeUnit timeUnit) {
        ConnectionPriorityChangeOperation connectionPriorityChangeOperation = new ConnectionPriorityChangeOperation(this.rxBleGattCallback, this.bluetoothGatt, this.timeoutConfiguration, connectionPriority, delay, timeUnit, this.timeoutScheduler);
        return connectionPriorityChangeOperation;
    }
}
