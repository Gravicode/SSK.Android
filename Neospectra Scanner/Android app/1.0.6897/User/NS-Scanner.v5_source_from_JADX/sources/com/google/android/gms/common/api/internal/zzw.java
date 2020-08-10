package com.google.android.gms.common.api.internal;

final class zzw implements Runnable {
    private /* synthetic */ zzv zzfpu;

    zzw(zzv zzv) {
        this.zzfpu = zzv;
    }

    public final void run() {
        this.zzfpu.zzfps.lock();
        try {
            this.zzfpu.zzahl();
        } finally {
            this.zzfpu.zzfps.unlock();
        }
    }
}
