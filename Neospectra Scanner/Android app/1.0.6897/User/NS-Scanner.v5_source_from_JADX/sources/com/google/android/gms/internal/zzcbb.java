package com.google.android.gms.internal;

public final class zzcbb {
    private static zzcbb zzhiv;
    private final zzcaw zzhiw = new zzcaw();
    private final zzcax zzhix = new zzcax();

    static {
        zzcbb zzcbb = new zzcbb();
        synchronized (zzcbb.class) {
            zzhiv = zzcbb;
        }
    }

    private zzcbb() {
    }

    private static zzcbb zzara() {
        zzcbb zzcbb;
        synchronized (zzcbb.class) {
            zzcbb = zzhiv;
        }
        return zzcbb;
    }

    public static zzcaw zzarb() {
        return zzara().zzhiw;
    }

    public static zzcax zzarc() {
        return zzara().zzhix;
    }
}
