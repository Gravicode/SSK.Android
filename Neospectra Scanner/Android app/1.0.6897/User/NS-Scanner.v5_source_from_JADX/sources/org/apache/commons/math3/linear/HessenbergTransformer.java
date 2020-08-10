package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

class HessenbergTransformer {
    private RealMatrix cachedH;
    private RealMatrix cachedP;
    private RealMatrix cachedPt;
    private final double[][] householderVectors;
    private final double[] ort;

    HessenbergTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m = matrix.getRowDimension();
        this.householderVectors = matrix.getData();
        this.ort = new double[m];
        this.cachedP = null;
        this.cachedPt = null;
        this.cachedH = null;
        transform();
    }

    public RealMatrix getP() {
        if (this.cachedP == null) {
            int n = this.householderVectors.length;
            int high = n - 1;
            double[][] pa = (double[][]) Array.newInstance(double.class, new int[]{n, n});
            int i = 0;
            while (i < n) {
                int j = 0;
                while (j < n) {
                    pa[i][j] = i == j ? 1.0d : 0.0d;
                    j++;
                }
                i++;
            }
            for (int m = high - 1; m >= 1; m--) {
                if (this.householderVectors[m][m - 1] != 0.0d) {
                    for (int i2 = m + 1; i2 <= high; i2++) {
                        this.ort[i2] = this.householderVectors[i2][m - 1];
                    }
                    for (int j2 = m; j2 <= high; j2++) {
                        double g = 0.0d;
                        for (int i3 = m; i3 <= high; i3++) {
                            g += this.ort[i3] * pa[i3][j2];
                        }
                        double g2 = (g / this.ort[m]) / this.householderVectors[m][m - 1];
                        for (int i4 = m; i4 <= high; i4++) {
                            double[] dArr = pa[i4];
                            dArr[j2] = dArr[j2] + (this.ort[i4] * g2);
                        }
                    }
                }
            }
            this.cachedP = MatrixUtils.createRealMatrix(pa);
        }
        return this.cachedP;
    }

    public RealMatrix getPT() {
        if (this.cachedPt == null) {
            this.cachedPt = getP().transpose();
        }
        return this.cachedPt;
    }

    public RealMatrix getH() {
        if (this.cachedH == null) {
            int m = this.householderVectors.length;
            double[][] h = (double[][]) Array.newInstance(double.class, new int[]{m, m});
            for (int i = 0; i < m; i++) {
                if (i > 0) {
                    h[i][i - 1] = this.householderVectors[i][i - 1];
                }
                for (int j = i; j < m; j++) {
                    h[i][j] = this.householderVectors[i][j];
                }
            }
            this.cachedH = MatrixUtils.createRealMatrix(h);
        }
        return this.cachedH;
    }

    /* access modifiers changed from: 0000 */
    public double[][] getHouseholderVectorsRef() {
        return this.householderVectors;
    }

    private void transform() {
        int high;
        int n;
        int n2 = this.householderVectors.length;
        int high2 = n2 - 1;
        int m = 1;
        while (m <= high2 - 1) {
            double scale = 0.0d;
            for (int i = m; i <= high2; i++) {
                scale += FastMath.abs(this.householderVectors[i][m - 1]);
            }
            if (!Precision.equals(scale, 0.0d)) {
                double h = 0.0d;
                for (int i2 = high2; i2 >= m; i2--) {
                    this.ort[i2] = this.householderVectors[i2][m - 1] / scale;
                    h += this.ort[i2] * this.ort[i2];
                }
                double g = this.ort[m] > 0.0d ? -FastMath.sqrt(h) : FastMath.sqrt(h);
                double h2 = h - (this.ort[m] * g);
                double[] dArr = this.ort;
                dArr[m] = dArr[m] - g;
                for (int j = m; j < n2; j++) {
                    double f = 0.0d;
                    for (int i3 = high2; i3 >= m; i3--) {
                        f += this.ort[i3] * this.householderVectors[i3][j];
                    }
                    double f2 = f / h2;
                    int i4 = m;
                    while (i4 <= high2) {
                        double[] dArr2 = this.householderVectors[i4];
                        int n3 = n2;
                        dArr2[j] = dArr2[j] - (this.ort[i4] * f2);
                        i4++;
                        n2 = n3;
                    }
                }
                n = n2;
                for (int i5 = 0; i5 <= high2; i5++) {
                    double f3 = 0.0d;
                    for (int j2 = high2; j2 >= m; j2--) {
                        f3 += this.ort[j2] * this.householderVectors[i5][j2];
                    }
                    double f4 = f3 / h2;
                    int j3 = m;
                    while (j3 <= high2) {
                        double[] dArr3 = this.householderVectors[i5];
                        int high3 = high2;
                        dArr3[j3] = dArr3[j3] - (this.ort[j3] * f4);
                        j3++;
                        high2 = high3;
                    }
                }
                high = high2;
                this.ort[m] = this.ort[m] * scale;
                this.householderVectors[m][m - 1] = scale * g;
            } else {
                n = n2;
                high = high2;
            }
            m++;
            n2 = n;
            high2 = high;
        }
        int i6 = high2;
    }
}
