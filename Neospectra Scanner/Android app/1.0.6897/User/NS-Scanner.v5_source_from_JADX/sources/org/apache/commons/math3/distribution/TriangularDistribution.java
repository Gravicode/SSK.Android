package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

public class TriangularDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20120112;

    /* renamed from: a */
    private final double f556a;

    /* renamed from: b */
    private final double f557b;

    /* renamed from: c */
    private final double f558c;
    private final double solverAbsoluteAccuracy;

    public TriangularDistribution(double a, double c, double b) throws NumberIsTooLargeException, NumberIsTooSmallException {
        this(new Well19937c(), a, c, b);
    }

    public TriangularDistribution(RandomGenerator rng, double a, double c, double b) throws NumberIsTooLargeException, NumberIsTooSmallException {
        super(rng);
        if (a >= b) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(a), Double.valueOf(b), false);
        } else if (c < a) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_TOO_SMALL, Double.valueOf(c), Double.valueOf(a), true);
        } else if (c > b) {
            throw new NumberIsTooLargeException(LocalizedFormats.NUMBER_TOO_LARGE, Double.valueOf(c), Double.valueOf(b), true);
        } else {
            this.f556a = a;
            this.f558c = c;
            this.f557b = b;
            this.solverAbsoluteAccuracy = FastMath.max(FastMath.ulp(a), FastMath.ulp(b));
        }
    }

    public double getMode() {
        return this.f558c;
    }

    /* access modifiers changed from: protected */
    public double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double density(double x) {
        if (x < this.f556a) {
            return 0.0d;
        }
        if (this.f556a <= x && x < this.f558c) {
            return ((x - this.f556a) * 2.0d) / ((this.f557b - this.f556a) * (this.f558c - this.f556a));
        }
        if (x == this.f558c) {
            return 2.0d / (this.f557b - this.f556a);
        }
        if (this.f558c >= x || x > this.f557b) {
            return 0.0d;
        }
        return ((this.f557b - x) * 2.0d) / ((this.f557b - this.f556a) * (this.f557b - this.f558c));
    }

    public double cumulativeProbability(double x) {
        if (x < this.f556a) {
            return 0.0d;
        }
        if (this.f556a <= x && x < this.f558c) {
            return ((x - this.f556a) * (x - this.f556a)) / ((this.f557b - this.f556a) * (this.f558c - this.f556a));
        }
        if (x == this.f558c) {
            return (this.f558c - this.f556a) / (this.f557b - this.f556a);
        }
        if (this.f558c >= x || x > this.f557b) {
            return 1.0d;
        }
        return 1.0d - (((this.f557b - x) * (this.f557b - x)) / ((this.f557b - this.f556a) * (this.f557b - this.f558c)));
    }

    public double getNumericalMean() {
        return ((this.f556a + this.f557b) + this.f558c) / 3.0d;
    }

    public double getNumericalVariance() {
        return ((((((this.f556a * this.f556a) + (this.f557b * this.f557b)) + (this.f558c * this.f558c)) - (this.f556a * this.f557b)) - (this.f556a * this.f558c)) - (this.f557b * this.f558c)) / 18.0d;
    }

    public double getSupportLowerBound() {
        return this.f556a;
    }

    public double getSupportUpperBound() {
        return this.f557b;
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

    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        if (p < 0.0d || p > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(1));
        } else if (p == 0.0d) {
            return this.f556a;
        } else {
            if (p == 1.0d) {
                return this.f557b;
            }
            if (p < (this.f558c - this.f556a) / (this.f557b - this.f556a)) {
                return this.f556a + FastMath.sqrt((this.f557b - this.f556a) * p * (this.f558c - this.f556a));
            }
            return this.f557b - FastMath.sqrt(((1.0d - p) * (this.f557b - this.f556a)) * (this.f557b - this.f558c));
        }
    }
}
