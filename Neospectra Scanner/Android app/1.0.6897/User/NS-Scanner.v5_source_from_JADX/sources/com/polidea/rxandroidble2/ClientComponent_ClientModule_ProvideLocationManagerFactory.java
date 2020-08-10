package com.polidea.rxandroidble2;

import android.location.LocationManager;
import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideLocationManagerFactory implements Factory<LocationManager> {
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideLocationManagerFactory(ClientModule module2) {
        this.module = module2;
    }

    public LocationManager get() {
        return (LocationManager) Preconditions.checkNotNull(this.module.provideLocationManager(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideLocationManagerFactory create(ClientModule module2) {
        return new ClientComponent_ClientModule_ProvideLocationManagerFactory(module2);
    }

    public static LocationManager proxyProvideLocationManager(ClientModule instance) {
        return (LocationManager) Preconditions.checkNotNull(instance.provideLocationManager(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
