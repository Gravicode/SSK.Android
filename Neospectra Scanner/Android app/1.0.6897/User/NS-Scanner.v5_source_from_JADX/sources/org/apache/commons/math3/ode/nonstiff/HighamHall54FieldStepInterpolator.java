package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class HighamHall54FieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    HighamHall54FieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
    }

    /* access modifiers changed from: protected */
    public HighamHall54FieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        HighamHall54FieldStepInterpolator highamHall54FieldStepInterpolator = new HighamHall54FieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return highamHall54FieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T t) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t2 = theta;
        T t3 = thetaH;
        T bDot0 = (RealFieldElement) ((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-10.0d)).add(16.0d))).add(-7.5d))).add(1.0d);
        T bDot1 = (RealFieldElement) time.getField().getZero();
        T bDot2 = (RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(67.5d)).add(-91.125d))).add(28.6875d));
        T bDot3 = (RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-120.0d)).add(152.0d))).add(-44.0d));
        T bDot4 = (RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(62.5d)).add(-78.125d))).add(23.4375d));
        T bDot5 = (RealFieldElement) ((RealFieldElement) t2.multiply(0.625d)).multiply(((RealFieldElement) t2.multiply(2)).subtract(1.0d));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T bDot42 = bDot4;
            T bDot52 = bDot5;
            T theta2 = (RealFieldElement) t2.multiply(t2);
            T h = (RealFieldElement) t3.divide(t2);
            T bDot22 = bDot2;
            T bDot32 = bDot3;
            T[] interpolatedState2 = currentStateLinearCombination((RealFieldElement) h.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-2.5d)).add(5.333333333333333d))).add(-3.75d))).add(1.0d))).add(-0.08333333333333333d)), (RealFieldElement) time.getField().getZero(), (RealFieldElement) h.multiply(((RealFieldElement) theta2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(16.875d)).add(-30.375d))).add(14.34375d))).add(-0.84375d)), (RealFieldElement) h.multiply(((RealFieldElement) theta2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-30.0d)).add(50.666666666666664d))).add(-22.0d))).add(1.3333333333333333d)), (RealFieldElement) h.multiply(((RealFieldElement) theta2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(15.625d)).add(-26.041666666666668d))).add(11.71875d))).add(-1.3020833333333333d)), (RealFieldElement) h.multiply(((RealFieldElement) theta2.multiply(((RealFieldElement) t2.multiply(0.4166666666666667d)).add(-0.3125d))).add(-0.10416666666666667d)));
            interpolatedDerivatives = derivativeLinearCombination(bDot0, bDot1, bDot22, bDot32, bDot42, bDot52);
            interpolatedState = interpolatedState2;
        } else {
            T bDot43 = bDot4;
            T bDot53 = bDot5;
            interpolatedState = previousStateLinearCombination((RealFieldElement) t3.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-2.5d)).add(5.333333333333333d))).add(-3.75d))).add(1.0d)), (RealFieldElement) time.getField().getZero(), (RealFieldElement) t3.multiply(t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(16.875d)).add(-30.375d))).add(14.34375d))), (RealFieldElement) t3.multiply(t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(-30.0d)).add(50.666666666666664d))).add(-22.0d))), (RealFieldElement) t3.multiply(t2.multiply(((RealFieldElement) t2.multiply(((RealFieldElement) t2.multiply(15.625d)).add(-26.041666666666668d))).add(11.71875d))), (RealFieldElement) t3.multiply(t2.multiply(((RealFieldElement) t2.multiply(0.4166666666666667d)).add(-0.3125d))));
            interpolatedDerivatives = derivativeLinearCombination(bDot0, bDot1, bDot2, bDot3, bDot43, bDot53);
            RealFieldElement realFieldElement = bDot2;
            RealFieldElement realFieldElement2 = bDot3;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
