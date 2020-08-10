package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class ThreeEighthesFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    ThreeEighthesFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
    }

    /* access modifiers changed from: protected */
    public ThreeEighthesFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        ThreeEighthesFieldStepInterpolator threeEighthesFieldStepInterpolator = new ThreeEighthesFieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return threeEighthesFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t = theta;
        T coeffDot3 = (RealFieldElement) t.multiply(0.75d);
        T coeffDot1 = (RealFieldElement) ((RealFieldElement) coeffDot3.multiply(((RealFieldElement) t.multiply(4)).subtract(5.0d))).add(1.0d);
        T coeffDot2 = (RealFieldElement) coeffDot3.multiply(((RealFieldElement) t.multiply(-6)).add(5.0d));
        T coeffDot4 = (RealFieldElement) coeffDot3.multiply(((RealFieldElement) t.multiply(2)).subtract(1.0d));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T t2 = thetaH;
            T s = (RealFieldElement) oneMinusThetaH.divide(-8.0d);
            T fourTheta2 = (RealFieldElement) ((RealFieldElement) t.multiply(t)).multiply(4);
            T thetaPlus1 = (RealFieldElement) t.add(1.0d);
            T[] interpolatedState2 = currentStateLinearCombination((RealFieldElement) s.multiply(((RealFieldElement) ((RealFieldElement) fourTheta2.multiply(2)).subtract(t.multiply(7))).add(1.0d)), (RealFieldElement) ((RealFieldElement) s.multiply(thetaPlus1.subtract(fourTheta2))).multiply(3), (RealFieldElement) ((RealFieldElement) s.multiply(thetaPlus1)).multiply(3), (RealFieldElement) s.multiply(thetaPlus1.add(fourTheta2)));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot4);
            interpolatedState = interpolatedState2;
        } else {
            T s2 = (RealFieldElement) thetaH.divide(8.0d);
            T fourTheta22 = (RealFieldElement) ((RealFieldElement) t.multiply(t)).multiply(4);
            interpolatedState = previousStateLinearCombination((RealFieldElement) s2.multiply(((RealFieldElement) ((RealFieldElement) fourTheta22.multiply(2)).subtract(t.multiply(15))).add(8.0d)), (RealFieldElement) ((RealFieldElement) s2.multiply(((RealFieldElement) t.multiply(5)).subtract(fourTheta22))).multiply(3), (RealFieldElement) ((RealFieldElement) s2.multiply(t)).multiply(3), (RealFieldElement) s2.multiply(fourTheta22.subtract(t.multiply(3))));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot4);
            T t3 = oneMinusThetaH;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
