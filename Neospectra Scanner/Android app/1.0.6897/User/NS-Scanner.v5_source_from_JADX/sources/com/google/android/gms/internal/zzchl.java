package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzg;

public final class zzchl extends zzd<zzche> {
    public zzchl(Context context, Looper looper, zzf zzf, zzg zzg) {
        super(context, looper, 93, zzf, zzg, null);
    }

    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
        return queryLocalInterface instanceof zzche ? (zzche) queryLocalInterface : new zzchg(iBinder);
    }

    /* access modifiers changed from: protected */
    @NonNull
    public final String zzhi() {
        return "com.google.android.gms.measurement.START";
    }

    /* access modifiers changed from: protected */
    @NonNull
    public final String zzhj() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }
}
