package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.AreaI.OffsetArea;
import org.apache.poi.hssf.record.formula.eval.AreaEval;
import org.apache.poi.hssf.record.formula.eval.RefEvalBase;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.util.CellReference;

/* renamed from: org.apache.poi.ss.formula.LazyRefEval */
final class LazyRefEval extends RefEvalBase {
    private final SheetRefEvaluator _evaluator;

    public LazyRefEval(int rowIndex, int columnIndex, SheetRefEvaluator sre) {
        super(rowIndex, columnIndex);
        if (sre == null) {
            throw new IllegalArgumentException("sre must not be null");
        }
        this._evaluator = sre;
    }

    public ValueEval getInnerValueEval() {
        return this._evaluator.getEvalForCell(getRow(), getColumn());
    }

    public AreaEval offset(int relFirstRowIx, int relLastRowIx, int relFirstColIx, int relLastColIx) {
        OffsetArea offsetArea = new OffsetArea(getRow(), getColumn(), relFirstRowIx, relLastRowIx, relFirstColIx, relLastColIx);
        return new LazyAreaEval(offsetArea, this._evaluator);
    }

    public String toString() {
        CellReference cr = new CellReference(getRow(), getColumn());
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append("[");
        sb.append(this._evaluator.getSheetName());
        sb.append('!');
        sb.append(cr.formatAsString());
        sb.append("]");
        return sb.toString();
    }
}
