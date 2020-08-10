package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

public class FastSineTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120211;
    private final DstNormalization normalization;

    public FastSineTransformer(DstNormalization normalization2) {
        this.normalization = normalization2;
    }

    public double[] transform(double[] f, TransformType type) {
        if (this.normalization == DstNormalization.ORTHOGONAL_DST_I) {
            return TransformUtils.scaleArray(fst(f), FastMath.sqrt(2.0d / ((double) f.length)));
        } else if (type == TransformType.FORWARD) {
            return fst(f);
        } else {
            return TransformUtils.scaleArray(fst(f), 2.0d / ((double) f.length));
        }
    }

    public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type) {
        double[] data = FunctionUtils.sample(f, min, max, n);
        data[0] = 0.0d;
        return transform(data, type);
    }

    /* access modifiers changed from: protected */
    public double[] fst(double[] f) throws MathIllegalArgumentException {
        double[] dArr = f;
        double[] transformed = new double[dArr.length];
        if (!ArithmeticUtils.isPowerOfTwo((long) dArr.length)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, Integer.valueOf(dArr.length));
        } else if (dArr[0] != 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.FIRST_ELEMENT_NOT_ZERO, Double.valueOf(dArr[0]));
        } else {
            int n = dArr.length;
            if (n == 1) {
                transformed[0] = 0.0d;
                return transformed;
            }
            double[] x = new double[n];
            x[0] = 0.0d;
            x[n >> 1] = dArr[n >> 1] * 2.0d;
            for (int i = 1; i < (n >> 1); i++) {
                double a = FastMath.sin((((double) i) * 3.141592653589793d) / ((double) n)) * (dArr[i] + dArr[n - i]);
                double b = (dArr[i] - dArr[n - i]) * 0.5d;
                x[i] = a + b;
                x[n - i] = a - b;
            }
            Complex[] y = new FastFourierTransformer(DftNormalization.STANDARD).transform(x, TransformType.FORWARD);
            transformed[0] = 0.0d;
            transformed[1] = y[0].getReal() * 0.5d;
            for (int i2 = 1; i2 < (n >> 1); i2++) {
                transformed[i2 * 2] = -y[i2].getImaginary();
                transformed[(i2 * 2) + 1] = y[i2].getReal() + transformed[(i2 * 2) - 1];
            }
            return transformed;
        }
    }
}
