package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzeyb;
import com.google.android.gms.internal.zzeyc;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageMetadata.Builder;
import org.json.JSONException;

final class zzc implements Runnable {
    private StorageReference zzoht;
    private TaskCompletionSource<StorageMetadata> zzohu;
    private zzexr zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxOperationRetryTimeMillis());
    private StorageMetadata zzoih;

    public zzc(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<StorageMetadata> taskCompletionSource) {
        zzbq.checkNotNull(storageReference);
        zzbq.checkNotNull(taskCompletionSource);
        this.zzoht = storageReference;
        this.zzohu = taskCompletionSource;
    }

    public final void run() {
        try {
            zzeyc zzw = zzeyb.zzi(this.zzoht.getStorage().getApp()).zzw(this.zzoht.zzclr());
            this.zzohv.zza(zzw, true);
            if (zzw.zzcmm()) {
                try {
                    this.zzoih = new Builder(zzw.zzcmp(), this.zzoht).build();
                } catch (RemoteException | JSONException e) {
                    String str = "GetMetadataTask";
                    String str2 = "Unable to parse resulting metadata. ";
                    String valueOf = String.valueOf(zzw.zzcmk());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zzohu.setException(StorageException.fromException(e));
                    return;
                }
            }
            if (this.zzohu != null) {
                zzw.zza(this.zzohu, this.zzoih);
            }
        } catch (RemoteException e2) {
            Log.e("GetMetadataTask", "Unable to create firebase storage network request.", e2);
            this.zzohu.setException(StorageException.fromException(e2));
        }
    }
}
