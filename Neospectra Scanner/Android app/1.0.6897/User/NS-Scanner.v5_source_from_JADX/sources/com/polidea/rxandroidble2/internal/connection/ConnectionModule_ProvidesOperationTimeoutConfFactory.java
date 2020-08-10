package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import p005io.reactivex.Scheduler;

public final class ConnectionModule_ProvidesOperationTimeoutConfFactory implements Factory<TimeoutConfiguration> {
    private final ConnectionModule module;
    private final Provider<Scheduler> timeoutSchedulerProvider;

    public ConnectionModule_ProvidesOperationTimeoutConfFactory(ConnectionModule module2, Provider<Scheduler> timeoutSchedulerProvider2) {
        this.module = module2;
        this.timeoutSchedulerProvider = timeoutSchedulerProvider2;
    }

    public TimeoutConfiguration get() {
        return (TimeoutConfiguration) Preconditions.checkNotNull(this.module.providesOperationTimeoutConf((Scheduler) this.timeoutSchedulerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ConnectionModule_ProvidesOperationTimeoutConfFactory create(ConnectionModule module2, Provider<Scheduler> timeoutSchedulerProvider2) {
        return new ConnectionModule_ProvidesOperationTimeoutConfFactory(module2, timeoutSchedulerProvider2);
    }

    public static TimeoutConfiguration proxyProvidesOperationTimeoutConf(ConnectionModule instance, Scheduler timeoutScheduler) {
        return (TimeoutConfiguration) Preconditions.checkNotNull(instance.providesOperationTimeoutConf(timeoutScheduler), "Cannot return null from a non-@Nullable @Provides method");
    }
}
