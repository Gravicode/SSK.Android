package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Releasable;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.ResultTransform;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.TransformedResult;
import com.google.android.gms.common.internal.zzbq;
import java.lang.ref.WeakReference;

public final class zzdg<R extends Result> extends TransformedResult<R> implements ResultCallback<R> {
    /* access modifiers changed from: private */
    public final Object zzfou = new Object();
    /* access modifiers changed from: private */
    public final WeakReference<GoogleApiClient> zzfow;
    /* access modifiers changed from: private */
    public ResultTransform<? super R, ? extends Result> zzfux = null;
    /* access modifiers changed from: private */
    public zzdg<? extends Result> zzfuy = null;
    private volatile ResultCallbacks<? super R> zzfuz = null;
    private PendingResult<R> zzfva = null;
    private Status zzfvb = null;
    /* access modifiers changed from: private */
    public final zzdi zzfvc;
    private boolean zzfvd = false;

    public zzdg(WeakReference<GoogleApiClient> weakReference) {
        zzbq.checkNotNull(weakReference, "GoogleApiClient reference must not be null");
        this.zzfow = weakReference;
        GoogleApiClient googleApiClient = (GoogleApiClient) this.zzfow.get();
        this.zzfvc = new zzdi(this, googleApiClient != null ? googleApiClient.getLooper() : Looper.getMainLooper());
    }

    private final void zzajr() {
        if (this.zzfux != null || this.zzfuz != null) {
            GoogleApiClient googleApiClient = (GoogleApiClient) this.zzfow.get();
            if (!(this.zzfvd || this.zzfux == null || googleApiClient == null)) {
                googleApiClient.zza(this);
                this.zzfvd = true;
            }
            if (this.zzfvb != null) {
                zzx(this.zzfvb);
                return;
            }
            if (this.zzfva != null) {
                this.zzfva.setResultCallback(this);
            }
        }
    }

    private final boolean zzajt() {
        return (this.zzfuz == null || ((GoogleApiClient) this.zzfow.get()) == null) ? false : true;
    }

    /* access modifiers changed from: private */
    public static void zzd(Result result) {
        if (result instanceof Releasable) {
            try {
                ((Releasable) result).release();
            } catch (RuntimeException e) {
                String valueOf = String.valueOf(result);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 18);
                sb.append("Unable to release ");
                sb.append(valueOf);
                Log.w("TransformedResultImpl", sb.toString(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void zzd(Status status) {
        synchronized (this.zzfou) {
            this.zzfvb = status;
            zzx(this.zzfvb);
        }
    }

    private final void zzx(Status status) {
        synchronized (this.zzfou) {
            if (this.zzfux != null) {
                Status onFailure = this.zzfux.onFailure(status);
                zzbq.checkNotNull(onFailure, "onFailure must not return null");
                this.zzfuy.zzd(onFailure);
            } else if (zzajt()) {
                this.zzfuz.onFailure(status);
            }
        }
    }

    public final void andFinally(@NonNull ResultCallbacks<? super R> resultCallbacks) {
        synchronized (this.zzfou) {
            boolean z = false;
            zzbq.zza(this.zzfuz == null, (Object) "Cannot call andFinally() twice.");
            if (this.zzfux == null) {
                z = true;
            }
            zzbq.zza(z, (Object) "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzfuz = resultCallbacks;
            zzajr();
        }
    }

    public final void onResult(R r) {
        synchronized (this.zzfou) {
            if (!r.getStatus().isSuccess()) {
                zzd(r.getStatus());
                zzd((Result) r);
            } else if (this.zzfux != null) {
                zzcs.zzaip().submit(new zzdh(this, r));
            } else if (zzajt()) {
                this.zzfuz.onSuccess(r);
            }
        }
    }

    @NonNull
    public final <S extends Result> TransformedResult<S> then(@NonNull ResultTransform<? super R, ? extends S> resultTransform) {
        zzdg<? extends Result> zzdg;
        synchronized (this.zzfou) {
            boolean z = false;
            zzbq.zza(this.zzfux == null, (Object) "Cannot call then() twice.");
            if (this.zzfuz == null) {
                z = true;
            }
            zzbq.zza(z, (Object) "Cannot call then() and andFinally() on the same TransformedResult.");
            this.zzfux = resultTransform;
            zzdg = new zzdg<>(this.zzfow);
            this.zzfuy = zzdg;
            zzajr();
        }
        return zzdg;
    }

    public final void zza(PendingResult<?> pendingResult) {
        synchronized (this.zzfou) {
            this.zzfva = pendingResult;
            zzajr();
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzajs() {
        this.zzfuz = null;
    }
}
