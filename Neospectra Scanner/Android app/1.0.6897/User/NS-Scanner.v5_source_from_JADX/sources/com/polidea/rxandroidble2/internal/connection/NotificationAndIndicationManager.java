package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.NotificationSetupMode;
import com.polidea.rxandroidble2.exceptions.BleCannotSetCharacteristicNotificationException;
import com.polidea.rxandroidble2.exceptions.BleConflictingNotificationAlreadySetException;
import com.polidea.rxandroidble2.internal.util.ActiveCharacteristicNotification;
import com.polidea.rxandroidble2.internal.util.CharacteristicChangedEvent;
import com.polidea.rxandroidble2.internal.util.CharacteristicNotificationId;
import com.polidea.rxandroidble2.internal.util.ObservableUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.CompletableTransformer;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.subjects.PublishSubject;

@ConnectionScope
class NotificationAndIndicationManager {
    static final UUID CLIENT_CHARACTERISTIC_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    /* access modifiers changed from: private */
    public final Map<CharacteristicNotificationId, ActiveCharacteristicNotification> activeNotificationObservableMap = new HashMap();
    /* access modifiers changed from: private */
    public final BluetoothGatt bluetoothGatt;
    /* access modifiers changed from: private */
    public final byte[] configDisable;
    /* access modifiers changed from: private */
    public final byte[] configEnableIndication;
    /* access modifiers changed from: private */
    public final byte[] configEnableNotification;
    /* access modifiers changed from: private */
    public final DescriptorWriter descriptorWriter;
    /* access modifiers changed from: private */
    public final RxBleGattCallback gattCallback;

    @Inject
    NotificationAndIndicationManager(@Named("enable-notification-value") byte[] configEnableNotification2, @Named("enable-indication-value") byte[] configEnableIndication2, @Named("disable-notification-value") byte[] configDisable2, BluetoothGatt bluetoothGatt2, RxBleGattCallback gattCallback2, DescriptorWriter descriptorWriter2) {
        this.configEnableNotification = configEnableNotification2;
        this.configEnableIndication = configEnableIndication2;
        this.configDisable = configDisable2;
        this.bluetoothGatt = bluetoothGatt2;
        this.gattCallback = gattCallback2;
        this.descriptorWriter = descriptorWriter2;
    }

