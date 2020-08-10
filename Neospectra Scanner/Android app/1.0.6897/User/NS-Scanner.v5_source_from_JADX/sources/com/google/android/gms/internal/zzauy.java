package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.auth.api.accounttransfer.zzo;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzr;

public final class zzauy extends zzab<zzavd> {
    private final Bundle zzeet;

    public zzauy(Context context, Looper looper, zzr zzr, zzo zzo, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 128, zzr, connectionCallbacks, onConnectionFailedListener);
        if (zzo == null) {
            this.zzeet = new Bundle();
            return;
        }
        throw new NoSuchMethodError();
    }

    /* access modifiers changed from: protected */
    public final Bundle zzaap() {
        return this.zzeet;
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.auth.api.accounttransfer.internal.IAccountTransferService");
        return queryLocalInterface instanceof zzavd ? (zzavd) queryLocalInterface : new zzave(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.auth.api.accounttransfer.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.auth.api.accounttransfer.internal.IAccountTransferService";
    }
}
