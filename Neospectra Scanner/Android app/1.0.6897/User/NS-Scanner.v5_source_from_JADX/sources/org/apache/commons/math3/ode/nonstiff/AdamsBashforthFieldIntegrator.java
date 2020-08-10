package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.FieldMatrix;

public class AdamsBashforthFieldIntegrator<T extends RealFieldElement<T>> extends AdamsFieldIntegrator<T> {
    private static final String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(field, METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(field, METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    private T errorEstimation(T[] previousState, T[] predictedState, T[] predictedScaled, FieldMatrix<T> predictedNordsieck) {
        T error = (RealFieldElement) getField().getZero();
        for (int i = 0; i < this.mainSetDimension; i++) {
            T yScale = (RealFieldElement) predictedState[i].abs();
            T tol = (RealFieldElement) (this.vecAbsoluteTolerance == null ? ((RealFieldElement) yScale.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : ((RealFieldElement) yScale.multiply(this.vecRelativeTolerance[i])).add(this.vecAbsoluteTolerance[i]));
            T variation = (RealFieldElement) getField().getZero();
            int sign = predictedNordsieck.getRowDimension() % 2 == 0 ? -1 : 1;
            int k = predictedNordsieck.getRowDimension() - 1;
            while (true) {
                int k2 = k;
                if (k2 < 0) {
                    break;
                }
                variation = (RealFieldElement) variation.add(((RealFieldElement) predictedNordsieck.getEntry(k2, i)).multiply(sign));
                sign = -sign;
                k = k2 - 1;
            }
            T ratio = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) predictedState[i].subtract(previousState[i])).add((RealFieldElement) variation.subtract(predictedScaled[i]))).divide(tol);
            error = (RealFieldElement) error.add(ratio.multiply(ratio));
        }
        return (RealFieldElement) ((RealFieldElement) error.divide((double) this.mainSetDimension)).sqrt();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x01db, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r11.subtract(r3)).getReal() >= 0.0d) goto L_0x01dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x01df, code lost:
        r1 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x01ef, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r11.subtract(r3)).getReal() <= 0.0d) goto L_0x01dd;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.ode.FieldODEStateAndDerivative<T> integrate(org.apache.commons.math3.ode.FieldExpandableODE<T> r31, org.apache.commons.math3.ode.FieldODEState<T> r32, T r33) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r30 = this;
            r0 = r30
            r1 = r31
            r2 = r32
            r3 = r33
            r0.sanityChecks(r2, r3)
            org.apache.commons.math3.RealFieldElement r4 = r32.getTime()
            org.apache.commons.math3.ode.FieldEquationsMapper r5 = r31.getMapper()
            org.apache.commons.math3.RealFieldElement[] r5 = r5.mapState(r2)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r6 = r0.initIntegration(r1, r4, r5, r3)
            r0.setStepStart(r6)
            org.apache.commons.math3.RealFieldElement r6 = r32.getTime()
            java.lang.Object r6 = r3.subtract(r6)
            org.apache.commons.math3.RealFieldElement r6 = (org.apache.commons.math3.RealFieldElement) r6
            double r6 = r6.getReal()
            r8 = 0
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            r10 = 0
            if (r6 <= 0) goto L_0x0035
            r6 = 1
            goto L_0x0036
        L_0x0035:
            r6 = 0
        L_0x0036:
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r11 = r30.getStepStart()
            r0.start(r1, r11, r3)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r15 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r11 = r15.getTime()
            org.apache.commons.math3.RealFieldElement r12 = r30.getStepSize()
            java.lang.Object r11 = r11.add(r12)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            org.apache.commons.math3.RealFieldElement r12 = r30.getStepSize()
            org.apache.commons.math3.RealFieldElement[] r13 = r0.scaled
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r14 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r11 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r15, r11, r12, r13, r14)
            r0.setIsLastStep(r10)
        L_0x005e:
            r12 = 0
            org.apache.commons.math3.Field r13 = r30.getField()
            int r14 = r5.length
            java.lang.Object[] r13 = org.apache.commons.math3.util.MathArrays.buildArray(r13, r14)
            r14 = r13
            org.apache.commons.math3.RealFieldElement[] r14 = (org.apache.commons.math3.RealFieldElement[]) r14
            r13 = 0
            org.apache.commons.math3.Field r7 = r30.getField()
            java.lang.Object r7 = r7.getZero()
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r20 = r11
            r10 = 4621819117588971520(0x4024000000000000, double:10.0)
            java.lang.Object r7 = r7.add(r10)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r10 = r20
            r29 = r13
            r13 = r12
            r12 = r29
        L_0x0087:
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r11 = r7.subtract(r8)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            double r16 = r11.getReal()
            r18 = 0
            int r11 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r11 < 0) goto L_0x0131
            org.apache.commons.math3.RealFieldElement[] r13 = r10.getState()
            org.apache.commons.math3.RealFieldElement r11 = r10.getTime()
            org.apache.commons.math3.RealFieldElement[] r11 = r0.computeDerivatives(r11, r13)
            r16 = 0
        L_0x00a7:
            r21 = r16
            int r8 = r14.length
            r9 = r21
            if (r9 >= r8) goto L_0x00c3
            org.apache.commons.math3.RealFieldElement r8 = r30.getStepSize()
            r2 = r11[r9]
            java.lang.Object r2 = r8.multiply(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r14[r9] = r2
            int r16 = r9 + 1
            r2 = r32
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            goto L_0x00a7
        L_0x00c3:
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r2 = r0.nordsieck
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r12 = r0.updateHighOrderDerivativesPhase1(r2)
            org.apache.commons.math3.RealFieldElement[] r2 = r0.scaled
            r0.updateHighOrderDerivativesPhase2(r2, r14, r12)
            org.apache.commons.math3.RealFieldElement r7 = r0.errorEstimation(r5, r13, r14, r12)
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r2 = r7.subtract(r8)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double r8 = r2.getReal()
            r16 = 0
            int r2 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1))
            if (r2 < 0) goto L_0x0123
            org.apache.commons.math3.RealFieldElement r2 = r0.computeStepGrowShrinkFactor(r7)
            org.apache.commons.math3.RealFieldElement r8 = r30.getStepSize()
            java.lang.Object r8 = r8.multiply(r2)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r9 = 0
            org.apache.commons.math3.RealFieldElement r8 = r0.filterStep(r8, r6, r9)
            r0.rescale(r8)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r8 = r30.getStepStart()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r9 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r9 = r9.getTime()
            r22 = r2
            org.apache.commons.math3.RealFieldElement r2 = r30.getStepSize()
            java.lang.Object r2 = r9.add(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            org.apache.commons.math3.RealFieldElement r9 = r30.getStepSize()
            r23 = r4
            org.apache.commons.math3.RealFieldElement[] r4 = r0.scaled
            r24 = r7
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r7 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r10 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r8, r2, r9, r4, r7)
            goto L_0x0127
        L_0x0123:
            r23 = r4
            r24 = r7
        L_0x0127:
            r4 = r23
            r7 = r24
            r2 = r32
            r8 = 0
            goto L_0x0087
        L_0x0131:
            r23 = r4
            org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator r2 = new org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator
            org.apache.commons.math3.RealFieldElement r4 = r30.getStepSize()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r17 = r30.getStepStart()
            org.apache.commons.math3.ode.FieldEquationsMapper r19 = r31.getMapper()
            r11 = r2
            r8 = r12
            r12 = r4
            r4 = r13
            r13 = r10
            r9 = r14
            r20 = r15
            r15 = r8
            r16 = r6
            r18 = r10
            r11.<init>(r12, r13, r14, r15, r16, r17, r18, r19)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r0.acceptStep(r2, r3)
            r0.setStepStart(r2)
            r0.scaled = r9
            r0.nordsieck = r8
            boolean r2 = r30.isLastStep()
            if (r2 != 0) goto L_0x0235
            int r2 = r5.length
            r11 = 0
            java.lang.System.arraycopy(r4, r11, r5, r11, r2)
            boolean r2 = r30.resetOccurred()
            if (r2 == 0) goto L_0x0174
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r30.getStepStart()
            r0.start(r1, r2, r3)
        L_0x0174:
            org.apache.commons.math3.RealFieldElement r2 = r0.computeStepGrowShrinkFactor(r7)
            org.apache.commons.math3.RealFieldElement r12 = r30.getStepSize()
            java.lang.Object r12 = r12.multiply(r2)
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r13 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r13 = r13.getTime()
            java.lang.Object r13 = r13.add(r12)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            if (r6 == 0) goto L_0x01a6
            java.lang.Object r14 = r13.subtract(r3)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            double r14 = r14.getReal()
            r16 = 0
            int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r14 < 0) goto L_0x01a4
        L_0x01a2:
            r14 = 1
            goto L_0x01b7
        L_0x01a4:
            r14 = 0
            goto L_0x01b7
        L_0x01a6:
            r16 = 0
            java.lang.Object r14 = r13.subtract(r3)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            double r14 = r14.getReal()
            int r14 = (r14 > r16 ? 1 : (r14 == r16 ? 0 : -1))
            if (r14 > 0) goto L_0x01a4
            goto L_0x01a2
        L_0x01b7:
            org.apache.commons.math3.RealFieldElement r15 = r0.filterStep(r12, r6, r14)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r11 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r11 = r11.getTime()
            java.lang.Object r11 = r11.add(r15)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            if (r6 == 0) goto L_0x01e1
            java.lang.Object r16 = r11.subtract(r3)
            r1 = r16
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r16 = r1.getReal()
            r18 = 0
            int r1 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r1 < 0) goto L_0x01df
        L_0x01dd:
            r1 = 1
            goto L_0x01f2
        L_0x01df:
            r1 = 0
            goto L_0x01f2
        L_0x01e1:
            r18 = 0
            java.lang.Object r1 = r11.subtract(r3)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            double r16 = r1.getReal()
            int r1 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r1 > 0) goto L_0x01df
            goto L_0x01dd
        L_0x01f2:
            if (r1 == 0) goto L_0x0206
            r25 = r1
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r3.subtract(r1)
            r15 = r1
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            goto L_0x0208
        L_0x0206:
            r25 = r1
        L_0x0208:
            r0.rescale(r15)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r30.getStepStart()
            r26 = r2
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r30.getStepStart()
            org.apache.commons.math3.RealFieldElement r2 = r2.getTime()
            org.apache.commons.math3.RealFieldElement r3 = r30.getStepSize()
            java.lang.Object r2 = r2.add(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            org.apache.commons.math3.RealFieldElement r3 = r30.getStepSize()
            r27 = r4
            org.apache.commons.math3.RealFieldElement[] r4 = r0.scaled
            r28 = r5
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r5 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r1, r2, r3, r4, r5)
            r11 = r1
            goto L_0x023a
        L_0x0235:
            r28 = r5
            r18 = 0
            r11 = r10
        L_0x023a:
            boolean r1 = r30.isLastStep()
            if (r1 == 0) goto L_0x024c
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r30.getStepStart()
            r2 = 0
            r0.setStepStart(r2)
            r0.setStepSize(r2)
            return r1
        L_0x024c:
            r8 = r18
            r15 = r20
            r4 = r23
            r5 = r28
            r1 = r31
            r2 = r32
            r3 = r33
            r10 = 0
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.nonstiff.AdamsBashforthFieldIntegrator.integrate(org.apache.commons.math3.ode.FieldExpandableODE, org.apache.commons.math3.ode.FieldODEState, org.apache.commons.math3.RealFieldElement):org.apache.commons.math3.ode.FieldODEStateAndDerivative");
    }
}
