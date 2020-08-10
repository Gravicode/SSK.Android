package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideEnableNotificationValueFactory */
public final class C0698xd399626d implements Factory<byte[]> {
    private static final C0698xd399626d INSTANCE = new C0698xd399626d();

    public byte[] get() {
        return (byte[]) Preconditions.checkNotNull(ClientModule.provideEnableNotificationValue(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0698xd399626d create() {
        return INSTANCE;
    }

    public static byte[] proxyProvideEnableNotificationValue() {
        return (byte[]) Preconditions.checkNotNull(ClientModule.provideEnableNotificationValue(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
