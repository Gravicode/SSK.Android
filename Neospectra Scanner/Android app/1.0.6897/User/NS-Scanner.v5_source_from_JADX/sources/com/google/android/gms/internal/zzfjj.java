package com.google.android.gms.internal;

import java.io.IOException;
import org.apache.poi.hssf.record.formula.NotEqualPtg;
import org.apache.poi.hssf.record.formula.ParenthesisPtg;
import org.apache.poi.hssf.record.formula.UnionPtg;

public final class zzfjj {
    private final byte[] buffer;
    private int zzpfm;
    private int zzpfn = 64;
    private int zzpfo = 67108864;
    private int zzpfr;
    private int zzpft;
    private int zzpfu = Integer.MAX_VALUE;
    private final int zzpfw;
    private final int zzpmz;
    private int zzpna;
    private int zzpnb;

    private zzfjj(byte[] bArr, int i, int i2) {
        this.buffer = bArr;
        this.zzpmz = i;
        int i3 = i2 + i;
        this.zzpna = i3;
        this.zzpfw = i3;
        this.zzpnb = i;
    }

    public static zzfjj zzbe(byte[] bArr) {
        return zzn(bArr, 0, bArr.length);
    }

    private final void zzcwq() {
        this.zzpna += this.zzpfr;
        int i = this.zzpna;
        if (i > this.zzpfu) {
            this.zzpfr = i - this.zzpfu;
            this.zzpna -= this.zzpfr;
            return;
        }
        this.zzpfr = 0;
    }

    private final byte zzcwr() throws IOException {
        if (this.zzpnb == this.zzpna) {
            throw zzfjr.zzdai();
        }
        byte[] bArr = this.buffer;
        int i = this.zzpnb;
        this.zzpnb = i + 1;
        return bArr[i];
    }

    private final void zzku(int i) throws IOException {
        if (i < 0) {
            throw zzfjr.zzdaj();
        } else if (this.zzpnb + i > this.zzpfu) {
            zzku(this.zzpfu - this.zzpnb);
            throw zzfjr.zzdai();
        } else if (i <= this.zzpna - this.zzpnb) {
            this.zzpnb += i;
        } else {
            throw zzfjr.zzdai();
        }
    }

    public static zzfjj zzn(byte[] bArr, int i, int i2) {
        return new zzfjj(bArr, 0, i2);
    }

    public final int getPosition() {
        return this.zzpnb - this.zzpmz;
    }

    public final byte[] readBytes() throws IOException {
        int zzcwi = zzcwi();
        if (zzcwi < 0) {
            throw zzfjr.zzdaj();
        } else if (zzcwi == 0) {
            return zzfjv.zzpnv;
        } else {
            if (zzcwi > this.zzpna - this.zzpnb) {
                throw zzfjr.zzdai();
            }
            byte[] bArr = new byte[zzcwi];
            System.arraycopy(this.buffer, this.zzpnb, bArr, 0, zzcwi);
            this.zzpnb += zzcwi;
            return bArr;
        }
    }

    public final String readString() throws IOException {
        int zzcwi = zzcwi();
        if (zzcwi < 0) {
            throw zzfjr.zzdaj();
        } else if (zzcwi > this.zzpna - this.zzpnb) {
            throw zzfjr.zzdai();
        } else {
            String str = new String(this.buffer, this.zzpnb, zzcwi, zzfjq.UTF_8);
            this.zzpnb += zzcwi;
            return str;
        }
    }

    public final void zza(zzfjs zzfjs) throws IOException {
        int zzcwi = zzcwi();
        if (this.zzpfm >= this.zzpfn) {
            throw zzfjr.zzdal();
        }
        int zzks = zzks(zzcwi);
        this.zzpfm++;
        zzfjs.zza(this);
        zzkp(0);
        this.zzpfm--;
        zzkt(zzks);
    }

    public final void zza(zzfjs zzfjs, int i) throws IOException {
        if (this.zzpfm >= this.zzpfn) {
            throw zzfjr.zzdal();
        }
        this.zzpfm++;
        zzfjs.zza(this);
        zzkp((i << 3) | 4);
        this.zzpfm--;
    }

    public final byte[] zzal(int i, int i2) {
        if (i2 == 0) {
            return zzfjv.zzpnv;
        }
        byte[] bArr = new byte[i2];
        System.arraycopy(this.buffer, this.zzpmz + i, bArr, 0, i2);
        return bArr;
    }

    /* access modifiers changed from: 0000 */
    public final void zzam(int i, int i2) {
        if (i > this.zzpnb - this.zzpmz) {
            int i3 = this.zzpnb - this.zzpmz;
            StringBuilder sb = new StringBuilder(50);
            sb.append("Position ");
            sb.append(i);
            sb.append(" is beyond current ");
            sb.append(i3);
            throw new IllegalArgumentException(sb.toString());
        } else if (i < 0) {
            StringBuilder sb2 = new StringBuilder(24);
            sb2.append("Bad position ");
            sb2.append(i);
            throw new IllegalArgumentException(sb2.toString());
        } else {
            this.zzpnb = this.zzpmz + i;
            this.zzpft = i2;
        }
    }

