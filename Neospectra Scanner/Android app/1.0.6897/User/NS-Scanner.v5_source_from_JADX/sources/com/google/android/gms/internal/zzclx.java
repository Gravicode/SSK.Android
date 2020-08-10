package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclx extends zzfjm<zzclx> {
    private static volatile zzclx[] zzjks;
    public String name;
    public Boolean zzjkt;
    public Boolean zzjku;
    public Integer zzjkv;

    public zzclx() {
        this.name = null;
        this.zzjkt = null;
        this.zzjku = null;
        this.zzjkv = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzclx[] zzbbe() {
        if (zzjks == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjks == null) {
                    zzjks = new zzclx[0];
                }
            }
        }
        return zzjks;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclx)) {
            return false;
        }
        zzclx zzclx = (zzclx) obj;
        if (this.name == null) {
            if (zzclx.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzclx.name)) {
            return false;
        }
        if (this.zzjkt == null) {
            if (zzclx.zzjkt != null) {
                return false;
            }
        } else if (!this.zzjkt.equals(zzclx.zzjkt)) {
            return false;
        }
        if (this.zzjku == null) {
            if (zzclx.zzjku != null) {
                return false;
            }
        } else if (!this.zzjku.equals(zzclx.zzjku)) {
            return false;
        }
        if (this.zzjkv == null) {
            if (zzclx.zzjkv != null) {
                return false;
            }
        } else if (!this.zzjkv.equals(zzclx.zzjkv)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclx.zzpnc == null || zzclx.zzpnc.isEmpty() : this.zzpnc.equals(zzclx.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzjkt == null ? 0 : this.zzjkt.hashCode())) * 31) + (this.zzjku == null ? 0 : this.zzjku.hashCode())) * 31) + (this.zzjkv == null ? 0 : this.zzjkv.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                this.name = zzfjj.readString();
            } else if (zzcvt == 16) {
                this.zzjkt = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 24) {
                this.zzjku = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 32) {
                this.zzjkv = Integer.valueOf(zzfjj.zzcwi());
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.name != null) {
            zzfjk.zzn(1, this.name);
        }
        if (this.zzjkt != null) {
            zzfjk.zzl(2, this.zzjkt.booleanValue());
        }
        if (this.zzjku != null) {
            zzfjk.zzl(3, this.zzjku.booleanValue());
        }
        if (this.zzjkv != null) {
            zzfjk.zzaa(4, this.zzjkv.intValue());
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.name != null) {
            zzq += zzfjk.zzo(1, this.name);
        }
        if (this.zzjkt != null) {
            this.zzjkt.booleanValue();
            zzq += zzfjk.zzlg(2) + 1;
        }
        if (this.zzjku != null) {
            this.zzjku.booleanValue();
            zzq += zzfjk.zzlg(3) + 1;
        }
        return this.zzjkv != null ? zzq + zzfjk.zzad(4, this.zzjkv.intValue()) : zzq;
    }
}
