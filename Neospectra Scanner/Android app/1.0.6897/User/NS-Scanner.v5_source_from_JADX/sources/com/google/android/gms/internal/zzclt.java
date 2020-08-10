package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclt extends zzfjm<zzclt> {
    private static volatile zzclt[] zzjkb;
    public zzclw zzjkc;
    public zzclu zzjkd;
    public Boolean zzjke;
    public String zzjkf;

    public zzclt() {
        this.zzjkc = null;
        this.zzjkd = null;
        this.zzjke = null;
        this.zzjkf = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzclt[] zzbbc() {
        if (zzjkb == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjkb == null) {
                    zzjkb = new zzclt[0];
                }
            }
        }
        return zzjkb;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclt)) {
            return false;
        }
        zzclt zzclt = (zzclt) obj;
        if (this.zzjkc == null) {
            if (zzclt.zzjkc != null) {
                return false;
            }
        } else if (!this.zzjkc.equals(zzclt.zzjkc)) {
            return false;
        }
        if (this.zzjkd == null) {
            if (zzclt.zzjkd != null) {
                return false;
            }
        } else if (!this.zzjkd.equals(zzclt.zzjkd)) {
            return false;
        }
        if (this.zzjke == null) {
            if (zzclt.zzjke != null) {
                return false;
            }
        } else if (!this.zzjke.equals(zzclt.zzjke)) {
            return false;
        }
        if (this.zzjkf == null) {
            if (zzclt.zzjkf != null) {
                return false;
            }
        } else if (!this.zzjkf.equals(zzclt.zzjkf)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclt.zzpnc == null || zzclt.zzpnc.isEmpty() : this.zzpnc.equals(zzclt.zzpnc);
    }

    public final int hashCode() {
        int hashCode = getClass().getName().hashCode() + 527;
        zzclw zzclw = this.zzjkc;
        int i = 0;
        int hashCode2 = (hashCode * 31) + (zzclw == null ? 0 : zzclw.hashCode());
        zzclu zzclu = this.zzjkd;
        int hashCode3 = ((((((hashCode2 * 31) + (zzclu == null ? 0 : zzclu.hashCode())) * 31) + (this.zzjke == null ? 0 : this.zzjke.hashCode())) * 31) + (this.zzjkf == null ? 0 : this.zzjkf.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode3 + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        zzfjs zzfjs;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                if (this.zzjkc == null) {
                    this.zzjkc = new zzclw();
                }
                zzfjs = this.zzjkc;
            } else if (zzcvt == 18) {
                if (this.zzjkd == null) {
                    this.zzjkd = new zzclu();
                }
                zzfjs = this.zzjkd;
            } else if (zzcvt == 24) {
                this.zzjke = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 34) {
                this.zzjkf = zzfjj.readString();
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
            zzfjj.zza(zzfjs);
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjkc != null) {
            zzfjk.zza(1, (zzfjs) this.zzjkc);
        }
        if (this.zzjkd != null) {
            zzfjk.zza(2, (zzfjs) this.zzjkd);
        }
        if (this.zzjke != null) {
            zzfjk.zzl(3, this.zzjke.booleanValue());
        }
        if (this.zzjkf != null) {
            zzfjk.zzn(4, this.zzjkf);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjkc != null) {
            zzq += zzfjk.zzb(1, (zzfjs) this.zzjkc);
        }
        if (this.zzjkd != null) {
            zzq += zzfjk.zzb(2, (zzfjs) this.zzjkd);
        }
        if (this.zzjke != null) {
            this.zzjke.booleanValue();
            zzq += zzfjk.zzlg(3) + 1;
        }
        return this.zzjkf != null ? zzq + zzfjk.zzo(4, this.zzjkf) : zzq;
    }
}
