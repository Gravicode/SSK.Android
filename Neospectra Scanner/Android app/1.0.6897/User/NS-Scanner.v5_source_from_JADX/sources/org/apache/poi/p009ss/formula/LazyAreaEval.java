package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.AreaI;
import org.apache.poi.hssf.record.formula.AreaI.OffsetArea;
import org.apache.poi.hssf.record.formula.eval.AreaEval;
import org.apache.poi.hssf.record.formula.eval.AreaEvalBase;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.util.CellReference;

/* renamed from: org.apache.poi.ss.formula.LazyAreaEval */
final class LazyAreaEval extends AreaEvalBase {
    private final SheetRefEvaluator _evaluator;

    LazyAreaEval(AreaI ptg, SheetRefEvaluator evaluator) {
        super(ptg);
        this._evaluator = evaluator;
    }

    public LazyAreaEval(int firstRowIndex, int firstColumnIndex, int lastRowIndex, int lastColumnIndex, SheetRefEvaluator evaluator) {
        super(firstRowIndex, firstColumnIndex, lastRowIndex, lastColumnIndex);
        this._evaluator = evaluator;
    }

    public ValueEval getRelativeValue(int relativeRowIndex, int relativeColumnIndex) {
        return this._evaluator.getEvalForCell((getFirstRow() + relativeRowIndex) & 65535, (getFirstColumn() + relativeColumnIndex) & 255);
    }

    public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
        OffsetArea offsetArea = new OffsetArea(getFirstRow(), getFirstColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
        return new LazyAreaEval(offsetArea, this._evaluator);
    }

    public LazyAreaEval getRow(int rowIndex) {
        if (rowIndex >= getHeight()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid rowIndex ");
            sb.append(rowIndex);
            sb.append(".  Allowable range is (0..");
            sb.append(getHeight());
            sb.append(").");
            throw new IllegalArgumentException(sb.toString());
        }
        int absRowIx = getFirstRow() + rowIndex;
        LazyAreaEval lazyAreaEval = new LazyAreaEval(absRowIx, getFirstColumn(), absRowIx, getLastColumn(), this._evaluator);
        return lazyAreaEval;
    }

    public LazyAreaEval getColumn(int columnIndex) {
        if (columnIndex >= getWidth()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid columnIndex ");
            sb.append(columnIndex);
            sb.append(".  Allowable range is (0..");
            sb.append(getWidth());
            sb.append(").");
            throw new IllegalArgumentException(sb.toString());
        }
        int absColIx = getFirstColumn() + columnIndex;
        LazyAreaEval lazyAreaEval = new LazyAreaEval(getFirstRow(), absColIx, getLastRow(), absColIx, this._evaluator);
        return lazyAreaEval;
    }

    public String toString() {
        CellReference crA = new CellReference(getFirstRow(), getFirstColumn());
        CellReference crB = new CellReference(getLastRow(), getLastColumn());
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append("[");
        sb.append(this._evaluator.getSheetName());
        sb.append('!');
        sb.append(crA.formatAsString());
        sb.append(':');
        sb.append(crB.formatAsString());
        sb.append("]");
        return sb.toString();
    }
}
