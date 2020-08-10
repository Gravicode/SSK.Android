package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class zzawr extends zzab<zzawn> {
    public zzawr(Context context, Looper looper, zzr zzr, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, ShapeTypes.LEFT_RIGHT_RIBBON, zzr, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.phone.internal.ISmsRetrieverApiService");
        return queryLocalInterface instanceof zzawn ? (zzawn) queryLocalInterface : new zzawo(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.auth.api.phone.service.SmsRetrieverApiService.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.auth.api.phone.internal.ISmsRetrieverApiService";
    }
}
