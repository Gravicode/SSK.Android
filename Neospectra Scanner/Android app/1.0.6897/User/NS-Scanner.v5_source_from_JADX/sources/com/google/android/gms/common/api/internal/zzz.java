package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.internal.zzcxe;

public final class zzz<O extends ApiOptions> extends GoogleApi<O> {
    private final zza<? extends zzcxd, zzcxe> zzfmz;
    private final zze zzfpv;
    private final zzt zzfpw;
    private final zzr zzfpx;

    public zzz(@NonNull Context context, Api<O> api, Looper looper, @NonNull zze zze, @NonNull zzt zzt, zzr zzr, zza<? extends zzcxd, zzcxe> zza) {
        super(context, api, looper);
        this.zzfpv = zze;
        this.zzfpw = zzt;
        this.zzfpx = zzr;
        this.zzfmz = zza;
        this.zzfmi.zza((GoogleApi<?>) this);
    }

    public final zze zza(Looper looper, zzbo<O> zzbo) {
        this.zzfpw.zza(zzbo);
        return this.zzfpv;
    }

    public final zzcv zza(Context context, Handler handler) {
        return new zzcv(context, handler, this.zzfpx, this.zzfmz);
    }

    public final zze zzahp() {
        return this.zzfpv;
    }
}
