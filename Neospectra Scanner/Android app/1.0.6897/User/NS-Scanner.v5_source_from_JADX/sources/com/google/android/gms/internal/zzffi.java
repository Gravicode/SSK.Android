package com.google.android.gms.internal;

import com.google.android.gms.internal.zzffu.zzg;
import java.io.IOException;

final class zzffi implements zzfji {
    private final zzffg zzpfl;

    private zzffi(zzffg zzffg) {
        this.zzpfl = (zzffg) zzffz.zzc(zzffg, "output");
    }

    public static zzffi zzb(zzffg zzffg) {
        return zzffg.zzpga != null ? zzffg.zzpga : new zzffi(zzffg);
    }

    public final void zzb(int i, Object obj) {
        try {
            if (obj instanceof zzfes) {
                this.zzpfl.zzb(i, (zzfes) obj);
            } else {
                this.zzpfl.zzb(i, (zzfhe) obj);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final int zzcwv() {
        return zzg.zzphn;
    }
}
