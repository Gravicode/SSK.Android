package com.google.android.gms.common.internal;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResult.zza;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.TimeUnit;

final class zzbl implements zza {
    private /* synthetic */ PendingResult zzgbg;
    private /* synthetic */ TaskCompletionSource zzgbh;
    private /* synthetic */ zzbo zzgbi;
    private /* synthetic */ zzbp zzgbj;

    zzbl(PendingResult pendingResult, TaskCompletionSource taskCompletionSource, zzbo zzbo, zzbp zzbp) {
        this.zzgbg = pendingResult;
        this.zzgbh = taskCompletionSource;
        this.zzgbi = zzbo;
        this.zzgbj = zzbp;
    }

    public final void zzr(Status status) {
        if (status.isSuccess()) {
            this.zzgbh.setResult(this.zzgbi.zzb(this.zzgbg.await(0, TimeUnit.MILLISECONDS)));
            return;
        }
        this.zzgbh.setException(this.zzgbj.zzz(status));
    }
}
