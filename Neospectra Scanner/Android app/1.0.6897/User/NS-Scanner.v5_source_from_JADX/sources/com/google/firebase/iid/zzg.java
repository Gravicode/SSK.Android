package com.google.firebase.iid;

import android.util.Log;

final class zzg implements Runnable {
    private /* synthetic */ zzd zznym;
    private /* synthetic */ zzf zznyn;

    zzg(zzf zzf, zzd zzd) {
        this.zznyn = zzf;
        this.zznym = zzd;
    }

    public final void run() {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "bg processing of the intent starting now");
        }
        this.zznyn.zznyl.handleIntent(this.zznym.intent);
        this.zznym.finish();
    }
}
