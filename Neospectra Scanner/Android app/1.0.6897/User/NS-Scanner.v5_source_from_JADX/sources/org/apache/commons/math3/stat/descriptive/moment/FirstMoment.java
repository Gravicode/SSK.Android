package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

class FirstMoment extends AbstractStorelessUnivariateStatistic implements Serializable {
    private static final long serialVersionUID = 6112755307178490473L;
    protected double dev;

    /* renamed from: m1 */
    protected double f778m1;

    /* renamed from: n */
    protected long f779n;
    protected double nDev;

    FirstMoment() {
        this.f779n = 0;
        this.f778m1 = Double.NaN;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    FirstMoment(FirstMoment original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (this.f779n == 0) {
            this.f778m1 = 0.0d;
        }
        this.f779n++;
        double n0 = (double) this.f779n;
        this.dev = d - this.f778m1;
        this.nDev = this.dev / n0;
        this.f778m1 += this.nDev;
    }

    public void clear() {
        this.f778m1 = Double.NaN;
        this.f779n = 0;
        this.dev = Double.NaN;
        this.nDev = Double.NaN;
    }

    public double getResult() {
        return this.f778m1;
    }

    public long getN() {
        return this.f779n;
    }

    public FirstMoment copy() {
        FirstMoment result = new FirstMoment();
        copy(this, result);
        return result;
    }

    public static void copy(FirstMoment source, FirstMoment dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.f779n = source.f779n;
        dest.f778m1 = source.f778m1;
        dest.dev = source.dev;
        dest.nDev = source.nDev;
    }
}
