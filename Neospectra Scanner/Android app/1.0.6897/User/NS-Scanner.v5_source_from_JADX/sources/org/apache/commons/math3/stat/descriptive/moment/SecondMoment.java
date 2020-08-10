package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathUtils;

public class SecondMoment extends FirstMoment implements Serializable {
    private static final long serialVersionUID = 3942403127395076445L;

    /* renamed from: m2 */
    protected double f781m2;

    public /* bridge */ /* synthetic */ long getN() {
        return super.getN();
    }

    public SecondMoment() {
        this.f781m2 = Double.NaN;
    }

    public SecondMoment(SecondMoment original) throws NullArgumentException {
        super(original);
        this.f781m2 = original.f781m2;
    }

    public void increment(double d) {
        if (this.f779n < 1) {
            this.f781m2 = 0.0d;
            this.f778m1 = 0.0d;
        }
        super.increment(d);
        this.f781m2 += (((double) this.f779n) - 1.0d) * this.dev * this.nDev;
    }

    public void clear() {
        super.clear();
        this.f781m2 = Double.NaN;
    }

    public double getResult() {
        return this.f781m2;
    }

    public SecondMoment copy() {
        SecondMoment result = new SecondMoment();
        copy(this, result);
        return result;
    }

    public static void copy(SecondMoment source, SecondMoment dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        FirstMoment.copy(source, dest);
        dest.f781m2 = source.f781m2;
    }
}
