package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class BetaDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -1221965979403477668L;
    private final double alpha;
    private final double beta;
    private final double solverAbsoluteAccuracy;

    /* renamed from: z */
    private double f547z;

    private static final class ChengBetaSampler {
        private ChengBetaSampler() {
        }

        static double sample(RandomGenerator random, double alpha, double beta) {
            double a = FastMath.min(alpha, beta);
            double b = FastMath.max(alpha, beta);
            if (a > 1.0d) {
                return algorithmBB(random, alpha, a, b);
            }
            return algorithmBC(random, alpha, b, a);
        }

        private static double algorithmBB(RandomGenerator random, double a0, double a, double b) {
            double w;
            double d = a;
            double alpha = d + b;
            double beta = FastMath.sqrt((alpha - 2.0d) / (((2.0d * d) * b) - alpha));
            double gamma = (1.0d / beta) + d;
            while (true) {
                double u1 = random.nextDouble();
                double v = (FastMath.log(u1) - FastMath.log1p(-u1)) * beta;
                w = FastMath.exp(v) * d;
                double beta2 = beta;
                double z = u1 * u1 * random.nextDouble();
                double r = (gamma * v) - 1.3862944d;
                double s = (d + r) - w;
                if (s + 2.609438d < 5.0d * z) {
                    double t = FastMath.log(z);
                    if (s >= t || r + ((FastMath.log(alpha) - FastMath.log(b + w)) * alpha) >= t) {
                        break;
                    }
                    double d2 = a0;
                    beta = beta2;
                } else {
                    break;
                }
            }
            double w2 = FastMath.min(w, Double.MAX_VALUE);
            return Precision.equals(d, a0) ? w2 / (b + w2) : b / (b + w2);
        }

        /* JADX WARNING: Removed duplicated region for block: B:21:0x00ba A[EDGE_INSN: B:21:0x00ba->B:16:0x00ba ?: BREAK  , SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x00d8 A[SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static double algorithmBC(org.apache.commons.math3.random.RandomGenerator r32, double r33, double r35, double r37) {
            /*
                r0 = r35
                double r2 = r0 + r37
                r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
                double r6 = r4 / r37
                double r4 = r4 + r0
                double r4 = r4 - r37
                r8 = 4586165625342794696(0x3fa5555673aa1bc8, double:0.0416667)
                double r8 = r8 * r37
                r10 = 4579160027523720458(0x3f8c71c89a38250a, double:0.0138889)
                double r8 = r8 + r10
                double r8 = r8 * r4
                double r10 = r0 * r6
                r12 = 4605180820967230355(0x3fe8e38eb0318b93, double:0.777778)
                double r10 = r10 - r12
                double r8 = r8 / r10
                r10 = 4598175219545276416(0x3fd0000000000000, double:0.25)
                double r12 = r10 / r4
                r14 = 4602678819172646912(0x3fe0000000000000, double:0.5)
                double r12 = r12 + r14
                double r12 = r12 * r37
                double r12 = r12 + r10
            L_0x002d:
                double r10 = r32.nextDouble()
                double r18 = r32.nextDouble()
                double r20 = r10 * r18
                r22 = r4
                double r4 = r10 * r20
                int r24 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
                if (r24 >= 0) goto L_0x0059
                r16 = 4598175219545276416(0x3fd0000000000000, double:0.25)
                double r24 = r18 * r16
                double r24 = r24 + r4
                double r24 = r24 - r20
                int r24 = (r24 > r8 ? 1 : (r24 == r8 ? 0 : -1))
                if (r24 < 0) goto L_0x0056
                r30 = r6
                r26 = r8
            L_0x0050:
                r28 = r12
                r6 = r33
                goto L_0x00d8
            L_0x0056:
                r26 = r8
                goto L_0x0084
            L_0x0059:
                r16 = 4598175219545276416(0x3fd0000000000000, double:0.25)
                int r24 = (r4 > r16 ? 1 : (r4 == r16 ? 0 : -1))
                if (r24 > 0) goto L_0x007a
                double r14 = org.apache.commons.math3.util.FastMath.log(r10)
                r26 = r8
                double r8 = -r10
                double r8 = org.apache.commons.math3.util.FastMath.log1p(r8)
                double r14 = r14 - r8
                double r8 = r6 * r14
                double r14 = org.apache.commons.math3.util.FastMath.exp(r8)
                double r14 = r14 * r0
                r30 = r6
                r28 = r12
                r12 = r14
                goto L_0x00ba
            L_0x007a:
                r26 = r8
                int r8 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r8 < 0) goto L_0x0084
                r30 = r6
                goto L_0x0050
            L_0x0084:
                double r8 = org.apache.commons.math3.util.FastMath.log(r10)
                r28 = r12
                double r12 = -r10
                double r12 = org.apache.commons.math3.util.FastMath.log1p(r12)
                double r8 = r8 - r12
                double r8 = r8 * r6
                double r12 = org.apache.commons.math3.util.FastMath.exp(r8)
                double r12 = r12 * r0
                double r24 = org.apache.commons.math3.util.FastMath.log(r2)
                r30 = r6
                double r6 = r37 + r12
                double r6 = org.apache.commons.math3.util.FastMath.log(r6)
                double r24 = r24 - r6
                double r24 = r24 + r8
                double r24 = r24 * r2
                r6 = 4608922134115912717(0x3ff62e43096a0c0d, double:1.3862944)
                double r24 = r24 - r6
                double r6 = org.apache.commons.math3.util.FastMath.log(r4)
                int r6 = (r24 > r6 ? 1 : (r24 == r6 ? 0 : -1))
                if (r6 < 0) goto L_0x00d6
            L_0x00ba:
                r4 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
                double r4 = org.apache.commons.math3.util.FastMath.min(r12, r4)
                r6 = r33
                boolean r8 = org.apache.commons.math3.util.Precision.equals(r0, r6)
                if (r8 == 0) goto L_0x00d0
                double r8 = r37 + r4
                double r8 = r4 / r8
                goto L_0x00d5
            L_0x00d0:
                r8 = 0
                double r8 = r37 + r4
                double r8 = r37 / r8
            L_0x00d5:
                return r8
            L_0x00d6:
                r6 = r33
            L_0x00d8:
                r10 = r16
                r4 = r22
                r8 = r26
                r12 = r28
                r6 = r30
                goto L_0x002d
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.distribution.BetaDistribution.ChengBetaSampler.algorithmBC(org.apache.commons.math3.random.RandomGenerator, double, double, double):double");
        }
    }

    public BetaDistribution(double alpha2, double beta2) {
        this(alpha2, beta2, 1.0E-9d);
    }

    public BetaDistribution(double alpha2, double beta2, double inverseCumAccuracy) {
        this(new Well19937c(), alpha2, beta2, inverseCumAccuracy);
    }

    public BetaDistribution(RandomGenerator rng, double alpha2, double beta2) {
        this(rng, alpha2, beta2, 1.0E-9d);
    }

    public BetaDistribution(RandomGenerator rng, double alpha2, double beta2, double inverseCumAccuracy) {
        super(rng);
        this.alpha = alpha2;
        this.beta = beta2;
        this.f547z = Double.NaN;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getBeta() {
        return this.beta;
    }

    private void recomputeZ() {
        if (Double.isNaN(this.f547z)) {
            this.f547z = (Gamma.logGamma(this.alpha) + Gamma.logGamma(this.beta)) - Gamma.logGamma(this.alpha + this.beta);
        }
    }

    public double density(double x) {
        double logDensity = logDensity(x);
        if (logDensity == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logDensity);
    }

    public double logDensity(double x) {
        recomputeZ();
        if (x < 0.0d || x > 1.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x == 0.0d) {
            if (this.alpha >= 1.0d) {
                return Double.NEGATIVE_INFINITY;
            }
            throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, Double.valueOf(this.alpha), Integer.valueOf(1), false);
        } else if (x != 1.0d) {
            return (((this.alpha - 1.0d) * FastMath.log(x)) + ((this.beta - 1.0d) * FastMath.log1p(-x))) - this.f547z;
        } else if (this.beta >= 1.0d) {
            return Double.NEGATIVE_INFINITY;
        } else {
            throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, Double.valueOf(this.beta), Integer.valueOf(1), false);
        }
    }

    public double cumulativeProbability(double x) {
        if (x <= 0.0d) {
            return 0.0d;
        }
        if (x >= 1.0d) {
            return 1.0d;
        }
        return Beta.regularizedBeta(x, this.alpha, this.beta);
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        double a = getAlpha();
        return a / (getBeta() + a);
    }

    public double getNumericalVariance() {
        double a = getAlpha();
        double b = getBeta();
        double alphabetasum = a + b;
        return (a * b) / ((alphabetasum * alphabetasum) * (1.0d + alphabetasum));
    }

    public double getSupportLowerBound() {
        return 0.0d;
    }

    public double getSupportUpperBound() {
        return 1.0d;
    }

    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public double sample() {
        return ChengBetaSampler.sample(this.random, this.alpha, this.beta);
    }
}
