package Jama;

import java.io.Serializable;

public class LUDecomposition implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: LU */
    private double[][] f7LU;

    /* renamed from: m */
    private int f8m;

    /* renamed from: n */
    private int f9n;
    private int[] piv = new int[this.f8m];
    private int pivsign;

    public LUDecomposition(Matrix matrix) {
        this.f7LU = matrix.getArrayCopy();
        this.f8m = matrix.getRowDimension();
        this.f9n = matrix.getColumnDimension();
        for (int i = 0; i < this.f8m; i++) {
            this.piv[i] = i;
        }
        this.pivsign = 1;
        double[] dArr = new double[this.f8m];
        int i2 = 0;
        while (i2 < this.f9n) {
            for (int i3 = 0; i3 < this.f8m; i3++) {
                dArr[i3] = this.f7LU[i3][i2];
            }
            for (int i4 = 0; i4 < this.f8m; i4++) {
                double[] dArr2 = this.f7LU[i4];
                double d = 0.0d;
                for (int i5 = 0; i5 < Math.min(i4, i2); i5++) {
                    d += dArr2[i5] * dArr[i5];
                }
                double d2 = dArr[i4] - d;
                dArr[i4] = d2;
                dArr2[i2] = d2;
            }
            int i6 = i2 + 1;
            int i7 = i2;
            for (int i8 = i6; i8 < this.f8m; i8++) {
                if (Math.abs(dArr[i8]) > Math.abs(dArr[i7])) {
                    i7 = i8;
                }
            }
            if (i7 != i2) {
                for (int i9 = 0; i9 < this.f9n; i9++) {
                    double d3 = this.f7LU[i7][i9];
                    this.f7LU[i7][i9] = this.f7LU[i2][i9];
                    this.f7LU[i2][i9] = d3;
                }
                int i10 = this.piv[i7];
                this.piv[i7] = this.piv[i2];
                this.piv[i2] = i10;
                this.pivsign = -this.pivsign;
            }
            if ((i2 < this.f8m) && (this.f7LU[i2][i2] != 0.0d)) {
                for (int i11 = i6; i11 < this.f8m; i11++) {
                    double[] dArr3 = this.f7LU[i11];
                    dArr3[i2] = dArr3[i2] / this.f7LU[i2][i2];
                }
            }
            i2 = i6;
        }
    }

    public boolean isNonsingular() {
        for (int i = 0; i < this.f9n; i++) {
            if (this.f7LU[i][i] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix getL() {
        Matrix matrix = new Matrix(this.f8m, this.f9n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f8m; i++) {
            for (int i2 = 0; i2 < this.f9n; i2++) {
                if (i > i2) {
                    array[i][i2] = this.f7LU[i][i2];
                } else if (i == i2) {
                    array[i][i2] = 1.0d;
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public Matrix getU() {
        Matrix matrix = new Matrix(this.f9n, this.f9n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f9n; i++) {
            for (int i2 = 0; i2 < this.f9n; i2++) {
                if (i <= i2) {
                    array[i][i2] = this.f7LU[i][i2];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public int[] getPivot() {
        int[] iArr = new int[this.f8m];
        for (int i = 0; i < this.f8m; i++) {
            iArr[i] = this.piv[i];
        }
        return iArr;
    }

    public double[] getDoublePivot() {
        double[] dArr = new double[this.f8m];
        for (int i = 0; i < this.f8m; i++) {
            dArr[i] = (double) this.piv[i];
        }
        return dArr;
    }

    public double det() {
        if (this.f8m != this.f9n) {
            throw new IllegalArgumentException("Matrix must be square.");
        }
        double d = (double) this.pivsign;
        for (int i = 0; i < this.f9n; i++) {
            d *= this.f7LU[i][i];
        }
        return d;
    }

    public Matrix solve(Matrix matrix) {
        if (matrix.getRowDimension() != this.f8m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (!isNonsingular()) {
            throw new RuntimeException("Matrix is singular.");
        } else {
            int columnDimension = matrix.getColumnDimension();
            Matrix matrix2 = matrix.getMatrix(this.piv, 0, columnDimension - 1);
            double[][] array = matrix2.getArray();
            int i = 0;
            while (i < this.f9n) {
                int i2 = i + 1;
                for (int i3 = i2; i3 < this.f9n; i3++) {
                    for (int i4 = 0; i4 < columnDimension; i4++) {
                        double[] dArr = array[i3];
                        dArr[i4] = dArr[i4] - (array[i][i4] * this.f7LU[i3][i]);
                    }
                }
                i = i2;
            }
            for (int i5 = this.f9n - 1; i5 >= 0; i5--) {
                for (int i6 = 0; i6 < columnDimension; i6++) {
                    double[] dArr2 = array[i5];
                    dArr2[i6] = dArr2[i6] / this.f7LU[i5][i5];
                }
                for (int i7 = 0; i7 < i5; i7++) {
                    for (int i8 = 0; i8 < columnDimension; i8++) {
                        double[] dArr3 = array[i7];
                        dArr3[i8] = dArr3[i8] - (array[i5][i8] * this.f7LU[i7][i5]);
                    }
                }
            }
            return matrix2;
        }
    }
}
