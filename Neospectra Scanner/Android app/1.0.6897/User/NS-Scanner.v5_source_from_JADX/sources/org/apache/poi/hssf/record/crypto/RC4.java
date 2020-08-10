package org.apache.poi.hssf.record.crypto;

import org.apache.poi.util.HexDump;

final class RC4 {

    /* renamed from: _i */
    private int f849_i;

    /* renamed from: _j */
    private int f850_j;

    /* renamed from: _s */
    private final byte[] f851_s = new byte[256];

    public RC4(byte[] key) {
        int key_length = key.length;
        for (int i = 0; i < 256; i++) {
            this.f851_s[i] = (byte) i;
        }
        int j = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            j = (key[i2 % key_length] + j + this.f851_s[i2]) & 255;
            byte temp = this.f851_s[i2];
            this.f851_s[i2] = this.f851_s[j];
            this.f851_s[j] = temp;
        }
        this.f849_i = 0;
        this.f850_j = 0;
    }

    public byte output() {
        this.f849_i = (this.f849_i + 1) & 255;
        this.f850_j = (this.f850_j + this.f851_s[this.f849_i]) & 255;
        byte temp = this.f851_s[this.f849_i];
        this.f851_s[this.f849_i] = this.f851_s[this.f850_j];
        this.f851_s[this.f850_j] = temp;
        return this.f851_s[(this.f851_s[this.f849_i] + this.f851_s[this.f850_j]) & 255];
    }

    public void encrypt(byte[] in) {
        for (int i = 0; i < in.length; i++) {
            in[i] = (byte) (in[i] ^ output());
        }
    }

    public void encrypt(byte[] in, int offset, int len) {
        int end = offset + len;
        for (int i = offset; i < end; i++) {
            in[i] = (byte) (in[i] ^ output());
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getName());
        sb.append(" [");
        sb.append("i=");
        sb.append(this.f849_i);
        sb.append(" j=");
        sb.append(this.f850_j);
        sb.append("]");
        sb.append("\n");
        sb.append(HexDump.dump(this.f851_s, 0, 0));
        return sb.toString();
    }
}
