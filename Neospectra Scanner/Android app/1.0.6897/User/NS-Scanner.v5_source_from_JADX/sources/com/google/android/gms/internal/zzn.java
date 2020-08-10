package com.google.android.gms.internal;

import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import java.util.concurrent.BlockingQueue;

public final class zzn extends Thread {
    private final BlockingQueue<zzr<?>> zzaa;
    private final zzm zzab;
    private final zzb zzj;
    private final zzz zzk;
    private volatile boolean zzl = false;

    public zzn(BlockingQueue<zzr<?>> blockingQueue, zzm zzm, zzb zzb, zzz zzz) {
        this.zzaa = blockingQueue;
        this.zzab = zzm;
        this.zzj = zzb;
        this.zzk = zzz;
    }

    public final void quit() {
        this.zzl = true;
        interrupt();
    }

    public final void run() {
        Process.setThreadPriority(10);
        while (true) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            try {
                zzr zzr = (zzr) this.zzaa.take();
                try {
                    zzr.zzb("network-queue-take");
                    TrafficStats.setThreadStatsTag(zzr.zzd());
                    zzp zzc = this.zzab.zzc(zzr);
                    zzr.zzb("network-http-complete");
                    if (!zzc.zzad || !zzr.zzk()) {
                        zzw zza = zzr.zza(zzc);
                        zzr.zzb("network-parse-complete");
                        if (zzr.zzg() && zza.zzbh != null) {
                            this.zzj.zza(zzr.getUrl(), zza.zzbh);
                            zzr.zzb("network-cache-written");
                        }
                        zzr.zzj();
                        this.zzk.zzb(zzr, zza);
                        zzr.zza(zza);
                    } else {
                        zzr.zzc("not-modified");
                        zzr.zzl();
                    }
                } catch (zzad e) {
                    e.zza(SystemClock.elapsedRealtime() - elapsedRealtime);
                    this.zzk.zza(zzr, e);
                    zzr.zzl();
                } catch (Exception e2) {
                    zzae.zza(e2, "Unhandled exception %s", e2.toString());
                    zzad zzad = new zzad((Throwable) e2);
                    zzad.zza(SystemClock.elapsedRealtime() - elapsedRealtime);
                    this.zzk.zza(zzr, zzad);
                    zzr.zzl();
                }
            } catch (InterruptedException e3) {
                if (this.zzl) {
                    return;
                }
            }
        }
    }
}
