package com.google.android.gms.common.api.internal;

abstract class zzbj {
    private final zzbh zzfsv;

    protected zzbj(zzbh zzbh) {
        this.zzfsv = zzbh;
    }

    /* access modifiers changed from: protected */
    public abstract void zzaib();

    public final void zzc(zzbi zzbi) {
        zzbi.zzfps.lock();
        try {
            if (zzbi.zzfsr == this.zzfsv) {
                zzaib();
            }
        } finally {
            zzbi.zzfps.unlock();
        }
    }
}
