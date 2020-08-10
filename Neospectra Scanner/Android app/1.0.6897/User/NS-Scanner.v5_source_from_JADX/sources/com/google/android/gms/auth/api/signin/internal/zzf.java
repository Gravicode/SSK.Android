package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzf extends zzl<GoogleSignInResult> {
    final /* synthetic */ Context val$context;
    final /* synthetic */ GoogleSignInOptions zzeia;

    zzf(GoogleApiClient googleApiClient, Context context, GoogleSignInOptions googleSignInOptions) {
        this.val$context = context;
        this.zzeia = googleSignInOptions;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        ((zzu) ((zzd) zzb).zzakn()).zza(new zzg(this), this.zzeia);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return new GoogleSignInResult(null, status);
    }
}
