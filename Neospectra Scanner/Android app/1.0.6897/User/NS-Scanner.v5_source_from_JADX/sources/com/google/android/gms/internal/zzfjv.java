package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfjv {
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static int zzpnl = 11;
    private static int zzpnm = 12;
    private static int zzpnn = 16;
    private static int zzpno = 26;
    public static final int[] zzpnp = new int[0];
    public static final long[] zzpnq = new long[0];
    public static final float[] zzpnr = new float[0];
    public static final double[] zzpns = new double[0];
    public static final boolean[] zzpnt = new boolean[0];
    public static final byte[][] zzpnu = new byte[0][];
    public static final byte[] zzpnv = new byte[0];

    public static final int zzb(zzfjj zzfjj, int i) throws IOException {
        int position = zzfjj.getPosition();
        zzfjj.zzkq(i);
        int i2 = 1;
        while (zzfjj.zzcvt() == i) {
            zzfjj.zzkq(i);
            i2++;
        }
        zzfjj.zzam(position, i);
        return i2;
    }
}
