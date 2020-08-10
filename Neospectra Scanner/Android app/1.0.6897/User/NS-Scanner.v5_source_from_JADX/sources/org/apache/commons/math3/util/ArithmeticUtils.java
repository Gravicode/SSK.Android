package org.apache.commons.math3.util;

import java.math.BigInteger;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.Localizable;
import org.apache.commons.math3.exception.util.LocalizedFormats;

public final class ArithmeticUtils {
    private ArithmeticUtils() {
    }

    public static int addAndCheck(int x, int y) throws MathArithmeticException {
        long s = ((long) x) + ((long) y);
        if (s >= -2147483648L && s <= 2147483647L) {
            return (int) s;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Integer.valueOf(x), Integer.valueOf(y));
    }

    public static long addAndCheck(long a, long b) throws MathArithmeticException {
        return addAndCheck(a, b, LocalizedFormats.OVERFLOW_IN_ADDITION);
    }

    @Deprecated
    public static long binomialCoefficient(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficient(n, k);
    }

    @Deprecated
    public static double binomialCoefficientDouble(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficientDouble(n, k);
    }

    @Deprecated
    public static double binomialCoefficientLog(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.binomialCoefficientLog(n, k);
    }

    @Deprecated
    public static long factorial(int n) throws NotPositiveException, MathArithmeticException {
        return CombinatoricsUtils.factorial(n);
    }

    @Deprecated
    public static double factorialDouble(int n) throws NotPositiveException {
        return CombinatoricsUtils.factorialDouble(n);
    }

    @Deprecated
    public static double factorialLog(int n) throws NotPositiveException {
        return CombinatoricsUtils.factorialLog(n);
    }

