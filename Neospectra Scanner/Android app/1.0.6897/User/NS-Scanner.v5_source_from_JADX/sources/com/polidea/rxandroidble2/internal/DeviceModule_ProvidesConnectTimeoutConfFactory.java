package com.polidea.rxandroidble2.internal;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.internal.operations.TimeoutConfiguration;
import p005io.reactivex.Scheduler;

public final class DeviceModule_ProvidesConnectTimeoutConfFactory implements Factory<TimeoutConfiguration> {
    private final Provider<Scheduler> timeoutSchedulerProvider;

    public DeviceModule_ProvidesConnectTimeoutConfFactory(Provider<Scheduler> timeoutSchedulerProvider2) {
        this.timeoutSchedulerProvider = timeoutSchedulerProvider2;
    }

    public TimeoutConfiguration get() {
        return (TimeoutConfiguration) Preconditions.checkNotNull(DeviceModule.providesConnectTimeoutConf((Scheduler) this.timeoutSchedulerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static DeviceModule_ProvidesConnectTimeoutConfFactory create(Provider<Scheduler> timeoutSchedulerProvider2) {
        return new DeviceModule_ProvidesConnectTimeoutConfFactory(timeoutSchedulerProvider2);
    }

    public static TimeoutConfiguration proxyProvidesConnectTimeoutConf(Scheduler timeoutScheduler) {
        return (TimeoutConfiguration) Preconditions.checkNotNull(DeviceModule.providesConnectTimeoutConf(timeoutScheduler), "Cannot return null from a non-@Nullable @Provides method");
    }
}
