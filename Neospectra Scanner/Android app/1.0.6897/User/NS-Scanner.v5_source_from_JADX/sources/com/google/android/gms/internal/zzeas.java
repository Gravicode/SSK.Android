package com.google.android.gms.internal;

public final class zzeas<K, V> extends zzeat<K, V> {
    zzeas(K k, V v) {
        super(k, v, zzeao.zzbto(), zzeao.zzbto());
    }

    zzeas(K k, V v, zzeap<K, V> zzeap, zzeap<K, V> zzeap2) {
        super(k, v, zzeap, zzeap2);
    }

    public final int size() {
        return zzbtp().size() + 1 + zzbtq().size();
    }

    /* access modifiers changed from: protected */
    public final zzeat<K, V> zza(K k, V v, zzeap<K, V> zzeap, zzeap<K, V> zzeap2) {
        if (k == null) {
            k = getKey();
        }
        if (v == null) {
            v = getValue();
        }
        if (zzeap == null) {
            zzeap = zzbtp();
        }
        if (zzeap2 == null) {
            zzeap2 = zzbtq();
        }
        return new zzeas(k, v, zzeap, zzeap2);
    }

    /* access modifiers changed from: protected */
    public final int zzbtm() {
        return zzeaq.zzmml;
    }

    public final boolean zzbtn() {
        return true;
    }
}
