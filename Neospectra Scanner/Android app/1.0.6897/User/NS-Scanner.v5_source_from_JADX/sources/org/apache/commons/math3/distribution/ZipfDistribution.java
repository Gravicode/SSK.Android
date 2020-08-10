package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class ZipfDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = -140627372283420404L;
    private final double exponent;
    private final int numberOfElements;
    private double numericalMean;
    private boolean numericalMeanIsCalculated;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;
    private transient ZipfRejectionInversionSampler sampler;

    static final class ZipfRejectionInversionSampler {
        private final double exponent;
        private final double hIntegralNumberOfElements;
        private final double hIntegralX1 = (hIntegral(1.5d) - 1.0d);
        private final int numberOfElements;

        /* renamed from: s */
        private final double f559s;

        ZipfRejectionInversionSampler(int numberOfElements2, double exponent2) {
            this.exponent = exponent2;
            this.numberOfElements = numberOfElements2;
            this.hIntegralNumberOfElements = hIntegral(((double) numberOfElements2) + 0.5d);
            this.f559s = 2.0d - hIntegralInverse(hIntegral(2.5d) - m41h(2.0d));
        }

        /* access modifiers changed from: 0000 */
        public int sample(RandomGenerator random) {
            double u;
            int k;
            do {
                u = this.hIntegralNumberOfElements + (random.nextDouble() * (this.hIntegralX1 - this.hIntegralNumberOfElements));
                double x = hIntegralInverse(u);
                k = (int) (x + 0.5d);
                if (k < 1) {
                    k = 1;
                } else if (k > this.numberOfElements) {
                    k = this.numberOfElements;
                }
                if (((double) k) - x <= this.f559s) {
                    break;
                }
            } while (u < hIntegral(((double) k) + 0.5d) - m41h((double) k));
            return k;
        }

        private double hIntegral(double x) {
            double logX = FastMath.log(x);
            return helper2((1.0d - this.exponent) * logX) * logX;
        }

        /* renamed from: h */
        private double m41h(double x) {
            return FastMath.exp((-this.exponent) * FastMath.log(x));
        }

        private double hIntegralInverse(double x) {
            double t = (1.0d - this.exponent) * x;
            if (t < -1.0d) {
                t = -1.0d;
            }
            return FastMath.exp(helper1(t) * x);
        }

        static double helper1(double x) {
            if (FastMath.abs(x) > 1.0E-8d) {
                return FastMath.log1p(x) / x;
            }
            return 1.0d - ((0.5d - ((0.3333333333333333d - (0.25d * x)) * x)) * x);
        }

        static double helper2(double x) {
            if (FastMath.abs(x) > 1.0E-8d) {
                return FastMath.expm1(x) / x;
            }
            return (0.5d * x * ((0.3333333333333333d * x * ((0.25d * x) + 1.0d)) + 1.0d)) + 1.0d;
        }
    }

    public ZipfDistribution(int numberOfElements2, double exponent2) {
        this(new Well19937c(), numberOfElements2, exponent2);
    }

    public ZipfDistribution(RandomGenerator rng, int numberOfElements2, double exponent2) throws NotStrictlyPositiveException {
        super(rng);
        this.numericalMean = Double.NaN;
        this.numericalMeanIsCalculated = false;
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (numberOfElements2 <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(numberOfElements2));
        } else if (exponent2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.EXPONENT, Double.valueOf(exponent2));
        } else {
            this.numberOfElements = numberOfElements2;
            this.exponent = exponent2;
        }
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public double getExponent() {
        return this.exponent;
    }

    public double probability(int x) {
        if (x <= 0 || x > this.numberOfElements) {
            return 0.0d;
        }
        return (1.0d / FastMath.pow((double) x, this.exponent)) / generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    public double logProbability(int x) {
        if (x <= 0 || x > this.numberOfElements) {
            return Double.NEGATIVE_INFINITY;
        }
        return ((-FastMath.log((double) x)) * this.exponent) - FastMath.log(generalizedHarmonic(this.numberOfElements, this.exponent));
    }

    public double cumulativeProbability(int x) {
        if (x <= 0) {
            return 0.0d;
        }
        if (x >= this.numberOfElements) {
            return 1.0d;
        }
        return generalizedHarmonic(x, this.exponent) / generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    /* access modifiers changed from: protected */
    public double calculateNumericalMean() {
        int N = getNumberOfElements();
        double s = getExponent();
        return generalizedHarmonic(N, s - 1.0d) / generalizedHarmonic(N, s);
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    /* access modifiers changed from: protected */
    public double calculateNumericalVariance() {
        int N = getNumberOfElements();
        double s = getExponent();
        double Hs2 = generalizedHarmonic(N, s - 2.0d);
        double Hs1 = generalizedHarmonic(N, s - 1.0d);
        double Hs = generalizedHarmonic(N, s);
        return (Hs2 / Hs) - ((Hs1 * Hs1) / (Hs * Hs));
    }

    private double generalizedHarmonic(int n, double m) {
        double value = 0.0d;
        for (int k = n; k > 0; k--) {
            value += 1.0d / FastMath.pow((double) k, m);
        }
        return value;
    }

    public int getSupportLowerBound() {
        return 1;
    }

    public int getSupportUpperBound() {
        return getNumberOfElements();
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        if (this.sampler == null) {
            this.sampler = new ZipfRejectionInversionSampler(this.numberOfElements, this.exponent);
        }
        return this.sampler.sample(this.random);
    }
}
