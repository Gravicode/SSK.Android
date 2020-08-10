package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatus;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi18;
import com.polidea.rxandroidble2.internal.util.LocationServicesStatusApi23;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideLocationServicesStatusFactory */
public final class C0700x9cd4449f implements Factory<LocationServicesStatus> {
    private final Provider<Integer> deviceSdkProvider;
    private final Provider<LocationServicesStatusApi18> locationServicesStatusApi18Provider;
    private final Provider<LocationServicesStatusApi23> locationServicesStatusApi23Provider;
    private final ClientModule module;

    public C0700x9cd4449f(ClientModule module2, Provider<Integer> deviceSdkProvider2, Provider<LocationServicesStatusApi18> locationServicesStatusApi18Provider2, Provider<LocationServicesStatusApi23> locationServicesStatusApi23Provider2) {
        this.module = module2;
        this.deviceSdkProvider = deviceSdkProvider2;
        this.locationServicesStatusApi18Provider = locationServicesStatusApi18Provider2;
        this.locationServicesStatusApi23Provider = locationServicesStatusApi23Provider2;
    }

    public LocationServicesStatus get() {
        return (LocationServicesStatus) Preconditions.checkNotNull(this.module.provideLocationServicesStatus(((Integer) this.deviceSdkProvider.get()).intValue(), this.locationServicesStatusApi18Provider, this.locationServicesStatusApi23Provider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0700x9cd4449f create(ClientModule module2, Provider<Integer> deviceSdkProvider2, Provider<LocationServicesStatusApi18> locationServicesStatusApi18Provider2, Provider<LocationServicesStatusApi23> locationServicesStatusApi23Provider2) {
        return new C0700x9cd4449f(module2, deviceSdkProvider2, locationServicesStatusApi18Provider2, locationServicesStatusApi23Provider2);
    }

    public static LocationServicesStatus proxyProvideLocationServicesStatus(ClientModule instance, int deviceSdk, Provider<LocationServicesStatusApi18> locationServicesStatusApi18Provider2, Provider<LocationServicesStatusApi23> locationServicesStatusApi23Provider2) {
        return (LocationServicesStatus) Preconditions.checkNotNull(instance.provideLocationServicesStatus(deviceSdk, locationServicesStatusApi18Provider2, locationServicesStatusApi23Provider2), "Cannot return null from a non-@Nullable @Provides method");
    }
}
