package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;

public final class zzaut extends zzab<zzauu> {
    public zzaut(Context context, Looper looper, zzr zzr, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 74, zzr, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.accountactivationstate.internal.IAccountActivationStateService");
        return queryLocalInterface instanceof zzauu ? (zzauu) queryLocalInterface : new zzauv(iBinder);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public final String zzhi() {
        return "com.google.android.gms.auth.api.accountactivationstate.START";
    }

    /* access modifiers changed from: protected */
    @NonNull
    public final String zzhj() {
        return "com.google.android.gms.auth.api.accountactivationstate.internal.IAccountActivationStateService";
    }
}
