package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;

public class GraggBulirschStoerIntegrator extends AdaptiveStepsizeIntegrator {
    private static final String METHOD_NAME = "Gragg-Bulirsch-Stoer";
    private double[][] coeff;
    private int[] costPerStep;
    private double[] costPerTimeUnit;
    private int maxChecks;
    private int maxIter;
    private int maxOrder;
    private int mudif;
    private double[] optimalStep;
    private double orderControl1;
    private double orderControl2;
    private boolean performTest;
    private int[] sequence;
    private double stabilityReduction;
    private double stepControl1;
    private double stepControl2;
    private double stepControl3;
    private double stepControl4;
    private boolean useInterpolationError;

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setControlFactors(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public GraggBulirschStoerIntegrator(double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(METHOD_NAME, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        setStabilityCheck(true, -1, -1, -1.0d);
        setControlFactors(-1.0d, -1.0d, -1.0d, -1.0d);
        setOrderControl(-1, -1.0d, -1.0d);
        setInterpolationControl(true, -1);
    }

    public void setStabilityCheck(boolean performStabilityCheck, int maxNumIter, int maxNumChecks, double stepsizeReductionFactor) {
        this.performTest = performStabilityCheck;
        this.maxIter = maxNumIter <= 0 ? 2 : maxNumIter;
        this.maxChecks = maxNumChecks <= 0 ? 1 : maxNumChecks;
        if (stepsizeReductionFactor < 1.0E-4d || stepsizeReductionFactor > 0.9999d) {
            this.stabilityReduction = 0.5d;
        } else {
            this.stabilityReduction = stepsizeReductionFactor;
        }
    }

    public void setControlFactors(double control1, double control2, double control3, double control4) {
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.stepControl1 = 0.65d;
        } else {
            this.stepControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.stepControl2 = 0.94d;
        } else {
            this.stepControl2 = control2;
        }
        if (control3 < 1.0E-4d || control3 > 0.9999d) {
            this.stepControl3 = 0.02d;
        } else {
            this.stepControl3 = control3;
        }
        if (control4 < 1.0001d || control4 > 999.9d) {
            this.stepControl4 = 4.0d;
        } else {
            this.stepControl4 = control4;
        }
    }

    public void setOrderControl(int maximalOrder, double control1, double control2) {
        if (maximalOrder <= 6 || maximalOrder % 2 != 0) {
            this.maxOrder = 18;
        }
        if (control1 < 1.0E-4d || control1 > 0.9999d) {
            this.orderControl1 = 0.8d;
        } else {
            this.orderControl1 = control1;
        }
        if (control2 < 1.0E-4d || control2 > 0.9999d) {
            this.orderControl2 = 0.9d;
        } else {
            this.orderControl2 = control2;
        }
        initializeArrays();
    }

    public void addStepHandler(StepHandler handler) {
        super.addStepHandler(handler);
        initializeArrays();
    }

    public void addEventHandler(EventHandler function, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
        super.addEventHandler(function, maxCheckInterval, convergence, maxIterationCount, solver);
        initializeArrays();
    }

    private void initializeArrays() {
        int size = this.maxOrder / 2;
        if (this.sequence == null || this.sequence.length != size) {
            this.sequence = new int[size];
            this.costPerStep = new int[size];
            this.coeff = new double[size][];
            this.costPerTimeUnit = new double[size];
            this.optimalStep = new double[size];
        }
        for (int k = 0; k < size; k++) {
            this.sequence[k] = (k * 4) + 2;
        }
        this.costPerStep[0] = this.sequence[0] + 1;
        for (int k2 = 1; k2 < size; k2++) {
            this.costPerStep[k2] = this.costPerStep[k2 - 1] + this.sequence[k2];
        }
        int k3 = 0;
        while (k3 < size) {
            this.coeff[k3] = k3 > 0 ? new double[k3] : null;
            for (int l = 0; l < k3; l++) {
                double ratio = ((double) this.sequence[k3]) / ((double) this.sequence[(k3 - l) - 1]);
                this.coeff[k3][l] = 1.0d / ((ratio * ratio) - 1.0d);
            }
            k3++;
        }
    }

    public void setInterpolationControl(boolean useInterpolationErrorForControl, int mudifControlParameter) {
        this.useInterpolationError = useInterpolationErrorForControl;
        if (mudifControlParameter <= 0 || mudifControlParameter >= 7) {
            this.mudif = 4;
        } else {
            this.mudif = mudifControlParameter;
        }
    }

    private void rescale(double[] y1, double[] y2, double[] scale) {
        int i = 0;
        if (this.vecAbsoluteTolerance == null) {
            while (true) {
                int i2 = i;
                if (i2 < scale.length) {
                    scale[i2] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * FastMath.max(FastMath.abs(y1[i2]), FastMath.abs(y2[i2])));
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            while (true) {
                int i3 = i;
                if (i3 < scale.length) {
                    scale[i3] = this.vecAbsoluteTolerance[i3] + (this.vecRelativeTolerance[i3] * FastMath.max(FastMath.abs(y1[i3]), FastMath.abs(y2[i3])));
                    i = i3 + 1;
                } else {
                    return;
                }
            }
        }
    }

    private boolean tryStep(double t0, double[] y0, double step, int k, double[] scale, double[][] f, double[] yMiddle, double[] yEnd, double[] yTmp) throws MaxCountExceededException, DimensionMismatchException {
        double subStep;
        double subStep2;
        double[] dArr = y0;
        int i = k;
        double[] dArr2 = scale;
        double[] dArr3 = yEnd;
        int n = this.sequence[i];
        double subStep3 = step / ((double) n);
        double subStep22 = 2.0d * subStep3;
        double t = t0 + subStep3;
        for (int i2 = 0; i2 < dArr.length; i2++) {
            yTmp[i2] = dArr[i2];
            dArr3[i2] = dArr[i2] + (f[0][i2] * subStep3);
        }
        computeDerivatives(t, dArr3, f[1]);
        double t2 = t;
        int j = 1;
        while (j < n) {
            if (j * 2 == n) {
                System.arraycopy(dArr3, 0, yMiddle, 0, dArr.length);
            } else {
                double[] dArr4 = yMiddle;
            }
            double t3 = t2 + subStep3;
            int i3 = 0;
            while (true) {
                subStep = subStep3;
                if (i3 >= dArr.length) {
                    break;
                }
                double middle = dArr3[i3];
                dArr3[i3] = yTmp[i3] + (f[j][i3] * subStep22);
                yTmp[i3] = middle;
                i3++;
                subStep3 = subStep;
            }
            computeDerivatives(t3, dArr3, f[j + 1]);
            if (!this.performTest || j > this.maxChecks || i >= this.maxIter) {
                subStep2 = subStep22;
            } else {
                subStep2 = subStep22;
                double initialNorm = 0.0d;
                for (int l = 0; l < dArr2.length; l++) {
                    double ratio = f[0][l] / dArr2[l];
                    initialNorm += ratio * ratio;
                }
                double deltaNorm = 0.0d;
                for (int l2 = 0; l2 < dArr2.length; l2++) {
                    double ratio2 = (f[j + 1][l2] - f[0][l2]) / dArr2[l2];
                    deltaNorm += ratio2 * ratio2;
                }
                if (deltaNorm > FastMath.max(1.0E-15d, initialNorm) * 4.0d) {
                    return false;
                }
            }
            j++;
            t2 = t3;
            subStep3 = subStep;
            subStep22 = subStep2;
            i = k;
            dArr2 = scale;
        }
        double subStep4 = subStep3;
        double d = subStep22;
        for (int i4 = 0; i4 < dArr.length; i4++) {
            dArr3[i4] = (yTmp[i4] + dArr3[i4] + (f[n][i4] * subStep4)) * 0.5d;
        }
        return true;
    }

    private void extrapolate(int offset, int k, double[][] diag, double[] last) {
        int i = k;
        double[] dArr = last;
        int j = 1;
        while (true) {
            if (j >= i) {
                break;
            }
            for (int i2 = 0; i2 < dArr.length; i2++) {
                diag[(i - j) - 1][i2] = diag[i - j][i2] + (this.coeff[i + offset][j - 1] * (diag[i - j][i2] - diag[(i - j) - 1][i2]));
            }
            j++;
        }
        for (int i3 = 0; i3 < dArr.length; i3++) {
            dArr[i3] = diag[0][i3] + (this.coeff[i + offset][i - 1] * (diag[0][i3] - dArr[i3]));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:180:0x05c4  */
    /* JADX WARNING: Removed duplicated region for block: B:205:0x0683  */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x068e  */
    /* JADX WARNING: Removed duplicated region for block: B:208:0x06a6  */
    /* JADX WARNING: Removed duplicated region for block: B:217:0x0704  */
    /* JADX WARNING: Removed duplicated region for block: B:220:0x071c  */
    /* JADX WARNING: Removed duplicated region for block: B:223:0x0721  */
    /* JADX WARNING: Removed duplicated region for block: B:224:0x0728  */
    /* JADX WARNING: Removed duplicated region for block: B:229:0x0740 A[LOOP:3: B:24:0x0169->B:229:0x0740, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:233:0x0730 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void integrate(org.apache.commons.math3.ode.ExpandableStatefulODE r85, double r86) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r84 = this;
            r12 = r84
            r13 = r85
            r84.sanityChecks(r85, r86)
            r84.setEquations(r85)
            double r0 = r85.getTime()
            int r0 = (r86 > r0 ? 1 : (r86 == r0 ? 0 : -1))
            r10 = 1
            if (r0 <= 0) goto L_0x0015
            r0 = 1
            goto L_0x0016
        L_0x0015:
            r0 = 0
        L_0x0016:
            double[] r9 = r85.getCompleteState()
            java.lang.Object r1 = r9.clone()
            r8 = r1
            double[] r8 = (double[]) r8
            int r1 = r8.length
            double[] r7 = new double[r1]
            int r1 = r8.length
            double[] r6 = new double[r1]
            int r1 = r8.length
            double[] r5 = new double[r1]
            int r1 = r8.length
            double[] r4 = new double[r1]
            int[] r1 = r12.sequence
            int r1 = r1.length
            int r1 = r1 - r10
            double[][] r3 = new double[r1][]
            int[] r1 = r12.sequence
            int r1 = r1.length
            int r1 = r1 - r10
            double[][] r2 = new double[r1][]
            r1 = 0
        L_0x003a:
            int[] r11 = r12.sequence
            int r11 = r11.length
            int r11 = r11 - r10
            if (r1 >= r11) goto L_0x004d
            int r11 = r8.length
            double[] r11 = new double[r11]
            r3[r1] = r11
            int r11 = r8.length
            double[] r11 = new double[r11]
            r2[r1] = r11
            int r1 = r1 + 1
            goto L_0x003a
        L_0x004d:
            int[] r1 = r12.sequence
            int r1 = r1.length
            double[][][] r11 = new double[r1][][]
            r1 = 0
        L_0x0053:
            int[] r10 = r12.sequence
            int r10 = r10.length
            if (r1 >= r10) goto L_0x008d
            int[] r10 = r12.sequence
            r10 = r10[r1]
            r17 = 1
            int r10 = r10 + 1
            double[][] r10 = new double[r10][]
            r11[r1] = r10
            r10 = r11[r1]
            r16 = 0
            r10[r16] = r7
            r10 = 0
        L_0x006b:
            r18 = r2
            int[] r2 = r12.sequence
            r2 = r2[r1]
            if (r10 >= r2) goto L_0x0085
            r2 = r11[r1]
            int r19 = r10 + 1
            r20 = r3
            int r3 = r9.length
            double[] r3 = new double[r3]
            r2[r19] = r3
            int r10 = r10 + 1
            r2 = r18
            r3 = r20
            goto L_0x006b
        L_0x0085:
            r20 = r3
            int r1 = r1 + 1
            r2 = r18
            r10 = 1
            goto L_0x0053
        L_0x008d:
            r18 = r2
            r20 = r3
            if (r8 == r9) goto L_0x0098
            int r1 = r9.length
            r2 = 0
            java.lang.System.arraycopy(r9, r2, r8, r2, r1)
        L_0x0098:
            int r1 = r9.length
            double[] r10 = new double[r1]
            int[] r1 = r12.sequence
            int r1 = r1.length
            r3 = 2
            int r1 = r1 * 2
            r2 = 1
            int r1 = r1 + r2
            int r2 = r9.length
            int[] r1 = new int[]{r1, r2}
            java.lang.Class<double> r2 = double.class
            java.lang.Object r1 = java.lang.reflect.Array.newInstance(r2, r1)
            r19 = r1
            double[][] r19 = (double[][]) r19
            int r1 = r12.mainSetDimension
            double[] r2 = new double[r1]
            r12.rescale(r8, r8, r2)
            double[] r1 = r12.vecRelativeTolerance
            if (r1 != 0) goto L_0x00c2
            r21 = r4
            double r3 = r12.scalRelativeTolerance
            goto L_0x00cb
        L_0x00c2:
            r21 = r4
            double[] r1 = r12.vecRelativeTolerance
            r3 = 0
            r23 = r1[r3]
            r3 = r23
        L_0x00cb:
            r25 = r2
            r1 = 4457293557087583675(0x3ddb7cdfd9d7bdbb, double:1.0E-10)
            double r1 = org.apache.commons.math3.util.FastMath.max(r1, r3)
            double r23 = org.apache.commons.math3.util.FastMath.log10(r1)
            int[] r1 = r12.sequence
            int r1 = r1.length
            r22 = 2
            int r1 = r1 + -2
            r26 = 4603579539098121011(0x3fe3333333333333, double:0.6)
            double r26 = r26 * r23
            r28 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r30 = r3
            double r2 = r28 - r26
            double r2 = org.apache.commons.math3.util.FastMath.floor(r2)
            int r2 = (int) r2
            int r1 = org.apache.commons.math3.util.FastMath.min(r1, r2)
            r2 = 1
            int r26 = org.apache.commons.math3.util.FastMath.max(r2, r1)
            org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator r27 = new org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator
            org.apache.commons.math3.ode.EquationsMapper r32 = r85.getPrimaryMapper()
            org.apache.commons.math3.ode.EquationsMapper[] r33 = r85.getSecondaryMappers()
            r1 = r27
            r4 = r18
            r3 = r25
            r2 = r8
            r35 = r3
            r34 = r20
            r3 = r7
            r37 = r4
            r18 = r21
            r4 = r6
            r20 = r5
            r5 = r10
            r38 = r6
            r6 = r19
            r39 = r10
            r10 = r7
            r7 = r0
            r40 = r11
            r11 = r8
            r8 = r32
            r41 = r9
            r9 = r33
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            r9 = r27
            double r1 = r85.getTime()
            r9.storeTime(r1)
            double r1 = r85.getTime()
            r12.stepStart = r1
            r6 = 0
            r21 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            r8 = 0
            r25 = 1
            r27 = 1
            r32 = 0
            double r1 = r85.getTime()
            r4 = r0
            r0 = r12
            r3 = r41
            r42 = r4
            r4 = r86
            r0.initIntegration(r1, r3, r4)
            double[] r0 = r12.costPerTimeUnit
            r1 = 0
            r3 = 0
            r0[r3] = r1
            r12.isLastStep = r3
            r33 = r8
            r43 = r21
            r21 = r6
        L_0x0169:
            r45 = 0
            if (r27 == 0) goto L_0x019c
            r9.shift()
            if (r32 != 0) goto L_0x0177
            double r0 = r12.stepStart
            r12.computeDerivatives(r0, r11, r10)
        L_0x0177:
            if (r25 == 0) goto L_0x0194
            int r0 = r26 * 2
            r17 = 1
            int r2 = r0 + 1
            double r4 = r12.stepStart
            r0 = r12
            r1 = r42
            r3 = r35
            r6 = r11
            r7 = r10
            r8 = r20
            r13 = r9
            r9 = r18
            double r0 = r0.initializeStep(r1, r2, r3, r4, r6, r7, r8, r9)
            r21 = r0
            goto L_0x0197
        L_0x0194:
            r13 = r9
            r17 = 1
        L_0x0197:
            r27 = 0
            r0 = r21
            goto L_0x01a1
        L_0x019c:
            r13 = r9
            r17 = 1
            r0 = r21
        L_0x01a1:
            r12.stepSize = r0
            r9 = r42
            if (r9 == 0) goto L_0x01b0
            double r2 = r12.stepStart
            double r4 = r12.stepSize
            double r2 = r2 + r4
            int r2 = (r2 > r86 ? 1 : (r2 == r86 ? 0 : -1))
            if (r2 > 0) goto L_0x01bb
        L_0x01b0:
            if (r9 != 0) goto L_0x01c1
            double r2 = r12.stepStart
            double r4 = r12.stepSize
            double r2 = r2 + r4
            int r2 = (r2 > r86 ? 1 : (r2 == r86 ? 0 : -1))
            if (r2 >= 0) goto L_0x01c1
        L_0x01bb:
            double r2 = r12.stepStart
            double r2 = r86 - r2
            r12.stepSize = r2
        L_0x01c1:
            double r2 = r12.stepStart
            double r4 = r12.stepSize
            double r21 = r2 + r4
            if (r9 == 0) goto L_0x01d1
            int r2 = (r21 > r86 ? 1 : (r21 == r86 ? 0 : -1))
            if (r2 < 0) goto L_0x01cf
        L_0x01cd:
            r2 = 1
            goto L_0x01d6
        L_0x01cf:
            r2 = 0
            goto L_0x01d6
        L_0x01d1:
            int r2 = (r21 > r86 ? 1 : (r21 == r86 ? 0 : -1))
            if (r2 > 0) goto L_0x01cf
            goto L_0x01cd
        L_0x01d6:
            r12.isLastStep = r2
            r2 = -1
            r6 = r2
            r8 = r26
            r46 = r43
            r42 = r0
            r0 = 1
        L_0x01e1:
            r26 = r0
            if (r26 == 0) goto L_0x043b
            int r7 = r6 + 1
            double r1 = r12.stepStart
            double r4 = r12.stepSize
            r44 = r40[r7]
            if (r7 != 0) goto L_0x01f8
            r16 = 0
            r0 = r19[r16]
            r6 = r34
        L_0x01f5:
            r34 = r0
            goto L_0x0201
        L_0x01f8:
            r16 = 0
            int r0 = r7 + -1
            r6 = r34
            r0 = r6[r0]
            goto L_0x01f5
        L_0x0201:
            if (r7 != 0) goto L_0x0208
            r3 = r37
            r37 = r38
            goto L_0x0210
        L_0x0208:
            int r0 = r7 + -1
            r3 = r37
            r0 = r3[r0]
            r37 = r0
        L_0x0210:
            r0 = r12
            r14 = r3
            r3 = r11
            r15 = r6
            r6 = r7
            r48 = r13
            r13 = r7
            r7 = r35
            r49 = r15
            r15 = r8
            r8 = r44
            r50 = r15
            r15 = r9
            r9 = r34
            r51 = r10
            r52 = r39
            r10 = r37
            r53 = r11
            r54 = r14
            r16 = r40
            r14 = 0
            r11 = r20
            boolean r0 = r0.tryStep(r1, r3, r4, r6, r7, r8, r9, r10, r11)
            if (r0 != 0) goto L_0x025f
            double r0 = r12.stepSize
            double r2 = r12.stabilityReduction
            double r0 = r0 * r2
            double r0 = r12.filterStep(r0, r15, r14)
            double r42 = org.apache.commons.math3.util.FastMath.abs(r0)
            r45 = 1
            r0 = 0
            r6 = r13
            r9 = r15
            r40 = r16
            r13 = r48
            r34 = r49
            r8 = r50
            r10 = r51
            r39 = r52
            r11 = r53
            r37 = r54
        L_0x025c:
            r17 = 1
            goto L_0x01e1
        L_0x025f:
            if (r13 <= 0) goto L_0x0423
            r7 = r38
            r4 = r54
            r12.extrapolate(r14, r13, r4, r7)
            r9 = r35
            r8 = r53
            r12.rescale(r8, r7, r9)
            r0 = 0
            r1 = r0
            r0 = 0
        L_0x0273:
            int r3 = r12.mainSetDimension
            if (r0 >= r3) goto L_0x028b
            r5 = r7[r0]
            r3 = r4[r14]
            r10 = r3[r0]
            double r5 = r5 - r10
            double r5 = org.apache.commons.math3.util.FastMath.abs(r5)
            r10 = r9[r0]
            double r5 = r5 / r10
            double r10 = r5 * r5
            double r1 = r1 + r10
            int r0 = r0 + 1
            goto L_0x0273
        L_0x028b:
            int r0 = r12.mainSetDimension
            double r5 = (double) r0
            double r5 = r1 / r5
            double r0 = org.apache.commons.math3.util.FastMath.sqrt(r5)
            r2 = 4831355200913801216(0x430c6bf526340000, double:1.0E15)
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x03f3
            r10 = 1
            if (r13 <= r10) goto L_0x02ad
            int r2 = (r0 > r46 ? 1 : (r0 == r46 ? 0 : -1))
            if (r2 <= 0) goto L_0x02ad
            r58 = r8
            r59 = r9
            r11 = r15
            r8 = r50
            goto L_0x03fa
        L_0x02ad:
            r2 = 4616189618054758400(0x4010000000000000, double:4.0)
            double r2 = r2 * r0
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r46 = org.apache.commons.math3.util.FastMath.max(r2, r5)
            int r2 = r13 * 2
            int r2 = r2 + r10
            double r2 = (double) r2
            double r2 = r5 / r2
            double r10 = r12.stepControl2
            r55 = r15
            double r14 = r12.stepControl1
            double r14 = r0 / r14
            double r14 = org.apache.commons.math3.util.FastMath.pow(r14, r2)
            double r10 = r10 / r14
            double r14 = r12.stepControl3
            double r14 = org.apache.commons.math3.util.FastMath.pow(r14, r2)
            r56 = r2
            double r2 = r12.stepControl4
            double r2 = r14 / r2
            r58 = r8
            r59 = r9
            double r8 = r5 / r14
            double r8 = org.apache.commons.math3.util.FastMath.min(r8, r10)
            double r2 = org.apache.commons.math3.util.FastMath.max(r2, r8)
            double[] r8 = r12.optimalStep
            double r9 = r12.stepSize
            double r9 = r9 * r2
            r11 = r55
            r5 = 1
            double r9 = r12.filterStep(r9, r11, r5)
            double r5 = org.apache.commons.math3.util.FastMath.abs(r9)
            r8[r13] = r5
            double[] r5 = r12.costPerTimeUnit
            int[] r6 = r12.costPerStep
            r6 = r6[r13]
            double r8 = (double) r6
            double[] r6 = r12.optimalStep
            r34 = r6[r13]
            double r8 = r8 / r34
            r5[r13] = r8
            int r5 = r13 - r50
            switch(r5) {
                case -1: goto L_0x038f;
                case 0: goto L_0x0349;
                case 1: goto L_0x0317;
                default: goto L_0x030b;
            }
        L_0x030b:
            r60 = r2
            r8 = r50
            if (r25 != 0) goto L_0x03e8
            boolean r2 = r12.isLastStep
            if (r2 == 0) goto L_0x03f0
            goto L_0x03e8
        L_0x0317:
            r5 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r5 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x0342
            r45 = 1
            r8 = r50
            r5 = 1
            if (r8 <= r5) goto L_0x033b
            double[] r5 = r12.costPerTimeUnit
            int r6 = r8 + -1
            r9 = r5[r6]
            double r5 = r12.orderControl1
            r60 = r2
            double[] r2 = r12.costPerTimeUnit
            r34 = r2[r8]
            double r5 = r5 * r34
            int r2 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r2 >= 0) goto L_0x033d
            int r8 = r8 + -1
            goto L_0x033d
        L_0x033b:
            r60 = r2
        L_0x033d:
            double[] r2 = r12.optimalStep
            r42 = r2[r8]
            goto L_0x0346
        L_0x0342:
            r60 = r2
            r8 = r50
        L_0x0346:
            r2 = 0
            goto L_0x03f2
        L_0x0349:
            r60 = r2
            r8 = r50
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x0356
            r2 = 0
        L_0x0354:
            goto L_0x03f2
        L_0x0356:
            int[] r2 = r12.sequence
            int r3 = r13 + 1
            r2 = r2[r3]
            double r2 = (double) r2
            int[] r5 = r12.sequence
            r6 = 0
            r5 = r5[r6]
            double r5 = (double) r5
            double r2 = r2 / r5
            double r5 = r2 * r2
            int r5 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x038e
            r45 = 1
            r26 = 0
            r5 = 1
            if (r8 <= r5) goto L_0x0388
            double[] r5 = r12.costPerTimeUnit
            int r6 = r8 + -1
            r9 = r5[r6]
            double r5 = r12.orderControl1
            r62 = r2
            double[] r2 = r12.costPerTimeUnit
            r34 = r2[r8]
            double r5 = r5 * r34
            int r2 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r2 >= 0) goto L_0x038a
            int r8 = r8 + -1
            goto L_0x038a
        L_0x0388:
            r62 = r2
        L_0x038a:
            double[] r2 = r12.optimalStep
            r42 = r2[r8]
        L_0x038e:
            goto L_0x03f0
        L_0x038f:
            r60 = r2
            r8 = r50
            r2 = 1
            if (r8 <= r2) goto L_0x03f0
            if (r33 != 0) goto L_0x03f0
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x03a0
            r2 = 0
            goto L_0x0354
        L_0x03a0:
            int[] r2 = r12.sequence
            r2 = r2[r8]
            double r2 = (double) r2
            int[] r5 = r12.sequence
            int r6 = r8 + 1
            r5 = r5[r6]
            double r5 = (double) r5
            double r2 = r2 * r5
            int[] r5 = r12.sequence
            r6 = 0
            r5 = r5[r6]
            int[] r9 = r12.sequence
            r9 = r9[r6]
            int r5 = r5 * r9
            double r5 = (double) r5
            double r2 = r2 / r5
            double r5 = r2 * r2
            int r5 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x03e7
            r45 = 1
            r26 = 0
            r5 = r13
            r6 = 1
            if (r5 <= r6) goto L_0x03e0
            double[] r6 = r12.costPerTimeUnit
            int r8 = r5 + -1
            r8 = r6[r8]
            r64 = r2
            double r2 = r12.orderControl1
            double[] r6 = r12.costPerTimeUnit
            r34 = r6[r5]
            double r2 = r2 * r34
            int r2 = (r8 > r2 ? 1 : (r8 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x03e2
            int r5 = r5 + -1
            goto L_0x03e2
        L_0x03e0:
            r64 = r2
        L_0x03e2:
            r8 = r5
            double[] r2 = r12.optimalStep
            r42 = r2[r8]
        L_0x03e7:
            goto L_0x03f0
        L_0x03e8:
            r2 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x03f0
            r2 = 0
            goto L_0x03f2
        L_0x03f0:
            r2 = r26
        L_0x03f2:
            goto L_0x040c
        L_0x03f3:
            r58 = r8
            r59 = r9
            r11 = r15
            r8 = r50
        L_0x03fa:
            double r2 = r12.stepSize
            double r5 = r12.stabilityReduction
            double r2 = r2 * r5
            r5 = 0
            double r2 = r12.filterStep(r2, r11, r5)
            double r42 = org.apache.commons.math3.util.FastMath.abs(r2)
            r45 = 1
            r2 = 0
        L_0x040c:
            r0 = r2
            r37 = r4
            r38 = r7
            r9 = r11
            r6 = r13
            r40 = r16
            r13 = r48
            r34 = r49
            r10 = r51
            r39 = r52
            r11 = r58
            r35 = r59
            goto L_0x025c
        L_0x0423:
            r8 = r50
            r6 = r13
            r9 = r15
            r40 = r16
            r0 = r26
            r13 = r48
            r34 = r49
            r10 = r51
            r39 = r52
            r11 = r53
            r37 = r54
            r17 = 1
            goto L_0x01e1
        L_0x043b:
            r51 = r10
            r58 = r11
            r48 = r13
            r49 = r34
            r59 = r35
            r4 = r37
            r7 = r38
            r52 = r39
            r16 = r40
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r11 = r9
            if (r45 != 0) goto L_0x045d
            double r2 = r12.stepStart
            double r9 = r12.stepSize
            double r2 = r2 + r9
            r9 = r52
            r12.computeDerivatives(r2, r7, r9)
            goto L_0x045f
        L_0x045d:
            r9 = r52
        L_0x045f:
            double r2 = r84.getMaxStep()
            if (r45 != 0) goto L_0x05b2
            r5 = 1
        L_0x0466:
            if (r5 > r6) goto L_0x0473
            r10 = 0
            r13 = r19[r10]
            r14 = r49
            r12.extrapolate(r10, r5, r14, r13)
            int r5 = r5 + 1
            goto L_0x0466
        L_0x0473:
            r14 = r49
            int r5 = r6 * 2
            int r10 = r12.mudif
            int r5 = r5 - r10
            int r5 = r5 + 3
            r10 = 0
        L_0x047d:
            if (r10 >= r5) goto L_0x0563
            int r13 = r10 / 2
            int[] r15 = r12.sequence
            r15 = r15[r13]
            double r0 = (double) r15
            double r0 = r0 * r28
            double r0 = org.apache.commons.math3.util.FastMath.pow(r0, r10)
            r15 = r16[r13]
            int r15 = r15.length
            r66 = r11
            r11 = 2
            int r15 = r15 / r11
            r17 = 0
        L_0x0495:
            r67 = r17
            r68 = r2
            r11 = r41
            int r2 = r11.length
            r3 = r67
            if (r3 >= r2) goto L_0x04b8
            int r2 = r10 + 1
            r2 = r19[r2]
            r17 = r16[r13]
            int r26 = r15 + r10
            r17 = r17[r26]
            r34 = r17[r3]
            double r34 = r34 * r0
            r2[r3] = r34
            int r17 = r3 + 1
            r41 = r11
            r2 = r68
            r11 = 2
            goto L_0x0495
        L_0x04b8:
            r1 = r0
            r0 = 1
        L_0x04ba:
            int r3 = r6 - r13
            if (r0 > r3) goto L_0x04ff
            int[] r3 = r12.sequence
            int r17 = r0 + r13
            r3 = r3[r17]
            r70 = r1
            double r1 = (double) r3
            double r1 = r1 * r28
            double r1 = org.apache.commons.math3.util.FastMath.pow(r1, r10)
            int r3 = r13 + r0
            r3 = r16[r3]
            int r3 = r3.length
            r17 = 2
            int r15 = r3 / 2
            r3 = 0
        L_0x04d7:
            r72 = r4
            int r4 = r11.length
            if (r3 >= r4) goto L_0x04f3
            int r4 = r0 + -1
            r4 = r14[r4]
            int r17 = r13 + r0
            r17 = r16[r17]
            int r26 = r15 + r10
            r17 = r17[r26]
            r34 = r17[r3]
            double r34 = r34 * r1
            r4[r3] = r34
            int r3 = r3 + 1
            r4 = r72
            goto L_0x04d7
        L_0x04f3:
            int r3 = r10 + 1
            r3 = r19[r3]
            r12.extrapolate(r13, r0, r14, r3)
            int r0 = r0 + 1
            r4 = r72
            goto L_0x04ba
        L_0x04ff:
            r70 = r1
            r72 = r4
            r0 = 0
        L_0x0504:
            int r1 = r11.length
            if (r0 >= r1) goto L_0x051e
            int r1 = r10 + 1
            r1 = r19[r1]
            r2 = r1[r0]
            r74 = r13
            r73 = r14
            double r13 = r12.stepSize
            double r2 = r2 * r13
            r1[r0] = r2
            int r0 = r0 + 1
            r14 = r73
            r13 = r74
            goto L_0x0504
        L_0x051e:
            r74 = r13
            r73 = r14
            int r0 = r10 + 1
            r1 = 2
            int r0 = r0 / r1
        L_0x0526:
            if (r0 > r6) goto L_0x0553
            r1 = r16[r0]
            int r1 = r1.length
            r2 = 1
            int r1 = r1 - r2
        L_0x052d:
            int r2 = r10 + 1
            r3 = 2
            int r2 = r2 * 2
            if (r1 < r2) goto L_0x0550
            r2 = 0
        L_0x0535:
            int r3 = r11.length
            if (r2 >= r3) goto L_0x054d
            r3 = r16[r0]
            r3 = r3[r1]
            r13 = r3[r2]
            r4 = r16[r0]
            int r17 = r1 + -2
            r4 = r4[r17]
            r34 = r4[r2]
            double r13 = r13 - r34
            r3[r2] = r13
            int r2 = r2 + 1
            goto L_0x0535
        L_0x054d:
            int r1 = r1 + -1
            goto L_0x052d
        L_0x0550:
            int r0 = r0 + 1
            goto L_0x0526
        L_0x0553:
            int r10 = r10 + 1
            r41 = r11
            r11 = r66
            r2 = r68
            r4 = r72
            r14 = r73
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            goto L_0x047d
        L_0x0563:
            r68 = r2
            r72 = r4
            r66 = r11
            r73 = r14
            r11 = r41
            if (r5 < 0) goto L_0x05ad
            r0 = r48
            org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator r0 = (org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerStepInterpolator) r0
            double r1 = r12.stepSize
            r0.computeCoefficients(r5, r1)
            boolean r1 = r12.useInterpolationError
            if (r1 == 0) goto L_0x05ad
            r10 = r59
            double r1 = r0.estimateError(r10)
            double r3 = r12.stepSize
            int r13 = r5 + 4
            double r13 = (double) r13
            r34 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r13 = r34 / r13
            double r13 = org.apache.commons.math3.util.FastMath.pow(r1, r13)
            r76 = r5
            r75 = r6
            r5 = 4576918229304087675(0x3f847ae147ae147b, double:0.01)
            double r5 = org.apache.commons.math3.util.FastMath.max(r13, r5)
            double r3 = r3 / r5
            double r3 = org.apache.commons.math3.util.FastMath.abs(r3)
            r5 = 4621819117588971520(0x4024000000000000, double:10.0)
            int r5 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x05ab
            r42 = r3
            r45 = 1
        L_0x05ab:
            r13 = r3
            goto L_0x05c2
        L_0x05ad:
            r75 = r6
            r10 = r59
            goto L_0x05c0
        L_0x05b2:
            r68 = r2
            r72 = r4
            r75 = r6
            r66 = r11
            r11 = r41
            r73 = r49
            r10 = r59
        L_0x05c0:
            r13 = r68
        L_0x05c2:
            if (r45 != 0) goto L_0x0704
            double r0 = r12.stepStart
            double r2 = r12.stepSize
            double r0 = r0 + r2
            r6 = r48
            r6.storeTime(r0)
            r0 = r12
            r1 = r6
            r2 = r7
            r3 = r9
            r15 = r72
            r4 = r86
            double r0 = r0.acceptStep(r1, r2, r3, r4)
            r12.stepStart = r0
            double r0 = r12.stepStart
            r6.storeTime(r0)
            int r0 = r11.length
            r1 = r58
            r2 = 0
            java.lang.System.arraycopy(r7, r2, r1, r2, r0)
            int r0 = r11.length
            r3 = r51
            java.lang.System.arraycopy(r9, r2, r3, r2, r0)
            r0 = 1
            r2 = r75
            r4 = 1
            if (r2 != r4) goto L_0x0603
            r5 = 2
            if (r33 == 0) goto L_0x05f8
            r5 = 1
        L_0x05f8:
            r78 = r0
            r77 = r3
        L_0x05fc:
            r79 = r6
            r82 = r7
        L_0x0600:
            r3 = 2
            goto L_0x068c
        L_0x0603:
            if (r2 > r8) goto L_0x0648
            r5 = r2
            double[] r4 = r12.costPerTimeUnit
            int r17 = r2 + -1
            r34 = r4[r17]
            r77 = r3
            double r3 = r12.orderControl1
            r78 = r0
            double[] r0 = r12.costPerTimeUnit
            r36 = r0[r2]
            double r3 = r3 * r36
            int r0 = (r34 > r3 ? 1 : (r34 == r3 ? 0 : -1))
            if (r0 >= 0) goto L_0x061f
            int r5 = r2 + -1
            goto L_0x05fc
        L_0x061f:
            double[] r0 = r12.costPerTimeUnit
            r3 = r0[r2]
            r80 = r5
            r79 = r6
            double r5 = r12.orderControl2
            double[] r0 = r12.costPerTimeUnit
            int r17 = r2 + -1
            r34 = r0[r17]
            double r5 = r5 * r34
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 >= 0) goto L_0x0643
            int r6 = r2 + 1
            int[] r0 = r12.sequence
            int r0 = r0.length
            r3 = 2
            int r0 = r0 - r3
            int r5 = org.apache.commons.math3.util.FastMath.min(r6, r0)
            r82 = r7
            goto L_0x068c
        L_0x0643:
            r82 = r7
            r5 = r80
            goto L_0x0600
        L_0x0648:
            r78 = r0
            r77 = r3
            r79 = r6
            r3 = 2
            int r6 = r2 + -1
            if (r2 <= r3) goto L_0x066d
            double[] r0 = r12.costPerTimeUnit
            int r3 = r2 + -2
            r3 = r0[r3]
            r81 = r6
            double r5 = r12.orderControl1
            double[] r0 = r12.costPerTimeUnit
            int r17 = r2 + -1
            r34 = r0[r17]
            double r5 = r5 * r34
            int r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r0 >= 0) goto L_0x066f
            int r6 = r2 + -2
            r5 = r6
            goto L_0x0671
        L_0x066d:
            r81 = r6
        L_0x066f:
            r5 = r81
        L_0x0671:
            double[] r0 = r12.costPerTimeUnit
            r3 = r0[r2]
            r82 = r7
            double r6 = r12.orderControl2
            double[] r0 = r12.costPerTimeUnit
            r34 = r0[r5]
            double r6 = r6 * r34
            int r0 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x0600
            int[] r0 = r12.sequence
            int r0 = r0.length
            r3 = 2
            int r0 = r0 - r3
            int r5 = org.apache.commons.math3.util.FastMath.min(r2, r0)
        L_0x068c:
            if (r33 == 0) goto L_0x06a6
            int r0 = org.apache.commons.math3.util.FastMath.min(r5, r2)
            double r6 = r12.stepSize
            double r6 = org.apache.commons.math3.util.FastMath.abs(r6)
            double[] r4 = r12.optimalStep
            r3 = r4[r0]
            double r3 = org.apache.commons.math3.util.FastMath.min(r6, r3)
            r6 = r0
            r42 = r3
            r0 = r66
            goto L_0x06fa
        L_0x06a6:
            if (r5 > r2) goto L_0x06af
            double[] r0 = r12.optimalStep
            r3 = r0[r5]
            r0 = r66
            goto L_0x06f7
        L_0x06af:
            if (r2 >= r8) goto L_0x06de
            double[] r0 = r12.costPerTimeUnit
            r3 = r0[r2]
            double r6 = r12.orderControl2
            double[] r0 = r12.costPerTimeUnit
            int r17 = r2 + -1
            r34 = r0[r17]
            double r6 = r6 * r34
            int r0 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
            if (r0 >= 0) goto L_0x06de
            double[] r0 = r12.optimalStep
            r3 = r0[r2]
            int[] r0 = r12.costPerStep
            int r6 = r5 + 1
            r0 = r0[r6]
            double r6 = (double) r0
            double r3 = r3 * r6
            int[] r0 = r12.costPerStep
            r0 = r0[r2]
            double r6 = (double) r0
            double r3 = r3 / r6
            r0 = r66
            r6 = 0
            double r3 = r12.filterStep(r3, r0, r6)
            goto L_0x06f7
        L_0x06de:
            r0 = r66
            double[] r3 = r12.optimalStep
            r6 = r3[r2]
            int[] r3 = r12.costPerStep
            r3 = r3[r5]
            double r3 = (double) r3
            double r6 = r6 * r3
            int[] r3 = r12.costPerStep
            r3 = r3[r2]
            double r3 = (double) r3
            double r6 = r6 / r3
            r3 = 0
            double r6 = r12.filterStep(r6, r0, r3)
            r3 = r6
        L_0x06f7:
            r6 = r5
            r42 = r3
        L_0x06fa:
            r3 = 1
            r27 = r3
            r26 = r6
            r3 = r42
            r32 = r78
            goto L_0x0716
        L_0x0704:
            r82 = r7
            r79 = r48
            r77 = r51
            r1 = r58
            r0 = r66
            r15 = r72
            r2 = r75
            r26 = r8
            r3 = r42
        L_0x0716:
            double r3 = org.apache.commons.math3.util.FastMath.min(r3, r13)
            if (r0 != 0) goto L_0x071d
            double r3 = -r3
        L_0x071d:
            r25 = 0
            if (r45 == 0) goto L_0x0728
            r5 = 0
            r12.isLastStep = r5
            r6 = 1
            r33 = r6
            goto L_0x072c
        L_0x0728:
            r5 = 0
            r2 = 0
            r33 = r2
        L_0x072c:
            boolean r2 = r12.isLastStep
            if (r2 == 0) goto L_0x0740
            double r5 = r12.stepStart
            r7 = r79
            r2 = r85
            r2.setTime(r5)
            r2.setCompleteState(r1)
            r84.resetInternalState()
            return
        L_0x0740:
            r42 = r0
            r21 = r3
            r39 = r9
            r35 = r10
            r41 = r11
            r37 = r15
            r40 = r16
            r43 = r46
            r34 = r73
            r10 = r77
            r9 = r79
            r38 = r82
            r13 = r85
            r11 = r1
            goto L_0x0169
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator.integrate(org.apache.commons.math3.ode.ExpandableStatefulODE, double):void");
    }
}
