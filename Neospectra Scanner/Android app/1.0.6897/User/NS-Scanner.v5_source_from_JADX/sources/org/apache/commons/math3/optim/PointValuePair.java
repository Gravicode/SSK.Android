package org.apache.commons.math3.optim;

import java.io.Serializable;
import org.apache.commons.math3.util.Pair;

public class PointValuePair extends Pair<double[], Double> implements Serializable {
    private static final long serialVersionUID = 20120513;

    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20120513;
        private final double[] point;
        private final double value;

        DataTransferObject(double[] point2, double value2) {
            this.point = (double[]) point2.clone();
            this.value = value2;
        }

        private Object readResolve() {
            return new PointValuePair(this.point, this.value, false);
        }
    }

    public PointValuePair(double[] point, double value) {
        this(point, value, true);
    }

    public PointValuePair(double[] point, double value, boolean copyArray) {
        double[] dArr = copyArray ? point == null ? null : (double[]) point.clone() : point;
        super(dArr, Double.valueOf(value));
    }

    public double[] getPoint() {
        double[] p = (double[]) getKey();
        if (p == null) {
            return null;
        }
        return (double[]) p.clone();
    }

    public double[] getPointRef() {
        return (double[]) getKey();
    }

    private Object writeReplace() {
        return new DataTransferObject((double[]) getKey(), ((Double) getValue()).doubleValue());
    }
}
