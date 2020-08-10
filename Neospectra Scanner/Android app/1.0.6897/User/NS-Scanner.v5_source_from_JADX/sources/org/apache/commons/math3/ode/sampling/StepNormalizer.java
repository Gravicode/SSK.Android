package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class StepNormalizer implements StepHandler {
    private final StepNormalizerBounds bounds;
    private double firstTime;
    private boolean forward;

    /* renamed from: h */
    private double f703h;
    private final FixedStepHandler handler;
    private double[] lastDerivatives;
    private double[] lastState;
    private double lastTime;
    private final StepNormalizerMode mode;

    public StepNormalizer(double h, FixedStepHandler handler2) {
        this(h, handler2, StepNormalizerMode.INCREMENT, StepNormalizerBounds.FIRST);
    }

    public StepNormalizer(double h, FixedStepHandler handler2, StepNormalizerMode mode2) {
        this(h, handler2, mode2, StepNormalizerBounds.FIRST);
    }

    public StepNormalizer(double h, FixedStepHandler handler2, StepNormalizerBounds bounds2) {
        this(h, handler2, StepNormalizerMode.INCREMENT, bounds2);
    }

    public StepNormalizer(double h, FixedStepHandler handler2, StepNormalizerMode mode2, StepNormalizerBounds bounds2) {
        this.f703h = FastMath.abs(h);
        this.handler = handler2;
        this.mode = mode2;
        this.bounds = bounds2;
        this.firstTime = Double.NaN;
        this.lastTime = Double.NaN;
        this.lastState = null;
        this.lastDerivatives = null;
        this.forward = true;
    }

    public void init(double t0, double[] y0, double t) {
        this.firstTime = Double.NaN;
        this.lastTime = Double.NaN;
        this.lastState = null;
        this.lastDerivatives = null;
        this.forward = true;
        this.handler.init(t0, y0, t);
    }

    public void handleStep(StepInterpolator interpolator, boolean isLast) throws MaxCountExceededException {
        boolean z = false;
        if (this.lastState == null) {
            this.firstTime = interpolator.getPreviousTime();
            this.lastTime = interpolator.getPreviousTime();
            interpolator.setInterpolatedTime(this.lastTime);
            this.lastState = (double[]) interpolator.getInterpolatedState().clone();
            this.lastDerivatives = (double[]) interpolator.getInterpolatedDerivatives().clone();
            this.forward = interpolator.getCurrentTime() >= this.lastTime;
            if (!this.forward) {
                this.f703h = -this.f703h;
            }
        }
        double nextTime = this.mode == StepNormalizerMode.INCREMENT ? this.lastTime + this.f703h : (FastMath.floor(this.lastTime / this.f703h) + 1.0d) * this.f703h;
        if (this.mode == StepNormalizerMode.MULTIPLES && Precision.equals(nextTime, this.lastTime, 1)) {
            nextTime += this.f703h;
        }
        boolean nextInStep = isNextInStep(nextTime, interpolator);
        while (nextInStep) {
            doNormalizedStep(false);
            storeStep(interpolator, nextTime);
            nextTime += this.f703h;
            nextInStep = isNextInStep(nextTime, interpolator);
        }
        if (isLast) {
            boolean addLast = this.bounds.lastIncluded() && this.lastTime != interpolator.getCurrentTime();
            if (!addLast) {
                z = true;
            }
            doNormalizedStep(z);
            if (addLast) {
                storeStep(interpolator, interpolator.getCurrentTime());
                doNormalizedStep(true);
            }
        }
    }

    private boolean isNextInStep(double nextTime, StepInterpolator interpolator) {
        if (this.forward) {
            if (nextTime > interpolator.getCurrentTime()) {
                return false;
            }
        } else if (nextTime < interpolator.getCurrentTime()) {
            return false;
        }
        return true;
    }

    private void doNormalizedStep(boolean isLast) {
        if (this.bounds.firstIncluded() || this.firstTime != this.lastTime) {
            this.handler.handleStep(this.lastTime, this.lastState, this.lastDerivatives, isLast);
        }
    }

    private void storeStep(StepInterpolator interpolator, double t) throws MaxCountExceededException {
        this.lastTime = t;
        interpolator.setInterpolatedTime(this.lastTime);
        System.arraycopy(interpolator.getInterpolatedState(), 0, this.lastState, 0, this.lastState.length);
        System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.lastDerivatives, 0, this.lastDerivatives.length);
    }
}
