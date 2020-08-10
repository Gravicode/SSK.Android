package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

/* compiled from: BicubicSplineInterpolatingFunction */
class BicubicSplineFunction implements BivariateFunction {

    /* renamed from: N */
    private static final short f520N = 4;

    /* renamed from: a */
    private final double[][] f521a;
    private final BivariateFunction partialDerivativeX;
    private final BivariateFunction partialDerivativeXX;
    private final BivariateFunction partialDerivativeXY;
    private final BivariateFunction partialDerivativeY;
    private final BivariateFunction partialDerivativeYY;

    BicubicSplineFunction(double[] coeff) {
        this(coeff, false);
    }

    BicubicSplineFunction(double[] coeff, boolean initializeDerivatives) {
        int i = 4;
        this.f521a = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
        for (int i2 = 0; i2 < 4; i2++) {
            for (int j = 0; j < 4; j++) {
                this.f521a[i2][j] = coeff[(i2 * 4) + j];
            }
        }
        if (initializeDerivatives) {
            final double[][] aX = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
            final double[][] aY = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
            final double[][] aXX = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
            final double[][] aYY = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
            final double[][] aXY = (double[][]) Array.newInstance(double.class, new int[]{4, 4});
            int i3 = 0;
            while (i3 < i) {
                int j2 = 0;
                while (j2 < i) {
                    double c = this.f521a[i3][j2];
                    aX[i3][j2] = ((double) i3) * c;
                    aY[i3][j2] = ((double) j2) * c;
                    aXX[i3][j2] = ((double) (i3 - 1)) * aX[i3][j2];
                    aYY[i3][j2] = ((double) (j2 - 1)) * aY[i3][j2];
                    aXY[i3][j2] = ((double) j2) * aX[i3][j2];
                    j2++;
                    i = 4;
                }
                i3++;
                i = 4;
            }
            this.partialDerivativeX = new BivariateFunction() {
                public double value(double x, double y) {
                    double y2 = y * y;
                    return BicubicSplineFunction.this.apply(new double[]{0.0d, 1.0d, x, x * x}, new double[]{1.0d, y, y2, y2 * y}, aX);
                }
            };
            this.partialDerivativeY = new BivariateFunction() {
                public double value(double x, double y) {
                    double x2 = x * x;
                    return BicubicSplineFunction.this.apply(new double[]{1.0d, x, x2, x2 * x}, new double[]{0.0d, 1.0d, y, y * y}, aY);
                }
            };
            this.partialDerivativeXX = new BivariateFunction() {
                public double value(double x, double y) {
                    double y2 = y * y;
                    return BicubicSplineFunction.this.apply(new double[]{0.0d, 0.0d, 1.0d, x}, new double[]{1.0d, y, y2, y2 * y}, aXX);
                }
            };
            this.partialDerivativeYY = new BivariateFunction() {
                public double value(double x, double y) {
                    double x2 = x * x;
                    return BicubicSplineFunction.this.apply(new double[]{1.0d, x, x2, x2 * x}, new double[]{0.0d, 0.0d, 1.0d, y}, aYY);
                }
            };
            this.partialDerivativeXY = new BivariateFunction() {
                public double value(double x, double y) {
                    return BicubicSplineFunction.this.apply(new double[]{0.0d, 1.0d, x, x * x}, new double[]{0.0d, 1.0d, y, y * y}, aXY);
                }
            };
            return;
        }
        this.partialDerivativeX = null;
        this.partialDerivativeY = null;
        this.partialDerivativeXX = null;
        this.partialDerivativeYY = null;
        this.partialDerivativeXY = null;
    }

    public double value(double x, double y) {
        if (x < 0.0d || x > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(x), Integer.valueOf(0), Integer.valueOf(1));
        } else if (y < 0.0d || y > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(y), Integer.valueOf(0), Integer.valueOf(1));
        } else {
            double x2 = x * x;
            double y2 = y * y;
            return apply(new double[]{1.0d, x, x2, x2 * x}, new double[]{1.0d, y, y2, y2 * y}, this.f521a);
        }
    }

    /* access modifiers changed from: private */
    public double apply(double[] pX, double[] pY, double[][] coeff) {
        double result = 0.0d;
        int i = 0;
        while (i < 4) {
            double result2 = result;
            for (int j = 0; j < 4; j++) {
                result2 += coeff[i][j] * pX[i] * pY[j];
            }
            i++;
            result = result2;
        }
        return result;
    }

    public BivariateFunction partialDerivativeX() {
        return this.partialDerivativeX;
    }

    public BivariateFunction partialDerivativeY() {
        return this.partialDerivativeY;
    }

    public BivariateFunction partialDerivativeXX() {
        return this.partialDerivativeXX;
    }

    public BivariateFunction partialDerivativeYY() {
        return this.partialDerivativeYY;
    }

    public BivariateFunction partialDerivativeXY() {
        return this.partialDerivativeXY;
    }
}
