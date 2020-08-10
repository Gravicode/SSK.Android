package com.google.android.gms.common.api.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

final class zzbf extends Handler {
    private /* synthetic */ zzba zzfsj;

    zzbf(zzba zzba, Looper looper) {
        this.zzfsj = zzba;
        super(looper);
    }

    public final void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.zzfsj.zzaij();
                return;
            case 2:
                this.zzfsj.resume();
                return;
            default:
                int i = message.what;
                StringBuilder sb = new StringBuilder(31);
                sb.append("Unknown message id: ");
                sb.append(i);
                Log.w("GoogleApiClientImpl", sb.toString());
                return;
        }
    }
}
