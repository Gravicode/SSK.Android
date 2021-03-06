package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class SaveRecalcRecord extends StandardRecord {
    public static final short sid = 95;
    private short field_1_recalc;

    public SaveRecalcRecord() {
    }

    public SaveRecalcRecord(RecordInputStream in) {
        this.field_1_recalc = in.readShort();
    }

    public void setRecalc(boolean recalc) {
        int i = 1;
        if (!recalc) {
            i = 0;
        }
        this.field_1_recalc = (short) i;
    }

    public boolean getRecalc() {
        return this.field_1_recalc == 1;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[SAVERECALC]\n");
        buffer.append("    .recalc         = ");
        buffer.append(getRecalc());
        buffer.append("\n");
        buffer.append("[/SAVERECALC]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this.field_1_recalc);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return 2;
    }

    public short getSid() {
        return 95;
    }

    public Object clone() {
        SaveRecalcRecord rec = new SaveRecalcRecord();
        rec.field_1_recalc = this.field_1_recalc;
        return rec;
    }
}
