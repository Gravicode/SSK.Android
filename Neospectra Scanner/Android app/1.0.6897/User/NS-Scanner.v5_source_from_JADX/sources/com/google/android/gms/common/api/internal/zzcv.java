package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzbt;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.internal.zzcxa;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.internal.zzcxe;
import com.google.android.gms.internal.zzcxi;
import com.google.android.gms.internal.zzcxq;
import java.util.Set;

public final class zzcv extends zzcxi implements ConnectionCallbacks, OnConnectionFailedListener {
    private static zza<? extends zzcxd, zzcxe> zzfut = zzcxa.zzebg;
    private final Context mContext;
    private final Handler mHandler;
    private Set<Scope> zzehs;
    private final zza<? extends zzcxd, zzcxe> zzfls;
    private zzr zzfpx;
    private zzcxd zzfrd;
    /* access modifiers changed from: private */
    public zzcy zzfuu;

    @WorkerThread
    public zzcv(Context context, Handler handler, @NonNull zzr zzr) {
        this(context, handler, zzr, zzfut);
    }

    @WorkerThread
    public zzcv(Context context, Handler handler, @NonNull zzr zzr, zza<? extends zzcxd, zzcxe> zza) {
        this.mContext = context;
        this.mHandler = handler;
        this.zzfpx = (zzr) zzbq.checkNotNull(zzr, "ClientSettings must not be null");
        this.zzehs = zzr.zzakv();
        this.zzfls = zza;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzc(zzcxq zzcxq) {
        ConnectionResult zzahf = zzcxq.zzahf();
        if (zzahf.isSuccess()) {
            zzbt zzbdi = zzcxq.zzbdi();
            zzahf = zzbdi.zzahf();
            if (!zzahf.isSuccess()) {
                String valueOf = String.valueOf(zzahf);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 48);
                sb.append("Sign-in succeeded with resolve account failure: ");
                sb.append(valueOf);
                Log.wtf("SignInCoordinator", sb.toString(), new Exception());
            } else {
                this.zzfuu.zzb(zzbdi.zzalp(), this.zzehs);
                this.zzfrd.disconnect();
            }
        }
        this.zzfuu.zzh(zzahf);
        this.zzfrd.disconnect();
    }

    @WorkerThread
    public final void onConnected(@Nullable Bundle bundle) {
        this.zzfrd.zza(this);
    }

    @WorkerThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzfuu.zzh(connectionResult);
    }

    @WorkerThread
    public final void onConnectionSuspended(int i) {
        this.zzfrd.disconnect();
    }

    @WorkerThread
    public final void zza(zzcy zzcy) {
        if (this.zzfrd != null) {
            this.zzfrd.disconnect();
        }
        this.zzfpx.zzc(Integer.valueOf(System.identityHashCode(this)));
        this.zzfrd = (zzcxd) this.zzfls.zza(this.mContext, this.mHandler.getLooper(), this.zzfpx, this.zzfpx.zzalb(), this, this);
        this.zzfuu = zzcy;
        if (this.zzehs == null || this.zzehs.isEmpty()) {
            this.mHandler.post(new zzcw(this));
        } else {
            this.zzfrd.connect();
        }
    }

    public final zzcxd zzaje() {
        return this.zzfrd;
    }

    public final void zzajq() {
        if (this.zzfrd != null) {
            this.zzfrd.disconnect();
        }
    }

    @BinderThread
    public final void zzb(zzcxq zzcxq) {
        this.mHandler.post(new zzcx(this, zzcxq));
    }
}
