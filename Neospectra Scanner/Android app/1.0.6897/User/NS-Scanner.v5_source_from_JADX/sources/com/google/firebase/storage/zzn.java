package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.firebase.storage.StorageTask.ProvideError;

final class zzn implements zzab<OnPausedListener<? super TResult>, TResult> {
    zzn(StorageTask storageTask) {
    }

    public final /* synthetic */ void zzi(@NonNull Object obj, @NonNull Object obj2) {
        ((OnPausedListener) obj).onPaused((ProvideError) obj2);
    }
}
