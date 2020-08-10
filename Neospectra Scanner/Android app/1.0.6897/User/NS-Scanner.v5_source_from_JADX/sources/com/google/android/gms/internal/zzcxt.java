package com.google.android.gms.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzu;
import com.google.android.gms.common.util.zzy;
import java.util.HashMap;
import java.util.Map;

public final class zzcxt {
    private static boolean DEBUG = false;
    private static String TAG = "WakeLock";
    private static String zzkcd = "*gcore*:";
    private final Context mContext;
    private final String zzgdn;
    private final String zzgdp;
    private final WakeLock zzkce;
    private WorkSource zzkcf;
    private final int zzkcg;
    private final String zzkch;
    private boolean zzkci;
    private final Map<String, Integer[]> zzkcj;
    private int zzkck;

    public zzcxt(Context context, int i, String str) {
        this(context, 1, str, null, context == null ? null : context.getPackageName());
    }

    @SuppressLint({"UnwrappedWakeLock"})
    private zzcxt(Context context, int i, String str, String str2, String str3) {
        this(context, 1, str, null, str3, null);
    }

    @SuppressLint({"UnwrappedWakeLock"})
    private zzcxt(Context context, int i, String str, String str2, String str3, String str4) {
        this.zzkci = true;
        this.zzkcj = new HashMap();
        zzbq.zzh(str, "Wake lock name can NOT be empty");
        this.zzkcg = i;
        this.zzkch = null;
        this.zzgdp = null;
        this.mContext = context.getApplicationContext();
        if (!"com.google.android.gms".equals(context.getPackageName())) {
            String valueOf = String.valueOf(zzkcd);
            String valueOf2 = String.valueOf(str);
            this.zzgdn = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        } else {
            this.zzgdn = str;
        }
        this.zzkce = ((PowerManager) context.getSystemService("power")).newWakeLock(i, str);
        if (zzy.zzcy(this.mContext)) {
            if (zzu.zzgs(str3)) {
                str3 = context.getPackageName();
            }
            this.zzkcf = zzy.zzaa(context, str3);
            WorkSource workSource = this.zzkcf;
            if (workSource != null && zzy.zzcy(this.mContext)) {
                if (this.zzkcf != null) {
                    this.zzkcf.add(workSource);
                } else {
                    this.zzkcf = workSource;
                }
                try {
                    this.zzkce.setWorkSource(this.zzkcf);
                } catch (IllegalArgumentException e) {
                    Log.wtf(TAG, e.toString());
                }
            }
        }
    }

