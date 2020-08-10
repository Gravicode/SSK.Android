package com.google.android.gms.internal;

public class zzad extends Exception {
    private long zzae;
    private zzp zzbk;

    public zzad() {
        this.zzbk = null;
    }

    public zzad(zzp zzp) {
        this.zzbk = zzp;
    }

    public zzad(String str) {
        super(str);
        this.zzbk = null;
    }

    public zzad(Throwable th) {
        super(th);
        this.zzbk = null;
    }

    /* access modifiers changed from: 0000 */
    public final void zza(long j) {
        this.zzae = j;
    }
}
