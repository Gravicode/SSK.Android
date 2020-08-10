package org.apache.poi.ddf;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.poi.util.LittleEndian;

public abstract class EscherRecord {
    private short _options;
    private short _recordId;

    static class EscherRecordHeader {
        private short options;
        private short recordId;
        private int remainingBytes;

        private EscherRecordHeader() {
        }

        public static EscherRecordHeader readHeader(byte[] data, int offset) {
            EscherRecordHeader header = new EscherRecordHeader();
            header.options = LittleEndian.getShort(data, offset);
            header.recordId = LittleEndian.getShort(data, offset + 2);
            header.remainingBytes = LittleEndian.getInt(data, offset + 4);
            return header;
        }

        public short getOptions() {
            return this.options;
        }

        public short getRecordId() {
            return this.recordId;
        }

        public int getRemainingBytes() {
            return this.remainingBytes;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("EscherRecordHeader{options=");
            sb.append(this.options);
            sb.append(", recordId=");
            sb.append(this.recordId);
            sb.append(", remainingBytes=");
            sb.append(this.remainingBytes);
            sb.append(VectorFormat.DEFAULT_SUFFIX);
            return sb.toString();
        }
    }

    public abstract int fillFields(byte[] bArr, int i, EscherRecordFactory escherRecordFactory);

    public abstract String getRecordName();

    public abstract int getRecordSize();

    public abstract int serialize(int i, byte[] bArr, EscherSerializationListener escherSerializationListener);

    /* access modifiers changed from: protected */
    public int fillFields(byte[] data, EscherRecordFactory f) {
        return fillFields(data, 0, f);
    }

    /* access modifiers changed from: protected */
    public int readHeader(byte[] data, int offset) {
        EscherRecordHeader header = EscherRecordHeader.readHeader(data, offset);
        this._options = header.getOptions();
        this._recordId = header.getRecordId();
        return header.getRemainingBytes();
    }

    public boolean isContainerRecord() {
        return (this._options & 15) == 15;
    }

    public short getOptions() {
        return this._options;
    }

    public void setOptions(short options) {
        this._options = options;
    }

    public byte[] serialize() {
        byte[] retval = new byte[getRecordSize()];
        serialize(0, retval);
        return retval;
    }

    public int serialize(int offset, byte[] data) {
        return serialize(offset, data, new NullEscherSerializationListener());
    }

    public short getRecordId() {
        return this._recordId;
    }

    public void setRecordId(short recordId) {
        this._recordId = recordId;
    }

    public List<EscherRecord> getChildRecords() {
        return Collections.emptyList();
    }

    public void setChildRecords(List<EscherRecord> list) {
        throw new UnsupportedOperationException("This record does not support child records.");
    }

    public Object clone() {
        StringBuilder sb = new StringBuilder();
        sb.append("The class ");
        sb.append(getClass().getName());
        sb.append(" needs to define a clone method");
        throw new RuntimeException(sb.toString());
    }

    public EscherRecord getChild(int index) {
        return (EscherRecord) getChildRecords().get(index);
    }

    public void display(PrintWriter w, int indent) {
        for (int i = 0; i < indent * 4; i++) {
            w.print(' ');
        }
        w.println(getRecordName());
    }

    public short getInstance() {
        return (short) (this._options >> 4);
    }
}
