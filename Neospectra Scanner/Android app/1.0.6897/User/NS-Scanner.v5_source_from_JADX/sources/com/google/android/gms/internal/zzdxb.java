package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.internal.zza;
import com.google.firebase.auth.internal.zze;
import com.google.firebase.auth.internal.zzh;

final class zzdxb extends zzdxx<AuthResult, zza> {
    private PhoneAuthCredential zzmer;

    public zzdxb(PhoneAuthCredential phoneAuthCredential) {
        super(2);
        this.zzmer = (PhoneAuthCredential) zzbq.checkNotNull(phoneAuthCredential);
    }

    public final void dispatch() throws RemoteException {
        this.zzmfg.zza(this.zzmer, (zzdxn) this.zzmfe);
    }

    public final void zzbrl() {
        zzh zzb = zzdwc.zza(this.zzmcx, this.zzmfp);
        ((zza) this.zzmfh).zza(this.zzmfo, zzb);
        zzbd(new zze(zzb));
    }
}
