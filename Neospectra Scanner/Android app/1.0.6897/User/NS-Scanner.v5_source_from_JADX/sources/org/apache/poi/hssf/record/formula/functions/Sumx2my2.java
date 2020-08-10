package org.apache.poi.hssf.record.formula.functions;

public final class Sumx2my2 extends XYNumericFunction {
    private static final Accumulator XSquaredMinusYSquaredAccumulator = new Accumulator() {
        public double accumulate(double x, double y) {
            return (x * x) - (y * y);
        }
    };

    /* access modifiers changed from: protected */
    public Accumulator createAccumulator() {
        return XSquaredMinusYSquaredAccumulator;
    }
}
