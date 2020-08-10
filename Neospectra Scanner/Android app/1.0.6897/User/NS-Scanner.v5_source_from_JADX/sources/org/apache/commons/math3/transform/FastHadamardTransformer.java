package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;

public class FastHadamardTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120211;

    public double[] transform(double[] f, TransformType type) {
        if (type == TransformType.FORWARD) {
            return fht(f);
        }
        return TransformUtils.scaleArray(fht(f), 1.0d / ((double) f.length));
    }

    public double[] transform(UnivariateFunction f, double min, double max, int n, TransformType type) {
        return transform(FunctionUtils.sample(f, min, max, n), type);
    }

    public int[] transform(int[] f) {
        return fht(f);
    }

    /* access modifiers changed from: protected */
    public double[] fht(double[] x) throws MathIllegalArgumentException {
        int n = x.length;
        int halfN = n / 2;
        if (!ArithmeticUtils.isPowerOfTwo((long) n)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, Integer.valueOf(n));
        }
        double[] yCurrent = (double[]) x.clone();
        double[] yPrevious = new double[n];
        for (int j = 1; j < n; j <<= 1) {
            double[] yTmp = yCurrent;
            yCurrent = yPrevious;
            yPrevious = yTmp;
            for (int i = 0; i < halfN; i++) {
                int twoI = i * 2;
                yCurrent[i] = yPrevious[twoI] + yPrevious[twoI + 1];
            }
            for (int i2 = halfN; i2 < n; i2++) {
                int twoI2 = i2 * 2;
                yCurrent[i2] = yPrevious[twoI2 - n] - yPrevious[(twoI2 - n) + 1];
            }
        }
        return yCurrent;
    }

    /* access modifiers changed from: protected */
    public int[] fht(int[] x) throws MathIllegalArgumentException {
        int n = x.length;
        int halfN = n / 2;
        if (!ArithmeticUtils.isPowerOfTwo((long) n)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, Integer.valueOf(n));
        }
        int[] yCurrent = (int[]) x.clone();
        int[] yPrevious = new int[n];
        for (int j = 1; j < n; j <<= 1) {
            int[] yTmp = yCurrent;
            yCurrent = yPrevious;
            yPrevious = yTmp;
            for (int i = 0; i < halfN; i++) {
                int twoI = i * 2;
                yCurrent[i] = yPrevious[twoI] + yPrevious[twoI + 1];
            }
            for (int i2 = halfN; i2 < n; i2++) {
                int twoI2 = i2 * 2;
                yCurrent[i2] = yPrevious[twoI2 - n] - yPrevious[(twoI2 - n) + 1];
            }
        }
        return yCurrent;
    }
}
