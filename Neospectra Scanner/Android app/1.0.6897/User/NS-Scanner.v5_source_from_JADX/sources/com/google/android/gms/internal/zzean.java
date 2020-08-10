package com.google.android.gms.internal;

public final class zzean<K, V> extends zzeat<K, V> {
    private int size = -1;

    zzean(K k, V v, zzeap<K, V> zzeap, zzeap<K, V> zzeap2) {
        super(k, v, zzeap, zzeap2);
    }

    public final int size() {
        if (this.size == -1) {
            this.size = zzbtp().size() + 1 + zzbtq().size();
        }
        return this.size;
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
        return new zzean(k, v, zzeap, zzeap2);
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzeap<K, V> zzeap) {
        if (this.size != -1) {
            throw new IllegalStateException("Can't set left after using size");
        }
        super.zza(zzeap);
    }

    /* access modifiers changed from: protected */
    public final int zzbtm() {
        return zzeaq.zzmmm;
    }

    public final boolean zzbtn() {
        return false;
    }
}
