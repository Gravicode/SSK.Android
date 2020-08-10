package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.NotificationSetupMode;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleConnection.LongWriteOperationBuilder;
import com.polidea.rxandroidble2.RxBleCustomOperation;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.Priority;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.operations.OperationsProvider;
import com.polidea.rxandroidble2.internal.serialization.ConnectionOperationQueue;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.ByteAssociation;
import com.polidea.rxandroidble2.internal.util.QueueReleasingEmitterWrapper;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Single;
import p005io.reactivex.SingleSource;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Function;

@ConnectionScope
public class RxBleConnectionImpl implements RxBleConnection {
    /* access modifiers changed from: private */
    public final BluetoothGatt bluetoothGatt;
    /* access modifiers changed from: private */
    public final Scheduler callbackScheduler;
    private final DescriptorWriter descriptorWriter;
    /* access modifiers changed from: private */
    public final RxBleGattCallback gattCallback;
    private final IllegalOperationChecker illegalOperationChecker;
    private final Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider;
    private final MtuProvider mtuProvider;
    private final NotificationAndIndicationManager notificationIndicationManager;
    private final ConnectionOperationQueue operationQueue;
    private final OperationsProvider operationsProvider;
    private final ServiceDiscoveryManager serviceDiscoveryManager;

    @Inject
    public RxBleConnectionImpl(ConnectionOperationQueue operationQueue2, RxBleGattCallback gattCallback2, BluetoothGatt bluetoothGatt2, ServiceDiscoveryManager serviceDiscoveryManager2, NotificationAndIndicationManager notificationIndicationManager2, MtuProvider mtuProvider2, DescriptorWriter descriptorWriter2, OperationsProvider operationProvider, Provider<LongWriteOperationBuilder> longWriteOperationBuilderProvider2, @Named("bluetooth_interaction") Scheduler callbackScheduler2, IllegalOperationChecker illegalOperationChecker2) {
        this.operationQueue = operationQueue2;
        this.gattCallback = gattCallback2;
        this.bluetoothGatt = bluetoothGatt2;
        this.serviceDiscoveryManager = serviceDiscoveryManager2;
        this.notificationIndicationManager = notificationIndicationManager2;
        this.mtuProvider = mtuProvider2;
        this.descriptorWriter = descriptorWriter2;
        this.operationsProvider = operationProvider;
        this.longWriteOperationBuilderProvider = longWriteOperationBuilderProvider2;
        this.callbackScheduler = callbackScheduler2;
        this.illegalOperationChecker = illegalOperationChecker2;
    }

    public LongWriteOperationBuilder createNewLongWriteBuilder() {
        return (LongWriteOperationBuilder) this.longWriteOperationBuilderProvider.get();
    }

    @RequiresApi(api = 21)
    public Completable requestConnectionPriority(int connectionPriority, long delay, @NonNull TimeUnit timeUnit) {
        if (connectionPriority != 2 && connectionPriority != 0 && connectionPriority != 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("Connection priority must have valid value from BluetoothGatt (received ");
            sb.append(connectionPriority);
            sb.append(")");
            return Completable.error((Throwable) new IllegalArgumentException(sb.toString()));
        } else if (delay <= 0) {
            return Completable.error((Throwable) new IllegalArgumentException("Delay must be bigger than 0"));
        } else {
            return this.operationQueue.queue(this.operationsProvider.provideConnectionPriorityChangeOperation(connectionPriority, delay, timeUnit)).ignoreElements();
        }
    }

    @RequiresApi(api = 21)
    public Single<Integer> requestMtu(int mtu) {
        return this.operationQueue.queue(this.operationsProvider.provideMtuChangeOperation(mtu)).firstOrError();
    }

    public int getMtu() {
        return this.mtuProvider.getMtu();
    }

    public Single<RxBleDeviceServices> discoverServices() {
        return this.serviceDiscoveryManager.getDiscoverServicesSingle(20, TimeUnit.SECONDS);
    }

    public Single<RxBleDeviceServices> discoverServices(long timeout, @NonNull TimeUnit timeUnit) {
        return this.serviceDiscoveryManager.getDiscoverServicesSingle(timeout, timeUnit);
    }

    @Deprecated
    public Single<BluetoothGattCharacteristic> getCharacteristic(@NonNull final UUID characteristicUuid) {
        return discoverServices().flatMap(new Function<RxBleDeviceServices, Single<? extends BluetoothGattCharacteristic>>() {
            public Single<? extends BluetoothGattCharacteristic> apply(RxBleDeviceServices rxBleDeviceServices) {
                return rxBleDeviceServices.getCharacteristic(characteristicUuid);
            }
        });
    }

