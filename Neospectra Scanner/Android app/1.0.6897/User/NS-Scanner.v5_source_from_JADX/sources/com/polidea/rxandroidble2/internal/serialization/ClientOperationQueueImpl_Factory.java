package com.polidea.rxandroidble2.internal.serialization;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import p005io.reactivex.Scheduler;

public final class ClientOperationQueueImpl_Factory implements Factory<ClientOperationQueueImpl> {
    private final Provider<Scheduler> callbackSchedulerProvider;

    public ClientOperationQueueImpl_Factory(Provider<Scheduler> callbackSchedulerProvider2) {
        this.callbackSchedulerProvider = callbackSchedulerProvider2;
    }

    public ClientOperationQueueImpl get() {
        return new ClientOperationQueueImpl((Scheduler) this.callbackSchedulerProvider.get());
    }

    public static ClientOperationQueueImpl_Factory create(Provider<Scheduler> callbackSchedulerProvider2) {
        return new ClientOperationQueueImpl_Factory(callbackSchedulerProvider2);
    }
}
