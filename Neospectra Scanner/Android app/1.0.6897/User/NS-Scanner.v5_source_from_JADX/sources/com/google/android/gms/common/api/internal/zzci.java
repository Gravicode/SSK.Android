package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbq;

public final class zzci<L> {
    private final zzcj zzfuj;
    private volatile L zzfuk;
    private final zzck<L> zzful;

    zzci(@NonNull Looper looper, @NonNull L l, @NonNull String str) {
        this.zzfuj = new zzcj(this, looper);
        this.zzfuk = zzbq.checkNotNull(l, "Listener must not be null");
        this.zzful = new zzck<>(l, zzbq.zzgm(str));
    }

    public final void clear() {
        this.zzfuk = null;
    }

    public final void zza(zzcl<? super L> zzcl) {
        zzbq.checkNotNull(zzcl, "Notifier must not be null");
        this.zzfuj.sendMessage(this.zzfuj.obtainMessage(1, zzcl));
    }

    public final boolean zzaek() {
        return this.zzfuk != null;
    }

    @NonNull
    public final zzck<L> zzajo() {
        return this.zzful;
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(zzcl<? super L> zzcl) {
        L l = this.zzfuk;
        if (l == null) {
            zzcl.zzahz();
            return;
        }
        try {
            zzcl.zzu(l);
        } catch (RuntimeException e) {
            zzcl.zzahz();
            throw e;
        }
    }
}
