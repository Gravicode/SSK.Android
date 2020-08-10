package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.zzcz;

final class zzdvy {
    private Context mContext;
    private Api zzfin;
    private zzcz zzfmh;

    zzdvy(zzdvv zzdvv, Context context, Api api, zzcz zzcz) {
        this.mContext = context;
        this.zzfin = api;
        this.zzfmh = zzcz;
    }

    /* access modifiers changed from: 0000 */
    public final GoogleApi<O> zza(zzdvw zzdvw) {
        return new zzdvz(this.mContext, this.zzfin, zzdvw, this.zzfmh);
    }
}
