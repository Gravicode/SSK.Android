package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

public class Array2DRowFieldMatrix<T extends FieldElement<T>> extends AbstractFieldMatrix<T> implements Serializable {
    private static final long serialVersionUID = 7260756672015356458L;
    private T[][] data;

    public Array2DRowFieldMatrix(Field<T> field) {
        super(field);
    }

    public Array2DRowFieldMatrix(Field<T> field, int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        super(field, rowDimension, columnDimension);
        this.data = (FieldElement[][]) MathArrays.buildArray(field, rowDimension, columnDimension);
    }

    public Array2DRowFieldMatrix(T[][] d) throws DimensionMismatchException, NullArgumentException, NoDataException {
        this(extractField(d), d);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[][] d) throws DimensionMismatchException, NullArgumentException, NoDataException {
        super(field);
        copyIn(d);
    }

    public Array2DRowFieldMatrix(T[][] d, boolean copyArray) throws DimensionMismatchException, NoDataException, NullArgumentException {
        this(extractField(d), d, copyArray);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[][] d, boolean copyArray) throws DimensionMismatchException, NoDataException, NullArgumentException {
        super(field);
        if (copyArray) {
            copyIn(d);
            return;
        }
        MathUtils.checkNotNull(d);
        int nRows = d.length;
        if (nRows == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        int nCols = d[0].length;
        if (nCols == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        for (int r = 1; r < nRows; r++) {
            if (d[r].length != nCols) {
                throw new DimensionMismatchException(nCols, d[r].length);
            }
        }
        this.data = d;
    }

    public Array2DRowFieldMatrix(T[] v) throws NoDataException {
        this(extractField(v), v);
    }

    public Array2DRowFieldMatrix(Field<T> field, T[] v) {
        super(field);
        int nRows = v.length;
        this.data = (FieldElement[][]) MathArrays.buildArray(getField(), nRows, 1);
        for (int row = 0; row < nRows; row++) {
            this.data[row][0] = v[row];
        }
    }

    public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new Array2DRowFieldMatrix(getField(), rowDimension, columnDimension);
    }

    public FieldMatrix<T> copy() {
        return new Array2DRowFieldMatrix(getField(), (T[][]) copyOut(), false);
    }

    public Array2DRowFieldMatrix<T> add(Array2DRowFieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkAdditionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        T[][] outData = (FieldElement[][]) MathArrays.buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m.data[row];
            T[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = (FieldElement) dataRow[col].add(mRow[col]);
            }
        }
        return new Array2DRowFieldMatrix<>(getField(), outData, false);
    }

    public Array2DRowFieldMatrix<T> subtract(Array2DRowFieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkSubtractionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        T[][] outData = (FieldElement[][]) MathArrays.buildArray(getField(), rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            T[] dataRow = this.data[row];
            T[] mRow = m.data[row];
            T[] outDataRow = outData[row];
            for (int col = 0; col < columnCount; col++) {
                outDataRow[col] = (FieldElement) dataRow[col].subtract(mRow[col]);
            }
        }
        return new Array2DRowFieldMatrix<>(getField(), outData, false);
    }

    public Array2DRowFieldMatrix<T> multiply(Array2DRowFieldMatrix<T> m) throws DimensionMismatchException {
        checkMultiplicationCompatible(m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(getField(), nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            T[] dataRow = this.data[row];
            FieldElement[] fieldElementArr2 = fieldElementArr[row];
            for (int col = 0; col < nCols; col++) {
                T sum = (FieldElement) getField().getZero();
                for (int i = 0; i < nSum; i++) {
                    sum = (FieldElement) sum.add(dataRow[i].multiply(m.data[i][col]));
                }
                fieldElementArr2[col] = sum;
            }
        }
        return new Array2DRowFieldMatrix<>(getField(), (T[][]) fieldElementArr, false);
    }

    public T[][] getData() {
        return copyOut();
    }

    public T[][] getDataRef() {
        return this.data;
    }

    public void setSubMatrix(T[][] subMatrix, int row, int column) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        if (this.data != null) {
            super.setSubMatrix(subMatrix, row, column);
        } else if (row > 0) {
            throw new MathIllegalStateException(LocalizedFormats.FIRST_ROWS_NOT_INITIALIZED_YET, Integer.valueOf(row));
        } else if (column > 0) {
            throw new MathIllegalStateException(LocalizedFormats.FIRST_COLUMNS_NOT_INITIALIZED_YET, Integer.valueOf(column));
        } else if (subMatrix.length == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        } else {
            int nCols = subMatrix[0].length;
            if (nCols == 0) {
                throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
            }
            this.data = (FieldElement[][]) MathArrays.buildArray(getField(), subMatrix.length, nCols);
            for (int i = 0; i < this.data.length; i++) {
                if (subMatrix[i].length != nCols) {
                    throw new DimensionMismatchException(nCols, subMatrix[i].length);
                }
                System.arraycopy(subMatrix[i], 0, this.data[i + row], column, nCols);
            }
        }
    }

