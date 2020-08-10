package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzeyb;
import com.google.android.gms.internal.zzeyc;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzb implements Runnable {
    private StorageReference zzoht;
    private TaskCompletionSource<Void> zzohu;
    private zzexr zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxOperationRetryTimeMillis());

    public zzb(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<Void> taskCompletionSource) {
        zzbq.checkNotNull(storageReference);
        zzbq.checkNotNull(taskCompletionSource);
        this.zzoht = storageReference;
        this.zzohu = taskCompletionSource;
    }

    public final void run() {
        try {
            zzeyc zzv = zzeyb.zzi(this.zzoht.getStorage().getApp()).zzv(this.zzoht.zzclr());
            this.zzohv.zza(zzv, true);
            zzv.zza(this.zzohu, null);
        } catch (RemoteException e) {
            Log.e("DeleteStorageTask", "Unable to create Firebase Storage network request.", e);
            this.zzohu.setException(StorageException.fromException(e));
        }
    }
}
