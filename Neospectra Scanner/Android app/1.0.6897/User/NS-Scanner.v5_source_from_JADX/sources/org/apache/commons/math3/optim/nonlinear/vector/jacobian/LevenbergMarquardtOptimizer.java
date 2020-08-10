package org.apache.commons.math3.optim.nonlinear.vector.jacobian;

import java.util.Arrays;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

@Deprecated
public class LevenbergMarquardtOptimizer extends AbstractLeastSquaresOptimizer {
    private static final double TWO_EPS = (Precision.EPSILON * 2.0d);
    private double[] beta;
    private final double costRelativeTolerance;
    private double[] diagR;
    private final double initialStepBoundFactor;
    private double[] jacNorm;
    private double[] lmDir;
    private double lmPar;
    private final double orthoTolerance;
    private final double parRelativeTolerance;
    private int[] permutation;
    private final double qrRankingThreshold;
    private int rank;
    private int solvedCols;
    private double[][] weightedJacobian;
    private double[] weightedResidual;

    public LevenbergMarquardtOptimizer() {
        this(100.0d, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        this(100.0d, checker, 1.0E-10d, 1.0E-10d, 1.0E-10d, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor2, ConvergenceChecker<PointVectorValuePair> checker, double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2, double threshold) {
        super(checker);
        this.initialStepBoundFactor = initialStepBoundFactor2;
        this.costRelativeTolerance = costRelativeTolerance2;
        this.parRelativeTolerance = parRelativeTolerance2;
        this.orthoTolerance = orthoTolerance2;
        this.qrRankingThreshold = threshold;
    }

    public LevenbergMarquardtOptimizer(double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2) {
        this(100.0d, costRelativeTolerance2, parRelativeTolerance2, orthoTolerance2, Precision.SAFE_MIN);
    }

    public LevenbergMarquardtOptimizer(double initialStepBoundFactor2, double costRelativeTolerance2, double parRelativeTolerance2, double orthoTolerance2, double threshold) {
        super(null);
        this.initialStepBoundFactor = initialStepBoundFactor2;
        this.costRelativeTolerance = costRelativeTolerance2;
        this.parRelativeTolerance = parRelativeTolerance2;
        this.orthoTolerance = orthoTolerance2;
        this.qrRankingThreshold = threshold;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0337  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x03dc  */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x03ca A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02f7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.optim.PointVectorValuePair doOptimize() {
        /*
            r73 = this;
            r8 = r73
            r73.checkParameters()
            double[] r0 = r73.getTarget()
            int r9 = r0.length
            double[] r10 = r73.getStartPoint()
            int r11 = r10.length
            int r0 = org.apache.commons.math3.util.FastMath.min(r9, r11)
            r8.solvedCols = r0
            double[] r0 = new double[r11]
            r8.diagR = r0
            double[] r0 = new double[r11]
            r8.jacNorm = r0
            double[] r0 = new double[r11]
            r8.beta = r0
            int[] r0 = new int[r11]
            r8.permutation = r0
            double[] r0 = new double[r11]
            r8.lmDir = r0
            r0 = 0
            r2 = 0
            double[] r12 = new double[r11]
            double[] r13 = new double[r11]
            double[] r4 = new double[r9]
            double[] r5 = new double[r9]
            double[] r14 = new double[r9]
            double[] r15 = new double[r11]
            double[] r7 = new double[r11]
            double[] r6 = new double[r11]
            r16 = r6
            org.apache.commons.math3.linear.RealMatrix r6 = r73.getWeightSquareRoot()
            r17 = r0
            double[] r0 = r8.computeObjectiveValue(r10)
            double[] r1 = r8.computeResiduals(r0)
            r19 = r2
            org.apache.commons.math3.optim.PointVectorValuePair r2 = new org.apache.commons.math3.optim.PointVectorValuePair
            r2.<init>(r10, r0)
            double r21 = r8.computeCost(r1)
            r23 = r4
            r24 = r5
            r4 = 0
            r8.lmPar = r4
            r3 = 1
            org.apache.commons.math3.optim.ConvergenceChecker r25 = r73.getConvergenceChecker()
            r26 = r21
        L_0x0067:
            r28 = r25
            r73.incrementIterationCount()
            r29 = r2
            org.apache.commons.math3.linear.RealMatrix r4 = r8.computeWeightedJacobian(r10)
            r8.qrDecomposition(r4)
            double[] r4 = r6.operate(r1)
            r8.weightedResidual = r4
            r21 = 0
            r4 = 0
        L_0x007e:
            if (r4 >= r9) goto L_0x0089
            double[] r5 = r8.weightedResidual
            r32 = r5[r4]
            r14[r4] = r32
            int r4 = r4 + 1
            goto L_0x007e
        L_0x0089:
            r8.qTy(r14)
            r4 = 0
        L_0x008d:
            int r5 = r8.solvedCols
            if (r4 >= r5) goto L_0x00aa
            int[] r5 = r8.permutation
            r5 = r5[r4]
            r34 = r0
            double[][] r0 = r8.weightedJacobian
            r0 = r0[r4]
            r35 = r1
            double[] r1 = r8.diagR
            r32 = r1[r5]
            r0[r5] = r32
            int r4 = r4 + 1
            r0 = r34
            r1 = r35
            goto L_0x008d
        L_0x00aa:
            r34 = r0
            r35 = r1
            if (r3 == 0) goto L_0x00e2
            r0 = 0
            r4 = r0
            r0 = 0
        L_0x00b4:
            if (r0 >= r11) goto L_0x00cf
            double[] r1 = r8.jacNorm
            r19 = r1[r0]
            r30 = 0
            int r1 = (r19 > r30 ? 1 : (r19 == r30 ? 0 : -1))
            if (r1 != 0) goto L_0x00c2
            r19 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x00c2:
            r32 = r10[r0]
            double r32 = r32 * r19
            double r36 = r32 * r32
            double r4 = r4 + r36
            r12[r0] = r19
            int r0 = r0 + 1
            goto L_0x00b4
        L_0x00cf:
            double r19 = org.apache.commons.math3.util.FastMath.sqrt(r4)
            r0 = 0
            int r4 = (r19 > r0 ? 1 : (r19 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x00dc
            double r0 = r8.initialStepBoundFactor
            goto L_0x00e0
        L_0x00dc:
            double r0 = r8.initialStepBoundFactor
            double r0 = r0 * r19
        L_0x00e0:
            r17 = r0
        L_0x00e2:
            r0 = 0
            r4 = r26
            r25 = 0
            int r22 = (r4 > r25 ? 1 : (r4 == r25 ? 0 : -1))
            if (r22 == 0) goto L_0x0152
            r39 = r6
            r38 = r7
            r6 = r0
            r0 = 0
        L_0x00f2:
            int r1 = r8.solvedCols
            if (r0 >= r1) goto L_0x0146
            int[] r1 = r8.permutation
            r1 = r1[r0]
            r40 = r3
            double[] r3 = r8.jacNorm
            r25 = r3[r1]
            r30 = 0
            int r3 = (r25 > r30 ? 1 : (r25 == r30 ? 0 : -1))
            if (r3 == 0) goto L_0x0135
            r32 = 0
            r41 = r9
            r42 = r10
            r9 = r32
            r3 = 0
        L_0x010f:
            if (r3 > r0) goto L_0x0124
            r43 = r15
            double[][] r15 = r8.weightedJacobian
            r15 = r15[r3]
            r32 = r15[r1]
            r36 = r14[r3]
            double r32 = r32 * r36
            double r9 = r9 + r32
            int r3 = r3 + 1
            r15 = r43
            goto L_0x010f
        L_0x0124:
            r43 = r15
            double r32 = org.apache.commons.math3.util.FastMath.abs(r9)
            double r36 = r25 * r4
            r44 = r9
            double r9 = r32 / r36
            double r6 = org.apache.commons.math3.util.FastMath.max(r6, r9)
            goto L_0x013b
        L_0x0135:
            r41 = r9
            r42 = r10
            r43 = r15
        L_0x013b:
            int r0 = r0 + 1
            r3 = r40
            r9 = r41
            r10 = r42
            r15 = r43
            goto L_0x00f2
        L_0x0146:
            r40 = r3
            r41 = r9
            r42 = r10
            r43 = r15
            r30 = 0
            r9 = r6
            goto L_0x0161
        L_0x0152:
            r40 = r3
            r39 = r6
            r38 = r7
            r41 = r9
            r42 = r10
            r43 = r15
            r30 = 0
            r9 = r0
        L_0x0161:
            double r0 = r8.orthoTolerance
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x016b
            r8.setCost(r4)
            return r2
        L_0x016b:
            r0 = 0
        L_0x016c:
            if (r0 >= r11) goto L_0x0181
            r6 = r12[r0]
            double[] r1 = r8.jacNorm
            r46 = r2
            r2 = r1[r0]
            double r1 = org.apache.commons.math3.util.FastMath.max(r6, r2)
            r12[r0] = r1
            int r0 = r0 + 1
            r2 = r46
            goto L_0x016c
        L_0x0181:
            r46 = r2
            r26 = r4
            r6 = r17
            r2 = r23
            r0 = r30
        L_0x018b:
            r17 = r0
            r22 = 4547007122018943789(0x3f1a36e2eb1c432d, double:1.0E-4)
            int r0 = (r17 > r22 ? 1 : (r17 == r22 ? 0 : -1))
            if (r0 >= 0) goto L_0x0411
            r0 = 0
        L_0x0197:
            int r1 = r8.solvedCols
            if (r0 >= r1) goto L_0x01a6
            int[] r1 = r8.permutation
            r1 = r1[r0]
            r3 = r42[r1]
            r13[r1] = r3
            int r0 = r0 + 1
            goto L_0x0197
        L_0x01a6:
            r32 = r26
            double[] r0 = r8.weightedResidual
            r8.weightedResidual = r2
            r15 = r0
            r25 = r34
            r34 = r24
            r24 = r25
            r0 = r8
            r1 = r14
            r2 = r6
            r4 = r12
            r5 = r43
            r47 = r9
            r36 = r39
            r9 = r6
            r6 = r38
            r37 = r38
            r7 = r16
            r0.determineLMParameter(r1, r2, r4, r5, r6, r7)
            r0 = 0
            r1 = r0
            r0 = 0
        L_0x01cb:
            int r3 = r8.solvedCols
            if (r0 >= r3) goto L_0x01f4
            int[] r3 = r8.permutation
            r3 = r3[r0]
            double[] r4 = r8.lmDir
            double[] r5 = r8.lmDir
            r6 = r5[r3]
            double r5 = -r6
            r4[r3] = r5
            r4 = r13[r3]
            double[] r6 = r8.lmDir
            r38 = r6[r3]
            double r4 = r4 + r38
            r42[r3] = r4
            r4 = r12[r3]
            double[] r6 = r8.lmDir
            r38 = r6[r3]
            double r4 = r4 * r38
            double r6 = r4 * r4
            double r1 = r1 + r6
            int r0 = r0 + 1
            goto L_0x01cb
        L_0x01f4:
            double r0 = org.apache.commons.math3.util.FastMath.sqrt(r1)
            if (r40 == 0) goto L_0x01ff
            double r6 = org.apache.commons.math3.util.FastMath.min(r9, r0)
            goto L_0x0200
        L_0x01ff:
            r6 = r9
        L_0x0200:
            r3 = r42
            double[] r2 = r8.computeObjectiveValue(r3)
            double[] r4 = r8.computeResiduals(r2)
            org.apache.commons.math3.optim.PointVectorValuePair r5 = new org.apache.commons.math3.optim.PointVectorValuePair
            r5.<init>(r3, r2)
            double r9 = r8.computeCost(r4)
            r26 = -4616189618054758400(0xbff0000000000000, double:-1.0)
            r34 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            double r38 = r9 * r34
            int r38 = (r38 > r32 ? 1 : (r38 == r32 ? 0 : -1))
            if (r38 >= 0) goto L_0x0228
            double r38 = r9 / r32
            r44 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            double r49 = r38 * r38
            double r26 = r44 - r49
        L_0x0228:
            r51 = r14
            r52 = r15
            r14 = r26
            r26 = 0
        L_0x0230:
            r53 = r26
            r54 = r4
            int r4 = r8.solvedCols
            r55 = r2
            r2 = r53
            if (r2 >= r4) goto L_0x026b
            int[] r4 = r8.permutation
            r4 = r4[r2]
            r56 = r13
            double[] r13 = r8.lmDir
            r26 = r13[r4]
            r43[r2] = r30
            r13 = 0
        L_0x0249:
            if (r13 > r2) goto L_0x0260
            r38 = r43[r13]
            r57 = r5
            double[][] r5 = r8.weightedJacobian
            r5 = r5[r13]
            r44 = r5[r4]
            double r44 = r44 * r26
            double r38 = r38 + r44
            r43[r13] = r38
            int r13 = r13 + 1
            r5 = r57
            goto L_0x0249
        L_0x0260:
            r57 = r5
            int r26 = r2 + 1
            r4 = r54
            r2 = r55
            r13 = r56
            goto L_0x0230
        L_0x026b:
            r57 = r5
            r56 = r13
            r4 = 0
            r2 = 0
        L_0x0272:
            int r13 = r8.solvedCols
            if (r2 >= r13) goto L_0x0281
            r26 = r43[r2]
            r38 = r43[r2]
            double r26 = r26 * r38
            double r4 = r4 + r26
            int r2 = r2 + 1
            goto L_0x0272
        L_0x0281:
            double r26 = r32 * r32
            double r4 = r4 / r26
            r58 = r3
            double r2 = r8.lmPar
            double r2 = r2 * r0
            double r2 = r2 * r0
            double r2 = r2 / r26
            r38 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r44 = r2 * r38
            double r44 = r4 + r44
            r59 = r12
            double r12 = r4 + r2
            double r12 = -r12
            int r42 = (r44 > r30 ? 1 : (r44 == r30 ? 0 : -1))
            if (r42 != 0) goto L_0x02a1
            r49 = r30
            goto L_0x02a3
        L_0x02a1:
            double r49 = r14 / r44
        L_0x02a3:
            r17 = r49
            r49 = 4598175219545276416(0x3fd0000000000000, double:0.25)
            int r42 = (r17 > r49 ? 1 : (r17 == r49 ? 0 : -1))
            r49 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            if (r42 > 0) goto L_0x02dc
            int r42 = (r14 > r30 ? 1 : (r14 == r30 ? 0 : -1))
            if (r42 >= 0) goto L_0x02ba
            double r60 = r12 * r49
            double r49 = r49 * r14
            double r49 = r12 + r49
            double r49 = r60 / r49
        L_0x02ba:
            double r60 = r9 * r34
            int r42 = (r60 > r32 ? 1 : (r60 == r32 ? 0 : -1))
            if (r42 >= 0) goto L_0x02c4
            int r34 = (r49 > r34 ? 1 : (r49 == r34 ? 0 : -1))
            if (r34 >= 0) goto L_0x02c9
        L_0x02c4:
            r49 = 4591870180066957722(0x3fb999999999999a, double:0.1)
        L_0x02c9:
            r34 = 4621819117588971520(0x4024000000000000, double:10.0)
            r62 = r2
            double r2 = r0 * r34
            double r2 = org.apache.commons.math3.util.FastMath.min(r6, r2)
            double r2 = r2 * r49
            double r6 = r8.lmPar
            double r6 = r6 / r49
            r8.lmPar = r6
            goto L_0x02f2
        L_0x02dc:
            r62 = r2
            double r2 = r8.lmPar
            int r2 = (r2 > r30 ? 1 : (r2 == r30 ? 0 : -1))
            if (r2 == 0) goto L_0x02ea
            r2 = 4604930618986332160(0x3fe8000000000000, double:0.75)
            int r2 = (r17 > r2 ? 1 : (r17 == r2 ? 0 : -1))
            if (r2 < 0) goto L_0x02f3
        L_0x02ea:
            double r2 = r0 * r38
            double r6 = r8.lmPar
            double r6 = r6 * r49
            r8.lmPar = r6
        L_0x02f2:
            r6 = r2
        L_0x02f3:
            int r2 = (r17 > r22 ? 1 : (r17 == r22 ? 0 : -1))
            if (r2 < 0) goto L_0x0337
            r40 = 0
            r2 = 0
            r64 = r0
            r0 = r2
            r2 = 0
        L_0x02ff:
            if (r2 >= r11) goto L_0x030e
            r19 = r59[r2]
            r22 = r58[r2]
            double r19 = r19 * r22
            double r22 = r19 * r19
            double r0 = r0 + r22
            int r2 = r2 + 1
            goto L_0x02ff
        L_0x030e:
            double r19 = org.apache.commons.math3.util.FastMath.sqrt(r0)
            r0 = r28
            if (r0 == 0) goto L_0x0328
            int r1 = r73.getIterations()
            r3 = r29
            r2 = r57
            boolean r1 = r0.converged(r1, r3, r2)
            if (r1 == 0) goto L_0x032c
            r8.setCost(r9)
            return r2
        L_0x0328:
            r3 = r29
            r2 = r57
        L_0x032c:
            r66 = r0
            r46 = r2
            r67 = r52
            r34 = r55
            r2 = r58
            goto L_0x0373
        L_0x0337:
            r64 = r0
            r0 = r28
            r3 = r29
            r2 = r57
            r9 = r32
            r1 = 0
        L_0x0342:
            r66 = r0
            int r0 = r8.solvedCols
            if (r1 >= r0) goto L_0x0355
            int[] r0 = r8.permutation
            r0 = r0[r1]
            r22 = r56[r0]
            r58[r0] = r22
            int r1 = r1 + 1
            r0 = r66
            goto L_0x0342
        L_0x0355:
            double[] r0 = r8.weightedResidual
            r1 = r52
            r8.weightedResidual = r1
            r1 = r0
            r25 = r55
            r0 = r24
            r22 = r25
            r67 = r1
            org.apache.commons.math3.optim.PointVectorValuePair r1 = new org.apache.commons.math3.optim.PointVectorValuePair
            r68 = r2
            r2 = r58
            r1.<init>(r2, r0)
            r34 = r0
            r46 = r1
            r24 = r22
        L_0x0373:
            double r0 = org.apache.commons.math3.util.FastMath.abs(r14)
            r70 = r2
            r69 = r3
            double r2 = r8.costRelativeTolerance
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x038b
            double r0 = r8.costRelativeTolerance
            int r0 = (r44 > r0 ? 1 : (r44 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x038b
            int r0 = (r17 > r38 ? 1 : (r17 == r38 ? 0 : -1))
            if (r0 <= 0) goto L_0x0393
        L_0x038b:
            double r0 = r8.parRelativeTolerance
            double r0 = r0 * r19
            int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x0397
        L_0x0393:
            r8.setCost(r9)
            return r46
        L_0x0397:
            double r0 = org.apache.commons.math3.util.FastMath.abs(r14)
            double r2 = TWO_EPS
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 1
            if (r0 > 0) goto L_0x03c0
            double r2 = TWO_EPS
            int r0 = (r44 > r2 ? 1 : (r44 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x03c0
            int r0 = (r17 > r38 ? 1 : (r17 == r38 ? 0 : -1))
            if (r0 > 0) goto L_0x03c0
            org.apache.commons.math3.exception.ConvergenceException r0 = new org.apache.commons.math3.exception.ConvergenceException
            org.apache.commons.math3.exception.util.LocalizedFormats r2 = org.apache.commons.math3.exception.util.LocalizedFormats.TOO_SMALL_COST_RELATIVE_TOLERANCE
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r71 = r4
            double r3 = r8.costRelativeTolerance
            java.lang.Double r3 = java.lang.Double.valueOf(r3)
            r1[r21] = r3
            r0.<init>(r2, r1)
            throw r0
        L_0x03c0:
            r71 = r4
            double r2 = TWO_EPS
            double r2 = r2 * r19
            int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x03dc
            org.apache.commons.math3.exception.ConvergenceException r0 = new org.apache.commons.math3.exception.ConvergenceException
            org.apache.commons.math3.exception.util.LocalizedFormats r2 = org.apache.commons.math3.exception.util.LocalizedFormats.TOO_SMALL_PARAMETERS_RELATIVE_TOLERANCE
            java.lang.Object[] r1 = new java.lang.Object[r1]
            double r3 = r8.parRelativeTolerance
            java.lang.Double r3 = java.lang.Double.valueOf(r3)
            r1[r21] = r3
            r0.<init>(r2, r1)
            throw r0
        L_0x03dc:
            double r2 = TWO_EPS
            int r0 = (r47 > r2 ? 1 : (r47 == r2 ? 0 : -1))
            if (r0 > 0) goto L_0x03f4
            org.apache.commons.math3.exception.ConvergenceException r0 = new org.apache.commons.math3.exception.ConvergenceException
            org.apache.commons.math3.exception.util.LocalizedFormats r2 = org.apache.commons.math3.exception.util.LocalizedFormats.TOO_SMALL_ORTHOGONALITY_TOLERANCE
            java.lang.Object[] r1 = new java.lang.Object[r1]
            double r3 = r8.orthoTolerance
            java.lang.Double r3 = java.lang.Double.valueOf(r3)
            r1[r21] = r3
            r0.<init>(r2, r1)
            throw r0
        L_0x03f4:
            r26 = r9
            r0 = r17
            r39 = r36
            r38 = r37
            r9 = r47
            r14 = r51
            r35 = r54
            r13 = r56
            r12 = r59
            r28 = r66
            r2 = r67
            r29 = r69
            r42 = r70
            goto L_0x018b
        L_0x0411:
            r67 = r2
            r9 = r6
            r59 = r12
            r56 = r13
            r51 = r14
            r66 = r28
            r37 = r38
            r36 = r39
            r70 = r42
            r17 = r9
            r4 = r30
            r0 = r34
            r1 = r35
            r6 = r36
            r7 = r37
            r3 = r40
            r9 = r41
            r15 = r43
            r2 = r46
            r25 = r66
            r23 = r67
            r10 = r70
            goto L_0x0067
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.nonlinear.vector.jacobian.LevenbergMarquardtOptimizer.doOptimize():org.apache.commons.math3.optim.PointVectorValuePair");
    }

    private void determineLMParameter(double[] qy, double delta, double[] diag, double[] work1, double[] work2, double[] work3) {
        double parl;
        double sum2;
        int nC;
        double[] dArr = qy;
        double d = delta;
        double[] dArr2 = work1;
        double[] dArr3 = work2;
        double[] dArr4 = work3;
        int i = this.weightedJacobian[0].length;
        for (int j = 0; j < this.rank; j++) {
            this.lmDir[this.permutation[j]] = dArr[j];
        }
        for (int j2 = this.rank; j2 < i; j2++) {
            this.lmDir[this.permutation[j2]] = 0.0d;
        }
        for (int k = this.rank - 1; k >= 0; k--) {
            int pk = this.permutation[k];
            double ypk = this.lmDir[pk] / this.diagR[pk];
            for (int i2 = 0; i2 < k; i2++) {
                double[] dArr5 = this.lmDir;
                int i3 = this.permutation[i2];
                dArr5[i3] = dArr5[i3] - (this.weightedJacobian[i2][pk] * ypk);
            }
            this.lmDir[pk] = ypk;
        }
        double dxNorm = 0.0d;
        for (int j3 = 0; j3 < this.solvedCols; j3++) {
            int pj = this.permutation[j3];
            double s = diag[pj] * this.lmDir[pj];
            dArr2[pj] = s;
            dxNorm += s * s;
        }
        double dxNorm2 = FastMath.sqrt(dxNorm);
        double fp = dxNorm2 - d;
        if (fp <= d * 0.1d) {
            this.lmPar = 0.0d;
            return;
        }
        long j4 = 0;
        if (this.rank == this.solvedCols) {
            for (int j5 = 0; j5 < this.solvedCols; j5++) {
                int pj2 = this.permutation[j5];
                dArr2[pj2] = dArr2[pj2] * (diag[pj2] / dxNorm2);
            }
            double sum22 = 0.0d;
            int j6 = 0;
            while (j6 < this.solvedCols) {
                int pj3 = this.permutation[j6];
                double sum = 0.0d;
                int i4 = 0;
                while (true) {
                    nC = i;
                    int i5 = i4;
                    if (i5 >= j6) {
                        break;
                    }
                    sum += this.weightedJacobian[i5][pj3] * dArr2[this.permutation[i5]];
                    i4 = i5 + 1;
                    i = nC;
                    j4 = j4;
                }
                long j7 = j4;
                double s2 = (dArr2[pj3] - sum) / this.diagR[pj3];
                dArr2[pj3] = s2;
                sum22 += s2 * s2;
                j6++;
                i = nC;
                j4 = j7;
            }
            int nC2 = i;
            long j8 = j4;
            parl = fp / (d * sum22);
        } else {
            int nC3 = i;
            parl = 0.0d;
        }
        double fp2 = fp;
        double sum23 = 0.0d;
        int j9 = 0;
        while (j9 < this.solvedCols) {
            int pj4 = this.permutation[j9];
            double sum3 = 0.0d;
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 > j9) {
                    break;
                }
                sum3 += this.weightedJacobian[i7][pj4] * dArr[i7];
                i6 = i7 + 1;
                double[] dArr6 = work2;
                double[] dArr7 = work3;
            }
            double sum4 = sum3 / diag[pj4];
            sum23 += sum4 * sum4;
            j9++;
            double[] dArr8 = work2;
            double[] dArr9 = work3;
        }
        double gNorm = FastMath.sqrt(sum23);
        double paru = gNorm / d;
        if (paru == 0.0d) {
            double d2 = sum23;
            sum2 = Precision.SAFE_MIN / FastMath.min(d, 0.1d);
        } else {
            sum2 = paru;
        }
        this.lmPar = FastMath.min(sum2, FastMath.max(this.lmPar, parl));
        if (this.lmPar == 0.0d) {
            this.lmPar = gNorm / dxNorm2;
        }
        int countdown = 10;
        while (countdown >= 0) {
            double gNorm2 = gNorm;
            if (this.lmPar == 0.0d) {
                double d3 = dxNorm2;
                this.lmPar = FastMath.max(Precision.SAFE_MIN, sum2 * 0.001d);
            }
            double sPar = FastMath.sqrt(this.lmPar);
            for (int j10 = 0; j10 < this.solvedCols; j10++) {
                int pj5 = this.permutation[j10];
                dArr2[pj5] = diag[pj5] * sPar;
            }
            double[] dArr10 = work2;
            double[] dArr11 = work3;
            determineLMDirection(dArr, dArr2, dArr10, dArr11);
            double d4 = sPar;
            double dxNorm3 = 0.0d;
            int j11 = 0;
            while (j11 < this.solvedCols) {
                int pj6 = this.permutation[j11];
                double s3 = diag[pj6] * this.lmDir[pj6];
                dArr11[pj6] = s3;
                dxNorm3 += s3 * s3;
                j11++;
                double[] dArr12 = qy;
            }
            dxNorm2 = FastMath.sqrt(dxNorm3);
            double previousFP = fp2;
            int countdown2 = countdown;
            double d5 = delta;
            double fp3 = dxNorm2 - d5;
            if (FastMath.abs(fp3) <= d5 * 0.1d) {
                double d6 = previousFP;
            } else if (parl != 0.0d || fp3 > previousFP || previousFP >= 0.0d) {
                int j12 = 0;
                while (true) {
                    double previousFP2 = previousFP;
                    int j13 = j12;
                    if (j13 >= this.solvedCols) {
                        break;
                    }
                    int pj7 = this.permutation[j13];
                    dArr2[pj7] = (dArr11[pj7] * diag[pj7]) / dxNorm2;
                    j12 = j13 + 1;
                    previousFP = previousFP2;
                }
                int j14 = 0;
                while (j14 < this.solvedCols) {
                    int pj8 = this.permutation[j14];
                    dArr2[pj8] = dArr2[pj8] / dArr10[j14];
                    double tmp = dArr2[pj8];
                    int i8 = j14 + 1;
                    while (true) {
                        int i9 = i8;
                        if (i9 >= this.solvedCols) {
                            break;
                        }
                        int i10 = this.permutation[i9];
                        dArr2[i10] = dArr2[i10] - (this.weightedJacobian[i9][pj8] * tmp);
                        i8 = i9 + 1;
                        double[] dArr13 = work2;
                        double[] dArr14 = work3;
                    }
                    j14++;
                    dArr10 = work2;
                    double[] dArr15 = work3;
                }
                double sum24 = 0.0d;
                for (int j15 = 0; j15 < this.solvedCols; j15++) {
                    double s4 = dArr2[this.permutation[j15]];
                    sum24 += s4 * s4;
                }
                double correction = fp3 / (d5 * sum24);
                if (fp3 > 0.0d) {
                    parl = FastMath.max(parl, this.lmPar);
                } else if (fp3 < 0.0d) {
                    sum2 = FastMath.min(sum2, this.lmPar);
                }
                this.lmPar = FastMath.max(parl, this.lmPar + correction);
                fp2 = fp3;
                countdown = countdown2 - 1;
                gNorm = gNorm2;
                dArr = qy;
            } else {
                double d7 = previousFP;
            }
            return;
        }
        double d8 = dxNorm2;
        double d9 = delta;
    }

    private void determineLMDirection(double[] qy, double[] diag, double[] lmDiag, double[] work) {
        int j;
        double dpj;
        int pj;
        double cotan;
        double sin;
        double[] dArr = lmDiag;
        for (int j2 = 0; j2 < this.solvedCols; j2++) {
            int pj2 = this.permutation[j2];
            for (int i = j2 + 1; i < this.solvedCols; i++) {
                this.weightedJacobian[i][pj2] = this.weightedJacobian[j2][this.permutation[i]];
            }
            this.lmDir[j2] = this.diagR[pj2];
            work[j2] = qy[j2];
        }
        int j3 = 0;
        while (true) {
            double d = 0.0d;
            if (j3 >= this.solvedCols) {
                break;
            }
            int pj3 = this.permutation[j3];
            double dpj2 = diag[pj3];
            if (dpj2 != 0.0d) {
                Arrays.fill(dArr, j3 + 1, dArr.length, 0.0d);
            }
            dArr[j3] = dpj2;
            double qtbpj = 0.0d;
            int k = j3;
            while (k < this.solvedCols) {
                int pk = this.permutation[k];
                if (dArr[k] != d) {
                    double rkk = this.weightedJacobian[k][pk];
                    pj = pj3;
                    if (FastMath.abs(rkk) < FastMath.abs(dArr[k])) {
                        double cotan2 = rkk / dArr[k];
                        dpj = dpj2;
                        sin = 1.0d / FastMath.sqrt((cotan2 * cotan2) + 1.0d);
                        cotan = cotan2 * sin;
                        j = j3;
                    } else {
                        dpj = dpj2;
                        double tan = dArr[k] / rkk;
                        j = j3;
                        cotan = 1.0d / FastMath.sqrt((tan * tan) + 1.0d);
                        sin = cotan * tan;
                    }
                    double sin2 = sin;
                    this.weightedJacobian[k][pk] = (cotan * rkk) + (dArr[k] * sin2);
                    double temp = (work[k] * cotan) + (sin2 * qtbpj);
                    double qtbpj2 = ((-sin2) * work[k]) + (cotan * qtbpj);
                    work[k] = temp;
                    int i2 = k + 1;
                    while (i2 < this.solvedCols) {
                        double rik = this.weightedJacobian[i2][pk];
                        double temp2 = (cotan * rik) + (dArr[i2] * sin2);
                        double temp3 = temp;
                        dArr[i2] = ((-sin2) * rik) + (dArr[i2] * cotan);
                        this.weightedJacobian[i2][pk] = temp2;
                        i2++;
                        temp = temp3;
                    }
                    qtbpj = qtbpj2;
                } else {
                    j = j3;
                    pj = pj3;
                    dpj = dpj2;
                }
                k++;
                pj3 = pj;
                dpj2 = dpj;
                j3 = j;
                d = 0.0d;
            }
            int j4 = j3;
            int i3 = pj3;
            double d2 = dpj2;
            dArr[j4] = this.weightedJacobian[j4][this.permutation[j4]];
            this.weightedJacobian[j4][this.permutation[j4]] = this.lmDir[j4];
            j3 = j4 + 1;
        }
        int nSing = this.solvedCols;
        for (int j5 = 0; j5 < this.solvedCols; j5++) {
            if (dArr[j5] == 0.0d && nSing == this.solvedCols) {
                nSing = j5;
            }
            if (nSing < this.solvedCols) {
                work[j5] = 0.0d;
            }
        }
        if (nSing > 0) {
            for (int j6 = nSing - 1; j6 >= 0; j6--) {
                int pj4 = this.permutation[j6];
                double sum = 0.0d;
                for (int i4 = j6 + 1; i4 < nSing; i4++) {
                    sum += this.weightedJacobian[i4][pj4] * work[i4];
                }
                work[j6] = (work[j6] - sum) / dArr[j6];
            }
        }
        int j7 = 0;
        while (true) {
            int j8 = j7;
            if (j8 < this.lmDir.length) {
                this.lmDir[this.permutation[j8]] = work[j8];
                j7 = j8 + 1;
            } else {
                return;
            }
        }
    }

    private void qrDecomposition(RealMatrix jacobian) throws ConvergenceException {
        this.weightedJacobian = jacobian.scalarMultiply(-1.0d).getData();
        char c = 0;
        int nC = this.weightedJacobian[0].length;
        for (int k = 0; k < nC; k++) {
            this.permutation[k] = k;
            double norm2 = 0.0d;
            for (double[] dArr : this.weightedJacobian) {
                double akk = dArr[k];
                norm2 += akk * akk;
            }
            this.jacNorm[k] = FastMath.sqrt(norm2);
        }
        int k2 = 0;
        while (k2 < nC) {
            double ak2 = Double.NEGATIVE_INFINITY;
            int nextColumn = -1;
            for (int i = k2; i < nC; i++) {
                double norm22 = 0.0d;
                for (int j = k2; j < nR; j++) {
                    double aki = this.weightedJacobian[j][this.permutation[i]];
                    norm22 += aki * aki;
                }
                if (Double.isInfinite(norm22) != 0 || Double.isNaN(norm22)) {
                    LocalizedFormats localizedFormats = LocalizedFormats.UNABLE_TO_PERFORM_QR_DECOMPOSITION_ON_JACOBIAN;
                    Object[] objArr = new Object[2];
                    objArr[c] = Integer.valueOf(nR);
                    objArr[1] = Integer.valueOf(nC);
                    throw new ConvergenceException(localizedFormats, objArr);
                }
                if (norm22 > ak2) {
                    nextColumn = i;
                    ak2 = norm22;
                }
            }
            if (ak2 <= this.qrRankingThreshold) {
                this.rank = k2;
                return;
            }
            int pk = this.permutation[nextColumn];
            this.permutation[nextColumn] = this.permutation[k2];
            this.permutation[k2] = pk;
            double akk2 = this.weightedJacobian[k2][pk];
            double alpha = akk2 > 0.0d ? -FastMath.sqrt(ak2) : FastMath.sqrt(ak2);
            double betak = 1.0d / (ak2 - (akk2 * alpha));
            this.beta[pk] = betak;
            this.diagR[pk] = alpha;
            double[] dArr2 = this.weightedJacobian[k2];
            dArr2[pk] = dArr2[pk] - alpha;
            int dk = (nC - 1) - k2;
            while (dk > 0) {
                double gamma = 0.0d;
                int j2 = k2;
                while (true) {
                    int j3 = j2;
                    if (j3 >= nR) {
                        break;
                    }
                    gamma += this.weightedJacobian[j3][pk] * this.weightedJacobian[j3][this.permutation[k2 + dk]];
                    j2 = j3 + 1;
                    nC = nC;
                    RealMatrix realMatrix = jacobian;
                }
                int nC2 = nC;
                double gamma2 = gamma * betak;
                int j4 = k2;
                while (j4 < nR) {
                    double[] dArr3 = this.weightedJacobian[j4];
                    int i2 = this.permutation[k2 + dk];
                    int nR = nR;
                    dArr3[i2] = dArr3[i2] - (this.weightedJacobian[j4][pk] * gamma2);
                    j4++;
                    nR = nR;
                }
                dk--;
                nC = nC2;
                RealMatrix realMatrix2 = jacobian;
            }
            int i3 = nC;
            k2++;
            RealMatrix realMatrix3 = jacobian;
            c = 0;
        }
        int i4 = nC;
        this.rank = this.solvedCols;
    }

    private void qTy(double[] y) {
        int nR = this.weightedJacobian.length;
        int nC = this.weightedJacobian[0].length;
        for (int k = 0; k < nC; k++) {
            int pk = this.permutation[k];
            double gamma = 0.0d;
            for (int i = k; i < nR; i++) {
                gamma += this.weightedJacobian[i][pk] * y[i];
            }
            double gamma2 = gamma * this.beta[pk];
            for (int i2 = k; i2 < nR; i2++) {
                y[i2] = y[i2] - (this.weightedJacobian[i2][pk] * gamma2);
            }
        }
    }

    private void checkParameters() {
        if (getLowerBound() != null || getUpperBound() != null) {
            throw new MathUnsupportedOperationException(LocalizedFormats.CONSTRAINT, new Object[0]);
        }
    }
}
