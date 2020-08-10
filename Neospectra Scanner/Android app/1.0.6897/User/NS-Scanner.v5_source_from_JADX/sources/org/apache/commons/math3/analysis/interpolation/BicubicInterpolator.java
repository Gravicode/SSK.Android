package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

public class BicubicInterpolator implements BivariateGridInterpolator {
    public BicubicInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NoDataException, DimensionMismatchException, NonMonotonicSequenceException, NumberIsTooSmallException {
        double[] dArr = xval;
        double[] dArr2 = yval;
        double[][] dArr3 = fval;
        if (dArr.length == 0 || dArr2.length == 0 || dArr3.length == 0) {
            throw new NoDataException();
        } else if (dArr.length != dArr3.length) {
            throw new DimensionMismatchException(dArr.length, dArr3.length);
        } else {
            MathArrays.checkOrder(xval);
            MathArrays.checkOrder(yval);
            int xLen = dArr.length;
            int yLen = dArr2.length;
            double[][] dFdX = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            double[][] dFdY = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            double[][] d2FdXdY = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            for (int i = 1; i < xLen - 1; i++) {
                int nI = i + 1;
                int pI = i - 1;
                double nX = dArr[nI];
                double deltaX = nX - dArr[pI];
                int j = 1;
                while (true) {
                    double nX2 = nX;
                    int j2 = j;
                    if (j2 >= yLen - 1) {
                        break;
                    }
                    int j3 = j2 + 1;
                    int pJ = j2 - 1;
                    double deltaY = dArr2[j3] - dArr2[pJ];
                    dFdX[i][j2] = (dArr3[nI][j2] - dArr3[pI][j2]) / deltaX;
                    dFdY[i][j2] = (dArr3[i][j3] - dArr3[i][pJ]) / deltaY;
                    d2FdXdY[i][j2] = (((dArr3[nI][j3] - dArr3[nI][pJ]) - dArr3[pI][j3]) + dArr3[pI][pJ]) / (deltaX * deltaY);
                    j = j2 + 1;
                    nX = nX2;
                }
            }
            final double[] dArr4 = dArr;
            final double[] dArr5 = dArr2;
            C09051 r0 = new BicubicInterpolatingFunction(this, dArr, dArr2, dArr3, dFdX, dFdY, d2FdXdY) {
                final /* synthetic */ BicubicInterpolator this$0;

                {
                    this.this$0 = r9;
                }

                public boolean isValidPoint(double x, double y) {
                    if (x < dArr4[1] || x > dArr4[dArr4.length - 2] || y < dArr5[1] || y > dArr5[dArr5.length - 2]) {
                        return false;
                    }
                    return true;
                }
            };
            return r0;
        }
    }
}
