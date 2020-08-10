package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

class ThirdMoment extends SecondMoment implements Serializable {
    private static final long serialVersionUID = -7818711964045118679L;

    /* renamed from: m3 */
    protected double f782m3;
    protected double nDevSq;

    ThirdMoment() {
        this.f782m3 = Double.NaN;
        this.nDevSq = Double.NaN;
    }

    ThirdMoment(ThirdMoment original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (this.f779n < 1) {
            this.f778m1 = 0.0d;
            this.f781m2 = 0.0d;
            this.f782m3 = 0.0d;
        }
        double prevM2 = this.f781m2;
        super.increment(d);
        this.nDevSq = this.nDev * this.nDev;
        double n0 = (double) this.f779n;
        this.f782m3 = (this.f782m3 - ((this.nDev * 3.0d) * prevM2)) + ((n0 - 1.0d) * (n0 - 2.0d) * this.nDevSq * this.dev);
    }

    public double getResult() {
        return this.f782m3;
    }

    public void clear() {
        super.clear();
        this.f782m3 = Double.NaN;
        this.nDevSq = Double.NaN;
    }

    public ThirdMoment copy() {
        ThirdMoment result = new ThirdMoment();
        copy(this, result);
        return result;
    }

    public static void copy(ThirdMoment source, ThirdMoment dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        SecondMoment.copy(source, dest);
        dest.f782m3 = source.f782m3;
        dest.nDevSq = source.nDevSq;
    }
}
