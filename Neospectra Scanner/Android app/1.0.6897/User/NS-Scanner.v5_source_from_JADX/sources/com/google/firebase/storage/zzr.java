package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;

final class zzr implements OnFailureListener {
    private /* synthetic */ zzp zzojt;

    zzr(zzp zzp) {
        this.zzojt = zzp;
    }

    public final void onFailure(@NonNull Exception exc) {
        this.zzojt.zzgbh.setException(exc);
    }
}
