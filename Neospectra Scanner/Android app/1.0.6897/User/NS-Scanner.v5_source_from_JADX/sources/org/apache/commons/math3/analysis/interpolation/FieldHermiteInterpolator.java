package org.apache.commons.math3.analysis.interpolation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class FieldHermiteInterpolator<T extends FieldElement<T>> {
    private final List<T> abscissae = new ArrayList();
    private final List<T[]> bottomDiagonal = new ArrayList();
    private final List<T[]> topDiagonal = new ArrayList();

    public void addSamplePoint(T x, T[]... value) throws ZeroException, MathArithmeticException, DimensionMismatchException, NullArgumentException {
        MathUtils.checkNotNull(x);
        T factorial = (FieldElement) x.getField().getOne();
        for (int i = 0; i < value.length; i++) {
            T[] y = (FieldElement[]) value[i].clone();
            if (i > 1) {
                factorial = (FieldElement) factorial.multiply(i);
                T inv = (FieldElement) factorial.reciprocal();
                for (int j = 0; j < y.length; j++) {
                    y[j] = (FieldElement) y[j].multiply(inv);
                }
            }
            int n = this.abscissae.size();
            this.bottomDiagonal.add(n - i, y);
            T[] bottom0 = y;
            for (int j2 = i; j2 < n; j2++) {
                T[] bottom1 = (FieldElement[]) this.bottomDiagonal.get(n - (j2 + 1));
                if (x.equals(this.abscissae.get(n - (j2 + 1)))) {
                    throw new ZeroException(LocalizedFormats.DUPLICATED_ABSCISSA_DIVISION_BY_ZERO, x);
                }
                T inv2 = (FieldElement) ((FieldElement) x.subtract(this.abscissae.get(n - (j2 + 1)))).reciprocal();
                for (int k = 0; k < y.length; k++) {
                    bottom1[k] = (FieldElement) inv2.multiply(bottom0[k].subtract(bottom1[k]));
                }
                bottom0 = bottom1;
            }
            this.topDiagonal.add(bottom0.clone());
            this.abscissae.add(x);
        }
    }

    public T[] value(T x) throws NoDataException, NullArgumentException {
        MathUtils.checkNotNull(x);
        if (this.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
        T[] value = (FieldElement[]) MathArrays.buildArray(x.getField(), ((FieldElement[]) this.topDiagonal.get(0)).length);
        T valueCoeff = (FieldElement) x.getField().getOne();
        for (int i = 0; i < this.topDiagonal.size(); i++) {
            T[] dividedDifference = (FieldElement[]) this.topDiagonal.get(i);
            for (int k = 0; k < value.length; k++) {
                value[k] = (FieldElement) value[k].add(dividedDifference[k].multiply(valueCoeff));
            }
            valueCoeff = (FieldElement) valueCoeff.multiply((FieldElement) x.subtract(this.abscissae.get(i)));
        }
        return value;
    }

    public T[][] derivatives(T x, int order) throws NoDataException, NullArgumentException {
        FieldHermiteInterpolator fieldHermiteInterpolator = this;
        int i = order;
        MathUtils.checkNotNull(x);
        if (fieldHermiteInterpolator.abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
        T one = (FieldElement) x.getField().getOne();
        T[] tj = (FieldElement[]) MathArrays.buildArray(x.getField(), i + 1);
        tj[0] = (FieldElement) x.getField().getZero();
        for (int i2 = 0; i2 < i; i2++) {
            tj[i2 + 1] = (FieldElement) tj[i2].add(one);
        }
        T[][] derivatives = (FieldElement[][]) MathArrays.buildArray(x.getField(), i + 1, ((FieldElement[]) fieldHermiteInterpolator.topDiagonal.get(0)).length);
        T[] valueCoeff = (FieldElement[]) MathArrays.buildArray(x.getField(), i + 1);
        valueCoeff[0] = (FieldElement) x.getField().getOne();
        int i3 = 0;
        while (i3 < fieldHermiteInterpolator.topDiagonal.size()) {
            T[] dividedDifference = (FieldElement[]) fieldHermiteInterpolator.topDiagonal.get(i3);
            T deltaX = (FieldElement) x.subtract(fieldHermiteInterpolator.abscissae.get(i3));
            int j = i;
            while (j >= 0) {
                int k = 0;
                while (k < derivatives[j].length) {
                    derivatives[j][k] = (FieldElement) derivatives[j][k].add(dividedDifference[k].multiply(valueCoeff[j]));
                    k++;
                }
                valueCoeff[j] = (FieldElement) valueCoeff[j].multiply(deltaX);
                if (j > 0) {
                    valueCoeff[j] = (FieldElement) valueCoeff[j].add(tj[j].multiply(valueCoeff[j - 1]));
                }
                j--;
            }
            i3++;
            fieldHermiteInterpolator = this;
        }
        T t = x;
        return derivatives;
    }
}
