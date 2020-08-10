package org.apache.commons.math3.analysis.polynomials;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

public class PolynomialsUtils {
    private static final List<BigFraction> CHEBYSHEV_COEFFICIENTS = new ArrayList();
    private static final List<BigFraction> HERMITE_COEFFICIENTS = new ArrayList();
    private static final Map<JacobiKey, List<BigFraction>> JACOBI_COEFFICIENTS = new HashMap();
    private static final List<BigFraction> LAGUERRE_COEFFICIENTS = new ArrayList();
    private static final List<BigFraction> LEGENDRE_COEFFICIENTS = new ArrayList();

    private static class JacobiKey {

        /* renamed from: v */
        private final int f531v;

        /* renamed from: w */
        private final int f532w;

        JacobiKey(int v, int w) {
            this.f531v = v;
            this.f532w = w;
        }

        public int hashCode() {
            return (this.f531v << 16) ^ this.f532w;
        }

        public boolean equals(Object key) {
            boolean z = false;
            if (key == null || !(key instanceof JacobiKey)) {
                return false;
            }
            JacobiKey otherK = (JacobiKey) key;
            if (this.f531v == otherK.f531v && this.f532w == otherK.f532w) {
                z = true;
            }
            return z;
        }
    }

    private interface RecurrenceCoefficientsGenerator {
        BigFraction[] generate(int i);
    }

