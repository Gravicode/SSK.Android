package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.internal.util.LocationServicesOkObservableApi23;
import p005io.reactivex.Observable;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideLocationServicesOkObservableFactory */
public final class C0699x61f40e72 implements Factory<Observable<Boolean>> {
    private final Provider<Integer> deviceSdkProvider;
    private final Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider;
    private final ClientModule module;

    public C0699x61f40e72(ClientModule module2, Provider<Integer> deviceSdkProvider2, Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider2) {
        this.module = module2;
        this.deviceSdkProvider = deviceSdkProvider2;
        this.locationServicesOkObservableApi23Provider = locationServicesOkObservableApi23Provider2;
    }

    public Observable<Boolean> get() {
        return (Observable) Preconditions.checkNotNull(this.module.provideLocationServicesOkObservable(((Integer) this.deviceSdkProvider.get()).intValue(), this.locationServicesOkObservableApi23Provider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0699x61f40e72 create(ClientModule module2, Provider<Integer> deviceSdkProvider2, Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider2) {
        return new C0699x61f40e72(module2, deviceSdkProvider2, locationServicesOkObservableApi23Provider2);
    }

    public static Observable<Boolean> proxyProvideLocationServicesOkObservable(ClientModule instance, int deviceSdk, Provider<LocationServicesOkObservableApi23> locationServicesOkObservableApi23Provider2) {
        return (Observable) Preconditions.checkNotNull(instance.provideLocationServicesOkObservable(deviceSdk, locationServicesOkObservableApi23Provider2), "Cannot return null from a non-@Nullable @Provides method");
    }
}
