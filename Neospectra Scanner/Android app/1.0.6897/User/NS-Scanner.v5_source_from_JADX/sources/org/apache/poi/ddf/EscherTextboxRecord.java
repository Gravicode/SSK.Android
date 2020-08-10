package org.apache.poi.ddf;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.RecordFormatException;

public class EscherTextboxRecord extends EscherRecord {
    private static final byte[] NO_BYTES = new byte[0];
    public static final String RECORD_DESCRIPTION = "msofbtClientTextbox";
    public static final short RECORD_ID = -4083;
    private byte[] thedata = NO_BYTES;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        this.thedata = new byte[bytesRemaining];
        System.arraycopy(data, offset + 8, this.thedata, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, this.thedata.length);
        System.arraycopy(this.thedata, 0, data, offset + 8, this.thedata.length);
        int pos = offset + 8 + this.thedata.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        int size = pos - offset;
        if (size == getRecordSize()) {
            return size;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(size);
        sb.append(" bytes written but getRecordSize() reports ");
        sb.append(getRecordSize());
        throw new RecordFormatException(sb.toString());
    }

    public byte[] getData() {
        return this.thedata;
    }

    public void setData(byte[] b, int start, int length) {
        this.thedata = new byte[length];
        System.arraycopy(b, start, this.thedata, 0, length);
    }

    public void setData(byte[] b) {
        setData(b, 0, b.length);
    }

    public int getRecordSize() {
        return this.thedata.length + 8;
    }

    public Object clone() {
        return super.clone();
    }

    public String getRecordName() {
        return "ClientTextbox";
    }

    public String toString() {
        String nl = System.getProperty("line.separator");
        String theDumpHex = "";
        try {
            if (this.thedata.length != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("  Extra Data:");
                sb.append(nl);
                String theDumpHex2 = sb.toString();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(theDumpHex2);
                sb2.append(HexDump.dump(this.thedata, 0, 0));
                theDumpHex = sb2.toString();
            }
        } catch (Exception e) {
            theDumpHex = "Error!!";
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getClass().getName());
        sb3.append(":");
        sb3.append(nl);
        sb3.append("  isContainer: ");
        sb3.append(isContainerRecord());
        sb3.append(nl);
        sb3.append("  options: 0x");
        sb3.append(HexDump.toHex(getOptions()));
        sb3.append(nl);
        sb3.append("  recordId: 0x");
        sb3.append(HexDump.toHex(getRecordId()));
        sb3.append(nl);
        sb3.append("  numchildren: ");
        sb3.append(getChildRecords().size());
        sb3.append(nl);
        sb3.append(theDumpHex);
        return sb3.toString();
    }
}
