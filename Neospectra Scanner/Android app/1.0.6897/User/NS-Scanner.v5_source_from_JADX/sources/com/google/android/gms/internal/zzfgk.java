package com.google.android.gms.internal;

public class zzfgk {
    private static final zzffm zzpfe = zzffm.zzcxb();
    private zzfes zzphy;
    private volatile zzfhe zzphz;
    private volatile zzfes zzpia;

    private zzfhe zzj(zzfhe zzfhe) {
        if (this.zzphz == null) {
            synchronized (this) {
                if (this.zzphz == null) {
                    try {
                        this.zzphz = zzfhe;
                        this.zzpia = zzfes.zzpfg;
                    } catch (zzfge e) {
                        this.zzphz = zzfhe;
                        this.zzpia = zzfes.zzpfg;
                    }
                }
            }
        }
        return this.zzphz;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzfgk)) {
            return false;
        }
        zzfgk zzfgk = (zzfgk) obj;
        zzfhe zzfhe = this.zzphz;
        zzfhe zzfhe2 = zzfgk.zzphz;
        return (zzfhe == null && zzfhe2 == null) ? toByteString().equals(zzfgk.toByteString()) : (zzfhe == null || zzfhe2 == null) ? zzfhe != null ? zzfhe.equals(zzfgk.zzj(zzfhe.zzcxq())) : zzj(zzfhe2.zzcxq()).equals(zzfhe2) : zzfhe.equals(zzfhe2);
    }

    public int hashCode() {
        return 1;
    }

    public final zzfes toByteString() {
        if (this.zzpia != null) {
            return this.zzpia;
        }
        synchronized (this) {
            if (this.zzpia != null) {
                zzfes zzfes = this.zzpia;
                return zzfes;
            }
            this.zzpia = this.zzphz == null ? zzfes.zzpfg : this.zzphz.toByteString();
            zzfes zzfes2 = this.zzpia;
            return zzfes2;
        }
    }

    public final int zzho() {
        if (this.zzpia != null) {
            return this.zzpia.size();
        }
        if (this.zzphz != null) {
            return this.zzphz.zzho();
        }
        return 0;
    }

    public final zzfhe zzk(zzfhe zzfhe) {
        zzfhe zzfhe2 = this.zzphz;
        this.zzphy = null;
        this.zzpia = null;
        this.zzphz = zzfhe;
        return zzfhe2;
    }
}
