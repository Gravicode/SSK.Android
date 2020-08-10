package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule.zzc;
import com.google.android.gms.dynamite.DynamiteModule.zzd;

final class zzf implements zzd {
    zzf() {
    }

    public final zzj zza(Context context, String str, zzi zzi) throws zzc {
        int i;
        zzj zzj = new zzj();
        zzj.zzgxg = zzi.zzab(context, str);
        zzj.zzgxh = zzi.zzc(context, str, true);
        if (zzj.zzgxg == 0 && zzj.zzgxh == 0) {
            i = 0;
        } else if (zzj.zzgxh >= zzj.zzgxg) {
            zzj.zzgxi = 1;
            return zzj;
        } else {
            i = -1;
        }
        zzj.zzgxi = i;
        return zzj;
    }
}
