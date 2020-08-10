package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.ValueEval;

public abstract class NumericFunction implements Function {
    public static final Function ABS = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.abs(d);
        }
    };
    public static final Function ACOS = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.acos(d);
        }
    };
    public static final Function ACOSH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.acosh(d);
        }
    };
    public static final Function ASIN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.asin(d);
        }
    };
    public static final Function ASINH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.asinh(d);
        }
    };
    public static final Function ATAN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.atan(d);
        }
    };
    public static final Function ATAN2 = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) throws EvaluationException {
            if (d0 != 0.0d || d1 != 0.0d) {
                return Math.atan2(d1, d0);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    };
    public static final Function ATANH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.atanh(d);
        }
    };
    public static final Function CEILING = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) {
            return MathX.ceiling(d0, d1);
        }
    };
    public static final Function COMBIN = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) throws EvaluationException {
            if (d0 <= 2.147483647E9d && d1 <= 2.147483647E9d) {
                return MathX.nChooseK((int) d0, (int) d1);
            }
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    };
    public static final Function COS = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.cos(d);
        }
    };
    public static final Function COSH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.cosh(d);
        }
    };
    public static final Function DEGREES = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.toDegrees(d);
        }
    };
    public static final Function DOLLAR = new Var1or2ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            return evaluate(srcRowIndex, srcColumnIndex, arg0, NumericFunction.DOLLAR_ARG2_DEFAULT);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                double val = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                try {
                    if (((int) NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex)) > 127) {
                        return ErrorEval.VALUE_INVALID;
                    }
                    return new NumberEval(val);
                } catch (EvaluationException e) {
                    e = e;
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                e = e2;
                return e.getErrorEval();
            }
        }
    };
    static final NumberEval DOLLAR_ARG2_DEFAULT = new NumberEval(2.0d);
    public static final Function EXP = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.pow(2.718281828459045d, d);
        }
    };
    public static final Function FACT = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.factorial((int) d);
        }
    };
    public static final Function FLOOR = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) throws EvaluationException {
            if (d1 != 0.0d) {
                return MathX.floor(d0, d1);
            }
            if (d0 == 0.0d) {
                return 0.0d;
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    };
    public static final Function INT = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return (double) Math.round(d - 0.5d);
        }
    };

    /* renamed from: LN */
    public static final Function f866LN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.log(d);
        }
    };
    public static final Function LOG = new Log();
    public static final Function LOG10 = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.log(d) / NumericFunction.LOG_10_TO_BASE_e;
        }
    };
    static final double LOG_10_TO_BASE_e = Math.log(10.0d);
    public static final Function MOD = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) throws EvaluationException {
            if (d1 != 0.0d) {
                return MathX.mod(d0, d1);
            }
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    };

    /* renamed from: PI */
    public static final Function f867PI = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return NumericFunction.PI_EVAL;
        }
    };
    static final NumberEval PI_EVAL = new NumberEval(3.141592653589793d);
    public static final Function POISSON = new Fixed3ArgFunction() {
        private static final double DEFAULT_RETURN_RESULT = 1.0d;
        private final long[] FACTORIALS = {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};

        private boolean isDefaultResult(double x, double mean) {
            if (x == 0.0d && mean == 0.0d) {
                return true;
            }
            return false;
        }

        private boolean checkArgument(double aDouble) throws EvaluationException {
            NumericFunction.checkValue(aDouble);
            if (aDouble >= 0.0d) {
                return true;
            }
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }

        private double probability(int k, double lambda) {
            return (Math.pow(lambda, (double) k) * Math.exp(-lambda)) / ((double) factorial(k));
        }

        private double cumulativeProbability(int x, double lambda) {
            double result = 0.0d;
            for (int k = 0; k <= x; k++) {
                result += probability(k, lambda);
            }
            return result;
        }

        public long factorial(int n) {
            if (n >= 0 && n <= 20) {
                return this.FACTORIALS[n];
            }
            throw new IllegalArgumentException("Valid argument should be in the range [0..20]");
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {
            double result;
            boolean cumulative = ((BoolEval) arg2).getBooleanValue();
            try {
                double x = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                double mean = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
                if (isDefaultResult(x, mean)) {
                    return new NumberEval((double) DEFAULT_RETURN_RESULT);
                }
                checkArgument(x);
                checkArgument(mean);
                if (cumulative) {
                    result = cumulativeProbability((int) x, mean);
                } else {
                    result = probability((int) x, mean);
                }
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    };
    public static final Function POWER = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) {
            return Math.pow(d0, d1);
        }
    };
    public static final Function RADIANS = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.toRadians(d);
        }
    };
    public static final Function RAND = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return new NumberEval(Math.random());
        }
    };
    public static final Function ROUND = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) {
            return MathX.round(d0, (int) d1);
        }
    };
    public static final Function ROUNDDOWN = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) {
            return MathX.roundDown(d0, (int) d1);
        }
    };
    public static final Function ROUNDUP = new TwoArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d0, double d1) {
            return MathX.roundUp(d0, (int) d1);
        }
    };
    public static final Function SIGN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return (double) MathX.sign(d);
        }
    };
    public static final Function SIN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.sin(d);
        }
    };
    public static final Function SINH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.sinh(d);
        }
    };
    public static final Function SQRT = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.sqrt(d);
        }
    };
    public static final Function TAN = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return Math.tan(d);
        }
    };
    public static final Function TANH = new OneArg() {
        /* access modifiers changed from: protected */
        public double evaluate(double d) {
            return MathX.tanh(d);
        }
    };
    static final double TEN = 10.0d;
    public static final Function TRUNC = new Var1or2ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            return evaluate(srcRowIndex, srcColumnIndex, arg0, NumericFunction.TRUNC_ARG2_DEFAULT);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                double multi = Math.pow(10.0d, NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex));
                double result = Math.floor(d0 * multi) / multi;
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    };
    static final NumberEval TRUNC_ARG2_DEFAULT = new NumberEval(0.0d);
    static final double ZERO = 0.0d;

    private static final class Log extends Var1or2ArgFunction {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            try {
                double result = Math.log(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex)) / NumericFunction.LOG_10_TO_BASE_e;
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            double result;
            int i = srcRowIndex;
            int i2 = srcColumnIndex;
            try {
                double d0 = NumericFunction.singleOperandEvaluate(arg0, i, i2);
                try {
                    double d1 = NumericFunction.singleOperandEvaluate(arg1, i, i2);
                    double logE = Math.log(d0);
                    double base = d1;
                    if (base == 2.718281828459045d) {
                        result = logE;
                    } else {
                        result = logE / Math.log(base);
                    }
                    NumericFunction.checkValue(result);
                    return new NumberEval(result);
                } catch (EvaluationException e) {
                    e = e;
                    return e.getErrorEval();
                }
            } catch (EvaluationException e2) {
                e = e2;
                ValueEval valueEval = arg1;
                return e.getErrorEval();
            }
        }
    }

    public static abstract class OneArg extends Fixed1ArgFunction {
        /* access modifiers changed from: protected */
        public abstract double evaluate(double d) throws EvaluationException;

        protected OneArg() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            try {
                double result = evaluate(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex));
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }

        /* access modifiers changed from: protected */
        public final double eval(ValueEval[] args, int srcCellRow, int srcCellCol) throws EvaluationException {
            if (args.length == 1) {
                return evaluate(NumericFunction.singleOperandEvaluate(args[0], srcCellRow, srcCellCol));
            }
            throw new EvaluationException(ErrorEval.VALUE_INVALID);
        }
    }

    public static abstract class TwoArg extends Fixed2ArgFunction {
        /* access modifiers changed from: protected */
        public abstract double evaluate(double d, double d2) throws EvaluationException;

        protected TwoArg() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            try {
                double result = evaluate(NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex), NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex));
                NumericFunction.checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract double eval(ValueEval[] valueEvalArr, int i, int i2) throws EvaluationException;

    protected static final double singleOperandEvaluate(ValueEval arg, int srcRowIndex, int srcColumnIndex) throws EvaluationException {
        if (arg == null) {
            throw new IllegalArgumentException("arg must not be null");
        }
        double result = OperandResolver.coerceValueToDouble(OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex));
        checkValue(result);
        return result;
    }

    static final void checkValue(double result) throws EvaluationException {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    }

    public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        try {
            double result = eval(args, srcCellRow, srcCellCol);
            try {
                checkValue(result);
                return new NumberEval(result);
            } catch (EvaluationException e) {
                e = e;
                return e.getErrorEval();
            }
        } catch (EvaluationException e2) {
            e = e2;
            return e.getErrorEval();
        }
    }
}
