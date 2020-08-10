package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class GillFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    private final T one_minus_inv_sqrt_2;
    private final T one_plus_inv_sqrt_2;

    GillFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        T sqrt = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) field.getZero()).add(0.5d)).sqrt();
        this.one_minus_inv_sqrt_2 = (RealFieldElement) ((RealFieldElement) field.getOne()).subtract(sqrt);
        this.one_plus_inv_sqrt_2 = (RealFieldElement) ((RealFieldElement) field.getOne()).add(sqrt);
    }

    /* access modifiers changed from: protected */
    public GillFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        GillFieldStepInterpolator gillFieldStepInterpolator = new GillFieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return gillFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t = theta;
        T one = (RealFieldElement) time.getField().getOne();
        T twoTheta = (RealFieldElement) t.multiply(2);
        T fourTheta2 = (RealFieldElement) twoTheta.multiply(twoTheta);
        T coeffDot1 = (RealFieldElement) ((RealFieldElement) t.multiply(twoTheta.subtract(3.0d))).add(1.0d);
        T cDot23 = (RealFieldElement) twoTheta.multiply(one.subtract(t));
        T coeffDot2 = (RealFieldElement) cDot23.multiply(this.one_minus_inv_sqrt_2);
        T coeffDot3 = (RealFieldElement) cDot23.multiply(this.one_plus_inv_sqrt_2);
        T coeffDot4 = (RealFieldElement) t.multiply(twoTheta.subtract(1.0d));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T t2 = thetaH;
            RealFieldElement realFieldElement = one;
            RealFieldElement realFieldElement2 = cDot23;
            T s = (RealFieldElement) oneMinusThetaH.divide(-6.0d);
            T twoTheta2 = twoTheta;
            T c23 = (RealFieldElement) s.multiply(((RealFieldElement) twoTheta2.add(2.0d)).subtract(fourTheta2));
            RealFieldElement realFieldElement3 = twoTheta2;
            T[] interpolatedState2 = currentStateLinearCombination((RealFieldElement) s.multiply(((RealFieldElement) fourTheta2.subtract(t.multiply(5))).add(1.0d)), (RealFieldElement) c23.multiply(this.one_minus_inv_sqrt_2), (RealFieldElement) c23.multiply(this.one_plus_inv_sqrt_2), (RealFieldElement) s.multiply(((RealFieldElement) fourTheta2.add(t)).add(1.0d)));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot4);
            interpolatedState = interpolatedState2;
        } else {
            T twoTheta3 = twoTheta;
            T s2 = (RealFieldElement) thetaH.divide(6.0d);
            T c232 = (RealFieldElement) s2.multiply(((RealFieldElement) t.multiply(6)).subtract(fourTheta2));
            RealFieldElement realFieldElement4 = one;
            RealFieldElement realFieldElement5 = cDot23;
            interpolatedState = previousStateLinearCombination((RealFieldElement) s2.multiply(((RealFieldElement) fourTheta2.subtract(t.multiply(9))).add(6.0d)), (RealFieldElement) c232.multiply(this.one_minus_inv_sqrt_2), (RealFieldElement) c232.multiply(this.one_plus_inv_sqrt_2), (RealFieldElement) s2.multiply(fourTheta2.subtract(t.multiply(3))));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot4);
            RealFieldElement realFieldElement6 = twoTheta3;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
