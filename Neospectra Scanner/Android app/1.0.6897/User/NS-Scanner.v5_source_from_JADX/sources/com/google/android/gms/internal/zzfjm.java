package com.google.android.gms.internal;

import com.google.android.gms.internal.zzfjm;
import java.io.IOException;

public abstract class zzfjm<M extends zzfjm<M>> extends zzfjs {
    protected zzfjo zzpnc;

    public final <T> T zza(zzfjn<M, T> zzfjn) {
        if (this.zzpnc == null) {
            return null;
        }
        zzfjp zzmj = this.zzpnc.zzmj(zzfjn.tag >>> 3);
        if (zzmj == null) {
            return null;
        }
        return zzmj.zzb(zzfjn);
    }

    public void zza(zzfjk zzfjk) throws IOException {
        if (this.zzpnc != null) {
            for (int i = 0; i < this.zzpnc.size(); i++) {
                this.zzpnc.zzmk(i).zza(zzfjk);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final boolean zza(zzfjj zzfjj, int i) throws IOException {
        int position = zzfjj.getPosition();
        if (!zzfjj.zzkq(i)) {
            return false;
        }
        int i2 = i >>> 3;
        zzfju zzfju = new zzfju(i, zzfjj.zzal(position, zzfjj.getPosition() - position));
        zzfjp zzfjp = null;
        if (this.zzpnc == null) {
            this.zzpnc = new zzfjo();
        } else {
            zzfjp = this.zzpnc.zzmj(i2);
        }
        if (zzfjp == null) {
            zzfjp = new zzfjp();
            this.zzpnc.zza(i2, zzfjp);
        }
        zzfjp.zza(zzfju);
        return true;
    }

    /* renamed from: zzdaf */
    public M clone() throws CloneNotSupportedException {
        M m = (zzfjm) super.clone();
        zzfjq.zza(this, (zzfjm) m);
        return m;
    }

    public /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfjm) clone();
    }

    /* access modifiers changed from: protected */
    public int zzq() {
        if (this.zzpnc == null) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < this.zzpnc.size(); i2++) {
            i += this.zzpnc.zzmk(i2).zzq();
        }
        return i;
    }
}
