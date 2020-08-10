package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcmc extends zzfjm<zzcmc> {
    private static volatile zzcmc[] zzjlk;
    public String name;
    public String zzgcc;
    private Float zzjjk;
    public Double zzjjl;
    public Long zzjll;

    public zzcmc() {
        this.name = null;
        this.zzgcc = null;
        this.zzjll = null;
        this.zzjjk = null;
        this.zzjjl = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzcmc[] zzbbi() {
        if (zzjlk == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjlk == null) {
                    zzjlk = new zzcmc[0];
                }
            }
        }
        return zzjlk;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcmc)) {
            return false;
        }
        zzcmc zzcmc = (zzcmc) obj;
        if (this.name == null) {
            if (zzcmc.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzcmc.name)) {
            return false;
        }
        if (this.zzgcc == null) {
            if (zzcmc.zzgcc != null) {
                return false;
            }
        } else if (!this.zzgcc.equals(zzcmc.zzgcc)) {
            return false;
        }
        if (this.zzjll == null) {
            if (zzcmc.zzjll != null) {
                return false;
            }
        } else if (!this.zzjll.equals(zzcmc.zzjll)) {
            return false;
        }
        if (this.zzjjk == null) {
            if (zzcmc.zzjjk != null) {
                return false;
            }
        } else if (!this.zzjjk.equals(zzcmc.zzjjk)) {
            return false;
        }
        if (this.zzjjl == null) {
            if (zzcmc.zzjjl != null) {
                return false;
            }
        } else if (!this.zzjjl.equals(zzcmc.zzjjl)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcmc.zzpnc == null || zzcmc.zzpnc.isEmpty() : this.zzpnc.equals(zzcmc.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzgcc == null ? 0 : this.zzgcc.hashCode())) * 31) + (this.zzjll == null ? 0 : this.zzjll.hashCode())) * 31) + (this.zzjjk == null ? 0 : this.zzjjk.hashCode())) * 31) + (this.zzjjl == null ? 0 : this.zzjjl.hashCode())) * 31;
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
            } else if (zzcvt == 18) {
                this.zzgcc = zzfjj.readString();
            } else if (zzcvt == 24) {
                this.zzjll = Long.valueOf(zzfjj.zzcwn());
            } else if (zzcvt == 37) {
                this.zzjjk = Float.valueOf(Float.intBitsToFloat(zzfjj.zzcwo()));
            } else if (zzcvt == 41) {
                this.zzjjl = Double.valueOf(Double.longBitsToDouble(zzfjj.zzcwp()));
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.name != null) {
            zzfjk.zzn(1, this.name);
        }
        if (this.zzgcc != null) {
            zzfjk.zzn(2, this.zzgcc);
        }
        if (this.zzjll != null) {
            zzfjk.zzf(3, this.zzjll.longValue());
        }
        if (this.zzjjk != null) {
            zzfjk.zzc(4, this.zzjjk.floatValue());
        }
        if (this.zzjjl != null) {
            zzfjk.zza(5, this.zzjjl.doubleValue());
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.name != null) {
            zzq += zzfjk.zzo(1, this.name);
        }
        if (this.zzgcc != null) {
            zzq += zzfjk.zzo(2, this.zzgcc);
        }
        if (this.zzjll != null) {
            zzq += zzfjk.zzc(3, this.zzjll.longValue());
        }
        if (this.zzjjk != null) {
            this.zzjjk.floatValue();
            zzq += zzfjk.zzlg(4) + 4;
        }
        if (this.zzjjl == null) {
            return zzq;
        }
        this.zzjjl.doubleValue();
        return zzq + zzfjk.zzlg(5) + 8;
    }
}
