package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianOutput;

public final class DrawingRecord extends StandardRecord {
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short sid = 236;
    private byte[] contd;
    private byte[] recordData;

    public DrawingRecord() {
        this.recordData = EMPTY_BYTE_ARRAY;
    }

    public DrawingRecord(RecordInputStream in) {
        this.recordData = in.readRemainder();
    }

    public void processContinueRecord(byte[] record) {
        this.contd = record;
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this.recordData);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return this.recordData.length;
    }

    public short getSid() {
        return 236;
    }

    public byte[] getData() {
        if (this.contd == null) {
            return this.recordData;
        }
        byte[] newBuffer = new byte[(this.recordData.length + this.contd.length)];
        System.arraycopy(this.recordData, 0, newBuffer, 0, this.recordData.length);
        System.arraycopy(this.contd, 0, newBuffer, this.recordData.length, this.contd.length);
        return newBuffer;
    }

    public void setData(byte[] thedata) {
        if (thedata == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        this.recordData = thedata;
    }

    public Object clone() {
        DrawingRecord rec = new DrawingRecord();
        rec.recordData = (byte[]) this.recordData.clone();
        if (this.contd != null) {
            rec.contd = (byte[]) this.contd.clone();
        }
        return rec;
    }
}
