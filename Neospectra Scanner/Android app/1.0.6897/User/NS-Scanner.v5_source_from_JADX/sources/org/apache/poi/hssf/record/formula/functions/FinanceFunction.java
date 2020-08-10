package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

public abstract class FinanceFunction implements Function3Arg, Function4Arg {
    private static final ValueEval DEFAULT_ARG3 = NumberEval.ZERO;
    private static final ValueEval DEFAULT_ARG4 = BoolEval.FALSE;

    /* renamed from: FV */
    public static final Function f864FV = new FinanceFunction() {
        /* access modifiers changed from: protected */
        public double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.m73fv(rate, arg1, arg2, arg3, type);
        }
    };
    public static final Function NPER = new FinanceFunction() {
        /* access modifiers changed from: protected */
        public double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.nper(rate, arg1, arg2, arg3, type);
        }
    };
    public static final Function PMT = new FinanceFunction() {
        /* access modifiers changed from: protected */
        public double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.pmt(rate, arg1, arg2, arg3, type);
        }
    };

    /* renamed from: PV */
    public static final Function f865PV = new FinanceFunction() {
        /* access modifiers changed from: protected */
        public double evaluate(double rate, double arg1, double arg2, double arg3, boolean type) {
            return FinanceLib.m74pv(rate, arg1, arg2, arg3, type);
        }
    };

    /* access modifiers changed from: protected */
    public abstract double evaluate(double d, double d2, double d3, double d4, boolean z) throws EvaluationException;

    protected FinanceFunction() {
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, DEFAULT_ARG3);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3) {
        return evaluate(srcRowIndex, srcColumnIndex, arg0, arg1, arg2, arg3, DEFAULT_ARG4);
    }

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2, ValueEval arg3, ValueEval arg4) {
        double result;
        int i = srcRowIndex;
        int i2 = srcColumnIndex;
        try {
            try {
            } catch (EvaluationException e) {
                e = e;
                return e.getErrorEval();
            }
            try {
                result = evaluate(NumericFunction.singleOperandEvaluate(arg0, i, i2), NumericFunction.singleOperandEvaluate(arg1, i, i2), NumericFunction.singleOperandEvaluate(arg2, i, i2), NumericFunction.singleOperandEvaluate(arg3, i, i2), NumericFunction.singleOperandEvaluate(arg4, i, i2) != 0.0d);
            } catch (EvaluationException e2) {
                e = e2;
                return e.getErrorEval();
            }
            try {
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e3) {
                e = e3;
                double d = result;
                return e.getErrorEval();
            }
        } catch (EvaluationException e4) {
            e = e4;
            ValueEval valueEval = arg4;
            return e.getErrorEval();
        }
    }

    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        ValueEval[] valueEvalArr = args;
        switch (valueEvalArr.length) {
            case 3:
                return evaluate(srcRowIndex, srcColumnIndex, valueEvalArr[0], valueEvalArr[1], valueEvalArr[2], DEFAULT_ARG3, DEFAULT_ARG4);
            case 4:
                return evaluate(srcRowIndex, srcColumnIndex, valueEvalArr[0], valueEvalArr[1], valueEvalArr[2], valueEvalArr[3], DEFAULT_ARG4);
            case 5:
                return evaluate(srcRowIndex, srcColumnIndex, valueEvalArr[0], valueEvalArr[1], valueEvalArr[2], valueEvalArr[3], valueEvalArr[4]);
            default:
                return ErrorEval.VALUE_INVALID;
        }
    }

    /* access modifiers changed from: protected */
    public double evaluate(double[] ds) throws EvaluationException {
        double arg3 = 0.0d;
        double arg4 = 0.0d;
        switch (ds.length) {
            case 3:
                break;
            case 4:
                break;
            case 5:
                arg4 = ds[4];
                break;
            default:
                throw new IllegalStateException("Wrong number of arguments");
        }
        arg3 = ds[3];
        return evaluate(ds[0], ds[1], ds[2], arg3, arg4 != 0.0d);
    }
}
