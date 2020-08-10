package com.google.android.gms.common.api.internal;

import android.support.annotation.BinderThread;
import com.google.android.gms.internal.zzcxi;
import com.google.android.gms.internal.zzcxq;
import java.lang.ref.WeakReference;

final class zzav extends zzcxi {
    private final WeakReference<zzao> zzfrm;

    zzav(zzao zzao) {
        this.zzfrm = new WeakReference<>(zzao);
    }

    @BinderThread
    public final void zzb(zzcxq zzcxq) {
        zzao zzao = (zzao) this.zzfrm.get();
        if (zzao != null) {
            zzao.zzfqv.zza((zzbj) new zzaw(this, zzao, zzao, zzcxq));
        }
    }
}
