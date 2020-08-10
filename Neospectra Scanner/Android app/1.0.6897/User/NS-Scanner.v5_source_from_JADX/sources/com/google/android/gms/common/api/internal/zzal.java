package com.google.android.gms.common.api.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Result;

public final class zzal implements zzbh {
    /* access modifiers changed from: private */
    public final zzbi zzfqv;
    private boolean zzfqw = false;

    public zzal(zzbi zzbi) {
        this.zzfqv = zzbi;
    }

    public final void begin() {
    }

    public final void connect() {
        if (this.zzfqw) {
            this.zzfqw = false;
            this.zzfqv.zza((zzbj) new zzan(this, this));
        }
    }

    public final boolean disconnect() {
        if (this.zzfqw) {
            return false;
        }
        if (this.zzfqv.zzfpi.zzail()) {
            this.zzfqw = true;
            for (zzdg zzajs : this.zzfqv.zzfpi.zzfsg) {
                zzajs.zzajs();
            }
            return false;
        }
        this.zzfqv.zzg(null);
        return true;
    }

    public final void onConnected(Bundle bundle) {
    }

    public final void onConnectionSuspended(int i) {
        this.zzfqv.zzg(null);
        this.zzfqv.zzfsu.zzf(i, this.zzfqw);
    }

    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
    }

    /* access modifiers changed from: 0000 */
    public final void zzaia() {
        if (this.zzfqw) {
            this.zzfqw = false;
            this.zzfqv.zzfpi.zzfsh.release();
            disconnect();
        }
    }

    public final <A extends zzb, R extends Result, T extends zzm<R, A>> T zzd(T t) {
        return zze(t);
    }

    /* JADX WARNING: type inference failed for: r0v11, types: [com.google.android.gms.common.api.Api$zzg] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <A extends com.google.android.gms.common.api.Api.zzb, T extends com.google.android.gms.common.api.internal.zzm<? extends com.google.android.gms.common.api.Result, A>> T zze(T r4) {
        /*
            r3 = this;
            com.google.android.gms.common.api.internal.zzbi r0 = r3.zzfqv     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.internal.zzba r0 = r0.zzfpi     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.internal.zzdj r0 = r0.zzfsh     // Catch:{ DeadObjectException -> 0x0049 }
            r0.zzb(r4)     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.internal.zzbi r0 = r3.zzfqv     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.internal.zzba r0 = r0.zzfpi     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.Api$zzc r1 = r4.zzagf()     // Catch:{ DeadObjectException -> 0x0049 }
            java.util.Map<com.google.android.gms.common.api.Api$zzc<?>, com.google.android.gms.common.api.Api$zze> r0 = r0.zzfsb     // Catch:{ DeadObjectException -> 0x0049 }
            java.lang.Object r0 = r0.get(r1)     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.Api$zze r0 = (com.google.android.gms.common.api.Api.zze) r0     // Catch:{ DeadObjectException -> 0x0049 }
            java.lang.String r1 = "Appropriate Api was not requested."
            com.google.android.gms.common.internal.zzbq.checkNotNull(r0, r1)     // Catch:{ DeadObjectException -> 0x0049 }
            boolean r1 = r0.isConnected()     // Catch:{ DeadObjectException -> 0x0049 }
            if (r1 != 0) goto L_0x003d
            com.google.android.gms.common.api.internal.zzbi r1 = r3.zzfqv     // Catch:{ DeadObjectException -> 0x0049 }
            java.util.Map<com.google.android.gms.common.api.Api$zzc<?>, com.google.android.gms.common.ConnectionResult> r1 = r1.zzfsq     // Catch:{ DeadObjectException -> 0x0049 }
            com.google.android.gms.common.api.Api$zzc r2 = r4.zzagf()     // Catch:{ DeadObjectException -> 0x0049 }
            boolean r1 = r1.containsKey(r2)     // Catch:{ DeadObjectException -> 0x0049 }
            if (r1 == 0) goto L_0x003d
            com.google.android.gms.common.api.Status r0 = new com.google.android.gms.common.api.Status     // Catch:{ DeadObjectException -> 0x0049 }
            r1 = 17
            r0.<init>(r1)     // Catch:{ DeadObjectException -> 0x0049 }
            r4.zzu(r0)     // Catch:{ DeadObjectException -> 0x0049 }
            return r4
        L_0x003d:
            boolean r1 = r0 instanceof com.google.android.gms.common.internal.zzbz     // Catch:{ DeadObjectException -> 0x0049 }
            if (r1 == 0) goto L_0x0045
            com.google.android.gms.common.api.Api$zzg r0 = com.google.android.gms.common.internal.zzbz.zzals()     // Catch:{ DeadObjectException -> 0x0049 }
        L_0x0045:
            r4.zzb(r0)     // Catch:{ DeadObjectException -> 0x0049 }
            return r4
        L_0x0049:
            r0 = move-exception
            com.google.android.gms.common.api.internal.zzbi r0 = r3.zzfqv
            com.google.android.gms.common.api.internal.zzam r1 = new com.google.android.gms.common.api.internal.zzam
            r1.<init>(r3, r3)
            r0.zza(r1)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzal.zze(com.google.android.gms.common.api.internal.zzm):com.google.android.gms.common.api.internal.zzm");
    }
}
