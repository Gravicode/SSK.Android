package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

final class zzc<TResult, TContinuationResult> implements OnFailureListener, OnSuccessListener<TContinuationResult>, zzk<TResult> {
    private final Executor zzkev;
    /* access modifiers changed from: private */
    public final Continuation<TResult, Task<TContinuationResult>> zzkty;
    /* access modifiers changed from: private */
    public final zzn<TContinuationResult> zzktz;

    public zzc(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation, @NonNull zzn<TContinuationResult> zzn) {
        this.zzkev = executor;
        this.zzkty = continuation;
        this.zzktz = zzn;
    }

    public final void cancel() {
        throw new UnsupportedOperationException();
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzkev.execute(new zzd(this, task));
    }

    public final void onFailure(@NonNull Exception exc) {
        this.zzktz.setException(exc);
    }

    public final void onSuccess(TContinuationResult tcontinuationresult) {
        this.zzktz.setResult(tcontinuationresult);
    }
}
