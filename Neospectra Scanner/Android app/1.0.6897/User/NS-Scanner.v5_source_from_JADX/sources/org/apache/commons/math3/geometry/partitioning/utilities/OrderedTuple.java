package org.apache.commons.math3.geometry.partitioning.utilities;

import java.util.Arrays;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class OrderedTuple implements Comparable<OrderedTuple> {
    private static final long EXPONENT_MASK = 9218868437227405312L;
    private static final long IMPLICIT_ONE = 4503599627370496L;
    private static final long MANTISSA_MASK = 4503599627370495L;
    private static final long SIGN_MASK = Long.MIN_VALUE;
    private double[] components;
    private long[] encoding;
    private int lsb = Integer.MAX_VALUE;
    private boolean nan = false;
    private boolean negInf = false;
    private int offset;
    private boolean posInf = false;

    public OrderedTuple(double... components2) {
        this.components = (double[]) components2.clone();
        int msb = Integer.MIN_VALUE;
        for (int i = 0; i < components2.length; i++) {
            if (Double.isInfinite(components2[i])) {
                if (components2[i] < 0.0d) {
                    this.negInf = true;
                } else {
                    this.posInf = true;
                }
            } else if (Double.isNaN(components2[i])) {
                this.nan = true;
            } else {
                long b = Double.doubleToLongBits(components2[i]);
                long m = mantissa(b);
                if (m != 0) {
                    int e = exponent(b);
                    msb = FastMath.max(msb, computeMSB(m) + e);
                    this.lsb = FastMath.min(this.lsb, computeLSB(m) + e);
                }
            }
        }
        if (this.posInf != 0 && this.negInf) {
            this.posInf = false;
            this.negInf = false;
            this.nan = true;
        }
        if (this.lsb <= msb) {
            encode(msb + 16);
            return;
        }
        this.encoding = new long[]{0};
    }

    private void encode(int minOffset) {
        this.offset = minOffset + 31;
        this.offset -= this.offset % 32;
        if (this.encoding == null || this.encoding.length != 1 || this.encoding[0] != 0) {
            this.encoding = new long[(this.components.length * ((((this.offset + 1) - this.lsb) + 62) / 63))];
            int eIndex = 0;
            int eIndex2 = 62;
            long word = 0;
            int k = this.offset;
            while (eIndex < this.encoding.length) {
                int shift = eIndex2;
                int shift2 = eIndex;
                for (int vIndex = 0; vIndex < this.components.length; vIndex++) {
                    if (getBit(vIndex, k) != 0) {
                        word |= 1 << shift;
                    }
                    int shift3 = shift - 1;
                    if (shift == 0) {
                        int eIndex3 = shift2 + 1;
                        this.encoding[shift2] = word;
                        shift = 62;
                        word = 0;
                        shift2 = eIndex3;
                    } else {
                        shift = shift3;
                    }
                }
                k--;
                eIndex = shift2;
                eIndex2 = shift;
            }
        }
    }

    public int compareTo(OrderedTuple ot) {
        if (this.components.length != ot.components.length) {
            return this.components.length - ot.components.length;
        }
        if (this.nan) {
            return 1;
        }
        if (ot.nan || this.negInf || ot.posInf) {
            return -1;
        }
        if (this.posInf || ot.negInf) {
            return 1;
        }
        if (this.offset < ot.offset) {
            encode(ot.offset);
        } else if (this.offset > ot.offset) {
            ot.encode(this.offset);
        }
        int limit = FastMath.min(this.encoding.length, ot.encoding.length);
        for (int i = 0; i < limit; i++) {
            if (this.encoding[i] < ot.encoding[i]) {
                return -1;
            }
            if (this.encoding[i] > ot.encoding[i]) {
                return 1;
            }
        }
        if (this.encoding.length < ot.encoding.length) {
            return -1;
        }
        if (this.encoding.length > ot.encoding.length) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof OrderedTuple)) {
            return false;
        }
        if (compareTo((OrderedTuple) other) != 0) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int i = 71;
        int hash = ((((((((Arrays.hashCode(this.components) * 37) + this.offset) * 37) + this.lsb) * 37) + (this.posInf ? 97 : 71)) * 37) + (this.negInf ? 97 : 71)) * 37;
        if (this.nan) {
            i = 97;
        }
        return hash + i;
    }

    public double[] getComponents() {
        return (double[]) this.components.clone();
    }

    private static long sign(long bits) {
        return SIGN_MASK & bits;
    }

    private static int exponent(long bits) {
        return ((int) ((EXPONENT_MASK & bits) >> 52)) - 1075;
    }

    private static long mantissa(long bits) {
        return (EXPONENT_MASK & bits) == 0 ? (bits & 4503599627370495L) << 1 : (bits & 4503599627370495L) | 4503599627370496L;
    }

    private static int computeMSB(long l) {
        long ll = l;
        long mask = 4294967295L;
        int scale = 32;
        int msb = 0;
        while (scale != 0) {
            if ((ll & mask) != ll) {
                msb |= scale;
                ll >>= scale;
            }
            scale >>= 1;
            mask >>= scale;
        }
        return msb;
    }

    private static int computeLSB(long l) {
        long ll = l;
        long mask = -4294967296L;
        int scale = 32;
        int lsb2 = 0;
        while (scale != 0) {
            if ((ll & mask) == ll) {
                lsb2 |= scale;
                ll >>= scale;
            }
            scale >>= 1;
            mask >>= scale;
        }
        return lsb2;
    }

    private int getBit(int i, int k) {
        long bits = Double.doubleToLongBits(this.components[i]);
        int e = exponent(bits);
        int i2 = 0;
        if (k < e || k > this.offset) {
            return 0;
        }
        if (k == this.offset) {
            if (sign(bits) == 0) {
                i2 = 1;
            }
            return i2;
        } else if (k > e + 52) {
            if (sign(bits) != 0) {
                i2 = 1;
            }
            return i2;
        } else {
            return (int) (((sign(bits) == 0 ? mantissa(bits) : -mantissa(bits)) >> (k - e)) & 1);
        }
    }
}
