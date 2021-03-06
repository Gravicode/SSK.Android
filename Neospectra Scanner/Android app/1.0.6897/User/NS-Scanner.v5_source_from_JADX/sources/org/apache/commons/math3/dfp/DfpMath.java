package org.apache.commons.math3.dfp;

public class DfpMath {
    private static final String POW_TRAP = "pow";

    private DfpMath() {
    }

    protected static Dfp[] split(DfpField field, String a) {
        Dfp[] result = new Dfp[2];
        int sp = 0;
        char[] buf = new char[a.length()];
        int i = 0;
        int sig = 0;
        boolean leading = true;
        int i2 = 0;
        while (true) {
            if (i2 >= buf.length) {
                break;
            }
            buf[i2] = a.charAt(i2);
            if (buf[i2] >= '1' && buf[i2] <= '9') {
                leading = false;
            }
            if (buf[i2] == '.') {
                sig += (400 - sig) % 4;
                leading = false;
            }
            if (sig == (field.getRadixDigits() / 2) * 4) {
                sp = i2;
                break;
            }
            if (buf[i2] >= '0' && buf[i2] <= '9' && !leading) {
                sig++;
            }
            i2++;
        }
        result[0] = field.newDfp(new String(buf, 0, sp));
        while (true) {
            int i3 = i;
            if (i3 < buf.length) {
                buf[i3] = a.charAt(i3);
                if (buf[i3] >= '0' && buf[i3] <= '9' && i3 < sp) {
                    buf[i3] = '0';
                }
                i = i3 + 1;
            } else {
                result[1] = field.newDfp(new String(buf));
                return result;
            }
        }
    }

    protected static Dfp[] split(Dfp a) {
        Dfp[] result = new Dfp[2];
        Dfp shift = a.multiply(a.power10K(a.getRadixDigits() / 2));
        result[0] = a.add(shift).subtract(shift);
        result[1] = a.subtract(result[0]);
        return result;
    }

    protected static Dfp[] splitMult(Dfp[] a, Dfp[] b) {
        Dfp[] result = new Dfp[2];
        result[1] = a[0].getZero();
        result[0] = a[0].multiply(b[0]);
        if (result[0].classify() == 1 || result[0].equals(result[1])) {
            return result;
        }
        result[1] = a[0].multiply(b[1]).add(a[1].multiply(b[0])).add(a[1].multiply(b[1]));
        return result;
    }

    protected static Dfp[] splitDiv(Dfp[] a, Dfp[] b) {
        Dfp[] result = {a[0].divide(b[0]), a[1].multiply(b[0]).subtract(a[0].multiply(b[1]))};
        result[1] = result[1].divide(b[0].multiply(b[0]).add(b[0].multiply(b[1])));
        return result;
    }

    protected static Dfp splitPow(Dfp[] base, int a) {
        int prevtrial;
        boolean invert = false;
        Dfp[] r = new Dfp[2];
        Dfp[] result = {base[0].getOne(), base[0].getZero()};
        if (a == 0) {
            return result[0].add(result[1]);
        }
        if (a < 0) {
            invert = true;
            a = -a;
        }
        while (true) {
            r[0] = new Dfp(base[0]);
            r[1] = new Dfp(base[1]);
            Dfp[] r2 = r;
            int trial = 1;
            while (true) {
                prevtrial = trial;
                trial *= 2;
                if (trial > a) {
                    break;
                }
                r2 = splitMult(r2, r2);
            }
            a -= prevtrial;
            result = splitMult(result, r2);
            if (a < 1) {
                break;
            }
            r = r2;
        }
        result[0] = result[0].add(result[1]);
        if (invert) {
            result[0] = base[0].getOne().divide(result[0]);
        }
        return result[0];
    }

    public static Dfp pow(Dfp base, int a) {
        Dfp prevr;
        int prevtrial;
        boolean invert = false;
        Dfp result = base.getOne();
        if (a == 0) {
            return result;
        }
        if (a < 0) {
            invert = true;
            a = -a;
        }
        do {
            Dfp r = new Dfp(base);
            int trial = 1;
            do {
                prevr = new Dfp(r);
                prevtrial = trial;
                r = r.multiply(r);
                trial *= 2;
            } while (a > trial);
            a -= prevtrial;
            result = result.multiply(prevr);
        } while (a >= 1);
        if (invert) {
            result = base.getOne().divide(result);
        }
        return base.newInstance(result);
    }

