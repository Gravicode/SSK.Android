package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

public class MidpointFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public MidpointFieldIntegrator(Field<T> field, T step) {
        super(field, "midpoint", step);
    }

    public T[] getC() {
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 1);
        c[0] = (RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(0.5d);
        return c;
    }

    public T[][] getA() {
        T[][] a = (RealFieldElement[][]) MathArrays.buildArray(getField(), 1, 1);
        a[0][0] = fraction(1, 2);
        return a;
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 2);
        b[0] = (RealFieldElement) getField().getZero();
        b[1] = (RealFieldElement) getField().getOne();
        return b;
    }

    /* access modifiers changed from: protected */
    public MidpointFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        MidpointFieldStepInterpolator midpointFieldStepInterpolator = new MidpointFieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return midpointFieldStepInterpolator;
    }
}
