package org.apache.commons.math3.special;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.FastMath;

public class Gamma {
    private static final double C_LIMIT = 49.0d;
    private static final double DEFAULT_EPSILON = 1.0E-14d;
    public static final double GAMMA = 0.5772156649015329d;
    private static final double HALF_LOG_2_PI = (FastMath.log(6.283185307179586d) * 0.5d);
    private static final double INV_GAMMA1P_M1_A0 = 6.116095104481416E-9d;
    private static final double INV_GAMMA1P_M1_A1 = 6.247308301164655E-9d;
    private static final double INV_GAMMA1P_M1_B1 = 0.203610414066807d;
    private static final double INV_GAMMA1P_M1_B2 = 0.026620534842894922d;
    private static final double INV_GAMMA1P_M1_B3 = 4.939449793824468E-4d;
    private static final double INV_GAMMA1P_M1_B4 = -8.514194324403149E-6d;
    private static final double INV_GAMMA1P_M1_B5 = -6.4304548177935305E-6d;
    private static final double INV_GAMMA1P_M1_B6 = 9.926418406727737E-7d;
    private static final double INV_GAMMA1P_M1_B7 = -6.077618957228252E-8d;
    private static final double INV_GAMMA1P_M1_B8 = 1.9575583661463974E-10d;
    private static final double INV_GAMMA1P_M1_C = -0.42278433509846713d;
    private static final double INV_GAMMA1P_M1_C0 = 0.5772156649015329d;
    private static final double INV_GAMMA1P_M1_C1 = -0.6558780715202539d;
    private static final double INV_GAMMA1P_M1_C10 = -2.013485478078824E-5d;
    private static final double INV_GAMMA1P_M1_C11 = -1.2504934821426706E-6d;
    private static final double INV_GAMMA1P_M1_C12 = 1.133027231981696E-6d;
    private static final double INV_GAMMA1P_M1_C13 = -2.056338416977607E-7d;
    private static final double INV_GAMMA1P_M1_C2 = -0.04200263503409524d;
    private static final double INV_GAMMA1P_M1_C3 = 0.16653861138229148d;
    private static final double INV_GAMMA1P_M1_C4 = -0.04219773455554433d;
    private static final double INV_GAMMA1P_M1_C5 = -0.009621971527876973d;
    private static final double INV_GAMMA1P_M1_C6 = 0.0072189432466631d;
    private static final double INV_GAMMA1P_M1_C7 = -0.0011651675918590652d;
    private static final double INV_GAMMA1P_M1_C8 = -2.1524167411495098E-4d;
    private static final double INV_GAMMA1P_M1_C9 = 1.280502823881162E-4d;
    private static final double INV_GAMMA1P_M1_P0 = 6.116095104481416E-9d;
    private static final double INV_GAMMA1P_M1_P1 = 6.8716741130671986E-9d;
    private static final double INV_GAMMA1P_M1_P2 = 6.820161668496171E-10d;
    private static final double INV_GAMMA1P_M1_P3 = 4.686843322948848E-11d;
    private static final double INV_GAMMA1P_M1_P4 = 1.5728330277104463E-12d;
    private static final double INV_GAMMA1P_M1_P5 = -1.2494415722763663E-13d;
    private static final double INV_GAMMA1P_M1_P6 = 4.343529937408594E-15d;
    private static final double INV_GAMMA1P_M1_Q1 = 0.3056961078365221d;
    private static final double INV_GAMMA1P_M1_Q2 = 0.054642130860422966d;
    private static final double INV_GAMMA1P_M1_Q3 = 0.004956830093825887d;
    private static final double INV_GAMMA1P_M1_Q4 = 2.6923694661863613E-4d;
    private static final double[] LANCZOS = {0.9999999999999971d, 57.15623566586292d, -59.59796035547549d, 14.136097974741746d, -0.4919138160976202d, 3.399464998481189E-5d, 4.652362892704858E-5d, -9.837447530487956E-5d, 1.580887032249125E-4d, -2.1026444172410488E-4d, 2.1743961811521265E-4d, -1.643181065367639E-4d, 8.441822398385275E-5d, -2.6190838401581408E-5d, 3.6899182659531625E-6d};
    public static final double LANCZOS_G = 4.7421875d;
    private static final double SQRT_TWO_PI = 2.5066282746310007d;
    private static final double S_LIMIT = 1.0E-5d;

    private Gamma() {
    }

