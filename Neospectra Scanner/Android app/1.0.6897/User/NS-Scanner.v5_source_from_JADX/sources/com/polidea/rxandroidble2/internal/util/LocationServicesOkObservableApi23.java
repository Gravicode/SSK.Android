package com.polidea.rxandroidble2.internal.util;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import bleshadow.javax.inject.Inject;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.functions.Action;

@TargetApi(19)
public class LocationServicesOkObservableApi23 extends Observable<Boolean> {
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final LocationServicesStatus locationServicesStatus;

    @Inject
    LocationServicesOkObservableApi23(Context context2, LocationServicesStatus locationServicesStatus2) {
        this.context = context2;
        this.locationServicesStatus = locationServicesStatus2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final Observer<? super Boolean> observer) {
        boolean locationProviderOk = this.locationServicesStatus.isLocationProviderOk();
        final AtomicBoolean locationProviderOkAtomicBoolean = new AtomicBoolean(locationProviderOk);
        final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                boolean newLocationProviderOkValue = LocationServicesOkObservableApi23.this.locationServicesStatus.isLocationProviderOk();
                if (locationProviderOkAtomicBoolean.compareAndSet(!newLocationProviderOkValue, newLocationProviderOkValue)) {
                    observer.onNext(Boolean.valueOf(newLocationProviderOkValue));
                }
            }
        };
        this.context.registerReceiver(broadcastReceiver, new IntentFilter("android.location.MODE_CHANGED"));
        observer.onSubscribe(Disposables.fromAction(new Action() {
            public void run() throws Exception {
                LocationServicesOkObservableApi23.this.context.unregisterReceiver(broadcastReceiver);
            }
        }));
        observer.onNext(Boolean.valueOf(locationProviderOk));
    }
}
