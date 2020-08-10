package org.apache.commons.math3.stat.descriptive.summary;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

public class Sum extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = -8231831954703408316L;

    /* renamed from: n */
    private long f788n;
    private double value;

    public Sum() {
        this.f788n = 0;
        this.value = 0.0d;
    }

    public Sum(Sum original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        this.value += d;
        this.f788n++;
    }

    public double getResult() {
        return this.value;
    }

    public long getN() {
        return this.f788n;
    }

    public void clear() {
        this.value = 0.0d;
        this.f788n = 0;
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length, true)) {
            return Double.NaN;
        }
        double sum = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            sum += values[i];
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, weights, begin, length, true)) {
            return Double.NaN;
        }
        double sum = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            sum += values[i] * weights[i];
        }
        return sum;
    }

    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    public Sum copy() {
        Sum result = new Sum();
        copy(this, result);
        return result;
    }

    public static void copy(Sum source, Sum dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f788n = source.f788n;
        dest.value = source.value;
    }
}
