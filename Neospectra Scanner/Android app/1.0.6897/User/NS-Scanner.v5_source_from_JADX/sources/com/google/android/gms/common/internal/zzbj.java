package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zzbj {
    private static final zzbp zzgbf = new zzbk();

    public static <R extends Result, T extends Response<R>> Task<T> zza(PendingResult<R> pendingResult, T t) {
        return zza(pendingResult, (zzbo<R, T>) new zzbm<R,T>(t));
    }

    public static <R extends Result, T> Task<T> zza(PendingResult<R> pendingResult, zzbo<R, T> zzbo) {
        zzbp zzbp = zzgbf;
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        pendingResult.zza(new zzbl(pendingResult, taskCompletionSource, zzbo, zzbp));
        return taskCompletionSource.getTask();
    }

    public static <R extends Result> Task<Void> zzb(PendingResult<R> pendingResult) {
        return zza(pendingResult, (zzbo<R, T>) new zzbn<R,T>());
    }
}
