package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.internal.zzj;
import java.util.Iterator;
import java.util.Map;

final class zzar extends zzay {
    final /* synthetic */ zzao zzfrl;
    private final Map<zze, zzaq> zzfrn;

    public zzar(zzao zzao, Map<zze, zzaq> map) {
        this.zzfrl = zzao;
        super(zzao, null);
        this.zzfrn = map;
    }

    @WorkerThread
    public final void zzaib() {
        boolean z;
        Iterator it = this.zzfrn.keySet().iterator();
        boolean z2 = true;
        int i = 0;
        boolean z3 = false;
        boolean z4 = true;
        while (true) {
            if (!it.hasNext()) {
                z2 = z3;
                z = false;
                break;
            }
            zze zze = (zze) it.next();
            if (!zze.zzagg()) {
                z4 = false;
            } else if (!((zzaq) this.zzfrn.get(zze)).zzfpg) {
                z = true;
                break;
            } else {
                z3 = true;
            }
        }
        if (z2) {
            i = this.zzfrl.zzfqc.isGooglePlayServicesAvailable(this.zzfrl.mContext);
        }
        if (i == 0 || (!z && !z4)) {
            if (this.zzfrl.zzfrf) {
                this.zzfrl.zzfrd.connect();
            }
            for (zze zze2 : this.zzfrn.keySet()) {
                zzj zzj = (zzj) this.zzfrn.get(zze2);
                if (!zze2.zzagg() || i == 0) {
                    zze2.zza(zzj);
                } else {
                    this.zzfrl.zzfqv.zza((zzbj) new zzat(this, this.zzfrl, zzj));
                }
            }
            return;
        }
        this.zzfrl.zzfqv.zza((zzbj) new zzas(this, this.zzfrl, new ConnectionResult(i, null)));
    }
}
