package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class ClassicalRungeKuttaFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    ClassicalRungeKuttaFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
    }

    /* access modifiers changed from: protected */
    public ClassicalRungeKuttaFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        ClassicalRungeKuttaFieldStepInterpolator classicalRungeKuttaFieldStepInterpolator = new ClassicalRungeKuttaFieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return classicalRungeKuttaFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t = theta;
        T one = (RealFieldElement) time.getField().getOne();
        T oneMinusTheta = (RealFieldElement) one.subtract(t);
        T oneMinus2Theta = (RealFieldElement) one.subtract(t.multiply(2));
        T coeffDot1 = (RealFieldElement) oneMinusTheta.multiply(oneMinus2Theta);
        T coeffDot23 = (RealFieldElement) ((RealFieldElement) t.multiply(oneMinusTheta)).multiply(2);
        T coeffDot4 = (RealFieldElement) ((RealFieldElement) t.multiply(oneMinus2Theta)).negate();
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T t2 = thetaH;
            T fourTheta = (RealFieldElement) t.multiply(4);
            T s = (RealFieldElement) oneMinusThetaH.divide(6.0d);
            T coeff23 = (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(fourTheta.subtract(2.0d))).subtract(2.0d));
            RealFieldElement realFieldElement = one;
            RealFieldElement realFieldElement2 = oneMinusTheta;
            interpolatedState = currentStateLinearCombination((RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) fourTheta.negate()).add(5.0d))).subtract(1.0d)), coeff23, coeff23, (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) fourTheta.negate()).subtract(1.0d))).subtract(1.0d)));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot23, coeffDot23, coeffDot4);
        } else {
            T fourTheta2 = (RealFieldElement) ((RealFieldElement) t.multiply(t)).multiply(4);
            T s2 = (RealFieldElement) thetaH.divide(6.0d);
            T coeff232 = (RealFieldElement) s2.multiply(((RealFieldElement) t.multiply(6)).subtract(fourTheta2));
            interpolatedState = previousStateLinearCombination((RealFieldElement) s2.multiply(((RealFieldElement) fourTheta2.subtract(t.multiply(9))).add(6.0d)), coeff232, coeff232, (RealFieldElement) s2.multiply(fourTheta2.subtract(t.multiply(3))));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot23, coeffDot23, coeffDot4);
            T t3 = oneMinusThetaH;
            RealFieldElement realFieldElement3 = one;
            RealFieldElement realFieldElement4 = oneMinusTheta;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
