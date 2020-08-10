package com.google.android.gms.common.api.internal;

public final class zzck<L> {
    private final L zzfuk;
    private final String zzfun;

    zzck(L l, String str) {
        this.zzfuk = l;
        this.zzfun = str;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzck)) {
            return false;
        }
        zzck zzck = (zzck) obj;
        return this.zzfuk == zzck.zzfuk && this.zzfun.equals(zzck.zzfun);
    }

    public final int hashCode() {
        return (System.identityHashCode(this.zzfuk) * 31) + this.zzfun.hashCode();
    }
}
