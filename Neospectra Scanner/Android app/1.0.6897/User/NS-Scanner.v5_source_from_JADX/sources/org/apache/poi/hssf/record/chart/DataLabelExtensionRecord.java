package org.apache.poi.hssf.record.chart;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class DataLabelExtensionRecord extends StandardRecord {
    public static final short sid = 2154;
    private int grbitFrt;

    /* renamed from: rt */
    private int f846rt;
    private byte[] unused = new byte[8];

    public DataLabelExtensionRecord(RecordInputStream in) {
        this.f846rt = in.readShort();
        this.grbitFrt = in.readShort();
        in.readFully(this.unused);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return 12;
    }

    public short getSid() {
        return sid;
    }

    /* access modifiers changed from: protected */
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.f846rt);
        out.writeShort(this.grbitFrt);
        out.write(this.unused);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[DATALABEXT]\n");
        buffer.append("    .rt      =");
        buffer.append(HexDump.shortToHex(this.f846rt));
        buffer.append(10);
        buffer.append("    .grbitFrt=");
        buffer.append(HexDump.shortToHex(this.grbitFrt));
        buffer.append(10);
        buffer.append("    .unused  =");
        buffer.append(HexDump.toHex(this.unused));
        buffer.append(10);
        buffer.append("[/DATALABEXT]\n");
        return buffer.toString();
    }
}
