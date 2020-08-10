package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperandResolver;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.p009ss.formula.TwoDEval;

public abstract class MultiOperandNumericFunction implements Function {
    private static final int DEFAULT_MAX_NUM_OPERANDS = 30;
    static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
    private final boolean _isBlankCounted;
    private final boolean _isReferenceBoolCounted;

    private static class DoubleList {
        private double[] _array = new double[8];
        private int _count = 0;

        public double[] toArray() {
            if (this._count < 1) {
                return MultiOperandNumericFunction.EMPTY_DOUBLE_ARRAY;
            }
            double[] result = new double[this._count];
            System.arraycopy(this._array, 0, result, 0, this._count);
            return result;
        }

        private void ensureCapacity(int reqSize) {
            if (reqSize > this._array.length) {
                double[] newArr = new double[((reqSize * 3) / 2)];
                System.arraycopy(this._array, 0, newArr, 0, this._count);
                this._array = newArr;
            }
        }

        public void add(double value) {
            ensureCapacity(this._count + 1);
            this._array[this._count] = value;
            this._count++;
        }
    }

    /* access modifiers changed from: protected */
    public abstract double evaluate(double[] dArr) throws EvaluationException;

    protected MultiOperandNumericFunction(boolean isReferenceBoolCounted, boolean isBlankCounted) {
        this._isReferenceBoolCounted = isReferenceBoolCounted;
        this._isBlankCounted = isBlankCounted;
    }

    public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        try {
            double d = evaluate(getNumberArray(args));
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                return ErrorEval.NUM_ERROR;
            }
            return new NumberEval(d);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    /* access modifiers changed from: protected */
    public int getMaxNumOperands() {
        return 30;
    }

    /* access modifiers changed from: protected */
    public final double[] getNumberArray(ValueEval[] operands) throws EvaluationException {
        if (operands.length > getMaxNumOperands()) {
            throw EvaluationException.invalidValue();
        }
        DoubleList retval = new DoubleList();
        for (ValueEval collectValues : operands) {
            collectValues(collectValues, retval);
        }
        return retval.toArray();
    }

    private void collectValues(ValueEval operand, DoubleList temp) throws EvaluationException {
        if (operand instanceof TwoDEval) {
            TwoDEval ae = (TwoDEval) operand;
            int width = ae.getWidth();
            int height = ae.getHeight();
            for (int rrIx = 0; rrIx < height; rrIx++) {
                for (int rcIx = 0; rcIx < width; rcIx++) {
                    collectValue(ae.getValue(rrIx, rcIx), true, temp);
                }
            }
        } else if (operand instanceof RefEval) {
            collectValue(((RefEval) operand).getInnerValueEval(), true, temp);
        } else {
            collectValue(operand, false, temp);
        }
    }

    private void collectValue(ValueEval ve, boolean isViaReference, DoubleList temp) throws EvaluationException {
        if (ve == null) {
            throw new IllegalArgumentException("ve must not be null");
        } else if (ve instanceof NumberEval) {
            temp.add(((NumberEval) ve).getNumberValue());
        } else if (ve instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) ve);
        } else if (ve instanceof StringEval) {
            if (!isViaReference) {
                Double d = OperandResolver.parseDouble(((StringEval) ve).getStringValue());
                if (d == null) {
                    throw new EvaluationException(ErrorEval.VALUE_INVALID);
                }
                temp.add(d.doubleValue());
            }
        } else if (ve instanceof BoolEval) {
            if (!isViaReference || this._isReferenceBoolCounted) {
                temp.add(((BoolEval) ve).getNumberValue());
            }
        } else if (ve == BlankEval.instance) {
            if (this._isBlankCounted) {
                temp.add(0.0d);
            }
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid ValueEval type passed for conversion: (");
            sb.append(ve.getClass());
            sb.append(")");
            throw new RuntimeException(sb.toString());
        }
    }
}
