package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcly extends zzfjm<zzcly> {
    public String zzixs;
    public Long zzjkw;
    private Integer zzjkx;
    public zzclz[] zzjky;
    public zzclx[] zzjkz;
    public zzclr[] zzjla;

    public zzcly() {
        this.zzjkw = null;
        this.zzixs = null;
        this.zzjkx = null;
        this.zzjky = zzclz.zzbbf();
        this.zzjkz = zzclx.zzbbe();
        this.zzjla = zzclr.zzbba();
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcly)) {
            return false;
        }
        zzcly zzcly = (zzcly) obj;
        if (this.zzjkw == null) {
            if (zzcly.zzjkw != null) {
                return false;
            }
        } else if (!this.zzjkw.equals(zzcly.zzjkw)) {
            return false;
        }
        if (this.zzixs == null) {
            if (zzcly.zzixs != null) {
                return false;
            }
        } else if (!this.zzixs.equals(zzcly.zzixs)) {
            return false;
        }
        if (this.zzjkx == null) {
            if (zzcly.zzjkx != null) {
                return false;
            }
        } else if (!this.zzjkx.equals(zzcly.zzjkx)) {
            return false;
        }
        if (zzfjq.equals((Object[]) this.zzjky, (Object[]) zzcly.zzjky) && zzfjq.equals((Object[]) this.zzjkz, (Object[]) zzcly.zzjkz) && zzfjq.equals((Object[]) this.zzjla, (Object[]) zzcly.zzjla)) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcly.zzpnc == null || zzcly.zzpnc.isEmpty() : this.zzpnc.equals(zzcly.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzjkw == null ? 0 : this.zzjkw.hashCode())) * 31) + (this.zzixs == null ? 0 : this.zzixs.hashCode())) * 31) + (this.zzjkx == null ? 0 : this.zzjkx.hashCode())) * 31) + zzfjq.hashCode((Object[]) this.zzjky)) * 31) + zzfjq.hashCode((Object[]) this.zzjkz)) * 31) + zzfjq.hashCode((Object[]) this.zzjla)) * 31;
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
            if (zzcvt == 8) {
                this.zzjkw = Long.valueOf(zzfjj.zzcwn());
            } else if (zzcvt == 18) {
                this.zzixs = zzfjj.readString();
            } else if (zzcvt == 24) {
                this.zzjkx = Integer.valueOf(zzfjj.zzcwi());
            } else if (zzcvt == 34) {
                int zzb = zzfjv.zzb(zzfjj, 34);
                int length = this.zzjky == null ? 0 : this.zzjky.length;
                zzclz[] zzclzArr = new zzclz[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjky, 0, zzclzArr, 0, length);
                }
                while (length < zzclzArr.length - 1) {
                    zzclzArr[length] = new zzclz();
                    zzfjj.zza(zzclzArr[length]);
                    zzfjj.zzcvt();
                    length++;
                }
                zzclzArr[length] = new zzclz();
                zzfjj.zza(zzclzArr[length]);
                this.zzjky = zzclzArr;
            } else if (zzcvt == 42) {
                int zzb2 = zzfjv.zzb(zzfjj, 42);
                int length2 = this.zzjkz == null ? 0 : this.zzjkz.length;
                zzclx[] zzclxArr = new zzclx[(zzb2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.zzjkz, 0, zzclxArr, 0, length2);
                }
                while (length2 < zzclxArr.length - 1) {
                    zzclxArr[length2] = new zzclx();
                    zzfjj.zza(zzclxArr[length2]);
                    zzfjj.zzcvt();
                    length2++;
                }
                zzclxArr[length2] = new zzclx();
                zzfjj.zza(zzclxArr[length2]);
                this.zzjkz = zzclxArr;
            } else if (zzcvt == 50) {
                int zzb3 = zzfjv.zzb(zzfjj, 50);
                int length3 = this.zzjla == null ? 0 : this.zzjla.length;
                zzclr[] zzclrArr = new zzclr[(zzb3 + length3)];
                if (length3 != 0) {
                    System.arraycopy(this.zzjla, 0, zzclrArr, 0, length3);
                }
                while (length3 < zzclrArr.length - 1) {
                    zzclrArr[length3] = new zzclr();
                    zzfjj.zza(zzclrArr[length3]);
                    zzfjj.zzcvt();
                    length3++;
                }
                zzclrArr[length3] = new zzclr();
                zzfjj.zza(zzclrArr[length3]);
                this.zzjla = zzclrArr;
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjkw != null) {
            zzfjk.zzf(1, this.zzjkw.longValue());
        }
        if (this.zzixs != null) {
            zzfjk.zzn(2, this.zzixs);
        }
        if (this.zzjkx != null) {
            zzfjk.zzaa(3, this.zzjkx.intValue());
        }
        if (this.zzjky != null && this.zzjky.length > 0) {
            for (zzclz zzclz : this.zzjky) {
                if (zzclz != null) {
                    zzfjk.zza(4, (zzfjs) zzclz);
                }
            }
        }
        if (this.zzjkz != null && this.zzjkz.length > 0) {
            for (zzclx zzclx : this.zzjkz) {
                if (zzclx != null) {
                    zzfjk.zza(5, (zzfjs) zzclx);
                }
            }
        }
        if (this.zzjla != null && this.zzjla.length > 0) {
            for (zzclr zzclr : this.zzjla) {
                if (zzclr != null) {
                    zzfjk.zza(6, (zzfjs) zzclr);
                }
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjkw != null) {
            zzq += zzfjk.zzc(1, this.zzjkw.longValue());
        }
        if (this.zzixs != null) {
            zzq += zzfjk.zzo(2, this.zzixs);
        }
        if (this.zzjkx != null) {
            zzq += zzfjk.zzad(3, this.zzjkx.intValue());
        }
        if (this.zzjky != null && this.zzjky.length > 0) {
            int i = zzq;
            for (zzclz zzclz : this.zzjky) {
                if (zzclz != null) {
                    i += zzfjk.zzb(4, (zzfjs) zzclz);
                }
            }
            zzq = i;
        }
        if (this.zzjkz != null && this.zzjkz.length > 0) {
            int i2 = zzq;
            for (zzclx zzclx : this.zzjkz) {
                if (zzclx != null) {
                    i2 += zzfjk.zzb(5, (zzfjs) zzclx);
                }
            }
            zzq = i2;
        }
        if (this.zzjla != null && this.zzjla.length > 0) {
            for (zzclr zzclr : this.zzjla) {
                if (zzclr != null) {
                    zzq += zzfjk.zzb(6, (zzfjs) zzclr);
                }
            }
        }
        return zzq;
    }
}
