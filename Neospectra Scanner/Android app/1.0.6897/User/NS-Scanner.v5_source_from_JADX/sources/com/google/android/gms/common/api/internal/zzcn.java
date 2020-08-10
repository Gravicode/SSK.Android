package com.google.android.gms.common.api.internal;

import android.app.Activity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzb;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.concurrent.CancellationException;

public class zzcn extends zzo {
    private TaskCompletionSource<Void> zzedx = new TaskCompletionSource<>();

    private zzcn(zzcf zzcf) {
        super(zzcf);
        this.zzfud.zza("GmsAvailabilityHelper", (LifecycleCallback) this);
    }

    public static zzcn zzp(Activity activity) {
        zzcf zzn = zzn(activity);
        zzcn zzcn = (zzcn) zzn.zza("GmsAvailabilityHelper", zzcn.class);
        if (zzcn == null) {
            return new zzcn(zzn);
        }
        if (zzcn.zzedx.getTask().isComplete()) {
            zzcn.zzedx = new TaskCompletionSource<>();
        }
        return zzcn;
    }

    public final Task<Void> getTask() {
        return this.zzedx.getTask();
    }

    public final void onDestroy() {
        super.onDestroy();
        this.zzedx.trySetException(new CancellationException("Host activity was destroyed before Google Play services could be made available."));
    }

    /* access modifiers changed from: protected */
    public final void zza(ConnectionResult connectionResult, int i) {
        this.zzedx.setException(zzb.zzy(new Status(connectionResult.getErrorCode(), connectionResult.getErrorMessage(), connectionResult.getResolution())));
    }

    /* access modifiers changed from: protected */
    public final void zzagz() {
        int isGooglePlayServicesAvailable = this.zzfmy.isGooglePlayServicesAvailable(this.zzfud.zzajn());
        if (isGooglePlayServicesAvailable == 0) {
            this.zzedx.setResult(null);
            return;
        }
        if (!this.zzedx.getTask().isComplete()) {
            zzb(new ConnectionResult(isGooglePlayServicesAvailable, null), 0);
        }
    }
}
