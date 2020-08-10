package org.apache.poi.hssf.record.formula.eval;

import org.apache.poi.hssf.usermodel.HSSFErrorConstants;

public final class ErrorEval implements ValueEval {
    public static final ErrorEval CIRCULAR_REF_ERROR = new ErrorEval(CIRCULAR_REF_ERROR_CODE);
    private static final int CIRCULAR_REF_ERROR_CODE = -60;
    public static final ErrorEval DIV_ZERO = new ErrorEval(7);

    /* renamed from: EC */
    private static final HSSFErrorConstants f853EC = null;
    private static final int FUNCTION_NOT_IMPLEMENTED_CODE = -30;

    /* renamed from: NA */
    public static final ErrorEval f854NA = new ErrorEval(42);
    public static final ErrorEval NAME_INVALID = new ErrorEval(29);
    public static final ErrorEval NULL_INTERSECTION = new ErrorEval(0);
    public static final ErrorEval NUM_ERROR = new ErrorEval(36);
    public static final ErrorEval REF_INVALID = new ErrorEval(23);
    public static final ErrorEval VALUE_INVALID = new ErrorEval(15);
    private int _errorCode;

    static {
        HSSFErrorConstants hSSFErrorConstants = f853EC;
        HSSFErrorConstants hSSFErrorConstants2 = f853EC;
        HSSFErrorConstants hSSFErrorConstants3 = f853EC;
        HSSFErrorConstants hSSFErrorConstants4 = f853EC;
        HSSFErrorConstants hSSFErrorConstants5 = f853EC;
        HSSFErrorConstants hSSFErrorConstants6 = f853EC;
        HSSFErrorConstants hSSFErrorConstants7 = f853EC;
    }

    public static ErrorEval valueOf(int errorCode) {
        if (errorCode == CIRCULAR_REF_ERROR_CODE) {
            return CIRCULAR_REF_ERROR;
        }
        if (errorCode == 0) {
            return NULL_INTERSECTION;
        }
        if (errorCode == 7) {
            return DIV_ZERO;
        }
        if (errorCode == 15) {
            return VALUE_INVALID;
        }
        if (errorCode == 23) {
            return REF_INVALID;
        }
        if (errorCode == 29) {
            return NAME_INVALID;
        }
        if (errorCode == 36) {
            return NUM_ERROR;
        }
        if (errorCode == 42) {
            return f854NA;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected error code (");
        sb.append(errorCode);
        sb.append(")");
        throw new RuntimeException(sb.toString());
    }

    public static String getText(int errorCode) {
        if (HSSFErrorConstants.isValidCode(errorCode)) {
            return HSSFErrorConstants.getText(errorCode);
        }
        if (errorCode == CIRCULAR_REF_ERROR_CODE) {
            return "~CIRCULAR~REF~";
        }
        if (errorCode == FUNCTION_NOT_IMPLEMENTED_CODE) {
            return "~FUNCTION~NOT~IMPLEMENTED~";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("~non~std~err(");
        sb.append(errorCode);
        sb.append(")~");
        return sb.toString();
    }

    private ErrorEval(int errorCode) {
        this._errorCode = errorCode;
    }

    public int getErrorCode() {
        return this._errorCode;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(64);
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append(getText(this._errorCode));
        sb.append("]");
        return sb.toString();
    }
}
