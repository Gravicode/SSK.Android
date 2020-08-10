package com.google.firebase.messaging;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.C1133R;
import com.google.android.gms.common.util.zzq;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.poi.p009ss.usermodel.ShapeTypes;
import org.json.JSONArray;
import org.json.JSONException;

final class zza {
    private static zza zznzz;
    private final Context mContext;
    private Bundle zzfwe;
    private Method zzoaa;
    private Method zzoab;
    private final AtomicInteger zzoac = new AtomicInteger((int) SystemClock.elapsedRealtime());

    private zza(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @TargetApi(26)
    private final Notification zza(CharSequence charSequence, String str, int i, Integer num, Uri uri, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str2) {
        Builder smallIcon = new Builder(this.mContext).setAutoCancel(true).setSmallIcon(i);
        if (!TextUtils.isEmpty(charSequence)) {
            smallIcon.setContentTitle(charSequence);
        }
        if (!TextUtils.isEmpty(str)) {
            smallIcon.setContentText(str);
            smallIcon.setStyle(new BigTextStyle().bigText(str));
        }
        if (num != null) {
            smallIcon.setColor(num.intValue());
        }
        if (uri != null) {
            smallIcon.setSound(uri);
        }
        if (pendingIntent != null) {
            smallIcon.setContentIntent(pendingIntent);
        }
        if (pendingIntent2 != null) {
            smallIcon.setDeleteIntent(pendingIntent2);
        }
        if (str2 != null) {
            if (this.zzoaa == null) {
                this.zzoaa = zzrr("setChannelId");
            }
            if (this.zzoaa == null) {
                this.zzoaa = zzrr("setChannel");
            }
            if (this.zzoaa == null) {
                Log.e("FirebaseMessaging", "Error while setting the notification channel");
            } else {
                try {
                    this.zzoaa.invoke(smallIcon, new Object[]{str2});
                } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException e) {
                    Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
                }
            }
        }
        return smallIcon.build();
    }

    private static void zza(Intent intent, Bundle bundle) {
        for (String str : bundle.keySet()) {
            if (str.startsWith("google.c.a.") || str.equals("from")) {
                intent.putExtra(str, bundle.getString(str));
            }
        }
    }

    static boolean zzag(Bundle bundle) {
        return "1".equals(zze(bundle, "gcm.n.e")) || zze(bundle, "gcm.n.icon") != null;
    }

    @Nullable
    static Uri zzah(@NonNull Bundle bundle) {
        String zze = zze(bundle, "gcm.n.link_android");
        if (TextUtils.isEmpty(zze)) {
            zze = zze(bundle, "gcm.n.link");
        }
        if (!TextUtils.isEmpty(zze)) {
            return Uri.parse(zze);
        }
        return null;
    }

    static String zzai(Bundle bundle) {
        String zze = zze(bundle, "gcm.n.sound2");
        return TextUtils.isEmpty(zze) ? zze(bundle, "gcm.n.sound") : zze;
    }

    private final Bundle zzauu() {
        if (this.zzfwe != null) {
            return this.zzfwe;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = this.mContext.getPackageManager().getApplicationInfo(this.mContext.getPackageName(), 128);
        } catch (NameNotFoundException e) {
        }
        if (applicationInfo == null || applicationInfo.metaData == null) {
            return Bundle.EMPTY;
        }
        this.zzfwe = applicationInfo.metaData;
        return this.zzfwe;
    }

    static String zze(Bundle bundle, String str) {
        String string = bundle.getString(str);
        return string == null ? bundle.getString(str.replace("gcm.n.", "gcm.notification.")) : string;
    }

    static synchronized zza zzex(Context context) {
        zza zza;
        synchronized (zza.class) {
            if (zznzz == null) {
                zznzz = new zza(context);
            }
            zza = zznzz;
        }
        return zza;
    }

