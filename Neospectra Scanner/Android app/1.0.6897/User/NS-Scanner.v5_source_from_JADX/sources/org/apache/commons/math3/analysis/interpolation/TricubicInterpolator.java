package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

public class TricubicInterpolator implements TrivariateGridInterpolator {
    public TricubicInterpolatingFunction interpolate(double[] xval, double[] yval, double[] zval, double[][][] fval) throws NoDataException, NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        double pX;
        double[] dArr = xval;
        double[] dArr2 = yval;
        double[] dArr3 = zval;
        double[][][] dArr4 = fval;
        if (dArr.length == 0 || dArr2.length == 0 || dArr3.length == 0 || dArr4.length == 0) {
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
            double[][][] dFdX = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] dFdY = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] dFdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d2FdXdY = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d2FdXdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d2FdYdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            double[][][] d3FdXdYdZ = (double[][][]) Array.newInstance(double.class, new int[]{xLen, yLen, zLen});
            for (int i = 1; i < xLen - 1; i++) {
                if (dArr2.length != dArr4[i].length) {
                    throw new DimensionMismatchException(dArr2.length, dArr4[i].length);
                }
                int nI = i + 1;
                int pI = i - 1;
                double nX = dArr[nI];
                double pX2 = dArr[pI];
                double deltaX = nX - pX2;
                int j = 1;
                while (j < yLen - 1) {
                    double nX2 = nX;
                    if (dArr3.length != dArr4[i][j].length) {
                        throw new DimensionMismatchException(dArr3.length, dArr4[i][j].length);
                    }
                    int nJ = j + 1;
                    int pJ = j - 1;
                    double deltaY = dArr2[nJ] - dArr2[pJ];
                    double deltaXY = deltaX * deltaY;
                    int k = 1;
                    while (true) {
                        pX = pX2;
                        if (k >= zLen - 1) {
                            break;
                        }
                        int nK = k + 1;
                        int pK = k - 1;
                        double deltaZ = dArr3[nK] - dArr3[pK];
                        dFdX[i][j][k] = (dArr4[nI][j][k] - dArr4[pI][j][k]) / deltaX;
                        dFdY[i][j][k] = (dArr4[i][nJ][k] - dArr4[i][pJ][k]) / deltaY;
                        dFdZ[i][j][k] = (dArr4[i][j][nK] - dArr4[i][j][pK]) / deltaZ;
                        double deltaXZ = deltaX * deltaZ;
                        double deltaYZ = deltaY * deltaZ;
                        d2FdXdY[i][j][k] = (((dArr4[nI][nJ][k] - dArr4[nI][pJ][k]) - dArr4[pI][nJ][k]) + dArr4[pI][pJ][k]) / deltaXY;
                        d2FdXdZ[i][j][k] = (((dArr4[nI][j][nK] - dArr4[nI][j][pK]) - dArr4[pI][j][nK]) + dArr4[pI][j][pK]) / deltaXZ;
                        d2FdYdZ[i][j][k] = (((dArr4[i][nJ][nK] - dArr4[i][nJ][pK]) - dArr4[i][pJ][nK]) + dArr4[i][pJ][pK]) / deltaYZ;
                        d3FdXdYdZ[i][j][k] = (((((((dArr4[nI][nJ][nK] - dArr4[nI][pJ][nK]) - dArr4[pI][nJ][nK]) + dArr4[pI][pJ][nK]) - dArr4[nI][nJ][pK]) + dArr4[nI][pJ][pK]) + dArr4[pI][nJ][pK]) - dArr4[pI][pJ][pK]) / (deltaXY * deltaZ);
                        k++;
                        pX2 = pX;
                    }
                    j++;
                    nX = nX2;
                    pX2 = pX;
                }
            }
            int i2 = zLen;
            int i3 = yLen;
            int i4 = xLen;
            final double[] dArr5 = dArr;
            final double[] dArr6 = yval;
            final double[] dArr7 = zval;
            C09121 r0 = new TricubicInterpolatingFunction(this, dArr, dArr2, dArr3, dArr4, dFdX, dFdY, dFdZ, d2FdXdY, d2FdXdZ, d2FdYdZ, d3FdXdYdZ) {
                final /* synthetic */ TricubicInterpolator this$0;

                {
                    this.this$0 = r14;
                }

                public boolean isValidPoint(double x, double y, double z) {
                    if (x < dArr5[1] || x > dArr5[dArr5.length - 2] || y < dArr6[1] || y > dArr6[dArr6.length - 2] || z < dArr7[1] || z > dArr7[dArr7.length - 2]) {
                        return false;
                    }
                    return true;
                }
            };
            return r0;
        }
    }
}
