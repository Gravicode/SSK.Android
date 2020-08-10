package com.google.android.gms.auth.api.accounttransfer;

import android.os.RemoteException;
import com.google.android.gms.internal.zzavb;
import com.google.android.gms.internal.zzavd;
import com.google.android.gms.internal.zzavj;

final class zzd extends zzc {
    private /* synthetic */ zzavj zzedp;

    zzd(AccountTransferClient accountTransferClient, zzavj zzavj) {
        this.zzedp = zzavj;
        super(null);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzavd zzavd) throws RemoteException {
        zzavd.zza((zzavb) this.zzedy, this.zzedp);
    }
}
