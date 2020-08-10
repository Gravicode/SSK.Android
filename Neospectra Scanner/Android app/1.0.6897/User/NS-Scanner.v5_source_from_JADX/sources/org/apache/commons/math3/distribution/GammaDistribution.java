package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;

public class GammaDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = 20120524;
    private final double densityPrefactor1;
    private final double densityPrefactor2;
    private final double logDensityPrefactor1;
    private final double logDensityPrefactor2;
    private final double maxLogY;
    private final double minY;
    private final double scale;
    private final double shape;
    private final double shiftedShape;
    private final double solverAbsoluteAccuracy;

    public GammaDistribution(double shape2, double scale2) throws NotStrictlyPositiveException {
        this(shape2, scale2, 1.0E-9d);
    }

    public GammaDistribution(double shape2, double scale2, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        this(new Well19937c(), shape2, scale2, inverseCumAccuracy);
    }

    public GammaDistribution(RandomGenerator rng, double shape2, double scale2) throws NotStrictlyPositiveException {
        this(rng, shape2, scale2, 1.0E-9d);
    }

    public GammaDistribution(RandomGenerator rng, double shape2, double scale2, double inverseCumAccuracy) throws NotStrictlyPositiveException {
        double d = shape2;
        double d2 = scale2;
        super(rng);
        if (d <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, Double.valueOf(shape2));
        } else if (d2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SCALE, Double.valueOf(scale2));
        } else {
            this.shape = d;
            this.scale = d2;
            this.solverAbsoluteAccuracy = inverseCumAccuracy;
            this.shiftedShape = d + 4.7421875d + 0.5d;
            double aux = 2.718281828459045d / (this.shiftedShape * 6.283185307179586d);
            this.densityPrefactor2 = (FastMath.sqrt(aux) * d) / Gamma.lanczos(shape2);
            this.logDensityPrefactor2 = (FastMath.log(shape2) + (FastMath.log(aux) * 0.5d)) - FastMath.log(Gamma.lanczos(shape2));
            this.densityPrefactor1 = (this.densityPrefactor2 / d2) * FastMath.pow(this.shiftedShape, -d) * FastMath.exp(d + 4.7421875d);
            this.logDensityPrefactor1 = ((this.logDensityPrefactor2 - FastMath.log(scale2)) - (FastMath.log(this.shiftedShape) * d)) + d + 4.7421875d;
            this.minY = (d + 4.7421875d) - FastMath.log(Double.MAX_VALUE);
            this.maxLogY = FastMath.log(Double.MAX_VALUE) / (d - 1.0d);
        }
    }

    @Deprecated
    public double getAlpha() {
        return this.shape;
    }

    public double getShape() {
        return this.shape;
    }

    @Deprecated
    public double getBeta() {
        return this.scale;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double x) {
        if (x < 0.0d) {
            return 0.0d;
        }
        double y = x / this.scale;
        if (y > this.minY && FastMath.log(y) < this.maxLogY) {
            return this.densityPrefactor1 * FastMath.exp(-y) * FastMath.pow(y, this.shape - 1.0d);
        }
        double aux1 = (y - this.shiftedShape) / this.shiftedShape;
        return (this.densityPrefactor2 / x) * FastMath.exp((((-y) * 5.2421875d) / this.shiftedShape) + 4.7421875d + (this.shape * (FastMath.log1p(aux1) - aux1)));
    }

    public double logDensity(double x) {
        if (x < 0.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        double y = x / this.scale;
        if (y > this.minY && FastMath.log(y) < this.maxLogY) {
            return (this.logDensityPrefactor1 - y) + (FastMath.log(y) * (this.shape - 1.0d));
        }
        double aux1 = (y - this.shiftedShape) / this.shiftedShape;
        return (this.logDensityPrefactor2 - FastMath.log(x)) + (((-y) * 5.2421875d) / this.shiftedShape) + 4.7421875d + (this.shape * (FastMath.log1p(aux1) - aux1));
    }

    public double cumulativeProbability(double x) {
        if (x <= 0.0d) {
            return 0.0d;
        }
        return Gamma.regularizedGammaP(this.shape, x / this.scale);
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return this.shape * this.scale;
    }

    public double getNumericalVariance() {
        return this.shape * this.scale * this.scale;
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

    public double sample() {
        if (this.shape < 1.0d) {
            while (true) {
                double bGS = (this.shape / 2.718281828459045d) + 1.0d;
                double p = bGS * this.random.nextDouble();
                if (p <= 1.0d) {
                    double x = FastMath.pow(p, 1.0d / this.shape);
                    if (this.random.nextDouble() <= FastMath.exp(-x)) {
                        return this.scale * x;
                    }
                } else {
                    double x2 = FastMath.log((bGS - p) / this.shape) * -1.0d;
                    if (this.random.nextDouble() <= FastMath.pow(x2, this.shape - 1.0d)) {
                        return this.scale * x2;
                    }
                }
            }
        } else {
            double d = this.shape - 0.3333333333333333d;
            double c = 1.0d / (FastMath.sqrt(d) * 3.0d);
            while (true) {
                double x3 = this.random.nextGaussian();
                double v = ((c * x3) + 1.0d) * ((c * x3) + 1.0d) * ((c * x3) + 1.0d);
                if (v > 0.0d) {
                    double x22 = x3 * x3;
                    double u = this.random.nextDouble();
                    if (u < 1.0d - ((0.0331d * x22) * x22)) {
                        return this.scale * d * v;
                    }
                    if (FastMath.log(u) < (0.5d * x22) + (((1.0d - v) + FastMath.log(v)) * d)) {
                        return this.scale * d * v;
                    }
                }
            }
        }
    }
}
