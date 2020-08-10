package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;

public class QRDecomposition {
    private RealMatrix cachedH;
    private RealMatrix cachedQ;
    private RealMatrix cachedQT;
    private RealMatrix cachedR;
    private double[][] qrt;
    private double[] rDiag;
    private final double threshold;

    private static class Solver implements DecompositionSolver {
        private final double[][] qrt;
        private final double[] rDiag;
        private final double threshold;

        private Solver(double[][] qrt2, double[] rDiag2, double threshold2) {
            this.qrt = qrt2;
            this.rDiag = rDiag2;
            this.threshold = threshold2;
        }

        public boolean isNonSingular() {
            for (double diag : this.rDiag) {
                if (FastMath.abs(diag) <= this.threshold) {
                    return false;
                }
            }
            return true;
        }

        public RealVector solve(RealVector b) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            if (b.getDimension() != m) {
                throw new DimensionMismatchException(b.getDimension(), m);
            } else if (!isNonSingular()) {
                throw new SingularMatrixException();
            } else {
                double[] x = new double[n];
                double[] y = b.toArray();
                for (int minor = 0; minor < FastMath.min(m, n); minor++) {
                    double[] qrtMinor = this.qrt[minor];
                    double dotProduct = 0.0d;
                    for (int row = minor; row < m; row++) {
                        dotProduct += y[row] * qrtMinor[row];
                    }
                    double dotProduct2 = dotProduct / (this.rDiag[minor] * qrtMinor[minor]);
                    for (int row2 = minor; row2 < m; row2++) {
                        y[row2] = y[row2] + (qrtMinor[row2] * dotProduct2);
                    }
                }
                for (int row3 = this.rDiag.length - 1; row3 >= 0; row3--) {
                    y[row3] = y[row3] / this.rDiag[row3];
                    double yRow = y[row3];
                    double[] qrtRow = this.qrt[row3];
                    x[row3] = yRow;
                    for (int i = 0; i < row3; i++) {
                        y[i] = y[i] - (qrtRow[i] * yRow);
                    }
                }
                return new ArrayRealVector(x, false);
            }
        }

        public RealMatrix solve(RealMatrix b) {
            double d;
            int m;
            int n;
            Solver solver = this;
            int k = solver.qrt.length;
            int k2 = solver.qrt[0].length;
            if (b.getRowDimension() != k2) {
                throw new DimensionMismatchException(b.getRowDimension(), k2);
            } else if (!isNonSingular()) {
                throw new SingularMatrixException();
            } else {
                int columns = b.getColumnDimension();
                char c = '4';
                int cBlocks = ((columns + 52) - 1) / 52;
                double[][] xBlocks = BlockRealMatrix.createBlocksLayout(k, columns);
                double[][] y = (double[][]) Array.newInstance(double.class, new int[]{b.getRowDimension(), 52});
                double[] alpha = new double[52];
                int kBlock = 0;
                while (true) {
                    int kBlock2 = kBlock;
                    if (kBlock2 < cBlocks) {
                        int kStart = kBlock2 * 52;
                        int kEnd = FastMath.min(kStart + 52, columns);
                        int kWidth = kEnd - kStart;
                        int kBlock3 = kBlock2;
                        b.copySubMatrix(0, k2 - 1, kStart, kEnd - 1, y);
                        int minor = 0;
                        while (true) {
                            d = 1.0d;
                            if (minor >= FastMath.min(k2, k)) {
                                break;
                            }
                            double[] qrtMinor = solver.qrt[minor];
                            double factor = 1.0d / (solver.rDiag[minor] * qrtMinor[minor]);
                            char c2 = c;
                            Arrays.fill(alpha, 0, kWidth, 0.0d);
                            int row = minor;
                            while (row < k2) {
                                double d2 = qrtMinor[row];
                                double[] yRow = y[row];
                                int k3 = 0;
                                while (true) {
                                    n = k;
                                    int n2 = k3;
                                    if (n2 >= kWidth) {
                                        break;
                                    }
                                    alpha[n2] = alpha[n2] + (yRow[n2] * d2);
                                    k3 = n2 + 1;
                                    k = n;
                                }
                                row++;
                                k = n;
                            }
                            int n3 = k;
                            for (int k4 = 0; k4 < kWidth; k4++) {
                                alpha[k4] = alpha[k4] * factor;
                            }
                            int row2 = minor;
                            while (row2 < k2) {
                                double d3 = qrtMinor[row2];
                                double[] yRow2 = y[row2];
                                int k5 = 0;
                                while (true) {
                                    m = k2;
                                    int k6 = k5;
                                    if (k6 >= kWidth) {
                                        break;
                                    }
                                    yRow2[k6] = yRow2[k6] + (alpha[k6] * d3);
                                    k5 = k6 + 1;
                                    k2 = m;
                                }
                                row2++;
                                k2 = m;
                            }
                            int m2 = k2;
                            minor++;
                            c = c2;
                            k = n3;
                        }
                        int n4 = k;
                        int m3 = k2;
                        char c3 = c;
                        int j = solver.rDiag.length - 1;
                        while (j >= 0) {
                            int jBlock = j / 52;
                            int jStart = jBlock * 52;
                            double factor2 = d / solver.rDiag[j];
                            double[] yJ = y[j];
                            double[] xBlock = xBlocks[(jBlock * cBlocks) + kBlock3];
                            int index = (j - jStart) * kWidth;
                            int index2 = 0;
                            while (true) {
                                int k7 = index2;
                                if (k7 >= kWidth) {
                                    break;
                                }
                                yJ[k7] = yJ[k7] * factor2;
                                int index3 = index + 1;
                                xBlock[index] = yJ[k7];
                                index2 = k7 + 1;
                                index = index3;
                            }
                            double[] qrtJ = solver.qrt[j];
                            int i = 0;
                            while (i < j) {
                                double rIJ = qrtJ[i];
                                double[] yI = y[i];
                                int k8 = 0;
                                while (true) {
                                    int k9 = k8;
                                    if (k9 >= kWidth) {
                                        break;
                                    }
                                    yI[k9] = yI[k9] - (yJ[k9] * rIJ);
                                    k8 = k9 + 1;
                                }
                                i++;
                            }
                            j--;
                            solver = this;
                            d = 1.0d;
                        }
                        kBlock = kBlock3 + 1;
                        c = c3;
                        k = n4;
                        k2 = m3;
                        solver = this;
                    } else {
                        int n5 = k;
                        int i2 = k2;
                        char c4 = c;
                        return new BlockRealMatrix(k, columns, xBlocks, false);
                    }
                }
            }
        }

