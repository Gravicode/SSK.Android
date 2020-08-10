package com.google.firebase.storage;

import com.google.firebase.storage.StorageTask.ProvideError;

final class zzaa implements Runnable {
    private /* synthetic */ zzx zzoky;
    private /* synthetic */ ProvideError zzokz;
    private /* synthetic */ Object zzola;

    zzaa(zzx zzx, Object obj, ProvideError provideError) {
        this.zzoky = zzx;
        this.zzola = obj;
        this.zzokz = provideError;
    }

    public final void run() {
        this.zzoky.zzokw.zzi(this.zzola, this.zzokz);
    }
}
