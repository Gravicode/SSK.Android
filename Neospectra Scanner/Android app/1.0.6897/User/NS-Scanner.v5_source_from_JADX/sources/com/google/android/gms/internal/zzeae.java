package com.google.android.gms.internal;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class zzeae<K, V> extends zzeag<K, V> {
    /* access modifiers changed from: private */
    public final K[] zzmly;
    /* access modifiers changed from: private */
    public final V[] zzmlz;
    private final Comparator<K> zzmma;

    public zzeae(Comparator<K> comparator) {
        this.zzmly = new Object[0];
        this.zzmlz = new Object[0];
        this.zzmma = comparator;
    }

    private zzeae(Comparator<K> comparator, K[] kArr, V[] vArr) {
        this.zzmly = kArr;
        this.zzmlz = vArr;
        this.zzmma = comparator;
    }

    public static <A, B, C> zzeae<A, C> zza(List<A> list, Map<B, C> map, zzeaj<A, B> zzeaj, Comparator<A> comparator) {
        Collections.sort(list, comparator);
        int size = list.size();
        Object[] objArr = new Object[size];
        Object[] objArr2 = new Object[size];
        int i = 0;
        for (Object next : list) {
            objArr[i] = next;
            objArr2[i] = map.get(zzeaj.zzbk(next));
            i++;
        }
        return new zzeae<>(comparator, objArr, objArr2);
    }

    private static <T> T[] zza(T[] tArr, int i) {
        int length = tArr.length - 1;
        T[] tArr2 = new Object[length];
        System.arraycopy(tArr, 0, tArr2, 0, i);
        System.arraycopy(tArr, i + 1, tArr2, i, length - i);
        return tArr2;
    }

    private static <T> T[] zza(T[] tArr, int i, T t) {
        int length = tArr.length + 1;
        T[] tArr2 = new Object[length];
        System.arraycopy(tArr, 0, tArr2, 0, i);
        tArr2[i] = t;
        System.arraycopy(tArr, i, tArr2, i + 1, (length - i) - 1);
        return tArr2;
    }

    private static <T> T[] zzb(T[] tArr, int i, T t) {
        int length = tArr.length;
        T[] tArr2 = new Object[length];
        System.arraycopy(tArr, 0, tArr2, 0, length);
        tArr2[i] = t;
        return tArr2;
    }

    private final int zzbi(K k) {
        int i = 0;
        while (i < this.zzmly.length && this.zzmma.compare(this.zzmly[i], k) < 0) {
            i++;
        }
        return i;
    }

    private final int zzbj(K k) {
        int i = 0;
        for (K compare : this.zzmly) {
            if (this.zzmma.compare(k, compare) == 0) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private final Iterator<Entry<K, V>> zzj(int i, boolean z) {
        return new zzeaf(this, i, z);
    }

    public final boolean containsKey(K k) {
        return zzbj(k) != -1;
    }

    public final V get(K k) {
        int zzbj = zzbj(k);
        if (zzbj != -1) {
            return this.zzmlz[zzbj];
        }
        return null;
    }

    public final Comparator<K> getComparator() {
        return this.zzmma;
    }

    public final int indexOf(K k) {
        return zzbj(k);
    }

    public final boolean isEmpty() {
        return this.zzmly.length == 0;
    }

    public final Iterator<Entry<K, V>> iterator() {
        return zzj(0, false);
    }

    public final int size() {
        return this.zzmly.length;
    }

    public final void zza(zzear<K, V> zzear) {
        for (int i = 0; i < this.zzmly.length; i++) {
            zzear.zzh(this.zzmly[i], this.zzmlz[i]);
        }
    }

    public final zzeag<K, V> zzbf(K k) {
        int zzbj = zzbj(k);
        if (zzbj == -1) {
            return this;
        }
        return new zzeae(this.zzmma, zza(this.zzmly, zzbj), zza(this.zzmlz, zzbj));
    }

    public final Iterator<Entry<K, V>> zzbg(K k) {
        return zzj(zzbi(k), false);
    }

    public final K zzbh(K k) {
        int zzbj = zzbj(k);
        if (zzbj == -1) {
            throw new IllegalArgumentException("Can't find predecessor of nonexistent key");
        } else if (zzbj > 0) {
            return this.zzmly[zzbj - 1];
        } else {
            return null;
        }
    }

    public final K zzbtg() {
        if (this.zzmly.length > 0) {
            return this.zzmly[0];
        }
        return null;
    }

    public final K zzbth() {
        if (this.zzmly.length > 0) {
            return this.zzmly[this.zzmly.length - 1];
        }
        return null;
    }

    public final Iterator<Entry<K, V>> zzbti() {
        return zzj(this.zzmly.length - 1, true);
    }

    public final zzeag<K, V> zzg(K k, V v) {
        int zzbj = zzbj(k);
        if (zzbj != -1) {
            if (this.zzmly[zzbj] == k && this.zzmlz[zzbj] == v) {
                return this;
            }
            return new zzeae(this.zzmma, zzb(this.zzmly, zzbj, k), zzb(this.zzmlz, zzbj, v));
        } else if (this.zzmly.length > 25) {
            HashMap hashMap = new HashMap(this.zzmly.length + 1);
            for (int i = 0; i < this.zzmly.length; i++) {
                hashMap.put(this.zzmly[i], this.zzmlz[i]);
            }
            hashMap.put(k, v);
            return zzeau.zzb(hashMap, this.zzmma);
        } else {
            int zzbi = zzbi(k);
            return new zzeae(this.zzmma, zza(this.zzmly, zzbi, k), zza(this.zzmlz, zzbi, v));
        }
    }
}
