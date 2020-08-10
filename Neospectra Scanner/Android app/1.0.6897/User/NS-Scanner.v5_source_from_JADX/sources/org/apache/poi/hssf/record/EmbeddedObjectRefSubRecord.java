package org.apache.poi.hssf.record;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.MissingArgPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianInputStream;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class EmbeddedObjectRefSubRecord extends SubRecord {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short sid = 9;
    private int field_1_unknown_int;
    private Ptg field_2_refPtg;
    private byte[] field_2_unknownFormulaData;
    private boolean field_3_unicode_flag;
    private String field_4_ole_classname;
    private Byte field_4_unknownByte;
    private Integer field_5_stream_id;
    private byte[] field_6_unknown;

    EmbeddedObjectRefSubRecord() {
        this.field_2_unknownFormulaData = new byte[]{2, 108, 106, MissingArgPtg.sid, 1};
        this.field_6_unknown = EMPTY_BYTE_ARRAY;
        this.field_4_ole_classname = null;
    }

    public short getSid() {
        return 9;
    }

    public EmbeddedObjectRefSubRecord(LittleEndianInput in, int size) {
        int remaining = size - 2;
        int dataLenAfterFormula = remaining - in.readShort();
        int formulaSize = in.readUShort();
        int remaining2 = remaining - 2;
        this.field_1_unknown_int = in.readInt();
        int remaining3 = remaining2 - 4;
        byte[] formulaRawBytes = readRawData(in, formulaSize);
        int remaining4 = remaining3 - formulaSize;
        this.field_2_refPtg = readRefPtg(formulaRawBytes);
        if (this.field_2_refPtg == null) {
            this.field_2_unknownFormulaData = formulaRawBytes;
        } else {
            this.field_2_unknownFormulaData = null;
        }
        int stringByteCount = 0;
        if (remaining4 < dataLenAfterFormula + 3) {
            this.field_4_ole_classname = null;
        } else if (in.readByte() != 3) {
            throw new RecordFormatException("Expected byte 0x03 here");
        } else {
            int nChars = in.readUShort();
            int stringByteCount2 = 1 + 2;
            if (nChars > 0) {
                boolean z = true;
                if ((in.readByte() & 1) == 0) {
                    z = false;
                }
                this.field_3_unicode_flag = z;
                int stringByteCount3 = stringByteCount2 + 1;
                if (this.field_3_unicode_flag) {
                    this.field_4_ole_classname = StringUtil.readUnicodeLE(in, nChars);
                    stringByteCount2 = stringByteCount3 + (nChars * 2);
                } else {
                    this.field_4_ole_classname = StringUtil.readCompressedUnicode(in, nChars);
                    stringByteCount2 = stringByteCount3 + nChars;
                }
            } else {
                this.field_4_ole_classname = "";
            }
            stringByteCount = stringByteCount2;
        }
        int stringByteCount4 = stringByteCount;
        int remaining5 = remaining4 - stringByteCount4;
        if ((stringByteCount4 + formulaSize) % 2 != 0) {
            int b = in.readByte();
            remaining5--;
            if (this.field_2_refPtg != null && this.field_4_ole_classname == null) {
                this.field_4_unknownByte = Byte.valueOf((byte) b);
            }
        }
        int nUnexpectedPadding = remaining5 - dataLenAfterFormula;
        if (nUnexpectedPadding > 0) {
            PrintStream printStream = System.err;
            StringBuilder sb = new StringBuilder();
            sb.append("Discarding ");
            sb.append(nUnexpectedPadding);
            sb.append(" unexpected padding bytes ");
            printStream.println(sb.toString());
            readRawData(in, nUnexpectedPadding);
            remaining5 -= nUnexpectedPadding;
        }
        if (dataLenAfterFormula >= 4) {
            this.field_5_stream_id = Integer.valueOf(in.readInt());
            remaining5 -= 4;
        } else {
            this.field_5_stream_id = null;
        }
        this.field_6_unknown = readRawData(in, remaining5);
    }

    private static Ptg readRefPtg(byte[] formulaRawBytes) {
        LittleEndianInput in = new LittleEndianInputStream(new ByteArrayInputStream(formulaRawBytes));
        switch (in.readByte()) {
            case 36:
                return new RefPtg(in);
            case 37:
                return new AreaPtg(in);
            case 58:
                return new Ref3DPtg(in);
            case 59:
                return new Area3DPtg(in);
            default:
                return null;
        }
    }

    private static byte[] readRawData(LittleEndianInput in, int size) {
        if (size < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Negative size (");
            sb.append(size);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        } else if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        } else {
            byte[] result = new byte[size];
            in.readFully(result);
            return result;
        }
    }

    private int getStreamIDOffset(int formulaSize) {
        int result = 6 + formulaSize;
        if (this.field_4_ole_classname != null) {
            result += 3;
            int stringLen = this.field_4_ole_classname.length();
            if (stringLen > 0) {
                int result2 = result + 1;
                if (this.field_3_unicode_flag) {
                    result = result2 + (stringLen * 2);
                } else {
                    result = result2 + stringLen;
                }
            }
        }
        if (result % 2 != 0) {
            return result + 1;
        }
        return result;
    }

    private int getDataSize(int idOffset) {
        int result = idOffset + 2;
        if (this.field_5_stream_id != null) {
            result += 4;
        }
        return this.field_6_unknown.length + result;
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return getDataSize(getStreamIDOffset(this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize()));
    }

    public void serialize(LittleEndianOutput out) {
        int formulaSize = this.field_2_refPtg == null ? this.field_2_unknownFormulaData.length : this.field_2_refPtg.getSize();
        int idOffset = getStreamIDOffset(formulaSize);
        int dataSize = getDataSize(idOffset);
        out.writeShort(9);
        out.writeShort(dataSize);
        out.writeShort(idOffset);
        out.writeShort(formulaSize);
        out.writeInt(this.field_1_unknown_int);
        if (this.field_2_refPtg == null) {
            out.write(this.field_2_unknownFormulaData);
        } else {
            this.field_2_refPtg.write(out);
        }
        int pos = 12 + formulaSize;
        if (this.field_4_ole_classname != null) {
            out.writeByte(3);
            int pos2 = pos + 1;
            int stringLen = this.field_4_ole_classname.length();
            out.writeShort(stringLen);
            pos = pos2 + 2;
            if (stringLen > 0) {
                out.writeByte(this.field_3_unicode_flag ? 1 : 0);
                int pos3 = pos + 1;
                if (this.field_3_unicode_flag) {
                    StringUtil.putUnicodeLE(this.field_4_ole_classname, out);
                    pos = pos3 + (stringLen * 2);
                } else {
                    StringUtil.putCompressedUnicode(this.field_4_ole_classname, out);
                    pos = pos3 + stringLen;
                }
            }
        }
        switch (idOffset - (pos - 6)) {
            case 0:
                break;
            case 1:
                out.writeByte(this.field_4_unknownByte == null ? 0 : this.field_4_unknownByte.intValue());
                pos++;
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Bad padding calculation (");
                sb.append(idOffset);
                sb.append(", ");
                sb.append(pos);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
        }
        if (this.field_5_stream_id != null) {
            out.writeInt(this.field_5_stream_id.intValue());
            int pos4 = pos + 4;
        }
        out.write(this.field_6_unknown);
    }

    public Integer getStreamId() {
        return this.field_5_stream_id;
    }

    public String getOLEClassName() {
        return this.field_4_ole_classname;
    }

    public byte[] getObjectData() {
        return this.field_6_unknown;
    }

    public Object clone() {
        return this;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[ftPictFmla]\n");
        sb.append("    .f2unknown     = ");
        sb.append(HexDump.intToHex(this.field_1_unknown_int));
        sb.append("\n");
        if (this.field_2_refPtg == null) {
            sb.append("    .f3unknown     = ");
            sb.append(HexDump.toHex(this.field_2_unknownFormulaData));
            sb.append("\n");
        } else {
            sb.append("    .formula       = ");
            sb.append(this.field_2_refPtg.toString());
            sb.append("\n");
        }
        if (this.field_4_ole_classname != null) {
            sb.append("    .unicodeFlag   = ");
            sb.append(this.field_3_unicode_flag);
            sb.append("\n");
            sb.append("    .oleClassname  = ");
            sb.append(this.field_4_ole_classname);
            sb.append("\n");
        }
        if (this.field_4_unknownByte != null) {
            sb.append("    .f4unknown   = ");
            sb.append(HexDump.byteToHex(this.field_4_unknownByte.intValue()));
            sb.append("\n");
        }
        if (this.field_5_stream_id != null) {
            sb.append("    .streamId      = ");
            sb.append(HexDump.intToHex(this.field_5_stream_id.intValue()));
            sb.append("\n");
        }
        if (this.field_6_unknown.length > 0) {
            sb.append("    .f7unknown     = ");
            sb.append(HexDump.toHex(this.field_6_unknown));
            sb.append("\n");
        }
        sb.append("[/ftPictFmla]");
        return sb.toString();
    }
}
