package com.google.android.gms.internal;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;

final class zzfiq {
    private static final Logger logger = Logger.getLogger(zzfiq.class.getName());
    private static final Unsafe zzlrs = zzczz();
    private static final boolean zzpfz = zzdaa();
    private static final Class<?> zzpks = zztx("libcore.io.Memory");
    private static final boolean zzpkt = (zztx("org.robolectric.Robolectric") != null);
    private static final boolean zzpku = zzp(Long.TYPE);
    private static final boolean zzpkv = zzp(Integer.TYPE);
    private static final zzd zzpkw;
    private static final boolean zzpkx = zzdab();
    private static final long zzpky = ((long) zzn(byte[].class));
    private static final long zzpkz = ((long) zzn(boolean[].class));
    private static final long zzpla = ((long) zzo(boolean[].class));
    private static final long zzplb = ((long) zzn(int[].class));
    private static final long zzplc = ((long) zzo(int[].class));
    private static final long zzpld = ((long) zzn(long[].class));
    private static final long zzple = ((long) zzo(long[].class));
    private static final long zzplf = ((long) zzn(float[].class));
    private static final long zzplg = ((long) zzo(float[].class));
    private static final long zzplh = ((long) zzn(double[].class));
    private static final long zzpli = ((long) zzo(double[].class));
    private static final long zzplj = ((long) zzn(Object[].class));
    private static final long zzplk = ((long) zzo(Object[].class));
    private static final long zzpll;
    /* access modifiers changed from: private */
    public static final boolean zzplm;

    static final class zza extends zzd {
        zza(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzfiq.zzplm) {
                zzfiq.zza(obj, j, b);
            } else {
                zzfiq.zzb(obj, j, b);
            }
        }

