package com.polidea.rxandroidble2.internal.util;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;

public class LocationServicesStatusApi23 implements LocationServicesStatus {
    private final CheckerLocationPermission checkerLocationPermission;
    private final CheckerLocationProvider checkerLocationProvider;
    private final boolean isAndroidWear;
    private final int targetSdk;

    @Inject
    LocationServicesStatusApi23(CheckerLocationProvider checkerLocationProvider2, CheckerLocationPermission checkerLocationPermission2, @Named("target-sdk") int targetSdk2, @Named("android-wear") boolean isAndroidWear2) {
        this.checkerLocationProvider = checkerLocationProvider2;
        this.checkerLocationPermission = checkerLocationPermission2;
        this.targetSdk = targetSdk2;
        this.isAndroidWear = isAndroidWear2;
    }

    public boolean isLocationPermissionOk() {
        return this.checkerLocationPermission.isLocationPermissionGranted();
    }

    public boolean isLocationProviderOk() {
        return !isLocationProviderEnabledRequired() || this.checkerLocationProvider.isLocationProviderEnabled();
    }

    private boolean isLocationProviderEnabledRequired() {
        return !this.isAndroidWear && this.targetSdk >= 23;
    }
}
