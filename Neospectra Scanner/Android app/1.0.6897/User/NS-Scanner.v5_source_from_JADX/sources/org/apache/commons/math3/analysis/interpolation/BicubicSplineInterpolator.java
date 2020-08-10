package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
public class BicubicSplineInterpolator implements BivariateGridInterpolator {
    private final boolean initializeDerivatives;

    public BicubicSplineInterpolator() {
        this(false);
    }

    public BicubicSplineInterpolator(boolean initializeDerivatives2) {
        this.initializeDerivatives = initializeDerivatives2;
    }

    public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NoDataException, DimensionMismatchException, NonMonotonicSequenceException, NumberIsTooSmallException {
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
            double[][] fX = (double[][]) Array.newInstance(double.class, new int[]{yLen, xLen});
            for (int i = 0; i < xLen; i++) {
                if (dArr3[i].length != yLen) {
                    throw new DimensionMismatchException(dArr3[i].length, yLen);
                }
                for (int j = 0; j < yLen; j++) {
                    fX[j][i] = dArr3[i][j];
                }
            }
            SplineInterpolator spInterpolator = new SplineInterpolator();
            PolynomialSplineFunction[] ySplineX = new PolynomialSplineFunction[yLen];
            for (int j2 = 0; j2 < yLen; j2++) {
                ySplineX[j2] = spInterpolator.interpolate(dArr, fX[j2]);
            }
            PolynomialSplineFunction[] xSplineY = new PolynomialSplineFunction[xLen];
            for (int i2 = 0; i2 < xLen; i2++) {
                xSplineY[i2] = spInterpolator.interpolate(dArr2, dArr3[i2]);
            }
            double[][] dFdX = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            for (int j3 = 0; j3 < yLen; j3++) {
                UnivariateFunction f = ySplineX[j3].derivative();
                int i3 = 0;
                while (i3 < xLen) {
                    double[][] fX2 = fX;
                    SplineInterpolator spInterpolator2 = spInterpolator;
                    dFdX[i3][j3] = f.value(dArr[i3]);
                    i3++;
                    fX = fX2;
                    spInterpolator = spInterpolator2;
                }
                SplineInterpolator splineInterpolator = spInterpolator;
            }
            SplineInterpolator splineInterpolator2 = spInterpolator;
            double[][] dFdY = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            int i4 = 0;
            while (i4 < xLen) {
                UnivariateFunction f2 = xSplineY[i4].derivative();
                int j4 = 0;
                while (j4 < yLen) {
                    int i5 = i4;
                    dFdY[i4][j4] = f2.value(dArr2[j4]);
                    j4++;
                    i4 = i5;
                }
                i4++;
            }
            double[][] d2FdXdY = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            for (int i6 = 0; i6 < xLen; i6++) {
                int nI = nextIndex(i6, xLen);
                int pI = previousIndex(i6);
                for (int j5 = 0; j5 < yLen; j5++) {
                    int nJ = nextIndex(j5, yLen);
                    int pJ = previousIndex(j5);
                    d2FdXdY[i6][j5] = (((dArr3[nI][nJ] - dArr3[nI][pJ]) - dArr3[pI][nJ]) + dArr3[pI][pJ]) / ((dArr[nI] - dArr[pI]) * (dArr2[nJ] - dArr2[pJ]));
                }
            }
            PolynomialSplineFunction[] polynomialSplineFunctionArr = xSplineY;
            PolynomialSplineFunction[] polynomialSplineFunctionArr2 = ySplineX;
            BicubicSplineInterpolatingFunction bicubicSplineInterpolatingFunction = new BicubicSplineInterpolatingFunction(dArr, dArr2, dArr3, dFdX, dFdY, d2FdXdY, this.initializeDerivatives);
            return bicubicSplineInterpolatingFunction;
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
