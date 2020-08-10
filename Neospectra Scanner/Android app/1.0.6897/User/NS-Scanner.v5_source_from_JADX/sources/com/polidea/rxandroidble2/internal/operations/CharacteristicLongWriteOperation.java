package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationAckStrategy;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattCallbackTimeoutException;
import com.polidea.rxandroidble2.exceptions.BleGattCannotStartException;
import com.polidea.rxandroidble2.exceptions.BleGattCharacteristicException;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.connection.PayloadSizeLimitProvider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.ByteAssociation;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;
import com.polidea.rxandroidble2.internal.util.QueueReleasingEmitterWrapper;
import java.nio.ByteBuffer;
import java.util.UUID;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableOnSubscribe;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.observers.DisposableObserver;

public class CharacteristicLongWriteOperation extends QueueOperation<byte[]> {
    private final PayloadSizeLimitProvider batchSizeProvider;
    private final BluetoothGatt bluetoothGatt;
    private final BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private final Scheduler bluetoothInteractionScheduler;
    /* access modifiers changed from: private */
    public final byte[] bytesToWrite;
    private final RxBleGattCallback rxBleGattCallback;
    private byte[] tempBatchArray;
    private final TimeoutConfiguration timeoutConfiguration;
    private final WriteOperationAckStrategy writeOperationAckStrategy;
    private final WriteOperationRetryStrategy writeOperationRetryStrategy;

    CharacteristicLongWriteOperation(BluetoothGatt bluetoothGatt2, RxBleGattCallback rxBleGattCallback2, @Named("bluetooth_interaction") Scheduler bluetoothInteractionScheduler2, @Named("operation-timeout") TimeoutConfiguration timeoutConfiguration2, BluetoothGattCharacteristic bluetoothGattCharacteristic2, PayloadSizeLimitProvider batchSizeProvider2, WriteOperationAckStrategy writeOperationAckStrategy2, WriteOperationRetryStrategy writeOperationRetryStrategy2, byte[] bytesToWrite2) {
        this.bluetoothGatt = bluetoothGatt2;
        this.rxBleGattCallback = rxBleGattCallback2;
        this.bluetoothInteractionScheduler = bluetoothInteractionScheduler2;
        this.timeoutConfiguration = timeoutConfiguration2;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic2;
        this.batchSizeProvider = batchSizeProvider2;
        this.writeOperationAckStrategy = writeOperationAckStrategy2;
        this.writeOperationRetryStrategy = writeOperationRetryStrategy2;
        this.bytesToWrite = bytesToWrite2;
    }

