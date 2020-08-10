package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzfjs {
    protected volatile int zzpfd = -1;

    public static final <T extends zzfjs> T zza(T t, byte[] bArr) throws zzfjr {
        return zza(t, bArr, 0, bArr.length);
    }

    private static <T extends zzfjs> T zza(T t, byte[] bArr, int i, int i2) throws zzfjr {
        try {
            zzfjj zzn = zzfjj.zzn(bArr, 0, i2);
            t.zza(zzn);
            zzn.zzkp(0);
            return t;
        } catch (zzfjr e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
        }
    }

    public static final byte[] zzc(zzfjs zzfjs) {
        byte[] bArr = new byte[zzfjs.zzho()];
        try {
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzfjs.zza(zzo);
            zzo.zzcwt();
            return bArr;
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public String toString() {
        return zzfjt.zzd(this);
    }

    public abstract zzfjs zza(zzfjj zzfjj) throws IOException;

    public void zza(zzfjk zzfjk) throws IOException {
    }

    /* renamed from: zzdag */
    public zzfjs clone() throws CloneNotSupportedException {
        return (zzfjs) super.clone();
    }

    public final int zzdam() {
        if (this.zzpfd < 0) {
            zzho();
        }
        return this.zzpfd;
    }

    public final int zzho() {
        int zzq = zzq();
        this.zzpfd = zzq;
        return zzq;
    }

    /* access modifiers changed from: protected */
    public int zzq() {
        return 0;
    }
}
