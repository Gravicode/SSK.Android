package org.apache.poi.hssf.record;

import java.util.Arrays;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianOutput;
import org.apache.poi.util.StringUtil;

public final class WriteAccessRecord extends StandardRecord {
    private static final int DATA_SIZE = 112;
    private static final byte[] PADDING = new byte[112];
    private static final byte PAD_CHAR = 32;
    public static final short sid = 92;
    private String field_1_username;

    static {
        Arrays.fill(PADDING, 32);
    }

    public WriteAccessRecord() {
        setUsername("");
    }

    public WriteAccessRecord(RecordInputStream in) {
        String rawText;
        if (in.remaining() > 112) {
            StringBuilder sb = new StringBuilder();
            sb.append("Expected data size (112) but got (");
            sb.append(in.remaining());
            sb.append(")");
            throw new RecordFormatException(sb.toString());
        }
        int nChars = in.readUShort();
        int is16BitFlag = in.readUByte();
        if (nChars > 112 || (is16BitFlag & 254) != 0) {
            byte[] data = new byte[(in.remaining() + 3)];
            LittleEndian.putUShort(data, 0, nChars);
            LittleEndian.putByte(data, 2, is16BitFlag);
            in.readFully(data, 3, data.length - 3);
            setUsername(new String(data).trim());
            return;
        }
        if ((is16BitFlag & 1) == 0) {
            rawText = StringUtil.readCompressedUnicode(in, nChars);
        } else {
            rawText = StringUtil.readUnicodeLE(in, nChars);
        }
        this.field_1_username = rawText.trim();
        for (int padSize = in.remaining(); padSize > 0; padSize--) {
            in.readUByte();
        }
    }

    public void setUsername(String username) {
        if (112 - ((username.length() * (StringUtil.hasMultibyte(username) ? 2 : 1)) + 3) < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Name is too long: ");
            sb.append(username);
            throw new IllegalArgumentException(sb.toString());
        }
        this.field_1_username = username;
    }

    public String getUsername() {
        return this.field_1_username;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[WRITEACCESS]\n");
        buffer.append("    .name = ");
        buffer.append(this.field_1_username.toString());
        buffer.append("\n");
        buffer.append("[/WRITEACCESS]\n");
        return buffer.toString();
    }

    public void serialize(LittleEndianOutput out) {
        String username = getUsername();
        boolean is16bit = StringUtil.hasMultibyte(username);
        out.writeShort(username.length());
        out.writeByte(is16bit);
        if (is16bit) {
            StringUtil.putUnicodeLE(username, out);
        } else {
            StringUtil.putCompressedUnicode(username, out);
        }
        out.write(PADDING, 0, 112 - ((username.length() * (is16bit ? 2 : 1)) + 3));
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return 112;
    }

    public short getSid() {
        return 92;
    }
}
