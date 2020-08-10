package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler.Action;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

public class EventState {
    private final double convergence;
    private ExpandableStatefulODE expandable = null;
    private boolean forward;

    /* renamed from: g0 */
    private double f648g0 = Double.NaN;
    private boolean g0Positive = true;
    /* access modifiers changed from: private */
    public final EventHandler handler;
    private boolean increasing = true;
    private final double maxCheckInterval;
    private final int maxIterationCount;
    private Action nextAction = Action.CONTINUE;
    private boolean pendingEvent = false;
    private double pendingEventTime = Double.NaN;
    private double previousEventTime = Double.NaN;
    private final UnivariateSolver solver;

    /* renamed from: t0 */
    private double f649t0 = Double.NaN;

    private static class LocalMaxCountExceededException extends RuntimeException {
        private static final long serialVersionUID = 20120901;
        private final MaxCountExceededException wrapped;

        LocalMaxCountExceededException(MaxCountExceededException exception) {
            this.wrapped = exception;
        }

        public MaxCountExceededException getException() {
            return this.wrapped;
        }
    }

    public EventState(EventHandler handler2, double maxCheckInterval2, double convergence2, int maxIterationCount2, UnivariateSolver solver2) {
        this.handler = handler2;
        this.maxCheckInterval = maxCheckInterval2;
        this.convergence = FastMath.abs(convergence2);
        this.maxIterationCount = maxIterationCount2;
        this.solver = solver2;
    }

    public EventHandler getEventHandler() {
        return this.handler;
    }

    public void setExpandable(ExpandableStatefulODE expandable2) {
        this.expandable = expandable2;
    }

    public double getMaxCheckInterval() {
        return this.maxCheckInterval;
    }

    public double getConvergence() {
        return this.convergence;
    }

    public int getMaxIterationCount() {
        return this.maxIterationCount;
    }

    public void reinitializeBegin(StepInterpolator interpolator) throws MaxCountExceededException {
        this.f649t0 = interpolator.getPreviousTime();
        interpolator.setInterpolatedTime(this.f649t0);
        this.f648g0 = this.handler.mo18647g(this.f649t0, getCompleteState(interpolator));
        if (this.f648g0 == 0.0d) {
            double tStart = this.f649t0 + (0.5d * FastMath.max(this.solver.getAbsoluteAccuracy(), FastMath.abs(this.solver.getRelativeAccuracy() * this.f649t0)));
            interpolator.setInterpolatedTime(tStart);
            this.f648g0 = this.handler.mo18647g(tStart, getCompleteState(interpolator));
        }
        this.g0Positive = this.f648g0 >= 0.0d;
    }

