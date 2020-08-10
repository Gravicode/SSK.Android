package com.google.android.gms.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

final class zzam {
    final String key;
    final String zza;
    final long zzb;
    final long zzc;
    long zzcb;
    final long zzd;
    final long zze;
    final List<zzl> zzg;

    zzam(String str, zzc zzc2) {
        this(str, zzc2.zza, zzc2.zzb, zzc2.zzc, zzc2.zzd, zzc2.zze, zzc2.zzg != null ? zzc2.zzg : zzao.zza(zzc2.zzf));
        this.zzcb = (long) zzc2.data.length;
    }

    private zzam(String str, String str2, long j, long j2, long j3, long j4, List<zzl> list) {
        this.key = str;
        if ("".equals(str2)) {
            str2 = null;
        }
        this.zza = str2;
        this.zzb = j;
        this.zzc = j2;
        this.zzd = j3;
        this.zze = j4;
        this.zzg = list;
    }

    static zzam zzc(zzan zzan) throws IOException {
        if (zzal.zzb((InputStream) zzan) != 538247942) {
            throw new IOException();
        }
        zzam zzam = new zzam(zzal.zza(zzan), zzal.zza(zzan), zzal.zzc(zzan), zzal.zzc(zzan), zzal.zzc(zzan), zzal.zzc(zzan), zzal.zzb(zzan));
        return zzam;
    }

    /* access modifiers changed from: 0000 */
    public final boolean zza(OutputStream outputStream) {
        try {
            zzal.zza(outputStream, 538247942);
            zzal.zza(outputStream, this.key);
            zzal.zza(outputStream, this.zza == null ? "" : this.zza);
            zzal.zza(outputStream, this.zzb);
            zzal.zza(outputStream, this.zzc);
            zzal.zza(outputStream, this.zzd);
            zzal.zza(outputStream, this.zze);
            List<zzl> list = this.zzg;
            if (list != null) {
                zzal.zza(outputStream, list.size());
                for (zzl zzl : list) {
                    zzal.zza(outputStream, zzl.getName());
                    zzal.zza(outputStream, zzl.getValue());
                }
            } else {
                zzal.zza(outputStream, 0);
            }
            outputStream.flush();
            return true;
        } catch (IOException e) {
            zzae.zzb("%s", e.toString());
            return false;
        }
    }
}
