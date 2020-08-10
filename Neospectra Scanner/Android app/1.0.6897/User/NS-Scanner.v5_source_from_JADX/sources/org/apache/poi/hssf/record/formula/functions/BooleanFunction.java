package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.TwoDEval;

public abstract class BooleanFunction implements Function {
    public static final Function AND = new BooleanFunction() {
        /* access modifiers changed from: protected */
        public boolean getInitialResultValue() {
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
            return cumulativeResult && currentValue;
        }
    };
    public static final Function FALSE = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return BoolEval.FALSE;
        }
    };
    public static final Function NOT = new Fixed1ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            try {
                boolean z = false;
                Boolean b = OperandResolver.coerceValueToBoolean(OperandResolver.getSingleValue(arg0, srcRowIndex, srcColumnIndex), false);
                if (!(b == null ? false : b.booleanValue())) {
                    z = true;
                }
                return BoolEval.valueOf(z);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    };

    /* renamed from: OR */
    public static final Function f857OR = new BooleanFunction() {
        /* access modifiers changed from: protected */
        public boolean getInitialResultValue() {
            return false;
        }

        /* access modifiers changed from: protected */
        public boolean partialEvaluate(boolean cumulativeResult, boolean currentValue) {
            return cumulativeResult || currentValue;
        }
    };
    public static final Function TRUE = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return BoolEval.TRUE;
        }
    };

    /* access modifiers changed from: protected */
    public abstract boolean getInitialResultValue();

    /* access modifiers changed from: protected */
    public abstract boolean partialEvaluate(boolean z, boolean z2);

    public final ValueEval evaluate(ValueEval[] args, int srcRow, int srcCol) {
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            return BoolEval.valueOf(calculate(args));
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private boolean calculate(ValueEval[] args) throws EvaluationException {
        Boolean tempVe;
        ValueEval[] valueEvalArr = args;
        boolean result = getInitialResultValue();
        boolean result2 = false;
        for (ValueEval arg : valueEvalArr) {
            if (arg instanceof TwoDEval) {
                TwoDEval ae = (TwoDEval) arg;
                int height = ae.getHeight();
                int width = ae.getWidth();
                boolean result3 = result2;
                boolean atleastOneNonBlank = result;
                int rrIx = 0;
                while (rrIx < height) {
                    boolean atleastOneNonBlank2 = result3;
                    boolean result4 = atleastOneNonBlank;
                    for (int rcIx = 0; rcIx < width; rcIx++) {
                        Boolean tempVe2 = OperandResolver.coerceValueToBoolean(ae.getValue(rrIx, rcIx), true);
                        if (tempVe2 != null) {
                            result4 = partialEvaluate(result4, tempVe2.booleanValue());
                            atleastOneNonBlank2 = true;
                        }
                    }
                    rrIx++;
                    atleastOneNonBlank = result4;
                    result3 = atleastOneNonBlank2;
                }
                result = atleastOneNonBlank;
                result2 = result3;
            } else {
                if (arg instanceof RefEval) {
                    tempVe = OperandResolver.coerceValueToBoolean(((RefEval) arg).getInnerValueEval(), true);
                } else {
                    tempVe = OperandResolver.coerceValueToBoolean(arg, false);
                }
                if (tempVe != null) {
                    result = partialEvaluate(result, tempVe.booleanValue());
                    result2 = true;
                }
            }
        }
        if (result2) {
            return result;
        }
        throw new EvaluationException(ErrorEval.VALUE_INVALID);
    }
}
