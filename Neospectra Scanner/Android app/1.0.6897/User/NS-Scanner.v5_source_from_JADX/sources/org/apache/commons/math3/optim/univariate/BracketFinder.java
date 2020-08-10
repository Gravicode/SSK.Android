package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.IntegerSequence.Incrementor;

public class BracketFinder {
    private static final double EPS_MIN = 1.0E-21d;
    private static final double GOLD = 1.618034d;
    private Incrementor evaluations;
    private double fHi;
    private double fLo;
    private double fMid;
    private final double growLimit;

    /* renamed from: hi */
    private double f716hi;

    /* renamed from: lo */
    private double f717lo;
    private double mid;

    public BracketFinder() {
        this(100.0d, 500);
    }

    public BracketFinder(double growLimit2, int maxEvaluations) {
        if (growLimit2 <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(growLimit2));
        } else if (maxEvaluations <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxEvaluations));
        } else {
            this.growLimit = growLimit2;
            this.evaluations = Incrementor.create().withMaximalCount(maxEvaluations);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00ad, code lost:
        r15 = r9;
        r9 = r3;
        r7 = r11;
        r11 = r25;
        r3 = r15;
        r5 = r17;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0138, code lost:
        r5 = r17;
        r3 = r31;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ba  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00bf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void search(org.apache.commons.math3.analysis.UnivariateFunction r39, org.apache.commons.math3.optim.nonlinear.scalar.GoalType r40, double r41, double r43) {
        /*
            r38 = this;
            r0 = r38
            r1 = r39
            org.apache.commons.math3.util.IntegerSequence$Incrementor r2 = r0.evaluations
            r3 = 0
            org.apache.commons.math3.util.IntegerSequence$Incrementor r2 = r2.withStart(r3)
            r0.evaluations = r2
            org.apache.commons.math3.optim.nonlinear.scalar.GoalType r2 = org.apache.commons.math3.optim.nonlinear.scalar.GoalType.MINIMIZE
            r4 = r40
            if (r4 != r2) goto L_0x0015
            r3 = 1
        L_0x0015:
            r2 = r3
            r5 = r41
            double r7 = r0.eval(r1, r5)
            r9 = r43
            double r11 = r0.eval(r1, r9)
            if (r2 == 0) goto L_0x0029
            int r3 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r3 >= 0) goto L_0x0033
            goto L_0x002d
        L_0x0029:
            int r3 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
            if (r3 <= 0) goto L_0x0033
        L_0x002d:
            r13 = r5
            r5 = r9
            r9 = r13
            r13 = r7
            r7 = r11
            r11 = r13
        L_0x0033:
            r3 = 0
            double r13 = r9 - r5
            r15 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            double r13 = r13 * r15
            double r13 = r13 + r9
            double r17 = r0.eval(r1, r13)
        L_0x0042:
            if (r2 == 0) goto L_0x004d
            int r3 = (r17 > r11 ? 1 : (r17 == r11 ? 0 : -1))
            if (r3 >= 0) goto L_0x0049
            goto L_0x0051
        L_0x0049:
            r31 = r5
            goto L_0x0138
        L_0x004d:
            int r3 = (r17 > r11 ? 1 : (r17 == r11 ? 0 : -1))
            if (r3 <= 0) goto L_0x0136
        L_0x0051:
            r3 = 0
            double r19 = r9 - r5
            double r21 = r11 - r17
            double r19 = r19 * r21
            double r21 = r9 - r13
            double r23 = r11 - r7
            double r21 = r21 * r23
            double r3 = r21 - r19
            double r23 = org.apache.commons.math3.util.FastMath.abs(r3)
            r25 = 4292743757239851855(0x3b92e3b40a0e9b4f, double:1.0E-21)
            int r23 = (r23 > r25 ? 1 : (r23 == r25 ? 0 : -1))
            if (r23 >= 0) goto L_0x0073
            r23 = 4297247356867222351(0x3ba2e3b40a0e9b4f, double:2.0E-21)
            goto L_0x0077
        L_0x0073:
            r23 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r23 = r23 * r3
        L_0x0077:
            r25 = 0
            double r25 = r9 - r13
            double r25 = r25 * r21
            double r27 = r9 - r5
            double r27 = r27 * r19
            double r25 = r25 - r27
            double r25 = r25 / r23
            r29 = r3
            double r3 = r9 - r25
            r31 = r5
            double r5 = r0.growLimit
            double r25 = r13 - r9
            double r5 = r5 * r25
            double r5 = r5 + r9
            double r25 = r3 - r13
            double r27 = r9 - r3
            double r25 = r25 * r27
            r27 = 0
            int r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1))
            if (r25 <= 0) goto L_0x00da
            double r25 = r0.eval(r1, r3)
            if (r2 == 0) goto L_0x00a9
            int r27 = (r25 > r17 ? 1 : (r25 == r17 ? 0 : -1))
            if (r27 >= 0) goto L_0x00b8
            goto L_0x00ad
        L_0x00a9:
            int r27 = (r25 > r17 ? 1 : (r25 == r17 ? 0 : -1))
            if (r27 <= 0) goto L_0x00b8
        L_0x00ad:
            r15 = r9
            r9 = r3
            r7 = r11
            r11 = r25
            r3 = r15
            r5 = r17
            goto L_0x013c
        L_0x00b8:
            if (r2 == 0) goto L_0x00bf
            int r27 = (r25 > r11 ? 1 : (r25 == r11 ? 0 : -1))
            if (r27 <= 0) goto L_0x00cd
            goto L_0x00c3
        L_0x00bf:
            int r27 = (r25 > r11 ? 1 : (r25 == r11 ? 0 : -1))
            if (r27 >= 0) goto L_0x00cd
        L_0x00c3:
            r13 = r3
            r17 = r25
            r5 = r17
            r3 = r31
            goto L_0x013c
        L_0x00cd:
            r27 = 0
            double r27 = r13 - r9
            double r27 = r27 * r15
            double r3 = r13 + r27
            double r25 = r0.eval(r1, r3)
            goto L_0x0126
        L_0x00da:
            r25 = 0
            double r25 = r3 - r5
            double r33 = r5 - r13
            double r25 = r25 * r33
            int r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1))
            if (r25 < 0) goto L_0x00ec
            r3 = r5
            double r25 = r0.eval(r1, r3)
            goto L_0x0126
        L_0x00ec:
            r25 = 0
            double r25 = r3 - r5
            double r33 = r13 - r3
            double r25 = r25 * r33
            int r25 = (r25 > r27 ? 1 : (r25 == r27 ? 0 : -1))
            if (r25 <= 0) goto L_0x011a
            double r25 = r0.eval(r1, r3)
            if (r2 == 0) goto L_0x0103
            int r27 = (r25 > r17 ? 1 : (r25 == r17 ? 0 : -1))
            if (r27 >= 0) goto L_0x0126
            goto L_0x0107
        L_0x0103:
            int r27 = (r25 > r17 ? 1 : (r25 == r17 ? 0 : -1))
            if (r27 <= 0) goto L_0x0126
        L_0x0107:
            r9 = r13
            r13 = r3
            r27 = 0
            double r27 = r13 - r9
            double r27 = r27 * r15
            double r3 = r13 + r27
            r11 = r17
            r17 = r25
            double r25 = r0.eval(r1, r3)
            goto L_0x0126
        L_0x011a:
            r25 = 0
            double r25 = r13 - r9
            double r25 = r25 * r15
            double r3 = r13 + r25
            double r25 = r0.eval(r1, r3)
        L_0x0126:
            r27 = r9
            r7 = r11
            r9 = r13
            r11 = r17
            r13 = r3
            r17 = r25
            r5 = r27
            r4 = r40
            goto L_0x0042
        L_0x0136:
            r31 = r5
        L_0x0138:
            r5 = r17
            r3 = r31
        L_0x013c:
            r0.f717lo = r3
            r0.fLo = r7
            r0.mid = r9
            r0.fMid = r11
            r0.f716hi = r13
            r0.fHi = r5
            r35 = r2
            double r1 = r0.f717lo
            r36 = r3
            double r3 = r0.f716hi
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0164
            double r1 = r0.f717lo
            double r3 = r0.f716hi
            r0.f717lo = r3
            r0.f716hi = r1
            double r1 = r0.fLo
            double r3 = r0.fHi
            r0.fLo = r3
            r0.fHi = r1
        L_0x0164:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optim.univariate.BracketFinder.search(org.apache.commons.math3.analysis.UnivariateFunction, org.apache.commons.math3.optim.nonlinear.scalar.GoalType, double, double):void");
    }

    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public double getLo() {
        return this.f717lo;
    }

    public double getFLo() {
        return this.fLo;
    }

    public double getHi() {
        return this.f716hi;
    }

    public double getFHi() {
        return this.fHi;
    }

    public double getMid() {
        return this.mid;
    }

    public double getFMid() {
        return this.fMid;
    }

    private double eval(UnivariateFunction f, double x) {
        try {
            this.evaluations.increment();
            return f.value(x);
        } catch (MaxCountExceededException e) {
            throw new TooManyEvaluationsException(e.getMax());
        }
    }
}
