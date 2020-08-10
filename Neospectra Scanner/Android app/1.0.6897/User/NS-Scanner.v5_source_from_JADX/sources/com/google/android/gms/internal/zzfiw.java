package com.google.android.gms.internal;

final class zzfiw extends zzfit {
    zzfiw() {
    }

    private static int zza(byte[] bArr, int i, long j, int i2) {
        switch (i2) {
            case 0:
                return zzfis.zzme(i);
            case 1:
                return zzfis.zzaj(i, zzfiq.zzb(bArr, j));
            case 2:
                return zzfis.zzi(i, zzfiq.zzb(bArr, j), zzfiq.zzb(bArr, j + 1));
            default:
                throw new AssertionError();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004f, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00ab, code lost:
        return -1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int zzb(byte[] r11, long r12, int r14) {
        /*
            r0 = 0
            r1 = 1
            r3 = 16
            if (r14 >= r3) goto L_0x0009
            r3 = 0
            goto L_0x001b
        L_0x0009:
            r4 = r12
            r3 = 0
        L_0x000b:
            if (r3 >= r14) goto L_0x001a
            long r6 = r4 + r1
            byte r4 = com.google.android.gms.internal.zzfiq.zzb(r11, r4)
            if (r4 >= 0) goto L_0x0016
            goto L_0x001b
        L_0x0016:
            int r3 = r3 + 1
            r4 = r6
            goto L_0x000b
        L_0x001a:
            r3 = r14
        L_0x001b:
            int r14 = r14 - r3
            long r3 = (long) r3
            long r12 = r12 + r3
        L_0x001e:
            r3 = 0
        L_0x001f:
            if (r14 <= 0) goto L_0x002f
            long r3 = r12 + r1
            byte r12 = com.google.android.gms.internal.zzfiq.zzb(r11, r12)
            if (r12 < 0) goto L_0x0032
            int r14 = r14 + -1
            r9 = r3
            r3 = r12
            r12 = r9
            goto L_0x001f
        L_0x002f:
            r9 = r12
            r12 = r3
            r3 = r9
        L_0x0032:
            if (r14 != 0) goto L_0x0035
            return r0
        L_0x0035:
            int r14 = r14 + -1
            r13 = -32
            r5 = -65
            r6 = -1
            if (r12 >= r13) goto L_0x0050
            if (r14 != 0) goto L_0x0041
            return r12
        L_0x0041:
            int r14 = r14 + -1
            r13 = -62
            if (r12 < r13) goto L_0x004f
            long r12 = r3 + r1
            byte r3 = com.google.android.gms.internal.zzfiq.zzb(r11, r3)
            if (r3 <= r5) goto L_0x001e
        L_0x004f:
            return r6
        L_0x0050:
            r7 = -16
            if (r12 >= r7) goto L_0x007c
            r7 = 2
            if (r14 >= r7) goto L_0x005c
            int r11 = zza(r11, r12, r3, r14)
            return r11
        L_0x005c:
            int r14 = r14 + -2
            long r7 = r3 + r1
            byte r3 = com.google.android.gms.internal.zzfiq.zzb(r11, r3)
            if (r3 > r5) goto L_0x007b
            r4 = -96
            if (r12 != r13) goto L_0x006c
            if (r3 < r4) goto L_0x007b
        L_0x006c:
            r13 = -19
            if (r12 != r13) goto L_0x0072
            if (r3 >= r4) goto L_0x007b
        L_0x0072:
            r12 = 0
            long r12 = r7 + r1
            byte r3 = com.google.android.gms.internal.zzfiq.zzb(r11, r7)
            if (r3 <= r5) goto L_0x001e
        L_0x007b:
            return r6
        L_0x007c:
            r13 = 3
            if (r14 >= r13) goto L_0x0084
            int r11 = zza(r11, r12, r3, r14)
            return r11
        L_0x0084:
            int r14 = r14 + -3
            long r7 = r3 + r1
            byte r13 = com.google.android.gms.internal.zzfiq.zzb(r11, r3)
            if (r13 > r5) goto L_0x00ab
            int r12 = r12 << 28
            int r13 = r13 + 112
            int r12 = r12 + r13
            int r12 = r12 >> 30
            if (r12 != 0) goto L_0x00ab
            long r12 = r7 + r1
            byte r3 = com.google.android.gms.internal.zzfiq.zzb(r11, r7)
            if (r3 > r5) goto L_0x00ab
            long r3 = r12 + r1
            byte r12 = com.google.android.gms.internal.zzfiq.zzb(r11, r12)
            if (r12 <= r5) goto L_0x00a8
            return r6
        L_0x00a8:
            r12 = r3
            goto L_0x001e
        L_0x00ab:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzfiw.zzb(byte[], long, int):int");
    }

    /* access modifiers changed from: 0000 */
    public final int zzb(int i, byte[] bArr, int i2, int i3) {
        if ((i2 | i3 | (bArr.length - i3)) < 0) {
            throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", new Object[]{Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)}));
        }
        long j = (long) i2;
        return zzb(bArr, j, (int) (((long) i3) - j));
    }

    /* access modifiers changed from: 0000 */
    public final int zzb(CharSequence charSequence, byte[] bArr, int i, int i2) {
        long j;
        long j2;
        CharSequence charSequence2 = charSequence;
        byte[] bArr2 = bArr;
        int i3 = i;
        int i4 = i2;
        long j3 = (long) i3;
        long j4 = ((long) i4) + j3;
        int length = charSequence.length();
        if (length > i4 || bArr2.length - i4 < i3) {
            char charAt = charSequence2.charAt(length - 1);
            int i5 = i3 + i4;
            StringBuilder sb = new StringBuilder(37);
            sb.append("Failed writing ");
            sb.append(charAt);
            sb.append(" at index ");
            sb.append(i5);
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
        int i6 = 0;
        while (i6 < length) {
            char charAt2 = charSequence2.charAt(i6);
            if (charAt2 >= 128) {
                break;
            }
            long j5 = 1 + j;
            zzfiq.zza(bArr2, j, (byte) charAt2);
            i6++;
            j3 = j5;
        }
        if (i6 == length) {
            return (int) j;
        }
        while (i6 < length) {
            char charAt3 = charSequence2.charAt(i6);
            if (charAt3 < 128 && j < j4) {
                j2 = j + 1;
            } else if (charAt3 < 2048 && j <= j4 - 2) {
                long j6 = j + 1;
                zzfiq.zza(bArr2, j, (byte) ((charAt3 >>> 6) | 960));
                j = j6 + 1;
                zzfiq.zza(bArr2, j6, (byte) ((charAt3 & '?') | 128));
                i6++;
            } else if ((charAt3 < 55296 || 57343 < charAt3) && j <= j4 - 3) {
                long j7 = j + 1;
                zzfiq.zza(bArr2, j, (byte) ((charAt3 >>> 12) | 480));
                j = j7 + 1;
                zzfiq.zza(bArr2, j7, (byte) (((charAt3 >>> 6) & 63) | 128));
                j2 = j + 1;
                charAt3 = (charAt3 & '?') | 128;
            } else if (j <= j4 - 4) {
                int i7 = i6 + 1;
                if (i7 != length) {
                    char charAt4 = charSequence2.charAt(i7);
                    if (Character.isSurrogatePair(charAt3, charAt4)) {
                        int codePoint = Character.toCodePoint(charAt3, charAt4);
                        long j8 = j + 1;
                        zzfiq.zza(bArr2, j, (byte) ((codePoint >>> 18) | 240));
                        long j9 = j8 + 1;
                        zzfiq.zza(bArr2, j8, (byte) (((codePoint >>> 12) & 63) | 128));
                        long j10 = j9 + 1;
                        zzfiq.zza(bArr2, j9, (byte) (((codePoint >>> 6) & 63) | 128));
                        j = j10 + 1;
                        zzfiq.zza(bArr2, j10, (byte) ((codePoint & 63) | 128));
                        i6 = i7;
                        i6++;
                    }
                } else {
                    i7 = i6;
                }
                throw new zzfiv(i7 - 1, length);
            } else {
                if (55296 <= charAt3 && charAt3 <= 57343) {
                    int i8 = i6 + 1;
                    if (i8 == length || !Character.isSurrogatePair(charAt3, charSequence2.charAt(i8))) {
                        throw new zzfiv(i6, length);
                    }
                }
                StringBuilder sb2 = new StringBuilder(46);
                sb2.append("Failed writing ");
                sb2.append(charAt3);
                sb2.append(" at index ");
                sb2.append(j);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            }
            zzfiq.zza(bArr2, j, (byte) charAt3);
            j = j2;
            i6++;
        }
        return (int) j;
    }
}
