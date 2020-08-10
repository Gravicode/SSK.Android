package com.google.android.gms.internal;

import java.io.UnsupportedEncodingException;

public class zzau extends zzr<String> {
    private final zzy<String> zzcj;

    public zzau(int i, String str, zzy<String> zzy, zzx zzx) {
        super(i, str, zzx);
        this.zzcj = zzy;
    }

    /* access modifiers changed from: protected */
    public final zzw<String> zza(zzp zzp) {
        String str;
        try {
            str = new String(zzp.data, zzao.zzb(zzp.zzac));
        } catch (UnsupportedEncodingException e) {
            str = new String(zzp.data);
        }
        return zzw.zza(str, zzao.zzb(zzp));
    }

    /* access modifiers changed from: protected */
    /* renamed from: zzh */
    public void zza(String str) {
        if (this.zzcj != null) {
            this.zzcj.zzb(str);
        }
    }
}