    static {
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ZERO);
        CHEBYSHEV_COEFFICIENTS.add(BigFraction.ONE);
        HERMITE_COEFFICIENTS.add(BigFraction.ONE);
        HERMITE_COEFFICIENTS.add(BigFraction.ZERO);
        HERMITE_COEFFICIENTS.add(BigFraction.TWO);
        LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(BigFraction.ONE);
        LAGUERRE_COEFFICIENTS.add(BigFraction.MINUS_ONE);
        LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
        LEGENDRE_COEFFICIENTS.add(BigFraction.ZERO);
        LEGENDRE_COEFFICIENTS.add(BigFraction.ONE);
    }

    private PolynomialsUtils() {
    }

    public static PolynomialFunction createChebyshevPolynomial(int degree) {
        return buildPolynomial(degree, CHEBYSHEV_COEFFICIENTS, new RecurrenceCoefficientsGenerator() {
            private final BigFraction[] coeffs = {BigFraction.ZERO, BigFraction.TWO, BigFraction.ONE};

            public BigFraction[] generate(int k) {
                return this.coeffs;
            }
        });
    }

    public static PolynomialFunction createHermitePolynomial(int degree) {
        return buildPolynomial(degree, HERMITE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() {
            public BigFraction[] generate(int k) {
                return new BigFraction[]{BigFraction.ZERO, BigFraction.TWO, new BigFraction(k * 2)};
            }
        });
    }

    public static PolynomialFunction createLaguerrePolynomial(int degree) {
        return buildPolynomial(degree, LAGUERRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() {
            public BigFraction[] generate(int k) {
                int kP1 = k + 1;
                return new BigFraction[]{new BigFraction((k * 2) + 1, kP1), new BigFraction(-1, kP1), new BigFraction(k, kP1)};
            }
        });
    }

    public static PolynomialFunction createLegendrePolynomial(int degree) {
        return buildPolynomial(degree, LEGENDRE_COEFFICIENTS, new RecurrenceCoefficientsGenerator() {
            public BigFraction[] generate(int k) {
                int kP1 = k + 1;
                return new BigFraction[]{BigFraction.ZERO, new BigFraction(k + kP1, kP1), new BigFraction(k, kP1)};
            }
        });
    }

    public static PolynomialFunction createJacobiPolynomial(int degree, final int v, final int w) {
        JacobiKey key = new JacobiKey(v, w);
        if (!JACOBI_COEFFICIENTS.containsKey(key)) {
            List<BigFraction> list = new ArrayList<>();
            JACOBI_COEFFICIENTS.put(key, list);
            list.add(BigFraction.ONE);
            list.add(new BigFraction(v - w, 2));
            list.add(new BigFraction(v + 2 + w, 2));
        }
        return buildPolynomial(degree, (List) JACOBI_COEFFICIENTS.get(key), new RecurrenceCoefficientsGenerator() {
            public BigFraction[] generate(int k) {
                int k2 = k + 1;
                int kvw = v + k2 + w;
                int twoKvw = kvw + k2;
                int twoKvwM1 = twoKvw - 1;
                int twoKvwM2 = twoKvw - 2;
                int den = k2 * 2 * kvw * twoKvwM2;
                return new BigFraction[]{new BigFraction(((v * v) - (w * w)) * twoKvwM1, den), new BigFraction(twoKvwM1 * twoKvw * twoKvwM2, den), new BigFraction(((v + k2) - 1) * 2 * ((w + k2) - 1) * twoKvw, den)};
            }
        });
    }

    public static double[] shift(double[] coefficients, double shift) {
        int dp1 = coefficients.length;
        double[] newCoefficients = new double[dp1];
        int[][] coeff = (int[][]) Array.newInstance(int.class, new int[]{dp1, dp1});
        for (int i = 0; i < dp1; i++) {
            for (int j = 0; j <= i; j++) {
                coeff[i][j] = (int) CombinatoricsUtils.binomialCoefficient(i, j);
            }
        }
        for (int i2 = 0; i2 < dp1; i2++) {
            newCoefficients[0] = newCoefficients[0] + (coefficients[i2] * FastMath.pow(shift, i2));
        }
        int d = dp1 - 1;
        for (int i3 = 0; i3 < d; i3++) {
            for (int j2 = i3; j2 < d; j2++) {
                int i4 = i3 + 1;
                newCoefficients[i4] = newCoefficients[i4] + (((double) coeff[j2 + 1][j2 - i3]) * coefficients[j2 + 1] * FastMath.pow(shift, j2 - i3));
            }
        }
        return newCoefficients;
    }

    private static PolynomialFunction buildPolynomial(int degree, List<BigFraction> coefficients, RecurrenceCoefficientsGenerator generator) {
        synchronized (coefficients) {
            int maxDegree = ((int) FastMath.floor(FastMath.sqrt((double) (coefficients.size() * 2)))) - 1;
            if (degree > maxDegree) {
                computeUpToDegree(degree, maxDegree, generator, coefficients);
            }
        }
        int start = ((degree + 1) * degree) / 2;
        double[] a = new double[(degree + 1)];
        for (int i = 0; i <= degree; i++) {
            a[i] = ((BigFraction) coefficients.get(start + i)).doubleValue();
        }
        return new PolynomialFunction(a);
    }

    private static void computeUpToDegree(int degree, int maxDegree, RecurrenceCoefficientsGenerator generator, List<BigFraction> coefficients) {
        List<BigFraction> list = coefficients;
        int startK = ((maxDegree - 1) * maxDegree) / 2;
        for (int k = maxDegree; k < degree; k++) {
            int startKm1 = startK;
            startK += k;
            BigFraction[] ai = generator.generate(k);
            BigFraction ck = (BigFraction) list.get(startK);
            BigFraction ckm1 = (BigFraction) list.get(startKm1);
            char c = 0;
            list.add(ck.multiply(ai[0]).subtract(ckm1.multiply(ai[2])));
            BigFraction bigFraction = ckm1;
            BigFraction ck2 = ck;
            int i = 1;
            while (i < k) {
                BigFraction ckPrev = ck2;
                ck2 = (BigFraction) list.get(startK + i);
                list.add(ck2.multiply(ai[c]).add(ckPrev.multiply(ai[1])).subtract(((BigFraction) list.get(startKm1 + i)).multiply(ai[2])));
                i++;
                c = 0;
            }
            BigFraction ckPrev2 = ck2;
            BigFraction ck3 = (BigFraction) list.get(startK + k);
            list.add(ck3.multiply(ai[0]).add(ckPrev2.multiply(ai[1])));
            list.add(ck3.multiply(ai[1]));
        }
        RecurrenceCoefficientsGenerator recurrenceCoefficientsGenerator = generator;
    }
}
