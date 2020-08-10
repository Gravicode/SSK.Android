package com.google.android.gms.common;

import java.lang.ref.WeakReference;

abstract class zzj extends zzh {
    private static final WeakReference<byte[]> zzfle = new WeakReference<>(null);
    private WeakReference<byte[]> zzfld = zzfle;

    zzj(byte[] bArr) {
        super(bArr);
    }

    /* access modifiers changed from: 0000 */
    public final byte[] getBytes() {
        byte[] bArr;
        synchronized (this) {
            bArr = (byte[]) this.zzfld.get();
            if (bArr == null) {
                bArr = zzagc();
                this.zzfld = new WeakReference<>(bArr);
            }
        }
        return bArr;
    }

    /* access modifiers changed from: protected */
    public abstract byte[] zzagc();
}
