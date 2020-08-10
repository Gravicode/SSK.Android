package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.common.zzf;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.zzn;
import com.si_ware.neospectra.Global.GlobalVariables;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public final class DynamiteModule {
    private static Boolean zzgwq;
    private static zzk zzgwr;
    private static zzm zzgws;
    private static String zzgwt;
    private static final ThreadLocal<zza> zzgwu = new ThreadLocal<>();
    private static final zzi zzgwv = new zza();
    public static final zzd zzgww = new zzb();
    private static zzd zzgwx = new zzc();
    public static final zzd zzgwy = new zzd();
    public static final zzd zzgwz = new zze();
    public static final zzd zzgxa = new zzf();
    public static final zzd zzgxb = new zzg();
    private final Context zzgxc;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        public static ClassLoader sClassLoader;
    }

    static class zza {
        public Cursor zzgxd;

        private zza() {
        }

        /* synthetic */ zza(zza zza) {
            this();
        }
    }

    static class zzb implements zzi {
        private final int zzgxe;
        private final int zzgxf = 0;

        public zzb(int i, int i2) {
            this.zzgxe = i;
        }

        public final int zzab(Context context, String str) {
            return this.zzgxe;
        }

        public final int zzc(Context context, String str, boolean z) {
            return 0;
        }
    }

    public static class zzc extends Exception {
        private zzc(String str) {
            super(str);
        }

        /* synthetic */ zzc(String str, zza zza) {
            this(str);
        }

        private zzc(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ zzc(String str, Throwable th, zza zza) {
            this(str, th);
        }
    }

    public interface zzd {
        zzj zza(Context context, String str, zzi zzi) throws zzc;
    }

    private DynamiteModule(Context context) {
        this.zzgxc = (Context) zzbq.checkNotNull(context);
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, zzm zzm) {
        try {
            return (Context) zzn.zzx(zzm.zza(zzn.zzz(context), str, i, zzn.zzz(cursor)));
        } catch (Exception e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to load DynamiteLoader: ";
            String valueOf = String.valueOf(e.toString());
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return null;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0080, code lost:
        if (r1.zzgxd != null) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00e1, code lost:
        if (r1.zzgxd != null) goto L_0x0082;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.dynamite.DynamiteModule zza(android.content.Context r10, com.google.android.gms.dynamite.DynamiteModule.zzd r11, java.lang.String r12) throws com.google.android.gms.dynamite.DynamiteModule.zzc {
        /*
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r0 = zzgwu
            java.lang.Object r0 = r0.get()
            com.google.android.gms.dynamite.DynamiteModule$zza r0 = (com.google.android.gms.dynamite.DynamiteModule.zza) r0
            com.google.android.gms.dynamite.DynamiteModule$zza r1 = new com.google.android.gms.dynamite.DynamiteModule$zza
            r2 = 0
            r1.<init>(r2)
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r3 = zzgwu
            r3.set(r1)
            com.google.android.gms.dynamite.zzi r3 = zzgwv     // Catch:{ all -> 0x0131 }
            com.google.android.gms.dynamite.zzj r3 = r11.zza(r10, r12, r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r4 = "DynamiteModule"
            int r5 = r3.zzgxg     // Catch:{ all -> 0x0131 }
            int r6 = r3.zzgxh     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = java.lang.String.valueOf(r12)     // Catch:{ all -> 0x0131 }
            int r7 = r7.length()     // Catch:{ all -> 0x0131 }
            int r7 = r7 + 68
            java.lang.String r8 = java.lang.String.valueOf(r12)     // Catch:{ all -> 0x0131 }
            int r8 = r8.length()     // Catch:{ all -> 0x0131 }
            int r7 = r7 + r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r8.<init>(r7)     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = "Considering local module "
            r8.append(r7)     // Catch:{ all -> 0x0131 }
            r8.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = ":"
            r8.append(r7)     // Catch:{ all -> 0x0131 }
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = " and remote module "
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            r8.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = ":"
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            r8.append(r6)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x0131 }
            android.util.Log.i(r4, r5)     // Catch:{ all -> 0x0131 }
            int r4 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            if (r4 == 0) goto L_0x0107
            int r4 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            r5 = -1
            if (r4 != r5) goto L_0x006b
            int r4 = r3.zzgxg     // Catch:{ all -> 0x0131 }
            if (r4 == 0) goto L_0x0107
        L_0x006b:
            int r4 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            r6 = 1
            if (r4 != r6) goto L_0x0076
            int r4 = r3.zzgxh     // Catch:{ all -> 0x0131 }
            if (r4 != 0) goto L_0x0076
            goto L_0x0107
        L_0x0076:
            int r4 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            if (r4 != r5) goto L_0x008d
            com.google.android.gms.dynamite.DynamiteModule r10 = zzad(r10, r12)     // Catch:{ all -> 0x0131 }
            android.database.Cursor r11 = r1.zzgxd
            if (r11 == 0) goto L_0x0087
        L_0x0082:
            android.database.Cursor r11 = r1.zzgxd
            r11.close()
        L_0x0087:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r11 = zzgwu
            r11.set(r0)
            return r10
        L_0x008d:
            int r4 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            if (r4 != r6) goto L_0x00ec
            int r4 = r3.zzgxh     // Catch:{ zzc -> 0x00a6 }
            com.google.android.gms.dynamite.DynamiteModule r4 = zza(r10, r12, r4)     // Catch:{ zzc -> 0x00a6 }
            android.database.Cursor r10 = r1.zzgxd
            if (r10 == 0) goto L_0x00a0
            android.database.Cursor r10 = r1.zzgxd
            r10.close()
        L_0x00a0:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r10 = zzgwu
            r10.set(r0)
            return r4
        L_0x00a6:
            r4 = move-exception
            java.lang.String r6 = "DynamiteModule"
            java.lang.String r7 = "Failed to load remote module: "
            java.lang.String r8 = r4.getMessage()     // Catch:{ all -> 0x0131 }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x0131 }
            int r9 = r8.length()     // Catch:{ all -> 0x0131 }
            if (r9 == 0) goto L_0x00be
            java.lang.String r7 = r7.concat(r8)     // Catch:{ all -> 0x0131 }
            goto L_0x00c4
        L_0x00be:
            java.lang.String r8 = new java.lang.String     // Catch:{ all -> 0x0131 }
            r8.<init>(r7)     // Catch:{ all -> 0x0131 }
            r7 = r8
        L_0x00c4:
            android.util.Log.w(r6, r7)     // Catch:{ all -> 0x0131 }
            int r6 = r3.zzgxg     // Catch:{ all -> 0x0131 }
            if (r6 == 0) goto L_0x00e4
            com.google.android.gms.dynamite.DynamiteModule$zzb r6 = new com.google.android.gms.dynamite.DynamiteModule$zzb     // Catch:{ all -> 0x0131 }
            int r3 = r3.zzgxg     // Catch:{ all -> 0x0131 }
            r7 = 0
            r6.<init>(r3, r7)     // Catch:{ all -> 0x0131 }
            com.google.android.gms.dynamite.zzj r11 = r11.zza(r10, r12, r6)     // Catch:{ all -> 0x0131 }
            int r11 = r11.zzgxi     // Catch:{ all -> 0x0131 }
            if (r11 != r5) goto L_0x00e4
            com.google.android.gms.dynamite.DynamiteModule r10 = zzad(r10, r12)     // Catch:{ all -> 0x0131 }
            android.database.Cursor r11 = r1.zzgxd
            if (r11 == 0) goto L_0x0087
            goto L_0x0082
        L_0x00e4:
            com.google.android.gms.dynamite.DynamiteModule$zzc r10 = new com.google.android.gms.dynamite.DynamiteModule$zzc     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = "Remote load failed. No local fallback found."
            r10.<init>(r11, r4, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x00ec:
            com.google.android.gms.dynamite.DynamiteModule$zzc r10 = new com.google.android.gms.dynamite.DynamiteModule$zzc     // Catch:{ all -> 0x0131 }
            int r11 = r3.zzgxi     // Catch:{ all -> 0x0131 }
            r12 = 47
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r3.<init>(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r12 = "VersionPolicy returned invalid code:"
            r3.append(r12)     // Catch:{ all -> 0x0131 }
            r3.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = r3.toString()     // Catch:{ all -> 0x0131 }
            r10.<init>(r11, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x0107:
            com.google.android.gms.dynamite.DynamiteModule$zzc r10 = new com.google.android.gms.dynamite.DynamiteModule$zzc     // Catch:{ all -> 0x0131 }
            int r11 = r3.zzgxg     // Catch:{ all -> 0x0131 }
            int r12 = r3.zzgxh     // Catch:{ all -> 0x0131 }
            r3 = 91
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r4.<init>(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r3 = "No acceptable module found. Local version is "
            r4.append(r3)     // Catch:{ all -> 0x0131 }
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = " and remote version is "
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            r4.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = "."
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = r4.toString()     // Catch:{ all -> 0x0131 }
            r10.<init>(r11, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x0131:
            r10 = move-exception
            android.database.Cursor r11 = r1.zzgxd
            if (r11 == 0) goto L_0x013b
            android.database.Cursor r11 = r1.zzgxd
            r11.close()
        L_0x013b:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r11 = zzgwu
            r11.set(r0)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zza(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$zzd, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    private static DynamiteModule zza(Context context, String str, int i) throws zzc {
        Boolean bool;
        synchronized (DynamiteModule.class) {
            bool = zzgwq;
        }
        if (bool != null) {
            return bool.booleanValue() ? zzc(context, str, i) : zzb(context, str, i);
        }
        throw new zzc("Failed to determine which loading route to use.", (zza) null);
    }

    private static void zza(ClassLoader classLoader) throws zzc {
        zzm zzm;
        try {
            IBinder iBinder = (IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]);
            if (iBinder == null) {
                zzm = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoaderV2");
                zzm = queryLocalInterface instanceof zzm ? (zzm) queryLocalInterface : new zzn(iBinder);
            }
            zzgws = zzm;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new zzc("Failed to instantiate dynamite loader", e, null);
        }
    }

    public static int zzab(Context context, String str) {
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            String str2 = "com.google.android.gms.dynamite.descriptors.";
            String str3 = "ModuleDescriptor";
            StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 1 + String.valueOf(str).length() + String.valueOf(str3).length());
            sb.append(str2);
            sb.append(str);
            sb.append(".");
            sb.append(str3);
            Class loadClass = classLoader.loadClass(sb.toString());
            Field declaredField = loadClass.getDeclaredField(GlobalVariables.KEY_MODULE_ID);
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 51 + String.valueOf(str).length());
            sb2.append("Module descriptor id '");
            sb2.append(valueOf);
            sb2.append("' didn't match expected id '");
            sb2.append(str);
            sb2.append("'");
            Log.e("DynamiteModule", sb2.toString());
            return 0;
        } catch (ClassNotFoundException e) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(str).length() + 45);
            sb3.append("Local module descriptor class for ");
            sb3.append(str);
            sb3.append(" not found.");
            Log.w("DynamiteModule", sb3.toString());
            return 0;
        } catch (Exception e2) {
            String str4 = "DynamiteModule";
            String str5 = "Failed to load module descriptor class: ";
            String valueOf2 = String.valueOf(e2.getMessage());
            Log.e(str4, valueOf2.length() != 0 ? str5.concat(valueOf2) : new String(str5));
            return 0;
        }
    }

    public static int zzac(Context context, String str) {
        return zzc(context, str, false);
    }

    private static DynamiteModule zzad(Context context, String str) {
        String str2 = "DynamiteModule";
        String str3 = "Selected local version of ";
        String valueOf = String.valueOf(str);
        Log.i(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws zzc {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        zzk zzdf = zzdf(context);
        if (zzdf == null) {
            throw new zzc("Failed to create IDynamiteLoader.", (zza) null);
        }
        try {
            IObjectWrapper zza2 = zzdf.zza(zzn.zzz(context), str, i);
            if (zzn.zzx(zza2) != null) {
                return new DynamiteModule((Context) zzn.zzx(zza2));
            }
            throw new zzc("Failed to load remote module.", (zza) null);
        } catch (RemoteException e) {
            throw new zzc("Failed to load remote module.", e, null);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0088, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:18:0x0037=Splitter:B:18:0x0037, B:35:0x007b=Splitter:B:35:0x007b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int zzc(android.content.Context r8, java.lang.String r9, boolean r10) {
        /*
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r0 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r0)
            java.lang.Boolean r1 = zzgwq     // Catch:{ all -> 0x00e9 }
            if (r1 != 0) goto L_0x00b6
            android.content.Context r1 = r8.getApplicationContext()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule$DynamiteLoaderClassLoader> r2 = com.google.android.gms.dynamite.DynamiteModule.DynamiteLoaderClassLoader.class
            java.lang.String r2 = r2.getName()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            java.lang.Class r1 = r1.loadClass(r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            java.lang.String r2 = "sClassLoader"
            java.lang.reflect.Field r2 = r1.getDeclaredField(r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            monitor-enter(r1)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
            r3 = 0
            java.lang.Object r4 = r2.get(r3)     // Catch:{ all -> 0x008a }
            java.lang.ClassLoader r4 = (java.lang.ClassLoader) r4     // Catch:{ all -> 0x008a }
            if (r4 == 0) goto L_0x003a
            java.lang.ClassLoader r2 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x008a }
            if (r4 != r2) goto L_0x0032
        L_0x002f:
            java.lang.Boolean r2 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x008a }
            goto L_0x0087
        L_0x0032:
            zza(r4)     // Catch:{ zzc -> 0x0036 }
            goto L_0x0037
        L_0x0036:
            r2 = move-exception
        L_0x0037:
            java.lang.Boolean r2 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x008a }
            goto L_0x0087
        L_0x003a:
            java.lang.String r4 = "com.google.android.gms"
            android.content.Context r5 = r8.getApplicationContext()     // Catch:{ all -> 0x008a }
            java.lang.String r5 = r5.getPackageName()     // Catch:{ all -> 0x008a }
            boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x008a }
            if (r4 == 0) goto L_0x0052
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x008a }
            r2.set(r3, r4)     // Catch:{ all -> 0x008a }
            goto L_0x002f
        L_0x0052:
            int r4 = zze(r8, r9, r10)     // Catch:{ zzc -> 0x007e }
            java.lang.String r5 = zzgwt     // Catch:{ zzc -> 0x007e }
            if (r5 == 0) goto L_0x007b
            java.lang.String r5 = zzgwt     // Catch:{ zzc -> 0x007e }
            boolean r5 = r5.isEmpty()     // Catch:{ zzc -> 0x007e }
            if (r5 == 0) goto L_0x0063
            goto L_0x007b
        L_0x0063:
            com.google.android.gms.dynamite.zzh r5 = new com.google.android.gms.dynamite.zzh     // Catch:{ zzc -> 0x007e }
            java.lang.String r6 = zzgwt     // Catch:{ zzc -> 0x007e }
            java.lang.ClassLoader r7 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ zzc -> 0x007e }
            r5.<init>(r6, r7)     // Catch:{ zzc -> 0x007e }
            zza(r5)     // Catch:{ zzc -> 0x007e }
            r2.set(r3, r5)     // Catch:{ zzc -> 0x007e }
            java.lang.Boolean r5 = java.lang.Boolean.TRUE     // Catch:{ zzc -> 0x007e }
            zzgwq = r5     // Catch:{ zzc -> 0x007e }
            monitor-exit(r1)     // Catch:{ all -> 0x008a }
            monitor-exit(r0)     // Catch:{ all -> 0x00e9 }
            return r4
        L_0x007b:
            monitor-exit(r1)     // Catch:{ all -> 0x008a }
            monitor-exit(r0)     // Catch:{ all -> 0x00e9 }
            return r4
        L_0x007e:
            r4 = move-exception
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x008a }
            r2.set(r3, r4)     // Catch:{ all -> 0x008a }
            goto L_0x002f
        L_0x0087:
            monitor-exit(r1)     // Catch:{ all -> 0x008a }
            r1 = r2
            goto L_0x00b4
        L_0x008a:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x008a }
            throw r2     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008d }
        L_0x008d:
            r1 = move-exception
            java.lang.String r2 = "DynamiteModule"
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x00e9 }
            java.lang.String r3 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x00e9 }
            int r3 = r3.length()     // Catch:{ all -> 0x00e9 }
            int r3 = r3 + 30
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e9 }
            r4.<init>(r3)     // Catch:{ all -> 0x00e9 }
            java.lang.String r3 = "Failed to load module via V2: "
            r4.append(r3)     // Catch:{ all -> 0x00e9 }
            r4.append(r1)     // Catch:{ all -> 0x00e9 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00e9 }
            android.util.Log.w(r2, r1)     // Catch:{ all -> 0x00e9 }
            java.lang.Boolean r1 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00e9 }
        L_0x00b4:
            zzgwq = r1     // Catch:{ all -> 0x00e9 }
        L_0x00b6:
            monitor-exit(r0)     // Catch:{ all -> 0x00e9 }
            boolean r0 = r1.booleanValue()
            if (r0 == 0) goto L_0x00e4
            int r8 = zze(r8, r9, r10)     // Catch:{ zzc -> 0x00c2 }
            return r8
        L_0x00c2:
            r8 = move-exception
            java.lang.String r9 = "DynamiteModule"
            java.lang.String r10 = "Failed to retrieve remote module version: "
            java.lang.String r8 = r8.getMessage()
            java.lang.String r8 = java.lang.String.valueOf(r8)
            int r0 = r8.length()
            if (r0 == 0) goto L_0x00da
            java.lang.String r8 = r10.concat(r8)
            goto L_0x00df
        L_0x00da:
            java.lang.String r8 = new java.lang.String
            r8.<init>(r10)
        L_0x00df:
            android.util.Log.w(r9, r8)
            r8 = 0
            return r8
        L_0x00e4:
            int r8 = zzd(r8, r9, r10)
            return r8
        L_0x00e9:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00e9 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zzc(android.content.Context, java.lang.String, boolean):int");
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws zzc {
        zzm zzm;
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        synchronized (DynamiteModule.class) {
            zzm = zzgws;
        }
        if (zzm == null) {
            throw new zzc("DynamiteLoaderV2 was not cached.", (zza) null);
        }
        zza zza2 = (zza) zzgwu.get();
        if (zza2 == null || zza2.zzgxd == null) {
            throw new zzc("No result cursor", (zza) null);
        }
        Context zza3 = zza(context.getApplicationContext(), str, i, zza2.zzgxd, zzm);
        if (zza3 != null) {
            return new DynamiteModule(zza3);
        }
        throw new zzc("Failed to get module context", (zza) null);
    }

    private static int zzd(Context context, String str, boolean z) {
        zzk zzdf = zzdf(context);
        if (zzdf == null) {
            return 0;
        }
        try {
            return zzdf.zza(zzn.zzz(context), str, z);
        } catch (RemoteException e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to retrieve remote module version: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return 0;
        }
    }

    private static zzk zzdf(Context context) {
        zzk zzk;
        synchronized (DynamiteModule.class) {
            if (zzgwr != null) {
                zzk zzk2 = zzgwr;
                return zzk2;
            } else if (zzf.zzafy().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    IBinder iBinder = (IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance();
                    if (iBinder == null) {
                        zzk = null;
                    } else {
                        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamite.IDynamiteLoader");
                        zzk = queryLocalInterface instanceof zzk ? (zzk) queryLocalInterface : new zzl(iBinder);
                    }
                    if (zzk != null) {
                        zzgwr = zzk;
                        return zzk;
                    }
                } catch (Exception e) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:55:0x00b0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int zze(android.content.Context r8, java.lang.String r9, boolean r10) throws com.google.android.gms.dynamite.DynamiteModule.zzc {
        /*
            r0 = 0
            android.content.ContentResolver r1 = r8.getContentResolver()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r10 == 0) goto L_0x000a
            java.lang.String r8 = "api_force_staging"
            goto L_0x000c
        L_0x000a:
            java.lang.String r8 = "api"
        L_0x000c:
            java.lang.String r10 = "content://com.google.android.gms.chimera/"
            java.lang.String r2 = java.lang.String.valueOf(r10)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r2 = r2.length()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r2 = r2 + 1
            java.lang.String r3 = java.lang.String.valueOf(r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r3 = r3.length()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r2 = r2 + r3
            java.lang.String r3 = java.lang.String.valueOf(r9)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r3 = r3.length()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            int r2 = r2 + r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r3.<init>(r2)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r3.append(r10)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r3.append(r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            java.lang.String r8 = "/"
            r3.append(r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r3.append(r9)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            java.lang.String r8 = r3.toString()     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            android.net.Uri r2 = android.net.Uri.parse(r8)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r8 = r1.query(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x009d, all -> 0x009b }
            if (r8 == 0) goto L_0x008c
            boolean r9 = r8.moveToFirst()     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            if (r9 != 0) goto L_0x0056
            goto L_0x008c
        L_0x0056:
            r9 = 0
            int r9 = r8.getInt(r9)     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            if (r9 <= 0) goto L_0x007d
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r10 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r10)     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            r1 = 2
            java.lang.String r1 = r8.getString(r1)     // Catch:{ all -> 0x007a }
            zzgwt = r1     // Catch:{ all -> 0x007a }
            monitor-exit(r10)     // Catch:{ all -> 0x007a }
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r10 = zzgwu     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            java.lang.Object r10 = r10.get()     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            com.google.android.gms.dynamite.DynamiteModule$zza r10 = (com.google.android.gms.dynamite.DynamiteModule.zza) r10     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            if (r10 == 0) goto L_0x007d
            android.database.Cursor r1 = r10.zzgxd     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            if (r1 != 0) goto L_0x007d
            r10.zzgxd = r8     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            r8 = r0
            goto L_0x007d
        L_0x007a:
            r9 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x007a }
            throw r9     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
        L_0x007d:
            if (r8 == 0) goto L_0x0082
            r8.close()
        L_0x0082:
            return r9
        L_0x0083:
            r9 = move-exception
            r0 = r8
            r8 = r9
            goto L_0x00ae
        L_0x0087:
            r9 = move-exception
            r7 = r9
            r9 = r8
            r8 = r7
            goto L_0x009f
        L_0x008c:
            java.lang.String r9 = "DynamiteModule"
            java.lang.String r10 = "Failed to retrieve remote module version."
            android.util.Log.w(r9, r10)     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            com.google.android.gms.dynamite.DynamiteModule$zzc r9 = new com.google.android.gms.dynamite.DynamiteModule$zzc     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            java.lang.String r10 = "Failed to connect to dynamite module ContentResolver."
            r9.<init>(r10, r0)     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
            throw r9     // Catch:{ Exception -> 0x0087, all -> 0x0083 }
        L_0x009b:
            r8 = move-exception
            goto L_0x00ae
        L_0x009d:
            r8 = move-exception
            r9 = r0
        L_0x009f:
            boolean r10 = r8 instanceof com.google.android.gms.dynamite.DynamiteModule.zzc     // Catch:{ all -> 0x00ac }
            if (r10 == 0) goto L_0x00a4
            throw r8     // Catch:{ all -> 0x00ac }
        L_0x00a4:
            com.google.android.gms.dynamite.DynamiteModule$zzc r10 = new com.google.android.gms.dynamite.DynamiteModule$zzc     // Catch:{ all -> 0x00ac }
            java.lang.String r1 = "V2 version check failed"
            r10.<init>(r1, r8, r0)     // Catch:{ all -> 0x00ac }
            throw r10     // Catch:{ all -> 0x00ac }
        L_0x00ac:
            r8 = move-exception
            r0 = r9
        L_0x00ae:
            if (r0 == 0) goto L_0x00b3
            r0.close()
        L_0x00b3:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zze(android.content.Context, java.lang.String, boolean):int");
    }

    public final Context zzaqb() {
        return this.zzgxc;
    }

    public final IBinder zzhb(String str) throws zzc {
        try {
            return (IBinder) this.zzgxc.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String str2 = "Failed to instantiate module class: ";
            String valueOf = String.valueOf(str);
            throw new zzc(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e, null);
        }
    }
}
