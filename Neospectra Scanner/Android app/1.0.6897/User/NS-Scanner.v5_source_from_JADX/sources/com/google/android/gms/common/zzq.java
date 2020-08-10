package com.google.android.gms.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzbhf;

public class zzq {
    private static zzq zzflp;
    private final Context mContext;

    private zzq(Context context) {
        this.mContext = context.getApplicationContext();
    }

    static zzh zza(PackageInfo packageInfo, zzh... zzhArr) {
        if (packageInfo.signatures == null) {
            return null;
        }
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return null;
        }
        zzi zzi = new zzi(packageInfo.signatures[0].toByteArray());
        for (int i = 0; i < zzhArr.length; i++) {
            if (zzhArr[i].equals(zzi)) {
                return zzhArr[i];
            }
        }
        return null;
    }

    private static boolean zza(PackageInfo packageInfo, boolean z) {
        if (!(packageInfo == null || packageInfo.signatures == null)) {
            if (zza(packageInfo, z ? zzk.zzflf : new zzh[]{zzk.zzflf[0]}) != null) {
                return true;
            }
        }
        return false;
    }

    private static boolean zzb(PackageInfo packageInfo, boolean z) {
        if (packageInfo.signatures.length != 1) {
            Log.w("GoogleSignatureVerifier", "Package has more than one signature.");
            return false;
        }
        zzi zzi = new zzi(packageInfo.signatures[0].toByteArray());
        String str = packageInfo.packageName;
        boolean zzb = z ? zzg.zzb(str, zzi) : zzg.zza(str, zzi);
        if (!zzb) {
            StringBuilder sb = new StringBuilder(27);
            sb.append("Cert not in list. atk=");
            sb.append(z);
            Log.d("GoogleSignatureVerifier", sb.toString());
        }
        return zzb;
    }

    public static zzq zzci(Context context) {
        zzbq.checkNotNull(context);
        synchronized (zzq.class) {
            if (zzflp == null) {
                zzg.zzcg(context);
                zzflp = new zzq(context);
            }
        }
        return zzflp;
    }

    private final boolean zzfy(String str) {
        try {
            PackageInfo packageInfo = zzbhf.zzdb(this.mContext).getPackageInfo(str, 64);
            if (packageInfo == null) {
                return false;
            }
            if (zzp.zzch(this.mContext)) {
                return zzb(packageInfo, true);
            }
            boolean zzb = zzb(packageInfo, false);
            if (!zzb && zzb(packageInfo, true)) {
                Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
            }
            return zzb;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public final boolean zza(PackageInfo packageInfo) {
        if (packageInfo == null) {
            return false;
        }
        if (zza(packageInfo, false)) {
            return true;
        }
        if (zza(packageInfo, true)) {
            if (zzp.zzch(this.mContext)) {
                return true;
            }
            Log.w("GoogleSignatureVerifier", "Test-keys aren't accepted on this build.");
        }
        return false;
    }

    public final boolean zzbq(int i) {
        String[] packagesForUid = zzbhf.zzdb(this.mContext).getPackagesForUid(i);
        if (packagesForUid == null || packagesForUid.length == 0) {
            return false;
        }
        for (String zzfy : packagesForUid) {
            if (zzfy(zzfy)) {
                return true;
            }
        }
        return false;
    }
}
