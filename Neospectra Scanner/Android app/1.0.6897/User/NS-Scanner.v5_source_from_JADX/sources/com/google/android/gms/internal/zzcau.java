package com.google.android.gms.internal;

import android.os.RemoteException;

public final class zzcau extends zzcaq<Long> {
    public zzcau(int i, String str, Long l) {
        super(0, str, l);
    }

    /* access modifiers changed from: private */
    /* renamed from: zzd */
    public final Long zza(zzcay zzcay) {
        try {
            return Long.valueOf(zzcay.getLongFlagValue(getKey(), ((Long) zziv()).longValue(), getSource()));
        } catch (RemoteException e) {
            return (Long) zziv();
        }
    }
}
