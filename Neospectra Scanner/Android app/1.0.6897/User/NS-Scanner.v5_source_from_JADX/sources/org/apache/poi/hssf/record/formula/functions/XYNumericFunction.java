package org.apache.poi.hssf.record.formula.functions;

import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.EvaluationException;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.LookupUtils.ValueVector;
import org.apache.poi.p009ss.formula.TwoDEval;

public abstract class XYNumericFunction extends Fixed2ArgFunction {

    protected interface Accumulator {
        double accumulate(double d, double d2);
    }

    private static final class AreaValueArray extends ValueArray {
        private final TwoDEval _ae;
        private final int _width;

        public AreaValueArray(TwoDEval ae) {
            super(ae.getWidth() * ae.getHeight());
            this._ae = ae;
            this._width = ae.getWidth();
        }

        /* access modifiers changed from: protected */
        public ValueEval getItemInternal(int index) {
            return this._ae.getValue(index / this._width, index % this._width);
        }
    }

    private static final class RefValueArray extends ValueArray {
        private final RefEval _ref;

        public RefValueArray(RefEval ref) {
            super(1);
            this._ref = ref;
        }

        /* access modifiers changed from: protected */
        public ValueEval getItemInternal(int index) {
            return this._ref.getInnerValueEval();
        }
    }

    private static final class SingleCellValueArray extends ValueArray {
        private final ValueEval _value;

        public SingleCellValueArray(ValueEval value) {
            super(1);
            this._value = value;
        }

        /* access modifiers changed from: protected */
        public ValueEval getItemInternal(int index) {
            return this._value;
        }
    }

    private static abstract class ValueArray implements ValueVector {
        private final int _size;

        /* access modifiers changed from: protected */
        public abstract ValueEval getItemInternal(int i);

        protected ValueArray(int size) {
            this._size = size;
        }

        public ValueEval getItem(int index) {
            if (index >= 0 && index <= this._size) {
                return getItemInternal(index);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Specified index ");
            sb.append(index);
            sb.append(" is outside range (0..");
            sb.append(this._size - 1);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }

        public final int getSize() {
            return this._size;
        }
    }

    /* access modifiers changed from: protected */
    public abstract Accumulator createAccumulator();

    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
        try {
            ValueVector vvX = createValueVector(arg0);
            ValueVector vvY = createValueVector(arg1);
            int size = vvX.getSize();
            if (size != 0) {
                if (vvY.getSize() == size) {
                    double result = evaluateInternal(vvX, vvY, size);
                    if (Double.isNaN(result) || Double.isInfinite(result)) {
                        return ErrorEval.NUM_ERROR;
                    }
                    return new NumberEval(result);
                }
            }
            return ErrorEval.f854NA;
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
    }

    private double evaluateInternal(ValueVector x, ValueVector y, int size) throws EvaluationException {
        Accumulator acc = createAccumulator();
        ErrorEval firstXerr = null;
        ErrorEval firstYerr = null;
        boolean accumlatedSome = false;
        double result = 0.0d;
        for (int i = 0; i < size; i++) {
            ValueEval vx = x.getItem(i);
            ValueEval vy = y.getItem(i);
            if ((vx instanceof ErrorEval) && firstXerr == null) {
                firstXerr = (ErrorEval) vx;
            } else if ((vy instanceof ErrorEval) && firstYerr == null) {
                firstYerr = (ErrorEval) vy;
            } else if ((vx instanceof NumberEval) && (vy instanceof NumberEval)) {
                accumlatedSome = true;
                result += acc.accumulate(((NumberEval) vx).getNumberValue(), ((NumberEval) vy).getNumberValue());
            }
        }
        ValueVector valueVector = y;
        if (firstXerr != null) {
            throw new EvaluationException(firstXerr);
        } else if (firstYerr != null) {
            throw new EvaluationException(firstYerr);
        } else if (accumlatedSome) {
            return result;
        } else {
            throw new EvaluationException(ErrorEval.DIV_ZERO);
        }
    }

    private static ValueVector createValueVector(ValueEval arg) throws EvaluationException {
        if (arg instanceof ErrorEval) {
            throw new EvaluationException((ErrorEval) arg);
        } else if (arg instanceof TwoDEval) {
            return new AreaValueArray((TwoDEval) arg);
        } else {
            if (arg instanceof RefEval) {
                return new RefValueArray((RefEval) arg);
            }
            return new SingleCellValueArray(arg);
        }
    }
}
