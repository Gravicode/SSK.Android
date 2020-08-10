package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class MidpointFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    MidpointFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
    }

    /* access modifiers changed from: protected */
    public MidpointFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        MidpointFieldStepInterpolator midpointFieldStepInterpolator = new MidpointFieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return midpointFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T coeffDot2 = (RealFieldElement) theta.multiply(2);
        T coeffDot1 = (RealFieldElement) ((RealFieldElement) time.getField().getOne()).subtract(coeffDot2);
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            interpolatedState = currentStateLinearCombination((RealFieldElement) oneMinusThetaH.multiply(theta), (RealFieldElement) ((RealFieldElement) oneMinusThetaH.multiply(theta.add(1.0d))).negate());
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2);
        } else {
            interpolatedState = previousStateLinearCombination((RealFieldElement) theta.multiply(oneMinusThetaH), (RealFieldElement) theta.multiply(thetaH));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2);
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
