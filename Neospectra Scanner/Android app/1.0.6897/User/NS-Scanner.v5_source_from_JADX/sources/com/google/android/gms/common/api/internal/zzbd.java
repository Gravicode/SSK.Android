package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;

final class zzbd implements OnConnectionFailedListener {
    private /* synthetic */ zzda zzfsl;

    zzbd(zzba zzba, zzda zzda) {
        this.zzfsl = zzda;
    }

    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzfsl.setResult(new Status(8));
    }
}
