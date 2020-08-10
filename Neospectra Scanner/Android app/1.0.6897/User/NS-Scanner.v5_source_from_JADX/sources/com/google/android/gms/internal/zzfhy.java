package com.google.android.gms.internal;

import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

class zzfhy<K extends Comparable<K>, V> extends AbstractMap<K, V> {
    private boolean zzktj;
    private final int zzpjz;
    /* access modifiers changed from: private */
    public List<zzfid> zzpka;
    /* access modifiers changed from: private */
    public Map<K, V> zzpkb;
    private volatile zzfif zzpkc;
    private Map<K, V> zzpkd;

    private zzfhy(int i) {
        this.zzpjz = i;
        this.zzpka = Collections.emptyList();
        this.zzpkb = Collections.emptyMap();
        this.zzpkd = Collections.emptyMap();
    }

    /* synthetic */ zzfhy(int i, zzfhz zzfhz) {
        this(i);
    }

    private final int zza(K k) {
        int size = this.zzpka.size() - 1;
        if (size >= 0) {
            int compareTo = k.compareTo((Comparable) ((zzfid) this.zzpka.get(size)).getKey());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i = 0;
        while (i <= size) {
            int i2 = (i + size) / 2;
            int compareTo2 = k.compareTo((Comparable) ((zzfid) this.zzpka.get(i2)).getKey());
            if (compareTo2 < 0) {
                size = i2 - 1;
            } else if (compareTo2 <= 0) {
                return i2;
            } else {
                i = i2 + 1;
            }
        }
        return -(i + 1);
    }

    /* access modifiers changed from: private */
    public final void zzczl() {
        if (this.zzktj) {
            throw new UnsupportedOperationException();
        }
    }

    private final SortedMap<K, V> zzczm() {
        zzczl();
        if (this.zzpkb.isEmpty() && !(this.zzpkb instanceof TreeMap)) {
            this.zzpkb = new TreeMap();
            this.zzpkd = ((TreeMap) this.zzpkb).descendingMap();
        }
        return (SortedMap) this.zzpkb;
    }

    static <FieldDescriptorType extends zzffs<FieldDescriptorType>> zzfhy<FieldDescriptorType, Object> zzma(int i) {
        return new zzfhz(i);
    }

    /* access modifiers changed from: private */
    public final V zzmc(int i) {
        zzczl();
        V value = ((zzfid) this.zzpka.remove(i)).getValue();
        if (!this.zzpkb.isEmpty()) {
            Iterator it = zzczm().entrySet().iterator();
            this.zzpka.add(new zzfid(this, (Entry) it.next()));
            it.remove();
        }
        return value;
    }

    public void clear() {
        zzczl();
        if (!this.zzpka.isEmpty()) {
            this.zzpka.clear();
        }
        if (!this.zzpkb.isEmpty()) {
            this.zzpkb.clear();
        }
    }

    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return zza((K) comparable) >= 0 || this.zzpkb.containsKey(comparable);
    }

    public Set<Entry<K, V>> entrySet() {
        if (this.zzpkc == null) {
            this.zzpkc = new zzfif(this, null);
        }
        return this.zzpkc;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzfhy)) {
            return super.equals(obj);
        }
        zzfhy zzfhy = (zzfhy) obj;
        int size = size();
        if (size != zzfhy.size()) {
            return false;
        }
        int zzczj = zzczj();
        if (zzczj != zzfhy.zzczj()) {
            return entrySet().equals(zzfhy.entrySet());
        }
        for (int i = 0; i < zzczj; i++) {
            if (!zzmb(i).equals(zzfhy.zzmb(i))) {
                return false;
            }
        }
        if (zzczj != size) {
            return this.zzpkb.equals(zzfhy.zzpkb);
        }
        return true;
    }

    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int zza = zza((K) comparable);
        return zza >= 0 ? ((zzfid) this.zzpka.get(zza)).getValue() : this.zzpkb.get(comparable);
    }

    public int hashCode() {
        int i = 0;
        for (int i2 = 0; i2 < zzczj(); i2++) {
            i += ((zzfid) this.zzpka.get(i2)).hashCode();
        }
        return this.zzpkb.size() > 0 ? i + this.zzpkb.hashCode() : i;
    }

    public final boolean isImmutable() {
        return this.zzktj;
    }

    public V remove(Object obj) {
        zzczl();
        Comparable comparable = (Comparable) obj;
        int zza = zza((K) comparable);
        if (zza >= 0) {
            return zzmc(zza);
        }
        if (this.zzpkb.isEmpty()) {
            return null;
        }
        return this.zzpkb.remove(comparable);
    }

    public int size() {
        return this.zzpka.size() + this.zzpkb.size();
    }

    /* renamed from: zza */
    public final V put(K k, V v) {
        zzczl();
        int zza = zza(k);
        if (zza >= 0) {
            return ((zzfid) this.zzpka.get(zza)).setValue(v);
        }
        zzczl();
        if (this.zzpka.isEmpty() && !(this.zzpka instanceof ArrayList)) {
            this.zzpka = new ArrayList(this.zzpjz);
        }
        int i = -(zza + 1);
        if (i >= this.zzpjz) {
            return zzczm().put(k, v);
        }
        if (this.zzpka.size() == this.zzpjz) {
            zzfid zzfid = (zzfid) this.zzpka.remove(this.zzpjz - 1);
            zzczm().put((Comparable) zzfid.getKey(), zzfid.getValue());
        }
        this.zzpka.add(i, new zzfid(this, k, v));
        return null;
    }

    public void zzbiy() {
        if (!this.zzktj) {
            this.zzpkb = this.zzpkb.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzpkb);
            this.zzpkd = this.zzpkd.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.zzpkd);
            this.zzktj = true;
        }
    }

    public final int zzczj() {
        return this.zzpka.size();
    }

    public final Iterable<Entry<K, V>> zzczk() {
        return this.zzpkb.isEmpty() ? zzfia.zzczn() : this.zzpkb.entrySet();
    }

    public final Entry<K, V> zzmb(int i) {
        return (Entry) this.zzpka.get(i);
    }
}
