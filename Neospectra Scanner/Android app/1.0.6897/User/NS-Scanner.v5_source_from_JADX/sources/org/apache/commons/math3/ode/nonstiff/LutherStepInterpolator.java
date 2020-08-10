package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

class LutherStepInterpolator extends RungeKuttaStepInterpolator {

    /* renamed from: Q */
    private static final double f694Q = FastMath.sqrt(21.0d);
    private static final long serialVersionUID = 20140416;

    public LutherStepInterpolator() {
    }

    LutherStepInterpolator(LutherStepInterpolator interpolator) {
        super(interpolator);
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new LutherStepInterpolator(this);
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double coeffDot1 = (((((((21.0d * theta) - 0.095703125d) * theta) + 36.0d) * theta) - 0.4125d) * theta) + 1.0d;
        long j = 0;
        double coeffDot3 = ((((((112.0d * theta) - 0.022135416666666668d) * theta) + 106.66666666666667d) * theta) - 0.31666666666666665d) * theta;
        double coeffDot4 = (((((((-567.0d * theta) / 5.0d) + 194.4d) * theta) - 0.0462890625d) * theta) + 12.96d) * theta;
        double coeffDot5 = ((((f694Q * 343.0d) + 833.0d) / 150.0d) + ((((-637.0d - (f694Q * 357.0d)) / 30.0d) + (((((f694Q * 287.0d) + 392.0d) / 15.0d) + (((-49.0d - (f694Q * 49.0d)) * theta) / 5.0d)) * theta)) * theta)) * theta;
        double coeffDot6 = (((833.0d - (f694Q * 343.0d)) / 150.0d) + (((((f694Q * 357.0d) - 0.00685882568359375d) / 30.0d) + ((((392.0d - (f694Q * 287.0d)) / 15.0d) + ((((f694Q * 49.0d) - 0.091796875d) * theta) / 5.0d)) * theta)) * theta)) * theta;
        double coeffDot7 = ((((3.0d * theta) - 1.5d) * theta) + 0.6d) * theta;
        if (this.previousState == null || theta > 0.5d) {
            double coeffDot52 = coeffDot5;
            double coeff1 = (theta * ((theta * ((theta * (((-21.0d * theta) / 5.0d) + 7.55d)) - 0.94375d)) + 0.95d)) - 89.6d;
            double coeff3 = (((((theta * (((-112.0d * theta) / 5.0d) + 28.266666666666666d)) - 0.5888888888888889d) * theta) - 12.622222222222222d) * theta) - 12.622222222222222d;
            double coeff4 = theta * ((theta * (((567.0d * theta) / 25.0d) - 0.1725d)) + 6.48d) * theta;
            double coeff5 = (((theta * ((((f694Q * 1029.0d) + 2254.0d) / 900.0d) + ((((-1372.0d - (f694Q * 847.0d)) / 300.0d) + ((((f694Q * 49.0d) + 49.0d) * theta) / 25.0d)) * theta))) - 15.28888888888889d) * theta) - 15.28888888888889d;
            double coeff6 = ((((((2254.0d - (f694Q * 1029.0d)) / 900.0d) + (theta * ((((f694Q * 847.0d) - 0.00324249267578125d) / 300.0d) + ((theta * (49.0d - (f694Q * 49.0d))) / 25.0d)))) * theta) - 15.28888888888889d) * theta) - 15.28888888888889d;
            double coeff7 = (theta * ((theta * ((-0.75d * theta) + 0.25d)) - 89.6d)) - 89.6d;
            for (int i = 0; i < this.interpolatedState.length; i++) {
                double yDot1 = this.yDotK[0][i];
                double yDot2 = this.yDotK[1][i];
                double yDot3 = this.yDotK[2][i];
                double yDot4 = this.yDotK[3][i];
                double yDot5 = this.yDotK[4][i];
                double yDot6 = this.yDotK[5][i];
                double yDot7 = this.yDotK[6][i];
                this.interpolatedState[i] = this.currentState[i] + (((coeff1 * yDot1) + (yDot2 * 0.0d) + (coeff3 * yDot3) + (coeff4 * yDot4) + (coeff5 * yDot5) + (coeff6 * yDot6) + (coeff7 * yDot7)) * oneMinusThetaH);
                this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (yDot2 * 0.0d) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4) + (coeffDot52 * yDot5) + (coeffDot6 * yDot6) + (coeffDot7 * yDot7);
            }
            return;
        }
        double coeff12 = (theta * ((theta * ((theta * (((21.0d * theta) / 5.0d) - 0.3828125d)) + 12.0d)) - 0.825d)) + 1.0d;
        double coeff32 = ((((theta * (((112.0d * theta) / 5.0d) - 0.08854166666666667d)) + 35.55555555555556d) * theta) - 0.6333333333333333d) * theta;
        double coeff42 = theta * ((theta * ((theta * (((-567.0d * theta) / 25.0d) + 48.6d)) - 0.12421875d)) + 6.48d);
        double coeff52 = ((((f694Q * 343.0d) + 833.0d) / 300.0d) + ((((-637.0d - (f694Q * 357.0d)) / 90.0d) + (((((f694Q * 287.0d) + 392.0d) / 60.0d) + (((-49.0d - (f694Q * 49.0d)) * theta) / 25.0d)) * theta)) * theta)) * theta;
        double coeff62 = (((833.0d - (f694Q * 343.0d)) / 300.0d) + (theta * ((((f694Q * 357.0d) - 0.00685882568359375d) / 90.0d) + (theta * (((392.0d - (f694Q * 287.0d)) / 60.0d) + ((((f694Q * 49.0d) - 0.091796875d) * theta) / 25.0d)))))) * theta;
        double coeff72 = theta * ((theta * ((0.75d * theta) - 4.0d)) + 0.3d);
        int i2 = 0;
        while (true) {
            long j2 = j;
            if (i2 < this.interpolatedState.length) {
                double yDot12 = this.yDotK[0][i2];
                double yDot22 = this.yDotK[1][i2];
                double yDot32 = this.yDotK[2][i2];
                double yDot42 = this.yDotK[3][i2];
                double yDot52 = this.yDotK[4][i2];
                double yDot62 = this.yDotK[5][i2];
                double yDot72 = this.yDotK[6][i2];
                double coeffDot53 = coeffDot5;
                this.interpolatedState[i2] = this.previousState[i2] + (this.f701h * theta * ((coeff12 * yDot12) + (yDot22 * 0.0d) + (coeff32 * yDot32) + (coeff42 * yDot42) + (coeff52 * yDot52) + (coeff62 * yDot62) + (coeff72 * yDot72)));
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot12) + (yDot22 * 0.0d) + (coeffDot3 * yDot32) + (coeffDot4 * yDot42) + (coeffDot53 * yDot52) + (coeffDot6 * yDot62) + (coeffDot7 * yDot72);
                i2++;
                j = j2;
                coeffDot5 = coeffDot53;
            } else {
                return;
            }
        }
    }
}
