package com.polidea.rxandroidble2;

import android.content.ContentResolver;
import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideContentResolverFactory implements Factory<ContentResolver> {
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideContentResolverFactory(ClientModule module2) {
        this.module = module2;
    }

    public ContentResolver get() {
        return (ContentResolver) Preconditions.checkNotNull(this.module.provideContentResolver(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideContentResolverFactory create(ClientModule module2) {
        return new ClientComponent_ClientModule_ProvideContentResolverFactory(module2);
    }

    public static ContentResolver proxyProvideContentResolver(ClientModule instance) {
        return (ContentResolver) Preconditions.checkNotNull(instance.provideContentResolver(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
