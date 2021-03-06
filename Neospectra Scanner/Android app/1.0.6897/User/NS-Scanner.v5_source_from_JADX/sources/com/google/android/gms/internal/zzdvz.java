package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.zzcz;

final class zzdvz<O extends ApiOptions> extends GoogleApi<O> {
    public zzdvz(@NonNull Context context, Api<O> api, O o, zzcz zzcz) {
        super(context, api, o, zzcz);
    }
}
