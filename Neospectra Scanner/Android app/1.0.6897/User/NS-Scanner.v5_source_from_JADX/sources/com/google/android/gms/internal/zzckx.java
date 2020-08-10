package com.google.android.gms.internal;

final class zzckx implements Runnable {
    private /* synthetic */ zzcku zzjit;
    private /* synthetic */ zzche zzjiu;

    zzckx(zzcku zzcku, zzche zzche) {
        this.zzjit = zzcku;
        this.zzjiu = zzche;
    }

    public final void run() {
        synchronized (this.zzjit) {
            this.zzjit.zzjiq = false;
            if (!this.zzjit.zzjij.isConnected()) {
                this.zzjit.zzjij.zzawy().zzazi().log("Connected to remote service");
                this.zzjit.zzjij.zza(this.zzjiu);
            }
        }
    }
}