    /* access modifiers changed from: private */
    public double[] getCompleteState(StepInterpolator interpolator) {
        double[] complete = new double[this.expandable.getTotalDimension()];
        this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), complete);
        int index = 0;
        EquationsMapper[] arr$ = this.expandable.getSecondaryMappers();
        int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            int index2 = index + 1;
            arr$[i$].insertEquationData(interpolator.getInterpolatedSecondaryState(index), complete);
            i$++;
            index = index2;
        }
        return complete;
    }

    public boolean evaluateStep(StepInterpolator interpolator) throws MaxCountExceededException, NoBracketingException {
        double t1;
        double h;
        double dt;
        double root;
        double ta;
        final StepInterpolator stepInterpolator = interpolator;
        try {
            this.forward = interpolator.isForward();
            double tb = interpolator.getCurrentTime();
            double dt2 = tb - this.f649t0;
            if (FastMath.abs(dt2) < this.convergence) {
                return false;
            }
            int n = FastMath.max(1, (int) FastMath.ceil(FastMath.abs(dt2) / this.maxCheckInterval));
            double h2 = dt2 / ((double) n);
            C10001 r12 = new UnivariateFunction() {
                public double value(double t) throws LocalMaxCountExceededException {
                    try {
                        stepInterpolator.setInterpolatedTime(t);
                        return EventState.this.handler.mo18647g(t, EventState.this.getCompleteState(stepInterpolator));
                    } catch (MaxCountExceededException mcee) {
                        throw new LocalMaxCountExceededException(mcee);
                    }
                }
            };
            double ta2 = this.f649t0;
            double ga = this.f648g0;
            double ta3 = ta2;
            int i = 0;
            while (i < n) {
                if (i == n - 1) {
                    t1 = tb;
                } else {
                    t1 = tb;
                    tb = (((double) (i + 1)) * h2) + this.f649t0;
                }
                stepInterpolator.setInterpolatedTime(tb);
                double gb = this.handler.mo18647g(tb, getCompleteState(interpolator));
                if (this.g0Positive ^ (gb >= 0.0d)) {
                    this.increasing = gb >= ga;
                    if (this.solver instanceof BracketedUnivariateSolver) {
                        BracketedUnivariateSolver<UnivariateFunction> bracketing = (BracketedUnivariateSolver) this.solver;
                        root = this.forward ? bracketing.solve(this.maxIterationCount, r12, ta3, tb, AllowedSolution.RIGHT_SIDE) : bracketing.solve(this.maxIterationCount, r12, tb, ta3, AllowedSolution.LEFT_SIDE);
                        dt = dt2;
                    } else {
                        double baseRoot = this.forward ? this.solver.solve(this.maxIterationCount, r12, ta3, tb) : this.solver.solve(this.maxIterationCount, r12, tb, ta3);
                        int remainingEval = this.maxIterationCount - this.solver.getEvaluations();
                        dt = dt2;
                        PegasusSolver pegasusSolver = new PegasusSolver(this.solver.getRelativeAccuracy(), this.solver.getAbsoluteAccuracy());
                        root = this.forward ? UnivariateSolverUtils.forceSide(remainingEval, r12, pegasusSolver, baseRoot, ta3, tb, AllowedSolution.RIGHT_SIDE) : UnivariateSolverUtils.forceSide(remainingEval, r12, pegasusSolver, baseRoot, tb, ta3, AllowedSolution.LEFT_SIDE);
                    }
                    double root2 = root;
                    if (!Double.isNaN(this.previousEventTime)) {
                        h = h2;
                        if (FastMath.abs(root2 - ta3) <= this.convergence && FastMath.abs(root2 - this.previousEventTime) <= this.convergence) {
                            while (true) {
                                ta = this.forward ? ta3 + this.convergence : ta3 - this.convergence;
                                ga = r12.value(ta);
                                if (!(this.g0Positive ^ (ga >= 0.0d))) {
                                    break;
                                }
                                if (!(this.forward ^ (ta >= tb))) {
                                    break;
                                }
                                ta3 = ta;
                            }
                            if (this.forward ^ (ta >= tb)) {
                                i--;
                                ta3 = ta;
                            } else {
                                this.pendingEventTime = root2;
                                this.pendingEvent = true;
                                return true;
                            }
                        }
                    } else {
                        h = h2;
                    }
                    if (!Double.isNaN(this.previousEventTime)) {
                        if (FastMath.abs(this.previousEventTime - root2) <= this.convergence) {
                            ta = tb;
                            ga = gb;
                            ta3 = ta;
                        }
                    }
                    this.pendingEventTime = root2;
                    this.pendingEvent = true;
                    return true;
                }
                dt = dt2;
                h = h2;
                ga = gb;
                ta3 = tb;
                i++;
                tb = t1;
                dt2 = dt;
                h2 = h;
                stepInterpolator = interpolator;
            }
            double t12 = tb;
            double d = dt2;
            double d2 = h2;
            this.pendingEvent = false;
            this.pendingEventTime = Double.NaN;
            return false;
        } catch (LocalMaxCountExceededException e) {
            throw e.getException();
        }
    }

    public double getEventTime() {
        if (this.pendingEvent) {
            return this.pendingEventTime;
        }
        return this.forward ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public void stepAccepted(double t, double[] y) {
        this.f649t0 = t;
        this.f648g0 = this.handler.mo18647g(t, y);
        boolean z = false;
        if (!this.pendingEvent || FastMath.abs(this.pendingEventTime - t) > this.convergence) {
            if (this.f648g0 >= 0.0d) {
                z = true;
            }
            this.g0Positive = z;
            this.nextAction = Action.CONTINUE;
            return;
        }
        this.previousEventTime = t;
        this.g0Positive = this.increasing;
        EventHandler eventHandler = this.handler;
        if (!(this.increasing ^ this.forward)) {
            z = true;
        }
        this.nextAction = eventHandler.eventOccurred(t, y, z);
    }

    public boolean stop() {
        return this.nextAction == Action.STOP;
    }

    public boolean reset(double t, double[] y) {
        boolean z = false;
        if (!this.pendingEvent || FastMath.abs(this.pendingEventTime - t) > this.convergence) {
            return false;
        }
        if (this.nextAction == Action.RESET_STATE) {
            this.handler.resetState(t, y);
        }
        this.pendingEvent = false;
        this.pendingEventTime = Double.NaN;
        if (this.nextAction == Action.RESET_STATE || this.nextAction == Action.RESET_DERIVATIVES) {
            z = true;
        }
        return z;
    }
}
