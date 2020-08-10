package com.google.android.gms.internal;

import java.util.Iterator;

final class zzeay implements Iterator<zzeaz> {
    private int zzmmw = (this.zzmmx.length - 1);
    private /* synthetic */ zzeax zzmmx;

    zzeay(zzeax zzeax) {
        this.zzmmx = zzeax;
    }

    public final boolean hasNext() {
        return this.zzmmw >= 0;
    }

    public final /* synthetic */ Object next() {
        long zzb = this.zzmmx.value & ((long) (1 << this.zzmmw));
        zzeaz zzeaz = new zzeaz();
        zzeaz.zzmmy = zzb == 0;
        zzeaz.zzmmz = (int) Math.pow(2.0d, (double) this.zzmmw);
        this.zzmmw--;
        return zzeaz;
    }

    public final void remove() {
    }
}
