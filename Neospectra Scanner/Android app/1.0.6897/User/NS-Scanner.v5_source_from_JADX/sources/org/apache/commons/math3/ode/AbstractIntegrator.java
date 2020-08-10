package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.IntegerSequence.Incrementor;
import org.apache.commons.math3.util.Precision;

public abstract class AbstractIntegrator implements FirstOrderIntegrator {
    private Incrementor evaluations;
    private Collection<EventState> eventsStates;
    private transient ExpandableStatefulODE expandable;
    protected boolean isLastStep;
    private final String name;
    protected boolean resetOccurred;
    private boolean statesInitialized;
    protected Collection<StepHandler> stepHandlers;
    protected double stepSize;
    protected double stepStart;

    public abstract void integrate(ExpandableStatefulODE expandableStatefulODE, double d) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException;

    public AbstractIntegrator(String name2) {
        this.name = name2;
        this.stepHandlers = new ArrayList();
        this.stepStart = Double.NaN;
        this.stepSize = Double.NaN;
        this.eventsStates = new ArrayList();
        this.statesInitialized = false;
        this.evaluations = Incrementor.create().withMaximalCount(Integer.MAX_VALUE);
    }

    protected AbstractIntegrator() {
        this(null);
    }

    public String getName() {
        return this.name;
    }

    public void addStepHandler(StepHandler handler) {
        this.stepHandlers.add(handler);
    }

    public Collection<StepHandler> getStepHandlers() {
        return Collections.unmodifiableCollection(this.stepHandlers);
    }

    public void clearStepHandlers() {
        this.stepHandlers.clear();
    }