    public static int gcd(int p, int q) throws MathArithmeticException {
        int a = p;
        int b = q;
        if (a != 0 && b != 0) {
            long al = (long) a;
            long bl = (long) b;
            boolean useLong = false;
            if (a < 0) {
                if (Integer.MIN_VALUE == a) {
                    useLong = true;
                } else {
                    a = -a;
                }
                al = -al;
            }
            if (b < 0) {
                if (Integer.MIN_VALUE == b) {
                    useLong = true;
                } else {
                    b = -b;
                }
                bl = -bl;
            }
            if (useLong) {
                if (al == bl) {
                    throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p), Integer.valueOf(q));
                }
                long blbu = bl;
                long bl2 = al;
                long al2 = blbu % al;
                if (al2 != 0) {
                    b = (int) al2;
                    a = (int) (bl2 % al2);
                } else if (bl2 <= 2147483647L) {
                    return (int) bl2;
                } else {
                    throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p), Integer.valueOf(q));
                }
            }
            return gcdPositive(a, b);
        } else if (a != Integer.MIN_VALUE && b != Integer.MIN_VALUE) {
            return FastMath.abs(a + b);
        } else {
            throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, Integer.valueOf(p), Integer.valueOf(q));
        }
    }

    private static int gcdPositive(int a, int b) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Integer.numberOfTrailingZeros(a);
        int a2 = a >> aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b);
        int b2 = b >> bTwos;
        int shift = FastMath.min(aTwos, bTwos);
        while (a2 != b2) {
            int delta = a2 - b2;
            b2 = Math.min(a2, b2);
            int a3 = Math.abs(delta);
            a2 = a3 >> Integer.numberOfTrailingZeros(a3);
        }
        return a2 << shift;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0052  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long gcd(long r18, long r20) throws org.apache.commons.math3.exception.MathArithmeticException {
        /*
            r0 = r18
            r2 = r20
            r4 = 0
            int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            r7 = 2
            r8 = 1
            r9 = 0
            if (r6 == 0) goto L_0x007c
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 != 0) goto L_0x0013
            goto L_0x007c
        L_0x0013:
            int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0018
            long r0 = -r0
        L_0x0018:
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x001d
            long r2 = -r2
        L_0x001d:
            r10 = r2
            r1 = r0
            r0 = 0
        L_0x0020:
            r6 = r0
            r12 = 1
            long r14 = r1 & r12
            int r0 = (r14 > r4 ? 1 : (r14 == r4 ? 0 : -1))
            r3 = 63
            r14 = 2
            if (r0 != 0) goto L_0x003a
            long r16 = r10 & r12
            int r0 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x003a
            if (r6 >= r3) goto L_0x003a
            long r1 = r1 / r14
            long r10 = r10 / r14
            int r0 = r6 + 1
            goto L_0x0020
        L_0x003a:
            if (r6 != r3) goto L_0x0052
            org.apache.commons.math3.exception.MathArithmeticException r0 = new org.apache.commons.math3.exception.MathArithmeticException
            org.apache.commons.math3.exception.util.LocalizedFormats r3 = org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS
            java.lang.Object[] r4 = new java.lang.Object[r7]
            java.lang.Long r5 = java.lang.Long.valueOf(r18)
            r4[r9] = r5
            java.lang.Long r5 = java.lang.Long.valueOf(r20)
            r4[r8] = r5
            r0.<init>(r3, r4)
            throw r0
        L_0x0052:
            long r7 = r1 & r12
            int r0 = (r7 > r12 ? 1 : (r7 == r12 ? 0 : -1))
            if (r0 != 0) goto L_0x005a
            r7 = r10
            goto L_0x005d
        L_0x005a:
            long r7 = r1 / r14
            long r7 = -r7
        L_0x005d:
            long r16 = r7 & r12
            int r0 = (r16 > r4 ? 1 : (r16 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x0065
            long r7 = r7 / r14
            goto L_0x005d
        L_0x0065:
            int r0 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x006c
            long r0 = -r7
            r1 = r0
            goto L_0x006e
        L_0x006c:
            r9 = r7
            r10 = r9
        L_0x006e:
            r0 = 0
            long r16 = r10 - r1
            long r7 = r16 / r14
            int r0 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r0 != 0) goto L_0x005d
            long r3 = -r1
            long r12 = r12 << r6
            long r3 = r3 * r12
            return r3
        L_0x007c:
            r4 = -9223372036854775808
            int r6 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r6 == 0) goto L_0x0091
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 != 0) goto L_0x0087
            goto L_0x0091
        L_0x0087:
            long r4 = org.apache.commons.math3.util.FastMath.abs(r0)
            long r6 = org.apache.commons.math3.util.FastMath.abs(r2)
            long r4 = r4 + r6
            return r4
        L_0x0091:
            org.apache.commons.math3.exception.MathArithmeticException r4 = new org.apache.commons.math3.exception.MathArithmeticException
            org.apache.commons.math3.exception.util.LocalizedFormats r5 = org.apache.commons.math3.exception.util.LocalizedFormats.GCD_OVERFLOW_64_BITS
            java.lang.Object[] r6 = new java.lang.Object[r7]
            java.lang.Long r7 = java.lang.Long.valueOf(r18)
            r6[r9] = r7
            java.lang.Long r7 = java.lang.Long.valueOf(r20)
            r6[r8] = r7
            r4.<init>(r5, r6)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.util.ArithmeticUtils.gcd(long, long):long");
    }

    public static int lcm(int a, int b) throws MathArithmeticException {
        if (a == 0 || b == 0) {
            return 0;
        }
        int lcm = FastMath.abs(mulAndCheck(a / gcd(a, b), b));
        if (lcm != Integer.MIN_VALUE) {
            return lcm;
        }
        throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_32_BITS, Integer.valueOf(a), Integer.valueOf(b));
    }

    public static long lcm(long a, long b) throws MathArithmeticException {
        if (a == 0 || b == 0) {
            return 0;
        }
        long lcm = FastMath.abs(mulAndCheck(a / gcd(a, b), b));
        if (lcm != Long.MIN_VALUE) {
            return lcm;
        }
        throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_64_BITS, Long.valueOf(a), Long.valueOf(b));
    }

    public static int mulAndCheck(int x, int y) throws MathArithmeticException {
        long m = ((long) x) * ((long) y);
        if (m >= -2147483648L && m <= 2147483647L) {
            return (int) m;
        }
        throw new MathArithmeticException();
    }

    public static long mulAndCheck(long a, long b) throws MathArithmeticException {
        long ret = 0;
        if (a > b) {
            ret = mulAndCheck(b, a);
        } else if (a < 0) {
            if (b < 0) {
                if (a >= Long.MAX_VALUE / b) {
                    ret = a * b;
                } else {
                    throw new MathArithmeticException();
                }
            } else if (b <= 0) {
                ret = 0;
            } else if (Long.MIN_VALUE / b <= a) {
                ret = a * b;
            } else {
                throw new MathArithmeticException();
            }
        } else if (a > 0) {
            if (a <= Long.MAX_VALUE / b) {
                ret = a * b;
            } else {
                throw new MathArithmeticException();
            }
        }
        return ret;
    }

    public static int subAndCheck(int x, int y) throws MathArithmeticException {
        long s = ((long) x) - ((long) y);
        if (s >= -2147483648L && s <= 2147483647L) {
            return (int) s;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, Integer.valueOf(x), Integer.valueOf(y));
    }

    public static long subAndCheck(long a, long b) throws MathArithmeticException {
        if (b != Long.MIN_VALUE) {
            return addAndCheck(a, -b, LocalizedFormats.OVERFLOW_IN_ADDITION);
        }
        if (a < 0) {
            return a - b;
        }
        throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, Long.valueOf(a), Long.valueOf(-b));
    }

    public static int pow(int k, int e) throws NotPositiveException, MathArithmeticException {
        if (e < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
        }
        int result = 1;
        int exp = e;
        int k2p = k;
        while (true) {
            if ((exp & 1) != 0) {
                try {
                    result = mulAndCheck(result, k2p);
                } catch (MathArithmeticException mae) {
                    mae.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
                    mae.getContext().addMessage(LocalizedFormats.BASE, Integer.valueOf(k));
                    mae.getContext().addMessage(LocalizedFormats.EXPONENT, Integer.valueOf(e));
                    throw mae;
                }
            }
            exp >>= 1;
            if (exp == 0) {
                return result;
            }
            k2p = mulAndCheck(k2p, k2p);
        }
    }

    @Deprecated
    public static int pow(int k, long e) throws NotPositiveException {
        if (e < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
        }
        long j = e;
        int k2p = k;
        int result = 1;
        for (long e2 = j; e2 != 0; e2 >>= 1) {
            if ((1 & e2) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
        }
        return result;
    }

    public static long pow(long k, int e) throws NotPositiveException, MathArithmeticException {
        if (e < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
        }
        long result = 1;
        int exp = e;
        long k2p = k;
        while (true) {
            if ((exp & 1) != 0) {
                try {
                    result = mulAndCheck(result, k2p);
                } catch (MathArithmeticException mae) {
                    mae.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
                    mae.getContext().addMessage(LocalizedFormats.BASE, Long.valueOf(k));
                    mae.getContext().addMessage(LocalizedFormats.EXPONENT, Integer.valueOf(e));
                    throw mae;
                }
            }
            exp >>= 1;
            if (exp == 0) {
                return result;
            }
            k2p = mulAndCheck(k2p, k2p);
        }
    }

    @Deprecated
    public static long pow(long k, long e) throws NotPositiveException {
        if (e < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
        }
        long result = 1;
        long k2p = k;
        for (long e2 = e; e2 != 0; e2 >>= 1) {
            if ((1 & e2) != 0) {
                result *= k2p;
            }
            k2p *= k2p;
        }
        return result;
    }

    public static BigInteger pow(BigInteger k, int e) throws NotPositiveException {
        if (e >= 0) {
            return k.pow(e);
        }
        throw new NotPositiveException(LocalizedFormats.EXPONENT, Integer.valueOf(e));
    }

    public static BigInteger pow(BigInteger k, long e) throws NotPositiveException {
        if (e < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, Long.valueOf(e));
        }
        long j = e;
        BigInteger k2p = k;
        BigInteger result = BigInteger.ONE;
        for (long e2 = j; e2 != 0; e2 >>= 1) {
            if ((1 & e2) != 0) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
        }
        return result;
    }

    public static BigInteger pow(BigInteger k, BigInteger e) throws NotPositiveException {
        if (e.compareTo(BigInteger.ZERO) < 0) {
            throw new NotPositiveException(LocalizedFormats.EXPONENT, e);
        }
        BigInteger result = BigInteger.ONE;
        BigInteger k2p = k;
        for (BigInteger e2 = e; !BigInteger.ZERO.equals(e2); e2 = e2.shiftRight(1)) {
            if (e2.testBit(0)) {
                result = result.multiply(k2p);
            }
            k2p = k2p.multiply(k2p);
        }
        return result;
    }

    @Deprecated
    public static long stirlingS2(int n, int k) throws NotPositiveException, NumberIsTooLargeException, MathArithmeticException {
        return CombinatoricsUtils.stirlingS2(n, k);
    }

    private static long addAndCheck(long a, long b, Localizable pattern) throws MathArithmeticException {
        long result = a + b;
        if (((a ^ b) < 0) || ((a ^ result) >= 0)) {
            return result;
        }
        throw new MathArithmeticException(pattern, Long.valueOf(a), Long.valueOf(b));
    }

    public static boolean isPowerOfTwo(long n) {
        return n > 0 && ((n - 1) & n) == 0;
    }
}
