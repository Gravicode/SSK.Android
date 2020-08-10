package com.google.android.gms.auth.api;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.internal.zzavy;

final class zza extends com.google.android.gms.common.api.Api.zza<zzavy, AuthCredentialsOptions> {
    zza() {
    }

    public final /* synthetic */ zze zza(Context context, Looper looper, zzr zzr, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        zzavy zzavy = new zzavy(context, looper, zzr, (AuthCredentialsOptions) obj, connectionCallbacks, onConnectionFailedListener);
        return zzavy;
    }
}
