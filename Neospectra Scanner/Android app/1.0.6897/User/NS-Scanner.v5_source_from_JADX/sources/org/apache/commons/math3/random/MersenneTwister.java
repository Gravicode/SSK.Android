package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;

public class MersenneTwister extends BitsStreamGenerator implements Serializable {

    /* renamed from: M */
    private static final int f742M = 397;
    private static final int[] MAG01 = {0, -1727483681};

    /* renamed from: N */
    private static final int f743N = 624;
    private static final long serialVersionUID = 8661194735290153518L;

    /* renamed from: mt */
    private int[] f744mt = new int[f743N];
    private int mti;

    public MersenneTwister() {
        setSeed(System.currentTimeMillis() + ((long) System.identityHashCode(this)));
    }

    public MersenneTwister(int seed) {
        setSeed(seed);
    }

    public MersenneTwister(int[] seed) {
        setSeed(seed);
    }

    public MersenneTwister(long seed) {
        setSeed(seed);
    }

    public void setSeed(int seed) {
        long longMT = (long) seed;
        this.f744mt[0] = (int) longMT;
        this.mti = 1;
        while (this.mti < f743N) {
            longMT = ((((longMT >> 30) ^ longMT) * 1812433253) + ((long) this.mti)) & 4294967295L;
            this.f744mt[this.mti] = (int) longMT;
            this.mti++;
        }
        clear();
    }

    public void setSeed(int[] seed) {
        char c;
        int[] iArr = seed;
        if (iArr == null) {
            setSeed(System.currentTimeMillis() + ((long) System.identityHashCode(this)));
            return;
        }
        setSeed(19650218);
        int i = 1;
        int j = 0;
        int k = FastMath.max((int) f743N, iArr.length);
        while (true) {
            c = 30;
            if (k == 0) {
                break;
            }
            long l0 = (((long) this.f744mt[i]) & 2147483647L) | (this.f744mt[i] < 0 ? 2147483648L : 0);
            long l1 = (((long) this.f744mt[i - 1]) & 2147483647L) | (this.f744mt[i + -1] < 0 ? 2147483648L : 0);
            long j2 = l0;
            this.f744mt[i] = (int) (4294967295L & (((((l1 >> 30) ^ l1) * 1664525) ^ l0) + ((long) iArr[j]) + ((long) j)));
            i++;
            j++;
            if (i >= f743N) {
                this.f744mt[0] = this.f744mt[623];
                i = 1;
            }
            if (j >= iArr.length) {
                j = 0;
            }
            k--;
        }
        int i2 = i;
        int k2 = 623;
        while (k2 != 0) {
            long l02 = (((long) this.f744mt[i2]) & 2147483647L) | (this.f744mt[i2] < 0 ? 2147483648L : 0);
            long l12 = (((long) this.f744mt[i2 - 1]) & 2147483647L) | (this.f744mt[i2 + -1] < 0 ? 2147483648L : 0);
            long j3 = l02;
            this.f744mt[i2] = (int) (((l02 ^ ((l12 ^ (l12 >> c)) * 1566083941)) - ((long) i2)) & 4294967295L);
            i2++;
            if (i2 >= f743N) {
                this.f744mt[0] = this.f744mt[623];
                i2 = 1;
            }
            k2--;
            c = 30;
        }
        this.f744mt[0] = Integer.MIN_VALUE;
        clear();
    }

    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (4294967295L & seed)});
    }

    /* access modifiers changed from: protected */
    public int next(int bits) {
        int mtCurr;
        if (this.mti >= f743N) {
            int mtNext = this.f744mt[0];
            int k = 0;
            while (true) {
                mtCurr = 227;
                if (k >= 227) {
                    break;
                }
                int mtCurr2 = mtNext;
                mtNext = this.f744mt[k + 1];
                int y = (Integer.MAX_VALUE & mtNext) | (Integer.MIN_VALUE & mtCurr2);
                this.f744mt[k] = (this.f744mt[k + f742M] ^ (y >>> 1)) ^ MAG01[y & 1];
                k++;
            }
            while (true) {
                int k2 = mtCurr;
                if (k2 >= 623) {
                    break;
                }
                int mtCurr3 = mtNext;
                mtNext = this.f744mt[k2 + 1];
                int y2 = (mtCurr3 & Integer.MIN_VALUE) | (mtNext & Integer.MAX_VALUE);
                this.f744mt[k2] = (this.f744mt[k2 - 227] ^ (y2 >>> 1)) ^ MAG01[y2 & 1];
                mtCurr = k2 + 1;
            }
            int y3 = (mtNext & Integer.MIN_VALUE) | (Integer.MAX_VALUE & this.f744mt[0]);
            this.f744mt[623] = (this.f744mt[396] ^ (y3 >>> 1)) ^ MAG01[y3 & 1];
            this.mti = 0;
        }
        int[] iArr = this.f744mt;
        int i = this.mti;
        this.mti = i + 1;
        int y4 = iArr[i];
        int y5 = y4 ^ (y4 >>> 11);
        int y6 = y5 ^ ((y5 << 7) & -1658038656);
        int y7 = y6 ^ ((y6 << 15) & -272236544);
        return (y7 ^ (y7 >>> 18)) >>> (32 - bits);
    }
}
