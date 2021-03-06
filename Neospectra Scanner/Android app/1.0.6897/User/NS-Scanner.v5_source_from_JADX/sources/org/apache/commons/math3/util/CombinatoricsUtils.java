package org.apache.commons.math3.util;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public final class CombinatoricsUtils {
    static final long[] FACTORIALS = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final AtomicReference<long[][]> STIRLING_S2 = new AtomicReference<>(null);

    private CombinatoricsUtils() {
    }

    public static long binomialCoefficient(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 1;
        }
        int j = 1;
        if (k == 1 || k == n - 1) {
            return (long) n;
        }
        if (k > n / 2) {
            return binomialCoefficient(n, n - k);
        }
        long result = 1;
        if (n <= 61) {
            int i = (n - k) + 1;
            while (j <= k) {
                result = (((long) i) * result) / ((long) j);
                i++;
                j++;
            }
        } else if (n <= 66) {
            int i2 = (n - k) + 1;
            while (j <= k) {
                long d = (long) ArithmeticUtils.gcd(i2, j);
                result = (result / (((long) j) / d)) * (((long) i2) / d);
                i2++;
                j++;
            }
        } else {
            int i3 = (n - k) + 1;
            while (j <= k) {
                long d2 = (long) ArithmeticUtils.gcd(i3, j);
                result = ArithmeticUtils.mulAndCheck(result / (((long) j) / d2), ((long) i3) / d2);
                i3++;
                j++;
            }
        }
        return result;
    }

    public static double binomialCoefficientDouble(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 1.0d;
        }
        if (k == 1 || k == n - 1) {
            return (double) n;
        }
        if (k > n / 2) {
            return binomialCoefficientDouble(n, n - k);
        }
        if (n < 67) {
            return (double) binomialCoefficient(n, k);
        }
        double result = 1.0d;
        for (int i = 1; i <= k; i++) {
            result *= ((double) ((n - k) + i)) / ((double) i);
        }
        return FastMath.floor(0.5d + result);
    }

    public static double binomialCoefficientLog(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        checkBinomial(n, k);
        if (n == k || k == 0) {
            return 0.0d;
        }
        if (k == 1 || k == n - 1) {
            return FastMath.log((double) n);
        }
        if (n < 67) {
            return FastMath.log((double) binomialCoefficient(n, k));
        }
        if (n < 1030) {
            return FastMath.log(binomialCoefficientDouble(n, k));
        }
        if (k > n / 2) {
            return binomialCoefficientLog(n, n - k);
        }
        double logSum = 0.0d;
        int i = (n - k) + 1;
        while (true) {
            int i2 = i;
            if (i2 > n) {
                break;
            }
            logSum += FastMath.log((double) i2);
            i = i2 + 1;
        }
        for (int i3 = 2; i3 <= k; i3++) {
            logSum -= FastMath.log((double) i3);
        }
        return logSum;
    }

    public static long factorial(int n) throws NotPositiveException, MathArithmeticException {
        if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
        } else if (n <= 20) {
            return FACTORIALS[n];
        } else {
            throw new MathArithmeticException();
        }
    }

    public static double factorialDouble(int n) throws NotPositiveException {
        if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
        } else if (n < 21) {
            return (double) FACTORIALS[n];
        } else {
            return FastMath.floor(FastMath.exp(factorialLog(n)) + 0.5d);
        }
    }

    public static double factorialLog(int n) throws NotPositiveException {
        if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
        } else if (n < 21) {
            return FastMath.log((double) FACTORIALS[n]);
        } else {
            double logSum = 0.0d;
            for (int i = 2; i <= n; i++) {
                logSum += FastMath.log((double) i);
            }
            return logSum;
        }
    }

    public static long stirlingS2(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        int i = n;
        int i2 = k;
        if (i2 < 0) {
            throw new NotPositiveException(Integer.valueOf(k));
        } else if (i2 > i) {
            throw new NumberIsTooLargeException(Integer.valueOf(k), Integer.valueOf(n), true);
        } else {
            long[][] stirlingS2 = (long[][]) STIRLING_S2.get();
            long sign = 1;
            if (stirlingS2 == null) {
                stirlingS2 = new long[26][];
                stirlingS2[0] = new long[]{1};
                for (int i3 = 1; i3 < stirlingS2.length; i3++) {
                    stirlingS2[i3] = new long[(i3 + 1)];
                    stirlingS2[i3][0] = 0;
                    stirlingS2[i3][1] = 1;
                    stirlingS2[i3][i3] = 1;
                    for (int j = 2; j < i3; j++) {
                        stirlingS2[i3][j] = (((long) j) * stirlingS2[i3 - 1][j]) + stirlingS2[i3 - 1][j - 1];
                    }
                }
                STIRLING_S2.compareAndSet(null, stirlingS2);
            }
            if (i < stirlingS2.length) {
                return stirlingS2[i][i2];
            }
            if (i2 == 0) {
                return 0;
            }
            if (i2 == 1) {
            } else if (i2 == i) {
                long[][] jArr = stirlingS2;
            } else if (i2 == 2) {
                return (1 << (i - 1)) - 1;
            } else {
                if (i2 == i - 1) {
                    return binomialCoefficient(i, 2);
                }
                if ((i2 & 1) != 0) {
                    sign = -1;
                }
                long sum = 0;
                long sign2 = sign;
                int j2 = 1;
                while (j2 <= i2) {
                    sign2 = -sign2;
                    long[][] stirlingS22 = stirlingS2;
                    sum += binomialCoefficient(i2, j2) * sign2 * ((long) ArithmeticUtils.pow(j2, i));
                    if (sum < 0) {
                        throw new MathArithmeticException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, Integer.valueOf(n), Integer.valueOf(0), Integer.valueOf(stirlingS22.length - 1));
                    }
                    j2++;
                    stirlingS2 = stirlingS22;
                }
                return sum / factorial(k);
            }
            return 1;
        }
    }

    public static Iterator<int[]> combinationsIterator(int n, int k) {
        return new Combinations(n, k).iterator();
    }

    public static void checkBinomial(int n, int k) throws NumberIsTooLargeException, NotPositiveException {
        if (n < k) {
            throw new NumberIsTooLargeException(LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, Integer.valueOf(k), Integer.valueOf(n), true);
        } else if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, Integer.valueOf(n));
        }
    }
}
