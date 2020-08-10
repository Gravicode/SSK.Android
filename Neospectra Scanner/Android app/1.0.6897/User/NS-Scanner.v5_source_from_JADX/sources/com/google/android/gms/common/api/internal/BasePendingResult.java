package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzaq;
import com.google.android.gms.common.internal.zzbq;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@KeepName
public abstract class BasePendingResult<R extends Result> extends PendingResult<R> {
    static final ThreadLocal<Boolean> zzfot = new zzs();
    @KeepName
    private zzb mResultGuardian;
    private Status mStatus;
    private boolean zzan;
    private final CountDownLatch zzapd;
    /* access modifiers changed from: private */
    public R zzfne;
    private final Object zzfou;
    private zza<R> zzfov;
    private WeakReference<GoogleApiClient> zzfow;
    private final ArrayList<com.google.android.gms.common.api.PendingResult.zza> zzfox;
    private ResultCallback<? super R> zzfoy;
    private final AtomicReference<zzdm> zzfoz;
    private volatile boolean zzfpa;
    private boolean zzfpb;
    private zzaq zzfpc;
    private volatile zzdg<R> zzfpd;
    private boolean zzfpe;

    public static class zza<R extends Result> extends Handler {
        public zza() {
            this(Looper.getMainLooper());
        }

        public zza(Looper looper) {
            super(looper);
        }

