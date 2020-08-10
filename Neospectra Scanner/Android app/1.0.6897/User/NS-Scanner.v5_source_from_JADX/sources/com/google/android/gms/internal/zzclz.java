package com.google.android.gms.internal;

import java.io.IOException;

public final class zzclz extends zzfjm<zzclz> {
    private static volatile zzclz[] zzjlb;
    public String key;
    public String value;

    public zzclz() {
        this.key = null;
        this.value = null;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzclz[] zzbbf() {
        if (zzjlb == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzjlb == null) {
                    zzjlb = new zzclz[0];
                }
            }
        }
        return zzjlb;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzclz)) {
            return false;
        }
        zzclz zzclz = (zzclz) obj;
        if (this.key == null) {
            if (zzclz.key != null) {
                return false;
            }
        } else if (!this.key.equals(zzclz.key)) {
            return false;
        }
        if (this.value == null) {
            if (zzclz.value != null) {
                return false;
            }
        } else if (!this.value.equals(zzclz.value)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzclz.zzpnc == null || zzclz.zzpnc.isEmpty() : this.zzpnc.equals(zzclz.zzpnc);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((getClass().getName().hashCode() + 527) * 31) + (this.key == null ? 0 : this.key.hashCode())) * 31) + (this.value == null ? 0 : this.value.hashCode())) * 31;
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
                this.key = zzfjj.readString();
            } else if (zzcvt == 18) {
                this.value = zzfjj.readString();
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.key != null) {
            zzfjk.zzn(1, this.key);
        }
        if (this.value != null) {
            zzfjk.zzn(2, this.value);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.key != null) {
            zzq += zzfjk.zzo(1, this.key);
        }
        return this.value != null ? zzq + zzfjk.zzo(2, this.value) : zzq;
    }
}
