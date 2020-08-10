package org.apache.poi.hssf.record;

import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.p009ss.formula.Formula;
import org.apache.poi.util.BitField;
import org.apache.poi.util.BitFieldFactory;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class FormulaRecord extends CellRecord {
    private static int FIXED_SIZE = 14;
    private static final BitField alwaysCalc = BitFieldFactory.getInstance(1);
    private static final BitField calcOnLoad = BitFieldFactory.getInstance(2);
    private static final BitField sharedFormula = BitFieldFactory.getInstance(8);
    public static final short sid = 6;
    private double field_4_value;
    private short field_5_options;
    private int field_6_zero;
    private Formula field_8_parsed_expr;
    private SpecialCachedValue specialCachedValue;

    private static final class SpecialCachedValue {
        private static final long BIT_MARKER = -281474976710656L;
        public static final int BOOLEAN = 1;
        private static final int DATA_INDEX = 2;
        public static final int EMPTY = 3;
        public static final int ERROR_CODE = 2;
        public static final int STRING = 0;
        private static final int VARIABLE_DATA_LENGTH = 6;
        private final byte[] _variableData;

        private SpecialCachedValue(byte[] data) {
            this._variableData = data;
        }

        public int getTypeCode() {
            return this._variableData[0];
        }

        public static SpecialCachedValue create(long valueLongBits) {
            if ((valueLongBits & BIT_MARKER) != BIT_MARKER) {
                return null;
            }
            byte[] result = new byte[6];
            long x = valueLongBits;
            for (int i = 0; i < 6; i++) {
                result[i] = (byte) ((int) x);
                x >>= 8;
            }
            switch (result[0]) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return new SpecialCachedValue(result);
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Bad special value code (");
                    sb.append(result[0]);
                    sb.append(")");
                    throw new RecordFormatException(sb.toString());
            }
        }

        public void serialize(LittleEndianOutput out) {
            out.write(this._variableData);
            out.writeShort(65535);
        }

        public String formatDebugString() {
            StringBuilder sb = new StringBuilder();
            sb.append(formatValue());
            sb.append(' ');
            sb.append(HexDump.toHex(this._variableData));
            return sb.toString();
        }

        private String formatValue() {
            int typeCode = getTypeCode();
            switch (typeCode) {
                case 0:
                    return "<string>";
                case 1:
                    return getDataValue() == 0 ? "FALSE" : "TRUE";
                case 2:
                    return ErrorEval.getText(getDataValue());
                case 3:
                    return "<empty>";
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("#error(type=");
                    sb.append(typeCode);
                    sb.append(")#");
                    return sb.toString();
            }
        }

        private int getDataValue() {
            return this._variableData[2];
        }

        public static SpecialCachedValue createCachedEmptyValue() {
            return create(3, 0);
        }

        public static SpecialCachedValue createForString() {
            return create(0, 0);
        }

        public static SpecialCachedValue createCachedBoolean(boolean b) {
            return create(1, b);
        }

        public static SpecialCachedValue createCachedErrorCode(int errorCode) {
            return create(2, errorCode);
        }

        private static SpecialCachedValue create(int code, int data) {
            return new SpecialCachedValue(new byte[]{(byte) code, 0, (byte) data, 0, 0, 0});
        }

        public String toString() {
            StringBuffer sb = new StringBuffer(64);
            sb.append(getClass().getName());
            sb.append('[');
            sb.append(formatValue());
            sb.append(']');
            return sb.toString();
        }

        public int getValueType() {
            int typeCode = getTypeCode();
            switch (typeCode) {
                case 0:
                    return 1;
                case 1:
                    return 4;
                case 2:
                    return 5;
                case 3:
                    return 1;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected type id (");
                    sb.append(typeCode);
                    sb.append(")");
                    throw new IllegalStateException(sb.toString());
            }
        }

        public boolean getBooleanValue() {
            if (getTypeCode() != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("Not a boolean cached value - ");
                sb.append(formatValue());
                throw new IllegalStateException(sb.toString());
            } else if (getDataValue() != 0) {
                return true;
            } else {
                return false;
            }
        }

        public int getErrorValue() {
            if (getTypeCode() == 2) {
                return getDataValue();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Not an error cached value - ");
            sb.append(formatValue());
            throw new IllegalStateException(sb.toString());
        }
    }

    public FormulaRecord() {
        this.field_8_parsed_expr = Formula.create(Ptg.EMPTY_PTG_ARRAY);
    }

    public FormulaRecord(RecordInputStream ris) {
        super(ris);
        RecordInputStream recordInputStream = ris;
        long valueLongBits = recordInputStream.readLong();
        this.field_5_options = recordInputStream.readShort();
        this.specialCachedValue = SpecialCachedValue.create(valueLongBits);
        if (this.specialCachedValue == null) {
            this.field_4_value = Double.longBitsToDouble(valueLongBits);
        }
        this.field_6_zero = recordInputStream.readInt();
        this.field_8_parsed_expr = Formula.read(recordInputStream.readShort(), recordInputStream, recordInputStream.available());
    }

    public void setValue(double value) {
        this.field_4_value = value;
        this.specialCachedValue = null;
    }

    public void setCachedResultTypeEmptyString() {
        this.specialCachedValue = SpecialCachedValue.createCachedEmptyValue();
    }

    public void setCachedResultTypeString() {
        this.specialCachedValue = SpecialCachedValue.createForString();
    }

    public void setCachedResultErrorCode(int errorCode) {
        this.specialCachedValue = SpecialCachedValue.createCachedErrorCode(errorCode);
    }

    public void setCachedResultBoolean(boolean value) {
        this.specialCachedValue = SpecialCachedValue.createCachedBoolean(value);
    }

    public boolean hasCachedResultString() {
        boolean z = false;
        if (this.specialCachedValue == null) {
            return false;
        }
        if (this.specialCachedValue.getTypeCode() == 0) {
            z = true;
        }
        return z;
    }

    public int getCachedResultType() {
        if (this.specialCachedValue == null) {
            return 0;
        }
        return this.specialCachedValue.getValueType();
    }

    public boolean getCachedBooleanValue() {
        return this.specialCachedValue.getBooleanValue();
    }

    public int getCachedErrorValue() {
        return this.specialCachedValue.getErrorValue();
    }

    public void setOptions(short options) {
        this.field_5_options = options;
    }

    public double getValue() {
        return this.field_4_value;
    }

    public short getOptions() {
        return this.field_5_options;
    }

    public boolean isSharedFormula() {
        return sharedFormula.isSet(this.field_5_options);
    }

    public void setSharedFormula(boolean flag) {
        this.field_5_options = sharedFormula.setShortBoolean(this.field_5_options, flag);
    }

    public boolean isAlwaysCalc() {
        return alwaysCalc.isSet(this.field_5_options);
    }

    public void setAlwaysCalc(boolean flag) {
        this.field_5_options = alwaysCalc.setShortBoolean(this.field_5_options, flag);
    }

    public boolean isCalcOnLoad() {
        return calcOnLoad.isSet(this.field_5_options);
    }

    public void setCalcOnLoad(boolean flag) {
        this.field_5_options = calcOnLoad.setShortBoolean(this.field_5_options, flag);
    }

    public Ptg[] getParsedExpression() {
        return this.field_8_parsed_expr.getTokens();
    }

    public Formula getFormula() {
        return this.field_8_parsed_expr;
    }

    public void setParsedExpression(Ptg[] ptgs) {
        this.field_8_parsed_expr = Formula.create(ptgs);
    }

    public short getSid() {
        return 6;
    }

    /* access modifiers changed from: protected */
    public int getValueDataSize() {
        return FIXED_SIZE + this.field_8_parsed_expr.getEncodedSize();
    }

    /* access modifiers changed from: protected */
    public void serializeValue(LittleEndianOutput out) {
        if (this.specialCachedValue == null) {
            out.writeDouble(this.field_4_value);
        } else {
            this.specialCachedValue.serialize(out);
        }
        out.writeShort(getOptions());
        out.writeInt(this.field_6_zero);
        this.field_8_parsed_expr.serialize(out);
    }

    /* access modifiers changed from: protected */
    public String getRecordName() {
        return "FORMULA";
    }

    /* access modifiers changed from: protected */
    public void appendValueText(StringBuilder sb) {
        sb.append("  .value\t = ");
        if (this.specialCachedValue == null) {
            sb.append(this.field_4_value);
            sb.append("\n");
        } else {
            sb.append(this.specialCachedValue.formatDebugString());
            sb.append("\n");
        }
        sb.append("  .options   = ");
        sb.append(HexDump.shortToHex(getOptions()));
        sb.append("\n");
        sb.append("    .alwaysCalc= ");
        sb.append(isAlwaysCalc());
        sb.append("\n");
        sb.append("    .calcOnLoad= ");
        sb.append(isCalcOnLoad());
        sb.append("\n");
        sb.append("    .shared    = ");
        sb.append(isSharedFormula());
        sb.append("\n");
        sb.append("  .zero      = ");
        sb.append(HexDump.intToHex(this.field_6_zero));
        Ptg[] ptgs = this.field_8_parsed_expr.getTokens();
        for (int k = 0; k < ptgs.length; k++) {
            if (k > 0) {
                sb.append("\n");
            }
            sb.append("    Ptg[");
            sb.append(k);
            sb.append("]=");
            Ptg ptg = ptgs[k];
            sb.append(ptg.toString());
            sb.append(ptg.getRVAType());
        }
    }

    public Object clone() {
        FormulaRecord rec = new FormulaRecord();
        copyBaseFields(rec);
        rec.field_4_value = this.field_4_value;
        rec.field_5_options = this.field_5_options;
        rec.field_6_zero = this.field_6_zero;
        rec.field_8_parsed_expr = this.field_8_parsed_expr;
        rec.specialCachedValue = this.specialCachedValue;
        return rec;
    }
}
