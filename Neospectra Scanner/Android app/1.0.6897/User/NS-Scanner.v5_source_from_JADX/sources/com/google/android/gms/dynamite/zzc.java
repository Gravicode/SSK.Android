package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule.zzd;

final class zzc implements zzd {
    zzc() {
    }

    public final zzj zza(Context context, String str, zzi zzi) throws com.google.android.gms.dynamite.DynamiteModule.zzc {
        zzj zzj = new zzj();
        zzj.zzgxg = zzi.zzab(context, str);
        if (zzj.zzgxg != 0) {
            zzj.zzgxi = -1;
            return zzj;
        }
        zzj.zzgxh = zzi.zzc(context, str, true);
        if (zzj.zzgxh != 0) {
            zzj.zzgxi = 1;
        }
        return zzj;
    }
}
