package com.google.firebase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.p001v4.content.ContextCompat;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.ArraySet;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.internal.zzk;
import com.google.android.gms.common.api.internal.zzl;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzs;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.internal.InternalTokenProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseApp {
    public static final String DEFAULT_APP_NAME = "[DEFAULT]";
    /* access modifiers changed from: private */
    public static final Object sLock = new Object();
    static final Map<String, FirebaseApp> zzifg = new ArrayMap();
    private static final List<String> zzman = Arrays.asList(new String[]{"com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId"});
    private static final List<String> zzmao = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
    private static final List<String> zzmap = Arrays.asList(new String[]{"com.google.android.gms.measurement.AppMeasurement"});
    private static final List<String> zzmaq = Arrays.asList(new String[0]);
    private static final Set<String> zzmar = Collections.emptySet();
    private final Context mApplicationContext;
    private final String mName;
    private final FirebaseOptions zzmas;
    private final AtomicBoolean zzmat = new AtomicBoolean(false);
    private final AtomicBoolean zzmau = new AtomicBoolean();
    private final List<IdTokenListener> zzmav = new CopyOnWriteArrayList();
    private final List<zza> zzmaw = new CopyOnWriteArrayList();
    private final List<Object> zzmax = new CopyOnWriteArrayList();
    private InternalTokenProvider zzmay;
    private zzb zzmaz;

    @KeepForSdk
    public interface IdTokenListener {
        void zzb(@NonNull com.google.firebase.internal.zzc zzc);
    }

    public interface zza {
        void zzbf(boolean z);
    }

    public interface zzb {
        void zzgj(int i);
    }

    @TargetApi(24)
    static class zzc extends BroadcastReceiver {
        private static AtomicReference<zzc> zzmba = new AtomicReference<>();
        private final Context mApplicationContext;

        private zzc(Context context) {
            this.mApplicationContext = context;
        }

        /* access modifiers changed from: private */
        public static void zzer(Context context) {
            if (zzmba.get() == null) {
                zzc zzc = new zzc(context);
                if (zzmba.compareAndSet(null, zzc)) {
                    context.registerReceiver(zzc, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }

        public final void onReceive(Context context, Intent intent) {
            synchronized (FirebaseApp.sLock) {
                for (FirebaseApp zza : FirebaseApp.zzifg.values()) {
                    zza.zzbqr();
                }
            }
            this.mApplicationContext.unregisterReceiver(this);
        }
    }

    private FirebaseApp(Context context, String str, FirebaseOptions firebaseOptions) {
        this.mApplicationContext = (Context) zzbq.checkNotNull(context);
        this.mName = zzbq.zzgm(str);
        this.zzmas = (FirebaseOptions) zzbq.checkNotNull(firebaseOptions);
        this.zzmaz = new com.google.firebase.internal.zza();
    }

    public static List<FirebaseApp> getApps(Context context) {
        ArrayList arrayList;
        com.google.firebase.internal.zzb.zzew(context);
        synchronized (sLock) {
            arrayList = new ArrayList(zzifg.values());
            com.google.firebase.internal.zzb.zzcjr();
            Set<String> zzcjs = com.google.firebase.internal.zzb.zzcjs();
            zzcjs.removeAll(zzifg.keySet());
            for (String str : zzcjs) {
                com.google.firebase.internal.zzb.zzrq(str);
                arrayList.add(initializeApp(context, null, str));
            }
        }
        return arrayList;
    }

    @Nullable
    public static FirebaseApp getInstance() {
        FirebaseApp firebaseApp;
        synchronized (sLock) {
            firebaseApp = (FirebaseApp) zzifg.get(DEFAULT_APP_NAME);
            if (firebaseApp == null) {
                String zzamo = zzs.zzamo();
                StringBuilder sb = new StringBuilder(String.valueOf(zzamo).length() + 116);
                sb.append("Default FirebaseApp is not initialized in this process ");
                sb.append(zzamo);
                sb.append(". Make sure to call FirebaseApp.initializeApp(Context) first.");
                throw new IllegalStateException(sb.toString());
            }
        }
        return firebaseApp;
    }

    public static FirebaseApp getInstance(@NonNull String str) {
        FirebaseApp firebaseApp;
        String str2;
        synchronized (sLock) {
            firebaseApp = (FirebaseApp) zzifg.get(str.trim());
            if (firebaseApp == null) {
                List zzbqq = zzbqq();
                if (zzbqq.isEmpty()) {
                    str2 = "";
                } else {
                    String str3 = "Available app names: ";
                    String valueOf = String.valueOf(TextUtils.join(", ", zzbqq));
                    str2 = valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3);
                }
                throw new IllegalStateException(String.format("FirebaseApp with name %s doesn't exist. %s", new Object[]{str, str2}));
            }
        }
        return firebaseApp;
    }

    @Nullable
    public static FirebaseApp initializeApp(Context context) {
        synchronized (sLock) {
            if (zzifg.containsKey(DEFAULT_APP_NAME)) {
                FirebaseApp instance = getInstance();
                return instance;
            }
            FirebaseOptions fromResource = FirebaseOptions.fromResource(context);
            if (fromResource == null) {
                return null;
            }
            FirebaseApp initializeApp = initializeApp(context, fromResource);
            return initializeApp;
        }
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, DEFAULT_APP_NAME);
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String str) {
        FirebaseApp firebaseApp;
        com.google.firebase.internal.zzb.zzew(context);
        if (context.getApplicationContext() instanceof Application) {
            zzk.zza((Application) context.getApplicationContext());
            zzk.zzahb().zza((zzl) new zza());
        }
        String trim = str.trim();
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        synchronized (sLock) {
            boolean z = !zzifg.containsKey(trim);
            StringBuilder sb = new StringBuilder(String.valueOf(trim).length() + 33);
            sb.append("FirebaseApp name ");
            sb.append(trim);
            sb.append(" already exists!");
            zzbq.zza(z, (Object) sb.toString());
            zzbq.checkNotNull(context, "Application context cannot be null.");
            firebaseApp = new FirebaseApp(context, trim, firebaseOptions);
            zzifg.put(trim, firebaseApp);
        }
        com.google.firebase.internal.zzb.zzg(firebaseApp);
        firebaseApp.zza(FirebaseApp.class, firebaseApp, zzman);
        if (firebaseApp.zzbqo()) {
            firebaseApp.zza(FirebaseApp.class, firebaseApp, zzmao);
            firebaseApp.zza(Context.class, firebaseApp.getApplicationContext(), zzmap);
        }
        return firebaseApp;
    }

    private final <T> void zza(Class<T> cls, T t, Iterable<String> iterable) {
        boolean isDeviceProtectedStorage = ContextCompat.isDeviceProtectedStorage(this.mApplicationContext);
        if (isDeviceProtectedStorage) {
            zzc.zzer(this.mApplicationContext);
        }
        for (String str : iterable) {
            if (isDeviceProtectedStorage) {
                try {
                    if (zzmaq.contains(str)) {
                    }
                } catch (ClassNotFoundException e) {
                    if (zzmar.contains(str)) {
                        throw new IllegalStateException(String.valueOf(str).concat(" is missing, but is required. Check if it has been removed by Proguard."));
                    }
                    Log.d("FirebaseApp", String.valueOf(str).concat(" is not linked. Skipping initialization."));
                } catch (NoSuchMethodException e2) {
                    throw new IllegalStateException(String.valueOf(str).concat("#getInstance has been removed by Proguard. Add keep rule to prevent it."));
                } catch (InvocationTargetException e3) {
                    Log.wtf("FirebaseApp", "Firebase API initialization failure.", e3);
                } catch (IllegalAccessException e4) {
                    String str2 = "FirebaseApp";
                    String str3 = "Failed to initialize ";
                    String valueOf = String.valueOf(str);
                    Log.wtf(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e4);
                }
            }
            Method method = Class.forName(str).getMethod("getInstance", new Class[]{cls});
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                method.invoke(null, new Object[]{t});
            }
        }
    }

    public static void zzbf(boolean z) {
        synchronized (sLock) {
            ArrayList arrayList = new ArrayList(zzifg.values());
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                FirebaseApp firebaseApp = (FirebaseApp) obj;
                if (firebaseApp.zzmat.get()) {
                    firebaseApp.zzcd(z);
                }
            }
        }
    }

    private final void zzbqn() {
        zzbq.zza(!this.zzmau.get(), (Object) "FirebaseApp was deleted");
    }

    private static List<String> zzbqq() {
        ArraySet arraySet = new ArraySet();
        synchronized (sLock) {
            for (FirebaseApp name : zzifg.values()) {
                arraySet.add(name.getName());
            }
            if (com.google.firebase.internal.zzb.zzcjr() != null) {
                arraySet.addAll(com.google.firebase.internal.zzb.zzcjs());
            }
        }
        ArrayList arrayList = new ArrayList(arraySet);
        Collections.sort(arrayList);
        return arrayList;
    }

    /* access modifiers changed from: private */
    public final void zzbqr() {
        zza(FirebaseApp.class, this, zzman);
        if (zzbqo()) {
            zza(FirebaseApp.class, this, zzmao);
            zza(Context.class, this.mApplicationContext, zzmap);
        }
    }

    private final void zzcd(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        for (zza zzbf : this.zzmaw) {
            zzbf.zzbf(z);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FirebaseApp)) {
            return false;
        }
        return this.mName.equals(((FirebaseApp) obj).getName());
    }

    @NonNull
    public Context getApplicationContext() {
        zzbqn();
        return this.mApplicationContext;
    }

    @NonNull
    public String getName() {
        zzbqn();
        return this.mName;
    }

    @NonNull
    public FirebaseOptions getOptions() {
        zzbqn();
        return this.zzmas;
    }

    @KeepForSdk
    public Task<GetTokenResult> getToken(boolean z) {
        zzbqn();
        return this.zzmay == null ? Tasks.forException(new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.")) : this.zzmay.zzce(z);
    }

    @Nullable
    public final String getUid() throws FirebaseApiNotAvailableException {
        zzbqn();
        if (this.zzmay != null) {
            return this.zzmay.getUid();
        }
        throw new FirebaseApiNotAvailableException("firebase-auth is not linked, please fall back to unauthenticated mode.");
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public void setAutomaticResourceManagementEnabled(boolean z) {
        zzbqn();
        if (this.zzmat.compareAndSet(!z, z)) {
            boolean zzahc = zzk.zzahb().zzahc();
            if (z && zzahc) {
                zzcd(true);
            } else if (!z && zzahc) {
                zzcd(false);
            }
        }
    }

    public String toString() {
        return zzbg.zzx(this).zzg("name", this.mName).zzg("options", this.zzmas).toString();
    }

    public final void zza(@NonNull IdTokenListener idTokenListener) {
        zzbqn();
        zzbq.checkNotNull(idTokenListener);
        this.zzmav.add(idTokenListener);
        this.zzmaz.zzgj(this.zzmav.size());
    }

    public final void zza(zza zza2) {
        zzbqn();
        if (this.zzmat.get() && zzk.zzahb().zzahc()) {
            zza2.zzbf(true);
        }
        this.zzmaw.add(zza2);
    }

    public final void zza(@NonNull zzb zzb2) {
        this.zzmaz = (zzb) zzbq.checkNotNull(zzb2);
        this.zzmaz.zzgj(this.zzmav.size());
    }

    public final void zza(@NonNull InternalTokenProvider internalTokenProvider) {
        this.zzmay = (InternalTokenProvider) zzbq.checkNotNull(internalTokenProvider);
    }

    @UiThread
    public final void zza(@NonNull com.google.firebase.internal.zzc zzc2) {
        Log.d("FirebaseApp", "Notifying auth state listeners.");
        int i = 0;
        for (IdTokenListener zzb2 : this.zzmav) {
            zzb2.zzb(zzc2);
            i++;
        }
        Log.d("FirebaseApp", String.format("Notified %d auth state listeners.", new Object[]{Integer.valueOf(i)}));
    }

    public final void zzb(@NonNull IdTokenListener idTokenListener) {
        zzbqn();
        zzbq.checkNotNull(idTokenListener);
        this.zzmav.remove(idTokenListener);
        this.zzmaz.zzgj(this.zzmav.size());
    }

    public final boolean zzbqo() {
        return DEFAULT_APP_NAME.equals(getName());
    }

    public final String zzbqp() {
        String zzm = com.google.android.gms.common.util.zzb.zzm(getName().getBytes());
        String zzm2 = com.google.android.gms.common.util.zzb.zzm(getOptions().getApplicationId().getBytes());
        StringBuilder sb = new StringBuilder(String.valueOf(zzm).length() + 1 + String.valueOf(zzm2).length());
        sb.append(zzm);
        sb.append("+");
        sb.append(zzm2);
        return sb.toString();
    }
}
