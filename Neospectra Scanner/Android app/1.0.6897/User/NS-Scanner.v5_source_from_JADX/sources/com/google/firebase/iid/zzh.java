package com.google.firebase.iid;

import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public final class zzh implements ServiceConnection {
    private final Context zzair;
    private final Intent zzifb;
    private final ScheduledExecutorService zzifc;
    private final Queue<zzd> zzifd;
    private boolean zziff;
    private zzf zznyo;

    public zzh(Context context, String str) {
        this(context, str, new ScheduledThreadPoolExecutor(0));
    }

    @VisibleForTesting
    private zzh(Context context, String str, ScheduledExecutorService scheduledExecutorService) {
        this.zzifd = new ArrayDeque();
        this.zziff = false;
        this.zzair = context.getApplicationContext();
        this.zzifb = new Intent(str).setPackage(this.zzair.getPackageName());
        this.zzifc = scheduledExecutorService;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b2, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final synchronized void zzavd() {
        /*
            r5 = this;
            monitor-enter(r5)
            java.lang.String r0 = "EnhancedIntentService"
            r1 = 3
            boolean r0 = android.util.Log.isLoggable(r0, r1)     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0011
            java.lang.String r0 = "EnhancedIntentService"
            java.lang.String r2 = "flush queue called"
            android.util.Log.d(r0, r2)     // Catch:{ all -> 0x00b5 }
        L_0x0011:
            java.util.Queue<com.google.firebase.iid.zzd> r0 = r5.zzifd     // Catch:{ all -> 0x00b5 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x00b5 }
            if (r0 != 0) goto L_0x00b3
            java.lang.String r0 = "EnhancedIntentService"
            boolean r0 = android.util.Log.isLoggable(r0, r1)     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0028
            java.lang.String r0 = "EnhancedIntentService"
            java.lang.String r2 = "found intent to be delivered"
            android.util.Log.d(r0, r2)     // Catch:{ all -> 0x00b5 }
        L_0x0028:
            com.google.firebase.iid.zzf r0 = r5.zznyo     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0051
            com.google.firebase.iid.zzf r0 = r5.zznyo     // Catch:{ all -> 0x00b5 }
            boolean r0 = r0.isBinderAlive()     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0051
            java.lang.String r0 = "EnhancedIntentService"
            boolean r0 = android.util.Log.isLoggable(r0, r1)     // Catch:{ all -> 0x00b5 }
            if (r0 == 0) goto L_0x0043
            java.lang.String r0 = "EnhancedIntentService"
            java.lang.String r2 = "binder is alive, sending the intent."
            android.util.Log.d(r0, r2)     // Catch:{ all -> 0x00b5 }
        L_0x0043:
            java.util.Queue<com.google.firebase.iid.zzd> r0 = r5.zzifd     // Catch:{ all -> 0x00b5 }
            java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x00b5 }
            com.google.firebase.iid.zzd r0 = (com.google.firebase.iid.zzd) r0     // Catch:{ all -> 0x00b5 }
            com.google.firebase.iid.zzf r2 = r5.zznyo     // Catch:{ all -> 0x00b5 }
            r2.zza(r0)     // Catch:{ all -> 0x00b5 }
            goto L_0x0011
        L_0x0051:
            java.lang.String r0 = "EnhancedIntentService"
            boolean r0 = android.util.Log.isLoggable(r0, r1)     // Catch:{ all -> 0x00b5 }
            r1 = 1
            if (r0 == 0) goto L_0x0075
            java.lang.String r0 = "EnhancedIntentService"
            boolean r2 = r5.zziff     // Catch:{ all -> 0x00b5 }
            r2 = r2 ^ r1
            r3 = 39
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00b5 }
            r4.<init>(r3)     // Catch:{ all -> 0x00b5 }
            java.lang.String r3 = "binder is dead. start connection? "
            r4.append(r3)     // Catch:{ all -> 0x00b5 }
            r4.append(r2)     // Catch:{ all -> 0x00b5 }
            java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x00b5 }
            android.util.Log.d(r0, r2)     // Catch:{ all -> 0x00b5 }
        L_0x0075:
            boolean r0 = r5.zziff     // Catch:{ all -> 0x00b5 }
            if (r0 != 0) goto L_0x00b1
            r5.zziff = r1     // Catch:{ all -> 0x00b5 }
            com.google.android.gms.common.stats.zza r0 = com.google.android.gms.common.stats.zza.zzamc()     // Catch:{ SecurityException -> 0x0095 }
            android.content.Context r1 = r5.zzair     // Catch:{ SecurityException -> 0x0095 }
            android.content.Intent r2 = r5.zzifb     // Catch:{ SecurityException -> 0x0095 }
            r3 = 65
            boolean r0 = r0.zza(r1, r2, r5, r3)     // Catch:{ SecurityException -> 0x0095 }
            if (r0 == 0) goto L_0x008d
            monitor-exit(r5)
            return
        L_0x008d:
            java.lang.String r0 = "EnhancedIntentService"
            java.lang.String r1 = "binding to the service failed"
            android.util.Log.e(r0, r1)     // Catch:{ SecurityException -> 0x0095 }
            goto L_0x009d
        L_0x0095:
            r0 = move-exception
            java.lang.String r1 = "EnhancedIntentService"
            java.lang.String r2 = "Exception while binding the service"
            android.util.Log.e(r1, r2, r0)     // Catch:{ all -> 0x00b5 }
        L_0x009d:
            java.util.Queue<com.google.firebase.iid.zzd> r0 = r5.zzifd     // Catch:{ all -> 0x00b5 }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x00b5 }
            if (r0 != 0) goto L_0x00b1
            java.util.Queue<com.google.firebase.iid.zzd> r0 = r5.zzifd     // Catch:{ all -> 0x00b5 }
            java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x00b5 }
            com.google.firebase.iid.zzd r0 = (com.google.firebase.iid.zzd) r0     // Catch:{ all -> 0x00b5 }
            r0.finish()     // Catch:{ all -> 0x00b5 }
            goto L_0x009d
        L_0x00b1:
            monitor-exit(r5)
            return
        L_0x00b3:
            monitor-exit(r5)
            return
        L_0x00b5:
            r0 = move-exception
            monitor-exit(r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzh.zzavd():void");
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this) {
            this.zziff = false;
            this.zznyo = (zzf) iBinder;
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                String valueOf = String.valueOf(componentName);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 20);
                sb.append("onServiceConnected: ");
                sb.append(valueOf);
                Log.d("EnhancedIntentService", sb.toString());
            }
            zzavd();
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            String valueOf = String.valueOf(componentName);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
            sb.append("onServiceDisconnected: ");
            sb.append(valueOf);
            Log.d("EnhancedIntentService", sb.toString());
        }
        zzavd();
    }

    public final synchronized void zza(Intent intent, PendingResult pendingResult) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "new intent queued in the bind-strategy delivery");
        }
        this.zzifd.add(new zzd(intent, pendingResult, this.zzifc));
        zzavd();
    }
}
