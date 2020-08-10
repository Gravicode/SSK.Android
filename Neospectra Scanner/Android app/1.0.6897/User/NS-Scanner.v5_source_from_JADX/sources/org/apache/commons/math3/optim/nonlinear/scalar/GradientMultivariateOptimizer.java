package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;

public abstract class GradientMultivariateOptimizer extends MultivariateOptimizer {
    private MultivariateVectorFunction gradient;

    protected GradientMultivariateOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    /* access modifiers changed from: protected */
    public double[] computeObjectiveGradient(double[] params) {
        return this.gradient.value(params);
    }

    public PointValuePair optimize(OptimizationData... optData) throws TooManyEvaluationsException {
        return super.optimize(optData);
    }

    /* access modifiers changed from: protected */
    public void parseOptimizationData(OptimizationData... optData) {
        OptimizationData[] arr$;
        super.parseOptimizationData(optData);
        for (OptimizationData data : optData) {
            if (data instanceof ObjectiveFunctionGradient) {
                this.gradient = ((ObjectiveFunctionGradient) data).getObjectiveFunctionGradient();
                return;
            }
        }
    }
}
