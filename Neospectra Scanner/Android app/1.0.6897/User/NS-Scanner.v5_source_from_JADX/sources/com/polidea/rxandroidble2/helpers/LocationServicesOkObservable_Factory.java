package com.polidea.rxandroidble2.helpers;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;
import p005io.reactivex.Observable;

public final class LocationServicesOkObservable_Factory implements Factory<LocationServicesOkObservable> {
    private final Provider<Observable<Boolean>> locationServicesOkObsImplProvider;

    public LocationServicesOkObservable_Factory(Provider<Observable<Boolean>> locationServicesOkObsImplProvider2) {
        this.locationServicesOkObsImplProvider = locationServicesOkObsImplProvider2;
    }

    public LocationServicesOkObservable get() {
        return new LocationServicesOkObservable((Observable) this.locationServicesOkObsImplProvider.get());
    }

    public static LocationServicesOkObservable_Factory create(Provider<Observable<Boolean>> locationServicesOkObsImplProvider2) {
        return new LocationServicesOkObservable_Factory(locationServicesOkObsImplProvider2);
    }

    public static LocationServicesOkObservable newLocationServicesOkObservable(Observable<Boolean> locationServicesOkObsImpl) {
        return new LocationServicesOkObservable(locationServicesOkObsImpl);
    }
}
