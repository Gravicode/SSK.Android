package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.util.FastMath;

public class BrentOptimizer extends UnivariateOptimizer {
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
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0141  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0166  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x016a  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0182  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x018f  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0195  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x01b5  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.optim.univariate.UnivariatePointValuePair doOptimize() {
        /*
            r84 = this;
            r0 = r84
            org.apache.commons.math3.optim.nonlinear.scalar.GoalType r1 = r84.getGoalType()
            org.apache.commons.math3.optim.nonlinear.scalar.GoalType r2 = org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE
            if (r1 != r2) goto L_0x000c
            r1 = 1
            goto L_0x000d
        L_0x000c:
            r1 = 0
        L_0x000d:
            double r5 = r84.getMin()
            double r7 = r84.getStartValue()
            double r9 = r84.getMax()
            org.apache.commons.math3.optim.ConvergenceChecker r2 = r84.getConvergenceChecker()
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
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r7 = new org.apache.commons.math3.optim.univariate.UnivariatePointValuePair
            if (r1 == 0) goto L_0x0048
            r32 = r9
            r8 = r5
            goto L_0x004b
        L_0x0048:
            r32 = r9
            double r8 = -r5
        L_0x004b:
            r7.<init>(r3, r8)
            r36 = r1
            r37 = r2
            r35 = r7
            r8 = r17
            r1 = r19
            r34 = r29
            r17 = r15
            r15 = r5
            r5 = r23
        L_0x005f:
            r10 = 0
            double r19 = r11 + r13
            r23 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r19 = r19 * r23
            r40 = r7
            r38 = r8
            double r7 = r0.relativeThreshold
            double r9 = org.apache.commons.math3.util.FastMath.abs(r3)
            double r7 = r7 * r9
            double r9 = r0.absoluteThreshold
            double r7 = r7 + r9
            r9 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r41 = r7 * r9
            double r9 = r3 - r19
            double r9 = org.apache.commons.math3.util.FastMath.abs(r9)
            double r45 = r13 - r11
            double r45 = r45 * r23
            double r45 = r41 - r45
            int r9 = (r9 > r45 ? 1 : (r9 == r45 ? 0 : -1))
            if (r9 > 0) goto L_0x008b
            r9 = 1
            goto L_0x008c
        L_0x008b:
            r9 = 0
        L_0x008c:
            if (r9 != 0) goto L_0x0233
            r45 = 0
            r47 = 0
            r49 = 0
            r51 = 0
            double r53 = org.apache.commons.math3.util.FastMath.abs(r5)
            int r10 = (r53 > r7 ? 1 : (r53 == r7 ? 0 : -1))
            r53 = 0
            if (r10 <= 0) goto L_0x011f
            double r55 = r3 - r1
            double r57 = r15 - r17
            double r55 = r55 * r57
            double r49 = r3 - r38
            double r57 = r15 - r27
            double r49 = r49 * r57
            double r47 = r3 - r38
            double r47 = r47 * r49
            double r57 = r3 - r1
            double r57 = r57 * r55
            r59 = r9
            double r9 = r47 - r57
            double r45 = r49 - r55
            r60 = r1
            r43 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r1 = r45 * r43
            int r29 = (r1 > r53 ? 1 : (r1 == r53 ? 0 : -1))
            if (r29 <= 0) goto L_0x00c8
            double r9 = -r9
        L_0x00c5:
            r47 = r1
            goto L_0x00ca
        L_0x00c8:
            double r1 = -r1
            goto L_0x00c5
        L_0x00ca:
            r49 = r5
            r1 = r21
            r5 = 0
            double r5 = r11 - r3
            double r5 = r5 * r47
            int r5 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r5 <= 0) goto L_0x010d
            double r5 = r13 - r3
            double r5 = r5 * r47
            int r5 = (r9 > r5 ? 1 : (r9 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x010d
            double r5 = org.apache.commons.math3.util.FastMath.abs(r9)
            double r23 = r23 * r47
            r62 = r1
            double r1 = r23 * r49
            double r1 = org.apache.commons.math3.util.FastMath.abs(r1)
            int r1 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r1 >= 0) goto L_0x010f
            double r1 = r9 / r47
            double r51 = r3 + r1
            double r5 = r51 - r11
            int r5 = (r5 > r41 ? 1 : (r5 == r41 ? 0 : -1))
            if (r5 < 0) goto L_0x0105
            double r5 = r13 - r51
            int r5 = (r5 > r41 ? 1 : (r5 == r41 ? 0 : -1))
            if (r5 >= 0) goto L_0x0102
            goto L_0x0105
        L_0x0102:
            r5 = r62
            goto L_0x0139
        L_0x0105:
            int r5 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1))
            if (r5 > 0) goto L_0x010b
            r1 = r7
            goto L_0x0102
        L_0x010b:
            double r1 = -r7
            goto L_0x0102
        L_0x010d:
            r62 = r1
        L_0x010f:
            int r1 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1))
            if (r1 >= 0) goto L_0x0116
            double r1 = r13 - r3
        L_0x0115:
            goto L_0x011a
        L_0x0116:
            r1 = 0
            double r1 = r11 - r3
            goto L_0x0115
        L_0x011a:
            double r5 = GOLDEN_SECTION
            double r5 = r5 * r1
            goto L_0x0134
        L_0x011f:
            r60 = r1
            r59 = r9
            int r1 = (r3 > r19 ? 1 : (r3 == r19 ? 0 : -1))
            if (r1 >= 0) goto L_0x012a
            double r1 = r13 - r3
        L_0x0129:
            goto L_0x012e
        L_0x012a:
            r1 = 0
            double r1 = r11 - r3
            goto L_0x0129
        L_0x012e:
            double r5 = GOLDEN_SECTION
            double r5 = r5 * r1
            r9 = r45
        L_0x0134:
            r82 = r1
            r1 = r5
            r5 = r82
        L_0x0139:
            double r21 = org.apache.commons.math3.util.FastMath.abs(r1)
            int r21 = (r21 > r7 ? 1 : (r21 == r7 ? 0 : -1))
            if (r21 >= 0) goto L_0x014d
            int r21 = (r1 > r53 ? 1 : (r1 == r53 ? 0 : -1))
            if (r21 < 0) goto L_0x0148
            double r21 = r3 + r7
            goto L_0x0151
        L_0x0148:
            r21 = 0
            double r21 = r3 - r7
            goto L_0x0151
        L_0x014d:
            r21 = 0
            double r21 = r3 + r1
        L_0x0151:
            r64 = r1
            r1 = r21
            r66 = r5
            double r5 = r0.computeObjectiveValue(r1)
            if (r36 != 0) goto L_0x015e
            double r5 = -r5
        L_0x015e:
            r68 = r35
            r69 = r7
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r7 = new org.apache.commons.math3.optim.univariate.UnivariatePointValuePair
            if (r36 == 0) goto L_0x016a
            r71 = r9
            r8 = r5
            goto L_0x016d
        L_0x016a:
            r71 = r9
            double r8 = -r5
        L_0x016d:
            r7.<init>(r1, r8)
            r8 = r36
            r9 = r68
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r10 = r0.best(r9, r7, r8)
            r73 = r11
            r11 = r40
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r10 = r0.best(r11, r10, r8)
            if (r37 == 0) goto L_0x018f
            int r11 = r84.getIterations()
            r12 = r37
            boolean r11 = r12.converged(r11, r9, r7)
            if (r11 == 0) goto L_0x0191
            return r10
        L_0x018f:
            r12 = r37
        L_0x0191:
            int r11 = (r5 > r15 ? 1 : (r5 == r15 ? 0 : -1))
            if (r11 > 0) goto L_0x01b5
            int r11 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r11 >= 0) goto L_0x019d
            r13 = r3
            r21 = r73
            goto L_0x019f
        L_0x019d:
            r21 = r3
        L_0x019f:
            r23 = r60
            r17 = r27
            r34 = r3
            r27 = r15
            r3 = r1
            r15 = r5
            r75 = r9
            r76 = r10
            r77 = r12
            r11 = r21
            r1 = r34
            goto L_0x021d
        L_0x01b5:
            int r11 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r11 >= 0) goto L_0x01bc
            r21 = r1
            goto L_0x01bf
        L_0x01bc:
            r13 = r1
            r21 = r73
        L_0x01bf:
            int r11 = (r5 > r27 ? 1 : (r5 == r27 ? 0 : -1))
            if (r11 <= 0) goto L_0x0201
            r75 = r9
            r76 = r10
            r9 = r60
            boolean r11 = org.apache.commons.math3.util.Precision.equals(r9, r3)
            if (r11 == 0) goto L_0x01d6
            r77 = r12
            r78 = r13
            r12 = r38
            goto L_0x020d
        L_0x01d6:
            int r11 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1))
            if (r11 <= 0) goto L_0x01f1
            r77 = r12
            r78 = r13
            r12 = r38
            boolean r11 = org.apache.commons.math3.util.Precision.equals(r12, r3)
            if (r11 != 0) goto L_0x01f7
            boolean r11 = org.apache.commons.math3.util.Precision.equals(r12, r9)
            if (r11 == 0) goto L_0x01ed
            goto L_0x01f7
        L_0x01ed:
            r1 = r9
            r23 = r12
            goto L_0x01fe
        L_0x01f1:
            r77 = r12
            r78 = r13
            r12 = r38
        L_0x01f7:
            r11 = r1
            r1 = r5
            r17 = r1
            r1 = r9
            r23 = r11
        L_0x01fe:
            r11 = r21
            goto L_0x021b
        L_0x0201:
            r75 = r9
            r76 = r10
            r77 = r12
            r78 = r13
            r12 = r38
            r9 = r60
        L_0x020d:
            r11 = r9
            r13 = r27
            r9 = r1
            r17 = r5
            r23 = r11
            r27 = r17
            r11 = r21
            r17 = r13
        L_0x021b:
            r13 = r78
        L_0x021d:
            r84.incrementIterationCount()
            r35 = r7
            r36 = r8
            r8 = r23
            r21 = r64
            r5 = r66
            r34 = r75
            r7 = r76
            r37 = r77
            goto L_0x005f
        L_0x0233:
            r69 = r7
            r59 = r9
            r73 = r11
            r8 = r36
            r77 = r37
            r11 = r40
            r9 = r1
            r1 = r13
            r12 = r38
            r80 = r1
            r7 = r34
            r14 = r35
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r1 = r0.best(r7, r14, r8)
            org.apache.commons.math3.optim.univariate.UnivariatePointValuePair r1 = r0.best(r11, r1, r8)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.univariate.BrentOptimizer.doOptimize():org.apache.commons.math3.optim.univariate.UnivariatePointValuePair");
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
