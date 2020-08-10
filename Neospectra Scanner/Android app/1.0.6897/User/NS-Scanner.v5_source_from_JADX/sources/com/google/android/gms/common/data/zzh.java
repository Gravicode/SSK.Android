package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

public final class zzh<T> extends zzb<T> {
    private T zzfwq;

    public zzh(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    public final T next() {
        if (!hasNext()) {
            int i = this.zzfvv;
            StringBuilder sb = new StringBuilder(46);
            sb.append("Cannot advance the iterator beyond ");
            sb.append(i);
            throw new NoSuchElementException(sb.toString());
        }
        this.zzfvv++;
        if (this.zzfvv == 0) {
            this.zzfwq = this.zzfvu.get(0);
            if (!(this.zzfwq instanceof zzc)) {
                String valueOf = String.valueOf(this.zzfwq.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 44);
                sb2.append("DataBuffer reference of type ");
                sb2.append(valueOf);
                sb2.append(" is not movable");
                throw new IllegalStateException(sb2.toString());
            }
        } else {
            ((zzc) this.zzfwq).zzbx(this.zzfvv);
        }
        return this.zzfwq;
    }
}
