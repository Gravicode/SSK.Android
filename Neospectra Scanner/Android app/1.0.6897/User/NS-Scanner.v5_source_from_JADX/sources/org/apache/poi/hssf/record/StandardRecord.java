package org.apache.poi.hssf.record;

import org.apache.poi.util.LittleEndianByteArrayOutputStream;
import org.apache.poi.util.LittleEndianOutput;

public abstract class StandardRecord extends Record {
    /* access modifiers changed from: protected */
    public abstract int getDataSize();

    /* access modifiers changed from: protected */
    public abstract void serialize(LittleEndianOutput littleEndianOutput);

    public final int getRecordSize() {
        return getDataSize() + 4;
    }

    public final int serialize(int offset, byte[] data) {
        int dataSize = getDataSize();
        int recSize = dataSize + 4;
        LittleEndianByteArrayOutputStream out = new LittleEndianByteArrayOutputStream(data, offset, recSize);
        out.writeShort(getSid());
        out.writeShort(dataSize);
        serialize(out);
        if (out.getWriteIndex() - offset == recSize) {
            return recSize;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Error in serialization of (");
        sb.append(getClass().getName());
        sb.append("): ");
        sb.append("Incorrect number of bytes written - expected ");
        sb.append(recSize);
        sb.append(" but got ");
        sb.append(out.getWriteIndex() - offset);
        throw new IllegalStateException(sb.toString());
    }
}
