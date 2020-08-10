package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzfhj<T> implements zzfhv<T> {
    private final zzfhe zzpiu;
    private final zzfin<?, ?> zzpiv;
    private final boolean zzpiw;
    private final zzffn<?> zzpix;

    private zzfhj(Class<T> cls, zzfin<?, ?> zzfin, zzffn<?> zzffn, zzfhe zzfhe) {
        this.zzpiv = zzfin;
        this.zzpiw = zzffn.zzh(cls);
        this.zzpix = zzffn;
        this.zzpiu = zzfhe;
    }

    static <T> zzfhj<T> zza(Class<T> cls, zzfin<?, ?> zzfin, zzffn<?> zzffn, zzfhe zzfhe) {
        return new zzfhj<>(cls, zzfin, zzffn, zzfhe);
    }

    public final void zza(T t, zzfji zzfji) {
        int zzhq;
        Object value;
        Iterator it = this.zzpix.zzcn(t).iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            zzffs zzffs = (zzffs) entry.getKey();
            if (zzffs.zzcxi() != zzfjd.MESSAGE || zzffs.zzcxj() || zzffs.zzcxk()) {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
            if (entry instanceof zzfgi) {
                zzhq = zzffs.zzhq();
                value = ((zzfgi) entry).zzcyk().toByteString();
            } else {
                zzhq = zzffs.zzhq();
                value = entry.getValue();
            }
            zzfji.zzb(zzhq, value);
        }
        zzfin<?, ?> zzfin = this.zzpiv;
        zzfin.zzb(zzfin.zzcq(t), zzfji);
    }

    public final int zzcp(T t) {
        zzfin<?, ?> zzfin = this.zzpiv;
        int zzcr = zzfin.zzcr(zzfin.zzcq(t)) + 0;
        return this.zzpiw ? zzcr + this.zzpix.zzcn(t).zzcxg() : zzcr;
    }
}
