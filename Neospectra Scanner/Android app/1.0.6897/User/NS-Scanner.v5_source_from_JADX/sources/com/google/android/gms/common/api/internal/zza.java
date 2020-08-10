package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.zzq;

public abstract class zza {
    private int zzeie;

    public zza(int i) {
        this.zzeie = i;
    }

    /* access modifiers changed from: private */
    public static Status zza(RemoteException remoteException) {
        StringBuilder sb = new StringBuilder();
        if (zzq.zzamh() && (remoteException instanceof TransactionTooLargeException)) {
            sb.append("TransactionTooLargeException: ");
        }
        sb.append(remoteException.getLocalizedMessage());
        return new Status(8, sb.toString());
    }

    public abstract void zza(@NonNull zzae zzae, boolean z);

    public abstract void zza(zzbo<?> zzbo) throws DeadObjectException;

    public abstract void zzs(@NonNull Status status);
}
