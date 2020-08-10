package com.google.android.gms.common;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzba;
import com.google.android.gms.common.internal.zzbb;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.zzc;

final class zzg {
    private static zzba zzfky;
    private static final Object zzfkz = new Object();
    private static Context zzfla;

    static boolean zza(String str, zzh zzh) {
        return zza(str, zzh, false);
    }

    private static boolean zza(String str, zzh zzh, boolean z) {
        if (!zzafz()) {
            return false;
        }
        zzbq.checkNotNull(zzfla);
        try {
            return zzfky.zza(new zzn(str, zzh, z), zzn.zzz(zzfla.getPackageManager()));
        } catch (RemoteException e) {
            Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e);
            return false;
        }
    }

    private static boolean zzafz() {
        if (zzfky != null) {
            return true;
        }
        zzbq.checkNotNull(zzfla);
        synchronized (zzfkz) {
            if (zzfky == null) {
                try {
                    zzfky = zzbb.zzan(DynamiteModule.zza(zzfla, DynamiteModule.zzgwz, "com.google.android.gms.googlecertificates").zzhb("com.google.android.gms.common.GoogleCertificatesImpl"));
                } catch (zzc e) {
                    Log.e("GoogleCertificates", "Failed to load com.google.android.gms.googlecertificates", e);
                    return false;
                }
            }
        }
        return true;
    }

    static boolean zzb(String str, zzh zzh) {
        return zza(str, zzh, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static synchronized void zzcg(android.content.Context r2) {
        /*
            java.lang.Class<com.google.android.gms.common.zzg> r0 = com.google.android.gms.common.zzg.class
            monitor-enter(r0)
            android.content.Context r1 = zzfla     // Catch:{ all -> 0x001a }
            if (r1 != 0) goto L_0x0011
            if (r2 == 0) goto L_0x0018
            android.content.Context r2 = r2.getApplicationContext()     // Catch:{ all -> 0x001a }
            zzfla = r2     // Catch:{ all -> 0x001a }
            monitor-exit(r0)
            return
        L_0x0011:
            java.lang.String r2 = "GoogleCertificates"
            java.lang.String r1 = "GoogleCertificates has been initialized already"
            android.util.Log.w(r2, r1)     // Catch:{ all -> 0x001a }
        L_0x0018:
            monitor-exit(r0)
            return
        L_0x001a:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zzg.zzcg(android.content.Context):void");
    }
}
