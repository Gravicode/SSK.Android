package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.support.p001v4.content.AsyncTaskLoader;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.zzcu;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class zzb extends AsyncTaskLoader<Void> implements zzcu {
    private Semaphore zzehw = new Semaphore(0);
    private Set<GoogleApiClient> zzehx;

    public zzb(Context context, Set<GoogleApiClient> set) {
        super(context);
        this.zzehx = set;
    }

    /* access modifiers changed from: private */
    /* renamed from: zzabh */
    public final Void loadInBackground() {
        int i = 0;
        for (GoogleApiClient zza : this.zzehx) {
            if (zza.zza((zzcu) this)) {
                i++;
            }
        }
        try {
            this.zzehw.tryAcquire(i, 5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Log.i("GACSignInLoader", "Unexpected InterruptedException", e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public final void onStartLoading() {
        this.zzehw.drainPermits();
        forceLoad();
    }

    public final void zzabi() {
        this.zzehw.release();
    }
}
