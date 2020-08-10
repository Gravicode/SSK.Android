package com.google.android.gms.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.common.util.zzd;

public final class zzchq extends zzcjl {
    public zzchq(zzcim zzcim) {
        super(zzcim);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002b  */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] zzc(java.net.HttpURLConnection r5) throws java.io.IOException {
        /*
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x0025 }
            r1.<init>()     // Catch:{ all -> 0x0025 }
            java.io.InputStream r5 = r5.getInputStream()     // Catch:{ all -> 0x0025 }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ all -> 0x0023 }
        L_0x000e:
            int r2 = r5.read(r0)     // Catch:{ all -> 0x0023 }
            if (r2 <= 0) goto L_0x0019
            r3 = 0
            r1.write(r0, r3, r2)     // Catch:{ all -> 0x0023 }
            goto L_0x000e
        L_0x0019:
            byte[] r0 = r1.toByteArray()     // Catch:{ all -> 0x0023 }
            if (r5 == 0) goto L_0x0022
            r5.close()
        L_0x0022:
            return r0
        L_0x0023:
            r0 = move-exception
            goto L_0x0029
        L_0x0025:
            r5 = move-exception
            r4 = r0
            r0 = r5
            r5 = r4
        L_0x0029:
            if (r5 == 0) goto L_0x002e
            r5.close()
        L_0x002e:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchq.zzc(java.net.HttpURLConnection):byte[]");
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ void zzawi() {
        super.zzawi();
    }

    public final /* bridge */ /* synthetic */ void zzawj() {
        super.zzawj();
    }

    public final /* bridge */ /* synthetic */ zzcgd zzawk() {
        return super.zzawk();
    }

    public final /* bridge */ /* synthetic */ zzcgk zzawl() {
        return super.zzawl();
    }

    public final /* bridge */ /* synthetic */ zzcjn zzawm() {
        return super.zzawm();
    }

    public final /* bridge */ /* synthetic */ zzchh zzawn() {
        return super.zzawn();
    }

    public final /* bridge */ /* synthetic */ zzcgu zzawo() {
        return super.zzawo();
    }

    public final /* bridge */ /* synthetic */ zzckg zzawp() {
        return super.zzawp();
    }

    public final /* bridge */ /* synthetic */ zzckc zzawq() {
        return super.zzawq();
    }

    public final /* bridge */ /* synthetic */ zzchi zzawr() {
        return super.zzawr();
    }

    public final /* bridge */ /* synthetic */ zzcgo zzaws() {
        return super.zzaws();
    }

    public final /* bridge */ /* synthetic */ zzchk zzawt() {
        return super.zzawt();
    }

    public final /* bridge */ /* synthetic */ zzclq zzawu() {
        return super.zzawu();
    }

    public final /* bridge */ /* synthetic */ zzcig zzawv() {
        return super.zzawv();
    }

    public final /* bridge */ /* synthetic */ zzclf zzaww() {
        return super.zzaww();
    }

    public final /* bridge */ /* synthetic */ zzcih zzawx() {
        return super.zzawx();
    }

    public final /* bridge */ /* synthetic */ zzchm zzawy() {
        return super.zzawy();
    }

    public final /* bridge */ /* synthetic */ zzchx zzawz() {
        return super.zzawz();
    }

    public final /* bridge */ /* synthetic */ zzcgn zzaxa() {
        return super.zzaxa();
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }

    public final boolean zzzs() {
        NetworkInfo networkInfo;
        zzxf();
        try {
            networkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (SecurityException e) {
            networkInfo = null;
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
