package org.apache.commons.math3.analysis.function;

import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class Gaussian implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double i2s2;

    /* renamed from: is */
    private final double f504is;
    private final double mean;
    private final double norm;

    public static class Parametric implements ParametricUnivariateFunction {
        public double value(double x, double... param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            validateParameters(param);
            return Gaussian.value(x - param[1], param[0], 1.0d / ((param[2] * 2.0d) * param[2]));
        }

        public double[] gradient(double x, double... param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            double[] dArr = param;
            validateParameters(dArr);
            double norm = dArr[0];
            double diff = x - dArr[1];
            double sigma = dArr[2];
            double i2s2 = 1.0d / ((sigma * 2.0d) * sigma);
            double n = Gaussian.value(diff, 1.0d, i2s2);
            double m = norm * n * 2.0d * i2s2 * diff;
            return new double[]{n, m, (m * diff) / sigma};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            if (param == null) {
                throw new NullArgumentException();
            } else if (param.length != 3) {
                throw new DimensionMismatchException(param.length, 3);
            } else if (param[2] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(param[2]));
            }
        }
    }

    public Gaussian(double norm2, double mean2, double sigma) throws NotStrictlyPositiveException {
        if (sigma <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(sigma));
        }
        this.norm = norm2;
        this.mean = mean2;
        this.f504is = 1.0d / sigma;
        this.i2s2 = this.f504is * 0.5d * this.f504is;
    }

    public Gaussian(double mean2, double sigma) throws NotStrictlyPositiveException {
        this(1.0d / (FastMath.sqrt(6.283185307179586d) * sigma), mean2, sigma);
    }

    public Gaussian() {
        this(0.0d, 1.0d);
    }

    public double value(double x) {
        return value(x - this.mean, this.norm, this.i2s2);
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* access modifiers changed from: private */
    public static double value(double xMinusMean, double norm2, double i2s22) {
        return FastMath.exp((-xMinusMean) * xMinusMean * i2s22) * norm2;
    }

    public DerivativeStructure value(DerivativeStructure t) throws DimensionMismatchException {
        double[] p;
        double u = this.f504is * (t.getValue() - this.mean);
        int i = 1;
        double[] f = new double[(t.getOrder() + 1)];
        double[] p2 = new double[f.length];
        p2[0] = 1.0d;
        double u2 = u * u;
        double coeff = this.norm * FastMath.exp(-0.5d * u2);
        if (coeff <= Precision.SAFE_MIN) {
            Arrays.fill(f, 0.0d);
            double[] dArr = p2;
        } else {
            f[0] = coeff;
            double coeff2 = coeff;
            int n = 1;
            while (n < f.length) {
                p2[n] = -p2[n - 1];
                double v = 0.0d;
                int k = n;
                while (k >= 0) {
                    v = (v * u2) + p2[k];
                    if (k > 2) {
                        p = p2;
                        p[k - 2] = (((double) (k - 1)) * p[k - 1]) - p[k - 3];
                    } else {
                        p = p2;
                        if (k == 2) {
                            i = 1;
                            p[0] = p[1];
                            k -= 2;
                            p2 = p;
                        }
                    }
                    i = 1;
                    k -= 2;
                    p2 = p;
                }
                double[] p3 = p2;
                if ((n & 1) == i) {
                    v *= u;
                }
                coeff2 *= this.f504is;
                f[n] = coeff2 * v;
                n++;
                p2 = p3;
            }
            double d = coeff2;
        }
        return t.compose(f);
    }
}
