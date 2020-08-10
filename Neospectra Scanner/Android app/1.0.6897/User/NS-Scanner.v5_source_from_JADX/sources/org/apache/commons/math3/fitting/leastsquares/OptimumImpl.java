package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

class OptimumImpl implements Optimum {
    private final int evaluations;
    private final int iterations;
    private final Evaluation value;

    OptimumImpl(Evaluation value2, int evaluations2, int iterations2) {
        this.value = value2;
        this.evaluations = evaluations2;
        this.iterations = iterations2;
    }

    public int getEvaluations() {
        return this.evaluations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public RealMatrix getCovariances(double threshold) {
        return this.value.getCovariances(threshold);
    }

    public RealVector getSigma(double covarianceSingularityThreshold) {
        return this.value.getSigma(covarianceSingularityThreshold);
    }

    public double getRMS() {
        return this.value.getRMS();
    }

    public RealMatrix getJacobian() {
        return this.value.getJacobian();
    }

    public double getCost() {
        return this.value.getCost();
    }

    public RealVector getResiduals() {
        return this.value.getResiduals();
    }

    public RealVector getPoint() {
        return this.value.getPoint();
    }
}
