package com.polidea.rxandroidble2.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.ClientComponent.ClientModule;
import com.polidea.rxandroidble2.DaggerClientComponent;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.observers.DisposableObserver;

public class LocationServicesOkObservable extends Observable<Boolean> {
    @NonNull
    private final Observable<Boolean> locationServicesOkObsImpl;

    public static LocationServicesOkObservable createInstance(@NonNull Context context) {
        return DaggerClientComponent.builder().clientModule(new ClientModule(context)).build().locationServicesOkObservable();
    }

    @Inject
    LocationServicesOkObservable(@NonNull @Named("location-ok-boolean-observable") Observable<Boolean> locationServicesOkObsImpl2) {
        this.locationServicesOkObsImpl = locationServicesOkObsImpl2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super Boolean> observer) {
        DisposableObserver<? super Boolean> disposableObserver = DisposableUtil.disposableObserver(observer);
        observer.onSubscribe(disposableObserver);
        this.locationServicesOkObsImpl.subscribeWith(disposableObserver);
    }
}
