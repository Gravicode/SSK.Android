package com.google.firebase.messaging;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.internal.zzfjr;
import com.google.android.gms.internal.zzfkt;
import com.google.android.gms.internal.zzfku;
import com.google.android.gms.measurement.AppMeasurement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class zzc {
    @Nullable
    private static Object zza(@NonNull zzfku zzfku, @NonNull String str, @NonNull zzb zzb) {
        Object obj;
        String str2 = null;
        try {
            Class cls = Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            Bundle zzaz = zzaz(zzfku.zzpri, zzfku.zzprj);
            obj = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            try {
                cls.getField("mOrigin").set(obj, str);
                cls.getField("mCreationTimestamp").set(obj, Long.valueOf(zzfku.zzprk));
                cls.getField("mName").set(obj, zzfku.zzpri);
                cls.getField("mValue").set(obj, zzfku.zzprj);
                if (!TextUtils.isEmpty(zzfku.zzprl)) {
                    str2 = zzfku.zzprl;
                }
                cls.getField("mTriggerEventName").set(obj, str2);
                cls.getField("mTimedOutEventName").set(obj, !TextUtils.isEmpty(zzfku.zzprq) ? zzfku.zzprq : zzb.zzbqu());
                cls.getField("mTimedOutEventParams").set(obj, zzaz);
                cls.getField("mTriggerTimeout").set(obj, Long.valueOf(zzfku.zzprm));
                cls.getField("mTriggeredEventName").set(obj, !TextUtils.isEmpty(zzfku.zzpro) ? zzfku.zzpro : zzb.zzbqt());
                cls.getField("mTriggeredEventParams").set(obj, zzaz);
                cls.getField("mTimeToLive").set(obj, Long.valueOf(zzfku.zzghq));
                cls.getField("mExpiredEventName").set(obj, !TextUtils.isEmpty(zzfku.zzprr) ? zzfku.zzprr : zzb.zzbqv());
                cls.getField("mExpiredEventParams").set(obj, zzaz);
                return obj;
            } catch (Exception e) {
                e = e;
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
                return obj;
            }
        } catch (Exception e2) {
            e = e2;
            obj = null;
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return obj;
        }
    }

    private static String zza(@Nullable zzfku zzfku, @NonNull zzb zzb) {
        return (zzfku == null || TextUtils.isEmpty(zzfku.zzprp)) ? zzb.zzbqw() : zzfku.zzprp;
    }

    private static List<Object> zza(@NonNull AppMeasurement appMeasurement, @NonNull String str) {
        List<Object> list;
        List<Object> arrayList = new ArrayList<>();
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getConditionalUserProperties", new Class[]{String.class, String.class});
            declaredMethod.setAccessible(true);
            list = (List) declaredMethod.invoke(appMeasurement, new Object[]{str, ""});
        } catch (Exception e) {
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            list = arrayList;
        }
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            int size = list.size();
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 55);
            sb.append("Number of currently set _Es for origin: ");
            sb.append(str);
            sb.append(" is ");
            sb.append(size);
            Log.v("FirebaseAbtUtil", sb.toString());
        }
        return list;
    }

    private static void zza(@NonNull Context context, @NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull String str4) {
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str5 = "FirebaseAbtUtil";
            String str6 = "_CE(experimentId) called by ";
            String valueOf = String.valueOf(str);
            Log.v(str5, valueOf.length() != 0 ? str6.concat(valueOf) : new String(str6));
        }
        if (zzet(context)) {
            AppMeasurement zzdc = zzdc(context);
            try {
                Method declaredMethod = AppMeasurement.class.getDeclaredMethod("clearConditionalUserProperty", new Class[]{String.class, String.class, Bundle.class});
                declaredMethod.setAccessible(true);
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 17 + String.valueOf(str3).length());
                    sb.append("Clearing _E: [");
                    sb.append(str2);
                    sb.append(", ");
                    sb.append(str3);
                    sb.append("]");
                    Log.v("FirebaseAbtUtil", sb.toString());
                }
                declaredMethod.invoke(zzdc, new Object[]{str2, str4, zzaz(str2, str3)});
            } catch (Exception e) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        }
    }

    public static void zza(@NonNull Context context, @NonNull String str, @NonNull byte[] bArr, @NonNull zzb zzb, int i) {
        boolean z;
        String str2 = str;
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str3 = "FirebaseAbtUtil";
            String str4 = "_SE called by ";
            String valueOf = String.valueOf(str);
            Log.v(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4));
        }
        if (zzet(context)) {
            AppMeasurement zzdc = zzdc(context);
            zzfku zzam = zzam(bArr);
            if (zzam == null) {
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    Log.v("FirebaseAbtUtil", "_SE failed; either _P was not set, or we couldn't deserialize the _P.");
                }
                return;
            }
            try {
                Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
                boolean z2 = false;
                for (Object next : zza(zzdc, str2)) {
                    String zzba = zzba(next);
                    String zzbb = zzbb(next);
                    long longValue = ((Long) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mCreationTimestamp").get(next)).longValue();
                    if (!zzam.zzpri.equals(zzba) || !zzam.zzprj.equals(zzbb)) {
                        zzfkt[] zzfktArr = zzam.zzprt;
                        int length = zzfktArr.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 >= length) {
                                z = false;
                                break;
                            } else if (zzfktArr[i2].zzpri.equals(zzba)) {
                                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                    StringBuilder sb = new StringBuilder(String.valueOf(zzba).length() + 33 + String.valueOf(zzbb).length());
                                    sb.append("_E is found in the _OE list. [");
                                    sb.append(zzba);
                                    sb.append(", ");
                                    sb.append(zzbb);
                                    sb.append("]");
                                    Log.v("FirebaseAbtUtil", sb.toString());
                                }
                                z = true;
                            } else {
                                i2++;
                            }
                        }
                        if (z) {
                            Context context2 = context;
                            zzb zzb2 = zzb;
                        } else if (zzam.zzprk > longValue) {
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                StringBuilder sb2 = new StringBuilder(String.valueOf(zzba).length() + 115 + String.valueOf(zzbb).length());
                                sb2.append("Clearing _E as it was not in the _OE list, andits start time is older than the start time of the _E to be set. [");
                                sb2.append(zzba);
                                sb2.append(", ");
                                sb2.append(zzbb);
                                sb2.append("]");
                                Log.v("FirebaseAbtUtil", sb2.toString());
                            }
                            zza(context, str2, zzba, zzbb, zza(zzam, zzb));
                        } else {
                            Context context3 = context;
                            zzb zzb3 = zzb;
                            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                                StringBuilder sb3 = new StringBuilder(String.valueOf(zzba).length() + 109 + String.valueOf(zzbb).length());
                                sb3.append("_E was not found in the _OE list, but not clearing it as it has a new start time than the _E to be set.  [");
                                sb3.append(zzba);
                                sb3.append(", ");
                                sb3.append(zzbb);
                                sb3.append("]");
                                Log.v("FirebaseAbtUtil", sb3.toString());
                            }
                        }
                    } else {
                        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                            StringBuilder sb4 = new StringBuilder(String.valueOf(zzba).length() + 23 + String.valueOf(zzbb).length());
                            sb4.append("_E is already set. [");
                            sb4.append(zzba);
                            sb4.append(", ");
                            sb4.append(zzbb);
                            sb4.append("]");
                            Log.v("FirebaseAbtUtil", sb4.toString());
                        }
                        z2 = true;
                    }
                }
                Context context4 = context;
                zzb zzb4 = zzb;
                if (z2) {
                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        String str5 = zzam.zzpri;
                        String str6 = zzam.zzprj;
                        StringBuilder sb5 = new StringBuilder(String.valueOf(str5).length() + 44 + String.valueOf(str6).length());
                        sb5.append("_E is already set. Not setting it again [");
                        sb5.append(str5);
                        sb5.append(", ");
                        sb5.append(str6);
                        sb5.append("]");
                        Log.v("FirebaseAbtUtil", sb5.toString());
                    }
                    return;
                }
                zza(zzdc, context4, str2, zzam, zzb4, 1);
            } catch (Exception e) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        }
    }

    private static void zza(@NonNull AppMeasurement appMeasurement, @NonNull Context context, @NonNull String str, @NonNull zzfku zzfku, @NonNull zzb zzb, int i) {
        if (Log.isLoggable("FirebaseAbtUtil", 2)) {
            String str2 = zzfku.zzpri;
            String str3 = zzfku.zzprj;
            StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 7 + String.valueOf(str3).length());
            sb.append("_SEI: ");
            sb.append(str2);
            sb.append(" ");
            sb.append(str3);
            Log.v("FirebaseAbtUtil", sb.toString());
        }
        try {
            Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            List zza = zza(appMeasurement, str);
            if (zza(appMeasurement, str).size() >= zzb(appMeasurement, str)) {
                if ((zzfku.zzprs != 0 ? zzfku.zzprs : 1) == 1) {
                    Object obj = zza.get(0);
                    String zzba = zzba(obj);
                    String zzbb = zzbb(obj);
                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        StringBuilder sb2 = new StringBuilder(String.valueOf(zzba).length() + 38);
                        sb2.append("Clearing _E due to overflow policy: [");
                        sb2.append(zzba);
                        sb2.append("]");
                        Log.v("FirebaseAbtUtil", sb2.toString());
                    }
                    zza(context, str, zzba, zzbb, zza(zzfku, zzb));
                } else {
                    if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                        String str4 = zzfku.zzpri;
                        String str5 = zzfku.zzprj;
                        StringBuilder sb3 = new StringBuilder(String.valueOf(str4).length() + 44 + String.valueOf(str5).length());
                        sb3.append("_E won't be set due to overflow policy. [");
                        sb3.append(str4);
                        sb3.append(", ");
                        sb3.append(str5);
                        sb3.append("]");
                        Log.v("FirebaseAbtUtil", sb3.toString());
                    }
                    return;
                }
            }
            for (Object next : zza) {
                String zzba2 = zzba(next);
                String zzbb2 = zzbb(next);
                if (zzba2.equals(zzfku.zzpri) && !zzbb2.equals(zzfku.zzprj) && Log.isLoggable("FirebaseAbtUtil", 2)) {
                    StringBuilder sb4 = new StringBuilder(String.valueOf(zzba2).length() + 77 + String.valueOf(zzbb2).length());
                    sb4.append("Clearing _E, as only one _V of the same _E can be set atany given time: [");
                    sb4.append(zzba2);
                    sb4.append(", ");
                    sb4.append(zzbb2);
                    sb4.append("].");
                    Log.v("FirebaseAbtUtil", sb4.toString());
                    zza(context, str, zzba2, zzbb2, zza(zzfku, zzb));
                }
            }
            Object zza2 = zza(zzfku, str, zzb);
            if (zza2 == null) {
                if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                    String str6 = zzfku.zzpri;
                    String str7 = zzfku.zzprj;
                    StringBuilder sb5 = new StringBuilder(String.valueOf(str6).length() + 42 + String.valueOf(str7).length());
                    sb5.append("Could not create _CUP for: [");
                    sb5.append(str6);
                    sb5.append(", ");
                    sb5.append(str7);
                    sb5.append("]. Skipping.");
                    Log.v("FirebaseAbtUtil", sb5.toString());
                }
                return;
            }
            try {
                Method declaredMethod = AppMeasurement.class.getDeclaredMethod("setConditionalUserProperty", new Class[]{Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty")});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(appMeasurement, new Object[]{zza2});
            } catch (Exception e) {
                Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            }
        } catch (Exception e2) {
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e2);
        }
    }

    @Nullable
    private static zzfku zzam(@NonNull byte[] bArr) {
        try {
            return zzfku.zzbi(bArr);
        } catch (zzfjr e) {
            return null;
        }
    }

    private static Bundle zzaz(@NonNull String str, @NonNull String str2) {
        Bundle bundle = new Bundle();
        bundle.putString(str, str2);
        return bundle;
    }

    private static int zzb(@NonNull AppMeasurement appMeasurement, @NonNull String str) {
        try {
            Method declaredMethod = AppMeasurement.class.getDeclaredMethod("getMaxUserProperties", new Class[]{String.class});
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(appMeasurement, new Object[]{str})).intValue();
        } catch (Exception e) {
            Log.e("FirebaseAbtUtil", "Could not complete the operation due to an internal error.", e);
            return 20;
        }
    }

    private static String zzba(@NonNull Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mName").get(obj);
    }

    private static String zzbb(@NonNull Object obj) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return (String) Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty").getField("mValue").get(obj);
    }

    @Nullable
    private static AppMeasurement zzdc(Context context) {
        try {
            return AppMeasurement.getInstance(context);
        } catch (NoClassDefFoundError e) {
            return null;
        }
    }

    private static boolean zzet(Context context) {
        if (zzdc(context) == null) {
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "Firebase Analytics not available");
            }
            return false;
        }
        try {
            Class.forName("com.google.android.gms.measurement.AppMeasurement$ConditionalUserProperty");
            return true;
        } catch (ClassNotFoundException e) {
            if (Log.isLoggable("FirebaseAbtUtil", 2)) {
                Log.v("FirebaseAbtUtil", "Firebase Analytics library is missing support for abt. Please update to a more recent version.");
            }
            return false;
        }
    }
}
