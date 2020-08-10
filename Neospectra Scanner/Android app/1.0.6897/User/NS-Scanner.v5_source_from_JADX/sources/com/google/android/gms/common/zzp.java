package com.google.android.gms.common;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import com.google.android.gms.common.util.zzx;
import com.google.android.gms.internal.zzbhf;
import java.util.concurrent.atomic.AtomicBoolean;

public class zzp {
    @Deprecated
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    @Deprecated
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE = 11910000;
    public static final String GOOGLE_PLAY_STORE_PACKAGE = "com.android.vending";
    private static boolean zzflj = false;
    private static boolean zzflk = false;
    private static boolean zzfll = false;
    private static boolean zzflm = false;
    static final AtomicBoolean zzfln = new AtomicBoolean();
    private static final AtomicBoolean zzflo = new AtomicBoolean();

    zzp() {
    }

    @Deprecated
    public static PendingIntent getErrorPendingIntent(int i, Context context, int i2) {
        return zzf.zzafy().getErrorResolutionPendingIntent(context, i, i2);
    }

    @Deprecated
    public static String getErrorString(int i) {
        return ConnectionResult.getStatusString(i);
    }

    public static Context getRemoteContext(Context context) {
        try {
            return context.createPackageContext("com.google.android.gms", 3);
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static Resources getRemoteResource(Context context) {
        try {
            return context.getPackageManager().getResourcesForApplication("com.google.android.gms");
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /* JADX INFO: used method not loaded: com.google.android.gms.common.zzq.zza(android.content.pm.PackageInfo, com.google.android.gms.common.zzh[]):null, types can be incorrect */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00c0, code lost:
        if (com.google.android.gms.common.zzq.zza(r6, r8) == null) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00cd, code lost:
        if (com.google.android.gms.common.zzq.zza(r6, com.google.android.gms.common.zzk.zzflf) == null) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00d8, code lost:
        if ((r6.versionCode / 1000) >= (GOOGLE_PLAY_SERVICES_VERSION_CODE / 1000)) goto L_0x0100;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00da, code lost:
        r0 = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        r1 = r6.versionCode;
        r3 = new java.lang.StringBuilder(77);
        r3.append("Google Play services out of date.  Requires ");
        r3.append(r0);
        r3.append(" but found ");
        r3.append(r1);
        android.util.Log.w("GooglePlayServicesUtil", r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00ff, code lost:
        return 2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0100, code lost:
        r8 = r6.applicationInfo;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0102, code lost:
        if (r8 != null) goto L_0x0114;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
        r8 = r0.getApplicationInfo("com.google.android.gms", 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x010b, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x010c, code lost:
        android.util.Log.wtf("GooglePlayServicesUtil", "Google Play services missing when getting application info.", r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x0113, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0116, code lost:
        if (r8.enabled != false) goto L_0x011a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0119, code lost:
        return 3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x011a, code lost:
        return 0;
     */
    @java.lang.Deprecated
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int isGooglePlayServicesAvailable(android.content.Context r8) {
        /*
            android.content.pm.PackageManager r0 = r8.getPackageManager()
            android.content.res.Resources r1 = r8.getResources()     // Catch:{ Throwable -> 0x000e }
            int r2 = com.google.android.gms.C1133R.string.common_google_play_services_unknown_issue     // Catch:{ Throwable -> 0x000e }
            r1.getString(r2)     // Catch:{ Throwable -> 0x000e }
            goto L_0x0016
        L_0x000e:
            r1 = move-exception
            java.lang.String r1 = "GooglePlayServicesUtil"
            java.lang.String r2 = "The Google Play services resources were not found. Check your project configuration to ensure that the resources are included."
            android.util.Log.e(r1, r2)
        L_0x0016:
            java.lang.String r1 = "com.google.android.gms"
            java.lang.String r2 = r8.getPackageName()
            boolean r1 = r1.equals(r2)
            if (r1 != 0) goto L_0x0076
            java.util.concurrent.atomic.AtomicBoolean r1 = zzflo
            boolean r1 = r1.get()
            if (r1 != 0) goto L_0x0076
            int r1 = com.google.android.gms.common.internal.zzbf.zzcq(r8)
            if (r1 != 0) goto L_0x0038
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r0 = "A required meta-data tag in your app's AndroidManifest.xml does not exist.  You must have the following declaration within the <application> element:     <meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />"
            r8.<init>(r0)
            throw r8
        L_0x0038:
            int r2 = GOOGLE_PLAY_SERVICES_VERSION_CODE
            if (r1 == r2) goto L_0x0076
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            int r0 = GOOGLE_PLAY_SERVICES_VERSION_CODE
            java.lang.String r2 = "com.google.android.gms.version"
            java.lang.String r3 = java.lang.String.valueOf(r2)
            int r3 = r3.length()
            int r3 = r3 + 290
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>(r3)
            java.lang.String r3 = "The meta-data tag in your app's AndroidManifest.xml does not have the right value.  Expected "
            r4.append(r3)
            r4.append(r0)
            java.lang.String r0 = " but found "
            r4.append(r0)
            r4.append(r1)
            java.lang.String r0 = ".  You must have the following declaration within the <application> element:     <meta-data android:name=\""
            r4.append(r0)
            r4.append(r2)
            java.lang.String r0 = "\" android:value=\"@integer/google_play_services_version\" />"
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            r8.<init>(r0)
            throw r8
        L_0x0076:
            boolean r1 = com.google.android.gms.common.util.zzi.zzct(r8)
            r2 = 0
            r3 = 1
            if (r1 != 0) goto L_0x0086
            boolean r1 = com.google.android.gms.common.util.zzi.zzcv(r8)
            if (r1 != 0) goto L_0x0086
            r1 = 1
            goto L_0x0087
        L_0x0086:
            r1 = 0
        L_0x0087:
            r4 = 0
            r5 = 9
            if (r1 == 0) goto L_0x009e
            java.lang.String r4 = "com.android.vending"
            r6 = 8256(0x2040, float:1.1569E-41)
            android.content.pm.PackageInfo r4 = r0.getPackageInfo(r4, r6)     // Catch:{ NameNotFoundException -> 0x0095 }
            goto L_0x009e
        L_0x0095:
            r8 = move-exception
            java.lang.String r8 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play Store is missing."
        L_0x009a:
            android.util.Log.w(r8, r0)
            return r5
        L_0x009e:
            java.lang.String r6 = "com.google.android.gms"
            r7 = 64
            android.content.pm.PackageInfo r6 = r0.getPackageInfo(r6, r7)     // Catch:{ NameNotFoundException -> 0x011b }
            com.google.android.gms.common.zzq.zzci(r8)
            if (r1 == 0) goto L_0x00c7
            com.google.android.gms.common.zzh[] r8 = com.google.android.gms.common.zzk.zzflf
            com.google.android.gms.common.zzh r8 = com.google.android.gms.common.zzq.zza(r4, r8)
            if (r8 != 0) goto L_0x00b8
            java.lang.String r8 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play Store signature invalid."
            goto L_0x009a
        L_0x00b8:
            com.google.android.gms.common.zzh[] r1 = new com.google.android.gms.common.zzh[r3]
            r1[r2] = r8
            com.google.android.gms.common.zzh r8 = com.google.android.gms.common.zzq.zza(r6, r1)
            if (r8 != 0) goto L_0x00d0
        L_0x00c2:
            java.lang.String r8 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play services signature invalid."
            goto L_0x009a
        L_0x00c7:
            com.google.android.gms.common.zzh[] r8 = com.google.android.gms.common.zzk.zzflf
            com.google.android.gms.common.zzh r8 = com.google.android.gms.common.zzq.zza(r6, r8)
            if (r8 != 0) goto L_0x00d0
            goto L_0x00c2
        L_0x00d0:
            int r8 = GOOGLE_PLAY_SERVICES_VERSION_CODE
            int r8 = r8 / 1000
            int r1 = r6.versionCode
            int r1 = r1 / 1000
            if (r1 >= r8) goto L_0x0100
            java.lang.String r8 = "GooglePlayServicesUtil"
            int r0 = GOOGLE_PLAY_SERVICES_VERSION_CODE
            int r1 = r6.versionCode
            r2 = 77
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r2)
            java.lang.String r2 = "Google Play services out of date.  Requires "
            r3.append(r2)
            r3.append(r0)
            java.lang.String r0 = " but found "
            r3.append(r0)
            r3.append(r1)
            java.lang.String r0 = r3.toString()
            android.util.Log.w(r8, r0)
            r8 = 2
            return r8
        L_0x0100:
            android.content.pm.ApplicationInfo r8 = r6.applicationInfo
            if (r8 != 0) goto L_0x0114
            java.lang.String r8 = "com.google.android.gms"
            android.content.pm.ApplicationInfo r8 = r0.getApplicationInfo(r8, r2)     // Catch:{ NameNotFoundException -> 0x010b }
            goto L_0x0114
        L_0x010b:
            r8 = move-exception
            java.lang.String r0 = "GooglePlayServicesUtil"
            java.lang.String r1 = "Google Play services missing when getting application info."
            android.util.Log.wtf(r0, r1, r8)
            return r3
        L_0x0114:
            boolean r8 = r8.enabled
            if (r8 != 0) goto L_0x011a
            r8 = 3
            return r8
        L_0x011a:
            return r2
        L_0x011b:
            r8 = move-exception
            java.lang.String r8 = "GooglePlayServicesUtil"
            java.lang.String r0 = "Google Play services is missing."
            android.util.Log.w(r8, r0)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zzp.isGooglePlayServicesAvailable(android.content.Context):int");
    }

    @Deprecated
    public static boolean isUserRecoverableError(int i) {
        if (i != 9) {
            switch (i) {
                case 1:
                case 2:
                case 3:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    @TargetApi(19)
    @Deprecated
    public static boolean zzb(Context context, int i, String str) {
        return zzx.zzb(context, i, str);
    }

    @Deprecated
    public static void zzbp(Context context) throws GooglePlayServicesRepairableException, GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = zzf.zzafy().isGooglePlayServicesAvailable(context);
        if (isGooglePlayServicesAvailable != 0) {
            zzf.zzafy();
            Intent zza = zzf.zza(context, isGooglePlayServicesAvailable, "e");
            StringBuilder sb = new StringBuilder(57);
            sb.append("GooglePlayServices not available due to error ");
            sb.append(isGooglePlayServicesAvailable);
            Log.e("GooglePlayServicesUtil", sb.toString());
            if (zza == null) {
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
            }
            throw new GooglePlayServicesRepairableException(isGooglePlayServicesAvailable, "Google Play Services not available", zza);
        }
    }

    @Deprecated
    public static void zzce(Context context) {
        if (!zzfln.getAndSet(true)) {
            try {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager != null) {
                    notificationManager.cancel(10436);
                }
            } catch (SecurityException e) {
            }
        }
    }

    @Deprecated
    public static int zzcf(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.w("GooglePlayServicesUtil", "Google Play services is missing.");
            return 0;
        }
    }

    public static boolean zzch(Context context) {
        if (!zzflm) {
            try {
                PackageInfo packageInfo = zzbhf.zzdb(context).getPackageInfo("com.google.android.gms", 64);
                if (packageInfo != null) {
                    zzq.zzci(context);
                    if (zzq.zza(packageInfo, zzk.zzflf[1]) != null) {
                        zzfll = true;
                        zzflm = true;
                    }
                }
                zzfll = false;
            } catch (NameNotFoundException e) {
                Log.w("GooglePlayServicesUtil", "Cannot find Google Play services package name.", e);
            } catch (Throwable th) {
                zzflm = true;
                throw th;
            }
            zzflm = true;
        }
        return zzfll || !"user".equals(Build.TYPE);
    }

    @Deprecated
    public static boolean zze(Context context, int i) {
        if (i == 18) {
            return true;
        }
        if (i == 1) {
            return zzv(context, "com.google.android.gms");
        }
        return false;
    }

    @Deprecated
    public static boolean zzf(Context context, int i) {
        return zzx.zzf(context, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0075 A[RETURN] */
    @android.annotation.TargetApi(21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean zzv(android.content.Context r5, java.lang.String r6) {
        /*
            java.lang.String r0 = "com.google.android.gms"
            boolean r0 = r6.equals(r0)
            boolean r1 = com.google.android.gms.common.util.zzq.zzamn()
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0037
            android.content.pm.PackageManager r1 = r5.getPackageManager()     // Catch:{ Exception -> 0x0035 }
            android.content.pm.PackageInstaller r1 = r1.getPackageInstaller()     // Catch:{ Exception -> 0x0035 }
            java.util.List r1 = r1.getAllSessions()     // Catch:{ Exception -> 0x0035 }
            java.util.Iterator r1 = r1.iterator()
        L_0x001e:
            boolean r4 = r1.hasNext()
            if (r4 == 0) goto L_0x0037
            java.lang.Object r4 = r1.next()
            android.content.pm.PackageInstaller$SessionInfo r4 = (android.content.pm.PackageInstaller.SessionInfo) r4
            java.lang.String r4 = r4.getAppPackageName()
            boolean r4 = r6.equals(r4)
            if (r4 == 0) goto L_0x001e
            return r2
        L_0x0035:
            r5 = move-exception
            return r3
        L_0x0037:
            android.content.pm.PackageManager r1 = r5.getPackageManager()
            r4 = 8192(0x2000, float:1.14794E-41)
            android.content.pm.ApplicationInfo r6 = r1.getApplicationInfo(r6, r4)     // Catch:{ NameNotFoundException -> 0x0077 }
            if (r0 == 0) goto L_0x0046
            boolean r5 = r6.enabled     // Catch:{ NameNotFoundException -> 0x0077 }
            return r5
        L_0x0046:
            boolean r6 = r6.enabled     // Catch:{ NameNotFoundException -> 0x0077 }
            if (r6 == 0) goto L_0x0076
            boolean r6 = com.google.android.gms.common.util.zzq.zzamk()     // Catch:{ NameNotFoundException -> 0x0077 }
            if (r6 == 0) goto L_0x0072
            java.lang.String r6 = "user"
            java.lang.Object r6 = r5.getSystemService(r6)     // Catch:{ NameNotFoundException -> 0x0077 }
            android.os.UserManager r6 = (android.os.UserManager) r6     // Catch:{ NameNotFoundException -> 0x0077 }
            java.lang.String r5 = r5.getPackageName()     // Catch:{ NameNotFoundException -> 0x0077 }
            android.os.Bundle r5 = r6.getApplicationRestrictions(r5)     // Catch:{ NameNotFoundException -> 0x0077 }
            if (r5 == 0) goto L_0x0072
            java.lang.String r6 = "true"
            java.lang.String r0 = "restricted_profile"
            java.lang.String r5 = r5.getString(r0)     // Catch:{ NameNotFoundException -> 0x0077 }
            boolean r5 = r6.equals(r5)     // Catch:{ NameNotFoundException -> 0x0077 }
            if (r5 == 0) goto L_0x0072
            r5 = 1
            goto L_0x0073
        L_0x0072:
            r5 = 0
        L_0x0073:
            if (r5 != 0) goto L_0x0076
            return r2
        L_0x0076:
            return r3
        L_0x0077:
            r5 = move-exception
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.zzp.zzv(android.content.Context, java.lang.String):boolean");
    }
}
