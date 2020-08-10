package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class LogisticDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20141003;

    /* renamed from: mu */
    private final double f553mu;

    /* renamed from: s */
    private final double f554s;

    public LogisticDistribution(double mu, double s) {
        this(new Well19937c(), mu, s);
    }

    public LogisticDistribution(RandomGenerator rng, double mu, double s) {
        super(rng);
        if (s <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NOT_POSITIVE_SCALE, Double.valueOf(s));
        }
        this.f553mu = mu;
        this.f554s = s;
    }

    public double getLocation() {
        return this.f553mu;
    }

    public double getScale() {
        return this.f554s;
    }

    public double density(double x) {
        double v = FastMath.exp(-((x - this.f553mu) / this.f554s));
        return ((1.0d / this.f554s) * v) / ((v + 1.0d) * (1.0d + v));
    }

    public double cumulativeProbability(double x) {
        return 1.0d / (FastMath.exp(-((1.0d / this.f554s) * (x - this.f553mu))) + 1.0d);
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Double.valueOf(0.0d), Double.valueOf(1.0d));
        } else if (p == 0.0d) {
            return 0.0d;
        } else {
            if (p == 1.0d) {
                return Double.POSITIVE_INFINITY;
            }
            return (this.f554s * Math.log(p / (1.0d - p))) + this.f553mu;
        }
    }

    public double getNumericalMean() {
        return this.f553mu;
    }

    public double getNumericalVariance() {
        return (1.0d / (this.f554s * this.f554s)) * 3.289868133696453d;
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
