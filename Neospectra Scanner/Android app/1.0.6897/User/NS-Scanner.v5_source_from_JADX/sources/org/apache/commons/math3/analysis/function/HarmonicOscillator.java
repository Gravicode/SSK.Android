package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

public class HarmonicOscillator implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double amplitude;
    private final double omega;
    private final double phase;

    public static class Parametric implements ParametricUnivariateFunction {
        public double value(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return HarmonicOscillator.value((param[1] * x) + param[2], param[0]);
        }

        public double[] gradient(double x, double... param) throws NullArgumentException, DimensionMismatchException {
            double[] dArr = param;
            validateParameters(dArr);
            double amplitude = dArr[0];
            double omega = dArr[1];
            double phase = dArr[2];
            double xTimesOmegaPlusPhase = (omega * x) + phase;
            double d = phase;
            double d2 = omega;
            double p = (-amplitude) * FastMath.sin(xTimesOmegaPlusPhase);
            return new double[]{HarmonicOscillator.value(xTimesOmegaPlusPhase, 1.0d), p * x, p};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            } else if (param.length != 3) {
                throw new DimensionMismatchException(param.length, 3);
            }
        }
    }

    public HarmonicOscillator(double amplitude2, double omega2, double phase2) {
        this.amplitude = amplitude2;
        this.omega = omega2;
        this.phase = phase2;
    }

    public double value(double x) {
        return value((this.omega * x) + this.phase, this.amplitude);
    }

    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* access modifiers changed from: private */
    public static double value(double xTimesOmegaPlusPhase, double amplitude2) {
        return FastMath.cos(xTimesOmegaPlusPhase) * amplitude2;
    }

    public DerivativeStructure value(DerivativeStructure t) throws DimensionMismatchException {
        double[] f = new double[(t.getOrder() + 1)];
        double alpha = (this.omega * t.getValue()) + this.phase;
        f[0] = this.amplitude * FastMath.cos(alpha);
        if (f.length > 1) {
            f[1] = (-this.amplitude) * this.omega * FastMath.sin(alpha);
            double mo2 = (-this.omega) * this.omega;
            for (int i = 2; i < f.length; i++) {
                f[i] = f[i - 2] * mo2;
            }
        }
        return t.compose(f);
    }
}
