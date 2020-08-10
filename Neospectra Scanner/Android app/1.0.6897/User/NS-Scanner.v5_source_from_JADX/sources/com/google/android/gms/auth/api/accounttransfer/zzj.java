package com.google.android.gms.auth.api.accounttransfer;

import android.os.RemoteException;
import com.google.android.gms.internal.zzavb;
import com.google.android.gms.internal.zzavd;
import com.google.android.gms.internal.zzavf;

final class zzj extends zzc {
    private /* synthetic */ zzavf zzedv;

    zzj(AccountTransferClient accountTransferClient, zzavf zzavf) {
        this.zzedv = zzavf;
        super(null);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzavd zzavd) throws RemoteException {
        zzavd.zza((zzavb) this.zzedy, this.zzedv);
    }
}
