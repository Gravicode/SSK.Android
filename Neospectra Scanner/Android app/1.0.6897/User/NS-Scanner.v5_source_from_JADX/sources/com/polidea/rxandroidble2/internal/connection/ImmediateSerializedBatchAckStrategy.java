package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.RxBleConnection.WriteOperationAckStrategy;
import p005io.reactivex.Observable;

public class ImmediateSerializedBatchAckStrategy implements WriteOperationAckStrategy {
    public Observable<Boolean> apply(Observable<Boolean> objectObservable) {
        return objectObservable;
    }
}
