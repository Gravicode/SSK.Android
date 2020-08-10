package org.apache.poi.hssf.record.formula.functions;

import java.util.regex.Pattern;
import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.CountUtils.I_MatchPredicate;
import org.apache.poi.p009ss.formula.TwoDEval;
import org.apache.poi.p009ss.usermodel.ErrorConstants;

public final class Countif extends Fixed2ArgFunction {

    private static final class BooleanMatcher extends MatcherBase {
        private final int _value;

        public BooleanMatcher(boolean value, CmpOp operator) {
            super(operator);
            this._value = boolToInt(value);
        }

        /* access modifiers changed from: protected */
        public String getValueText() {
            return this._value == 1 ? "TRUE" : "FALSE";
        }

        private static int boolToInt(boolean value) {
            return value;
        }

        public boolean matches(ValueEval x) {
            if (!(x instanceof StringEval) && (x instanceof BoolEval)) {
                return evaluate(boolToInt(((BoolEval) x).getBooleanValue()) - this._value);
            }
            return false;
        }
    }

    private static final class CmpOp {

        /* renamed from: EQ */
        public static final int f858EQ = 1;

        /* renamed from: GE */
        public static final int f859GE = 6;

        /* renamed from: GT */
        public static final int f860GT = 5;

        /* renamed from: LE */
        public static final int f861LE = 3;

        /* renamed from: LT */
        public static final int f862LT = 4;

        /* renamed from: NE */
        public static final int f863NE = 2;
        public static final int NONE = 0;
        public static final CmpOp OP_EQ = m72op("=", 1);
        public static final CmpOp OP_GE = m72op(">=", 6);
        public static final CmpOp OP_GT = m72op(">", 5);
        public static final CmpOp OP_LE = m72op("<=", 3);
        public static final CmpOp OP_LT = m72op("<", 4);
        public static final CmpOp OP_NE = m72op("<>", 2);
        public static final CmpOp OP_NONE = m72op("", 0);
        private final int _code;
        private final String _representation;

        /* renamed from: op */
        private static CmpOp m72op(String rep, int code) {
            return new CmpOp(rep, code);
        }

        private CmpOp(String representation, int code) {
            this._representation = representation;
            this._code = code;
        }

        public int getLength() {
            return this._representation.length();
        }

        public int getCode() {
            return this._code;
        }

        public static CmpOp getOperator(String value) {
            int len = value.length();
            if (len < 1) {
                return OP_NONE;
            }
            switch (value.charAt(0)) {
                case '<':
                    if (len > 1) {
                        switch (value.charAt(1)) {
                            case '=':
                                return OP_LE;
                            case '>':
                                return OP_NE;
                        }
                    }
                    return OP_LT;
                case '=':
                    return OP_EQ;
                case '>':
                    if (len <= 1 || value.charAt(1) != '=') {
                        return OP_GT;
                    }
                    return OP_GE;
                default:
                    return OP_NONE;
            }
        }

        public boolean evaluate(boolean cmpResult) {
            switch (this._code) {
                case 0:
                case 1:
                    return cmpResult;
                case 2:
                    return !cmpResult;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot call boolean evaluate on non-equality operator '");
                    sb.append(this._representation);
                    sb.append("'");
                    throw new RuntimeException(sb.toString());
            }
        }

        public boolean evaluate(int cmpResult) {
            boolean z = false;
            switch (this._code) {
                case 0:
                case 1:
                    if (cmpResult == 0) {
                        z = true;
                    }
                    return z;
                case 2:
                    if (cmpResult != 0) {
                        z = true;
                    }
                    return z;
                case 3:
                    if (cmpResult <= 0) {
                        z = true;
                    }
                    return z;
                case 4:
                    if (cmpResult < 0) {
                        z = true;
                    }
                    return z;
                case 5:
                    if (cmpResult > 0) {
                        z = true;
                    }
                    return z;
                case 6:
                    if (cmpResult <= 0) {
                        z = true;
                    }
                    return z;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot call boolean evaluate on non-equality operator '");
                    sb.append(this._representation);
                    sb.append("'");
                    throw new RuntimeException(sb.toString());
            }
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [");
            sb.append(this._representation);
            sb.append("]");
            return sb.toString();
        }

        public String getRepresentation() {
            return this._representation;
        }
    }

    private static final class ErrorMatcher extends MatcherBase {
        private final int _value;

        public ErrorMatcher(int errorCode, CmpOp operator) {
            super(operator);
            this._value = errorCode;
        }

        /* access modifiers changed from: protected */
        public String getValueText() {
            return ErrorConstants.getText(this._value);
        }

        public boolean matches(ValueEval x) {
            if (x instanceof ErrorEval) {
                return evaluate(((ErrorEval) x).getErrorCode() - this._value);
            }
            return false;
        }
    }

