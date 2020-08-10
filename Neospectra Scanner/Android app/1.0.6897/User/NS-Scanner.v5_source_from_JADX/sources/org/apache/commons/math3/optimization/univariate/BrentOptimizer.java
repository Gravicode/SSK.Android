package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;

@Deprecated
public class BrentOptimizer extends BaseAbstractUnivariateOptimizer {
    private static final double GOLDEN_SECTION = ((3.0d - FastMath.sqrt(5.0d)) * 0.5d);
    private static final double MIN_RELATIVE_TOLERANCE = (FastMath.ulp(1.0d) * 2.0d);
    private final double absoluteThreshold;
    private final double relativeThreshold;

    public BrentOptimizer(double rel, double abs, ConvergenceChecker<UnivariatePointValuePair> checker) {
        super(checker);
        if (rel < MIN_RELATIVE_TOLERANCE) {
            throw new NumberIsTooSmallException(Double.valueOf(rel), Double.valueOf(MIN_RELATIVE_TOLERANCE), true);
        } else if (abs <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(abs));
        } else {
            this.relativeThreshold = rel;
            this.absoluteThreshold = abs;
        }
    }

    public BrentOptimizer(double rel, double abs) {
        this(rel, abs, null);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x01e7, code lost:
        if (org.apache.commons.math3.util.Precision.equals(r11, r13) == false) goto L_0x01f2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair doOptimize() {
        /*
            r79 = this;
            r0 = r79
            org.apache.commons.math3.optimization.GoalType r1 = r79.getGoalType()
            org.apache.commons.math3.optimization.GoalType r2 = org.apache.commons.math3.optimization.GoalType.MINIMIZE
            if (r1 != r2) goto L_0x000c
            r1 = 1
            goto L_0x000d
        L_0x000c:
            r1 = 0
        L_0x000d:
            double r5 = r79.getMin()
            double r7 = r79.getStartValue()
            double r9 = r79.getMax()
            org.apache.commons.math3.optimization.ConvergenceChecker r2 = r79.getConvergenceChecker()
            int r11 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1))
            if (r11 >= 0) goto L_0x0024
            r11 = r5
            r13 = r9
            goto L_0x0026
        L_0x0024:
            r11 = r9
            r13 = r5
        L_0x0026:
            r15 = r7
            r17 = r15
            r19 = r15
            r21 = 0
            r23 = 0
            r25 = r5
            r3 = r15
            double r5 = r0.computeObjectiveValue(r3)
            if (r1 != 0) goto L_0x0039
            double r5 = -r5
        L_0x0039:
            r15 = r5
            r27 = r5
            r29 = 0
            r30 = r7
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r7 = new org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair
            if (r1 == 0) goto L_0x0048
            r32 = r9
            r8 = r5
            goto L_0x004b
        L_0x0048:
            r32 = r9
            double r8 = -r5
        L_0x004b:
            r7.<init>(r3, r8)
            r8 = r7
            r36 = r1
            r37 = r2
            r10 = r7
            r34 = r8
            r8 = r17
            r1 = r19
            r35 = r29
            r17 = r15
            r15 = r5
            r6 = r23
            r4 = r3
            r3 = 0
        L_0x0063:
            r19 = 0
            double r19 = r11 + r13
            r23 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r19 = r19 * r23
            r38 = r8
            double r8 = r0.relativeThreshold
            double r40 = org.apache.commons.math3.util.FastMath.abs(r4)
            double r8 = r8 * r40
            r42 = r1
            double r1 = r0.absoluteThreshold
            double r8 = r8 + r1
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r40 = r8 * r1
            double r1 = r4 - r19
            double r1 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r46 = r13 - r11
            double r46 = r46 * r23
            double r46 = r40 - r46
            int r1 = (r1 > r46 ? 1 : (r1 == r46 ? 0 : -1))
            if (r1 > 0) goto L_0x0090
            r1 = 1
            goto L_0x0091
        L_0x0090:
            r1 = 0
        L_0x0091:
            if (r1 != 0) goto L_0x0221
            r46 = 0
            r48 = 0
            r50 = 0
            r52 = 0
            double r54 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r2 = (r54 > r8 ? 1 : (r54 == r8 ? 0 : -1))
            r54 = 0
            if (r2 <= 0) goto L_0x0130
            double r56 = r4 - r42
            double r58 = r15 - r17
            double r56 = r56 * r58
            double r50 = r4 - r38
            double r58 = r15 - r27
            double r50 = r50 * r58
            double r48 = r4 - r38
            double r48 = r48 * r50
            double r58 = r4 - r42
            double r58 = r58 * r56
            r60 = r1
            double r1 = r48 - r58
            double r46 = r50 - r56
            r61 = r8
            r44 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r8 = r46 * r44
            int r29 = (r8 > r54 ? 1 : (r8 == r54 ? 0 : -1))
            if (r29 <= 0) goto L_0x00cd
            double r1 = -r1
        L_0x00ca:
            r48 = r8
            goto L_0x00cf
        L_0x00cd:
            double r8 = -r8
            goto L_0x00ca
        L_0x00cf:
            r50 = r6
            r6 = r21
            r8 = 0
            double r8 = r11 - r4
            double r8 = r8 * r48
            int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0118
            double r8 = r13 - r4
            double r8 = r8 * r48
            int r8 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r8 >= 0) goto L_0x0118
            double r8 = org.apache.commons.math3.util.FastMath.abs(r1)
            double r23 = r23 * r48
            r63 = r6
            double r6 = r23 * r50
            double r6 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r6 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1))
            if (r6 >= 0) goto L_0x0115
            double r6 = r1 / r48
            double r52 = r4 + r6
            double r8 = r52 - r11
            int r8 = (r8 > r40 ? 1 : (r8 == r40 ? 0 : -1))
            if (r8 < 0) goto L_0x010a
            double r8 = r13 - r52
            int r8 = (r8 > r40 ? 1 : (r8 == r40 ? 0 : -1))
            if (r8 >= 0) goto L_0x0107
            goto L_0x010a
        L_0x0107:
            r8 = r61
            goto L_0x0145
        L_0x010a:
            int r8 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r8 > 0) goto L_0x0111
            r6 = r61
            goto L_0x0107
        L_0x0111:
            r8 = r61
            double r6 = -r8
            goto L_0x0145
        L_0x0115:
            r8 = r61
            goto L_0x011c
        L_0x0118:
            r63 = r6
            r8 = r61
        L_0x011c:
            int r6 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r6 >= 0) goto L_0x0123
            double r6 = r13 - r4
        L_0x0122:
            goto L_0x0127
        L_0x0123:
            r6 = 0
            double r6 = r11 - r4
            goto L_0x0122
        L_0x0127:
            double r23 = GOLDEN_SECTION
            double r23 = r23 * r6
            r63 = r6
            r6 = r23
            goto L_0x0145
        L_0x0130:
            r60 = r1
            int r1 = (r4 > r19 ? 1 : (r4 == r19 ? 0 : -1))
            if (r1 >= 0) goto L_0x0139
            double r1 = r13 - r4
        L_0x0138:
            goto L_0x013d
        L_0x0139:
            r1 = 0
            double r1 = r11 - r4
            goto L_0x0138
        L_0x013d:
            double r6 = GOLDEN_SECTION
            double r6 = r6 * r1
            r63 = r1
            r1 = r46
        L_0x0145:
            double r21 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r21 = (r21 > r8 ? 1 : (r21 == r8 ? 0 : -1))
            if (r21 >= 0) goto L_0x0159
            int r21 = (r6 > r54 ? 1 : (r6 == r54 ? 0 : -1))
            if (r21 < 0) goto L_0x0154
            double r21 = r4 + r8
            goto L_0x015d
        L_0x0154:
            r21 = 0
            double r21 = r4 - r8
            goto L_0x015d
        L_0x0159:
            r21 = 0
            double r21 = r4 + r6
        L_0x015d:
            r65 = r1
            r1 = r21
            r67 = r6
            double r6 = r0.computeObjectiveValue(r1)
            if (r36 != 0) goto L_0x016a
            double r6 = -r6
        L_0x016a:
            r69 = r10
            r70 = r8
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r8 = new org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair
            if (r36 == 0) goto L_0x0176
            r72 = r11
            r11 = r6
            goto L_0x0179
        L_0x0176:
            r72 = r11
            double r11 = -r6
        L_0x0179:
            r8.<init>(r1, r11)
            r10 = r8
            r8 = r36
            r9 = r69
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r11 = r0.best(r9, r10, r8)
            r12 = r34
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r34 = r0.best(r12, r11, r8)
            if (r37 == 0) goto L_0x0196
            r11 = r37
            boolean r12 = r11.converged(r3, r9, r10)
            if (r12 == 0) goto L_0x0198
            return r34
        L_0x0196:
            r11 = r37
        L_0x0198:
            int r12 = (r6 > r15 ? 1 : (r6 == r15 ? 0 : -1))
            if (r12 > 0) goto L_0x01b8
            int r12 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r12 >= 0) goto L_0x01a4
            r13 = r4
            r21 = r72
            goto L_0x01a6
        L_0x01a4:
            r21 = r4
        L_0x01a6:
            r23 = r42
            r17 = r27
            r35 = r4
            r27 = r15
            r4 = r1
            r15 = r6
            r76 = r11
            r11 = r21
            r1 = r35
            goto L_0x0210
        L_0x01b8:
            int r12 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r12 >= 0) goto L_0x01bf
            r21 = r1
            goto L_0x01c2
        L_0x01bf:
            r13 = r1
            r21 = r72
        L_0x01c2:
            int r12 = (r6 > r27 ? 1 : (r6 == r27 ? 0 : -1))
            if (r12 <= 0) goto L_0x01fa
            r74 = r13
            r13 = r42
            boolean r12 = org.apache.commons.math3.util.Precision.equals(r13, r4)
            if (r12 == 0) goto L_0x01d5
            r76 = r11
            r11 = r38
            goto L_0x0202
        L_0x01d5:
            int r12 = (r6 > r17 ? 1 : (r6 == r17 ? 0 : -1))
            if (r12 <= 0) goto L_0x01ea
            r76 = r11
            r11 = r38
            boolean r23 = org.apache.commons.math3.util.Precision.equals(r11, r4)
            if (r23 != 0) goto L_0x01ee
            boolean r23 = org.apache.commons.math3.util.Precision.equals(r11, r13)
            if (r23 == 0) goto L_0x01f2
            goto L_0x01ee
        L_0x01ea:
            r76 = r11
            r11 = r38
        L_0x01ee:
            r11 = r1
            r1 = r6
            r17 = r1
        L_0x01f2:
            r23 = r11
            r1 = r13
            r11 = r21
            r13 = r74
            goto L_0x0210
        L_0x01fa:
            r76 = r11
            r74 = r13
            r11 = r38
            r13 = r42
        L_0x0202:
            r11 = r13
            r17 = r27
            r13 = r1
            r23 = r6
            r27 = r23
            r13 = r74
            r23 = r11
            r11 = r21
        L_0x0210:
            int r3 = r3 + 1
            r36 = r8
            r35 = r9
            r8 = r23
            r6 = r63
            r21 = r67
            r37 = r76
            goto L_0x0063
        L_0x0221:
            r60 = r1
            r70 = r8
            r72 = r11
            r1 = r13
            r9 = r34
            r8 = r36
            r76 = r37
            r11 = r38
            r13 = r42
            r77 = r1
            r1 = r35
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r2 = r0.best(r1, r10, r8)
            org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair r2 = r0.best(r9, r2, r8)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.univariate.BrentOptimizer.doOptimize():org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair");
    }

    private UnivariatePointValuePair best(UnivariatePointValuePair a, UnivariatePointValuePair b, boolean isMinim) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (isMinim) {
            return a.getValue() <= b.getValue() ? a : b;
        }
        return a.getValue() >= b.getValue() ? a : b;
    }
}
