package org.apache.poi.p009ss.formula.eval.forked;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.p009ss.formula.EvaluationCell;
import org.apache.poi.p009ss.formula.EvaluationSheet;
import org.apache.poi.p009ss.formula.EvaluationWorkbook;
import org.apache.poi.p009ss.usermodel.Cell;
import org.apache.poi.p009ss.usermodel.Row;
import org.apache.poi.p009ss.usermodel.Sheet;
import org.apache.poi.p009ss.util.CellReference;

/* renamed from: org.apache.poi.ss.formula.eval.forked.ForkedEvaluationSheet */
final class ForkedEvaluationSheet implements EvaluationSheet {
    private final EvaluationSheet _masterSheet;
    private final Map<RowColKey, ForkedEvaluationCell> _sharedCellsByRowCol = new HashMap();

    /* renamed from: org.apache.poi.ss.formula.eval.forked.ForkedEvaluationSheet$RowColKey */
    private static final class RowColKey implements Comparable<RowColKey> {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final int _columnIndex;
        private final int _rowIndex;

        static {
            Class<ForkedEvaluationSheet> cls = ForkedEvaluationSheet.class;
        }

        public RowColKey(int rowIndex, int columnIndex) {
            this._rowIndex = rowIndex;
            this._columnIndex = columnIndex;
        }

        public boolean equals(Object obj) {
            RowColKey other = (RowColKey) obj;
            return this._rowIndex == other._rowIndex && this._columnIndex == other._columnIndex;
        }

        public int hashCode() {
            return this._rowIndex ^ this._columnIndex;
        }

        public int compareTo(RowColKey o) {
            int cmp = this._rowIndex - o._rowIndex;
            if (cmp != 0) {
                return cmp;
            }
            return this._columnIndex - o._columnIndex;
        }

        public int getRowIndex() {
            return this._rowIndex;
        }

        public int getColumnIndex() {
            return this._columnIndex;
        }
    }

    public ForkedEvaluationSheet(EvaluationSheet masterSheet) {
        this._masterSheet = masterSheet;
    }

    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        ForkedEvaluationCell result = (ForkedEvaluationCell) this._sharedCellsByRowCol.get(new RowColKey(rowIndex, columnIndex));
        if (result == null) {
            return this._masterSheet.getCell(rowIndex, columnIndex);
        }
        return result;
    }

    public ForkedEvaluationCell getOrCreateUpdatableCell(int rowIndex, int columnIndex) {
        RowColKey key = new RowColKey(rowIndex, columnIndex);
        ForkedEvaluationCell result = (ForkedEvaluationCell) this._sharedCellsByRowCol.get(key);
        if (result != null) {
            return result;
        }
        EvaluationCell mcell = this._masterSheet.getCell(rowIndex, columnIndex);
        if (mcell == null) {
            CellReference cr = new CellReference(rowIndex, columnIndex);
            StringBuilder sb = new StringBuilder();
            sb.append("Underlying cell '");
            sb.append(cr.formatAsString());
            sb.append("' is missing in master sheet.");
            throw new UnsupportedOperationException(sb.toString());
        }
        ForkedEvaluationCell result2 = new ForkedEvaluationCell(this, mcell);
        this._sharedCellsByRowCol.put(key, result2);
        return result2;
    }

    public void copyUpdatedCells(Sheet sheet) {
        RowColKey[] keys = new RowColKey[this._sharedCellsByRowCol.size()];
        this._sharedCellsByRowCol.keySet().toArray(keys);
        Arrays.sort(keys);
        for (RowColKey key : keys) {
            Row row = sheet.getRow(key.getRowIndex());
            if (row == null) {
                row = sheet.createRow(key.getRowIndex());
            }
            Cell destCell = row.getCell(key.getColumnIndex());
            if (destCell == null) {
                destCell = row.createCell(key.getColumnIndex());
            }
            ((ForkedEvaluationCell) this._sharedCellsByRowCol.get(key)).copyValue(destCell);
        }
    }

    public int getSheetIndex(EvaluationWorkbook mewb) {
        return mewb.getSheetIndex(this._masterSheet);
    }
}
