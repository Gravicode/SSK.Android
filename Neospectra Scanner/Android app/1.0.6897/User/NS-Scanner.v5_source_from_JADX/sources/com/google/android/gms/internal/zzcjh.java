package com.google.android.gms.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzcjh implements Callable<List<zzclp>> {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;

    zzcjh(zzcir zzcir, zzcgi zzcgi) {
        this.zzjgo = zzcir;
        this.zzjgn = zzcgi;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzjgo.zziwf.zzbal();
        return this.zzjgo.zziwf.zzaws().zzja(this.zzjgn.packageName);
    }
}
