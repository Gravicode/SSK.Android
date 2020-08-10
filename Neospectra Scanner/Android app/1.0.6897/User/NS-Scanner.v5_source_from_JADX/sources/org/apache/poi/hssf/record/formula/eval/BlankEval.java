package org.apache.poi.hssf.record.formula.eval;

public final class BlankEval implements ValueEval {
    public static final BlankEval INSTANCE = instance;
    public static final BlankEval instance = new BlankEval();

    private BlankEval() {
    }
}
