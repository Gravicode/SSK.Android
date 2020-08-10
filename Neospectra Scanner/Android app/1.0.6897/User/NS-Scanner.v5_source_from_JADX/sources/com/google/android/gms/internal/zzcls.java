package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcls extends zzfjm<zzcls> {
    private static volatile zzcls[] zzjjv;
    public Integer zzjjw;
    public String zzjjx;
    public zzclt[] zzjjy;
    private Boolean zzjjz;
    public zzclu zzjka;

    public zzcls() {
        this.zzjjw = null;
        this.zzjjx = null;
        this.zzjjy = zzclt.zzbbc();
        this.zzjjz = null;
        this.zzjka = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzcls[] zzbbb() {
        if (zzjjv == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjjv == null) {
                    zzjjv = new zzcls[0];
                }
            }
        }
        return zzjjv;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcls)) {
            return false;
        }
        zzcls zzcls = (zzcls) obj;
        if (this.zzjjw == null) {
            if (zzcls.zzjjw != null) {
                return false;
            }
        } else if (!this.zzjjw.equals(zzcls.zzjjw)) {
            return false;
        }
        if (this.zzjjx == null) {
            if (zzcls.zzjjx != null) {
                return false;
            }
        } else if (!this.zzjjx.equals(zzcls.zzjjx)) {
            return false;
        }
        if (!zzfjq.equals((Object[]) this.zzjjy, (Object[]) zzcls.zzjjy)) {
            return false;
        }
        if (this.zzjjz == null) {
            if (zzcls.zzjjz != null) {
                return false;
            }
        } else if (!this.zzjjz.equals(zzcls.zzjjz)) {
            return false;
        }
        if (this.zzjka == null) {
            if (zzcls.zzjka != null) {
                return false;
            }
        } else if (!this.zzjka.equals(zzcls.zzjka)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcls.zzpnc == null || zzcls.zzpnc.isEmpty() : this.zzpnc.equals(zzcls.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzjjw == null ? 0 : this.zzjjw.hashCode())) * 31) + (this.zzjjx == null ? 0 : this.zzjjx.hashCode())) * 31) + zzfjq.hashCode((Object[]) this.zzjjy)) * 31) + (this.zzjjz == null ? 0 : this.zzjjz.hashCode());
        zzclu zzclu = this.zzjka;
        int hashCode2 = ((hashCode * 31) + (zzclu == null ? 0 : zzclu.hashCode())) * 31;
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
                this.zzjjx = zzfjj.readString();
            } else if (zzcvt == 26) {
                int zzb = zzfjv.zzb(zzfjj, 26);
                int length = this.zzjjy == null ? 0 : this.zzjjy.length;
                zzclt[] zzcltArr = new zzclt[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjjy, 0, zzcltArr, 0, length);
                }
                while (length < zzcltArr.length - 1) {
                    zzcltArr[length] = new zzclt();
                    zzfjj.zza(zzcltArr[length]);
                    zzfjj.zzcvt();
                    length++;
                }
                zzcltArr[length] = new zzclt();
                zzfjj.zza(zzcltArr[length]);
                this.zzjjy = zzcltArr;
            } else if (zzcvt == 32) {
                this.zzjjz = Boolean.valueOf(zzfjj.zzcvz());
            } else if (zzcvt == 42) {
                if (this.zzjka == null) {
                    this.zzjka = new zzclu();
                }
                zzfjj.zza(this.zzjka);
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjjw != null) {
            zzfjk.zzaa(1, this.zzjjw.intValue());
        }
        if (this.zzjjx != null) {
            zzfjk.zzn(2, this.zzjjx);
        }
        if (this.zzjjy != null && this.zzjjy.length > 0) {
            for (zzclt zzclt : this.zzjjy) {
                if (zzclt != null) {
                    zzfjk.zza(3, (zzfjs) zzclt);
                }
            }
        }
        if (this.zzjjz != null) {
            zzfjk.zzl(4, this.zzjjz.booleanValue());
        }
        if (this.zzjka != null) {
            zzfjk.zza(5, (zzfjs) this.zzjka);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjjw != null) {
            zzq += zzfjk.zzad(1, this.zzjjw.intValue());
        }
        if (this.zzjjx != null) {
            zzq += zzfjk.zzo(2, this.zzjjx);
        }
        if (this.zzjjy != null && this.zzjjy.length > 0) {
            for (zzclt zzclt : this.zzjjy) {
                if (zzclt != null) {
                    zzq += zzfjk.zzb(3, (zzfjs) zzclt);
                }
            }
        }
        if (this.zzjjz != null) {
            this.zzjjz.booleanValue();
            zzq += zzfjk.zzlg(4) + 1;
        }
        return this.zzjka != null ? zzq + zzfjk.zzb(5, (zzfjs) this.zzjka) : zzq;
    }
}
