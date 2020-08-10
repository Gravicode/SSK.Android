package com.google.android.gms.internal;

import android.app.job.JobParameters;

final /* synthetic */ class zzclc implements Runnable {
    private final zzcla zzjiv;
    private final zzchm zzjiz;
    private final JobParameters zzjja;

    zzclc(zzcla zzcla, zzchm zzchm, JobParameters jobParameters) {
        this.zzjiv = zzcla;
        this.zzjiz = zzchm;
        this.zzjja = jobParameters;
    }

    public final void run() {
        this.zzjiv.zza(this.zzjiz, this.zzjja);
    }
}
