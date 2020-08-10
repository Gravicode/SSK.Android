package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.AreaEval;
import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.NumericValueEval;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.TwoDEval;

public final class Sumproduct implements Function {
    public ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        if (args.length < 1) {
            return ErrorEval.VALUE_INVALID;
        }
        ValueEval firstArg = args[0];
        try {
            if (firstArg instanceof NumericValueEval) {
                return evaluateSingleProduct(args);
            }
            if (firstArg instanceof RefEval) {
                return evaluateSingleProduct(args);
            }
            if (firstArg instanceof TwoDEval) {
                TwoDEval ae = (TwoDEval) firstArg;
                if (!ae.isRow() || !ae.isColumn()) {
                    return evaluateAreaSumProduct(args);
                }
                return evaluateSingleProduct(args);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid arg type for SUMPRODUCT: (");
            sb.append(firstArg.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private static ValueEval evaluateSingleProduct(ValueEval[] evalArgs) throws EvaluationException {
        double term = 1.0d;
        for (ValueEval scalarValue : evalArgs) {
            term *= getScalarValue(scalarValue);
        }
        return new NumberEval(term);
    }

    private static double getScalarValue(ValueEval arg) throws EvaluationException {
        ValueEval eval;
        if (arg instanceof RefEval) {
            eval = ((RefEval) arg).getInnerValueEval();
        } else {
            eval = arg;
        }
        if (eval == null) {
            throw new RuntimeException("parameter may not be null");
        }
        if (eval instanceof AreaEval) {
            AreaEval ae = (AreaEval) eval;
            if (!ae.isColumn() || !ae.isRow()) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
            eval = ae.getRelativeValue(0, 0);
        }
        return getProductTerm(eval, true);
    }

    private static ValueEval evaluateAreaSumProduct(ValueEval[] evalArgs) throws EvaluationException {
        int maxN = evalArgs.length;
        TwoDEval[] args = new TwoDEval[maxN];
        try {
            System.arraycopy(evalArgs, 0, args, 0, maxN);
            TwoDEval firstArg = args[0];
            int height = firstArg.getHeight();
            int width = firstArg.getWidth();
            if (!areasAllSameSize(args, height, width)) {
                for (int i = 1; i < args.length; i++) {
                    throwFirstError(args[i]);
                }
                return ErrorEval.VALUE_INVALID;
            }
            double acc = 0.0d;
            int rrIx = 0;
            while (rrIx < height) {
                double acc2 = acc;
                for (int rcIx = 0; rcIx < width; rcIx++) {
                    double term = 1.0d;
                    for (int n = 0; n < maxN; n++) {
                        term *= getProductTerm(args[n].getValue(rrIx, rcIx), false);
                    }
                    acc2 += term;
                }
                rrIx++;
                acc = acc2;
            }
            return new NumberEval(acc);
        } catch (ArrayStoreException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }

    private static void throwFirstError(TwoDEval areaEval) throws EvaluationException {
        int height = areaEval.getHeight();
        int width = areaEval.getWidth();
        for (int rrIx = 0; rrIx < height; rrIx++) {
            for (int rcIx = 0; rcIx < width; rcIx++) {
                ValueEval ve = areaEval.getValue(rrIx, rcIx);
                if (ve instanceof ErrorEval) {
                    throw new EvaluationException((ErrorEval) ve);
                }
            }
        }
    }

    private static boolean areasAllSameSize(TwoDEval[] args, int height, int width) {
        for (TwoDEval areaEval : args) {
            if (areaEval.getHeight() != height || areaEval.getWidth() != width) {
                return false;
            }
        }
        return true;
    }

    private static double getProductTerm(ValueEval ve, boolean isScalarProduct) throws EvaluationException {
        if ((ve instanceof BlankEval) || ve == null) {
            if (!isScalarProduct) {
                return 0.0d;
            }
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        } else if (ve instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) ve);
        } else if (ve instanceof StringEval) {
            if (!isScalarProduct) {
                return 0.0d;
            }
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        } else if (ve instanceof NumericValueEval) {
            return ((NumericValueEval) ve).getNumberValue();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value eval class (");
            sb.append(ve.getClass().getName());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
    }
}
