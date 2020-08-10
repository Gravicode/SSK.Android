package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbq;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

final class zzcik<V> extends FutureTask<V> implements Comparable<zzcik> {
    private final String zzjep;
    private /* synthetic */ zzcih zzjeq;
    private final long zzjer = zzcih.zzjeo.getAndIncrement();
    final boolean zzjes;

    zzcik(zzcih zzcih, Runnable runnable, boolean z, String str) {
        this.zzjeq = zzcih;
        super(runnable, null);
        zzbq.checkNotNull(str);
        this.zzjep = str;
        this.zzjes = false;
        if (this.zzjer == Long.MAX_VALUE) {
            zzcih.zzawy().zzazd().log("Tasks index overflow");
        }
    }

    zzcik(zzcih zzcih, Callable<V> callable, boolean z, String str) {
        this.zzjeq = zzcih;
        super(callable);
        zzbq.checkNotNull(str);
        this.zzjep = str;
        this.zzjes = z;
        if (this.zzjer == Long.MAX_VALUE) {
            zzcih.zzawy().zzazd().log("Tasks index overflow");
        }
    }

    public final /* synthetic */ int compareTo(@NonNull Object obj) {
        zzcik zzcik = (zzcik) obj;
        if (this.zzjes != zzcik.zzjes) {
            return this.zzjes ? -1 : 1;
        }
        if (this.zzjer < zzcik.zzjer) {
            return -1;
        }
        if (this.zzjer > zzcik.zzjer) {
            return 1;
        }
        this.zzjeq.zzawy().zzaze().zzj("Two tasks share the same index. index", Long.valueOf(this.zzjer));
        return 0;
    }

    /* access modifiers changed from: protected */
    public final void setException(Throwable th) {
        this.zzjeq.zzawy().zzazd().zzj(this.zzjep, th);
        if (th instanceof zzcii) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), th);
        }
        super.setException(th);
    }
}
