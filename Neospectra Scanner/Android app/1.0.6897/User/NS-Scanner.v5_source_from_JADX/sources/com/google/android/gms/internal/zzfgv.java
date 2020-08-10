package com.google.android.gms.internal;

import java.io.IOException;

public final class zzfgv<K, V> {
    private final V value;
    private final K zzmmo;
    private final zzfgx<K, V> zzpin;

    private zzfgv(zzfiy zzfiy, K k, zzfiy zzfiy2, V v) {
        this.zzpin = new zzfgx<>(zzfiy, k, zzfiy2, v);
        this.zzmmo = k;
        this.value = v;
    }

    static <K, V> int zza(zzfgx<K, V> zzfgx, K k, V v) {
        return zzffq.zza(zzfgx.zzpio, 1, (Object) k) + zzffq.zza(zzfgx.zzpiq, 2, (Object) v);
    }

    public static <K, V> zzfgv<K, V> zza(zzfiy zzfiy, K k, zzfiy zzfiy2, V v) {
        return new zzfgv<>(zzfiy, k, zzfiy2, v);
    }

    private static <T> T zza(zzffb zzffb, zzffm zzffm, zzfiy zzfiy, T t) throws IOException {
        switch (zzfiy) {
            case MESSAGE:
                zzfhf zzcxp = ((zzfhe) t).zzcxp();
                zzffb.zza(zzcxp, zzffm);
                return zzcxp.zzcxu();
            case ENUM:
                return Integer.valueOf(zzffb.zzcwd());
            case GROUP:
                throw new RuntimeException("Groups are not allowed in maps.");
            default:
                return zzffq.zza(zzffb, zzfiy, true);
        }
    }

    static <K, V> void zza(zzffg zzffg, zzfgx<K, V> zzfgx, K k, V v) throws IOException {
        zzffq.zza(zzffg, zzfgx.zzpio, 1, k);
        zzffq.zza(zzffg, zzfgx.zzpiq, 2, v);
    }

    public final void zza(zzffg zzffg, int i, K k, V v) throws IOException {
        zzffg.zzz(i, 2);
        zzffg.zzld(zza(this.zzpin, k, v));
        zza(zzffg, this.zzpin, k, v);
    }

    public final void zza(zzfgy<K, V> zzfgy, zzffb zzffb, zzffm zzffm) throws IOException {
        int zzks = zzffb.zzks(zzffb.zzcwi());
        K k = this.zzpin.zzpip;
        V v = this.zzpin.zzjxd;
        while (true) {
            int zzcvt = zzffb.zzcvt();
            if (zzcvt == 0) {
                break;
            } else if (zzcvt == (this.zzpin.zzpio.zzdae() | 8)) {
                k = zza(zzffb, zzffm, this.zzpin.zzpio, (T) k);
            } else if (zzcvt == (this.zzpin.zzpiq.zzdae() | 16)) {
                v = zza(zzffb, zzffm, this.zzpin.zzpiq, (T) v);
            } else if (!zzffb.zzkq(zzcvt)) {
                break;
            }
        }
        zzffb.zzkp(0);
        zzffb.zzkt(zzks);
        zzfgy.put(k, v);
    }

    public final int zzb(int i, K k, V v) {
        return zzffg.zzlg(i) + zzffg.zzln(zza(this.zzpin, k, v));
    }
}
