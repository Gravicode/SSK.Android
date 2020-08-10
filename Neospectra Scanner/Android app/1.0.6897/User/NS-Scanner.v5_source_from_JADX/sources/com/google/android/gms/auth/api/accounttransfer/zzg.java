package com.google.android.gms.auth.api.accounttransfer;

import android.os.RemoteException;
import com.google.android.gms.internal.zzauz;
import com.google.android.gms.internal.zzavb;
import com.google.android.gms.internal.zzavd;

final class zzg extends zzb<DeviceMetaData> {
    private /* synthetic */ zzauz zzeds;

    zzg(AccountTransferClient accountTransferClient, zzauz zzauz) {
        this.zzeds = zzauz;
        super(null);
    }

    /* access modifiers changed from: protected */
    public final void zza(zzavd zzavd) throws RemoteException {
        zzavd.zza((zzavb) new zzh(this, this), this.zzeds);
    }
}
