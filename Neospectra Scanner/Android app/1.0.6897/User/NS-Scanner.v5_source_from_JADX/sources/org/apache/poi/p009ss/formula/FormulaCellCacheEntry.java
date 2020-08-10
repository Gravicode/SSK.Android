package org.apache.poi.p009ss.formula;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.FormulaUsedBlankCellSet.BookSheetKey;

/* renamed from: org.apache.poi.ss.formula.FormulaCellCacheEntry */
final class FormulaCellCacheEntry extends CellCacheEntry {
    private CellCacheEntry[] _sensitiveInputCells;
    private FormulaUsedBlankCellSet _usedBlankCellGroup;

    public boolean isInputSensitive() {
        if (this._sensitiveInputCells != null && this._sensitiveInputCells.length > 0) {
            return true;
        }
        boolean z = false;
        if (this._usedBlankCellGroup != null && !this._usedBlankCellGroup.isEmpty()) {
            z = true;
        }
        return z;
    }

    public void setSensitiveInputCells(CellCacheEntry[] sensitiveInputCells) {
        changeConsumingCells(sensitiveInputCells == null ? CellCacheEntry.EMPTY_ARRAY : sensitiveInputCells);
        this._sensitiveInputCells = sensitiveInputCells;
    }

    public void clearFormulaEntry() {
        CellCacheEntry[] usedCells = this._sensitiveInputCells;
        if (usedCells != null) {
            for (int i = usedCells.length - 1; i >= 0; i--) {
                usedCells[i].clearConsumingCell(this);
            }
        }
        this._sensitiveInputCells = null;
        clearValue();
    }

    private void changeConsumingCells(CellCacheEntry[] usedCells) {
        Set<CellCacheEntry> usedSet;
        CellCacheEntry[] prevUsedCells = this._sensitiveInputCells;
        for (CellCacheEntry addConsumingCell : usedCells) {
            addConsumingCell.addConsumingCell(this);
        }
        if (prevUsedCells != null) {
            if (nPrevUsed >= 1) {
                if (nUsed < 1) {
                    usedSet = Collections.emptySet();
                } else {
                    usedSet = new HashSet<>((nUsed * 3) / 2);
                    for (CellCacheEntry add : usedCells) {
                        usedSet.add(add);
                    }
                }
                for (CellCacheEntry prevUsed : prevUsedCells) {
                    if (!usedSet.contains(prevUsed)) {
                        prevUsed.clearConsumingCell(this);
                    }
                }
            }
        }
    }

    public void updateFormulaResult(ValueEval result, CellCacheEntry[] sensitiveInputCells, FormulaUsedBlankCellSet usedBlankAreas) {
        updateValue(result);
        setSensitiveInputCells(sensitiveInputCells);
        this._usedBlankCellGroup = usedBlankAreas;
    }

    public void notifyUpdatedBlankCell(BookSheetKey bsk, int rowIndex, int columnIndex, IEvaluationListener evaluationListener) {
        if (this._usedBlankCellGroup != null && this._usedBlankCellGroup.containsCell(bsk, rowIndex, columnIndex)) {
            clearFormulaEntry();
            recurseClearCachedFormulaResults(evaluationListener);
        }
    }
}
