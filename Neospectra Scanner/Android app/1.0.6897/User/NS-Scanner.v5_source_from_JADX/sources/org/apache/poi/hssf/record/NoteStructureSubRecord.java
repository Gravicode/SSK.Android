package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianInput;
import org.apache.poi.util.LittleEndianOutput;

public final class NoteStructureSubRecord extends SubRecord {
    private static final int ENCODED_SIZE = 22;
    public static final short sid = 13;
    private byte[] reserved;

    public NoteStructureSubRecord() {
        this.reserved = new byte[22];
    }

    public NoteStructureSubRecord(LittleEndianInput in, int size) {
        if (size != 22) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected size (");
            sb.append(size);
            sb.append(")");
            throw new RecordFormatException(sb.toString());
        }
        byte[] buf = new byte[size];
        in.readFully(buf);
        this.reserved = buf;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ftNts ]");
        buffer.append("\n");
        buffer.append("  size     = ");
        buffer.append(getDataSize());
        buffer.append("\n");
        buffer.append("  reserved = ");
        buffer.append(HexDump.toHex(this.reserved));
        buffer.append("\n");
        buffer.append("[/ftNts ]");
        buffer.append("\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(13);
        out.writeShort(this.reserved.length);
        out.write(this.reserved);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return this.reserved.length;
    }

    public short getSid() {
        return 13;
    }

    public Object clone() {
        NoteStructureSubRecord rec = new NoteStructureSubRecord();
        byte[] recdata = new byte[this.reserved.length];
        System.arraycopy(this.reserved, 0, recdata, 0, recdata.length);
        rec.reserved = recdata;
        return rec;
    }
}
