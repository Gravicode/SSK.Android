package org.apache.commons.math3.stat.descriptive.rank;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

public class Min extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = -2941995784909003131L;

    /* renamed from: n */
    private long f785n;
    private double value;

    public Min() {
        this.f785n = 0;
        this.value = Double.NaN;
    }

    public Min(Min original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (d < this.value || Double.isNaN(this.value)) {
            this.value = d;
        }
        this.f785n++;
    }

    public void clear() {
        this.value = Double.NaN;
        this.f785n = 0;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.f785n;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length)) {
            return Double.NaN;
        }
        double min = values[begin];
        int i = begin;
        while (i < begin + length) {
            if (!Double.isNaN(values[i])) {
                min = min < values[i] ? min : values[i];
            }
            i++;
        }
        return min;
    }

    public Min copy() {
        Min result = new Min();
        copy(this, result);
        return result;
    }

    public static void copy(Min source, Min dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f785n = source.f785n;
        dest.value = source.value;
    }
}
