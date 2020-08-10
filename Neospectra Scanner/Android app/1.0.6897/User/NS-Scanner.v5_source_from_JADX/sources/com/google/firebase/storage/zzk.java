package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.StorageTask.ProvideError;

final class zzk implements zzab<OnFailureListener, TResult> {
    private /* synthetic */ StorageTask zzojr;

    zzk(StorageTask storageTask) {
        this.zzojr = storageTask;
    }

    public final /* synthetic */ void zzi(@NonNull Object obj, @NonNull Object obj2) {
        OnFailureListener onFailureListener = (OnFailureListener) obj;
        ProvideError provideError = (ProvideError) obj2;
        zzt.zzclx().zzc(this.zzojr);
        onFailureListener.onFailure(provideError.getError());
    }
}
