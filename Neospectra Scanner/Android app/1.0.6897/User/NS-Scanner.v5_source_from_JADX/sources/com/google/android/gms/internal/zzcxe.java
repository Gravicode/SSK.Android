package com.google.android.gms.internal;

import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;

public final class zzcxe implements Optional {
    public static final zzcxe zzkbs;
    private final boolean zzefl = false;
    private final String zzefm = null;
    private final boolean zzehn = false;
    private final String zzeho = null;
    private final boolean zzkbt = false;
    private final boolean zzkbu = false;
    private final Long zzkbv = null;
    private final Long zzkbw = null;

    static {
        new zzcxf();
        zzcxe zzcxe = new zzcxe(false, false, null, false, null, false, null, null);
        zzkbs = zzcxe;
    }

    private zzcxe(boolean z, boolean z2, String str, boolean z3, String str2, boolean z4, Long l, Long l2) {
    }

    public final String getServerClientId() {
        return this.zzefm;
    }

    public final boolean isIdTokenRequested() {
        return this.zzefl;
    }

    public final boolean zzbdc() {
        return this.zzkbt;
    }

    public final boolean zzbdd() {
        return this.zzehn;
    }

    @Nullable
    public final String zzbde() {
        return this.zzeho;
    }

    public final boolean zzbdf() {
        return this.zzkbu;
    }

    @Nullable
    public final Long zzbdg() {
        return this.zzkbv;
    }

    @Nullable
    public final Long zzbdh() {
        return this.zzkbw;
    }
}
