package com.google.firebase.storage;

import com.google.android.gms.internal.zzexw;
import com.google.android.gms.internal.zzeyc;

final class zzad implements Runnable {
    private /* synthetic */ zzeyc zzolk;
    private /* synthetic */ UploadTask zzoll;

    zzad(UploadTask uploadTask, zzeyc zzeyc) {
        this.zzoll = uploadTask;
        this.zzolk = zzeyc;
    }

    public final void run() {
        this.zzolk.zze(zzexw.zzh(this.zzoll.zzoht.getStorage().getApp()), this.zzoll.zzoht.getStorage().getApp().getApplicationContext());
    }
}
