package org.apache.commons.math3.p006ml.neuralnet.sofm;

import org.apache.commons.math3.p006ml.neuralnet.sofm.util.ExponentialDecayFunction;
import org.apache.commons.math3.p006ml.neuralnet.sofm.util.QuasiSigmoidDecayFunction;
import org.apache.commons.math3.util.FastMath;

/* renamed from: org.apache.commons.math3.ml.neuralnet.sofm.NeighbourhoodSizeFunctionFactory */
public class NeighbourhoodSizeFunctionFactory {
    private NeighbourhoodSizeFunctionFactory() {
    }

    public static NeighbourhoodSizeFunction exponentialDecay(double initValue, double valueAtNumCall, long numCall) {
        final double d = initValue;
        final double d2 = valueAtNumCall;
        final long j = numCall;
        C09931 r0 = new NeighbourhoodSizeFunction() {
            private final ExponentialDecayFunction decay;

            {
                ExponentialDecayFunction exponentialDecayFunction = new ExponentialDecayFunction(d, d2, j);
                this.decay = exponentialDecayFunction;
            }

            public int value(long n) {
                return (int) FastMath.rint(this.decay.value(n));
            }
        };
        return r0;
    }

    public static NeighbourhoodSizeFunction quasiSigmoidDecay(double initValue, double slope, long numCall) {
        final double d = initValue;
        final double d2 = slope;
        final long j = numCall;
        C09942 r0 = new NeighbourhoodSizeFunction() {
            private final QuasiSigmoidDecayFunction decay;

            {
                QuasiSigmoidDecayFunction quasiSigmoidDecayFunction = new QuasiSigmoidDecayFunction(d, d2, j);
                this.decay = quasiSigmoidDecayFunction;
            }

            public int value(long n) {
                return (int) FastMath.rint(this.decay.value(n));
            }
        };
        return r0;
    }
}
