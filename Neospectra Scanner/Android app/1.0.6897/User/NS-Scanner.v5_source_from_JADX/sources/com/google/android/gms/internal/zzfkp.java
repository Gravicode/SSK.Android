package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;

public final class zzfkp extends zzfjm<zzfkp> implements Cloneable {
    private byte[] zzpqh;
    private String zzpqi;
    private byte[][] zzpqj;
    private boolean zzpqk;

    public zzfkp() {
        this.zzpqh = zzfjv.zzpnv;
        this.zzpqi = "";
        this.zzpqj = zzfjv.zzpnu;
        this.zzpqk = false;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzday */
    public zzfkp clone() {
        try {
            zzfkp zzfkp = (zzfkp) super.clone();
            if (this.zzpqj != null && this.zzpqj.length > 0) {
                zzfkp.zzpqj = (byte[][]) this.zzpqj.clone();
            }
            return zzfkp;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfkp)) {
            return false;
        }
        zzfkp zzfkp = (zzfkp) obj;
        if (!Arrays.equals(this.zzpqh, zzfkp.zzpqh)) {
            return false;
        }
        if (this.zzpqi == null) {
            if (zzfkp.zzpqi != null) {
                return false;
            }
        } else if (!this.zzpqi.equals(zzfkp.zzpqi)) {
            return false;
        }
        if (zzfjq.zza(this.zzpqj, zzfkp.zzpqj) && this.zzpqk == zzfkp.zzpqk) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzfkp.zzpnc == null || zzfkp.zzpnc.isEmpty() : this.zzpnc.equals(zzfkp.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + Arrays.hashCode(this.zzpqh)) * 31) + (this.zzpqi == null ? 0 : this.zzpqi.hashCode())) * 31) + zzfjq.zzd(this.zzpqj)) * 31) + (this.zzpqk ? 1231 : 1237)) * 31;
        if (this.zzpnc != null && !this.zzpnc.isEmpty()) {
            i = this.zzpnc.hashCode();
        }
        return hashCode + i;
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                this.zzpqh = zzfjj.readBytes();
            } else if (zzcvt == 18) {
                int zzb = zzfjv.zzb(zzfjj, 18);
                int length = this.zzpqj == null ? 0 : this.zzpqj.length;
                byte[][] bArr = new byte[(zzb + length)][];
                if (length != 0) {
                    System.arraycopy(this.zzpqj, 0, bArr, 0, length);
                }
                while (length < bArr.length - 1) {
                    bArr[length] = zzfjj.readBytes();
                    zzfjj.zzcvt();
                    length++;
                }
                bArr[length] = zzfjj.readBytes();
                this.zzpqj = bArr;
            } else if (zzcvt == 24) {
                this.zzpqk = zzfjj.zzcvz();
            } else if (zzcvt == 34) {
                this.zzpqi = zzfjj.readString();
            } else if (!super.zza(zzfjj, zzcvt)) {
                return this;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (!Arrays.equals(this.zzpqh, zzfjv.zzpnv)) {
            zzfjk.zzc(1, this.zzpqh);
        }
        if (this.zzpqj != null && this.zzpqj.length > 0) {
            for (byte[] bArr : this.zzpqj) {
                if (bArr != null) {
                    zzfjk.zzc(2, bArr);
                }
            }
        }
        if (this.zzpqk) {
            zzfjk.zzl(3, this.zzpqk);
        }
        if (this.zzpqi != null && !this.zzpqi.equals("")) {
            zzfjk.zzn(4, this.zzpqi);
        }
        super.zza(zzfjk);
    }

    public final /* synthetic */ zzfjm zzdaf() throws CloneNotSupportedException {
        return (zzfkp) clone();
    }

    public final /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfkp) clone();
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (!Arrays.equals(this.zzpqh, zzfjv.zzpnv)) {
            zzq += zzfjk.zzd(1, this.zzpqh);
        }
        if (this.zzpqj != null && this.zzpqj.length > 0) {
            int i = 0;
            int i2 = 0;
            for (byte[] bArr : this.zzpqj) {
                if (bArr != null) {
                    i2++;
                    i += zzfjk.zzbg(bArr);
                }
            }
            zzq = zzq + i + (i2 * 1);
        }
        if (this.zzpqk) {
            zzq += zzfjk.zzlg(3) + 1;
        }
        return (this.zzpqi == null || this.zzpqi.equals("")) ? zzq : zzq + zzfjk.zzo(4, this.zzpqi);
    }
}
