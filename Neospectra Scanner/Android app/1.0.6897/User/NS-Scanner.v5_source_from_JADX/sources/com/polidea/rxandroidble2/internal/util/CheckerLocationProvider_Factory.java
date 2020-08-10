package com.polidea.rxandroidble2.internal.util;

import android.content.ContentResolver;
import android.location.LocationManager;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class CheckerLocationProvider_Factory implements Factory<CheckerLocationProvider> {
    private final Provider<ContentResolver> contentResolverProvider;
    private final Provider<LocationManager> locationManagerProvider;

    public CheckerLocationProvider_Factory(Provider<ContentResolver> contentResolverProvider2, Provider<LocationManager> locationManagerProvider2) {
        this.contentResolverProvider = contentResolverProvider2;
        this.locationManagerProvider = locationManagerProvider2;
    }

    public CheckerLocationProvider get() {
        return new CheckerLocationProvider((ContentResolver) this.contentResolverProvider.get(), (LocationManager) this.locationManagerProvider.get());
    }

    public static CheckerLocationProvider_Factory create(Provider<ContentResolver> contentResolverProvider2, Provider<LocationManager> locationManagerProvider2) {
        return new CheckerLocationProvider_Factory(contentResolverProvider2, locationManagerProvider2);
    }

    public static CheckerLocationProvider newCheckerLocationProvider(ContentResolver contentResolver, LocationManager locationManager) {
        return new CheckerLocationProvider(contentResolver, locationManager);
    }
}
