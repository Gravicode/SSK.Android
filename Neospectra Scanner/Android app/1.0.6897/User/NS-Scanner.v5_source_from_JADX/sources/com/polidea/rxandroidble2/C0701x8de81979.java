package com.polidea.rxandroidble2;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import bleshadow.javax.inject.Provider;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifier;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi18;
import com.polidea.rxandroidble2.internal.scan.ScanPreconditionsVerifierApi24;

/* renamed from: com.polidea.rxandroidble2.ClientComponent_ClientModule_ProvideScanPreconditionVerifierFactory */
public final class C0701x8de81979 implements Factory<ScanPreconditionsVerifier> {
    private final Provider<Integer> deviceSdkProvider;
    private final Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierForApi18Provider;
    private final Provider<ScanPreconditionsVerifierApi24> scanPreconditionVerifierForApi24Provider;

    public C0701x8de81979(Provider<Integer> deviceSdkProvider2, Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierForApi18Provider2, Provider<ScanPreconditionsVerifierApi24> scanPreconditionVerifierForApi24Provider2) {
        this.deviceSdkProvider = deviceSdkProvider2;
        this.scanPreconditionVerifierForApi18Provider = scanPreconditionVerifierForApi18Provider2;
        this.scanPreconditionVerifierForApi24Provider = scanPreconditionVerifierForApi24Provider2;
    }

    public ScanPreconditionsVerifier get() {
        return (ScanPreconditionsVerifier) Preconditions.checkNotNull(ClientModule.provideScanPreconditionVerifier(((Integer) this.deviceSdkProvider.get()).intValue(), this.scanPreconditionVerifierForApi18Provider, this.scanPreconditionVerifierForApi24Provider), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static C0701x8de81979 create(Provider<Integer> deviceSdkProvider2, Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierForApi18Provider2, Provider<ScanPreconditionsVerifierApi24> scanPreconditionVerifierForApi24Provider2) {
        return new C0701x8de81979(deviceSdkProvider2, scanPreconditionVerifierForApi18Provider2, scanPreconditionVerifierForApi24Provider2);
    }

    public static ScanPreconditionsVerifier proxyProvideScanPreconditionVerifier(int deviceSdk, Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierForApi18, Provider<ScanPreconditionsVerifierApi24> scanPreconditionVerifierForApi24) {
        return (ScanPreconditionsVerifier) Preconditions.checkNotNull(ClientModule.provideScanPreconditionVerifier(deviceSdk, scanPreconditionVerifierForApi18, scanPreconditionVerifierForApi24), "Cannot return null from a non-@Nullable @Provides method");
    }
}
