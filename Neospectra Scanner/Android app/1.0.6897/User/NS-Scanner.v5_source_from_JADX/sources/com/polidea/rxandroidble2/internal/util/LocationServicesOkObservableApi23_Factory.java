package com.polidea.rxandroidble2.internal.util;

import android.content.Context;
import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class LocationServicesOkObservableApi23_Factory implements Factory<LocationServicesOkObservableApi23> {
    private final Provider<Context> contextProvider;
    private final Provider<LocationServicesStatus> locationServicesStatusProvider;

    public LocationServicesOkObservableApi23_Factory(Provider<Context> contextProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2) {
        this.contextProvider = contextProvider2;
        this.locationServicesStatusProvider = locationServicesStatusProvider2;
    }

    public LocationServicesOkObservableApi23 get() {
        return new LocationServicesOkObservableApi23((Context) this.contextProvider.get(), (LocationServicesStatus) this.locationServicesStatusProvider.get());
    }

    public static LocationServicesOkObservableApi23_Factory create(Provider<Context> contextProvider2, Provider<LocationServicesStatus> locationServicesStatusProvider2) {
        return new LocationServicesOkObservableApi23_Factory(contextProvider2, locationServicesStatusProvider2);
    }

    public static LocationServicesOkObservableApi23 newLocationServicesOkObservableApi23(Context context, LocationServicesStatus locationServicesStatus) {
        return new LocationServicesOkObservableApi23(context, locationServicesStatus);
    }
}
