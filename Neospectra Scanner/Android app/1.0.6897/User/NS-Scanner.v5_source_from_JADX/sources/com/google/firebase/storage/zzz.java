package com.google.firebase.storage;

import com.google.firebase.storage.StorageTask.ProvideError;

final class zzz implements Runnable {
    private /* synthetic */ Object zzokx;
    private /* synthetic */ zzx zzoky;
    private /* synthetic */ ProvideError zzokz;

    zzz(zzx zzx, Object obj, ProvideError provideError) {
        this.zzoky = zzx;
        this.zzokx = obj;
        this.zzokz = provideError;
    }

    public final void run() {
        this.zzoky.zzokw.zzi(this.zzokx, this.zzokz);
    }
}
