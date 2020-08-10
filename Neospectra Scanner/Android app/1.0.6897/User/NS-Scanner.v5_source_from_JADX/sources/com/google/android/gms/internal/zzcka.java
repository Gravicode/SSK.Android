package com.google.android.gms.internal;

final class zzcka implements Runnable {
    private /* synthetic */ zzcjn zzjhc;

    zzcka(zzcjn zzcjn) {
        this.zzjhc = zzcjn;
    }

    public final void run() {
        zzcjn zzcjn = this.zzjhc;
        zzcjn.zzve();
        zzcjn.zzxf();
        zzcjn.zzawy().zzazi().log("Resetting analytics data (FE)");
        zzcjn.zzawp().resetAnalyticsData();
    }
}
