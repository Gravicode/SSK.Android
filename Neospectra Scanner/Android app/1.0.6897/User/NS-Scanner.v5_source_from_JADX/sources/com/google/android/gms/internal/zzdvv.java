package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzdd;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.internal.zzdvw;
import com.google.android.gms.tasks.Task;
import java.util.Map;

public class zzdvv<O extends zzdvw> {
    private static zzbgg zzecc = new zzbgg("BiChannelGoogleApi", "FirebaseAuth: ");
    private GoogleApi<O> zzmeb;
    private GoogleApi<O> zzmec;
    private zzdvy zzmed;
    private O zzmee;
    private Integer zzmef;
    private Integer zzmeg;
    private zzdwb zzmeh;

    private zzdvv(@NonNull Context context, @NonNull Api<O> api, @NonNull O o, @NonNull zzcz zzcz) {
        zzdvz zzdvz;
        this.zzmee = o;
        this.zzmeg = Integer.valueOf(DynamiteModule.zzac(context, "com.google.android.gms.firebase_auth"));
        this.zzmef = Integer.valueOf(DynamiteModule.zzab(context, "com.google.firebase.auth"));
        if (this.zzmeg.intValue() != 0) {
            zzdvw zzdvw = (zzdvw) this.zzmee.clone();
            zzdvw.zzmei = false;
            zzdvz = new zzdvz(context, api, zzdvw, zzcz);
        } else {
            zzecc.zze("No Gms module; NOT initializing GMS implementation", new Object[0]);
            zzdvz = null;
        }
        this.zzmeb = zzdvz;
        if (this.zzmef.intValue() != 0) {
            this.zzmed = new zzdvy(this, context, api, zzcz);
        } else {
            zzecc.zze("No Fallback module; NOT setting up for lazy initialization", new Object[0]);
        }
    }

    public zzdvv(@NonNull Context context, Api<O> api, O o, zzcz zzcz, int i, int i2, Map<String, Integer> map) {
        this(context, api, o, zzcz);
        this.zzmeh = new zzdvx(i, i2, map, this.zzmeg.intValue() != 0);
    }

    private final GoogleApi zzc(zzdwa zzdwa) {
        if (this.zzmeh.zzd(zzdwa)) {
            zzecc.zzf("getGoogleApiForMethod() returned Fallback", new Object[0]);
            if (this.zzmec == null && this.zzmed != null) {
                zzdvy zzdvy = this.zzmed;
                zzdvw zzdvw = (zzdvw) this.zzmee.clone();
                zzdvw.zzmei = true;
                this.zzmec = zzdvy.zza(zzdvw);
            }
            return this.zzmec;
        }
        zzecc.zzf("getGoogleApiForMethod() returned Gms", new Object[0]);
        return this.zzmeb;
    }

    public final <TResult, A extends zzb> Task<TResult> zza(zzdwa<A, TResult> zzdwa) {
        return zzc(zzdwa).zza((zzdd<A, TResult>) zzdwa);
    }

    public final <TResult, A extends zzb> Task<TResult> zzb(zzdwa<A, TResult> zzdwa) {
        return zzc(zzdwa).zzb((zzdd<A, TResult>) zzdwa);
    }
}
