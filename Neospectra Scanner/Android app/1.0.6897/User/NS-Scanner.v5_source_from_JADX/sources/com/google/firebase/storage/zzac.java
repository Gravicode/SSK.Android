package com.google.firebase.storage;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.internal.zzexr;
import com.google.android.gms.internal.zzeyc;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageMetadata.Builder;
import org.json.JSONException;

final class zzac implements Runnable {
    private final StorageReference zzoht;
    private final TaskCompletionSource<StorageMetadata> zzohu;
    private zzexr zzohv;
    private StorageMetadata zzoih = null;
    private final StorageMetadata zzolb;

    public zzac(@NonNull StorageReference storageReference, @NonNull TaskCompletionSource<StorageMetadata> taskCompletionSource, @NonNull StorageMetadata storageMetadata) {
        this.zzoht = storageReference;
        this.zzohu = taskCompletionSource;
        this.zzolb = storageMetadata;
        this.zzohv = new zzexr(this.zzoht.getStorage().getApp(), this.zzoht.getStorage().getMaxOperationRetryTimeMillis());
    }

    public final void run() {
        try {
            zzeyc zza = this.zzoht.zzclq().zza(this.zzoht.zzclr(), this.zzolb.zzclo());
            this.zzohv.zza(zza, true);
            if (zza.zzcmm()) {
                try {
                    this.zzoih = new Builder(zza.zzcmp(), this.zzoht).build();
                } catch (RemoteException | JSONException e) {
                    String str = "UpdateMetadataTask";
                    String str2 = "Unable to parse a valid JSON object from resulting metadata:";
                    String valueOf = String.valueOf(zza.zzcmk());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e);
                    this.zzohu.setException(StorageException.fromException(e));
                    return;
                }
            }
            if (this.zzohu != null) {
                zza.zza(this.zzohu, this.zzoih);
            }
        } catch (RemoteException | JSONException e2) {
            Log.e("UpdateMetadataTask", "Unable to create the request from metadata.", e2);
            this.zzohu.setException(StorageException.fromException(e2));
        }
    }
}
