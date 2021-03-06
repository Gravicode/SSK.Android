package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

public class GillFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public GillFieldIntegrator(Field<T> field, T step) {
        super(field, "Gill", step);
    }

    public T[] getC() {
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 3);
        c[0] = fraction(1, 2);
        c[1] = c[0];
        c[2] = (RealFieldElement) getField().getOne();
        return c;
    }

    public T[][] getA() {
        T sqrtTwo = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getZero()).add(2.0d)).sqrt();
        T[][] a = (RealFieldElement[][]) MathArrays.buildArray(getField(), 3, -1);
        for (int i = 0; i < a.length; i++) {
            a[i] = (RealFieldElement[]) MathArrays.buildArray(getField(), i + 1);
        }
        a[0][0] = fraction(1, 2);
        a[1][0] = (RealFieldElement) ((RealFieldElement) sqrtTwo.subtract(1.0d)).multiply(0.5d);
        a[1][1] = (RealFieldElement) ((RealFieldElement) sqrtTwo.subtract(2.0d)).multiply(-0.5d);
        a[2][0] = (RealFieldElement) getField().getZero();
        a[2][1] = (RealFieldElement) sqrtTwo.multiply(-0.5d);
        a[2][2] = (RealFieldElement) ((RealFieldElement) sqrtTwo.add(2.0d)).multiply(0.5d);
        return a;
    }

    public T[] getB() {
        T sqrtTwo = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) getField().getZero()).add(2.0d)).sqrt();
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 4);
        b[0] = fraction(1, 6);
        b[1] = (RealFieldElement) ((RealFieldElement) sqrtTwo.subtract(2.0d)).divide(-6.0d);
        b[2] = (RealFieldElement) ((RealFieldElement) sqrtTwo.add(2.0d)).divide(6.0d);
        b[3] = b[0];
        return b;
    }

    /* access modifiers changed from: protected */
    public GillFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        GillFieldStepInterpolator gillFieldStepInterpolator = new GillFieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return gillFieldStepInterpolator;
    }
}
