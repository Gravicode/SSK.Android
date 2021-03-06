package com.google.android.gms.auth;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.internal.zzey;
import java.io.IOException;

final class zzf implements zzj<Void> {
    private /* synthetic */ Bundle val$extras;
    private /* synthetic */ String zzecf;

    zzf(String str, Bundle bundle) {
        this.zzecf = str;
        this.val$extras = bundle;
    }

    public final /* synthetic */ Object zzac(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
        Bundle bundle = (Bundle) zzd.zzp(zzey.zza(iBinder).zza(this.zzecf, this.val$extras));
        String string = bundle.getString("Error");
        if (bundle.getBoolean("booleanResult")) {
            return null;
        }
        throw new GoogleAuthException(string);
    }
}
