package org.apache.poi.p009ss.usermodel;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;

/* renamed from: org.apache.poi.ss.usermodel.CellValue */
public final class CellValue {
    public static final CellValue FALSE;
    public static final CellValue TRUE;
    private final boolean _booleanValue;
    private final int _cellType;
    private final int _errorCode;
    private final double _numberValue;
    private final String _textValue;

    static {
        CellValue cellValue = new CellValue(4, 0.0d, true, null, 0);
        TRUE = cellValue;
        CellValue cellValue2 = new CellValue(4, 0.0d, false, null, 0);
        FALSE = cellValue2;
    }

    private CellValue(int cellType, double numberValue, boolean booleanValue, String textValue, int errorCode) {
        this._cellType = cellType;
        this._numberValue = numberValue;
        this._booleanValue = booleanValue;
        this._textValue = textValue;
        this._errorCode = errorCode;
    }

    public CellValue(double numberValue) {
        this(0, numberValue, false, null, 0);
    }

    public static CellValue valueOf(boolean booleanValue) {
        return booleanValue ? TRUE : FALSE;
    }

    public CellValue(String stringValue) {
        this(1, 0.0d, false, stringValue, 0);
    }

    public static CellValue getError(int errorCode) {
        CellValue cellValue = new CellValue(5, 0.0d, false, null, errorCode);
        return cellValue;
    }

    public boolean getBooleanValue() {
        return this._booleanValue;
    }

    public double getNumberValue() {
        return this._numberValue;
    }

    public String getStringValue() {
        return this._textValue;
    }

    public int getCellType() {
        return this._cellType;
    }

    public byte getErrorValue() {
        return (byte) this._errorCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(formatAsString());
        sb.append("]");
        return sb.toString();
    }

    public String formatAsString() {
        switch (this._cellType) {
            case 0:
                return String.valueOf(this._numberValue);
            case 1:
                StringBuilder sb = new StringBuilder();
                sb.append('\"');
                sb.append(this._textValue);
                sb.append('\"');
                return sb.toString();
            case 4:
                return this._booleanValue ? "TRUE" : "FALSE";
            case 5:
                return ErrorEval.getText(this._errorCode);
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("<error unexpected cell type ");
                sb2.append(this._cellType);
                sb2.append(">");
                return sb2.toString();
        }
    }
}
