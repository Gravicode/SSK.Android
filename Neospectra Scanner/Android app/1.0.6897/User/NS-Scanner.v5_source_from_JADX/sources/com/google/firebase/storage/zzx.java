package com.google.firebase.storage;

import android.app.Activity;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzexo;
import com.google.android.gms.internal.zzexv;
import com.google.firebase.storage.StorageTask.ProvideError;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

final class zzx<TListenerType, TResult extends ProvideError> {
    private final Queue<TListenerType> zzoks = new ConcurrentLinkedQueue();
    private final HashMap<TListenerType, zzexv> zzokt = new HashMap<>();
    private StorageTask<TResult> zzoku;
    private int zzokv;
    /* access modifiers changed from: private */
    public zzab<TListenerType, TResult> zzokw;

    public zzx(@NonNull StorageTask<TResult> storageTask, int i, @NonNull zzab<TListenerType, TResult> zzab) {
        this.zzoku = storageTask;
        this.zzokv = i;
        this.zzokw = zzab;
    }

    public final void zza(@Nullable Activity activity, @Nullable Executor executor, @NonNull TListenerType tlistenertype) {
        boolean z;
        zzexv zzexv;
        zzbq.checkNotNull(tlistenertype);
        synchronized (this.zzoku.mSyncObject) {
            z = (this.zzoku.zzclt() & this.zzokv) != 0;
            this.zzoks.add(tlistenertype);
            zzexv = new zzexv(executor);
            this.zzokt.put(tlistenertype, zzexv);
            if (activity != null) {
                if (VERSION.SDK_INT >= 17) {
                    zzbq.checkArgument(!activity.isDestroyed(), "Activity is already destroyed!");
                }
                zzexo.zzcme().zza(activity, tlistenertype, new zzy(this, tlistenertype));
            }
        }
        if (z) {
            zzexv.zzw(new zzz(this, tlistenertype, this.zzoku.zzclu()));
        }
    }

    public final void zzcl(@NonNull TListenerType tlistenertype) {
        zzbq.checkNotNull(tlistenertype);
        synchronized (this.zzoku.mSyncObject) {
            this.zzokt.remove(tlistenertype);
            this.zzoks.remove(tlistenertype);
            zzexo.zzcme().zzcm(tlistenertype);
        }
    }

    public final void zzcmb() {
        if ((this.zzoku.zzclt() & this.zzokv) != 0) {
            ProvideError zzclu = this.zzoku.zzclu();
            for (Object next : this.zzoks) {
                zzexv zzexv = (zzexv) this.zzokt.get(next);
                if (zzexv != null) {
                    zzexv.zzw(new zzaa(this, next, zzclu));
                }
            }
        }
    }
}
