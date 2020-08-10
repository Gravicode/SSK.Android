package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.p001v4.app.NotificationCompat;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzd;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.security.auth.x500.X500Principal;

public final class zzclq extends zzcjl {
    private static String[] zzjjn = {"firebase_"};
    private SecureRandom zzjjo;
    private final AtomicLong zzjjp = new AtomicLong(0);
    private int zzjjq;

    zzclq(zzcim zzcim) {
        super(zzcim);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int zza(java.lang.String r9, java.lang.Object r10, boolean r11) {
        /*
            r8 = this;
            r0 = 0
            if (r11 == 0) goto L_0x0035
            java.lang.String r1 = "param"
            boolean r2 = r10 instanceof android.os.Parcelable[]
            r3 = 1
            if (r2 == 0) goto L_0x000f
            r2 = r10
            android.os.Parcelable[] r2 = (android.os.Parcelable[]) r2
            int r2 = r2.length
            goto L_0x001a
        L_0x000f:
            boolean r2 = r10 instanceof java.util.ArrayList
            if (r2 == 0) goto L_0x0030
            r2 = r10
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            int r2 = r2.size()
        L_0x001a:
            r4 = 1000(0x3e8, float:1.401E-42)
            if (r2 <= r4) goto L_0x0030
            com.google.android.gms.internal.zzchm r3 = r8.zzawy()
            com.google.android.gms.internal.zzcho r3 = r3.zzazf()
            java.lang.String r4 = "Parameter array is too long; discarded. Value kind, name, array length"
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r3.zzd(r4, r1, r9, r2)
            r3 = 0
        L_0x0030:
            if (r3 != 0) goto L_0x0035
            r9 = 17
            return r9
        L_0x0035:
            boolean r1 = zzki(r9)
            if (r1 == 0) goto L_0x0048
            java.lang.String r3 = "param"
            r5 = 256(0x100, float:3.59E-43)
            r2 = r8
            r4 = r9
            r6 = r10
            r7 = r11
            boolean r9 = r2.zza(r3, r4, r5, r6, r7)
            goto L_0x0054
        L_0x0048:
            java.lang.String r2 = "param"
            r4 = 100
            r1 = r8
            r3 = r9
            r5 = r10
            r6 = r11
            boolean r9 = r1.zza(r2, r3, r4, r5, r6)
        L_0x0054:
            if (r9 == 0) goto L_0x0057
            return r0
        L_0x0057:
            r9 = 4
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzclq.zza(java.lang.String, java.lang.Object, boolean):int");
    }

    private static Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Double)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Float) {
            return Double.valueOf(((Float) obj).doubleValue());
        } else {
            if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
                return zza(String.valueOf(obj), i, z);
            }
            return null;
        }
    }

    public static Object zza(zzcmb zzcmb, String str) {
        zzcmc[] zzcmcArr;
        for (zzcmc zzcmc : zzcmb.zzjlh) {
            if (zzcmc.name.equals(str)) {
                if (zzcmc.zzgcc != null) {
                    return zzcmc.zzgcc;
                }
                if (zzcmc.zzjll != null) {
                    return zzcmc.zzjll;
                }
                if (zzcmc.zzjjl != null) {
                    return zzcmc.zzjjl;
                }
            }
        }
        return null;
    }

    public static String zza(String str, int i, boolean z) {
        if (str.codePointCount(0, str.length()) > i) {
            if (z) {
                return String.valueOf(str.substring(0, str.offsetByCodePoints(0, i))).concat("...");
            }
            str = null;
        }
        return str;
    }

    @Nullable
    public static String zza(String str, String[] strArr, String[] strArr2) {
        zzbq.checkNotNull(strArr);
        zzbq.checkNotNull(strArr2);
        int min = Math.min(strArr.length, strArr2.length);
        for (int i = 0; i < min; i++) {
            if (zzas(str, strArr[i])) {
                return strArr2[i];
            }
        }
        return null;
    }

    private final boolean zza(String str, String str2, int i, Object obj, boolean z) {
        Parcelable[] parcelableArr;
        if (obj == null || (obj instanceof Long) || (obj instanceof Float) || (obj instanceof Integer) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean) || (obj instanceof Double)) {
            return true;
        }
        if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
            String valueOf = String.valueOf(obj);
            if (valueOf.codePointCount(0, valueOf.length()) > i) {
                zzawy().zzazf().zzd("Value is too long; discarded. Value kind, name, value length", str, str2, Integer.valueOf(valueOf.length()));
                return false;
            }
            return true;
        } else if ((obj instanceof Bundle) && z) {
            return true;
        } else {
            if ((obj instanceof Parcelable[]) && z) {
                for (Parcelable parcelable : (Parcelable[]) obj) {
                    if (!(parcelable instanceof Bundle)) {
                        zzawy().zzazf().zze("All Parcelable[] elements must be of type Bundle. Value type, name", parcelable.getClass(), str2);
                        return false;
                    }
                }
                return true;
            } else if (!(obj instanceof ArrayList) || !z) {
                return false;
            } else {
                ArrayList arrayList = (ArrayList) obj;
                int size = arrayList.size();
                int i2 = 0;
                while (i2 < size) {
                    Object obj2 = arrayList.get(i2);
                    i2++;
                    if (!(obj2 instanceof Bundle)) {
                        zzawy().zzazf().zze("All ArrayList elements must be of type Bundle. Value type, name", obj2.getClass(), str2);
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private final boolean zza(String str, String[] strArr, String str2) {
        boolean z;
        boolean z2;
        if (str2 == null) {
            zzawy().zzazd().zzj("Name is required and can't be null. Type", str);
            return false;
        }
        zzbq.checkNotNull(str2);
        int i = 0;
        while (true) {
            if (i >= zzjjn.length) {
                z = false;
                break;
            } else if (str2.startsWith(zzjjn[i])) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        if (z) {
            zzawy().zzazd().zze("Name starts with reserved prefix. Type, name", str, str2);
            return false;
        }
        if (strArr != null) {
            zzbq.checkNotNull(strArr);
            int i2 = 0;
            while (true) {
                if (i2 >= strArr.length) {
                    z2 = false;
                    break;
                } else if (zzas(str2, strArr[i2])) {
                    z2 = true;
                    break;
                } else {
                    i2++;
                }
            }
            if (z2) {
                zzawy().zzazd().zze("Name is reserved. Type, name", str, str2);
                return false;
            }
        }
        return true;
    }

    public static boolean zza(long[] jArr, int i) {
        if (i >= (jArr.length << 6)) {
            return false;
        }
        return ((1 << (i % 64)) & jArr[i / 64]) != 0;
    }

    static byte[] zza(Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        } finally {
            obtain.recycle();
        }
    }

    public static long[] zza(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        long[] jArr = new long[length];
        for (int i = 0; i < length; i++) {
            jArr[i] = 0;
            for (int i2 = 0; i2 < 64; i2++) {
                int i3 = (i << 6) + i2;
                if (i3 >= bitSet.length()) {
                    break;
                }
                if (bitSet.get(i3)) {
                    jArr[i] = jArr[i] | (1 << i2);
                }
            }
        }
        return jArr;
    }

    static zzcmc[] zza(zzcmc[] zzcmcArr, String str, Object obj) {
        int length = zzcmcArr.length;
        int i = 0;
        while (i < length) {
            zzcmc zzcmc = zzcmcArr[i];
            if (Objects.equals(zzcmc.name, str)) {
                zzcmc.zzjll = null;
                zzcmc.zzgcc = null;
                zzcmc.zzjjl = null;
                if (obj instanceof Long) {
                    zzcmc.zzjll = (Long) obj;
                    return zzcmcArr;
                } else if (obj instanceof String) {
                    zzcmc.zzgcc = (String) obj;
                    return zzcmcArr;
                } else {
                    if (obj instanceof Double) {
                        zzcmc.zzjjl = (Double) obj;
                    }
                    return zzcmcArr;
                }
            } else {
                i++;
            }
        }
        zzcmc[] zzcmcArr2 = new zzcmc[(zzcmcArr.length + 1)];
        System.arraycopy(zzcmcArr, 0, zzcmcArr2, 0, zzcmcArr.length);
        zzcmc zzcmc2 = new zzcmc();
        zzcmc2.name = str;
        if (obj instanceof Long) {
            zzcmc2.zzjll = (Long) obj;
        } else if (obj instanceof String) {
            zzcmc2.zzgcc = (String) obj;
        } else if (obj instanceof Double) {
            zzcmc2.zzjjl = (Double) obj;
        }
        zzcmcArr2[zzcmcArr.length] = zzcmc2;
        return zzcmcArr2;
    }

    public static Bundle[] zzaf(Object obj) {
        Object[] array;
        if (obj instanceof Bundle) {
            return new Bundle[]{(Bundle) obj};
        }
        if (obj instanceof Parcelable[]) {
            Parcelable[] parcelableArr = (Parcelable[]) obj;
            array = Arrays.copyOf(parcelableArr, parcelableArr.length, Bundle[].class);
        } else if (!(obj instanceof ArrayList)) {
            return null;
        } else {
            ArrayList arrayList = (ArrayList) obj;
            array = arrayList.toArray(new Bundle[arrayList.size()]);
        }
        return (Bundle[]) array;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0037 A[Catch:{ IOException | ClassNotFoundException -> 0x003b }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003f A[Catch:{ IOException | ClassNotFoundException -> 0x003b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object zzag(java.lang.Object r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ all -> 0x0032 }
            r1.<init>()     // Catch:{ all -> 0x0032 }
            java.io.ObjectOutputStream r2 = new java.io.ObjectOutputStream     // Catch:{ all -> 0x0032 }
            r2.<init>(r1)     // Catch:{ all -> 0x0032 }
            r2.writeObject(r4)     // Catch:{ all -> 0x002f }
            r2.flush()     // Catch:{ all -> 0x002f }
            java.io.ObjectInputStream r4 = new java.io.ObjectInputStream     // Catch:{ all -> 0x002f }
            java.io.ByteArrayInputStream r3 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x002f }
            byte[] r1 = r1.toByteArray()     // Catch:{ all -> 0x002f }
            r3.<init>(r1)     // Catch:{ all -> 0x002f }
            r4.<init>(r3)     // Catch:{ all -> 0x002f }
            java.lang.Object r1 = r4.readObject()     // Catch:{ all -> 0x002d }
            r2.close()     // Catch:{ IOException | ClassNotFoundException -> 0x003b }
            r4.close()     // Catch:{ IOException | ClassNotFoundException -> 0x003b }
            return r1
        L_0x002d:
            r1 = move-exception
            goto L_0x0035
        L_0x002f:
            r1 = move-exception
            r4 = r0
            goto L_0x0035
        L_0x0032:
            r1 = move-exception
            r4 = r0
            r2 = r4
        L_0x0035:
            if (r2 == 0) goto L_0x003d
            r2.close()     // Catch:{ IOException | ClassNotFoundException -> 0x003b }
            goto L_0x003d
        L_0x003b:
            r4 = move-exception
            return r0
        L_0x003d:
            if (r4 == 0) goto L_0x0042
            r4.close()     // Catch:{ IOException | ClassNotFoundException -> 0x003b }
        L_0x0042:
            throw r1     // Catch:{ IOException | ClassNotFoundException -> 0x003b }
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzclq.zzag(java.lang.Object):java.lang.Object");
    }

    private final boolean zzag(Context context, String str) {
        zzcho zzcho;
        String str2;
        X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        try {
            PackageInfo packageInfo = zzbhf.zzdb(context).getPackageInfo(str, 64);
            if (!(packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0)) {
                return ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
        } catch (CertificateException e) {
            e = e;
            zzcho = zzawy().zzazd();
            str2 = "Error obtaining certificate";
            zzcho.zzj(str2, e);
            return true;
        } catch (NameNotFoundException e2) {
            e = e2;
            zzcho = zzawy().zzazd();
            str2 = "Package name not found";
            zzcho.zzj(str2, e);
            return true;
        }
        return true;
    }

    private final boolean zzaq(String str, String str2) {
        if (str2 == null) {
            zzawy().zzazd().zzj("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzawy().zzazd().zzj("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (!Character.isLetter(codePointAt)) {
                zzawy().zzazd().zze("Name must start with a letter. Type, name", str, str2);
                return false;
            }
            int length = str2.length();
            int charCount = Character.charCount(codePointAt);
            while (charCount < length) {
                int codePointAt2 = str2.codePointAt(charCount);
                if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                    charCount += Character.charCount(codePointAt2);
                } else {
                    zzawy().zzazd().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                    return false;
                }
            }
            return true;
        }
    }

    private final boolean zzar(String str, String str2) {
        if (str2 == null) {
            zzawy().zzazd().zzj("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.length() == 0) {
            zzawy().zzazd().zzj("Name is required and can't be empty. Type", str);
            return false;
        } else {
            int codePointAt = str2.codePointAt(0);
            if (Character.isLetter(codePointAt) || codePointAt == 95) {
                int length = str2.length();
                int charCount = Character.charCount(codePointAt);
                while (charCount < length) {
                    int codePointAt2 = str2.codePointAt(charCount);
                    if (codePointAt2 == 95 || Character.isLetterOrDigit(codePointAt2)) {
                        charCount += Character.charCount(codePointAt2);
                    } else {
                        zzawy().zzazd().zze("Name must consist of letters, digits or _ (underscores). Type, name", str, str2);
                        return false;
                    }
                }
                return true;
            }
            zzawy().zzazd().zze("Name must start with a letter or _ (underscore). Type, name", str, str2);
            return false;
        }
    }

    public static boolean zzas(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    private static void zzb(Bundle bundle, Object obj) {
        zzbq.checkNotNull(bundle);
        if (obj == null) {
            return;
        }
        if ((obj instanceof String) || (obj instanceof CharSequence)) {
            bundle.putLong("_el", (long) String.valueOf(obj).length());
        }
    }

    private final boolean zzb(String str, int i, String str2) {
        if (str2 == null) {
            zzawy().zzazd().zzj("Name is required and can't be null. Type", str);
            return false;
        } else if (str2.codePointCount(0, str2.length()) <= i) {
            return true;
        } else {
            zzawy().zzazd().zzd("Name is too long. Type, maximum supported length, name", str, Integer.valueOf(i), str2);
            return false;
        }
    }

    private static boolean zzd(Bundle bundle, int i) {
        if (bundle.getLong("_err") != 0) {
            return false;
        }
        bundle.putLong("_err", (long) i);
        return true;
    }

    @WorkerThread
    static boolean zzd(zzcha zzcha, zzcgi zzcgi) {
        zzbq.checkNotNull(zzcha);
        zzbq.checkNotNull(zzcgi);
        return !TextUtils.isEmpty(zzcgi.zzixs);
    }

    static MessageDigest zzek(String str) {
        int i = 0;
        while (i < 2) {
            try {
                MessageDigest instance = MessageDigest.getInstance(str);
                if (instance != null) {
                    return instance;
                }
                i++;
            } catch (NoSuchAlgorithmException e) {
            }
        }
        return null;
    }

    static boolean zzjz(String str) {
        zzbq.zzgm(str);
        return str.charAt(0) != '_' || str.equals("_ep");
    }

    private final int zzke(String str) {
        if (!zzaq("event param", str)) {
            return 3;
        }
        if (!zza("event param", (String[]) null, str)) {
            return 14;
        }
        return !zzb("event param", 40, str) ? 3 : 0;
    }

    private final int zzkf(String str) {
        if (!zzar("event param", str)) {
            return 3;
        }
        if (!zza("event param", (String[]) null, str)) {
            return 14;
        }
        return !zzb("event param", 40, str) ? 3 : 0;
    }

    private static int zzkh(String str) {
        return "_ldl".equals(str) ? 2048 : 36;
    }

    public static boolean zzki(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith("_");
    }

    static boolean zzkk(String str) {
        return str != null && str.matches("(\\+|-)?([0-9]+\\.?[0-9]*|[0-9]*\\.?[0-9]+)") && str.length() <= 310;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003b A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003c A[RETURN] */
    @android.support.annotation.WorkerThread
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean zzkn(java.lang.String r4) {
        /*
            com.google.android.gms.common.internal.zzbq.zzgm(r4)
            int r0 = r4.hashCode()
            r1 = 94660(0x171c4, float:1.32647E-40)
            r2 = 0
            r3 = 1
            if (r0 == r1) goto L_0x002d
            r1 = 95025(0x17331, float:1.33158E-40)
            if (r0 == r1) goto L_0x0023
            r1 = 95027(0x17333, float:1.33161E-40)
            if (r0 == r1) goto L_0x0019
            goto L_0x0037
        L_0x0019:
            java.lang.String r0 = "_ui"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0037
            r4 = 1
            goto L_0x0038
        L_0x0023:
            java.lang.String r0 = "_ug"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0037
            r4 = 2
            goto L_0x0038
        L_0x002d:
            java.lang.String r0 = "_in"
            boolean r4 = r4.equals(r0)
            if (r4 == 0) goto L_0x0037
            r4 = 0
            goto L_0x0038
        L_0x0037:
            r4 = -1
        L_0x0038:
            switch(r4) {
                case 0: goto L_0x003c;
                case 1: goto L_0x003c;
                case 2: goto L_0x003c;
                default: goto L_0x003b;
            }
        L_0x003b:
            return r2
        L_0x003c:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzclq.zzkn(java.lang.String):boolean");
    }

    public static boolean zzo(Intent intent) {
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }

    static long zzs(byte[] bArr) {
        zzbq.checkNotNull(bArr);
        int i = 0;
        zzbq.checkState(bArr.length > 0);
        long j = 0;
        int length = bArr.length - 1;
        while (length >= 0 && length >= bArr.length - 8) {
            j += (((long) bArr[length]) & 255) << i;
            i += 8;
            length--;
        }
        return j;
    }

    public static boolean zzt(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, str), 4);
            if (serviceInfo != null && serviceInfo.enabled) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
        }
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final Bundle zza(String str, Bundle bundle, @Nullable List<String> list, boolean z, boolean z2) {
        int i;
        if (bundle == null) {
            return null;
        }
        Bundle bundle2 = new Bundle(bundle);
        int i2 = 0;
        for (String str2 : bundle.keySet()) {
            if (list == null || !list.contains(str2)) {
                i = z ? zzke(str2) : 0;
                if (i == 0) {
                    i = zzkf(str2);
                }
            } else {
                i = 0;
            }
            if (i == 0) {
                int zza = zza(str2, bundle.get(str2), z2);
                if (zza == 0 || "_ev".equals(str2)) {
                    if (zzjz(str2)) {
                        i2++;
                        if (i2 > 25) {
                            StringBuilder sb = new StringBuilder(48);
                            sb.append("Event can't contain more then 25 params");
                            zzawy().zzazd().zze(sb.toString(), zzawt().zzjh(str), zzawt().zzx(bundle));
                            zzd(bundle2, 5);
                        }
                    }
                } else if (zzd(bundle2, zza)) {
                    bundle2.putString("_ev", zza(str2, 40, true));
                    zzb(bundle2, bundle.get(str2));
                }
            } else if (zzd(bundle2, i)) {
                bundle2.putString("_ev", zza(str2, 40, true));
                if (i == 3) {
                    zzb(bundle2, (Object) str2);
                }
            }
            bundle2.remove(str2);
        }
        return bundle2;
    }

    /* access modifiers changed from: 0000 */
    public final zzcha zza(String str, Bundle bundle, String str2, long j, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (zzkb(str) != 0) {
            zzawy().zzazd().zzj("Invalid conditional property event name", zzawt().zzjj(str));
            throw new IllegalArgumentException();
        }
        Bundle bundle2 = bundle != null ? new Bundle(bundle) : new Bundle();
        bundle2.putString("_o", str2);
        String str3 = str;
        zzcha zzcha = new zzcha(str3, new zzcgx(zzy(zza(str3, bundle2, Collections.singletonList("_o"), false, false))), str2, j);
        return zzcha;
    }

    public final void zza(int i, String str, String str2, int i2) {
        zza((String) null, i, str, str2, i2);
    }

    public final void zza(Bundle bundle, String str, Object obj) {
        if (bundle != null) {
            if (obj instanceof Long) {
                bundle.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof String) {
                bundle.putString(str, String.valueOf(obj));
            } else if (obj instanceof Double) {
                bundle.putDouble(str, ((Double) obj).doubleValue());
            } else {
                if (str != null) {
                    zzawy().zzazg().zze("Not putting event parameter. Invalid value type. name, type", zzawt().zzji(str), obj != null ? obj.getClass().getSimpleName() : null);
                }
            }
        }
    }

    public final void zza(zzcmc zzcmc, Object obj) {
        zzbq.checkNotNull(obj);
        zzcmc.zzgcc = null;
        zzcmc.zzjll = null;
        zzcmc.zzjjl = null;
        if (obj instanceof String) {
            zzcmc.zzgcc = (String) obj;
        } else if (obj instanceof Long) {
            zzcmc.zzjll = (Long) obj;
        } else if (obj instanceof Double) {
            zzcmc.zzjjl = (Double) obj;
        } else {
            zzawy().zzazd().zzj("Ignoring invalid (type) event param value", obj);
        }
    }

    public final void zza(zzcmg zzcmg, Object obj) {
        zzbq.checkNotNull(obj);
        zzcmg.zzgcc = null;
        zzcmg.zzjll = null;
        zzcmg.zzjjl = null;
        if (obj instanceof String) {
            zzcmg.zzgcc = (String) obj;
        } else if (obj instanceof Long) {
            zzcmg.zzjll = (Long) obj;
        } else if (obj instanceof Double) {
            zzcmg.zzjjl = (Double) obj;
        } else {
            zzawy().zzazd().zzj("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public final void zza(String str, int i, String str2, String str3, int i2) {
        Bundle bundle = new Bundle();
        zzd(bundle, i);
        if (!TextUtils.isEmpty(str2)) {
            bundle.putString(str2, str3);
        }
        if (i == 6 || i == 7 || i == 2) {
            bundle.putLong("_el", (long) i2);
        }
        this.zziwf.zzawm().zzc("auto", "_err", bundle);
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final long zzaf(Context context, String str) {
        zzve();
        zzbq.checkNotNull(context);
        zzbq.zzgm(str);
        PackageManager packageManager = context.getPackageManager();
        MessageDigest zzek = zzek("MD5");
        if (zzek == null) {
            zzawy().zzazd().log("Could not get MD5 instance");
            return -1;
        }
        if (packageManager != null) {
            try {
                if (!zzag(context, str)) {
                    PackageInfo packageInfo = zzbhf.zzdb(context).getPackageInfo(getContext().getPackageName(), 64);
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        return zzs(zzek.digest(packageInfo.signatures[0].toByteArray()));
                    }
                    zzawy().zzazf().log("Could not get signatures");
                    return -1;
                }
            } catch (NameNotFoundException e) {
                zzawy().zzazd().zzj("Package name not found", e);
            }
        }
        return 0;
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
    @WorkerThread
    public final void zzayy() {
        zzve();
        SecureRandom secureRandom = new SecureRandom();
        long nextLong = secureRandom.nextLong();
        if (nextLong == 0) {
            nextLong = secureRandom.nextLong();
            if (nextLong == 0) {
                zzawy().zzazf().log("Utils falling back to Random for random id");
            }
        }
        this.zzjjp.set(nextLong);
    }

    /* access modifiers changed from: 0000 */
    public final <T extends Parcelable> T zzb(byte[] bArr, Creator<T> creator) {
        if (bArr == null) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        try {
            obtain.unmarshall(bArr, 0, bArr.length);
            obtain.setDataPosition(0);
            return (Parcelable) creator.createFromParcel(obtain);
        } catch (zzbfo e) {
            zzawy().zzazd().log("Failed to load parcelable from buffer");
            return null;
        } finally {
            obtain.recycle();
        }
    }

    public final byte[] zzb(zzcmd zzcmd) {
        try {
            byte[] bArr = new byte[zzcmd.zzho()];
            zzfjk zzo = zzfjk.zzo(bArr, 0, bArr.length);
            zzcmd.zza(zzo);
            zzo.zzcwt();
            return bArr;
        } catch (IOException e) {
            zzawy().zzazd().zzj("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    public final long zzbay() {
        long andIncrement;
        long j;
        if (this.zzjjp.get() == 0) {
            synchronized (this.zzjjp) {
                long nextLong = new Random(System.nanoTime() ^ zzws().currentTimeMillis()).nextLong();
                int i = this.zzjjq + 1;
                this.zzjjq = i;
                j = nextLong + ((long) i);
            }
            return j;
        }
        synchronized (this.zzjjp) {
            this.zzjjp.compareAndSet(-1, 1);
            andIncrement = this.zzjjp.getAndIncrement();
        }
        return andIncrement;
    }

    /* access modifiers changed from: 0000 */
    @WorkerThread
    public final SecureRandom zzbaz() {
        zzve();
        if (this.zzjjo == null) {
            this.zzjjo = new SecureRandom();
        }
        return this.zzjjo;
    }

    @WorkerThread
    public final boolean zzeb(String str) {
        zzve();
        if (zzbhf.zzdb(getContext()).checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzawy().zzazi().zzj("Permission not granted", str);
        return false;
    }

    public final boolean zzf(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zzws().currentTimeMillis() - j) > j2;
    }

    public final Object zzk(String str, Object obj) {
        boolean z;
        int i = 256;
        if ("_ev".equals(str)) {
            z = true;
        } else {
            if (!zzki(str)) {
                i = 100;
            }
            z = false;
        }
        return zza(i, obj, z);
    }

    public final int zzka(String str) {
        if (!zzaq(NotificationCompat.CATEGORY_EVENT, str)) {
            return 2;
        }
        if (!zza(NotificationCompat.CATEGORY_EVENT, Event.zziwg, str)) {
            return 13;
        }
        return !zzb(NotificationCompat.CATEGORY_EVENT, 40, str) ? 2 : 0;
    }

    public final int zzkb(String str) {
        if (!zzar(NotificationCompat.CATEGORY_EVENT, str)) {
            return 2;
        }
        if (!zza(NotificationCompat.CATEGORY_EVENT, Event.zziwg, str)) {
            return 13;
        }
        return !zzb(NotificationCompat.CATEGORY_EVENT, 40, str) ? 2 : 0;
    }

    public final int zzkc(String str) {
        if (!zzaq("user property", str)) {
            return 6;
        }
        if (!zza("user property", UserProperty.zziwn, str)) {
            return 15;
        }
        return !zzb("user property", 24, str) ? 6 : 0;
    }

    public final int zzkd(String str) {
        if (!zzar("user property", str)) {
            return 6;
        }
        if (!zza("user property", UserProperty.zziwn, str)) {
            return 15;
        }
        return !zzb("user property", 24, str) ? 6 : 0;
    }

    public final boolean zzkg(String str) {
        if (TextUtils.isEmpty(str)) {
            zzawy().zzazd().log("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
            return false;
        }
        zzbq.checkNotNull(str);
        if (str.matches("^1:\\d+:android:[a-f0-9]+$")) {
            return true;
        }
        zzawy().zzazd().zzj("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", str);
        return false;
    }

    public final boolean zzkj(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return zzaxa().zzayd().equals(str);
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzkl(String str) {
        return "1".equals(zzawv().zzam(str, "measurement.upload.blacklist_internal"));
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzkm(String str) {
        return "1".equals(zzawv().zzam(str, "measurement.upload.blacklist_public"));
    }

    public final int zzl(String str, Object obj) {
        return "_ldl".equals(str) ? zza("user property referrer", str, zzkh(str), obj, false) : zza("user property", str, zzkh(str), obj, false) ? 0 : 7;
    }

    public final Object zzm(String str, Object obj) {
        int zzkh;
        boolean z;
        if ("_ldl".equals(str)) {
            zzkh = zzkh(str);
            z = true;
        } else {
            zzkh = zzkh(str);
            z = false;
        }
        return zza(zzkh, obj, z);
    }

    public final Bundle zzp(@NonNull Uri uri) {
        String str;
        String str2;
        String str3;
        String str4;
        if (uri == null) {
            return null;
        }
        try {
            if (uri.isHierarchical()) {
                str4 = uri.getQueryParameter("utm_campaign");
                str3 = uri.getQueryParameter("utm_source");
                str2 = uri.getQueryParameter("utm_medium");
                str = uri.getQueryParameter("gclid");
            } else {
                str4 = null;
                str3 = null;
                str2 = null;
                str = null;
            }
            if (TextUtils.isEmpty(str4) && TextUtils.isEmpty(str3) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str)) {
                return null;
            }
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(str4)) {
                bundle.putString(Param.CAMPAIGN, str4);
            }
            if (!TextUtils.isEmpty(str3)) {
                bundle.putString(Param.SOURCE, str3);
            }
            if (!TextUtils.isEmpty(str2)) {
                bundle.putString(Param.MEDIUM, str2);
            }
            if (!TextUtils.isEmpty(str)) {
                bundle.putString("gclid", str);
            }
            String queryParameter = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty(queryParameter)) {
                bundle.putString(Param.TERM, queryParameter);
            }
            String queryParameter2 = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty(queryParameter2)) {
                bundle.putString(Param.CONTENT, queryParameter2);
            }
            String queryParameter3 = uri.getQueryParameter(Param.ACLID);
            if (!TextUtils.isEmpty(queryParameter3)) {
                bundle.putString(Param.ACLID, queryParameter3);
            }
            String queryParameter4 = uri.getQueryParameter(Param.CP1);
            if (!TextUtils.isEmpty(queryParameter4)) {
                bundle.putString(Param.CP1, queryParameter4);
            }
            String queryParameter5 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty(queryParameter5)) {
                bundle.putString("anid", queryParameter5);
            }
            return bundle;
        } catch (UnsupportedOperationException e) {
            zzawy().zzazf().zzj("Install referrer url isn't a hierarchical URI", e);
            return null;
        }
    }

    public final byte[] zzq(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzawy().zzazd().zzj("Failed to gzip content", e);
            throw e;
        }
    }

    public final byte[] zzr(byte[] bArr) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr2, 0, read);
                } else {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            }
        } catch (IOException e) {
            zzawy().zzazd().zzj("Failed to ungzip content", e);
            throw e;
        }
    }

    public final /* bridge */ /* synthetic */ void zzve() {
        super.zzve();
    }

    public final /* bridge */ /* synthetic */ zzd zzws() {
        return super.zzws();
    }

    /* access modifiers changed from: 0000 */
    public final Bundle zzy(Bundle bundle) {
        Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object zzk = zzk(str, bundle.get(str));
                if (zzk == null) {
                    zzawy().zzazf().zzj("Param value can't be null", zzawt().zzji(str));
                } else {
                    zza(bundle2, str, zzk);
                }
            }
        }
        return bundle2;
    }
}
