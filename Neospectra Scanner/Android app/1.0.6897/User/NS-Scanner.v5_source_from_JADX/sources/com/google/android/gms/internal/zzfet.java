package com.google.android.gms.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;

final class zzfet implements Iterator {
    private final int limit = this.zzpfi.size();
    private int position = 0;
    private /* synthetic */ zzfes zzpfi;

    zzfet(zzfes zzfes) {
        this.zzpfi = zzfes;
    }

    private final byte nextByte() {
        try {
            zzfes zzfes = this.zzpfi;
            int i = this.position;
            this.position = i + 1;
            return zzfes.zzkn(i);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    public final boolean hasNext() {
        return this.position < this.limit;
    }

    public final /* synthetic */ Object next() {
        return Byte.valueOf(nextByte());
    }

    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
