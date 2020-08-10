package com.polidea.rxandroidble2.utils;

import com.polidea.rxandroidble2.RxBleConnection;
import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.ObservableTransformer;
import p005io.reactivex.functions.Action;

@Deprecated
public class ConnectionSharingAdapter implements ObservableTransformer<RxBleConnection, RxBleConnection> {
    /* access modifiers changed from: private */
    public final AtomicReference<Observable<RxBleConnection>> connectionObservable = new AtomicReference<>();

    public ObservableSource<RxBleConnection> apply(Observable<RxBleConnection> upstream) {
        synchronized (this.connectionObservable) {
            Observable<RxBleConnection> rxBleConnectionObservable = (Observable) this.connectionObservable.get();
            if (rxBleConnectionObservable != null) {
                return rxBleConnectionObservable;
            }
            Observable<RxBleConnection> newConnectionObservable = upstream.doFinally(new Action() {
                public void run() {
                    ConnectionSharingAdapter.this.connectionObservable.set(null);
                }
            }).replay(1).refCount();
            this.connectionObservable.set(newConnectionObservable);
            return newConnectionObservable;
        }
    }
}
