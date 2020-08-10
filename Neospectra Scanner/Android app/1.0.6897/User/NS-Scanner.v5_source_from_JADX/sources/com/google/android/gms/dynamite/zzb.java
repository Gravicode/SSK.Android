package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule.zzc;
import com.google.android.gms.dynamite.DynamiteModule.zzd;

final class zzb implements zzd {
    zzb() {
    }

    public final zzj zza(Context context, String str, zzi zzi) throws zzc {
        zzj zzj = new zzj();
        zzj.zzgxh = zzi.zzc(context, str, true);
        if (zzj.zzgxh != 0) {
            zzj.zzgxi = 1;
            return zzj;
        }
        zzj.zzgxg = zzi.zzab(context, str);
        if (zzj.zzgxg != 0) {
            zzj.zzgxi = -1;
        }
        return zzj;
    }
}
