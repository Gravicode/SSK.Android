package com.polidea.rxandroidble2.internal.connection;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import p005io.reactivex.Observable;
import p005io.reactivex.disposables.SerialDisposable;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Predicate;

@ConnectionScope
class MtuWatcher implements ConnectionSubscriptionWatcher, MtuProvider, Consumer<Integer> {
    private Integer currentMtu;
    private final Observable<Integer> mtuObservable;
    private final SerialDisposable serialSubscription = new SerialDisposable();

    @Inject
    MtuWatcher(RxBleGattCallback rxBleGattCallback, @Named("GATT_MTU_MINIMUM") int initialValue) {
        this.mtuObservable = rxBleGattCallback.getOnMtuChanged().retry((Predicate<? super Throwable>) new Predicate<Throwable>() {
            public boolean test(Throwable throwable) {
                return (throwable instanceof BleGattException) && ((BleGattException) throwable).getBleGattOperationType() == BleGattOperationType.ON_MTU_CHANGED;
            }
        });
        this.currentMtu = Integer.valueOf(initialValue);
    }

    public int getMtu() {
        return this.currentMtu.intValue();
    }

    public void onConnectionSubscribed() {
        this.serialSubscription.set(this.mtuObservable.subscribe(this, new Consumer<Throwable>() {
            public void accept(Throwable throwable) {
            }
        }));
    }

    public void onConnectionUnsubscribed() {
        this.serialSubscription.dispose();
    }

    public void accept(Integer newMtu) {
        this.currentMtu = newMtu;
    }
}
