package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.util.Incrementor;

@Deprecated
public class BracketFinder {
    private static final double EPS_MIN = 1.0E-21d;
    private static final double GOLD = 1.618034d;
    private final Incrementor evaluations;
    private double fHi;
    private double fLo;
    private double fMid;
    private final double growLimit;

    /* renamed from: hi */
    private double f736hi;

    /* renamed from: lo */
    private double f737lo;
    private double mid;

    public BracketFinder() {
        this(100.0d, 50);
    }

    public BracketFinder(double growLimit2, int maxEvaluations) {
        this.evaluations = new Incrementor();
        if (growLimit2 <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(growLimit2));
        } else if (maxEvaluations <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(maxEvaluations));
        } else {
            this.growLimit = growLimit2;
            this.evaluations.setMaximalCount(maxEvaluations);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c7, code lost:
        r12 = r3;
        r14 = r26;
        r3 = r30;
        r5 = r32;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0154, code lost:
        r14 = r16;
        r3 = r30;
        r5 = r32;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void search(org.apache.commons.math3.analysis.UnivariateFunction r40, org.apache.commons.math3.optimization.GoalType r41, double r42, double r44) {
        /*
            r39 = this;
            r0 = r39
            r1 = r40
            org.apache.commons.math3.util.Incrementor r2 = r0.evaluations
            r2.resetCount()
            org.apache.commons.math3.optimization.GoalType r2 = org.apache.commons.math3.optimization.GoalType.MINIMIZE
            r3 = r41
            if (r3 != r2) goto L_0x0011
            r2 = 1
            goto L_0x0012
        L_0x0011:
            r2 = 0
        L_0x0012:
            r4 = r42
            double r6 = r0.eval(r1, r4)
            r8 = r44
            double r10 = r0.eval(r1, r8)
            if (r2 == 0) goto L_0x0025
            int r12 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r12 >= 0) goto L_0x002f
            goto L_0x0029
        L_0x0025:
            int r12 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r12 <= 0) goto L_0x002f
        L_0x0029:
            r12 = r4
            r4 = r8
            r8 = r12
            r12 = r6
            r6 = r10
            r10 = r12
        L_0x002f:
            r12 = 0
            double r12 = r8 - r4
            r14 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            double r12 = r12 * r14
            double r12 = r12 + r8
            double r16 = r0.eval(r1, r12)
        L_0x003e:
            if (r2 == 0) goto L_0x004b
            int r18 = (r16 > r10 ? 1 : (r16 == r10 ? 0 : -1))
            if (r18 >= 0) goto L_0x0045
            goto L_0x004f
        L_0x0045:
            r30 = r4
            r32 = r6
            goto L_0x0154
        L_0x004b:
            int r18 = (r16 > r10 ? 1 : (r16 == r10 ? 0 : -1))
            if (r18 <= 0) goto L_0x0150
        L_0x004f:
            r18 = 0
            double r18 = r8 - r4
            double r20 = r10 - r16
            double r18 = r18 * r20
            double r20 = r8 - r12
            double r22 = r10 - r6
            double r20 = r20 * r22
            double r14 = r20 - r18
            double r22 = org.apache.commons.math3.util.FastMath.abs(r14)
            r26 = 4292743757239851855(0x3b92e3b40a0e9b4f, double:1.0E-21)
            int r22 = (r22 > r26 ? 1 : (r22 == r26 ? 0 : -1))
            if (r22 >= 0) goto L_0x0072
            r22 = 4297247356867222351(0x3ba2e3b40a0e9b4f, double:2.0E-21)
            goto L_0x0076
        L_0x0072:
            r22 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r22 = r22 * r14
        L_0x0076:
            r26 = 0
            double r26 = r8 - r12
            double r26 = r26 * r20
            double r28 = r8 - r4
            double r28 = r28 * r18
            double r26 = r26 - r28
            double r26 = r26 / r22
            r30 = r4
            double r3 = r8 - r26
            r32 = r6
            double r5 = r0.growLimit
            double r26 = r12 - r8
            double r5 = r5 * r26
            double r5 = r5 + r8
            double r26 = r3 - r12
            double r28 = r8 - r3
            double r26 = r26 * r28
            r28 = 0
            int r7 = (r26 > r28 ? 1 : (r26 == r28 ? 0 : -1))
            if (r7 <= 0) goto L_0x00e4
            double r26 = r0.eval(r1, r3)
            if (r2 == 0) goto L_0x00a8
            int r7 = (r26 > r16 ? 1 : (r26 == r16 ? 0 : -1))
            if (r7 >= 0) goto L_0x00bc
            goto L_0x00ac
        L_0x00a8:
            int r7 = (r26 > r16 ? 1 : (r26 == r16 ? 0 : -1))
            if (r7 <= 0) goto L_0x00bc
        L_0x00ac:
            r24 = r8
            r8 = r3
            r28 = r10
            r10 = r26
            r14 = r16
            r3 = r24
            r5 = r28
            goto L_0x015a
        L_0x00bc:
            if (r2 == 0) goto L_0x00c3
            int r7 = (r26 > r10 ? 1 : (r26 == r10 ? 0 : -1))
            if (r7 <= 0) goto L_0x00d3
            goto L_0x00c7
        L_0x00c3:
            int r7 = (r26 > r10 ? 1 : (r26 == r10 ? 0 : -1))
            if (r7 >= 0) goto L_0x00d3
        L_0x00c7:
            r12 = r3
            r16 = r26
            r14 = r16
            r3 = r30
            r5 = r32
            goto L_0x015a
        L_0x00d3:
            r7 = 0
            double r28 = r12 - r8
            r24 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            double r28 = r28 * r24
            double r3 = r12 + r28
            double r26 = r0.eval(r1, r3)
            goto L_0x00f4
        L_0x00e4:
            r7 = 0
            double r26 = r3 - r5
            double r34 = r5 - r12
            double r26 = r26 * r34
            int r7 = (r26 > r28 ? 1 : (r26 == r28 ? 0 : -1))
            if (r7 < 0) goto L_0x00fa
            r3 = r5
            double r26 = r0.eval(r1, r3)
        L_0x00f4:
            r24 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            goto L_0x013b
        L_0x00fa:
            r7 = 0
            double r26 = r3 - r5
            double r34 = r12 - r3
            double r26 = r26 * r34
            int r7 = (r26 > r28 ? 1 : (r26 == r28 ? 0 : -1))
            if (r7 <= 0) goto L_0x012b
            double r26 = r0.eval(r1, r3)
            if (r2 == 0) goto L_0x0110
            int r7 = (r26 > r16 ? 1 : (r26 == r16 ? 0 : -1))
            if (r7 >= 0) goto L_0x00f4
            goto L_0x0114
        L_0x0110:
            int r7 = (r26 > r16 ? 1 : (r26 == r16 ? 0 : -1))
            if (r7 <= 0) goto L_0x00f4
        L_0x0114:
            r8 = r12
            r12 = r3
            r7 = 0
            double r28 = r12 - r8
            r24 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            double r28 = r28 * r24
            double r3 = r12 + r28
            r10 = r16
            r16 = r26
            double r26 = r0.eval(r1, r3)
            goto L_0x00f4
        L_0x012b:
            r7 = 0
            double r26 = r12 - r8
            r24 = 4609965796492119705(0x3ff9e3779e9d0e99, double:1.618034)
            double r26 = r26 * r24
            double r3 = r12 + r26
            double r26 = r0.eval(r1, r3)
        L_0x013b:
            r28 = r8
            r30 = r10
            r8 = r12
            r10 = r16
            r12 = r3
            r16 = r26
            r14 = r24
            r4 = r28
            r6 = r30
            r3 = r41
            goto L_0x003e
        L_0x0150:
            r30 = r4
            r32 = r6
        L_0x0154:
            r14 = r16
            r3 = r30
            r5 = r32
        L_0x015a:
            r0.f737lo = r3
            r0.fLo = r5
            r0.mid = r8
            r0.fMid = r10
            r0.f736hi = r12
            r0.fHi = r14
            r36 = r2
            double r1 = r0.f737lo
            r37 = r3
            double r3 = r0.f736hi
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r1 <= 0) goto L_0x0182
            double r1 = r0.f737lo
            double r3 = r0.f736hi
            r0.f737lo = r3
            r0.f736hi = r1
            double r1 = r0.fLo
            double r3 = r0.fHi
            r0.fLo = r3
            r0.fHi = r1
        L_0x0182:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.univariate.BracketFinder.search(org.apache.commons.math3.analysis.UnivariateFunction, org.apache.commons.math3.optimization.GoalType, double, double):void");
    }

    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public double getLo() {
        return this.f737lo;
    }

    public double getFLo() {
        return this.fLo;
    }

    public double getHi() {
        return this.f736hi;
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
            this.evaluations.incrementCount();
            return f.value(x);
        } catch (MaxCountExceededException e) {
            throw new TooManyEvaluationsException(e.getMax());
        }
    }
}
