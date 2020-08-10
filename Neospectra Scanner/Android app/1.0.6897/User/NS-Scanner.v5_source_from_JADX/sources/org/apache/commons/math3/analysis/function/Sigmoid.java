package org.apache.commons.math3.analysis.function;

import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

public class Sigmoid implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: hi */
    private final double f513hi;

    /* renamed from: lo */
    private final double f514lo;

    public static class Parametric implements ParametricUnivariateFunction {
        public double value(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return Sigmoid.value(x, param[0], param[1]);
        }

        public double[] gradient(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double invExp1 = 1.0d / (FastMath.exp(-x) + 1.0d);
            return new double[]{1.0d - invExp1, invExp1};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            } else if (param.length != 2) {
                throw new DimensionMismatchException(param.length, 2);
            }
        }
    }

    public Sigmoid() {
        this(0.0d, 1.0d);
    }

    public Sigmoid(double lo, double hi) {
        this.f514lo = lo;
        this.f513hi = hi;
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    public double value(double x) {
        return value(x, this.f514lo, this.f513hi);
    }

    /* access modifiers changed from: private */
    public static double value(double x, double lo, double hi) {
        return ((hi - lo) / (FastMath.exp(-x) + 1.0d)) + lo;
    }

    public DerivativeStructure value(DerivativeStructure t) throws DimensionMismatchException {
        int i = 1;
        double[] f = new double[(t.getOrder() + 1)];
        double exp = FastMath.exp(-t.getValue());
        if (Double.isInfinite(exp)) {
            f[0] = this.f514lo;
            Arrays.fill(f, 1, f.length, 0.0d);
        } else {
            double[] p = new double[f.length];
            double d = 1.0d;
            double inv = 1.0d / (exp + 1.0d);
            double coeff = this.f513hi - this.f514lo;
            int n = 0;
            while (n < f.length) {
                double v = 0.0d;
                p[n] = d;
                int k = n;
                while (k >= 0) {
                    v = (v * exp) + p[k];
                    if (k > i) {
                        p[k - 1] = (((double) ((n - k) + 2)) * p[k - 2]) - (((double) (k - 1)) * p[k - 1]);
                    } else {
                        p[0] = 0.0d;
                    }
                    k--;
                    i = 1;
                }
                coeff *= inv;
                f[n] = coeff * v;
                n++;
                i = 1;
                d = 1.0d;
            }
            f[0] = f[0] + this.f514lo;
        }
        return t.compose(f);
    }
}
