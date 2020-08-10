package org.apache.commons.math3.optimization.fitting;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optimization.MultivariateDifferentiableVectorOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;

@Deprecated
public class CurveFitter<T extends ParametricUnivariateFunction> {
    /* access modifiers changed from: private */
    public final List<WeightedObservedPoint> observations;
    @Deprecated
    private final DifferentiableMultivariateVectorOptimizer oldOptimizer;
    private final MultivariateDifferentiableVectorOptimizer optimizer;

    @Deprecated
    private class OldTheoreticalValuesFunction implements DifferentiableMultivariateVectorFunction {
        /* access modifiers changed from: private */

        /* renamed from: f */
        public final ParametricUnivariateFunction f727f;

        OldTheoreticalValuesFunction(ParametricUnivariateFunction f) {
            this.f727f = f;
        }

        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() {
                public double[][] value(double[] point) {
                    double[][] jacobian = new double[CurveFitter.this.observations.size()][];
                    int i = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i2 = i + 1;
                        jacobian[i] = OldTheoreticalValuesFunction.this.f727f.gradient(observed.getX(), point);
                        i = i2;
                    }
                    return jacobian;
                }
            };
        }

        public double[] value(double[] point) {
            double[] values = new double[CurveFitter.this.observations.size()];
            int i = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                int i2 = i + 1;
                values[i] = this.f727f.value(observed.getX(), point);
                i = i2;
            }
            return values;
        }
    }

    private class TheoreticalValuesFunction implements MultivariateDifferentiableVectorFunction {

        /* renamed from: f */
        private final ParametricUnivariateFunction f728f;

        TheoreticalValuesFunction(ParametricUnivariateFunction f) {
            this.f728f = f;
        }

        public double[] value(double[] point) {
            double[] values = new double[CurveFitter.this.observations.size()];
            int i = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                int i2 = i + 1;
                values[i] = this.f728f.value(observed.getX(), point);
                i = i2;
            }
            return values;
        }

        public DerivativeStructure[] value(DerivativeStructure[] point) {
            DerivativeStructure[] derivativeStructureArr = point;
            double[] parameters = new double[derivativeStructureArr.length];
            for (int k = 0; k < derivativeStructureArr.length; k++) {
                parameters[k] = derivativeStructureArr[k].getValue();
            }
            DerivativeStructure[] values = new DerivativeStructure[CurveFitter.this.observations.size()];
            int i = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                DerivativeStructure vi = new DerivativeStructure(derivativeStructureArr.length, 1, this.f728f.value(observed.getX(), parameters));
                for (int k2 = 0; k2 < derivativeStructureArr.length; k2++) {
                    DerivativeStructure derivativeStructure = r10;
                    DerivativeStructure derivativeStructure2 = new DerivativeStructure(derivativeStructureArr.length, 1, k2, 0.0d);
                    vi = vi.add(derivativeStructure);
                }
                int i2 = i + 1;
                values[i] = vi;
                i = i2;
            }
            return values;
        }
    }

    @Deprecated
    public CurveFitter(DifferentiableMultivariateVectorOptimizer optimizer2) {
        this.oldOptimizer = optimizer2;
        this.optimizer = null;
        this.observations = new ArrayList();
    }

    public CurveFitter(MultivariateDifferentiableVectorOptimizer optimizer2) {
        this.oldOptimizer = null;
        this.optimizer = optimizer2;
        this.observations = new ArrayList();
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
        PointVectorValuePair optimum;
        double[] target = new double[this.observations.size()];
        double[] weights = new double[this.observations.size()];
        int i = 0;
        for (WeightedObservedPoint point : this.observations) {
            target[i] = point.getY();
            weights[i] = point.getWeight();
            i++;
        }
        if (this.optimizer == null) {
            optimum = this.oldOptimizer.optimize(maxEval, new OldTheoreticalValuesFunction(f), target, weights, initialGuess);
        } else {
            optimum = this.optimizer.optimize(maxEval, new TheoreticalValuesFunction(f), target, weights, initialGuess);
        }
        return optimum.getPointRef();
    }
}
