package com.google.android.gms.common.data;

import android.database.CharArrayBuffer;
import android.net.Uri;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbq;
import java.util.Arrays;

public class zzc {
    protected final DataHolder zzfqt;
    protected int zzfvx;
    private int zzfvy;

    public zzc(DataHolder dataHolder, int i) {
        this.zzfqt = (DataHolder) zzbq.checkNotNull(dataHolder);
        zzbx(i);
    }

    public boolean equals(Object obj) {
        if (obj instanceof zzc) {
            zzc zzc = (zzc) obj;
            if (zzbg.equal(Integer.valueOf(zzc.zzfvx), Integer.valueOf(this.zzfvx)) && zzbg.equal(Integer.valueOf(zzc.zzfvy), Integer.valueOf(this.zzfvy)) && zzc.zzfqt == this.zzfqt) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final boolean getBoolean(String str) {
        return this.zzfqt.zze(str, this.zzfvx, this.zzfvy);
    }

    /* access modifiers changed from: protected */
    public final byte[] getByteArray(String str) {
        return this.zzfqt.zzg(str, this.zzfvx, this.zzfvy);
    }

    /* access modifiers changed from: protected */
    public final float getFloat(String str) {
        return this.zzfqt.zzf(str, this.zzfvx, this.zzfvy);
    }

    /* access modifiers changed from: protected */
    public final int getInteger(String str) {
        return this.zzfqt.zzc(str, this.zzfvx, this.zzfvy);
    }

    /* access modifiers changed from: protected */
    public final long getLong(String str) {
        return this.zzfqt.zzb(str, this.zzfvx, this.zzfvy);
    }

    /* access modifiers changed from: protected */
    public final String getString(String str) {
        return this.zzfqt.zzd(str, this.zzfvx, this.zzfvy);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.zzfvx), Integer.valueOf(this.zzfvy), this.zzfqt});
    }

    public boolean isDataValid() {
        return !this.zzfqt.isClosed();
    }

    /* access modifiers changed from: protected */
    public final void zza(String str, CharArrayBuffer charArrayBuffer) {
        this.zzfqt.zza(str, this.zzfvx, this.zzfvy, charArrayBuffer);
    }

    /* access modifiers changed from: protected */
    public final void zzbx(int i) {
        zzbq.checkState(i >= 0 && i < this.zzfqt.zzfwg);
        this.zzfvx = i;
        this.zzfvy = this.zzfqt.zzbz(this.zzfvx);
    }

    public final boolean zzga(String str) {
        return this.zzfqt.zzga(str);
    }

    /* access modifiers changed from: protected */
    public final Uri zzgb(String str) {
        String zzd = this.zzfqt.zzd(str, this.zzfvx, this.zzfvy);
        if (zzd == null) {
            return null;
        }
        return Uri.parse(zzd);
    }

    /* access modifiers changed from: protected */
    public final boolean zzgc(String str) {
        return this.zzfqt.zzh(str, this.zzfvx, this.zzfvy);
    }
}