    public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new BracketingNthOrderBrentSolver(convergence, 5));
    }

    public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
        Collection<EventState> collection = this.eventsStates;
        EventState eventState = new EventState(handler, maxCheckInterval, convergence, maxIterationCount, solver);
        collection.add(eventState);
    }

    public Collection<EventHandler> getEventHandlers() {
        List<EventHandler> list = new ArrayList<>(this.eventsStates.size());
        for (EventState state : this.eventsStates) {
            list.add(state.getEventHandler());
        }
        return Collections.unmodifiableCollection(list);
    }

    public void clearEventHandlers() {
        this.eventsStates.clear();
    }

    public double getCurrentStepStart() {
        return this.stepStart;
    }

    public double getCurrentSignedStepsize() {
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
    public void initIntegration(double t0, double[] y0, double t) {
        this.evaluations = this.evaluations.withStart(0);
        for (EventState state : this.eventsStates) {
            state.setExpandable(this.expandable);
            state.getEventHandler().init(t0, y0, t);
        }
        for (StepHandler handler : this.stepHandlers) {
            handler.init(t0, y0, t);
        }
        setStateInitialized(false);
    }

    /* access modifiers changed from: protected */
    public void setEquations(ExpandableStatefulODE equations) {
        this.expandable = equations;
    }

    /* access modifiers changed from: protected */
    public ExpandableStatefulODE getExpandable() {
        return this.expandable;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public org.apache.commons.math3.util.Incrementor getEvaluationsCounter() {
        return org.apache.commons.math3.util.Incrementor.wrap(this.evaluations);
    }

    /* access modifiers changed from: protected */
    public Incrementor getCounter() {
        return this.evaluations;
    }

    public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t, double[] y) throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        if (y0.length != equations.getDimension()) {
            throw new DimensionMismatchException(y0.length, equations.getDimension());
        } else if (y.length != equations.getDimension()) {
            throw new DimensionMismatchException(y.length, equations.getDimension());
        } else {
            ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
            expandableODE.setTime(t0);
            expandableODE.setPrimaryState(y0);
            integrate(expandableODE, t);
            System.arraycopy(expandableODE.getPrimaryState(), 0, y, 0, y.length);
            return expandableODE.getTime();
        }
    }

    public void computeDerivatives(double t, double[] y, double[] yDot) throws MaxCountExceededException, DimensionMismatchException, NullPointerException {
        this.evaluations.increment();
        this.expandable.computeDerivatives(t, y, yDot);
    }

    /* access modifiers changed from: protected */
    public void setStateInitialized(boolean stateInitialized) {
        this.statesInitialized = stateInitialized;
    }

    /* access modifiers changed from: protected */
    public double acceptStep(AbstractStepInterpolator interpolator, double[] y, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        boolean z;
        AbstractStepInterpolator abstractStepInterpolator = interpolator;
        double[] dArr = y;
        double previousT = interpolator.getGlobalPreviousTime();
        double currentT = interpolator.getGlobalCurrentTime();
        if (!this.statesInitialized) {
            for (EventState state : this.eventsStates) {
                state.reinitializeBegin(abstractStepInterpolator);
            }
            this.statesInitialized = true;
        }
        final int orderingSign = interpolator.isForward() ? 1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() {
            public int compare(EventState es0, EventState es1) {
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });
        for (EventState state2 : this.eventsStates) {
            if (state2.evaluateStep(abstractStepInterpolator)) {
                occurringEvents.add(state2);
            }
        }
        while (!occurringEvents.isEmpty()) {
            Iterator<EventState> iterator = occurringEvents.iterator();
            EventState currentEvent = (EventState) iterator.next();
            iterator.remove();
            double eventT = currentEvent.getEventTime();
            abstractStepInterpolator.setSoftPreviousTime(previousT);
            abstractStepInterpolator.setSoftCurrentTime(eventT);
            abstractStepInterpolator.setInterpolatedTime(eventT);
            double[] eventYComplete = new double[dArr.length];
            this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), eventYComplete);
            int index = 0;
            EquationsMapper[] arr$ = this.expandable.getSecondaryMappers();
            double d = previousT;
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                int len$2 = len$;
                int index2 = index + 1;
                arr$[i$].insertEquationData(abstractStepInterpolator.getInterpolatedSecondaryState(index), eventYComplete);
                i$++;
                len$ = len$2;
                index = index2;
            }
            for (EventState state3 : this.eventsStates) {
                state3.stepAccepted(eventT, eventYComplete);
                this.isLastStep = this.isLastStep || state3.stop();
            }
            for (StepHandler handler : this.stepHandlers) {
                handler.handleStep(abstractStepInterpolator, this.isLastStep);
            }
            if (this.isLastStep) {
                System.arraycopy(eventYComplete, 0, dArr, 0, dArr.length);
                return eventT;
            }
            this.resetOccurred = false;
            if (currentEvent.reset(eventT, eventYComplete)) {
                abstractStepInterpolator.setInterpolatedTime(eventT);
                System.arraycopy(eventYComplete, 0, dArr, 0, dArr.length);
                computeDerivatives(eventT, dArr, yDot);
                this.resetOccurred = true;
                return eventT;
            }
            double[] dArr2 = yDot;
            double previousT2 = eventT;
            abstractStepInterpolator.setSoftPreviousTime(eventT);
            abstractStepInterpolator.setSoftCurrentTime(currentT);
            if (currentEvent.evaluateStep(abstractStepInterpolator)) {
                occurringEvents.add(currentEvent);
            }
            previousT = previousT2;
        }
        double[] dArr3 = yDot;
        double d2 = previousT;
        abstractStepInterpolator.setInterpolatedTime(currentT);
        double[] currentY = new double[dArr.length];
        this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), currentY);
        EquationsMapper[] arr$2 = this.expandable.getSecondaryMappers();
        int len$3 = arr$2.length;
        int index3 = 0;
        int i$2 = 0;
        while (i$2 < len$3) {
            int index4 = index3 + 1;
            arr$2[i$2].insertEquationData(abstractStepInterpolator.getInterpolatedSecondaryState(index3), currentY);
            i$2++;
            index3 = index4;
        }
        for (EventState state4 : this.eventsStates) {
            state4.stepAccepted(currentT, currentY);
            this.isLastStep = this.isLastStep || state4.stop();
        }
        if (!this.isLastStep) {
            z = true;
            if (!Precision.equals(currentT, tEnd, 1)) {
                z = false;
            }
        } else {
            double d3 = tEnd;
            z = true;
        }
        this.isLastStep = z;
        for (StepHandler handler2 : this.stepHandlers) {
            handler2.handleStep(abstractStepInterpolator, this.isLastStep);
        }
        return currentT;
    }

    /* access modifiers changed from: protected */
    public void sanityChecks(ExpandableStatefulODE equations, double t) throws NumberIsTooSmallException, DimensionMismatchException {
        double threshold = FastMath.ulp(FastMath.max(FastMath.abs(equations.getTime()), FastMath.abs(t))) * 1000.0d;
        double dt = FastMath.abs(equations.getTime() - t);
        if (dt <= threshold) {
            throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, Double.valueOf(dt), Double.valueOf(threshold), false);
        }
    }
}
