package com.google.android.gms.internal;

import android.content.SharedPreferences.Editor;
import android.support.annotation.WorkerThread;
import android.util.Pair;
import com.google.android.gms.common.internal.zzbq;

public final class zzcib {
    private final long zzdyr;
    private /* synthetic */ zzchx zzjdm;
    private String zzjdo;
    private final String zzjdp;
    private final String zzjdq;

    private zzcib(zzchx zzchx, String str, long j) {
        this.zzjdm = zzchx;
        zzbq.zzgm(str);
        zzbq.checkArgument(j > 0);
        this.zzjdo = String.valueOf(str).concat(":start");
        this.zzjdp = String.valueOf(str).concat(":count");
        this.zzjdq = String.valueOf(str).concat(":value");
        this.zzdyr = j;
    }

    @WorkerThread
    private final void zzaac() {
        this.zzjdm.zzve();
        long currentTimeMillis = this.zzjdm.zzws().currentTimeMillis();
        Editor edit = this.zzjdm.zzazl().edit();
        edit.remove(this.zzjdp);
        edit.remove(this.zzjdq);
        edit.putLong(this.zzjdo, currentTimeMillis);
        edit.apply();
    }

    @WorkerThread
    private final long zzaae() {
        return this.zzjdm.zzazl().getLong(this.zzjdo, 0);
    }

    @WorkerThread
    public final Pair<String, Long> zzaad() {
        long j;
        this.zzjdm.zzve();
        this.zzjdm.zzve();
        long zzaae = zzaae();
        if (zzaae == 0) {
            zzaac();
            j = 0;
        } else {
            j = Math.abs(zzaae - this.zzjdm.zzws().currentTimeMillis());
        }
        if (j < this.zzdyr) {
            return null;
        }
        if (j > (this.zzdyr << 1)) {
            zzaac();
            return null;
        }
        String string = this.zzjdm.zzazl().getString(this.zzjdq, null);
        long j2 = this.zzjdm.zzazl().getLong(this.zzjdp, 0);
        zzaac();
        return (string == null || j2 <= 0) ? zzchx.zzjcp : new Pair<>(string, Long.valueOf(j2));
    }

    @WorkerThread
    public final void zzf(String str, long j) {
        this.zzjdm.zzve();
        if (zzaae() == 0) {
            zzaac();
        }
        if (str == null) {
            str = "";
        }
        long j2 = this.zzjdm.zzazl().getLong(this.zzjdp, 0);
        if (j2 <= 0) {
            Editor edit = this.zzjdm.zzazl().edit();
            edit.putString(this.zzjdq, str);
            edit.putLong(this.zzjdp, 1);
            edit.apply();
            return;
        }
        long j3 = j2 + 1;
        boolean z = (this.zzjdm.zzawu().zzbaz().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / j3;
        Editor edit2 = this.zzjdm.zzazl().edit();
        if (z) {
            edit2.putString(this.zzjdq, str);
        }
        edit2.putLong(this.zzjdp, j3);
        edit2.apply();
    }
}
