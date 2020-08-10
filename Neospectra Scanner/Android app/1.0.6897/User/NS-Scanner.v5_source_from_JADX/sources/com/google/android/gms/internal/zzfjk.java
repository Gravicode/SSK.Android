package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class zzfjk {
    private final ByteBuffer buffer;

    private zzfjk(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    private zzfjk(byte[] bArr, int i, int i2) {
        this(ByteBuffer.wrap(bArr, i, i2));
    }

    private static int zza(CharSequence charSequence, byte[] bArr, int i, int i2) {
        int i3;
        int length = charSequence.length();
        int i4 = i2 + i;
        int i5 = 0;
        while (i5 < length) {
            int i6 = i5 + i;
            if (i6 >= i4) {
                break;
            }
            char charAt = charSequence.charAt(i5);
            if (charAt >= 128) {
                break;
            }
            bArr[i6] = (byte) charAt;
            i5++;
        }
        if (i5 == length) {
            return i + length;
        }
        int i7 = i + i5;
        while (i5 < length) {
            char charAt2 = charSequence.charAt(i5);
            if (charAt2 < 128 && i7 < i4) {
                i3 = i7 + 1;
                bArr[i7] = (byte) charAt2;
            } else if (charAt2 < 2048 && i7 <= i4 - 2) {
                int i8 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> 6) | 960);
                i7 = i8 + 1;
                bArr[i8] = (byte) ((charAt2 & '?') | 128);
                i5++;
            } else if ((charAt2 < 55296 || 57343 < charAt2) && i7 <= i4 - 3) {
                int i9 = i7 + 1;
                bArr[i7] = (byte) ((charAt2 >>> 12) | 480);
                int i10 = i9 + 1;
                bArr[i9] = (byte) (((charAt2 >>> 6) & 63) | 128);
                i3 = i10 + 1;
                bArr[i10] = (byte) ((charAt2 & '?') | 128);
            } else if (i7 <= i4 - 4) {
                int i11 = i5 + 1;
                if (i11 != charSequence.length()) {
                    char charAt3 = charSequence.charAt(i11);
                    if (!Character.isSurrogatePair(charAt2, charAt3)) {
                        i5 = i11;
                    } else {
                        int codePoint = Character.toCodePoint(charAt2, charAt3);
                        int i12 = i7 + 1;
                        bArr[i7] = (byte) ((codePoint >>> 18) | 240);
                        int i13 = i12 + 1;
                        bArr[i12] = (byte) (((codePoint >>> 12) & 63) | 128);
                        int i14 = i13 + 1;
                        bArr[i13] = (byte) (((codePoint >>> 6) & 63) | 128);
                        i7 = i14 + 1;
                        bArr[i14] = (byte) ((codePoint & 63) | 128);
                        i5 = i11;
                        i5++;
                    }
                }
                int i15 = i5 - 1;
                StringBuilder sb = new StringBuilder(39);
                sb.append("Unpaired surrogate at index ");
                sb.append(i15);
                throw new IllegalArgumentException(sb.toString());
            } else {
                StringBuilder sb2 = new StringBuilder(37);
                sb2.append("Failed writing ");
                sb2.append(charAt2);
                sb2.append(" at index ");
                sb2.append(i7);
                throw new ArrayIndexOutOfBoundsException(sb2.toString());
            }
            i7 = i3;
            i5++;
        }
        return i7;
    }

    private static void zza(CharSequence charSequence, ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
            } catch (ArrayIndexOutOfBoundsException e) {
                BufferOverflowException bufferOverflowException = new BufferOverflowException();
                bufferOverflowException.initCause(e);
                throw bufferOverflowException;
            }
        } else {
            zzb(charSequence, byteBuffer);
        }
    }

    public static int zzad(int i, int i2) {
        return zzlg(i) + zzlh(i2);
    }

    public static int zzb(int i, zzfjs zzfjs) {
        int zzlg = zzlg(i);
        int zzho = zzfjs.zzho();
        return zzlg + zzlp(zzho) + zzho;
    }

    private static void zzb(CharSequence charSequence, ByteBuffer byteBuffer) {
        int i;
        int length = charSequence.length();
        int i2 = 0;
        while (i2 < length) {
            char charAt = charSequence.charAt(i2);
            if (charAt >= 128) {
                if (charAt < 2048) {
                    i = (charAt >>> 6) | 960;
                } else if (charAt < 55296 || 57343 < charAt) {
                    byteBuffer.put((byte) ((charAt >>> 12) | 480));
                    i = ((charAt >>> 6) & 63) | 128;
                } else {
                    int i3 = i2 + 1;
                    if (i3 != charSequence.length()) {
                        char charAt2 = charSequence.charAt(i3);
                        if (!Character.isSurrogatePair(charAt, charAt2)) {
                            i2 = i3;
                        } else {
                            int codePoint = Character.toCodePoint(charAt, charAt2);
                            byteBuffer.put((byte) ((codePoint >>> 18) | 240));
                            byteBuffer.put((byte) (((codePoint >>> 12) & 63) | 128));
                            byteBuffer.put((byte) (((codePoint >>> 6) & 63) | 128));
                            byteBuffer.put((byte) ((codePoint & 63) | 128));
                            i2 = i3;
                            i2++;
                        }
                    }
                    int i4 = i2 - 1;
                    StringBuilder sb = new StringBuilder(39);
                    sb.append("Unpaired surrogate at index ");
                    sb.append(i4);
                    throw new IllegalArgumentException(sb.toString());
                }
                byteBuffer.put((byte) i);
                charAt = (charAt & '?') | 128;
            }
            byteBuffer.put((byte) charAt);
            i2++;
        }
    }

    public static zzfjk zzbf(byte[] bArr) {
        return zzo(bArr, 0, bArr.length);
    }

    public static int zzbg(byte[] bArr) {
        return zzlp(bArr.length) + bArr.length;
    }

    public static int zzc(int i, long j) {
        return zzlg(i) + zzdi(j);
    }

    public static int zzd(int i, byte[] bArr) {
        return zzlg(i) + zzbg(bArr);
    }

    private static int zzd(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < 128) {
            i2++;
        }
        int i3 = length;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i2);
            if (charAt < 2048) {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 2048) {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if (55296 <= charAt2 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i2) < 65536) {
                                StringBuilder sb = new StringBuilder(39);
                                sb.append("Unpaired surrogate at index ");
                                sb.append(i2);
                                throw new IllegalArgumentException(sb.toString());
                            }
                            i2++;
                        }
                    }
                    i2++;
                }
                i3 += i;
            }
        }
        if (i3 >= length) {
            return i3;
        }
        long j = ((long) i3) + 4294967296L;
        StringBuilder sb2 = new StringBuilder(54);
        sb2.append("UTF-8 length does not fit in int: ");
        sb2.append(j);
        throw new IllegalArgumentException(sb2.toString());
    }

    private static long zzdb(long j) {
        return (j >> 63) ^ (j << 1);
    }

    private final void zzdh(long j) throws IOException {
        while ((-128 & j) != 0) {
            zzmh((((int) j) & ShapeTypes.VERTICAL_SCROLL) | 128);
            j >>>= 7;
        }
        zzmh((int) j);
    }

    public static int zzdi(long j) {
        if ((-128 & j) == 0) {
            return 1;
        }
        if ((-16384 & j) == 0) {
            return 2;
        }
        if ((-2097152 & j) == 0) {
            return 3;
        }
        if ((-268435456 & j) == 0) {
            return 4;
        }
        if ((-34359738368L & j) == 0) {
            return 5;
        }
        if ((-4398046511104L & j) == 0) {
            return 6;
        }
        if ((-562949953421312L & j) == 0) {
            return 7;
        }
        if ((-72057594037927936L & j) == 0) {
            return 8;
        }
        return (j & Long.MIN_VALUE) == 0 ? 9 : 10;
    }

    private final void zzdj(long j) throws IOException {
        if (this.buffer.remaining() < 8) {
            throw new zzfjl(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putLong(j);
    }

    public static int zzh(int i, long j) {
        return zzlg(i) + zzdi(zzdb(j));
    }

    public static int zzlg(int i) {
        return zzlp(i << 3);
    }

    public static int zzlh(int i) {
        if (i >= 0) {
            return zzlp(i);
        }
        return 10;
    }

    public static int zzlo(int i) {
        return (i >> 31) ^ (i << 1);
    }

    public static int zzlp(int i) {
        if ((i & -128) == 0) {
            return 1;
        }
        if ((i & -16384) == 0) {
            return 2;
        }
        if ((-2097152 & i) == 0) {
            return 3;
        }
        return (i & -268435456) == 0 ? 4 : 5;
    }

    private final void zzmh(int i) throws IOException {
        byte b = (byte) i;
        if (!this.buffer.hasRemaining()) {
            throw new zzfjl(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(b);
    }

    public static int zzo(int i, String str) {
        return zzlg(i) + zztt(str);
    }

    public static zzfjk zzo(byte[] bArr, int i, int i2) {
        return new zzfjk(bArr, 0, i2);
    }

    public static int zztt(String str) {
        int zzd = zzd(str);
        return zzlp(zzd) + zzd;
    }

    public final void zza(int i, double d) throws IOException {
        zzz(i, 1);
        zzdj(Double.doubleToLongBits(d));
    }

    public final void zza(int i, long j) throws IOException {
        zzz(i, 0);
        zzdh(j);
    }

    public final void zza(int i, zzfjs zzfjs) throws IOException {
        zzz(i, 2);
        zzb(zzfjs);
    }

    public final void zzaa(int i, int i2) throws IOException {
        zzz(i, 0);
        if (i2 >= 0) {
            zzmi(i2);
        } else {
            zzdh((long) i2);
        }
    }

    public final void zzb(int i, long j) throws IOException {
        zzz(i, 1);
        zzdj(j);
    }

    public final void zzb(zzfjs zzfjs) throws IOException {
        zzmi(zzfjs.zzdam());
        zzfjs.zza(this);
    }

    public final void zzbh(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (this.buffer.remaining() >= length) {
            this.buffer.put(bArr, 0, length);
            return;
        }
        throw new zzfjl(this.buffer.position(), this.buffer.limit());
    }

    public final void zzc(int i, float f) throws IOException {
        zzz(i, 5);
        int floatToIntBits = Float.floatToIntBits(f);
        if (this.buffer.remaining() < 4) {
            throw new zzfjl(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.putInt(floatToIntBits);
    }

    public final void zzc(int i, byte[] bArr) throws IOException {
        zzz(i, 2);
        zzmi(bArr.length);
        zzbh(bArr);
    }

    public final void zzcwt() {
        if (this.buffer.remaining() != 0) {
            throw new IllegalStateException(String.format("Did not write as much data as expected, %s bytes remaining.", new Object[]{Integer.valueOf(this.buffer.remaining())}));
        }
    }

    public final void zzf(int i, long j) throws IOException {
        zzz(i, 0);
        zzdh(j);
    }

    public final void zzg(int i, long j) throws IOException {
        zzz(i, 0);
        zzdh(zzdb(j));
    }

    public final void zzl(int i, boolean z) throws IOException {
        zzz(i, 0);
        byte b = z ? (byte) 1 : 0;
        if (!this.buffer.hasRemaining()) {
            throw new zzfjl(this.buffer.position(), this.buffer.limit());
        }
        this.buffer.put(b);
    }

    public final void zzmi(int i) throws IOException {
        while ((i & -128) != 0) {
            zzmh((i & ShapeTypes.VERTICAL_SCROLL) | 128);
            i >>>= 7;
        }
        zzmh(i);
    }

    public final void zzn(int i, String str) throws IOException {
        zzz(i, 2);
        try {
            int zzlp = zzlp(str.length());
            if (zzlp == zzlp(str.length() * 3)) {
                int position = this.buffer.position();
                if (this.buffer.remaining() < zzlp) {
                    throw new zzfjl(position + zzlp, this.buffer.limit());
                }
                this.buffer.position(position + zzlp);
                zza((CharSequence) str, this.buffer);
                int position2 = this.buffer.position();
                this.buffer.position(position);
                zzmi((position2 - position) - zzlp);
                this.buffer.position(position2);
                return;
            }
            zzmi(zzd(str));
            zza((CharSequence) str, this.buffer);
        } catch (BufferOverflowException e) {
            zzfjl zzfjl = new zzfjl(this.buffer.position(), this.buffer.limit());
            zzfjl.initCause(e);
            throw zzfjl;
        }
    }

    public final void zzz(int i, int i2) throws IOException {
        zzmi((i << 3) | i2);
    }
}
