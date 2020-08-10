package com.google.android.gms.tasks;

final class zzh implements Runnable {
    private /* synthetic */ Task zzkua;
    private /* synthetic */ zzg zzkug;

    zzh(zzg zzg, Task task) {
        this.zzkug = zzg;
        this.zzkua = task;
    }

    public final void run() {
        synchronized (this.zzkug.mLock) {
            if (this.zzkug.zzkuf != null) {
                this.zzkug.zzkuf.onFailure(this.zzkua.getException());
            }
        }
    }
}
