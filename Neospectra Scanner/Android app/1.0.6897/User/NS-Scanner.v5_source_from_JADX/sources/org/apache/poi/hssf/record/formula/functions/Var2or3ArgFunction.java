package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

abstract class Var2or3ArgFunction implements Function2Arg, Function3Arg {
    Var2or3ArgFunction() {
    }

    public final ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        switch (args.length) {
            case 2:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1]);
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, args[0], args[1], args[2]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }
}
