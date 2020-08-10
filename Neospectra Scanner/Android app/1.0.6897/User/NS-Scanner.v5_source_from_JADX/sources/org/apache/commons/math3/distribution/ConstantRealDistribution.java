package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;

public class ConstantRealDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = -4157745166772046273L;
    private final double value;

    public ConstantRealDistribution(double value2) {
        super(null);
        this.value = value2;
    }

    public double density(double x) {
        return x == this.value ? 1.0d : 0.0d;
    }

    public double cumulativeProbability(double x) {
        return x < this.value ? 0.0d : 1.0d;
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p >= 0.0d && p <= 1.0d) {
            return this.value;
        }
        throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
    }

    public double getNumericalMean() {
        return this.value;
    }

    public double getNumericalVariance() {
        return 0.0d;
    }

    public double getSupportLowerBound() {
        return this.value;
    }

    public double getSupportUpperBound() {
        return this.value;
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
        return this.value;
    }

    public void reseedRandomGenerator(long seed) {
    }
}
