package com.google.android.gms.internal;

final class zzfgt implements zzfhd {
    private zzfhd[] zzpii;

    zzfgt(zzfhd... zzfhdArr) {
        this.zzpii = zzfhdArr;
    }

    public final boolean zzi(Class<?> cls) {
        for (zzfhd zzi : this.zzpii) {
            if (zzi.zzi(cls)) {
                return true;
            }
        }
        return false;
    }

    public final zzfhc zzj(Class<?> cls) {
        zzfhd[] zzfhdArr;
        for (zzfhd zzfhd : this.zzpii) {
            if (zzfhd.zzi(cls)) {
                return zzfhd.zzj(cls);
            }
        }
        String str = "No factory is available for message type: ";
        String valueOf = String.valueOf(cls.getName());
        throw new UnsupportedOperationException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str));
    }
}
