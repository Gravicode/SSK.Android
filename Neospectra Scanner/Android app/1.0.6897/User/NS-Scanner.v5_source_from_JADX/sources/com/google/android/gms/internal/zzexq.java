package com.google.android.gms.internal;

import android.util.Log;
import java.io.IOException;
import java.io.InputStream;

public final class zzexq {
    private static final Runtime zzolr = Runtime.getRuntime();
    private byte[] buffer = new byte[262144];
    private final InputStream zzols;
    private int zzolt = 0;
    private boolean zzolu = false;
    private boolean zzolv = true;

    public zzexq(InputStream inputStream, int i) {
        this.zzols = inputStream;
    }

    private final int zzim(int i) {
        int max = Math.max(this.buffer.length << 1, i);
        long maxMemory = zzolr.maxMemory() - (zzolr.totalMemory() - zzolr.freeMemory());
        if (!this.zzolv || ((long) max) >= maxMemory) {
            Log.w("AdaptiveStreamBuffer", "Turning off adaptive buffer resizing to conserve memory.");
        } else {
            try {
                byte[] bArr = new byte[max];
                System.arraycopy(this.buffer, 0, bArr, 0, this.zzolt);
                this.buffer = bArr;
            } catch (OutOfMemoryError e) {
                Log.w("AdaptiveStreamBuffer", "Turning off adaptive buffer resizing due to low memory.");
                this.zzolv = false;
            }
        }
        return this.buffer.length;
    }

    public final int available() {
        return this.zzolt;
    }

    public final void close() throws IOException {
        this.zzols.close();
    }

    public final boolean isFinished() {
        return this.zzolu;
    }

    public final byte[] zzcmg() {
        return this.buffer;
    }

    public final int zzik(int i) throws IOException {
        if (i <= this.zzolt) {
            this.zzolt -= i;
            System.arraycopy(this.buffer, i, this.buffer, 0, this.zzolt);
            return i;
        }
        this.zzolt = 0;
        int i2 = this.zzolt;
        while (i2 < i) {
            long skip = this.zzols.skip((long) (i - i2));
            int i3 = (skip > 0 ? 1 : (skip == 0 ? 0 : -1));
            if (i3 <= 0) {
                if (i3 == 0) {
                    if (this.zzols.read() == -1) {
                        break;
                    }
                    i2++;
                } else {
                    continue;
                }
            } else {
                i2 = (int) (((long) i2) + skip);
            }
        }
        return i2;
    }

    public final int zzil(int i) throws IOException {
        if (i > this.buffer.length) {
            i = Math.min(i, zzim(i));
        }
        while (true) {
            if (this.zzolt >= i) {
                break;
            }
            int read = this.zzols.read(this.buffer, this.zzolt, i - this.zzolt);
            if (read == -1) {
                this.zzolu = true;
                break;
            }
            this.zzolt += read;
        }
        return this.zzolt;
    }
}
