package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;

final class zzdxj extends zzdxx<Void, OnVerificationStateChangedCallbacks> {
    private final zzdyu zzmey;

    public zzdxj(zzdyu zzdyu) {
        super(8);
        this.zzmey = (zzdyu) zzbq.checkNotNull(zzdyu);
    }

    public final void dispatch() throws RemoteException {
        this.zzmfg.zza(this.zzmey, (zzdxn) this.zzmfe);
    }

    public final void zzbrl() {
    }
}
