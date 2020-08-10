package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import java.util.concurrent.atomic.AtomicReference;

public abstract class zzo extends LifecycleCallback implements OnCancelListener {
    protected volatile boolean mStarted;
    protected final GoogleApiAvailability zzfmy;
    protected final AtomicReference<zzp> zzfol;
    private final Handler zzfom;

    protected zzo(zzcf zzcf) {
        this(zzcf, GoogleApiAvailability.getInstance());
    }

    private zzo(zzcf zzcf, GoogleApiAvailability googleApiAvailability) {
        super(zzcf);
        this.zzfol = new AtomicReference<>(null);
        this.zzfom = new Handler(Looper.getMainLooper());
        this.zzfmy = googleApiAvailability;
    }

    private static int zza(@Nullable zzp zzp) {
        if (zzp == null) {
            return -1;
        }
        return zzp.zzahe();
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onActivityResult(int r4, int r5, android.content.Intent r6) {
        /*
            r3 = this;
            java.util.concurrent.atomic.AtomicReference<com.google.android.gms.common.api.internal.zzp> r0 = r3.zzfol
            java.lang.Object r0 = r0.get()
            com.google.android.gms.common.api.internal.zzp r0 = (com.google.android.gms.common.api.internal.zzp) r0
            r1 = 1
            r2 = 0
            switch(r4) {
                case 1: goto L_0x002e;
                case 2: goto L_0x000e;
                default: goto L_0x000d;
            }
        L_0x000d:
            goto L_0x0053
        L_0x000e:
            com.google.android.gms.common.GoogleApiAvailability r4 = r3.zzfmy
            android.app.Activity r5 = r3.getActivity()
            int r4 = r4.isGooglePlayServicesAvailable(r5)
            if (r4 != 0) goto L_0x001b
            goto L_0x001c
        L_0x001b:
            r1 = 0
        L_0x001c:
            if (r0 != 0) goto L_0x001f
            return
        L_0x001f:
            com.google.android.gms.common.ConnectionResult r5 = r0.zzahf()
            int r5 = r5.getErrorCode()
            r6 = 18
            if (r5 != r6) goto L_0x0054
            if (r4 != r6) goto L_0x0054
            return
        L_0x002e:
            r4 = -1
            if (r5 != r4) goto L_0x0032
            goto L_0x0054
        L_0x0032:
            if (r5 != 0) goto L_0x0053
            r4 = 13
            if (r6 == 0) goto L_0x003e
            java.lang.String r5 = "<<ResolutionFailureErrorDetail>>"
            int r4 = r6.getIntExtra(r5, r4)
        L_0x003e:
            com.google.android.gms.common.api.internal.zzp r5 = new com.google.android.gms.common.api.internal.zzp
            com.google.android.gms.common.ConnectionResult r6 = new com.google.android.gms.common.ConnectionResult
            r1 = 0
            r6.<init>(r4, r1)
            int r4 = zza(r0)
            r5.<init>(r6, r4)
            java.util.concurrent.atomic.AtomicReference<com.google.android.gms.common.api.internal.zzp> r4 = r3.zzfol
            r4.set(r5)
            r0 = r5
        L_0x0053:
            r1 = 0
        L_0x0054:
            if (r1 == 0) goto L_0x005a
            r3.zzahd()
            return
        L_0x005a:
            if (r0 == 0) goto L_0x0067
            com.google.android.gms.common.ConnectionResult r4 = r0.zzahf()
            int r5 = r0.zzahe()
            r3.zza(r4, r5)
        L_0x0067:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzo.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onCancel(DialogInterface dialogInterface) {
        zza(new ConnectionResult(13, null), zza((zzp) this.zzfol.get()));
        zzahd();
    }

    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzfol.set(bundle.getBoolean("resolving_error", false) ? new zzp(new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution")), bundle.getInt("failed_client_id", -1)) : null);
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        zzp zzp = (zzp) this.zzfol.get();
        if (zzp != null) {
            bundle.putBoolean("resolving_error", true);
            bundle.putInt("failed_client_id", zzp.zzahe());
            bundle.putInt("failed_status", zzp.zzahf().getErrorCode());
            bundle.putParcelable("failed_resolution", zzp.zzahf().getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        this.mStarted = true;
    }

    public void onStop() {
        super.onStop();
        this.mStarted = false;
    }

    /* access modifiers changed from: protected */
    public abstract void zza(ConnectionResult connectionResult, int i);

    /* access modifiers changed from: protected */
    public abstract void zzagz();

    /* access modifiers changed from: protected */
    public final void zzahd() {
        this.zzfol.set(null);
        zzagz();
    }

    public final void zzb(ConnectionResult connectionResult, int i) {
        zzp zzp = new zzp(connectionResult, i);
        if (this.zzfol.compareAndSet(null, zzp)) {
            this.zzfom.post(new zzq(this, zzp));
        }
    }
}
