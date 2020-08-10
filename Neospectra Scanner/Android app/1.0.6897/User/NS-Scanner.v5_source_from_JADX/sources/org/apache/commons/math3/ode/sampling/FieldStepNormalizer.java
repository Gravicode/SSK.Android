package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

public class FieldStepNormalizer<T extends RealFieldElement<T>> implements FieldStepHandler<T> {
    private final StepNormalizerBounds bounds;
    private FieldODEStateAndDerivative<T> first;
    private boolean forward;

    /* renamed from: h */
    private double f702h;
    private final FieldFixedStepHandler<T> handler;
    private FieldODEStateAndDerivative<T> last;
    private final StepNormalizerMode mode;

    public FieldStepNormalizer(double h, FieldFixedStepHandler<T> handler2) {
        this(h, handler2, StepNormalizerMode.INCREMENT, StepNormalizerBounds.FIRST);
    }

    public FieldStepNormalizer(double h, FieldFixedStepHandler<T> handler2, StepNormalizerMode mode2) {
        this(h, handler2, mode2, StepNormalizerBounds.FIRST);
    }

    public FieldStepNormalizer(double h, FieldFixedStepHandler<T> handler2, StepNormalizerBounds bounds2) {
        this(h, handler2, StepNormalizerMode.INCREMENT, bounds2);
    }

    public FieldStepNormalizer(double h, FieldFixedStepHandler<T> handler2, StepNormalizerMode mode2, StepNormalizerBounds bounds2) {
        this.f702h = FastMath.abs(h);
        this.handler = handler2;
        this.mode = mode2;
        this.bounds = bounds2;
        this.first = null;
        this.last = null;
        this.forward = true;
    }

    public void init(FieldODEStateAndDerivative<T> initialState, T finalTime) {
        this.first = null;
        this.last = null;
        this.forward = true;
        this.handler.init(initialState, finalTime);
    }

    public void handleStep(FieldStepInterpolator<T> interpolator, boolean isLast) throws MaxCountExceededException {
        RealFieldElement realFieldElement;
        double floor;
        boolean z;
        if (this.last == null) {
            this.first = interpolator.getPreviousState();
            this.last = this.first;
            this.forward = interpolator.isForward();
            if (!this.forward) {
                this.f702h = -this.f702h;
            }
        }
        if (this.mode == StepNormalizerMode.INCREMENT) {
            realFieldElement = this.last.getTime();
            floor = this.f702h;
        } else {
            realFieldElement = (RealFieldElement) this.last.getTime().getField().getZero();
            floor = (FastMath.floor(this.last.getTime().getReal() / this.f702h) + 1.0d) * this.f702h;
        }
        T nextTime = (RealFieldElement) realFieldElement.add(floor);
        if (this.mode == StepNormalizerMode.MULTIPLES && Precision.equals(nextTime.getReal(), this.last.getTime().getReal(), 1)) {
            nextTime = (RealFieldElement) nextTime.add(this.f702h);
        }
        boolean nextInStep = isNextInStep(nextTime, interpolator);
        while (true) {
            z = false;
            if (!nextInStep) {
                break;
            }
            doNormalizedStep(false);
            this.last = interpolator.getInterpolatedState(nextTime);
            nextTime = (RealFieldElement) nextTime.add(this.f702h);
            nextInStep = isNextInStep(nextTime, interpolator);
        }
        if (isLast) {
            boolean addLast = this.bounds.lastIncluded() && this.last.getTime().getReal() != interpolator.getCurrentState().getTime().getReal();
            if (!addLast) {
                z = true;
            }
            doNormalizedStep(z);
            if (addLast) {
                this.last = interpolator.getCurrentState();
                doNormalizedStep(true);
            }
        }
    }

    private boolean isNextInStep(T nextTime, FieldStepInterpolator<T> interpolator) {
        if (this.forward) {
            if (nextTime.getReal() > interpolator.getCurrentState().getTime().getReal()) {
                return false;
            }
        } else if (nextTime.getReal() < interpolator.getCurrentState().getTime().getReal()) {
            return false;
        }
        return true;
    }

    private void doNormalizedStep(boolean isLast) {
        if (this.bounds.firstIncluded() || this.first.getTime().getReal() != this.last.getTime().getReal()) {
            this.handler.handleStep(this.last, isLast);
        }
    }
}
