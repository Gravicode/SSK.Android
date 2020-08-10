package org.apache.poi.hssf.record.formula.functions;

public final class Sumx2py2 extends XYNumericFunction {
    private static final Accumulator XSquaredPlusYSquaredAccumulator = new Accumulator() {
        public double accumulate(double x, double y) {
            return (x * x) + (y * y);
        }
    };

    /* access modifiers changed from: protected */
    public Accumulator createAccumulator() {
        return XSquaredPlusYSquaredAccumulator;
    }
}
