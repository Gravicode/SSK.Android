package com.polidea.rxandroidble2.internal.util;

import p005io.reactivex.Observable;
import p005io.reactivex.ObservableTransformer;

public class ObservableUtil {
    private static final ObservableTransformer<?, ?> IDENTITY_TRANSFORMER = new ObservableTransformer<Object, Object>() {
        public Observable<Object> apply(Observable<Object> rxBleInternalScanResultObservable) {
            return rxBleInternalScanResultObservable;
        }
    };

    private ObservableUtil() {
    }

    public static <T> Observable<T> justOnNext(T onNext) {
        return Observable.never().startWith(onNext);
    }

    public static <T> ObservableTransformer<T, T> identityTransformer() {
        return IDENTITY_TRANSFORMER;
    }
}
