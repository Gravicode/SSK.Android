package org.apache.poi.poifs.storage;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.POILogFactory;
import org.apache.poi.util.POILogger;

public class RawDataBlock implements ListManagedBlock {
    private static POILogger log = POILogFactory.getLogger(RawDataBlock.class);
    private byte[] _data;
    private boolean _eof;
    private boolean _hasData;

    public RawDataBlock(InputStream stream) throws IOException {
        this(stream, 512);
    }

    public RawDataBlock(InputStream stream, int blockSize) throws IOException {
        this._data = new byte[blockSize];
        int count = IOUtils.readFully(stream, this._data);
        this._hasData = count > 0;
        if (count == -1) {
            this._eof = true;
        } else if (count != blockSize) {
            this._eof = true;
            StringBuilder sb = new StringBuilder();
            sb.append(" byte");
            sb.append(count == 1 ? "" : "s");
            String type = sb.toString();
            POILogger pOILogger = log;
            int i = POILogger.ERROR;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to read entire block; ");
            sb2.append(count);
            sb2.append(type);
            sb2.append(" read before EOF; expected ");
            sb2.append(blockSize);
            sb2.append(" bytes. Your document ");
            sb2.append("was either written by software that ");
            sb2.append("ignores the spec, or has been truncated!");
            pOILogger.log(i, (Object) sb2.toString());
        } else {
            this._eof = false;
        }
    }

    public boolean eof() {
        return this._eof;
    }

    public boolean hasData() {
        return this._hasData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RawDataBlock of size ");
        sb.append(this._data.length);
        return sb.toString();
    }

    public byte[] getData() throws IOException {
        if (hasData()) {
            return this._data;
        }
        throw new IOException("Cannot return empty data");
    }

    public int getBigBlockSize() {
        return this._data.length;
    }
}
