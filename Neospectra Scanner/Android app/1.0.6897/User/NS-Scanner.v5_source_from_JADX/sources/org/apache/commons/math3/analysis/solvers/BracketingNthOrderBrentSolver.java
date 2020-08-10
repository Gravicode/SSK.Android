package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;

public class BracketingNthOrderBrentSolver extends AbstractUnivariateSolver implements BracketedUnivariateSolver<UnivariateFunction> {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private static final int DEFAULT_MAXIMAL_ORDER = 5;
    private static final int MAXIMAL_AGING = 2;
    private static final double REDUCTION_FACTOR = 0.0625d;
    private AllowedSolution allowed;
    private final int maximalOrder;

    public BracketingNthOrderBrentSolver() {
        this(1.0E-6d, 5);
    }

    public BracketingNthOrderBrentSolver(double absoluteAccuracy, int maximalOrder2) throws NumberIsTooSmallException {
        super(absoluteAccuracy);
        if (maximalOrder2 < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder2), Integer.valueOf(2), true);
        }
        this.maximalOrder = maximalOrder2;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, int maximalOrder2) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy);
        if (maximalOrder2 < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder2), Integer.valueOf(2), true);
        }
        this.maximalOrder = maximalOrder2;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public BracketingNthOrderBrentSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, int maximalOrder2) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        if (maximalOrder2 < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder2), Integer.valueOf(2), true);
        }
        this.maximalOrder = maximalOrder2;
        this.allowed = AllowedSolution.ANY_SIDE;
    }

    public int getMaximalOrder() {
        return this.maximalOrder;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0167  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x016a  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x018c  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x01a9  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01f3  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x020e  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01a8 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public double doSolve() throws org.apache.commons.math3.exception.TooManyEvaluationsException, org.apache.commons.math3.exception.NumberIsTooLargeException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r53 = this;
            r7 = r53
            int r0 = r7.maximalOrder
            r8 = 1
            int r0 = r0 + r8
            double[] r9 = new double[r0]
            int r0 = r7.maximalOrder
            int r0 = r0 + r8
            double[] r10 = new double[r0]
            double r0 = r53.getMin()
            r11 = 0
            r9[r11] = r0
            double r0 = r53.getStartValue()
            r9[r8] = r0
            double r0 = r53.getMax()
            r12 = 2
            r9[r12] = r0
            r1 = r9[r11]
            r3 = r9[r8]
            r5 = r9[r12]
            r0 = r7
            r0.verifySequence(r1, r3, r5)
            r0 = r9[r8]
            double r0 = r7.computeObjectiveValue(r0)
            r10[r8] = r0
            r0 = r10[r8]
            r13 = 0
            boolean r0 = org.apache.commons.math3.util.Precision.equals(r0, r13, r8)
            if (r0 == 0) goto L_0x0040
            r0 = r9[r8]
            return r0
        L_0x0040:
            r0 = r9[r11]
            double r0 = r7.computeObjectiveValue(r0)
            r10[r11] = r0
            r0 = r10[r11]
            boolean r0 = org.apache.commons.math3.util.Precision.equals(r0, r13, r8)
            if (r0 == 0) goto L_0x0053
            r0 = r9[r11]
            return r0
        L_0x0053:
            r0 = r10[r11]
            r2 = r10[r8]
            double r0 = r0 * r2
            int r0 = (r0 > r13 ? 1 : (r0 == r13 ? 0 : -1))
            if (r0 >= 0) goto L_0x0060
            r0 = 2
            r1 = 1
        L_0x005f:
            goto L_0x0080
        L_0x0060:
            r0 = r9[r12]
            double r0 = r7.computeObjectiveValue(r0)
            r10[r12] = r0
            r0 = r10[r12]
            boolean r0 = org.apache.commons.math3.util.Precision.equals(r0, r13, r8)
            if (r0 == 0) goto L_0x0073
            r0 = r9[r12]
            return r0
        L_0x0073:
            r0 = r10[r8]
            r2 = r10[r12]
            double r0 = r0 * r2
            int r0 = (r0 > r13 ? 1 : (r0 == r13 ? 0 : -1))
            if (r0 >= 0) goto L_0x0275
            r0 = 3
            r1 = 2
            goto L_0x005f
        L_0x0080:
            int r2 = r9.length
            double[] r15 = new double[r2]
            int r2 = r1 + -1
            r2 = r9[r2]
            int r4 = r1 + -1
            r4 = r10[r4]
            double r16 = org.apache.commons.math3.util.FastMath.abs(r4)
            r6 = 0
            r18 = r9[r1]
            r13 = r10[r1]
            double r22 = org.apache.commons.math3.util.FastMath.abs(r13)
            r24 = r0
            r25 = r1
            r11 = r6
            r0 = 0
            r49 = r2
            r1 = r22
            r51 = r4
            r5 = r18
            r18 = r13
            r3 = r16
            r13 = r49
            r16 = r51
        L_0x00af:
            double r22 = r53.getAbsoluteAccuracy()
            double r26 = r53.getRelativeAccuracy()
            r28 = r9
            double r8 = org.apache.commons.math3.util.FastMath.abs(r13)
            r29 = r13
            double r12 = org.apache.commons.math3.util.FastMath.abs(r5)
            double r8 = org.apache.commons.math3.util.FastMath.max(r8, r12)
            double r26 = r26 * r8
            double r22 = r22 + r26
            double r8 = r5 - r29
            int r8 = (r8 > r22 ? 1 : (r8 == r22 ? 0 : -1))
            if (r8 <= 0) goto L_0x0230
            double r8 = org.apache.commons.math3.util.FastMath.max(r3, r1)
            double r12 = r53.getFunctionValueAccuracy()
            int r8 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
            if (r8 >= 0) goto L_0x00ed
            r26 = r0
            r38 = r1
            r31 = r5
            r1 = r24
            r0 = r25
            r14 = r28
            r27 = r3
            goto L_0x023e
        L_0x00ed:
            r12 = 2
            if (r11 < r12) goto L_0x0110
            int r12 = r11 + -2
            r13 = 1
            int r14 = r13 << r12
            int r14 = r14 - r13
            double r13 = (double) r14
            int r8 = r12 + 1
            double r8 = (double) r8
            double r26 = r13 * r16
            r31 = 4589168020290535424(0x3fb0000000000000, double:0.0625)
            double r31 = r31 * r8
            double r31 = r31 * r18
            double r26 = r26 - r31
            double r31 = r13 + r8
            double r26 = r26 / r31
            r35 = r0
            r33 = r1
        L_0x010d:
            r1 = r26
            goto L_0x0136
        L_0x0110:
            r8 = 2
            if (r0 < r8) goto L_0x0130
            int r8 = r0 + -2
            int r9 = r8 + 1
            double r12 = (double) r9
            r9 = 1
            int r14 = r9 << r8
            int r14 = r14 - r9
            r35 = r0
            r33 = r1
            double r0 = (double) r14
            double r26 = r0 * r18
            r31 = 4589168020290535424(0x3fb0000000000000, double:0.0625)
            double r31 = r31 * r12
            double r31 = r31 * r16
            double r26 = r26 - r31
            double r31 = r12 + r0
            double r26 = r26 / r31
            goto L_0x010d
        L_0x0130:
            r35 = r0
            r33 = r1
            r1 = 0
        L_0x0136:
            r8 = r33
            r0 = 0
            r12 = r0
            r0 = r24
        L_0x013c:
            r13 = r0
            int r0 = r13 - r12
            r14 = r28
            java.lang.System.arraycopy(r14, r12, r15, r12, r0)
            r26 = r35
            r0 = r7
            r27 = r3
            r3 = r15
            r4 = r10
            r31 = r5
            r5 = r12
            r6 = r13
            double r3 = r0.guessX(r1, r3, r4, r5, r6)
            int r0 = (r3 > r29 ? 1 : (r3 == r29 ? 0 : -1))
            if (r0 <= 0) goto L_0x015f
            int r0 = (r3 > r31 ? 1 : (r3 == r31 ? 0 : -1))
            if (r0 < 0) goto L_0x015c
            goto L_0x015f
        L_0x015c:
            r0 = r25
            goto L_0x016e
        L_0x015f:
            r0 = r25
            int r5 = r0 - r12
            int r6 = r13 - r0
            if (r5 < r6) goto L_0x016a
            int r12 = r12 + 1
            goto L_0x016c
        L_0x016a:
            int r13 = r13 + -1
        L_0x016c:
            r3 = 9221120237041090560(0x7ff8000000000000, double:NaN)
        L_0x016e:
            boolean r5 = java.lang.Double.isNaN(r3)
            if (r5 == 0) goto L_0x0186
            int r5 = r13 - r12
            r6 = 1
            if (r5 > r6) goto L_0x017a
            goto L_0x0186
        L_0x017a:
            r25 = r0
            r0 = r13
            r35 = r26
            r3 = r27
            r5 = r31
            r28 = r14
            goto L_0x013c
        L_0x0186:
            boolean r5 = java.lang.Double.isNaN(r3)
            if (r5 == 0) goto L_0x0197
            r5 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r33 = r31 - r29
            double r33 = r33 * r5
            double r3 = r29 + r33
            int r12 = r0 + -1
            r13 = r0
        L_0x0197:
            double r5 = r7.computeObjectiveValue(r3)
            r36 = r1
            r38 = r8
            r1 = 1
            r8 = 0
            boolean r2 = org.apache.commons.math3.util.Precision.equals(r5, r8, r1)
            if (r2 == 0) goto L_0x01a9
            return r3
        L_0x01a9:
            r1 = r24
            r2 = 2
            if (r1 <= r2) goto L_0x01c0
            int r2 = r13 - r12
            if (r2 == r1) goto L_0x01c0
            int r1 = r13 - r12
            r2 = 0
            java.lang.System.arraycopy(r14, r12, r14, r2, r1)
            java.lang.System.arraycopy(r10, r12, r10, r2, r1)
            int r25 = r0 - r12
        L_0x01bd:
            r0 = r25
            goto L_0x01d6
        L_0x01c0:
            int r2 = r14.length
            if (r1 != r2) goto L_0x01d6
            int r1 = r1 + -1
            int r2 = r14.length
            r8 = 1
            int r2 = r2 + r8
            r9 = 2
            int r2 = r2 / r9
            if (r0 < r2) goto L_0x01d6
            r2 = 0
            java.lang.System.arraycopy(r14, r8, r14, r2, r1)
            java.lang.System.arraycopy(r10, r8, r10, r2, r1)
            int r25 = r0 + -1
            goto L_0x01bd
        L_0x01d6:
            int r2 = r0 + 1
            int r8 = r1 - r0
            java.lang.System.arraycopy(r14, r0, r14, r2, r8)
            r14[r0] = r3
            int r2 = r0 + 1
            int r8 = r1 - r0
            java.lang.System.arraycopy(r10, r0, r10, r2, r8)
            r10[r0] = r5
            r2 = 1
            int r24 = r1 + 1
            double r8 = r5 * r16
            r20 = 0
            int r1 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1))
            if (r1 > 0) goto L_0x020e
            r8 = r3
            r40 = r5
            r42 = r3
            r2 = r40
            double r18 = org.apache.commons.math3.util.FastMath.abs(r2)
            int r11 = r11 + 1
            r1 = 0
            r25 = r0
            r0 = r1
            r5 = r8
            r49 = r2
            r3 = r27
            r1 = r18
            r18 = r49
            goto L_0x0229
        L_0x020e:
            r42 = r3
            r1 = r42
            r3 = r5
            double r8 = org.apache.commons.math3.util.FastMath.abs(r3)
            r11 = 0
            int r16 = r26 + 1
            int r0 = r0 + 1
            r25 = r0
            r29 = r1
            r0 = r16
            r5 = r31
            r1 = r38
            r16 = r3
            r3 = r8
        L_0x0229:
            r9 = r14
            r13 = r29
            r8 = 1
            r12 = 2
            goto L_0x00af
        L_0x0230:
            r26 = r0
            r38 = r1
            r31 = r5
            r1 = r24
            r0 = r25
            r14 = r28
            r27 = r3
        L_0x023e:
            int[] r2 = org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver.C09201.f535xe6390080
            org.apache.commons.math3.analysis.solvers.AllowedSolution r3 = r7.allowed
            int r3 = r3.ordinal()
            r2 = r2[r3]
            switch(r2) {
                case 1: goto L_0x026b;
                case 2: goto L_0x026a;
                case 3: goto L_0x0269;
                case 4: goto L_0x025d;
                case 5: goto L_0x0251;
                default: goto L_0x024b;
            }
        L_0x024b:
            org.apache.commons.math3.exception.MathInternalError r2 = new org.apache.commons.math3.exception.MathInternalError
            r2.<init>()
            throw r2
        L_0x0251:
            r2 = 0
            int r2 = (r16 > r2 ? 1 : (r16 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x025a
            r2 = r31
            goto L_0x025c
        L_0x025a:
            r2 = r29
        L_0x025c:
            return r2
        L_0x025d:
            r2 = 0
            int r2 = (r16 > r2 ? 1 : (r16 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x0266
            r2 = r29
            goto L_0x0268
        L_0x0266:
            r2 = r31
        L_0x0268:
            return r2
        L_0x0269:
            return r31
        L_0x026a:
            return r29
        L_0x026b:
            int r2 = (r27 > r38 ? 1 : (r27 == r38 ? 0 : -1))
            if (r2 >= 0) goto L_0x0272
            r2 = r29
            goto L_0x0274
        L_0x0272:
            r2 = r31
        L_0x0274:
            return r2
        L_0x0275:
            r14 = r9
            org.apache.commons.math3.exception.NoBracketingException r0 = new org.apache.commons.math3.exception.NoBracketingException
            r1 = 0
            r41 = r14[r1]
            r2 = 2
            r43 = r14[r2]
            r45 = r10[r1]
            r47 = r10[r2]
            r40 = r0
            r40.<init>(r41, r43, r45, r47)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver.doSolve():double");
    }

    private double guessX(double targetY, double[] x, double[] y, int start, int end) {
        for (int i = start; i < end - 1; i++) {
            int delta = (i + 1) - start;
            for (int j = end - 1; j > i; j--) {
                x[j] = (x[j] - x[j - 1]) / (y[j] - y[j - delta]);
            }
        }
        double x0 = 0.0d;
        for (int j2 = end - 1; j2 >= start; j2--) {
            x0 = x[j2] + ((targetY - y[j2]) * x0);
        }
        return x0;
    }

    public double solve(int maxEval, UnivariateFunction f, double min, double max, AllowedSolution allowedSolution) throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        this.allowed = allowedSolution;
        return super.solve(maxEval, f, min, max);
    }

    public double solve(int maxEval, UnivariateFunction f, double min, double max, double startValue, AllowedSolution allowedSolution) throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        this.allowed = allowedSolution;
        return super.solve(maxEval, f, min, max, startValue);
    }
}
