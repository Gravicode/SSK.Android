package org.apache.commons.math3.ode;

import org.apache.commons.math3.RealFieldElement;

public class FieldODEStateAndDerivative<T extends RealFieldElement<T>> extends FieldODEState<T> {
    private final T[] derivative;
    private final T[][] secondaryDerivative;

    public FieldODEStateAndDerivative(T time, T[] state, T[] derivative2) {
        RealFieldElement[][] realFieldElementArr = null;
        this(time, state, derivative2, realFieldElementArr, realFieldElementArr);
    }

    public FieldODEStateAndDerivative(T time, T[] state, T[] derivative2, T[][] secondaryState, T[][] secondaryDerivative2) {
        super(time, state, secondaryState);
        this.derivative = (RealFieldElement[]) derivative2.clone();
        this.secondaryDerivative = copy(time.getField(), secondaryDerivative2);
    }

    public T[] getDerivative() {
        return (RealFieldElement[]) this.derivative.clone();
    }

    public T[] getSecondaryDerivative(int index) {
        return (RealFieldElement[]) (index == 0 ? this.derivative : this.secondaryDerivative[index - 1]).clone();
    }
}
