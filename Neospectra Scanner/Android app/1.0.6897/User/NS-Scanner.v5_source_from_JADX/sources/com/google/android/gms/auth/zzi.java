package com.google.android.gms.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.internal.zzaxb;
import com.google.android.gms.internal.zzey;
import java.io.IOException;

final class zzi implements zzj<Boolean> {
    private /* synthetic */ String zzeci;

    zzi(String str) {
        this.zzeci = str;
    }

    public final /* synthetic */ Object zzac(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
        Bundle bundle = (Bundle) zzd.zzp(zzey.zza(iBinder).zzp(this.zzeci));
        String string = bundle.getString("Error");
        Intent intent = (Intent) bundle.getParcelable("userRecoveryIntent");
        zzaxb zzfb = zzaxb.zzfb(string);
        if (zzaxb.SUCCESS.equals(zzfb)) {
            return Boolean.valueOf(true);
        }
        if (zzaxb.zza(zzfb)) {
            String valueOf = String.valueOf(zzfb);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 31);
            sb.append("isUserRecoverableError status: ");
            sb.append(valueOf);
            zzd.zzecc.zzf("GoogleAuthUtil", sb.toString());
            throw new UserRecoverableAuthException(string, intent);
        }
        throw new GoogleAuthException(string);
    }
}