    public static Dfp exp(Dfp a) {
        Dfp inta = a.rint();
        Dfp fraca = a.subtract(inta);
        int ia = inta.intValue();
        if (ia > 2147483646) {
            return a.newInstance(1, 1);
        }
        if (ia < -2147483646) {
            return a.newInstance();
        }
        return splitPow(a.getField().getESplit(), ia).multiply(expInternal(fraca));
    }

    protected static Dfp expInternal(Dfp a) {
        Dfp y = a.getOne();
        Dfp x = a.getOne();
        Dfp fact = a.getOne();
        Dfp py = new Dfp(y);
        for (int i = 1; i < 90; i++) {
            x = x.multiply(a);
            fact = fact.divide(i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new Dfp(y);
        }
        return y;
    }

    public static Dfp log(Dfp a) {
        int p2 = 0;
        if (a.equals(a.getZero()) || a.lessThan(a.getZero()) || a.isNaN()) {
            a.getField().setIEEEFlagsBits(1);
            return a.dotrap(1, "ln", a, a.newInstance(1, 3));
        } else if (a.classify() == 1) {
            return a;
        } else {
            Dfp x = new Dfp(a);
            int lr = x.log10K();
            Dfp x2 = x.divide(pow(a.newInstance(10000), lr));
            int ix = x2.floor().intValue();
            while (ix > 2) {
                ix >>= 1;
                p2++;
            }
            Dfp[] spx = split(x2);
            Dfp[] spy = new Dfp[2];
            spy[0] = pow(a.getTwo(), p2);
            spx[0] = spx[0].divide(spy[0]);
            spx[1] = spx[1].divide(spy[0]);
            spy[0] = a.newInstance("1.33333");
            while (spx[0].add(spx[1]).greaterThan(spy[0])) {
                spx[0] = spx[0].divide(2);
                spx[1] = spx[1].divide(2);
                p2++;
            }
            Dfp[] spz = logInternal(spx);
            StringBuilder sb = new StringBuilder();
            sb.append((lr * 4) + p2);
            spx[0] = a.newInstance(sb.toString());
            spx[1] = a.getZero();
            Dfp[] spy2 = splitMult(a.getField().getLn2Split(), spx);
            spz[0] = spz[0].add(spy2[0]);
            spz[1] = spz[1].add(spy2[1]);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(lr * 4);
            spx[0] = a.newInstance(sb2.toString());
            spx[1] = a.getZero();
            Dfp[] spy3 = splitMult(a.getField().getLn5Split(), spx);
            spz[0] = spz[0].add(spy3[0]);
            spz[1] = spz[1].add(spy3[1]);
            return a.newInstance(spz[0].add(spz[1]));
        }
    }

    protected static Dfp[] logInternal(Dfp[] a) {
        Dfp t = a[0].divide(4).add(a[1].divide(4));
        Dfp x = t.add(a[0].newInstance("-0.25")).divide(t.add(a[0].newInstance("0.25")));
        Dfp y = new Dfp(x);
        Dfp num = new Dfp(x);
        int den = 1;
        Dfp py = new Dfp(y);
        Dfp y2 = y;
        Dfp y3 = t;
        for (int i = 0; i < 10000; i++) {
            num = num.multiply(x).multiply(x);
            den += 2;
            y2 = y2.add(num.divide(den));
            if (y2.equals(py)) {
                break;
            }
            py = new Dfp(y2);
        }
        return split(y2.multiply(a[0].getTwo()));
    }

    public static Dfp pow(Dfp x, Dfp y) {
        Dfp u;
        Dfp a;
        if (x.getField().getRadixDigits() != y.getField().getRadixDigits()) {
            x.getField().setIEEEFlagsBits(1);
            Dfp result = x.newInstance(x.getZero());
            result.nans = 3;
            return x.dotrap(1, POW_TRAP, x, result);
        }
        Dfp zero = x.getZero();
        Dfp one = x.getOne();
        Dfp two = x.getTwo();
        boolean invert = false;
        if (y.equals(zero)) {
            return x.newInstance(one);
        }
        if (y.equals(one)) {
            if (!x.isNaN()) {
                return x;
            }
            x.getField().setIEEEFlagsBits(1);
            return x.dotrap(1, POW_TRAP, x, x);
        } else if (x.isNaN() || y.isNaN()) {
            x.getField().setIEEEFlagsBits(1);
            return x.dotrap(1, POW_TRAP, x, x.newInstance(1, 3));
        } else if (!x.equals(zero)) {
            if (x.lessThan(zero)) {
                x = x.negate();
                invert = true;
            }
            if (!x.greaterThan(one) || y.classify() != 1) {
                if (!x.lessThan(one) || y.classify() != 1) {
                    if (x.equals(one) && y.classify() == 1) {
                        x.getField().setIEEEFlagsBits(1);
                        return x.dotrap(1, POW_TRAP, x, x.newInstance(1, 3));
                    } else if (x.classify() == 1) {
                        if (invert) {
                            if (y.classify() != 0 || !y.rint().equals(y) || y.remainder(two).equals(zero)) {
                                if (y.greaterThan(zero)) {
                                    return x.newInstance(x.newInstance(1, 1));
                                }
                                return x.newInstance(zero);
                            } else if (y.greaterThan(zero)) {
                                return x.newInstance(x.newInstance(-1, 1));
                            } else {
                                return x.newInstance(zero.negate());
                            }
                        } else if (y.greaterThan(zero)) {
                            return x;
                        } else {
                            return x.newInstance(zero);
                        }
                    } else if (!invert || y.rint().equals(y)) {
                        if (!y.lessThan(x.newInstance(100000000)) || !y.greaterThan(x.newInstance(-100000000))) {
                            u = exp(log(x).multiply(y));
                        } else {
                            Dfp u2 = y.rint();
                            int ui = u2.intValue();
                            Dfp v = y.subtract(u2);
                            if (v.unequal(zero)) {
                                Dfp a2 = v.multiply(log(x));
                                Dfp b = a2.divide(x.getField().getLn2()).rint();
                                a = splitPow(split(x), ui).multiply(pow(two, b.intValue())).multiply(exp(a2.subtract(b.multiply(x.getField().getLn2()))));
                            } else {
                                a = splitPow(split(x), ui);
                            }
                            u = a;
                        }
                        if (invert && y.rint().equals(y) && !y.remainder(two).equals(zero)) {
                            u = u.negate();
                        }
                        return x.newInstance(u);
                    } else {
                        x.getField().setIEEEFlagsBits(1);
                        return x.dotrap(1, POW_TRAP, x, x.newInstance(1, 3));
                    }
                } else if (y.greaterThan(zero)) {
                    return x.newInstance(zero);
                } else {
                    return x.newInstance(Dfp.copysign(y, one));
                }
            } else if (y.greaterThan(zero)) {
                return y;
            } else {
                return x.newInstance(zero);
            }
        } else if (Dfp.copysign(one, x).greaterThan(zero)) {
            if (y.greaterThan(zero)) {
                return x.newInstance(zero);
            }
            return x.newInstance(x.newInstance(1, 1));
        } else if (y.classify() != 0 || !y.rint().equals(y) || y.remainder(two).equals(zero)) {
            if (y.greaterThan(zero)) {
                return x.newInstance(zero);
            }
            return x.newInstance(x.newInstance(1, 1));
        } else if (y.greaterThan(zero)) {
            return x.newInstance(zero.negate());
        } else {
            return x.newInstance(x.newInstance(-1, 1));
        }
    }

    protected static Dfp sinInternal(Dfp[] a) {
        Dfp c = a[0].add(a[1]);
        Dfp y = c;
        Dfp c2 = c.multiply(c);
        Dfp x = y;
        Dfp fact = a[0].getOne();
        Dfp py = new Dfp(y);
        for (int i = 3; i < 90; i += 2) {
            x = x.multiply(c2).negate();
            fact = fact.divide((i - 1) * i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new Dfp(y);
        }
        return y;
    }

    protected static Dfp cosInternal(Dfp[] a) {
        Dfp one = a[0].getOne();
        Dfp x = one;
        Dfp y = one;
        Dfp c = a[0].add(a[1]);
        Dfp c2 = c.multiply(c);
        Dfp fact = one;
        Dfp py = new Dfp(y);
        for (int i = 2; i < 90; i += 2) {
            x = x.multiply(c2).negate();
            fact = fact.divide((i - 1) * i);
            y = y.add(x.multiply(fact));
            if (y.equals(py)) {
                break;
            }
            py = new Dfp(y);
        }
        return y;
    }

    public static Dfp sin(Dfp a) {
        Dfp y;
        Dfp pi = a.getField().getPi();
        Dfp zero = a.getField().getZero();
        boolean neg = false;
        Dfp x = a.remainder(pi.multiply(2));
        if (x.lessThan(zero)) {
            x = x.negate();
            neg = true;
        }
        if (x.greaterThan(pi.divide(2))) {
            x = pi.subtract(x);
        }
        if (x.lessThan(pi.divide(4))) {
            y = sinInternal(split(x));
        } else {
            Dfp[] piSplit = a.getField().getPiSplit();
            y = cosInternal(new Dfp[]{piSplit[0].divide(2).subtract(x), piSplit[1].divide(2)});
        }
        if (neg) {
            y = y.negate();
        }
        return a.newInstance(y);
    }

    public static Dfp cos(Dfp a) {
        Dfp y;
        Dfp pi = a.getField().getPi();
        Dfp zero = a.getField().getZero();
        boolean neg = false;
        Dfp x = a.remainder(pi.multiply(2));
        if (x.lessThan(zero)) {
            x = x.negate();
        }
        if (x.greaterThan(pi.divide(2))) {
            x = pi.subtract(x);
            neg = true;
        }
        if (x.lessThan(pi.divide(4))) {
            y = cosInternal(new Dfp[]{x, zero});
        } else {
            Dfp[] piSplit = a.getField().getPiSplit();
            y = sinInternal(new Dfp[]{piSplit[0].divide(2).subtract(x), piSplit[1].divide(2)});
        }
        if (neg) {
            y = y.negate();
        }
        return a.newInstance(y);
    }

    public static Dfp tan(Dfp a) {
        return sin(a).divide(cos(a));
    }

    protected static Dfp atanInternal(Dfp a) {
        Dfp y = new Dfp(a);
        Dfp x = new Dfp(y);
        Dfp py = new Dfp(y);
        for (int i = 3; i < 90; i += 2) {
            x = x.multiply(a).multiply(a).negate();
            y = y.add(x.divide(i));
            if (y.equals(py)) {
                break;
            }
            py = new Dfp(y);
        }
        return y;
    }

    public static Dfp atan(Dfp a) {
        char c;
        Dfp dfp = a;
        Dfp zero = a.getField().getZero();
        Dfp one = a.getField().getOne();
        Dfp[] sqr2Split = a.getField().getSqr2Split();
        Dfp[] piSplit = a.getField().getPiSplit();
        boolean recp = false;
        boolean neg = false;
        boolean sub = false;
        Dfp ty = sqr2Split[0].subtract(one).add(sqr2Split[1]);
        Dfp x = new Dfp(dfp);
        if (x.lessThan(zero)) {
            neg = true;
            x = x.negate();
        }
        if (x.greaterThan(one)) {
            recp = true;
            x = one.divide(x);
        }
        if (x.greaterThan(ty)) {
            sub = true;
            Dfp[] sty = {sqr2Split[0].subtract(one), sqr2Split[1]};
            Dfp[] xs = split(x);
            Dfp[] ds = splitMult(xs, sty);
            ds[0] = ds[0].add(one);
            xs[0] = xs[0].subtract(sty[0]);
            xs[1] = xs[1].subtract(sty[1]);
            Dfp[] xs2 = splitDiv(xs, ds);
            x = xs2[0].add(xs2[1]);
        }
        Dfp y = atanInternal(x);
        if (sub) {
            c = 1;
            y = y.add(piSplit[0].divide(8)).add(piSplit[1].divide(8));
        } else {
            c = 1;
        }
        if (recp) {
            y = piSplit[0].divide(2).subtract(y).add(piSplit[c].divide(2));
        }
        if (neg) {
            y = y.negate();
        }
        return dfp.newInstance(y);
    }

    public static Dfp asin(Dfp a) {
        return atan(a.divide(a.getOne().subtract(a.multiply(a)).sqrt()));
    }

    public static Dfp acos(Dfp a) {
        boolean negative = false;
        if (a.lessThan(a.getZero())) {
            negative = true;
        }
        Dfp a2 = Dfp.copysign(a, a.getOne());
        Dfp result = atan(a2.getOne().subtract(a2.multiply(a2)).sqrt().divide(a2));
        if (negative) {
            result = a2.getField().getPi().subtract(result);
        }
        return a2.newInstance(result);
    }
}
