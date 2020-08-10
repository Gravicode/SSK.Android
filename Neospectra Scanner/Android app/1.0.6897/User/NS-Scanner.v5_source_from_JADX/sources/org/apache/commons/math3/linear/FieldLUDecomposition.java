package org.apache.commons.math3.linear;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.MathArrays;

public class FieldLUDecomposition<T extends FieldElement<T>> {
    private FieldMatrix<T> cachedL;
    private FieldMatrix<T> cachedP;
    private FieldMatrix<T> cachedU;
    private boolean even;
    private final Field<T> field;

    /* renamed from: lu */
    private T[][] f610lu;
    private int[] pivot;
    private boolean singular;

    private static class Solver<T extends FieldElement<T>> implements FieldDecompositionSolver<T> {
        private final Field<T> field;

        /* renamed from: lu */
        private final T[][] f611lu;
        private final int[] pivot;
        private final boolean singular;

        private Solver(Field<T> field2, T[][] lu, int[] pivot2, boolean singular2) {
            this.field = field2;
            this.f611lu = lu;
            this.pivot = pivot2;
            this.singular = singular2;
        }

        public boolean isNonSingular() {
            return !this.singular;
        }

        public FieldVector<T> solve(FieldVector<T> b) {
            try {
                return solve((ArrayFieldVector) b);
            } catch (ClassCastException e) {
                int m = this.pivot.length;
                if (b.getDimension() != m) {
                    throw new DimensionMismatchException(b.getDimension(), m);
                } else if (this.singular) {
                    throw new SingularMatrixException();
                } else {
                    T[] bp = (FieldElement[]) MathArrays.buildArray(this.field, m);
                    for (int row = 0; row < m; row++) {
                        bp[row] = b.getEntry(this.pivot[row]);
                    }
                    for (int col = 0; col < m; col++) {
                        T bpCol = bp[col];
                        for (int i = col + 1; i < m; i++) {
                            bp[i] = (FieldElement) bp[i].subtract(bpCol.multiply(this.f611lu[i][col]));
                        }
                    }
                    for (int col2 = m - 1; col2 >= 0; col2--) {
                        bp[col2] = (FieldElement) bp[col2].divide(this.f611lu[col2][col2]);
                        T bpCol2 = bp[col2];
                        for (int i2 = 0; i2 < col2; i2++) {
                            bp[i2] = (FieldElement) bp[i2].subtract(bpCol2.multiply(this.f611lu[i2][col2]));
                        }
                    }
                    return new ArrayFieldVector(this.field, bp, false);
                }
            }
        }

        public ArrayFieldVector<T> solve(ArrayFieldVector<T> b) {
            int m = this.pivot.length;
            int length = b.getDimension();
            if (length != m) {
                throw new DimensionMismatchException(length, m);
            } else if (this.singular) {
                throw new SingularMatrixException();
            } else {
                T[] bp = (FieldElement[]) MathArrays.buildArray(this.field, m);
                for (int row = 0; row < m; row++) {
                    bp[row] = b.getEntry(this.pivot[row]);
                }
                for (int col = 0; col < m; col++) {
                    T bpCol = bp[col];
                    for (int i = col + 1; i < m; i++) {
                        bp[i] = (FieldElement) bp[i].subtract(bpCol.multiply(this.f611lu[i][col]));
                    }
                }
                for (int col2 = m - 1; col2 >= 0; col2--) {
                    bp[col2] = (FieldElement) bp[col2].divide(this.f611lu[col2][col2]);
                    T bpCol2 = bp[col2];
                    for (int i2 = 0; i2 < col2; i2++) {
                        bp[i2] = (FieldElement) bp[i2].subtract(bpCol2.multiply(this.f611lu[i2][col2]));
                    }
                }
                return new ArrayFieldVector<>(bp, false);
            }
        }

        public FieldMatrix<T> solve(FieldMatrix<T> b) {
            int m = this.pivot.length;
            if (b.getRowDimension() != m) {
                throw new DimensionMismatchException(b.getRowDimension(), m);
            } else if (this.singular) {
                throw new SingularMatrixException();
            } else {
                int nColB = b.getColumnDimension();
                T[][] bp = (FieldElement[][]) MathArrays.buildArray(this.field, m, nColB);
                for (int row = 0; row < m; row++) {
                    T[] bpRow = bp[row];
                    int pRow = this.pivot[row];
                    for (int col = 0; col < nColB; col++) {
                        bpRow[col] = b.getEntry(pRow, col);
                    }
                }
                for (int col2 = 0; col2 < m; col2++) {
                    T[] bpCol = bp[col2];
                    for (int i = col2 + 1; i < m; i++) {
                        T[] bpI = bp[i];
                        T luICol = this.f611lu[i][col2];
                        for (int j = 0; j < nColB; j++) {
                            bpI[j] = (FieldElement) bpI[j].subtract(bpCol[j].multiply(luICol));
                        }
                    }
                }
                for (int col3 = m - 1; col3 >= 0; col3--) {
                    T[] bpCol2 = bp[col3];
                    T luDiag = this.f611lu[col3][col3];
                    for (int j2 = 0; j2 < nColB; j2++) {
                        bpCol2[j2] = (FieldElement) bpCol2[j2].divide(luDiag);
                    }
                    for (int i2 = 0; i2 < col3; i2++) {
                        T[] bpI2 = bp[i2];
                        T luICol2 = this.f611lu[i2][col3];
                        for (int j3 = 0; j3 < nColB; j3++) {
                            bpI2[j3] = (FieldElement) bpI2[j3].subtract(bpCol2[j3].multiply(luICol2));
                        }
                    }
                }
                return new Array2DRowFieldMatrix(this.field, bp, false);
            }
        }

