package org.apache.commons.math3.fitting;

import java.util.Collection;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction.Parametric;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;

public class PolynomialCurveFitter extends AbstractCurveFitter {
    private static final Parametric FUNCTION = new Parametric();
    private final double[] initialGuess;
    private final int maxIter;

    private PolynomialCurveFitter(double[] initialGuess2, int maxIter2) {
        this.initialGuess = initialGuess2;
        this.maxIter = maxIter2;
    }

    public static PolynomialCurveFitter create(int degree) {
        return new PolynomialCurveFitter(new double[(degree + 1)], Integer.MAX_VALUE);
    }

    public PolynomialCurveFitter withStartPoint(double[] newStart) {
        return new PolynomialCurveFitter((double[]) newStart.clone(), this.maxIter);
    }

    public PolynomialCurveFitter withMaxIterations(int newMaxIter) {
        return new PolynomialCurveFitter(this.initialGuess, newMaxIter);
    }

    /* access modifiers changed from: protected */
    public LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> observations) {
        int len = observations.size();
        double[] target = new double[len];
        double[] weights = new double[len];
        int i = 0;
        for (WeightedObservedPoint obs : observations) {
            target[i] = obs.getY();
            weights[i] = obs.getWeight();
            i++;
        }
        TheoreticalValuesFunction model = new TheoreticalValuesFunction(FUNCTION, observations);
        if (this.initialGuess != null) {
            return new LeastSquaresBuilder().maxEvaluations(Integer.MAX_VALUE).maxIterations(this.maxIter).start(this.initialGuess).target(target).weight(new DiagonalMatrix(weights)).model(model.getModelFunction(), model.getModelFunctionJacobian()).build();
        }
        throw new MathInternalError();
    }
}
