package com.google.android.gms.internal;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import org.apache.commons.math3.geometry.VectorFormat;

public abstract class zzeag<K, V> implements Iterable<Entry<K, V>> {
    public abstract boolean containsKey(K k);

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzeag)) {
            return false;
        }
        zzeag zzeag = (zzeag) obj;
        if (!getComparator().equals(zzeag.getComparator()) || size() != zzeag.size()) {
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = zzeag.iterator();
        while (it.hasNext()) {
            if (!((Entry) it.next()).equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    public abstract V get(K k);

    public abstract Comparator<K> getComparator();

    public int hashCode() {
        int hashCode = getComparator().hashCode();
        Iterator it = iterator();
        while (it.hasNext()) {
            hashCode = (hashCode * 31) + ((Entry) it.next()).hashCode();
        }
        return hashCode;
    }

    public abstract int indexOf(K k);

    public abstract boolean isEmpty();

    public abstract Iterator<Entry<K, V>> iterator();

    public abstract int size();

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(VectorFormat.DEFAULT_PREFIX);
        Iterator it = iterator();
        boolean z = true;
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (z) {
                z = false;
            } else {
                sb.append(", ");
            }
            sb.append("(");
            sb.append(entry.getKey());
            sb.append("=>");
            sb.append(entry.getValue());
            sb.append(")");
        }
        sb.append("};");
        return sb.toString();
    }

    public abstract void zza(zzear<K, V> zzear);

    public abstract zzeag<K, V> zzbf(K k);

    public abstract Iterator<Entry<K, V>> zzbg(K k);

    public abstract K zzbh(K k);

    public abstract K zzbtg();

    public abstract K zzbth();

    public abstract Iterator<Entry<K, V>> zzbti();

    public abstract zzeag<K, V> zzg(K k, V v);
}
