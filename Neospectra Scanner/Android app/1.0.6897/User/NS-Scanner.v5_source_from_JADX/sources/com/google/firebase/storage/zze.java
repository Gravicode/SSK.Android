package com.google.firebase.storage;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zze implements OnSuccessListener<StorageMetadata> {
    private /* synthetic */ TaskCompletionSource zzojg;

    zze(StorageReference storageReference, TaskCompletionSource taskCompletionSource) {
        this.zzojg = taskCompletionSource;
    }

    public final /* synthetic */ void onSuccess(Object obj) {
        this.zzojg.setResult(((StorageMetadata) obj).getDownloadUrl());
    }
}
