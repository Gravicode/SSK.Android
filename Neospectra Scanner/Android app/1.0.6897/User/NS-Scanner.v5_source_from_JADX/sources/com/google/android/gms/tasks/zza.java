package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

final class zza<TResult, TContinuationResult> implements zzk<TResult> {
    private final Executor zzkev;
    /* access modifiers changed from: private */
    public final Continuation<TResult, TContinuationResult> zzkty;
    /* access modifiers changed from: private */
    public final zzn<TContinuationResult> zzktz;

    public zza(@NonNull Executor executor, @NonNull Continuation<TResult, TContinuationResult> continuation, @NonNull zzn<TContinuationResult> zzn) {
        this.zzkev = executor;
        this.zzkty = continuation;
        this.zzktz = zzn;
    }

    public final void cancel() {
        throw new UnsupportedOperationException();
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzkev.execute(new zzb(this, task));
    }
}