        public FieldMatrix<T> getInverse() {
            int m = this.pivot.length;
            T one = (FieldElement) this.field.getOne();
            FieldMatrix<T> identity = new Array2DRowFieldMatrix<>(this.field, m, m);
            for (int i = 0; i < m; i++) {
                identity.setEntry(i, i, one);
            }
            return solve(identity);
        }
    }

    public FieldLUDecomposition(FieldMatrix<T> matrix) {
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        int m = matrix.getColumnDimension();
        this.field = matrix.getField();
        this.f610lu = matrix.getData();
        this.pivot = new int[m];
        this.cachedL = null;
        this.cachedU = null;
        this.cachedP = null;
        for (int row = 0; row < m; row++) {
            this.pivot[row] = row;
        }
        this.even = true;
        this.singular = false;
        for (int col = 0; col < m; col++) {
            FieldElement fieldElement = (FieldElement) this.field.getZero();
            int row2 = 0;
            while (row2 < col) {
                T[] luRow = this.f610lu[row2];
                T sum = luRow[col];
                for (int i = 0; i < row2; i++) {
                    sum = (FieldElement) sum.subtract(luRow[i].multiply(this.f610lu[i][col]));
                }
                luRow[col] = sum;
                row2++;
                T t = sum;
            }
            int row3 = col;
            int nonZero = row3;
            while (row3 < m) {
                T[] luRow2 = this.f610lu[row3];
                T sum2 = luRow2[col];
                for (int i2 = 0; i2 < col; i2++) {
                    sum2 = (FieldElement) sum2.subtract(luRow2[i2].multiply(this.f610lu[i2][col]));
                }
                luRow2[col] = sum2;
                if (this.f610lu[nonZero][col].equals(this.field.getZero())) {
                    nonZero++;
                }
                row3++;
                T t2 = sum2;
            }
            if (nonZero >= m) {
                this.singular = true;
                return;
            }
            if (nonZero != col) {
                FieldElement fieldElement2 = (FieldElement) this.field.getZero();
                for (int i3 = 0; i3 < m; i3++) {
                    T tmp = this.f610lu[nonZero][i3];
                    this.f610lu[nonZero][i3] = this.f610lu[col][i3];
                    this.f610lu[col][i3] = tmp;
                }
                int temp = this.pivot[nonZero];
                this.pivot[nonZero] = this.pivot[col];
                this.pivot[col] = temp;
                this.even = !this.even;
            }
            T luDiag = this.f610lu[col][col];
            for (int row4 = col + 1; row4 < m; row4++) {
                T[] luRow3 = this.f610lu[row4];
                luRow3[col] = (FieldElement) luRow3[col].divide(luDiag);
            }
        }
    }

    public FieldMatrix<T> getL() {
        if (this.cachedL == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedL = new Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                T[] luI = this.f610lu[i];
                for (int j = 0; j < i; j++) {
                    this.cachedL.setEntry(i, j, luI[j]);
                }
                this.cachedL.setEntry(i, i, (FieldElement) this.field.getOne());
            }
        }
        return this.cachedL;
    }

    public FieldMatrix<T> getU() {
        if (this.cachedU == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedU = new Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                T[] luI = this.f610lu[i];
                for (int j = i; j < m; j++) {
                    this.cachedU.setEntry(i, j, luI[j]);
                }
            }
        }
        return this.cachedU;
    }

    public FieldMatrix<T> getP() {
        if (this.cachedP == null && !this.singular) {
            int m = this.pivot.length;
            this.cachedP = new Array2DRowFieldMatrix(this.field, m, m);
            for (int i = 0; i < m; i++) {
                this.cachedP.setEntry(i, this.pivot[i], (FieldElement) this.field.getOne());
            }
        }
        return this.cachedP;
    }

    public int[] getPivot() {
        return (int[]) this.pivot.clone();
    }

    public T getDeterminant() {
        if (this.singular) {
            return (FieldElement) this.field.getZero();
        }
        T determinant = (FieldElement) (this.even ? this.field.getOne() : ((FieldElement) this.field.getZero()).subtract(this.field.getOne()));
        for (int i = 0; i < this.pivot.length; i++) {
            determinant = (FieldElement) determinant.multiply(this.f610lu[i][i]);
        }
        return determinant;
    }

    public FieldDecompositionSolver<T> getSolver() {
        Solver solver = new Solver(this.field, this.f610lu, this.pivot, this.singular);
        return solver;
    }
}
