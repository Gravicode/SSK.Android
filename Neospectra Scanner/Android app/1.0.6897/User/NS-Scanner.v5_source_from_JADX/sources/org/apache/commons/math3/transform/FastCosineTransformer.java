package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

public class FastCosineTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120212;
    private final DctNormalization normalization;

    public FastCosineTransformer(DctNormalization normalization2) {
        this.normalization = normalization2;
    }

    public double[] transform(double[] f, TransformType type) throws MathIllegalArgumentException {
        double s1;
        if (type != TransformType.FORWARD) {
            double s2 = 2.0d / ((double) (f.length - 1));
            if (this.normalization == DctNormalization.ORTHOGONAL_DCT_I) {
                s1 = FastMath.sqrt(s2);
            } else {
                s1 = s2;
            }
            return TransformUtils.scaleArray(fct(f), s1);
        } else if (this.normalization != DctNormalization.ORTHOGONAL_DCT_I) {
            return fct(f);
        } else {
            return TransformUtils.scaleArray(fct(f), FastMath.sqrt(2.0d / ((double) (f.length - 1))));
        }
    }

    public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type) throws MathIllegalArgumentException {
        return transform(FunctionUtils.sample(f, min, max, n), type);
    }

    /* access modifiers changed from: protected */
    public double[] fct(double[] f) throws MathIllegalArgumentException {
        double[] dArr = f;
        double[] transformed = new double[dArr.length];
        int n = dArr.length - 1;
        if (!ArithmeticUtils.isPowerOfTwo((long) n)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_PLUS_ONE, Integer.valueOf(dArr.length));
        }
        double d = 0.5d;
        if (n == 1) {
            transformed[0] = (dArr[0] + dArr[1]) * 0.5d;
            transformed[1] = (dArr[0] - dArr[1]) * 0.5d;
            return transformed;
        }
        double[] x = new double[n];
        x[0] = (dArr[0] + dArr[n]) * 0.5d;
        x[n >> 1] = dArr[n >> 1];
        double t1 = (dArr[0] - dArr[n]) * 0.5d;
        int i = 1;
        while (i < (n >> 1)) {
            double a = (dArr[i] + dArr[n - i]) * d;
            double b = FastMath.sin((((double) i) * 3.141592653589793d) / ((double) n)) * (dArr[i] - dArr[n - i]);
            double[] x2 = x;
            double c = FastMath.cos((((double) i) * 3.141592653589793d) / ((double) n)) * (dArr[i] - dArr[n - i]);
            x2[i] = a - b;
            x2[n - i] = a + b;
            t1 += c;
            i++;
            x = x2;
            d = 0.5d;
        }
        Complex[] y = new FastFourierTransformer(DftNormalization.STANDARD).transform(x, TransformType.FORWARD);
        transformed[0] = y[0].getReal();
        transformed[1] = t1;
        for (int i2 = 1; i2 < (n >> 1); i2++) {
            transformed[i2 * 2] = y[i2].getReal();
            transformed[(i2 * 2) + 1] = transformed[(i2 * 2) - 1] - y[i2].getImaginary();
        }
        transformed[n] = y[n >> 1].getReal();
        return transformed;
    }
}
