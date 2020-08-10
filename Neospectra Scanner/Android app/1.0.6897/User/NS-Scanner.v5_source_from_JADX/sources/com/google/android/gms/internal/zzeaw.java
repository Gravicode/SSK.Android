package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class zzeaw<A, B, C> {
    private final Map<B, C> values;
    private final List<A> zzmms;
    private final zzeaj<A, B> zzmmt;
    private zzeat<A, C> zzmmu;
    private zzeat<A, C> zzmmv;

    private zzeaw(List<A> list, Map<B, C> map, zzeaj<A, B> zzeaj) {
        this.zzmms = list;
        this.values = map;
        this.zzmmt = zzeaj;
    }

    private final C zzbp(A a) {
        return this.values.get(this.zzmmt.zzbk(a));
    }

    public static <A, B, C> zzeau<A, C> zzc(List<A> list, Map<B, C> map, zzeaj<A, B> zzeaj, Comparator<A> comparator) {
        int i;
        zzeaw zzeaw = new zzeaw(list, map, zzeaj);
        Collections.sort(list, comparator);
        Iterator it = new zzeax(list.size()).iterator();
        int size = list.size();
        while (it.hasNext()) {
            zzeaz zzeaz = (zzeaz) it.next();
            size -= zzeaz.zzmmz;
            if (zzeaz.zzmmy) {
                i = zzeaq.zzmmm;
            } else {
                zzeaw.zzf(zzeaq.zzmmm, zzeaz.zzmmz, size);
                size -= zzeaz.zzmmz;
                i = zzeaq.zzmml;
            }
            zzeaw.zzf(i, zzeaz.zzmmz, size);
        }
        return new zzeau<>(zzeaw.zzmmu == null ? zzeao.zzbto() : zzeaw.zzmmu, comparator);
    }

    private final void zzf(int i, int i2, int i3) {
        zzeap zzu = zzu(i3 + 1, i2 - 1);
        Object obj = this.zzmms.get(i3);
        zzeat<A, C> zzeas = i == zzeaq.zzmml ? new zzeas<>(obj, zzbp(obj), null, zzu) : new zzean<>(obj, zzbp(obj), null, zzu);
        if (this.zzmmu == null) {
            this.zzmmu = zzeas;
        } else {
            this.zzmmv.zza((zzeap<K, V>) zzeas);
        }
        this.zzmmv = zzeas;
    }

    private final zzeap<A, C> zzu(int i, int i2) {
        if (i2 == 0) {
            return zzeao.zzbto();
        }
        if (i2 == 1) {
            Object obj = this.zzmms.get(i);
            return new zzean(obj, zzbp(obj), null, null);
        }
        int i3 = i2 / 2;
        int i4 = i + i3;
        zzeap zzu = zzu(i, i3);
        zzeap zzu2 = zzu(i4 + 1, i3);
        Object obj2 = this.zzmms.get(i4);
        return new zzean(obj2, zzbp(obj2), zzu, zzu2);
    }
}
