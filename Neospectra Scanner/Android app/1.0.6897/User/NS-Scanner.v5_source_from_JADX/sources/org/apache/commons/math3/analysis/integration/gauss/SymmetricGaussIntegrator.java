package org.apache.commons.math3.analysis.integration.gauss;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.util.Pair;

public class SymmetricGaussIntegrator extends GaussIntegrator {
    public SymmetricGaussIntegrator(double[] points, double[] weights) throws NonMonotonicSequenceException, DimensionMismatchException {
        super(points, weights);
    }

    public SymmetricGaussIntegrator(Pair<double[], double[]> pointsAndWeights) throws NonMonotonicSequenceException {
        this((double[]) pointsAndWeights.getFirst(), (double[]) pointsAndWeights.getSecond());
    }

    public double integrate(UnivariateFunction f) {
        UnivariateFunction univariateFunction = f;
        int ruleLength = getNumberOfPoints();
        if (ruleLength == 1) {
            return getWeight(0) * univariateFunction.value(0.0d);
        }
        int iMax = ruleLength / 2;
        double s = 0.0d;
        double c = 0.0d;
        for (int i = 0; i < iMax; i++) {
            double p = getPoint(i);
            double y = ((univariateFunction.value(p) + univariateFunction.value(-p)) * getWeight(i)) - c;
            double t = s + y;
            c = (t - s) - y;
            s = t;
        }
        if (ruleLength % 2 != 0) {
            s += (univariateFunction.value(0.0d) * getWeight(iMax)) - c;
        }
        return s;
    }
}
