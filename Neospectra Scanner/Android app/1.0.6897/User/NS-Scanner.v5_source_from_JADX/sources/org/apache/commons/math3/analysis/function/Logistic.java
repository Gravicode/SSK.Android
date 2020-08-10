package org.apache.commons.math3.analysis.function;

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

public class Logistic implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: a */
    private final double f505a;

    /* renamed from: b */
    private final double f506b;

    /* renamed from: k */
    private final double f507k;

    /* renamed from: m */
    private final double f508m;
    private final double oneOverN;

    /* renamed from: q */
    private final double f509q;

    public static class Parametric implements ParametricUnivariateFunction {
        public double value(double x, double... param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            double[] dArr = param;
            validateParameters(dArr);
            return Logistic.value(dArr[1] - x, dArr[0], dArr[2], dArr[3], dArr[4], 1.0d / dArr[5]);
        }

        public double[] gradient(double x, double... param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            double[] dArr = param;
            validateParameters(dArr);
            double b = dArr[2];
            double q = dArr[3];
            double mMinusX = dArr[1] - x;
            double oneOverN = 1.0d / dArr[5];
            double exp = FastMath.exp(b * mMinusX);
            double qExp = q * exp;
            double qExp1 = qExp + 1.0d;
            double factor1 = ((dArr[0] - dArr[4]) * oneOverN) / FastMath.pow(qExp1, oneOverN);
            double factor2 = (-factor1) / qExp1;
            double d = b;
            double factor12 = factor1;
            double factor13 = q;
            double qExp12 = qExp1;
            double oneOverN2 = oneOverN;
            return new double[]{Logistic.value(mMinusX, 1.0d, d, factor13, 0.0d, oneOverN), factor2 * b * qExp, factor2 * mMinusX * qExp, factor2 * exp, Logistic.value(mMinusX, 0.0d, d, factor13, 1.0d, oneOverN), factor12 * FastMath.log(qExp12) * oneOverN2};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException, NotStrictlyPositiveException {
            if (param == null) {
                throw new NullArgumentException();
            } else if (param.length != 6) {
                throw new DimensionMismatchException(param.length, 6);
            } else if (param[5] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(param[5]));
            }
        }
    }

    public Logistic(double k, double m, double b, double q, double a, double n) throws NotStrictlyPositiveException {
        if (n <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(n));
        }
        this.f507k = k;
        this.f508m = m;
        this.f506b = b;
        this.f509q = q;
        this.f505a = a;
        this.oneOverN = 1.0d / n;
    }

    public double value(double x) {
        return value(this.f508m - x, this.f507k, this.f506b, this.f509q, this.f505a, this.oneOverN);
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* access modifiers changed from: private */
    public static double value(double mMinusX, double k, double b, double q, double a, double oneOverN2) {
        return ((k - a) / FastMath.pow((FastMath.exp(b * mMinusX) * q) + 1.0d, oneOverN2)) + a;
    }

    public DerivativeStructure value(DerivativeStructure t) {
        return t.negate().add(this.f508m).multiply(this.f506b).exp().multiply(this.f509q).add(1.0d).pow(this.oneOverN).reciprocal().multiply(this.f507k - this.f505a).add(this.f505a);
    }
}
