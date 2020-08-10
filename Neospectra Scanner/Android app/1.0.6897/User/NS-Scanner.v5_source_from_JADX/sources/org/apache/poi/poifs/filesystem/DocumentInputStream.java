package org.apache.poi.poifs.filesystem;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.poifs.storage.DataInputBlock;
import org.apache.poi.util.LittleEndianInput;

public final class DocumentInputStream extends InputStream implements LittleEndianInput {
    private static final int EOF = -1;
    private static final int SIZE_INT = 4;
    private static final int SIZE_LONG = 8;
    private static final int SIZE_SHORT = 2;
    private boolean _closed;
    private DataInputBlock _currentBlock;
    private int _current_offset;
    private POIFSDocument _document;
    private int _document_size;
    private int _marked_offset;

    public DocumentInputStream(DocumentEntry document) throws IOException {
        if (!(document instanceof DocumentNode)) {
            throw new IOException("Cannot open internal document storage");
        }
        this._current_offset = 0;
        this._marked_offset = 0;
        this._document_size = document.getSize();
        this._closed = false;
        this._document = ((DocumentNode) document).getDocument();
        this._currentBlock = getDataInputBlock(0);
    }

    public DocumentInputStream(POIFSDocument document) {
        this._current_offset = 0;
        this._marked_offset = 0;
        this._document_size = document.getSize();
        this._closed = false;
        this._document = document;
        this._currentBlock = getDataInputBlock(0);
    }

    public int available() {
        if (!this._closed) {
            return this._document_size - this._current_offset;
        }
        throw new IllegalStateException("cannot perform requested operation on a closed stream");
    }

    public void close() {
        this._closed = true;
    }

    public void mark(int ignoredReadlimit) {
        this._marked_offset = this._current_offset;
    }

    public boolean markSupported() {
        return true;
    }

    private DataInputBlock getDataInputBlock(int offset) {
        return this._document.getDataInputBlock(offset);
    }

    public int read() throws IOException {
        dieIfClosed();
        if (atEOD()) {
            return -1;
        }
        int result = this._currentBlock.readUByte();
        this._current_offset++;
        if (this._currentBlock.available() < 1) {
            this._currentBlock = getDataInputBlock(this._current_offset);
        }
        return result;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        dieIfClosed();
        if (b == null) {
            throw new IllegalArgumentException("buffer must not be null");
        } else if (off < 0 || len < 0 || b.length < off + len) {
            throw new IndexOutOfBoundsException("can't read past buffer boundaries");
        } else if (len == 0) {
            return 0;
        } else {
            if (atEOD()) {
                return -1;
            }
            int limit = Math.min(available(), len);
            readFully(b, off, limit);
            return limit;
        }
    }

    public void reset() {
        this._current_offset = this._marked_offset;
        this._currentBlock = getDataInputBlock(this._current_offset);
    }

    public long skip(long n) throws IOException {
        dieIfClosed();
        if (n < 0) {
            return 0;
        }
        int new_offset = this._current_offset + ((int) n);
        if (new_offset < this._current_offset) {
            new_offset = this._document_size;
        } else if (new_offset > this._document_size) {
            new_offset = this._document_size;
        }
        long rval = (long) (new_offset - this._current_offset);
        this._current_offset = new_offset;
        this._currentBlock = getDataInputBlock(this._current_offset);
        return rval;
    }

    private void dieIfClosed() throws IOException {
        if (this._closed) {
            throw new IOException("cannot perform requested operation on a closed stream");
        }
    }

    private boolean atEOD() {
        return this._current_offset == this._document_size;
    }

    private void checkAvaliable(int requestedSize) {
        if (this._closed) {
            throw new IllegalStateException("cannot perform requested operation on a closed stream");
        } else if (requestedSize > this._document_size - this._current_offset) {
            StringBuilder sb = new StringBuilder();
            sb.append("Buffer underrun - requested ");
            sb.append(requestedSize);
            sb.append(" bytes but ");
            sb.append(this._document_size - this._current_offset);
            sb.append(" was available");
            throw new RuntimeException(sb.toString());
        }
    }

    public byte readByte() {
        return (byte) readUByte();
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public void readFully(byte[] buf) {
        readFully(buf, 0, buf.length);
    }

    public short readShort() {
        return (short) readUShort();
    }

    public void readFully(byte[] buf, int off, int len) {
        int reqSize;
        checkAvaliable(len);
        int blockAvailable = this._currentBlock.available();
        if (blockAvailable > len) {
            this._currentBlock.readFully(buf, off, len);
            this._current_offset += len;
            return;
        }
        int remaining = len;
        int blockAvailable2 = blockAvailable;
        int writePos = off;
        while (true) {
            if (remaining <= 0) {
                break;
            }
            boolean blockIsExpiring = remaining >= blockAvailable2;
            if (blockIsExpiring) {
                reqSize = blockAvailable2;
            } else {
                reqSize = remaining;
            }
            this._currentBlock.readFully(buf, writePos, reqSize);
            remaining -= reqSize;
            writePos += reqSize;
            this._current_offset += reqSize;
            if (blockIsExpiring) {
                if (this._current_offset != this._document_size) {
                    this._currentBlock = getDataInputBlock(this._current_offset);
                    blockAvailable2 = this._currentBlock.available();
                } else if (remaining > 0) {
                    throw new IllegalStateException("reached end of document stream unexpectedly");
                } else {
                    this._currentBlock = null;
                }
            }
        }
    }

    public long readLong() {
        long result;
        long result2;
        checkAvaliable(8);
        int blockAvailable = this._currentBlock.available();
        if (blockAvailable > 8) {
            result = this._currentBlock.readLongLE();
        } else {
            DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
            if (blockAvailable == 8) {
                result2 = this._currentBlock.readLongLE();
            } else {
                result2 = nextBlock.readLongLE(this._currentBlock, blockAvailable);
            }
            this._currentBlock = nextBlock;
            result = result2;
        }
        this._current_offset += 8;
        return result;
    }

    public int readInt() {
        int result;
        int result2;
        checkAvaliable(4);
        int blockAvailable = this._currentBlock.available();
        if (blockAvailable > 4) {
            result = this._currentBlock.readIntLE();
        } else {
            DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
            if (blockAvailable == 4) {
                result2 = this._currentBlock.readIntLE();
            } else {
                result2 = nextBlock.readIntLE(this._currentBlock, blockAvailable);
            }
            this._currentBlock = nextBlock;
            result = result2;
        }
        this._current_offset += 4;
        return result;
    }

    public int readUShort() {
        int result;
        int result2;
        checkAvaliable(2);
        int blockAvailable = this._currentBlock.available();
        if (blockAvailable > 2) {
            result = this._currentBlock.readUShortLE();
        } else {
            DataInputBlock nextBlock = getDataInputBlock(this._current_offset + blockAvailable);
            if (blockAvailable == 2) {
                result2 = this._currentBlock.readUShortLE();
            } else {
                result2 = nextBlock.readUShortLE(this._currentBlock);
            }
            this._currentBlock = nextBlock;
            result = result2;
        }
        this._current_offset += 2;
        return result;
    }

    public int readUByte() {
        checkAvaliable(1);
        int result = this._currentBlock.readUByte();
        this._current_offset++;
        if (this._currentBlock.available() < 1) {
            this._currentBlock = getDataInputBlock(this._current_offset);
        }
        return result;
    }
}
