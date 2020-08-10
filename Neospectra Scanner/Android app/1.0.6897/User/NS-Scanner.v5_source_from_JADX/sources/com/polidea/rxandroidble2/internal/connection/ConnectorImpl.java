package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGatt;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.ConnectionSetup;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.internal.connection.ConnectionComponent.Builder;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.Consumer;

public class ConnectorImpl implements Connector {
    /* access modifiers changed from: private */
    public final Scheduler callbacksScheduler;
    private final ClientOperationQueue clientOperationQueue;
    /* access modifiers changed from: private */
    public final Builder connectionComponentBuilder;

    @Inject
    public ConnectorImpl(ClientOperationQueue clientOperationQueue2, Builder connectionComponentBuilder2, @Named("bluetooth_callbacks") Scheduler callbacksScheduler2) {
        this.clientOperationQueue = clientOperationQueue2;
        this.connectionComponentBuilder = connectionComponentBuilder2;
        this.callbacksScheduler = callbacksScheduler2;
    }

    public Observable<RxBleConnection> prepareConnection(final ConnectionSetup options) {
        return Observable.defer(new Callable<ObservableSource<RxBleConnection>>() {
            public ObservableSource<RxBleConnection> call() throws Exception {
                ConnectionComponent connectionComponent = ConnectorImpl.this.connectionComponentBuilder.connectionModule(new ConnectionModule(options)).build();
                final Set<ConnectionSubscriptionWatcher> connSubWatchers = connectionComponent.connectionSubscriptionWatchers();
                return ConnectorImpl.obtainRxBleConnection(connectionComponent).delaySubscription(ConnectorImpl.this.enqueueConnectOperation(connectionComponent)).mergeWith((ObservableSource<? extends T>) ConnectorImpl.observeDisconnections(connectionComponent)).doOnSubscribe(new Consumer<Disposable>() {
                    public void accept(Disposable disposable) throws Exception {
                        for (ConnectionSubscriptionWatcher csa : connSubWatchers) {
                            csa.onConnectionSubscribed();
                        }
                    }
                }).doFinally(new Action() {
                    public void run() throws Exception {
                        for (ConnectionSubscriptionWatcher csa : connSubWatchers) {
                            csa.onConnectionUnsubscribed();
                        }
                    }
                }).subscribeOn(ConnectorImpl.this.callbacksScheduler).unsubscribeOn(ConnectorImpl.this.callbacksScheduler);
            }
        });
    }

    /* access modifiers changed from: private */
    public static Observable<RxBleConnection> obtainRxBleConnection(final ConnectionComponent connectionComponent) {
        return Observable.fromCallable(new Callable<RxBleConnection>() {
            public RxBleConnection call() throws Exception {
                return connectionComponent.rxBleConnection();
            }
        });
    }

    /* access modifiers changed from: private */
    public static Observable<RxBleConnection> observeDisconnections(ConnectionComponent connectionComponent) {
        return connectionComponent.gattCallback().observeDisconnect();
    }

    /* access modifiers changed from: private */
    public Observable<BluetoothGatt> enqueueConnectOperation(ConnectionComponent connectionComponent) {
        return this.clientOperationQueue.queue(connectionComponent.connectOperation());
    }
}
