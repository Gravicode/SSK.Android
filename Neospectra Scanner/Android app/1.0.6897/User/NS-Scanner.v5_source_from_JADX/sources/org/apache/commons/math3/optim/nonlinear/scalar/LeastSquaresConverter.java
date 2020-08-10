package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;

public class LeastSquaresConverter implements MultivariateFunction {
    private final MultivariateVectorFunction function;
    private final double[] observations;
    private final RealMatrix scale;
    private final double[] weights;

    public LeastSquaresConverter(MultivariateVectorFunction function2, double[] observations2) {
        this.function = function2;
        this.observations = (double[]) observations2.clone();
        this.weights = null;
        this.scale = null;
    }

    public LeastSquaresConverter(MultivariateVectorFunction function2, double[] observations2, double[] weights2) {
        if (observations2.length != weights2.length) {
            throw new DimensionMismatchException(observations2.length, weights2.length);
        }
        this.function = function2;
        this.observations = (double[]) observations2.clone();
        this.weights = (double[]) weights2.clone();
        this.scale = null;
    }

    public LeastSquaresConverter(MultivariateVectorFunction function2, double[] observations2, RealMatrix scale2) {
        if (observations2.length != scale2.getColumnDimension()) {
            throw new DimensionMismatchException(observations2.length, scale2.getColumnDimension());
        }
        this.function = function2;
        this.observations = (double[]) observations2.clone();
        this.weights = null;
        this.scale = scale2.copy();
    }

    public double value(double[] point) {
        double[] residuals = this.function.value(point);
        if (residuals.length != this.observations.length) {
            throw new DimensionMismatchException(residuals.length, this.observations.length);
        }
        int i = 0;
        for (int i2 = 0; i2 < residuals.length; i2++) {
            residuals[i2] = residuals[i2] - this.observations[i2];
        }
        double sumSquares = 0.0d;
        if (this.weights != null) {
            while (i < residuals.length) {
                double ri = residuals[i];
                sumSquares += this.weights[i] * ri * ri;
                i++;
            }
        } else if (this.scale != null) {
            double[] arr$ = this.scale.operate(residuals);
            int len$ = arr$.length;
            while (i < len$) {
                double yi = arr$[i];
                sumSquares += yi * yi;
                i++;
            }
        } else {
            double[] arr$2 = residuals;
            int len$2 = arr$2.length;
            while (i < len$2) {
                double ri2 = arr$2[i];
                sumSquares += ri2 * ri2;
                i++;
            }
        }
        return sumSquares;
    }
}
