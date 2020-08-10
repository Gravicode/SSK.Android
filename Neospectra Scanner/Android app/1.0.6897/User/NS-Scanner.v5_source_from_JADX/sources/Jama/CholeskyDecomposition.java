package Jama;

import java.io.Serializable;
import java.lang.reflect.Array;

public class CholeskyDecomposition implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: L */
    private double[][] f0L = ((double[][]) Array.newInstance(double.class, new int[]{this.f1n, this.f1n}));
    private boolean isspd;

    /* renamed from: n */
    private int f1n;

    public CholeskyDecomposition(Matrix matrix) {
        double[][] array = matrix.getArray();
        this.f1n = matrix.getRowDimension();
        this.isspd = matrix.getColumnDimension() == this.f1n;
        int i = 0;
        while (i < this.f1n) {
            double[] dArr = this.f0L[i];
            double d = 0.0d;
            for (int i2 = 0; i2 < i; i2++) {
                double[] dArr2 = this.f0L[i2];
                double d2 = 0.0d;
                for (int i3 = 0; i3 < i2; i3++) {
                    d2 += dArr2[i3] * dArr[i3];
                }
                double d3 = (array[i][i2] - d2) / this.f0L[i2][i2];
                dArr[i2] = d3;
                d += d3 * d3;
                this.isspd &= array[i2][i] == array[i][i2];
            }
            double d4 = array[i][i] - d;
            this.isspd &= d4 > 0.0d;
            this.f0L[i][i] = Math.sqrt(Math.max(d4, 0.0d));
            int i4 = i + 1;
            for (int i5 = i4; i5 < this.f1n; i5++) {
                this.f0L[i][i5] = 0.0d;
            }
            i = i4;
        }
    }

    public boolean isSPD() {
        return this.isspd;
    }

    public Matrix getL() {
        return new Matrix(this.f0L, this.f1n, this.f1n);
    }

    public Matrix solve(Matrix matrix) {
        if (matrix.getRowDimension() != this.f1n) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (!this.isspd) {
            throw new RuntimeException("Matrix is not symmetric positive definite.");
        } else {
            double[][] arrayCopy = matrix.getArrayCopy();
            int columnDimension = matrix.getColumnDimension();
            for (int i = 0; i < this.f1n; i++) {
                for (int i2 = 0; i2 < columnDimension; i2++) {
                    for (int i3 = 0; i3 < i; i3++) {
                        double[] dArr = arrayCopy[i];
                        dArr[i2] = dArr[i2] - (arrayCopy[i3][i2] * this.f0L[i][i3]);
                    }
                    double[] dArr2 = arrayCopy[i];
                    dArr2[i2] = dArr2[i2] / this.f0L[i][i];
                }
            }
            for (int i4 = this.f1n - 1; i4 >= 0; i4--) {
                for (int i5 = 0; i5 < columnDimension; i5++) {
                    for (int i6 = i4 + 1; i6 < this.f1n; i6++) {
                        double[] dArr3 = arrayCopy[i4];
                        dArr3[i5] = dArr3[i5] - (arrayCopy[i6][i5] * this.f0L[i6][i4]);
                    }
                    double[] dArr4 = arrayCopy[i4];
                    dArr4[i5] = dArr4[i5] / this.f0L[i4][i4];
                }
            }
            return new Matrix(arrayCopy, this.f1n, columnDimension);
        }
    }
}
