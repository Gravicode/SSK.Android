package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyRequest;
import com.google.android.gms.common.api.GoogleApiClient;

final class zzawy extends zzaww {
    private /* synthetic */ ProxyRequest zzego;

    zzawy(zzawx zzawx, GoogleApiClient googleApiClient, ProxyRequest proxyRequest) {
        this.zzego = proxyRequest;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final void zza(Context context, zzawl zzawl) throws RemoteException {
        zzawl.zza(new zzawz(this), this.zzego);
    }
}
