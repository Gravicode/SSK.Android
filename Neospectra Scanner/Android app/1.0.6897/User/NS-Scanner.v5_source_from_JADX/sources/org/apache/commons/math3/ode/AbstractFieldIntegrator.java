package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.events.FieldEventHandler;
import org.apache.commons.math3.ode.events.FieldEventState;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.IntegerSequence.Incrementor;

public abstract class AbstractFieldIntegrator<T extends RealFieldElement<T>> implements FirstOrderFieldIntegrator<T> {
    private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.0E-15d;
    private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-14d;
    private transient FieldExpandableODE<T> equations;
    private Incrementor evaluations = Incrementor.create().withMaximalCount(Integer.MAX_VALUE);
    private Collection<FieldEventState<T>> eventsStates = new ArrayList();
    private final Field<T> field;
    private boolean isLastStep;
    private final String name;
    private boolean resetOccurred;
    private boolean statesInitialized = false;
    private Collection<FieldStepHandler<T>> stepHandlers = new ArrayList();
    private T stepSize = null;
    private FieldODEStateAndDerivative<T> stepStart = null;

    protected AbstractFieldIntegrator(Field<T> field2, String name2) {
        this.field = field2;
        this.name = name2;
    }

    public Field<T> getField() {
        return this.field;
    }

    public String getName() {
        return this.name;
    }

    public void addStepHandler(FieldStepHandler<T> handler) {
        this.stepHandlers.add(handler);
    }

    public Collection<FieldStepHandler<T>> getStepHandlers() {
        return Collections.unmodifiableCollection(this.stepHandlers);
    }

    public void clearStepHandlers() {
        this.stepHandlers.clear();
    }

