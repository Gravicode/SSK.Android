package com.google.android.gms.internal;

import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.Status;
import java.util.ArrayList;
import java.util.TimeZone;

public final class zzbea {
    private String zzfjb;
    private int zzfjc;
    private String zzfjd;
    private String zzfje;
    private int zzfjg;
    private final zzbec zzfjk;
    private ArrayList<Integer> zzfjl;
    private ArrayList<String> zzfjm;
    private ArrayList<Integer> zzfjn;
    private ArrayList<zzctx> zzfjo;
    private ArrayList<byte[]> zzfjp;
    private boolean zzfjq;
    private final zzfkq zzfjr;
    private boolean zzfjs;
    private /* synthetic */ zzbdy zzfjt;

    private zzbea(zzbdy zzbdy, byte[] bArr) {
        this(zzbdy, bArr, (zzbec) null);
    }

    /* synthetic */ zzbea(zzbdy zzbdy, byte[] bArr, zzbdz zzbdz) {
        this(zzbdy, bArr);
    }

    private zzbea(zzbdy zzbdy, byte[] bArr, zzbec zzbec) {
        this.zzfjt = zzbdy;
        this.zzfjc = this.zzfjt.zzfjc;
        this.zzfjb = this.zzfjt.zzfjb;
        zzbdy zzbdy2 = this.zzfjt;
        this.zzfjd = null;
        zzbdy zzbdy3 = this.zzfjt;
        this.zzfje = null;
        this.zzfjg = 0;
        this.zzfjl = null;
        this.zzfjm = null;
        this.zzfjn = null;
        this.zzfjo = null;
        this.zzfjp = null;
        this.zzfjq = true;
        this.zzfjr = new zzfkq();
        this.zzfjs = false;
        this.zzfjd = null;
        this.zzfje = null;
        this.zzfjr.zzpql = zzbdy.zzddz.currentTimeMillis();
        this.zzfjr.zzpqm = zzbdy.zzddz.elapsedRealtime();
        zzfkq zzfkq = this.zzfjr;
        zzbdy.zzfji;
        zzfkq.zzpqx = (long) (TimeZone.getDefault().getOffset(this.zzfjr.zzpql) / 1000);
        if (bArr != null) {
            this.zzfjr.zzpqs = bArr;
        }
        this.zzfjk = null;
    }

    public final void zzbk() {
        if (this.zzfjs) {
            throw new IllegalStateException("do not reuse LogEventBuilder");
        }
        this.zzfjs = true;
        zzbew zzbew = new zzbew(this.zzfjt.packageName, this.zzfjt.zzfja, this.zzfjc, this.zzfjb, this.zzfjd, this.zzfje, this.zzfjt.zzfjf, 0);
        zzbeh zzbeh = new zzbeh(zzbew, this.zzfjr, null, null, zzbdy.zzb(null), null, zzbdy.zzb(null), null, null, this.zzfjq);
        zzbew zzbew2 = zzbeh.zzfjx;
        if (this.zzfjt.zzfjj.zzg(zzbew2.zzfjb, zzbew2.zzfjc)) {
            this.zzfjt.zzfjh.zza(zzbeh);
        } else {
            PendingResults.immediatePendingResult(Status.zzfni);
        }
    }
}