    private static abstract class MatcherBase implements I_MatchPredicate {
        private final CmpOp _operator;

        /* access modifiers changed from: protected */
        public abstract String getValueText();

        MatcherBase(CmpOp operator) {
            this._operator = operator;
        }

        /* access modifiers changed from: protected */
        public final int getCode() {
            return this._operator.getCode();
        }

        /* access modifiers changed from: protected */
        public final boolean evaluate(int cmpResult) {
            return this._operator.evaluate(cmpResult);
        }

        /* access modifiers changed from: protected */
        public final boolean evaluate(boolean cmpResult) {
            return this._operator.evaluate(cmpResult);
        }

        public final String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append(" [");
            sb.append(this._operator.getRepresentation());
            sb.append(getValueText());
            sb.append("]");
            return sb.toString();
        }
    }

    private static final class NumberMatcher extends MatcherBase {
        private final double _value;

        public NumberMatcher(double value, CmpOp operator) {
            super(operator);
            this._value = value;
        }

        /* access modifiers changed from: protected */
        public String getValueText() {
            return String.valueOf(this._value);
        }

        public boolean matches(ValueEval x) {
            boolean z = false;
            if (x instanceof StringEval) {
                switch (getCode()) {
                    case 0:
                    case 1:
                        Double val = OperandResolver.parseDouble(((StringEval) x).getStringValue());
                        if (val == null) {
                            return false;
                        }
                        if (this._value == val.doubleValue()) {
                            z = true;
                        }
                        return z;
                    case 2:
                        return true;
                    default:
                        return false;
                }
            } else if (x instanceof NumberEval) {
                return evaluate(Double.compare(((NumberEval) x).getNumberValue(), this._value));
            } else {
                return false;
            }
        }
    }

    private static final class StringMatcher extends MatcherBase {
        private final Pattern _pattern;
        private final String _value;

        public StringMatcher(String value, CmpOp operator) {
            super(operator);
            this._value = value;
            switch (operator.getCode()) {
                case 0:
                case 1:
                case 2:
                    this._pattern = getWildCardPattern(value);
                    return;
                default:
                    this._pattern = null;
                    return;
            }
        }

        /* access modifiers changed from: protected */
        public String getValueText() {
            if (this._pattern == null) {
                return this._value;
            }
            return this._pattern.pattern();
        }

        public boolean matches(ValueEval x) {
            boolean z = true;
            if (x instanceof BlankEval) {
                switch (getCode()) {
                    case 0:
                    case 1:
                        if (this._value.length() != 0) {
                            z = false;
                        }
                        return z;
                    default:
                        return false;
                }
            } else if (!(x instanceof StringEval)) {
                return false;
            } else {
                String testedValue = ((StringEval) x).getStringValue();
                if (testedValue.length() < 1 && this._value.length() < 1) {
                    switch (getCode()) {
                        case 0:
                            return true;
                        case 1:
                            return false;
                        case 2:
                            return true;
                        default:
                            return false;
                    }
                } else if (this._pattern != null) {
                    return evaluate(this._pattern.matcher(testedValue).matches());
                } else {
                    return evaluate(testedValue.compareTo(this._value));
                }
            }
        }

        private static Pattern getWildCardPattern(String value) {
            int len = value.length();
            StringBuffer sb = new StringBuffer(len);
            boolean hasWildCard = false;
            int i = 0;
            while (i < len) {
                char ch = value.charAt(i);
                if (!(ch == '$' || ch == '.')) {
                    if (ch == '?') {
                        hasWildCard = true;
                        sb.append('.');
                    } else if (ch != '[') {
                        if (ch != '~') {
                            switch (ch) {
                                case '(':
                                case ')':
                                    break;
                                case '*':
                                    hasWildCard = true;
                                    sb.append(".*");
                                    continue;
                                default:
                                    switch (ch) {
                                        case ']':
                                        case '^':
                                            break;
                                        default:
                                            sb.append(ch);
                                            continue;
                                            continue;
                                    }
                            }
                        } else {
                            if (i + 1 < len) {
                                char ch2 = value.charAt(i + 1);
                                if (ch2 == '*' || ch2 == '?') {
                                    hasWildCard = true;
                                    sb.append('[');
                                    sb.append(ch2);
                                    sb.append(']');
                                    i++;
                                }
                            }
                            sb.append('~');
                        }
                    }
                    i++;
                }
                sb.append("\\");
                sb.append(ch);
                i++;
            }
            if (hasWildCard) {
                return Pattern.compile(sb.toString());
            }
            return null;
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        I_MatchPredicate mp = createCriteriaPredicate(arg1, srcRowIndex, srcColumnIndex);
        if (mp == null) {
            return NumberEval.ZERO;
        }
        return new NumberEval(countMatchingCellsInArea(arg0, mp));
    }

