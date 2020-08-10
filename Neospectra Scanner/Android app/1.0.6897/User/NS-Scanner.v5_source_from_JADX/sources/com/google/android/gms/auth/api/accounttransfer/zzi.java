package com.google.android.gms.auth.api.accounttransfer;

import android.os.RemoteException;
import com.google.android.gms.internal.zzavb;
import com.google.android.gms.internal.zzavd;
import com.google.android.gms.internal.zzavl;

final class zzi extends zzc {
    private /* synthetic */ zzavl zzedu;

    zzi(AccountTransferClient accountTransferClient, zzavl zzavl) {
        this.zzedu = zzavl;
        super(null);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzavd zzavd) throws RemoteException {
        zzavd.zza((zzavb) this.zzedy, this.zzedu);
    }
}
