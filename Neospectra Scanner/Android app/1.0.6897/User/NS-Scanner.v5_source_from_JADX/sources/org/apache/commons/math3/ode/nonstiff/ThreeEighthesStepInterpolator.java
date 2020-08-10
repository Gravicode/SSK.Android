package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

class ThreeEighthesStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public ThreeEighthesStepInterpolator() {
    }

    ThreeEighthesStepInterpolator(ThreeEighthesStepInterpolator interpolator) {
        super(interpolator);
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new ThreeEighthesStepInterpolator(this);
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double coeffDot3 = 0.75d * theta;
        double coeffDot1 = (((theta * 4.0d) - 5.0d) * coeffDot3) + 1.0d;
        double coeffDot2 = (5.0d - (6.0d * theta)) * coeffDot3;
        double coeffDot4 = ((theta * 2.0d) - 1.0d) * coeffDot3;
        if (this.previousState == null || theta > 0.5d) {
            double coeffDot22 = coeffDot2;
            double s = oneMinusThetaH / 8.0d;
            double fourTheta2 = 4.0d * theta * theta;
            double coeff1 = ((1.0d - (7.0d * theta)) + (2.0d * fourTheta2)) * s;
            double coeff2 = s * 3.0d * ((theta + 1.0d) - fourTheta2);
            double coeff3 = 3.0d * s * (theta + 1.0d);
            double coeff4 = (theta + 1.0d + fourTheta2) * s;
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < this.interpolatedState.length) {
                    double yDot1 = this.yDotK[0][i2];
                    double yDot2 = this.yDotK[1][i2];
                    double yDot3 = this.yDotK[2][i2];
                    double yDot4 = this.yDotK[3][i2];
                    double fourTheta22 = fourTheta2;
                    this.interpolatedState[i2] = (((this.currentState[i2] - (coeff1 * yDot1)) - (coeff2 * yDot2)) - (coeff3 * yDot3)) - (coeff4 * yDot4);
                    this.interpolatedDerivatives[i2] = (coeffDot1 * yDot1) + (coeffDot22 * yDot2) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4);
                    i = i2 + 1;
                    fourTheta2 = fourTheta22;
                } else {
                    return;
                }
            }
        } else {
            double coeffDot23 = coeffDot2;
            double s2 = (theta * this.f701h) / 8.0d;
            double fourTheta23 = 4.0d * theta * theta;
            double coeff12 = ((8.0d - (15.0d * theta)) + (2.0d * fourTheta23)) * s2;
            double coeff22 = s2 * 3.0d * ((5.0d * theta) - fourTheta23);
            double coeff32 = 3.0d * s2 * theta;
            double coeff42 = ((-3.0d * theta) + fourTheta23) * s2;
            int i3 = 0;
            while (true) {
                double fourTheta24 = fourTheta23;
                if (i3 < this.interpolatedState.length) {
                    double yDot12 = this.yDotK[0][i3];
                    double yDot22 = this.yDotK[1][i3];
                    double yDot32 = this.yDotK[2][i3];
                    double yDot42 = this.yDotK[3][i3];
                    this.interpolatedState[i3] = this.previousState[i3] + (coeff12 * yDot12) + (coeff22 * yDot22) + (coeff32 * yDot32) + (coeff42 * yDot42);
                    this.interpolatedDerivatives[i3] = (coeffDot1 * yDot12) + (coeffDot23 * yDot22) + (coeffDot3 * yDot32) + (coeffDot4 * yDot42);
                    i3++;
                    fourTheta23 = fourTheta24;
                } else {
                    return;
                }
            }
        }
    }
}
