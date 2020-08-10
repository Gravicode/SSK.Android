package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.CountUtils.I_MatchPredicate;
import org.apache.poi.p009ss.formula.TwoDEval;

public final class Countblank extends Fixed1ArgFunction {
    private static final I_MatchPredicate predicate = new I_MatchPredicate() {
        public boolean matches(ValueEval valueEval) {
            return valueEval == BlankEval.instance;
        }
    };

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        double result;
        if (arg0 instanceof RefEval) {
            result = (double) CountUtils.countMatchingCell((RefEval) arg0, predicate);
        } else if (arg0 instanceof TwoDEval) {
            result = (double) CountUtils.countMatchingCellsInArea((TwoDEval) arg0, predicate);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Bad range arg type (");
            sb.append(arg0.getClass().getName());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        return new NumberEval(result);
    }
}
