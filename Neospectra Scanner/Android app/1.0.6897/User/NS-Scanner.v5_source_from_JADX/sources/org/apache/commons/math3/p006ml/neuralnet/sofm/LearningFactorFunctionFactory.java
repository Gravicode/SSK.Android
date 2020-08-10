package org.apache.commons.math3.p006ml.neuralnet.sofm;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.p006ml.neuralnet.sofm.util.ExponentialDecayFunction;
import org.apache.commons.math3.p006ml.neuralnet.sofm.util.QuasiSigmoidDecayFunction;

/* renamed from: org.apache.commons.math3.ml.neuralnet.sofm.LearningFactorFunctionFactory */
public class LearningFactorFunctionFactory {
    private LearningFactorFunctionFactory() {
    }

    public static LearningFactorFunction exponentialDecay(double initValue, double valueAtNumCall, long numCall) {
        if (initValue <= 0.0d || initValue > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(initValue), Integer.valueOf(0), Integer.valueOf(1));
        }
        final double d = initValue;
        final double d2 = valueAtNumCall;
        final long j = numCall;
        C09911 r1 = new LearningFactorFunction() {
            private final ExponentialDecayFunction decay;

            {
                ExponentialDecayFunction exponentialDecayFunction = new ExponentialDecayFunction(d, d2, j);
                this.decay = exponentialDecayFunction;
            }

            public double value(long n) {
                return this.decay.value(n);
            }
        };
        return r1;
    }

    public static LearningFactorFunction quasiSigmoidDecay(double initValue, double slope, long numCall) {
        if (initValue <= 0.0d || initValue > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(initValue), Integer.valueOf(0), Integer.valueOf(1));
        }
        final double d = initValue;
        final double d2 = slope;
        final long j = numCall;
        C09922 r1 = new LearningFactorFunction() {
            private final QuasiSigmoidDecayFunction decay;

            {
                QuasiSigmoidDecayFunction quasiSigmoidDecayFunction = new QuasiSigmoidDecayFunction(d, d2, j);
                this.decay = quasiSigmoidDecayFunction;
            }

            public double value(long n) {
                return this.decay.value(n);
            }
        };
        return r1;
    }
}
