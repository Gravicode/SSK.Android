package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

class DormandPrince54FieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    private final T a70;
    private final T a72;
    private final T a73;
    private final T a74;
    private final T a75;

    /* renamed from: d0 */
    private final T f660d0;

    /* renamed from: d2 */
    private final T f661d2;

    /* renamed from: d3 */
    private final T f662d3;

    /* renamed from: d4 */
    private final T f663d4;

    /* renamed from: d5 */
    private final T f664d5;

    /* renamed from: d6 */
    private final T f665d6;

    DormandPrince54FieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        T one = (RealFieldElement) field.getOne();
        this.a70 = (RealFieldElement) ((RealFieldElement) one.multiply(35.0d)).divide(384.0d);
        this.a72 = (RealFieldElement) ((RealFieldElement) one.multiply(500.0d)).divide(1113.0d);
        this.a73 = (RealFieldElement) ((RealFieldElement) one.multiply(125.0d)).divide(192.0d);
        this.a74 = (RealFieldElement) ((RealFieldElement) one.multiply(-2187.0d)).divide(6784.0d);
        this.a75 = (RealFieldElement) ((RealFieldElement) one.multiply(11.0d)).divide(84.0d);
        this.f660d0 = (RealFieldElement) ((RealFieldElement) one.multiply(-1.2715105075E10d)).divide(1.1282082432E10d);
        this.f661d2 = (RealFieldElement) ((RealFieldElement) one.multiply(8.74874797E10d)).divide(3.2700410799E10d);
        this.f662d3 = (RealFieldElement) ((RealFieldElement) one.multiply(-1.0690763975E10d)).divide(1.880347072E9d);
        this.f663d4 = (RealFieldElement) ((RealFieldElement) one.multiply(7.01980252875E11d)).divide(1.99316789632E11d);
        this.f664d5 = (RealFieldElement) ((RealFieldElement) one.multiply(-1.453857185E9d)).divide(8.22651844E8d);
        this.f665d6 = (RealFieldElement) ((RealFieldElement) one.multiply(6.9997945E7d)).divide(2.9380423E7d);
    }

    /* access modifiers changed from: protected */
    public DormandPrince54FieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        DormandPrince54FieldStepInterpolator dormandPrince54FieldStepInterpolator = new DormandPrince54FieldStepInterpolator(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return dormandPrince54FieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        T t = theta;
        T one = (RealFieldElement) time.getField().getOne();
        T eta = (RealFieldElement) one.subtract(t);
        T twoTheta = (RealFieldElement) t.multiply(2);
        T dot2 = (RealFieldElement) one.subtract(twoTheta);
        T dot3 = (RealFieldElement) t.multiply(((RealFieldElement) t.multiply(-3)).add(2.0d));
        T dot4 = (RealFieldElement) twoTheta.multiply(((RealFieldElement) t.multiply(twoTheta.subtract(3.0d))).add(1.0d));
        if (getGlobalPreviousState() == null || theta.getReal() > 0.5d) {
            RealFieldElement realFieldElement = twoTheta;
            T dot22 = dot2;
            T dot42 = dot4;
            T f1 = (RealFieldElement) oneMinusThetaH.negate();
            T t2 = theta;
            T f2 = (RealFieldElement) oneMinusThetaH.multiply(t2);
            T f3 = (RealFieldElement) f2.multiply(t2);
            T eta2 = eta;
            T f4 = (RealFieldElement) f3.multiply(eta2);
            T coeff2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f1.multiply(this.a72)).subtract(f2.multiply(this.a72))).add(f3.multiply(this.a72.multiply(2)))).add(f4.multiply(this.f661d2));
            T coeff3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f1.multiply(this.a73)).subtract(f2.multiply(this.a73))).add(f3.multiply(this.a73.multiply(2)))).add(f4.multiply(this.f662d3));
            T coeff4 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f1.multiply(this.a74)).subtract(f2.multiply(this.a74))).add(f3.multiply(this.a74.multiply(2)))).add(f4.multiply(this.f663d4));
            RealFieldElement realFieldElement2 = f1;
            T coeff5 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f1.multiply(this.a75)).subtract(f2.multiply(this.a75))).add(f3.multiply(this.a75.multiply(2)))).add(f4.multiply(this.f664d5));
            T coeff6 = (RealFieldElement) ((RealFieldElement) f4.multiply(this.f665d6)).subtract(f3);
            RealFieldElement realFieldElement3 = f2;
            T dot23 = dot22;
            RealFieldElement realFieldElement4 = f3;
            RealFieldElement realFieldElement5 = eta2;
            T dot43 = dot42;
            T coeffDot0 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a70.subtract(dot23.multiply(this.a70.subtract(1.0d)))).add(dot3.multiply(((RealFieldElement) this.a70.multiply(2)).subtract(1.0d)))).add(dot43.multiply(this.f660d0));
            T coeffDot1 = (RealFieldElement) time.getField().getZero();
            RealFieldElement realFieldElement6 = f4;
            T coeffDot2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a72.subtract(dot23.multiply(this.a72))).add(dot3.multiply(this.a72.multiply(2)))).add(dot43.multiply(this.f661d2));
            T coeffDot3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a73.subtract(dot23.multiply(this.a73))).add(dot3.multiply(this.a73.multiply(2)))).add(dot43.multiply(this.f662d3));
            T coeffDot4 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a74.subtract(dot23.multiply(this.a74))).add(dot3.multiply(this.a74.multiply(2)))).add(dot43.multiply(this.f663d4));
            RealFieldElement realFieldElement7 = dot23;
            T coeffDot5 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a75.subtract(dot23.multiply(this.a75))).add(dot3.multiply(this.a75.multiply(2)))).add(dot43.multiply(this.f664d5));
            T coeffDot6 = (RealFieldElement) ((RealFieldElement) dot43.multiply(this.f665d6)).subtract(dot3);
            RealFieldElement realFieldElement8 = dot43;
            T[] interpolatedState2 = currentStateLinearCombination((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f1.multiply(this.a70)).subtract(f2.multiply(this.a70.subtract(1.0d)))).add(f3.multiply(((RealFieldElement) this.a70.multiply(2)).subtract(1.0d)))).add(f4.multiply(this.f660d0)), (RealFieldElement) time.getField().getZero(), coeff2, coeff3, coeff4, coeff5, coeff6);
            interpolatedDerivatives = derivativeLinearCombination(coeffDot0, coeffDot1, coeffDot2, coeffDot3, coeffDot4, coeffDot5, coeffDot6);
            interpolatedState = interpolatedState2;
        } else {
            T f12 = thetaH;
            T f22 = (RealFieldElement) f12.multiply(eta);
            T f32 = (RealFieldElement) f22.multiply(t);
            T f42 = (RealFieldElement) f32.multiply(eta);
            RealFieldElement realFieldElement9 = one;
            T eta3 = eta;
            T coeff22 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f12.multiply(this.a72)).subtract(f22.multiply(this.a72))).add(f32.multiply(this.a72.multiply(2)))).add(f42.multiply(this.f661d2));
            T coeff32 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f12.multiply(this.a73)).subtract(f22.multiply(this.a73))).add(f32.multiply(this.a73.multiply(2)))).add(f42.multiply(this.f662d3));
            RealFieldElement realFieldElement10 = twoTheta;
            T coeff42 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f12.multiply(this.a74)).subtract(f22.multiply(this.a74))).add(f32.multiply(this.a74.multiply(2)))).add(f42.multiply(this.f663d4));
            T t3 = f12;
            T coeff52 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f12.multiply(this.a75)).subtract(f22.multiply(this.a75))).add(f32.multiply(this.a75.multiply(2)))).add(f42.multiply(this.f664d5));
            T coeff62 = (RealFieldElement) ((RealFieldElement) f42.multiply(this.f665d6)).subtract(f32);
            RealFieldElement realFieldElement11 = f22;
            RealFieldElement realFieldElement12 = f32;
            RealFieldElement realFieldElement13 = f42;
            T coeffDot02 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a70.subtract(dot2.multiply(this.a70.subtract(1.0d)))).add(dot3.multiply(((RealFieldElement) this.a70.multiply(2)).subtract(1.0d)))).add(dot4.multiply(this.f660d0));
            T coeffDot12 = (RealFieldElement) time.getField().getZero();
            T coeffDot22 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a72.subtract(dot2.multiply(this.a72))).add(dot3.multiply(this.a72.multiply(2)))).add(dot4.multiply(this.f661d2));
            T coeffDot32 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a73.subtract(dot2.multiply(this.a73))).add(dot3.multiply(this.a73.multiply(2)))).add(dot4.multiply(this.f662d3));
            T coeffDot42 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a74.subtract(dot2.multiply(this.a74))).add(dot3.multiply(this.a74.multiply(2)))).add(dot4.multiply(this.f663d4));
            T dot24 = dot2;
            T coeffDot52 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.a75.subtract(dot2.multiply(this.a75))).add(dot3.multiply(this.a75.multiply(2)))).add(dot4.multiply(this.f664d5));
            T coeffDot62 = (RealFieldElement) ((RealFieldElement) dot4.multiply(this.f665d6)).subtract(dot3);
            T dot44 = dot4;
            interpolatedState = previousStateLinearCombination((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) f12.multiply(this.a70)).subtract(f22.multiply(this.a70.subtract(1.0d)))).add(f32.multiply(((RealFieldElement) this.a70.multiply(2)).subtract(1.0d)))).add(f42.multiply(this.f660d0)), (RealFieldElement) time.getField().getZero(), coeff22, coeff32, coeff42, coeff52, coeff62);
            interpolatedDerivatives = derivativeLinearCombination(coeffDot02, coeffDot12, coeffDot22, coeffDot32, coeffDot42, coeffDot52, coeffDot62);
            RealFieldElement realFieldElement14 = eta3;
            RealFieldElement realFieldElement15 = dot24;
            RealFieldElement realFieldElement16 = dot44;
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
