package com.google.android.gms.common.api.internal;

import java.lang.ref.WeakReference;

final class zzbg extends zzby {
    private WeakReference<zzba> zzfsn;

    zzbg(zzba zzba) {
        this.zzfsn = new WeakReference<>(zzba);
    }

    public final void zzahg() {
        zzba zzba = (zzba) this.zzfsn.get();
        if (zzba != null) {
            zzba.resume();
        }
    }
}
