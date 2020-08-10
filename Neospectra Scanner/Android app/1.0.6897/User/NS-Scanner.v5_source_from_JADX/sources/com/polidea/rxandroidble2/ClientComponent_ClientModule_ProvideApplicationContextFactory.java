package com.polidea.rxandroidble2;

import android.content.Context;
import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

public final class ClientComponent_ClientModule_ProvideApplicationContextFactory implements Factory<Context> {
    private final ClientModule module;

    public ClientComponent_ClientModule_ProvideApplicationContextFactory(ClientModule module2) {
        this.module = module2;
    }

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.provideApplicationContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideApplicationContextFactory create(ClientModule module2) {
        return new ClientComponent_ClientModule_ProvideApplicationContextFactory(module2);
    }

    public static Context proxyProvideApplicationContext(ClientModule instance) {
        return (Context) Preconditions.checkNotNull(instance.provideApplicationContext(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
