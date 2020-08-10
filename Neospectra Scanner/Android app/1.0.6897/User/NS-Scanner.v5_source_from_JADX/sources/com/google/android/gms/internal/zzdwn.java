package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.PhoneAuthCredential;

final class zzdwn<ResultT, CallbackT> extends zzdwa<zzdxk, ResultT> implements zzdxw<ResultT> {
    private TaskCompletionSource<ResultT> zzedx;
    private final String zzmes;
    private zzdxx<ResultT, CallbackT> zzmet;

    public zzdwn(zzdxx<ResultT, CallbackT> zzdxx, String str) {
        this.zzmet = zzdxx;
        this.zzmes = str;
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb, TaskCompletionSource taskCompletionSource) throws RemoteException {
        zzdxk zzdxk = (zzdxk) zzb;
        this.zzedx = taskCompletionSource;
        zzdxx<ResultT, CallbackT> zzdxx = this.zzmet;
        zzdxx.zzmfg = zzdxk.zzbrm();
        zzdxx.dispatch();
    }

    public final void zza(ResultT resultt, Status status) {
        zzbq.checkNotNull(this.zzedx, "doExecute must be called before onComplete");
        if (status == null) {
            this.zzedx.setResult(resultt);
        } else if (this.zzmet.zzmft != null) {
            this.zzedx.setException(zzdxm.zzb(status, (PhoneAuthCredential) this.zzmet.zzmft.clone()));
            this.zzmet.zzmft = null;
        } else {
            this.zzedx.setException(zzdxm.zzao(status));
        }
    }

    /* access modifiers changed from: 0000 */
    public final String zzbrk() {
        return this.zzmes;
    }
}
