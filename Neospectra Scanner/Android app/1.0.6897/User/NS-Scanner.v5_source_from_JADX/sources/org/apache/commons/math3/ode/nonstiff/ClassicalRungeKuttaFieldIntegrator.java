package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

public class ClassicalRungeKuttaFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public ClassicalRungeKuttaFieldIntegrator(Field<T> field, T step) {
        super(field, "classical Runge-Kutta", step);
    }

    public T[] getC() {
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 3);
        c[0] = (RealFieldElement) ((RealFieldElement) getField().getOne()).multiply(0.5d);
        c[1] = c[0];
        c[2] = (RealFieldElement) getField().getOne();
        return c;
    }

    public T[][] getA() {
        T[][] a = (RealFieldElement[][]) MathArrays.buildArray(getField(), 3, -1);
        for (int i = 0; i < a.length; i++) {
            a[i] = (RealFieldElement[]) MathArrays.buildArray(getField(), i + 1);
        }
        a[0][0] = fraction(1, 2);
        a[1][0] = (RealFieldElement) getField().getZero();
        a[1][1] = a[0][0];
        a[2][0] = (RealFieldElement) getField().getZero();
        a[2][1] = (RealFieldElement) getField().getZero();
        a[2][2] = (RealFieldElement) getField().getOne();
        return a;
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 4);
        b[0] = fraction(1, 6);
        b[1] = fraction(1, 3);
        b[2] = b[1];
        b[3] = b[0];
        return b;
    }

    /* access modifiers changed from: protected */
    public ClassicalRungeKuttaFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        ClassicalRungeKuttaFieldStepInterpolator classicalRungeKuttaFieldStepInterpolator = new ClassicalRungeKuttaFieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return classicalRungeKuttaFieldStepInterpolator;
    }
}
