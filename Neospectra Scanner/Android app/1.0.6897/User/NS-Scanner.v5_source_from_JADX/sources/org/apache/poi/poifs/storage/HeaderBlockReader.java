package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;

public final class HeaderBlockReader {
    private final int _bat_count;
    private final byte[] _data;
    private final int _property_start;
    private final int _sbat_count;
    private final int _sbat_start;
    private final int _xbat_count;
    private final int _xbat_start;
    private final POIFSBigBlockSize bigBlockSize;

    public HeaderBlockReader(InputStream stream) throws IOException {
        byte[] blockStart = new byte[32];
        int bsCount = IOUtils.readFully(stream, blockStart);
        if (bsCount != 32) {
            throw alertShortRead(bsCount, 32);
        }
        long signature = LittleEndian.getLong(blockStart, 0);
        if (signature != HeaderBlockConstants._signature) {
            byte[] OOXML_FILE_HEADER = POIFSConstants.OOXML_FILE_HEADER;
            if (blockStart[0] == OOXML_FILE_HEADER[0] && blockStart[1] == OOXML_FILE_HEADER[1] && blockStart[2] == OOXML_FILE_HEADER[2] && blockStart[3] == OOXML_FILE_HEADER[3]) {
                throw new OfficeXmlFileException("The supplied data appears to be in the Office 2007+ XML. You are calling the part of POI that deals with OLE2 Office Documents. You need to call a different part of POI to process this data (eg XSSF instead of HSSF)");
            } else if ((-31525197391593473L & signature) == 4503608217567241L) {
                throw new IllegalArgumentException("The supplied data appears to be in BIFF2 format.  POI only supports BIFF8 format");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid header signature; read ");
                sb.append(longToHex(signature));
                sb.append(", expected ");
                sb.append(longToHex(HeaderBlockConstants._signature));
                throw new IOException(sb.toString());
            }
        } else {
            byte b = blockStart[30];
            if (b == 9) {
                this.bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
            } else if (b != 12) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unsupported blocksize  (2^");
                sb2.append(blockStart[30]);
                sb2.append("). Expected 2^9 or 2^12.");
                throw new IOException(sb2.toString());
            } else {
                this.bigBlockSize = POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS;
            }
            this._data = new byte[this.bigBlockSize.getBigBlockSize()];
            System.arraycopy(blockStart, 0, this._data, 0, blockStart.length);
            int byte_count = IOUtils.readFully(stream, this._data, blockStart.length, this._data.length - blockStart.length);
            if (byte_count + bsCount != this.bigBlockSize.getBigBlockSize()) {
                throw alertShortRead(byte_count, this.bigBlockSize.getBigBlockSize());
            }
            this._bat_count = getInt(44, this._data);
            this._property_start = getInt(48, this._data);
            this._sbat_start = getInt(60, this._data);
            this._sbat_count = getInt(64, this._data);
            this._xbat_start = getInt(68, this._data);
            this._xbat_count = getInt(72, this._data);
        }
    }

    private static int getInt(int offset, byte[] data) {
        return LittleEndian.getInt(data, offset);
    }

    private static String longToHex(long value) {
        return new String(HexDump.longToHex(value));
    }

    private static IOException alertShortRead(int pRead, int expectedReadSize) {
        int read;
        if (pRead < 0) {
            read = 0;
        } else {
            read = pRead;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" byte");
        sb.append(read == 1 ? "" : "s");
        String type = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Unable to read entire header; ");
        sb2.append(read);
        sb2.append(type);
        sb2.append(" read; expected ");
        sb2.append(expectedReadSize);
        sb2.append(" bytes");
        return new IOException(sb2.toString());
    }

    public int getPropertyStart() {
        return this._property_start;
    }

    public int getSBATStart() {
        return this._sbat_start;
    }

    public int getSBATCount() {
        return this._sbat_count;
    }

    public int getBATCount() {
        return this._bat_count;
    }

    public int[] getBATArray() {
        int[] result = new int[109];
        int offset = 76;
        for (int j = 0; j < 109; j++) {
            result[j] = LittleEndian.getInt(this._data, offset);
            offset += 4;
        }
        return result;
    }

    public int getXBATCount() {
        return this._xbat_count;
    }

    public int getXBATIndex() {
        return this._xbat_start;
    }

    public POIFSBigBlockSize getBigBlockSize() {
        return this.bigBlockSize;
    }
}
