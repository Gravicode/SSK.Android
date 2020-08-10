package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageTask.ProvideError;

final class zzj implements zzab<OnSuccessListener<? super TResult>, TResult> {
    private /* synthetic */ StorageTask zzojr;

    zzj(StorageTask storageTask) {
        this.zzojr = storageTask;
    }

    public final /* synthetic */ void zzi(@NonNull Object obj, @NonNull Object obj2) {
        OnSuccessListener onSuccessListener = (OnSuccessListener) obj;
        ProvideError provideError = (ProvideError) obj2;
        zzt.zzclx().zzc(this.zzojr);
        onSuccessListener.onSuccess(provideError);
    }
}
