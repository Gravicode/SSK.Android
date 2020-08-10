package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.MissingArgEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.OperationEvaluationContext;

public final class Indirect implements FreeRefFunction {
    public static final FreeRefFunction instance = new Indirect();

    private Indirect() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        boolean isA1style;
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            String text = OperandResolver.coerceValueToString(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
            switch (args.length) {
                case 1:
                    isA1style = true;
                    break;
                case 2:
                    isA1style = evaluateBooleanArg(args[1], ec);
                    break;
                default:
                    return ErrorEval.VALUE_INVALID;
            }
            return evaluateIndirect(ec, text, isA1style);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static boolean evaluateBooleanArg(ValueEval arg, OperationEvaluationContext ec) throws EvaluationException {
        ValueEval ve = OperandResolver.getSingleValue(arg, ec.getRowIndex(), ec.getColumnIndex());
        if (ve == BlankEval.instance || ve == MissingArgEval.instance) {
            return false;
        }
        return OperandResolver.coerceValueToBoolean(ve, false).booleanValue();
    }

    private static ValueEval evaluateIndirect(OperationEvaluationContext ec, String text, boolean isA1style) {
        String sheetName;
        String workbookName;
        String workbookName2;
        String refStrPart1;
        String trim;
        int plingPos = text.lastIndexOf(33);
        if (plingPos < 0) {
            workbookName = null;
            sheetName = null;
            workbookName2 = text;
        } else {
            String[] parts = parseWorkbookAndSheetName(text.subSequence(0, plingPos));
            if (parts == null) {
                return ErrorEval.REF_INVALID;
            }
            String workbookName3 = parts[0];
            String sheetName2 = parts[1];
            workbookName2 = text.substring(plingPos + 1);
            workbookName = workbookName3;
            sheetName = sheetName2;
        }
        int colonPos = workbookName2.indexOf(58);
        if (colonPos < 0) {
            refStrPart1 = workbookName2.trim();
            trim = null;
        } else {
            refStrPart1 = workbookName2.substring(0, colonPos).trim();
            trim = workbookName2.substring(colonPos + 1).trim();
        }
        return ec.getDynamicReference(workbookName, sheetName, refStrPart1, trim, isA1style);
    }

    private static String[] parseWorkbookAndSheetName(CharSequence text) {
        String wbName;
        int rbPos;
        int lastIx = text.length() - 1;
        if (lastIx < 0 || canTrim(text)) {
            return null;
        }
        char firstChar = text.charAt(0);
        if (Character.isWhitespace(firstChar)) {
            return null;
        }
        if (firstChar == '\'') {
            if (text.charAt(lastIx) != '\'') {
                return null;
            }
            char firstChar2 = text.charAt(1);
            if (Character.isWhitespace(firstChar2)) {
                return null;
            }
            if (firstChar2 == '[') {
                int rbPos2 = text.toString().lastIndexOf(93);
                if (rbPos2 < 0) {
                    return null;
                }
                wbName = unescapeString(text.subSequence(2, rbPos2));
                if (wbName == null || canTrim(wbName)) {
                    return null;
                }
                rbPos = rbPos2 + 1;
            } else {
                wbName = null;
                rbPos = 1;
            }
            String sheetName = unescapeString(text.subSequence(rbPos, lastIx));
            if (sheetName == null) {
                return null;
            }
            return new String[]{wbName, sheetName};
        } else if (firstChar == '[') {
            int rbPos3 = text.toString().lastIndexOf(93);
            if (rbPos3 < 0) {
                return null;
            }
            CharSequence wbName2 = text.subSequence(1, rbPos3);
            if (canTrim(wbName2)) {
                return null;
            }
            CharSequence sheetName2 = text.subSequence(rbPos3 + 1, text.length());
            if (canTrim(sheetName2)) {
                return null;
            }
            return new String[]{wbName2.toString(), sheetName2.toString()};
        } else {
            return new String[]{null, text.toString()};
        }
    }

    private static String unescapeString(CharSequence text) {
        int len = text.length();
        StringBuilder sb = new StringBuilder(len);
        int i = 0;
        while (i < len) {
            char ch = text.charAt(i);
            if (ch == '\'') {
                i++;
                if (i >= len) {
                    return null;
                }
                ch = text.charAt(i);
                if (ch != '\'') {
                    return null;
                }
            }
            sb.append(ch);
            i++;
        }
        return sb.toString();
    }

    private static boolean canTrim(CharSequence text) {
        int lastIx = text.length() - 1;
        if (lastIx < 0) {
            return false;
        }
        if (!Character.isWhitespace(text.charAt(0)) && !Character.isWhitespace(text.charAt(lastIx))) {
            return false;
        }
        return true;
    }
}
