package com.google.android.gms.internal;

import android.os.RemoteException;

public final class zzcat extends zzcaq<Integer> {
    public zzcat(int i, String str, Integer num) {
        super(0, str, num);
    }

    /* access modifiers changed from: private */
    /* renamed from: zzc */
    public final Integer zza(zzcay zzcay) {
        try {
            return Integer.valueOf(zzcay.getIntFlagValue(getKey(), ((Integer) zziv()).intValue(), getSource()));
        } catch (RemoteException e) {
            return (Integer) zziv();
        }
    }
}
