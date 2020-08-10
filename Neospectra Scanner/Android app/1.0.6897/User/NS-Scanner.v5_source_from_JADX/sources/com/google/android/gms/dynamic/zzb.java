package com.google.android.gms.dynamic;

import java.util.Iterator;

final class zzb implements zzo<T> {
    private /* synthetic */ zza zzgwh;

    zzb(zza zza) {
        this.zzgwh = zza;
    }

    public final void zza(T t) {
        this.zzgwh.zzgwd = t;
        Iterator it = this.zzgwh.zzgwf.iterator();
        while (it.hasNext()) {
            ((zzi) it.next()).zzb(this.zzgwh.zzgwd);
        }
        this.zzgwh.zzgwf.clear();
        this.zzgwh.zzgwe = null;
    }
}
