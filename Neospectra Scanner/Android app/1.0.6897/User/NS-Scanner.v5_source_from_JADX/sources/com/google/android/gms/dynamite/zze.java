package com.google.android.gms.dynamite;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule.zzc;
import com.google.android.gms.dynamite.DynamiteModule.zzd;

final class zze implements zzd {
    zze() {
    }

    public final zzj zza(Context context, String str, zzi zzi) throws zzc {
        zzj zzj = new zzj();
        zzj.zzgxg = zzi.zzab(context, str);
        zzj.zzgxh = zzj.zzgxg != 0 ? zzi.zzc(context, str, false) : zzi.zzc(context, str, true);
        if (zzj.zzgxg == 0 && zzj.zzgxh == 0) {
            zzj.zzgxi = 0;
            return zzj;
        } else if (zzj.zzgxg >= zzj.zzgxh) {
            zzj.zzgxi = -1;
            return zzj;
        } else {
            zzj.zzgxi = 1;
            return zzj;
        }
    }
}
