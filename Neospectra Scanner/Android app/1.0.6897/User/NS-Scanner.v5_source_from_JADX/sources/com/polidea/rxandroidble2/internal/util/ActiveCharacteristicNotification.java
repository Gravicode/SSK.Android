package com.polidea.rxandroidble2.internal.util;

import p005io.reactivex.Observable;

public class ActiveCharacteristicNotification {
    public final boolean isIndication;
    public final Observable<Observable<byte[]>> notificationObservable;

    public ActiveCharacteristicNotification(Observable<Observable<byte[]>> notificationObservable2, boolean isIndication2) {
        this.notificationObservable = notificationObservable2;
        this.isIndication = isIndication2;
    }
}
