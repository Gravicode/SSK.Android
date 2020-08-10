package Jama;

import Jama.util.Maths;
import java.io.Serializable;

public class QRDecomposition implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: QR */
    private double[][] f13QR;
    private double[] Rdiag = new double[this.f15n];

    /* renamed from: m */
    private int f14m;

    /* renamed from: n */
    private int f15n;

    public QRDecomposition(Matrix matrix) {
        this.f13QR = matrix.getArrayCopy();
        this.f14m = matrix.getRowDimension();
        this.f15n = matrix.getColumnDimension();
        for (int i = 0; i < this.f15n; i++) {
            double d = 0.0d;
            for (int i2 = i; i2 < this.f14m; i2++) {
                d = Maths.hypot(d, this.f13QR[i2][i]);
            }
            if (d != 0.0d) {
                if (this.f13QR[i][i] < 0.0d) {
                    d = -d;
                }
                for (int i3 = i; i3 < this.f14m; i3++) {
                    double[] dArr = this.f13QR[i3];
                    dArr[i] = dArr[i] / d;
                }
                double[] dArr2 = this.f13QR[i];
                dArr2[i] = dArr2[i] + 1.0d;
                for (int i4 = i + 1; i4 < this.f15n; i4++) {
                    double d2 = 0.0d;
                    for (int i5 = i; i5 < this.f14m; i5++) {
                        d2 += this.f13QR[i5][i] * this.f13QR[i5][i4];
                    }
                    double d3 = (-d2) / this.f13QR[i][i];
                    for (int i6 = i; i6 < this.f14m; i6++) {
                        double[] dArr3 = this.f13QR[i6];
                        dArr3[i4] = dArr3[i4] + (this.f13QR[i6][i] * d3);
                    }
                }
            }
            this.Rdiag[i] = -d;
        }
    }

    public boolean isFullRank() {
        for (int i = 0; i < this.f15n; i++) {
            if (this.Rdiag[i] == 0.0d) {
                return false;
            }
        }
        return true;
    }

    public Matrix getH() {
        Matrix matrix = new Matrix(this.f14m, this.f15n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f14m; i++) {
            for (int i2 = 0; i2 < this.f15n; i2++) {
                if (i >= i2) {
                    array[i][i2] = this.f13QR[i][i2];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public Matrix getR() {
        Matrix matrix = new Matrix(this.f15n, this.f15n);
        double[][] array = matrix.getArray();
        for (int i = 0; i < this.f15n; i++) {
            for (int i2 = 0; i2 < this.f15n; i2++) {
                if (i < i2) {
                    array[i][i2] = this.f13QR[i][i2];
                } else if (i == i2) {
                    array[i][i2] = this.Rdiag[i];
                } else {
                    array[i][i2] = 0.0d;
                }
            }
        }
        return matrix;
    }

    public Matrix getQ() {
        Matrix matrix = new Matrix(this.f14m, this.f15n);
        double[][] array = matrix.getArray();
        for (int i = this.f15n - 1; i >= 0; i--) {
            for (int i2 = 0; i2 < this.f14m; i2++) {
                array[i2][i] = 0.0d;
            }
            array[i][i] = 1.0d;
            for (int i3 = i; i3 < this.f15n; i3++) {
                if (this.f13QR[i][i] != 0.0d) {
                    double d = 0.0d;
                    for (int i4 = i; i4 < this.f14m; i4++) {
                        d += this.f13QR[i4][i] * array[i4][i3];
                    }
                    double d2 = (-d) / this.f13QR[i][i];
                    for (int i5 = i; i5 < this.f14m; i5++) {
                        double[] dArr = array[i5];
                        dArr[i3] = dArr[i3] + (this.f13QR[i5][i] * d2);
                    }
                }
            }
        }
        return matrix;
    }

    public Matrix solve(Matrix matrix) {
        if (matrix.getRowDimension() != this.f14m) {
            throw new IllegalArgumentException("Matrix row dimensions must agree.");
        } else if (!isFullRank()) {
            throw new RuntimeException("Matrix is rank deficient.");
        } else {
            int columnDimension = matrix.getColumnDimension();
            double[][] arrayCopy = matrix.getArrayCopy();
            for (int i = 0; i < this.f15n; i++) {
                for (int i2 = 0; i2 < columnDimension; i2++) {
                    double d = 0.0d;
                    for (int i3 = i; i3 < this.f14m; i3++) {
                        d += this.f13QR[i3][i] * arrayCopy[i3][i2];
                    }
                    double d2 = (-d) / this.f13QR[i][i];
                    for (int i4 = i; i4 < this.f14m; i4++) {
                        double[] dArr = arrayCopy[i4];
                        dArr[i2] = dArr[i2] + (this.f13QR[i4][i] * d2);
                    }
                }
            }
            for (int i5 = this.f15n - 1; i5 >= 0; i5--) {
                for (int i6 = 0; i6 < columnDimension; i6++) {
                    double[] dArr2 = arrayCopy[i5];
                    dArr2[i6] = dArr2[i6] / this.Rdiag[i5];
                }
                for (int i7 = 0; i7 < i5; i7++) {
                    for (int i8 = 0; i8 < columnDimension; i8++) {
                        double[] dArr3 = arrayCopy[i7];
                        dArr3[i8] = dArr3[i8] - (arrayCopy[i5][i8] * this.f13QR[i7][i5]);
                    }
                }
            }
            return new Matrix(arrayCopy, this.f15n, columnDimension).getMatrix(0, this.f15n - 1, 0, columnDimension - 1);
        }
    }
}
