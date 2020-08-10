package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class Kurtosis extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 2784465764798260919L;
    protected boolean incMoment;
    protected FourthMoment moment;

    public Kurtosis() {
        this.incMoment = true;
        this.moment = new FourthMoment();
    }

    public Kurtosis(FourthMoment m4) {
        this.incMoment = false;
        this.moment = m4;
    }

    public Kurtosis(Kurtosis original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (this.incMoment) {
            this.moment.increment(d);
        }
    }

    public double getResult() {
        if (this.moment.getN() <= 3) {
            return Double.NaN;
        }
        double variance = this.moment.f781m2 / ((double) (this.moment.f779n - 1));
        if (this.moment.f779n <= 3 || variance < 1.0E-19d) {
            return 0.0d;
        }
        double n = (double) this.moment.f779n;
        return ((((n + 1.0d) * n) * this.moment.getResult()) - (((this.moment.f781m2 * 3.0d) * this.moment.f781m2) * (n - 1.0d))) / (((((n - 1.0d) * (n - 2.0d)) * (n - 3.0d)) * variance) * variance);
    }

    public void clear() {
        if (this.incMoment) {
            this.moment.clear();
        }
    }

    public long getN() {
        return this.moment.getN();
    }

    public double evaluate(double[] values, int begin, int length) throws MathIllegalArgumentException {
        double[] dArr = values;
        int i = begin;
        int i2 = length;
        if (!test(values, begin, length) || i2 <= 3) {
            return Double.NaN;
        }
        Variance variance = new Variance();
        variance.incrementAll(dArr, i, i2);
        double mean = variance.moment.f778m1;
        double stdDev = FastMath.sqrt(variance.getResult());
        double accum3 = 0.0d;
        int i3 = i;
        while (i3 < i + i2) {
            accum3 += FastMath.pow(dArr[i3] - mean, 4.0d);
            i3++;
            dArr = values;
            i = begin;
        }
        double n0 = (double) i2;
        return ((((n0 + 1.0d) * n0) / (((n0 - 1.0d) * (n0 - 2.0d)) * (n0 - 3.0d))) * (accum3 / FastMath.pow(stdDev, 4.0d))) - ((FastMath.pow(n0 - 1.0d, 2.0d) * 3.0d) / ((n0 - 2.0d) * (n0 - 3.0d)));
    }

    public Kurtosis copy() {
        Kurtosis result = new Kurtosis();
        copy(this, result);
        return result;
    }

    public static void copy(Kurtosis source, Kurtosis dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.moment = source.moment.copy();
        dest.incMoment = source.incMoment;
    }
}
