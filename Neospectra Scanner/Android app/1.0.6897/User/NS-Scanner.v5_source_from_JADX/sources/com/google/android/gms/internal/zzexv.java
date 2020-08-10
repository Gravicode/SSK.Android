package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.storage.zzu;
import java.util.concurrent.Executor;

public final class zzexv {
    private static boolean zzcek = false;
    private final Handler mHandler;
    private final Executor zzkev;

    public zzexv(@Nullable Executor executor) {
        this.zzkev = executor;
        this.mHandler = this.zzkev == null ? new Handler(Looper.getMainLooper()) : null;
    }

    public final void zzw(@NonNull Runnable runnable) {
        zzbq.checkNotNull(runnable);
        if (this.mHandler != null) {
            this.mHandler.post(runnable);
        } else if (this.zzkev != null) {
            this.zzkev.execute(runnable);
        } else {
            zzu.zzv(runnable);
        }
    }
}
