package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.Map.Entry;

final class zzfie implements Iterator<Entry<K, V>> {
    private int pos;
    private /* synthetic */ zzfhy zzpkh;
    private boolean zzpki;
    private Iterator<Entry<K, V>> zzpkj;

    private zzfie(zzfhy zzfhy) {
        this.zzpkh = zzfhy;
        this.pos = -1;
    }

    /* synthetic */ zzfie(zzfhy zzfhy, zzfhz zzfhz) {
        this(zzfhy);
    }

    private final Iterator<Entry<K, V>> zzczp() {
        if (this.zzpkj == null) {
            this.zzpkj = this.zzpkh.zzpkb.entrySet().iterator();
        }
        return this.zzpkj;
    }

    public final boolean hasNext() {
        if (this.pos + 1 >= this.zzpkh.zzpka.size()) {
            return !this.zzpkh.zzpkb.isEmpty() && zzczp().hasNext();
        }
        return true;
    }

    public final /* synthetic */ Object next() {
        this.zzpki = true;
        int i = this.pos + 1;
        this.pos = i;
        return (Entry) (i < this.zzpkh.zzpka.size() ? this.zzpkh.zzpka.get(this.pos) : zzczp().next());
    }

    public final void remove() {
        if (!this.zzpki) {
            throw new IllegalStateException("remove() was called before next()");
        }
        this.zzpki = false;
        this.zzpkh.zzczl();
        if (this.pos < this.zzpkh.zzpka.size()) {
            zzfhy zzfhy = this.zzpkh;
            int i = this.pos;
            this.pos = i - 1;
            zzfhy.zzmc(i);
            return;
        }
        zzczp().remove();
    }
}
