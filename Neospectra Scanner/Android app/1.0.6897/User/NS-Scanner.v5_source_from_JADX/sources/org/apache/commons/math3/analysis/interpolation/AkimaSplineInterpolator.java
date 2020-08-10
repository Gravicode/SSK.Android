package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

public class AkimaSplineInterpolator implements UnivariateInterpolator {
    private static final int MINIMUM_NUMBER_POINTS = 5;

    public PolynomialSplineFunction interpolate(double[] xvals, double[] yvals) throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException {
        double[] dArr = xvals;
        double[] dArr2 = yvals;
        if (dArr == null || dArr2 == null) {
            throw new NullArgumentException();
        } else if (dArr.length != dArr2.length) {
            throw new DimensionMismatchException(dArr.length, dArr2.length);
        } else if (dArr.length < 5) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(dArr.length), Integer.valueOf(5), true);
        } else {
            MathArrays.checkOrder(xvals);
            int numberOfDiffAndWeightElements = dArr.length - 1;
            double[] differences = new double[numberOfDiffAndWeightElements];
            double[] weights = new double[numberOfDiffAndWeightElements];
            for (int i = 0; i < differences.length; i++) {
                differences[i] = (dArr2[i + 1] - dArr2[i]) / (dArr[i + 1] - dArr[i]);
            }
            for (int i2 = 1; i2 < weights.length; i2++) {
                weights[i2] = FastMath.abs(differences[i2] - differences[i2 - 1]);
            }
            double[] firstDerivatives = new double[dArr.length];
            for (int i3 = 2; i3 < firstDerivatives.length - 2; i3++) {
                double wP = weights[i3 + 1];
                double wM = weights[i3 - 1];
                if (!Precision.equals(wP, 0.0d) || !Precision.equals(wM, 0.0d)) {
                    firstDerivatives[i3] = ((differences[i3 - 1] * wP) + (differences[i3] * wM)) / (wP + wM);
                } else {
                    double xv = dArr[i3];
                    double xvP = dArr[i3 + 1];
                    double xvM = dArr[i3 - 1];
                    firstDerivatives[i3] = (((xvP - xv) * differences[i3 - 1]) + ((xv - xvM) * differences[i3])) / (xvP - xvM);
                }
            }
            double[] dArr3 = dArr2;
            firstDerivatives[0] = differentiateThreePoint(dArr, dArr3, 0, 0, 1, 2);
            firstDerivatives[1] = differentiateThreePoint(dArr, dArr3, 1, 0, 1, 2);
            firstDerivatives[dArr.length - 2] = differentiateThreePoint(dArr, dArr3, dArr.length - 2, dArr.length - 3, dArr.length - 2, dArr.length - 1);
            firstDerivatives[dArr.length - 1] = differentiateThreePoint(dArr, dArr3, dArr.length - 1, dArr.length - 3, dArr.length - 2, dArr.length - 1);
            return interpolateHermiteSorted(dArr, dArr2, firstDerivatives);
        }
    }

    private double differentiateThreePoint(double[] xvals, double[] yvals, int indexOfDifferentiation, int indexOfFirstSample, int indexOfSecondsample, int indexOfThirdSample) {
        double x0 = yvals[indexOfFirstSample];
        double x1 = yvals[indexOfSecondsample];
        double t1 = xvals[indexOfSecondsample] - xvals[indexOfFirstSample];
        double t2 = xvals[indexOfThirdSample] - xvals[indexOfFirstSample];
        double a = ((yvals[indexOfThirdSample] - x0) - ((t2 / t1) * (x1 - x0))) / ((t2 * t2) - (t1 * t2));
        return (2.0d * a * (xvals[indexOfDifferentiation] - xvals[indexOfFirstSample])) + (((x1 - x0) - ((a * t1) * t1)) / t1);
    }

    private PolynomialSplineFunction interpolateHermiteSorted(double[] xvals, double[] yvals, double[] firstDerivatives) {
        double[] dArr = xvals;
        double[] dArr2 = yvals;
        double[] dArr3 = firstDerivatives;
        if (dArr.length != dArr2.length) {
            throw new DimensionMismatchException(dArr.length, dArr2.length);
        } else if (dArr.length != dArr3.length) {
            throw new DimensionMismatchException(dArr.length, dArr3.length);
        } else {
            char c = 2;
            if (dArr.length < 2) {
                throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(dArr.length), Integer.valueOf(2), true);
            }
            PolynomialFunction[] polynomials = new PolynomialFunction[(dArr.length - 1)];
            double[] coefficients = new double[4];
            int i = 0;
            while (i < polynomials.length) {
                double w = dArr[i + 1] - dArr[i];
                double w2 = w * w;
                double yv = dArr2[i];
                double yvP = dArr2[i + 1];
                double fd = dArr3[i];
                double fdP = dArr3[i + 1];
                coefficients[0] = yv;
                coefficients[1] = dArr3[i];
                coefficients[c] = (((((yvP - yv) * 3.0d) / w) - (fd * 2.0d)) - fdP) / w;
                coefficients[3] = (((((yv - yvP) * 2.0d) / w) + fd) + fdP) / w2;
                polynomials[i] = new PolynomialFunction(coefficients);
                i++;
                c = 2;
            }
            return new PolynomialSplineFunction(dArr, polynomials);
        }
    }
}
