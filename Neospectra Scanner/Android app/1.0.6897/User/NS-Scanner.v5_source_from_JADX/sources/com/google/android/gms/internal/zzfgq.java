package com.google.android.gms.internal;

final class zzfgq implements zzfhw {
    private static final zzfhd zzpig = new zzfgr();
    private final zzfhd zzpie;
    private final zzfgu zzpif;

    public zzfgq() {
        this(zzfgu.DYNAMIC);
    }

    private zzfgq(zzfgu zzfgu) {
        this(new zzfgt(zzfft.zzcxl(), zzcyo()), zzfgu);
    }

    private zzfgq(zzfhd zzfhd, zzfgu zzfgu) {
        this.zzpie = (zzfhd) zzffz.zzc(zzfhd, "messageInfoFactory");
        this.zzpif = (zzfgu) zzffz.zzc(zzfgu, "mode");
    }

    private static <T> zzfhv<T> zza(Class<T> cls, zzfhc zzfhc) {
        if (zzffu.class.isAssignableFrom(cls)) {
            if (zza(zzfhc)) {
                return zzfhi.zza(cls, zzfhc, zzfgm.zzcyn(), zzfhx.zzczg(), zzffp.zzcxd(), zzfhb.zzcyt());
            }
            return zzfhi.zza(cls, zzfhc, zzfgm.zzcyn(), zzfhx.zzczg(), null, zzfhb.zzcyt());
        } else if (zza(zzfhc)) {
            return zzfhi.zza(cls, zzfhc, zzfgm.zzcym(), zzfhx.zzcze(), zzffp.zzcxe(), zzfhb.zzcys());
        } else {
            return zzfhi.zza(cls, zzfhc, zzfgm.zzcym(), zzfhx.zzczf(), null, zzfhb.zzcys());
        }
    }

    private static boolean zza(zzfhc zzfhc) {
        return zzfhc.zzcyv() == zzfhm.zzpiy;
    }

    private static <T> zzfhv<T> zzb(Class<T> cls, zzfhc zzfhc) {
        if (zzffu.class.isAssignableFrom(cls)) {
            if (zza(zzfhc)) {
                return zzfhi.zzb(cls, zzfhc, zzfgm.zzcyn(), zzfhx.zzczg(), zzffp.zzcxd(), zzfhb.zzcyt());
            }
            return zzfhi.zzb(cls, zzfhc, zzfgm.zzcyn(), zzfhx.zzczg(), null, zzfhb.zzcyt());
        } else if (zza(zzfhc)) {
            return zzfhi.zzb(cls, zzfhc, zzfgm.zzcym(), zzfhx.zzcze(), zzffp.zzcxe(), zzfhb.zzcys());
        } else {
            return zzfhi.zzb(cls, zzfhc, zzfgm.zzcym(), zzfhx.zzczf(), null, zzfhb.zzcys());
        }
    }

    private static zzfhd zzcyo() {
        try {
            return (zzfhd) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception e) {
            return zzpig;
        }
    }

    public final <T> zzfhv<T> zzk(Class<T> cls) {
        zzfin zzcze;
        zzffn zzcxe;
        zzfhx.zzm(cls);
        zzfhc zzj = this.zzpie.zzj(cls);
        if (zzj.zzcyw()) {
            if (zzffu.class.isAssignableFrom(cls)) {
                zzcze = zzfhx.zzczg();
                zzcxe = zzffp.zzcxd();
            } else {
                zzcze = zzfhx.zzcze();
                zzcxe = zzffp.zzcxe();
            }
            return zzfhj.zza(cls, zzcze, zzcxe, zzj.zzcyx());
        }
        switch (this.zzpif) {
            case TABLE:
                return zza(cls, zzj);
            case LOOKUP:
                return zzb(cls, zzj);
            default:
                return zzj.zzcyy() ? zza(cls, zzj) : zzb(cls, zzj);
        }
    }
}
