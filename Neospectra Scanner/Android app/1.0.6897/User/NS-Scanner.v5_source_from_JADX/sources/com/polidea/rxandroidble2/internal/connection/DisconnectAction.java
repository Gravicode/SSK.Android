package com.polidea.rxandroidble2.internal.connection;

import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.operations.DisconnectOperation;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;
import p005io.reactivex.internal.functions.Functions;

@ConnectionScope
class DisconnectAction implements ConnectionSubscriptionWatcher {
    private final ClientOperationQueue clientOperationQueue;
    private final DisconnectOperation operationDisconnect;

    @Inject
    DisconnectAction(ClientOperationQueue clientOperationQueue2, DisconnectOperation operationDisconnect2) {
        this.clientOperationQueue = clientOperationQueue2;
        this.operationDisconnect = operationDisconnect2;
    }

    public void onConnectionSubscribed() {
    }

    public void onConnectionUnsubscribed() {
        this.clientOperationQueue.queue(this.operationDisconnect).subscribe(Functions.emptyConsumer(), Functions.emptyConsumer());
    }
}