    /* access modifiers changed from: 0000 */
    public Observable<Observable<byte[]>> setupServerInitiatedCharacteristicRead(@NonNull final BluetoothGattCharacteristic characteristic, final NotificationSetupMode setupMode, final boolean isIndication) {
        return Observable.defer(new Callable<ObservableSource<Observable<byte[]>>>() {
            public ObservableSource<Observable<byte[]>> call() throws Exception {
                synchronized (NotificationAndIndicationManager.this.activeNotificationObservableMap) {
                    final CharacteristicNotificationId id = new CharacteristicNotificationId(characteristic.getUuid(), Integer.valueOf(characteristic.getInstanceId()));
                    ActiveCharacteristicNotification activeCharacteristicNotification = (ActiveCharacteristicNotification) NotificationAndIndicationManager.this.activeNotificationObservableMap.get(id);
                    if (activeCharacteristicNotification == null) {
                        byte[] enableNotificationTypeValue = isIndication ? NotificationAndIndicationManager.this.configEnableIndication : NotificationAndIndicationManager.this.configEnableNotification;
                        final PublishSubject<?> notificationCompletedSubject = PublishSubject.create();
                        Observable<Observable<byte[]>> newObservable = NotificationAndIndicationManager.setCharacteristicNotification(NotificationAndIndicationManager.this.bluetoothGatt, characteristic, true).compose(NotificationAndIndicationManager.setupModeTransformer(NotificationAndIndicationManager.this.descriptorWriter, characteristic, enableNotificationTypeValue, setupMode)).andThen((ObservableSource<T>) ObservableUtil.justOnNext(NotificationAndIndicationManager.observeOnCharacteristicChangeCallbacks(NotificationAndIndicationManager.this.gattCallback, id).takeUntil((ObservableSource<U>) notificationCompletedSubject))).doFinally(new Action() {
                            public void run() {
                                notificationCompletedSubject.onComplete();
                                synchronized (NotificationAndIndicationManager.this.activeNotificationObservableMap) {
                                    NotificationAndIndicationManager.this.activeNotificationObservableMap.remove(id);
                                }
                                NotificationAndIndicationManager.setCharacteristicNotification(NotificationAndIndicationManager.this.bluetoothGatt, characteristic, false).compose(NotificationAndIndicationManager.setupModeTransformer(NotificationAndIndicationManager.this.descriptorWriter, characteristic, NotificationAndIndicationManager.this.configDisable, setupMode)).subscribe(Functions.EMPTY_ACTION, Functions.emptyConsumer());
                            }
                        }).mergeWith((ObservableSource<? extends T>) NotificationAndIndicationManager.this.gattCallback.observeDisconnect()).replay(1).refCount();
                        NotificationAndIndicationManager.this.activeNotificationObservableMap.put(id, new ActiveCharacteristicNotification(newObservable, isIndication));
                        return newObservable;
                    } else if (activeCharacteristicNotification.isIndication == isIndication) {
                        Observable<Observable<byte[]>> observable = activeCharacteristicNotification.notificationObservable;
                        return observable;
                    } else {
                        Observable error = Observable.error((Throwable) new BleConflictingNotificationAlreadySetException(characteristic.getUuid(), true ^ isIndication));
                        return error;
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    @NonNull
    public static Completable setCharacteristicNotification(final BluetoothGatt bluetoothGatt2, final BluetoothGattCharacteristic characteristic, final boolean isNotificationEnabled) {
        return Completable.fromAction(new Action() {
            public void run() {
                if (!bluetoothGatt2.setCharacteristicNotification(characteristic, isNotificationEnabled)) {
                    throw new BleCannotSetCharacteristicNotificationException(characteristic, 1, null);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    @NonNull
    public static CompletableTransformer setupModeTransformer(final DescriptorWriter descriptorWriter2, final BluetoothGattCharacteristic characteristic, final byte[] value, final NotificationSetupMode mode) {
        return new CompletableTransformer() {
            public Completable apply(Completable completable) {
                if (mode == NotificationSetupMode.DEFAULT) {
                    return completable.andThen((CompletableSource) NotificationAndIndicationManager.writeClientCharacteristicConfig(characteristic, descriptorWriter2, value));
                }
                return completable;
            }
        };
    }

    /* access modifiers changed from: private */
    @NonNull
    public static Observable<byte[]> observeOnCharacteristicChangeCallbacks(RxBleGattCallback gattCallback2, final CharacteristicNotificationId characteristicId) {
        return gattCallback2.getOnCharacteristicChanged().filter(new Predicate<CharacteristicChangedEvent>() {
            public boolean test(CharacteristicChangedEvent notificationIdWithData) {
                return notificationIdWithData.equals(characteristicId);
            }
        }).map(new Function<CharacteristicChangedEvent, byte[]>() {
            public byte[] apply(CharacteristicChangedEvent notificationIdWithData) {
                return notificationIdWithData.data;
            }
        });
    }

    /* access modifiers changed from: private */
    @NonNull
    public static Completable writeClientCharacteristicConfig(final BluetoothGattCharacteristic bluetoothGattCharacteristic, DescriptorWriter descriptorWriter2, byte[] value) {
        BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID);
        if (descriptor == null) {
            return Completable.error((Throwable) new BleCannotSetCharacteristicNotificationException(bluetoothGattCharacteristic, 2, null));
        }
        return descriptorWriter2.writeDescriptor(descriptor, value).onErrorResumeNext(new Function<Throwable, CompletableSource>() {
            public CompletableSource apply(Throwable throwable) throws Exception {
                return Completable.error((Throwable) new BleCannotSetCharacteristicNotificationException(bluetoothGattCharacteristic, 3, throwable));
            }
        });
    }
}
