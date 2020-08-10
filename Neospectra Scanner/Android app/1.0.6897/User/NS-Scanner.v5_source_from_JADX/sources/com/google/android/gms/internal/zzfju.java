package com.google.android.gms.internal;

import java.util.Arrays;

final class zzfju {
    final int tag;
    final byte[] zzjng;

    zzfju(int i, byte[] bArr) {
        this.tag = i;
        this.zzjng = bArr;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfju)) {
            return false;
        }
        zzfju zzfju = (zzfju) obj;
        return this.tag == zzfju.tag && Arrays.equals(this.zzjng, zzfju.zzjng);
    }

    public final int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzjng);
    }
}
