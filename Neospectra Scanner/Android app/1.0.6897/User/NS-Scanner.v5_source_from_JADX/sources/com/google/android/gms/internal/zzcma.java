package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcma extends zzfjm<zzcma> {
    private static volatile zzcma[] zzjlc;
    public Integer zzjjs;
    public zzcmf zzjld;
    public zzcmf zzjle;
    public Boolean zzjlf;

    public zzcma() {
        this.zzjjs = null;
        this.zzjld = null;
        this.zzjle = null;
        this.zzjlf = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzcma[] zzbbg() {
        if (zzjlc == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjlc == null) {
                    zzjlc = new zzcma[0];
                }
            }
        }
        return zzjlc;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcma)) {
            return false;
        }
        zzcma zzcma = (zzcma) obj;
        if (this.zzjjs == null) {
            if (zzcma.zzjjs != null) {
                return false;
            }
        } else if (!this.zzjjs.equals(zzcma.zzjjs)) {
            return false;
        }
        if (this.zzjld == null) {
            if (zzcma.zzjld != null) {
                return false;
            }
        } else if (!this.zzjld.equals(zzcma.zzjld)) {
            return false;
        }
        if (this.zzjle == null) {
            if (zzcma.zzjle != null) {
                return false;
            }
        } else if (!this.zzjle.equals(zzcma.zzjle)) {
            return false;
        }
        if (this.zzjlf == null) {
            if (zzcma.zzjlf != null) {
                return false;
            }
        } else if (!this.zzjlf.equals(zzcma.zzjlf)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcma.zzpnc == null || zzcma.zzpnc.isEmpty() : this.zzpnc.equals(zzcma.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((getClass().getName().hashCode() + 527) * 31) + (this.zzjjs == null ? 0 : this.zzjjs.hashCode());
        zzcmf zzcmf = this.zzjld;
        int hashCode2 = (hashCode * 31) + (zzcmf == null ? 0 : zzcmf.hashCode());
        zzcmf zzcmf2 = this.zzjle;
        int hashCode3 = ((((hashCode2 * 31) + (zzcmf2 == null ? 0 : zzcmf2.hashCode())) * 31) + (this.zzjlf == null ? 0 : this.zzjlf.hashCode())) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode3 + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        zzcmf zzcmf;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt != 8) {
                if (zzcvt == 18) {
                    if (this.zzjld == null) {
                        this.zzjld = new zzcmf();
                    }
                    zzcmf = this.zzjld;
                } else if (zzcvt == 26) {
                    if (this.zzjle == null) {
                        this.zzjle = new zzcmf();
                    }
                    zzcmf = this.zzjle;
                } else if (zzcvt == 32) {
                    this.zzjlf = Boolean.valueOf(zzfjj.zzcvz());
                } else if (!super.zza(zzfjj, zzcvt)) {
                    return this;
                }
                zzfjj.zza(zzcmf);
            } else {
                this.zzjjs = Integer.valueOf(zzfjj.zzcwi());
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjjs != null) {
            zzfjk.zzaa(1, this.zzjjs.intValue());
        }
        if (this.zzjld != null) {
            zzfjk.zza(2, (zzfjs) this.zzjld);
        }
        if (this.zzjle != null) {
            zzfjk.zza(3, (zzfjs) this.zzjle);
        }
        if (this.zzjlf != null) {
            zzfjk.zzl(4, this.zzjlf.booleanValue());
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjjs != null) {
            zzq += zzfjk.zzad(1, this.zzjjs.intValue());
        }
        if (this.zzjld != null) {
            zzq += zzfjk.zzb(2, (zzfjs) this.zzjld);
        }
        if (this.zzjle != null) {
            zzq += zzfjk.zzb(3, (zzfjs) this.zzjle);
        }
        if (this.zzjlf == null) {
            return zzq;
        }
        this.zzjlf.booleanValue();
        return zzq + zzfjk.zzlg(4) + 1;
    }
}
