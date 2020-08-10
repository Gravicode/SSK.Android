package org.apache.poi.ddf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndian;

public class EscherClientDataRecord extends EscherRecord {
    public static final String RECORD_DESCRIPTION = "MsofbtClientData";
    public static final short RECORD_ID = -4079;
    private byte[] remainingData;

    public int fillFields(byte[] data, int offset, EscherRecordFactory recordFactory) {
        int bytesRemaining = readHeader(data, offset);
        int pos = offset + 8;
        this.remainingData = new byte[bytesRemaining];
        System.arraycopy(data, pos, this.remainingData, 0, bytesRemaining);
        return bytesRemaining + 8;
    }

    public int serialize(int offset, byte[] data, EscherSerializationListener listener) {
        listener.beforeRecordSerialize(offset, getRecordId(), this);
        if (this.remainingData == null) {
            this.remainingData = new byte[0];
        }
        LittleEndian.putShort(data, offset, getOptions());
        LittleEndian.putShort(data, offset + 2, getRecordId());
        LittleEndian.putInt(data, offset + 4, this.remainingData.length);
        System.arraycopy(this.remainingData, 0, data, offset + 8, this.remainingData.length);
        int pos = offset + 8 + this.remainingData.length;
        listener.afterRecordSerialize(pos, getRecordId(), pos - offset, this);
        return pos - offset;
    }

    public int getRecordSize() {
        return (this.remainingData == null ? 0 : this.remainingData.length) + 8;
    }

    public short getRecordId() {
        return RECORD_ID;
    }

    public String getRecordName() {
        return "ClientData";
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
        sb.append("  Extra Data:");
        sb.append(nl);
        sb.append(extraData);
        return sb.toString();
    }

    public byte[] getRemainingData() {
        return this.remainingData;
    }

    public void setRemainingData(byte[] remainingData2) {
        this.remainingData = remainingData2;
    }
}
