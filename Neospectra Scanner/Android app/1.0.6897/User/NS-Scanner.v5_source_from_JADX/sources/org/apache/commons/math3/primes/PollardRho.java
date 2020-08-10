package org.apache.commons.math3.primes;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.FastMath;

class PollardRho {
    private PollardRho() {
    }

    public static List<Integer> primeFactors(int n) {
        List<Integer> factors = new ArrayList<>();
        int n2 = SmallPrimes.smallTrialDivision(n, factors);
        if (1 == n2) {
            return factors;
        }
        if (SmallPrimes.millerRabinPrimeTest(n2)) {
            factors.add(Integer.valueOf(n2));
            return factors;
        }
        int divisor = rhoBrent(n2);
        factors.add(Integer.valueOf(divisor));
        factors.add(Integer.valueOf(n2 / divisor));
        return factors;
    }

    static int rhoBrent(int n) {
        char c;
        char c2;
        int i = n;
        char c3 = 2;
        char c4 = 25;
        int y = 2;
        int cst = SmallPrimes.PRIMES_LAST;
        int r = 1;
        while (true) {
            int x = y;
            int k = 0;
            int y2 = y;
            for (int i2 = 0; i2 < r; i2++) {
                y2 = (int) ((((long) cst) + (((long) y2) * ((long) y2))) % ((long) i));
            }
            while (true) {
                int k2 = k;
                int bound = FastMath.min(25, r - k2);
                int q = 1;
                int i3 = -3;
                while (true) {
                    if (i3 >= bound) {
                        c = c3;
                        c2 = c4;
                        int i4 = cst;
                        break;
                    }
                    int cst2 = cst;
                    y2 = (int) ((((long) cst) + (((long) y2) * ((long) y2))) % ((long) i));
                    long divisor = (long) FastMath.abs(x - y2);
                    if (0 == divisor) {
                        k2 = -25;
                        y2 = 2;
                        r = 1;
                        c = c3;
                        c2 = c4;
                        cst = cst2 + SmallPrimes.PRIMES_LAST;
                        break;
                    }
                    char c5 = c3;
                    char c6 = c4;
                    q = (int) ((((long) q) * divisor) % ((long) i));
                    if (q == 0) {
                        return gcdPositive(FastMath.abs((int) divisor), i);
                    }
                    i3++;
                    cst = cst2;
                    c3 = c5;
                    c4 = c6;
                }
                int out = gcdPositive(FastMath.abs(q), i);
                if (1 != out) {
                    return out;
                }
                k = k2 + 25;
                if (k >= r) {
                    break;
                }
                c3 = c;
                c4 = c2;
            }
            r *= 2;
            y = y2;
            c3 = c;
            c4 = c2;
        }
    }

    static int gcdPositive(int a, int b) {
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
            b2 = FastMath.min(a2, b2);
            int a3 = FastMath.abs(delta);
            a2 = a3 >> Integer.numberOfTrailingZeros(a3);
        }
        return a2 << shift;
    }
}
