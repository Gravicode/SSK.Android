package com.google.android.gms.auth.api.accounttransfer;

import android.os.RemoteException;
import com.google.android.gms.internal.zzavb;
import com.google.android.gms.internal.zzavd;
import com.google.android.gms.internal.zzavh;

final class zze extends zzb<byte[]> {
    private /* synthetic */ zzavh zzedq;

    zze(AccountTransferClient accountTransferClient, zzavh zzavh) {
        this.zzedq = zzavh;
        super(null);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzavd zzavd) throws RemoteException {
        zzavd.zza((zzavb) new zzf(this, this), this.zzedq);
    }
}
