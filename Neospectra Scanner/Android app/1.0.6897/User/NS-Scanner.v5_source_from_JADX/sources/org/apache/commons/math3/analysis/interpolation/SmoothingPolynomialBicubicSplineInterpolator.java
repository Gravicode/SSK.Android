package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fitting.PolynomialFitter;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.GaussNewtonOptimizer;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

@Deprecated
public class SmoothingPolynomialBicubicSplineInterpolator extends BicubicSplineInterpolator {
    private final int xDegree;
    private final PolynomialFitter xFitter;
    private final int yDegree;
    private final PolynomialFitter yFitter;

    public SmoothingPolynomialBicubicSplineInterpolator() {
        this(3);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int degree) throws NotPositiveException {
        this(degree, degree);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int xDegree2, int yDegree2) throws NotPositiveException {
        if (xDegree2 < 0) {
            throw new NotPositiveException(Integer.valueOf(xDegree2));
        } else if (yDegree2 < 0) {
            throw new NotPositiveException(Integer.valueOf(yDegree2));
        } else {
            this.xDegree = xDegree2;
            this.yDegree = yDegree2;
            SimpleVectorValueChecker checker = new SimpleVectorValueChecker(Precision.EPSILON * 100.0d, Precision.SAFE_MIN * 100.0d);
            this.xFitter = new PolynomialFitter(new GaussNewtonOptimizer(false, checker));
            this.yFitter = new PolynomialFitter(new GaussNewtonOptimizer(false, checker));
        }
    }

    public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NoDataException, NullArgumentException, DimensionMismatchException, NonMonotonicSequenceException {
        double[] dArr = xval;
        double[] dArr2 = yval;
        double[][] dArr3 = fval;
        if (dArr.length == 0 || dArr2.length == 0 || dArr3.length == 0) {
            throw new NoDataException();
        } else if (dArr.length != dArr3.length) {
            throw new DimensionMismatchException(dArr.length, dArr3.length);
        } else {
            int xLen = dArr.length;
            int yLen = dArr2.length;
            for (int i = 0; i < xLen; i++) {
                if (dArr3[i].length != yLen) {
                    throw new DimensionMismatchException(dArr3[i].length, yLen);
                }
            }
            MathArrays.checkOrder(xval);
            MathArrays.checkOrder(yval);
            PolynomialFunction[] yPolyX = new PolynomialFunction[yLen];
            for (int j = 0; j < yLen; j++) {
                this.xFitter.clearObservations();
                for (int i2 = 0; i2 < xLen; i2++) {
                    this.xFitter.addObservedPoint(1.0d, dArr[i2], dArr3[i2][j]);
                }
                yPolyX[j] = new PolynomialFunction(this.xFitter.fit(new double[(this.xDegree + 1)]));
            }
            double[][] fval_1 = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            for (int j2 = 0; j2 < yLen; j2++) {
                PolynomialFunction f = yPolyX[j2];
                for (int i3 = 0; i3 < xLen; i3++) {
                    fval_1[i3][j2] = f.value(dArr[i3]);
                }
            }
            PolynomialFunction[] xPolyY = new PolynomialFunction[xLen];
            for (int i4 = 0; i4 < xLen; i4++) {
                this.yFitter.clearObservations();
                for (int j3 = 0; j3 < yLen; j3++) {
                    this.yFitter.addObservedPoint(1.0d, dArr2[j3], fval_1[i4][j3]);
                }
                xPolyY[i4] = new PolynomialFunction(this.yFitter.fit(new double[(this.yDegree + 1)]));
            }
            double[][] fval_2 = (double[][]) Array.newInstance(double.class, new int[]{xLen, yLen});
            for (int i5 = 0; i5 < xLen; i5++) {
                PolynomialFunction f2 = xPolyY[i5];
                int j4 = 0;
                while (j4 < yLen) {
                    PolynomialFunction[] yPolyX2 = yPolyX;
                    fval_2[i5][j4] = f2.value(dArr2[j4]);
                    j4++;
                    yPolyX = yPolyX2;
                }
            }
            return super.interpolate(dArr, dArr2, fval_2);
        }
    }
}
