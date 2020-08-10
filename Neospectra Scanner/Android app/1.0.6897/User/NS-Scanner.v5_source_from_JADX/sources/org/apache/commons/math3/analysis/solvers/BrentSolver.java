package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.FastMath;

public class BrentSolver extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;

    public BrentSolver() {
        this(1.0E-6d);
    }

    public BrentSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    public BrentSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    public BrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    /* access modifiers changed from: protected */
    public double doSolve() throws NoBracketingException, TooManyEvaluationsException, NumberIsTooLargeException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double yInitial = computeObjectiveValue(initial);
        if (FastMath.abs(yInitial) <= functionValueAccuracy) {
            return initial;
        }
        double yMin = computeObjectiveValue(min);
        if (FastMath.abs(yMin) <= functionValueAccuracy) {
            return min;
        }
        if (yInitial * yMin < 0.0d) {
            double d = yMin;
            double d2 = yInitial;
            return brent(min, initial, yMin, yInitial);
        }
        double yMin2 = yMin;
        double yInitial2 = yInitial;
        double yMax = computeObjectiveValue(max);
        if (FastMath.abs(yMax) <= functionValueAccuracy) {
            return max;
        }
        if (yInitial2 * yMax < 0.0d) {
            double d3 = yMax;
            return brent(initial, max, yInitial2, yMax);
        }
        NoBracketingException noBracketingException = new NoBracketingException(min, max, yMin2, yMax);
        throw noBracketingException;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00f3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double brent(double r54, double r56, double r58, double r60) {
        /*
            r53 = this;
            r0 = r54
            r2 = r58
            r4 = r56
            r6 = r60
            r8 = r0
            r10 = r2
            double r12 = r4 - r0
            r14 = r12
            double r16 = r53.getAbsoluteAccuracy()
            double r18 = r53.getRelativeAccuracy()
        L_0x0015:
            double r20 = org.apache.commons.math3.util.FastMath.abs(r10)
            double r22 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r20 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r20 >= 0) goto L_0x0027
            r0 = r4
            r4 = r8
            r8 = r0
            r2 = r6
            r6 = r10
            r10 = r2
        L_0x0027:
            r20 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r22 = r18 * r20
            double r24 = org.apache.commons.math3.util.FastMath.abs(r4)
            double r22 = r22 * r24
            double r22 = r22 + r16
            double r24 = r8 - r4
            r26 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            r28 = r12
            double r12 = r24 * r26
            double r24 = org.apache.commons.math3.util.FastMath.abs(r12)
            int r24 = (r24 > r22 ? 1 : (r24 == r22 ? 0 : -1))
            if (r24 <= 0) goto L_0x0134
            r30 = r4
            r4 = 0
            boolean r24 = org.apache.commons.math3.util.Precision.equals(r6, r4)
            if (r24 == 0) goto L_0x0057
            r43 = r0
            r45 = r2
            r49 = r6
            r6 = r53
            goto L_0x013e
        L_0x0057:
            double r24 = org.apache.commons.math3.util.FastMath.abs(r14)
            int r24 = (r24 > r22 ? 1 : (r24 == r22 ? 0 : -1))
            if (r24 < 0) goto L_0x00db
            double r24 = org.apache.commons.math3.util.FastMath.abs(r2)
            double r32 = org.apache.commons.math3.util.FastMath.abs(r6)
            int r24 = (r24 > r32 ? 1 : (r24 == r32 ? 0 : -1))
            if (r24 > 0) goto L_0x0071
            r43 = r0
            r45 = r2
            goto L_0x00df
        L_0x0071:
            double r24 = r6 / r2
            int r32 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1))
            r33 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            if (r32 != 0) goto L_0x0088
            double r20 = r20 * r12
            double r20 = r20 * r24
            double r33 = r33 - r24
        L_0x007f:
            r43 = r0
            r45 = r2
            r0 = r20
            r2 = r33
            goto L_0x00ab
        L_0x0088:
            double r35 = r2 / r10
            double r37 = r6 / r10
            double r20 = r20 * r12
            double r20 = r20 * r35
            r32 = 0
            double r39 = r35 - r37
            double r20 = r20 * r39
            double r39 = r30 - r0
            double r41 = r37 - r33
            double r39 = r39 * r41
            double r20 = r20 - r39
            double r20 = r20 * r24
            double r39 = r35 - r33
            double r41 = r37 - r33
            double r39 = r39 * r41
            double r32 = r24 - r33
            double r33 = r39 * r32
            goto L_0x007f
        L_0x00ab:
            int r20 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r20 <= 0) goto L_0x00b1
            double r2 = -r2
            goto L_0x00b2
        L_0x00b1:
            double r0 = -r0
        L_0x00b2:
            r20 = r14
            r14 = r28
            r24 = 4609434218613702656(0x3ff8000000000000, double:1.5)
            double r24 = r24 * r12
            double r24 = r24 * r2
            double r4 = r22 * r2
            double r4 = org.apache.commons.math3.util.FastMath.abs(r4)
            double r24 = r24 - r4
            int r4 = (r0 > r24 ? 1 : (r0 == r24 ? 0 : -1))
            if (r4 >= 0) goto L_0x00d7
            double r26 = r26 * r20
            double r4 = r26 * r2
            double r4 = org.apache.commons.math3.util.FastMath.abs(r4)
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x00d5
            goto L_0x00d7
        L_0x00d5:
            double r0 = r0 / r2
            goto L_0x00e1
        L_0x00d7:
            r4 = r12
            r14 = r4
            r0 = r4
            goto L_0x00e1
        L_0x00db:
            r43 = r0
            r45 = r2
        L_0x00df:
            r0 = r12
            r14 = r0
        L_0x00e1:
            r2 = r30
            r4 = r6
            double r20 = org.apache.commons.math3.util.FastMath.abs(r0)
            int r20 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r20 <= 0) goto L_0x00f3
            double r20 = r30 + r0
        L_0x00ee:
            r47 = r0
            r0 = r20
            goto L_0x0101
        L_0x00f3:
            r20 = 0
            int r24 = (r12 > r20 ? 1 : (r12 == r20 ? 0 : -1))
            if (r24 <= 0) goto L_0x00fc
            double r20 = r30 + r22
            goto L_0x00ee
        L_0x00fc:
            r20 = 0
            double r20 = r30 - r22
            goto L_0x00ee
        L_0x0101:
            r49 = r6
            r6 = r53
            double r20 = r6.computeObjectiveValue(r0)
            r24 = 0
            int r7 = (r20 > r24 ? 1 : (r20 == r24 ? 0 : -1))
            if (r7 <= 0) goto L_0x0113
            int r7 = (r10 > r24 ? 1 : (r10 == r24 ? 0 : -1))
            if (r7 > 0) goto L_0x011b
        L_0x0113:
            int r7 = (r20 > r24 ? 1 : (r20 == r24 ? 0 : -1))
            if (r7 > 0) goto L_0x0128
            int r7 = (r10 > r24 ? 1 : (r10 == r24 ? 0 : -1))
            if (r7 > 0) goto L_0x0128
        L_0x011b:
            r7 = r2
            r9 = r4
            r11 = 0
            double r24 = r0 - r2
            r11 = r24
            r14 = r11
            r12 = r24
            r10 = r9
            r8 = r7
            goto L_0x012a
        L_0x0128:
            r12 = r47
        L_0x012a:
            r6 = r20
            r51 = r2
            r2 = r4
            r4 = r0
            r0 = r51
            goto L_0x0015
        L_0x0134:
            r43 = r0
            r45 = r2
            r30 = r4
            r49 = r6
            r6 = r53
        L_0x013e:
            return r30
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.analysis.solvers.BrentSolver.brent(double, double, double, double):double");
    }
}
