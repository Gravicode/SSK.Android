package com.google.android.gms.internal;

import java.util.concurrent.Callable;

final class zzcio implements Callable<String> {
    private /* synthetic */ String zzimf;
    private /* synthetic */ zzcim zzjgh;

    zzcio(zzcim zzcim, String str) {
        this.zzjgh = zzcim;
        this.zzimf = str;
    }

    public final /* synthetic */ Object call() throws Exception {
        zzcgh zzjb = this.zzjgh.zzaws().zzjb(this.zzimf);
        if (zzjb != null) {
            return zzjb.getAppInstanceId();
        }
        this.zzjgh.zzawy().zzazf().log("App info was null when attempting to get app instance id");
        return null;
    }
}
