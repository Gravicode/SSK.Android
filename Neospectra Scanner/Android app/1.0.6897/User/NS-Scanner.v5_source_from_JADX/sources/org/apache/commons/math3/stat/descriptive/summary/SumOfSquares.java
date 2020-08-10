package org.apache.commons.math3.stat.descriptive.summary;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

public class SumOfSquares extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 1460986908574398008L;

    /* renamed from: n */
    private long f790n;
    private double value;

    public SumOfSquares() {
        this.f790n = 0;
        this.value = 0.0d;
    }

    public SumOfSquares(SumOfSquares original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        this.value += d * d;
        this.f790n++;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.f790n;
    }

    public void clear() {
        this.value = 0.0d;
        this.f790n = 0;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length, true)) {
            return Double.NaN;
        }
        double sumSq = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            sumSq += values[i] * values[i];
        }
        return sumSq;
    }

    public SumOfSquares copy() {
        SumOfSquares result = new SumOfSquares();
        copy(this, result);
        return result;
    }

    public static void copy(SumOfSquares source, SumOfSquares dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f790n = source.f790n;
        dest.value = source.value;
    }
}
