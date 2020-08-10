package com.google.android.gms.internal;

import android.content.ComponentName;

final class zzckw implements Runnable {
    private /* synthetic */ ComponentName val$name;
    private /* synthetic */ zzcku zzjit;

    zzckw(zzcku zzcku, ComponentName componentName) {
        this.zzjit = zzcku;
        this.val$name = componentName;
    }

    public final void run() {
        this.zzjit.zzjij.onServiceDisconnected(this.val$name);
    }
}
