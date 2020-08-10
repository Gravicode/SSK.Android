package org.apache.poi.hssf.record;

import org.apache.poi.util.HexDump;
import org.apache.poi.util.LittleEndianOutput;

public final class FilePassRecord extends StandardRecord {
    private static final int ENCRYPTION_OTHER = 1;
    private static final int ENCRYPTION_OTHER_CAPI_2 = 2;
    private static final int ENCRYPTION_OTHER_CAPI_3 = 3;
    private static final int ENCRYPTION_OTHER_RC4 = 1;
    private static final int ENCRYPTION_XOR = 0;
    public static final short sid = 47;
    private byte[] _docId;
    private int _encryptionInfo;
    private int _encryptionType;
    private int _minorVersionNo;
    private byte[] _saltData;
    private byte[] _saltHash;

    public FilePassRecord(RecordInputStream in) {
        this._encryptionType = in.readUShort();
        switch (this._encryptionType) {
            case 0:
                throw new RecordFormatException("HSSF does not currently support XOR obfuscation");
            case 1:
                this._encryptionInfo = in.readUShort();
                switch (this._encryptionInfo) {
                    case 1:
                        this._minorVersionNo = in.readUShort();
                        if (this._minorVersionNo != 1) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Unexpected VersionInfo number for RC4Header ");
                            sb.append(this._minorVersionNo);
                            throw new RecordFormatException(sb.toString());
                        }
                        this._docId = read(in, 16);
                        this._saltData = read(in, 16);
                        this._saltHash = read(in, 16);
                        return;
                    case 2:
                    case 3:
                        throw new RecordFormatException("HSSF does not currently support CryptoAPI encryption");
                    default:
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Unknown encryption info ");
                        sb2.append(this._encryptionInfo);
                        throw new RecordFormatException(sb2.toString());
                }
            default:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Unknown encryption type ");
                sb3.append(this._encryptionType);
                throw new RecordFormatException(sb3.toString());
        }
    }

    private static byte[] read(RecordInputStream in, int size) {
        byte[] result = new byte[size];
        in.readFully(result);
        return result;
    }

    public void serialize(LittleEndianOutput out) {
        out.writeShort(this._encryptionType);
        out.writeShort(this._encryptionInfo);
        out.writeShort(this._minorVersionNo);
        out.write(this._docId);
        out.write(this._saltData);
        out.write(this._saltHash);
    }

    /* access modifiers changed from: protected */
    public int getDataSize() {
        return 54;
    }

    public byte[] getDocId() {
        return (byte[]) this._docId.clone();
    }

    public void setDocId(byte[] docId) {
        this._docId = (byte[]) docId.clone();
    }

    public byte[] getSaltData() {
        return (byte[]) this._saltData.clone();
    }

    public void setSaltData(byte[] saltData) {
        this._saltData = (byte[]) saltData.clone();
    }

    public byte[] getSaltHash() {
        return (byte[]) this._saltHash.clone();
    }

    public void setSaltHash(byte[] saltHash) {
        this._saltHash = (byte[]) saltHash.clone();
    }

    public short getSid() {
        return 47;
    }

    public Object clone() {
        return this;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[FILEPASS]\n");
        buffer.append("    .type = ");
        buffer.append(HexDump.shortToHex(this._encryptionType));
        buffer.append("\n");
        buffer.append("    .info = ");
        buffer.append(HexDump.shortToHex(this._encryptionInfo));
        buffer.append("\n");
        buffer.append("    .ver  = ");
        buffer.append(HexDump.shortToHex(this._minorVersionNo));
        buffer.append("\n");
        buffer.append("    .docId= ");
        buffer.append(HexDump.toHex(this._docId));
        buffer.append("\n");
        buffer.append("    .salt = ");
        buffer.append(HexDump.toHex(this._saltData));
        buffer.append("\n");
        buffer.append("    .hash = ");
        buffer.append(HexDump.toHex(this._saltHash));
        buffer.append("\n");
        buffer.append("[/FILEPASS]\n");
        return buffer.toString();
    }
}
