package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class zzeau<K, V> extends zzeag<K, V> {
    private Comparator<K> zzmma;
    private zzeap<K, V> zzmmr;

    private zzeau(zzeap<K, V> zzeap, Comparator<K> comparator) {
        this.zzmmr = zzeap;
        this.zzmma = comparator;
    }

    public static <A, B> zzeau<A, B> zzb(Map<A, B> map, Comparator<A> comparator) {
        return zzeaw.zzc(new ArrayList(map.keySet()), map, zzeah.zzbtj(), comparator);
    }

    private final zzeap<K, V> zzbo(K k) {
        zzeap<K, V> zzeap = this.zzmmr;
        while (!zzeap.isEmpty()) {
            int compare = this.zzmma.compare(k, zzeap.getKey());
            if (compare < 0) {
                zzeap = zzeap.zzbtp();
            } else if (compare == 0) {
                return zzeap;
            } else {
                zzeap = zzeap.zzbtq();
            }
        }
        return null;
    }

    public final boolean containsKey(K k) {
        return zzbo(k) != null;
    }

    public final V get(K k) {
        zzeap zzbo = zzbo(k);
        if (zzbo != null) {
            return zzbo.getValue();
        }
        return null;
    }

    public final Comparator<K> getComparator() {
        return this.zzmma;
    }

    public final int indexOf(K k) {
        zzeap<K, V> zzeap = this.zzmmr;
        int i = 0;
        while (!zzeap.isEmpty()) {
            int compare = this.zzmma.compare(k, zzeap.getKey());
            if (compare == 0) {
                return i + zzeap.zzbtp().size();
            }
            if (compare < 0) {
                zzeap = zzeap.zzbtp();
            } else {
                i += zzeap.zzbtp().size() + 1;
                zzeap = zzeap.zzbtq();
            }
        }
        return -1;
    }

    public final boolean isEmpty() {
        return this.zzmmr.isEmpty();
    }

    public final Iterator<Entry<K, V>> iterator() {
        return new zzeak(this.zzmmr, null, this.zzmma, false);
    }

    public final int size() {
        return this.zzmmr.size();
    }

    public final void zza(zzear<K, V> zzear) {
        this.zzmmr.zza(zzear);
    }

    public final zzeag<K, V> zzbf(K k) {
        return !containsKey(k) ? this : new zzeau(this.zzmmr.zza(k, this.zzmma).zza(null, null, zzeaq.zzmmm, null, null), this.zzmma);
    }

    public final Iterator<Entry<K, V>> zzbg(K k) {
        return new zzeak(this.zzmmr, k, this.zzmma, false);
    }

    public final K zzbh(K k) {
        zzeap<K, V> zzeap = this.zzmmr;
        zzeap<K, V> zzeap2 = null;
        while (!zzeap.isEmpty()) {
            int compare = this.zzmma.compare(k, zzeap.getKey());
            if (compare == 0) {
                if (!zzeap.zzbtp().isEmpty()) {
                    zzeap zzbtp = zzeap.zzbtp();
                    while (!zzbtp.zzbtq().isEmpty()) {
                        zzbtp = zzbtp.zzbtq();
                    }
                    return zzbtp.getKey();
                } else if (zzeap2 != null) {
                    return zzeap2.getKey();
                } else {
                    return null;
                }
            } else if (compare < 0) {
                zzeap = zzeap.zzbtp();
            } else {
                zzeap2 = zzeap;
                zzeap = zzeap.zzbtq();
            }
        }
        String valueOf = String.valueOf(k);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 50);
        sb.append("Couldn't find predecessor key of non-present key: ");
        sb.append(valueOf);
        throw new IllegalArgumentException(sb.toString());
    }

    public final K zzbtg() {
        return this.zzmmr.zzbtr().getKey();
    }

    public final K zzbth() {
        return this.zzmmr.zzbts().getKey();
    }

    public final Iterator<Entry<K, V>> zzbti() {
        return new zzeak(this.zzmmr, null, this.zzmma, true);
    }

    public final zzeag<K, V> zzg(K k, V v) {
        return new zzeau(this.zzmmr.zza(k, v, this.zzmma).zza(null, null, zzeaq.zzmmm, null, null), this.zzmma);
    }
}
