package org.apache.commons.math3.optimization;

import java.io.Serializable;
import org.apache.commons.math3.util.Pair;

@Deprecated
public class PointVectorValuePair extends Pair<double[], double[]> implements Serializable {
    private static final long serialVersionUID = 20120513;

    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20120513;
        private final double[] point;
        private final double[] value;

        DataTransferObject(double[] point2, double[] value2) {
            this.point = (double[]) point2.clone();
            this.value = (double[]) value2.clone();
        }

        private Object readResolve() {
            return new PointVectorValuePair(this.point, this.value, false);
        }
    }

    public PointVectorValuePair(double[] point, double[] value) {
        this(point, value, true);
    }

    public PointVectorValuePair(double[] point, double[] value, boolean copyArray) {
        double[] dArr = null;
        double[] dArr2 = copyArray ? point == null ? null : (double[]) point.clone() : point;
        if (!copyArray) {
            dArr = value;
        } else if (value != null) {
            dArr = (double[]) value.clone();
        }
        super(dArr2, dArr);
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

    public double[] getValue() {
        double[] v = (double[]) super.getValue();
        if (v == null) {
            return null;
        }
        return (double[]) v.clone();
    }

    public double[] getValueRef() {
        return (double[]) super.getValue();
    }

    private Object writeReplace() {
        return new DataTransferObject((double[]) getKey(), getValue());
    }
}
