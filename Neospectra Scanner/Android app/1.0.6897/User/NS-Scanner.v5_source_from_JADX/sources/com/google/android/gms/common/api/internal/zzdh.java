package com.google.android.gms.common.api.internal;

import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;

final class zzdh implements Runnable {
    private /* synthetic */ Result zzfve;
    private /* synthetic */ zzdg zzfvf;

    zzdh(zzdg zzdg, Result result) {
        this.zzfvf = zzdg;
        this.zzfve = result;
    }

    @WorkerThread
    public final void run() {
        try {
            BasePendingResult.zzfot.set(Boolean.valueOf(true));
            this.zzfvf.zzfvc.sendMessage(this.zzfvf.zzfvc.obtainMessage(0, this.zzfvf.zzfux.onSuccess(this.zzfve)));
            BasePendingResult.zzfot.set(Boolean.valueOf(false));
            zzdg.zzd(this.zzfve);
            GoogleApiClient googleApiClient = (GoogleApiClient) this.zzfvf.zzfow.get();
            if (googleApiClient != null) {
                googleApiClient.zzb(this.zzfvf);
            }
        } catch (RuntimeException e) {
            this.zzfvf.zzfvc.sendMessage(this.zzfvf.zzfvc.obtainMessage(1, e));
            BasePendingResult.zzfot.set(Boolean.valueOf(false));
            zzdg.zzd(this.zzfve);
            GoogleApiClient googleApiClient2 = (GoogleApiClient) this.zzfvf.zzfow.get();
            if (googleApiClient2 != null) {
                googleApiClient2.zzb(this.zzfvf);
            }
        } catch (Throwable th) {
            BasePendingResult.zzfot.set(Boolean.valueOf(false));
            zzdg.zzd(this.zzfve);
            GoogleApiClient googleApiClient3 = (GoogleApiClient) this.zzfvf.zzfow.get();
            if (googleApiClient3 != null) {
                googleApiClient3.zzb(this.zzfvf);
            }
            throw th;
        }
    }
}
