package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzh;
import com.google.firebase.FirebaseApp;
import java.util.Random;

public final class zzexr {
    private static zzd zzegr = zzh.zzamg();
    private static Random zzlgv = new Random();
    private static zzext zzolw = new zzexu();
    private volatile boolean zzan;
    private FirebaseApp zzoic;
    private long zzolx;

    public zzexr(FirebaseApp firebaseApp, long j) {
        this.zzoic = firebaseApp;
        this.zzolx = j;
    }

    public static boolean zzin(int i) {
        return (i >= 500 && i < 600) || i == -2 || i == 429 || i == 408;
    }

    public final void cancel() {
        this.zzan = true;
    }

    public final void reset() {
        this.zzan = false;
    }

    public final void zza(@NonNull zzeyc zzeyc, boolean z) {
        zzbq.checkNotNull(zzeyc);
        long elapsedRealtime = zzegr.elapsedRealtime() + this.zzolx;
        if (z) {
            zzeyc.zze(zzexw.zzh(this.zzoic), this.zzoic.getApplicationContext());
        } else {
            zzeyc.zzsm(zzexw.zzh(this.zzoic));
        }
        int i = 1000;
        while (zzegr.elapsedRealtime() + ((long) i) <= elapsedRealtime && !zzeyc.zzcmm() && zzin(zzeyc.getResultCode())) {
            try {
                zzolw.zzio(zzlgv.nextInt(Callback.DEFAULT_SWIPE_ANIMATION_DURATION) + i);
                if (i < 30000) {
                    if (zzeyc.getResultCode() != -2) {
                        i <<= 1;
                        Log.w("ExponenentialBackoff", "network error occurred, backing off/sleeping.");
                    } else {
                        Log.w("ExponenentialBackoff", "network unavailable, sleeping.");
                        i = 1000;
                    }
                }
                if (!this.zzan) {
                    zzeyc.reset();
                    if (z) {
                        zzeyc.zze(zzexw.zzh(this.zzoic), this.zzoic.getApplicationContext());
                    } else {
                        zzeyc.zzsm(zzexw.zzh(this.zzoic));
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                Log.w("ExponenentialBackoff", "thread interrupted during exponential backoff.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
