package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.DisconnectOperation;
import com.polidea.rxandroidble2.internal.serialization.ClientOperationQueue;

public final class DisconnectAction_Factory implements Factory<DisconnectAction> {
    private final Provider<ClientOperationQueue> clientOperationQueueProvider;
    private final Provider<DisconnectOperation> operationDisconnectProvider;

    public DisconnectAction_Factory(Provider<ClientOperationQueue> clientOperationQueueProvider2, Provider<DisconnectOperation> operationDisconnectProvider2) {
        this.clientOperationQueueProvider = clientOperationQueueProvider2;
        this.operationDisconnectProvider = operationDisconnectProvider2;
    }

    public DisconnectAction get() {
        return new DisconnectAction((ClientOperationQueue) this.clientOperationQueueProvider.get(), (DisconnectOperation) this.operationDisconnectProvider.get());
    }

    public static DisconnectAction_Factory create(Provider<ClientOperationQueue> clientOperationQueueProvider2, Provider<DisconnectOperation> operationDisconnectProvider2) {
        return new DisconnectAction_Factory(clientOperationQueueProvider2, operationDisconnectProvider2);
    }

    public static DisconnectAction newDisconnectAction(ClientOperationQueue clientOperationQueue, DisconnectOperation operationDisconnect) {
        return new DisconnectAction(clientOperationQueue, operationDisconnect);
    }
}
