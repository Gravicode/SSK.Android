package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;

public final class zzd extends zzab<zzu> {
    private final GoogleSignInOptions zzehy;

    public zzd(Context context, Looper looper, zzr zzr, GoogleSignInOptions googleSignInOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 91, zzr, connectionCallbacks, onConnectionFailedListener);
        if (googleSignInOptions == null) {
            googleSignInOptions = new Builder().build();
        }
        if (!zzr.zzakw().isEmpty()) {
            Builder builder = new Builder(googleSignInOptions);
            for (Scope requestScopes : zzr.zzakw()) {
                builder.requestScopes(requestScopes, new Scope[0]);
            }
            googleSignInOptions = builder.build();
        }
        this.zzehy = googleSignInOptions;
    }

    public final Intent getSignInIntent() {
        return zze.zza(getContext(), this.zzehy);
    }

    public final boolean zzabj() {
        return true;
    }

    public final GoogleSignInOptions zzabk() {
        return this.zzehy;
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.signin.internal.ISignInService");
        return queryLocalInterface instanceof zzu ? (zzu) queryLocalInterface : new zzv(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.auth.api.signin.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.auth.api.signin.internal.ISignInService";
    }
}
