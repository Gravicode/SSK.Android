package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;

public final class zzo extends zze {
    private /* synthetic */ zzd zzfza;

    @BinderThread
    public zzo(zzd zzd, @Nullable int i, Bundle bundle) {
        this.zzfza = zzd;
        super(zzd, i, null);
    }

    /* access modifiers changed from: protected */
    public final boolean zzakr() {
        this.zzfza.zzfym.zzf(ConnectionResult.zzfkr);
        return true;
    }

    /* access modifiers changed from: protected */
    public final void zzj(ConnectionResult connectionResult) {
        this.zzfza.zzfym.zzf(connectionResult);
        this.zzfza.onConnectionFailed(connectionResult);
    }
}