    private final String zzkz(String str) {
        if (!this.zzkci) {
            return this.zzkch;
        }
        if (!TextUtils.isEmpty(null)) {
            return null;
        }
        return this.zzkch;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        if (r13 == false) goto L_0x004f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0055, code lost:
        if (r11.zzkck == 0) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        com.google.android.gms.common.stats.zze.zzamf();
        com.google.android.gms.common.stats.zze.zza(r11.mContext, com.google.android.gms.common.stats.zzc.zza(r11.zzkce, r4), 7, r11.zzgdn, r4, null, r11.zzkcg, com.google.android.gms.common.util.zzy.zzb(r11.zzkcf), 1000);
        r11.zzkck++;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void acquire(long r12) {
        /*
            r11 = this;
            r12 = 0
            java.lang.String r4 = r11.zzkz(r12)
            monitor-enter(r11)
            java.util.Map<java.lang.String, java.lang.Integer[]> r12 = r11.zzkcj     // Catch:{ all -> 0x0081 }
            boolean r12 = r12.isEmpty()     // Catch:{ all -> 0x0081 }
            r13 = 0
            if (r12 == 0) goto L_0x0013
            int r12 = r11.zzkck     // Catch:{ all -> 0x0081 }
            if (r12 <= 0) goto L_0x0022
        L_0x0013:
            android.os.PowerManager$WakeLock r12 = r11.zzkce     // Catch:{ all -> 0x0081 }
            boolean r12 = r12.isHeld()     // Catch:{ all -> 0x0081 }
            if (r12 != 0) goto L_0x0022
            java.util.Map<java.lang.String, java.lang.Integer[]> r12 = r11.zzkcj     // Catch:{ all -> 0x0081 }
            r12.clear()     // Catch:{ all -> 0x0081 }
            r11.zzkck = r13     // Catch:{ all -> 0x0081 }
        L_0x0022:
            boolean r12 = r11.zzkci     // Catch:{ all -> 0x0081 }
            r10 = 1
            if (r12 == 0) goto L_0x004f
            java.util.Map<java.lang.String, java.lang.Integer[]> r12 = r11.zzkcj     // Catch:{ all -> 0x0081 }
            java.lang.Object r12 = r12.get(r4)     // Catch:{ all -> 0x0081 }
            java.lang.Integer[] r12 = (java.lang.Integer[]) r12     // Catch:{ all -> 0x0081 }
            if (r12 != 0) goto L_0x0040
            java.util.Map<java.lang.String, java.lang.Integer[]> r12 = r11.zzkcj     // Catch:{ all -> 0x0081 }
            java.lang.Integer[] r0 = new java.lang.Integer[r10]     // Catch:{ all -> 0x0081 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r10)     // Catch:{ all -> 0x0081 }
            r0[r13] = r1     // Catch:{ all -> 0x0081 }
            r12.put(r4, r0)     // Catch:{ all -> 0x0081 }
            r13 = 1
            goto L_0x004d
        L_0x0040:
            r0 = r12[r13]     // Catch:{ all -> 0x0081 }
            int r0 = r0.intValue()     // Catch:{ all -> 0x0081 }
            int r0 = r0 + r10
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x0081 }
            r12[r13] = r0     // Catch:{ all -> 0x0081 }
        L_0x004d:
            if (r13 != 0) goto L_0x0057
        L_0x004f:
            boolean r12 = r11.zzkci     // Catch:{ all -> 0x0081 }
            if (r12 != 0) goto L_0x0078
            int r12 = r11.zzkck     // Catch:{ all -> 0x0081 }
            if (r12 != 0) goto L_0x0078
        L_0x0057:
            com.google.android.gms.common.stats.zze.zzamf()     // Catch:{ all -> 0x0081 }
            android.content.Context r0 = r11.mContext     // Catch:{ all -> 0x0081 }
            android.os.PowerManager$WakeLock r12 = r11.zzkce     // Catch:{ all -> 0x0081 }
            java.lang.String r1 = com.google.android.gms.common.stats.zzc.zza(r12, r4)     // Catch:{ all -> 0x0081 }
            r2 = 7
            java.lang.String r3 = r11.zzgdn     // Catch:{ all -> 0x0081 }
            r5 = 0
            int r6 = r11.zzkcg     // Catch:{ all -> 0x0081 }
            android.os.WorkSource r12 = r11.zzkcf     // Catch:{ all -> 0x0081 }
            java.util.List r7 = com.google.android.gms.common.util.zzy.zzb(r12)     // Catch:{ all -> 0x0081 }
            r8 = 1000(0x3e8, double:4.94E-321)
            com.google.android.gms.common.stats.zze.zza(r0, r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0081 }
            int r12 = r11.zzkck     // Catch:{ all -> 0x0081 }
            int r12 = r12 + r10
            r11.zzkck = r12     // Catch:{ all -> 0x0081 }
        L_0x0078:
            monitor-exit(r11)     // Catch:{ all -> 0x0081 }
            android.os.PowerManager$WakeLock r12 = r11.zzkce
            r0 = 1000(0x3e8, double:4.94E-321)
            r12.acquire(r0)
            return
        L_0x0081:
            r12 = move-exception
            monitor-exit(r11)     // Catch:{ all -> 0x0081 }
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcxt.acquire(long):void");
    }

    public final boolean isHeld() {
        return this.zzkce.isHeld();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        if (r1 == false) goto L_0x0035;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        if (r10.zzkck == 1) goto L_0x003d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        com.google.android.gms.common.stats.zze.zzamf();
        com.google.android.gms.common.stats.zze.zza(r10.mContext, com.google.android.gms.common.stats.zzc.zza(r10.zzkce, r5), 8, r10.zzgdn, r5, null, r10.zzkcg, com.google.android.gms.common.util.zzy.zzb(r10.zzkcf));
        r10.zzkck--;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void release() {
        /*
            r10 = this;
            r0 = 0
            java.lang.String r5 = r10.zzkz(r0)
            monitor-enter(r10)
            boolean r0 = r10.zzkci     // Catch:{ all -> 0x0066 }
            r9 = 1
            if (r0 == 0) goto L_0x0035
            java.util.Map<java.lang.String, java.lang.Integer[]> r0 = r10.zzkcj     // Catch:{ all -> 0x0066 }
            java.lang.Object r0 = r0.get(r5)     // Catch:{ all -> 0x0066 }
            java.lang.Integer[] r0 = (java.lang.Integer[]) r0     // Catch:{ all -> 0x0066 }
            r1 = 0
            if (r0 != 0) goto L_0x0017
            goto L_0x0033
        L_0x0017:
            r2 = r0[r1]     // Catch:{ all -> 0x0066 }
            int r2 = r2.intValue()     // Catch:{ all -> 0x0066 }
            if (r2 != r9) goto L_0x0026
            java.util.Map<java.lang.String, java.lang.Integer[]> r0 = r10.zzkcj     // Catch:{ all -> 0x0066 }
            r0.remove(r5)     // Catch:{ all -> 0x0066 }
            r1 = 1
            goto L_0x0033
        L_0x0026:
            r2 = r0[r1]     // Catch:{ all -> 0x0066 }
            int r2 = r2.intValue()     // Catch:{ all -> 0x0066 }
            int r2 = r2 - r9
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ all -> 0x0066 }
            r0[r1] = r2     // Catch:{ all -> 0x0066 }
        L_0x0033:
            if (r1 != 0) goto L_0x003d
        L_0x0035:
            boolean r0 = r10.zzkci     // Catch:{ all -> 0x0066 }
            if (r0 != 0) goto L_0x005d
            int r0 = r10.zzkck     // Catch:{ all -> 0x0066 }
            if (r0 != r9) goto L_0x005d
        L_0x003d:
            com.google.android.gms.common.stats.zze.zzamf()     // Catch:{ all -> 0x0066 }
            android.content.Context r1 = r10.mContext     // Catch:{ all -> 0x0066 }
            android.os.PowerManager$WakeLock r0 = r10.zzkce     // Catch:{ all -> 0x0066 }
            java.lang.String r2 = com.google.android.gms.common.stats.zzc.zza(r0, r5)     // Catch:{ all -> 0x0066 }
            r3 = 8
            java.lang.String r4 = r10.zzgdn     // Catch:{ all -> 0x0066 }
            r6 = 0
            int r7 = r10.zzkcg     // Catch:{ all -> 0x0066 }
            android.os.WorkSource r0 = r10.zzkcf     // Catch:{ all -> 0x0066 }
            java.util.List r8 = com.google.android.gms.common.util.zzy.zzb(r0)     // Catch:{ all -> 0x0066 }
            com.google.android.gms.common.stats.zze.zza(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0066 }
            int r0 = r10.zzkck     // Catch:{ all -> 0x0066 }
            int r0 = r0 - r9
            r10.zzkck = r0     // Catch:{ all -> 0x0066 }
        L_0x005d:
            monitor-exit(r10)     // Catch:{ all -> 0x0066 }
            android.os.PowerManager$WakeLock r0 = r10.zzkce     // Catch:{ RuntimeException -> 0x0064 }
            r0.release()     // Catch:{ RuntimeException -> 0x0064 }
            return
        L_0x0064:
            r0 = move-exception
            return
        L_0x0066:
            r0 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0066 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzcxt.release():void");
    }

    public final void setReferenceCounted(boolean z) {
        this.zzkce.setReferenceCounted(false);
        this.zzkci = false;
    }
}
