package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzan;
import com.google.android.gms.common.internal.zzj;
import java.util.Set;

final class zzbu implements zzcy, zzj {
    private Set<Scope> zzehs = null;
    /* access modifiers changed from: private */
    public final zzh<?> zzfmf;
    /* access modifiers changed from: private */
    public final zze zzfpv;
    private zzan zzfrh = null;
    final /* synthetic */ zzbm zzfti;
    /* access modifiers changed from: private */
    public boolean zzftu = false;

    public zzbu(zzbm zzbm, zze zze, zzh<?> zzh) {
        this.zzfti = zzbm;
        this.zzfpv = zze;
        this.zzfmf = zzh;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzajg() {
        if (this.zzftu && this.zzfrh != null) {
            this.zzfpv.zza(this.zzfrh, this.zzehs);
        }
    }

    @WorkerThread
    public final void zzb(zzan zzan, Set<Scope> set) {
        if (zzan == null || set == null) {
            Log.wtf("GoogleApiManager", "Received null response from onSignInSuccess", new Exception());
            zzh(new ConnectionResult(4));
            return;
        }
        this.zzfrh = zzan;
        this.zzehs = set;
        zzajg();
    }

    public final void zzf(@NonNull ConnectionResult connectionResult) {
        this.zzfti.mHandler.post(new zzbv(this, connectionResult));
    }

    @WorkerThread
    public final void zzh(ConnectionResult connectionResult) {
        ((zzbo) this.zzfti.zzfpy.get(this.zzfmf)).zzh(connectionResult);
    }
}
