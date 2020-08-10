package org.apache.commons.math3.special;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.FastMath;

public class Beta {
    private static final double DEFAULT_EPSILON = 1.0E-14d;
    private static final double[] DELTA = {0.08333333333333333d, -2.777777777777778E-5d, 7.936507936507937E-8d, -5.952380952380953E-10d, 8.417508417508329E-12d, -1.917526917518546E-13d, 6.410256405103255E-15d, -2.955065141253382E-16d, 1.7964371635940225E-17d, -1.3922896466162779E-18d, 1.338028550140209E-19d, -1.542460098679661E-20d, 1.9770199298095743E-21d, -2.3406566479399704E-22d, 1.713480149663986E-23d};
    private static final double HALF_LOG_TWO_PI = 0.9189385332046727d;

    private Beta() {
    }

    public static double regularizedBeta(double x, double a, double b) {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a, double b, double epsilon) {
        return regularizedBeta(x, a, b, epsilon, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double x, double a, double b, int maxIterations) {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, maxIterations);
    }

    public static double regularizedBeta(double x, double a, double b, double epsilon, int maxIterations) {
        double ret;
        double d = x;
        final double d2 = a;
        final double d3 = b;
        if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b) || d < 0.0d || d > 1.0d || d2 <= 0.0d || d3 <= 0.0d) {
            ret = Double.NaN;
        } else if (d <= (d2 + 1.0d) / ((d3 + 2.0d) + d2) || 1.0d - d > (d3 + 1.0d) / ((2.0d + d3) + d2)) {
            ret = (FastMath.exp((((FastMath.log(x) * d2) + (FastMath.log1p(-d) * d3)) - FastMath.log(a)) - logBeta(a, b)) * 1.0d) / new ContinuedFraction() {
                /* access modifiers changed from: protected */
                public double getB(int n, double x) {
                    double ret;
                    int i = n;
                    if (i % 2 == 0) {
                        double m = ((double) i) / 2.0d;
                        ret = (((d3 - m) * m) * x) / (((d2 + (m * 2.0d)) - 1.0d) * (d2 + (2.0d * m)));
                    } else {
                        double m2 = (((double) i) - 1.0d) / 2.0d;
                        ret = (-(((d2 + m2) * ((d2 + d3) + m2)) * x)) / ((d2 + (m2 * 2.0d)) * ((d2 + (2.0d * m2)) + 1.0d));
                    }
                    return ret;
                }

                /* access modifiers changed from: protected */
                public double getA(int n, double x) {
                    return 1.0d;
                }
            }.evaluate(d, epsilon, maxIterations);
        } else {
            ret = 1.0d - regularizedBeta(1.0d - d, d3, d2, epsilon, maxIterations);
        }
        return ret;
    }

    @Deprecated
    public static double logBeta(double a, double b, double epsilon, int maxIterations) {
        return logBeta(a, b);
    }

    private static double logGammaSum(double a, double b) throws OutOfRangeException {
        if (a < 1.0d || a > 2.0d) {
            throw new OutOfRangeException(Double.valueOf(a), Double.valueOf(1.0d), Double.valueOf(2.0d));
        } else if (b < 1.0d || b > 2.0d) {
            throw new OutOfRangeException(Double.valueOf(b), Double.valueOf(1.0d), Double.valueOf(2.0d));
        } else {
            double x = (a - 1.0d) + (b - 1.0d);
            if (x <= 0.5d) {
                return Gamma.logGamma1p(1.0d + x);
            }
            if (x <= 1.5d) {
                return Gamma.logGamma1p(x) + FastMath.log1p(x);
            }
            return Gamma.logGamma1p(x - 1.0d) + FastMath.log((1.0d + x) * x);
        }
    }

    private static double logGammaMinusLogGammaSum(double a, double b) throws NumberIsTooSmallException {
        double w;
        double d;
        if (a < 0.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(a), Double.valueOf(0.0d), true);
        } else if (b < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(b), Double.valueOf(10.0d), true);
        } else {
            if (a <= b) {
                d = (a - 0.5d) + b;
                w = deltaMinusDeltaSum(a, b);
            } else {
                d = (b - 0.5d) + a;
                w = deltaMinusDeltaSum(b, a);
            }
            double u = FastMath.log1p(a / b) * d;
            double v = (FastMath.log(b) - 1.0d) * a;
            return u <= v ? (w - u) - v : (w - v) - u;
        }
    }

    private static double deltaMinusDeltaSum(double a, double b) throws OutOfRangeException, NumberIsTooSmallException {
        if (a < 0.0d || a > b) {
            throw new OutOfRangeException(Double.valueOf(a), Integer.valueOf(0), Double.valueOf(b));
        } else if (b < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(b), Integer.valueOf(10), true);
        } else {
            double h = a / b;
            double p = h / (h + 1.0d);
            double q = 1.0d / (h + 1.0d);
            double q2 = q * q;
            double[] s = new double[DELTA.length];
            s[0] = 1.0d;
            for (int i = 1; i < s.length; i++) {
                s[i] = q + (s[i - 1] * q2) + 1.0d;
            }
            double sqrtT = 10.0d / b;
            double t = sqrtT * sqrtT;
            double w = DELTA[DELTA.length - 1] * s[s.length - 1];
            for (int i2 = DELTA.length - 2; i2 >= 0; i2--) {
                w = (t * w) + (DELTA[i2] * s[i2]);
            }
            return (w * p) / b;
        }
    }

    private static double sumDeltaMinusDeltaSum(double p, double q) {
        if (p < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(p), Double.valueOf(10.0d), true);
        } else if (q < 10.0d) {
            throw new NumberIsTooSmallException(Double.valueOf(q), Double.valueOf(10.0d), true);
        } else {
            double a = FastMath.min(p, q);
            double b = FastMath.max(p, q);
            double sqrtT = 10.0d / a;
            double t = sqrtT * sqrtT;
            double z = DELTA[DELTA.length - 1];
            for (int i = DELTA.length - 2; i >= 0; i--) {
                z = (t * z) + DELTA[i];
            }
            return (z / a) + deltaMinusDeltaSum(a, b);
        }
    }

    public static double logBeta(double p, double q) {
        if (Double.isNaN(p) || Double.isNaN(q) || p <= 0.0d || q <= 0.0d) {
            return Double.NaN;
        }
        double a = FastMath.min(p, q);
        double b = FastMath.max(p, q);
        if (a >= 10.0d) {
            double w = sumDeltaMinusDeltaSum(a, b);
            double h = a / b;
            double u = (-(a - 0.5d)) * FastMath.log(h / (1.0d + h));
            double v = FastMath.log1p(h) * b;
            if (u <= v) {
                return ((((FastMath.log(b) * -0.5d) + HALF_LOG_TWO_PI) + w) - u) - v;
            }
            return ((((FastMath.log(b) * -0.5d) + HALF_LOG_TWO_PI) + w) - v) - u;
        }
        if (a > 2.0d) {
            if (b > 1000.0d) {
                int n = (int) FastMath.floor(a - 1.0d);
                double prod = 1.0d;
                double ared = a;
                for (int i = 0; i < n; i++) {
                    ared -= 1.0d;
                    prod *= ared / ((ared / b) + 1.0d);
                }
                return (FastMath.log(prod) - (((double) n) * FastMath.log(b))) + Gamma.logGamma(ared) + logGammaMinusLogGammaSum(ared, b);
            }
            double prod1 = 1.0d;
            double ared2 = a;
            while (ared2 > 2.0d) {
                ared2 -= 1.0d;
                double h2 = ared2 / b;
                prod1 *= h2 / (h2 + 1.0d);
            }
            if (b >= 10.0d) {
                return FastMath.log(prod1) + Gamma.logGamma(ared2) + logGammaMinusLogGammaSum(ared2, b);
            }
            double prod2 = 1.0d;
            double bred = b;
            for (double u2 = 2.0d; bred > u2; u2 = 2.0d) {
                bred -= 1.0d;
                prod2 *= bred / (ared2 + bred);
            }
            return FastMath.log(prod1) + FastMath.log(prod2) + Gamma.logGamma(ared2) + (Gamma.logGamma(bred) - logGammaSum(ared2, bred));
        } else if (a >= 1.0d) {
            if (b <= 2.0d) {
                return (Gamma.logGamma(a) + Gamma.logGamma(b)) - logGammaSum(a, b);
            }
            if (b >= 10.0d) {
                return Gamma.logGamma(a) + logGammaMinusLogGammaSum(a, b);
            }
            double prod3 = 1.0d;
            double bred2 = b;
            while (bred2 > 2.0d) {
                bred2 -= 1.0d;
                prod3 *= bred2 / (a + bred2);
            }
            return FastMath.log(prod3) + Gamma.logGamma(a) + (Gamma.logGamma(bred2) - logGammaSum(a, bred2));
        } else if (b >= 10.0d) {
            return Gamma.logGamma(a) + logGammaMinusLogGammaSum(a, b);
        } else {
            return FastMath.log((Gamma.gamma(a) * Gamma.gamma(b)) / Gamma.gamma(a + b));
        }
    }
}
