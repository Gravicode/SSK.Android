package com.google.firebase.auth.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseNetworkException;

final class zzm implements OnFailureListener {
    private /* synthetic */ zzl zzmhw;

    zzm(zzl zzl) {
        this.zzmhw = zzl;
    }

    public final void onFailure(@NonNull Exception exc) {
        if (exc instanceof FirebaseNetworkException) {
            zzk.zzecc.zza("Failure to refresh token; scheduling refresh after failure", new Object[0]);
            this.zzmhw.zzmhv.zzbse();
        }
    }
}
