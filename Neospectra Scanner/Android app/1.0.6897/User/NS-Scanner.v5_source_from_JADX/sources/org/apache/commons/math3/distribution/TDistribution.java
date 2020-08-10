package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

public class TDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -5852615386664158222L;
    private final double degreesOfFreedom;
    private final double factor;
    private final double solverAbsoluteAccuracy;

    public TDistribution(double degreesOfFreedom2) throws NotStrictlyPositiveException {
        this(degreesOfFreedom2, 1.0E-9d);
    }

    public TDistribution(double degreesOfFreedom2, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        this(new Well19937c(), degreesOfFreedom2, inverseCumAccuracy);
    }

    public TDistribution(RandomGenerator rng, double degreesOfFreedom2) throws NotStrictlyPositiveException {
        this(rng, degreesOfFreedom2, 1.0E-9d);
    }

    public TDistribution(RandomGenerator rng, double degreesOfFreedom2, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        double d = degreesOfFreedom2;
        super(rng);
        if (d <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DEGREES_OF_FREEDOM, Double.valueOf(degreesOfFreedom2));
        }
        this.degreesOfFreedom = d;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
        double n = d;
        this.factor = (Gamma.logGamma((1.0d + n) / 2.0d) - ((FastMath.log(3.141592653589793d) + FastMath.log(n)) * 0.5d)) - Gamma.logGamma(n / 2.0d);
    }

    public double getDegreesOfFreedom() {
        return this.degreesOfFreedom;
    }

    public double density(double x) {
        return FastMath.exp(logDensity(x));
    }

    public double logDensity(double x) {
        double n = this.degreesOfFreedom;
        return this.factor - (FastMath.log(((x * x) / n) + 1.0d) * ((n + 1.0d) / 2.0d));
    }

    public double cumulativeProbability(double x) {
        if (x == 0.0d) {
            return 0.5d;
        }
        double t = Beta.regularizedBeta(this.degreesOfFreedom / (this.degreesOfFreedom + (x * x)), this.degreesOfFreedom * 0.5d, 0.5d);
        if (x < 0.0d) {
            return t * 0.5d;
        }
        return 1.0d - (0.5d * t);
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        if (getDegreesOfFreedom() > 1.0d) {
            return 0.0d;
        }
        return Double.NaN;
    }

    public double getNumericalVariance() {
        double df = getDegreesOfFreedom();
        if (df > 2.0d) {
            return df / (df - 2.0d);
        }
        if (df <= 1.0d || df > 2.0d) {
            return Double.NaN;
        }
        return Double.POSITIVE_INFINITY;
    }

    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
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
}
