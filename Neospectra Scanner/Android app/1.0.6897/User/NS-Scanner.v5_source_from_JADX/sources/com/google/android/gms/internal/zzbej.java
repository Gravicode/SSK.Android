package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzg;

public final class zzbej extends GoogleApi<NoOptions> implements zzbee {
    private zzbej(@NonNull Context context) {
        super(context, zzbdy.API, null, (zzcz) new zzg());
    }

    public static zzbee zzca(@NonNull Context context) {
        return new zzbej(context);
    }

    public final PendingResult<Status> zza(zzbeh zzbeh) {
        return zzc(new zzbem(zzbeh, zzago()));
    }
}
