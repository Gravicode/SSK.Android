package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;

public final class zzl implements ServiceConnection {
    private /* synthetic */ zzd zzfza;
    private final int zzfzd;

    public zzl(zzd zzd, int i) {
        this.zzfza = zzd;
        this.zzfzd = i;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzay zzay;
        if (iBinder == null) {
            this.zzfza.zzcf(16);
            return;
        }
        synchronized (this.zzfza.zzfyk) {
            zzd zzd = this.zzfza;
            if (iBinder == null) {
                zzay = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                zzay = (queryLocalInterface == null || !(queryLocalInterface instanceof zzay)) ? new zzaz(iBinder) : (zzay) queryLocalInterface;
            }
            zzd.zzfyl = zzay;
        }
        this.zzfza.zza(0, (Bundle) null, this.zzfzd);
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.zzfza.zzfyk) {
            this.zzfza.zzfyl = null;
        }
        this.zzfza.mHandler.sendMessage(this.zzfza.mHandler.obtainMessage(6, this.zzfzd, 1));
    }
}
