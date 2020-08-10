package org.apache.commons.math3.ode;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.util.MathArrays;

public class FieldODEState<T extends RealFieldElement<T>> {
    private final T[][] secondaryState;
    private final T[] state;
    private final T time;

    public FieldODEState(T time2, T[] state2) {
        this(time2, state2, null);
    }

    public FieldODEState(T time2, T[] state2, T[][] secondaryState2) {
        this.time = time2;
        this.state = (RealFieldElement[]) state2.clone();
        this.secondaryState = copy(time2.getField(), secondaryState2);
    }

    /* access modifiers changed from: protected */
    public T[][] copy(Field<T> field, T[][] original) {
        if (original == null) {
            return (RealFieldElement[][]) null;
        }
        T[][] copied = (RealFieldElement[][]) MathArrays.buildArray(field, original.length, -1);
        for (int i = 0; i < original.length; i++) {
            copied[i] = (RealFieldElement[]) original[i].clone();
        }
        return copied;
    }

    public T getTime() {
        return this.time;
    }

    public int getStateDimension() {
        return this.state.length;
    }

    public T[] getState() {
        return (RealFieldElement[]) this.state.clone();
    }

    public int getNumberOfSecondaryStates() {
        if (this.secondaryState == null) {
            return 0;
        }
        return this.secondaryState.length;
    }

    public int getSecondaryStateDimension(int index) {
        return index == 0 ? this.state.length : this.secondaryState[index - 1].length;
    }

    public T[] getSecondaryState(int index) {
        return (RealFieldElement[]) (index == 0 ? this.state : this.secondaryState[index - 1]).clone();
    }
}
