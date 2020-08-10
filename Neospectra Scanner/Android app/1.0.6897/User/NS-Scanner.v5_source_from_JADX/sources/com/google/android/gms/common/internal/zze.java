package com.google.android.gms.common.internal;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import com.google.android.gms.common.ConnectionResult;

abstract class zze extends zzi<Boolean> {
    private int statusCode;
    private Bundle zzfyz;
    private /* synthetic */ zzd zzfza;

    @BinderThread
    protected zze(zzd zzd, int i, Bundle bundle) {
        this.zzfza = zzd;
        super(zzd, Boolean.valueOf(true));
        this.statusCode = i;
        this.zzfyz = bundle;
    }

    /* access modifiers changed from: protected */
    public abstract boolean zzakr();

    /* access modifiers changed from: protected */
    public abstract void zzj(ConnectionResult connectionResult);

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zzw(Object obj) {
        PendingIntent pendingIntent = null;
        if (((Boolean) obj) == null) {
            this.zzfza.zza(1, null);
            return;
        }
        int i = this.statusCode;
        if (i == 0) {
            if (!zzakr()) {
                this.zzfza.zza(1, null);
                zzj(new ConnectionResult(8, null));
            }
        } else if (i != 10) {
            this.zzfza.zza(1, null);
            if (this.zzfyz != null) {
                pendingIntent = (PendingIntent) this.zzfyz.getParcelable("pendingIntent");
            }
            zzj(new ConnectionResult(this.statusCode, pendingIntent));
        } else {
            this.zzfza.zza(1, null);
            throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
        }
    }
}
