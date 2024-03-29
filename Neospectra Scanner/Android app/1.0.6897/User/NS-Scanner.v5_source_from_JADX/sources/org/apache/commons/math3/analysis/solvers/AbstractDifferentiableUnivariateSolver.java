package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

@Deprecated
public abstract class AbstractDifferentiableUnivariateSolver extends BaseAbstractUnivariateSolver<DifferentiableUnivariateFunction> implements DifferentiableUnivariateSolver {
    private UnivariateFunction functionDerivative;

    protected AbstractDifferentiableUnivariateSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    protected AbstractDifferentiableUnivariateSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    /* access modifiers changed from: protected */
    public double computeDerivativeObjectiveValue(double point) throws TooManyEvaluationsException {
        incrementEvaluationCount();
        return this.functionDerivative.value(point);
    }

    /* access modifiers changed from: protected */
    public void setup(int maxEval, DifferentiableUnivariateFunction f, double min, double max, double startValue) {
        super.setup(maxEval, f, min, max, startValue);
        this.functionDerivative = f.derivative();
    }
}
