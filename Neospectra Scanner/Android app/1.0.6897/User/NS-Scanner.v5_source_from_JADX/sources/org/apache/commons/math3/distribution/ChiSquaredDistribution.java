package org.apache.commons.math3.distribution;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

public class ChiSquaredDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -8352658048349159782L;
    private final GammaDistribution gamma;
    private final double solverAbsoluteAccuracy;

    public ChiSquaredDistribution(double degreesOfFreedom) {
        this(degreesOfFreedom, 1.0E-9d);
    }

    public ChiSquaredDistribution(double degreesOfFreedom, double inverseCumAccuracy) {
        this(new Well19937c(), degreesOfFreedom, inverseCumAccuracy);
    }

    public ChiSquaredDistribution(RandomGenerator rng, double degreesOfFreedom) {
        this(rng, degreesOfFreedom, 1.0E-9d);
    }

    public ChiSquaredDistribution(RandomGenerator rng, double degreesOfFreedom, double inverseCumAccuracy) {
        super(rng);
        this.gamma = new GammaDistribution(degreesOfFreedom / 2.0d, 2.0d);
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getDegreesOfFreedom() {
        return this.gamma.getShape() * 2.0d;
    }

    public double density(double x) {
        return this.gamma.density(x);
    }

    public double logDensity(double x) {
        return this.gamma.logDensity(x);
    }

    public double cumulativeProbability(double x) {
        return this.gamma.cumulativeProbability(x);
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return getDegreesOfFreedom();
    }

    public double getNumericalVariance() {
        return getDegreesOfFreedom() * 2.0d;
    }

    public double getSupportLowerBound() {
        return 0.0d;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    public boolean isSupportConnected() {
        return true;
    }
}
