package org.apache.poi.hssf.record.common;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.util.LittleEndianOutput;

public final class FtrHeader {
    private short grbitFrt;
    private short recordType;
    private byte[] reserved = new byte[8];

    public FtrHeader() {
    }

    public FtrHeader(RecordInputStream in) {
        this.recordType = in.readShort();
        this.grbitFrt = in.readShort();
        in.read(this.reserved, 0, 8);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" [FUTURE HEADER]\n");
        StringBuilder sb = new StringBuilder();
        sb.append("   Type ");
        sb.append(this.recordType);
        buffer.append(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("   Flags ");
        sb2.append(this.grbitFrt);
        buffer.append(sb2.toString());
        buffer.append(" [/FUTURE HEADER]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.recordType);
        out.writeShort(this.grbitFrt);
        out.write(this.reserved);
    }

    public static int getDataSize() {
        return 12;
    }

    public short getRecordType() {
        return this.recordType;
    }

    public void setRecordType(short recordType2) {
        this.recordType = recordType2;
    }

    public short getGrbitFrt() {
        return this.grbitFrt;
    }

    public void setGrbitFrt(short grbitFrt2) {
        this.grbitFrt = grbitFrt2;
    }

    public byte[] getReserved() {
        return this.reserved;
    }

    public void setReserved(byte[] reserved2) {
        this.reserved = reserved2;
    }
}
