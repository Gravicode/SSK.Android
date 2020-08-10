package org.apache.poi.ddf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherClientAnchorRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtClientAnchor";
    public static final short RECORD_ID = -4080;
    private short field_1_flag;
    private short field_2_col1;
    private short field_3_dx1;
    private short field_4_row1;
    private short field_5_dy1;
    private short field_6_col2;
    private short field_7_dx2;
    private short field_8_row2;
    private short field_9_dy2;
    private byte[] remainingData;
    private boolean shortRecord = false;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        int size = 0;
        if (bytesRemaining != 4) {
            this.field_1_flag = LittleEndian.getShort(data, pos + 0);
            int size2 = 0 + 2;
            this.field_2_col1 = LittleEndian.getShort(data, pos + size2);
            int size3 = size2 + 2;
            this.field_3_dx1 = LittleEndian.getShort(data, pos + size3);
            int size4 = size3 + 2;
            this.field_4_row1 = LittleEndian.getShort(data, pos + size4);
            size = size4 + 2;
            if (bytesRemaining >= 18) {
                this.field_5_dy1 = LittleEndian.getShort(data, pos + size);
                int size5 = size + 2;
                this.field_6_col2 = LittleEndian.getShort(data, pos + size5);
                int size6 = size5 + 2;
                this.field_7_dx2 = LittleEndian.getShort(data, pos + size6);
                int size7 = size6 + 2;
                this.field_8_row2 = LittleEndian.getShort(data, pos + size7);
                int size8 = size7 + 2;
                this.field_9_dy2 = LittleEndian.getShort(data, pos + size8);
                size = size8 + 2;
                this.shortRecord = false;
            } else {
                this.shortRecord = true;
            }
        }
        int bytesRemaining2 = bytesRemaining - size;
        this.remainingData = new byte[bytesRemaining2];
        System.arraycopy(data, pos + size, this.remainingData, 0, bytesRemaining2);
        return size + 8 + bytesRemaining2;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this.remainingData == null) {
            this.remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        int i = 18;
        LittleEndian.putInt(data, offset + 4, this.remainingData.length + (this.shortRecord ? 8 : 18));
        LittleEndian.putShort(data, offset + 8, this.field_1_flag);
        LittleEndian.putShort(data, offset + 10, this.field_2_col1);
        LittleEndian.putShort(data, offset + 12, this.field_3_dx1);
        LittleEndian.putShort(data, offset + 14, this.field_4_row1);
        if (!this.shortRecord) {
            LittleEndian.putShort(data, offset + 16, this.field_5_dy1);
            LittleEndian.putShort(data, offset + 18, this.field_6_col2);
            LittleEndian.putShort(data, offset + 20, this.field_7_dx2);
            LittleEndian.putShort(data, offset + 22, this.field_8_row2);
            LittleEndian.putShort(data, offset + 24, this.field_9_dy2);
        }
        System.arraycopy(this.remainingData, 0, data, (this.shortRecord ? 16 : 26) + offset, this.remainingData.length);
        int i2 = offset + 8;
        if (this.shortRecord) {
            i = 8;
        }
        int pos = i2 + i + this.remainingData.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return (this.shortRecord ? 8 : 18) + 8 + (this.remainingData == null ? 0 : this.remainingData.length);
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientAnchor";
    }

    public String toString() {
        String extraData;
        String nl = System.getProperty("line.separator");
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            HexDump.dump(this.remainingData, 0, (OutputStream) b, 0);
            extraData = b.toString();
        } catch (Exception e) {
            extraData = "error\n";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(":");
        sb.append(nl);
        sb.append("  RecordId: 0x");
        sb.append(HexDump.toHex((short) RECORD_ID));
        sb.append(nl);
        sb.append("  Options: 0x");
        sb.append(HexDump.toHex(getOptions()));
        sb.append(nl);
        sb.append("  Flag: ");
        sb.append(this.field_1_flag);
        sb.append(nl);
        sb.append("  Col1: ");
        sb.append(this.field_2_col1);
        sb.append(nl);
        sb.append("  DX1: ");
        sb.append(this.field_3_dx1);
        sb.append(nl);
        sb.append("  Row1: ");
        sb.append(this.field_4_row1);
        sb.append(nl);
        sb.append("  DY1: ");
        sb.append(this.field_5_dy1);
        sb.append(nl);
        sb.append("  Col2: ");
        sb.append(this.field_6_col2);
        sb.append(nl);
        sb.append("  DX2: ");
        sb.append(this.field_7_dx2);
        sb.append(nl);
        sb.append("  Row2: ");
        sb.append(this.field_8_row2);
        sb.append(nl);
        sb.append("  DY2: ");
        sb.append(this.field_9_dy2);
        sb.append(nl);
        sb.append("  Extra Data:");
        sb.append(nl);
        sb.append(extraData);
        return sb.toString();
    }

    public short getFlag() {
        return this.field_1_flag;
    }

    public void setFlag(short field_1_flag2) {
        this.field_1_flag = field_1_flag2;
    }

    public short getCol1() {
        return this.field_2_col1;
    }

    public void setCol1(short field_2_col12) {
        this.field_2_col1 = field_2_col12;
    }

    public short getDx1() {
        return this.field_3_dx1;
    }

    public void setDx1(short field_3_dx12) {
        this.field_3_dx1 = field_3_dx12;
    }

    public short getRow1() {
        return this.field_4_row1;
    }

    public void setRow1(short field_4_row12) {
        this.field_4_row1 = field_4_row12;
    }

    public short getDy1() {
        return this.field_5_dy1;
    }

    public void setDy1(short field_5_dy12) {
        this.shortRecord = false;
        this.field_5_dy1 = field_5_dy12;
    }

    public short getCol2() {
        return this.field_6_col2;
    }

    public void setCol2(short field_6_col22) {
        this.shortRecord = false;
        this.field_6_col2 = field_6_col22;
    }

    public short getDx2() {
        return this.field_7_dx2;
    }

    public void setDx2(short field_7_dx22) {
        this.shortRecord = false;
        this.field_7_dx2 = field_7_dx22;
    }

    public short getRow2() {
        return this.field_8_row2;
    }

    public void setRow2(short field_8_row22) {
        this.shortRecord = false;
        this.field_8_row2 = field_8_row22;
    }

    public short getDy2() {
        return this.field_9_dy2;
    }

    public void setDy2(short field_9_dy22) {
        this.shortRecord = false;
        this.field_9_dy2 = field_9_dy22;
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public void setRemainingData(byte[] remainingData2) {
        this.remainingData = remainingData2;
    }
}
