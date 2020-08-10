package com.polidea.rxandroidble2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;
import bleshadow.javax.inject.Inject;
import p005io.reactivex.Observable;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.functions.Action;

public class RxBleAdapterStateObservable extends Observable<BleAdapterState> {
    private static final String TAG = "AdapterStateObs";
    /* access modifiers changed from: private */
    @NonNull
    public final Context context;

    public static class BleAdapterState {
        public static final BleAdapterState STATE_OFF = new BleAdapterState(false);
        public static final BleAdapterState STATE_ON = new BleAdapterState(true);
        public static final BleAdapterState STATE_TURNING_OFF = new BleAdapterState(false);
        public static final BleAdapterState STATE_TURNING_ON = new BleAdapterState(false);
        private final boolean isUsable;

        private BleAdapterState(boolean isUsable2) {
            this.isUsable = isUsable2;
        }

        public boolean isUsable() {
            return this.isUsable;
        }
    }

    @Inject
    public RxBleAdapterStateObservable(@NonNull Context context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final Observer<? super BleAdapterState> observer) {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction())) {
                    observer.onNext(RxBleAdapterStateObservable.mapToBleAdapterState(intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1)));
                }
            }
        };
        observer.onSubscribe(Disposables.fromAction(new Action() {
            public void run() throws Exception {
                try {
                    RxBleAdapterStateObservable.this.context.unregisterReceiver(receiver);
                } catch (IllegalArgumentException e) {
                    Log.d(RxBleAdapterStateObservable.TAG, "The receiver is already not registered.");
                }
            }
        }));
        this.context.registerReceiver(receiver, createFilter());
    }

    /* access modifiers changed from: private */
    public static BleAdapterState mapToBleAdapterState(int state) {
        switch (state) {
            case 11:
                return BleAdapterState.STATE_TURNING_ON;
            case 12:
                return BleAdapterState.STATE_ON;
            case 13:
                return BleAdapterState.STATE_TURNING_OFF;
            default:
                return BleAdapterState.STATE_OFF;
        }
    }

    private static IntentFilter createFilter() {
        return new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED");
    }
}
