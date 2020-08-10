package com.google.firebase.iid;

import android.os.Handler.Callback;
import android.os.Message;

final /* synthetic */ class zzl implements Callback {
    private final zzk zznzg;

    zzl(zzk zzk) {
        this.zznzg = zzk;
    }

    public final boolean handleMessage(Message message) {
        return this.zznzg.zzd(message);
    }
}
