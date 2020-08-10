package com.google.firebase.auth.internal;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.internal.zzk;
import com.google.android.gms.common.api.internal.zzl;
import com.google.android.gms.internal.zzdym;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseApp.zzb;

public final class zzs implements zzb {
    private volatile int zzmia;
    /* access modifiers changed from: private */
    public final zzk zzmib;
    /* access modifiers changed from: private */
    public volatile boolean zzmic;

    private zzs(@NonNull Context context, @NonNull zzk zzk) {
        this.zzmic = false;
        this.zzmia = 0;
        this.zzmib = zzk;
        zzk.zza((Application) context.getApplicationContext());
        zzk.zzahb().zza((zzl) new zzt(this));
    }

    public zzs(@NonNull FirebaseApp firebaseApp) {
        this(firebaseApp.getApplicationContext(), new zzk(firebaseApp));
    }

    /* access modifiers changed from: private */
    public final boolean zzbsi() {
        return this.zzmia > 0 && !this.zzmic;
    }

    public final void cancel() {
        this.zzmib.cancel();
    }

    public final void zzc(@NonNull zzdym zzdym) {
        if (zzdym != null) {
            long zzbrv = zzdym.zzbrv();
            if (zzbrv <= 0) {
                zzbrv = 3600;
            }
            long zzbrw = (zzdym.zzbrw() + (zzbrv * 1000)) - 300000;
            zzk zzk = this.zzmib;
            zzk.zzmhs = zzbrw;
            zzk.zzmht = -1;
            if (zzbsi()) {
                this.zzmib.zzbsd();
            }
        }
    }

    public final void zzgj(int i) {
        if (i > 0 && this.zzmia == 0) {
            this.zzmia = i;
            if (zzbsi()) {
                this.zzmib.zzbsd();
            }
        } else if (i == 0 && this.zzmia != 0) {
            this.zzmib.cancel();
        }
        this.zzmia = i;
    }
}
