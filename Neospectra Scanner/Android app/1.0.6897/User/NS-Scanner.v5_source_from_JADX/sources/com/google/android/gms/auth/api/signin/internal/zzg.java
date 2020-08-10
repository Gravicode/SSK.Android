package com.google.android.gms.auth.api.signin.internal;

import android.os.RemoteException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Status;

final class zzg extends zza {
    private /* synthetic */ zzf zzeib;

    zzg(zzf zzf) {
        this.zzeib = zzf;
    }

    public final void zza(GoogleSignInAccount googleSignInAccount, Status status) throws RemoteException {
        if (googleSignInAccount != null) {
            zzo.zzbr(this.zzeib.val$context).zza(this.zzeib.zzeia, googleSignInAccount);
        }
        this.zzeib.setResult(new GoogleSignInResult(googleSignInAccount, status));
    }
}
