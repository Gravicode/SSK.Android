package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class Sinc implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private static final double SHORTCUT = 0.006d;
    private final boolean normalized;

    public Sinc() {
        this(false);
    }

    public Sinc(boolean normalized2) {
        this.normalized = normalized2;
    }

    public double value(double x) {
        double scaledX = this.normalized ? 3.141592653589793d * x : x;
        if (FastMath.abs(scaledX) > SHORTCUT) {
            return FastMath.sin(scaledX) / scaledX;
        }
        double scaledX2 = scaledX * scaledX;
        return (((scaledX2 - 20.0d) * scaledX2) + 120.0d) / 120.0d;
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    public DerivativeStructure value(DerivativeStructure t) throws DimensionMismatchException {
        double[] f;
        double[] f2;
        int kStart;
        int i;
        double d = 1.0d;
        double scaledX = (this.normalized ? 3.141592653589793d : 1.0d) * t.getValue();
        double scaledX2 = scaledX * scaledX;
        double[] f3 = new double[(t.getOrder() + 1)];
        int k = 0;
        if (FastMath.abs(scaledX) <= SHORTCUT) {
            while (true) {
                int i2 = k;
                if (i2 >= f3.length) {
                    break;
                }
                int k2 = i2 / 2;
                if ((i2 & 1) == 0) {
                    i = i2;
                    f3[i] = ((double) ((k2 & 1) == 0 ? 1 : -1)) * ((d / ((double) (i2 + 1))) - (((d / ((double) ((i2 * 2) + 6))) - (scaledX2 / ((double) ((i * 24) + ShapeTypes.CLOUD_CALLOUT)))) * scaledX2));
                } else {
                    i = i2;
                    f3[i] = ((k2 & 1) == 0 ? -scaledX : scaledX) * ((1.0d / ((double) (i + 2))) - (((1.0d / ((double) ((i * 6) + 24))) - (scaledX2 / ((double) ((i * ShapeTypes.CLOUD_CALLOUT) + 720)))) * scaledX2));
                }
                k = i + 1;
                d = 1.0d;
            }
            f = f3;
        } else {
            double inv = 1.0d / scaledX;
            double cos = FastMath.cos(scaledX);
            double sin = FastMath.sin(scaledX);
            f3[0] = inv * sin;
            double[] sc = new double[f3.length];
            sc[0] = 1.0d;
            double coeff = inv;
            int n = 1;
            while (n < f3.length) {
                double c = 0.0d;
                if ((n & 1) == 0) {
                    sc[n] = 0.0d;
                    kStart = n;
                } else {
                    sc[n] = sc[n - 1];
                    c = sc[n];
                    kStart = n - 1;
                }
                double c2 = c;
                double s = 0.0d;
                int k3 = kStart;
                while (true) {
                    int kStart2 = kStart;
                    int k4 = k3;
                    if (k4 <= 1) {
                        break;
                    }
                    double[] f4 = f3;
                    sc[k4] = (((double) (k4 - n)) * sc[k4]) - sc[k4 - 1];
                    s = (s * scaledX2) + sc[k4];
                    sc[k4 - 1] = (((double) ((k4 - 1) - n)) * sc[k4 - 1]) + sc[k4 - 2];
                    c2 = (c2 * scaledX2) + sc[k4 - 1];
                    k3 = k4 - 2;
                    kStart = kStart2;
                    f3 = f4;
                }
                double[] f5 = f3;
                int n2 = n;
                sc[0] = sc[0] * ((double) (-n));
                coeff *= inv;
                f5[n2] = ((((s * scaledX2) + sc[0]) * sin) + (c2 * scaledX * cos)) * coeff;
                n = n2 + 1;
                f3 = f5;
            }
            f = f3;
        }
        if (this.normalized) {
            double scale = 3.141592653589793d;
            int i3 = 1;
            while (true) {
                int i4 = i3;
                f2 = f;
                if (i4 >= f2.length) {
                    break;
                }
                f2[i4] = f2[i4] * scale;
                scale *= 3.141592653589793d;
                i3 = i4 + 1;
                f = f2;
            }
        } else {
            f2 = f;
        }
        return t.compose(f2);
    }
}
