package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

public class BicubicInterpolatingFunction implements BivariateFunction {
    private static final double[][] AINV = {new double[]{1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{-3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d}, new double[]{-3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d}, new double[]{9.0d, -9.0d, -9.0d, 9.0d, 6.0d, 3.0d, -6.0d, -3.0d, 6.0d, -6.0d, 3.0d, -3.0d, 4.0d, 2.0d, 2.0d, 1.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -3.0d, -3.0d, 3.0d, 3.0d, -4.0d, 4.0d, -2.0d, 2.0d, -2.0d, -2.0d, -1.0d, -1.0d}, new double[]{2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -4.0d, -2.0d, 4.0d, 2.0d, -3.0d, 3.0d, -3.0d, 3.0d, -2.0d, -1.0d, -2.0d, -1.0d}, new double[]{4.0d, -4.0d, -4.0d, 4.0d, 2.0d, 2.0d, -2.0d, -2.0d, 2.0d, -2.0d, 2.0d, -2.0d, 1.0d, 1.0d, 1.0d, 1.0d}};
    private static final int NUM_COEFF = 16;
    private final BicubicFunction[][] splines;
    private final double[] xval;
    private final double[] yval;

    public BicubicInterpolatingFunction(double[] x, double[] y, double[][] f, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY) throws DimensionMismatchException, NoDataException, NonMonotonicSequenceException {
        double[][] dArr = f;
        double[][] dArr2 = dFdX;
        double[][] dArr3 = dFdY;
        double[][] dArr4 = d2FdXdY;
        int xLen = x.length;
        int yLen = y.length;
        if (xLen == 0 || yLen == 0 || dArr.length == 0 || dArr[0].length == 0) {
            throw new NoDataException();
        } else if (xLen != dArr.length) {
            throw new DimensionMismatchException(xLen, dArr.length);
        } else if (xLen != dArr2.length) {
            throw new DimensionMismatchException(xLen, dArr2.length);
        } else if (xLen != dArr3.length) {
            throw new DimensionMismatchException(xLen, dArr3.length);
        } else if (xLen != dArr4.length) {
            throw new DimensionMismatchException(xLen, dArr4.length);
        } else {
            MathArrays.checkOrder(x);
            MathArrays.checkOrder(y);
            this.xval = (double[]) x.clone();
            this.yval = (double[]) y.clone();
            int lastI = xLen - 1;
            int lastJ = yLen - 1;
            this.splines = (BicubicFunction[][]) Array.newInstance(BicubicFunction.class, new int[]{lastI, lastJ});
            int i = 0;
            while (i < lastI) {
                if (dArr[i].length != yLen) {
                    throw new DimensionMismatchException(dArr[i].length, yLen);
                } else if (dArr2[i].length != yLen) {
                    throw new DimensionMismatchException(dArr2[i].length, yLen);
                } else if (dArr3[i].length != yLen) {
                    throw new DimensionMismatchException(dArr3[i].length, yLen);
                } else if (dArr4[i].length != yLen) {
                    throw new DimensionMismatchException(dArr4[i].length, yLen);
                } else {
                    int ip1 = i + 1;
                    double xR = this.xval[ip1] - this.xval[i];
                    int j = 0;
                    while (j < lastJ) {
                        int jp1 = j + 1;
                        double yR = this.yval[jp1] - this.yval[j];
                        double xRyR = xR * yR;
                        this.splines[i][j] = new BicubicFunction(computeSplineCoefficients(new double[]{dArr[i][j], dArr[ip1][j], dArr[i][jp1], dArr[ip1][jp1], dArr2[i][j] * xR, dArr2[ip1][j] * xR, dArr2[i][jp1] * xR, dArr2[ip1][jp1] * xR, dArr3[i][j] * yR, dArr3[ip1][j] * yR, dArr3[i][jp1] * yR, dArr3[ip1][jp1] * yR, dArr4[i][j] * xRyR, dArr4[ip1][j] * xRyR, dArr4[i][jp1] * xRyR, dArr4[ip1][jp1] * xRyR}));
                        j++;
                        dArr = f;
                        dArr2 = dFdX;
                        dArr3 = dFdY;
                    }
                    i++;
                    dArr = f;
                    dArr2 = dFdX;
                    dArr3 = dFdY;
                }
            }
        }
    }

    public double value(double x, double y) throws OutOfRangeException {
        int i = searchIndex(x, this.xval);
        int j = searchIndex(y, this.yval);
        return this.splines[i][j].value((x - this.xval[i]) / (this.xval[i + 1] - this.xval[i]), (y - this.yval[j]) / (this.yval[j + 1] - this.yval[j]));
    }

    public boolean isValidPoint(double x, double y) {
        return x >= this.xval[0] && x <= this.xval[this.xval.length - 1] && y >= this.yval[0] && y <= this.yval[this.yval.length - 1];
    }

    private int searchIndex(double c, double[] val) {
        int r = Arrays.binarySearch(val, c);
        if (r == -1 || r == (-val.length) - 1) {
            throw new OutOfRangeException(Double.valueOf(c), Double.valueOf(val[0]), Double.valueOf(val[val.length - 1]));
        } else if (r < 0) {
            return (-r) - 2;
        } else {
            int last = val.length - 1;
            if (r == last) {
                return last - 1;
            }
            return r;
        }
    }

    private double[] computeSplineCoefficients(double[] beta) {
        double[] a = new double[16];
        for (int i = 0; i < 16; i++) {
            double[] row = AINV[i];
            double result = 0.0d;
            for (int j = 0; j < 16; j++) {
                result += row[j] * beta[j];
            }
            a[i] = result;
        }
        return a;
    }
}
