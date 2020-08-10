package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

class FourthMoment extends ThirdMoment implements Serializable {
    private static final long serialVersionUID = 4763990447117157611L;

    /* renamed from: m4 */
    private double f780m4;

    FourthMoment() {
        this.f780m4 = Double.NaN;
    }

    FourthMoment(FourthMoment original) throws NullArgumentException {
        copy(original, this);
    }

    public void increment(double d) {
        if (this.f779n < 1) {
            this.f780m4 = 0.0d;
            this.f782m3 = 0.0d;
            this.f781m2 = 0.0d;
            this.f778m1 = 0.0d;
        }
        double prevM3 = this.f782m3;
        double prevM2 = this.f781m2;
        super.increment(d);
        double n0 = (double) this.f779n;
        double d2 = prevM3;
        this.f780m4 = (this.f780m4 - ((this.nDev * 4.0d) * prevM3)) + (this.nDevSq * 6.0d * prevM2) + (((n0 * n0) - ((n0 - 1.0d) * 3.0d)) * this.nDevSq * this.nDevSq * (n0 - 1.0d) * n0);
    }

    public double getResult() {
        return this.f780m4;
    }

    public void clear() {
        super.clear();
        this.f780m4 = Double.NaN;
    }

    public FourthMoment copy() {
        FourthMoment result = new FourthMoment();
        copy(this, result);
        return result;
    }

    public static void copy(FourthMoment source, FourthMoment dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        ThirdMoment.copy(source, dest);
        dest.f780m4 = source.f780m4;
    }
}
