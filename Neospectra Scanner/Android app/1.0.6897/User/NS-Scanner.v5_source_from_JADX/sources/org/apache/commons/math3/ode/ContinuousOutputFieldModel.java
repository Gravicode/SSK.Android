package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;
import org.apache.commons.math3.ode.sampling.FieldStepInterpolator;

public class ContinuousOutputFieldModel<T extends RealFieldElement<T>> implements FieldStepHandler<T> {
    private T finalTime = null;
    private boolean forward = true;
    private int index = 0;
    private T initialTime = null;
    private List<FieldStepInterpolator<T>> steps = new ArrayList();

    public void append(ContinuousOutputFieldModel<T> model) throws MathIllegalArgumentException, MaxCountExceededException {
        if (model.steps.size() != 0) {
            if (this.steps.size() == 0) {
                this.initialTime = model.initialTime;
                this.forward = model.forward;
            } else {
                FieldODEStateAndDerivative<T> s1 = ((FieldStepInterpolator) this.steps.get(0)).getPreviousState();
                FieldODEStateAndDerivative<T> s2 = ((FieldStepInterpolator) model.steps.get(0)).getPreviousState();
                checkDimensionsEquality(s1.getStateDimension(), s2.getStateDimension());
                checkDimensionsEquality(s1.getNumberOfSecondaryStates(), s2.getNumberOfSecondaryStates());
                for (int i = 0; i < s1.getNumberOfSecondaryStates(); i++) {
                    checkDimensionsEquality(s1.getSecondaryStateDimension(i), s2.getSecondaryStateDimension(i));
                }
                if ((this.forward ^ model.forward) != 0) {
                    throw new MathIllegalArgumentException(LocalizedFormats.PROPAGATION_DIRECTION_MISMATCH, new Object[0]);
                }
                FieldStepInterpolator<T> lastInterpolator = (FieldStepInterpolator) this.steps.get(this.index);
                T current = lastInterpolator.getCurrentState().getTime();
                T gap = (RealFieldElement) model.getInitialTime().subtract(current);
                if (((RealFieldElement) ((RealFieldElement) gap.abs()).subtract(((RealFieldElement) ((RealFieldElement) current.subtract(lastInterpolator.getPreviousState().getTime())).abs()).multiply(0.001d))).getReal() > 0.0d) {
                    throw new MathIllegalArgumentException(LocalizedFormats.HOLE_BETWEEN_MODELS_TIME_RANGES, Double.valueOf(((RealFieldElement) gap.abs()).getReal()));
                }
            }
            for (FieldStepInterpolator<T> interpolator : model.steps) {
                this.steps.add(interpolator);
            }
            this.index = this.steps.size() - 1;
            this.finalTime = ((FieldStepInterpolator) this.steps.get(this.index)).getCurrentState().getTime();
        }
    }

    private void checkDimensionsEquality(int d1, int d2) throws DimensionMismatchException {
        if (d1 != d2) {
            throw new DimensionMismatchException(d2, d1);
        }
    }

    public void init(FieldODEStateAndDerivative<T> initialState, T t) {
        this.initialTime = initialState.getTime();
        this.finalTime = t;
        this.forward = true;
        this.index = 0;
        this.steps.clear();
    }

    public void handleStep(FieldStepInterpolator<T> interpolator, boolean isLast) throws MaxCountExceededException {
        if (this.steps.size() == 0) {
            this.initialTime = interpolator.getPreviousState().getTime();
            this.forward = interpolator.isForward();
        }
        this.steps.add(interpolator);
        if (isLast) {
            this.finalTime = interpolator.getCurrentState().getTime();
            this.index = this.steps.size() - 1;
        }
    }

    public T getInitialTime() {
        return this.initialTime;
    }

