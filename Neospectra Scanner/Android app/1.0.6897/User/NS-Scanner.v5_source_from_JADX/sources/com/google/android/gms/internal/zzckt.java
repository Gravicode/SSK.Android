package com.google.android.gms.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zzckt implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ boolean zzjhf;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckt(zzckg zzckg, AtomicReference atomicReference, zzcgi zzcgi, boolean z) {
        this.zzjij = zzckg;
        this.zzjik = atomicReference;
        this.zzjgn = zzcgi;
        this.zzjhf = z;
    }

    public final void run() {
        AtomicReference atomicReference;
        synchronized (this.zzjik) {
            try {
                zzche zzd = this.zzjij.zzjid;
                if (zzd == null) {
                    this.zzjij.zzawy().zzazd().log("Failed to get user properties");
                    this.zzjik.notify();
                    return;
                }
                this.zzjik.set(zzd.zza(this.zzjgn, this.zzjhf));
                this.zzjij.zzxr();
                atomicReference = this.zzjik;
                atomicReference.notify();
            } catch (RemoteException e) {
                try {
                    this.zzjij.zzawy().zzazd().zzj("Failed to get user properties", e);
                    atomicReference = this.zzjik;
                } catch (Throwable th) {
                    this.zzjik.notify();
                    throw th;
                }
            }
        }
    }
}
