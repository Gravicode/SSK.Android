package com.polidea.rxandroidble2.internal.connection;

import com.polidea.rxandroidble2.exceptions.BleException;
import p005io.reactivex.Observable;

public interface DisconnectionRouterOutput {
    <T> Observable<T> asErrorOnlyObservable();

    Observable<BleException> asValueOnlyObservable();
}
