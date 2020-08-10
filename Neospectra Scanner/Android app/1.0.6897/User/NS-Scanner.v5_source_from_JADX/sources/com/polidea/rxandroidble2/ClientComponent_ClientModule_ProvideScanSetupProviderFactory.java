package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilder;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi18;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi21;
import com.polidea.rxandroidble2.internal.scan.ScanSetupBuilderImplApi23;

public final class ClientComponent_ClientModule_ProvideScanSetupProviderFactory implements Factory<ScanSetupBuilder> {
    private final Provider<Integer> deviceSdkProvider;
    private final Provider<ScanSetupBuilderImplApi18> scanSetupBuilderProviderForApi18Provider;
    private final Provider<ScanSetupBuilderImplApi21> scanSetupBuilderProviderForApi21Provider;
    private final Provider<ScanSetupBuilderImplApi23> scanSetupBuilderProviderForApi23Provider;

    public ClientComponent_ClientModule_ProvideScanSetupProviderFactory(Provider<Integer> deviceSdkProvider2, Provider<ScanSetupBuilderImplApi18> scanSetupBuilderProviderForApi18Provider2, Provider<ScanSetupBuilderImplApi21> scanSetupBuilderProviderForApi21Provider2, Provider<ScanSetupBuilderImplApi23> scanSetupBuilderProviderForApi23Provider2) {
        this.deviceSdkProvider = deviceSdkProvider2;
        this.scanSetupBuilderProviderForApi18Provider = scanSetupBuilderProviderForApi18Provider2;
        this.scanSetupBuilderProviderForApi21Provider = scanSetupBuilderProviderForApi21Provider2;
        this.scanSetupBuilderProviderForApi23Provider = scanSetupBuilderProviderForApi23Provider2;
    }

    public ScanSetupBuilder get() {
        return (ScanSetupBuilder) Preconditions.checkNotNull(ClientModule.provideScanSetupProvider(((Integer) this.deviceSdkProvider.get()).intValue(), this.scanSetupBuilderProviderForApi18Provider, this.scanSetupBuilderProviderForApi21Provider, this.scanSetupBuilderProviderForApi23Provider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ClientComponent_ClientModule_ProvideScanSetupProviderFactory create(Provider<Integer> deviceSdkProvider2, Provider<ScanSetupBuilderImplApi18> scanSetupBuilderProviderForApi18Provider2, Provider<ScanSetupBuilderImplApi21> scanSetupBuilderProviderForApi21Provider2, Provider<ScanSetupBuilderImplApi23> scanSetupBuilderProviderForApi23Provider2) {
        return new ClientComponent_ClientModule_ProvideScanSetupProviderFactory(deviceSdkProvider2, scanSetupBuilderProviderForApi18Provider2, scanSetupBuilderProviderForApi21Provider2, scanSetupBuilderProviderForApi23Provider2);
    }

    public static ScanSetupBuilder proxyProvideScanSetupProvider(int deviceSdk, Provider<ScanSetupBuilderImplApi18> scanSetupBuilderProviderForApi18, Provider<ScanSetupBuilderImplApi21> scanSetupBuilderProviderForApi21, Provider<ScanSetupBuilderImplApi23> scanSetupBuilderProviderForApi23) {
        return (ScanSetupBuilder) Preconditions.checkNotNull(ClientModule.provideScanSetupProvider(deviceSdk, scanSetupBuilderProviderForApi18, scanSetupBuilderProviderForApi21, scanSetupBuilderProviderForApi23), "Cannot return null from a non-@Nullable @Provides method");
    }
}
