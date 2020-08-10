package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzj;
import java.lang.ref.WeakReference;

final class zzaq implements zzj {
    private final Api<?> zzfin;
    /* access modifiers changed from: private */
    public final boolean zzfpg;
    private final WeakReference<zzao> zzfrm;

    public zzaq(zzao zzao, Api<?> api, boolean z) {
        this.zzfrm = new WeakReference<>(zzao);
        this.zzfin = api;
        this.zzfpg = z;
    }

    public final void zzf(@NonNull ConnectionResult connectionResult) {
        zzao zzao = (zzao) this.zzfrm.get();
        if (zzao != null) {
            zzbq.zza(Looper.myLooper() == zzao.zzfqv.zzfpi.getLooper(), (Object) "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zzao.zzfps.lock();
            try {
                if (zzao.zzbt(0)) {
                    if (!connectionResult.isSuccess()) {
                        zzao.zzb(connectionResult, this.zzfin, this.zzfpg);
                    }
                    if (zzao.zzaic()) {
                        zzao.zzaid();
                    }
                }
            } finally {
                zzao.zzfps.unlock();
            }
        }
    }
}
