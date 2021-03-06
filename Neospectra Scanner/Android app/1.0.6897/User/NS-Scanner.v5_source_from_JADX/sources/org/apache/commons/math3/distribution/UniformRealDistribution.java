package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class UniformRealDistribution extends AbstractRealDistribution {
    @Deprecated
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 20120109;
    private final double lower;
    private final double upper;

    public UniformRealDistribution() {
        this(0.0d, 1.0d);
    }

    public UniformRealDistribution(double lower2, double upper2) throws NumberIsTooLargeException {
        this((RandomGenerator) new Well19937c(), lower2, upper2);
    }

    @Deprecated
    public UniformRealDistribution(double lower2, double upper2, double inverseCumAccuracy) throws NumberIsTooLargeException {
        this((RandomGenerator) new Well19937c(), lower2, upper2);
    }

    @Deprecated
    public UniformRealDistribution(RandomGenerator rng, double lower2, double upper2, double inverseCumAccuracy) {
        this(rng, lower2, upper2);
    }

    public UniformRealDistribution(RandomGenerator rng, double lower2, double upper2) throws NumberIsTooLargeException {
        super(rng);
        if (lower2 >= upper2) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(lower2), Double.valueOf(upper2), false);
        }
        this.lower = lower2;
        this.upper = upper2;
    }

    public double density(double x) {
        if (x < this.lower || x > this.upper) {
            return 0.0d;
        }
        return 1.0d / (this.upper - this.lower);
    }

    public double cumulativeProbability(double x) {
        if (x <= this.lower) {
            return 0.0d;
        }
        if (x >= this.upper) {
            return 1.0d;
        }
        return (x - this.lower) / (this.upper - this.lower);
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p >= 0.0d && p <= 1.0d) {
            return ((this.upper - this.lower) * p) + this.lower;
        }
        throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
    }

    public double getNumericalMean() {
        return (this.lower + this.upper) * 0.5d;
    }

    public double getNumericalVariance() {
        double ul = this.upper - this.lower;
        return (ul * ul) / 12.0d;
    }

    public double getSupportLowerBound() {
        return this.lower;
    }

    public double getSupportUpperBound() {
        return this.upper;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public double sample() {
        double u = this.random.nextDouble();
        return (this.upper * u) + ((1.0d - u) * this.lower);
    }
}
