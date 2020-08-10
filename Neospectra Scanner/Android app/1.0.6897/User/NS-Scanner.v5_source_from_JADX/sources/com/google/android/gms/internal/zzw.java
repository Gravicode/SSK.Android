package com.google.android.gms.internal;

public final class zzw<T> {
    public final T result;
    public final zzc zzbh;
    public final zzad zzbi;
    public boolean zzbj;

    private zzw(zzad zzad) {
        this.zzbj = false;
        this.result = null;
        this.zzbh = null;
        this.zzbi = zzad;
    }

    private zzw(T t, zzc zzc) {
        this.zzbj = false;
        this.result = t;
        this.zzbh = zzc;
        this.zzbi = null;
    }

    public static <T> zzw<T> zza(T t, zzc zzc) {
        return new zzw<>(t, zzc);
    }

    public static <T> zzw<T> zzc(zzad zzad) {
        return new zzw<>(zzad);
    }
}
