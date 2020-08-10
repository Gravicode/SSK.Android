package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcmb extends zzfjm<zzcmb> {
    private static volatile zzcmb[] zzjlg;
    public Integer count;
    public String name;
    public zzcmc[] zzjlh;
    public Long zzjli;
    public Long zzjlj;

    public zzcmb() {
        this.zzjlh = zzcmc.zzbbi();
        this.name = null;
        this.zzjli = null;
        this.zzjlj = null;
        this.count = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzcmb[] zzbbh() {
        if (zzjlg == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjlg == null) {
                    zzjlg = new zzcmb[0];
                }
            }
        }
        return zzjlg;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcmb)) {
            return false;
        }
        zzcmb zzcmb = (zzcmb) obj;
        if (!zzfjq.equals((Object[]) this.zzjlh, (Object[]) zzcmb.zzjlh)) {
            return false;
        }
        if (this.name == null) {
            if (zzcmb.name != null) {
                return false;
            }
        } else if (!this.name.equals(zzcmb.name)) {
            return false;
        }
        if (this.zzjli == null) {
            if (zzcmb.zzjli != null) {
                return false;
            }
        } else if (!this.zzjli.equals(zzcmb.zzjli)) {
            return false;
        }
        if (this.zzjlj == null) {
            if (zzcmb.zzjlj != null) {
                return false;
            }
        } else if (!this.zzjlj.equals(zzcmb.zzjlj)) {
            return false;
        }
        if (this.count == null) {
            if (zzcmb.count != null) {
                return false;
            }
        } else if (!this.count.equals(zzcmb.count)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcmb.zzpnc == null || zzcmb.zzpnc.isEmpty() : this.zzpnc.equals(zzcmb.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((getClass().getName().hashCode() + 527) * 31) + zzfjq.hashCode((Object[]) this.zzjlh)) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzjli == null ? 0 : this.zzjli.hashCode())) * 31) + (this.zzjlj == null ? 0 : this.zzjlj.hashCode())) * 31) + (this.count == null ? 0 : this.count.hashCode())) * 31;
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
                int zzb = zzfjv.zzb(zzfjj, 10);
                int length = this.zzjlh == null ? 0 : this.zzjlh.length;
                zzcmc[] zzcmcArr = new zzcmc[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjlh, 0, zzcmcArr, 0, length);
                }
                while (length < zzcmcArr.length - 1) {
                    zzcmcArr[length] = new zzcmc();
                    zzfjj.zza(zzcmcArr[length]);
                    zzfjj.zzcvt();
                    length++;
                }
                zzcmcArr[length] = new zzcmc();
                zzfjj.zza(zzcmcArr[length]);
                this.zzjlh = zzcmcArr;
            } else if (zzcvt == 18) {
                this.name = zzfjj.readString();
            } else if (zzcvt == 24) {
                this.zzjli = Long.valueOf(zzfjj.zzcwn());
            } else if (zzcvt == 32) {
                this.zzjlj = Long.valueOf(zzfjj.zzcwn());
            } else if (zzcvt == 40) {
                this.count = Integer.valueOf(zzfjj.zzcwi());
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjlh != null && this.zzjlh.length > 0) {
            for (zzcmc zzcmc : this.zzjlh) {
                if (zzcmc != null) {
                    zzfjk.zza(1, (zzfjs) zzcmc);
                }
            }
        }
        if (this.name != null) {
            zzfjk.zzn(2, this.name);
        }
        if (this.zzjli != null) {
            zzfjk.zzf(3, this.zzjli.longValue());
        }
        if (this.zzjlj != null) {
            zzfjk.zzf(4, this.zzjlj.longValue());
        }
        if (this.count != null) {
            zzfjk.zzaa(5, this.count.intValue());
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjlh != null && this.zzjlh.length > 0) {
            for (zzcmc zzcmc : this.zzjlh) {
                if (zzcmc != null) {
                    zzq += zzfjk.zzb(1, (zzfjs) zzcmc);
                }
            }
        }
        if (this.name != null) {
            zzq += zzfjk.zzo(2, this.name);
        }
        if (this.zzjli != null) {
            zzq += zzfjk.zzc(3, this.zzjli.longValue());
        }
        if (this.zzjlj != null) {
            zzq += zzfjk.zzc(4, this.zzjlj.longValue());
        }
        return this.count != null ? zzq + zzfjk.zzad(5, this.count.intValue()) : zzq;
    }
}
