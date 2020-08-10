package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class GumbelDistribution extends AbstractRealDistribution {
    private static final double EULER = 0.5778636748954609d;
    private static final long serialVersionUID = 20141003;
    private final double beta;

    /* renamed from: mu */
    private final double f548mu;

    public GumbelDistribution(double mu, double beta2) {
        this(new Well19937c(), mu, beta2);
    }

    public GumbelDistribution(RandomGenerator rng, double mu, double beta2) {
        super(rng);
        if (beta2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(beta2));
        }
        this.beta = beta2;
        this.f548mu = mu;
    }

    public double getLocation() {
        return this.f548mu;
    }

    public double getScale() {
        return this.beta;
    }

    public double density(double x) {
        double z = (x - this.f548mu) / this.beta;
        return FastMath.exp((-z) - FastMath.exp(-z)) / this.beta;
    }

    public double cumulativeProbability(double x) {
        return FastMath.exp(-FastMath.exp(-((x - this.f548mu) / this.beta)));
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Double.valueOf(0.0d), Double.valueOf(1.0d));
        } else if (p == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        } else {
            if (p == 1.0d) {
                return Double.POSITIVE_INFINITY;
            }
            return this.f548mu - (FastMath.log(-FastMath.log(p)) * this.beta);
        }
    }

    public double getNumericalMean() {
        return this.f548mu + (this.beta * EULER);
    }

    public double getNumericalVariance() {
        return this.beta * this.beta * 1.6449340668482264d;
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
