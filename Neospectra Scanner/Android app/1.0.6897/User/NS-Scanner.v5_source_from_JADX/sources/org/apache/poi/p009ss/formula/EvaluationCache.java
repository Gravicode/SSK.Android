package org.apache.poi.p009ss.formula;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.FormulaUsedBlankCellSet.BookSheetKey;
import org.apache.poi.p009ss.formula.PlainCellCache.Loc;

/* renamed from: org.apache.poi.ss.formula.EvaluationCache */
final class EvaluationCache {
    final IEvaluationListener _evaluationListener;
    private final FormulaCellCache _formulaCellCache = new FormulaCellCache();
    private final PlainCellCache _plainCellCache = new PlainCellCache();

    EvaluationCache(IEvaluationListener evaluationListener) {
        this._evaluationListener = evaluationListener;
    }

    public void notifyUpdateCell(int bookIndex, int sheetIndex, EvaluationCell cell) {
        FormulaCellCacheEntry fcce = this._formulaCellCache.get(cell);
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        Loc loc = new Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
        PlainValueCellCacheEntry pcce = this._plainCellCache.get(loc);
        if (cell.getCellType() == 2) {
            if (fcce == null) {
                FormulaCellCacheEntry fcce2 = new FormulaCellCacheEntry();
                if (pcce == null) {
                    if (this._evaluationListener != null) {
                        this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, fcce2);
                    }
                    updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
                }
                this._formulaCellCache.put(cell, fcce2);
            } else {
                fcce.recurseClearCachedFormulaResults(this._evaluationListener);
                fcce.clearFormulaEntry();
            }
            if (pcce != null) {
                pcce.recurseClearCachedFormulaResults(this._evaluationListener);
                this._plainCellCache.remove(loc);
                return;
            }
            return;
        }
        ValueEval value = WorkbookEvaluator.getValueFromNonFormulaCell(cell);
        if (pcce != null) {
            if (pcce.updateValue(value)) {
                pcce.recurseClearCachedFormulaResults(this._evaluationListener);
            }
            if (value == BlankEval.instance) {
                this._plainCellCache.remove(loc);
            }
        } else if (value != BlankEval.instance) {
            PlainValueCellCacheEntry pcce2 = new PlainValueCellCacheEntry(value);
            if (fcce == null) {
                if (this._evaluationListener != null) {
                    this._evaluationListener.onChangeFromBlankValue(sheetIndex, rowIndex, columnIndex, cell, pcce2);
                }
                updateAnyBlankReferencingFormulas(bookIndex, sheetIndex, rowIndex, columnIndex);
            }
            this._plainCellCache.put(loc, pcce2);
        }
        if (fcce != null) {
            this._formulaCellCache.remove(cell);
            fcce.setSensitiveInputCells(null);
            fcce.recurseClearCachedFormulaResults(this._evaluationListener);
        }
    }

    private void updateAnyBlankReferencingFormulas(int bookIndex, int sheetIndex, final int rowIndex, final int columnIndex) {
        final BookSheetKey bsk = new BookSheetKey(bookIndex, sheetIndex);
        this._formulaCellCache.applyOperation(new IEntryOperation() {
            public void processEntry(FormulaCellCacheEntry entry) {
                entry.notifyUpdatedBlankCell(bsk, rowIndex, columnIndex, EvaluationCache.this._evaluationListener);
            }
        });
    }

    public PlainValueCellCacheEntry getPlainValueEntry(int bookIndex, int sheetIndex, int rowIndex, int columnIndex, ValueEval value) {
        Loc loc = new Loc(bookIndex, sheetIndex, rowIndex, columnIndex);
        PlainValueCellCacheEntry result = this._plainCellCache.get(loc);
        if (result == null) {
            result = new PlainValueCellCacheEntry(value);
            this._plainCellCache.put(loc, result);
            if (this._evaluationListener != null) {
                this._evaluationListener.onReadPlainValue(sheetIndex, rowIndex, columnIndex, result);
            }
        } else if (!areValuesEqual(result.getValue(), value)) {
            throw new IllegalStateException("value changed");
        } else if (this._evaluationListener != null) {
            this._evaluationListener.onCacheHit(sheetIndex, rowIndex, columnIndex, value);
        }
        return result;
    }

    private boolean areValuesEqual(ValueEval a, ValueEval b) {
        boolean z = false;
        if (a == null) {
            return false;
        }
        Class<?> cls = a.getClass();
        if (cls != b.getClass()) {
            return false;
        }
        if (a == BlankEval.instance) {
            if (b == a) {
                z = true;
            }
            return z;
        } else if (cls == NumberEval.class) {
            if (((NumberEval) a).getNumberValue() == ((NumberEval) b).getNumberValue()) {
                z = true;
            }
            return z;
        } else if (cls == StringEval.class) {
            return ((StringEval) a).getStringValue().equals(((StringEval) b).getStringValue());
        } else {
            if (cls == BoolEval.class) {
                if (((BoolEval) a).getBooleanValue() == ((BoolEval) b).getBooleanValue()) {
                    z = true;
                }
                return z;
            } else if (cls == ErrorEval.class) {
                if (((ErrorEval) a).getErrorCode() == ((ErrorEval) b).getErrorCode()) {
                    z = true;
                }
                return z;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected value class (");
                sb.append(cls.getName());
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public FormulaCellCacheEntry getOrCreateFormulaCellEntry(EvaluationCell cell) {
        FormulaCellCacheEntry result = this._formulaCellCache.get(cell);
        if (result != null) {
            return result;
        }
        FormulaCellCacheEntry result2 = new FormulaCellCacheEntry();
        this._formulaCellCache.put(cell, result2);
        return result2;
    }

    public void clear() {
        if (this._evaluationListener != null) {
            this._evaluationListener.onClearWholeCache();
        }
        this._plainCellCache.clear();
        this._formulaCellCache.clear();
    }

    public void notifyDeleteCell(int bookIndex, int sheetIndex, EvaluationCell cell) {
        if (cell.getCellType() == 2) {
            FormulaCellCacheEntry fcce = this._formulaCellCache.remove(cell);
            if (fcce != null) {
                fcce.setSensitiveInputCells(null);
                fcce.recurseClearCachedFormulaResults(this._evaluationListener);
                return;
            }
            return;
        }
        PlainValueCellCacheEntry pcce = this._plainCellCache.get(new Loc(bookIndex, sheetIndex, cell.getRowIndex(), cell.getColumnIndex()));
        if (pcce != null) {
            pcce.recurseClearCachedFormulaResults(this._evaluationListener);
        }
    }
}
