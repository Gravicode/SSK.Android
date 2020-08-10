package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;

public final class zzbgb extends zzab<zzbge> {
    public zzbgb(Context context, Looper looper, zzr zzr, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 39, zzr, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
        return queryLocalInterface instanceof zzbge ? (zzbge) queryLocalInterface : new zzbgf(iBinder);
    }

    public final String zzhi() {
        return "com.google.android.gms.common.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.common.internal.service.ICommonService";
    }
}
