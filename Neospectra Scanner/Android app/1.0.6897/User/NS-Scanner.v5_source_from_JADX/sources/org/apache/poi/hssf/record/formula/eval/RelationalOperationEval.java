package org.apache.poi.hssf.record.formula.eval;

import org.apache.poi.hssf.record.formula.functions.Fixed2ArgFunction;
import org.apache.poi.hssf.record.formula.functions.Function;
import org.apache.poi.p009ss.util.NumberComparer;

public abstract class RelationalOperationEval extends Fixed2ArgFunction {
    public static final Function EqualEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult == 0;
        }
    };
    public static final Function GreaterEqualEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult >= 0;
        }
    };
    public static final Function GreaterThanEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult > 0;
        }
    };
    public static final Function LessEqualEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult <= 0;
        }
    };
    public static final Function LessThanEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult < 0;
        }
    };
    public static final Function NotEqualEval = new RelationalOperationEval() {
        /* access modifiers changed from: protected */
        public boolean convertComparisonResult(int cmpResult) {
            return cmpResult != 0;
        }
    };

    /* access modifiers changed from: protected */
    public abstract boolean convertComparisonResult(int i);

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            try {
                return BoolEval.valueOf(convertComparisonResult(doCompare(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex))));
            } catch (EvaluationException e) {
                e = e;
                return e.getErrorEval();
            }
        } catch (EvaluationException e2) {
            e = e2;
            return e.getErrorEval();
        }
    }

    private static int doCompare(ValueEval va, ValueEval vb) {
        if (va == BlankEval.instance) {
            return compareBlank(vb);
        }
        if (vb == BlankEval.instance) {
            return -compareBlank(va);
        }
        int i = -1;
        if (va instanceof BoolEval) {
            if (!(vb instanceof BoolEval)) {
                return 1;
            }
            BoolEval bA = (BoolEval) va;
            if (bA.getBooleanValue() == ((BoolEval) vb).getBooleanValue()) {
                return 0;
            }
            if (bA.getBooleanValue()) {
                i = 1;
            }
            return i;
        } else if (vb instanceof BoolEval) {
            return -1;
        } else {
            if (va instanceof StringEval) {
                if (!(vb instanceof StringEval)) {
                    return 1;
                }
                return ((StringEval) va).getStringValue().compareToIgnoreCase(((StringEval) vb).getStringValue());
            } else if (vb instanceof StringEval) {
                return -1;
            } else {
                if (!(va instanceof NumberEval) || !(vb instanceof NumberEval)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bad operand types (");
                    sb.append(va.getClass().getName());
                    sb.append("), (");
                    sb.append(vb.getClass().getName());
                    sb.append(")");
                    throw new IllegalArgumentException(sb.toString());
                }
                return NumberComparer.compare(((NumberEval) va).getNumberValue(), ((NumberEval) vb).getNumberValue());
            }
        }
    }

    private static int compareBlank(ValueEval v) {
        int i = 0;
        if (v == BlankEval.instance) {
            return 0;
        }
        if (v instanceof BoolEval) {
            if (((BoolEval) v).getBooleanValue()) {
                i = -1;
            }
            return i;
        } else if (v instanceof NumberEval) {
            return NumberComparer.compare(0.0d, ((NumberEval) v).getNumberValue());
        } else {
            if (v instanceof StringEval) {
                if (((StringEval) v).getStringValue().length() >= 1) {
                    i = -1;
                }
                return i;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("bad value class (");
            sb.append(v.getClass().getName());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
