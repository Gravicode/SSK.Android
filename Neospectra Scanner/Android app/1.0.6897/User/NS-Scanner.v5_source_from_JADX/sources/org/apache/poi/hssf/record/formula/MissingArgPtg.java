package org.apache.poi.hssf.record.formula;

import org.apache.poi.util.LittleEndianOutput;

public final class MissingArgPtg extends ScalarConstantPtg {
    private static final int SIZE = 1;
    public static final Ptg instance = new MissingArgPtg();
    public static final byte sid = 22;

    private MissingArgPtg() {
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + sid);
    }

    public int getSize() {
        return 1;
    }

    public String toFormulaString() {
        return " ";
    }
}
