package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractFieldIntegrator;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public abstract class AdaptiveStepsizeFieldIntegrator<T extends RealFieldElement<T>> extends AbstractFieldIntegrator<T> {
    private T initialStep;
    protected int mainSetDimension;
    private T maxStep;
    private T minStep;
    protected double scalAbsoluteTolerance;
    protected double scalRelativeTolerance;
    protected double[] vecAbsoluteTolerance;
    protected double[] vecRelativeTolerance;

    public AdaptiveStepsizeFieldIntegrator(Field<T> field, String name, double minStep2, double maxStep2, double scalAbsoluteTolerance2, double scalRelativeTolerance2) {
        super(field, name);
        setStepSizeControl(minStep2, maxStep2, scalAbsoluteTolerance2, scalRelativeTolerance2);
        resetInternalState();
    }

    public AdaptiveStepsizeFieldIntegrator(Field<T> field, String name, double minStep2, double maxStep2, double[] vecAbsoluteTolerance2, double[] vecRelativeTolerance2) {
        super(field, name);
        setStepSizeControl(minStep2, maxStep2, vecAbsoluteTolerance2, vecRelativeTolerance2);
        resetInternalState();
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double absoluteTolerance, double relativeTolerance) {
        this.minStep = (RealFieldElement) ((RealFieldElement) getField().getZero()).add(FastMath.abs(minimalStep));
        this.maxStep = (RealFieldElement) ((RealFieldElement) getField().getZero()).add(FastMath.abs(maximalStep));
        this.initialStep = (RealFieldElement) ((RealFieldElement) getField().getOne()).negate();
        this.scalAbsoluteTolerance = absoluteTolerance;
        this.scalRelativeTolerance = relativeTolerance;
        this.vecAbsoluteTolerance = null;
        this.vecRelativeTolerance = null;
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double[] absoluteTolerance, double[] relativeTolerance) {
        this.minStep = (RealFieldElement) ((RealFieldElement) getField().getZero()).add(FastMath.abs(minimalStep));
        this.maxStep = (RealFieldElement) ((RealFieldElement) getField().getZero()).add(FastMath.abs(maximalStep));
        this.initialStep = (RealFieldElement) ((RealFieldElement) getField().getOne()).negate();
        this.scalAbsoluteTolerance = 0.0d;
        this.scalRelativeTolerance = 0.0d;
        this.vecAbsoluteTolerance = (double[]) absoluteTolerance.clone();
        this.vecRelativeTolerance = (double[]) relativeTolerance.clone();
    }

    public void setInitialStepSize(T initialStepSize) {
        if (((RealFieldElement) initialStepSize.subtract(this.minStep)).getReal() < 0.0d || ((RealFieldElement) initialStepSize.subtract(this.maxStep)).getReal() > 0.0d) {
            this.initialStep = (RealFieldElement) ((RealFieldElement) getField().getOne()).negate();
        } else {
            this.initialStep = initialStepSize;
        }
    }

    /* access modifiers changed from: protected */
    public void sanityChecks(FieldODEState<T> eqn, T t) throws DimensionMismatchException, NumberIsTooSmallException {
        super.sanityChecks(eqn, t);
        this.mainSetDimension = eqn.getStateDimension();
        if (this.vecAbsoluteTolerance != null && this.vecAbsoluteTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecAbsoluteTolerance.length);
        } else if (this.vecRelativeTolerance != null && this.vecRelativeTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecRelativeTolerance.length);
        }
    }

    public T initializeStep(boolean forward, int order, T[] scale, FieldODEStateAndDerivative<T> state0, FieldEquationsMapper<T> mapper) throws MaxCountExceededException, DimensionMismatchException {
        T h1;
        T[] tArr = scale;
        FieldODEStateAndDerivative<T> fieldODEStateAndDerivative = state0;
        FieldEquationsMapper<T> fieldEquationsMapper = mapper;
        if (this.initialStep.getReal() > 0.0d) {
            return forward ? this.initialStep : (RealFieldElement) this.initialStep.negate();
        }
        T[] y0 = fieldEquationsMapper.mapState(fieldODEStateAndDerivative);
        T[] yDot0 = fieldEquationsMapper.mapDerivative(fieldODEStateAndDerivative);
        T yDotOnScale2 = (RealFieldElement) getField().getZero();
        T yOnScale2 = (RealFieldElement) getField().getZero();
        for (int j = 0; j < tArr.length; j++) {
            T ratio = (RealFieldElement) y0[j].divide(tArr[j]);
            yOnScale2 = (RealFieldElement) yOnScale2.add(ratio.multiply(ratio));
            T ratioDot = (RealFieldElement) yDot0[j].divide(tArr[j]);
            yDotOnScale2 = (RealFieldElement) yDotOnScale2.add(ratioDot.multiply(ratioDot));
        }
        T h = (RealFieldElement) ((yOnScale2.getReal() < 1.0E-10d || yDotOnScale2.getReal() < 1.0E-10d) ? ((RealFieldElement) getField().getZero()).add(1.0E-6d) : ((RealFieldElement) ((RealFieldElement) yOnScale2.divide(yDotOnScale2)).sqrt()).multiply(0.01d));
        if (!forward) {
            h = (RealFieldElement) h.negate();
        }
        T[] y1 = (RealFieldElement[]) MathArrays.buildArray(getField(), y0.length);
        for (int j2 = 0; j2 < y0.length; j2++) {
            y1[j2] = (RealFieldElement) y0[j2].add(yDot0[j2].multiply(h));
        }
        T[] yDot1 = computeDerivatives((RealFieldElement) state0.getTime().add(h), y1);
        T yDDotOnScale = (RealFieldElement) getField().getZero();
        int j3 = 0;
        while (true) {
            int j4 = j3;
            if (j4 >= tArr.length) {
                break;
            }
            T ratioDotDot = (RealFieldElement) ((RealFieldElement) yDot1[j4].subtract(yDot0[j4])).divide(tArr[j4]);
            yDDotOnScale = (RealFieldElement) yDDotOnScale.add(ratioDotDot.multiply(ratioDotDot));
            j3 = j4 + 1;
        }
        T yDDotOnScale2 = (RealFieldElement) ((RealFieldElement) yDDotOnScale.sqrt()).divide(h);
        T maxInv2 = MathUtils.max((RealFieldElement) yDotOnScale2.sqrt(), yDDotOnScale2);
        if (maxInv2.getReal() < 1.0E-15d) {
            RealFieldElement[] realFieldElementArr = y0;
            RealFieldElement realFieldElement = yOnScale2;
            RealFieldElement[] realFieldElementArr2 = yDot1;
            h1 = MathUtils.max((RealFieldElement) ((RealFieldElement) getField().getZero()).add(1.0E-6d), (RealFieldElement) ((RealFieldElement) h.abs()).multiply(0.001d));
            int i = order;
            RealFieldElement realFieldElement2 = yDotOnScale2;
            RealFieldElement realFieldElement3 = yDDotOnScale2;
        } else {
            RealFieldElement realFieldElement4 = yOnScale2;
            RealFieldElement[] realFieldElementArr3 = yDot1;
            RealFieldElement realFieldElement5 = yDotOnScale2;
            RealFieldElement realFieldElement6 = yDDotOnScale2;
            h1 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) maxInv2.multiply(100)).reciprocal()).pow(1.0d / ((double) order));
        }
        T h2 = MathUtils.max(this.minStep, MathUtils.min(this.maxStep, MathUtils.max(MathUtils.min((RealFieldElement) ((RealFieldElement) h.abs()).multiply(100), h1), (RealFieldElement) ((RealFieldElement) state0.getTime().abs()).multiply(1.0E-12d))));
        if (!forward) {
            h2 = (RealFieldElement) h2.negate();
        }
        return h2;
    }

    /* access modifiers changed from: protected */
    public T filterStep(T h, boolean forward, boolean acceptSmall) throws NumberIsTooSmallException {
        T filteredH = h;
        if (((RealFieldElement) ((RealFieldElement) h.abs()).subtract(this.minStep)).getReal() < 0.0d) {
            if (acceptSmall) {
                filteredH = forward ? this.minStep : (RealFieldElement) this.minStep.negate();
            } else {
                throw new NumberIsTooSmallException(LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, Double.valueOf(((RealFieldElement) h.abs()).getReal()), Double.valueOf(this.minStep.getReal()), true);
            }
        }
        if (((RealFieldElement) filteredH.subtract(this.maxStep)).getReal() > 0.0d) {
            return this.maxStep;
        }
        if (((RealFieldElement) filteredH.add(this.maxStep)).getReal() < 0.0d) {
            return (RealFieldElement) this.maxStep.negate();
        }
        return filteredH;
    }

    /* access modifiers changed from: protected */
    public void resetInternalState() {
        setStepStart(null);
        setStepSize((RealFieldElement) ((RealFieldElement) this.minStep.multiply(this.maxStep)).sqrt());
    }

    public T getMinStep() {
        return this.minStep;
    }

    public T getMaxStep() {
        return this.maxStep;
    }
}
