package com.google.android.gms.internal;

import java.io.IOException;
import java.util.Arrays;
import org.apache.poi.hssf.record.formula.NotEqualPtg;
import org.apache.poi.hssf.record.formula.ParenthesisPtg;
import org.apache.poi.hssf.record.formula.UnionPtg;

final class zzffd extends zzffb {
    private final byte[] buffer;
    private final boolean immutable;
    private int limit;
    private int pos;
    private int zzpfr;
    private int zzpfs;
    private int zzpft;
    private int zzpfu;

    private zzffd(byte[] bArr, int i, int i2, boolean z) {
        super();
        this.zzpfu = Integer.MAX_VALUE;
        this.buffer = bArr;
        this.limit = i2 + i;
        this.pos = i;
        this.zzpfs = this.pos;
        this.immutable = z;
    }

    private final long zzcwn() throws IOException {
        long j;
        int i;
        long j2;
        long j3;
        byte b;
        int i2 = this.pos;
        if (this.limit != i2) {
            byte[] bArr = this.buffer;
            int i3 = i2 + 1;
            byte b2 = bArr[i2];
            if (b2 >= 0) {
                this.pos = i3;
                return (long) b2;
            } else if (this.limit - i3 >= 9) {
                int i4 = i3 + 1;
                byte b3 = b2 ^ (bArr[i3] << 7);
                if (b3 < 0) {
                    b = b3 ^ Byte.MIN_VALUE;
                } else {
                    int i5 = i4 + 1;
                    byte b4 = b3 ^ (bArr[i4] << NotEqualPtg.sid);
                    if (b4 >= 0) {
                        long j4 = (long) (b4 ^ 16256);
                        i = i5;
                        j = j4;
                    } else {
                        i4 = i5 + 1;
                        byte b5 = b4 ^ (bArr[i5] << ParenthesisPtg.sid);
                        if (b5 < 0) {
                            b = b5 ^ -2080896;
                        } else {
                            long j5 = (long) b5;
                            i = i4 + 1;
                            long j6 = (((long) bArr[i4]) << 28) ^ j5;
                            if (j6 >= 0) {
                                j3 = 266354560;
                            } else {
                                int i6 = i + 1;
                                long j7 = j6 ^ (((long) bArr[i]) << 35);
                                if (j7 < 0) {
                                    j2 = -34093383808L;
                                } else {
                                    i = i6 + 1;
                                    j6 = j7 ^ (((long) bArr[i6]) << 42);
                                    if (j6 >= 0) {
                                        j3 = 4363953127296L;
                                    } else {
                                        i6 = i + 1;
                                        j7 = j6 ^ (((long) bArr[i]) << 49);
                                        if (j7 < 0) {
                                            j2 = -558586000294016L;
                                        } else {
                                            i = i6 + 1;
                                            long j8 = (j7 ^ (((long) bArr[i6]) << 56)) ^ 71499008037633920L;
                                            if (j8 < 0) {
                                                i6 = i + 1;
                                                if (((long) bArr[i]) >= 0) {
                                                    j = j8;
                                                    i = i6;
                                                }
                                            } else {
                                                j = j8;
                                            }
                                        }
                                    }
                                }
                                j = j2 ^ j7;
                                i = i6;
                            }
                            j = j6 ^ j3;
                        }
                    }
                    this.pos = i;
                    return j;
                }
                j = (long) b;
                i = i4;
                this.pos = i;
                return j;
            }
        }
        return zzcwj();
    }

