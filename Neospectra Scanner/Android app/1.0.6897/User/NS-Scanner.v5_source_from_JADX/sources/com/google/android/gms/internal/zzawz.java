package com.google.android.gms.internal;

import com.google.android.gms.auth.api.proxy.ProxyResponse;

final class zzawz extends zzawh {
    private /* synthetic */ zzawy zzegp;

    zzawz(zzawy zzawy) {
        this.zzegp = zzawy;
    }

    public final void zza(ProxyResponse proxyResponse) {
        this.zzegp.setResult(new zzaxa(proxyResponse));
    }
}