        public final byte zzf(Object obj, long j) {
            return zzfiq.zzplm ? zzfiq.zzb(obj, j) : zzfiq.zzc(obj, j);
        }
    }

    static final class zzb extends zzd {
        zzb(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zze(Object obj, long j, byte b) {
            if (zzfiq.zzplm) {
                zzfiq.zza(obj, j, b);
            } else {
                zzfiq.zzb(obj, j, b);
            }
        }

        public final byte zzf(Object obj, long j) {
            return zzfiq.zzplm ? zzfiq.zzb(obj, j) : zzfiq.zzc(obj, j);
        }
    }

    static final class zzc extends zzd {
        zzc(Unsafe unsafe) {
            super(unsafe);
        }

        public final void zze(Object obj, long j, byte b) {
            this.zzpln.putByte(obj, j, b);
        }

        public final byte zzf(Object obj, long j) {
            return this.zzpln.getByte(obj, j);
        }
    }

    static abstract class zzd {
        Unsafe zzpln;

        zzd(Unsafe unsafe) {
            this.zzpln = unsafe;
        }

        public final int zza(Object obj, long j) {
            return this.zzpln.getInt(obj, j);
        }

        public abstract void zze(Object obj, long j, byte b);

        public abstract byte zzf(Object obj, long j);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00f5, code lost:
        if (r0 != null) goto L_0x0100;
     */
    static {
        /*
            java.lang.Class<com.google.android.gms.internal.zzfiq> r0 = com.google.android.gms.internal.zzfiq.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            logger = r0
            sun.misc.Unsafe r0 = zzczz()
            zzlrs = r0
            java.lang.String r0 = "libcore.io.Memory"
            java.lang.Class r0 = zztx(r0)
            zzpks = r0
            java.lang.String r0 = "org.robolectric.Robolectric"
            java.lang.Class r0 = zztx(r0)
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0026
            r0 = 1
            goto L_0x0027
        L_0x0026:
            r0 = 0
        L_0x0027:
            zzpkt = r0
            java.lang.Class r0 = java.lang.Long.TYPE
            boolean r0 = zzp(r0)
            zzpku = r0
            java.lang.Class r0 = java.lang.Integer.TYPE
            boolean r0 = zzp(r0)
            zzpkv = r0
            sun.misc.Unsafe r0 = zzlrs
            r3 = 0
            if (r0 != 0) goto L_0x003f
            goto L_0x0064
        L_0x003f:
            boolean r0 = zzdac()
            if (r0 == 0) goto L_0x005d
            boolean r0 = zzpku
            if (r0 == 0) goto L_0x0051
            com.google.android.gms.internal.zzfiq$zzb r3 = new com.google.android.gms.internal.zzfiq$zzb
            sun.misc.Unsafe r0 = zzlrs
            r3.<init>(r0)
            goto L_0x0064
        L_0x0051:
            boolean r0 = zzpkv
            if (r0 == 0) goto L_0x0064
            com.google.android.gms.internal.zzfiq$zza r3 = new com.google.android.gms.internal.zzfiq$zza
            sun.misc.Unsafe r0 = zzlrs
            r3.<init>(r0)
            goto L_0x0064
        L_0x005d:
            com.google.android.gms.internal.zzfiq$zzc r3 = new com.google.android.gms.internal.zzfiq$zzc
            sun.misc.Unsafe r0 = zzlrs
            r3.<init>(r0)
        L_0x0064:
            zzpkw = r3
            boolean r0 = zzdab()
            zzpkx = r0
            boolean r0 = zzdaa()
            zzpfz = r0
            java.lang.Class<byte[]> r0 = byte[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzpky = r3
            java.lang.Class<boolean[]> r0 = boolean[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzpkz = r3
            java.lang.Class<boolean[]> r0 = boolean[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzpla = r3
            java.lang.Class<int[]> r0 = int[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzplb = r3
            java.lang.Class<int[]> r0 = int[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzplc = r3
            java.lang.Class<long[]> r0 = long[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzpld = r3
            java.lang.Class<long[]> r0 = long[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzple = r3
            java.lang.Class<float[]> r0 = float[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzplf = r3
            java.lang.Class<float[]> r0 = float[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzplg = r3
            java.lang.Class<double[]> r0 = double[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzplh = r3
            java.lang.Class<double[]> r0 = double[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzpli = r3
            java.lang.Class<java.lang.Object[]> r0 = java.lang.Object[].class
            int r0 = zzn(r0)
            long r3 = (long) r0
            zzplj = r3
            java.lang.Class<java.lang.Object[]> r0 = java.lang.Object[].class
            int r0 = zzo(r0)
            long r3 = (long) r0
            zzplk = r3
            boolean r0 = zzdac()
            if (r0 == 0) goto L_0x00f8
            java.lang.Class<java.nio.Buffer> r0 = java.nio.Buffer.class
            java.lang.String r3 = "effectiveDirectAddress"
            java.lang.reflect.Field r0 = zza(r0, r3)
            if (r0 == 0) goto L_0x00f8
            goto L_0x0100
        L_0x00f8:
            java.lang.Class<java.nio.Buffer> r0 = java.nio.Buffer.class
            java.lang.String r3 = "address"
            java.lang.reflect.Field r0 = zza(r0, r3)
        L_0x0100:
            if (r0 == 0) goto L_0x0110
            com.google.android.gms.internal.zzfiq$zzd r3 = zzpkw
            if (r3 != 0) goto L_0x0107
            goto L_0x0110
        L_0x0107:
            com.google.android.gms.internal.zzfiq$zzd r3 = zzpkw
            sun.misc.Unsafe r3 = r3.zzpln
            long r3 = r3.objectFieldOffset(r0)
            goto L_0x0112
        L_0x0110:
            r3 = -1
        L_0x0112:
            zzpll = r3
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r3 = java.nio.ByteOrder.BIG_ENDIAN
            if (r0 != r3) goto L_0x011d
            r1 = 1
        L_0x011d:
            zzplm = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzfiq.<clinit>():void");
    }

    private zzfiq() {
    }

    static int zza(Object obj, long j) {
        return zzpkw.zza(obj, j);
    }

    private static Field zza(Class<?> cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Throwable th) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static void zza(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int i = ((~((int) j)) & 3) << 3;
        int i2 = (255 & b) << i;
        zza(obj, j2, i2 | (zza(obj, j2) & (~(255 << i))));
    }

    private static void zza(Object obj, long j, int i) {
        zzpkw.zzpln.putInt(obj, j, i);
    }

    static void zza(byte[] bArr, long j, byte b) {
        zzpkw.zze(bArr, zzpky + j, b);
    }

    /* access modifiers changed from: private */
    public static byte zzb(Object obj, long j) {
        return (byte) (zza(obj, -4 & j) >>> ((int) (((~j) & 3) << 3)));
    }

    static byte zzb(byte[] bArr, long j) {
        return zzpkw.zzf(bArr, zzpky + j);
    }

    /* access modifiers changed from: private */
    public static void zzb(Object obj, long j, byte b) {
        long j2 = -4 & j;
        int i = (((int) j) & 3) << 3;
        zza(obj, j2, ((255 & b) << i) | (zza(obj, j2) & (~(255 << i))));
    }

    /* access modifiers changed from: private */
    public static byte zzc(Object obj, long j) {
        return (byte) (zza(obj, -4 & j) >>> ((int) ((j & 3) << 3)));
    }

    static boolean zzczx() {
        return zzpfz;
    }

    static boolean zzczy() {
        return zzpkx;
    }

    private static Unsafe zzczz() {
        try {
            return (Unsafe) AccessController.doPrivileged(new zzfir());
        } catch (Throwable th) {
            return null;
        }
    }

    private static boolean zzdaa() {
        if (zzlrs == null) {
            return false;
        }
        try {
            Class cls = zzlrs.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("arrayBaseOffset", new Class[]{Class.class});
            cls.getMethod("arrayIndexScale", new Class[]{Class.class});
            cls.getMethod("getInt", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putInt", new Class[]{Object.class, Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putLong", new Class[]{Object.class, Long.TYPE, Long.TYPE});
            cls.getMethod("getObject", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putObject", new Class[]{Object.class, Long.TYPE, Object.class});
            if (zzdac()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putByte", new Class[]{Object.class, Long.TYPE, Byte.TYPE});
            cls.getMethod("getBoolean", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putBoolean", new Class[]{Object.class, Long.TYPE, Boolean.TYPE});
            cls.getMethod("getFloat", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putFloat", new Class[]{Object.class, Long.TYPE, Float.TYPE});
            cls.getMethod("getDouble", new Class[]{Object.class, Long.TYPE});
            cls.getMethod("putDouble", new Class[]{Object.class, Long.TYPE, Double.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeArrayOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzdab() {
        if (zzlrs == null) {
            return false;
        }
        try {
            Class cls = zzlrs.getClass();
            cls.getMethod("objectFieldOffset", new Class[]{Field.class});
            cls.getMethod("getLong", new Class[]{Object.class, Long.TYPE});
            if (zzdac()) {
                return true;
            }
            cls.getMethod("getByte", new Class[]{Long.TYPE});
            cls.getMethod("putByte", new Class[]{Long.TYPE, Byte.TYPE});
            cls.getMethod("getInt", new Class[]{Long.TYPE});
            cls.getMethod("putInt", new Class[]{Long.TYPE, Integer.TYPE});
            cls.getMethod("getLong", new Class[]{Long.TYPE});
            cls.getMethod("putLong", new Class[]{Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Long.TYPE, Long.TYPE, Long.TYPE});
            cls.getMethod("copyMemory", new Class[]{Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE});
            return true;
        } catch (Throwable th) {
            String valueOf = String.valueOf(th);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 71);
            sb.append("platform method missing - proto runtime falling back to safer methods: ");
            sb.append(valueOf);
            logger.logp(Level.WARNING, "com.google.protobuf.UnsafeUtil", "supportsUnsafeByteBufferOperations", sb.toString());
            return false;
        }
    }

    private static boolean zzdac() {
        return zzpks != null && !zzpkt;
    }

    private static int zzn(Class<?> cls) {
        if (zzpfz) {
            return zzpkw.zzpln.arrayBaseOffset(cls);
        }
        return -1;
    }

    private static int zzo(Class<?> cls) {
        if (zzpfz) {
            return zzpkw.zzpln.arrayIndexScale(cls);
        }
        return -1;
    }

    private static boolean zzp(Class<?> cls) {
        if (!zzdac()) {
            return false;
        }
        try {
            Class<?> cls2 = zzpks;
            cls2.getMethod("peekLong", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeLong", new Class[]{cls, Long.TYPE, Boolean.TYPE});
            cls2.getMethod("pokeInt", new Class[]{cls, Integer.TYPE, Boolean.TYPE});
            cls2.getMethod("peekInt", new Class[]{cls, Boolean.TYPE});
            cls2.getMethod("pokeByte", new Class[]{cls, Byte.TYPE});
            cls2.getMethod("peekByte", new Class[]{cls});
            cls2.getMethod("pokeByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            cls2.getMethod("peekByteArray", new Class[]{cls, byte[].class, Integer.TYPE, Integer.TYPE});
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    private static <T> Class<T> zztx(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable th) {
            return null;
        }
    }
}
