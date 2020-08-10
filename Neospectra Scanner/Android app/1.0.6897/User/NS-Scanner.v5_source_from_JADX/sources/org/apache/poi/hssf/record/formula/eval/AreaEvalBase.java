package org.apache.poi.hssf.record.formula.eval;

import org.apache.poi.hssf.record.formula.AreaI;

public abstract class AreaEvalBase implements AreaEval {
    private final int _firstColumn;
    private final int _firstRow;
    private final int _lastColumn;
    private final int _lastRow;
    private final int _nColumns = ((this._lastColumn - this._firstColumn) + 1);
    private final int _nRows = ((this._lastRow - this._firstRow) + 1);

    public abstract ValueEval getRelativeValue(int i, int i2);

    protected AreaEvalBase(int firstRow, int firstColumn, int lastRow, int lastColumn) {
        this._firstColumn = firstColumn;
        this._firstRow = firstRow;
        this._lastColumn = lastColumn;
        this._lastRow = lastRow;
    }

    protected AreaEvalBase(AreaI ptg) {
        this._firstRow = ptg.getFirstRow();
        this._firstColumn = ptg.getFirstColumn();
        this._lastRow = ptg.getLastRow();
        this._lastColumn = ptg.getLastColumn();
    }

    public final int getFirstColumn() {
        return this._firstColumn;
    }

    public final int getFirstRow() {
        return this._firstRow;
    }

    public final int getLastColumn() {
        return this._lastColumn;
    }

    public final int getLastRow() {
        return this._lastRow;
    }

    public final ValueEval getAbsoluteValue(int row, int col) {
        int rowOffsetIx = row - this._firstRow;
        int colOffsetIx = col - this._firstColumn;
        if (rowOffsetIx < 0 || rowOffsetIx >= this._nRows) {
            StringBuilder sb = new StringBuilder();
            sb.append("Specified row index (");
            sb.append(row);
            sb.append(") is outside the allowed range (");
            sb.append(this._firstRow);
            sb.append("..");
            sb.append(this._lastRow);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        } else if (colOffsetIx >= 0 && colOffsetIx < this._nColumns) {
            return getRelativeValue(rowOffsetIx, colOffsetIx);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Specified column index (");
            sb2.append(col);
            sb2.append(") is outside the allowed range (");
            sb2.append(this._firstColumn);
            sb2.append("..");
            sb2.append(col);
            sb2.append(")");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public final boolean contains(int row, int col) {
        return this._firstRow <= row && this._lastRow >= row && this._firstColumn <= col && this._lastColumn >= col;
    }

    public final boolean containsRow(int row) {
        return this._firstRow <= row && this._lastRow >= row;
    }

    public final boolean containsColumn(int col) {
        return this._firstColumn <= col && this._lastColumn >= col;
    }

    public final boolean isColumn() {
        return this._firstColumn == this._lastColumn;
    }

    public final boolean isRow() {
        return this._firstRow == this._lastRow;
    }

    public int getHeight() {
        return (this._lastRow - this._firstRow) + 1;
    }

    public final ValueEval getValue(int row, int col) {
        return getRelativeValue(row, col);
    }

    public int getWidth() {
        return (this._lastColumn - this._firstColumn) + 1;
    }
}
