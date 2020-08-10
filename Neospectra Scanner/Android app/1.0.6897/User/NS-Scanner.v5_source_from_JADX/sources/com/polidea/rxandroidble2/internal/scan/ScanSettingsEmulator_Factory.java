package com.polidea.rxandroidble2.internal.scan;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import p005io.reactivex.Scheduler;

public final class ScanSettingsEmulator_Factory implements Factory<ScanSettingsEmulator> {
    private final Provider<Scheduler> schedulerProvider;

    public ScanSettingsEmulator_Factory(Provider<Scheduler> schedulerProvider2) {
        this.schedulerProvider = schedulerProvider2;
    }

    public ScanSettingsEmulator get() {
        return new ScanSettingsEmulator((Scheduler) this.schedulerProvider.get());
    }

    public static ScanSettingsEmulator_Factory create(Provider<Scheduler> schedulerProvider2) {
        return new ScanSettingsEmulator_Factory(schedulerProvider2);
    }
}
