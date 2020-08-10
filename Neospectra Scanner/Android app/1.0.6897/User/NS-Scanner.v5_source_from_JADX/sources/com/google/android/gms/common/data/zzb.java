package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.zzbq;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class zzb<T> implements Iterator<T> {
    protected final DataBuffer<T> zzfvu;
    protected int zzfvv = -1;

    public zzb(DataBuffer<T> dataBuffer) {
        this.zzfvu = (DataBuffer) zzbq.checkNotNull(dataBuffer);
    }

    public boolean hasNext() {
        return this.zzfvv < this.zzfvu.getCount() - 1;
    }

    public T next() {
        if (!hasNext()) {
            int i = this.zzfvv;
            StringBuilder sb = new StringBuilder(46);
            sb.append("Cannot advance the iterator beyond ");
            sb.append(i);
            throw new NoSuchElementException(sb.toString());
        }
        DataBuffer<T> dataBuffer = this.zzfvu;
        int i2 = this.zzfvv + 1;
        this.zzfvv = i2;
        return dataBuffer.get(i2);
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove elements from a DataBufferIterator");
    }
}
