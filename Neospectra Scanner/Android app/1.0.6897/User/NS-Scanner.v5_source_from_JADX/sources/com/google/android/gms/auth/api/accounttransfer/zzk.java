package com.google.android.gms.auth.api.accounttransfer;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.internal.zzauw;

final class zzk extends zzauw {
    private /* synthetic */ zzc zzedz;

    zzk(zzc zzc) {
        this.zzedz = zzc;
    }

    public final void onFailure(Status status) {
        this.zzedz.zzd(status);
    }

    public final void zzaau() {
        this.zzedz.setResult(null);
    }
}
