package com.google.android.gms.internal;

import android.os.RemoteException;

public final class zzcav extends zzcaq<String> {
    public zzcav(int i, String str, String str2) {
        super(0, str, str2);
    }

    /* access modifiers changed from: private */
    /* renamed from: zze */
    public final String zza(zzcay zzcay) {
        try {
            return zzcay.getStringFlagValue(getKey(), (String) zziv(), getSource());
        } catch (RemoteException e) {
            return (String) zziv();
        }
    }
}
