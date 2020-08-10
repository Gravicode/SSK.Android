package com.google.android.gms.internal;

import android.os.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

final class zzckj implements Runnable {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzckg zzjij;
    private /* synthetic */ AtomicReference zzjik;

    zzckj(zzckg zzckg, AtomicReference atomicReference, zzcgi zzcgi) {
        this.zzjij = zzckg;
        this.zzjik = atomicReference;
        this.zzjgn = zzcgi;
    }

    public final void run() {
        AtomicReference atomicReference;
        synchronized (this.zzjik) {
            try {
                zzche zzd = this.zzjij.zzjid;
                if (zzd == null) {
                    this.zzjij.zzawy().zzazd().log("Failed to get app instance id");
                    this.zzjik.notify();
                    return;
                }
                this.zzjik.set(zzd.zzc(this.zzjgn));
                String str = (String) this.zzjik.get();
                if (str != null) {
                    this.zzjij.zzawm().zzjp(str);
                    this.zzjij.zzawz().zzjcx.zzjq(str);
                }
                this.zzjij.zzxr();
                atomicReference = this.zzjik;
                atomicReference.notify();
            } catch (RemoteException e) {
                try {
                    this.zzjij.zzawy().zzazd().zzj("Failed to get app instance id", e);
                    atomicReference = this.zzjik;
                } catch (Throwable th) {
                    this.zzjik.notify();
                    throw th;
                }
            }
        }
    }
}
