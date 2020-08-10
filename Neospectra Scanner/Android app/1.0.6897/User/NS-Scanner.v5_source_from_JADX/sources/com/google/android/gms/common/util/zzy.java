package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.WorkSource;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.internal.zzbhf;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class zzy {
    private static final Method zzgew = zzamp();
    private static final Method zzgex = zzamq();
    private static final Method zzgey = zzamr();
    private static final Method zzgez = zzams();
    private static final Method zzgfa = zzamt();

    private static int zza(WorkSource workSource) {
        if (zzgey != null) {
            try {
                return ((Integer) zzgey.invoke(workSource, new Object[0])).intValue();
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return 0;
    }

    @Nullable
    private static String zza(WorkSource workSource, int i) {
        if (zzgfa != null) {
            try {
                return (String) zzgfa.invoke(workSource, new Object[]{Integer.valueOf(i)});
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
            }
        }
        return null;
    }

    @Nullable
    public static WorkSource zzaa(Context context, @Nullable String str) {
        if (context == null || context.getPackageManager() == null || str == null) {
            return null;
        }
        try {
            ApplicationInfo applicationInfo = zzbhf.zzdb(context).getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                return zze(applicationInfo.uid, str);
            }
            String str2 = "WorkSourceUtil";
            String str3 = "Could not get applicationInfo from package: ";
            String valueOf = String.valueOf(str);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return null;
        } catch (NameNotFoundException e) {
            String str4 = "WorkSourceUtil";
            String str5 = "Could not find package: ";
            String valueOf2 = String.valueOf(str);
            Log.e(str4, valueOf2.length() != 0 ? str5.concat(valueOf2) : new String(str5));
            return null;
        }
    }

    private static Method zzamp() {
        try {
            return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzamq() {
        if (zzq.zzamk()) {
            try {
                return WorkSource.class.getMethod("add", new Class[]{Integer.TYPE, String.class});
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static Method zzamr() {
        try {
            return WorkSource.class.getMethod("size", new Class[0]);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzams() {
        try {
            return WorkSource.class.getMethod("get", new Class[]{Integer.TYPE});
        } catch (Exception e) {
            return null;
        }
    }

    private static Method zzamt() {
        if (zzq.zzamk()) {
            try {
                return WorkSource.class.getMethod("getName", new Class[]{Integer.TYPE});
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static List<String> zzb(@Nullable WorkSource workSource) {
        int zza = workSource == null ? 0 : zza(workSource);
        if (zza == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < zza; i++) {
            String zza2 = zza(workSource, i);
            if (!zzu.zzgs(zza2)) {
                arrayList.add(zza2);
            }
        }
        return arrayList;
    }

    public static boolean zzcy(Context context) {
        return (context == null || context.getPackageManager() == null || zzbhf.zzdb(context).checkPermission("android.permission.UPDATE_DEVICE_STATS", context.getPackageName()) != 0) ? false : true;
    }

    private static WorkSource zze(int i, String str) {
        WorkSource workSource = new WorkSource();
        if (zzgex != null) {
            if (str == null) {
                str = "";
            }
            try {
                zzgex.invoke(workSource, new Object[]{Integer.valueOf(i), str});
                return workSource;
            } catch (Exception e) {
                Log.wtf("WorkSourceUtil", "Unable to assign blame through WorkSource", e);
                return workSource;
            }
        } else {
            if (zzgew != null) {
                zzgew.invoke(workSource, new Object[]{Integer.valueOf(i)});
            }
            return workSource;
        }
    }
}
