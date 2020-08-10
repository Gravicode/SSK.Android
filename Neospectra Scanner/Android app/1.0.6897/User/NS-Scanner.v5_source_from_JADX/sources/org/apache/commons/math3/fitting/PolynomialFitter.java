package org.apache.commons.math3.fitting;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction.Parametric;
import org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer;

@Deprecated
public class PolynomialFitter extends CurveFitter<Parametric> {
    public PolynomialFitter(MultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    public double[] fit(int maxEval, double[] guess) {
        return fit(maxEval, new Parametric(), guess);
    }

    public double[] fit(double[] guess) {
        return fit(new Parametric(), guess);
    }
}
