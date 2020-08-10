package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.util.FastMath;

public class HighamHall54Integrator extends EmbeddedRungeKuttaIntegrator {
    private static final String METHOD_NAME = "Higham-Hall 5(4)";
    private static final double[][] STATIC_A = {new double[]{0.2222222222222222d}, new double[]{0.08333333333333333d, 0.25d}, new double[]{0.125d, 0.0d, 0.375d}, new double[]{0.182d, -0.27d, 0.624d, 0.064d}, new double[]{-0.55d, 1.35d, 2.4d, -7.2d, 5.0d}, new double[]{0.08333333333333333d, 0.0d, 0.84375d, -1.3333333333333333d, 1.3020833333333333d, 0.10416666666666667d}};
    private static final double[] STATIC_B = {0.08333333333333333d, 0.0d, 0.84375d, -1.3333333333333333d, 1.3020833333333333d, 0.10416666666666667d, 0.0d};
    private static final double[] STATIC_C = {0.2222222222222222d, 0.3333333333333333d, 0.5d, 0.6d, 1.0d, 1.0d};
    private static final double[] STATIC_E = {-0.05d, 0.0d, 0.50625d, -1.2d, 0.78125d, 0.0625d, -0.1d};

    public HighamHall54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, (RungeKuttaStepInterpolator) new HighamHall54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public HighamHall54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, (RungeKuttaStepInterpolator) new HighamHall54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    public int getOrder() {
        return 5;
    }

    /* access modifiers changed from: protected */
    public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0.0d;
        for (int j = 0; j < this.mainSetDimension; j++) {
            double errSum = STATIC_E[0] * yDotK[0][j];
            for (int l = 1; l < STATIC_E.length; l++) {
                errSum += STATIC_E[l] * yDotK[l][j];
            }
            double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
            double ratio = (h * errSum) / (this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yScale) : this.vecAbsoluteTolerance[j] + (this.vecRelativeTolerance[j] * yScale));
            error += ratio * ratio;
        }
        return FastMath.sqrt(error / ((double) this.mainSetDimension));
    }
}
