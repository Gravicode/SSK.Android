package com.google.firebase.storage;

final class zzs implements Runnable {
    private /* synthetic */ StorageTask zzojr;

    zzs(StorageTask storageTask) {
        this.zzojr = storageTask;
    }

    public final void run() {
        try {
            this.zzojr.run();
        } finally {
            this.zzojr.zzclw();
        }
    }
}
