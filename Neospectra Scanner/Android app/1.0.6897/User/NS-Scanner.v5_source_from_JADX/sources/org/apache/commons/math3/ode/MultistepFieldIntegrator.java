package org.apache.commons.math3.ode;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeFieldIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853FieldIntegrator;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;
import org.apache.commons.math3.ode.sampling.FieldStepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public abstract class MultistepFieldIntegrator<T extends RealFieldElement<T>> extends AdaptiveStepsizeFieldIntegrator<T> {
    private double exp;
    private double maxGrowth;
    private double minReduction;
    private final int nSteps;
    protected Array2DRowFieldMatrix<T> nordsieck;
    private double safety;
    protected T[] scaled;
    private FirstOrderFieldIntegrator<T> starter;

    private class FieldNordsieckInitializer implements FieldStepHandler<T> {
        private int count = 0;
        private final FieldEquationsMapper<T> mapper;
        private FieldODEStateAndDerivative<T> savedStart;

        /* renamed from: t */
        private final T[] f643t;

        /* renamed from: y */
        private final T[][] f644y;
        private final T[][] yDot;

        FieldNordsieckInitializer(FieldEquationsMapper<T> mapper2, int nbStartPoints) {
            this.mapper = mapper2;
            this.f643t = (RealFieldElement[]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), nbStartPoints);
            this.f644y = (RealFieldElement[][]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), nbStartPoints, -1);
            this.yDot = (RealFieldElement[][]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), nbStartPoints, -1);
        }

        public void handleStep(FieldStepInterpolator<T> interpolator, boolean isLast) throws MaxCountExceededException {
            if (this.count == 0) {
                FieldODEStateAndDerivative<T> prev = interpolator.getPreviousState();
                this.savedStart = prev;
                this.f643t[this.count] = prev.getTime();
                this.f644y[this.count] = this.mapper.mapState(prev);
                this.yDot[this.count] = this.mapper.mapDerivative(prev);
            }
            this.count++;
            FieldODEStateAndDerivative<T> curr = interpolator.getCurrentState();
            this.f643t[this.count] = curr.getTime();
            this.f644y[this.count] = this.mapper.mapState(curr);
            this.yDot[this.count] = this.mapper.mapDerivative(curr);
            if (this.count == this.f643t.length - 1) {
                MultistepFieldIntegrator.this.setStepSize((RealFieldElement) ((RealFieldElement) this.f643t[this.f643t.length - 1].subtract(this.f643t[0])).divide((double) (this.f643t.length - 1)));
                MultistepFieldIntegrator.this.scaled = (RealFieldElement[]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), this.yDot[0].length);
                for (int j = 0; j < MultistepFieldIntegrator.this.scaled.length; j++) {
                    MultistepFieldIntegrator.this.scaled[j] = (RealFieldElement) this.yDot[0][j].multiply(MultistepFieldIntegrator.this.getStepSize());
                }
                MultistepFieldIntegrator.this.nordsieck = MultistepFieldIntegrator.this.initializeHighOrderDerivatives(MultistepFieldIntegrator.this.getStepSize(), this.f643t, this.f644y, this.yDot);
                MultistepFieldIntegrator.this.setStepStart(this.savedStart);
                throw new InitializationCompletedMarkerException();
            }
        }

        public void init(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, T t) {
        }
    }

    private static class InitializationCompletedMarkerException extends RuntimeException {
        private static final long serialVersionUID = -1914085471038046418L;

        InitializationCompletedMarkerException() {
            super(null);
        }
    }

    /* access modifiers changed from: protected */
    public abstract Array2DRowFieldMatrix<T> initializeHighOrderDerivatives(T t, T[] tArr, T[][] tArr2, T[][] tArr3);

    protected MultistepFieldIntegrator(Field<T> field, String name, int nSteps2, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        int i = nSteps2;
        super(field, name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        if (i < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS, Integer.valueOf(nSteps2), Integer.valueOf(2), true);
        }
        DormandPrince853FieldIntegrator dormandPrince853FieldIntegrator = new DormandPrince853FieldIntegrator(field, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.starter = dormandPrince853FieldIntegrator;
        this.nSteps = i;
        this.exp = -1.0d / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    protected MultistepFieldIntegrator(Field<T> field, String name, int nSteps2, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        Field<T> field2 = field;
        super(field2, name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        DormandPrince853FieldIntegrator dormandPrince853FieldIntegrator = new DormandPrince853FieldIntegrator(field2, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.starter = dormandPrince853FieldIntegrator;
        this.nSteps = nSteps2;
        this.exp = -1.0d / ((double) order);
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    public FirstOrderFieldIntegrator<T> getStarterIntegrator() {
        return this.starter;
    }

    public void setStarterIntegrator(FirstOrderFieldIntegrator<T> starterIntegrator) {
        this.starter = starterIntegrator;
    }

    /* access modifiers changed from: protected */
    public void start(FieldExpandableODE<T> equations, FieldODEState<T> initialState, T t) throws DimensionMismatchException, NumberIsTooSmallException, MaxCountExceededException, NoBracketingException {
        this.starter.clearEventHandlers();
        this.starter.clearStepHandlers();
        this.starter.addStepHandler(new FieldNordsieckInitializer(equations.getMapper(), (this.nSteps + 3) / 2));
        try {
            this.starter.integrate(equations, initialState, t);
            throw new MathIllegalStateException(LocalizedFormats.MULTISTEP_STARTER_STOPPED_EARLY, new Object[0]);
        } catch (InitializationCompletedMarkerException e) {
            getEvaluationsCounter().increment(this.starter.getEvaluations());
            this.starter.clearStepHandlers();
        }
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction2) {
        this.minReduction = minReduction2;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth2) {
        this.maxGrowth = maxGrowth2;
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety2) {
        this.safety = safety2;
    }

    public int getNSteps() {
        return this.nSteps;
    }

    /* access modifiers changed from: protected */
    public void rescale(T newStepSize) {
        T ratio = (RealFieldElement) newStepSize.divide(getStepSize());
        for (int i = 0; i < this.scaled.length; i++) {
            this.scaled[i] = (RealFieldElement) this.scaled[i].multiply(ratio);
        }
        T[][] nData = (RealFieldElement[][]) this.nordsieck.getDataRef();
        T power = ratio;
        for (T[] nDataI : nData) {
            power = (RealFieldElement) power.multiply(ratio);
            for (int j = 0; j < nDataI.length; j++) {
                nDataI[j] = (RealFieldElement) nDataI[j].multiply(power);
            }
        }
        setStepSize(newStepSize);
    }

    /* access modifiers changed from: protected */
    public T computeStepGrowShrinkFactor(T error) {
        return MathUtils.min((RealFieldElement) ((RealFieldElement) error.getField().getZero()).add(this.maxGrowth), MathUtils.max((RealFieldElement) ((RealFieldElement) error.getField().getZero()).add(this.minReduction), (RealFieldElement) ((RealFieldElement) error.pow(this.exp)).multiply(this.safety)));
    }
}
