package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.util.FastMath;

public abstract class RungeKuttaIntegrator extends AbstractIntegrator {

    /* renamed from: a */
    private final double[][] f698a;

    /* renamed from: b */
    private final double[] f699b;

    /* renamed from: c */
    private final double[] f700c;
    private final RungeKuttaStepInterpolator prototype;
    private final double step;

    protected RungeKuttaIntegrator(String name, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype2, double step2) {
        super(name);
        this.f700c = c;
        this.f698a = a;
        this.f699b = b;
        this.prototype = prototype2;
        this.step = FastMath.abs(step2);
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        int stages;
        ExpandableStatefulODE expandableStatefulODE = equations;
        sanityChecks(equations, t);
        setEquations(equations);
        char c = 0;
        boolean forward = t > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y = (double[]) y0.clone();
        int stages2 = this.f700c.length + 1;
        double[][] yDotK = new double[stages2][];
        for (int i = 0; i < stages2; i++) {
            yDotK[i] = new double[y0.length];
        }
        double[] yTmp = (double[]) y0.clone();
        RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) this.prototype.copy();
        RungeKuttaStepInterpolator interpolator2 = interpolator;
        double[] yDotTmp = new double[y0.length];
        double[] yTmp2 = yTmp;
        double[][] yDotK2 = yDotK;
        int stages3 = stages2;
        interpolator.reinitialize(this, yTmp, yDotK, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        interpolator2.storeTime(equations.getTime());
        this.stepStart = equations.getTime();
        if (forward) {
            if (this.stepStart + this.step >= t) {
                this.stepSize = t - this.stepStart;
            } else {
                this.stepSize = this.step;
            }
        } else if (this.stepStart - this.step <= t) {
            this.stepSize = t - this.stepStart;
        } else {
            this.stepSize = -this.step;
        }
        initIntegration(equations.getTime(), y0, t);
        this.isLastStep = false;
        while (true) {
            interpolator2.shift();
            computeDerivatives(this.stepStart, y, yDotK2[c]);
            int k = 1;
            while (true) {
                stages = stages3;
                if (k >= stages) {
                    break;
                }
                int j = 0;
                while (j < y0.length) {
                    double sum = this.f698a[k - 1][c] * yDotK2[c][j];
                    for (int l = 1; l < k; l++) {
                        sum += this.f698a[k - 1][l] * yDotK2[l][j];
                    }
                    RungeKuttaStepInterpolator interpolator3 = interpolator2;
                    yTmp2[j] = y[j] + (this.stepSize * sum);
                    j++;
                    interpolator2 = interpolator3;
                    c = 0;
                }
                RungeKuttaStepInterpolator interpolator4 = interpolator2;
                boolean forward2 = forward;
                computeDerivatives(this.stepStart + (this.f700c[k - 1] * this.stepSize), yTmp2, yDotK2[k]);
                k++;
                stages3 = stages;
                interpolator2 = interpolator4;
                forward = forward2;
                c = 0;
            }
            RungeKuttaStepInterpolator interpolator5 = interpolator2;
            boolean forward3 = forward;
            double[] yTmp3 = yTmp2;
            for (int j2 = 0; j2 < y0.length; j2++) {
                double sum2 = this.f699b[0] * yDotK2[0][j2];
                for (int l2 = 1; l2 < stages; l2++) {
                    sum2 += this.f699b[l2] * yDotK2[l2][j2];
                }
                yTmp3[j2] = y[j2] + (this.stepSize * sum2);
            }
            interpolator2 = interpolator5;
            interpolator2.storeTime(this.stepStart + this.stepSize);
            System.arraycopy(yTmp3, 0, y, 0, y0.length);
            double[] yDotTmp2 = yDotTmp;
            System.arraycopy(yDotK2[stages - 1], 0, yDotTmp2, 0, y0.length);
            double[] yDotTmp3 = yDotTmp2;
            this.stepStart = acceptStep(interpolator2, y, yDotTmp2, t);
            if (!this.isLastStep) {
                interpolator2.storeTime(this.stepStart);
                double nextT = this.stepStart + this.stepSize;
                if (!forward3 ? nextT <= t : nextT >= t) {
                    this.stepSize = t - this.stepStart;
                }
            }
            if (this.isLastStep) {
                expandableStatefulODE.setTime(this.stepStart);
                expandableStatefulODE.setCompleteState(y);
                this.stepStart = Double.NaN;
                this.stepSize = Double.NaN;
                return;
            }
            stages3 = stages;
            yTmp2 = yTmp3;
            yDotTmp = yDotTmp3;
            forward = forward3;
            c = 0;
        }
    }

    public double[] singleStep(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t) {
        FirstOrderDifferentialEquations firstOrderDifferentialEquations = equations;
        double d = t0;
        double[] dArr = y0;
        double[] y = (double[]) y0.clone();
        int stages = this.f700c.length + 1;
        double[][] yDotK = new double[stages][];
        for (int i = 0; i < stages; i++) {
            yDotK[i] = new double[dArr.length];
        }
        double[] yTmp = (double[]) y0.clone();
        double h = t - d;
        firstOrderDifferentialEquations.computeDerivatives(d, y, yDotK[0]);
        for (int k = 1; k < stages; k++) {
            int j = 0;
            while (true) {
                int j2 = j;
                if (j2 >= dArr.length) {
                    break;
                }
                double sum = this.f698a[k - 1][0] * yDotK[0][j2];
                for (int l = 1; l < k; l++) {
                    sum += this.f698a[k - 1][l] * yDotK[l][j2];
                }
                yTmp[j2] = y[j2] + (h * sum);
                j = j2 + 1;
            }
            firstOrderDifferentialEquations.computeDerivatives((this.f700c[k - 1] * h) + d, yTmp, yDotK[k]);
        }
        for (int j3 = 0; j3 < dArr.length; j3++) {
            double sum2 = this.f699b[0] * yDotK[0][j3];
            for (int l2 = 1; l2 < stages; l2++) {
                sum2 += this.f699b[l2] * yDotK[l2][j3];
            }
            y[j3] = y[j3] + (h * sum2);
        }
        return y;
    }
}
