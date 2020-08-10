package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
import org.apache.commons.math3.stat.descriptive.summary.Sum;
import org.apache.commons.math3.util.MathUtils;

public class Mean extends AbstractStorelessUnivariateStatistic implements Serializable, WeightedEvaluation {
    private static final long serialVersionUID = -1296043746617791564L;
    protected boolean incMoment;
    protected FirstMoment moment;

    public Mean() {
        this.incMoment = true;
        this.moment = new FirstMoment();
    }

    public Mean(FirstMoment m1) {
        this.moment = m1;
        this.incMoment = false;
    }

    public Mean(Mean original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }

    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    public double getResult() {
        return this.moment.f778m1;
    }

    public long getN() {
        return this.moment.getN();
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        if (!test(values, begin, length)) {
            return Double.NaN;
        }
        double sampleSize = (double) length;
        double xbar = new Sum().evaluate(values, begin, length) / sampleSize;
        double correction = 0.0d;
        for (int i = begin; i < begin + length; i++) {
            correction += values[i] - xbar;
        }
        return (correction / sampleSize) + xbar;
    }

    public double evaluate(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        double[] dArr = values;
        double[] dArr2 = weights;
        int i = begin;
        int i2 = length;
        if (!test(values, weights, begin, length)) {
            return Double.NaN;
        }
        Sum sum = new Sum();
        double sumw = sum.evaluate(dArr2, i, i2);
        double xbarw = sum.evaluate(dArr, dArr2, i, i2) / sumw;
        double correction = 0.0d;
        for (int i3 = i; i3 < i + i2; i3++) {
            correction += dArr2[i3] * (dArr[i3] - xbarw);
        }
        return (correction / sumw) + xbarw;
    }

    public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }

    public Mean copy() {
        Mean result = new Mean();
        copy(this, result);
        return result;
    }

    public static void copy(Mean source, Mean dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.incMoment = source.incMoment;
        dest.moment = source.moment.copy();
    }
}
