package org.apache.commons.math3.stat.descriptive.moment;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class VectorialCovariance implements Serializable {
    private static final long serialVersionUID = 4118372414238930270L;
    private final boolean isBiasCorrected;

    /* renamed from: n */
    private long f783n = 0;
    private final double[] productsSums;
    private final double[] sums;

    public VectorialCovariance(int dimension, boolean isBiasCorrected2) {
        this.sums = new double[dimension];
        this.productsSums = new double[(((dimension + 1) * dimension) / 2)];
        this.isBiasCorrected = isBiasCorrected2;
    }

    public void increment(double[] v) throws DimensionMismatchException {
        if (v.length != this.sums.length) {
            throw new DimensionMismatchException(v.length, this.sums.length);
        }
        int k = 0;
        int i = 0;
        while (i < v.length) {
            double[] dArr = this.sums;
            dArr[i] = dArr[i] + v[i];
            int k2 = k;
            int j = 0;
            while (j <= i) {
                double[] dArr2 = this.productsSums;
                int k3 = k2 + 1;
                dArr2[k2] = dArr2[k2] + (v[i] * v[j]);
                j++;
                k2 = k3;
            }
            i++;
            k = k2;
        }
        this.f783n++;
    }

    public RealMatrix getResult() {
        int dimension = this.sums.length;
        RealMatrix result = MatrixUtils.createRealMatrix(dimension, dimension);
        if (this.f783n > 1) {
            double c = 1.0d / ((double) (this.f783n * (this.isBiasCorrected ? this.f783n - 1 : this.f783n)));
            int k = 0;
            int i = 0;
            while (i < dimension) {
                int k2 = k;
                int j = 0;
                while (j <= i) {
                    int k3 = k2 + 1;
                    double e = ((((double) this.f783n) * this.productsSums[k2]) - (this.sums[i] * this.sums[j])) * c;
                    result.setEntry(i, j, e);
                    result.setEntry(j, i, e);
                    j++;
                    k2 = k3;
                }
                i++;
                k = k2;
            }
        }
        return result;
    }

    public long getN() {
        return this.f783n;
    }

    public void clear() {
        this.f783n = 0;
        Arrays.fill(this.sums, 0.0d);
        Arrays.fill(this.productsSums, 0.0d);
    }

    public int hashCode() {
        return (((((((1 * 31) + (this.isBiasCorrected ? 1231 : 1237)) * 31) + ((int) (this.f783n ^ (this.f783n >>> 32)))) * 31) + Arrays.hashCode(this.productsSums)) * 31) + Arrays.hashCode(this.sums);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VectorialCovariance)) {
            return false;
        }
        VectorialCovariance other = (VectorialCovariance) obj;
        if (this.isBiasCorrected == other.isBiasCorrected && this.f783n == other.f783n && Arrays.equals(this.productsSums, other.productsSums) && Arrays.equals(this.sums, other.sums)) {
            return true;
        }
        return false;
    }
}
