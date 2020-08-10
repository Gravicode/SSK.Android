package org.apache.commons.math3.distribution;

import java.io.Serializable;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.FastMath;

public abstract class AbstractRealDistribution implements RealDistribution, Serializable {
    public static final double SOLVER_DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private static final long serialVersionUID = -38038050983108802L;
    protected final RandomGenerator random;
    @Deprecated
    protected RandomDataImpl randomData;
    private double solverAbsoluteAccuracy;

    @Deprecated
    protected AbstractRealDistribution() {
        this.randomData = new RandomDataImpl();
        this.solverAbsoluteAccuracy = 1.0E-6d;
        this.random = null;
    }

    protected AbstractRealDistribution(RandomGenerator rng) {
        this.randomData = new RandomDataImpl();
        this.solverAbsoluteAccuracy = 1.0E-6d;
        this.random = rng;
    }

    @Deprecated
    public double cumulativeProbability(double x0, double x1) throws NumberIsTooLargeException {
        return probability(x0, x1);
    }

    public double probability(double x0, double x1) {
        if (x0 <= x1) {
            return cumulativeProbability(x1) - cumulativeProbability(x0);
        }
        throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, Double.valueOf(x0), Double.valueOf(x1), true);
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        final double d = p;
        boolean z = false;
        if (d < 0.0d || d > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        }
        double lowerBound = getSupportLowerBound();
        if (d == 0.0d) {
            return lowerBound;
        }
        double upperBound = getSupportUpperBound();
        if (d == 1.0d) {
            return upperBound;
        }
        double mu = getNumericalMean();
        double sig = FastMath.sqrt(getNumericalVariance());
        if (!Double.isInfinite(mu) && !Double.isNaN(mu) && !Double.isInfinite(sig) && !Double.isNaN(sig)) {
            z = true;
        }
        boolean chebyshevApplies = z;
        if (lowerBound == Double.NEGATIVE_INFINITY) {
            if (chebyshevApplies) {
                lowerBound = mu - (FastMath.sqrt((1.0d - d) / d) * sig);
            } else {
                lowerBound = -1.0d;
                while (cumulativeProbability(lowerBound) >= d) {
                    lowerBound *= 2.0d;
                }
            }
        }
        if (upperBound == Double.POSITIVE_INFINITY) {
            if (chebyshevApplies) {
                upperBound = mu + (FastMath.sqrt(d / (1.0d - d)) * sig);
            } else {
                upperBound = 1.0d;
                while (cumulativeProbability(upperBound) < d) {
                    upperBound *= 2.0d;
                }
            }
        }
        double x = UnivariateSolverUtils.solve(new UnivariateFunction() {
            public double value(double x) {
                return AbstractRealDistribution.this.cumulativeProbability(x) - d;
            }
        }, lowerBound, upperBound, getSolverAbsoluteAccuracy());
        if (!isSupportConnected()) {
            double dx = getSolverAbsoluteAccuracy();
            if (x - dx >= getSupportLowerBound()) {
                double px = cumulativeProbability(x);
                double d2 = upperBound;
                if (cumulativeProbability(x - dx) == px) {
                    double upperBound2 = x;
                    while (upperBound2 - lowerBound > dx) {
                        double dx2 = dx;
                        double midPoint = (lowerBound + upperBound2) * 0.5d;
                        if (cumulativeProbability(midPoint) < px) {
                            lowerBound = midPoint;
                        } else {
                            upperBound2 = midPoint;
                        }
                        dx = dx2;
                    }
                    return upperBound2;
                }
                return x;
            }
        }
        double d3 = upperBound;
        return x;
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public void reseedRandomGenerator(long seed) {
        this.random.setSeed(seed);
        this.randomData.reSeed(seed);
    }

    public double sample() {
        return inverseCumulativeProbability(this.random.nextDouble());
    }

    public double[] sample(int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, Integer.valueOf(sampleSize));
        }
        double[] out = new double[sampleSize];
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
        return out;
    }

    public double probability(double x) {
        return 0.0d;
    }

    public double logDensity(double x) {
        return FastMath.log(density(x));
    }
}
