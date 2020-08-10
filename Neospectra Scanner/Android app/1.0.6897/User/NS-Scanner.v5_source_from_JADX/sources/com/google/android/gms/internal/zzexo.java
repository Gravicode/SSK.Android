package com.google.android.gms.internal;

import android.app.Activity;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zzce;
import com.google.android.gms.common.api.internal.zzcf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class zzexo {
    private static final zzexo zzoln = new zzexo();
    private final Map<Object, zzexp> zzolo = new HashMap();
    private final Object zzolp = new Object();

    static class zza extends LifecycleCallback {
        private final List<zzexp> zzezo = new ArrayList();

        private zza(zzcf zzcf) {
            super(zzcf);
            this.zzfud.zza("StorageOnStopCallback", (LifecycleCallback) this);
        }

        public static zza zzt(Activity activity) {
            zzcf zzb = zzb(new zzce(activity));
            zza zza = (zza) zzb.zza("StorageOnStopCallback", zza.class);
            return zza == null ? new zza(zzb) : zza;
        }

        @MainThread
        public final void onStop() {
            ArrayList arrayList;
            synchronized (this.zzezo) {
                arrayList = new ArrayList(this.zzezo);
                this.zzezo.clear();
            }
            ArrayList arrayList2 = arrayList;
            int size = arrayList2.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList2.get(i);
                i++;
                zzexp zzexp = (zzexp) obj;
                if (zzexp != null) {
                    Log.d("StorageOnStopCallback", "removing subscription from activity.");
                    zzexp.zzbko().run();
                    zzexo.zzcme().zzcm(zzexp.zzcmf());
                }
            }
        }

        public final void zza(zzexp zzexp) {
            synchronized (this.zzezo) {
                this.zzezo.add(zzexp);
            }
        }

        public final void zzb(zzexp zzexp) {
            synchronized (this.zzezo) {
                this.zzezo.remove(zzexp);
            }
        }
    }

    private zzexo() {
    }

    @NonNull
    public static zzexo zzcme() {
        return zzoln;
    }

    public final void zza(@NonNull Activity activity, @NonNull Object obj, @NonNull Runnable runnable) {
        synchronized (this.zzolp) {
            zzexp zzexp = new zzexp(activity, runnable, obj);
            zza.zzt(activity).zza(zzexp);
            this.zzolo.put(obj, zzexp);
        }
    }

    public final void zzcm(@NonNull Object obj) {
        synchronized (this.zzolp) {
            zzexp zzexp = (zzexp) this.zzolo.get(obj);
            if (zzexp != null) {
                zza.zzt(zzexp.getActivity()).zzb(zzexp);
            }
        }
    }
}
