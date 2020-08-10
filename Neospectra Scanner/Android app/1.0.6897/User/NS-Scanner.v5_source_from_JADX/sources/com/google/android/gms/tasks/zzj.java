package com.google.android.gms.tasks;

final class zzj implements Runnable {
    private /* synthetic */ Task zzkua;
    private /* synthetic */ zzi zzkui;

    zzj(zzi zzi, Task task) {
        this.zzkui = zzi;
        this.zzkua = task;
    }

    public final void run() {
        synchronized (this.zzkui.mLock) {
            if (this.zzkui.zzkuh != null) {
                this.zzkui.zzkuh.onSuccess(this.zzkua.getResult());
            }
        }
    }
}
