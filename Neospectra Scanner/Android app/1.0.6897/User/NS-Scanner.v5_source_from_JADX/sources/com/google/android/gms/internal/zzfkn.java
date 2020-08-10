package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfkn extends zzfjm<zzfkn> implements Cloneable {
    private String[] zzpqb;
    private String[] zzpqc;
    private int[] zzpqd;
    private long[] zzpqe;
    private long[] zzpqf;

    public zzfkn() {
        this.zzpqb = zzfjv.EMPTY_STRING_ARRAY;
        this.zzpqc = zzfjv.EMPTY_STRING_ARRAY;
        this.zzpqd = zzfjv.zzpnp;
        this.zzpqe = zzfjv.zzpnq;
        this.zzpqf = zzfjv.zzpnq;
        this.zzpnc = null;
        this.zzpfd = -1;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzdaw */
    public zzfkn clone() {
        try {
            zzfkn zzfkn = (zzfkn) super.clone();
            if (this.zzpqb != null && this.zzpqb.length > 0) {
                zzfkn.zzpqb = (String[]) this.zzpqb.clone();
            }
            if (this.zzpqc != null && this.zzpqc.length > 0) {
                zzfkn.zzpqc = (String[]) this.zzpqc.clone();
            }
            if (this.zzpqd != null && this.zzpqd.length > 0) {
                zzfkn.zzpqd = (int[]) this.zzpqd.clone();
            }
            if (this.zzpqe != null && this.zzpqe.length > 0) {
                zzfkn.zzpqe = (long[]) this.zzpqe.clone();
            }
            if (this.zzpqf != null && this.zzpqf.length > 0) {
                zzfkn.zzpqf = (long[]) this.zzpqf.clone();
            }
            return zzfkn;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfkn)) {
            return false;
        }
        zzfkn zzfkn = (zzfkn) obj;
        if (zzfjq.equals((Object[]) this.zzpqb, (Object[]) zzfkn.zzpqb) && zzfjq.equals((Object[]) this.zzpqc, (Object[]) zzfkn.zzpqc) && zzfjq.equals(this.zzpqd, zzfkn.zzpqd) && zzfjq.equals(this.zzpqe, zzfkn.zzpqe) && zzfjq.equals(this.zzpqf, zzfkn.zzpqf)) {
            return (this.zzpnc == null || this.zzpnc.isEmpty()) ? zzfkn.zzpnc == null || zzfkn.zzpnc.isEmpty() : this.zzpnc.equals(zzfkn.zzpnc);
        }
        return false;
    }

    public final int hashCode() {
        return ((((((((((((getClass().getName().hashCode() + 527) * 31) + zzfjq.hashCode((Object[]) this.zzpqb)) * 31) + zzfjq.hashCode((Object[]) this.zzpqc)) * 31) + zzfjq.hashCode(this.zzpqd)) * 31) + zzfjq.hashCode(this.zzpqe)) * 31) + zzfjq.hashCode(this.zzpqf)) * 31) + ((this.zzpnc == null || this.zzpnc.isEmpty()) ? 0 : this.zzpnc.hashCode());
    }

    public final /* synthetic */ zzfjs zza(zzfjj zzfjj) throws IOException {
        int i;
        while (true) {
            int zzcvt = zzfjj.zzcvt();
            if (zzcvt == 0) {
                return this;
            }
            if (zzcvt == 10) {
                int zzb = zzfjv.zzb(zzfjj, 10);
                int length = this.zzpqb == null ? 0 : this.zzpqb.length;
                String[] strArr = new String[(zzb + length)];
                if (length != 0) {
                    System.arraycopy(this.zzpqb, 0, strArr, 0, length);
                }
                while (length < strArr.length - 1) {
                    strArr[length] = zzfjj.readString();
                    zzfjj.zzcvt();
                    length++;
                }
                strArr[length] = zzfjj.readString();
                this.zzpqb = strArr;
            } else if (zzcvt == 18) {
                int zzb2 = zzfjv.zzb(zzfjj, 18);
                int length2 = this.zzpqc == null ? 0 : this.zzpqc.length;
                String[] strArr2 = new String[(zzb2 + length2)];
                if (length2 != 0) {
                    System.arraycopy(this.zzpqc, 0, strArr2, 0, length2);
                }
                while (length2 < strArr2.length - 1) {
                    strArr2[length2] = zzfjj.readString();
                    zzfjj.zzcvt();
                    length2++;
                }
                strArr2[length2] = zzfjj.readString();
                this.zzpqc = strArr2;
            } else if (zzcvt != 24) {
                if (zzcvt == 26) {
                    i = zzfjj.zzks(zzfjj.zzcwi());
                    int position = zzfjj.getPosition();
                    int i2 = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcvw();
                        i2++;
                    }
                    zzfjj.zzmg(position);
                    int length3 = this.zzpqd == null ? 0 : this.zzpqd.length;
                    int[] iArr = new int[(i2 + length3)];
                    if (length3 != 0) {
                        System.arraycopy(this.zzpqd, 0, iArr, 0, length3);
                    }
                    while (length3 < iArr.length) {
                        iArr[length3] = zzfjj.zzcvw();
                        length3++;
                    }
                    this.zzpqd = iArr;
                } else if (zzcvt == 32) {
                    int zzb3 = zzfjv.zzb(zzfjj, 32);
                    int length4 = this.zzpqe == null ? 0 : this.zzpqe.length;
                    long[] jArr = new long[(zzb3 + length4)];
                    if (length4 != 0) {
                        System.arraycopy(this.zzpqe, 0, jArr, 0, length4);
                    }
                    while (length4 < jArr.length - 1) {
                        jArr[length4] = zzfjj.zzcvv();
                        zzfjj.zzcvt();
                        length4++;
                    }
                    jArr[length4] = zzfjj.zzcvv();
                    this.zzpqe = jArr;
                } else if (zzcvt == 34) {
                    i = zzfjj.zzks(zzfjj.zzcwi());
                    int position2 = zzfjj.getPosition();
                    int i3 = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcvv();
                        i3++;
                    }
                    zzfjj.zzmg(position2);
                    int length5 = this.zzpqe == null ? 0 : this.zzpqe.length;
                    long[] jArr2 = new long[(i3 + length5)];
                    if (length5 != 0) {
                        System.arraycopy(this.zzpqe, 0, jArr2, 0, length5);
                    }
                    while (length5 < jArr2.length) {
                        jArr2[length5] = zzfjj.zzcvv();
                        length5++;
                    }
                    this.zzpqe = jArr2;
                } else if (zzcvt == 40) {
                    int zzb4 = zzfjv.zzb(zzfjj, 40);
                    int length6 = this.zzpqf == null ? 0 : this.zzpqf.length;
                    long[] jArr3 = new long[(zzb4 + length6)];
                    if (length6 != 0) {
                        System.arraycopy(this.zzpqf, 0, jArr3, 0, length6);
                    }
                    while (length6 < jArr3.length - 1) {
                        jArr3[length6] = zzfjj.zzcvv();
                        zzfjj.zzcvt();
                        length6++;
                    }
                    jArr3[length6] = zzfjj.zzcvv();
                    this.zzpqf = jArr3;
                } else if (zzcvt == 42) {
                    i = zzfjj.zzks(zzfjj.zzcwi());
                    int position3 = zzfjj.getPosition();
                    int i4 = 0;
                    while (zzfjj.zzcwk() > 0) {
                        zzfjj.zzcvv();
                        i4++;
                    }
                    zzfjj.zzmg(position3);
                    int length7 = this.zzpqf == null ? 0 : this.zzpqf.length;
                    long[] jArr4 = new long[(i4 + length7)];
                    if (length7 != 0) {
                        System.arraycopy(this.zzpqf, 0, jArr4, 0, length7);
                    }
                    while (length7 < jArr4.length) {
                        jArr4[length7] = zzfjj.zzcvv();
                        length7++;
                    }
                    this.zzpqf = jArr4;
                } else if (!super.zza(zzfjj, zzcvt)) {
                    return this;
                }
                zzfjj.zzkt(i);
            } else {
                int zzb5 = zzfjv.zzb(zzfjj, 24);
                int length8 = this.zzpqd == null ? 0 : this.zzpqd.length;
                int[] iArr2 = new int[(zzb5 + length8)];
                if (length8 != 0) {
                    System.arraycopy(this.zzpqd, 0, iArr2, 0, length8);
                }
                while (length8 < iArr2.length - 1) {
                    iArr2[length8] = zzfjj.zzcvw();
                    zzfjj.zzcvt();
                    length8++;
                }
                iArr2[length8] = zzfjj.zzcvw();
                this.zzpqd = iArr2;
            }
        }
    }

    public final void zza(zzfjk zzfjk) throws IOException {
        if (this.zzpqb != null && this.zzpqb.length > 0) {
            for (String str : this.zzpqb) {
                if (str != null) {
                    zzfjk.zzn(1, str);
                }
            }
        }
        if (this.zzpqc != null && this.zzpqc.length > 0) {
            for (String str2 : this.zzpqc) {
                if (str2 != null) {
                    zzfjk.zzn(2, str2);
                }
            }
        }
        if (this.zzpqd != null && this.zzpqd.length > 0) {
            for (int zzaa : this.zzpqd) {
                zzfjk.zzaa(3, zzaa);
            }
        }
        if (this.zzpqe != null && this.zzpqe.length > 0) {
            for (long zzf : this.zzpqe) {
                zzfjk.zzf(4, zzf);
            }
        }
        if (this.zzpqf != null && this.zzpqf.length > 0) {
            for (long zzf2 : this.zzpqf) {
                zzfjk.zzf(5, zzf2);
            }
        }
        super.zza(zzfjk);
    }

    public final /* synthetic */ zzfjm zzdaf() throws CloneNotSupportedException {
        return (zzfkn) clone();
    }

    public final /* synthetic */ zzfjs zzdag() throws CloneNotSupportedException {
        return (zzfkn) clone();
    }

    /* access modifiers changed from: protected */
    public final int zzq() {
        int zzq = super.zzq();
        if (this.zzpqb != null && this.zzpqb.length > 0) {
            int i = 0;
            int i2 = 0;
            for (String str : this.zzpqb) {
                if (str != null) {
                    i2++;
                    i += zzfjk.zztt(str);
                }
            }
            zzq = zzq + i + (i2 * 1);
        }
        if (this.zzpqc != null && this.zzpqc.length > 0) {
            int i3 = 0;
            int i4 = 0;
            for (String str2 : this.zzpqc) {
                if (str2 != null) {
                    i4++;
                    i3 += zzfjk.zztt(str2);
                }
            }
            zzq = zzq + i3 + (i4 * 1);
        }
        if (this.zzpqd != null && this.zzpqd.length > 0) {
            int i5 = 0;
            for (int zzlh : this.zzpqd) {
                i5 += zzfjk.zzlh(zzlh);
            }
            zzq = zzq + i5 + (this.zzpqd.length * 1);
        }
        if (this.zzpqe != null && this.zzpqe.length > 0) {
            int i6 = 0;
            for (long zzdi : this.zzpqe) {
                i6 += zzfjk.zzdi(zzdi);
            }
            zzq = zzq + i6 + (this.zzpqe.length * 1);
        }
        if (this.zzpqf == null || this.zzpqf.length <= 0) {
            return zzq;
        }
        int i7 = 0;
        for (long zzdi2 : this.zzpqf) {
            i7 += zzfjk.zzdi(zzdi2);
        }
        return zzq + i7 + (this.zzpqf.length * 1);
    }
}
