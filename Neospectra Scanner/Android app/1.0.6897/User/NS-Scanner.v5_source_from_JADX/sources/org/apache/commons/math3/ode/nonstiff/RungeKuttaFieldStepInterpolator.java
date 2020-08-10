package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.util.MathArrays;

abstract class RungeKuttaFieldStepInterpolator<T extends RealFieldElement<T>> extends AbstractFieldStepInterpolator<T> {
    private final Field<T> field;
    private final T[][] yDotK;

    /* access modifiers changed from: protected */
    public abstract RungeKuttaFieldStepInterpolator<T> create(Field<T> field2, boolean z, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative3, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative4, FieldEquationsMapper<T> fieldEquationsMapper);

    protected RungeKuttaFieldStepInterpolator(Field<T> field2, boolean forward, T[][] yDotK2, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(forward, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        this.field = field2;
        this.yDotK = (RealFieldElement[][]) MathArrays.buildArray(field2, yDotK2.length, -1);
        for (int i = 0; i < yDotK2.length; i++) {
            this.yDotK[i] = (RealFieldElement[]) yDotK2[i].clone();
        }
    }

    /* access modifiers changed from: protected */
    public RungeKuttaFieldStepInterpolator<T> create(boolean newForward, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        return create(this.field, newForward, this.yDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
    }

    /* access modifiers changed from: protected */
    public final T[] previousStateLinearCombination(T... coefficients) {
        return combine(getPreviousState().getState(), coefficients);
    }

    /* access modifiers changed from: protected */
    public T[] currentStateLinearCombination(T... coefficients) {
        return combine(getCurrentState().getState(), coefficients);
    }

    /* access modifiers changed from: protected */
    public T[] derivativeLinearCombination(T... coefficients) {
        return combine((RealFieldElement[]) MathArrays.buildArray(this.field, this.yDotK[0].length), coefficients);
    }

    private T[] combine(T[] a, T... coefficients) {
        for (int i = 0; i < a.length; i++) {
            for (int k = 0; k < coefficients.length; k++) {
                a[i] = (RealFieldElement) a[i].add(coefficients[k].multiply(this.yDotK[k][i]));
            }
        }
        return a;
    }
}
