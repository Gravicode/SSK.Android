package com.google.android.gms.internal;

import android.os.RemoteException;

public final class zzcas extends zzcaq<Boolean> {
    public zzcas(int i, String str, Boolean bool) {
        super(0, str, bool);
    }

    /* access modifiers changed from: private */
    /* renamed from: zzb */
    public final Boolean zza(zzcay zzcay) {
        try {
            return Boolean.valueOf(zzcay.getBooleanFlagValue(getKey(), ((Boolean) zziv()).booleanValue(), getSource()));
        } catch (RemoteException e) {
            return (Boolean) zziv();
        }
    }
}
