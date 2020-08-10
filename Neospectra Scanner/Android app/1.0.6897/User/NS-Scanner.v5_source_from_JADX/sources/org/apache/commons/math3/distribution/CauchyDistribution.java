package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class CauchyDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 8589540077390120676L;
    private final double median;
    private final double scale;
    private final double solverAbsoluteAccuracy;

    public CauchyDistribution() {
        this(0.0d, 1.0d);
    }

    public CauchyDistribution(double median2, double scale2) {
        this(median2, scale2, 1.0E-9d);
    }

    public CauchyDistribution(double median2, double scale2, double inverseCumAccuracy) {
        this(new Well19937c(), median2, scale2, inverseCumAccuracy);
    }

    public CauchyDistribution(RandomGenerator rng, double median2, double scale2) {
        this(rng, median2, scale2, 1.0E-9d);
    }

    public CauchyDistribution(RandomGenerator rng, double median2, double scale2, double inverseCumAccuracy) {
        super(rng);
        if (scale2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(scale2));
        }
        this.scale = scale2;
        this.median = median2;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double cumulativeProbability(double x) {
        return (FastMath.atan((x - this.median) / this.scale) / 3.141592653589793d) + 0.5d;
    }

    public double getMedian() {
        return this.median;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double x) {
        double dev = x - this.median;
        return (this.scale / ((dev * dev) + (this.scale * this.scale))) * 0.3183098861837907d;
    }

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        } else if (p == 0.0d) {
            return Double.NEGATIVE_INFINITY;
        } else {
            if (p == 1.0d) {
                return Double.POSITIVE_INFINITY;
            }
            return this.median + (this.scale * FastMath.tan((p - 0.5d) * 3.141592653589793d));
        }
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return Double.NaN;
    }

    public double getNumericalVariance() {
        return Double.NaN;
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
