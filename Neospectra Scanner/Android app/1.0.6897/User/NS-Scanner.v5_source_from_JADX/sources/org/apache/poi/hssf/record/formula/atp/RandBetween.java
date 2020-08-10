package org.apache.poi.hssf.record.formula.atp;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.p009ss.formula.OperationEvaluationContext;

final class RandBetween implements FreeRefFunction {
    public static final FreeRefFunction instance = new RandBetween();

    private RandBetween() {
    }

    public ValueEval evaluate(ValueEval[] args, OperationEvaluationContext ec) {
        if (args.length != 2) {
            return ErrorEval.VALUE_INVALID;
        }
        try {
            double bottom = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[0], ec.getRowIndex(), ec.getColumnIndex()));
            try {
                double top = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(args[1], ec.getRowIndex(), ec.getColumnIndex()));
                if (bottom > top) {
                    return ErrorEval.NUM_ERROR;
                }
                double bottom2 = Math.ceil(bottom);
                double top2 = Math.floor(top);
                if (bottom2 > top2) {
                    top2 = bottom2;
                }
                return new NumberEval(((double) ((int) (Math.random() * ((top2 - bottom2) + 1.0d)))) + bottom2);
            } catch (EvaluationException e) {
                return ErrorEval.VALUE_INVALID;
            }
        } catch (EvaluationException e2) {
            return ErrorEval.VALUE_INVALID;
        }
    }
}
