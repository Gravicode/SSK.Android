package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.internal.zza;
import com.google.firebase.auth.internal.zze;
import com.google.firebase.auth.internal.zzh;

final class zzdwm extends zzdxx<AuthResult, zza> implements zzdxz {
    @NonNull
    private final PhoneAuthCredential zzmer;

    public zzdwm(@NonNull PhoneAuthCredential phoneAuthCredential) {
        super(2);
        this.zzmer = (PhoneAuthCredential) zzbq.checkNotNull(phoneAuthCredential, "credential cannot be null");
    }

    public final void dispatch() throws RemoteException {
        this.zzmfg.zza(this.zzmff.zzbrg(), this.zzmer, (zzdxn) this.zzmfe);
    }

    public final void zza(@NonNull Status status, @NonNull PhoneAuthCredential phoneAuthCredential) throws RemoteException {
        boolean z = this.zzmfd == 2;
        int i = this.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfu = false;
        this.zzmft = phoneAuthCredential;
        if (this.zzmfi != null) {
            this.zzmfi.onError(status);
        }
        zzap(status);
    }

    public final void zzbrl() {
        zzh zzb = zzdwc.zza(this.zzmcx, this.zzmfp);
        ((zza) this.zzmfh).zza(this.zzmfo, zzb);
        zzbd(new zze(zzb));
    }
}
