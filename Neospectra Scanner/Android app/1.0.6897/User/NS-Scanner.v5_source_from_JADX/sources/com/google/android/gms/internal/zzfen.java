package com.google.android.gms.internal;

import com.google.android.gms.internal.zzfhe;

public abstract class zzfen<MessageType extends zzfhe> implements zzfhk<MessageType> {
    private static final zzffm zzpfe = zzffm.zzcxb();

    public final /* synthetic */ Object zzc(zzffb zzffb, zzffm zzffm) throws zzfge {
        zzfhe zzfhe = (zzfhe) zze(zzffb, zzffm);
        if (zzfhe == null || zzfhe.isInitialized()) {
            return zzfhe;
        }
        zzfim zzfim = zzfhe instanceof zzfek ? new zzfim((zzfek) zzfhe) : zzfhe instanceof zzfem ? new zzfim((zzfem) zzfhe) : new zzfim(zzfhe);
        throw zzfim.zzczt().zzi(zzfhe);
    }
}