    private double countMatchingCellsInArea(ValueEval rangeArg, I_MatchPredicate criteriaPredicate) {
        if (rangeArg instanceof RefEval) {
            return (double) CountUtils.countMatchingCell((RefEval) rangeArg, criteriaPredicate);
        }
        if (rangeArg instanceof TwoDEval) {
            return (double) CountUtils.countMatchingCellsInArea((TwoDEval) rangeArg, criteriaPredicate);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Bad range arg type (");
        sb.append(rangeArg.getClass().getName());
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }

    static I_MatchPredicate createCriteriaPredicate(ValueEval arg, int srcRowIndex, int srcColumnIndex) {
        ValueEval evaluatedCriteriaArg = evaluateCriteriaArg(arg, srcRowIndex, srcColumnIndex);
        if (evaluatedCriteriaArg instanceof NumberEval) {
            return new NumberMatcher(((NumberEval) evaluatedCriteriaArg).getNumberValue(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg instanceof BoolEval) {
            return new BooleanMatcher(((BoolEval) evaluatedCriteriaArg).getBooleanValue(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg instanceof StringEval) {
            return createGeneralMatchPredicate((StringEval) evaluatedCriteriaArg);
        }
        if (evaluatedCriteriaArg instanceof ErrorEval) {
            return new ErrorMatcher(((ErrorEval) evaluatedCriteriaArg).getErrorCode(), CmpOp.OP_NONE);
        }
        if (evaluatedCriteriaArg == BlankEval.instance) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unexpected type for criteria (");
        sb.append(evaluatedCriteriaArg.getClass().getName());
        sb.append(")");
        throw new RuntimeException(sb.toString());
    }

    private static ValueEval evaluateCriteriaArg(ValueEval arg, int srcRowIndex, int srcColumnIndex) {
        try {
            return OperandResolver.getSingleValue(arg, srcRowIndex, (short) srcColumnIndex);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static I_MatchPredicate createGeneralMatchPredicate(StringEval stringEval) {
        String value = stringEval.getStringValue();
        CmpOp operator = CmpOp.getOperator(value);
        String value2 = value.substring(operator.getLength());
        Boolean booleanVal = parseBoolean(value2);
        if (booleanVal != null) {
            return new BooleanMatcher(booleanVal.booleanValue(), operator);
        }
        Double doubleVal = OperandResolver.parseDouble(value2);
        if (doubleVal != null) {
            return new NumberMatcher(doubleVal.doubleValue(), operator);
        }
        ErrorEval ee = parseError(value2);
        if (ee != null) {
            return new ErrorMatcher(ee.getErrorCode(), operator);
        }
        return new StringMatcher(value2, operator);
    }

    private static ErrorEval parseError(String value) {
        if (value.length() < 4 || value.charAt(0) != '#') {
            return null;
        }
        if (value.equals("#NULL!")) {
            return ErrorEval.NULL_INTERSECTION;
        }
        if (value.equals("#DIV/0!")) {
            return ErrorEval.DIV_ZERO;
        }
        if (value.equals("#VALUE!")) {
            return ErrorEval.VALUE_INVALID;
        }
        if (value.equals("#REF!")) {
            return ErrorEval.REF_INVALID;
        }
        if (value.equals("#NAME?")) {
            return ErrorEval.NAME_INVALID;
        }
        if (value.equals("#NUM!")) {
            return ErrorEval.NUM_ERROR;
        }
        if (value.equals("#N/A")) {
            return ErrorEval.f854NA;
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        if (r0 != 't') goto L_0x0035;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Boolean parseBoolean(java.lang.String r3) {
        /*
            int r0 = r3.length()
            r1 = 0
            r2 = 1
            if (r0 >= r2) goto L_0x0009
            return r1
        L_0x0009:
            r0 = 0
            char r0 = r3.charAt(r0)
            r2 = 70
            if (r0 == r2) goto L_0x002a
            r2 = 84
            if (r0 == r2) goto L_0x001f
            r2 = 102(0x66, float:1.43E-43)
            if (r0 == r2) goto L_0x002a
            r2 = 116(0x74, float:1.63E-43)
            if (r0 == r2) goto L_0x001f
            goto L_0x0035
        L_0x001f:
            java.lang.String r0 = "TRUE"
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x0035
            java.lang.Boolean r0 = java.lang.Boolean.TRUE
            return r0
        L_0x002a:
            java.lang.String r0 = "FALSE"
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x0035
            java.lang.Boolean r0 = java.lang.Boolean.FALSE
            return r0
        L_0x0035:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.poi.hssf.record.formula.functions.Countif.parseBoolean(java.lang.String):java.lang.Boolean");
    }
}
