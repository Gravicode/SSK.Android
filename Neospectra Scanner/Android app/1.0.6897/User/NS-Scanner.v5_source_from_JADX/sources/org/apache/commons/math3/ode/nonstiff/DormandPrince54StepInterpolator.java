package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

class DormandPrince54StepInterpolator extends RungeKuttaStepInterpolator {
    private static final double A70 = 0.09114583333333333d;
    private static final double A72 = 0.44923629829290207d;
    private static final double A73 = 0.6510416666666666d;
    private static final double A74 = -0.322376179245283d;
    private static final double A75 = 0.13095238095238096d;

    /* renamed from: D0 */
    private static final double f672D0 = -1.1270175653862835d;

    /* renamed from: D2 */
    private static final double f673D2 = 2.675424484351598d;

    /* renamed from: D3 */
    private static final double f674D3 = -5.685526961588504d;

    /* renamed from: D4 */
    private static final double f675D4 = 3.5219323679207912d;

    /* renamed from: D5 */
    private static final double f676D5 = -1.7672812570757455d;

    /* renamed from: D6 */
    private static final double f677D6 = 2.382468931778144d;
    private static final long serialVersionUID = 20111120;

    /* renamed from: v1 */
    private double[] f678v1;

    /* renamed from: v2 */
    private double[] f679v2;

    /* renamed from: v3 */
    private double[] f680v3;

    /* renamed from: v4 */
    private double[] f681v4;
    private boolean vectorsInitialized;

    public DormandPrince54StepInterpolator() {
        this.f678v1 = null;
        this.f679v2 = null;
        this.f680v3 = null;
        this.f681v4 = null;
        this.vectorsInitialized = false;
    }

    DormandPrince54StepInterpolator(DormandPrince54StepInterpolator interpolator) {
        super(interpolator);
        if (interpolator.f678v1 == null) {
            this.f678v1 = null;
            this.f679v2 = null;
            this.f680v3 = null;
            this.f681v4 = null;
            this.vectorsInitialized = false;
            return;
        }
        this.f678v1 = (double[]) interpolator.f678v1.clone();
        this.f679v2 = (double[]) interpolator.f679v2.clone();
        this.f680v3 = (double[]) interpolator.f680v3.clone();
        this.f681v4 = (double[]) interpolator.f681v4.clone();
        this.vectorsInitialized = interpolator.vectorsInitialized;
    }

    /* access modifiers changed from: protected */
    public StepInterpolator doCopy() {
        return new DormandPrince54StepInterpolator(this);
    }

    public void reinitialize(AbstractIntegrator integrator, double[] y, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super.reinitialize(integrator, y, yDotK, forward, primaryMapper, secondaryMappers);
        this.f678v1 = null;
        this.f679v2 = null;
        this.f680v3 = null;
        this.f681v4 = null;
        this.vectorsInitialized = false;
    }

    public void storeTime(double t) {
        super.storeTime(t);
        this.vectorsInitialized = false;
    }

    /* access modifiers changed from: protected */
    public void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        char c = 0;
        if (!this.vectorsInitialized) {
            if (this.f678v1 == null) {
                this.f678v1 = new double[this.interpolatedState.length];
                this.f679v2 = new double[this.interpolatedState.length];
                this.f680v3 = new double[this.interpolatedState.length];
                this.f681v4 = new double[this.interpolatedState.length];
            }
            int i = 0;
            while (i < this.interpolatedState.length) {
                double yDot0 = this.yDotK[c][i];
                double yDot2 = this.yDotK[2][i];
                double yDot3 = this.yDotK[3][i];
                double yDot4 = this.yDotK[4][i];
                double yDot5 = this.yDotK[5][i];
                double yDot6 = this.yDotK[6][i];
                this.f678v1[i] = (A70 * yDot0) + (A72 * yDot2) + (A73 * yDot3) + (A74 * yDot4) + (A75 * yDot5);
                this.f679v2[i] = yDot0 - this.f678v1[i];
                this.f680v3[i] = (this.f678v1[i] - this.f679v2[i]) - yDot6;
                this.f681v4[i] = (f672D0 * yDot0) + (f673D2 * yDot2) + (f674D3 * yDot3) + (f675D4 * yDot4) + (f676D5 * yDot5) + (f677D6 * yDot6);
                i++;
                c = 0;
            }
            this.vectorsInitialized = true;
        }
        double eta = 1.0d - theta;
        double twoTheta = theta * 2.0d;
        double dot2 = 1.0d - twoTheta;
        double dot3 = (2.0d - (theta * 3.0d)) * theta;
        double dot4 = (((twoTheta - 3.0d) * theta) + 1.0d) * twoTheta;
        if (this.previousState == null || theta > 0.5d) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.interpolatedState.length) {
                    this.interpolatedState[i3] = this.currentState[i3] - ((this.f678v1[i3] - (theta * (this.f679v2[i3] + (theta * (this.f680v3[i3] + (this.f681v4[i3] * eta)))))) * oneMinusThetaH);
                    this.interpolatedDerivatives[i3] = this.f678v1[i3] + (this.f679v2[i3] * dot2) + (this.f680v3[i3] * dot3) + (this.f681v4[i3] * dot4);
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else {
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < this.interpolatedState.length) {
                    double twoTheta2 = twoTheta;
                    this.interpolatedState[i5] = this.previousState[i5] + (this.f701h * theta * (this.f678v1[i5] + ((this.f679v2[i5] + ((this.f680v3[i5] + (this.f681v4[i5] * eta)) * theta)) * eta)));
                    this.interpolatedDerivatives[i5] = this.f678v1[i5] + (this.f679v2[i5] * dot2) + (this.f680v3[i5] * dot3) + (this.f681v4[i5] * dot4);
                    i4 = i5 + 1;
                    twoTheta = twoTheta2;
                } else {
                    return;
                }
            }
        }
    }
}
