package com.google.android.gms.tasks;

import android.support.annotation.NonNull;

public class TaskCompletionSource<TResult> {
    private final zzn<TResult> zzkul = new zzn<>();

    @NonNull
    public Task<TResult> getTask() {
        return this.zzkul;
    }

    public void setException(@NonNull Exception exc) {
        this.zzkul.setException(exc);
    }

    public void setResult(TResult tresult) {
        this.zzkul.setResult(tresult);
    }

    public boolean trySetException(@NonNull Exception exc) {
        return this.zzkul.trySetException(exc);
    }

    public boolean trySetResult(TResult tresult) {
        return this.zzkul.trySetResult(tresult);
    }
}
