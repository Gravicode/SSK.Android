package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.FieldStepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class FieldEventState<T extends RealFieldElement<T>> {
    private final T convergence;
    private boolean forward;

    /* renamed from: g0 */
    private T f650g0 = null;
    private boolean g0Positive = true;
    /* access modifiers changed from: private */
    public final FieldEventHandler<T> handler;
    private boolean increasing = true;
    private final double maxCheckInterval;
    private final int maxIterationCount;
    private Action nextAction = Action.CONTINUE;
    private boolean pendingEvent = false;
    private T pendingEventTime = null;
    private T previousEventTime = null;
    private final BracketedRealFieldUnivariateSolver<T> solver;

    /* renamed from: t0 */
    private T f651t0 = null;

    public FieldEventState(FieldEventHandler<T> handler2, double maxCheckInterval2, T convergence2, int maxIterationCount2, BracketedRealFieldUnivariateSolver<T> solver2) {
        this.handler = handler2;
        this.maxCheckInterval = maxCheckInterval2;
        this.convergence = (RealFieldElement) convergence2.abs();
        this.maxIterationCount = maxIterationCount2;
        this.solver = solver2;
    }

    public FieldEventHandler<T> getEventHandler() {
        return this.handler;
    }

    public double getMaxCheckInterval() {
        return this.maxCheckInterval;
    }

    public T getConvergence() {
        return this.convergence;
    }

    public int getMaxIterationCount() {
        return this.maxIterationCount;
    }

    public void reinitializeBegin(FieldStepInterpolator<T> interpolator) throws MaxCountExceededException {
        FieldODEStateAndDerivative<T> s0 = interpolator.getPreviousState();
        this.f651t0 = s0.getTime();
        this.f650g0 = this.handler.mo18663g(s0);
        if (this.f650g0.getReal() == 0.0d) {
            this.f650g0 = this.handler.mo18663g(interpolator.getInterpolatedState((RealFieldElement) this.f651t0.add(0.5d * FastMath.max(this.solver.getAbsoluteAccuracy().getReal(), FastMath.abs(((RealFieldElement) this.solver.getRelativeAccuracy().multiply(this.f651t0)).getReal())))));
        }
        this.g0Positive = this.f650g0.getReal() >= 0.0d;
    }

    public boolean evaluateStep(FieldStepInterpolator<T> interpolator) throws MaxCountExceededException, NoBracketingException {
        double d;
        int i;
        T tb;
        int i2;
        RealFieldElement realFieldElement;
        BracketedRealFieldUnivariateSolver<T> bracketedRealFieldUnivariateSolver;
        int i3;
        AllowedSolution allowedSolution;
        C10011 r14;
        RealFieldElement realFieldElement2;
        T root;
        RealFieldElement realFieldElement3;
        T ga;
        final FieldStepInterpolator<T> fieldStepInterpolator = interpolator;
        this.forward = interpolator.isForward();
        T t1 = interpolator.getCurrentState().getTime();
        T dt = (RealFieldElement) t1.subtract(this.f651t0);
        double d2 = 0.0d;
        if (((RealFieldElement) ((RealFieldElement) dt.abs()).subtract(this.convergence)).getReal() < 0.0d) {
            return false;
        }
        int n = FastMath.max(1, (int) FastMath.ceil(FastMath.abs(dt.getReal()) / this.maxCheckInterval));
        T h = (RealFieldElement) dt.divide((double) n);
        C10011 r11 = new RealFieldUnivariateFunction<T>() {
            public T value(T t) {
                return FieldEventState.this.handler.mo18663g(fieldStepInterpolator.getInterpolatedState(t));
            }
        };
        RealFieldElement realFieldElement4 = this.f651t0;
        T ga2 = this.f650g0;
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < n) {
                T tb2 = i5 == n + -1 ? t1 : (RealFieldElement) this.f651t0.add(h.multiply(i5 + 1));
                T gb = this.handler.mo18663g(fieldStepInterpolator.getInterpolatedState(tb2));
                if (this.g0Positive ^ (gb.getReal() >= d2)) {
                    this.increasing = ((RealFieldElement) gb.subtract(ga2)).getReal() >= d2;
                    if (this.forward) {
                        bracketedRealFieldUnivariateSolver = this.solver;
                        int i6 = this.maxIterationCount;
                        allowedSolution = AllowedSolution.RIGHT_SIDE;
                        i2 = i5;
                        i3 = i6;
                        T t = ga2;
                        r14 = r11;
                        realFieldElement = realFieldElement4;
                        realFieldElement2 = tb2;
                    } else {
                        i2 = i5;
                        T t2 = ga2;
                        realFieldElement = realFieldElement4;
                        bracketedRealFieldUnivariateSolver = this.solver;
                        i3 = this.maxIterationCount;
                        allowedSolution = AllowedSolution.LEFT_SIDE;
                        r14 = r11;
                        realFieldElement4 = tb2;
                        realFieldElement2 = realFieldElement;
                    }
                    root = bracketedRealFieldUnivariateSolver.solve(i3, r14, realFieldElement4, realFieldElement2, allowedSolution);
                    if (this.previousEventTime != null) {
                        realFieldElement3 = realFieldElement;
                        if (((RealFieldElement) ((RealFieldElement) ((RealFieldElement) root.subtract(realFieldElement3)).abs()).subtract(this.convergence)).getReal() <= 0.0d && ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) root.subtract(this.previousEventTime)).abs()).subtract(this.convergence)).getReal() <= 0.0d) {
                            do {
                                realFieldElement3 = (RealFieldElement) (this.forward ? realFieldElement3.add(this.convergence) : realFieldElement3.subtract(this.convergence));
                                ga = r11.value(realFieldElement3);
                                if (!(this.g0Positive ^ (ga.getReal() >= 0.0d))) {
                                    break;
                                }
                            } while (this.forward ^ (((RealFieldElement) realFieldElement3.subtract(tb2)).getReal() >= 0.0d));
                            if (this.forward ^ (((RealFieldElement) realFieldElement3.subtract(tb2)).getReal() >= 0.0d)) {
                                i2--;
                                d = 0.0d;
                                int i7 = i2;
                                T ga3 = ga;
                                RealFieldElement realFieldElement5 = realFieldElement3;
                                i = i7;
                                realFieldElement4 = realFieldElement5;
                                tb = ga3;
                            } else {
                                this.pendingEventTime = root;
                                this.pendingEvent = true;
                                return true;
                            }
                        }
                    } else {
                        RealFieldElement realFieldElement6 = realFieldElement;
                    }
                    if (this.previousEventTime == null) {
                        break;
                    }
                    d = 0.0d;
                    if (((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.previousEventTime.subtract(root)).abs()).subtract(this.convergence)).getReal() > 0.0d) {
                        break;
                    }
                    realFieldElement3 = tb2;
                    ga = gb;
                    int i72 = i2;
                    T ga32 = ga;
                    RealFieldElement realFieldElement52 = realFieldElement3;
                    i = i72;
                    realFieldElement4 = realFieldElement52;
                    tb = ga32;
                } else {
                    d = d2;
                    int i8 = i5;
                    T t3 = ga2;
                    RealFieldElement realFieldElement7 = realFieldElement4;
                    T ta = tb2;
                    tb = gb;
                    i = i8;
                    realFieldElement4 = ta;
                }
                i4 = i + 1;
                ga2 = tb;
                d2 = d;
            } else {
                T t4 = ga2;
                RealFieldElement realFieldElement8 = realFieldElement4;
                this.pendingEvent = false;
                this.pendingEventTime = null;
                return false;
            }
        }
        this.pendingEventTime = root;
        this.pendingEvent = true;
        return true;
    }

    public T getEventTime() {
        if (this.pendingEvent) {
            return this.pendingEventTime;
        }
        return (RealFieldElement) ((RealFieldElement) this.f651t0.getField().getZero()).add(this.forward ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public void stepAccepted(FieldODEStateAndDerivative<T> state) {
        this.f651t0 = state.getTime();
        this.f650g0 = this.handler.mo18663g(state);
        boolean z = false;
        if (!this.pendingEvent || ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.pendingEventTime.subtract(state.getTime())).abs()).subtract(this.convergence)).getReal() > 0.0d) {
            if (this.f650g0.getReal() >= 0.0d) {
                z = true;
            }
            this.g0Positive = z;
            this.nextAction = Action.CONTINUE;
            return;
        }
        this.previousEventTime = state.getTime();
        this.g0Positive = this.increasing;
        FieldEventHandler<T> fieldEventHandler = this.handler;
        if (!(this.increasing ^ this.forward)) {
            z = true;
        }
        this.nextAction = fieldEventHandler.eventOccurred(state, z);
    }

    public boolean stop() {
        return this.nextAction == Action.STOP;
    }

    public FieldODEState<T> reset(FieldODEStateAndDerivative<T> state) {
        FieldODEState fieldODEState;
        if (!this.pendingEvent || ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.pendingEventTime.subtract(state.getTime())).abs()).subtract(this.convergence)).getReal() > 0.0d) {
            return null;
        }
        if (this.nextAction == Action.RESET_STATE) {
            fieldODEState = this.handler.resetState(state);
        } else if (this.nextAction == Action.RESET_DERIVATIVES) {
            fieldODEState = state;
        } else {
            fieldODEState = 0;
        }
        this.pendingEvent = false;
        this.pendingEventTime = null;
        return fieldODEState;
    }
}