    public Observable<Observable<byte[]>> setupNotification(@NonNull UUID characteristicUuid) {
        return setupNotification(characteristicUuid, NotificationSetupMode.DEFAULT);
    }

    public Observable<Observable<byte[]>> setupNotification(@NonNull BluetoothGattCharacteristic characteristic) {
        return setupNotification(characteristic, NotificationSetupMode.DEFAULT);
    }

    public Observable<Observable<byte[]>> setupNotification(@NonNull UUID characteristicUuid, @NonNull final NotificationSetupMode setupMode) {
        return getCharacteristic(characteristicUuid).flatMapObservable(new Function<BluetoothGattCharacteristic, ObservableSource<? extends Observable<byte[]>>>() {
            public Observable<? extends Observable<byte[]>> apply(BluetoothGattCharacteristic characteristic) {
                return RxBleConnectionImpl.this.setupNotification(characteristic, setupMode);
            }
        });
    }

    public Observable<Observable<byte[]>> setupNotification(@NonNull BluetoothGattCharacteristic characteristic, @NonNull NotificationSetupMode setupMode) {
        return this.illegalOperationChecker.checkAnyPropertyMatches(characteristic, 16).andThen((ObservableSource<T>) this.notificationIndicationManager.setupServerInitiatedCharacteristicRead(characteristic, setupMode, false));
    }

    public Observable<Observable<byte[]>> setupIndication(@NonNull UUID characteristicUuid) {
        return setupIndication(characteristicUuid, NotificationSetupMode.DEFAULT);
    }

    public Observable<Observable<byte[]>> setupIndication(@NonNull BluetoothGattCharacteristic characteristic) {
        return setupIndication(characteristic, NotificationSetupMode.DEFAULT);
    }

    public Observable<Observable<byte[]>> setupIndication(@NonNull UUID characteristicUuid, @NonNull final NotificationSetupMode setupMode) {
        return getCharacteristic(characteristicUuid).flatMapObservable(new Function<BluetoothGattCharacteristic, ObservableSource<? extends Observable<byte[]>>>() {
            public Observable<? extends Observable<byte[]>> apply(BluetoothGattCharacteristic characteristic) {
                return RxBleConnectionImpl.this.setupIndication(characteristic, setupMode);
            }
        });
    }

    public Observable<Observable<byte[]>> setupIndication(@NonNull BluetoothGattCharacteristic characteristic, @NonNull NotificationSetupMode setupMode) {
        return this.illegalOperationChecker.checkAnyPropertyMatches(characteristic, 32).andThen((ObservableSource<T>) this.notificationIndicationManager.setupServerInitiatedCharacteristicRead(characteristic, setupMode, true));
    }

    public Single<byte[]> readCharacteristic(@NonNull UUID characteristicUuid) {
        return getCharacteristic(characteristicUuid).flatMap(new Function<BluetoothGattCharacteristic, SingleSource<? extends byte[]>>() {
            public SingleSource<? extends byte[]> apply(BluetoothGattCharacteristic characteristic) {
                return RxBleConnectionImpl.this.readCharacteristic(characteristic);
            }
        });
    }

    public Single<byte[]> readCharacteristic(@NonNull BluetoothGattCharacteristic characteristic) {
        return this.illegalOperationChecker.checkAnyPropertyMatches(characteristic, 2).andThen((ObservableSource<T>) this.operationQueue.queue(this.operationsProvider.provideReadCharacteristic(characteristic))).firstOrError();
    }

    public Single<byte[]> writeCharacteristic(@NonNull UUID characteristicUuid, @NonNull final byte[] data) {
        return getCharacteristic(characteristicUuid).flatMap(new Function<BluetoothGattCharacteristic, SingleSource<? extends byte[]>>() {
            public SingleSource<? extends byte[]> apply(BluetoothGattCharacteristic characteristic) {
                return RxBleConnectionImpl.this.writeCharacteristic(characteristic, data);
            }
        });
    }

