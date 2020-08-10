package org.apache.commons.math3.fitting;

import java.util.Collection;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

public abstract class AbstractCurveFitter {

    protected static class TheoreticalValuesFunction {
        /* access modifiers changed from: private */

        /* renamed from: f */
        public final ParametricUnivariateFunction f564f;
        /* access modifiers changed from: private */
        public final double[] points;

        public TheoreticalValuesFunction(ParametricUnivariateFunction f, Collection<WeightedObservedPoint> observations) {
            this.f564f = f;
            this.points = new double[observations.size()];
            int i = 0;
            for (WeightedObservedPoint obs : observations) {
                int i2 = i + 1;
                this.points[i] = obs.getX();
                i = i2;
            }
        }

        public MultivariateVectorFunction getModelFunction() {
            return new MultivariateVectorFunction() {
                public double[] value(double[] p) {
                    int len = TheoreticalValuesFunction.this.points.length;
                    double[] values = new double[len];
                    for (int i = 0; i < len; i++) {
                        values[i] = TheoreticalValuesFunction.this.f564f.value(TheoreticalValuesFunction.this.points[i], p);
                    }
                    return values;
                }
            };
        }

        public MultivariateMatrixFunction getModelFunctionJacobian() {
            return new MultivariateMatrixFunction() {
                public double[][] value(double[] p) {
                    int len = TheoreticalValuesFunction.this.points.length;
                    double[][] jacobian = new double[len][];
                    for (int i = 0; i < len; i++) {
                        jacobian[i] = TheoreticalValuesFunction.this.f564f.gradient(TheoreticalValuesFunction.this.points[i], p);
                    }
                    return jacobian;
                }
            };
        }
    }

    /* access modifiers changed from: protected */
    public abstract LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> collection);

    public double[] fit(Collection<WeightedObservedPoint> points) {
        return getOptimizer().optimize(getProblem(points)).getPoint().toArray();
    }

    /* access modifiers changed from: protected */
    public LeastSquaresOptimizer getOptimizer() {
        return new LevenbergMarquardtOptimizer();
    }
}
