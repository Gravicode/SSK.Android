package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzbz;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzp;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class zzbo<O extends ApiOptions> implements ConnectionCallbacks, OnConnectionFailedListener, zzu {
    private final zzh<O> zzfmf;
    /* access modifiers changed from: private */
    public final zze zzfpv;
    private boolean zzfrw;
    final /* synthetic */ zzbm zzfti;
    private final Queue<zza> zzftj = new LinkedList();
    private final zzb zzftk;
    private final zzae zzftl;
    private final Set<zzj> zzftm = new HashSet();
    private final Map<zzck<?>, zzcr> zzftn = new HashMap();
    private final int zzfto;
    private final zzcv zzftp;
    private ConnectionResult zzftq = null;

    @WorkerThread
    public zzbo(zzbm zzbm, GoogleApi<O> googleApi) {
        this.zzfti = zzbm;
        this.zzfpv = googleApi.zza(zzbm.mHandler.getLooper(), this);
        this.zzftk = this.zzfpv instanceof zzbz ? zzbz.zzals() : this.zzfpv;
        this.zzfmf = googleApi.zzagn();
        this.zzftl = new zzae();
        this.zzfto = googleApi.getInstanceId();
        if (this.zzfpv.zzaay()) {
            this.zzftp = googleApi.zza(zzbm.mContext, zzbm.mHandler);
        } else {
            this.zzftp = null;
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzaiw() {
        zzaiz();
        zzi(ConnectionResult.zzfkr);
        zzajb();
        for (zzcr zzcr : this.zzftn.values()) {
            try {
                zzcr.zzfnq.zzb(this.zzftk, new TaskCompletionSource());
            } catch (DeadObjectException e) {
                onConnectionSuspended(1);
                this.zzfpv.disconnect();
            } catch (RemoteException e2) {
            }
        }
        while (this.zzfpv.isConnected() && !this.zzftj.isEmpty()) {
            zzb((zza) this.zzftj.remove());
        }
        zzajc();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzaix() {
        zzaiz();
        this.zzfrw = true;
        this.zzftl.zzahw();
        this.zzfti.mHandler.sendMessageDelayed(Message.obtain(this.zzfti.mHandler, 9, this.zzfmf), this.zzfti.zzfry);
        this.zzfti.mHandler.sendMessageDelayed(Message.obtain(this.zzfti.mHandler, 11, this.zzfmf), this.zzfti.zzfrx);
        this.zzfti.zzftc = -1;
    }

    @WorkerThread
    private final void zzajb() {
        if (this.zzfrw) {
            this.zzfti.mHandler.removeMessages(11, this.zzfmf);
            this.zzfti.mHandler.removeMessages(9, this.zzfmf);
            this.zzfrw = false;
        }
    }

    private final void zzajc() {
        this.zzfti.mHandler.removeMessages(12, this.zzfmf);
        this.zzfti.mHandler.sendMessageDelayed(this.zzfti.mHandler.obtainMessage(12, this.zzfmf), this.zzfti.zzfta);
    }

    @WorkerThread
    private final void zzb(zza zza) {
        zza.zza(this.zzftl, zzaay());
        try {
            zza.zza(this);
        } catch (DeadObjectException e) {
            onConnectionSuspended(1);
            this.zzfpv.disconnect();
        }
    }

    @WorkerThread
    private final void zzi(ConnectionResult connectionResult) {
        for (zzj zzj : this.zzftm) {
            String str = null;
            if (connectionResult == ConnectionResult.zzfkr) {
                str = this.zzfpv.zzagi();
            }
            zzj.zza(this.zzfmf, connectionResult, str);
        }
        this.zzftm.clear();
    }

    @WorkerThread
    public final void connect() {
        zzbq.zza(this.zzfti.mHandler);
        if (!this.zzfpv.isConnected() && !this.zzfpv.isConnecting()) {
            if (this.zzfpv.zzagg() && this.zzfti.zzftc != 0) {
                this.zzfti.zzftc = this.zzfti.zzfmy.isGooglePlayServicesAvailable(this.zzfti.mContext);
                if (this.zzfti.zzftc != 0) {
                    onConnectionFailed(new ConnectionResult(this.zzfti.zzftc, null));
                    return;
                }
            }
            zzbu zzbu = new zzbu(this.zzfti, this.zzfpv, this.zzfmf);
            if (this.zzfpv.zzaay()) {
                this.zzftp.zza((zzcy) zzbu);
            }
            this.zzfpv.zza((zzj) zzbu);
        }
    }

    public final int getInstanceId() {
        return this.zzfto;
    }

    /* access modifiers changed from: 0000 */
    public final boolean isConnected() {
        return this.zzfpv.isConnected();
    }

    public final void onConnected(@Nullable Bundle bundle) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            zzaiw();
        } else {
            this.zzfti.mHandler.post(new zzbp(this));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0069, code lost:
        if (r4.zzfti.zzc(r5, r4.zzfto) != false) goto L_0x00c8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0071, code lost:
        if (r5.getErrorCode() != 18) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0073, code lost:
        r4.zzfrw = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
        if (r4.zzfrw == false) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007a, code lost:
        r4.zzfti.mHandler.sendMessageDelayed(android.os.Message.obtain(r4.zzfti.mHandler, 9, r4.zzfmf), r4.zzfti.zzfry);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0097, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0098, code lost:
        r1 = r4.zzfmf.zzagy();
        r3 = new java.lang.StringBuilder(java.lang.String.valueOf(r1).length() + 38);
        r3.append("API: ");
        r3.append(r1);
        r3.append(" is not available on this device.");
        zzw(new com.google.android.gms.common.api.Status(17, r3.toString()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00c8, code lost:
        return;
     */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onConnectionFailed(@android.support.annotation.NonNull com.google.android.gms.common.ConnectionResult r5) {
        /*
            r4 = this;
            com.google.android.gms.common.api.internal.zzbm r0 = r4.zzfti
            android.os.Handler r0 = r0.mHandler
            com.google.android.gms.common.internal.zzbq.zza(r0)
            com.google.android.gms.common.api.internal.zzcv r0 = r4.zzftp
            if (r0 == 0) goto L_0x0012
            com.google.android.gms.common.api.internal.zzcv r0 = r4.zzftp
            r0.zzajq()
        L_0x0012:
            r4.zzaiz()
            com.google.android.gms.common.api.internal.zzbm r0 = r4.zzfti
            r1 = -1
            r0.zzftc = r1
            r4.zzi(r5)
            int r0 = r5.getErrorCode()
            r1 = 4
            if (r0 != r1) goto L_0x002d
            com.google.android.gms.common.api.Status r5 = com.google.android.gms.common.api.internal.zzbm.zzfsz
            r4.zzw(r5)
            return
        L_0x002d:
            java.util.Queue<com.google.android.gms.common.api.internal.zza> r0 = r4.zzftj
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x0038
            r4.zzftq = r5
            return
        L_0x0038:
            java.lang.Object r0 = com.google.android.gms.common.api.internal.zzbm.sLock
            monitor-enter(r0)
            com.google.android.gms.common.api.internal.zzbm r1 = r4.zzfti     // Catch:{ all -> 0x00c9 }
            com.google.android.gms.common.api.internal.zzah r1 = r1.zzftf     // Catch:{ all -> 0x00c9 }
            if (r1 == 0) goto L_0x0060
            com.google.android.gms.common.api.internal.zzbm r1 = r4.zzfti     // Catch:{ all -> 0x00c9 }
            java.util.Set r1 = r1.zzftg     // Catch:{ all -> 0x00c9 }
            com.google.android.gms.common.api.internal.zzh<O> r2 = r4.zzfmf     // Catch:{ all -> 0x00c9 }
            boolean r1 = r1.contains(r2)     // Catch:{ all -> 0x00c9 }
            if (r1 == 0) goto L_0x0060
            com.google.android.gms.common.api.internal.zzbm r1 = r4.zzfti     // Catch:{ all -> 0x00c9 }
            com.google.android.gms.common.api.internal.zzah r1 = r1.zzftf     // Catch:{ all -> 0x00c9 }
            int r2 = r4.zzfto     // Catch:{ all -> 0x00c9 }
            r1.zzb(r5, r2)     // Catch:{ all -> 0x00c9 }
            monitor-exit(r0)     // Catch:{ all -> 0x00c9 }
            return
        L_0x0060:
            monitor-exit(r0)     // Catch:{ all -> 0x00c9 }
            com.google.android.gms.common.api.internal.zzbm r0 = r4.zzfti
            int r1 = r4.zzfto
            boolean r0 = r0.zzc(r5, r1)
            if (r0 != 0) goto L_0x00c8
            int r5 = r5.getErrorCode()
            r0 = 18
            if (r5 != r0) goto L_0x0076
            r5 = 1
            r4.zzfrw = r5
        L_0x0076:
            boolean r5 = r4.zzfrw
            if (r5 == 0) goto L_0x0098
            com.google.android.gms.common.api.internal.zzbm r5 = r4.zzfti
            android.os.Handler r5 = r5.mHandler
            com.google.android.gms.common.api.internal.zzbm r0 = r4.zzfti
            android.os.Handler r0 = r0.mHandler
            r1 = 9
            com.google.android.gms.common.api.internal.zzh<O> r2 = r4.zzfmf
            android.os.Message r0 = android.os.Message.obtain(r0, r1, r2)
            com.google.android.gms.common.api.internal.zzbm r1 = r4.zzfti
            long r1 = r1.zzfry
            r5.sendMessageDelayed(r0, r1)
            return
        L_0x0098:
            com.google.android.gms.common.api.Status r5 = new com.google.android.gms.common.api.Status
            r0 = 17
            com.google.android.gms.common.api.internal.zzh<O> r1 = r4.zzfmf
            java.lang.String r1 = r1.zzagy()
            java.lang.String r2 = java.lang.String.valueOf(r1)
            int r2 = r2.length()
            int r2 = r2 + 38
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "API: "
            r3.append(r2)
            r3.append(r1)
            java.lang.String r1 = " is not available on this device."
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            r5.<init>(r0, r1)
            r4.zzw(r5)
        L_0x00c8:
            return
        L_0x00c9:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00c9 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzbo.onConnectionFailed(com.google.android.gms.common.ConnectionResult):void");
    }

    public final void onConnectionSuspended(int i) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            zzaix();
        } else {
            this.zzfti.mHandler.post(new zzbq(this));
        }
    }

    @WorkerThread
    public final void resume() {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfrw) {
            connect();
        }
    }

    @WorkerThread
    public final void signOut() {
        zzbq.zza(this.zzfti.mHandler);
        zzw(zzbm.zzfsy);
        this.zzftl.zzahv();
        for (zzck zzf : (zzck[]) this.zzftn.keySet().toArray(new zzck[this.zzftn.size()])) {
            zza((zza) new zzf(zzf, new TaskCompletionSource()));
        }
        zzi(new ConnectionResult(4));
        if (this.zzfpv.isConnected()) {
            this.zzfpv.zza((zzp) new zzbs(this));
        }
    }

    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (Looper.myLooper() == this.zzfti.mHandler.getLooper()) {
            onConnectionFailed(connectionResult);
        } else {
            this.zzfti.mHandler.post(new zzbr(this, connectionResult));
        }
    }

    @WorkerThread
    public final void zza(zza zza) {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfpv.isConnected()) {
            zzb(zza);
            zzajc();
            return;
        }
        this.zzftj.add(zza);
        if (this.zzftq == null || !this.zzftq.hasResolution()) {
            connect();
        } else {
            onConnectionFailed(this.zzftq);
        }
    }

    @WorkerThread
    public final void zza(zzj zzj) {
        zzbq.zza(this.zzfti.mHandler);
        this.zzftm.add(zzj);
    }

    public final boolean zzaay() {
        return this.zzfpv.zzaay();
    }

    public final zze zzahp() {
        return this.zzfpv;
    }

    @WorkerThread
    public final void zzaij() {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfrw) {
            zzajb();
            zzw(this.zzfti.zzfmy.isGooglePlayServicesAvailable(this.zzfti.mContext) == 18 ? new Status(8, "Connection timed out while waiting for Google Play services update to complete.") : new Status(8, "API failed to connect while resuming due to an unknown error."));
            this.zzfpv.disconnect();
        }
    }

    public final Map<zzck<?>, zzcr> zzaiy() {
        return this.zzftn;
    }

    @WorkerThread
    public final void zzaiz() {
        zzbq.zza(this.zzfti.mHandler);
        this.zzftq = null;
    }

    @WorkerThread
    public final ConnectionResult zzaja() {
        zzbq.zza(this.zzfti.mHandler);
        return this.zzftq;
    }

    @WorkerThread
    public final void zzajd() {
        zzbq.zza(this.zzfti.mHandler);
        if (this.zzfpv.isConnected() && this.zzftn.size() == 0) {
            if (this.zzftl.zzahu()) {
                zzajc();
                return;
            }
            this.zzfpv.disconnect();
        }
    }

    /* access modifiers changed from: 0000 */
    public final zzcxd zzaje() {
        if (this.zzftp == null) {
            return null;
        }
        return this.zzftp.zzaje();
    }

    @WorkerThread
    public final void zzh(@NonNull ConnectionResult connectionResult) {
        zzbq.zza(this.zzfti.mHandler);
        this.zzfpv.disconnect();
        onConnectionFailed(connectionResult);
    }

    @WorkerThread
    public final void zzw(Status status) {
        zzbq.zza(this.zzfti.mHandler);
        for (zza zzs : this.zzftj) {
            zzs.zzs(status);
        }
        this.zzftj.clear();
    }
}
