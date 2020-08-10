package com.google.android.gms.internal;

final class zzcks implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcln zzjgt;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ boolean zzjin;

    zzcks(zzckg zzckg, boolean z, zzcln zzcln, zzcgi zzcgi) {
        this.zzjij = zzckg;
        this.zzjin = z;
        this.zzjgt = zzcln;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        zzche zzd = this.zzjij.zzjid;
        if (zzd == null) {
            this.zzjij.zzawy().zzazd().log("Discarding data. Failed to set user attribute");
            return;
        }
        this.zzjij.zza(zzd, this.zzjin ? null : this.zzjgt, this.zzjgn);
        this.zzjij.zzxr();
    }
}
