package org.apache.poi.hpsf;

import org.apache.poi.util.HexDump;

public class ClassID {
    public static final int LENGTH = 16;
    protected byte[] bytes;

    public ClassID(byte[] src, int offset) {
        read(src, offset);
    }

    public ClassID() {
        this.bytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            this.bytes[i] = 0;
        }
    }

    public int length() {
        return 16;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes2) {
        for (int i = 0; i < this.bytes.length; i++) {
            this.bytes[i] = bytes2[i];
        }
    }

    public byte[] read(byte[] src, int offset) {
        this.bytes = new byte[16];
        this.bytes[0] = src[offset + 3];
        this.bytes[1] = src[offset + 2];
        this.bytes[2] = src[offset + 1];
        this.bytes[3] = src[offset + 0];
        this.bytes[4] = src[offset + 5];
        this.bytes[5] = src[offset + 4];
        this.bytes[6] = src[offset + 7];
        this.bytes[7] = src[offset + 6];
        for (int i = 8; i < 16; i++) {
            this.bytes[i] = src[i + offset];
        }
        return this.bytes;
    }

    public void write(byte[] dst, int offset) throws ArrayStoreException {
        if (dst.length < 16) {
            StringBuilder sb = new StringBuilder();
            sb.append("Destination byte[] must have room for at least 16 bytes, but has a length of only ");
            sb.append(dst.length);
            sb.append(".");
            throw new ArrayStoreException(sb.toString());
        }
        dst[offset + 0] = this.bytes[3];
        dst[offset + 1] = this.bytes[2];
        dst[offset + 2] = this.bytes[1];
        dst[offset + 3] = this.bytes[0];
        dst[offset + 4] = this.bytes[5];
        dst[offset + 5] = this.bytes[4];
        dst[offset + 6] = this.bytes[7];
        dst[offset + 7] = this.bytes[6];
        for (int i = 8; i < 16; i++) {
            dst[i + offset] = this.bytes[i];
        }
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ClassID)) {
            return false;
        }
        ClassID cid = (ClassID) o;
        if (this.bytes.length != cid.bytes.length) {
            return false;
        }
        for (int i = 0; i < this.bytes.length; i++) {
            if (this.bytes[i] != cid.bytes[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return new String(this.bytes).hashCode();
    }

    public String toString() {
        StringBuffer sbClassId = new StringBuffer(38);
        sbClassId.append('{');
        for (int i = 0; i < 16; i++) {
            sbClassId.append(HexDump.toHex(this.bytes[i]));
            if (i == 3 || i == 5 || i == 7 || i == 9) {
                sbClassId.append('-');
            }
        }
        sbClassId.append('}');
        return sbClassId.toString();
    }
}
