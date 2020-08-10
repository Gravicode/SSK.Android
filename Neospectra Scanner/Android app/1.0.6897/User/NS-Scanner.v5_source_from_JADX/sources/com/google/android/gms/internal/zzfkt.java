package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfkt extends zzfjm<zzfkt> {
    private static volatile zzfkt[] zzprh;
    public String zzpri;

    public zzfkt() {
        this.zzpri = "";
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public static zzfkt[] zzdbd() {
        if (zzprh == null) {
            synchronized (zzfjq.zzpnk) {
                if (zzprh == null) {
                    zzprh = new zzfkt[0];
                }
            }
        }
        return zzprh;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                this.zzpri = zzfjj.readString();
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzpri != null && !this.zzpri.equals("")) {
            zzfjk.zzn(1, this.zzpri);
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        return (this.zzpri == null || this.zzpri.equals("")) ? zzq : zzq + zzfjk.zzo(1, this.zzpri);
    }
}
