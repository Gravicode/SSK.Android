package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.util.FastMath;

public class Power implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: p */
    private final double f512p;

    public Power(double p) {
        this.f512p = p;
    }

    public double value(double x) {
        return FastMath.pow(x, this.f512p);
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    public DerivativeStructure value(DerivativeStructure t) {
        return t.pow(this.f512p);
    }
}
