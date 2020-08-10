package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.util.FastMath;

public class DormandPrince54Integrator extends EmbeddedRungeKuttaIntegrator {

    /* renamed from: E1 */
    private static final double f666E1 = 0.0012326388888888888d;

    /* renamed from: E3 */
    private static final double f667E3 = -0.0042527702905061394d;

    /* renamed from: E4 */
    private static final double f668E4 = 0.03697916666666667d;

    /* renamed from: E5 */
    private static final double f669E5 = -0.05086379716981132d;

    /* renamed from: E6 */
    private static final double f670E6 = 0.0419047619047619d;

    /* renamed from: E7 */
    private static final double f671E7 = -0.025d;
    private static final String METHOD_NAME = "Dormand-Prince 5(4)";
    private static final double[][] STATIC_A = {new double[]{0.2d}, new double[]{0.075d, 0.225d}, new double[]{0.9777777777777777d, -3.7333333333333334d, 3.5555555555555554d}, new double[]{2.9525986892242035d, -11.595793324188385d, 9.822892851699436d, -0.2908093278463649d}, new double[]{2.8462752525252526d, -10.757575757575758d, 8.906422717743473d, 0.2784090909090909d, -0.2735313036020583d}, new double[]{0.09114583333333333d, 0.0d, 0.44923629829290207d, 0.6510416666666666d, -0.322376179245283d, 0.13095238095238096d}};
    private static final double[] STATIC_B = {0.09114583333333333d, 0.0d, 0.44923629829290207d, 0.6510416666666666d, -0.322376179245283d, 0.13095238095238096d, 0.0d};
    private static final double[] STATIC_C = {0.2d, 0.3d, 0.8d, 0.8888888888888888d, 1.0d, 1.0d};

    public DormandPrince54Integrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (RungeKuttaStepInterpolator) new DormandPrince54StepInterpolator(), minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public DormandPrince54Integrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, true, STATIC_C, STATIC_A, STATIC_B, (RungeKuttaStepInterpolator) new DormandPrince54StepInterpolator(), minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    public int getOrder() {
        return 5;
    }

    /* access modifiers changed from: protected */
    public double estimateError(double[][] yDotK, double[] y0, double[] y1, double h) {
        double error = 0.0d;
        for (int j = 0; j < this.mainSetDimension; j++) {
            double errSum = (yDotK[0][j] * f666E1) + (yDotK[2][j] * f667E3) + (yDotK[3][j] * f668E4) + (yDotK[4][j] * f669E5) + (yDotK[5][j] * f670E6) + (yDotK[6][j] * f671E7);
            double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
            double ratio = (h * errSum) / (this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yScale) : this.vecAbsoluteTolerance[j] + (this.vecRelativeTolerance[j] * yScale));
            error += ratio * ratio;
        }
        return FastMath.sqrt(error / ((double) this.mainSetDimension));
    }
}
