package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.support.p001v4.util.ArraySet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.zzbq;

public class zzah extends zzo {
    private zzbm zzfmi;
    private final ArraySet<zzh<?>> zzfqs = new ArraySet<>();

    private zzah(zzcf zzcf) {
        super(zzcf);
        this.zzfud.zza("ConnectionlessLifecycleHelper", (LifecycleCallback) this);
    }

    public static void zza(Activity activity, zzbm zzbm, zzh<?> zzh) {
        zzcf zzn = zzn(activity);
        zzah zzah = (zzah) zzn.zza("ConnectionlessLifecycleHelper", zzah.class);
        if (zzah == null) {
            zzah = new zzah(zzn);
        }
        zzah.zzfmi = zzbm;
        zzbq.checkNotNull(zzh, "ApiKey cannot be null");
        zzah.zzfqs.add(zzh);
        zzbm.zza(zzah);
    }

    private final void zzahy() {
        if (!this.zzfqs.isEmpty()) {
            this.zzfmi.zza(this);
        }
    }

    public final void onResume() {
        super.onResume();
        zzahy();
    }

    public final void onStart() {
        super.onStart();
        zzahy();
    }

    public final void onStop() {
        super.onStop();
        this.zzfmi.zzb(this);
    }

    /* access modifiers changed from: protected */
    public final void zza(ConnectionResult connectionResult, int i) {
        this.zzfmi.zza(connectionResult, i);
    }

    /* access modifiers changed from: protected */
    public final void zzagz() {
        this.zzfmi.zzagz();
    }

    /* access modifiers changed from: 0000 */
    public final ArraySet<zzh<?>> zzahx() {
        return this.zzfqs;
    }
}
