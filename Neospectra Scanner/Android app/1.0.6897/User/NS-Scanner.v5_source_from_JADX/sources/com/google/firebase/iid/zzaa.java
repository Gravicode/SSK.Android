package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.IOException;

final class zzaa implements Runnable {
    private final zzu zznys;
    private final long zznzv;
    private final WakeLock zznzw = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zznzx;

    zzaa(FirebaseInstanceId firebaseInstanceId, zzu zzu, long j) {
        this.zznzx = firebaseInstanceId;
        this.zznys = zzu;
        this.zznzv = j;
        this.zznzw.setReferenceCounted(false);
    }

    private final boolean zzcjn() {
        zzz zzciu = this.zznzx.zzciu();
        if (zzciu != null && !zzciu.zzro(this.zznys.zzcjg())) {
            return true;
        }
        try {
            String zzciv = this.zznzx.zzciv();
            if (zzciv == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzciu == null || (zzciu != null && !zzciv.equals(zzciu.zzldj))) {
                Context context = getContext();
                Intent intent = new Intent("com.google.firebase.iid.TOKEN_REFRESH");
                Intent intent2 = new Intent("com.google.firebase.INSTANCE_ID_EVENT");
                intent2.setClass(context, FirebaseInstanceIdReceiver.class);
                intent2.putExtra("wrapped_intent", intent);
                context.sendBroadcast(intent2);
            }
            return true;
        } catch (IOException | SecurityException e) {
            String str = "FirebaseInstanceId";
            String str2 = "Token retrieval failed: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        if (zzrp(r1) != false) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzcjo() {
        /*
            r3 = this;
        L_0x0000:
            com.google.firebase.iid.FirebaseInstanceId r0 = r3.zznzx
            monitor-enter(r0)
            com.google.firebase.iid.zzy r1 = com.google.firebase.iid.FirebaseInstanceId.zzciw()     // Catch:{ all -> 0x0028 }
            java.lang.String r1 = r1.zzcjm()     // Catch:{ all -> 0x0028 }
            if (r1 != 0) goto L_0x0017
            java.lang.String r1 = "FirebaseInstanceId"
            java.lang.String r2 = "topic sync succeeded"
            android.util.Log.d(r1, r2)     // Catch:{ all -> 0x0028 }
            r1 = 1
            monitor-exit(r0)     // Catch:{ all -> 0x0028 }
            return r1
        L_0x0017:
            monitor-exit(r0)     // Catch:{ all -> 0x0028 }
            boolean r0 = r3.zzrp(r1)
            if (r0 != 0) goto L_0x0020
            r0 = 0
            return r0
        L_0x0020:
            com.google.firebase.iid.zzy r0 = com.google.firebase.iid.FirebaseInstanceId.zzciw()
            r0.zzri(r1)
            goto L_0x0000
        L_0x0028:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0028 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzaa.zzcjo():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
        android.util.Log.d(r7, r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0047, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final boolean zzrp(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.String r0 = "!"
            java.lang.String[] r7 = r7.split(r0)
            int r0 = r7.length
            r1 = 1
            r2 = 2
            if (r0 != r2) goto L_0x0079
            r0 = 0
            r2 = r7[r0]
            r7 = r7[r1]
            r3 = -1
            int r4 = r2.hashCode()     // Catch:{ IOException -> 0x0058 }
            r5 = 83
            if (r4 == r5) goto L_0x0028
            r5 = 85
            if (r4 == r5) goto L_0x001e
            goto L_0x0031
        L_0x001e:
            java.lang.String r4 = "U"
            boolean r2 = r2.equals(r4)     // Catch:{ IOException -> 0x0058 }
            if (r2 == 0) goto L_0x0031
            r3 = 1
            goto L_0x0031
        L_0x0028:
            java.lang.String r4 = "S"
            boolean r2 = r2.equals(r4)     // Catch:{ IOException -> 0x0058 }
            if (r2 == 0) goto L_0x0031
            r3 = 0
        L_0x0031:
            switch(r3) {
                case 0: goto L_0x0048;
                case 1: goto L_0x0035;
                default: goto L_0x0034;
            }     // Catch:{ IOException -> 0x0058 }
        L_0x0034:
            return r1
        L_0x0035:
            com.google.firebase.iid.FirebaseInstanceId r2 = r6.zznzx     // Catch:{ IOException -> 0x0058 }
            r2.zzrh(r7)     // Catch:{ IOException -> 0x0058 }
            boolean r7 = com.google.firebase.iid.FirebaseInstanceId.zzcix()     // Catch:{ IOException -> 0x0058 }
            if (r7 == 0) goto L_0x0079
            java.lang.String r7 = "FirebaseInstanceId"
            java.lang.String r2 = "unsubscribe operation succeeded"
        L_0x0044:
            android.util.Log.d(r7, r2)     // Catch:{ IOException -> 0x0058 }
            return r1
        L_0x0048:
            com.google.firebase.iid.FirebaseInstanceId r2 = r6.zznzx     // Catch:{ IOException -> 0x0058 }
            r2.zzrg(r7)     // Catch:{ IOException -> 0x0058 }
            boolean r7 = com.google.firebase.iid.FirebaseInstanceId.zzcix()     // Catch:{ IOException -> 0x0058 }
            if (r7 == 0) goto L_0x0079
            java.lang.String r7 = "FirebaseInstanceId"
            java.lang.String r2 = "subscribe operation succeeded"
            goto L_0x0044
        L_0x0058:
            r7 = move-exception
            java.lang.String r1 = "FirebaseInstanceId"
            java.lang.String r2 = "Topic sync failed: "
            java.lang.String r7 = r7.getMessage()
            java.lang.String r7 = java.lang.String.valueOf(r7)
            int r3 = r7.length()
            if (r3 == 0) goto L_0x0070
            java.lang.String r7 = r2.concat(r7)
            goto L_0x0075
        L_0x0070:
            java.lang.String r7 = new java.lang.String
            r7.<init>(r2)
        L_0x0075:
            android.util.Log.e(r1, r7)
            return r0
        L_0x0079:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzaa.zzrp(java.lang.String):boolean");
    }

    /* access modifiers changed from: 0000 */
    public final Context getContext() {
        return this.zznzx.getApp().getApplicationContext();
    }

    public final void run() {
        FirebaseInstanceId firebaseInstanceId;
        this.zznzw.acquire();
        try {
            boolean z = true;
            this.zznzx.zzcr(true);
            if (this.zznys.zzcjf() == 0) {
                z = false;
            }
            if (!z) {
                firebaseInstanceId = this.zznzx;
            } else {
                if (!zzcjp()) {
                    new zzab(this).zzcjq();
                } else if (!zzcjn() || !zzcjo()) {
                    this.zznzx.zzcc(this.zznzv);
                } else {
                    firebaseInstanceId = this.zznzx;
                }
            }
            firebaseInstanceId.zzcr(false);
        } finally {
            this.zznzw.release();
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzcjp() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
