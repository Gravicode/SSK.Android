package com.polidea.rxandroidble2.internal.util;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class LocationServicesStatusApi23_Factory implements Factory<LocationServicesStatusApi23> {
    private final Provider<CheckerLocationPermission> checkerLocationPermissionProvider;
    private final Provider<CheckerLocationProvider> checkerLocationProvider;
    private final Provider<Boolean> isAndroidWearProvider;
    private final Provider<Integer> targetSdkProvider;

    public LocationServicesStatusApi23_Factory(Provider<CheckerLocationProvider> checkerLocationProvider2, Provider<CheckerLocationPermission> checkerLocationPermissionProvider2, Provider<Integer> targetSdkProvider2, Provider<Boolean> isAndroidWearProvider2) {
        this.checkerLocationProvider = checkerLocationProvider2;
        this.checkerLocationPermissionProvider = checkerLocationPermissionProvider2;
        this.targetSdkProvider = targetSdkProvider2;
        this.isAndroidWearProvider = isAndroidWearProvider2;
    }

    public LocationServicesStatusApi23 get() {
        return new LocationServicesStatusApi23((CheckerLocationProvider) this.checkerLocationProvider.get(), (CheckerLocationPermission) this.checkerLocationPermissionProvider.get(), ((Integer) this.targetSdkProvider.get()).intValue(), ((Boolean) this.isAndroidWearProvider.get()).booleanValue());
    }

    public static LocationServicesStatusApi23_Factory create(Provider<CheckerLocationProvider> checkerLocationProvider2, Provider<CheckerLocationPermission> checkerLocationPermissionProvider2, Provider<Integer> targetSdkProvider2, Provider<Boolean> isAndroidWearProvider2) {
        return new LocationServicesStatusApi23_Factory(checkerLocationProvider2, checkerLocationPermissionProvider2, targetSdkProvider2, isAndroidWearProvider2);
    }

    public static LocationServicesStatusApi23 newLocationServicesStatusApi23(CheckerLocationProvider checkerLocationProvider2, CheckerLocationPermission checkerLocationPermission, int targetSdk, boolean isAndroidWear) {
        return new LocationServicesStatusApi23(checkerLocationProvider2, checkerLocationPermission, targetSdk, isAndroidWear);
    }
}