    /* access modifiers changed from: protected */
    public void protectedRun(ObservableEmitter<byte[]> emitter, QueueReleaseInterface queueReleaseInterface) throws Throwable {
        int batchSize = this.batchSizeProvider.getPayloadSizeLimit();
        if (batchSize <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("batchSizeProvider value must be greater than zero (now: ");
            sb.append(batchSize);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        Observable<ByteAssociation<UUID>> timeoutObservable = Observable.error((Throwable) new BleGattCallbackTimeoutException(this.bluetoothGatt, BleGattOperationType.CHARACTERISTIC_LONG_WRITE));
        ByteBuffer byteBuffer = ByteBuffer.wrap(this.bytesToWrite);
        final QueueReleasingEmitterWrapper<byte[]> emitterWrapper = new QueueReleasingEmitterWrapper<>(emitter, queueReleaseInterface);
        writeBatchAndObserve(batchSize, byteBuffer).subscribeOn(this.bluetoothInteractionScheduler).filter(writeResponseForMatchingCharacteristic(this.bluetoothGattCharacteristic)).take(1).timeout(this.timeoutConfiguration.timeout, this.timeoutConfiguration.timeoutTimeUnit, this.timeoutConfiguration.timeoutScheduler, timeoutObservable).repeatWhen(m27x3cbe3949(this.writeOperationAckStrategy, byteBuffer, emitterWrapper)).retryWhen(errorIsRetryableAndAccordingTo(this.writeOperationRetryStrategy, byteBuffer, batchSize)).subscribe((Observer<? super T>) new Observer<ByteAssociation<UUID>>() {
            public void onSubscribe(Disposable d) {
            }

            public void onNext(ByteAssociation<UUID> byteAssociation) {
            }

            public void onError(Throwable e) {
                emitterWrapper.onError(e);
            }

            public void onComplete() {
                emitterWrapper.onNext(CharacteristicLongWriteOperation.this.bytesToWrite);
                emitterWrapper.onComplete();
            }
        });
    }

    /* access modifiers changed from: protected */
    public BleException provideException(DeadObjectException deadObjectException) {
        return new BleDisconnectedException(deadObjectException, this.bluetoothGatt.getDevice().getAddress());
    }

    @NonNull
    private Observable<ByteAssociation<UUID>> writeBatchAndObserve(final int batchSize, final ByteBuffer byteBuffer) {
        final Observable<ByteAssociation<UUID>> onCharacteristicWrite = this.rxBleGattCallback.getOnCharacteristicWrite();
        return Observable.create(new ObservableOnSubscribe<ByteAssociation<UUID>>() {
            public void subscribe(ObservableEmitter<ByteAssociation<UUID>> emitter) throws Exception {
                emitter.setDisposable((DisposableObserver) onCharacteristicWrite.subscribeWith(DisposableUtil.disposableObserverFromEmitter(emitter)));
                try {
                    CharacteristicLongWriteOperation.this.writeData(CharacteristicLongWriteOperation.this.getNextBatch(byteBuffer, batchSize));
                } catch (Throwable throwable) {
                    emitter.onError(throwable);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public byte[] getNextBatch(ByteBuffer byteBuffer, int batchSize) {
        int nextBatchSize = Math.min(byteBuffer.remaining(), batchSize);
        if (this.tempBatchArray == null || this.tempBatchArray.length != nextBatchSize) {
            this.tempBatchArray = new byte[nextBatchSize];
        }
        byteBuffer.get(this.tempBatchArray);
        return this.tempBatchArray;
    }

    /* access modifiers changed from: private */
    public void writeData(byte[] bytesBatch) {
        this.bluetoothGattCharacteristic.setValue(bytesBatch);
        if (!this.bluetoothGatt.writeCharacteristic(this.bluetoothGattCharacteristic)) {
            throw new BleGattCannotStartException(this.bluetoothGatt, BleGattOperationType.CHARACTERISTIC_LONG_WRITE);
        }
    }

    private static Predicate<ByteAssociation<UUID>> writeResponseForMatchingCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic2) {
        return new Predicate<ByteAssociation<UUID>>() {
            public boolean test(ByteAssociation<UUID> uuidByteAssociation) {
                return ((UUID) uuidByteAssociation.first).equals(bluetoothGattCharacteristic2.getUuid());
            }
        };
    }

    /* renamed from: bufferIsNotEmptyAndOperationHasBeenAcknowledgedAndNotUnsubscribed */
    private static Function<Observable<?>, ObservableSource<?>> m27x3cbe3949(final WriteOperationAckStrategy writeOperationAckStrategy2, final ByteBuffer byteBuffer, final QueueReleasingEmitterWrapper<byte[]> emitterWrapper) {
        return new Function<Observable<?>, ObservableSource<?>>() {
            public ObservableSource<?> apply(Observable<?> emittingOnBatchWriteFinished) throws Exception {
                return emittingOnBatchWriteFinished.takeWhile(notUnsubscribed(emitterWrapper)).map(bufferIsNotEmpty(byteBuffer)).compose(writeOperationAckStrategy2).takeUntil((Predicate<? super T>) new Predicate<Boolean>() {
                    public boolean test(Boolean hasRemaining) throws Exception {
                        return !hasRemaining.booleanValue();
                    }
                });
            }

            @NonNull
            private Function<Object, Boolean> bufferIsNotEmpty(final ByteBuffer byteBuffer) {
                return new Function<Object, Boolean>() {
                    public Boolean apply(Object emittedFromActStrategy) throws Exception {
                        return Boolean.valueOf(byteBuffer.hasRemaining());
                    }
                };
            }

            @NonNull
            private Predicate<Object> notUnsubscribed(final QueueReleasingEmitterWrapper<byte[]> emitterWrapper) {
                return new Predicate<Object>() {
                    public boolean test(Object emission) {
                        return !emitterWrapper.isWrappedEmitterUnsubscribed();
                    }
                };
            }
        };
    }

    private static Function<Observable<Throwable>, ObservableSource<?>> errorIsRetryableAndAccordingTo(final WriteOperationRetryStrategy writeOperationRetryStrategy2, final ByteBuffer byteBuffer, final int batchSize) {
        return new Function<Observable<Throwable>, ObservableSource<?>>() {
            public ObservableSource<?> apply(Observable<Throwable> emittedOnWriteFailure) {
                return emittedOnWriteFailure.flatMap(toLongWriteFailureOrError()).doOnNext(repositionByteBufferForRetry()).compose(writeOperationRetryStrategy2);
            }

            @NonNull
            private Function<Throwable, Observable<LongWriteFailure>> toLongWriteFailureOrError() {
                return new Function<Throwable, Observable<LongWriteFailure>>() {
                    public Observable<LongWriteFailure> apply(Throwable throwable) {
                        if ((throwable instanceof BleGattCharacteristicException) || (throwable instanceof BleGattCannotStartException)) {
                            return Observable.just(new LongWriteFailure(C07785.this.calculateFailedBatchIndex(byteBuffer, batchSize), (BleGattException) throwable));
                        }
                        return Observable.error(throwable);
                    }
                };
            }

            @NonNull
            private Consumer<LongWriteFailure> repositionByteBufferForRetry() {
                return new Consumer<LongWriteFailure>() {
                    public void accept(LongWriteFailure longWriteFailure) {
                        byteBuffer.position(longWriteFailure.getBatchIndex() * batchSize);
                    }
                };
            }

            /* access modifiers changed from: private */
            public int calculateFailedBatchIndex(ByteBuffer byteBuffer, int batchSize) {
                return ((int) Math.ceil((double) (((float) byteBuffer.position()) / ((float) batchSize)))) - 1;
            }
        };
    }
}
