package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

public abstract class BitsStreamGenerator implements RandomGenerator, Serializable {
    private static final long serialVersionUID = 20130104;
    private double nextGaussian = Double.NaN;

    /* access modifiers changed from: protected */
    public abstract int next(int i);

    public abstract void setSeed(int i);

    public abstract void setSeed(long j);

    public abstract void setSeed(int[] iArr);

    public boolean nextBoolean() {
        return next(1) != 0;
    }

    public double nextDouble() {
        return ((double) (((long) next(26)) | (((long) next(26)) << 26))) * 2.220446049250313E-16d;
    }

    public float nextFloat() {
        return ((float) next(23)) * 1.1920929E-7f;
    }

    public double nextGaussian() {
        if (Double.isNaN(this.nextGaussian)) {
            double alpha = 6.283185307179586d * nextDouble();
            double r = FastMath.sqrt(FastMath.log(nextDouble()) * -2.0d);
            double random = FastMath.cos(alpha) * r;
            this.nextGaussian = FastMath.sin(alpha) * r;
            return random;
        }
        double random2 = this.nextGaussian;
        this.nextGaussian = Double.NaN;
        return random2;
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int n) throws IllegalArgumentException {
        int bits;
        int val;
        if (n <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(n));
        } else if (((-n) & n) == n) {
            return (int) ((((long) n) * ((long) next(31))) >> 31);
        } else {
            do {
                bits = next(31);
                val = bits % n;
            } while ((bits - val) + (n - 1) < 0);
            return val;
        }
    }

    public long nextLong() {
        return (((long) next(32)) << 32) | (((long) next(32)) & 4294967295L);
    }

    public long nextLong(long n) throws IllegalArgumentException {
        long bits;
        long val;
        if (n > 0) {
            do {
                bits = (((long) next(31)) << 32) | (((long) next(32)) & 4294967295L);
                val = bits % n;
            } while ((bits - val) + (n - 1) < 0);
            return val;
        }
        throw new NotStrictlyPositiveException(Long.valueOf(n));
    }

    public void clear() {
        this.nextGaussian = Double.NaN;
    }

    public void nextBytes(byte[] bytes) {
        nextBytesFill(bytes, 0, bytes.length);
    }

    public void nextBytes(byte[] bytes, int start, int len) {
        if (start < 0 || start >= bytes.length) {
            throw new OutOfRangeException(Integer.valueOf(start), Integer.valueOf(0), Integer.valueOf(bytes.length));
        } else if (len < 0 || len > bytes.length - start) {
            throw new OutOfRangeException(Integer.valueOf(len), Integer.valueOf(0), Integer.valueOf(bytes.length - start));
        } else {
            nextBytesFill(bytes, start, len);
        }
    }

    private void nextBytesFill(byte[] bytes, int start, int len) {
        int index = start;
        int indexLoopLimit = (2147483644 & len) + index;
        while (index < indexLoopLimit) {
            int random = next(32);
            int index2 = index + 1;
            bytes[index] = (byte) random;
            int index3 = index2 + 1;
            bytes[index2] = (byte) (random >>> 8);
            int index4 = index3 + 1;
            bytes[index3] = (byte) (random >>> 16);
            index = index4 + 1;
            bytes[index4] = (byte) (random >>> 24);
        }
        int indexLimit = start + len;
        if (index < indexLimit) {
            int random2 = next(32);
            while (true) {
                int index5 = index + 1;
                bytes[index] = (byte) random2;
                if (index5 < indexLimit) {
                    random2 >>>= 8;
                    index = index5;
                } else {
                    int i = index5;
                    return;
                }
            }
        }
    }
}
