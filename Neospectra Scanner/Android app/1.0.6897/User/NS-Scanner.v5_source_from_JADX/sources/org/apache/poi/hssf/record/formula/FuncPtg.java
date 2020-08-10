package org.apache.poi.hssf.record.formula;

import org.apache.poi.hssf.record.formula.function.FunctionMetadata;
import org.apache.poi.hssf.record.formula.function.FunctionMetadataRegistry;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class FuncPtg extends AbstractFunctionPtg {
    public static final int SIZE = 3;
    public static final byte sid = 33;

    public static FuncPtg create(LittleEndianInput in) {
        return create(in.readUShort());
    }

    private FuncPtg(int funcIndex, FunctionMetadata fm) {
        super(funcIndex, fm.getReturnClassCode(), fm.getParameterClassCodes(), fm.getMinParams());
    }

    public static FuncPtg create(int functionIndex) {
        FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(functionIndex);
        if (fm != null) {
            return new FuncPtg(functionIndex, fm);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid built-in function index (");
        sb.append(functionIndex);
        sb.append(")");
        throw new RuntimeException(sb.toString());
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 33);
        out.writeShort(getFunctionIndex());
    }

    public int getSize() {
        return 3;
    }
}
