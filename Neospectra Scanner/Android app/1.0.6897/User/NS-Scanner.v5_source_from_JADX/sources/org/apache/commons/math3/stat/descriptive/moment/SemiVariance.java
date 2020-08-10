package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractUnivariateStatistic;
import org.apache.commons.math3.util.MathUtils;

public class SemiVariance extends AbstractUnivariateStatistic implements Serializable {
    public static final Direction DOWNSIDE_VARIANCE = Direction.DOWNSIDE;
    public static final Direction UPSIDE_VARIANCE = Direction.UPSIDE;
    private static final long serialVersionUID = -2653430366886024994L;
    private boolean biasCorrected = true;
    private Direction varianceDirection = Direction.DOWNSIDE;

    public enum Direction {
        UPSIDE(true),
        DOWNSIDE(false);
        
        private boolean direction;

        private Direction(boolean b) {
            this.direction = b;
        }

        /* access modifiers changed from: 0000 */
        public boolean getDirection() {
            return this.direction;
        }
    }

    public SemiVariance() {
    }

    public SemiVariance(boolean biasCorrected2) {
        this.biasCorrected = biasCorrected2;
    }

    public SemiVariance(Direction direction) {
        this.varianceDirection = direction;
    }

    public SemiVariance(boolean corrected, Direction direction) {
        this.biasCorrected = corrected;
        this.varianceDirection = direction;
    }

    public SemiVariance(SemiVariance original) throws NullArgumentException {
        copy(original, this);
    }

    public SemiVariance copy() {
        SemiVariance result = new SemiVariance();
        copy(this, result);
        return result;
    }

    public static void copy(SemiVariance source, SemiVariance dest) throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.biasCorrected = source.biasCorrected;
        dest.varianceDirection = source.varianceDirection;
    }

    public double evaluate(double[] values, int start, int length) throws MathIllegalArgumentException {
        return evaluate(values, new Mean().evaluate(values, start, length), this.varianceDirection, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, Direction direction) throws MathIllegalArgumentException {
        return evaluate(values, new Mean().evaluate(values), direction, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff) throws MathIllegalArgumentException {
        return evaluate(values, cutoff, this.varianceDirection, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff, Direction direction) throws MathIllegalArgumentException {
        return evaluate(values, cutoff, direction, this.biasCorrected, 0, values.length);
    }

    public double evaluate(double[] values, double cutoff, Direction direction, boolean corrected, int start, int length) throws MathIllegalArgumentException {
        double[] dArr = values;
        int i = length;
        int i2 = start;
        test(dArr, i2, i);
        if (dArr.length == 0) {
            return Double.NaN;
        }
        if (dArr.length == 1) {
            return 0.0d;
        }
        boolean booleanDirection = direction.getDirection();
        double sumsq = 0.0d;
        for (int i3 = i2; i3 < i; i3++) {
            if ((dArr[i3] > cutoff) == booleanDirection) {
                double dev = dArr[i3] - cutoff;
                sumsq += dev * dev;
                double d = dev;
            }
        }
        if (corrected) {
            return sumsq / (((double) i) - 1.0d);
        }
        return sumsq / ((double) i);
    }

    public boolean isBiasCorrected() {
        return this.biasCorrected;
    }

    public void setBiasCorrected(boolean biasCorrected2) {
        this.biasCorrected = biasCorrected2;
    }

    public Direction getVarianceDirection() {
        return this.varianceDirection;
    }

    public void setVarianceDirection(Direction varianceDirection2) {
        this.varianceDirection = varianceDirection2;
    }
}
