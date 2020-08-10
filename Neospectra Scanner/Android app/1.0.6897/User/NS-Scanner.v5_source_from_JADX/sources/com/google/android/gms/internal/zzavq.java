package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzavq extends zzavw<CredentialRequestResult> {
    private /* synthetic */ CredentialRequest zzegd;

    zzavq(zzavp zzavp, GoogleApiClient googleApiClient, CredentialRequest credentialRequest) {
        this.zzegd = credentialRequest;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final void zza(Context context, zzawd zzawd) throws RemoteException {
        zzawd.zza((zzawb) new zzavr(this), this.zzegd);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return zzavo.zzf(status);
    }
}
