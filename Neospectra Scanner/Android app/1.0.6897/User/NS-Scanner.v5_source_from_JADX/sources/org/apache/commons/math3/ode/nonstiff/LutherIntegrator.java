package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.util.FastMath;

public class LutherIntegrator extends RungeKuttaIntegrator {

    /* renamed from: Q */
    private static final double f693Q = FastMath.sqrt(21.0d);
    private static final double[][] STATIC_A = {new double[]{1.0d}, new double[]{0.375d, 0.125d}, new double[]{0.2962962962962963d, 0.07407407407407407d, 0.2962962962962963d}, new double[]{((f693Q * 9.0d) - 0.2109375d) / 392.0d, ((f693Q * 8.0d) - 0.078125d) / 392.0d, (336.0d - (f693Q * 48.0d)) / 392.0d, ((f693Q * 3.0d) - 0.064453125d) / 392.0d}, new double[]{(-1155.0d - (f693Q * 255.0d)) / 1960.0d, (-280.0d - (f693Q * 40.0d)) / 1960.0d, (0.0d - (f693Q * 320.0d)) / 1960.0d, ((f693Q * 363.0d) + 63.0d) / 1960.0d, ((f693Q * 392.0d) + 2352.0d) / 1960.0d}, new double[]{((f693Q * 105.0d) + 330.0d) / 180.0d, ((f693Q * 0.0d) + 120.0d) / 180.0d, ((f693Q * 280.0d) - 0.0224609375d) / 180.0d, (126.0d - (f693Q * 189.0d)) / 180.0d, (-686.0d - (f693Q * 126.0d)) / 180.0d, (490.0d - (f693Q * 70.0d)) / 180.0d}};
    private static final double[] STATIC_B = {0.05d, 0.0d, 0.35555555555555557d, 0.0d, 0.2722222222222222d, 0.2722222222222222d, 0.05d};
    private static final double[] STATIC_C = {1.0d, 0.5d, 0.6666666666666666d, (7.0d - f693Q) / 14.0d, (f693Q + 7.0d) / 14.0d, 1.0d};

    public LutherIntegrator(double step) {
        super("Luther", STATIC_C, STATIC_A, STATIC_B, new LutherStepInterpolator(), step);
    }
}
