package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

public class Minus implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    public double value(double x) {
        return -x;
    }

    @Deprecated
    public DifferentiableUnivariateFunction derivative() {
        return new Constant(-1.0d);
    }

    public DerivativeStructure value(DerivativeStructure t) {
        return t.negate();
    }
}
