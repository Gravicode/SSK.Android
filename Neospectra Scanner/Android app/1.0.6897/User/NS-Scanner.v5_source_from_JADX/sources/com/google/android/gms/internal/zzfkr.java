package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfkr extends zzfjm<zzfkr> implements Cloneable {
    private static volatile zzfkr[] zzpre;
    private String key;
    private String value;

    public zzfkr() {
        this.key = "";
        this.value = "";
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzfkr[] zzdba() {
        if (zzpre == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzpre == null) {
                    zzpre = new zzfkr[0];
                }
            }
        }
        return zzpre;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzdbb */
    public zzfkr clone() {
        try {
            return (zzfkr) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfkr)) {
            return false;
        }
        zzfkr zzfkr = (zzfkr) obj;
        if (this.key == null) {
            if (zzfkr.key != null) {
                return false;
            }
        } else if (!this.key.equals(zzfkr.key)) {
            return false;
        }
        if (this.value == null) {
            if (zzfkr.value != null) {
                return false;
            }
        } else if (!this.value.equals(zzfkr.value)) {
            return false;
        }
        return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzfkr.zzpnc == null || zzfkr.zzpnc.isEmpty() : this.zzpnc.equals(zzfkr.zzpnc);
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
        if (this.key != null && !this.key.equals("")) {
            zzfjk.zzn(1, this.key);
        }
        if (this.value != null && !this.value.equals("")) {
            zzfjk.zzn(2, this.value);
        }
        super.zza(zzfjk);
    }

    public final /* synthetic */ zzfjm zzdaf() throws CloneNotSupportedException {
        return (zzfkr) clone();
    }

    public final /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfkr) clone();
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.key != null && !this.key.equals("")) {
            zzq += zzfjk.zzo(1, this.key);
        }
        return (this.value == null || this.value.equals("")) ? zzq : zzq + zzfjk.zzo(2, this.value);
    }
}
