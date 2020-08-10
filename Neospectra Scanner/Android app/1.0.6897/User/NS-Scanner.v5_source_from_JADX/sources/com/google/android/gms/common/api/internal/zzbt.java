package com.google.android.gms.common.api.internal;

final class zzbt implements Runnable {
    private /* synthetic */ zzbs zzftt;

    zzbt(zzbs zzbs) {
        this.zzftt = zzbs;
    }

    public final void run() {
        this.zzftt.zzftr.zzfpv.disconnect();
    }
}
