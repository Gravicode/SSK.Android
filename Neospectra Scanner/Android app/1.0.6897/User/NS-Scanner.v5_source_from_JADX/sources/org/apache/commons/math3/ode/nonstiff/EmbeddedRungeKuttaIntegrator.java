package org.apache.commons.math3.ode.nonstiff;

import java.lang.reflect.Array;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.FastMath;

public abstract class EmbeddedRungeKuttaIntegrator extends AdaptiveStepsizeIntegrator {

    /* renamed from: a */
    private final double[][] f688a;

    /* renamed from: b */
    private final double[] f689b;

    /* renamed from: c */
    private final double[] f690c;
    private final double exp = (-1.0d / ((double) getOrder()));
    private final boolean fsal;
    private double maxGrowth;
    private double minReduction;
    private final RungeKuttaStepInterpolator prototype;
    private double safety;

    /* access modifiers changed from: protected */
    public abstract double estimateError(double[][] dArr, double[] dArr2, double[] dArr3, double d);

    public abstract int getOrder();

    protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal2, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype2, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.fsal = fsal2;
        this.f690c = c;
        this.f688a = a;
        this.f689b = b;
        this.prototype = prototype2;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal2, double[] c, double[][] a, double[] b, RungeKuttaStepInterpolator prototype2, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.fsal = fsal2;
        this.f690c = c;
        this.f688a = a;
        this.f689b = b;
        this.prototype = prototype2;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety2) {
        this.safety = safety2;
    }

    public void integrate(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        int stages;
        double[] yTmp;
        char c;
        boolean forward;
        double[] y0;
        double[] y;
        double error;
        double[] y02;
        double[] y2;
        double filteredNextT;
        boolean forward2;
        double[] y3;
        double[] y03;
        double hNew;
        int stages2;
        double error2;
        ExpandableStatefulODE expandableStatefulODE = equations;
        sanityChecks(equations, t);
        setEquations(equations);
        char c2 = 0;
        boolean forward3 = t > equations.getTime();
        double[] y04 = equations.getCompleteState();
        double[] y4 = (double[]) y04.clone();
        int stages3 = this.f690c.length + 1;
        double[][] yDotK = (double[][]) Array.newInstance(double.class, new int[]{stages3, y4.length});
        double[] yTmp2 = (double[]) y04.clone();
        RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) this.prototype.copy();
        RungeKuttaStepInterpolator interpolator2 = interpolator;
        double[] yDotTmp = new double[y4.length];
        double[] yTmp3 = yTmp2;
        int stages4 = stages3;
        interpolator.reinitialize(this, yTmp2, yDotK, forward3, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        interpolator2.storeTime(equations.getTime());
        this.stepStart = equations.getTime();
        double hNew2 = 0.0d;
        boolean firstTime = true;
        initIntegration(equations.getTime(), y04, t);
        this.isLastStep = false;
        while (true) {
            interpolator2.shift();
            double error3 = 10.0d;
            double hNew3 = hNew2;
            boolean firstTime2 = firstTime;
            while (true) {
                error = error3;
                if (error < 1.0d) {
                    break;
                }
                if (firstTime2 || !this.fsal) {
                    computeDerivatives(this.stepStart, y, yDotK[c]);
                }
                if (firstTime2) {
                    double[] scale = new double[this.mainSetDimension];
                    if (this.vecAbsoluteTolerance == null) {
                        int i = 0;
                        while (i < scale.length) {
                            double error4 = error;
                            scale[i] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * FastMath.abs(y[i]));
                            i++;
                            error = error4;
                            ExpandableStatefulODE expandableStatefulODE2 = equations;
                        }
                        error2 = error;
                    } else {
                        error2 = error;
                        for (int i2 = 0; i2 < scale.length; i2++) {
                            scale[i2] = this.vecAbsoluteTolerance[i2] + (this.vecRelativeTolerance[i2] * FastMath.abs(y[i2]));
                        }
                    }
                    int order = getOrder();
                    double d = this.stepStart;
                    double[] dArr = scale;
                    double d2 = error2;
                    double[] dArr2 = scale;
                    double[] scale2 = y;
                    y3 = y;
                    double[] y5 = yDotK[c];
                    y03 = y0;
                    forward2 = forward;
                    firstTime2 = false;
                    hNew = initializeStep(forward, order, dArr, d, scale2, y5, yTmp, yDotK[1]);
                } else {
                    y3 = y;
                    y03 = y0;
                    forward2 = forward;
                    hNew = hNew3;
                }
                this.stepSize = hNew;
                if (forward2) {
                    if (this.stepStart + this.stepSize >= t) {
                        this.stepSize = t - this.stepStart;
                    }
                } else if (this.stepStart + this.stepSize <= t) {
                    this.stepSize = t - this.stepStart;
                }
                int k = 1;
                while (true) {
                    stages2 = stages;
                    if (k >= stages2) {
                        break;
                    }
                    int j = 0;
                    while (j < y03.length) {
                        double sum = this.f688a[k - 1][0] * yDotK[0][j];
                        for (int l = 1; l < k; l++) {
                            sum += this.f688a[k - 1][l] * yDotK[l][j];
                        }
                        double hNew4 = hNew;
                        yTmp[j] = y3[j] + (this.stepSize * sum);
                        j++;
                        hNew = hNew4;
                    }
                    double hNew5 = hNew;
                    computeDerivatives(this.stepStart + (this.f690c[k - 1] * this.stepSize), yTmp, yDotK[k]);
                    k++;
                    stages = stages2;
                    hNew = hNew5;
                }
                double hNew6 = hNew;
                double[] yTmp4 = yTmp;
                for (int j2 = 0; j2 < y03.length; j2++) {
                    double sum2 = this.f689b[0] * yDotK[0][j2];
                    for (int l2 = 1; l2 < stages2; l2++) {
                        sum2 += this.f689b[l2] * yDotK[l2][j2];
                    }
                    yTmp4[j2] = y3[j2] + (this.stepSize * sum2);
                }
                error3 = estimateError(yDotK, y3, yTmp4, this.stepSize);
                if (error3 >= 1.0d) {
                    double[] y6 = y3;
                    int stages5 = stages2;
                    hNew3 = filterStep(this.stepSize * FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error3, this.exp))), forward2, false);
                    yTmp = yTmp4;
                    y0 = y03;
                    forward = forward2;
                    y = y6;
                    stages = stages5;
                    ExpandableStatefulODE expandableStatefulODE3 = equations;
                    c = 0;
                } else {
                    yTmp = yTmp4;
                    y0 = y03;
                    y = y3;
                    stages = stages2;
                    forward = forward2;
                    hNew3 = hNew6;
                    ExpandableStatefulODE expandableStatefulODE4 = equations;
                    c = 0;
                }
            }
            double error5 = error;
            double[] y7 = y;
            double[] y05 = y0;
            boolean forward4 = forward;
            double[] yTmp5 = yTmp;
            int stages6 = stages;
            interpolator2.storeTime(this.stepStart + this.stepSize);
            double[] y8 = y7;
            System.arraycopy(yTmp5, 0, y8, 0, y05.length);
            double[] yDotTmp2 = yDotTmp;
            System.arraycopy(yDotK[stages6 - 1], 0, yDotTmp2, 0, y05.length);
            this.stepStart = acceptStep(interpolator2, y8, yDotTmp2, t);
            System.arraycopy(y8, 0, yTmp5, 0, y8.length);
            if (!this.isLastStep) {
                interpolator2.storeTime(this.stepStart);
                if (this.fsal) {
                    System.arraycopy(yDotTmp2, 0, yDotK[0], 0, y05.length);
                }
                y2 = y8;
                y02 = y05;
                double factor = FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error5, this.exp)));
                double scaledH = this.stepSize * factor;
                double nextT = this.stepStart + scaledH;
                double hNew7 = filterStep(scaledH, forward4, !forward4 ? nextT <= t : nextT >= t);
                double d3 = factor;
                double factor2 = this.stepStart + hNew7;
                if (!forward4 ? factor2 <= t : factor2 >= t) {
                    double d4 = factor2;
                    filteredNextT = t - this.stepStart;
                } else {
                    filteredNextT = hNew7;
                }
            } else {
                y2 = y8;
                y02 = y05;
                filteredNextT = hNew3;
            }
            if (this.isLastStep) {
                ExpandableStatefulODE expandableStatefulODE5 = equations;
                expandableStatefulODE5.setTime(this.stepStart);
                expandableStatefulODE5.setCompleteState(y2);
                resetInternalState();
                return;
            }
            yTmp3 = yTmp5;
            yDotTmp = yDotTmp2;
            forward3 = forward4;
            firstTime = firstTime2;
            stages4 = stages6;
            y4 = y2;
            y04 = y02;
            ExpandableStatefulODE expandableStatefulODE6 = equations;
            c2 = 0;
            hNew2 = filteredNextT;
        }
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction2) {
        this.minReduction = minReduction2;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth2) {
        this.maxGrowth = maxGrowth2;
    }
}
