package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.internal.zza;
import com.google.firebase.auth.internal.zze;
import com.google.firebase.auth.internal.zzh;

final class zzdxc extends zzdxx<AuthResult, zza> {
    public zzdxc() {
        super(2);
    }

    public final void dispatch() throws RemoteException {
        this.zzmfg.zzd(this.zzmff.zzbrg(), this.zzmfe);
    }

    public final void zzbrl() {
        zzh zzb = zzdwc.zza(this.zzmcx, this.zzmfp);
        ((zza) this.zzmfh).zza(this.zzmfo, zzb);
        zzbd(new zze(zzb));
    }
}
