package org.apache.commons.math3.special;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class BesselJ implements UnivariateFunction {
    private static final double ENMTEN = 8.9E-308d;
    private static final double ENSIG = 1.0E16d;
    private static final double ENTEN = 1.0E308d;
    private static final double[] FACT = {1.0d, 1.0d, 2.0d, 6.0d, 24.0d, 120.0d, 720.0d, 5040.0d, 40320.0d, 362880.0d, 3628800.0d, 3.99168E7d, 4.790016E8d, 6.2270208E9d, 8.71782912E10d, 1.307674368E12d, 2.0922789888E13d, 3.55687428096E14d, 6.402373705728E15d, 1.21645100408832E17d, 2.43290200817664E18d, 5.109094217170944E19d, 1.1240007277776077E21d, 2.585201673888498E22d, 6.204484017332394E23d};
    private static final double PI2 = 0.6366197723675814d;
    private static final double RTNSIG = 1.0E-4d;
    private static final double TOWPI1 = 6.28125d;
    private static final double TWOPI = 6.283185307179586d;
    private static final double TWOPI2 = 0.001935307179586477d;
    private static final double X_MAX = 10000.0d;
    private static final double X_MIN = 0.0d;
    private final double order;

    public static class BesselJResult {
        /* access modifiers changed from: private */
        public final int nVals;
        /* access modifiers changed from: private */
        public final double[] vals;

        public BesselJResult(double[] b, int n) {
            this.vals = MathArrays.copyOf(b, b.length);
            this.nVals = n;
        }

        public double[] getVals() {
            return MathArrays.copyOf(this.vals, this.vals.length);
        }

        public int getnVals() {
            return this.nVals;
        }
    }

    public BesselJ(double order2) {
        this.order = order2;
    }

    public double value(double x) throws MathIllegalArgumentException, ConvergenceException {
        return value(this.order, x);
    }

    public static double value(double order2, double x) throws MathIllegalArgumentException, ConvergenceException {
        double d = order2;
        int n = (int) d;
        int nb = n + 1;
        BesselJResult res = rjBesl(x, d - ((double) n), nb);
        if (res.nVals >= nb) {
            return res.vals[n];
        }
        if (res.nVals < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.BESSEL_FUNCTION_BAD_ARGUMENT, Double.valueOf(order2), Double.valueOf(x));
        } else if (FastMath.abs(res.vals[res.nVals - 1]) < 1.0E-100d) {
            return res.vals[n];
        } else {
            throw new ConvergenceException(LocalizedFormats.BESSEL_FUNCTION_FAILED_CONVERGENCE, Double.valueOf(order2), Double.valueOf(x));
        }
    }

    public static BesselJResult rjBesl(double x, double alpha, int nb) {
        int ncalc;
        int i;
        double alpem;
        boolean readyToInitialize;
        int n;
        double test;
        double pold;
        int m;
        double tempa;
        double d = x;
        double d2 = alpha;
        int i2 = nb;
        double[] b = new double[i2];
        int magx = (int) d;
        if (i2 <= 0 || d < 0.0d || d > X_MAX || d2 < 0.0d || d2 >= 1.0d) {
            int i3 = magx;
            if (b.length > 0) {
                i = 0;
                b[0] = 0.0d;
            } else {
                i = 0;
            }
            ncalc = FastMath.min(i2, i) - 1;
        } else {
            ncalc = i2;
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 >= i2) {
                    break;
                }
                b[i5] = 0;
                i4 = i5 + 1;
            }
            if (d < RTNSIG) {
                double tempa2 = 1.0d;
                alpem = d2 + 1.0d;
                double halfx = 0.0d;
                if (d > ENMTEN) {
                    halfx = d * 0.5d;
                }
                double halfx2 = halfx;
                if (d2 != 0.0d) {
                    tempa2 = FastMath.pow(halfx2, d2) / (Gamma.gamma(alpha) * d2);
                }
                double tempb = 0.0d;
                if (d + 1.0d > 1.0d) {
                    tempb = (-halfx2) * halfx2;
                }
                b[0] = tempa + ((tempa * tempb) / alpem);
                if (d != 0.0d && b[0] == 0.0d) {
                    ncalc = 0;
                }
                if (i2 != 1) {
                    if (d > 0.0d) {
                        double tempc = halfx2;
                        double tover = tempb != 0.0d ? ENMTEN / tempb : 1.78E-307d / d;
                        int n2 = 1;
                        while (true) {
                            int n3 = n2;
                            if (n3 >= i2) {
                                break;
                            }
                            double tempa3 = tempa / alpem;
                            alpem += 1.0d;
                            tempa = tempa3 * tempc;
                            if (tempa <= tover * alpem) {
                                tempa = 0.0d;
                            }
                            b[n3] = tempa + ((tempa * tempb) / alpem);
                            if (b[n3] == 0.0d && ncalc > n3) {
                                ncalc = n3;
                            }
                            n2 = n3 + 1;
                        }
                    } else {
                        int n4 = 1;
                        while (true) {
                            int n5 = n4;
                            if (n5 >= i2) {
                                break;
                            }
                            b[n5] = 0;
                            n4 = n5 + 1;
                        }
                    }
                }
                int i6 = magx;
            } else if (d <= 25.0d || i2 > magx + 1) {
                int magx2 = magx;
                int nbmx = i2 - magx2;
                int k = magx2 + 1;
                double pold2 = (((double) k) + d2) * 2.0d;
                double en = 1.0d;
                double plast = pold2 / d;
                double test2 = 2.0E16d;
                boolean readyToInitialize2 = false;
                if (nbmx >= 3) {
                    int nend = magx2 + 2;
                    int nend2 = i2 - 1;
                    double d3 = pold2;
                    double d4 = 2.0d;
                    double p = plast;
                    double plast2 = 1.0d;
                    double en2 = (((double) (nend - 1)) + d2) * 2.0d;
                    int n6 = k;
                    int k2 = nend;
                    while (true) {
                        if (k2 > nend2) {
                            int i7 = nbmx;
                            k = n6;
                            pold2 = en2;
                            en = plast2;
                            plast = p;
                            n = ncalc;
                            test = 2.0E16d;
                            break;
                        }
                        n6 = k2;
                        en2 += d4;
                        double pold3 = plast2;
                        plast2 = p;
                        p = ((en2 * plast2) / d) - pold3;
                        if (p > 1.0E292d) {
                            double p2 = p / ENTEN;
                            double plast3 = plast2 / ENTEN;
                            double psave = p2;
                            double psavel = plast3;
                            int nstart = n6 + 1;
                            do {
                                n6++;
                                en2 += 2.0d;
                                pold = plast3;
                                plast3 = p2;
                                p2 = ((en2 * plast3) / d) - pold;
                            } while (p2 <= 1.0d);
                            double tempb2 = en2 / d;
                            double test3 = ((pold * plast3) * (0.5d - (0.5d / (tempb2 * tempb2)))) / ENSIG;
                            double p3 = plast3 * ENTEN;
                            int n7 = n6 - 1;
                            double en3 = en2 - 2.0d;
                            int nend3 = FastMath.min(i2, n7);
                            int l = nstart;
                            while (true) {
                                if (l > nend3) {
                                    break;
                                }
                                double pold4 = psavel;
                                psavel = psave;
                                psave = ((en3 * psavel) / d) - pold4;
                                if (psave * psavel > test3) {
                                    int ncalc2 = l - 1;
                                    break;
                                }
                                l++;
                            }
                            int i8 = nbmx;
                            readyToInitialize2 = true;
                            k = n7;
                            pold2 = en3;
                            en = plast3;
                            plast = p3;
                            n = nend3;
                            nend2 = nend3;
                            nend = nstart;
                            test = test3;
                        } else {
                            k2++;
                            d4 = 2.0d;
                        }
                    }
                    if (!readyToInitialize2) {
                        int n8 = nend2;
                        int i9 = nend;
                        int i10 = nend2;
                        pold2 = (((double) n8) + d2) * 2.0d;
                        readyToInitialize = readyToInitialize2;
                        int n9 = n8;
                        test2 = FastMath.max(test, FastMath.sqrt(en * ENSIG) * FastMath.sqrt(plast * 2.0d));
                        ncalc = n;
                        k = n9;
                    } else {
                        int nstart2 = nend;
                        int i11 = nend2;
                        readyToInitialize = readyToInitialize2;
                        test2 = test;
                        ncalc = n;
                    }
                } else {
                    double d5 = pold2;
                    readyToInitialize = false;
                }
                if (!readyToInitialize) {
                    do {
                        k++;
                        pold2 += 2.0d;
                        double pold5 = en;
                        en = plast;
                        plast = ((pold2 * en) / d) - pold5;
                    } while (plast < test2);
                }
                int n10 = k + 1;
                double en4 = pold2 + 2.0d;
                double tempa4 = 1.0d / plast;
                int m2 = (n10 * 2) - ((n10 / 2) * 4);
                double sum = 0;
                int ncalc3 = ncalc;
                double tempb3 = 0.0d;
                double em = (double) (n10 / 2);
                double sum2 = (em - 1.0d) + d2;
                double alp2em = (em * 2.0d) + d2;
                if (m2 != 0) {
                    sum = ((tempa4 * sum2) * alp2em) / em;
                }
                int nend4 = n10 - i2;
                boolean readyToNormalize = false;
                boolean calculatedB0 = false;
                double sum3 = sum;
                double em2 = em;
                for (int l2 = 1; l2 <= nend4; l2++) {
                    n10--;
                    en4 -= 2.0d;
                    double tempc2 = tempb3;
                    tempb3 = tempa4;
                    tempa4 = ((en4 * tempb3) / d) - tempc2;
                    m2 = 2 - m2;
                    if (m2 != 0) {
                        em2 -= 1.0d;
                        alp2em = (em2 * 2.0d) + d2;
                        if (n10 == 1) {
                            break;
                        }
                        double alpem2 = (em2 - 1.0d) + d2;
                        if (alpem2 == 0.0d) {
                            alpem2 = 1.0d;
                        }
                        sum3 = ((sum3 + (tempa4 * alp2em)) * alpem2) / em2;
                        sum2 = alpem2;
                    }
                }
                b[n10 - 1] = tempa4;
                if (nend4 >= 0) {
                    if (i2 <= 1) {
                        double alp2em2 = d2;
                        if (d2 + 1.0d == 1.0d) {
                            alp2em2 = 1.0d;
                        }
                        alp2em = alp2em2;
                        sum3 += b[0] * alp2em;
                        readyToNormalize = true;
                    } else {
                        n10--;
                        en4 -= 2.0d;
                        b[n10 - 1] = ((en4 * tempa4) / d) - tempb3;
                        if (n10 == 1) {
                            calculatedB0 = true;
                        } else {
                            m2 = 2 - m2;
                            if (m2 != 0) {
                                em2 -= 1.0d;
                                alp2em = (em2 * 2.0d) + d2;
                                double alpem3 = (em2 - 1.0d) + d2;
                                if (alpem3 == 0.0d) {
                                    alpem3 = 4607182418800017408;
                                }
                                sum2 = alpem3;
                                sum3 = ((sum3 + (b[n10 - 1] * alp2em)) * sum2) / em2;
                            }
                        }
                    }
                }
                if (!readyToNormalize && !calculatedB0) {
                    nend4 = n10 - 2;
                    if (nend4 != 0) {
                        for (int l3 = 1; l3 <= nend4; l3++) {
                            n10--;
                            en4 -= 2.0d;
                            b[n10 - 1] = ((b[n10] * en4) / d) - b[n10 + 1];
                            m2 = 2 - m2;
                            if (m2 != 0) {
                                em2 -= 1.0d;
                                alp2em = (em2 * 2.0d) + d2;
                                double alpem4 = (em2 - 1.0d) + d2;
                                if (alpem4 == 0.0d) {
                                    alpem4 = 1.0d;
                                }
                                sum3 = ((sum3 + (b[n10 - 1] * alp2em)) * alpem4) / em2;
                                sum2 = alpem4;
                            }
                        }
                    }
                }
                if (!readyToNormalize) {
                    if (!calculatedB0) {
                        b[0] = ((((d2 + 1.0d) * 2.0d) * b[1]) / d) - b[2];
                    }
                    double alp2em3 = (2.0d * (em2 - 1.0d)) + d2;
                    if (alp2em3 == 0.0d) {
                        alp2em3 = 1.0d;
                    }
                    alp2em = alp2em3;
                    sum3 += b[0] * alp2em;
                }
                if (FastMath.abs(alpha) > 1.0E-16d) {
                    int i12 = nend4;
                    double d6 = tempa4;
                    sum3 *= Gamma.gamma(alpha) * FastMath.pow(d * 0.5d, -d2);
                } else {
                    double d7 = tempa4;
                }
                double tempa5 = ENMTEN;
                if (sum3 > 1.0d) {
                    tempa5 = ENMTEN * sum3;
                }
                for (int n11 = 0; n11 < i2; n11++) {
                    if (FastMath.abs(b[n11]) < tempa5) {
                        b[n11] = 0.0d;
                    }
                    b[n11] = b[n11] / sum3;
                }
                alpem = sum2;
                double d8 = alp2em;
                ncalc = ncalc3;
            } else {
                int i13 = magx;
                double t = FastMath.sqrt(PI2 / d);
                double mul = 0.125d / d;
                double xin = mul * mul;
                if (d >= 130.0d) {
                    m = 4;
                } else if (d >= 35.0d) {
                    m = 8;
                } else {
                    m = 11;
                }
                double vsin = ((double) m) * 4.0d;
                double t2 = (double) ((int) ((d / 6.283185307179586d) + 0.5d));
                double alpem5 = ((d - (TOWPI1 * t2)) - (TWOPI2 * t2)) - ((d2 + 0.5d) / PI2);
                double vsin2 = FastMath.sin(alpem5);
                double gnu = d2 * 2.0d;
                double d9 = alpem5;
                double vcos = FastMath.cos(alpem5);
                double vcos2 = vsin2;
                double vsin3 = t2;
                int i14 = 1;
                while (i14 <= 2) {
                    double s = ((vsin - 1.0d) - gnu) * ((vsin - 1.0d) + gnu) * xin * 0.5d;
                    double t3 = (gnu - (vsin - 3.0d)) * (gnu + (vsin - 3.0d));
                    double capp = (s * t3) / FACT[m * 2];
                    double capq = (s * ((gnu - (vsin + 1.0d)) * (gnu + (vsin + 1.0d)))) / FACT[(m * 2) + 1];
                    double xk = vsin;
                    double t1 = t3;
                    int k3 = m * 2;
                    for (int j = 2; j <= m; j++) {
                        xk -= 4.0d;
                        double s2 = ((xk - 1.0d) - gnu) * ((xk - 1.0d) + gnu);
                        double t4 = (gnu - (xk - 3.0d)) * (gnu + (xk - 3.0d));
                        capp = (capp + (1.0d / FACT[k3 - 2])) * s2 * t4 * xin;
                        capq = (capq + (1.0d / FACT[k3 - 1])) * s2 * t1 * xin;
                        k3 -= 2;
                        t1 = t4;
                    }
                    b[i14 - 1] = (((capp + 1.0d) * vcos) - ((((capq + 1.0d) * ((gnu * gnu) - 1.0d)) * (0.125d / d)) * vcos2)) * t;
                    if (i2 == 1) {
                        double d10 = t;
                        return new BesselJResult(MathArrays.copyOf(b, b.length), ncalc);
                    }
                    double xc = t;
                    double xc2 = vcos2;
                    double xm = vsin;
                    double vsin4 = -vcos;
                    vcos = xc2;
                    gnu += 2.0d;
                    i14++;
                    double capp2 = xc2;
                    vcos2 = vsin4;
                    t = xc;
                    vsin = xm;
                }
                double xc3 = t;
                double d11 = vsin;
                if (i2 > 2) {
                    double gnu2 = (d2 * 2.0d) + 2.0d;
                    int j2 = 2;
                    while (true) {
                        int j3 = j2;
                        if (j3 >= i2) {
                            break;
                        }
                        b[j3] = ((b[j3 - 1] * gnu2) / d) - b[j3 - 2];
                        gnu2 += 2.0d;
                        j2 = j3 + 1;
                    }
                }
                alpem = 0.0d;
            }
            double d12 = alpem;
        }
        return new BesselJResult(MathArrays.copyOf(b, b.length), ncalc);
    }
}
