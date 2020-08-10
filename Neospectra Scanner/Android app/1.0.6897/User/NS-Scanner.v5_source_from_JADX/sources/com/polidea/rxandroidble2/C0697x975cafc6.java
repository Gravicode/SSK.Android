package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideDisableNotificationValueFactory */
public final class C0697x975cafc6 implements Factory<byte[]> {
    private static final C0697x975cafc6 INSTANCE = new C0697x975cafc6();

    public byte[] get() {
        return (byte[]) Preconditions.checkNotNull(ClientModule.provideDisableNotificationValue(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0697x975cafc6 create() {
        return INSTANCE;
    }

    public static byte[] proxyProvideDisableNotificationValue() {
        return (byte[]) Preconditions.checkNotNull(ClientModule.provideDisableNotificationValue(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
