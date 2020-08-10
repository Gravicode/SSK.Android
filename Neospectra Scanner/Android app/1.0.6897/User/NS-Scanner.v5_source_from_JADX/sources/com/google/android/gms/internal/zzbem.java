package com.google.android.gms.internal;

import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzm;

final class zzbem extends zzm<Status, zzbeo> {
    private final zzbeh zzfkf;

    zzbem(zzbeh zzbeh, GoogleApiClient googleApiClient) {
        super(zzbdy.API, googleApiClient);
        this.zzfkf = zzbeh;
    }

    public final /* bridge */ /* synthetic */ void setResult(Object obj) {
        super.setResult((Status) obj);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void zza(zzb zzb) throws RemoteException {
        zzbeo zzbeo = (zzbeo) zzb;
        zzben zzben = new zzben(this);
        try {
            zzbeh zzbeh = this.zzfkf;
            if (zzbeh.zzfjk != null && zzbeh.zzfjr.zzpqs.length == 0) {
                zzbeh.zzfjr.zzpqs = zzbeh.zzfjk.zzafv();
            }
            if (zzbeh.zzfke != null && zzbeh.zzfjr.zzpqz.length == 0) {
                zzbeh.zzfjr.zzpqz = zzbeh.zzfke.zzafv();
            }
            zzbeh.zzfjy = zzfjs.zzc(zzbeh.zzfjr);
            ((zzbes) zzbeo.zzakn()).zza(zzben, this.zzfkf);
        } catch (RuntimeException e) {
            Log.e("ClearcutLoggerApiImpl", "derived ClearcutLogger.MessageProducer ", e);
            zzu(new Status(10, "MessageProducer"));
        }
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ Result zzb(Status status) {
        return status;
    }
}
