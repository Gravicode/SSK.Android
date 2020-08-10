package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import p005io.reactivex.Scheduler;

public final class DeviceModule_ProvidesDisconnectTimeoutConfFactory implements Factory<TimeoutConfiguration> {
    private final Provider<Scheduler> timeoutSchedulerProvider;

    public DeviceModule_ProvidesDisconnectTimeoutConfFactory(Provider<Scheduler> timeoutSchedulerProvider2) {
        this.timeoutSchedulerProvider = timeoutSchedulerProvider2;
    }

    public TimeoutConfiguration get() {
        return (TimeoutConfiguration) Preconditions.checkNotNull(DeviceModule.providesDisconnectTimeoutConf((Scheduler) this.timeoutSchedulerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvidesDisconnectTimeoutConfFactory create(Provider<Scheduler> timeoutSchedulerProvider2) {
        return new DeviceModule_ProvidesDisconnectTimeoutConfFactory(timeoutSchedulerProvider2);
    }

    public static TimeoutConfiguration proxyProvidesDisconnectTimeoutConf(Scheduler timeoutScheduler) {
        return (TimeoutConfiguration) Preconditions.checkNotNull(DeviceModule.providesDisconnectTimeoutConf(timeoutScheduler), "Cannot return null from a non-@Nullable @Provides method");
    }
}
