package com.google.android.gms.internal;

import android.os.SystemClock;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class zzal implements zzb {
    private final Map<String, zzam> zzbx;
    private long zzby;
    private final File zzbz;
    private final int zzca;

    public zzal(File file) {
        this(file, 5242880);
    }

    private zzal(File file, int i) {
        this.zzbx = new LinkedHashMap(16, 0.75f, true);
        this.zzby = 0;
        this.zzbz = file;
        this.zzca = 5242880;
    }

    private final synchronized void remove(String str) {
        boolean delete = zze(str).delete();
        removeEntry(str);
        if (!delete) {
            zzae.zzb("Could not delete cache entry for key=%s, filename=%s", str, zzd(str));
        }
    }

    private final void removeEntry(String str) {
        zzam zzam = (zzam) this.zzbx.remove(str);
        if (zzam != null) {
            this.zzby -= zzam.zzcb;
        }
    }

    private static int zza(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        throw new EOFException();
    }

    private static InputStream zza(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

    static String zza(zzan zzan) throws IOException {
        return new String(zza(zzan, zzc(zzan)), "UTF-8");
    }

    static void zza(OutputStream outputStream, int i) throws IOException {
        outputStream.write(i & 255);
        outputStream.write((i >> 8) & 255);
        outputStream.write((i >> 16) & 255);
        outputStream.write(i >>> 24);
    }

    static void zza(OutputStream outputStream, long j) throws IOException {
        outputStream.write((byte) ((int) j));
        outputStream.write((byte) ((int) (j >>> 8)));
        outputStream.write((byte) ((int) (j >>> 16)));
        outputStream.write((byte) ((int) (j >>> 24)));
        outputStream.write((byte) ((int) (j >>> 32)));
        outputStream.write((byte) ((int) (j >>> 40)));
        outputStream.write((byte) ((int) (j >>> 48)));
        outputStream.write((byte) ((int) (j >>> 56)));
    }

    static void zza(OutputStream outputStream, String str) throws IOException {
        byte[] bytes = str.getBytes("UTF-8");
        zza(outputStream, (long) bytes.length);
        outputStream.write(bytes, 0, bytes.length);
    }

    private final void zza(String str, zzam zzam) {
        if (!this.zzbx.containsKey(str)) {
            this.zzby += zzam.zzcb;
        } else {
            this.zzby += zzam.zzcb - ((zzam) this.zzbx.get(str)).zzcb;
        }
        this.zzbx.put(str, zzam);
    }

    private static byte[] zza(zzan zzan, long j) throws IOException {
        long zzn = zzan.zzn();
        if (j >= 0 && j <= zzn) {
            int i = (int) j;
            if (((long) i) == j) {
                byte[] bArr = new byte[i];
                new DataInputStream(zzan).readFully(bArr);
                return bArr;
            }
        }
        StringBuilder sb = new StringBuilder(73);
        sb.append("streamToBytes length=");
        sb.append(j);
        sb.append(", maxLength=");
        sb.append(zzn);
        throw new IOException(sb.toString());
    }

    static int zzb(InputStream inputStream) throws IOException {
        return (zza(inputStream) << 24) | zza(inputStream) | 0 | (zza(inputStream) << 8) | (zza(inputStream) << 16);
    }

    static List<zzl> zzb(zzan zzan) throws IOException {
        int zzb = zzb((InputStream) zzan);
        List<zzl> emptyList = zzb == 0 ? Collections.emptyList() : new ArrayList<>(zzb);
        for (int i = 0; i < zzb; i++) {
            emptyList.add(new zzl(zza(zzan).intern(), zza(zzan).intern()));
        }
        return emptyList;
    }

    static long zzc(InputStream inputStream) throws IOException {
        return (((long) zza(inputStream)) & 255) | 0 | ((((long) zza(inputStream)) & 255) << 8) | ((((long) zza(inputStream)) & 255) << 16) | ((((long) zza(inputStream)) & 255) << 24) | ((((long) zza(inputStream)) & 255) << 32) | ((((long) zza(inputStream)) & 255) << 40) | ((((long) zza(inputStream)) & 255) << 48) | ((255 & ((long) zza(inputStream))) << 56);
    }

    private static String zzd(String str) {
        int length = str.length() / 2;
        String valueOf = String.valueOf(String.valueOf(str.substring(0, length).hashCode()));
        String valueOf2 = String.valueOf(String.valueOf(str.substring(length).hashCode()));
        return valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
    }

    private final File zze(String str) {
        return new File(this.zzbz, zzd(str));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void initialize() {
        /*
            r9 = this;
            monitor-enter(r9)
            java.io.File r0 = r9.zzbz     // Catch:{ all -> 0x0062 }
            boolean r0 = r0.exists()     // Catch:{ all -> 0x0062 }
            r1 = 0
            if (r0 != 0) goto L_0x0024
            java.io.File r0 = r9.zzbz     // Catch:{ all -> 0x0062 }
            boolean r0 = r0.mkdirs()     // Catch:{ all -> 0x0062 }
            if (r0 != 0) goto L_0x0022
            java.lang.String r0 = "Unable to create cache dir %s"
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0062 }
            java.io.File r3 = r9.zzbz     // Catch:{ all -> 0x0062 }
            java.lang.String r3 = r3.getAbsolutePath()     // Catch:{ all -> 0x0062 }
            r2[r1] = r3     // Catch:{ all -> 0x0062 }
            com.google.android.gms.internal.zzae.zzc(r0, r2)     // Catch:{ all -> 0x0062 }
        L_0x0022:
            monitor-exit(r9)
            return
        L_0x0024:
            java.io.File r0 = r9.zzbz     // Catch:{ all -> 0x0062 }
            java.io.File[] r0 = r0.listFiles()     // Catch:{ all -> 0x0062 }
            if (r0 != 0) goto L_0x002e
            monitor-exit(r9)
            return
        L_0x002e:
            int r2 = r0.length     // Catch:{ all -> 0x0062 }
        L_0x002f:
            if (r1 >= r2) goto L_0x0060
            r3 = r0[r1]     // Catch:{ all -> 0x0062 }
            long r4 = r3.length()     // Catch:{ IOException -> 0x0059 }
            com.google.android.gms.internal.zzan r6 = new com.google.android.gms.internal.zzan     // Catch:{ IOException -> 0x0059 }
            java.io.BufferedInputStream r7 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0059 }
            java.io.InputStream r8 = zza(r3)     // Catch:{ IOException -> 0x0059 }
            r7.<init>(r8)     // Catch:{ IOException -> 0x0059 }
            r6.<init>(r7, r4)     // Catch:{ IOException -> 0x0059 }
            com.google.android.gms.internal.zzam r7 = com.google.android.gms.internal.zzam.zzc(r6)     // Catch:{ all -> 0x0054 }
            r7.zzcb = r4     // Catch:{ all -> 0x0054 }
            java.lang.String r4 = r7.key     // Catch:{ all -> 0x0054 }
            r9.zza(r4, r7)     // Catch:{ all -> 0x0054 }
            r6.close()     // Catch:{ IOException -> 0x0059 }
            goto L_0x005d
        L_0x0054:
            r4 = move-exception
            r6.close()     // Catch:{ IOException -> 0x0059 }
            throw r4     // Catch:{ IOException -> 0x0059 }
        L_0x0059:
            r4 = move-exception
            r3.delete()     // Catch:{ all -> 0x0062 }
        L_0x005d:
            int r1 = r1 + 1
            goto L_0x002f
        L_0x0060:
            monitor-exit(r9)
            return
        L_0x0062:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzal.initialize():void");
    }

    public final synchronized zzc zza(String str) {
        zzan zzan;
        zzam zzam = (zzam) this.zzbx.get(str);
        if (zzam == null) {
            return null;
        }
        File zze = zze(str);
        try {
            zzan = new zzan(new BufferedInputStream(zza(zze)), zze.length());
            zzam zzc = zzam.zzc(zzan);
            if (!TextUtils.equals(str, zzc.key)) {
                zzae.zzb("%s: key=%s, found=%s", zze.getAbsolutePath(), str, zzc.key);
                removeEntry(str);
                zzan.close();
                return null;
            }
            byte[] zza = zza(zzan, zzan.zzn());
            zzc zzc2 = new zzc();
            zzc2.data = zza;
            zzc2.zza = zzam.zza;
            zzc2.zzb = zzam.zzb;
            zzc2.zzc = zzam.zzc;
            zzc2.zzd = zzam.zzd;
            zzc2.zze = zzam.zze;
            zzc2.zzf = zzao.zza(zzam.zzg);
            zzc2.zzg = Collections.unmodifiableList(zzam.zzg);
            zzan.close();
            return zzc2;
        } catch (IOException e) {
            zzae.zzb("%s: %s", zze.getAbsolutePath(), e.toString());
            remove(str);
            return null;
        } catch (Throwable th) {
            zzan.close();
            throw th;
        }
    }

    public final synchronized void zza(String str, zzc zzc) {
        File zze;
        long j;
        String str2 = str;
        zzc zzc2 = zzc;
        synchronized (this) {
            try {
                long length = (long) zzc2.data.length;
                if (this.zzby + length >= ((long) this.zzca)) {
                    if (zzae.DEBUG) {
                        zzae.zza("Pruning old cache entries.", new Object[0]);
                    }
                    long j2 = this.zzby;
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    Iterator it = this.zzbx.entrySet().iterator();
                    int i = 0;
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        zzam zzam = (zzam) ((Entry) it.next()).getValue();
                        if (zze(zzam.key).delete()) {
                            j = length;
                            this.zzby -= zzam.zzcb;
                        } else {
                            j = length;
                            zzae.zzb("Could not delete cache entry for key=%s, filename=%s", zzam.key, zzd(zzam.key));
                        }
                        it.remove();
                        i++;
                        if (((float) (this.zzby + j)) < ((float) this.zzca) * 0.9f) {
                            break;
                        }
                        length = j;
                        String str3 = str;
                        zzc zzc3 = zzc;
                    }
                    if (zzae.DEBUG) {
                        zzae.zza("pruned %d files, %d bytes, %d ms", Integer.valueOf(i), Long.valueOf(this.zzby - j2), Long.valueOf(SystemClock.elapsedRealtime() - elapsedRealtime));
                    }
                    str2 = str;
                }
                zze = zze(str);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(zze));
                zzc zzc4 = zzc;
                zzam zzam2 = new zzam(str2, zzc4);
                if (!zzam2.zza(bufferedOutputStream)) {
                    bufferedOutputStream.close();
                    zzae.zzb("Failed to write header for %s", zze.getAbsolutePath());
                    throw new IOException();
                }
                bufferedOutputStream.write(zzc4.data);
                bufferedOutputStream.close();
                zza(str2, zzam2);
            } catch (IOException e) {
                if (!zze.delete()) {
                    zzae.zzb("Could not clean up file %s", zze.getAbsolutePath());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
