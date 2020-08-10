package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy;
import com.polidea.rxandroidble2.RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure;
import p005io.reactivex.Observable;
import p005io.reactivex.functions.Function;

public class NoRetryStrategy implements WriteOperationRetryStrategy {
    public Observable<LongWriteFailure> apply(Observable<LongWriteFailure> observable) {
        return observable.flatMap(new Function<LongWriteFailure, Observable<LongWriteFailure>>() {
            public Observable<LongWriteFailure> apply(LongWriteFailure longWriteFailure) {
                return Observable.error((Throwable) longWriteFailure.getCause());
            }
        });
    }
}
