package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

public abstract class AbstractRandomGenerator implements RandomGenerator {
    private double cachedNormalDeviate = Double.NaN;

    public abstract double nextDouble();

    public abstract void setSeed(long j);

    public void clear() {
        this.cachedNormalDeviate = Double.NaN;
    }

    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    public void setSeed(int[] seed) {
        long combined = 0;
        for (int s : seed) {
            combined = (4294967291L * combined) + ((long) s);
        }
        setSeed(combined);
    }

    public void nextBytes(byte[] bytes) {
        int bytesOut;
        for (int bytesOut2 = 0; bytesOut2 < bytes.length; bytesOut2 = bytesOut) {
            int randInt = nextInt();
            bytesOut = bytesOut2;
            int i = 0;
            while (i < 3) {
                if (i > 0) {
                    randInt >>= 8;
                }
                int bytesOut3 = bytesOut + 1;
                bytes[bytesOut] = (byte) randInt;
                if (bytesOut3 != bytes.length) {
                    i++;
                    bytesOut = bytesOut3;
                } else {
                    return;
                }
            }
        }
    }

    public int nextInt() {
        return (int) (((nextDouble() * 2.0d) - 1.0d) * 2.147483647E9d);
    }

    public int nextInt(int n) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(n));
        }
        int result = (int) (nextDouble() * ((double) n));
        return result < n ? result : n - 1;
    }

    public long nextLong() {
        return (long) (((nextDouble() * 2.0d) - 1.0d) * 9.223372036854776E18d);
    }

    public boolean nextBoolean() {
        return nextDouble() <= 0.5d;
    }

    public float nextFloat() {
        return (float) nextDouble();
    }

    public double nextGaussian() {
        if (!Double.isNaN(this.cachedNormalDeviate)) {
            double dev = this.cachedNormalDeviate;
            this.cachedNormalDeviate = Double.NaN;
            return dev;
        }
        double v2 = 0.0d;
        double v1 = 0.0d;
        double s = 1.0d;
        while (s >= 1.0d) {
            v1 = (nextDouble() * 2.0d) - 1.0d;
            v2 = (nextDouble() * 2.0d) - 1.0d;
            s = (v1 * v1) + (v2 * v2);
        }
        if (s != 0.0d) {
            s = FastMath.sqrt((FastMath.log(s) * -2.0d) / s);
        }
        this.cachedNormalDeviate = v2 * s;
        return v1 * s;
    }
}
