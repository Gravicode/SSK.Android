package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.NumericValueEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.LookupUtils.CompareResult;
import org.apache.poi.hssf.record.formula.functions.LookupUtils.LookupValueComparer;
import org.apache.poi.hssf.record.formula.functions.LookupUtils.ValueVector;
import org.apache.poi.p009ss.formula.TwoDEval;

public final class Match extends Var2or3ArgFunction {

    private static final class SingleValueVector implements ValueVector {
        private final ValueEval _value;

        public SingleValueVector(ValueEval value) {
            this._value = value;
        }

        public ValueEval getItem(int index) {
            if (index == 0) {
                return this._value;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid index (");
            sb.append(index);
            sb.append(") only zero is allowed");
            throw new RuntimeException(sb.toString());
        }

        public int getSize() {
            return 1;
        }
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        return eval(srcRowIndex, srcColumnIndex, arg0, arg1, 1.0d);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        try {
            return eval(srcRowIndex, srcColumnIndex, arg0, arg1, evaluateMatchTypeArg(arg2, srcRowIndex, srcColumnIndex));
        } catch (EvaluationException e) {
            return ErrorEval.REF_INVALID;
        }
    }

    private static ValueEval eval(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, double match_type) {
        boolean z = false;
        boolean matchExact = match_type == 0.0d;
        if (match_type > 0.0d) {
            z = true;
        }
        try {
            return new NumberEval((double) (findIndexOfValue(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), evaluateLookupRange(arg1), matchExact, z) + 1));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static ValueVector evaluateLookupRange(ValueEval eval) throws EvaluationException {
        if (eval instanceof RefEval) {
            return new SingleValueVector(((RefEval) eval).getInnerValueEval());
        }
        if (eval instanceof TwoDEval) {
            ValueVector result = LookupUtils.createVector((TwoDEval) eval);
            if (result != null) {
                return result;
            }
            throw new EvaluationException(ErrorEval.f854NA);
        } else if (eval instanceof NumericValueEval) {
            throw new EvaluationException(ErrorEval.f854NA);
        } else if (!(eval instanceof StringEval)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected eval type (");
            sb.append(eval.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        } else if (OperandResolver.parseDouble(((StringEval) eval).getStringValue()) == null) {
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        } else {
            throw new EvaluationException(ErrorEval.f854NA);
        }
    }

    private static double evaluateMatchTypeArg(ValueEval arg, int srcCellRow, int srcCellCol) throws EvaluationException {
        ValueEval match_type = OperandResolver.getSingleValue(arg, srcCellRow, srcCellCol);
        if (match_type instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) match_type);
        } else if (match_type instanceof NumericValueEval) {
            return ((NumericValueEval) match_type).getNumberValue();
        } else {
            if (match_type instanceof StringEval) {
                Double d = OperandResolver.parseDouble(((StringEval) match_type).getStringValue());
                if (d != null) {
                    return d.doubleValue();
                }
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected match_type type (");
            sb.append(match_type.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
    }

    private static int findIndexOfValue(ValueEval lookupValue, ValueVector lookupRange, boolean matchExact, boolean findLargestLessThanOrEqual) throws EvaluationException {
        LookupValueComparer lookupComparer = createLookupComparer(lookupValue, matchExact);
        int size = lookupRange.getSize();
        int i = 0;
        if (matchExact) {
            while (i < size) {
                if (lookupComparer.compareTo(lookupRange.getItem(i)).isEqual()) {
                    return i;
                }
                i++;
            }
            throw new EvaluationException(ErrorEval.f854NA);
        } else if (findLargestLessThanOrEqual) {
            for (int i2 = size - 1; i2 >= 0; i2--) {
                CompareResult cmp = lookupComparer.compareTo(lookupRange.getItem(i2));
                if (!cmp.isTypeMismatch() && !cmp.isLessThan()) {
                    return i2;
                }
            }
            throw new EvaluationException(ErrorEval.f854NA);
        } else {
            while (i < size) {
                CompareResult cmp2 = lookupComparer.compareTo(lookupRange.getItem(i));
                if (cmp2.isEqual()) {
                    return i;
                }
                if (!cmp2.isGreaterThan()) {
                    i++;
                } else if (i >= 1) {
                    return i - 1;
                } else {
                    throw new EvaluationException(ErrorEval.f854NA);
                }
            }
            throw new EvaluationException(ErrorEval.f854NA);
        }
    }

    private static LookupValueComparer createLookupComparer(ValueEval lookupValue, boolean matchExact) {
        if (matchExact && (lookupValue instanceof StringEval)) {
            String stringValue = ((StringEval) lookupValue).getStringValue();
            if (isLookupValueWild(stringValue)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Wildcard lookup values '");
                sb.append(stringValue);
                sb.append("' not supported yet");
                throw new RuntimeException(sb.toString());
            }
        }
        return LookupUtils.createLookupComparer(lookupValue);
    }

    private static boolean isLookupValueWild(String stringValue) {
        if (stringValue.indexOf(63) >= 0 || stringValue.indexOf(42) >= 0) {
            return true;
        }
        return false;
    }
}
