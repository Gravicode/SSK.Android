package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;

public class Constant implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: c */
    private final double f503c;

    public Constant(double c) {
        this.f503c = c;
    }

    public double value(double x) {
        return this.f503c;
    }

    @Deprecated
    public DifferentiableUnivariateFunction derivative() {
        return new Constant(0.0d);
    }

    public DerivativeStructure value(DerivativeStructure t) {
        return new DerivativeStructure(t.getFreeParameters(), t.getOrder(), this.f503c);
    }
}
