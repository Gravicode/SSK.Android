package com.google.android.gms.internal;

import java.util.List;
import java.util.concurrent.Callable;

final class zzciz implements Callable<List<zzcgl>> {
    private /* synthetic */ zzcgi zzjgn;
    private /* synthetic */ zzcir zzjgo;
    private /* synthetic */ String zzjgq;
    private /* synthetic */ String zzjgr;

    zzciz(zzcir zzcir, zzcgi zzcgi, String str, String str2) {
        this.zzjgo = zzcir;
        this.zzjgn = zzcgi;
        this.zzjgq = str;
        this.zzjgr = str2;
    }

    public final /* synthetic */ Object call() throws Exception {
        this.zzjgo.zziwf.zzbal();
        return this.zzjgo.zziwf.zzaws().zzh(this.zzjgn.packageName, this.zzjgq, this.zzjgr);
    }
}
