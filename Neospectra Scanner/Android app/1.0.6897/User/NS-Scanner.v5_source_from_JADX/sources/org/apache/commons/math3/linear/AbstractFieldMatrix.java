package org.apache.commons.math3.linear;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.MathArrays;

public abstract class AbstractFieldMatrix<T extends FieldElement<T>> implements FieldMatrix<T> {
    private final Field<T> field;

    public abstract void addToEntry(int i, int i2, T t) throws OutOfRangeException;

    public abstract FieldMatrix<T> copy();

    public abstract FieldMatrix<T> createMatrix(int i, int i2) throws NotStrictlyPositiveException;

    public abstract int getColumnDimension();

    public abstract T getEntry(int i, int i2) throws OutOfRangeException;

    public abstract int getRowDimension();

    public abstract void multiplyEntry(int i, int i2, T t) throws OutOfRangeException;

    public abstract void setEntry(int i, int i2, T t) throws OutOfRangeException;

    protected AbstractFieldMatrix() {
        this.field = null;
    }

    protected AbstractFieldMatrix(Field<T> field2) {
        this.field = field2;
    }

    protected AbstractFieldMatrix(Field<T> field2, int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        if (rowDimension <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(rowDimension));
        } else if (columnDimension <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(columnDimension));
        } else {
            this.field = field2;
        }
    }

    protected static <T extends FieldElement<T>> Field<T> extractField(T[][] d) throws NoDataException, NullArgumentException {
        if (d == null) {
            throw new NullArgumentException();
        } else if (d.length == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        } else if (d[0].length != 0) {
            return d[0][0].getField();
        } else {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
    }

    protected static <T extends FieldElement<T>> Field<T> extractField(T[] d) throws NoDataException {
        if (d.length != 0) {
            return d[0].getField();
        }
        throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
    }

    @Deprecated
    protected static <T extends FieldElement<T>> T[][] buildArray(Field<T> field2, int rows, int columns) {
        return (FieldElement[][]) MathArrays.buildArray(field2, rows, columns);
    }

    @Deprecated
    protected static <T extends FieldElement<T>> T[] buildArray(Field<T> field2, int length) {
        return (FieldElement[]) MathArrays.buildArray(field2, length);
    }

    public Field<T> getField() {
        return this.field;
    }

    public FieldMatrix<T> add(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkAdditionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).add(m.getEntry(row, col)));
            }
        }
        return out;
    }

    public FieldMatrix<T> subtract(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        checkSubtractionCompatible(m);
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).subtract(m.getEntry(row, col)));
            }
        }
        return out;
    }

    public FieldMatrix<T> scalarAdd(T d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).add(d));
            }
        }
        return out;
    }

    public FieldMatrix<T> scalarMultiply(T d) {
        int rowCount = getRowDimension();
        int columnCount = getColumnDimension();
        FieldMatrix<T> out = createMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                out.setEntry(row, col, (FieldElement) getEntry(row, col).multiply(d));
            }
        }
        return out;
    }

    public FieldMatrix<T> multiply(FieldMatrix<T> m) throws DimensionMismatchException {
        checkMultiplicationCompatible(m);
        int nRows = getRowDimension();
        int nCols = m.getColumnDimension();
        int nSum = getColumnDimension();
        FieldMatrix<T> out = createMatrix(nRows, nCols);
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                T sum = (FieldElement) this.field.getZero();
                for (int i = 0; i < nSum; i++) {
                    sum = (FieldElement) sum.add(getEntry(row, i).multiply(m.getEntry(i, col)));
                }
                out.setEntry(row, col, sum);
            }
        }
        return out;
    }

    public FieldMatrix<T> preMultiply(FieldMatrix<T> m) throws DimensionMismatchException {
        return m.multiply(this);
    }

    public FieldMatrix<T> power(int p) throws NonSquareMatrixException, NotPositiveException {
        if (p < 0) {
            throw new NotPositiveException(Integer.valueOf(p));
        } else if (!isSquare()) {
            throw new NonSquareMatrixException(getRowDimension(), getColumnDimension());
        } else if (p == 0) {
            return MatrixUtils.createFieldIdentityMatrix(getField(), getRowDimension());
        } else {
            if (p == 1) {
                return copy();
            }
            char[] binaryRepresentation = Integer.toBinaryString(p - 1).toCharArray();
            ArrayList<Integer> nonZeroPositions = new ArrayList<>();
            for (int i = 0; i < binaryRepresentation.length; i++) {
                if (binaryRepresentation[i] == '1') {
                    nonZeroPositions.add(Integer.valueOf((binaryRepresentation.length - i) - 1));
                }
            }
            ArrayList<FieldMatrix<T>> results = new ArrayList<>(binaryRepresentation.length);
            results.add(0, copy());
            for (int i2 = 1; i2 < binaryRepresentation.length; i2++) {
                FieldMatrix<T> s = (FieldMatrix) results.get(i2 - 1);
                results.add(i2, s.multiply(s));
            }
            FieldMatrix<T> result = copy();
            Iterator i$ = nonZeroPositions.iterator();
            while (i$.hasNext()) {
                result = result.multiply((FieldMatrix) results.get(((Integer) i$.next()).intValue()));
            }
            return result;
        }
    }

    public T[][] getData() {
        T[][] data = (FieldElement[][]) MathArrays.buildArray(this.field, getRowDimension(), getColumnDimension());
        for (int i = 0; i < data.length; i++) {
            T[] dataI = data[i];
            for (int j = 0; j < dataI.length; j++) {
                dataI[j] = getEntry(i, j);
            }
        }
        return data;
    }

    public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        FieldMatrix<T> subMatrix = createMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                subMatrix.setEntry(i - startRow, j - startColumn, getEntry(i, j));
            }
        }
        return subMatrix;
    }

    public FieldMatrix<T> getSubMatrix(final int[] selectedRows, final int[] selectedColumns) throws NoDataException, NullArgumentException, OutOfRangeException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        FieldMatrix<T> subMatrix = createMatrix(selectedRows.length, selectedColumns.length);
        subMatrix.walkInOptimizedOrder((FieldMatrixChangingVisitor<T>) new DefaultFieldMatrixChangingVisitor<T>((FieldElement) this.field.getZero()) {
            public T visit(int row, int column, T t) {
                return AbstractFieldMatrix.this.getEntry(selectedRows[row], selectedColumns[column]);
            }
        });
        return subMatrix;
    }

    public void copySubMatrix(int startRow, int endRow, int startColumn, int endColumn, final T[][] destination) throws MatrixDimensionMismatchException, NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        int rowsCount = (endRow + 1) - startRow;
        int columnsCount = (endColumn + 1) - startColumn;
        if (destination.length < rowsCount || destination[0].length < columnsCount) {
            throw new MatrixDimensionMismatchException(destination.length, destination[0].length, rowsCount, columnsCount);
        }
        walkInOptimizedOrder((FieldMatrixPreservingVisitor<T>) new DefaultFieldMatrixPreservingVisitor<T>((FieldElement) this.field.getZero()) {
            private int startColumn;
            private int startRow;

            public void start(int rows, int columns, int startRow2, int endRow, int startColumn2, int endColumn) {
                this.startRow = startRow2;
                this.startColumn = startColumn2;
            }

            public void visit(int row, int column, T value) {
                destination[row - this.startRow][column - this.startColumn] = value;
            }
        }, startRow, endRow, startColumn, endColumn);
    }

    public void copySubMatrix(int[] selectedRows, int[] selectedColumns, T[][] destination) throws MatrixDimensionMismatchException, NoDataException, NullArgumentException, OutOfRangeException {
        checkSubMatrixIndex(selectedRows, selectedColumns);
        if (destination.length < selectedRows.length || destination[0].length < selectedColumns.length) {
            throw new MatrixDimensionMismatchException(destination.length, destination[0].length, selectedRows.length, selectedColumns.length);
        }
        for (int i = 0; i < selectedRows.length; i++) {
            T[] destinationI = destination[i];
            for (int j = 0; j < selectedColumns.length; j++) {
                destinationI[j] = getEntry(selectedRows[i], selectedColumns[j]);
            }
        }
    }

    public void setSubMatrix(T[][] subMatrix, int row, int column) throws DimensionMismatchException, OutOfRangeException, NoDataException, NullArgumentException {
        if (subMatrix == null) {
            throw new NullArgumentException();
        }
        int nRows = subMatrix.length;
        if (nRows == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        int nCols = subMatrix[0].length;
        if (nCols == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        for (int r = 1; r < nRows; r++) {
            if (subMatrix[r].length != nCols) {
                throw new DimensionMismatchException(nCols, subMatrix[r].length);
            }
        }
        checkRowIndex(row);
        checkColumnIndex(column);
        checkRowIndex((nRows + row) - 1);
        checkColumnIndex((nCols + column) - 1);
        for (int i = 0; i < nRows; i++) {
            for (int j = 0; j < nCols; j++) {
                setEntry(row + i, column + j, subMatrix[i][j]);
            }
        }
    }

    public FieldMatrix<T> getRowMatrix(int row) throws OutOfRangeException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        FieldMatrix<T> out = createMatrix(1, nCols);
        for (int i = 0; i < nCols; i++) {
            out.setEntry(0, i, getEntry(row, i));
        }
        return out;
    }

    public void setRowMatrix(int row, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() == 1 && matrix.getColumnDimension() == nCols) {
            for (int i = 0; i < nCols; i++) {
                setEntry(row, i, matrix.getEntry(0, i));
            }
            return;
        }
        throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
    }

    public FieldMatrix<T> getColumnMatrix(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        FieldMatrix<T> out = createMatrix(nRows, 1);
        for (int i = 0; i < nRows; i++) {
            out.setEntry(i, 0, getEntry(i, column));
        }
        return out;
    }

    public void setColumnMatrix(int column, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() == nRows && matrix.getColumnDimension() == 1) {
            for (int i = 0; i < nRows; i++) {
                setEntry(i, column, matrix.getEntry(i, 0));
            }
            return;
        }
        throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
    }

    public FieldVector<T> getRowVector(int row) throws OutOfRangeException {
        return new ArrayFieldVector(this.field, (T[]) getRow(row), false);
    }

    public void setRowVector(int row, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (vector.getDimension() != nCols) {
            throw new MatrixDimensionMismatchException(1, vector.getDimension(), 1, nCols);
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, vector.getEntry(i));
        }
    }

    public FieldVector<T> getColumnVector(int column) throws OutOfRangeException {
        return new ArrayFieldVector(this.field, (T[]) getColumn(column), false);
    }

    public void setColumnVector(int column, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (vector.getDimension() != nRows) {
            throw new MatrixDimensionMismatchException(vector.getDimension(), 1, nRows, 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, vector.getEntry(i));
        }
    }

    public T[] getRow(int row) throws OutOfRangeException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        T[] out = (FieldElement[]) MathArrays.buildArray(this.field, nCols);
        for (int i = 0; i < nCols; i++) {
            out[i] = getEntry(row, i);
        }
        return out;
    }

    public void setRow(int row, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        for (int i = 0; i < nCols; i++) {
            setEntry(row, i, array[i]);
        }
    }

    public T[] getColumn(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        T[] out = (FieldElement[]) MathArrays.buildArray(this.field, nRows);
        for (int i = 0; i < nRows; i++) {
            out[i] = getEntry(i, column);
        }
        return out;
    }

    public void setColumn(int column, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        for (int i = 0; i < nRows; i++) {
            setEntry(i, column, array[i]);
        }
    }

    public FieldMatrix<T> transpose() {
        final FieldMatrix<T> out = createMatrix(getColumnDimension(), getRowDimension());
        walkInOptimizedOrder((FieldMatrixPreservingVisitor<T>) new DefaultFieldMatrixPreservingVisitor<T>((FieldElement) this.field.getZero()) {
            public void visit(int row, int column, T value) {
                out.setEntry(column, row, value);
            }
        });
        return out;
    }

    public boolean isSquare() {
        return getColumnDimension() == getRowDimension();
    }

    public T getTrace() throws NonSquareMatrixException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (nRows != nCols) {
            throw new NonSquareMatrixException(nRows, nCols);
        }
        T trace = (FieldElement) this.field.getZero();
        for (int i = 0; i < nRows; i++) {
            trace = (FieldElement) trace.add(getEntry(i, i));
        }
        return trace;
    }

    public T[] operate(T[] v) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nCols) {
            throw new DimensionMismatchException(v.length, nCols);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(this.field, nRows);
        for (int row = 0; row < nRows; row++) {
            T sum = (FieldElement) this.field.getZero();
            for (int i = 0; i < nCols; i++) {
                sum = (FieldElement) sum.add(getEntry(row, i).multiply(v[i]));
            }
            out[row] = sum;
        }
        return out;
    }

    public FieldVector<T> operate(FieldVector<T> v) throws DimensionMismatchException {
        try {
            return new ArrayFieldVector(this.field, (T[]) operate((T[]) ((ArrayFieldVector) v).getDataRef()), false);
        } catch (ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nCols) {
                throw new DimensionMismatchException(v.getDimension(), nCols);
            }
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, nRows);
            for (int row = 0; row < nRows; row++) {
                T sum = (FieldElement) this.field.getZero();
                for (int i = 0; i < nCols; i++) {
                    sum = (FieldElement) sum.add(getEntry(row, i).multiply(v.getEntry(i)));
                }
                fieldElementArr[row] = sum;
            }
            return new ArrayFieldVector(this.field, (T[]) fieldElementArr, false);
        }
    }

    public T[] preMultiply(T[] v) throws DimensionMismatchException {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (v.length != nRows) {
            throw new DimensionMismatchException(v.length, nRows);
        }
        T[] out = (FieldElement[]) MathArrays.buildArray(this.field, nCols);
        for (int col = 0; col < nCols; col++) {
            T sum = (FieldElement) this.field.getZero();
            for (int i = 0; i < nRows; i++) {
                sum = (FieldElement) sum.add(getEntry(i, col).multiply(v[i]));
            }
            out[col] = sum;
        }
        return out;
    }

    public FieldVector<T> preMultiply(FieldVector<T> v) throws DimensionMismatchException {
        try {
            return new ArrayFieldVector(this.field, (T[]) preMultiply((T[]) ((ArrayFieldVector) v).getDataRef()), false);
        } catch (ClassCastException e) {
            int nRows = getRowDimension();
            int nCols = getColumnDimension();
            if (v.getDimension() != nRows) {
                throw new DimensionMismatchException(v.getDimension(), nRows);
            }
            FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(this.field, nCols);
            for (int col = 0; col < nCols; col++) {
                T sum = (FieldElement) this.field.getZero();
                for (int i = 0; i < nRows; i++) {
                    sum = (FieldElement) sum.add(getEntry(i, col).multiply(v.getEntry(i)));
                }
                fieldElementArr[col] = sum;
            }
            return new ArrayFieldVector(this.field, (T[]) fieldElementArr, false);
        }
    }

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                setEntry(row, column, visitor.visit(row, column, getEntry(row, column)));
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                setEntry(row, column, visitor.visit(row, column, getEntry(row, column)));
            }
        }
        return visitor.end();
    }

    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int row = startRow; row <= endRow; row++) {
            for (int column = startColumn; column <= endColumn; column++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                setEntry(row, column, visitor.visit(row, column, getEntry(row, column)));
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor) {
        int rows = getRowDimension();
        int columns = getColumnDimension();
        visitor.start(rows, columns, 0, rows - 1, 0, columns - 1);
        for (int column = 0; column < columns; column++) {
            for (int row = 0; row < rows; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                setEntry(row, column, visitor.visit(row, column, getEntry(row, column)));
            }
        }
        return visitor.end();
    }

    public T walkInColumnOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        visitor.start(getRowDimension(), getColumnDimension(), startRow, endRow, startColumn, endColumn);
        for (int column = startColumn; column <= endColumn; column++) {
            for (int row = startRow; row <= endRow; row++) {
                visitor.visit(row, column, getEntry(row, column));
            }
        }
        return visitor.end();
    }

    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor) {
        return walkInRowOrder(visitor);
    }

    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor) {
        return walkInRowOrder(visitor);
    }

    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInRowOrder(visitor, startRow, endRow, startColumn, endColumn);
    }

    public String toString() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        StringBuffer res = new StringBuffer();
        String fullClassName = getClass().getName();
        res.append(fullClassName.substring(fullClassName.lastIndexOf(46) + 1));
        res.append(VectorFormat.DEFAULT_PREFIX);
        for (int i = 0; i < nRows; i++) {
            if (i > 0) {
                res.append(",");
            }
            res.append(VectorFormat.DEFAULT_PREFIX);
            for (int j = 0; j < nCols; j++) {
                if (j > 0) {
                    res.append(",");
                }
                res.append(getEntry(i, j));
            }
            res.append(VectorFormat.DEFAULT_SUFFIX);
        }
        res.append(VectorFormat.DEFAULT_SUFFIX);
        return res.toString();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof FieldMatrix)) {
            return false;
        }
        FieldMatrix<?> m = (FieldMatrix) object;
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        if (m.getColumnDimension() != nCols || m.getRowDimension() != nRows) {
            return false;
        }
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (!getEntry(row, col).equals(m.getEntry(row, col))) {
                    return false;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        int ret = (((322562 * 31) + nRows) * 31) + nCols;
        int row = 0;
        while (row < nRows) {
            int ret2 = ret;
            for (int col = 0; col < nCols; col++) {
                ret2 = (ret2 * 31) + ((((row + 1) * 11) + ((col + 1) * 17)) * getEntry(row, col).hashCode());
            }
            row++;
            ret = ret2;
        }
        return ret;
    }

    /* access modifiers changed from: protected */
    public void checkRowIndex(int row) throws OutOfRangeException {
        if (row < 0 || row >= getRowDimension()) {
            throw new OutOfRangeException(LocalizedFormats.ROW_INDEX, Integer.valueOf(row), Integer.valueOf(0), Integer.valueOf(getRowDimension() - 1));
        }
    }

    /* access modifiers changed from: protected */
    public void checkColumnIndex(int column) throws OutOfRangeException {
        if (column < 0 || column >= getColumnDimension()) {
            throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX, Integer.valueOf(column), Integer.valueOf(0), Integer.valueOf(getColumnDimension() - 1));
        }
    }

    /* access modifiers changed from: protected */
    public void checkSubMatrixIndex(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkRowIndex(startRow);
        checkRowIndex(endRow);
        if (endRow < startRow) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(endRow), Integer.valueOf(startRow), true);
        }
        checkColumnIndex(startColumn);
        checkColumnIndex(endColumn);
        if (endColumn < startColumn) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, Integer.valueOf(endColumn), Integer.valueOf(startColumn), true);
        }
    }

    /* access modifiers changed from: protected */
    public void checkSubMatrixIndex(int[] selectedRows, int[] selectedColumns) throws NoDataException, NullArgumentException, OutOfRangeException {
        if (selectedRows == null || selectedColumns == null) {
            throw new NullArgumentException();
        } else if (selectedRows.length == 0 || selectedColumns.length == 0) {
            throw new NoDataException();
        } else {
            for (int row : selectedRows) {
                checkRowIndex(row);
            }
            for (int column : selectedColumns) {
                checkColumnIndex(column);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void checkAdditionCompatible(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        if (getRowDimension() != m.getRowDimension() || getColumnDimension() != m.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(m.getRowDimension(), m.getColumnDimension(), getRowDimension(), getColumnDimension());
        }
    }

    /* access modifiers changed from: protected */
    public void checkSubtractionCompatible(FieldMatrix<T> m) throws MatrixDimensionMismatchException {
        if (getRowDimension() != m.getRowDimension() || getColumnDimension() != m.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(m.getRowDimension(), m.getColumnDimension(), getRowDimension(), getColumnDimension());
        }
    }

    /* access modifiers changed from: protected */
    public void checkMultiplicationCompatible(FieldMatrix<T> m) throws DimensionMismatchException {
        if (getColumnDimension() != m.getRowDimension()) {
            throw new DimensionMismatchException(m.getRowDimension(), getColumnDimension());
        }
    }
}
