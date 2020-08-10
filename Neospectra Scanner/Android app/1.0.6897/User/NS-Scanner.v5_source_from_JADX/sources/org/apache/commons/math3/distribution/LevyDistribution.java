package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.FastMath;

public class LevyDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20130314;

    /* renamed from: c */
    private final double f551c;
    private final double halfC;

    /* renamed from: mu */
    private final double f552mu;

    public LevyDistribution(double mu, double c) {
        this(new Well19937c(), mu, c);
    }

    public LevyDistribution(RandomGenerator rng, double mu, double c) {
        super(rng);
        this.f552mu = mu;
        this.f551c = c;
        this.halfC = 0.5d * c;
    }

    public double density(double x) {
        if (x < this.f552mu) {
            return Double.NaN;
        }
        double delta = x - this.f552mu;
        double f = this.halfC / delta;
        return (FastMath.sqrt(f / 3.141592653589793d) * FastMath.exp(-f)) / delta;
    }

    public double logDensity(double x) {
        if (x < this.f552mu) {
            return Double.NaN;
        }
        double delta = x - this.f552mu;
        double f = this.halfC / delta;
        return ((FastMath.log(f / 3.141592653589793d) * 0.5d) - f) - FastMath.log(delta);
    }

    public double cumulativeProbability(double x) {
        if (x < this.f552mu) {
            return Double.NaN;
        }
        return Erf.erfc(FastMath.sqrt(this.halfC / (x - this.f552mu)));
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        }
        double t = Erf.erfcInv(p);
        return this.f552mu + (this.halfC / (t * t));
    }

    public double getScale() {
        return this.f551c;
    }

    public double getLocation() {
        return this.f552mu;
    }

    public double getNumericalMean() {
        return Double.POSITIVE_INFINITY;
    }

    public double getNumericalVariance() {
        return Double.POSITIVE_INFINITY;
    }

    public double getSupportLowerBound() {
        return this.f552mu;
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
