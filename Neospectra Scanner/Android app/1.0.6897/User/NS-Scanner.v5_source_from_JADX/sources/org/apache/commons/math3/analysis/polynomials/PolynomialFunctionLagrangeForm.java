package org.apache.commons.math3.analysis.polynomials;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathArrays.OrderDirection;

public class PolynomialFunctionLagrangeForm implements UnivariateFunction {
    private double[] coefficients;
    private boolean coefficientsComputed = false;

    /* renamed from: x */
    private final double[] f526x;

    /* renamed from: y */
    private final double[] f527y;

    public PolynomialFunctionLagrangeForm(double[] x, double[] y) throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException {
        this.f526x = new double[x.length];
        this.f527y = new double[y.length];
        System.arraycopy(x, 0, this.f526x, 0, x.length);
        System.arraycopy(y, 0, this.f527y, 0, y.length);
        if (!verifyInterpolationArray(x, y, false)) {
            MathArrays.sortInPlace(this.f526x, this.f527y);
            verifyInterpolationArray(this.f526x, this.f527y, true);
        }
    }

    public double value(double z) {
        return evaluateInternal(this.f526x, this.f527y, z);
    }

    public int degree() {
        return this.f526x.length - 1;
    }

    public double[] getInterpolatingPoints() {
        double[] out = new double[this.f526x.length];
        System.arraycopy(this.f526x, 0, out, 0, this.f526x.length);
        return out;
    }

    public double[] getInterpolatingValues() {
        double[] out = new double[this.f527y.length];
        System.arraycopy(this.f527y, 0, out, 0, this.f527y.length);
        return out;
    }

    public double[] getCoefficients() {
        if (!this.coefficientsComputed) {
            computeCoefficients();
        }
        double[] out = new double[this.coefficients.length];
        System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
        return out;
    }

    public static double evaluate(double[] x, double[] y, double z) throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException {
        if (verifyInterpolationArray(x, y, false)) {
            return evaluateInternal(x, y, z);
        }
        double[] xNew = new double[x.length];
        double[] yNew = new double[y.length];
        System.arraycopy(x, 0, xNew, 0, x.length);
        System.arraycopy(y, 0, yNew, 0, y.length);
        MathArrays.sortInPlace(xNew, yNew);
        verifyInterpolationArray(xNew, yNew, true);
        return evaluateInternal(xNew, yNew, z);
    }

    private static double evaluateInternal(double[] x, double[] y, double z) {
        double d;
        double[] dArr = x;
        int n = dArr.length;
        double[] c = new double[n];
        double[] d2 = new double[n];
        double min_dist = Double.POSITIVE_INFINITY;
        int nearest = 0;
        for (int i = 0; i < n; i++) {
            c[i] = y[i];
            d2[i] = y[i];
            double dist = FastMath.abs(z - dArr[i]);
            if (dist < min_dist) {
                nearest = i;
                min_dist = dist;
            }
        }
        double value = y[nearest];
        int i2 = 1;
        int nearest2 = nearest;
        int i3 = 1;
        while (i3 < n) {
            for (int j = 0; j < n - i3; j++) {
                double td = dArr[i3 + j] - z;
                double w = (c[j + 1] - d2[j]) / (dArr[j] - dArr[i3 + j]);
                c[j] = (dArr[j] - z) * w;
                d2[j] = td * w;
            }
            int n2 = n;
            if (((double) nearest2) < ((double) ((n - i3) + i2)) * 0.5d) {
                d = c[nearest2];
            } else {
                nearest2--;
                d = d2[nearest2];
            }
            value += d;
            i3++;
            n = n2;
            dArr = x;
            i2 = 1;
        }
        return value;
    }

    /* access modifiers changed from: protected */
    public void computeCoefficients() {
        int n = degree() + 1;
        this.coefficients = new double[n];
        for (int i = 0; i < n; i++) {
            this.coefficients[i] = 0.0d;
        }
        double[] c = new double[(n + 1)];
        c[0] = 1.0d;
        for (int i2 = 0; i2 < n; i2++) {
            for (int j = i2; j > 0; j--) {
                c[j] = c[j - 1] - (c[j] * this.f526x[i2]);
            }
            c[0] = c[0] * (-this.f526x[i2]);
            c[i2 + 1] = 1.0d;
        }
        double[] tc = new double[n];
        for (int i3 = 0; i3 < n; i3++) {
            double d = 1.0d;
            for (int j2 = 0; j2 < n; j2++) {
                if (i3 != j2) {
                    d *= this.f526x[i3] - this.f526x[j2];
                }
            }
            double t = this.f527y[i3] / d;
            tc[n - 1] = c[n];
            double[] dArr = this.coefficients;
            int i4 = n - 1;
            dArr[i4] = dArr[i4] + (tc[n - 1] * t);
            for (int j3 = n - 2; j3 >= 0; j3--) {
                tc[j3] = c[j3 + 1] + (tc[j3 + 1] * this.f526x[i3]);
                double[] dArr2 = this.coefficients;
                dArr2[j3] = dArr2[j3] + (tc[j3] * t);
            }
        }
        this.coefficientsComputed = true;
    }

    public static boolean verifyInterpolationArray(double[] x, double[] y, boolean abort) throws DimensionMismatchException, NumberIsTooSmallException, NonMonotonicSequenceException {
        if (x.length != y.length) {
            throw new DimensionMismatchException(x.length, y.length);
        } else if (x.length >= 2) {
            return MathArrays.checkOrder(x, OrderDirection.INCREASING, true, abort);
        } else {
            throw new NumberIsTooSmallException(LocalizedFormats.WRONG_NUMBER_OF_POINTS, Integer.valueOf(2), Integer.valueOf(x.length), true);
        }
    }
}
