package org.apache.commons.math3.analysis.interpolation;

import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.TrivariateFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

/* compiled from: TricubicInterpolatingFunction */
class TricubicFunction implements TrivariateFunction {

    /* renamed from: N */
    private static final short f522N = 4;

    /* renamed from: a */
    private final double[][][] f523a = ((double[][][]) Array.newInstance(double.class, new int[]{4, 4, 4}));

    TricubicFunction(double[] aV) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    this.f523a[i][j][k] = aV[(((k * 4) + j) * 4) + i];
                }
            }
        }
    }

    public double value(double x, double y, double z) throws OutOfRangeException {
        if (x < 0.0d || x > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(x), Integer.valueOf(0), Integer.valueOf(1));
        } else if (y < 0.0d || y > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(y), Integer.valueOf(0), Integer.valueOf(1));
        } else if (z < 0.0d || z > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(z), Integer.valueOf(0), Integer.valueOf(1));
        } else {
            double x2 = x * x;
            int i = 4;
            double[] pX = {1.0d, x, x2, x2 * x};
            double y2 = y * y;
            double d = x2;
            double[] pY = {1.0d, y, y2, y2 * y};
            double z2 = z * z;
            double[] pZ = {1.0d, z, z2, z2 * z};
            double result = 0.0d;
            int i2 = 0;
            while (i2 < i) {
                double result2 = result;
                int j = 0;
                while (j < i) {
                    int k = 0;
                    while (k < i) {
                        result2 += this.f523a[i2][j][k] * pX[i2] * pY[j] * pZ[k];
                        k++;
                        i = 4;
                    }
                    j++;
                    i = 4;
                }
                i2++;
                result = result2;
                i = 4;
            }
            return result;
        }
    }
}
