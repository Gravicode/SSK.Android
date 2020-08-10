package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

class HighamHall54StepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public HighamHall54StepInterpolator() {
    }

    HighamHall54StepInterpolator(HighamHall54StepInterpolator interpolator) {
        super(interpolator);
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new HighamHall54StepInterpolator(this);
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double bDot0 = (((theta * (16.0d - (10.0d * theta))) - 0.5625d) * theta) + 1.0d;
        double bDot2 = ((((67.5d * theta) - 0.04925537109375d) * theta) + 28.6875d) * theta;
        double bDot3 = ((theta * (152.0d - (120.0d * theta))) - 0.1015625d) * theta;
        double bDot4 = ((((62.5d * theta) - 0.05560302734375d) * theta) + 23.4375d) * theta;
        double bDot5 = ((theta * 5.0d) / 8.0d) * ((theta * 2.0d) - 1.0d);
        if (this.previousState == null || theta > 0.5d) {
            double bDot52 = bDot5;
            double theta2 = theta * theta;
            double b0 = this.f701h * ((((((theta * (((-5.0d * theta) / 2.0d) + 5.333333333333333d)) - 1.125d) * theta) + 1.0d) * theta) - 53.333333333333336d);
            double bDot42 = bDot4;
            double b2 = this.f701h * (((((((theta * 135.0d) / 8.0d) - 0.1376953125d) * theta) + 14.34375d) * theta2) - 5.25d);
            double bDot32 = bDot3;
            double b3 = this.f701h * ((((((theta * -30.0d) + 50.666666666666664d) * theta) - 0.203125d) * theta2) + 1.3333333333333333d);
            double bDot22 = bDot2;
            double b4 = this.f701h * ((((theta * (((125.0d * theta) / 8.0d) - 0.17154947916666666d)) + 11.71875d) * theta2) - 3.3958333333333335d);
            double bDot02 = bDot0;
            double b5 = this.f701h * (((((5.0d * theta) / 12.0d) - 14.0d) * theta2) - 42.666666666666664d);
            for (int i = 0; i < this.interpolatedState.length; i++) {
                double yDot0 = this.yDotK[0][i];
                double yDot2 = this.yDotK[2][i];
                double yDot3 = this.yDotK[3][i];
                double yDot4 = this.yDotK[4][i];
                double yDot5 = this.yDotK[5][i];
                this.interpolatedState[i] = this.currentState[i] + (b0 * yDot0) + (b2 * yDot2) + (b3 * yDot3) + (b4 * yDot4) + (b5 * yDot5);
                this.interpolatedDerivatives[i] = (bDot02 * yDot0) + (bDot22 * yDot2) + (bDot32 * yDot3) + (bDot42 * yDot4) + (bDot52 * yDot5);
            }
            return;
        }
        double bDot53 = bDot5;
        double hTheta = this.f701h * theta;
        double b02 = ((((theta * (5.333333333333333d - (2.5d * theta))) - 1.125d) * theta) + 1.0d) * hTheta;
        double b22 = (((((theta * 135.0d) / 8.0d) - 0.1376953125d) * theta) + 14.34375d) * theta * hTheta;
        double b32 = ((((-30.0d * theta) + 50.666666666666664d) * theta) - 0.203125d) * theta * hTheta;
        double b42 = ((theta * (((125.0d * theta) / 8.0d) - 0.17154947916666666d)) + 11.71875d) * theta * hTheta;
        double b52 = theta * (((5.0d * theta) / 12.0d) - 14.0d) * hTheta;
        int i2 = 0;
        while (true) {
            double hTheta2 = hTheta;
            if (i2 < this.interpolatedState.length) {
                double yDot02 = this.yDotK[0][i2];
                double yDot22 = this.yDotK[2][i2];
                double yDot32 = this.yDotK[3][i2];
                double yDot42 = this.yDotK[4][i2];
                double yDot52 = this.yDotK[5][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (b02 * yDot02) + (b22 * yDot22) + (b32 * yDot32) + (b42 * yDot42) + (b52 * yDot52);
                this.interpolatedDerivatives[i2] = (bDot0 * yDot02) + (bDot2 * yDot22) + (bDot3 * yDot32) + (bDot4 * yDot42) + (bDot53 * yDot52);
                i2++;
                hTheta = hTheta2;
            } else {
                double d = bDot0;
                double d2 = bDot2;
                double d3 = bDot3;
                double d4 = bDot4;
                return;
            }
        }
    }
}
