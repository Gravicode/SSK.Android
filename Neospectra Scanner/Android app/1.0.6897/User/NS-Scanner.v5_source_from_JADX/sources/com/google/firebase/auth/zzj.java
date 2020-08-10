package com.google.firebase.auth;

import com.google.firebase.auth.FirebaseAuth.IdTokenListener;
import com.google.firebase.internal.zzc;

final class zzj implements Runnable {
    private /* synthetic */ FirebaseAuth zzmdi;
    private /* synthetic */ zzc zzmdk;

    zzj(FirebaseAuth firebaseAuth, zzc zzc) {
        this.zzmdi = firebaseAuth;
        this.zzmdk = zzc;
    }

    public final void run() {
        this.zzmdi.zzmcx.zza(this.zzmdk);
        for (IdTokenListener onIdTokenChanged : this.zzmdi.zzmav) {
            onIdTokenChanged.onIdTokenChanged(this.zzmdi);
        }
    }
}
