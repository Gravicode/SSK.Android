package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class TricubicSplineInterpolator implements TrivariateGridInterpolator {
    public TricubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[] zval, double[][][] fval) throws NoDataException, NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        BicubicSplineInterpolatingFunction[] zSplineXY;
        double[] dArr = xval;
        double[] dArr2 = yval;
        double[] dArr3 = zval;
        double[][][] dArr4 = fval;
        if (dArr.length == 0 || dArr2.length == 0 || dArr3.length == 0 || dArr4.length == 0) {
            double[] dArr5 = dArr;
            throw new NoDataException();
        } else if (dArr.length != dArr4.length) {
            throw new DimensionMismatchException(dArr.length, dArr4.length);
        } else {
            MathArrays.checkOrder(xval);
            MathArrays.checkOrder(yval);
            MathArrays.checkOrder(zval);
            int xLen = dArr.length;
            int yLen = dArr2.length;
            int zLen = dArr3.length;
            double[][][] fvalXY = (double[][][]) Array.newInstance(double.class, new int[]{zLen, xLen, yLen});
            double[][][] fvalZX = (double[][][]) Array.newInstance(double.class, new int[]{yLen, zLen, xLen});
            for (int i = 0; i < xLen; i++) {
                if (dArr4[i].length != yLen) {
                    throw new DimensionMismatchException(dArr4[i].length, yLen);
                }
                for (int j = 0; j < yLen; j++) {
                    if (dArr4[i][j].length != zLen) {
                        throw new DimensionMismatchException(dArr4[i][j].length, zLen);
                    }
                    for (int k = 0; k < zLen; k++) {
                        double v = dArr4[i][j][k];
                        fvalXY[k][i][j] = v;
                        fvalZX[j][k][i] = v;
                    }
                }
            }
            BicubicSplineInterpolator bsi = new BicubicSplineInterpolator(true);
            BicubicSplineInterpolatingFunction[] xSplineYZ = new BicubicSplineInterpolatingFunction[xLen];
            for (int i2 = 0; i2 < xLen; i2++) {
                xSplineYZ[i2] = bsi.interpolate(dArr2, dArr3, dArr4[i2]);
            }
            BicubicSplineInterpolatingFunction[] ySplineZX = new BicubicSplineInterpolatingFunction[yLen];
            for (int j2 = 0; j2 < yLen; j2++) {
                ySplineZX[j2] = bsi.interpolate(dArr3, dArr, fvalZX[j2]);
            }
            BicubicSplineInterpolatingFunction[] zSplineXY2 = new BicubicSplineInterpolatingFunction[zLen];
            for (int k2 = 0; k2 < zLen; k2++) {
                zSplineXY2[k2] = bsi.interpolate(dArr, dArr2, fvalXY[k2]);
            }
            double[][][] dFdX = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] dFdY = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d2FdXdY = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            int k3 = 0;
            while (k3 < zLen) {
                BicubicSplineInterpolatingFunction f = zSplineXY2[k3];
                int i3 = 0;
                while (i3 < xLen) {
                    int k4 = k3;
                    double x = dArr[i3];
                    int j3 = 0;
                    while (true) {
                        zSplineXY = zSplineXY2;
                        int j4 = j3;
                        if (j4 >= yLen) {
                            break;
                        }
                        double y = dArr2[j4];
                        dFdX[i3][j4][k4] = f.partialDerivativeX(x, y);
                        dFdY[i3][j4][k4] = f.partialDerivativeY(x, y);
                        d2FdXdY[i3][j4][k4] = f.partialDerivativeXY(x, y);
                        j3 = j4 + 1;
                        zSplineXY2 = zSplineXY;
                        double[][][] dArr6 = fval;
                        double[] dArr7 = xval;
                    }
                    i3++;
                    k3 = k4;
                    zSplineXY2 = zSplineXY;
                    double[][][] dArr8 = fval;
                    dArr = xval;
                }
                BicubicSplineInterpolatingFunction[] bicubicSplineInterpolatingFunctionArr = zSplineXY2;
                k3++;
                double[][][] dArr9 = fval;
                dArr = xval;
            }
            double[][][] dFdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d2FdYdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            for (int i4 = 0; i4 < xLen; i4++) {
                BicubicSplineInterpolatingFunction f2 = xSplineYZ[i4];
                for (int j5 = 0; j5 < yLen; j5++) {
                    double y2 = dArr2[j5];
                    int k5 = 0;
                    while (k5 < zLen) {
                        BicubicSplineInterpolatingFunction[] xSplineYZ2 = xSplineYZ;
                        BicubicSplineInterpolator bsi2 = bsi;
                        double z = dArr3[k5];
                        dFdZ[i4][j5][k5] = f2.partialDerivativeY(y2, z);
                        d2FdYdZ[i4][j5][k5] = f2.partialDerivativeXY(y2, z);
                        k5++;
                        bsi = bsi2;
                        xSplineYZ = xSplineYZ2;
                    }
                    BicubicSplineInterpolator bicubicSplineInterpolator = bsi;
                }
                BicubicSplineInterpolator bicubicSplineInterpolator2 = bsi;
            }
            BicubicSplineInterpolatingFunction[] xSplineYZ3 = xSplineYZ;
            BicubicSplineInterpolator bsi3 = bsi;
            double[][][] d2FdZdX = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            for (int j6 = 0; j6 < yLen; j6++) {
                BicubicSplineInterpolatingFunction f3 = ySplineZX[j6];
                for (int k6 = 0; k6 < zLen; k6++) {
                    double z2 = dArr3[k6];
                    int i5 = 0;
                    while (i5 < xLen) {
                        double[][][] dFdZ2 = dFdZ;
                        d2FdZdX[i5][j6][k6] = f3.partialDerivativeXY(z2, xval[i5]);
                        i5++;
                        dFdZ = dFdZ2;
                    }
                    double[] dArr10 = xval;
                }
                double[] dArr11 = xval;
            }
            double[][][] dFdZ3 = dFdZ;
            double[] dArr12 = xval;
            double[][][] d3FdXdYdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            for (int i6 = 0; i6 < xLen; i6++) {
                int nI = nextIndex(i6, xLen);
                int pI = previousIndex(i6);
                for (int j7 = 0; j7 < yLen; j7++) {
                    int nJ = nextIndex(j7, yLen);
                    int pJ = previousIndex(j7);
                    for (int k7 = 0; k7 < zLen; k7++) {
                        int nK = nextIndex(k7, zLen);
                        int pK = previousIndex(k7);
                        double[][][] dArr13 = fval;
                        d3FdXdYdZ[i6][j7][k7] = (((((((dArr13[nI][nJ][nK] - dArr13[nI][pJ][nK]) - dArr13[pI][nJ][nK]) + dArr13[pI][pJ][nK]) - dArr13[nI][nJ][pK]) + dArr13[nI][pJ][pK]) + dArr13[pI][nJ][pK]) - dArr13[pI][pJ][pK]) / (((dArr12[nI] - dArr12[pI]) * (dArr2[nJ] - dArr2[pJ])) * (dArr3[nK] - dArr3[pK]));
                    }
                    double[][][] dArr14 = fval;
                }
                double[][][] dArr15 = fval;
            }
            BicubicSplineInterpolatingFunction[] bicubicSplineInterpolatingFunctionArr2 = ySplineZX;
            BicubicSplineInterpolatingFunction[] bicubicSplineInterpolatingFunctionArr3 = xSplineYZ3;
            BicubicSplineInterpolator bicubicSplineInterpolator3 = bsi3;
            int i7 = zLen;
            int i8 = yLen;
            int i9 = xLen;
            TricubicSplineInterpolatingFunction tricubicSplineInterpolatingFunction = new TricubicSplineInterpolatingFunction(dArr12, dArr2, dArr3, fval, dFdX, dFdY, dFdZ3, d2FdXdY, d2FdZdX, d2FdYdZ, d3FdXdYdZ);
            return tricubicSplineInterpolatingFunction;
        }
    }

    private int nextIndex(int i, int max) {
        int index = i + 1;
        return index < max ? index : index - 1;
    }

    private int previousIndex(int i) {
        int index = i - 1;
        if (index >= 0) {
            return index;
        }
        return 0;
    }
}
