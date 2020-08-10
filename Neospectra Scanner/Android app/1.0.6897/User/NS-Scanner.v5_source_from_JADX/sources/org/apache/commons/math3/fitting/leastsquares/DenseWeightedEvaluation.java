package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem.Evaluation;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

class DenseWeightedEvaluation extends AbstractEvaluation {
    private final Evaluation unweighted;
    private final RealMatrix weightSqrt;

    DenseWeightedEvaluation(Evaluation unweighted2, RealMatrix weightSqrt2) {
        super(weightSqrt2.getColumnDimension());
        this.unweighted = unweighted2;
        this.weightSqrt = weightSqrt2;
    }

    public RealMatrix getJacobian() {
        return this.weightSqrt.multiply(this.unweighted.getJacobian());
    }

    public RealVector getResiduals() {
        return this.weightSqrt.operate(this.unweighted.getResiduals());
    }

    public RealVector getPoint() {
        return this.unweighted.getPoint();
    }
}
