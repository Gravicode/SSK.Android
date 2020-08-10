package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu.zzg;
import java.io.IOException;
import java.util.Arrays;

public final class zzfio {
    private static final zzfio zzpkp = new zzfio(0, new int[0], new Object[0], false);
    private int count;
    private boolean zzpfc;
    private int zzpgs;
    private int[] zzpkq;
    private Object[] zzpkr;

    private zzfio() {
        this(0, new int[8], new Object[8], true);
    }

    private zzfio(int i, int[] iArr, Object[] objArr, boolean z) {
        this.zzpgs = -1;
        this.count = i;
        this.zzpkq = iArr;
        this.zzpkr = objArr;
        this.zzpfc = z;
    }

    static zzfio zzb(zzfio zzfio, zzfio zzfio2) {
        int i = zzfio.count + zzfio2.count;
        int[] copyOf = Arrays.copyOf(zzfio.zzpkq, i);
        System.arraycopy(zzfio2.zzpkq, 0, copyOf, zzfio.count, zzfio2.count);
        Object[] copyOf2 = Arrays.copyOf(zzfio.zzpkr, i);
        System.arraycopy(zzfio2.zzpkr, 0, copyOf2, zzfio.count, zzfio2.count);
        return new zzfio(i, copyOf, copyOf2, true);
    }

    private void zzc(int i, Object obj) {
        zzczl();
        if (this.count == this.zzpkq.length) {
            int i2 = this.count + (this.count < 4 ? 8 : this.count >> 1);
            this.zzpkq = Arrays.copyOf(this.zzpkq, i2);
            this.zzpkr = Arrays.copyOf(this.zzpkr, i2);
        }
        this.zzpkq[this.count] = i;
        this.zzpkr[this.count] = obj;
        this.count++;
    }

    private final void zzczl() {
        if (!this.zzpfc) {
            throw new UnsupportedOperationException();
        }
    }

    public static zzfio zzczu() {
        return zzpkp;
    }

    static zzfio zzczv() {
        return new zzfio();
    }

    public final boolean equals(Object obj) {
        boolean z;
        boolean z2;
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzfio)) {
            return false;
        }
        zzfio zzfio = (zzfio) obj;
        if (this.count == zzfio.count) {
            int[] iArr = this.zzpkq;
            int[] iArr2 = zzfio.zzpkq;
            int i = this.count;
            int i2 = 0;
            while (true) {
                if (i2 >= i) {
                    z = true;
                    break;
                } else if (iArr[i2] != iArr2[i2]) {
                    z = false;
                    break;
                } else {
                    i2++;
                }
            }
            if (z) {
                Object[] objArr = this.zzpkr;
                Object[] objArr2 = zzfio.zzpkr;
                int i3 = this.count;
                int i4 = 0;
                while (true) {
                    if (i4 >= i3) {
                        z2 = true;
                        break;
                    } else if (!objArr[i4].equals(objArr2[i4])) {
                        z2 = false;
                        break;
                    } else {
                        i4++;
                    }
                }
                return z2;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.count + 527) * 31) + Arrays.hashCode(this.zzpkq)) * 31) + Arrays.deepHashCode(this.zzpkr);
    }

    public final void zza(zzffg zzffg) throws IOException {
        for (int i = 0; i < this.count; i++) {
            int i2 = this.zzpkq[i];
            int i3 = i2 >>> 3;
            int i4 = i2 & 7;
            if (i4 != 5) {
                switch (i4) {
                    case 0:
                        zzffg.zza(i3, ((Long) this.zzpkr[i]).longValue());
                        break;
                    case 1:
                        zzffg.zzb(i3, ((Long) this.zzpkr[i]).longValue());
                        break;
                    case 2:
                        zzffg.zza(i3, (zzfes) this.zzpkr[i]);
                        break;
                    case 3:
                        zzffg.zzz(i3, 3);
                        ((zzfio) this.zzpkr[i]).zza(zzffg);
                        zzffg.zzz(i3, 4);
                        break;
                    default:
                        throw zzfge.zzcyf();
                }
            } else {
                zzffg.zzac(i3, ((Integer) this.zzpkr[i]).intValue());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(zzfji zzfji) {
        if (zzfji.zzcwv() == zzg.zzpho) {
            for (int i = this.count - 1; i >= 0; i--) {
                zzfji.zzb(this.zzpkq[i] >>> 3, this.zzpkr[i]);
            }
            return;
        }
        for (int i2 = 0; i2 < this.count; i2++) {
            zzfji.zzb(this.zzpkq[i2] >>> 3, this.zzpkr[i2]);
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzb(int i, zzffb zzffb) throws IOException {
        int zzcvt;
        zzczl();
        int i2 = i >>> 3;
        switch (i & 7) {
            case 0:
                zzc(i, Long.valueOf(zzffb.zzcvv()));
                return true;
            case 1:
                zzc(i, Long.valueOf(zzffb.zzcvx()));
                return true;
            case 2:
                zzc(i, zzffb.zzcwb());
                return true;
            case 3:
                zzfio zzfio = new zzfio();
                do {
                    zzcvt = zzffb.zzcvt();
                    if (zzcvt != 0) {
                    }
                    zzffb.zzkp((i2 << 3) | 4);
                    zzc(i, zzfio);
                    return true;
                } while (zzfio.zzb(zzcvt, zzffb));
                zzffb.zzkp((i2 << 3) | 4);
                zzc(i, zzfio);
                return true;
            case 4:
                return false;
            case 5:
                zzc(i, Integer.valueOf(zzffb.zzcvy()));
                return true;
            default:
                throw zzfge.zzcyf();
        }
    }

    public final void zzbiy() {
        this.zzpfc = false;
    }

    public final int zzczw() {
        int i = this.zzpgs;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.count; i3++) {
            i2 += zzffg.zzd(this.zzpkq[i3] >>> 3, (zzfes) this.zzpkr[i3]);
        }
        this.zzpgs = i2;
        return i2;
    }

    /* access modifiers changed from: 0000 */
    public final void zzd(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < this.count; i2++) {
            zzfhh.zzb(sb, i, String.valueOf(this.zzpkq[i2] >>> 3), this.zzpkr[i2]);
        }
    }

    public final int zzho() {
        int zzaf;
        int i = this.zzpgs;
        if (i != -1) {
            return i;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < this.count; i3++) {
            int i4 = this.zzpkq[i3];
            int i5 = i4 >>> 3;
            int i6 = i4 & 7;
            if (i6 != 5) {
                switch (i6) {
                    case 0:
                        zzaf = zzffg.zzd(i5, ((Long) this.zzpkr[i3]).longValue());
                        break;
                    case 1:
                        zzaf = zzffg.zze(i5, ((Long) this.zzpkr[i3]).longValue());
                        break;
                    case 2:
                        zzaf = zzffg.zzc(i5, (zzfes) this.zzpkr[i3]);
                        break;
                    case 3:
                        zzaf = (zzffg.zzlg(i5) << 1) + ((zzfio) this.zzpkr[i3]).zzho();
                        break;
                    default:
                        throw new IllegalStateException(zzfge.zzcyf());
                }
            } else {
                zzaf = zzffg.zzaf(i5, ((Integer) this.zzpkr[i3]).intValue());
            }
            i2 += zzaf;
        }
        this.zzpgs = i2;
        return i2;
    }
}
