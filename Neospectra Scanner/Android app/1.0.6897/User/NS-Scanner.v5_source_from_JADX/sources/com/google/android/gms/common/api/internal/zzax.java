package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

final class zzax implements ConnectionCallbacks, OnConnectionFailedListener {
    private /* synthetic */ zzao zzfrl;

    private zzax(zzao zzao) {
        this.zzfrl = zzao;
    }

    /* synthetic */ zzax(zzao zzao, zzap zzap) {
        this(zzao);
    }

    public final void onConnected(Bundle bundle) {
        this.zzfrl.zzfrd.zza(new zzav(this.zzfrl));
    }

    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzfrl.zzfps.lock();
        try {
            if (this.zzfrl.zzd(connectionResult)) {
                this.zzfrl.zzaif();
                this.zzfrl.zzaid();
            } else {
                this.zzfrl.zze(connectionResult);
            }
        } finally {
            this.zzfrl.zzfps.unlock();
        }
    }

    public final void onConnectionSuspended(int i) {
    }
}
