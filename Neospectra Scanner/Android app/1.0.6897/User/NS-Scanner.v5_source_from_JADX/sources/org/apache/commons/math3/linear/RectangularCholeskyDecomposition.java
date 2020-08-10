package org.apache.commons.math3.linear;

import java.lang.reflect.Array;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecomposition {
    private int rank;
    private final RealMatrix root;

    public RectangularCholeskyDecomposition(RealMatrix matrix) throws NonPositiveDefiniteMatrixException {
        this(matrix, 0.0d);
    }

    public RectangularCholeskyDecomposition(RealMatrix matrix, double small) throws NonPositiveDefiniteMatrixException {
        boolean z;
        double d = small;
        int order = matrix.getRowDimension();
        double[][] c = matrix.getData();
        double[][] b = (double[][]) Array.newInstance(double.class, new int[]{order, order});
        int[] index = new int[order];
        for (int i = 0; i < order; i++) {
            index[i] = i;
        }
        int r = 0;
        boolean loop = true;
        while (loop) {
            int swapR = r + 1;
            int swapR2 = r;
            while (true) {
                int i2 = swapR;
                if (i2 >= order) {
                    break;
                }
                int ii = index[i2];
                int isr = index[swapR2];
                if (c[ii][ii] > c[isr][isr]) {
                    swapR2 = i2;
                }
                swapR = i2 + 1;
            }
            if (swapR2 != r) {
                int tmpIndex = index[r];
                index[r] = index[swapR2];
                index[swapR2] = tmpIndex;
                double[] tmpRow = b[r];
                b[r] = b[swapR2];
                b[swapR2] = tmpRow;
            }
            int ir = index[r];
            if (c[ir][ir] <= d) {
                if (r != 0) {
                    int i3 = r;
                    while (true) {
                        int i4 = i3;
                        if (i4 >= order) {
                            z = false;
                            break;
                        } else if (c[index[i4]][index[i4]] < (-d)) {
                            int i5 = i4;
                            NonPositiveDefiniteMatrixException nonPositiveDefiniteMatrixException = new NonPositiveDefiniteMatrixException(c[index[i4]][index[i4]], i4, d);
                            throw nonPositiveDefiniteMatrixException;
                        } else {
                            i3 = i4 + 1;
                        }
                    }
                } else {
                    NonPositiveDefiniteMatrixException nonPositiveDefiniteMatrixException2 = new NonPositiveDefiniteMatrixException(c[ir][ir], ir, d);
                    throw nonPositiveDefiniteMatrixException2;
                }
            } else {
                double sqrt = FastMath.sqrt(c[ir][ir]);
                b[r][r] = sqrt;
                double inverse = 1.0d / sqrt;
                double inverse2 = 1.0d / c[ir][ir];
                for (int i6 = r + 1; i6 < order; i6++) {
                    int ii2 = index[i6];
                    double e = c[ii2][ir] * inverse;
                    b[i6][r] = e;
                    double[] dArr = c[ii2];
                    dArr[ii2] = dArr[ii2] - ((c[ii2][ir] * c[ii2][ir]) * inverse2);
                    int ij = r + 1;
                    while (true) {
                        int j = ij;
                        if (j >= i6) {
                            break;
                        }
                        int ij2 = index[j];
                        double f = c[ii2][ij2] - (b[j][r] * e);
                        c[ii2][ij2] = f;
                        c[ij2][ii2] = f;
                        ij = j + 1;
                    }
                }
                r++;
                z = r < order;
            }
            loop = z;
        }
        this.rank = r;
        this.root = MatrixUtils.createRealMatrix(order, r);
        for (int i7 = 0; i7 < order; i7++) {
            for (int j2 = 0; j2 < r; j2++) {
                this.root.setEntry(index[i7], j2, b[i7][j2]);
            }
        }
    }

    public RealMatrix getRootMatrix() {
        return this.root;
    }

    public int getRank() {
        return this.rank;
    }
}
