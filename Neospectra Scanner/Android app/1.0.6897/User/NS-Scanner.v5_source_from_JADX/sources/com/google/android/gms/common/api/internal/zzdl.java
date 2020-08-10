package com.google.android.gms.common.api.internal;

import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import com.google.android.gms.common.api.zze;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;

final class zzdl implements DeathRecipient, zzdm {
    private final WeakReference<BasePendingResult<?>> zzfvl;
    private final WeakReference<zze> zzfvm;
    private final WeakReference<IBinder> zzfvn;

    private zzdl(BasePendingResult<?> basePendingResult, zze zze, IBinder iBinder) {
        this.zzfvm = new WeakReference<>(zze);
        this.zzfvl = new WeakReference<>(basePendingResult);
        this.zzfvn = new WeakReference<>(iBinder);
    }

    /* synthetic */ zzdl(BasePendingResult basePendingResult, zze zze, IBinder iBinder, zzdk zzdk) {
        this(basePendingResult, null, iBinder);
    }

    private final void zzajv() {
        BasePendingResult basePendingResult = (BasePendingResult) this.zzfvl.get();
        zze zze = (zze) this.zzfvm.get();
        if (!(zze == null || basePendingResult == null)) {
            zze.remove(basePendingResult.zzagv().intValue());
        }
        IBinder iBinder = (IBinder) this.zzfvn.get();
        if (iBinder != null) {
            try {
                iBinder.unlinkToDeath(this, 0);
            } catch (NoSuchElementException e) {
            }
        }
    }

    public final void binderDied() {
        zzajv();
    }

    public final void zzc(BasePendingResult<?> basePendingResult) {
        zzajv();
    }
}
