package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.auth.api.proxy.ProxyRequest;

public final class zzawm extends zzeu implements zzawl {
    zzawm(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.auth.api.internal.IAuthService");
    }

    public final void zza(zzawj zzawj, ProxyRequest proxyRequest) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawj);
        zzew.zza(zzbe, (Parcelable) proxyRequest);
        zzb(1, zzbe);
    }
}
