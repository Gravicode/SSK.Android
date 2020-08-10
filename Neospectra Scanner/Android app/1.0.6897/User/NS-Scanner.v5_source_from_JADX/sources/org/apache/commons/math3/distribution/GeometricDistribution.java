package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class GeometricDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20130507;
    private final double log1mProbabilityOfSuccess;
    private final double logProbabilityOfSuccess;
    private final double probabilityOfSuccess;

    public GeometricDistribution(double p) {
        this(new Well19937c(), p);
    }

    public GeometricDistribution(RandomGenerator rng, double p) {
        super(rng);
        if (p <= 0.0d || p > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        }
        this.probabilityOfSuccess = p;
        this.logProbabilityOfSuccess = FastMath.log(p);
        this.log1mProbabilityOfSuccess = FastMath.log1p(-p);
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    public double probability(int x) {
        if (x < 0) {
            return 0.0d;
        }
        return FastMath.exp(this.log1mProbabilityOfSuccess * ((double) x)) * this.probabilityOfSuccess;
    }

    public double logProbability(int x) {
        if (x < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return (((double) x) * this.log1mProbabilityOfSuccess) + this.logProbabilityOfSuccess;
    }

    public double cumulativeProbability(int x) {
        if (x < 0) {
            return 0.0d;
        }
        return -FastMath.expm1(this.log1mProbabilityOfSuccess * ((double) (x + 1)));
    }

    public double getNumericalMean() {
        return (1.0d - this.probabilityOfSuccess) / this.probabilityOfSuccess;
    }

    public double getNumericalVariance() {
        return (1.0d - this.probabilityOfSuccess) / (this.probabilityOfSuccess * this.probabilityOfSuccess);
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        } else if (p == 1.0d) {
            return Integer.MAX_VALUE;
        } else {
            if (p == 0.0d) {
                return 0;
            }
            return Math.max(0, (int) Math.ceil((FastMath.log1p(-p) / this.log1mProbabilityOfSuccess) - 1.0d));
        }
    }
}
