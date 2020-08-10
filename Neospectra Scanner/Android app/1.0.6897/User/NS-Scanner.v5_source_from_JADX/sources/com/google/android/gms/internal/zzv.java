package com.google.android.gms.internal;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzv {
    private final zzm zzab;
    private final AtomicInteger zzbb;
    private final Set<zzr<?>> zzbc;
    private final PriorityBlockingQueue<zzr<?>> zzbd;
    private final PriorityBlockingQueue<zzr<?>> zzbe;
    private final zzn[] zzbf;
    private final List<Object> zzbg;
    private final zzb zzj;
    private final zzz zzk;
    private zzd zzq;

    public zzv(zzb zzb, zzm zzm) {
        this(zzb, zzm, 4);
    }

    private zzv(zzb zzb, zzm zzm, int i) {
        this(zzb, zzm, 4, new zzi(new Handler(Looper.getMainLooper())));
    }

    private zzv(zzb zzb, zzm zzm, int i, zzz zzz) {
        this.zzbb = new AtomicInteger();
        this.zzbc = new HashSet();
        this.zzbd = new PriorityBlockingQueue<>();
        this.zzbe = new PriorityBlockingQueue<>();
        this.zzbg = new ArrayList();
        this.zzj = zzb;
        this.zzab = zzm;
        this.zzbf = new zzn[4];
        this.zzk = zzz;
    }

    public final void start() {
        zzn[] zznArr;
        if (this.zzq != null) {
            this.zzq.quit();
        }
        for (zzn zzn : this.zzbf) {
            if (zzn != null) {
                zzn.quit();
            }
        }
        this.zzq = new zzd(this.zzbd, this.zzbe, this.zzj, this.zzk);
        this.zzq.start();
        for (int i = 0; i < this.zzbf.length; i++) {
            zzn zzn2 = new zzn(this.zzbe, this.zzab, this.zzj, this.zzk);
            this.zzbf[i] = zzn2;
            zzn2.start();
        }
    }

    public final <T> zzr<T> zze(zzr<T> zzr) {
        zzr.zza(this);
        synchronized (this.zzbc) {
            this.zzbc.add(zzr);
        }
        zzr.zza(this.zzbb.incrementAndGet());
        zzr.zzb("add-to-queue");
        (!zzr.zzg() ? this.zzbe : this.zzbd).add(zzr);
        return zzr;
    }

    /* access modifiers changed from: 0000 */
    public final <T> void zzf(zzr<T> zzr) {
        synchronized (this.zzbc) {
            this.zzbc.remove(zzr);
        }
        synchronized (this.zzbg) {
            Iterator it = this.zzbg.iterator();
            while (it.hasNext()) {
                it.next();
            }
        }
    }
}
