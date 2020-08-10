package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.zzc;
import com.google.android.gms.dynamite.descriptors.com.google.android.gms.flags.ModuleDescriptor;

public final class zzcax {
    private boolean zzare = false;
    private zzcay zzhiu = null;

    public final void initialize(Context context) {
        synchronized (this) {
            if (!this.zzare) {
                try {
                    this.zzhiu = zzcaz.asInterface(DynamiteModule.zza(context, DynamiteModule.zzgxb, ModuleDescriptor.MODULE_ID).zzhb("com.google.android.gms.flags.impl.FlagProviderImpl"));
                    this.zzhiu.init(zzn.zzz(context));
                    this.zzare = true;
                } catch (RemoteException | zzc e) {
                    Log.w("FlagValueProvider", "Failed to initialize flags module.", e);
                }
            }
        }
    }

    public final <T> T zzb(zzcaq<T> zzcaq) {
        synchronized (this) {
            if (this.zzare) {
                return zzcaq.zza(this.zzhiu);
            }
            T zziv = zzcaq.zziv();
            return zziv;
        }
    }
}
