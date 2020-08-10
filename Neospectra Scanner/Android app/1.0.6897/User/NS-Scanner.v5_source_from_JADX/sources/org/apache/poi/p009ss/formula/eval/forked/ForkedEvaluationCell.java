package org.apache.poi.p009ss.formula.eval.forked;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.EvaluationCell;
import org.apache.poi.p009ss.formula.EvaluationSheet;
import org.apache.poi.p009ss.usermodel.Cell;

/* renamed from: org.apache.poi.ss.formula.eval.forked.ForkedEvaluationCell */
final class ForkedEvaluationCell implements EvaluationCell {
    private boolean _booleanValue;
    private int _cellType;
    private int _errorValue;
    private final EvaluationCell _masterCell;
    private double _numberValue;
    private final EvaluationSheet _sheet;
    private String _stringValue;

    public ForkedEvaluationCell(ForkedEvaluationSheet sheet, EvaluationCell masterCell) {
        this._sheet = sheet;
        this._masterCell = masterCell;
        setValue(BlankEval.instance);
    }

    public Object getIdentityKey() {
        return this._masterCell.getIdentityKey();
    }

    public void setValue(ValueEval value) {
        Class<? extends ValueEval> cls = value.getClass();
        if (cls == NumberEval.class) {
            this._cellType = 0;
            this._numberValue = ((NumberEval) value).getNumberValue();
        } else if (cls == StringEval.class) {
            this._cellType = 1;
            this._stringValue = ((StringEval) value).getStringValue();
        } else if (cls == BoolEval.class) {
            this._cellType = 4;
            this._booleanValue = ((BoolEval) value).getBooleanValue();
        } else if (cls == ErrorEval.class) {
            this._cellType = 5;
            this._errorValue = ((ErrorEval) value).getErrorCode();
        } else if (cls == BlankEval.class) {
            this._cellType = 3;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value class (");
            sb.append(cls.getName());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void copyValue(Cell destCell) {
        switch (this._cellType) {
            case 0:
                destCell.setCellValue(this._numberValue);
                return;
            case 1:
                destCell.setCellValue(this._stringValue);
                return;
            case 3:
                destCell.setCellType(3);
                return;
            case 4:
                destCell.setCellValue(this._booleanValue);
                return;
            case 5:
                destCell.setCellErrorValue((byte) this._errorValue);
                return;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unexpected data type (");
                sb.append(this._cellType);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
    }

    private void checkCellType(int expectedCellType) {
        if (this._cellType != expectedCellType) {
            StringBuilder sb = new StringBuilder();
            sb.append("Wrong data type (");
            sb.append(this._cellType);
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
    }

    public int getCellType() {
        return this._cellType;
    }

    public boolean getBooleanCellValue() {
        checkCellType(4);
        return this._booleanValue;
    }

    public int getErrorCellValue() {
        checkCellType(5);
        return this._errorValue;
    }

    public double getNumericCellValue() {
        checkCellType(0);
        return this._numberValue;
    }

    public String getStringCellValue() {
        checkCellType(1);
        return this._stringValue;
    }

    public EvaluationSheet getSheet() {
        return this._sheet;
    }

    public int getRowIndex() {
        return this._masterCell.getRowIndex();
    }

    public int getColumnIndex() {
        return this._masterCell.getColumnIndex();
    }
}