    public final int zzcvt() throws IOException {
        if (this.zzpnb == this.zzpna) {
            this.zzpft = 0;
            return 0;
        }
        this.zzpft = zzcwi();
        if (this.zzpft != 0) {
            return this.zzpft;
        }
        throw new zzfjr("Protocol message contained an invalid tag (zero).");
    }

    public final long zzcvv() throws IOException {
        return zzcwn();
    }

    public final int zzcvw() throws IOException {
        return zzcwi();
    }

    public final boolean zzcvz() throws IOException {
        return zzcwi() != 0;
    }

    public final long zzcwh() throws IOException {
        long zzcwn = zzcwn();
        return (-(zzcwn & 1)) ^ (zzcwn >>> 1);
    }

    public final int zzcwi() throws IOException {
        int i;
        byte zzcwr = zzcwr();
        if (zzcwr >= 0) {
            return zzcwr;
        }
        byte b = zzcwr & Byte.MAX_VALUE;
        byte zzcwr2 = zzcwr();
        if (zzcwr2 >= 0) {
            i = zzcwr2 << 7;
        } else {
            b |= (zzcwr2 & Byte.MAX_VALUE) << 7;
            byte zzcwr3 = zzcwr();
            if (zzcwr3 >= 0) {
                i = zzcwr3 << NotEqualPtg.sid;
            } else {
                b |= (zzcwr3 & Byte.MAX_VALUE) << NotEqualPtg.sid;
                byte zzcwr4 = zzcwr();
                if (zzcwr4 >= 0) {
                    i = zzcwr4 << ParenthesisPtg.sid;
                } else {
                    byte b2 = b | ((zzcwr4 & Byte.MAX_VALUE) << ParenthesisPtg.sid);
                    byte zzcwr5 = zzcwr();
                    byte b3 = b2 | (zzcwr5 << 28);
                    if (zzcwr5 >= 0) {
                        return b3;
                    }
                    for (int i2 = 0; i2 < 5; i2++) {
                        if (zzcwr() >= 0) {
                            return b3;
                        }
                    }
                    throw zzfjr.zzdak();
                }
            }
        }
        return b | i;
    }

    public final int zzcwk() {
        if (this.zzpfu == Integer.MAX_VALUE) {
            return -1;
        }
        return this.zzpfu - this.zzpnb;
    }

    public final long zzcwn() throws IOException {
        long j = 0;
        for (int i = 0; i < 64; i += 7) {
            byte zzcwr = zzcwr();
            j |= ((long) (zzcwr & Byte.MAX_VALUE)) << i;
            if ((zzcwr & 128) == 0) {
                return j;
            }
        }
        throw zzfjr.zzdak();
    }

    public final int zzcwo() throws IOException {
        return (zzcwr() & 255) | ((zzcwr() & 255) << 8) | ((zzcwr() & 255) << UnionPtg.sid) | ((zzcwr() & 255) << 24);
    }

    public final long zzcwp() throws IOException {
        byte zzcwr = zzcwr();
        byte zzcwr2 = zzcwr();
        return ((((long) zzcwr2) & 255) << 8) | (((long) zzcwr) & 255) | ((((long) zzcwr()) & 255) << 16) | ((((long) zzcwr()) & 255) << 24) | ((((long) zzcwr()) & 255) << 32) | ((((long) zzcwr()) & 255) << 40) | ((((long) zzcwr()) & 255) << 48) | ((((long) zzcwr()) & 255) << 56);
    }

    public final void zzkp(int i) throws zzfjr {
        if (this.zzpft != i) {
            throw new zzfjr("Protocol message end-group tag did not match expected tag.");
        }
    }

    public final boolean zzkq(int i) throws IOException {
        int zzcvt;
        switch (i & 7) {
            case 0:
                zzcwi();
                return true;
            case 1:
                zzcwp();
                return true;
            case 2:
                zzku(zzcwi());
                return true;
            case 3:
                break;
            case 4:
                return false;
            case 5:
                zzcwo();
                return true;
            default:
                throw new zzfjr("Protocol message tag had invalid wire type.");
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

    public final int zzks(int i) throws zzfjr {
        if (i < 0) {
            throw zzfjr.zzdaj();
        }
        int i2 = i + this.zzpnb;
        int i3 = this.zzpfu;
        if (i2 > i3) {
            throw zzfjr.zzdai();
        }
        this.zzpfu = i2;
        zzcwq();
        return i3;
    }

    public final void zzkt(int i) {
        this.zzpfu = i;
        zzcwq();
    }

    public final void zzmg(int i) {
        zzam(i, this.zzpft);
    }
}
