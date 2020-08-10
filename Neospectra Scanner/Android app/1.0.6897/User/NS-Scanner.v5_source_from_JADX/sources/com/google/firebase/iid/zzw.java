package com.google.firebase.iid;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

final class zzw extends Handler {
    private /* synthetic */ zzv zznzo;

    zzw(zzv zzv, Looper looper) {
        this.zznzo = zzv;
        super(looper);
    }

    public final void handleMessage(Message message) {
        this.zznzo.zze(message);
    }
}
