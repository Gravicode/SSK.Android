package org.apache.poi.util;

import java.io.IOException;
import java.io.InputStream;

public class BlockingInputStream extends InputStream {

    /* renamed from: is */
    protected InputStream f890is;

    public BlockingInputStream(InputStream is) {
        this.f890is = is;
    }

    public int available() throws IOException {
        return this.f890is.available();
    }

    public void close() throws IOException {
        this.f890is.close();
    }

    public void mark(int readLimit) {
        this.f890is.mark(readLimit);
    }

    public boolean markSupported() {
        return this.f890is.markSupported();
    }

    public int read() throws IOException {
        return this.f890is.read();
    }

    public int read(byte[] bf) throws IOException {
        int i = 0;
        int b = 4611;
        while (i < bf.length) {
            b = this.f890is.read();
            if (b == -1) {
                break;
            }
            int i2 = i + 1;
            bf[i] = (byte) b;
            i = i2;
        }
        if (i == 0 && b == -1) {
            return -1;
        }
        return i;
    }

    public int read(byte[] bf, int s, int l) throws IOException {
        return this.f890is.read(bf, s, l);
    }

    public void reset() throws IOException {
        this.f890is.reset();
    }

    public long skip(long n) throws IOException {
        return this.f890is.skip(n);
    }
}
