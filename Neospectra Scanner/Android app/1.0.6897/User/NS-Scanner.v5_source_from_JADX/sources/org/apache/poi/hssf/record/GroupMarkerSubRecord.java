package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class GroupMarkerSubRecord extends SubRecord {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short sid = 6;
    private byte[] reserved;

    public GroupMarkerSubRecord() {
        this.reserved = EMPTY_BYTE_ARRAY;
    }

    public GroupMarkerSubRecord(LittleEndianInput in, int size) {
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        String nl = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("[ftGmo]");
        sb.append(nl);
        buffer.append(sb.toString());
        buffer.append("  reserved = ");
        buffer.append(HexDump.toHex(this.reserved));
        buffer.append(nl);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("[/ftGmo]");
        sb2.append(nl);
        buffer.append(sb2.toString());
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(6);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return 6;
    }

    public Object clone() {
        GroupMarkerSubRecord rec = new GroupMarkerSubRecord();
        rec.reserved = new byte[this.reserved.length];
        for (int i = 0; i < this.reserved.length; i++) {
            rec.reserved[i] = this.reserved[i];
        }
        return rec;
    }
}