    public Single<byte[]> writeCharacteristic(@NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] data) {
        return this.illegalOperationChecker.checkAnyPropertyMatches(characteristic, 76).andThen((ObservableSource<T>) this.operationQueue.queue(this.operationsProvider.provideWriteCharacteristic(characteristic, data))).firstOrError();
    }

    public Single<byte[]> readDescriptor(@NonNull final UUID serviceUuid, @NonNull final UUID characteristicUuid, @NonNull final UUID descriptorUuid) {
        return discoverServices().flatMap(new Function<RxBleDeviceServices, SingleSource<BluetoothGattDescriptor>>() {
            public SingleSource<BluetoothGattDescriptor> apply(RxBleDeviceServices rxBleDeviceServices) {
                return rxBleDeviceServices.getDescriptor(serviceUuid, characteristicUuid, descriptorUuid);
            }
        }).flatMap(new Function<BluetoothGattDescriptor, SingleSource<byte[]>>() {
            public SingleSource<byte[]> apply(BluetoothGattDescriptor descriptor) {
                return RxBleConnectionImpl.this.readDescriptor(descriptor);
            }
        });
    }

    public Single<byte[]> readDescriptor(@NonNull BluetoothGattDescriptor descriptor) {
        return this.operationQueue.queue(this.operationsProvider.provideReadDescriptor(descriptor)).firstOrError().map(new Function<ByteAssociation<BluetoothGattDescriptor>, byte[]>() {
            public byte[] apply(ByteAssociation<BluetoothGattDescriptor> bluetoothGattDescriptorPair) {
                return bluetoothGattDescriptorPair.second;
            }
        });
    }

    public Completable writeDescriptor(@NonNull final UUID serviceUuid, @NonNull final UUID characteristicUuid, @NonNull final UUID descriptorUuid, @NonNull final byte[] data) {
        return discoverServices().flatMap(new Function<RxBleDeviceServices, SingleSource<BluetoothGattDescriptor>>() {
            public SingleSource<BluetoothGattDescriptor> apply(RxBleDeviceServices rxBleDeviceServices) {
                return rxBleDeviceServices.getDescriptor(serviceUuid, characteristicUuid, descriptorUuid);
            }
        }).flatMapCompletable(new Function<BluetoothGattDescriptor, CompletableSource>() {
            public CompletableSource apply(BluetoothGattDescriptor bluetoothGattDescriptor) {
                return RxBleConnectionImpl.this.writeDescriptor(bluetoothGattDescriptor, data);
            }
        });
    }

    public Completable writeDescriptor(@NonNull BluetoothGattDescriptor bluetoothGattDescriptor, @NonNull byte[] data) {
        return this.descriptorWriter.writeDescriptor(bluetoothGattDescriptor, data);
    }

    public Single<Integer> readRssi() {
        return this.operationQueue.queue(this.operationsProvider.provideRssiReadOperation()).firstOrError();
    }

    public <T> Observable<T> queue(@NonNull RxBleCustomOperation<T> operation) {
        return queue(operation, Priority.NORMAL);
    }

    public <T> Observable<T> queue(@NonNull final RxBleCustomOperation<T> operation, @NonNull final Priority priority) {
        return this.operationQueue.queue(new QueueOperation<T>() {
            /* JADX INFO: finally extract failed */
            /* access modifiers changed from: protected */
            public void protectedRun(ObservableEmitter<T> emitter, QueueReleaseInterface queueReleaseInterface) throws Throwable {
                try {
                    Observable<T> operationObservable = operation.asObservable(RxBleConnectionImpl.this.bluetoothGatt, RxBleConnectionImpl.this.gattCallback, RxBleConnectionImpl.this.callbackScheduler);
                    if (operationObservable == null) {
                        queueReleaseInterface.release();
                        throw new IllegalArgumentException("The custom operation asObservable method must return a non-null observable");
                    }
                    operationObservable.doOnTerminate(clearNativeCallbackReferenceAction()).subscribe((Observer<? super T>) new QueueReleasingEmitterWrapper<T>(emitter, queueReleaseInterface));
                } catch (Throwable throwable) {
                    queueReleaseInterface.release();
                    throw throwable;
                }
            }

            private Action clearNativeCallbackReferenceAction() {
                return new Action() {
                    public void run() {
                        RxBleConnectionImpl.this.gattCallback.setNativeCallback(null);
                    }
                };
            }

            /* access modifiers changed from: protected */
            public BleException provideException(DeadObjectException deadObjectException) {
                return new BleDisconnectedException(deadObjectException, RxBleConnectionImpl.this.bluetoothGatt.getDevice().getAddress());
            }

            public Priority definedPriority() {
                return priority;
            }
        });
    }
}
