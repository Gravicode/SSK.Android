package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleConnection.LongWriteOperationBuilder;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationAckStrategy;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import java.util.UUID;
import p005io.reactivex.Observable;
import p005io.reactivex.Single;
import p005io.reactivex.functions.Function;

public final class LongWriteOperationBuilderImpl implements LongWriteOperationBuilder {
    /* access modifiers changed from: private */
    public byte[] bytes;
    /* access modifiers changed from: private */
    public PayloadSizeLimitProvider maxBatchSizeProvider;
    /* access modifiers changed from: private */
    public final ConnectionOperationQueue operationQueue;
    /* access modifiers changed from: private */
    public final OperationsProvider operationsProvider;
    private final RxBleConnection rxBleConnection;
    /* access modifiers changed from: private */
    public WriteOperationAckStrategy writeOperationAckStrategy = new ImmediateSerializedBatchAckStrategy();
    /* access modifiers changed from: private */
    public WriteOperationRetryStrategy writeOperationRetryStrategy = new NoRetryStrategy();
    private Single<BluetoothGattCharacteristic> writtenCharacteristicObservable;

    @Inject
    LongWriteOperationBuilderImpl(ConnectionOperationQueue operationQueue2, MtuBasedPayloadSizeLimit defaultMaxBatchSizeProvider, RxBleConnection rxBleConnection2, OperationsProvider operationsProvider2) {
        this.operationQueue = operationQueue2;
        this.maxBatchSizeProvider = defaultMaxBatchSizeProvider;
        this.rxBleConnection = rxBleConnection2;
        this.operationsProvider = operationsProvider2;
    }

    public LongWriteOperationBuilder setBytes(@NonNull byte[] bytes2) {
        this.bytes = bytes2;
        return this;
    }

    public LongWriteOperationBuilder setCharacteristicUuid(@NonNull UUID uuid) {
        this.writtenCharacteristicObservable = this.rxBleConnection.getCharacteristic(uuid);
        return this;
    }

    public LongWriteOperationBuilder setCharacteristic(@NonNull BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.writtenCharacteristicObservable = Single.just(bluetoothGattCharacteristic);
        return this;
    }

    public LongWriteOperationBuilder setMaxBatchSize(int maxBatchSize) {
        this.maxBatchSizeProvider = new ConstantPayloadSizeLimit(maxBatchSize);
        return this;
    }

    public LongWriteOperationBuilder setWriteOperationRetryStrategy(@NonNull WriteOperationRetryStrategy writeOperationRetryStrategy2) {
        this.writeOperationRetryStrategy = writeOperationRetryStrategy2;
        return this;
    }

    public LongWriteOperationBuilder setWriteOperationAckStrategy(@NonNull WriteOperationAckStrategy writeOperationAckStrategy2) {
        this.writeOperationAckStrategy = writeOperationAckStrategy2;
        return this;
    }

    public Observable<byte[]> build() {
        if (this.writtenCharacteristicObservable == null) {
            throw new IllegalArgumentException("setCharacteristicUuid() or setCharacteristic() needs to be called before build()");
        } else if (this.bytes != null) {
            return this.writtenCharacteristicObservable.flatMapObservable(new Function<BluetoothGattCharacteristic, Observable<byte[]>>() {
                public Observable<byte[]> apply(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
                    return LongWriteOperationBuilderImpl.this.operationQueue.queue(LongWriteOperationBuilderImpl.this.operationsProvider.provideLongWriteOperation(bluetoothGattCharacteristic, LongWriteOperationBuilderImpl.this.writeOperationAckStrategy, LongWriteOperationBuilderImpl.this.writeOperationRetryStrategy, LongWriteOperationBuilderImpl.this.maxBatchSizeProvider, LongWriteOperationBuilderImpl.this.bytes));
                }
            });
        } else {
            throw new IllegalArgumentException("setBytes() needs to be called before build()");
        }
    }
}
