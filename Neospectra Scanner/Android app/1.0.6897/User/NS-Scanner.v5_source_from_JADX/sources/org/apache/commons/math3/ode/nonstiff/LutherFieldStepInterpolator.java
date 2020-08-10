package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class LutherFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    private final T c5a;
    private final T c5b;
    private final T c5c;
    private final T c5d;
    private final T c6a;
    private final T c6b;
    private final T c6c;
    private final T c6d;
    private final T d5a;
    private final T d5b;
    private final T d5c;
    private final T d6a;
    private final T d6b;
    private final T d6c;

    LutherFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        T q = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) field.getZero()).add(21.0d)).sqrt();
        this.c5a = (RealFieldElement) ((RealFieldElement) q.multiply(-49)).add(-49.0d);
        this.c5b = (RealFieldElement) ((RealFieldElement) q.multiply(287)).add(392.0d);
        this.c5c = (RealFieldElement) ((RealFieldElement) q.multiply(-357)).add(-637.0d);
        this.c5d = (RealFieldElement) ((RealFieldElement) q.multiply(343)).add(833.0d);
        this.c6a = (RealFieldElement) ((RealFieldElement) q.multiply(49)).add(-49.0d);
        this.c6b = (RealFieldElement) ((RealFieldElement) q.multiply(-287)).add(392.0d);
        this.c6c = (RealFieldElement) ((RealFieldElement) q.multiply(357)).add(-637.0d);
        this.c6d = (RealFieldElement) ((RealFieldElement) q.multiply(-343)).add(833.0d);
        this.d5a = (RealFieldElement) ((RealFieldElement) q.multiply(49)).add(49.0d);
        this.d5b = (RealFieldElement) ((RealFieldElement) q.multiply(-847)).add(-1372.0d);
        this.d5c = (RealFieldElement) ((RealFieldElement) q.multiply(1029)).add(2254.0d);
        this.d6a = (RealFieldElement) ((RealFieldElement) q.multiply(-49)).add(49.0d);
        this.d6b = (RealFieldElement) ((RealFieldElement) q.multiply(847)).add(-1372.0d);
        this.d6c = (RealFieldElement) ((RealFieldElement) q.multiply(-1029)).add(2254.0d);
    }

    /* access modifiers changed from: protected */
    public LutherFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        LutherFieldStepInterpolator lutherFieldStepInterpolator = new LutherFieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return lutherFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t = theta;
        T coeffDot1 = (RealFieldElement) ((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(21)).add(-47.0d))).add(36.0d))).add(-10.8d))).add(1.0d);
        T coeffDot2 = (RealFieldElement) time.getField().getZero();
        T coeffDot3 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(112)).add(-202.66666666666666d))).add(106.66666666666667d))).add(-13.866666666666667d));
        T coeffDot4 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-113.4d)).add(194.4d))).add(-97.2d))).add(12.96d));
        T coeffDot5 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.c5a.divide(5.0d))).add(this.c5b.divide(15.0d)))).add(this.c5c.divide(30.0d)))).add(this.c5d.divide(150.0d)));
        T coeffDot6 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.c6a.divide(5.0d))).add(this.c6b.divide(15.0d)))).add(this.c6c.divide(30.0d)))).add(this.c6d.divide(150.0d)));
        T coeffDot7 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(3.0d)).add(-3.0d))).add(0.6d));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            T coeffDot42 = coeffDot4;
            T coeffDot52 = coeffDot5;
            T coeffDot62 = coeffDot6;
            T coeffDot72 = coeffDot7;
            T s = oneMinusThetaH;
            interpolatedState = currentStateLinearCombination((RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-4.2d)).add(7.55d))).add(-4.45d))).add(0.95d))).add(-0.05d)), (RealFieldElement) time.getField().getZero(), (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-22.4d)).add(28.266666666666666d))).add(-7.288888888888889d))).add(-0.35555555555555557d))).add(-0.35555555555555557d)), (RealFieldElement) s.multiply(t.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(22.68d)).add(-25.92d))).add(6.48d)))), (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.d5a.divide(25.0d))).add(this.d5b.divide(300.0d)))).add(this.d5c.divide(900.0d)))).add(-0.2722222222222222d))).add(-0.2722222222222222d)), (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.d6a.divide(25.0d))).add(this.d6b.divide(300.0d)))).add(this.d6c.divide(900.0d)))).add(-0.2722222222222222d))).add(-0.2722222222222222d)), (RealFieldElement) s.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-0.75d)).add(0.25d))).add(-0.05d))).add(-0.05d)));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot42, coeffDot52, coeffDot62, coeffDot72);
        } else {
            T s2 = thetaH;
            T coeffDot63 = coeffDot6;
            T coeffDot73 = coeffDot7;
            T coeffDot43 = coeffDot4;
            T coeffDot53 = coeffDot5;
            interpolatedState = previousStateLinearCombination((RealFieldElement) s2.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(4.2d)).add(-11.75d))).add(12.0d))).add(-5.4d))).add(1.0d)), (RealFieldElement) time.getField().getZero(), (RealFieldElement) s2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(22.4d)).add(-50.666666666666664d))).add(35.55555555555556d))).add(-6.933333333333334d))), (RealFieldElement) s2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-22.68d)).add(48.6d))).add(-32.4d))).add(6.48d))), (RealFieldElement) s2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.c5a.divide(25.0d))).add(this.c5b.divide(60.0d)))).add(this.c5c.divide(90.0d)))).add(this.c5d.divide(300.0d)))), (RealFieldElement) s2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(this.c6a.divide(25.0d))).add(this.c6b.divide(60.0d)))).add(this.c6c.divide(90.0d)))).add(this.c6d.divide(300.0d)))), (RealFieldElement) s2.multiply(t.multiply(((RealFieldElement) t.multiply(((RealFieldElement) t.multiply(0.75d)).add(-1.0d))).add(0.3d))));
            interpolatedDerivatives = derivativeLinearCombination(coeffDot1, coeffDot2, coeffDot3, coeffDot43, coeffDot53, coeffDot63, coeffDot73);
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
