package com.google.android.gms.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.UserManager;
import android.support.p001v4.content.PermissionChecker;

public abstract class zzcui<T> {
    private static Context zzair = null;
    private static boolean zzcek = false;
    private static final Object zzjwy = new Object();
    private static Boolean zzjwz = null;
    private final zzcup zzjxa;
    final String zzjxb;
    private final String zzjxc;
    private final T zzjxd;
    private T zzjxe;

    private zzcui(zzcup zzcup, String str, T t) {
        this.zzjxe = null;
        if (zzcup.zzjxi == null && zzcup.zzjxj == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        } else if (zzcup.zzjxi == null || zzcup.zzjxj == null) {
            this.zzjxa = zzcup;
            String valueOf = String.valueOf(zzcup.zzjxk);
            String valueOf2 = String.valueOf(str);
            this.zzjxc = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            String valueOf3 = String.valueOf(zzcup.zzjxl);
            String valueOf4 = String.valueOf(str);
            this.zzjxb = valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3);
            this.zzjxd = t;
        } else {
            throw new IllegalArgumentException("Must pass one of SharedPreferences file name or ContentProvider URI");
        }
    }

    /* synthetic */ zzcui(zzcup zzcup, String str, Object obj, zzcum zzcum) {
        this(zzcup, str, obj);
    }

    /* access modifiers changed from: private */
    public static zzcui<String> zza(zzcup zzcup, String str, String str2) {
        return new zzcun(zzcup, str, str2);
    }

    private static <V> V zza(zzcuo<V> zzcuo) {
        long clearCallingIdentity;
        try {
            return zzcuo.zzbct();
        } catch (SecurityException e) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            V zzbct = zzcuo.zzbct();
            Binder.restoreCallingIdentity(clearCallingIdentity);
            return zzbct;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(clearCallingIdentity);
            throw th;
        }
    }

    @TargetApi(24)
    private final T zzbcp() {
        if (!zzg("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            if (this.zzjxa.zzjxj != null) {
                String str = (String) zza(new zzcuj(this, zzctv.zza(zzair.getContentResolver(), this.zzjxa.zzjxj)));
                if (str != null) {
                    return zzkt(str);
                }
            } else if (this.zzjxa.zzjxi == null || (VERSION.SDK_INT >= 24 && !zzair.isDeviceProtectedStorage() && !((UserManager) zzair.getSystemService(UserManager.class)).isUserUnlocked())) {
                return null;
            } else {
                SharedPreferences sharedPreferences = zzair.getSharedPreferences(this.zzjxa.zzjxi, 0);
                if (sharedPreferences.contains(this.zzjxb)) {
                    return zzb(sharedPreferences);
                }
            }
        }
        return null;
    }

    private final T zzbcq() {
        if (!this.zzjxa.zzjxm && zzbcr()) {
            String str = (String) zza(new zzcuk(this));
            if (str != null) {
                return zzkt(str);
            }
        }
        return null;
    }

    private static boolean zzbcr() {
        if (zzjwz == null) {
            boolean z = false;
            if (zzair == null) {
                return false;
            }
            if (PermissionChecker.checkCallingOrSelfPermission(zzair, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0) {
                z = true;
            }
            zzjwz = Boolean.valueOf(z);
        }
        return zzjwz.booleanValue();
    }

    public static void zzdz(Context context) {
        if (zzair == null) {
            synchronized (zzjwy) {
                if (VERSION.SDK_INT < 24 || !context.isDeviceProtectedStorage()) {
                    Context applicationContext = context.getApplicationContext();
                    if (applicationContext != null) {
                        context = applicationContext;
                    }
                }
                if (zzair != context) {
                    zzjwz = null;
                }
                zzair = context;
            }
            zzcek = false;
        }
    }

    static boolean zzg(String str, boolean z) {
        if (zzbcr()) {
            return ((Boolean) zza(new zzcul(str, false))).booleanValue();
        }
        return false;
    }

    public final T get() {
        if (zzair == null) {
            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
        }
        if (this.zzjxa.zzjxn) {
            T zzbcq = zzbcq();
            if (zzbcq != null) {
                return zzbcq;
            }
            T zzbcp = zzbcp();
            if (zzbcp != null) {
                return zzbcp;
            }
        } else {
            T zzbcp2 = zzbcp();
            if (zzbcp2 != null) {
                return zzbcp2;
            }
            T zzbcq2 = zzbcq();
            if (zzbcq2 != null) {
                return zzbcq2;
            }
        }
        return this.zzjxd;
    }

    public abstract T zzb(SharedPreferences sharedPreferences);

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ String zzbcs() {
        return zzdmf.zza(zzair.getContentResolver(), this.zzjxc, (String) null);
    }

    public abstract T zzkt(String str);
}
