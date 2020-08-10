package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzg;
import com.google.android.gms.common.stats.zza;

public final class zzcku implements ServiceConnection, zzf, zzg {
    final /* synthetic */ zzckg zzjij;
    /* access modifiers changed from: private */
    public volatile boolean zzjiq;
    private volatile zzchl zzjir;

    protected zzcku(zzckg zzckg) {
        this.zzjij = zzckg;
    }

    @MainThread
    public final void onConnected(@Nullable Bundle bundle) {
        zzbq.zzge("MeasurementServiceConnection.onConnected");
        synchronized (this) {
            try {
                zzche zzche = (zzche) this.zzjir.zzakn();
                this.zzjir = null;
                this.zzjij.zzawx().zzg(new zzckx(this, zzche));
            } catch (DeadObjectException | IllegalStateException e) {
                this.zzjir = null;
                this.zzjiq = false;
            }
        }
    }

    @MainThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        zzbq.zzge("MeasurementServiceConnection.onConnectionFailed");
        zzchm zzazx = this.zzjij.zziwf.zzazx();
        if (zzazx != null) {
            zzazx.zzazf().zzj("Service connection failed", connectionResult);
        }
        synchronized (this) {
            this.zzjiq = false;
            this.zzjir = null;
        }
        this.zzjij.zzawx().zzg(new zzckz(this));
    }

    @MainThread
    public final void onConnectionSuspended(int i) {
        zzbq.zzge("MeasurementServiceConnection.onConnectionSuspended");
        this.zzjij.zzawy().zzazi().log("Service connection suspended");
        this.zzjij.zzawx().zzg(new zzcky(this));
    }

    @MainThread
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        zzbq.zzge("MeasurementServiceConnection.onServiceConnected");
        synchronized (this) {
            if (iBinder == null) {
                this.zzjiq = false;
                this.zzjij.zzawy().zzazd().log("Service connected with null binder");
                return;
            }
            zzche zzche = null;
            try {
                String interfaceDescriptor = iBinder.getInterfaceDescriptor();
                if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                    if (iBinder != null) {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.internal.IMeasurementService");
                        zzche = queryLocalInterface instanceof zzche ? (zzche) queryLocalInterface : new zzchg(iBinder);
                    }
                    this.zzjij.zzawy().zzazj().log("Bound to IMeasurementService interface");
                } else {
                    this.zzjij.zzawy().zzazd().zzj("Got binder with a wrong descriptor", interfaceDescriptor);
                }
            } catch (RemoteException e) {
                this.zzjij.zzawy().zzazd().log("Service connect failed to get IMeasurementService");
            }
            if (zzche == null) {
                this.zzjiq = false;
                try {
                    zza.zzamc();
                    this.zzjij.getContext().unbindService(this.zzjij.zzjic);
                } catch (IllegalArgumentException e2) {
                }
            } else {
                this.zzjij.zzawx().zzg(new zzckv(this, zzche));
            }
        }
    }

    @MainThread
    public final void onServiceDisconnected(ComponentName componentName) {
        zzbq.zzge("MeasurementServiceConnection.onServiceDisconnected");
        this.zzjij.zzawy().zzazi().log("Service disconnected");
        this.zzjij.zzawx().zzg(new zzckw(this, componentName));
    }

    @WorkerThread
    public final void zzbau() {
        this.zzjij.zzve();
        Context context = this.zzjij.getContext();
        synchronized (this) {
            if (this.zzjiq) {
                this.zzjij.zzawy().zzazj().log("Connection attempt already in progress");
            } else if (this.zzjir != null) {
                this.zzjij.zzawy().zzazj().log("Already awaiting connection attempt");
            } else {
                this.zzjir = new zzchl(context, Looper.getMainLooper(), this, this);
                this.zzjij.zzawy().zzazj().log("Connecting to remote service");
                this.zzjiq = true;
                this.zzjir.zzakj();
            }
        }
    }

    @WorkerThread
    public final void zzn(Intent intent) {
        this.zzjij.zzve();
        Context context = this.zzjij.getContext();
        zza zzamc = zza.zzamc();
        synchronized (this) {
            if (this.zzjiq) {
                this.zzjij.zzawy().zzazj().log("Connection attempt already in progress");
                return;
            }
            this.zzjij.zzawy().zzazj().log("Using local app measurement service");
            this.zzjiq = true;
            zzamc.zza(context, intent, this.zzjij.zzjic, 129);
        }
    }
}
