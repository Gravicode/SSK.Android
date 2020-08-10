package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

public class EulerFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public EulerFieldIntegrator(Field<T> field, T step) {
        super(field, "Euler", step);
    }

    public T[] getC() {
        return (RealFieldElement[]) MathArrays.buildArray(getField(), 0);
    }

    public T[][] getA() {
        return (RealFieldElement[][]) MathArrays.buildArray(getField(), 0, 0);
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 1);
        b[0] = (RealFieldElement) getField().getOne();
        return b;
    }

    /* access modifiers changed from: protected */
    public EulerFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        EulerFieldStepInterpolator eulerFieldStepInterpolator = new EulerFieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return eulerFieldStepInterpolator;
    }
}