    public T getFinalTime() {
        return this.finalTime;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x01ee  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x01f1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.apache.commons.math3.ode.FieldODEStateAndDerivative<T> getInterpolatedState(T r28) {
        /*
            r27 = this;
            r0 = r27
            r1 = r28
            r2 = 0
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r3 = r0.steps
            java.lang.Object r3 = r3.get(r2)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r3 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r3
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r4 = r3.getPreviousState()
            org.apache.commons.math3.RealFieldElement r4 = r4.getTime()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r5 = r3.getCurrentState()
            org.apache.commons.math3.RealFieldElement r5 = r5.getTime()
            java.lang.Object r4 = r4.add(r5)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r5 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r4 = r4.multiply(r5)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r7 = r0.steps
            int r7 = r7.size()
            int r7 = r7 + -1
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r8 = r0.steps
            java.lang.Object r8 = r8.get(r7)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r8 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r8
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r9 = r8.getPreviousState()
            org.apache.commons.math3.RealFieldElement r9 = r9.getTime()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r10 = r8.getCurrentState()
            org.apache.commons.math3.RealFieldElement r10 = r10.getTime()
            java.lang.Object r9 = r9.add(r10)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r5)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            int r10 = r0.locatePoint(r1, r3)
            if (r10 > 0) goto L_0x0064
            r0.index = r2
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r5 = r3.getInterpolatedState(r1)
            return r5
        L_0x0064:
            int r10 = r0.locatePoint(r1, r8)
            if (r10 < 0) goto L_0x0071
            r0.index = r7
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r5 = r8.getInterpolatedState(r1)
            return r5
        L_0x0071:
            int r10 = r7 - r2
            r11 = 5
            if (r10 <= r11) goto L_0x020d
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r10 = r0.steps
            int r11 = r0.index
            java.lang.Object r10 = r10.get(r11)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r10 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r10
            int r11 = r0.locatePoint(r1, r10)
            if (r11 >= 0) goto L_0x00a6
            int r7 = r0.index
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r12 = r10.getPreviousState()
            org.apache.commons.math3.RealFieldElement r12 = r12.getTime()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r13 = r10.getCurrentState()
            org.apache.commons.math3.RealFieldElement r13 = r13.getTime()
            java.lang.Object r12 = r12.add(r13)
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            java.lang.Object r12 = r12.multiply(r5)
            r9 = r12
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            goto L_0x00c7
        L_0x00a6:
            if (r11 <= 0) goto L_0x0204
            int r2 = r0.index
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r12 = r10.getPreviousState()
            org.apache.commons.math3.RealFieldElement r12 = r12.getTime()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r13 = r10.getCurrentState()
            org.apache.commons.math3.RealFieldElement r13 = r13.getTime()
            java.lang.Object r12 = r12.add(r13)
            org.apache.commons.math3.RealFieldElement r12 = (org.apache.commons.math3.RealFieldElement) r12
            java.lang.Object r12 = r12.multiply(r5)
            r4 = r12
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
        L_0x00c7:
            int r12 = r2 + r7
            int r12 = r12 / 2
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r13 = r0.steps
            java.lang.Object r13 = r13.get(r12)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r13 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r13
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r14 = r13.getPreviousState()
            org.apache.commons.math3.RealFieldElement r14 = r14.getTime()
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r15 = r13.getCurrentState()
            org.apache.commons.math3.RealFieldElement r15 = r15.getTime()
            java.lang.Object r14 = r14.add(r15)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            java.lang.Object r14 = r14.multiply(r5)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            java.lang.Object r15 = r14.subtract(r4)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            java.lang.Object r15 = r15.abs()
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r5 = 4517329193108106637(0x3eb0c6f7a0b5ed8d, double:1.0E-6)
            java.lang.Object r15 = r15.subtract(r5)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            double r16 = r15.getReal()
            r18 = 0
            int r15 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
            if (r15 < 0) goto L_0x01ca
            java.lang.Object r15 = r9.subtract(r14)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            java.lang.Object r15 = r15.abs()
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            java.lang.Object r5 = r15.subtract(r5)
            org.apache.commons.math3.RealFieldElement r5 = (org.apache.commons.math3.RealFieldElement) r5
            double r5 = r5.getReal()
            int r5 = (r5 > r18 ? 1 : (r5 == r18 ? 0 : -1))
            if (r5 >= 0) goto L_0x0134
            r20 = r3
            r22 = r4
            r21 = r8
            r23 = r9
            goto L_0x01d2
        L_0x0134:
            java.lang.Object r5 = r9.subtract(r14)
            org.apache.commons.math3.RealFieldElement r5 = (org.apache.commons.math3.RealFieldElement) r5
            java.lang.Object r6 = r14.subtract(r4)
            org.apache.commons.math3.RealFieldElement r6 = (org.apache.commons.math3.RealFieldElement) r6
            java.lang.Object r15 = r9.subtract(r4)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            java.lang.Object r16 = r1.subtract(r9)
            r20 = r3
            r3 = r16
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            java.lang.Object r16 = r1.subtract(r14)
            r21 = r8
            r8 = r16
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            java.lang.Object r16 = r1.subtract(r4)
            r22 = r4
            r4 = r16
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r16 = r8.multiply(r4)
            r23 = r9
            r9 = r16
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r6)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r7)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r16 = r3.multiply(r4)
            r24 = r4
            r4 = r16
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r4 = r4.multiply(r15)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r4 = r4.multiply(r12)
            java.lang.Object r4 = r9.subtract(r4)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r9 = r3.multiply(r8)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r5)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r2)
            java.lang.Object r4 = r4.add(r9)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r9 = r5.multiply(r6)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r9 = r9.multiply(r15)
            java.lang.Object r4 = r4.divide(r9)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r25 = r5
            r26 = r6
            double r5 = r4.getReal()
            double r5 = org.apache.commons.math3.util.FastMath.rint(r5)
            int r5 = (int) r5
            r0.index = r5
            goto L_0x01d4
        L_0x01ca:
            r20 = r3
            r22 = r4
            r21 = r8
            r23 = r9
        L_0x01d2:
            r0.index = r12
        L_0x01d4:
            int r3 = r2 + 1
            int r4 = r2 * 9
            int r4 = r4 + r7
            int r4 = r4 / 10
            int r3 = org.apache.commons.math3.util.FastMath.max(r3, r4)
            int r4 = r7 + -1
            int r5 = r7 * 9
            int r5 = r5 + r2
            int r5 = r5 / 10
            int r4 = org.apache.commons.math3.util.FastMath.min(r4, r5)
            int r5 = r0.index
            if (r5 >= r3) goto L_0x01f1
            r0.index = r3
            goto L_0x01f7
        L_0x01f1:
            int r5 = r0.index
            if (r5 <= r4) goto L_0x01f7
            r0.index = r4
        L_0x01f7:
            r3 = r20
            r8 = r21
            r4 = r22
            r9 = r23
            r5 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            goto L_0x0071
        L_0x0204:
            r20 = r3
            r21 = r8
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r3 = r10.getInterpolatedState(r1)
            return r3
        L_0x020d:
            r20 = r3
            r21 = r8
            r0.index = r2
        L_0x0213:
            int r3 = r0.index
            if (r3 > r7) goto L_0x022e
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r3 = r0.steps
            int r5 = r0.index
            java.lang.Object r3 = r3.get(r5)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r3 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r3
            int r3 = r0.locatePoint(r1, r3)
            if (r3 <= 0) goto L_0x022e
            int r3 = r0.index
            int r3 = r3 + 1
            r0.index = r3
            goto L_0x0213
        L_0x022e:
            java.util.List<org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T>> r3 = r0.steps
            int r5 = r0.index
            java.lang.Object r3 = r3.get(r5)
            org.apache.commons.math3.ode.sampling.FieldStepInterpolator r3 = (org.apache.commons.math3.ode.sampling.FieldStepInterpolator) r3
            org.apache.commons.math3.ode.FieldODEStateAndDerivative r3 = r3.getInterpolatedState(r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.ContinuousOutputFieldModel.getInterpolatedState(org.apache.commons.math3.RealFieldElement):org.apache.commons.math3.ode.FieldODEStateAndDerivative");
    }

    private int locatePoint(T time, FieldStepInterpolator<T> interval) {
        if (this.forward) {
            if (((RealFieldElement) time.subtract(interval.getPreviousState().getTime())).getReal() < 0.0d) {
                return -1;
            }
            return ((RealFieldElement) time.subtract(interval.getCurrentState().getTime())).getReal() > 0.0d ? 1 : 0;
        } else if (((RealFieldElement) time.subtract(interval.getPreviousState().getTime())).getReal() > 0.0d) {
            return -1;
        } else {
            return ((RealFieldElement) time.subtract(interval.getCurrentState().getTime())).getReal() < 0.0d ? 1 : 0;
        }
    }
}
