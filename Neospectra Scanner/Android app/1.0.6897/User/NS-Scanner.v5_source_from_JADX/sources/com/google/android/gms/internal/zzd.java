package com.google.android.gms.internal;

import android.os.Process;
import java.util.concurrent.BlockingQueue;

public final class zzd extends Thread {
    private static final boolean DEBUG = zzae.DEBUG;
    private final BlockingQueue<zzr<?>> zzh;
    /* access modifiers changed from: private */
    public final BlockingQueue<zzr<?>> zzi;
    private final zzb zzj;
    /* access modifiers changed from: private */
    public final zzz zzk;
    private volatile boolean zzl = false;
    private final zzf zzm;

    public zzd(BlockingQueue<zzr<?>> blockingQueue, BlockingQueue<zzr<?>> blockingQueue2, zzb zzb, zzz zzz) {
        this.zzh = blockingQueue;
        this.zzi = blockingQueue2;
        this.zzj = zzb;
        this.zzk = zzz;
        this.zzm = new zzf(this);
    }

    public final void quit() {
        this.zzl = true;
        interrupt();
    }

    public final void run() {
        zzz zzz;
        BlockingQueue<zzr<?>> blockingQueue;
        if (DEBUG) {
            zzae.zza("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        this.zzj.initialize();
        while (true) {
            try {
                zzr zzr = (zzr) this.zzh.take();
                zzr.zzb("cache-queue-take");
                zzc zza = this.zzj.zza(zzr.getUrl());
                if (zza == null) {
                    zzr.zzb("cache-miss");
                    if (!this.zzm.zzb(zzr)) {
                        blockingQueue = this.zzi;
                    }
                } else if (zza.zza()) {
                    zzr.zzb("cache-hit-expired");
                    zzr.zza(zza);
                    if (!this.zzm.zzb(zzr)) {
                        blockingQueue = this.zzi;
                    }
                } else {
                    zzr.zzb("cache-hit");
                    zzw zza2 = zzr.zza(new zzp(zza.data, zza.zzf));
                    zzr.zzb("cache-hit-parsed");
                    if (!(zza.zze < System.currentTimeMillis())) {
                        zzz = this.zzk;
                    } else {
                        zzr.zzb("cache-hit-refresh-needed");
                        zzr.zza(zza);
                        zza2.zzbj = true;
                        if (!this.zzm.zzb(zzr)) {
                            this.zzk.zza(zzr, zza2, new zze(this, zzr));
                        } else {
                            zzz = this.zzk;
                        }
                    }
                    zzz.zzb(zzr, zza2);
                }
                blockingQueue.put(zzr);
            } catch (InterruptedException e) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
