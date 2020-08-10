package org.apache.commons.math3.stat.descriptive.rank;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

public class Max extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = -5593383832225844641L;

    /* renamed from: n */
    private long f784n;
    private double value;

    public Max() {
        this.f784n = 0;
        this.value = Double.NaN;
    }

    public Max(Max original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (d > this.value || Double.isNaN(this.value)) {
            this.value = d;
        }
        this.f784n++;
    }

    public void clear() {
        this.value = Double.NaN;
        this.f784n = 0;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.f784n;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length)) {
            return Double.NaN;
        }
        double max = values[begin];
        int i = begin;
        while (i < begin + length) {
            if (!Double.isNaN(values[i])) {
                max = max > values[i] ? max : values[i];
            }
            i++;
        }
        return max;
    }

    public Max copy() {
        Max result = new Max();
        copy(this, result);
        return result;
    }

    public static void copy(Max source, Max dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f784n = source.f784n;
        dest.value = source.value;
    }
}
