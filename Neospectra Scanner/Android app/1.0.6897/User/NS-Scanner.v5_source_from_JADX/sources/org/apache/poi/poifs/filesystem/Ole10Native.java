package org.apache.poi.poifs.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;

public class Ole10Native {
    public static final String OLE10_NATIVE = "\u0001Ole10Native";
    private final String command;
    private final byte[] dataBuffer;
    private final int dataSize;
    private final String fileName;
    private short flags1;
    private short flags2;
    private short flags3;
    private final String label;
    private final int totalSize;
    private byte[] unknown1;
    private byte[] unknown2;

    public static Ole10Native createFromEmbeddedOleObject(POIFSFileSystem poifs) throws IOException, Ole10NativeException {
        boolean plain;
        try {
            poifs.getRoot().getEntry("\u0001Ole10ItemName");
            plain = true;
        } catch (FileNotFoundException e) {
            plain = false;
        }
        DocumentInputStream dis = poifs.createDocumentInputStream(OLE10_NATIVE);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        IOUtils.copy(dis, bos);
        return new Ole10Native(bos.toByteArray(), 0, plain);
    }

    public Ole10Native(byte[] data, int offset) throws Ole10NativeException {
        this(data, offset, false);
    }

    public Ole10Native(byte[] data, int offset, boolean plain) throws Ole10NativeException {
        int ofs = offset;
        if (data.length < offset + 2) {
            throw new Ole10NativeException("data is too small");
        }
        this.totalSize = LittleEndian.getInt(data, ofs);
        int ofs2 = ofs + 4;
        if (plain) {
            this.dataBuffer = new byte[(this.totalSize - 4)];
            System.arraycopy(data, 4, this.dataBuffer, 0, this.dataBuffer.length);
            this.dataSize = this.totalSize - 4;
            byte[] oleLabel = new byte[8];
            System.arraycopy(this.dataBuffer, 0, oleLabel, 0, Math.min(this.dataBuffer.length, 8));
            StringBuilder sb = new StringBuilder();
            sb.append("ole-");
            sb.append(HexDump.toHex(oleLabel));
            this.label = sb.toString();
            this.fileName = this.label;
            this.command = this.label;
            return;
        }
        this.flags1 = LittleEndian.getShort(data, ofs2);
        int ofs3 = ofs2 + 2;
        int len = getStringLength(data, ofs3);
        this.label = StringUtil.getFromCompressedUnicode(data, ofs3, len - 1);
        int ofs4 = ofs3 + len;
        int len2 = getStringLength(data, ofs4);
        this.fileName = StringUtil.getFromCompressedUnicode(data, ofs4, len2 - 1);
        int ofs5 = ofs4 + len2;
        this.flags2 = LittleEndian.getShort(data, ofs5);
        int ofs6 = ofs5 + 2;
        int len3 = LittleEndian.getUnsignedByte(data, ofs6);
        this.unknown1 = new byte[len3];
        int ofs7 = ofs6 + len3;
        this.unknown2 = new byte[3];
        int ofs8 = ofs7 + 3;
        int len4 = getStringLength(data, ofs8);
        this.command = StringUtil.getFromCompressedUnicode(data, ofs8, len4 - 1);
        int ofs9 = ofs8 + len4;
        if ((this.totalSize + 4) - ofs9 > 4) {
            this.dataSize = LittleEndian.getInt(data, ofs9);
            int ofs10 = ofs9 + 4;
            if (this.dataSize > this.totalSize || this.dataSize < 0) {
                throw new Ole10NativeException("Invalid Ole10Native");
            }
            this.dataBuffer = new byte[this.dataSize];
            System.arraycopy(data, ofs10, this.dataBuffer, 0, this.dataSize);
            int ofs11 = ofs10 + this.dataSize;
            if (this.unknown1.length > 0) {
                this.flags3 = LittleEndian.getShort(data, ofs11);
                int ofs12 = ofs11 + 2;
                return;
            }
            this.flags3 = 0;
            return;
        }
        throw new Ole10NativeException("Invalid Ole10Native");
    }

    private static int getStringLength(byte[] data, int ofs) {
        int len = 0;
        while (len + ofs < data.length && data[ofs + len] != 0) {
            len++;
        }
        return len + 1;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public short getFlags1() {
        return this.flags1;
    }

    public String getLabel() {
        return this.label;
    }

    public String getFileName() {
        return this.fileName;
    }

    public short getFlags2() {
        return this.flags2;
    }

    public byte[] getUnknown1() {
        return this.unknown1;
    }

    public byte[] getUnknown2() {
        return this.unknown2;
    }

    public String getCommand() {
        return this.command;
    }

    public int getDataSize() {
        return this.dataSize;
    }

    public byte[] getDataBuffer() {
        return this.dataBuffer;
    }

    public short getFlags3() {
        return this.flags3;
    }
}
