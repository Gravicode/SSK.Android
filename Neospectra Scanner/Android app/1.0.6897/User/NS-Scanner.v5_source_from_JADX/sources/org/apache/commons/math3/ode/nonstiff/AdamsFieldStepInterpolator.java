package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.util.MathArrays;

class AdamsFieldStepInterpolator<T extends RealFieldElement<T>> extends AbstractFieldStepInterpolator<T> {
    private final Array2DRowFieldMatrix<T> nordsieck;
    private final FieldODEStateAndDerivative<T> reference;
    private final T[] scaled;
    private T scalingH;

    AdamsFieldStepInterpolator(T stepSize, FieldODEStateAndDerivative<T> reference2, T[] scaled2, Array2DRowFieldMatrix<T> nordsieck2, boolean isForward, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> equationsMapper) {
        this(stepSize, reference2, scaled2, nordsieck2, isForward, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, equationsMapper);
    }

    private AdamsFieldStepInterpolator(T stepSize, FieldODEStateAndDerivative<T> reference2, T[] scaled2, Array2DRowFieldMatrix<T> nordsieck2, boolean isForward, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> equationsMapper) {
        super(isForward, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, equationsMapper);
        this.scalingH = stepSize;
        this.reference = reference2;
        this.scaled = (RealFieldElement[]) scaled2.clone();
        this.nordsieck = new Array2DRowFieldMatrix<>((T[][]) nordsieck2.getData(), false);
    }

    /* access modifiers changed from: protected */
    public AdamsFieldStepInterpolator<T> create(boolean newForward, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        AdamsFieldStepInterpolator adamsFieldStepInterpolator = new AdamsFieldStepInterpolator(this.scalingH, this.reference, this.scaled, this.nordsieck, newForward, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
        return adamsFieldStepInterpolator;
    }

    /* access modifiers changed from: protected */
    public FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T time, T t, T t2, T t3) {
        return taylor(this.reference, time, this.scalingH, this.scaled, this.nordsieck);
    }

    public static <S extends RealFieldElement<S>> FieldODEStateAndDerivative<S> taylor(FieldODEStateAndDerivative<S> reference2, S time, S stepSize, S[] scaled2, Array2DRowFieldMatrix<S> nordsieck2) {
        int j;
        S s = time;
        S[] sArr = scaled2;
        S x = (RealFieldElement) s.subtract(reference2.getTime());
        S normalizedAbscissa = (RealFieldElement) x.divide(stepSize);
        S[] stateVariation = (RealFieldElement[]) MathArrays.buildArray(time.getField(), sArr.length);
        Arrays.fill(stateVariation, time.getField().getZero());
        S[] estimatedDerivatives = (RealFieldElement[]) MathArrays.buildArray(time.getField(), sArr.length);
        Arrays.fill(estimatedDerivatives, time.getField().getZero());
        S[][] nData = (RealFieldElement[][]) nordsieck2.getDataRef();
        int i = nData.length;
        while (true) {
            i--;
            j = 0;
            if (i < 0) {
                break;
            }
            int order = i + 2;
            S[] nDataI = nData[i];
            S power = (RealFieldElement) normalizedAbscissa.pow(order);
            while (j < nDataI.length) {
                S d = (RealFieldElement) nDataI[j].multiply(power);
                stateVariation[j] = (RealFieldElement) stateVariation[j].add(d);
                estimatedDerivatives[j] = (RealFieldElement) estimatedDerivatives[j].add(d.multiply(order));
                j++;
            }
        }
        S[] estimatedState = reference2.getState();
        while (j < stateVariation.length) {
            stateVariation[j] = (RealFieldElement) stateVariation[j].add(sArr[j].multiply(normalizedAbscissa));
            estimatedState[j] = (RealFieldElement) estimatedState[j].add(stateVariation[j]);
            estimatedDerivatives[j] = (RealFieldElement) ((RealFieldElement) estimatedDerivatives[j].add(sArr[j].multiply(normalizedAbscissa))).divide(x);
            j++;
        }
        return new FieldODEStateAndDerivative<>(s, estimatedState, estimatedDerivatives);
    }
}
