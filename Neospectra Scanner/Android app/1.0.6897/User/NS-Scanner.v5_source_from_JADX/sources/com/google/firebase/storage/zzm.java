package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.firebase.storage.StorageTask.ProvideError;

final class zzm implements zzab<OnProgressListener<? super TResult>, TResult> {
    zzm(StorageTask storageTask) {
    }

    public final /* synthetic */ void zzi(@NonNull Object obj, @NonNull Object obj2) {
        ((OnProgressListener) obj).onProgress((ProvideError) obj2);
    }
}