    public T getEntry(int row, int column) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        return this.data[row][column];
    }

    public void setEntry(int row, int column, T value) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        this.data[row][column] = value;
    }

    public void addToEntry(int row, int column, T increment) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        this.data[row][column] = (FieldElement) this.data[row][column].add(increment);
    }

    public void multiplyEntry(int row, int column, T factor) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        this.data[row][column] = (FieldElement) this.data[row][column].multiply(factor);
    }

    public int getRowDimension() {
        if (this.data == null) {
            return 0;
        }
        return this.data.length;
    }

    public int getColumnDimension() {
        if (this.data == null || this.data[0] == null) {
            return 0;
        }
        return this.data[0].length;
    }

    public T[] operate(T[] v) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nCols) {
            throw new DimensionMismatchException(v.length, nCols);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), nRows);
        for (int row = 0; row < nRows; row++) {
            T[] dataRow = this.data[row];
            T sum = (FieldElement) getField().getZero();
            for (int i = 0; i < nCols; i++) {
                sum = (FieldElement) sum.add(dataRow[i].multiply(v[i]));
            }
            out[row] = sum;
        }
        return out;
    }

    public T[] preMultiply(T[] v) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nRows) {
            throw new DimensionMismatchException(v.length, nRows);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(getField(), nCols);
        for (int col = 0; col < nCols; col++) {
            T sum = (FieldElement) getField().getZero();
            for (int i = 0; i < nRows; i++) {
                sum = (FieldElement) sum.add(this.data[i][col].multiply(v[i]));
            }
            out[col] = sum;
        }
        return out;
    }

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i = 0; i < rows; i++) {
            T[] rowI = this.data[i];
            for (int j = 0; j < columns; j++) {
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int i = 0; i < rows; i++) {
            T[] rowI = this.data[i];
            for (int j = 0; j < columns; j++) {
                visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i = startRow; i <= endRow; i++) {
            T[] rowI = this.data[i];
            for (int j = startColumn; j <= endColumn; j++) {
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int i = startRow; i <= endRow; i++) {
            T[] rowI = this.data[i];
            for (int j = startColumn; j <= endColumn; j++) {
                visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                T[] rowI = this.data[i];
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int j = 0; j < columns; j++) {
            for (int i = 0; i < rows; i++) {
                visitor.visit(i, j, this.data[i][j]);
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j = startColumn; j <= endColumn; j++) {
            for (int i = startRow; i <= endRow; i++) {
                T[] rowI = this.data[i];
                rowI[j] = visitor.visit(i, j, rowI[j]);
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws OutOfRangeException, NumberIsTooSmallException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int j = startColumn; j <= endColumn; j++) {
            for (int i = startRow; i <= endRow; i++) {
                visitor.visit(i, j, this.data[i][j]);
            }
        }
        return visitor.end();
    }

    private T[][] copyOut() {
        int nRows = getRowDimension();
        T[][] out = (FieldElement[][]) MathArrays.buildArray(getField(), nRows, getColumnDimension());
        for (int i = 0; i < nRows; i++) {
            System.arraycopy(this.data[i], 0, out[i], 0, this.data[i].length);
        }
        return out;
    }

    private void copyIn(T[][] in) throws NullArgumentException, NoDataException, DimensionMismatchException {
        setSubMatrix(in, 0, 0);
    }
}
