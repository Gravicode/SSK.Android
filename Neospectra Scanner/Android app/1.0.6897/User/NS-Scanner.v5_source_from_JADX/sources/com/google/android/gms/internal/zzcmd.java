package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcmd extends zzfjm<zzcmd> {
    public zzcme[] zzjlm;

    public zzcmd() {
        this.zzjlm = zzcme.zzbbj();
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcmd)) {
            return false;
        }
        zzcmd zzcmd = (zzcmd) obj;
        if (!zzfjq.equals((Object[]) this.zzjlm, (Object[]) zzcmd.zzjlm)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcmd.zzpnc == null || zzcmd.zzpnc.isEmpty() : this.zzpnc.equals(zzcmd.zzpnc);
    }

    public final int hashCode() {
        return ((((getClass().getName().hashCode() + 527) * 31) + zzfjq.hashCode((Object[]) this.zzjlm)) * 31) + ((this.zzpnc == null || this.zzpnc.isEmpty()) ? 0 : this.zzpnc.hashCode());
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                int zzb = zzfjv.zzb(zzfjj, 10);
                int length = this.zzjlm == null ? 0 : this.zzjlm.length;
                zzcme[] zzcmeArr = new zzcme[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzjlm, 0, zzcmeArr, 0, length);
                }
                while (length < zzcmeArr.length - 1) {
                    zzcmeArr[length] = new zzcme();
                    zzfjj.zza(zzcmeArr[length]);
                    zzfjj.zzcvt();
                    length++;
                }
                zzcmeArr[length] = new zzcme();
                zzfjj.zza(zzcmeArr[length]);
                this.zzjlm = zzcmeArr;
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjlm != null && this.zzjlm.length > 0) {
            for (zzcme zzcme : this.zzjlm) {
                if (zzcme != null) {
                    zzfjk.zza(1, (zzfjs) zzcme);
                }
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjlm != null && this.zzjlm.length > 0) {
            for (zzcme zzcme : this.zzjlm) {
                if (zzcme != null) {
                    zzq += zzfjk.zzb(1, (zzfjs) zzcme);
                }
            }
        }
        return zzq;
    }
}
