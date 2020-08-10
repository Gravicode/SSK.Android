package com.google.android.gms.internal;

import android.content.Context;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.util.zzd;
import com.google.firebase.iid.FirebaseInstanceId;
import java.math.BigInteger;
import java.util.Locale;

public final class zzchh extends zzcjl {
    private String mAppId;
    private String zzcwz;
    private String zzdqz;
    private String zzdra;
    private String zzixc;
    private long zzixg;
    private int zzjbk;
    private long zzjbl;
    private int zzjbm;

    zzchh(zzcim zzcim) {
        super(zzcim);
    }

    @WorkerThread
    private final String zzaxd() {
        zzve();
        try {
            return FirebaseInstanceId.getInstance().getId();
        } catch (IllegalStateException e) {
            zzawy().zzazf().log("Failed to retrieve Firebase Instance Id");
            return null;
        }
    }

    /* access modifiers changed from: 0000 */
    public final String getAppId() {
        zzxf();
        return this.mAppId;
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    /* access modifiers changed from: 0000 */
    public final String getGmpAppId() {
        zzxf();
        return this.zzcwz;
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
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0145 A[Catch:{ IllegalStateException -> 0x015d }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x014b A[Catch:{ IllegalStateException -> 0x015d }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0180  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzayy() {
        /*
            r10 = this;
            java.lang.String r0 = "unknown"
            java.lang.String r1 = "Unknown"
            java.lang.String r2 = "Unknown"
            android.content.Context r3 = r10.getContext()
            java.lang.String r3 = r3.getPackageName()
            android.content.Context r4 = r10.getContext()
            android.content.pm.PackageManager r4 = r4.getPackageManager()
            r5 = 0
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r4 != 0) goto L_0x002d
            com.google.android.gms.internal.zzchm r4 = r10.zzawy()
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()
            java.lang.String r7 = "PackageManager is null, app identity information might be inaccurate. appId"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r3)
            r4.zzj(r7, r8)
            goto L_0x008f
        L_0x002d:
            java.lang.String r7 = r4.getInstallerPackageName(r3)     // Catch:{ IllegalArgumentException -> 0x0033 }
            r0 = r7
            goto L_0x0045
        L_0x0033:
            r7 = move-exception
            com.google.android.gms.internal.zzchm r7 = r10.zzawy()
            com.google.android.gms.internal.zzcho r7 = r7.zzazd()
            java.lang.String r8 = "Error retrieving app installer package name. appId"
            java.lang.Object r9 = com.google.android.gms.internal.zzchm.zzjk(r3)
            r7.zzj(r8, r9)
        L_0x0045:
            if (r0 != 0) goto L_0x004a
            java.lang.String r0 = "manual_install"
            goto L_0x0054
        L_0x004a:
            java.lang.String r7 = "com.android.vending"
            boolean r7 = r7.equals(r0)
            if (r7 == 0) goto L_0x0054
            java.lang.String r0 = ""
        L_0x0054:
            android.content.Context r7 = r10.getContext()     // Catch:{ NameNotFoundException -> 0x007d }
            java.lang.String r7 = r7.getPackageName()     // Catch:{ NameNotFoundException -> 0x007d }
            android.content.pm.PackageInfo r7 = r4.getPackageInfo(r7, r5)     // Catch:{ NameNotFoundException -> 0x007d }
            if (r7 == 0) goto L_0x008f
            android.content.pm.ApplicationInfo r8 = r7.applicationInfo     // Catch:{ NameNotFoundException -> 0x007d }
            java.lang.CharSequence r4 = r4.getApplicationLabel(r8)     // Catch:{ NameNotFoundException -> 0x007d }
            boolean r8 = android.text.TextUtils.isEmpty(r4)     // Catch:{ NameNotFoundException -> 0x007d }
            if (r8 != 0) goto L_0x0073
            java.lang.String r4 = r4.toString()     // Catch:{ NameNotFoundException -> 0x007d }
            r2 = r4
        L_0x0073:
            java.lang.String r4 = r7.versionName     // Catch:{ NameNotFoundException -> 0x007d }
            int r1 = r7.versionCode     // Catch:{ NameNotFoundException -> 0x007a }
            r6 = r1
            r1 = r4
            goto L_0x008f
        L_0x007a:
            r1 = move-exception
            r1 = r4
            goto L_0x007e
        L_0x007d:
            r4 = move-exception
        L_0x007e:
            com.google.android.gms.internal.zzchm r4 = r10.zzawy()
            com.google.android.gms.internal.zzcho r4 = r4.zzazd()
            java.lang.String r7 = "Error retrieving package info. appId, appName"
            java.lang.Object r8 = com.google.android.gms.internal.zzchm.zzjk(r3)
            r4.zze(r7, r8, r2)
        L_0x008f:
            r10.mAppId = r3
            r10.zzixc = r0
            r10.zzdra = r1
            r10.zzjbk = r6
            r10.zzdqz = r2
            r0 = 0
            r10.zzjbl = r0
            android.content.Context r2 = r10.getContext()
            com.google.android.gms.common.api.Status r2 = com.google.android.gms.common.api.internal.zzbz.zzck(r2)
            r4 = 1
            if (r2 == 0) goto L_0x00b0
            boolean r6 = r2.isSuccess()
            if (r6 == 0) goto L_0x00b0
            r6 = 1
            goto L_0x00b1
        L_0x00b0:
            r6 = 0
        L_0x00b1:
            if (r6 != 0) goto L_0x00dc
            if (r2 != 0) goto L_0x00c3
            com.google.android.gms.internal.zzchm r2 = r10.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazd()
            java.lang.String r7 = "GoogleService failed to initialize (no status)"
            r2.log(r7)
            goto L_0x00dc
        L_0x00c3:
            com.google.android.gms.internal.zzchm r7 = r10.zzawy()
            com.google.android.gms.internal.zzcho r7 = r7.zzazd()
            java.lang.String r8 = "GoogleService failed to initialize, status"
            int r9 = r2.getStatusCode()
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.String r2 = r2.getStatusMessage()
            r7.zze(r8, r9, r2)
        L_0x00dc:
            if (r6 == 0) goto L_0x0134
            com.google.android.gms.internal.zzcgn r2 = r10.zzaxa()
            java.lang.String r6 = "firebase_analytics_collection_enabled"
            java.lang.Boolean r2 = r2.zziy(r6)
            com.google.android.gms.internal.zzcgn r6 = r10.zzaxa()
            boolean r6 = r6.zzaya()
            if (r6 == 0) goto L_0x0100
            com.google.android.gms.internal.zzchm r2 = r10.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazh()
            java.lang.String r4 = "Collection disabled with firebase_analytics_collection_deactivated=1"
        L_0x00fc:
            r2.log(r4)
            goto L_0x0134
        L_0x0100:
            if (r2 == 0) goto L_0x0113
            boolean r6 = r2.booleanValue()
            if (r6 != 0) goto L_0x0113
            com.google.android.gms.internal.zzchm r2 = r10.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazh()
            java.lang.String r4 = "Collection disabled with firebase_analytics_collection_enabled=0"
            goto L_0x00fc
        L_0x0113:
            if (r2 != 0) goto L_0x0126
            boolean r2 = com.google.android.gms.common.api.internal.zzbz.zzaji()
            if (r2 == 0) goto L_0x0126
            com.google.android.gms.internal.zzchm r2 = r10.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazh()
            java.lang.String r4 = "Collection disabled with google_app_measurement_enable=0"
            goto L_0x00fc
        L_0x0126:
            com.google.android.gms.internal.zzchm r2 = r10.zzawy()
            com.google.android.gms.internal.zzcho r2 = r2.zzazj()
            java.lang.String r6 = "Collection enabled"
            r2.log(r6)
            goto L_0x0135
        L_0x0134:
            r4 = 0
        L_0x0135:
            java.lang.String r2 = ""
            r10.zzcwz = r2
            r10.zzixg = r0
            java.lang.String r0 = com.google.android.gms.common.api.internal.zzbz.zzajh()     // Catch:{ IllegalStateException -> 0x015d }
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch:{ IllegalStateException -> 0x015d }
            if (r1 == 0) goto L_0x0147
            java.lang.String r0 = ""
        L_0x0147:
            r10.zzcwz = r0     // Catch:{ IllegalStateException -> 0x015d }
            if (r4 == 0) goto L_0x016f
            com.google.android.gms.internal.zzchm r0 = r10.zzawy()     // Catch:{ IllegalStateException -> 0x015d }
            com.google.android.gms.internal.zzcho r0 = r0.zzazj()     // Catch:{ IllegalStateException -> 0x015d }
            java.lang.String r1 = "App package, google app id"
            java.lang.String r2 = r10.mAppId     // Catch:{ IllegalStateException -> 0x015d }
            java.lang.String r4 = r10.zzcwz     // Catch:{ IllegalStateException -> 0x015d }
            r0.zze(r1, r2, r4)     // Catch:{ IllegalStateException -> 0x015d }
            goto L_0x016f
        L_0x015d:
            r0 = move-exception
            com.google.android.gms.internal.zzchm r1 = r10.zzawy()
            com.google.android.gms.internal.zzcho r1 = r1.zzazd()
            java.lang.String r2 = "getGoogleAppId or isMeasurementEnabled failed with exception. appId"
            java.lang.Object r3 = com.google.android.gms.internal.zzchm.zzjk(r3)
            r1.zze(r2, r3, r0)
        L_0x016f:
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 16
            if (r0 < r1) goto L_0x0180
            android.content.Context r0 = r10.getContext()
            boolean r0 = com.google.android.gms.internal.zzbhd.zzcz(r0)
            r10.zzjbm = r0
            return
        L_0x0180:
            r10.zzjbm = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzchh.zzayy():void");
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final String zzayz() {
        byte[] bArr = new byte[16];
        zzawu().zzbaz().nextBytes(bArr);
        return String.format(Locale.US, "%032x", new Object[]{new BigInteger(1, bArr)});
    }

    /* access modifiers changed from: 0000 */
    public final int zzaza() {
        zzxf();
        return this.zzjbk;
    }

    /* access modifiers changed from: 0000 */
    public final int zzazb() {
        zzxf();
        return this.zzjbm;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final zzcgi zzjg(String str) {
        zzve();
        String appId = getAppId();
        String gmpAppId = getGmpAppId();
        zzxf();
        String str2 = this.zzdra;
        long zzaza = (long) zzaza();
        zzxf();
        String str3 = this.zzixc;
        zzxf();
        zzve();
        if (this.zzjbl == 0) {
            this.zzjbl = this.zziwf.zzawu().zzaf(getContext(), getContext().getPackageName());
        }
        long j = this.zzjbl;
        boolean isEnabled = this.zziwf.isEnabled();
        boolean z = true;
        boolean z2 = !zzawz().zzjdj;
        String zzaxd = zzaxd();
        zzxf();
        long zzbaf = this.zziwf.zzbaf();
        int zzazb = zzazb();
        Boolean zziy = zzaxa().zziy("google_analytics_adid_collection_enabled");
        if (zziy != null && !zziy.booleanValue()) {
            z = false;
        }
        zzcgi zzcgi = new zzcgi(appId, gmpAppId, str2, zzaza, str3, 11910, j, str, isEnabled, z2, zzaxd, 0, zzbaf, zzazb, Boolean.valueOf(z).booleanValue());
        return zzcgi;
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }
}
