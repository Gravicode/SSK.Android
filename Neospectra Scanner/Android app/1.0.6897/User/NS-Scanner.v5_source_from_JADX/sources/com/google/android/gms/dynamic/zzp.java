package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.internal.zzbq;

public abstract class zzp<T> {
    private final String zzgwn;
    private T zzgwo;

    protected zzp(String str) {
        this.zzgwn = str;
    }

    /* access modifiers changed from: protected */
    public final T zzde(Context context) throws zzq {
        if (this.zzgwo == null) {
            zzbq.checkNotNull(context);
            Context remoteContext = com.google.android.gms.common.zzp.getRemoteContext(context);
            if (remoteContext == null) {
                throw new zzq("Could not get remote context.");
            }
            try {
                this.zzgwo = zze((IBinder) remoteContext.getClassLoader().loadClass(this.zzgwn).newInstance());
            } catch (ClassNotFoundException e) {
                throw new zzq("Could not load creator class.", e);
            } catch (InstantiationException e2) {
                throw new zzq("Could not instantiate creator.", e2);
            } catch (IllegalAccessException e3) {
                throw new zzq("Could not access creator.", e3);
            }
        }
        return this.zzgwo;
    }

    /* access modifiers changed from: protected */
    public abstract T zze(IBinder iBinder);
}
