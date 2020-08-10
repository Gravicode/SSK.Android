package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import p005io.reactivex.Scheduler;

public final class ScanPreconditionsVerifierApi24_Factory implements Factory<ScanPreconditionsVerifierApi24> {
    private final Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierApi18Provider;
    private final Provider<Scheduler> timeSchedulerProvider;

    public ScanPreconditionsVerifierApi24_Factory(Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierApi18Provider2, Provider<Scheduler> timeSchedulerProvider2) {
        this.scanPreconditionVerifierApi18Provider = scanPreconditionVerifierApi18Provider2;
        this.timeSchedulerProvider = timeSchedulerProvider2;
    }

    public ScanPreconditionsVerifierApi24 get() {
        return new ScanPreconditionsVerifierApi24((ScanPreconditionsVerifierApi18) this.scanPreconditionVerifierApi18Provider.get(), (Scheduler) this.timeSchedulerProvider.get());
    }

    public static ScanPreconditionsVerifierApi24_Factory create(Provider<ScanPreconditionsVerifierApi18> scanPreconditionVerifierApi18Provider2, Provider<Scheduler> timeSchedulerProvider2) {
        return new ScanPreconditionsVerifierApi24_Factory(scanPreconditionVerifierApi18Provider2, timeSchedulerProvider2);
    }
}
