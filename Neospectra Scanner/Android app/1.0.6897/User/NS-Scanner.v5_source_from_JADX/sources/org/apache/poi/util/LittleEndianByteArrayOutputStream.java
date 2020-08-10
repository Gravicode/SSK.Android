package org.apache.poi.util;

public final class LittleEndianByteArrayOutputStream implements LittleEndianOutput, DelayableLittleEndianOutput {
    private final byte[] _buf;
    private final int _endIndex;
    private int _writeIndex;

    public LittleEndianByteArrayOutputStream(byte[] buf, int startOffset, int maxWriteLen) {
        if (startOffset < 0 || startOffset > buf.length) {
            StringBuilder sb = new StringBuilder();
            sb.append("Specified startOffset (");
            sb.append(startOffset);
            sb.append(") is out of allowable range (0..");
            sb.append(buf.length);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this._buf = buf;
        this._writeIndex = startOffset;
        this._endIndex = startOffset + maxWriteLen;
        if (this._endIndex < startOffset || this._endIndex > buf.length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("calculated end index (");
            sb2.append(this._endIndex);
            sb2.append(") is out of allowable range (");
            sb2.append(this._writeIndex);
            sb2.append("..");
            sb2.append(buf.length);
            sb2.append(")");
            throw new IllegalArgumentException(sb2.toString());
        }
    }

    public LittleEndianByteArrayOutputStream(byte[] buf, int startOffset) {
        this(buf, startOffset, buf.length - startOffset);
    }

    private void checkPosition(int i) {
        if (i > this._endIndex - this._writeIndex) {
            throw new RuntimeException("Buffer overrun");
        }
    }

    public void writeByte(int v) {
        checkPosition(1);
        byte[] bArr = this._buf;
        int i = this._writeIndex;
        this._writeIndex = i + 1;
        bArr[i] = (byte) v;
    }

    public void writeDouble(double v) {
        writeLong(Double.doubleToLongBits(v));
    }

    public void writeInt(int v) {
        checkPosition(4);
        int i = this._writeIndex;
        int i2 = i + 1;
        this._buf[i] = (byte) ((v >>> 0) & 255);
        int i3 = i2 + 1;
        this._buf[i2] = (byte) ((v >>> 8) & 255);
        int i4 = i3 + 1;
        this._buf[i3] = (byte) ((v >>> 16) & 255);
        int i5 = i4 + 1;
        this._buf[i4] = (byte) ((v >>> 24) & 255);
        this._writeIndex = i5;
    }

    public void writeLong(long v) {
        writeInt((int) (v >> 0));
        writeInt((int) (v >> 32));
    }

    public void writeShort(int v) {
        checkPosition(2);
        int i = this._writeIndex;
        int i2 = i + 1;
        this._buf[i] = (byte) ((v >>> 0) & 255);
        int i3 = i2 + 1;
        this._buf[i2] = (byte) ((v >>> 8) & 255);
        this._writeIndex = i3;
    }

    public void write(byte[] b) {
        int len = b.length;
        checkPosition(len);
        System.arraycopy(b, 0, this._buf, this._writeIndex, len);
        this._writeIndex += len;
    }

    public void write(byte[] b, int offset, int len) {
        checkPosition(len);
        System.arraycopy(b, offset, this._buf, this._writeIndex, len);
        this._writeIndex += len;
    }

    public int getWriteIndex() {
        return this._writeIndex;
    }

    public LittleEndianOutput createDelayedOutput(int size) {
        checkPosition(size);
        LittleEndianOutput result = new LittleEndianByteArrayOutputStream(this._buf, this._writeIndex, size);
        this._writeIndex += size;
        return result;
    }
}
