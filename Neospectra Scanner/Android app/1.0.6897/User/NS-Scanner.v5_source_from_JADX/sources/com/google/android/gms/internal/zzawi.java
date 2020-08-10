package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.auth.api.zzd;
import com.google.android.gms.auth.api.zzf;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;

public final class zzawi extends zzab<zzawl> {
    private final Bundle zzeet;

    public zzawi(Context context, Looper looper, zzr zzr, zzf zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 16, zzr, connectionCallbacks, onConnectionFailedListener);
        if (zzf == null) {
            this.zzeet = new Bundle();
            return;
        }
        throw new NoSuchMethodError();
    }

    /* access modifiers changed from: protected */
    public final Bundle zzaap() {
        return this.zzeet;
    }

    public final boolean zzaay() {
        zzr zzalh = zzalh();
        return !TextUtils.isEmpty(zzalh.getAccountName()) && !zzalh.zzc(zzd.API).isEmpty();
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.internal.IAuthService");
        return queryLocalInterface instanceof zzawl ? (zzawl) queryLocalInterface : new zzawm(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.auth.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.auth.api.internal.IAuthService";
    }
}
