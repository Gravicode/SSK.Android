package org.apache.poi.hssf.record.formula.functions;

public final class Sumxmy2 extends XYNumericFunction {
    private static final Accumulator XMinusYSquaredAccumulator = new Accumulator() {
        public double accumulate(double x, double y) {
            double xmy = x - y;
            return xmy * xmy;
        }
    };

    /* access modifiers changed from: protected */
    public Accumulator createAccumulator() {
        return XMinusYSquaredAccumulator;
    }
}
