package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class LaplaceDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20141003;
    private final double beta;

    /* renamed from: mu */
    private final double f550mu;

    public LaplaceDistribution(double mu, double beta2) {
        this(new Well19937c(), mu, beta2);
    }

    public LaplaceDistribution(RandomGenerator rng, double mu, double beta2) {
        super(rng);
        if (beta2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(beta2));
        }
        this.f550mu = mu;
        this.beta = beta2;
    }

    public double getLocation() {
        return this.f550mu;
    }

    public double getScale() {
        return this.beta;
    }

    public double density(double x) {
        return FastMath.exp((-FastMath.abs(x - this.f550mu)) / this.beta) / (this.beta * 2.0d);
    }

    public double cumulativeProbability(double x) {
        if (x <= this.f550mu) {
            return FastMath.exp((x - this.f550mu) / this.beta) / 2.0d;
        }
        return 1.0d - (FastMath.exp((this.f550mu - x) / this.beta) / 2.0d);
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
            return this.f550mu + (this.beta * (p > 0.5d ? -Math.log(2.0d - (p * 2.0d)) : Math.log(2.0d * p)));
        }
    }

    public double getNumericalMean() {
        return this.f550mu;
    }

    public double getNumericalVariance() {
        return this.beta * 2.0d * this.beta;
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