    static String zzh(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_key");
        return zze(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
    }

    static Object[] zzi(Bundle bundle, String str) {
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("_loc_args");
        String zze = zze(bundle, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        if (TextUtils.isEmpty(zze)) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(zze);
            Object[] objArr = new String[jSONArray.length()];
            for (int i = 0; i < objArr.length; i++) {
                objArr[i] = jSONArray.opt(i);
            }
            return objArr;
        } catch (JSONException e) {
            String str2 = "FirebaseMessaging";
            String valueOf3 = String.valueOf(str);
            String valueOf4 = String.valueOf("_loc_args");
            String substring = (valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3)).substring(6);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 41 + String.valueOf(zze).length());
            sb.append("Malformed ");
            sb.append(substring);
            sb.append(": ");
            sb.append(zze);
            sb.append("  Default value will be used.");
            Log.w(str2, sb.toString());
            return null;
        }
    }

    @TargetApi(26)
    private final boolean zzid(int i) {
        if (VERSION.SDK_INT != 26) {
            return true;
        }
        try {
            Drawable drawable = this.mContext.getResources().getDrawable(i, null);
            if (drawable.getBounds().height() != 0 && drawable.getBounds().width() != 0) {
                return true;
            }
            StringBuilder sb = new StringBuilder(72);
            sb.append("Icon with id: ");
            sb.append(i);
            sb.append(" uses an invalid gradient. Using fallback icon.");
            Log.e("FirebaseMessaging", sb.toString());
            return false;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private final String zzj(Bundle bundle, String str) {
        String zze = zze(bundle, str);
        if (!TextUtils.isEmpty(zze)) {
            return zze;
        }
        String zzh = zzh(bundle, str);
        if (TextUtils.isEmpty(zzh)) {
            return null;
        }
        Resources resources = this.mContext.getResources();
        int identifier = resources.getIdentifier(zzh, "string", this.mContext.getPackageName());
        if (identifier == 0) {
            String str2 = "FirebaseMessaging";
            String valueOf = String.valueOf(str);
            String valueOf2 = String.valueOf("_loc_key");
            String substring = (valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)).substring(6);
            StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 49 + String.valueOf(zzh).length());
            sb.append(substring);
            sb.append(" resource not found: ");
            sb.append(zzh);
            sb.append(" Default value will be used.");
            Log.w(str2, sb.toString());
            return null;
        }
        Object[] zzi = zzi(bundle, str);
        if (zzi == null) {
            return resources.getString(identifier);
        }
        try {
            return resources.getString(identifier, zzi);
        } catch (MissingFormatArgumentException e) {
            String arrays = Arrays.toString(zzi);
            StringBuilder sb2 = new StringBuilder(String.valueOf(zzh).length() + 58 + String.valueOf(arrays).length());
            sb2.append("Missing format argument for ");
            sb2.append(zzh);
            sb2.append(": ");
            sb2.append(arrays);
            sb2.append(" Default value will be used.");
            Log.w("FirebaseMessaging", sb2.toString(), e);
            return null;
        }
    }

    @TargetApi(26)
    private static Method zzrr(String str) {
        try {
            return Builder.class.getMethod(str, new Class[]{String.class});
        } catch (NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private final Integer zzrs(String str) {
        if (VERSION.SDK_INT < 21) {
            return null;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                return Integer.valueOf(Color.parseColor(str));
            } catch (IllegalArgumentException e) {
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 54);
                sb.append("Color ");
                sb.append(str);
                sb.append(" not valid. Notification will use default color.");
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        int i = zzauu().getInt("com.google.firebase.messaging.default_notification_color", 0);
        if (i != 0) {
            try {
                return Integer.valueOf(ContextCompat.getColor(this.mContext, i));
            } catch (NotFoundException e2) {
                Log.w("FirebaseMessaging", "Cannot find the color resource referenced in AndroidManifest.");
            }
        }
        return null;
    }

    @TargetApi(26)
    private final String zzrt(String str) {
        String str2;
        String str3;
        if (!zzq.isAtLeastO()) {
            return null;
        }
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        try {
            if (this.zzoab == null) {
                this.zzoab = notificationManager.getClass().getMethod("getNotificationChannel", new Class[]{String.class});
            }
            if (!TextUtils.isEmpty(str)) {
                if (this.zzoab.invoke(notificationManager, new Object[]{str}) != null) {
                    return str;
                }
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + ShapeTypes.RIBBON);
                sb.append("Notification Channel requested (");
                sb.append(str);
                sb.append(") has not been created by the app. Manifest configuration, or default, value will be used.");
                Log.w("FirebaseMessaging", sb.toString());
            }
            String string = zzauu().getString("com.google.firebase.messaging.default_notification_channel_id");
            if (!TextUtils.isEmpty(string)) {
                if (this.zzoab.invoke(notificationManager, new Object[]{string}) != null) {
                    return string;
                }
                str2 = "FirebaseMessaging";
                str3 = "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.";
            } else {
                str2 = "FirebaseMessaging";
                str3 = "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.";
            }
            Log.w(str2, str3);
            if (this.zzoab.invoke(notificationManager, new Object[]{"fcm_fallback_notification_channel"}) == null) {
                Class cls = Class.forName("android.app.NotificationChannel");
                Object newInstance = cls.getConstructor(new Class[]{String.class, CharSequence.class, Integer.TYPE}).newInstance(new Object[]{"fcm_fallback_notification_channel", this.mContext.getString(C1133R.string.fcm_fallback_notification_channel_label), Integer.valueOf(3)});
                notificationManager.getClass().getMethod("createNotificationChannel", new Class[]{cls}).invoke(notificationManager, new Object[]{newInstance});
            }
            return "fcm_fallback_notification_channel";
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | LinkageError | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            Log.e("FirebaseMessaging", "Error while setting the notification channel", e);
            return null;
        }
    }

    private final PendingIntent zzt(Bundle bundle) {
        Intent intent;
        String zze = zze(bundle, "gcm.n.click_action");
        if (!TextUtils.isEmpty(zze)) {
            intent = new Intent(zze);
            intent.setPackage(this.mContext.getPackageName());
            intent.setFlags(268435456);
        } else {
            Uri zzah = zzah(bundle);
            if (zzah != null) {
                intent = new Intent("android.intent.action.VIEW");
                intent.setPackage(this.mContext.getPackageName());
                intent.setData(zzah);
            } else {
                intent = this.mContext.getPackageManager().getLaunchIntentForPackage(this.mContext.getPackageName());
                if (intent == null) {
                    Log.w("FirebaseMessaging", "No activity found to launch app");
                }
            }
        }
        if (intent == null) {
            return null;
        }
        intent.addFlags(67108864);
        Bundle bundle2 = new Bundle(bundle);
        FirebaseMessagingService.zzq(bundle2);
        intent.putExtras(bundle2);
        for (String str : bundle2.keySet()) {
            if (str.startsWith("gcm.n.") || str.startsWith("gcm.notification.")) {
                intent.removeExtra(str);
            }
        }
        return PendingIntent.getActivity(this.mContext, this.zzoac.incrementAndGet(), intent, 1073741824);
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0120  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x018d  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01fa  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0203  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0214  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x021d  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0222  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0227  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x023d  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0254  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean zzs(android.os.Bundle r13) {
        /*
            r12 = this;
            java.lang.String r0 = "1"
            java.lang.String r1 = "gcm.n.noui"
            java.lang.String r1 = zze(r13, r1)
            boolean r0 = r0.equals(r1)
            r1 = 1
            if (r0 == 0) goto L_0x0010
            return r1
        L_0x0010:
            android.content.Context r0 = r12.mContext
            java.lang.String r2 = "keyguard"
            java.lang.Object r0 = r0.getSystemService(r2)
            android.app.KeyguardManager r0 = (android.app.KeyguardManager) r0
            boolean r0 = r0.inKeyguardRestrictedInputMode()
            r2 = 0
            if (r0 != 0) goto L_0x005c
            boolean r0 = com.google.android.gms.common.util.zzq.zzamn()
            if (r0 != 0) goto L_0x002c
            r3 = 10
            android.os.SystemClock.sleep(r3)
        L_0x002c:
            int r0 = android.os.Process.myPid()
            android.content.Context r3 = r12.mContext
            java.lang.String r4 = "activity"
            java.lang.Object r3 = r3.getSystemService(r4)
            android.app.ActivityManager r3 = (android.app.ActivityManager) r3
            java.util.List r3 = r3.getRunningAppProcesses()
            if (r3 == 0) goto L_0x005c
            java.util.Iterator r3 = r3.iterator()
        L_0x0044:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto L_0x005c
            java.lang.Object r4 = r3.next()
            android.app.ActivityManager$RunningAppProcessInfo r4 = (android.app.ActivityManager.RunningAppProcessInfo) r4
            int r5 = r4.pid
            if (r5 != r0) goto L_0x0044
            int r0 = r4.importance
            r3 = 100
            if (r0 != r3) goto L_0x005c
            r0 = 1
            goto L_0x005d
        L_0x005c:
            r0 = 0
        L_0x005d:
            if (r0 == 0) goto L_0x0060
            return r2
        L_0x0060:
            java.lang.String r0 = "gcm.n.title"
            java.lang.String r0 = r12.zzj(r13, r0)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 == 0) goto L_0x007c
            android.content.Context r0 = r12.mContext
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo()
            android.content.Context r3 = r12.mContext
            android.content.pm.PackageManager r3 = r3.getPackageManager()
            java.lang.CharSequence r0 = r0.loadLabel(r3)
        L_0x007c:
            r4 = r0
            java.lang.String r0 = "gcm.n.body"
            java.lang.String r5 = r12.zzj(r13, r0)
            java.lang.String r0 = "gcm.n.icon"
            java.lang.String r0 = zze(r13, r0)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x00e5
            android.content.Context r3 = r12.mContext
            android.content.res.Resources r3 = r3.getResources()
            java.lang.String r6 = "drawable"
            android.content.Context r7 = r12.mContext
            java.lang.String r7 = r7.getPackageName()
            int r6 = r3.getIdentifier(r0, r6, r7)
            if (r6 == 0) goto L_0x00aa
            boolean r7 = r12.zzid(r6)
            if (r7 == 0) goto L_0x00aa
            goto L_0x010b
        L_0x00aa:
            java.lang.String r6 = "mipmap"
            android.content.Context r7 = r12.mContext
            java.lang.String r7 = r7.getPackageName()
            int r3 = r3.getIdentifier(r0, r6, r7)
            if (r3 == 0) goto L_0x00c0
            boolean r6 = r12.zzid(r3)
            if (r6 == 0) goto L_0x00c0
            r6 = r3
            goto L_0x010b
        L_0x00c0:
            java.lang.String r3 = "FirebaseMessaging"
            java.lang.String r6 = java.lang.String.valueOf(r0)
            int r6 = r6.length()
            int r6 = r6 + 61
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>(r6)
            java.lang.String r6 = "Icon resource "
            r7.append(r6)
            r7.append(r0)
            java.lang.String r0 = " not found. Notification will use default icon."
            r7.append(r0)
            java.lang.String r0 = r7.toString()
            android.util.Log.w(r3, r0)
        L_0x00e5:
            android.os.Bundle r0 = r12.zzauu()
            java.lang.String r3 = "com.google.firebase.messaging.default_notification_icon"
            int r0 = r0.getInt(r3, r2)
            if (r0 == 0) goto L_0x00f7
            boolean r3 = r12.zzid(r0)
            if (r3 != 0) goto L_0x00ff
        L_0x00f7:
            android.content.Context r0 = r12.mContext
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo()
            int r0 = r0.icon
        L_0x00ff:
            if (r0 == 0) goto L_0x0107
            boolean r3 = r12.zzid(r0)
            if (r3 != 0) goto L_0x010a
        L_0x0107:
            r0 = 17301651(0x1080093, float:2.4979667E-38)
        L_0x010a:
            r6 = r0
        L_0x010b:
            java.lang.String r0 = "gcm.n.color"
            java.lang.String r0 = zze(r13, r0)
            java.lang.Integer r7 = r12.zzrs(r0)
            java.lang.String r0 = zzai(r13)
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            r8 = 0
            if (r3 == 0) goto L_0x0122
            r0 = r8
            goto L_0x0183
        L_0x0122:
            java.lang.String r3 = "default"
            boolean r3 = r3.equals(r0)
            if (r3 != 0) goto L_0x017e
            android.content.Context r3 = r12.mContext
            android.content.res.Resources r3 = r3.getResources()
            java.lang.String r9 = "raw"
            android.content.Context r10 = r12.mContext
            java.lang.String r10 = r10.getPackageName()
            int r3 = r3.getIdentifier(r0, r9, r10)
            if (r3 == 0) goto L_0x017e
            java.lang.String r3 = "android.resource://"
            android.content.Context r9 = r12.mContext
            java.lang.String r9 = r9.getPackageName()
            java.lang.String r10 = java.lang.String.valueOf(r3)
            int r10 = r10.length()
            int r10 = r10 + 5
            java.lang.String r11 = java.lang.String.valueOf(r9)
            int r11 = r11.length()
            int r10 = r10 + r11
            java.lang.String r11 = java.lang.String.valueOf(r0)
            int r11 = r11.length()
            int r10 = r10 + r11
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>(r10)
            r11.append(r3)
            r11.append(r9)
            java.lang.String r3 = "/raw/"
            r11.append(r3)
            r11.append(r0)
            java.lang.String r0 = r11.toString()
            android.net.Uri r0 = android.net.Uri.parse(r0)
            goto L_0x0183
        L_0x017e:
            r0 = 2
            android.net.Uri r0 = android.media.RingtoneManager.getDefaultUri(r0)
        L_0x0183:
            android.app.PendingIntent r3 = r12.zzt(r13)
            boolean r9 = com.google.firebase.messaging.FirebaseMessagingService.zzaj(r13)
            if (r9 == 0) goto L_0x01c0
            android.content.Intent r8 = new android.content.Intent
            java.lang.String r9 = "com.google.firebase.messaging.NOTIFICATION_OPEN"
            r8.<init>(r9)
            zza(r8, r13)
            java.lang.String r9 = "pending_intent"
            r8.putExtra(r9, r3)
            android.content.Context r3 = r12.mContext
            java.util.concurrent.atomic.AtomicInteger r9 = r12.zzoac
            int r9 = r9.incrementAndGet()
            r10 = 1073741824(0x40000000, float:2.0)
            android.app.PendingIntent r3 = com.google.firebase.iid.zzx.zza(r3, r9, r8, r10)
            android.content.Intent r8 = new android.content.Intent
            java.lang.String r9 = "com.google.firebase.messaging.NOTIFICATION_DISMISS"
            r8.<init>(r9)
            zza(r8, r13)
            android.content.Context r9 = r12.mContext
            java.util.concurrent.atomic.AtomicInteger r11 = r12.zzoac
            int r11 = r11.incrementAndGet()
            android.app.PendingIntent r8 = com.google.firebase.iid.zzx.zza(r9, r11, r8, r10)
        L_0x01c0:
            r9 = r3
            r10 = r8
            boolean r3 = com.google.android.gms.common.util.zzq.isAtLeastO()
            if (r3 == 0) goto L_0x01e5
            android.content.Context r3 = r12.mContext
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo()
            int r3 = r3.targetSdkVersion
            r8 = 25
            if (r3 <= r8) goto L_0x01e5
            java.lang.String r3 = "gcm.n.android_channel_id"
            java.lang.String r3 = zze(r13, r3)
            java.lang.String r11 = r12.zzrt(r3)
            r3 = r12
            r8 = r0
            android.app.Notification r0 = r3.zza(r4, r5, r6, r7, r8, r9, r10, r11)
            goto L_0x022e
        L_0x01e5:
            android.support.v4.app.NotificationCompat$Builder r3 = new android.support.v4.app.NotificationCompat$Builder
            android.content.Context r8 = r12.mContext
            r3.<init>(r8)
            android.support.v4.app.NotificationCompat$Builder r3 = r3.setAutoCancel(r1)
            android.support.v4.app.NotificationCompat$Builder r3 = r3.setSmallIcon(r6)
            boolean r6 = android.text.TextUtils.isEmpty(r4)
            if (r6 != 0) goto L_0x01fd
            r3.setContentTitle(r4)
        L_0x01fd:
            boolean r4 = android.text.TextUtils.isEmpty(r5)
            if (r4 != 0) goto L_0x0212
            r3.setContentText(r5)
            android.support.v4.app.NotificationCompat$BigTextStyle r4 = new android.support.v4.app.NotificationCompat$BigTextStyle
            r4.<init>()
            android.support.v4.app.NotificationCompat$BigTextStyle r4 = r4.bigText(r5)
            r3.setStyle(r4)
        L_0x0212:
            if (r7 == 0) goto L_0x021b
            int r4 = r7.intValue()
            r3.setColor(r4)
        L_0x021b:
            if (r0 == 0) goto L_0x0220
            r3.setSound(r0)
        L_0x0220:
            if (r9 == 0) goto L_0x0225
            r3.setContentIntent(r9)
        L_0x0225:
            if (r10 == 0) goto L_0x022a
            r3.setDeleteIntent(r10)
        L_0x022a:
            android.app.Notification r0 = r3.build()
        L_0x022e:
            java.lang.String r3 = "gcm.n.tag"
            java.lang.String r13 = zze(r13, r3)
            java.lang.String r3 = "FirebaseMessaging"
            r4 = 3
            boolean r3 = android.util.Log.isLoggable(r3, r4)
            if (r3 == 0) goto L_0x0244
            java.lang.String r3 = "FirebaseMessaging"
            java.lang.String r4 = "Showing notification"
            android.util.Log.d(r3, r4)
        L_0x0244:
            android.content.Context r3 = r12.mContext
            java.lang.String r4 = "notification"
            java.lang.Object r3 = r3.getSystemService(r4)
            android.app.NotificationManager r3 = (android.app.NotificationManager) r3
            boolean r4 = android.text.TextUtils.isEmpty(r13)
            if (r4 == 0) goto L_0x026b
            long r4 = android.os.SystemClock.uptimeMillis()
            r13 = 37
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>(r13)
            java.lang.String r13 = "FCM-Notification:"
            r6.append(r13)
            r6.append(r4)
            java.lang.String r13 = r6.toString()
        L_0x026b:
            r3.notify(r13, r2, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.zza.zzs(android.os.Bundle):boolean");
    }
}
