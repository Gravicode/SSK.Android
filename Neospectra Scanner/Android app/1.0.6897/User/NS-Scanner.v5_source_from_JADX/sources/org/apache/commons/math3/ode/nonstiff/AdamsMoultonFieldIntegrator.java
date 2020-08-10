package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;
import org.apache.commons.math3.util.MathUtils;

public class AdamsMoultonFieldIntegrator<T extends RealFieldElement<T>> extends AdamsFieldIntegrator<T> {
    private static final String METHOD_NAME = "Adams-Moulton";

    private class Corrector implements FieldMatrixPreservingVisitor<T> {
        private final T[] after;
        private final T[] before;
        private final T[] previous;
        private final T[] scaled;

        Corrector(T[] previous2, T[] scaled2, T[] state) {
            this.previous = previous2;
            this.scaled = scaled2;
            this.after = state;
            this.before = (RealFieldElement[]) state.clone();
        }

        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(this.after, AdamsMoultonFieldIntegrator.this.getField().getZero());
        }

        public void visit(int row, int column, T value) {
            if ((row & 1) == 0) {
                this.after[column] = (RealFieldElement) this.after[column].subtract(value);
            } else {
                this.after[column] = (RealFieldElement) this.after[column].add(value);
            }
        }

        public T end() {
            T error = (RealFieldElement) AdamsMoultonFieldIntegrator.this.getField().getZero();
            for (int i = 0; i < this.after.length; i++) {
                this.after[i] = (RealFieldElement) this.after[i].add(this.previous[i].add(this.scaled[i]));
                if (i < AdamsMoultonFieldIntegrator.this.mainSetDimension) {
                    T yScale = MathUtils.max((RealFieldElement) this.previous[i].abs(), (RealFieldElement) this.after[i].abs());
                    T ratio = (RealFieldElement) ((RealFieldElement) this.after[i].subtract(this.before[i])).divide((RealFieldElement) (AdamsMoultonFieldIntegrator.this.vecAbsoluteTolerance == null ? ((RealFieldElement) yScale.multiply(AdamsMoultonFieldIntegrator.this.scalRelativeTolerance)).add(AdamsMoultonFieldIntegrator.this.scalAbsoluteTolerance) : ((RealFieldElement) yScale.multiply(AdamsMoultonFieldIntegrator.this.vecRelativeTolerance[i])).add(AdamsMoultonFieldIntegrator.this.vecAbsoluteTolerance[i])));
                    error = (RealFieldElement) error.add(ratio.multiply(ratio));
                }
            }
            return (RealFieldElement) ((RealFieldElement) error.divide((double) AdamsMoultonFieldIntegrator.this.mainSetDimension)).sqrt();
        }
    }

    public AdamsMoultonFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(field, METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsMoultonFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(field, METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x0218, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r1.subtract(r3)).getReal() >= 0.0d) goto L_0x021a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x021c, code lost:
        r2 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x022e, code lost:
        if (((org.apache.commons.math3.RealFieldElement) r1.subtract(r3)).getReal() <= 0.0d) goto L_0x021a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.ode.FieldODEStateAndDerivative<T> integrate(org.apache.commons.math3.ode.FieldExpandableODE<T> r32, org.apache.commons.math3.ode.FieldODEState<T> r33, T r34) throws org.apache.commons.math3.exception.NumberIsTooSmallException, org.apache.commons.math3.exception.DimensionMismatchException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            r31 = this;
            r0 = r31
            r1 = r32
            r2 = r33
            r3 = r34
            r0.sanityChecks(r2, r3)
            org.apache.commons.math3.RealFieldElement r4 = r33.getTime()
            org.apache.commons.math3.ode.FieldEquationsMapper r5 = r32.getMapper()
            org.apache.commons.math3.RealFieldElement[] r5 = r5.mapState(r2)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r6 = r0.initIntegration(r1, r4, r5, r3)
            r0.setStepStart(r6)
            org.apache.commons.math3.RealFieldElement r6 = r33.getTime()
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
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r11 = r31.getStepStart()
            r0.start(r1, r11, r3)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r15 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r11 = r15.getTime()
            org.apache.commons.math3.RealFieldElement r12 = r31.getStepSize()
            java.lang.Object r11 = r11.add(r12)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            org.apache.commons.math3.RealFieldElement r12 = r31.getStepSize()
            org.apache.commons.math3.RealFieldElement[] r13 = r0.scaled
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r14 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r11 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r15, r11, r12, r13, r14)
            r0.setIsLastStep(r10)
        L_0x005e:
            r12 = 0
            org.apache.commons.math3.Field r13 = r31.getField()
            int r14 = r5.length
            java.lang.Object[] r13 = org.apache.commons.math3.util.MathArrays.buildArray(r13, r14)
            r14 = r13
            org.apache.commons.math3.RealFieldElement[] r14 = (org.apache.commons.math3.RealFieldElement[]) r14
            r13 = 0
            org.apache.commons.math3.Field r7 = r31.getField()
            java.lang.Object r7 = r7.getZero()
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r20 = r11
            r10 = 4621819117588971520(0x4024000000000000, double:10.0)
            java.lang.Object r7 = r7.add(r10)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r11 = r13
            r10 = r20
        L_0x0083:
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r13 = r7.subtract(r8)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            double r16 = r13.getReal()
            r18 = 0
            int r13 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r13 < 0) goto L_0x0136
            org.apache.commons.math3.RealFieldElement[] r12 = r10.getState()
            org.apache.commons.math3.RealFieldElement r13 = r10.getTime()
            org.apache.commons.math3.RealFieldElement[] r13 = r0.computeDerivatives(r13, r12)
            r16 = 0
        L_0x00a3:
            r21 = r16
            int r8 = r14.length
            r9 = r21
            if (r9 >= r8) goto L_0x00bf
            org.apache.commons.math3.RealFieldElement r8 = r31.getStepSize()
            r2 = r13[r9]
            java.lang.Object r2 = r8.multiply(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r14[r9] = r2
            int r16 = r9 + 1
            r2 = r33
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            goto L_0x00a3
        L_0x00bf:
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r2 = r0.nordsieck
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r11 = r0.updateHighOrderDerivativesPhase1(r2)
            org.apache.commons.math3.RealFieldElement[] r2 = r0.scaled
            r0.updateHighOrderDerivativesPhase2(r2, r14, r11)
            org.apache.commons.math3.ode.nonstiff.AdamsMoultonFieldIntegrator$Corrector r2 = new org.apache.commons.math3.ode.nonstiff.AdamsMoultonFieldIntegrator$Corrector
            r2.<init>(r5, r14, r12)
            org.apache.commons.math3.FieldElement r2 = r11.walkInOptimizedOrder(r2)
            r7 = r2
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Object r2 = r7.subtract(r8)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double r8 = r2.getReal()
            r16 = 0
            int r2 = (r8 > r16 ? 1 : (r8 == r16 ? 0 : -1))
            if (r2 < 0) goto L_0x0128
            org.apache.commons.math3.RealFieldElement r2 = r0.computeStepGrowShrinkFactor(r7)
            org.apache.commons.math3.RealFieldElement r8 = r31.getStepSize()
            java.lang.Object r8 = r8.multiply(r2)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r9 = 0
            org.apache.commons.math3.RealFieldElement r8 = r0.filterStep(r8, r6, r9)
            r0.rescale(r8)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r8 = r31.getStepStart()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r9 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r9 = r9.getTime()
            r22 = r2
            org.apache.commons.math3.RealFieldElement r2 = r31.getStepSize()
            java.lang.Object r2 = r9.add(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            org.apache.commons.math3.RealFieldElement r9 = r31.getStepSize()
            r23 = r4
            org.apache.commons.math3.RealFieldElement[] r4 = r0.scaled
            r24 = r7
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r7 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r8, r2, r9, r4, r7)
            r10 = r2
            goto L_0x012c
        L_0x0128:
            r23 = r4
            r24 = r7
        L_0x012c:
            r4 = r23
            r7 = r24
            r2 = r33
            r8 = 0
            goto L_0x0083
        L_0x0136:
            r23 = r4
            org.apache.commons.math3.RealFieldElement r2 = r10.getTime()
            org.apache.commons.math3.RealFieldElement[] r2 = r0.computeDerivatives(r2, r12)
            org.apache.commons.math3.Field r4 = r31.getField()
            int r8 = r5.length
            java.lang.Object[] r4 = org.apache.commons.math3.util.MathArrays.buildArray(r4, r8)
            org.apache.commons.math3.RealFieldElement[] r4 = (org.apache.commons.math3.RealFieldElement[]) r4
            r8 = 0
        L_0x014c:
            int r9 = r4.length
            if (r8 >= r9) goto L_0x0160
            org.apache.commons.math3.RealFieldElement r9 = r31.getStepSize()
            r13 = r2[r8]
            java.lang.Object r9 = r9.multiply(r13)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r4[r8] = r9
            int r8 = r8 + 1
            goto L_0x014c
        L_0x0160:
            r0.updateHighOrderDerivativesPhase2(r14, r4, r11)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r13 = new org.apache.commons.math3.ode.FieldODEStateAndDerivative
            org.apache.commons.math3.RealFieldElement r8 = r10.getTime()
            r13.<init>(r8, r12, r2)
            org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator r8 = new org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator
            org.apache.commons.math3.RealFieldElement r9 = r31.getStepSize()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r17 = r31.getStepStart()
            org.apache.commons.math3.ode.FieldEquationsMapper r19 = r32.getMapper()
            r10 = r11
            r11 = r8
            r25 = r2
            r2 = r12
            r12 = r9
            r9 = r14
            r14 = r4
            r20 = r15
            r15 = r10
            r16 = r6
            r18 = r13
            r11.<init>(r12, r13, r14, r15, r16, r17, r18, r19)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r8 = r0.acceptStep(r8, r3)
            r0.setStepStart(r8)
            r0.scaled = r4
            r0.nordsieck = r10
            boolean r8 = r31.isLastStep()
            if (r8 != 0) goto L_0x0274
            int r8 = r5.length
            r11 = 0
            java.lang.System.arraycopy(r2, r11, r5, r11, r8)
            boolean r8 = r31.resetOccurred()
            if (r8 == 0) goto L_0x01af
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r8 = r31.getStepStart()
            r0.start(r1, r8, r3)
        L_0x01af:
            org.apache.commons.math3.RealFieldElement r8 = r0.computeStepGrowShrinkFactor(r7)
            org.apache.commons.math3.RealFieldElement r12 = r31.getStepSize()
            java.lang.Object r12 = r12.multiply(r8)
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r14 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r14 = r14.getTime()
            java.lang.Object r14 = r14.add(r12)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            if (r6 == 0) goto L_0x01e1
            java.lang.Object r15 = r14.subtract(r3)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            double r15 = r15.getReal()
            r17 = 0
            int r15 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r15 < 0) goto L_0x01df
        L_0x01dd:
            r15 = 1
            goto L_0x01f2
        L_0x01df:
            r15 = 0
            goto L_0x01f2
        L_0x01e1:
            r17 = 0
            java.lang.Object r15 = r14.subtract(r3)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            double r15 = r15.getReal()
            int r15 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r15 > 0) goto L_0x01df
            goto L_0x01dd
        L_0x01f2:
            org.apache.commons.math3.RealFieldElement r11 = r0.filterStep(r12, r6, r15)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r1.add(r11)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            if (r6 == 0) goto L_0x021e
            java.lang.Object r16 = r1.subtract(r3)
            r26 = r2
            r2 = r16
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double r16 = r2.getReal()
            r18 = 0
            int r2 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r2 < 0) goto L_0x021c
        L_0x021a:
            r2 = 1
            goto L_0x0231
        L_0x021c:
            r2 = 0
            goto L_0x0231
        L_0x021e:
            r26 = r2
            r18 = 0
            java.lang.Object r2 = r1.subtract(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            double r16 = r2.getReal()
            int r2 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r2 > 0) goto L_0x021c
            goto L_0x021a
        L_0x0231:
            if (r2 == 0) goto L_0x0245
            r27 = r1
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r1 = r1.getTime()
            java.lang.Object r1 = r3.subtract(r1)
            r11 = r1
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            goto L_0x0247
        L_0x0245:
            r27 = r1
        L_0x0247:
            r0.rescale(r11)
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r31.getStepStart()
            r28 = r2
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r2 = r31.getStepStart()
            org.apache.commons.math3.RealFieldElement r2 = r2.getTime()
            org.apache.commons.math3.RealFieldElement r3 = r31.getStepSize()
            java.lang.Object r2 = r2.add(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            org.apache.commons.math3.RealFieldElement r3 = r31.getStepSize()
            r29 = r4
            org.apache.commons.math3.RealFieldElement[] r4 = r0.scaled
            r30 = r5
            org.apache.commons.math3.linear.Array2DRowFieldMatrix r5 = r0.nordsieck
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = org.apache.commons.math3.ode.nonstiff.AdamsFieldStepInterpolator.taylor(r1, r2, r3, r4, r5)
            r11 = r1
            goto L_0x0279
        L_0x0274:
            r30 = r5
            r18 = 0
            r11 = r13
        L_0x0279:
            boolean r1 = r31.isLastStep()
            if (r1 == 0) goto L_0x028b
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r1 = r31.getStepStart()
            r2 = 0
            r0.setStepStart(r2)
            r0.setStepSize(r2)
            return r1
        L_0x028b:
            r8 = r18
            r15 = r20
            r4 = r23
            r5 = r30
            r1 = r32
            r2 = r33
            r3 = r34
            r10 = 0
            goto L_0x005e
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.nonstiff.AdamsMoultonFieldIntegrator.integrate(org.apache.commons.math3.ode.FieldExpandableODE, org.apache.commons.math3.ode.FieldODEState, org.apache.commons.math3.RealFieldElement):org.apache.commons.math3.ode.FieldODEStateAndDerivative");
    }
}
