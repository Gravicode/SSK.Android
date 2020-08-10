package org.apache.poi.hssf.record.formula;

import org.apache.poi.p009ss.formula.FormulaRenderingWorkbook;
import org.apache.poi.p009ss.formula.WorkbookDependentFormula;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class NameXPtg extends OperandPtg implements WorkbookDependentFormula {
    private static final int SIZE = 7;
    public static final short sid = 57;
    private final int _nameNumber;
    private final int _reserved;
    private final int _sheetRefIndex;

    private NameXPtg(int sheetRefIndex, int nameNumber, int reserved) {
        this._sheetRefIndex = sheetRefIndex;
        this._nameNumber = nameNumber;
        this._reserved = reserved;
    }

    public NameXPtg(int sheetRefIndex, int nameIndex) {
        this(sheetRefIndex, nameIndex + 1, 0);
    }

    public NameXPtg(LittleEndianInput in) {
        this(in.readUShort(), in.readUShort(), in.readUShort());
    }

    public void write(LittleEndianOutput out) {
        out.writeByte(getPtgClass() + 57);
        out.writeShort(this._sheetRefIndex);
        out.writeShort(this._nameNumber);
        out.writeShort(this._reserved);
    }

    public int getSize() {
        return 7;
    }

    public String toFormulaString(FormulaRenderingWorkbook book) {
        return book.resolveNameXText(this);
    }

    public String toFormulaString() {
        throw new RuntimeException("3D references need a workbook to determine formula text");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NameXPtg:[sheetRefIndex:");
        sb.append(this._sheetRefIndex);
        sb.append(" , nameNumber:");
        sb.append(this._nameNumber);
        sb.append("]");
        return sb.toString();
    }

    public byte getDefaultOperandClass() {
        return 32;
    }

    public int getSheetRefIndex() {
        return this._sheetRefIndex;
    }

    public int getNameIndex() {
        return this._nameNumber - 1;
    }
}
