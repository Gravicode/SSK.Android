package org.apache.commons.math3.dfp;

public class DfpDec extends Dfp {
    protected DfpDec(DfpField factory) {
        super(factory);
    }

    protected DfpDec(DfpField factory, byte x) {
        super(factory, x);
    }

    protected DfpDec(DfpField factory, int x) {
        super(factory, x);
    }

    protected DfpDec(DfpField factory, long x) {
        super(factory, x);
    }

    protected DfpDec(DfpField factory, double x) {
        super(factory, x);
        round(0);
    }

    public DfpDec(Dfp d) {
        super(d);
        round(0);
    }

    protected DfpDec(DfpField factory, String s) {
        super(factory, s);
        round(0);
    }

    protected DfpDec(DfpField factory, byte sign, byte nans) {
        super(factory, sign, nans);
    }

    public Dfp newInstance() {
        return new DfpDec(getField());
    }

    public Dfp newInstance(byte x) {
        return new DfpDec(getField(), x);
    }

    public Dfp newInstance(int x) {
        return new DfpDec(getField(), x);
    }

    public Dfp newInstance(long x) {
        return new DfpDec(getField(), x);
    }

    public Dfp newInstance(double x) {
        return new DfpDec(getField(), x);
    }

    public Dfp newInstance(Dfp d) {
        if (getField().getRadixDigits() == d.getField().getRadixDigits()) {
            return new DfpDec(d);
        }
        getField().setIEEEFlagsBits(1);
        Dfp result = newInstance(getZero());
        result.nans = 3;
        return dotrap(1, "newInstance", d, result);
    }

    public Dfp newInstance(String s) {
        return new DfpDec(getField(), s);
    }

    public Dfp newInstance(byte sign, byte nans) {
        return new DfpDec(getField(), sign, nans);
    }

    /* access modifiers changed from: protected */
    public int getDecimalDigits() {
        return (getRadixDigits() * 4) - 3;
    }

    /* access modifiers changed from: protected */
    public int round(int in) {
        int n;
        int discarded;
        boolean inc;
        int msb = this.mant[this.mant.length - 1];
        if (msb == 0) {
            return 0;
        }
        int cmaxdigits = this.mant.length * 4;
        int lsbthreshold = 1000;
        while (lsbthreshold > msb) {
            lsbthreshold /= 10;
            cmaxdigits--;
        }
        int digits = getDecimalDigits();
        int lsbshift = cmaxdigits - digits;
        int lsd = lsbshift / 4;
        int lsbthreshold2 = 1;
        for (int i = 0; i < lsbshift % 4; i++) {
            lsbthreshold2 *= 10;
        }
        int lsb = this.mant[lsd];
        if (lsbthreshold2 <= 1 && digits == (this.mant.length * 4) - 3) {
            return super.round(in);
        }
        int discarded2 = in;
        if (lsbthreshold2 == 1) {
            n = (this.mant[lsd - 1] / 1000) % 10;
            int[] iArr = this.mant;
            int i2 = lsd - 1;
            iArr[i2] = iArr[i2] % 1000;
            discarded = this.mant[lsd - 1] | discarded2;
        } else {
            n = ((lsb * 10) / lsbthreshold2) % 10;
            discarded = (lsb % (lsbthreshold2 / 10)) | discarded2;
        }
        int discarded3 = discarded;
        for (int i3 = 0; i3 < lsd; i3++) {
            discarded3 |= this.mant[i3];
            this.mant[i3] = 0;
        }
        this.mant[lsd] = (lsb / lsbthreshold2) * lsbthreshold2;
        switch (getField().getRoundingMode()) {
            case ROUND_DOWN:
                inc = false;
                break;
            case ROUND_UP:
                if (n != 0 || discarded3 != 0) {
                    inc = true;
                    break;
                } else {
                    inc = false;
                    break;
                }
            case ROUND_HALF_UP:
                if (n < 5) {
                    inc = false;
                    break;
                } else {
                    inc = true;
                    break;
                }
            case ROUND_HALF_DOWN:
                if (n <= 5) {
                    inc = false;
                    break;
                } else {
                    inc = true;
                    break;
                }
            case ROUND_HALF_EVEN:
                if (n <= 5 && ((n != 5 || discarded3 == 0) && (n != 5 || discarded3 != 0 || ((lsb / lsbthreshold2) & 1) != 1))) {
                    inc = false;
                    break;
                } else {
                    inc = true;
                    break;
                }
            case ROUND_HALF_ODD:
                if (n <= 5 && ((n != 5 || discarded3 == 0) && (n != 5 || discarded3 != 0 || ((lsb / lsbthreshold2) & 1) != 0))) {
                    inc = false;
                    break;
                } else {
                    inc = true;
                    break;
                }
            case ROUND_CEIL:
                if (this.sign == 1 && (n != 0 || discarded3 != 0)) {
                    inc = true;
                    break;
                } else {
                    inc = false;
                    break;
                }
            default:
                if (this.sign == -1 && (n != 0 || discarded3 != 0)) {
                    inc = true;
                    break;
                } else {
                    inc = false;
                    break;
                }
        }
        if (inc) {
            int rh = lsbthreshold2;
            for (int i4 = lsd; i4 < this.mant.length; i4++) {
                int r = this.mant[i4] + rh;
                rh = r / 10000;
                this.mant[i4] = r % 10000;
            }
            if (rh != 0) {
                shiftRight();
                this.mant[this.mant.length - 1] = rh;
            }
        }
        if (this.exp < -32767) {
            getField().setIEEEFlagsBits(8);
            return 8;
        } else if (this.exp > 32768) {
            getField().setIEEEFlagsBits(4);
            return 4;
        } else if (n == 0 && discarded3 == 0) {
            return 0;
        } else {
            getField().setIEEEFlagsBits(16);
            return 16;
        }
    }

    public Dfp nextAfter(Dfp x) {
        Dfp result;
        Dfp inc;
        String str = "nextAfter";
        if (getField().getRadixDigits() != x.getField().getRadixDigits()) {
            getField().setIEEEFlagsBits(1);
            Dfp result2 = newInstance(getZero());
            result2.nans = 3;
            return dotrap(1, "nextAfter", x, result2);
        }
        boolean up = false;
        if (lessThan(x)) {
            up = true;
        }
        if (equals(x)) {
            return newInstance(x);
        }
        if (lessThan(getZero())) {
            up = !up;
        }
        if (up) {
            Dfp inc2 = copysign(power10((intLog10() - getDecimalDigits()) + 1), this);
            if (equals(getZero())) {
                inc2 = power10K((-32767 - this.mant.length) - 1);
            }
            if (inc2.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = add(inc2);
            }
        } else {
            Dfp inc3 = copysign(power10(intLog10()), this);
            if (equals(inc3)) {
                inc = inc3.divide(power10(getDecimalDigits()));
            } else {
                inc = inc3.divide(power10(getDecimalDigits() - 1));
            }
            if (equals(getZero())) {
                inc = power10K((-32767 - this.mant.length) - 1);
            }
            if (inc.equals(getZero())) {
                result = copysign(newInstance(getZero()), this);
            } else {
                result = subtract(inc);
            }
        }
        if (result.classify() == 1 && classify() != 1) {
            getField().setIEEEFlagsBits(16);
            result = dotrap(16, "nextAfter", x, result);
        }
        if (result.equals(getZero()) && !equals(getZero())) {
            getField().setIEEEFlagsBits(16);
            result = dotrap(16, "nextAfter", x, result);
        }
        return result;
    }
}
