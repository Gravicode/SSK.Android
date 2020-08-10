package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.util.zzq;
import com.google.firebase.FirebaseApp;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.poi.hssf.record.formula.IntersectionPtg;

final class zzu {
    private final Context zzair;
    private String zzct;
    private String zznzk;
    private int zznzl;
    private int zznzm = 0;

    public zzu(Context context) {
        this.zzair = context;
    }

    public static String zzb(KeyPair keyPair) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            digest[0] = (byte) ((digest[0] & IntersectionPtg.sid) + 112);
            return Base64.encodeToString(digest, 0, 8, 11);
        } catch (NoSuchAlgorithmException e) {
            Log.w("FirebaseInstanceId", "Unexpected error, device missing required algorithms");
            return null;
        }
    }

    private final synchronized void zzcjj() {
        PackageInfo zzoa = zzoa(this.zzair.getPackageName());
        if (zzoa != null) {
            this.zznzk = Integer.toString(zzoa.versionCode);
            this.zzct = zzoa.versionName;
        }
    }

    public static String zzf(FirebaseApp firebaseApp) {
        String gcmSenderId = firebaseApp.getOptions().getGcmSenderId();
        if (gcmSenderId != null) {
            return gcmSenderId;
        }
        String applicationId = firebaseApp.getOptions().getApplicationId();
        if (!applicationId.startsWith("1:")) {
            return applicationId;
        }
        String[] split = applicationId.split(":");
        if (split.length < 2) {
            return null;
        }
        String str = split[1];
        if (str.isEmpty()) {
            return null;
        }
        return str;
    }

    private final PackageInfo zzoa(String str) {
        try {
            return this.zzair.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException e) {
            String valueOf = String.valueOf(e);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
            sb.append("Failed to find package ");
            sb.append(valueOf);
            Log.w("FirebaseInstanceId", sb.toString());
            return null;
        }
    }

    public final synchronized int zzcjf() {
        if (this.zznzm != 0) {
            return this.zznzm;
        }
        PackageManager packageManager = this.zzair.getPackageManager();
        if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
            Log.e("FirebaseInstanceId", "Google Play services missing or without correct permission.");
            return 0;
        }
        if (!zzq.isAtLeastO()) {
            Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
            intent.setPackage("com.google.android.gms");
            List queryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (queryIntentServices != null && queryIntentServices.size() > 0) {
                this.zznzm = 1;
                return this.zznzm;
            }
        }
        Intent intent2 = new Intent("com.google.iid.TOKEN_REQUEST");
        intent2.setPackage("com.google.android.gms");
        List queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent2, 0);
        if (queryBroadcastReceivers == null || queryBroadcastReceivers.size() <= 0) {
            Log.w("FirebaseInstanceId", "Failed to resolve IID implementation package, falling back");
            if (zzq.isAtLeastO()) {
                this.zznzm = 2;
            } else {
                this.zznzm = 1;
            }
            return this.zznzm;
        }
        this.zznzm = 2;
        return this.zznzm;
    }

    public final synchronized String zzcjg() {
        if (this.zznzk == null) {
            zzcjj();
        }
        return this.zznzk;
    }

    public final synchronized String zzcjh() {
        if (this.zzct == null) {
            zzcjj();
        }
        return this.zzct;
    }

    public final synchronized int zzcji() {
        if (this.zznzl == 0) {
            PackageInfo zzoa = zzoa("com.google.android.gms");
            if (zzoa != null) {
                this.zznzl = zzoa.versionCode;
            }
        }
        return this.zznzl;
    }
}
