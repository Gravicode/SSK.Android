package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.math3.util.FastMath;

class TriDiagonalTransformer {
    private RealMatrix cachedQ;
    private RealMatrix cachedQt;
    private RealMatrix cachedT;
    private final double[][] householderVectors;
    private final double[] main;
    private final double[] secondary;

    TriDiagonalTransformer(RealMatrix matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m = matrix.getRowDimension();
        this.householderVectors = matrix.getData();
        this.main = new double[m];
        this.secondary = new double[(m - 1)];
        this.cachedQ = null;
        this.cachedQt = null;
        this.cachedT = null;
        transform();
    }

    public RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    public RealMatrix getQT() {
        if (this.cachedQt == null) {
            int m = this.householderVectors.length;
            double[][] qta = (double[][]) Array.newInstance(double.class, new int[]{m, m});
            for (int k = m - 1; k >= 1; k--) {
                double[] hK = this.householderVectors[k - 1];
                qta[k][k] = 1.0d;
                if (hK[k] != 0.0d) {
                    double inv = 1.0d / (this.secondary[k - 1] * hK[k]);
                    double beta = 1.0d / this.secondary[k - 1];
                    qta[k][k] = (hK[k] * beta) + 1.0d;
                    for (int i = k + 1; i < m; i++) {
                        qta[k][i] = hK[i] * beta;
                    }
                    for (int j = k + 1; j < m; j++) {
                        double beta2 = 0.0d;
                        for (int i2 = k + 1; i2 < m; i2++) {
                            beta2 += qta[j][i2] * hK[i2];
                        }
                        double beta3 = beta2 * inv;
                        qta[j][k] = hK[k] * beta3;
                        for (int i3 = k + 1; i3 < m; i3++) {
                            double[] dArr = qta[j];
                            dArr[i3] = dArr[i3] + (hK[i3] * beta3);
                        }
                    }
                }
            }
            qta[0][0] = 1.0d;
            this.cachedQt = MatrixUtils.createRealMatrix(qta);
        }
        return this.cachedQt;
    }

    public RealMatrix getT() {
        if (this.cachedT == null) {
            int m = this.main.length;
            double[][] ta = (double[][]) Array.newInstance(double.class, new int[]{m, m});
            for (int i = 0; i < m; i++) {
                ta[i][i] = this.main[i];
                if (i > 0) {
                    ta[i][i - 1] = this.secondary[i - 1];
                }
                if (i < this.main.length - 1) {
                    ta[i][i + 1] = this.secondary[i];
                }
            }
            this.cachedT = MatrixUtils.createRealMatrix(ta);
        }
        return this.cachedT;
    }

    /* access modifiers changed from: 0000 */
    public double[][] getHouseholderVectorsRef() {
        return this.householderVectors;
    }

    /* access modifiers changed from: 0000 */
    public double[] getMainDiagonalRef() {
        return this.main;
    }

    /* access modifiers changed from: 0000 */
    public double[] getSecondaryDiagonalRef() {
        return this.secondary;
    }

    private void transform() {
        double xNormSqr;
        int m = this.householderVectors.length;
        double[] z = new double[m];
        for (int k = 0; k < m - 1; k++) {
            double[] hK = this.householderVectors[k];
            this.main[k] = hK[k];
            double xNormSqr2 = 0.0d;
            for (int j = k + 1; j < m; j++) {
                double c = hK[j];
                xNormSqr2 += c * c;
            }
            double a = hK[k + 1] > 0.0d ? -FastMath.sqrt(xNormSqr2) : FastMath.sqrt(xNormSqr2);
            this.secondary[k] = a;
            if (a != 0.0d) {
                int i = k + 1;
                hK[i] = hK[i] - a;
                double beta = -1.0d / (hK[k + 1] * a);
                Arrays.fill(z, k + 1, m, 0.0d);
                int i2 = k + 1;
                while (i2 < m) {
                    double[] hI = this.householderVectors[i2];
                    double hKI = hK[i2];
                    double zI = hI[i2] * hKI;
                    int j2 = i2 + 1;
                    while (true) {
                        xNormSqr = xNormSqr2;
                        int j3 = j2;
                        if (j3 >= m) {
                            break;
                        }
                        double hIJ = hI[j3];
                        zI += hK[j3] * hIJ;
                        z[j3] = z[j3] + (hIJ * hKI);
                        j2 = j3 + 1;
                        xNormSqr2 = xNormSqr;
                    }
                    z[i2] = (z[i2] + zI) * beta;
                    i2++;
                    xNormSqr2 = xNormSqr;
                }
                double gamma = 0.0d;
                for (int i3 = k + 1; i3 < m; i3++) {
                    gamma += z[i3] * hK[i3];
                }
                double gamma2 = gamma * (beta / 2.0d);
                for (int i4 = k + 1; i4 < m; i4++) {
                    z[i4] = z[i4] - (hK[i4] * gamma2);
                }
                for (int i5 = k + 1; i5 < m; i5++) {
                    double[] hI2 = this.householderVectors[i5];
                    for (int j4 = i5; j4 < m; j4++) {
                        hI2[j4] = hI2[j4] - ((hK[i5] * z[j4]) + (z[i5] * hK[j4]));
                    }
                }
            }
        }
        this.main[m - 1] = this.householderVectors[m - 1][m - 1];
    }
}
