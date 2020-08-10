package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

final class zzbk extends Handler {
    private /* synthetic */ zzbi zzfsw;

    zzbk(zzbi zzbi, Looper looper) {
        this.zzfsw = zzbi;
        super(looper);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                ((zzbj) message.obj).zzc(this.zzfsw);
                return;
            case 2:
                throw ((RuntimeException) message.obj);
            default:
                int i = message.what;
                StringBuilder sb = new StringBuilder(31);
                sb.append("Unknown message id: ");
                sb.append(i);
                Log.w("GACStateManager", sb.toString());
                return;
        }
    }
}
