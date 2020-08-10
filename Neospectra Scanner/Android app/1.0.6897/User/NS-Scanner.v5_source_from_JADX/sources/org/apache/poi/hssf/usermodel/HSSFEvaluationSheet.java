package org.apache.poi.hssf.usermodel;

import org.apache.poi.p009ss.formula.EvaluationCell;
import org.apache.poi.p009ss.formula.EvaluationSheet;

final class HSSFEvaluationSheet implements EvaluationSheet {
    private final HSSFSheet _hs;

    public HSSFEvaluationSheet(HSSFSheet hs) {
        this._hs = hs;
    }

    public HSSFSheet getHSSFSheet() {
        return this._hs;
    }

    public EvaluationCell getCell(int rowIndex, int columnIndex) {
        HSSFRow row = this._hs.getRow(rowIndex);
        if (row == null) {
            return null;
        }
        HSSFCell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }
        return new HSSFEvaluationCell(cell, this);
    }
}
