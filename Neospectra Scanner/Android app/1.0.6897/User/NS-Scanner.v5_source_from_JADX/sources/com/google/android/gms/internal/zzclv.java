package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclv extends zzfjm<zzclv> {
    private static volatile zzclv[] zzjkl;
    public Integer zzjjw;
    public String zzjkm;
    public zzclt zzjkn;

    public zzclv() {
        this.zzjjw = null;
        this.zzjkm = null;
        this.zzjkn = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzclv[] zzbbd() {
        if (zzjkl == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjkl == null) {
                    zzjkl = new zzclv[0];
                }
            }
        }
        return zzjkl;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclv)) {
            return false;
        }
        zzclv zzclv = (zzclv) obj;
        if (this.zzjjw == null) {
            if (zzclv.zzjjw != null) {
                return false;
            }
        } else if (!this.zzjjw.equals(zzclv.zzjjw)) {
            return false;
        }
        if (this.zzjkm == null) {
            if (zzclv.zzjkm != null) {
                return false;
            }
        } else if (!this.zzjkm.equals(zzclv.zzjkm)) {
            return false;
        }
        if (this.zzjkn == null) {
            if (zzclv.zzjkn != null) {
                return false;
            }
        } else if (!this.zzjkn.equals(zzclv.zzjkn)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclv.zzpnc == null || zzclv.zzpnc.isEmpty() : this.zzpnc.equals(zzclv.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((getClass().getName().hashCode() + 527) * 31) + (this.zzjjw == null ? 0 : this.zzjjw.hashCode())) * 31) + (this.zzjkm == null ? 0 : this.zzjkm.hashCode());
        zzclt zzclt = this.zzjkn;
        int hashCode2 = ((hashCode * 31) + (zzclt == null ? 0 : zzclt.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode2 + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 8) {
                this.zzjjw = Integer.valueOf(zzfjj.zzcwi());
            } else if (zzcvt == 18) {
                this.zzjkm = zzfjj.readString();
            } else if (zzcvt == 26) {
                if (this.zzjkn == null) {
                    this.zzjkn = new zzclt();
                }
                zzfjj.zza(this.zzjkn);
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjjw != null) {
            zzfjk.zzaa(1, this.zzjjw.intValue());
        }
        if (this.zzjkm != null) {
            zzfjk.zzn(2, this.zzjkm);
        }
        if (this.zzjkn != null) {
            zzfjk.zza(3, (zzfjs) this.zzjkn);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjjw != null) {
            zzq += zzfjk.zzad(1, this.zzjjw.intValue());
        }
        if (this.zzjkm != null) {
            zzq += zzfjk.zzo(2, this.zzjkm);
        }
        return this.zzjkn != null ? zzq + zzfjk.zzb(3, (zzfjs) this.zzjkn) : zzq;
    }
}
