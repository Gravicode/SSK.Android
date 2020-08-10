package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

class ClassicalRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public ClassicalRungeKuttaStepInterpolator() {
    }

    ClassicalRungeKuttaStepInterpolator(ClassicalRungeKuttaStepInterpolator interpolator) {
        super(interpolator);
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new ClassicalRungeKuttaStepInterpolator(this);
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double d = theta;
        double oneMinusTheta = 1.0d - d;
        double oneMinus2Theta = 1.0d - (d * 2.0d);
        double coeffDot1 = oneMinusTheta * oneMinus2Theta;
        double coeffDot23 = d * 2.0d * oneMinusTheta;
        double coeffDot4 = (-d) * oneMinus2Theta;
        if (this.previousState == null || d > 0.5d) {
            double fourTheta = d * 4.0d;
            double s = oneMinusThetaH / 6.0d;
            double coeff1 = ((((-fourTheta) + 5.0d) * d) - 1.0d) * s;
            double coeff23 = (((fourTheta - 2.0d) * d) - 2.0d) * s;
            double d2 = oneMinus2Theta;
            double coeff4 = ((((-fourTheta) - 1.0d) * d) - 1.0d) * s;
            int i = 0;
            while (i < this.interpolatedState.length) {
                double yDot1 = this.yDotK[0][i];
                double yDot23 = this.yDotK[1][i] + this.yDotK[2][i];
                double yDot4 = this.yDotK[3][i];
                this.interpolatedState[i] = this.currentState[i] + (coeff1 * yDot1) + (coeff23 * yDot23) + (coeff4 * yDot4);
                this.interpolatedDerivatives[i] = (coeffDot1 * yDot1) + (coeffDot23 * yDot23) + (coeffDot4 * yDot4);
                i++;
                double d3 = theta;
            }
            return;
        }
        double fourTheta2 = d * 4.0d * d;
        double d4 = oneMinusTheta;
        double s2 = (this.f701h * d) / 6.0d;
        double coeff12 = ((6.0d - (9.0d * d)) + fourTheta2) * s2;
        double coeff232 = ((d * 6.0d) - fourTheta2) * s2;
        double coeff42 = ((-3.0d * d) + fourTheta2) * s2;
        int i2 = 0;
        while (true) {
            double fourTheta22 = fourTheta2;
            if (i2 < this.interpolatedState.length) {
                double yDot12 = this.yDotK[0][i2];
                double yDot232 = this.yDotK[1][i2] + this.yDotK[2][i2];
                double yDot42 = this.yDotK[3][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (coeff12 * yDot12) + (coeff232 * yDot232) + (coeff42 * yDot42);
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot12) + (coeffDot23 * yDot232) + (coeffDot4 * yDot42);
                i2++;
                fourTheta2 = fourTheta22;
            } else {
                double d5 = oneMinus2Theta;
                return;
            }
        }
    }
}
