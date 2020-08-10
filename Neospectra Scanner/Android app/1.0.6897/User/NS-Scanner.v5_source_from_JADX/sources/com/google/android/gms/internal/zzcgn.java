package com.google.android.gms.internal;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.common.util.zzs;
import java.lang.reflect.InvocationTargetException;

public final class zzcgn extends zzcjk {
    private Boolean zzdvl;

    zzcgn(zzcim zzcim) {
        super(zzcim);
    }

    public static long zzayb() {
        return ((Long) zzchc.zzjbg.get()).longValue();
    }

    public static long zzayc() {
        return ((Long) zzchc.zzjag.get()).longValue();
    }

    public static boolean zzaye() {
        return ((Boolean) zzchc.zzjab.get()).booleanValue();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final long zza(String str, zzchd<Long> zzchd) {
        if (str != null) {
            String zzam = zzawv().zzam(str, zzchd.getKey());
            if (!TextUtils.isEmpty(zzam)) {
                try {
                    return ((Long) zzchd.get(Long.valueOf(Long.valueOf(zzam).longValue()))).longValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return ((Long) zzchd.get()).longValue();
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

    public final boolean zzaya() {
        Boolean zziy = zziy("firebase_analytics_collection_deactivated");
        return zziy != null && zziy.booleanValue();
    }

    public final String zzayd() {
        String str;
        zzcho zzcho;
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class}).invoke(null, new Object[]{"debug.firebase.analytics.app", ""});
        } catch (ClassNotFoundException e) {
            e = e;
            zzcho = zzawy().zzazd();
            str = "Could not find SystemProperties class";
            zzcho.zzj(str, e);
            return "";
        } catch (NoSuchMethodException e2) {
            e = e2;
            zzcho = zzawy().zzazd();
            str = "Could not find SystemProperties.get() method";
            zzcho.zzj(str, e);
            return "";
        } catch (IllegalAccessException e3) {
            e = e3;
            zzcho = zzawy().zzazd();
            str = "Could not access SystemProperties.get()";
            zzcho.zzj(str, e);
            return "";
        } catch (InvocationTargetException e4) {
            e = e4;
            zzcho = zzawy().zzazd();
            str = "SystemProperties.get() threw an exception";
            zzcho.zzj(str, e);
            return "";
        }
    }

    public final int zzb(String str, zzchd<Integer> zzchd) {
        if (str != null) {
            String zzam = zzawv().zzam(str, zzchd.getKey());
            if (!TextUtils.isEmpty(zzam)) {
                try {
                    return ((Integer) zzchd.get(Integer.valueOf(Integer.valueOf(zzam).intValue()))).intValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return ((Integer) zzchd.get()).intValue();
    }

    public final int zzix(@Size(min = 1) String str) {
        return zzb(str, zzchc.zzjar);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public final Boolean zziy(@Size(min = 1) String str) {
        zzbq.zzgm(str);
        try {
            if (getContext().getPackageManager() == null) {
                zzawy().zzazd().log("Failed to load metadata: PackageManager is null");
                return null;
            }
            ApplicationInfo applicationInfo = zzbhf.zzdb(getContext()).getApplicationInfo(getContext().getPackageName(), 128);
            if (applicationInfo == null) {
                zzawy().zzazd().log("Failed to load metadata: ApplicationInfo is null");
                return null;
            } else if (applicationInfo.metaData == null) {
                zzawy().zzazd().log("Failed to load metadata: Metadata bundle is null");
                return null;
            } else if (!applicationInfo.metaData.containsKey(str)) {
                return null;
            } else {
                return Boolean.valueOf(applicationInfo.metaData.getBoolean(str));
            }
        } catch (NameNotFoundException e) {
            zzawy().zzazd().zzj("Failed to load metadata: Package name not found", e);
            return null;
        }
    }

    public final boolean zziz(String str) {
        return "1".equals(zzawv().zzam(str, "gaia_collection_enabled"));
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }

    public final boolean zzyp() {
        if (this.zzdvl == null) {
            synchronized (this) {
                if (this.zzdvl == null) {
                    ApplicationInfo applicationInfo = getContext().getApplicationInfo();
                    String zzamo = zzs.zzamo();
                    if (applicationInfo != null) {
                        String str = applicationInfo.processName;
                        this.zzdvl = Boolean.valueOf(str != null && str.equals(zzamo));
                    }
                    if (this.zzdvl == null) {
                        this.zzdvl = Boolean.TRUE;
                        zzawy().zzazd().log("My process not in the list of running processes");
                    }
                }
            }
        }
        return this.zzdvl.booleanValue();
    }
}