        public RealMatrix getInverse() {
            return solve(MatrixUtils.createRealIdentityMatrix(this.qrt[0].length));
        }
    }

    public QRDecomposition(RealMatrix matrix) {
        this(matrix, 0.0d);
    }

    public QRDecomposition(RealMatrix matrix, double threshold2) {
        this.threshold = threshold2;
        int m = matrix.getRowDimension();
        int n = matrix.getColumnDimension();
        this.qrt = matrix.transpose().getData();
        this.rDiag = new double[FastMath.min(m, n)];
        this.cachedQ = null;
        this.cachedQT = null;
        this.cachedR = null;
        this.cachedH = null;
        decompose(this.qrt);
    }

    /* access modifiers changed from: protected */
    public void decompose(double[][] matrix) {
        for (int minor = 0; minor < FastMath.min(matrix.length, matrix[0].length); minor++) {
            performHouseholderReflection(minor, matrix);
        }
    }

    /* access modifiers changed from: protected */
    public void performHouseholderReflection(int minor, double[][] matrix) {
        double[][] dArr = matrix;
        double[] qrtMinor = dArr[minor];
        double xNormSqr = 0.0d;
        for (int row = minor; row < qrtMinor.length; row++) {
            double c = qrtMinor[row];
            xNormSqr += c * c;
        }
        double a = qrtMinor[minor] > 0.0d ? -FastMath.sqrt(xNormSqr) : FastMath.sqrt(xNormSqr);
        this.rDiag[minor] = a;
        if (a != 0.0d) {
            qrtMinor[minor] = qrtMinor[minor] - a;
            for (int col = minor + 1; col < dArr.length; col++) {
                double[] qrtCol = dArr[col];
                double alpha = 0.0d;
                for (int row2 = minor; row2 < qrtCol.length; row2++) {
                    alpha -= qrtCol[row2] * qrtMinor[row2];
                }
                double alpha2 = alpha / (qrtMinor[minor] * a);
                for (int row3 = minor; row3 < qrtCol.length; row3++) {
                    qrtCol[row3] = qrtCol[row3] - (qrtMinor[row3] * alpha2);
                }
            }
        }
    }

    public RealMatrix getR() {
        if (this.cachedR == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            double[][] ra = (double[][]) Array.newInstance(double.class, new int[]{m, n});
            for (int row = FastMath.min(m, n) - 1; row >= 0; row--) {
                ra[row][row] = this.rDiag[row];
                for (int col = row + 1; col < n; col++) {
                    ra[row][col] = this.qrt[col][row];
                }
            }
            this.cachedR = MatrixUtils.createRealMatrix(ra);
        }
        return this.cachedR;
    }

    public RealMatrix getQ() {
        if (this.cachedQ == null) {
            this.cachedQ = getQT().transpose();
        }
        return this.cachedQ;
    }

    public RealMatrix getQT() {
        double d;
        if (this.cachedQT == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            double[][] qta = (double[][]) Array.newInstance(double.class, new int[]{m, m});
            int minor = m - 1;
            while (true) {
                d = 1.0d;
                if (minor < FastMath.min(m, n)) {
                    break;
                }
                qta[minor][minor] = 1.0d;
                minor--;
            }
            int minor2 = FastMath.min(m, n) - 1;
            while (minor2 >= 0) {
                double[] qrtMinor = this.qrt[minor2];
                qta[minor2][minor2] = d;
                if (qrtMinor[minor2] != 0.0d) {
                    for (int col = minor2; col < m; col++) {
                        double alpha = 0.0d;
                        for (int row = minor2; row < m; row++) {
                            alpha -= qta[col][row] * qrtMinor[row];
                        }
                        double alpha2 = alpha / (this.rDiag[minor2] * qrtMinor[minor2]);
                        for (int row2 = minor2; row2 < m; row2++) {
                            double[] dArr = qta[col];
                            dArr[row2] = dArr[row2] + ((-alpha2) * qrtMinor[row2]);
                        }
                    }
                }
                minor2--;
                d = 1.0d;
            }
            this.cachedQT = MatrixUtils.createRealMatrix(qta);
        }
        return this.cachedQT;
    }

    public RealMatrix getH() {
        if (this.cachedH == null) {
            int n = this.qrt.length;
            int m = this.qrt[0].length;
            double[][] ha = (double[][]) Array.newInstance(double.class, new int[]{m, n});
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < FastMath.min(i + 1, n); j++) {
                    ha[i][j] = this.qrt[j][i] / (-this.rDiag[j]);
                }
            }
            this.cachedH = MatrixUtils.createRealMatrix(ha);
        }
        return this.cachedH;
    }

    public DecompositionSolver getSolver() {
        Solver solver = new Solver(this.qrt, this.rDiag, this.threshold);
        return solver;
    }
}
