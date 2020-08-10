package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzae implements Callback {
    private final Handler mHandler;
    private final Object mLock = new Object();
    private final zzaf zzgab;
    private final ArrayList<ConnectionCallbacks> zzgac = new ArrayList<>();
    private ArrayList<ConnectionCallbacks> zzgad = new ArrayList<>();
    private final ArrayList<OnConnectionFailedListener> zzgae = new ArrayList<>();
    private volatile boolean zzgaf = false;
    private final AtomicInteger zzgag = new AtomicInteger(0);
    private boolean zzgah = false;

    public zzae(Looper looper, zzaf zzaf) {
        this.zzgab = zzaf;
        this.mHandler = new Handler(looper, this);
    }

    public final boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.mLock) {
                if (this.zzgaf && this.zzgab.isConnected() && this.zzgac.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzgab.zzafi());
                }
            }
            return true;
        }
        int i = message.what;
        StringBuilder sb = new StringBuilder(45);
        sb.append("Don't know how to handle message: ");
        sb.append(i);
        Log.wtf("GmsClientEvents", sb.toString(), new Exception());
        return false;
    }

    public final boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        boolean contains;
        zzbq.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            contains = this.zzgac.contains(connectionCallbacks);
        }
        return contains;
    }

    public final boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        boolean contains;
        zzbq.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            contains = this.zzgae.contains(onConnectionFailedListener);
        }
        return contains;
    }

    public final void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzbq.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            if (this.zzgac.contains(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 62);
                sb.append("registerConnectionCallbacks(): listener ");
                sb.append(valueOf);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            } else {
                this.zzgac.add(connectionCallbacks);
            }
        }
        if (this.zzgab.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public final void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzbq.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (this.zzgae.contains(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 67);
                sb.append("registerConnectionFailedListener(): listener ");
                sb.append(valueOf);
                sb.append(" is already registered");
                Log.w("GmsClientEvents", sb.toString());
            } else {
                this.zzgae.add(onConnectionFailedListener);
            }
        }
    }

    public final void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzbq.checkNotNull(connectionCallbacks);
        synchronized (this.mLock) {
            if (!this.zzgac.remove(connectionCallbacks)) {
                String valueOf = String.valueOf(connectionCallbacks);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 52);
                sb.append("unregisterConnectionCallbacks(): listener ");
                sb.append(valueOf);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            } else if (this.zzgah) {
                this.zzgad.add(connectionCallbacks);
            }
        }
    }

    public final void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzbq.checkNotNull(onConnectionFailedListener);
        synchronized (this.mLock) {
            if (!this.zzgae.remove(onConnectionFailedListener)) {
                String valueOf = String.valueOf(onConnectionFailedListener);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 57);
                sb.append("unregisterConnectionFailedListener(): listener ");
                sb.append(valueOf);
                sb.append(" not found");
                Log.w("GmsClientEvents", sb.toString());
            }
        }
    }

    public final void zzali() {
        this.zzgaf = false;
        this.zzgag.incrementAndGet();
    }

    public final void zzalj() {
        this.zzgaf = true;
    }

    public final void zzcg(int i) {
        zzbq.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object) "onUnintentionalDisconnection must only be called on the Handler thread");
        this.mHandler.removeMessages(1);
        synchronized (this.mLock) {
            this.zzgah = true;
            ArrayList arrayList = new ArrayList(this.zzgac);
            int i2 = this.zzgag.get();
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i3 = 0;
            while (i3 < size) {
                Object obj = arrayList2.get(i3);
                i3++;
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) obj;
                if (!this.zzgaf || this.zzgag.get() != i2) {
                    break;
                } else if (this.zzgac.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzgad.clear();
            this.zzgah = false;
        }
    }

    public final void zzk(Bundle bundle) {
        boolean z = true;
        zzbq.zza(Looper.myLooper() == this.mHandler.getLooper(), (Object) "onConnectionSuccess must only be called on the Handler thread");
        synchronized (this.mLock) {
            zzbq.checkState(!this.zzgah);
            this.mHandler.removeMessages(1);
            this.zzgah = true;
            if (this.zzgad.size() != 0) {
                z = false;
            }
            zzbq.checkState(z);
            ArrayList arrayList = new ArrayList(this.zzgac);
            int i = this.zzgag.get();
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList2.get(i2);
                i2++;
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) obj;
                if (!this.zzgaf || !this.zzgab.isConnected() || this.zzgag.get() != i) {
                    break;
                } else if (!this.zzgad.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(bundle);
                }
            }
            this.zzgad.clear();
            this.zzgah = false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzk(com.google.android.gms.common.ConnectionResult r8) {
        /*
            r7 = this;
            android.os.Looper r0 = android.os.Looper.myLooper()
            android.os.Handler r1 = r7.mHandler
            android.os.Looper r1 = r1.getLooper()
            r2 = 0
            r3 = 1
            if (r0 != r1) goto L_0x0010
            r0 = 1
            goto L_0x0011
        L_0x0010:
            r0 = 0
        L_0x0011:
            java.lang.String r1 = "onConnectionFailure must only be called on the Handler thread"
            com.google.android.gms.common.internal.zzbq.zza(r0, r1)
            android.os.Handler r0 = r7.mHandler
            r0.removeMessages(r3)
            java.lang.Object r0 = r7.mLock
            monitor-enter(r0)
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0058 }
            java.util.ArrayList<com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener> r3 = r7.zzgae     // Catch:{ all -> 0x0058 }
            r1.<init>(r3)     // Catch:{ all -> 0x0058 }
            java.util.concurrent.atomic.AtomicInteger r3 = r7.zzgag     // Catch:{ all -> 0x0058 }
            int r3 = r3.get()     // Catch:{ all -> 0x0058 }
            java.util.ArrayList r1 = (java.util.ArrayList) r1     // Catch:{ all -> 0x0058 }
            int r4 = r1.size()     // Catch:{ all -> 0x0058 }
        L_0x0031:
            if (r2 >= r4) goto L_0x0056
            java.lang.Object r5 = r1.get(r2)     // Catch:{ all -> 0x0058 }
            int r2 = r2 + 1
            com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener r5 = (com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener) r5     // Catch:{ all -> 0x0058 }
            boolean r6 = r7.zzgaf     // Catch:{ all -> 0x0058 }
            if (r6 == 0) goto L_0x0054
            java.util.concurrent.atomic.AtomicInteger r6 = r7.zzgag     // Catch:{ all -> 0x0058 }
            int r6 = r6.get()     // Catch:{ all -> 0x0058 }
            if (r6 == r3) goto L_0x0048
            goto L_0x0054
        L_0x0048:
            java.util.ArrayList<com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener> r6 = r7.zzgae     // Catch:{ all -> 0x0058 }
            boolean r6 = r6.contains(r5)     // Catch:{ all -> 0x0058 }
            if (r6 == 0) goto L_0x0031
            r5.onConnectionFailed(r8)     // Catch:{ all -> 0x0058 }
            goto L_0x0031
        L_0x0054:
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            return
        L_0x0056:
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            return
        L_0x0058:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0058 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzae.zzk(com.google.android.gms.common.ConnectionResult):void");
    }
}
