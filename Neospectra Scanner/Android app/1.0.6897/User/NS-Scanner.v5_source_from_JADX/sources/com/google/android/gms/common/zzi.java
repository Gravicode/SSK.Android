package com.google.android.gms.common;

import java.util.Arrays;

final class zzi extends zzh {
    private final byte[] zzflc;

    zzi(byte[] bArr) {
        super(Arrays.copyOfRange(bArr, 0, 25));
        this.zzflc = bArr;
    }

    /* access modifiers changed from: 0000 */
    public final byte[] getBytes() {
        return this.zzflc;
    }
}
