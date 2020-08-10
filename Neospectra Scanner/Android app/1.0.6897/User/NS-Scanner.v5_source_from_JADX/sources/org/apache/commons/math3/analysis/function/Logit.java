package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

public class Logit implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: hi */
    private final double f510hi;

    /* renamed from: lo */
    private final double f511lo;

    public static class Parametric implements ParametricUnivariateFunction {
        public double value(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return Logit.value(x, param[0], param[1]);
        }

        public double[] gradient(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return new double[]{1.0d / (param[0] - x), 1.0d / (param[1] - x)};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            } else if (param.length != 2) {
                throw new DimensionMismatchException(param.length, 2);
            }
        }
    }

    public Logit() {
        this(0.0d, 1.0d);
    }

    public Logit(double lo, double hi) {
        this.f511lo = lo;
        this.f510hi = hi;
    }

    public double value(double x) throws OutOfRangeException {
        return value(x, this.f511lo, this.f510hi);
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* access modifiers changed from: private */
    public static double value(double x, double lo, double hi) throws OutOfRangeException {
        if (x >= lo && x <= hi) {
            return FastMath.log((x - lo) / (hi - x));
        }
        throw new OutOfRangeException(Double.valueOf(x), Double.valueOf(lo), Double.valueOf(hi));
    }

    public DerivativeStructure value(DerivativeStructure t) throws OutOfRangeException {
        double x = t.getValue();
        if (x < this.f511lo || x > this.f510hi) {
            throw new OutOfRangeException(Double.valueOf(x), Double.valueOf(this.f511lo), Double.valueOf(this.f510hi));
        }
        double[] f = new double[(t.getOrder() + 1)];
        f[0] = FastMath.log((x - this.f511lo) / (this.f510hi - x));
        if (Double.isInfinite(f[0])) {
            if (f.length > 1) {
                f[1] = Double.POSITIVE_INFINITY;
            }
            for (int i = 2; i < f.length; i++) {
                f[i] = f[i - 2];
            }
        } else {
            double invL = 1.0d / (x - this.f511lo);
            double xL = invL;
            double invH = 1.0d / (this.f510hi - x);
            double xH = invH;
            for (int i2 = 1; i2 < f.length; i2++) {
                f[i2] = xL + xH;
                xL *= ((double) (-i2)) * invL;
                xH *= ((double) i2) * invH;
            }
        }
        return t.compose(f);
    }
}
