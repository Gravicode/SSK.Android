package com.google.android.gms.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzbq;
import java.net.URL;
import java.util.Map;

@WorkerThread
final class zzchu implements Runnable {
    private final String mPackageName;
    private final URL zzbxv;
    private final byte[] zzgfx;
    private final zzchs zzjck;
    private final Map<String, String> zzjcl;
    private /* synthetic */ zzchq zzjcm;

    public zzchu(zzchq zzchq, String str, URL url, byte[] bArr, Map<String, String> map, zzchs zzchs) {
        this.zzjcm = zzchq;
        zzbq.zzgm(str);
        zzbq.checkNotNull(url);
        zzbq.checkNotNull(zzchs);
        this.zzbxv = url;
        this.zzgfx = bArr;
        this.zzjck = zzchs;
        this.mPackageName = str;
        this.zzjcl = map;
    }

    /* JADX WARNING: Removed duplicated region for block: B:52:0x00ee A[SYNTHETIC, Splitter:B:52:0x00ee] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x010a  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x012b A[SYNTHETIC, Splitter:B:63:0x012b] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0147  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r12 = this;
            com.google.android.gms.internal.zzchq r0 = r12.zzjcm
            r0.zzawj()
            r0 = 0
            r1 = 0
            java.net.URL r2 = r12.zzbxv     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            java.net.URLConnection r2 = r2.openConnection()     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            boolean r3 = r2 instanceof java.net.HttpURLConnection     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            if (r3 != 0) goto L_0x0019
            java.io.IOException r2 = new java.io.IOException     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            java.lang.String r3 = "Failed to obtain HTTP connection"
            r2.<init>(r3)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            throw r2     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
        L_0x0019:
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            r2.setDefaultUseCaches(r0)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            r3 = 60000(0xea60, float:8.4078E-41)
            r2.setConnectTimeout(r3)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            r3 = 61000(0xee48, float:8.5479E-41)
            r2.setReadTimeout(r3)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            r2.setInstanceFollowRedirects(r0)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            r3 = 1
            r2.setDoInput(r3)     // Catch:{ IOException -> 0x0124, all -> 0x00e7 }
            java.util.Map<java.lang.String, java.lang.String> r4 = r12.zzjcl     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            if (r4 == 0) goto L_0x005b
            java.util.Map<java.lang.String, java.lang.String> r4 = r12.zzjcl     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.util.Set r4 = r4.entrySet()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.util.Iterator r4 = r4.iterator()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
        L_0x003f:
            boolean r5 = r4.hasNext()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            if (r5 == 0) goto L_0x005b
            java.lang.Object r5 = r4.next()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.Object r6 = r5.getKey()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.Object r5 = r5.getValue()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r2.addRequestProperty(r6, r5)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            goto L_0x003f
        L_0x005b:
            byte[] r4 = r12.zzgfx     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            if (r4 == 0) goto L_0x00a5
            com.google.android.gms.internal.zzchq r4 = r12.zzjcm     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            com.google.android.gms.internal.zzclq r4 = r4.zzawu()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            byte[] r5 = r12.zzgfx     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            byte[] r4 = r4.zzq(r5)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            com.google.android.gms.internal.zzchq r5 = r12.zzjcm     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            com.google.android.gms.internal.zzchm r5 = r5.zzawy()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            com.google.android.gms.internal.zzcho r5 = r5.zzazj()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.String r6 = "Uploading data. size"
            int r7 = r4.length     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r5.zzj(r6, r7)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r2.setDoOutput(r3)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.lang.String r3 = "Content-Encoding"
            java.lang.String r5 = "gzip"
            r2.addRequestProperty(r3, r5)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            int r3 = r4.length     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r2.setFixedLengthStreamingMode(r3)     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r2.connect()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.io.OutputStream r3 = r2.getOutputStream()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            r3.write(r4)     // Catch:{ IOException -> 0x00a0, all -> 0x009b }
            r3.close()     // Catch:{ IOException -> 0x00a0, all -> 0x009b }
            goto L_0x00a5
        L_0x009b:
            r4 = move-exception
            r10 = r1
            r1 = r3
            r3 = r4
            goto L_0x00e0
        L_0x00a0:
            r4 = move-exception
            r9 = r1
            r1 = r3
            r7 = r4
            goto L_0x00e5
        L_0x00a5:
            int r7 = r2.getResponseCode()     // Catch:{ IOException -> 0x00e2, all -> 0x00de }
            java.util.Map r10 = r2.getHeaderFields()     // Catch:{ IOException -> 0x00d9, all -> 0x00d5 }
            com.google.android.gms.internal.zzchq r0 = r12.zzjcm     // Catch:{ IOException -> 0x00d1, all -> 0x00ce }
            byte[] r9 = com.google.android.gms.internal.zzchq.zzc(r2)     // Catch:{ IOException -> 0x00d1, all -> 0x00ce }
            if (r2 == 0) goto L_0x00b8
            r2.disconnect()
        L_0x00b8:
            com.google.android.gms.internal.zzchq r0 = r12.zzjcm
            com.google.android.gms.internal.zzcih r0 = r0.zzawx()
            com.google.android.gms.internal.zzcht r1 = new com.google.android.gms.internal.zzcht
            java.lang.String r5 = r12.mPackageName
            com.google.android.gms.internal.zzchs r6 = r12.zzjck
            r8 = 0
            r11 = 0
            r4 = r1
            r4.<init>(r5, r6, r7, r8, r9, r10)
        L_0x00ca:
            r0.zzg(r1)
            return
        L_0x00ce:
            r0 = move-exception
            r3 = r0
            goto L_0x00ec
        L_0x00d1:
            r0 = move-exception
            r6 = r7
            r9 = r10
            goto L_0x00dc
        L_0x00d5:
            r0 = move-exception
            r3 = r0
            r10 = r1
            goto L_0x00ec
        L_0x00d9:
            r0 = move-exception
            r9 = r1
            r6 = r7
        L_0x00dc:
            r7 = r0
            goto L_0x0129
        L_0x00de:
            r3 = move-exception
            r10 = r1
        L_0x00e0:
            r7 = 0
            goto L_0x00ec
        L_0x00e2:
            r3 = move-exception
            r9 = r1
            r7 = r3
        L_0x00e5:
            r6 = 0
            goto L_0x0129
        L_0x00e7:
            r2 = move-exception
            r10 = r1
            r3 = r2
            r7 = 0
            r2 = r10
        L_0x00ec:
            if (r1 == 0) goto L_0x0108
            r1.close()     // Catch:{ IOException -> 0x00f2 }
            goto L_0x0108
        L_0x00f2:
            r0 = move-exception
            com.google.android.gms.internal.zzchq r1 = r12.zzjcm
            com.google.android.gms.internal.zzchm r1 = r1.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()
            java.lang.String r4 = "Error closing HTTP compressed POST connection output stream. appId"
            java.lang.String r5 = r12.mPackageName
            java.lang.Object r5 = com.google.android.gms.internal.zzchm.zzjk(r5)
            r1.zze(r4, r5, r0)
        L_0x0108:
            if (r2 == 0) goto L_0x010d
            r2.disconnect()
        L_0x010d:
            com.google.android.gms.internal.zzchq r0 = r12.zzjcm
            com.google.android.gms.internal.zzcih r0 = r0.zzawx()
            com.google.android.gms.internal.zzcht r1 = new com.google.android.gms.internal.zzcht
            java.lang.String r5 = r12.mPackageName
            com.google.android.gms.internal.zzchs r6 = r12.zzjck
            r8 = 0
            r9 = 0
            r11 = 0
            r4 = r1
            r4.<init>(r5, r6, r7, r8, r9, r10)
            r0.zzg(r1)
            throw r3
        L_0x0124:
            r2 = move-exception
            r9 = r1
            r7 = r2
            r6 = 0
            r2 = r9
        L_0x0129:
            if (r1 == 0) goto L_0x0145
            r1.close()     // Catch:{ IOException -> 0x012f }
            goto L_0x0145
        L_0x012f:
            r0 = move-exception
            com.google.android.gms.internal.zzchq r1 = r12.zzjcm
            com.google.android.gms.internal.zzchm r1 = r1.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()
            java.lang.String r3 = "Error closing HTTP compressed POST connection output stream. appId"
            java.lang.String r4 = r12.mPackageName
            java.lang.Object r4 = com.google.android.gms.internal.zzchm.zzjk(r4)
            r1.zze(r3, r4, r0)
        L_0x0145:
            if (r2 == 0) goto L_0x014a
            r2.disconnect()
        L_0x014a:
            com.google.android.gms.internal.zzchq r0 = r12.zzjcm
            com.google.android.gms.internal.zzcih r0 = r0.zzawx()
            com.google.android.gms.internal.zzcht r1 = new com.google.android.gms.internal.zzcht
            java.lang.String r4 = r12.mPackageName
            com.google.android.gms.internal.zzchs r5 = r12.zzjck
            r8 = 0
            r10 = 0
            r3 = r1
            r3.<init>(r4, r5, r6, r7, r8, r9)
            goto L_0x00ca
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchu.run():void");
    }
}
