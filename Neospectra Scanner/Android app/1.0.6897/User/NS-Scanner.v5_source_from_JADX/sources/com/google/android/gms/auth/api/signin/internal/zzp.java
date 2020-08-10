package com.google.android.gms.auth.api.signin.internal;

public final class zzp {
    private static int zzeij = 31;
    private int zzeik = 1;

    public final int zzabn() {
        return this.zzeik;
    }

    public final zzp zzar(boolean z) {
        this.zzeik = (zzeij * this.zzeik) + (z ? 1 : 0);
        return this;
    }

    public final zzp zzs(Object obj) {
        this.zzeik = (zzeij * this.zzeik) + (obj == null ? 0 : obj.hashCode());
        return this;
    }
}