    private final int zzcwo() throws IOException {
        int i = this.pos;
        if (this.limit - i < 4) {
            throw zzfge.zzcya();
        }
        byte[] bArr = this.buffer;
        this.pos = i + 4;
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << UnionPtg.sid);
    }

    private final long zzcwp() throws IOException {
        int i = this.pos;
        if (this.limit - i < 8) {
            throw zzfge.zzcya();
        }
        byte[] bArr = this.buffer;
        this.pos = i + 8;
        return ((((long) bArr[i + 7]) & 255) << 56) | (((long) bArr[i]) & 255) | ((((long) bArr[i + 1]) & 255) << 8) | ((((long) bArr[i + 2]) & 255) << 16) | ((((long) bArr[i + 3]) & 255) << 24) | ((((long) bArr[i + 4]) & 255) << 32) | ((((long) bArr[i + 5]) & 255) << 40) | ((((long) bArr[i + 6]) & 255) << 48);
    }

    private final void zzcwq() {
        this.limit += this.zzpfr;
        int i = this.limit - this.zzpfs;
        if (i > this.zzpfu) {
            this.zzpfr = i - this.zzpfu;
            this.limit -= this.zzpfr;
            return;
        }
        this.zzpfr = 0;
    }

    private final byte zzcwr() throws IOException {
        if (this.pos == this.limit) {
            throw zzfge.zzcya();
        }
        byte[] bArr = this.buffer;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i];
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(zzcwp());
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(zzcwo());
    }

    public final String readString() throws IOException {
        int zzcwi = zzcwi();
        if (zzcwi > 0 && zzcwi <= this.limit - this.pos) {
            String str = new String(this.buffer, this.pos, zzcwi, zzffz.UTF_8);
            this.pos += zzcwi;
            return str;
        } else if (zzcwi == 0) {
            return "";
        } else {
            if (zzcwi < 0) {
                throw zzfge.zzcyb();
            }
            throw zzfge.zzcya();
        }
    }

    public final <T extends zzffu<T, ?>> T zza(T t, zzffm zzffm) throws IOException {
        int zzcwi = zzcwi();
        if (this.zzpfm >= this.zzpfn) {
            throw zzfge.zzcyg();
        }
        int zzks = zzks(zzcwi);
        this.zzpfm++;
        T zza = zzffu.zza(t, (zzffb) this, zzffm);
        zzkp(0);
        this.zzpfm--;
        zzkt(zzks);
        return zza;
    }

    public final void zza(zzfhf zzfhf, zzffm zzffm) throws IOException {
        int zzcwi = zzcwi();
        if (this.zzpfm >= this.zzpfn) {
            throw zzfge.zzcyg();
        }
        int zzks = zzks(zzcwi);
        this.zzpfm++;
        zzfhf.zzb(this, zzffm);
        zzkp(0);
        this.zzpfm--;
        zzkt(zzks);
    }

    public final int zzcvt() throws IOException {
        if (zzcwl()) {
            this.zzpft = 0;
            return 0;
        }
        this.zzpft = zzcwi();
        if ((this.zzpft >>> 3) != 0) {
            return this.zzpft;
        }
        throw zzfge.zzcyd();
    }

    public final long zzcvu() throws IOException {
        return zzcwn();
    }

    public final long zzcvv() throws IOException {
        return zzcwn();
    }

    public final int zzcvw() throws IOException {
        return zzcwi();
    }

    public final long zzcvx() throws IOException {
        return zzcwp();
    }

    public final int zzcvy() throws IOException {
        return zzcwo();
    }

    public final boolean zzcvz() throws IOException {
        return zzcwn() != 0;
    }

    public final String zzcwa() throws IOException {
        int zzcwi = zzcwi();
        if (zzcwi <= 0 || zzcwi > this.limit - this.pos) {
            if (zzcwi == 0) {
                return "";
            }
            if (zzcwi <= 0) {
                throw zzfge.zzcyb();
            }
            throw zzfge.zzcya();
        } else if (!zzfis.zzk(this.buffer, this.pos, this.pos + zzcwi)) {
            throw zzfge.zzcyi();
        } else {
            int i = this.pos;
            this.pos += zzcwi;
            return new String(this.buffer, i, zzcwi, zzffz.UTF_8);
        }
    }

    public final zzfes zzcwb() throws IOException {
        byte[] bArr;
        int zzcwi = zzcwi();
        if (zzcwi > 0 && zzcwi <= this.limit - this.pos) {
            zzfes zze = zzfes.zze(this.buffer, this.pos, zzcwi);
            this.pos += zzcwi;
            return zze;
        } else if (zzcwi == 0) {
            return zzfes.zzpfg;
        } else {
            if (zzcwi > 0 && zzcwi <= this.limit - this.pos) {
                int i = this.pos;
                this.pos += zzcwi;
                bArr = Arrays.copyOfRange(this.buffer, i, this.pos);
            } else if (zzcwi > 0) {
                throw zzfge.zzcya();
            } else if (zzcwi == 0) {
                bArr = zzffz.EMPTY_BYTE_ARRAY;
            } else {
                throw zzfge.zzcyb();
            }
            return zzfes.zzba(bArr);
        }
    }

    public final int zzcwc() throws IOException {
        return zzcwi();
    }

    public final int zzcwd() throws IOException {
        return zzcwi();
    }

    public final int zzcwe() throws IOException {
        return zzcwo();
    }

    public final long zzcwf() throws IOException {
        return zzcwp();
    }

    public final int zzcwg() throws IOException {
        return zzkv(zzcwi());
    }

    public final long zzcwh() throws IOException {
        return zzcs(zzcwn());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0068, code lost:
        if (r1[r2] >= 0) goto L_0x006a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int zzcwi() throws java.io.IOException {
        /*
            r5 = this;
            int r0 = r5.pos
            int r1 = r5.limit
            if (r1 == r0) goto L_0x006d
            byte[] r1 = r5.buffer
            int r2 = r0 + 1
            byte r0 = r1[r0]
            if (r0 < 0) goto L_0x0011
            r5.pos = r2
            return r0
        L_0x0011:
            int r3 = r5.limit
            int r3 = r3 - r2
            r4 = 9
            if (r3 < r4) goto L_0x006d
            int r3 = r2 + 1
            byte r2 = r1[r2]
            int r2 = r2 << 7
            r0 = r0 ^ r2
            if (r0 >= 0) goto L_0x0024
            r0 = r0 ^ -128(0xffffffffffffff80, float:NaN)
            goto L_0x006a
        L_0x0024:
            int r2 = r3 + 1
            byte r3 = r1[r3]
            int r3 = r3 << 14
            r0 = r0 ^ r3
            if (r0 < 0) goto L_0x0031
            r0 = r0 ^ 16256(0x3f80, float:2.278E-41)
        L_0x002f:
            r3 = r2
            goto L_0x006a
        L_0x0031:
            int r3 = r2 + 1
            byte r2 = r1[r2]
            int r2 = r2 << 21
            r0 = r0 ^ r2
            if (r0 >= 0) goto L_0x003f
            r1 = -2080896(0xffffffffffe03f80, float:NaN)
            r0 = r0 ^ r1
            goto L_0x006a
        L_0x003f:
            int r2 = r3 + 1
            byte r3 = r1[r3]
            int r4 = r3 << 28
            r0 = r0 ^ r4
            r4 = 266354560(0xfe03f80, float:2.2112565E-29)
            r0 = r0 ^ r4
            if (r3 >= 0) goto L_0x002f
            int r3 = r2 + 1
            byte r2 = r1[r2]
            if (r2 >= 0) goto L_0x006a
            int r2 = r3 + 1
            byte r3 = r1[r3]
            if (r3 >= 0) goto L_0x002f
            int r3 = r2 + 1
            byte r2 = r1[r2]
            if (r2 >= 0) goto L_0x006a
            int r2 = r3 + 1
            byte r3 = r1[r3]
            if (r3 >= 0) goto L_0x002f
            int r3 = r2 + 1
            byte r1 = r1[r2]
            if (r1 < 0) goto L_0x006d
        L_0x006a:
            r5.pos = r3
            return r0
        L_0x006d:
            long r0 = r5.zzcwj()
            int r0 = (int) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzffd.zzcwi():int");
    }

    /* access modifiers changed from: 0000 */
    public final long zzcwj() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzcwr = zzcwr();
            j |= ((long) (zzcwr & Byte.MAX_VALUE)) << i;
            if ((zzcwr & 128) == 0) {
                return j;
            }
        }
        throw zzfge.zzcyc();
    }

    public final int zzcwk() {
        if (this.zzpfu == Integer.MAX_VALUE) {
            return -1;
        }
        return this.zzpfu - zzcwm();
    }

    public final boolean zzcwl() throws IOException {
        return this.pos == this.limit;
    }

    public final int zzcwm() {
        return this.pos - this.zzpfs;
    }

    public final void zzkp(int i) throws zzfge {
        if (this.zzpft != i) {
            throw zzfge.zzcye();
        }
    }

    public final boolean zzkq(int i) throws IOException {
        int zzcvt;
        int i2 = 0;
        switch (i & 7) {
            case 0:
                if (this.limit - this.pos >= 10) {
                    while (i2 < 10) {
                        byte[] bArr = this.buffer;
                        int i3 = this.pos;
                        this.pos = i3 + 1;
                        if (bArr[i3] < 0) {
                            i2++;
                        }
                    }
                    throw zzfge.zzcyc();
                }
                while (i2 < 10) {
                    if (zzcwr() < 0) {
                        i2++;
                    }
                }
                throw zzfge.zzcyc();
                return true;
            case 1:
                zzku(8);
                return true;
            case 2:
                zzku(zzcwi());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzku(4);
                return true;
            default:
                throw zzfge.zzcyf();
        }
        do {
            zzcvt = zzcvt();
            if (zzcvt != 0) {
            }
            zzkp(((i >>> 3) << 3) | 4);
            return true;
        } while (zzkq(zzcvt));
        zzkp(((i >>> 3) << 3) | 4);
        return true;
    }

    public final int zzks(int i) throws zzfge {
        if (i < 0) {
            throw zzfge.zzcyb();
        }
        int zzcwm = i + zzcwm();
        int i2 = this.zzpfu;
        if (zzcwm > i2) {
            throw zzfge.zzcya();
        }
        this.zzpfu = zzcwm;
        zzcwq();
        return i2;
    }

    public final void zzkt(int i) {
        this.zzpfu = i;
        zzcwq();
    }

    public final void zzku(int i) throws IOException {
        if (i >= 0 && i <= this.limit - this.pos) {
            this.pos += i;
        } else if (i < 0) {
            throw zzfge.zzcyb();
        } else {
            throw zzfge.zzcya();
        }
    }
}
