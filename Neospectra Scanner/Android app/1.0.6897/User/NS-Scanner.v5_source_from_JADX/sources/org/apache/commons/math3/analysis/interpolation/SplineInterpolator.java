package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

public class SplineInterpolator implements UnivariateInterpolator {
    public PolynomialSplineFunction interpolate(double[] x, double[] y) throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException {
        double[] dArr = x;
        double[] dArr2 = y;
        if (dArr.length != dArr2.length) {
            throw new DimensionMismatchException(dArr.length, dArr2.length);
        } else if (dArr.length < 3) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_OF_POINTS, Integer.valueOf(dArr.length), Integer.valueOf(3), true);
        } else {
            int n = dArr.length - 1;
            MathArrays.checkOrder(x);
            double[] h = new double[n];
            for (int i = 0; i < n; i++) {
                h[i] = dArr[i + 1] - dArr[i];
            }
            double[] mu = new double[n];
            double[] z = new double[(n + 1)];
            mu[0] = 0.0d;
            z[0] = 0.0d;
            for (int i2 = 1; i2 < n; i2++) {
                double g = ((dArr[i2 + 1] - dArr[i2 - 1]) * 2.0d) - (h[i2 - 1] * mu[i2 - 1]);
                mu[i2] = h[i2] / g;
                z[i2] = ((((((dArr2[i2 + 1] * h[i2 - 1]) - (dArr2[i2] * (dArr[i2 + 1] - dArr[i2 - 1]))) + (dArr2[i2 - 1] * h[i2])) * 3.0d) / (h[i2 - 1] * h[i2])) - (h[i2 - 1] * z[i2 - 1])) / g;
            }
            double[] b = new double[n];
            double[] c = new double[(n + 1)];
            double[] d = new double[n];
            z[n] = 0.0d;
            c[n] = 0.0d;
            for (int j = n - 1; j >= 0; j--) {
                c[j] = z[j] - (mu[j] * c[j + 1]);
                b[j] = ((dArr2[j + 1] - dArr2[j]) / h[j]) - ((h[j] * (c[j + 1] + (c[j] * 2.0d))) / 3.0d);
                d[j] = (c[j + 1] - c[j]) / (h[j] * 3.0d);
            }
            PolynomialFunction[] polynomials = new PolynomialFunction[n];
            double[] coefficients = new double[4];
            for (int i3 = 0; i3 < n; i3++) {
                coefficients[0] = dArr2[i3];
                coefficients[1] = b[i3];
                coefficients[2] = c[i3];
                coefficients[3] = d[i3];
                polynomials[i3] = new PolynomialFunction(coefficients);
            }
            return new PolynomialSplineFunction(dArr, polynomials);
        }
    }
}
