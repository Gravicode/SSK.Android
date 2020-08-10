package com.google.firebase.iid;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.stats.zza;
import java.util.ArrayDeque;
import java.util.Queue;

final class zzk implements ServiceConnection {
    int state;
    final Messenger zznzb;
    zzp zznzc;
    final Queue<zzr<?>> zznzd;
    final SparseArray<zzr<?>> zznze;
    final /* synthetic */ zzi zznzf;

    private zzk(zzi zzi) {
        this.zznzf = zzi;
        this.state = 0;
        this.zznzb = new Messenger(new Handler(Looper.getMainLooper(), new zzl(this)));
        this.zznzd = new ArrayDeque();
        this.zznze = new SparseArray<>();
    }

    private final void zza(zzs zzs) {
        for (zzr zzb : this.zznzd) {
            zzb.zzb(zzs);
        }
        this.zznzd.clear();
        for (int i = 0; i < this.zznze.size(); i++) {
            ((zzr) this.zznze.valueAt(i)).zzb(zzs);
        }
        this.zznze.clear();
    }

    private final void zzcjb() {
        this.zznzf.zznyy.execute(new zzn(this));
    }

    public final synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service connected");
        }
        if (iBinder == null) {
            zzm(0, "Null service connection");
            return;
        }
        try {
            this.zznzc = new zzp(iBinder);
            this.state = 2;
            zzcjb();
        } catch (RemoteException e) {
            zzm(0, e.getMessage());
        }
    }

    public final synchronized void onServiceDisconnected(ComponentName componentName) {
        if (Log.isLoggable("MessengerIpcClient", 2)) {
            Log.v("MessengerIpcClient", "Service disconnected");
        }
        zzm(2, "Service disconnected");
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0074, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean zzb(com.google.firebase.iid.zzr r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            int r0 = r5.state     // Catch:{ all -> 0x008e }
            r1 = 0
            r2 = 1
            switch(r0) {
                case 0: goto L_0x001e;
                case 1: goto L_0x0017;
                case 2: goto L_0x000d;
                case 3: goto L_0x000b;
                case 4: goto L_0x000b;
                default: goto L_0x0008;
            }     // Catch:{ all -> 0x008e }
        L_0x0008:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException     // Catch:{ all -> 0x008e }
            goto L_0x0075
        L_0x000b:
            monitor-exit(r5)
            return r1
        L_0x000d:
            java.util.Queue<com.google.firebase.iid.zzr<?>> r0 = r5.zznzd     // Catch:{ all -> 0x008e }
            r0.add(r6)     // Catch:{ all -> 0x008e }
            r5.zzcjb()     // Catch:{ all -> 0x008e }
            monitor-exit(r5)
            return r2
        L_0x0017:
            java.util.Queue<com.google.firebase.iid.zzr<?>> r0 = r5.zznzd     // Catch:{ all -> 0x008e }
            r0.add(r6)     // Catch:{ all -> 0x008e }
            monitor-exit(r5)
            return r2
        L_0x001e:
            java.util.Queue<com.google.firebase.iid.zzr<?>> r0 = r5.zznzd     // Catch:{ all -> 0x008e }
            r0.add(r6)     // Catch:{ all -> 0x008e }
            int r6 = r5.state     // Catch:{ all -> 0x008e }
            if (r6 != 0) goto L_0x0029
            r6 = 1
            goto L_0x002a
        L_0x0029:
            r6 = 0
        L_0x002a:
            com.google.android.gms.common.internal.zzbq.checkState(r6)     // Catch:{ all -> 0x008e }
            java.lang.String r6 = "MessengerIpcClient"
            r0 = 2
            boolean r6 = android.util.Log.isLoggable(r6, r0)     // Catch:{ all -> 0x008e }
            if (r6 == 0) goto L_0x003d
            java.lang.String r6 = "MessengerIpcClient"
            java.lang.String r0 = "Starting bind to GmsCore"
            android.util.Log.v(r6, r0)     // Catch:{ all -> 0x008e }
        L_0x003d:
            r5.state = r2     // Catch:{ all -> 0x008e }
            android.content.Intent r6 = new android.content.Intent     // Catch:{ all -> 0x008e }
            java.lang.String r0 = "com.google.android.c2dm.intent.REGISTER"
            r6.<init>(r0)     // Catch:{ all -> 0x008e }
            java.lang.String r0 = "com.google.android.gms"
            r6.setPackage(r0)     // Catch:{ all -> 0x008e }
            com.google.android.gms.common.stats.zza r0 = com.google.android.gms.common.stats.zza.zzamc()     // Catch:{ all -> 0x008e }
            com.google.firebase.iid.zzi r3 = r5.zznzf     // Catch:{ all -> 0x008e }
            android.content.Context r3 = r3.zzair     // Catch:{ all -> 0x008e }
            boolean r6 = r0.zza(r3, r6, r5, r2)     // Catch:{ all -> 0x008e }
            if (r6 != 0) goto L_0x0061
            java.lang.String r6 = "Unable to bind to service"
            r5.zzm(r1, r6)     // Catch:{ all -> 0x008e }
            goto L_0x0073
        L_0x0061:
            com.google.firebase.iid.zzi r6 = r5.zznzf     // Catch:{ all -> 0x008e }
            java.util.concurrent.ScheduledExecutorService r6 = r6.zznyy     // Catch:{ all -> 0x008e }
            com.google.firebase.iid.zzm r0 = new com.google.firebase.iid.zzm     // Catch:{ all -> 0x008e }
            r0.<init>(r5)     // Catch:{ all -> 0x008e }
            r3 = 30
            java.util.concurrent.TimeUnit r1 = java.util.concurrent.TimeUnit.SECONDS     // Catch:{ all -> 0x008e }
            r6.schedule(r0, r3, r1)     // Catch:{ all -> 0x008e }
        L_0x0073:
            monitor-exit(r5)
            return r2
        L_0x0075:
            int r0 = r5.state     // Catch:{ all -> 0x008e }
            r1 = 26
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x008e }
            r2.<init>(r1)     // Catch:{ all -> 0x008e }
            java.lang.String r1 = "Unknown state: "
            r2.append(r1)     // Catch:{ all -> 0x008e }
            r2.append(r0)     // Catch:{ all -> 0x008e }
            java.lang.String r0 = r2.toString()     // Catch:{ all -> 0x008e }
            r6.<init>(r0)     // Catch:{ all -> 0x008e }
            throw r6     // Catch:{ all -> 0x008e }
        L_0x008e:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzk.zzb(com.google.firebase.iid.zzr):boolean");
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzcjc() {
        if (this.state == 2 && this.zznzd.isEmpty() && this.zznze.size() == 0) {
            if (Log.isLoggable("MessengerIpcClient", 2)) {
                Log.v("MessengerIpcClient", "Finished handling requests, unbinding");
            }
            this.state = 3;
            zza.zzamc();
            this.zznzf.zzair.unbindService(this);
        }
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzcjd() {
        if (this.state == 1) {
            zzm(1, "Timed out while binding");
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0052, code lost:
        r5 = r5.getData();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005d, code lost:
        if (r5.getBoolean("unsupported", false) == false) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x005f, code lost:
        r1.zzb(new com.google.firebase.iid.zzs(4, "Not supported by GmsCore"));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x006a, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006b, code lost:
        r1.zzac(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006e, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzd(android.os.Message r5) {
        /*
            r4 = this;
            int r0 = r5.arg1
            java.lang.String r1 = "MessengerIpcClient"
            r2 = 3
            boolean r1 = android.util.Log.isLoggable(r1, r2)
            if (r1 == 0) goto L_0x0023
            java.lang.String r1 = "MessengerIpcClient"
            r2 = 41
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Received response to request: "
            r3.append(r2)
            r3.append(r0)
            java.lang.String r2 = r3.toString()
            android.util.Log.d(r1, r2)
        L_0x0023:
            monitor-enter(r4)
            android.util.SparseArray<com.google.firebase.iid.zzr<?>> r1 = r4.zznze     // Catch:{ all -> 0x006f }
            java.lang.Object r1 = r1.get(r0)     // Catch:{ all -> 0x006f }
            com.google.firebase.iid.zzr r1 = (com.google.firebase.iid.zzr) r1     // Catch:{ all -> 0x006f }
            r2 = 1
            if (r1 != 0) goto L_0x0049
            java.lang.String r5 = "MessengerIpcClient"
            r1 = 50
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x006f }
            r3.<init>(r1)     // Catch:{ all -> 0x006f }
            java.lang.String r1 = "Received response for unknown request: "
            r3.append(r1)     // Catch:{ all -> 0x006f }
            r3.append(r0)     // Catch:{ all -> 0x006f }
            java.lang.String r0 = r3.toString()     // Catch:{ all -> 0x006f }
            android.util.Log.w(r5, r0)     // Catch:{ all -> 0x006f }
            monitor-exit(r4)     // Catch:{ all -> 0x006f }
            return r2
        L_0x0049:
            android.util.SparseArray<com.google.firebase.iid.zzr<?>> r3 = r4.zznze     // Catch:{ all -> 0x006f }
            r3.remove(r0)     // Catch:{ all -> 0x006f }
            r4.zzcjc()     // Catch:{ all -> 0x006f }
            monitor-exit(r4)     // Catch:{ all -> 0x006f }
            android.os.Bundle r5 = r5.getData()
            java.lang.String r0 = "unsupported"
            r3 = 0
            boolean r0 = r5.getBoolean(r0, r3)
            if (r0 == 0) goto L_0x006b
            com.google.firebase.iid.zzs r5 = new com.google.firebase.iid.zzs
            r0 = 4
            java.lang.String r3 = "Not supported by GmsCore"
            r5.<init>(r0, r3)
            r1.zzb(r5)
            return r2
        L_0x006b:
            r1.zzac(r5)
            return r2
        L_0x006f:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x006f }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzk.zzd(android.os.Message):boolean");
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzic(int i) {
        zzr zzr = (zzr) this.zznze.get(i);
        if (zzr != null) {
            StringBuilder sb = new StringBuilder(31);
            sb.append("Timing out request: ");
            sb.append(i);
            Log.w("MessengerIpcClient", sb.toString());
            this.zznze.remove(i);
            zzr.zzb(new zzs(3, "Timed out waiting for response"));
            zzcjc();
        }
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzm(int i, String str) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String str2 = "MessengerIpcClient";
            String str3 = "Disconnected: ";
            String valueOf = String.valueOf(str);
            Log.d(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        }
        switch (this.state) {
            case 0:
                throw new IllegalStateException();
            case 1:
            case 2:
                if (Log.isLoggable("MessengerIpcClient", 2)) {
                    Log.v("MessengerIpcClient", "Unbinding service");
                }
                this.state = 4;
                zza.zzamc();
                this.zznzf.zzair.unbindService(this);
                zza(new zzs(i, str));
                return;
            case 3:
                this.state = 4;
                return;
            case 4:
                return;
            default:
                int i2 = this.state;
                StringBuilder sb = new StringBuilder(26);
                sb.append("Unknown state: ");
                sb.append(i2);
                throw new IllegalStateException(sb.toString());
        }
    }
}
