package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.google.android.gms.auth.api.credentials.CredentialRequest;

public final class zzawe extends zzeu implements zzawd {
    zzawe(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.auth.api.credentials.internal.ICredentialsService");
    }

    public final void zza(zzawb zzawb) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawb);
        zzb(4, zzbe);
    }

    public final void zza(zzawb zzawb, CredentialRequest credentialRequest) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawb);
        zzew.zza(zzbe, (Parcelable) credentialRequest);
        zzb(1, zzbe);
    }

    public final void zza(zzawb zzawb, zzavz zzavz) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawb);
        zzew.zza(zzbe, (Parcelable) zzavz);
        zzb(3, zzbe);
    }

    public final void zza(zzawb zzawb, zzawf zzawf) throws RemoteException {
        Parcel zzbe = zzbe();
        zzew.zza(zzbe, (IInterface) zzawb);
        zzew.zza(zzbe, (Parcelable) zzawf);
        zzb(2, zzbe);
    }
}
