package com.google.android.gms.internal;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzckp implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ boolean zzjim = true;
    private /* synthetic */ boolean zzjin;
    private /* synthetic */ zzcgl zzjio;
    private /* synthetic */ zzcgl zzjip;

    zzckp(zzckg zzckg, boolean z, boolean z2, zzcgl zzcgl, zzcgi zzcgi, zzcgl zzcgl2) {
        this.zzjij = zzckg;
        this.zzjin = z2;
        this.zzjio = zzcgl;
        this.zzjgn = zzcgi;
        this.zzjip = zzcgl2;
    }

    public final void run() {
        zzche zzd = this.zzjij.zzjid;
        if (zzd == null) {
            this.zzjij.zzawy().zzazd().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzjim) {
            this.zzjij.zza(zzd, this.zzjin ? null : this.zzjio, this.zzjgn);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzjip.packageName)) {
                    zzd.zza(this.zzjio, this.zzjgn);
                } else {
                    zzd.zzb(this.zzjio);
                }
            } catch (RemoteException e) {
                this.zzjij.zzawy().zzazd().zzj("Failed to send conditional user property to the service", e);
            }
        }
        this.zzjij.zzxr();
    }
}
