package org.apache.commons.math3.fitting;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunction;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunctionJacobian;
import org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.Target;
import org.apache.commons.math3.optim.nonlinear.vector.Weight;

@Deprecated
public class CurveFitter<T extends ParametricUnivariateFunction> {
    /* access modifiers changed from: private */
    public final List<WeightedObservedPoint> observations = new ArrayList();
    private final MultivariateVectorOptimizer optimizer;

    private class TheoreticalValuesFunction {
        /* access modifiers changed from: private */

        /* renamed from: f */
        public final ParametricUnivariateFunction f565f;

        TheoreticalValuesFunction(ParametricUnivariateFunction f) {
            this.f565f = f;
        }

        public ModelFunction getModelFunction() {
            return new ModelFunction(new MultivariateVectorFunction() {
                public double[] value(double[] point) {
                    double[] values = new double[CurveFitter.this.observations.size()];
                    int i = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i2 = i + 1;
                        values[i] = TheoreticalValuesFunction.this.f565f.value(observed.getX(), point);
                        i = i2;
                    }
                    return values;
                }
            });
        }

        public ModelFunctionJacobian getModelFunctionJacobian() {
            return new ModelFunctionJacobian(new MultivariateMatrixFunction() {
                public double[][] value(double[] point) {
                    double[][] jacobian = new double[CurveFitter.this.observations.size()][];
                    int i = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i2 = i + 1;
                        jacobian[i] = TheoreticalValuesFunction.this.f565f.gradient(observed.getX(), point);
                        i = i2;
                    }
                    return jacobian;
                }
            });
        }
    }

    public CurveFitter(MultivariateVectorOptimizer optimizer2) {
        this.optimizer = optimizer2;
    }

    public void addObservedPoint(double x, double y) {
        addObservedPoint(1.0d, x, y);
    }

    public void addObservedPoint(double weight, double x, double y) {
        List<WeightedObservedPoint> list = this.observations;
        WeightedObservedPoint weightedObservedPoint = new WeightedObservedPoint(weight, x, y);
        list.add(weightedObservedPoint);
    }

    public void addObservedPoint(WeightedObservedPoint observed) {
        this.observations.add(observed);
    }

    public WeightedObservedPoint[] getObservations() {
        return (WeightedObservedPoint[]) this.observations.toArray(new WeightedObservedPoint[this.observations.size()]);
    }

    public void clearObservations() {
        this.observations.clear();
    }

    public double[] fit(T f, double[] initialGuess) {
        return fit(Integer.MAX_VALUE, f, initialGuess);
    }

    public double[] fit(int maxEval, T f, double[] initialGuess) {
        double[] target = new double[this.observations.size()];
        double[] weights = new double[this.observations.size()];
        int i = 0;
        for (WeightedObservedPoint point : this.observations) {
            target[i] = point.getY();
            weights[i] = point.getWeight();
            i++;
        }
        TheoreticalValuesFunction model = new TheoreticalValuesFunction<>(f);
        return this.optimizer.optimize(new OptimizationData[]{new MaxEval(maxEval), model.getModelFunction(), model.getModelFunctionJacobian(), new Target(target), new Weight(weights), new InitialGuess(initialGuess)}).getPointRef();
    }
}
