package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

public class NakagamiDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 20141003;
    private final double inverseAbsoluteAccuracy;

    /* renamed from: mu */
    private final double f555mu;
    private final double omega;

    public NakagamiDistribution(double mu, double omega2) {
        this(mu, omega2, 1.0E-9d);
    }

    public NakagamiDistribution(double mu, double omega2, double inverseAbsoluteAccuracy2) {
        this(new Well19937c(), mu, omega2, inverseAbsoluteAccuracy2);
    }

    public NakagamiDistribution(RandomGenerator rng, double mu, double omega2, double inverseAbsoluteAccuracy2) {
        super(rng);
        if (mu < 0.5d) {
            throw new NumberIsTooSmallException(Double.valueOf(mu), Double.valueOf(0.5d), true);
        } else if (omega2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(omega2));
        } else {
            this.f555mu = mu;
            this.omega = omega2;
            this.inverseAbsoluteAccuracy = inverseAbsoluteAccuracy2;
        }
    }

    public double getShape() {
        return this.f555mu;
    }

    public double getScale() {
        return this.omega;
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.inverseAbsoluteAccuracy;
    }

    public double density(double x) {
        if (x <= 0.0d) {
            return 0.0d;
        }
        return ((FastMath.pow(this.f555mu, this.f555mu) * 2.0d) / (Gamma.gamma(this.f555mu) * FastMath.pow(this.omega, this.f555mu))) * FastMath.pow(x, (this.f555mu * 2.0d) - 1.0d) * FastMath.exp((((-this.f555mu) * x) * x) / this.omega);
    }

    public double cumulativeProbability(double x) {
        return Gamma.regularizedGammaP(this.f555mu, ((this.f555mu * x) * x) / this.omega);
    }

    public double getNumericalMean() {
        return (Gamma.gamma(this.f555mu + 0.5d) / Gamma.gamma(this.f555mu)) * FastMath.sqrt(this.omega / this.f555mu);
    }

    public double getNumericalVariance() {
        double v = Gamma.gamma(this.f555mu + 0.5d) / Gamma.gamma(this.f555mu);
        return this.omega * (1.0d - (((1.0d / this.f555mu) * v) * v));
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
