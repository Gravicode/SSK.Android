package org.apache.poi.hssf.record.pivottable;

import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.StandardRecord;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class ViewSourceRecord extends StandardRecord {
    public static final short sid = 227;

    /* renamed from: vs */
    private int f869vs;

    public ViewSourceRecord(RecordInputStream in) {
        this.f869vs = in.readShort();
    }

    /* access modifiers changed from: protected */
    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.f869vs);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return 2;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SXVS]\n");
        buffer.append("    .vs      =");
        buffer.append(HexDump.shortToHex(this.f869vs));
        buffer.append(10);
        buffer.append("[/SXVS]\n");
        return buffer.toString();
    }
}
