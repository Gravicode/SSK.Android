package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.auth.account.zzc;
import com.google.android.gms.auth.account.zzd;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public final class zzauq extends zzab<zzc> {
    public zzauq(Context context, Looper looper, zzr zzr, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, ShapeTypes.CLOUD_CALLOUT, zzr, connectionCallbacks, onConnectionFailedListener);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        return zzd.zzad(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.auth.account.workaccount.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.auth.account.IWorkAccountService";
    }
}
