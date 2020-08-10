package com.google.firebase.storage;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;

final class zzl implements zzab<OnCompleteListener<TResult>, TResult> {
    private /* synthetic */ StorageTask zzojr;

    zzl(StorageTask storageTask) {
        this.zzojr = storageTask;
    }

    public final /* synthetic */ void zzi(@NonNull Object obj, @NonNull Object obj2) {
        OnCompleteListener onCompleteListener = (OnCompleteListener) obj;
        zzt.zzclx().zzc(this.zzojr);
        onCompleteListener.onComplete(this.zzojr);
    }
}
