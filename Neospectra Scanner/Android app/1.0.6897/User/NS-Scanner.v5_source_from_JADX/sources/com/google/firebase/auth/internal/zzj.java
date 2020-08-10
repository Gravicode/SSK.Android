package com.google.firebase.auth.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzdyi;
import com.google.firebase.auth.ProviderQueryResult;
import java.util.List;

public final class zzj implements ProviderQueryResult {
    private List<String> zzmhm;

    public zzj(@NonNull zzdyi zzdyi) {
        zzbq.checkNotNull(zzdyi);
        this.zzmhm = zzdyi.getAllProviders();
    }

    @Nullable
    public final List<String> getProviders() {
        return this.zzmhm;
    }
}
