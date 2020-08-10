package com.google.android.gms.internal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.apache.poi.hssf.record.formula.UnionPtg;

public final class zzbep {
    private static long zza(long j, long j2, long j3) {
        long j4 = (j ^ j2) * j3;
        long j5 = ((j4 ^ (j4 >>> 47)) ^ j2) * j3;
        return (j5 ^ (j5 >>> 47)) * j3;
    }

    private static long zza(byte[] bArr, int i, int i2) {
        byte[] bArr2 = bArr;
        long[] jArr = new long[2];
        long[] jArr2 = new long[2];
        long zzc = zzc(bArr2, 0) + 95310865018149119L;
        int i3 = i2 - 1;
        int i4 = ((i3 / 64) << 6) + 0;
        int i5 = i3 & 63;
        int i6 = (i4 + i5) - 63;
        long j = 2480279821605975764L;
        long j2 = 1390051526045402406L;
        int i7 = i;
        while (true) {
            long rotateRight = Long.rotateRight(zzc + j + jArr[0] + zzc(bArr2, i7 + 8), 37) * -5435081209227447693L;
            long j3 = rotateRight ^ jArr2[1];
            long rotateRight2 = (Long.rotateRight(j + jArr[1] + zzc(bArr2, i7 + 48), 42) * -5435081209227447693L) + jArr[0] + zzc(bArr2, i7 + 40);
            long rotateRight3 = Long.rotateRight(j2 + jArr2[0], 33) * -5435081209227447693L;
            zza(bArr2, i7, jArr[1] * -5435081209227447693L, j3 + jArr2[0], jArr);
            zza(bArr2, i7 + 32, rotateRight3 + jArr2[1], rotateRight2 + zzc(bArr2, i7 + 16), jArr2);
            i7 += 64;
            if (i7 == i4) {
                long j4 = ((j3 & 255) << 1) - 5435081209227447693L;
                jArr2[0] = jArr2[0] + ((long) i5);
                jArr[0] = jArr[0] + jArr2[0];
                jArr2[0] = jArr2[0] + jArr[0];
                long rotateRight4 = (Long.rotateRight(((rotateRight3 + rotateRight2) + jArr[0]) + zzc(bArr2, i6 + 8), 37) * j4) ^ (jArr2[1] * 9);
                long rotateRight5 = (Long.rotateRight(rotateRight2 + jArr[1] + zzc(bArr2, i6 + 48), 42) * j4) + (jArr[0] * 9) + zzc(bArr2, i6 + 40);
                long rotateRight6 = Long.rotateRight(j3 + jArr2[0], 33) * j4;
                byte[] bArr3 = bArr2;
                zza(bArr3, i6, jArr[1] * j4, rotateRight4 + jArr2[0], jArr);
                zza(bArr3, i6 + 32, rotateRight6 + jArr2[1], zzc(bArr2, i6 + 16) + rotateRight5, jArr2);
                long j5 = j4;
                return zza(zza(jArr[0], jArr2[0], j5) + (((rotateRight5 >>> 47) ^ rotateRight5) * -4348849565147123417L) + rotateRight4, zza(jArr[1], jArr2[1], j5) + rotateRight6, j5);
            }
            j2 = j3;
            j = rotateRight2;
            zzc = rotateRight3;
        }
    }

    private static void zza(byte[] bArr, int i, long j, long j2, long[] jArr) {
        long zzc = zzc(bArr, i);
        long zzc2 = zzc(bArr, i + 8);
        long zzc3 = zzc(bArr, i + 16);
        long zzc4 = zzc(bArr, i + 24);
        long j3 = j + zzc;
        long j4 = zzc2 + j3 + zzc3;
        long rotateRight = Long.rotateRight(j2 + j3 + zzc4, 21) + Long.rotateRight(j4, 44);
        jArr[0] = j4 + zzc4;
        jArr[1] = rotateRight + j3;
    }

    private static int zzb(byte[] bArr, int i) {
        return ((bArr[i + 3] & 255) << 24) | (bArr[i] & 255) | ((bArr[i + 1] & 255) << 8) | ((bArr[i + 2] & 255) << UnionPtg.sid);
    }

    private static long zzc(byte[] bArr, int i) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr, i, 8);
        wrap.order(ByteOrder.LITTLE_ENDIAN);
        return wrap.getLong();
    }

    public static long zzj(byte[] bArr) {
        byte[] bArr2 = bArr;
        int length = bArr2.length;
        if (length < 0 || length > bArr2.length) {
            StringBuilder sb = new StringBuilder(67);
            sb.append("Out of bound index with offput: 0 and length: ");
            sb.append(length);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (length <= 32) {
            if (length > 16) {
                long j = ((long) (length << 1)) - 7286425919675154353L;
                long zzc = zzc(bArr2, 0) * -5435081209227447693L;
                long zzc2 = zzc(bArr2, 8);
                int i = length + 0;
                long zzc3 = zzc(bArr2, i - 8) * j;
                return zza((zzc(bArr2, i - 16) * -7286425919675154353L) + Long.rotateRight(zzc + zzc2, 43) + Long.rotateRight(zzc3, 30), zzc + Long.rotateRight(zzc2 - 7286425919675154353L, 18) + zzc3, j);
            } else if (length >= 8) {
                long j2 = ((long) (length << 1)) - 7286425919675154353L;
                long zzc4 = zzc(bArr2, 0) - 7286425919675154353L;
                long zzc5 = zzc(bArr2, (length + 0) - 8);
                return zza((Long.rotateRight(zzc5, 37) * j2) + zzc4, (Long.rotateRight(zzc4, 25) + zzc5) * j2, j2);
            } else if (length >= 4) {
                return zza(((long) length) + ((((long) zzb(bArr2, 0)) & 4294967295L) << 3), ((long) zzb(bArr2, (length + 0) - 4)) & 4294967295L, ((long) (length << 1)) - 7286425919675154353L);
            } else if (length <= 0) {
                return -7286425919675154353L;
            } else {
                byte b = bArr2[0];
                byte b2 = bArr2[(length >> 1) + 0];
                long j3 = (((long) (length + ((bArr2[(length - 1) + 0] & 255) << 2))) * -4348849565147123417L) ^ (((long) ((b & 255) + ((b2 & 255) << 8))) * -7286425919675154353L);
                return (j3 ^ (j3 >>> 47)) * -7286425919675154353L;
            }
        } else if (length > 64) {
            return zza(bArr2, 0, length);
        } else {
            long j4 = ((long) (length << 1)) - 7286425919675154353L;
            long zzc6 = zzc(bArr2, 0) * -7286425919675154353L;
            long zzc7 = zzc(bArr2, 8);
            int i2 = length + 0;
            long zzc8 = zzc(bArr2, i2 - 8) * j4;
            long rotateRight = Long.rotateRight(zzc6 + zzc7, 43) + Long.rotateRight(zzc8, 30) + (zzc(bArr2, i2 - 16) * -7286425919675154353L);
            long zza = zza(rotateRight, zzc6 + Long.rotateRight(zzc7 - 7286425919675154353L, 18) + zzc8, j4);
            long zzc9 = zzc(bArr2, 16) * j4;
            long zzc10 = zzc(bArr2, 24);
            long zzc11 = (rotateRight + zzc(bArr2, i2 - 32)) * j4;
            return zza(Long.rotateRight(zzc9 + zzc10, 43) + Long.rotateRight(zzc11, 30) + ((zza + zzc(bArr2, i2 - 24)) * j4), zzc9 + Long.rotateRight(zzc10 + zzc6, 18) + zzc11, j4);
        }
    }
}