    public void addEventHandler(FieldEventHandler<T> handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new FieldBracketingNthOrderBrentSolver((RealFieldElement) ((RealFieldElement) this.field.getZero()).add(DEFAULT_RELATIVE_ACCURACY), (RealFieldElement) ((RealFieldElement) this.field.getZero()).add(convergence), (RealFieldElement) ((RealFieldElement) this.field.getZero()).add(1.0E-15d), 5));
    }

    public void addEventHandler(FieldEventHandler<T> handler, double maxCheckInterval, double convergence, int maxIterationCount, BracketedRealFieldUnivariateSolver<T> solver) {
        Collection<FieldEventState<T>> collection = this.eventsStates;
        FieldEventState fieldEventState = new FieldEventState(handler, maxCheckInterval, (RealFieldElement) ((RealFieldElement) this.field.getZero()).add(convergence), maxIterationCount, solver);
        collection.add(fieldEventState);
    }

    public Collection<FieldEventHandler<T>> getEventHandlers() {
        List<FieldEventHandler<T>> list = new ArrayList<>(this.eventsStates.size());
        for (FieldEventState<T> state : this.eventsStates) {
            list.add(state.getEventHandler());
        }
        return Collections.unmodifiableCollection(list);
    }

    public void clearEventHandlers() {
        this.eventsStates.clear();
    }

    public FieldODEStateAndDerivative<T> getCurrentStepStart() {
        return this.stepStart;
    }

    public T getCurrentSignedStepsize() {
        return this.stepSize;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        this.evaluations = this.evaluations.withMaximalCount(maxEvaluations < 0 ? Integer.MAX_VALUE : maxEvaluations);
    }

    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> initIntegration(FieldExpandableODE<T> eqn, T t0, T[] y0, T t) {
        this.equations = eqn;
        this.evaluations = this.evaluations.withStart(0);
        eqn.init(t0, y0, t);
        FieldODEStateAndDerivative<T> state0 = new FieldODEStateAndDerivative<>(t0, y0, computeDerivatives(t0, y0));
        for (FieldEventState<T> state : this.eventsStates) {
            state.getEventHandler().init(state0, t);
        }
        for (FieldStepHandler<T> handler : this.stepHandlers) {
            handler.init(state0, t);
        }
        setStateInitialized(false);
        return state0;
    }

    /* access modifiers changed from: protected */
    public FieldExpandableODE<T> getEquations() {
        return this.equations;
    }

    /* access modifiers changed from: protected */
    public Incrementor getEvaluationsCounter() {
        return this.evaluations;
    }

    public T[] computeDerivatives(T t, T[] y) throws DimensionMismatchException, MaxCountExceededException, NullPointerException {
        this.evaluations.increment();
        return this.equations.computeDerivatives(t, y);
    }

    /* access modifiers changed from: protected */
    public void setStateInitialized(boolean stateInitialized) {
        this.statesInitialized = stateInitialized;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> acceptStep(AbstractFieldStepInterpolator<T> interpolator, T tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        AbstractFieldStepInterpolator<T> abstractFieldStepInterpolator = interpolator;
        FieldODEStateAndDerivative<T> previousState = interpolator.getGlobalPreviousState();
        FieldODEStateAndDerivative<T> currentState = interpolator.getGlobalCurrentState();
        boolean z = true;
        if (!this.statesInitialized) {
            for (FieldEventState<T> state : this.eventsStates) {
                state.reinitializeBegin(abstractFieldStepInterpolator);
            }
            this.statesInitialized = true;
        }
        final int orderingSign = interpolator.isForward() ? 1 : -1;
        SortedSet<FieldEventState<T>> occurringEvents = new TreeSet<>(new Comparator<FieldEventState<T>>() {
            public int compare(FieldEventState<T> es0, FieldEventState<T> es1) {
                return orderingSign * Double.compare(es0.getEventTime().getReal(), es1.getEventTime().getReal());
            }
        });
        for (FieldEventState<T> state2 : this.eventsStates) {
            if (state2.evaluateStep(abstractFieldStepInterpolator)) {
                occurringEvents.add(state2);
            }
        }
        FieldODEStateAndDerivative<T> previousState2 = previousState;
        AbstractFieldStepInterpolator<T> restricted = abstractFieldStepInterpolator;
        while (!occurringEvents.isEmpty()) {
            Iterator<FieldEventState<T>> iterator = occurringEvents.iterator();
            FieldEventState<T> currentEvent = (FieldEventState) iterator.next();
            iterator.remove();
            FieldODEStateAndDerivative<T> eventState = restricted.getInterpolatedState(currentEvent.getEventTime());
            AbstractFieldStepInterpolator<T> restricted2 = restricted.restrictStep(previousState2, eventState);
            for (FieldEventState<T> state3 : this.eventsStates) {
                state3.stepAccepted(eventState);
                this.isLastStep = this.isLastStep || state3.stop();
            }
            for (FieldStepHandler<T> handler : this.stepHandlers) {
                handler.handleStep(restricted2, this.isLastStep);
            }
            if (this.isLastStep) {
                return eventState;
            }
            this.resetOccurred = false;
            for (FieldEventState<T> state4 : this.eventsStates) {
                FieldODEState<T> newState = state4.reset(eventState);
                if (newState != null) {
                    T[] y = this.equations.getMapper().mapState(newState);
                    T[] yDot = computeDerivatives(newState.getTime(), y);
                    this.resetOccurred = true;
                    return this.equations.getMapper().mapStateAndDerivative(newState.getTime(), y, yDot);
                }
                AbstractFieldStepInterpolator<T> abstractFieldStepInterpolator2 = interpolator;
            }
            previousState2 = eventState;
            restricted = restricted2.restrictStep(eventState, currentState);
            if (currentEvent.evaluateStep(restricted)) {
                occurringEvents.add(currentEvent);
            }
            AbstractFieldStepInterpolator<T> abstractFieldStepInterpolator3 = interpolator;
        }
        for (FieldEventState<T> state5 : this.eventsStates) {
            state5.stepAccepted(currentState);
            this.isLastStep = this.isLastStep || state5.stop();
        }
        if (this.isLastStep) {
            T t = tEnd;
        } else if (((RealFieldElement) ((RealFieldElement) currentState.getTime().subtract(tEnd)).abs()).getReal() > FastMath.ulp(tEnd.getReal())) {
            z = false;
        }
        this.isLastStep = z;
        for (FieldStepHandler<T> handler2 : this.stepHandlers) {
            handler2.handleStep(restricted, this.isLastStep);
        }
        return currentState;
    }

    /* access modifiers changed from: protected */
    public void sanityChecks(FieldODEState<T> eqn, T t) throws NumberIsTooSmallException, DimensionMismatchException {
        double threshold = FastMath.ulp(FastMath.max(FastMath.abs(eqn.getTime().getReal()), FastMath.abs(t.getReal()))) * 1000.0d;
        double dt = ((RealFieldElement) ((RealFieldElement) eqn.getTime().subtract(t)).abs()).getReal();
        if (dt <= threshold) {
            throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, Double.valueOf(dt), Double.valueOf(threshold), false);
        }
    }

    /* access modifiers changed from: protected */
    public boolean resetOccurred() {
        return this.resetOccurred;
    }

    /* access modifiers changed from: protected */
    public void setStepSize(T stepSize2) {
        this.stepSize = stepSize2;
    }

    /* access modifiers changed from: protected */
    public T getStepSize() {
        return this.stepSize;
    }

    /* access modifiers changed from: protected */
    public void setStepStart(FieldODEStateAndDerivative<T> stepStart2) {
        this.stepStart = stepStart2;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> getStepStart() {
        return this.stepStart;
    }

    /* access modifiers changed from: protected */
    public void setIsLastStep(boolean isLastStep2) {
        this.isLastStep = isLastStep2;
    }

    /* access modifiers changed from: protected */
    public boolean isLastStep() {
        return this.isLastStep;
    }
}
