package org.apache.commons.math3.p006ml.neuralnet.sofm.util;

import org.apache.commons.math3.analysis.function.Logistic;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;

/* renamed from: org.apache.commons.math3.ml.neuralnet.sofm.util.QuasiSigmoidDecayFunction */
public class QuasiSigmoidDecayFunction {
    private final double scale;
    private final Logistic sigmoid;

    public QuasiSigmoidDecayFunction(double initValue, double slope, long numCall) {
        long j = numCall;
        if (initValue <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(initValue));
        } else if (slope >= 0.0d) {
            throw new NumberIsTooLargeException(Double.valueOf(slope), Integer.valueOf(0), false);
        } else if (j <= 1) {
            throw new NotStrictlyPositiveException(Long.valueOf(numCall));
        } else {
            double k = initValue;
            double m = (double) j;
            Logistic logistic = r5;
            double d = m;
            Logistic logistic2 = new Logistic(k, m, (4.0d * slope) / initValue, 1.0d, 0.0d, 1.0d);
            this.sigmoid = logistic;
            this.scale = k / this.sigmoid.value(0.0d);
        }
    }

    public double value(long numCall) {
        return this.scale * this.sigmoid.value((double) numCall);
    }
}
