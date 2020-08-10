package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class UnivariatePeriodicInterpolator implements UnivariateInterpolator {
    public static final int DEFAULT_EXTEND = 5;
    private final int extend;
    private final UnivariateInterpolator interpolator;
    /* access modifiers changed from: private */
    public final double period;

    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator2, double period2, int extend2) {
        this.interpolator = interpolator2;
        this.period = period2;
        this.extend = extend2;
    }

    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator2, double period2) {
        this(interpolator2, period2, 5);
    }

    public UnivariateFunction interpolate(double[] xval, double[] yval) throws NumberIsTooSmallException, NonMonotonicSequenceException {
        double[] dArr = xval;
        if (dArr.length < this.extend) {
            throw new NumberIsTooSmallException(Integer.valueOf(dArr.length), Integer.valueOf(this.extend), true);
        }
        MathArrays.checkOrder(xval);
        final double offset = dArr[0];
        int len = dArr.length + (this.extend * 2);
        double[] x = new double[len];
        double[] y = new double[len];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= dArr.length) {
                break;
            }
            int index = i2 + this.extend;
            int i3 = i2;
            x[index] = MathUtils.reduce(dArr[i2], this.period, offset);
            y[index] = yval[i3];
            i = i3 + 1;
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < this.extend) {
                int index2 = (dArr.length - this.extend) + i5;
                int i6 = i5;
                double d = offset;
                x[i6] = MathUtils.reduce(dArr[index2], this.period, d) - this.period;
                y[i6] = yval[index2];
                int index3 = (len - this.extend) + i6;
                x[index3] = MathUtils.reduce(dArr[i6], this.period, d) + this.period;
                y[index3] = yval[i6];
                i4 = i6 + 1;
            } else {
                MathArrays.sortInPlace(x, y);
                final UnivariateFunction f = this.interpolator.interpolate(x, y);
                return new UnivariateFunction() {
                    public double value(double x) throws MathIllegalArgumentException {
                        return f.value(MathUtils.reduce(x, UnivariatePeriodicInterpolator.this.period, offset));
                    }
                };
            }
        }
    }
}
