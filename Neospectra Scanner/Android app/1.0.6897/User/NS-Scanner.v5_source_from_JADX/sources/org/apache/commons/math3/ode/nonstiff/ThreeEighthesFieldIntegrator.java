package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

public class ThreeEighthesFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public ThreeEighthesFieldIntegrator(Field<T> field, T step) {
        super(field, "3/8", step);
    }

    public T[] getC() {
        T[] c = (RealFieldElement[]) MathArrays.buildArray(getField(), 3);
        c[0] = fraction(1, 3);
        c[1] = (RealFieldElement) c[0].add(c[0]);
        c[2] = (RealFieldElement) getField().getOne();
        return c;
    }

    public T[][] getA() {
        T[][] a = (RealFieldElement[][]) MathArrays.buildArray(getField(), 3, -1);
        for (int i = 0; i < a.length; i++) {
            a[i] = (RealFieldElement[]) MathArrays.buildArray(getField(), i + 1);
        }
        a[0][0] = fraction(1, 3);
        a[1][0] = (RealFieldElement) a[0][0].negate();
        a[1][1] = (RealFieldElement) getField().getOne();
        a[2][0] = (RealFieldElement) getField().getOne();
        a[2][1] = (RealFieldElement) ((RealFieldElement) getField().getOne()).negate();
        a[2][2] = (RealFieldElement) getField().getOne();
        return a;
    }

    public T[] getB() {
        T[] b = (RealFieldElement[]) MathArrays.buildArray(getField(), 4);
        b[0] = fraction(1, 8);
        b[1] = fraction(3, 8);
        b[2] = b[1];
        b[3] = b[0];
        return b;
    }

    /* access modifiers changed from: protected */
    public ThreeEighthesFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        ThreeEighthesFieldStepInterpolator threeEighthesFieldStepInterpolator = new ThreeEighthesFieldStepInterpolator(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
        return threeEighthesFieldStepInterpolator;
    }
}