        public final void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Pair pair = (Pair) message.obj;
                    ResultCallback resultCallback = (ResultCallback) pair.first;
                    Result result = (Result) pair.second;
                    try {
                        resultCallback.onResult(result);
                        return;
                    } catch (RuntimeException e) {
                        BasePendingResult.zzd(result);
                        throw e;
                    }
                case 2:
                    ((BasePendingResult) message.obj).zzv(Status.zzfnl);
                    return;
                default:
                    int i = message.what;
                    StringBuilder sb = new StringBuilder(45);
                    sb.append("Don't know how to handle message: ");
                    sb.append(i);
                    Log.wtf("BasePendingResult", sb.toString(), new Exception());
                    return;
            }
        }

        public final void zza(ResultCallback<? super R> resultCallback, R r) {
            sendMessage(obtainMessage(1, new Pair(resultCallback, r)));
        }
    }

    final class zzb {
        private zzb() {
        }

        /* synthetic */ zzb(BasePendingResult basePendingResult, zzs zzs) {
            this();
        }

        /* access modifiers changed from: protected */
        public final void finalize() throws Throwable {
            BasePendingResult.zzd(BasePendingResult.this.zzfne);
            super.finalize();
        }
    }

    @Deprecated
    BasePendingResult() {
        this.zzfou = new Object();
        this.zzapd = new CountDownLatch(1);
        this.zzfox = new ArrayList<>();
        this.zzfoz = new AtomicReference<>();
        this.zzfpe = false;
        this.zzfov = new zza<>(Looper.getMainLooper());
        this.zzfow = new WeakReference<>(null);
    }

    @Deprecated
    protected BasePendingResult(Looper looper) {
        this.zzfou = new Object();
        this.zzapd = new CountDownLatch(1);
        this.zzfox = new ArrayList<>();
        this.zzfoz = new AtomicReference<>();
        this.zzfpe = false;
        this.zzfov = new zza<>(looper);
        this.zzfow = new WeakReference<>(null);
    }

    protected BasePendingResult(GoogleApiClient googleApiClient) {
        this.zzfou = new Object();
        this.zzapd = new CountDownLatch(1);
        this.zzfox = new ArrayList<>();
        this.zzfoz = new AtomicReference<>();
        this.zzfpe = false;
        this.zzfov = new zza<>(googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
        this.zzfow = new WeakReference<>(googleApiClient);
    }

    private final R get() {
        R r;
        synchronized (this.zzfou) {
            zzbq.zza(!this.zzfpa, (Object) "Result has already been consumed.");
            zzbq.zza(isReady(), (Object) "Result is not ready.");
            r = this.zzfne;
            this.zzfne = null;
            this.zzfoy = null;
            this.zzfpa = true;
        }
        zzdm zzdm = (zzdm) this.zzfoz.getAndSet(null);
        if (zzdm != null) {
            zzdm.zzc(this);
        }
        return r;
    }

    private final void zzc(R r) {
        this.zzfne = r;
        this.zzfpc = null;
        this.zzapd.countDown();
        this.mStatus = this.zzfne.getStatus();
        if (this.zzan) {
            this.zzfoy = null;
        } else if (this.zzfoy != null) {
            this.zzfov.removeMessages(2);
            this.zzfov.zza(this.zzfoy, get());
        } else if (this.zzfne instanceof Releasable) {
            this.mResultGuardian = new zzb(this, null);
        }
        ArrayList arrayList = this.zzfox;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((com.google.android.gms.common.api.PendingResult.zza) obj).zzr(this.mStatus);
        }
        this.zzfox.clear();
    }

    public static void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String valueOf = String.valueOf(result);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 18);
                sb.append("Unable to release ");
                sb.append(valueOf);
                Log.w("BasePendingResult", sb.toString(), e);
            }
        }
    }

    public final R await() {
        zzbq.zzgn("await must not be called on the UI thread");
        boolean z = true;
        zzbq.zza(!this.zzfpa, (Object) "Result has already been consumed");
        if (this.zzfpd != null) {
            z = false;
        }
        zzbq.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            this.zzapd.await();
        } catch (InterruptedException e) {
            zzv(Status.zzfnj);
        }
        zzbq.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    public final R await(long j, TimeUnit timeUnit) {
        if (j > 0) {
            zzbq.zzgn("await must not be called on the UI thread when time is greater than zero.");
        }
        boolean z = true;
        zzbq.zza(!this.zzfpa, (Object) "Result has already been consumed.");
        if (this.zzfpd != null) {
            z = false;
        }
        zzbq.zza(z, (Object) "Cannot await if then() has been called.");
        try {
            if (!this.zzapd.await(j, timeUnit)) {
                zzv(Status.zzfnl);
            }
        } catch (InterruptedException e) {
            zzv(Status.zzfnj);
        }
        zzbq.zza(isReady(), (Object) "Result is not ready.");
        return get();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel() {
        /*
            r2 = this;
            java.lang.Object r0 = r2.zzfou
            monitor-enter(r0)
            boolean r1 = r2.zzan     // Catch:{ all -> 0x002c }
            if (r1 != 0) goto L_0x002a
            boolean r1 = r2.zzfpa     // Catch:{ all -> 0x002c }
            if (r1 == 0) goto L_0x000c
            goto L_0x002a
        L_0x000c:
            com.google.android.gms.common.internal.zzaq r1 = r2.zzfpc     // Catch:{ all -> 0x002c }
            if (r1 == 0) goto L_0x0017
            com.google.android.gms.common.internal.zzaq r1 = r2.zzfpc     // Catch:{ RemoteException -> 0x0016 }
            r1.cancel()     // Catch:{ RemoteException -> 0x0016 }
            goto L_0x0017
        L_0x0016:
            r1 = move-exception
        L_0x0017:
            R r1 = r2.zzfne     // Catch:{ all -> 0x002c }
            zzd(r1)     // Catch:{ all -> 0x002c }
            r1 = 1
            r2.zzan = r1     // Catch:{ all -> 0x002c }
            com.google.android.gms.common.api.Status r1 = com.google.android.gms.common.api.Status.zzfnm     // Catch:{ all -> 0x002c }
            com.google.android.gms.common.api.Result r1 = r2.zzb(r1)     // Catch:{ all -> 0x002c }
            r2.zzc(r1)     // Catch:{ all -> 0x002c }
            monitor-exit(r0)     // Catch:{ all -> 0x002c }
            return
        L_0x002a:
            monitor-exit(r0)     // Catch:{ all -> 0x002c }
            return
        L_0x002c:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002c }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.cancel():void");
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this.zzfou) {
            z = this.zzan;
        }
        return z;
    }

    public final boolean isReady() {
        return this.zzapd.getCount() == 0;
    }

    public final void setResult(R r) {
        synchronized (this.zzfou) {
            if (this.zzfpb || this.zzan) {
                zzd(r);
                return;
            }
            isReady();
            zzbq.zza(!isReady(), (Object) "Results have already been set");
            zzbq.zza(!this.zzfpa, (Object) "Result has already been consumed");
            zzc(r);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003b, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r5) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.zzfou
            monitor-enter(r0)
            if (r5 != 0) goto L_0x000c
            r5 = 0
            r4.zzfoy = r5     // Catch:{ all -> 0x000a }
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x000a:
            r5 = move-exception
            goto L_0x003c
        L_0x000c:
            boolean r1 = r4.zzfpa     // Catch:{ all -> 0x000a }
            r2 = 1
            r1 = r1 ^ r2
            java.lang.String r3 = "Result has already been consumed."
            com.google.android.gms.common.internal.zzbq.zza(r1, r3)     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.zzdg<R> r1 = r4.zzfpd     // Catch:{ all -> 0x000a }
            if (r1 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r2 = 0
        L_0x001b:
            java.lang.String r1 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.zzbq.zza(r2, r1)     // Catch:{ all -> 0x000a }
            boolean r1 = r4.isCanceled()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0028
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x0028:
            boolean r1 = r4.isReady()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0038
            com.google.android.gms.common.api.internal.BasePendingResult$zza<R> r1 = r4.zzfov     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.Result r2 = r4.get()     // Catch:{ all -> 0x000a }
            r1.zza(r5, r2)     // Catch:{ all -> 0x000a }
            goto L_0x003a
        L_0x0038:
            r4.zzfoy = r5     // Catch:{ all -> 0x000a }
        L_0x003a:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x003c:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0049, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setResultCallback(com.google.android.gms.common.api.ResultCallback<? super R> r5, long r6, java.util.concurrent.TimeUnit r8) {
        /*
            r4 = this;
            java.lang.Object r0 = r4.zzfou
            monitor-enter(r0)
            if (r5 != 0) goto L_0x000c
            r5 = 0
            r4.zzfoy = r5     // Catch:{ all -> 0x000a }
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x000a:
            r5 = move-exception
            goto L_0x004a
        L_0x000c:
            boolean r1 = r4.zzfpa     // Catch:{ all -> 0x000a }
            r2 = 1
            r1 = r1 ^ r2
            java.lang.String r3 = "Result has already been consumed."
            com.google.android.gms.common.internal.zzbq.zza(r1, r3)     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.zzdg<R> r1 = r4.zzfpd     // Catch:{ all -> 0x000a }
            if (r1 != 0) goto L_0x001a
            goto L_0x001b
        L_0x001a:
            r2 = 0
        L_0x001b:
            java.lang.String r1 = "Cannot set callbacks if then() has been called."
            com.google.android.gms.common.internal.zzbq.zza(r2, r1)     // Catch:{ all -> 0x000a }
            boolean r1 = r4.isCanceled()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0028
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x0028:
            boolean r1 = r4.isReady()     // Catch:{ all -> 0x000a }
            if (r1 == 0) goto L_0x0038
            com.google.android.gms.common.api.internal.BasePendingResult$zza<R> r6 = r4.zzfov     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.Result r7 = r4.get()     // Catch:{ all -> 0x000a }
            r6.zza(r5, r7)     // Catch:{ all -> 0x000a }
            goto L_0x0048
        L_0x0038:
            r4.zzfoy = r5     // Catch:{ all -> 0x000a }
            com.google.android.gms.common.api.internal.BasePendingResult$zza<R> r5 = r4.zzfov     // Catch:{ all -> 0x000a }
            long r6 = r8.toMillis(r6)     // Catch:{ all -> 0x000a }
            r8 = 2
            android.os.Message r8 = r5.obtainMessage(r8, r4)     // Catch:{ all -> 0x000a }
            r5.sendMessageDelayed(r8, r6)     // Catch:{ all -> 0x000a }
        L_0x0048:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            return
        L_0x004a:
            monitor-exit(r0)     // Catch:{ all -> 0x000a }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.BasePendingResult.setResultCallback(com.google.android.gms.common.api.ResultCallback, long, java.util.concurrent.TimeUnit):void");
    }

    public <S extends Result> TransformedResult<S> then(ResultTransform<? super R, ? extends S> resultTransform) {
        TransformedResult<S> then;
        zzbq.zza(!this.zzfpa, (Object) "Result has already been consumed.");
        synchronized (this.zzfou) {
            boolean z = false;
            zzbq.zza(this.zzfpd == null, (Object) "Cannot call then() twice.");
            if (this.zzfoy == null) {
                z = true;
            }
            zzbq.zza(z, (Object) "Cannot call then() if callbacks are set.");
            zzbq.zza(!this.zzan, (Object) "Cannot call then() if result was canceled.");
            this.zzfpe = true;
            this.zzfpd = new zzdg<>(this.zzfow);
            then = this.zzfpd.then(resultTransform);
            if (isReady()) {
                this.zzfov.zza(this.zzfpd, get());
            } else {
                this.zzfoy = this.zzfpd;
            }
        }
        return then;
    }

    public final void zza(com.google.android.gms.common.api.PendingResult.zza zza2) {
        zzbq.checkArgument(zza2 != null, "Callback cannot be null.");
        synchronized (this.zzfou) {
            if (isReady()) {
                zza2.zzr(this.mStatus);
            } else {
                this.zzfox.add(zza2);
            }
        }
    }

    public final void zza(zzdm zzdm) {
        this.zzfoz.set(zzdm);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzaq zzaq) {
        synchronized (this.zzfou) {
            this.zzfpc = zzaq;
        }
    }

    public final Integer zzagv() {
        return null;
    }

    public final boolean zzahh() {
        boolean isCanceled;
        synchronized (this.zzfou) {
            if (((GoogleApiClient) this.zzfow.get()) == null || !this.zzfpe) {
                cancel();
            }
            isCanceled = isCanceled();
        }
        return isCanceled;
    }

    public final void zzahi() {
        this.zzfpe = this.zzfpe || ((Boolean) zzfot.get()).booleanValue();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public abstract R zzb(Status status);

    public final void zzv(Status status) {
        synchronized (this.zzfou) {
            if (!isReady()) {
                setResult(zzb(status));
                this.zzfpb = true;
            }
        }
    }
}
