package com.google.android.gms.internal;

import java.io.IOException;

public final class zzcmf extends zzfjm<zzcmf> {
    public long[] zzjmp;
    public long[] zzjmq;

    public zzcmf() {
        this.zzjmp = zzfjv.zzpnq;
        this.zzjmq = zzfjv.zzpnq;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzcmf)) {
            return false;
        }
        zzcmf zzcmf = (zzcmf) obj;
        if (zzfjq.equals(this.zzjmp, zzcmf.zzjmp) && zzfjq.equals(this.zzjmq, zzcmf.zzjmq)) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzcmf.zzpnc == null || zzcmf.zzpnc.isEmpty() : this.zzpnc.equals(zzcmf.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        return ((((((getClass().getName().hashCode() + 527) * 31) + zzfjq.hashCode(this.zzjmp)) * 31) + zzfjq.hashCode(this.zzjmq)) * 31) + ((this.zzpnc == null || this.zzpnc.isEmpty()) ? 0 : this.zzpnc.hashCode());
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        int i;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt != 8) {
                if (zzcvt == 10) {
                    i = zzfjj.zzks(zzfjj.zzcwi());
                    int position = zzfjj.getPosition();
                    int i2 = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcwn();
                        i2++;
                    }
                    zzfjj.zzmg(position);
                    int length = this.zzjmp == null ? 0 : this.zzjmp.length;
                    long[] jArr = new long[(i2 + length)];
                    if (length != 0) {
                        System.arraycopy(this.zzjmp, 0, jArr, 0, length);
                    }
                    while (length < jArr.length) {
                        jArr[length] = zzfjj.zzcwn();
                        length++;
                    }
                    this.zzjmp = jArr;
                } else if (zzcvt == 16) {
                    int zzb = zzfjv.zzb(zzfjj, 16);
                    int length2 = this.zzjmq == null ? 0 : this.zzjmq.length;
                    long[] jArr2 = new long[(zzb + length2)];
                    if (length2 != 0) {
                        System.arraycopy(this.zzjmq, 0, jArr2, 0, length2);
                    }
                    while (length2 < jArr2.length - 1) {
                        jArr2[length2] = zzfjj.zzcwn();
                        zzfjj.zzcvt();
                        length2++;
                    }
                    jArr2[length2] = zzfjj.zzcwn();
                    this.zzjmq = jArr2;
                } else if (zzcvt == 18) {
                    i = zzfjj.zzks(zzfjj.zzcwi());
                    int position2 = zzfjj.getPosition();
                    int i3 = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcwn();
                        i3++;
                    }
                    zzfjj.zzmg(position2);
                    int length3 = this.zzjmq == null ? 0 : this.zzjmq.length;
                    long[] jArr3 = new long[(i3 + length3)];
                    if (length3 != 0) {
                        System.arraycopy(this.zzjmq, 0, jArr3, 0, length3);
                    }
                    while (length3 < jArr3.length) {
                        jArr3[length3] = zzfjj.zzcwn();
                        length3++;
                    }
                    this.zzjmq = jArr3;
                } else if (!super.zza(zzfjj, zzcvt)) {
                    return this;
                }
                zzfjj.zzkt(i);
            } else {
                int zzb2 = zzfjv.zzb(zzfjj, 8);
                int length4 = this.zzjmp == null ? 0 : this.zzjmp.length;
                long[] jArr4 = new long[(zzb2 + length4)];
                if (length4 != 0) {
                    System.arraycopy(this.zzjmp, 0, jArr4, 0, length4);
                }
                while (length4 < jArr4.length - 1) {
                    jArr4[length4] = zzfjj.zzcwn();
                    zzfjj.zzcvt();
                    length4++;
                }
                jArr4[length4] = zzfjj.zzcwn();
                this.zzjmp = jArr4;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzjmp != null && this.zzjmp.length > 0) {
            for (long zza : this.zzjmp) {
                zzfjk.zza(1, zza);
            }
        }
        if (this.zzjmq != null && this.zzjmq.length > 0) {
            for (long zza2 : this.zzjmq) {
                zzfjk.zza(2, zza2);
            }
        }
        super.zza(zzfjk);
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzjmp != null && this.zzjmp.length > 0) {
            int i = 0;
            for (long zzdi : this.zzjmp) {
                i += zzfjk.zzdi(zzdi);
            }
            zzq = zzq + i + (this.zzjmp.length * 1);
        }
        if (this.zzjmq == null || this.zzjmq.length <= 0) {
            return zzq;
        }
        int i2 = 0;
        for (long zzdi2 : this.zzjmq) {
            i2 += zzfjk.zzdi(zzdi2);
        }
        return zzq + i2 + (this.zzjmq.length * 1);
    }
}