    public static double logGamma(double x) {
        double ret;
        if (Double.isNaN(x) || x <= 0.0d) {
            ret = Double.NaN;
        } else if (x < 0.5d) {
            return logGamma1p(x) - FastMath.log(x);
        } else {
            if (x <= 2.5d) {
                return logGamma1p((x - 0.5d) - 0.5d);
            }
            if (x <= 8.0d) {
                int n = (int) FastMath.floor(x - 1.5d);
                double prod = 1.0d;
                for (int i = 1; i <= n; i++) {
                    prod *= x - ((double) i);
                }
                return logGamma1p(x - ((double) (n + 1))) + FastMath.log(prod);
            }
            double tmp = 4.7421875d + x + 0.5d;
            ret = (((0.5d + x) * FastMath.log(tmp)) - tmp) + HALF_LOG_2_PI + FastMath.log(lanczos(x) / x);
        }
        return ret;
    }

    public static double regularizedGammaP(double a, double x) {
        return regularizedGammaP(a, x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaP(double a, double x, double epsilon, int maxIterations) {
        double d = x;
        int i = maxIterations;
        if (Double.isNaN(a) || Double.isNaN(x) || a <= 0.0d || d < 0.0d) {
            return Double.NaN;
        }
        if (d == 0.0d) {
            return 0.0d;
        }
        if (d >= a + 1.0d) {
            return 1.0d - regularizedGammaQ(a, x, epsilon, maxIterations);
        }
        double sum = 1.0d / a;
        double n = 0.0d;
        double an = sum;
        while (FastMath.abs(an / sum) > epsilon && n < ((double) i) && sum < Double.POSITIVE_INFINITY) {
            n += 1.0d;
            an *= d / (a + n);
            sum += an;
        }
        if (n >= ((double) i)) {
            throw new MaxCountExceededException(Integer.valueOf(maxIterations));
        } else if (Double.isInfinite(sum)) {
            return 1.0d;
        } else {
            return FastMath.exp(((-d) + (FastMath.log(x) * a)) - logGamma(a)) * sum;
        }
    }

    public static double regularizedGammaQ(double a, double x) {
        return regularizedGammaQ(a, x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    public static double regularizedGammaQ(final double a, double x, double epsilon, int maxIterations) {
        if (Double.isNaN(a) || Double.isNaN(x) || a <= 0.0d || x < 0.0d) {
            return Double.NaN;
        }
        if (x == 0.0d) {
            return 1.0d;
        }
        if (x < a + 1.0d) {
            return 1.0d - regularizedGammaP(a, x, epsilon, maxIterations);
        }
        return (1.0d / new ContinuedFraction() {
            /* access modifiers changed from: protected */
            public double getA(int n, double x) {
                return (((((double) n) * 2.0d) + 1.0d) - a) + x;
            }

            /* access modifiers changed from: protected */
            public double getB(int n, double x) {
                return ((double) n) * (a - ((double) n));
            }
        }.evaluate(x, epsilon, maxIterations)) * FastMath.exp(((-x) + (FastMath.log(x) * a)) - logGamma(a));
    }

    public static double digamma(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return x;
        }
        if (x > 0.0d && x <= 1.0E-5d) {
            return -0.5772156649015329d - (1.0d / x);
        }
        if (x < C_LIMIT) {
            return digamma(x + 1.0d) - (1.0d / x);
        }
        double inv = 1.0d / (x * x);
        return (FastMath.log(x) - (0.5d / x)) - ((((0.008333333333333333d - (inv / 252.0d)) * inv) + 0.08333333333333333d) * inv);
    }

    public static double trigamma(double x) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return x;
        }
        if (x > 0.0d && x <= 1.0E-5d) {
            return 1.0d / (x * x);
        }
        if (x < C_LIMIT) {
            return trigamma(x + 1.0d) + (1.0d / (x * x));
        }
        double inv = 1.0d / (x * x);
        return (1.0d / x) + (inv / 2.0d) + ((inv / x) * (0.16666666666666666d - (((inv / 42.0d) + 0.03333333333333333d) * inv)));
    }

    public static double lanczos(double x) {
        double sum = 0.0d;
        for (int i = LANCZOS.length - 1; i > 0; i--) {
            sum += LANCZOS[i] / (((double) i) + x);
        }
        return LANCZOS[0] + sum;
    }

    public static double invGamma1pm1(double x) {
        if (x < -0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(x), Double.valueOf(-0.5d), true);
        } else if (x > 1.5d) {
            throw new NumberIsTooLargeException(Double.valueOf(x), Double.valueOf(1.5d), true);
        } else {
            double t = x <= 0.5d ? x : (x - 0.5d) - 0.5d;
            if (t < 0.0d) {
                double c = (t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * (((((INV_GAMMA1P_M1_A1 * t) + 6.116095104481416E-9d) / ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * INV_GAMMA1P_M1_B8) + INV_GAMMA1P_M1_B7)) + INV_GAMMA1P_M1_B6)) + INV_GAMMA1P_M1_B5)) + INV_GAMMA1P_M1_B4)) + INV_GAMMA1P_M1_B3)) + INV_GAMMA1P_M1_B2)) + INV_GAMMA1P_M1_B1)) + 1.0d)) * t) + INV_GAMMA1P_M1_C13)) + INV_GAMMA1P_M1_C12)) + INV_GAMMA1P_M1_C11)) + INV_GAMMA1P_M1_C10)) + INV_GAMMA1P_M1_C9)) + INV_GAMMA1P_M1_C8)) + INV_GAMMA1P_M1_C7)) + INV_GAMMA1P_M1_C6)) + INV_GAMMA1P_M1_C5)) + INV_GAMMA1P_M1_C4)) + INV_GAMMA1P_M1_C3)) + INV_GAMMA1P_M1_C2)) + INV_GAMMA1P_M1_C1)) + INV_GAMMA1P_M1_C;
                if (x > 0.5d) {
                    return (t * c) / x;
                }
                return x * (c + 0.5d + 0.5d);
            }
            double c2 = (t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * ((t * (((((t * ((t * ((t * ((t * ((t * ((t * INV_GAMMA1P_M1_P6) + INV_GAMMA1P_M1_P5)) + INV_GAMMA1P_M1_P4)) + INV_GAMMA1P_M1_P3)) + INV_GAMMA1P_M1_P2)) + INV_GAMMA1P_M1_P1)) + 6.116095104481416E-9d) / ((t * ((t * ((t * ((t * INV_GAMMA1P_M1_Q4) + INV_GAMMA1P_M1_Q3)) + INV_GAMMA1P_M1_Q2)) + INV_GAMMA1P_M1_Q1)) + 1.0d)) * t) + INV_GAMMA1P_M1_C13)) + INV_GAMMA1P_M1_C12)) + INV_GAMMA1P_M1_C11)) + INV_GAMMA1P_M1_C10)) + INV_GAMMA1P_M1_C9)) + INV_GAMMA1P_M1_C8)) + INV_GAMMA1P_M1_C7)) + INV_GAMMA1P_M1_C6)) + INV_GAMMA1P_M1_C5)) + INV_GAMMA1P_M1_C4)) + INV_GAMMA1P_M1_C3)) + INV_GAMMA1P_M1_C2)) + INV_GAMMA1P_M1_C1)) + 0.5772156649015329d;
            if (x > 0.5d) {
                return (t / x) * ((c2 - 0.5d) - 0.5d);
            }
            return x * c2;
        }
    }

    public static double logGamma1p(double x) throws NumberIsTooSmallException, NumberIsTooLargeException {
        if (x < -0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(x), Double.valueOf(-0.5d), true);
        } else if (x <= 1.5d) {
            return -FastMath.log1p(invGamma1pm1(x));
        } else {
            throw new NumberIsTooLargeException(Double.valueOf(x), Double.valueOf(1.5d), true);
        }
    }

    public static double gamma(double x) {
        double prod;
        if (x == FastMath.rint(x) && x <= 0.0d) {
            return Double.NaN;
        }
        double absX = FastMath.abs(x);
        if (absX > 20.0d) {
            double y = 4.7421875d + absX + 0.5d;
            double gammaAbs = (SQRT_TWO_PI / absX) * FastMath.pow(y, 0.5d + absX) * FastMath.exp(-y) * lanczos(absX);
            if (x > 0.0d) {
                prod = gammaAbs;
            } else {
                prod = -3.141592653589793d / ((FastMath.sin(3.141592653589793d * x) * x) * gammaAbs);
            }
        } else if (x >= 1.0d) {
            double prod2 = 1.0d;
            double t = x;
            while (t > 2.5d) {
                t -= 1.0d;
                prod2 *= t;
            }
            prod = prod2 / (invGamma1pm1(t - 1.0d) + 1.0d);
        } else {
            double t2 = x;
            double prod3 = t2;
            while (t2 < -0.5d) {
                t2 += 1.0d;
                prod3 *= t2;
            }
            prod = 1.0d / ((invGamma1pm1(t2) + 1.0d) * prod3);
        }
        return prod;
    }
}
