package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.zza;
import com.google.android.gms.measurement.AppMeasurement.zzb;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public final class zzckc extends zzcjl {
    protected zzckf zzjhn;
    private volatile zzb zzjho;
    private zzb zzjhp;
    private long zzjhq;
    private final Map<Activity, zzckf> zzjhr = new ArrayMap();
    private final CopyOnWriteArrayList<zza> zzjhs = new CopyOnWriteArrayList<>();
    private boolean zzjht;
    private zzb zzjhu;
    private String zzjhv;

    public zzckc(zzcim zzcim) {
        super(zzcim);
    }

    @MainThread
    private final void zza(Activity activity, zzckf zzckf, boolean z) {
        zzb zzb = null;
        zzb zzb2 = this.zzjho != null ? this.zzjho : (this.zzjhp == null || Math.abs(zzws().elapsedRealtime() - this.zzjhq) >= 1000) ? null : this.zzjhp;
        if (zzb2 != null) {
            zzb = new zzb(zzb2);
        }
        boolean z2 = true;
        this.zzjht = true;
        try {
            Iterator it = this.zzjhs.iterator();
            while (it.hasNext()) {
                try {
                    z2 &= ((zza) it.next()).zza(zzb, zzckf);
                } catch (Exception e) {
                    zzawy().zzazd().zzj("onScreenChangeCallback threw exception", e);
                }
            }
        } catch (Exception e2) {
            zzawy().zzazd().zzj("onScreenChangeCallback loop threw exception", e2);
        } catch (Throwable th) {
            this.zzjht = false;
            throw th;
        }
        this.zzjht = false;
        zzb zzb3 = this.zzjho == null ? this.zzjhp : this.zzjho;
        if (z2) {
            if (zzckf.zziwl == null) {
                zzckf.zziwl = zzjy(activity.getClass().getCanonicalName());
            }
            zzckf zzckf2 = new zzckf(zzckf);
            this.zzjhp = this.zzjho;
            this.zzjhq = zzws().elapsedRealtime();
            this.zzjho = zzckf2;
            zzawx().zzg(new zzckd(this, z, zzb3, zzckf2));
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zza(@NonNull zzckf zzckf) {
        zzawk().zzaj(zzws().elapsedRealtime());
        if (zzaww().zzbs(zzckf.zzjib)) {
            zzckf.zzjib = false;
        }
    }

    public static void zza(zzb zzb, Bundle bundle) {
        if (bundle != null && zzb != null && !bundle.containsKey("_sc")) {
            if (zzb.zziwk != null) {
                bundle.putString("_sn", zzb.zziwk);
            }
            bundle.putString("_sc", zzb.zziwl);
            bundle.putLong("_si", zzb.zziwm);
        }
    }

    private static String zzjy(String str) {
        String[] split = str.split("\\.");
        if (split.length == 0) {
            return str.substring(0, 36);
        }
        String str2 = split[split.length - 1];
        if (str2.length() > 36) {
            str2 = str2.substring(0, 36);
        }
        return str2;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @MainThread
    public final void onActivityDestroyed(Activity activity) {
        this.zzjhr.remove(activity);
    }

    @MainThread
    public final void onActivityPaused(Activity activity) {
        zzckf zzq = zzq(activity);
        this.zzjhp = this.zzjho;
        this.zzjhq = zzws().elapsedRealtime();
        this.zzjho = null;
        zzawx().zzg(new zzcke(this, zzq));
    }

    @MainThread
    public final void onActivityResumed(Activity activity) {
        zza(activity, zzq(activity), false);
        zzcgd zzawk = zzawk();
        zzawk.zzawx().zzg(new zzcgg(zzawk, zzawk.zzws().elapsedRealtime()));
    }

    @MainThread
    public final void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null) {
            zzckf zzckf = (zzckf) this.zzjhr.get(activity);
            if (zzckf != null) {
                Bundle bundle2 = new Bundle();
                bundle2.putLong("id", zzckf.zziwm);
                bundle2.putString("name", zzckf.zziwk);
                bundle2.putString("referrer_name", zzckf.zziwl);
                bundle.putBundle("com.google.firebase.analytics.screen_service", bundle2);
            }
        }
    }

    @MainThread
    public final void registerOnScreenChangeCallback(@NonNull zza zza) {
        if (zza == null) {
            zzawy().zzazf().log("Attempting to register null OnScreenChangeCallback");
            return;
        }
        this.zzjhs.remove(zza);
        this.zzjhs.add(zza);
    }

    @MainThread
    public final void setCurrentScreen(@NonNull Activity activity, @Nullable @Size(max = 36, min = 1) String str, @Nullable @Size(max = 36, min = 1) String str2) {
        if (activity == null) {
            zzawy().zzazf().log("setCurrentScreen must be called with a non-null activity");
            return;
        }
        zzawx();
        if (!zzcih.zzau()) {
            zzawy().zzazf().log("setCurrentScreen must be called from the main thread");
        } else if (this.zzjht) {
            zzawy().zzazf().log("Cannot call setCurrentScreen from onScreenChangeCallback");
        } else if (this.zzjho == null) {
            zzawy().zzazf().log("setCurrentScreen cannot be called while no activity active");
        } else if (this.zzjhr.get(activity) == null) {
            zzawy().zzazf().log("setCurrentScreen must be called with an activity in the activity lifecycle");
        } else {
            if (str2 == null) {
                str2 = zzjy(activity.getClass().getCanonicalName());
            }
            boolean equals = this.zzjho.zziwl.equals(str2);
            boolean zzas = zzclq.zzas(this.zzjho.zziwk, str);
            if (equals && zzas) {
                zzawy().zzazg().log("setCurrentScreen cannot be called with the same class and name");
            } else if (str != null && (str.length() <= 0 || str.length() > 100)) {
                zzawy().zzazf().zzj("Invalid screen name length in setCurrentScreen. Length", Integer.valueOf(str.length()));
            } else if (str2 == null || (str2.length() > 0 && str2.length() <= 100)) {
                zzawy().zzazj().zze("Setting current screen to name, class", str == null ? "null" : str, str2);
                zzckf zzckf = new zzckf(str, str2, zzawu().zzbay());
                this.zzjhr.put(activity, zzckf);
                zza(activity, zzckf, true);
            } else {
                zzawy().zzazf().zzj("Invalid class name length in setCurrentScreen. Length", Integer.valueOf(str2.length()));
            }
        }
    }

    @MainThread
    public final void unregisterOnScreenChangeCallback(@NonNull zza zza) {
        this.zzjhs.remove(zza);
    }

    @WorkerThread
    public final void zza(String str, zzb zzb) {
        zzve();
        synchronized (this) {
            if (this.zzjhv == null || this.zzjhv.equals(str) || zzb != null) {
                this.zzjhv = str;
                this.zzjhu = zzb;
            }
        }
    }

    public final /* bridge */ /* synthetic */ void zzawi() {
        super.zzawi();
    }

    public final /* bridge */ /* synthetic */ void zzawj() {
        super.zzawj();
    }

    public final /* bridge */ /* synthetic */ zzcgd zzawk() {
        return super.zzawk();
    }

    public final /* bridge */ /* synthetic */ zzcgk zzawl() {
        return super.zzawl();
    }

    public final /* bridge */ /* synthetic */ zzcjn zzawm() {
        return super.zzawm();
    }

    public final /* bridge */ /* synthetic */ zzchh zzawn() {
        return super.zzawn();
    }

    public final /* bridge */ /* synthetic */ zzcgu zzawo() {
        return super.zzawo();
    }

    public final /* bridge */ /* synthetic */ zzckg zzawp() {
        return super.zzawp();
    }

    public final /* bridge */ /* synthetic */ zzckc zzawq() {
        return super.zzawq();
    }

    public final /* bridge */ /* synthetic */ zzchi zzawr() {
        return super.zzawr();
    }

    public final /* bridge */ /* synthetic */ zzcgo zzaws() {
        return super.zzaws();
    }

    public final /* bridge */ /* synthetic */ zzchk zzawt() {
        return super.zzawt();
    }

    public final /* bridge */ /* synthetic */ zzclq zzawu() {
        return super.zzawu();
    }

    public final /* bridge */ /* synthetic */ zzcig zzawv() {
        return super.zzawv();
    }

    public final /* bridge */ /* synthetic */ zzclf zzaww() {
        return super.zzaww();
    }

    public final /* bridge */ /* synthetic */ zzcih zzawx() {
        return super.zzawx();
    }

    public final /* bridge */ /* synthetic */ zzchm zzawy() {
        return super.zzawy();
    }

    public final /* bridge */ /* synthetic */ zzchx zzawz() {
        return super.zzawz();
    }

    public final /* bridge */ /* synthetic */ zzcgn zzaxa() {
        return super.zzaxa();
    }

    /* access modifiers changed from: protected */
    public final boolean zzaxz() {
        return false;
    }

    @WorkerThread
    public final zzckf zzbao() {
        zzxf();
        zzve();
        return this.zzjhn;
    }

    public final zzb zzbap() {
        zzb zzb = this.zzjho;
        if (zzb == null) {
            return null;
        }
        return new zzb(zzb);
    }

    /* access modifiers changed from: 0000 */
    @MainThread
    public final zzckf zzq(@NonNull Activity activity) {
        zzbq.checkNotNull(activity);
        zzckf zzckf = (zzckf) this.zzjhr.get(activity);
        if (zzckf != null) {
            return zzckf;
        }
        zzckf zzckf2 = new zzckf(null, zzjy(activity.getClass().getCanonicalName()), zzawu().zzbay());
        this.zzjhr.put(activity, zzckf2);
        return zzckf2;
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}
