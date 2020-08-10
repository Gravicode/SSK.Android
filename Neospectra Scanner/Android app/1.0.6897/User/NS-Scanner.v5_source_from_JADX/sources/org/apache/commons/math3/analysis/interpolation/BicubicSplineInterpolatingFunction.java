package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class BicubicSplineInterpolatingFunction implements BivariateFunction {
    private static final double[][] AINV = {new double[]{1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{-3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 3.0d, 0.0d, 0.0d, -2.0d, -1.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 2.0d, -2.0d, 0.0d, 0.0d, 1.0d, 1.0d, 0.0d, 0.0d}, new double[]{-3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, -3.0d, 0.0d, 3.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, -2.0d, 0.0d, -1.0d, 0.0d}, new double[]{9.0d, -9.0d, -9.0d, 9.0d, 6.0d, 3.0d, -6.0d, -3.0d, 6.0d, -6.0d, 3.0d, -3.0d, 4.0d, 2.0d, 2.0d, 1.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -3.0d, -3.0d, 3.0d, 3.0d, -4.0d, 4.0d, -2.0d, 2.0d, -2.0d, -2.0d, -1.0d, -1.0d}, new double[]{2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d, 2.0d, 0.0d, -2.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 1.0d, 0.0d, 1.0d, 0.0d}, new double[]{-6.0d, 6.0d, 6.0d, -6.0d, -4.0d, -2.0d, 4.0d, 2.0d, -3.0d, 3.0d, -3.0d, 3.0d, -2.0d, -1.0d, -2.0d, -1.0d}, new double[]{4.0d, -4.0d, -4.0d, 4.0d, 2.0d, 2.0d, -2.0d, -2.0d, 2.0d, -2.0d, 2.0d, -2.0d, 1.0d, 1.0d, 1.0d, 1.0d}};
    private static final int NUM_COEFF = 16;
    private final BivariateFunction[][][] partialDerivatives;
    private final BicubicSplineFunction[][] splines;
    private final double[] xval;
    private final double[] yval;

    public BicubicSplineInterpolatingFunction(double[] x, double[] y, double[][] f, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY) throws DimensionMismatchException, NoDataException, NonMonotonicSequenceException {
        this(x, y, f, dFdX, dFdY, d2FdXdY, false);
    }

    public BicubicSplineInterpolatingFunction(double[] x, double[] y, double[][] f, double[][] dFdX, double[][] dFdY, double[][] d2FdXdY, boolean initializeDerivatives) throws DimensionMismatchException, NoDataException, NonMonotonicSequenceException {
        double[][] dArr = f;
        double[][] dArr2 = dFdX;
        double[][] dArr3 = dFdY;
        double[][] dArr4 = d2FdXdY;
        boolean z = initializeDerivatives;
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
            this.splines = (BicubicSplineFunction[][]) Array.newInstance(BicubicSplineFunction.class, new int[]{lastI, lastJ});
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
                    int j = 0;
                    while (true) {
                        int j2 = j;
                        if (j2 >= lastJ) {
                            break;
                        }
                        int jp1 = j2 + 1;
                        this.splines[i][j2] = new BicubicSplineFunction(computeSplineCoefficients(new double[]{dArr[i][j2], dArr[ip1][j2], dArr[i][jp1], dArr[ip1][jp1], dArr2[i][j2], dArr2[ip1][j2], dArr2[i][jp1], dArr2[ip1][jp1], dArr3[i][j2], dArr3[ip1][j2], dArr3[i][jp1], dArr3[ip1][jp1], dArr4[i][j2], dArr4[ip1][j2], dArr4[i][jp1], dArr4[ip1][jp1]}), z);
                        j = j2 + 1;
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
            if (z) {
                this.partialDerivatives = (BivariateFunction[][][]) Array.newInstance(BivariateFunction.class, new int[]{5, lastI, lastJ});
                for (int i2 = 0; i2 < lastI; i2++) {
                    for (int j3 = 0; j3 < lastJ; j3++) {
                        BicubicSplineFunction bcs = this.splines[i2][j3];
                        this.partialDerivatives[0][i2][j3] = bcs.partialDerivativeX();
                        this.partialDerivatives[1][i2][j3] = bcs.partialDerivativeY();
                        this.partialDerivatives[2][i2][j3] = bcs.partialDerivativeXX();
                        this.partialDerivatives[3][i2][j3] = bcs.partialDerivativeYY();
                        this.partialDerivatives[4][i2][j3] = bcs.partialDerivativeXY();
                    }
                }
                return;
            }
            this.partialDerivatives = null;
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

    public double partialDerivativeX(double x, double y) throws OutOfRangeException {
        return partialDerivative(0, x, y);
    }

    public double partialDerivativeY(double x, double y) throws OutOfRangeException {
        return partialDerivative(1, x, y);
    }

    public double partialDerivativeXX(double x, double y) throws OutOfRangeException {
        return partialDerivative(2, x, y);
    }

    public double partialDerivativeYY(double x, double y) throws OutOfRangeException {
        return partialDerivative(3, x, y);
    }

    public double partialDerivativeXY(double x, double y) throws OutOfRangeException {
        return partialDerivative(4, x, y);
    }

    private double partialDerivative(int which, double x, double y) throws OutOfRangeException {
        double d = x;
        double d2 = y;
        int i = searchIndex(d, this.xval);
        int j = searchIndex(d2, this.yval);
        return this.partialDerivatives[which][i][j].value((d - this.xval[i]) / (this.xval[i + 1] - this.xval[i]), (d2 - this.yval[j]) / (this.yval[j + 1] - this.yval[j]));
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
