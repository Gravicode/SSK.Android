package org.apache.commons.math3.ode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class ContinuousOutputModel implements StepHandler, Serializable {
    private static final long serialVersionUID = -1417964919405031606L;
    private double finalTime = Double.NaN;
    private boolean forward = true;
    private int index = 0;
    private double initialTime = Double.NaN;
    private List<StepInterpolator> steps = new ArrayList();

    public void append(ContinuousOutputModel model) throws MathIllegalArgumentException, MaxCountExceededException {
        ContinuousOutputModel continuousOutputModel = model;
        if (continuousOutputModel.steps.size() != 0) {
            if (this.steps.size() == 0) {
                this.initialTime = continuousOutputModel.initialTime;
                this.forward = continuousOutputModel.forward;
            } else if (getInterpolatedState().length != model.getInterpolatedState().length) {
                throw new DimensionMismatchException(model.getInterpolatedState().length, getInterpolatedState().length);
            } else if (this.forward ^ continuousOutputModel.forward) {
                throw new MathIllegalArgumentException(LocalizedFormats.PROPAGATION_DIRECTION_MISMATCH, new Object[0]);
            } else {
                StepInterpolator lastInterpolator = (StepInterpolator) this.steps.get(this.index);
                double current = lastInterpolator.getCurrentTime();
                double gap = model.getInitialTime() - current;
                if (FastMath.abs(gap) > FastMath.abs(current - lastInterpolator.getPreviousTime()) * 0.001d) {
                    double d = current;
                    throw new MathIllegalArgumentException(LocalizedFormats.HOLE_BETWEEN_MODELS_TIME_RANGES, Double.valueOf(FastMath.abs(gap)));
                }
            }
            for (StepInterpolator interpolator : continuousOutputModel.steps) {
                this.steps.add(interpolator.copy());
            }
            this.index = this.steps.size() - 1;
            this.finalTime = ((StepInterpolator) this.steps.get(this.index)).getCurrentTime();
        }
    }

    public void init(double t0, double[] y0, double t) {
        this.initialTime = Double.NaN;
        this.finalTime = Double.NaN;
        this.forward = true;
        this.index = 0;
        this.steps.clear();
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        if (this.steps.size() == 0) {
            this.initialTime = interpolator.getPreviousTime();
            this.forward = interpolator.isForward();
        }
        this.steps.add(interpolator.copy());
        if (isLast) {
            this.finalTime = interpolator.getCurrentTime();
            this.index = this.steps.size() - 1;
        }
    }

    public double getInitialTime() {
        return this.initialTime;
    }

    public double getFinalTime() {
        return this.finalTime;
    }

    public double getInterpolatedTime() {
        return ((StepInterpolator) this.steps.get(this.index)).getInterpolatedTime();
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0124  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setInterpolatedTime(double r41) {
        /*
            r40 = this;
            r0 = r40
            r1 = r41
            r3 = 0
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r4 = r0.steps
            java.lang.Object r4 = r4.get(r3)
            org.apache.commons.math3.ode.sampling.StepInterpolator r4 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r4
            double r5 = r4.getPreviousTime()
            double r7 = r4.getCurrentTime()
            double r5 = r5 + r7
            r7 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r5 = r5 * r7
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r9 = r0.steps
            int r9 = r9.size()
            int r9 = r9 + -1
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r10 = r0.steps
            java.lang.Object r10 = r10.get(r9)
            org.apache.commons.math3.ode.sampling.StepInterpolator r10 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r10
            double r11 = r10.getPreviousTime()
            double r13 = r10.getCurrentTime()
            double r11 = r11 + r13
            double r11 = r11 * r7
            int r13 = r0.locatePoint(r1, r4)
            if (r13 > 0) goto L_0x0041
            r0.index = r3
            r4.setInterpolatedTime(r1)
            return
        L_0x0041:
            int r13 = r0.locatePoint(r1, r10)
            if (r13 < 0) goto L_0x004d
            r0.index = r9
            r10.setInterpolatedTime(r1)
            return
        L_0x004d:
            int r13 = r9 - r3
            r14 = 5
            if (r13 <= r14) goto L_0x013f
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r13 = r0.steps
            int r14 = r0.index
            java.lang.Object r13 = r13.get(r14)
            org.apache.commons.math3.ode.sampling.StepInterpolator r13 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r13
            int r14 = r0.locatePoint(r1, r13)
            if (r14 >= 0) goto L_0x0072
            int r9 = r0.index
            double r15 = r13.getPreviousTime()
            double r17 = r13.getCurrentTime()
            double r15 = r15 + r17
            double r15 = r15 * r7
            r11 = r15
            goto L_0x0083
        L_0x0072:
            if (r14 <= 0) goto L_0x0137
            int r3 = r0.index
            double r15 = r13.getPreviousTime()
            double r17 = r13.getCurrentTime()
            double r15 = r15 + r17
            double r15 = r15 * r7
            r5 = r15
        L_0x0083:
            int r15 = r3 + r9
            int r15 = r15 / 2
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r7 = r0.steps
            java.lang.Object r7 = r7.get(r15)
            org.apache.commons.math3.ode.sampling.StepInterpolator r7 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r7
            double r16 = r7.getPreviousTime()
            double r21 = r7.getCurrentTime()
            double r16 = r16 + r21
            r18 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r16 = r16 * r18
            r23 = r7
            double r7 = r16 - r5
            double r7 = org.apache.commons.math3.util.FastMath.abs(r7)
            r20 = 4517329193108106637(0x3eb0c6f7a0b5ed8d, double:1.0E-6)
            int r7 = (r7 > r20 ? 1 : (r7 == r20 ? 0 : -1))
            if (r7 < 0) goto L_0x00fd
            double r7 = r11 - r16
            double r7 = org.apache.commons.math3.util.FastMath.abs(r7)
            int r7 = (r7 > r20 ? 1 : (r7 == r20 ? 0 : -1))
            if (r7 >= 0) goto L_0x00c1
            r34 = r4
            r35 = r5
            r37 = r10
            r38 = r11
            goto L_0x0105
        L_0x00c1:
            r7 = 0
            double r7 = r11 - r16
            double r20 = r16 - r5
            double r24 = r11 - r5
            double r26 = r1 - r11
            double r28 = r1 - r16
            double r30 = r1 - r5
            double r32 = r28 * r30
            double r32 = r32 * r20
            r34 = r4
            r35 = r5
            double r4 = (double) r9
            double r32 = r32 * r4
            double r4 = r26 * r30
            double r4 = r4 * r24
            r37 = r10
            r38 = r11
            double r10 = (double) r15
            double r4 = r4 * r10
            double r32 = r32 - r4
            double r4 = r26 * r28
            double r4 = r4 * r7
            double r10 = (double) r3
            double r4 = r4 * r10
            double r32 = r32 + r4
            double r4 = r7 * r20
            double r4 = r4 * r24
            double r4 = r32 / r4
            double r10 = org.apache.commons.math3.util.FastMath.rint(r4)
            int r6 = (int) r10
            r0.index = r6
            goto L_0x0107
        L_0x00fd:
            r34 = r4
            r35 = r5
            r37 = r10
            r38 = r11
        L_0x0105:
            r0.index = r15
        L_0x0107:
            int r4 = r3 + 1
            int r5 = r3 * 9
            int r5 = r5 + r9
            int r5 = r5 / 10
            int r4 = org.apache.commons.math3.util.FastMath.max(r4, r5)
            int r5 = r9 + -1
            int r6 = r9 * 9
            int r6 = r6 + r3
            int r6 = r6 / 10
            int r5 = org.apache.commons.math3.util.FastMath.min(r5, r6)
            int r6 = r0.index
            if (r6 >= r4) goto L_0x0124
            r0.index = r4
            goto L_0x012a
        L_0x0124:
            int r6 = r0.index
            if (r6 <= r5) goto L_0x012a
            r0.index = r5
        L_0x012a:
            r7 = r18
            r4 = r34
            r5 = r35
            r10 = r37
            r11 = r38
            goto L_0x004d
        L_0x0137:
            r34 = r4
            r37 = r10
            r13.setInterpolatedTime(r1)
            return
        L_0x013f:
            r34 = r4
            r37 = r10
            r0.index = r3
        L_0x0145:
            int r4 = r0.index
            if (r4 > r9) goto L_0x0160
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r4 = r0.steps
            int r7 = r0.index
            java.lang.Object r4 = r4.get(r7)
            org.apache.commons.math3.ode.sampling.StepInterpolator r4 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r4
            int r4 = r0.locatePoint(r1, r4)
            if (r4 <= 0) goto L_0x0160
            int r4 = r0.index
            int r4 = r4 + 1
            r0.index = r4
            goto L_0x0145
        L_0x0160:
            java.util.List<org.apache.commons.math3.ode.sampling.StepInterpolator> r4 = r0.steps
            int r7 = r0.index
            java.lang.Object r4 = r4.get(r7)
            org.apache.commons.math3.ode.sampling.StepInterpolator r4 = (org.apache.commons.math3.ode.sampling.StepInterpolator) r4
            r4.setInterpolatedTime(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.ContinuousOutputModel.setInterpolatedTime(double):void");
    }

    public double[] getInterpolatedState() throws MaxCountExceededException {
        return ((StepInterpolator) this.steps.get(this.index)).getInterpolatedState();
    }

    public double[] getInterpolatedDerivatives() throws MaxCountExceededException {
        return ((StepInterpolator) this.steps.get(this.index)).getInterpolatedDerivatives();
    }

    public double[] getInterpolatedSecondaryState(int secondaryStateIndex) throws MaxCountExceededException {
        return ((StepInterpolator) this.steps.get(this.index)).getInterpolatedSecondaryState(secondaryStateIndex);
    }

    public double[] getInterpolatedSecondaryDerivatives(int secondaryStateIndex) throws MaxCountExceededException {
        return ((StepInterpolator) this.steps.get(this.index)).getInterpolatedSecondaryDerivatives(secondaryStateIndex);
    }

    private int locatePoint(double time, StepInterpolator interval) {
        if (this.forward) {
            if (time < interval.getPreviousTime()) {
                return -1;
            }
            return time > interval.getCurrentTime() ? 1 : 0;
        } else if (time > interval.getPreviousTime()) {
            return -1;
        } else {
            return time < interval.getCurrentTime() ? 1 : 0;
        }
    }
}
