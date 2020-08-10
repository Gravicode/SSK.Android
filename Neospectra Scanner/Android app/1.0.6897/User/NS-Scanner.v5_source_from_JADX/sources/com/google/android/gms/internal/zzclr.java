package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclr extends zzfjm<zzclr> {
    private static volatile zzclr[] zzjjr;
    public Integer zzjjs;
    public zzclv[] zzjjt;
    public zzcls[] zzjju;

    public zzclr() {
        this.zzjjs = null;
        this.zzjjt = zzclv.zzbbd();
        this.zzjju = zzcls.zzbbb();
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzclr[] zzbba() {
        if (zzjjr == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjjr == null) {
                    zzjjr = new zzclr[0];
                }
            }
        }
        return zzjjr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclr)) {
            return false;
        }
        zzclr zzclr = (zzclr) obj;
        if (this.zzjjs == null) {
            if (zzclr.zzjjs != null) {
                return false;
            }
        } else if (!this.zzjjs.equals(zzclr.zzjjs)) {
            return false;
        }
        if (zzfjq.equals((Object[]) this.zzjjt, (Object[]) zzclr.zzjjt) && zzfjq.equals((Object[]) this.zzjju, (Object[]) zzclr.zzjju)) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclr.zzpnc == null || zzclr.zzpnc.isEmpty() : this.zzpnc.equals(zzclr.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((getClass().getName().hashCode() + 527) * 31) + (this.zzjjs == null ? 0 : this.zzjjs.hashCode())) * 31) + zzfjq.hashCode((Object[]) this.zzjjt)) * 31) + zzfjq.hashCode((Object[]) this.zzjju)) * 31;
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
                this.zzjjs = Integer.valueOf(zzfjj.zzcwi());
            } else if (zzcvt == 18) {
                int zzb = zzfjv.zzb(zzfjj, 18);
                int length = this.zzjjt == null ? 0 : this.zzjjt.length;
                zzclv[] zzclvArr = new zzclv[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjjt, 0, zzclvArr, 0, length);
                }
                while (length < zzclvArr.length - 1) {
                    zzclvArr[length] = new zzclv();
                    zzfjj.zza(zzclvArr[length]);
                    zzfjj.zzcvt();
                    length++;
                }
                zzclvArr[length] = new zzclv();
                zzfjj.zza(zzclvArr[length]);
                this.zzjjt = zzclvArr;
            } else if (zzcvt == 26) {
                int zzb2 = zzfjv.zzb(zzfjj, 26);
                int length2 = this.zzjju == null ? 0 : this.zzjju.length;
                zzcls[] zzclsArr = new zzcls[(zzb2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.zzjju, 0, zzclsArr, 0, length2);
                }
                while (length2 < zzclsArr.length - 1) {
                    zzclsArr[length2] = new zzcls();
                    zzfjj.zza(zzclsArr[length2]);
                    zzfjj.zzcvt();
                    length2++;
                }
                zzclsArr[length2] = new zzcls();
                zzfjj.zza(zzclsArr[length2]);
                this.zzjju = zzclsArr;
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjjs != null) {
            zzfjk.zzaa(1, this.zzjjs.intValue());
        }
        if (this.zzjjt != null && this.zzjjt.length > 0) {
            for (zzclv zzclv : this.zzjjt) {
                if (zzclv != null) {
                    zzfjk.zza(2, (zzfjs) zzclv);
                }
            }
        }
        if (this.zzjju != null && this.zzjju.length > 0) {
            for (zzcls zzcls : this.zzjju) {
                if (zzcls != null) {
                    zzfjk.zza(3, (zzfjs) zzcls);
                }
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjjs != null) {
            zzq += zzfjk.zzad(1, this.zzjjs.intValue());
        }
        if (this.zzjjt != null && this.zzjjt.length > 0) {
            int i = zzq;
            for (zzclv zzclv : this.zzjjt) {
                if (zzclv != null) {
                    i += zzfjk.zzb(2, (zzfjs) zzclv);
                }
            }
            zzq = i;
        }
        if (this.zzjju != null && this.zzjju.length > 0) {
            for (zzcls zzcls : this.zzjju) {
                if (zzcls != null) {
                    zzq += zzfjk.zzb(3, (zzfjs) zzcls);
                }
            }
        }
        return zzq;
    }
}
