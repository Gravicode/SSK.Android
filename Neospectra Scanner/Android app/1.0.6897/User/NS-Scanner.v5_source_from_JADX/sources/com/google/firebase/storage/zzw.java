package com.google.firebase.storage;

import java.io.InputStream;
import java.util.concurrent.Callable;

final class zzw implements Callable<InputStream> {
    private /* synthetic */ StreamDownloadTask zzokk;

    zzw(StreamDownloadTask streamDownloadTask) {
        this.zzokk = streamDownloadTask;
    }

    public final /* synthetic */ Object call() throws Exception {
        return this.zzokk.zzcly();
    }
}
