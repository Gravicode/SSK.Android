package com.google.android.gms.internal;

import java.util.Comparator;

public abstract class zzeat<K, V> implements zzeap<K, V> {
    private final V value;
    private final K zzmmo;
    private zzeap<K, V> zzmmp;
    private final zzeap<K, V> zzmmq;

    zzeat(K k, V v, zzeap<K, V> zzeap, zzeap<K, V> zzeap2) {
        this.zzmmo = k;
        this.value = v;
        if (zzeap == null) {
            zzeap = zzeao.zzbto();
        }
        this.zzmmp = zzeap;
        if (zzeap2 == null) {
            zzeap2 = zzeao.zzbto();
        }
        this.zzmmq = zzeap2;
    }

    private static int zzb(zzeap zzeap) {
        return zzeap.zzbtn() ? zzeaq.zzmmm : zzeaq.zzmml;
    }

    private final zzeat<K, V> zzb(K k, V v, Integer num, zzeap<K, V> zzeap, zzeap<K, V> zzeap2) {
        K k2 = this.zzmmo;
        V v2 = this.value;
        if (zzeap == null) {
            zzeap = this.zzmmp;
        }
        if (zzeap2 == null) {
            zzeap2 = this.zzmmq;
        }
        return num == zzeaq.zzmml ? new zzeas(k2, v2, zzeap, zzeap2) : new zzean(k2, v2, zzeap, zzeap2);
    }

    private final zzeap<K, V> zzbtt() {
        if (this.zzmmp.isEmpty()) {
            return zzeao.zzbto();
        }
        zzeat zzbtu = (this.zzmmp.zzbtn() || this.zzmmp.zzbtp().zzbtn()) ? this : zzbtu();
        return zzbtu.zza(null, null, ((zzeat) zzbtu.zzmmp).zzbtt(), null).zzbtv();
    }

    private final zzeat<K, V> zzbtu() {
        zzeat<K, V> zzbty = zzbty();
        return zzbty.zzmmq.zzbtp().zzbtn() ? zzbty.zza(null, null, null, ((zzeat) zzbty.zzmmq).zzbtx()).zzbtw().zzbty() : zzbty;
    }

    private final zzeat<K, V> zzbtv() {
        zzeat zzbtw = (!this.zzmmq.zzbtn() || this.zzmmp.zzbtn()) ? this : zzbtw();
        if (zzbtw.zzmmp.zzbtn() && ((zzeat) zzbtw.zzmmp).zzmmp.zzbtn()) {
            zzbtw = zzbtw.zzbtx();
        }
        return (!zzbtw.zzmmp.zzbtn() || !zzbtw.zzmmq.zzbtn()) ? zzbtw : zzbtw.zzbty();
    }

    private final zzeat<K, V> zzbtw() {
        return (zzeat) this.zzmmq.zza(null, null, zzbtm(), zzb(null, null, zzeaq.zzmml, null, ((zzeat) this.zzmmq).zzmmp), null);
    }

    private final zzeat<K, V> zzbtx() {
        return (zzeat) this.zzmmp.zza(null, null, zzbtm(), null, zzb(null, null, zzeaq.zzmml, ((zzeat) this.zzmmp).zzmmq, null));
    }

    private final zzeat<K, V> zzbty() {
        return zzb(null, null, zzb(this), this.zzmmp.zza(null, null, zzb(this.zzmmp), null, null), this.zzmmq.zza(null, null, zzb(this.zzmmq), null, null));
    }

    public final K getKey() {
        return this.zzmmo;
    }

    public final V getValue() {
        return this.value;
    }

    public final boolean isEmpty() {
        return false;
    }

    public final /* synthetic */ zzeap zza(Object obj, Object obj2, int i, zzeap zzeap, zzeap zzeap2) {
        return zzb(null, null, i, zzeap, zzeap2);
    }

    public final zzeap<K, V> zza(K k, V v, Comparator<K> comparator) {
        int compare = comparator.compare(k, this.zzmmo);
        zzeat zzeat = compare < 0 ? zza(null, null, this.zzmmp.zza(k, v, comparator), null) : compare == 0 ? zza(k, v, null, null) : zza(null, null, null, this.zzmmq.zza(k, v, comparator));
        return zzeat.zzbtv();
    }

    public final zzeap<K, V> zza(K k, Comparator<K> comparator) {
        zzeat zzeat;
        if (comparator.compare(k, this.zzmmo) < 0) {
            zzeat zzbtu = (this.zzmmp.isEmpty() || this.zzmmp.zzbtn() || ((zzeat) this.zzmmp).zzmmp.zzbtn()) ? this : zzbtu();
            zzeat = zzbtu.zza(null, null, zzbtu.zzmmp.zza(k, comparator), null);
        } else {
            zzeat zzbtx = this.zzmmp.zzbtn() ? zzbtx() : this;
            if (!zzbtx.zzmmq.isEmpty() && !zzbtx.zzmmq.zzbtn() && !((zzeat) zzbtx.zzmmq).zzmmp.zzbtn()) {
                zzbtx = zzbtx.zzbty();
                if (zzbtx.zzmmp.zzbtp().zzbtn()) {
                    zzbtx = zzbtx.zzbtx().zzbty();
                }
            }
            if (comparator.compare(k, zzbtx.zzmmo) == 0) {
                if (zzbtx.zzmmq.isEmpty()) {
                    return zzeao.zzbto();
                }
                zzeap zzbtr = zzbtx.zzmmq.zzbtr();
                zzbtx = zzbtx.zza(zzbtr.getKey(), zzbtr.getValue(), null, ((zzeat) zzbtx.zzmmq).zzbtt());
            }
            zzeat = zzbtx.zza(null, null, null, zzbtx.zzmmq.zza(k, comparator));
        }
        return zzeat.zzbtv();
    }

    /* access modifiers changed from: protected */
    public abstract zzeat<K, V> zza(K k, V v, zzeap<K, V> zzeap, zzeap<K, V> zzeap2);

    /* access modifiers changed from: 0000 */
    public void zza(zzeap<K, V> zzeap) {
        this.zzmmp = zzeap;
    }

    public final void zza(zzear<K, V> zzear) {
        this.zzmmp.zza(zzear);
        zzear.zzh(this.zzmmo, this.value);
        this.zzmmq.zza(zzear);
    }

    /* access modifiers changed from: protected */
    public abstract int zzbtm();

    public final zzeap<K, V> zzbtp() {
        return this.zzmmp;
    }

    public final zzeap<K, V> zzbtq() {
        return this.zzmmq;
    }

    public final zzeap<K, V> zzbtr() {
        return this.zzmmp.isEmpty() ? this : this.zzmmp.zzbtr();
    }

    public final zzeap<K, V> zzbts() {
        return this.zzmmq.isEmpty() ? this : this.zzmmq.zzbts();
    }
}
