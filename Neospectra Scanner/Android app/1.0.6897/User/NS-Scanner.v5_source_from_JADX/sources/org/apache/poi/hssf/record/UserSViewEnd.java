package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class UserSViewEnd extends StandardRecord {
    public static final short sid = 427;
    private byte[] _rawData;

    public UserSViewEnd(byte[] data) {
        this._rawData = data;
    }

    public UserSViewEnd(RecordInputStream in) {
        this._rawData = in.readRemainder();
    }

    public void serialize(LittleEndianOutput out) {
        out.write(this._rawData);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return this._rawData.length;
    }

    public short getSid() {
        return sid;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("USERSVIEWEND");
        sb.append("] (0x");
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Integer.toHexString(427).toUpperCase());
        sb2.append(")\n");
        sb.append(sb2.toString());
        sb.append("  rawData=");
        sb.append(HexDump.toHex(this._rawData));
        sb.append("\n");
        sb.append("[/");
        sb.append("USERSVIEWEND");
        sb.append("]\n");
        return sb.toString();
    }

    public Object clone() {
        return cloneViaReserialise();
    }
}
