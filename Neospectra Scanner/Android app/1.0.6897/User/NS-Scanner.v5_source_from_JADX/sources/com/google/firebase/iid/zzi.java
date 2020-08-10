package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public final class zzi {
    private static zzi zznyx;
    /* access modifiers changed from: private */
    public final Context zzair;
    /* access modifiers changed from: private */
    public final ScheduledExecutorService zznyy;
    private zzk zznyz = new zzk(this);
    private int zznza = 1;

    @VisibleForTesting
    private zzi(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zznyy = scheduledExecutorService;
        this.zzair = context.getApplicationContext();
    }

    private final synchronized <T> Task<T> zza(zzr<T> zzr) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(zzr);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 9);
            sb.append("Queueing ");
            sb.append(valueOf);
            Log.d("MessengerIpcClient", sb.toString());
        }
        if (!this.zznyz.zzb(zzr)) {
            this.zznyz = new zzk(this);
            this.zznyz.zzb(zzr);
        }
        return zzr.zzgrq.getTask();
    }

    private final synchronized int zzcja() {
        int i;
        i = this.zznza;
        this.zznza = i + 1;
        return i;
    }

    public static synchronized zzi zzev(Context context) {
        zzi zzi;
        synchronized (zzi.class) {
            if (zznyx == null) {
                zznyx = new zzi(context, Executors.newSingleThreadScheduledExecutor());
            }
            zzi = zznyx;
        }
        return zzi;
    }

    public final Task<Void> zzh(int i, Bundle bundle) {
        return zza((zzr<T>) new zzq<T>(zzcja(), 2, bundle));
    }

    public final Task<Bundle> zzi(int i, Bundle bundle) {
        return zza((zzr<T>) new zzt<T>(zzcja(), 1, bundle));
    }
}
