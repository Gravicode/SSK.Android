package com.polidea.rxandroidble2.internal.util;

import android.content.Context;
import android.os.Process;
import bleshadow.javax.inject.Inject;

public class CheckerLocationPermission {
    private final Context context;

    @Inject
    public CheckerLocationPermission(Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: 0000 */
    public boolean isLocationPermissionGranted() {
        return isPermissionGranted("android.permission.ACCESS_COARSE_LOCATION") || isPermissionGranted("android.permission.ACCESS_FINE_LOCATION");
    }

    private boolean isPermissionGranted(String permission) {
        if (permission != null) {
            return this.context.checkPermission(permission, Process.myPid(), Process.myUid()) == 0;
        }
        throw new IllegalArgumentException("permission is null");
    }
}
