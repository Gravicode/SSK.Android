package com.google.firebase.iid;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FirebaseInstanceId {
    private static Map<String, FirebaseInstanceId> zzifg = new ArrayMap();
    private static final long zznyp = TimeUnit.HOURS.toSeconds(8);
    private static zzy zznyq;
    private static ScheduledThreadPoolExecutor zznyr;
    private KeyPair zzifj;
    private final FirebaseApp zzmki;
    private final zzu zznys;
    private final zzv zznyt;
    private boolean zznyu = false;

    private FirebaseInstanceId(FirebaseApp firebaseApp) {
        this.zzmki = firebaseApp;
        if (zzu.zzf(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        this.zznys = new zzu(firebaseApp.getApplicationContext());
        this.zznyt = new zzv(firebaseApp.getApplicationContext(), this.zznys);
        zzz zzciu = zzciu();
        if (zzciu == null || zzciu.zzro(this.zznys.zzcjg()) || zznyq.zzcjm() != null) {
            startSync();
        }
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(@NonNull FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        synchronized (FirebaseInstanceId.class) {
            firebaseInstanceId = (FirebaseInstanceId) zzifg.get(firebaseApp.getOptions().getApplicationId());
            if (firebaseInstanceId == null) {
                if (zznyq == null) {
                    zznyq = new zzy(firebaseApp.getApplicationContext());
                }
                firebaseInstanceId = new FirebaseInstanceId(firebaseApp);
                zzifg.put(firebaseApp.getOptions().getApplicationId(), firebaseInstanceId);
            }
        }
        return firebaseInstanceId;
    }

    private final synchronized void startSync() {
        if (!this.zznyu) {
            zzcc(0);
        }
    }

    private final void zzavf() {
        zznyq.zzrl("");
        this.zzifj = null;
    }

    private final String zzb(String str, String str2, Bundle bundle) throws IOException {
        bundle.putString("scope", str2);
        bundle.putString("sender", str);
        bundle.putString("subtype", str);
        bundle.putString("appid", getId());
        bundle.putString("gmp_app_id", this.zzmki.getOptions().getApplicationId());
        bundle.putString("gmsv", Integer.toString(this.zznys.zzcji()));
        bundle.putString("osv", Integer.toString(VERSION.SDK_INT));
        bundle.putString("app_ver", this.zznys.zzcjg());
        bundle.putString("app_ver_name", this.zznys.zzcjh());
        bundle.putString("cliv", "fiid-11910000");
        Bundle zzad = this.zznyt.zzad(bundle);
        if (zzad == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String string = zzad.getString("registration_id");
        if (string == null) {
            string = zzad.getString("unregistered");
            if (string == null) {
                String string2 = zzad.getString("error");
                if (string2 != null) {
                    throw new IOException(string2);
                }
                String valueOf = String.valueOf(zzad);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 20);
                sb.append("Unexpected response ");
                sb.append(valueOf);
                Log.w("FirebaseInstanceId", sb.toString(), new Throwable());
                throw new IOException("SERVICE_NOT_AVAILABLE");
            }
        }
        if (!"RST".equals(string) && !string.startsWith("RST|")) {
            return string;
        }
        zzciy();
        throw new IOException("SERVICE_NOT_AVAILABLE");
    }

    static void zzb(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zznyr == null) {
                zznyr = new ScheduledThreadPoolExecutor(1);
            }
            zznyr.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    static zzy zzciw() {
        return zznyq;
    }

    static boolean zzcix() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    @WorkerThread
    public void deleteInstanceId() throws IOException {
        deleteToken("*", "*");
        zzavf();
    }

    @WorkerThread
    public void deleteToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        Bundle bundle = new Bundle();
        bundle.putString("delete", "1");
        zzb(str, str2, bundle);
        zznyq.zzf("", str, str2);
    }

    /* access modifiers changed from: 0000 */
    public final FirebaseApp getApp() {
        return this.zzmki;
    }

    public long getCreationTime() {
        return zznyq.zzrj("");
    }

    @WorkerThread
    public String getId() {
        if (this.zzifj == null) {
            this.zzifj = zznyq.zzrm("");
        }
        if (this.zzifj == null) {
            this.zzifj = zznyq.zzrk("");
        }
        return zzu.zzb(this.zzifj);
    }

    @Nullable
    public String getToken() {
        zzz zzciu = zzciu();
        if (zzciu == null || zzciu.zzro(this.zznys.zzcjg())) {
            startSync();
        }
        if (zzciu != null) {
            return zzciu.zzldj;
        }
        return null;
    }

    @WorkerThread
    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        zzz zzp = zznyq.zzp("", str, str2);
        if (zzp != null && !zzp.zzro(this.zznys.zzcjg())) {
            return zzp.zzldj;
        }
        String zzb = zzb(str, str2, new Bundle());
        if (zzb != null) {
            zznyq.zza("", str, str2, zzb, this.zznys.zzcjg());
        }
        return zzb;
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzcc(long j) {
        zzb(new zzaa(this, this.zznys, Math.min(Math.max(30, j << 1), zznyp)), j);
        this.zznyu = true;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public final zzz zzciu() {
        return zznyq.zzp("", zzu.zzf(this.zzmki), "*");
    }

    /* access modifiers changed from: 0000 */
    public final String zzciv() throws IOException {
        return getToken(zzu.zzf(this.zzmki), "*");
    }

    /* access modifiers changed from: 0000 */
    public final void zzciy() {
        zznyq.zzavj();
        zzavf();
        startSync();
    }

    /* access modifiers changed from: 0000 */
    public final void zzciz() {
        zznyq.zzia("");
        startSync();
    }

    /* access modifiers changed from: 0000 */
    public final synchronized void zzcr(boolean z) {
        this.zznyu = z;
    }

    public final synchronized void zzrf(String str) {
        zznyq.zzrf(str);
        startSync();
    }

    /* access modifiers changed from: 0000 */
    public final void zzrg(String str) throws IOException {
        zzz zzciu = zzciu();
        if (zzciu == null || zzciu.zzro(this.zznys.zzcjg())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        String str3 = zzciu.zzldj;
        String valueOf3 = String.valueOf("/topics/");
        String valueOf4 = String.valueOf(str);
        zzb(str3, valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3), bundle);
    }

    /* access modifiers changed from: 0000 */
    public final void zzrh(String str) throws IOException {
        zzz zzciu = zzciu();
        if (zzciu == null || zzciu.zzro(this.zznys.zzcjg())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        bundle.putString("delete", "1");
        String str3 = zzciu.zzldj;
        String valueOf3 = String.valueOf("/topics/");
        String valueOf4 = String.valueOf(str);
        zzb(str3, valueOf4.length() != 0 ? valueOf3.concat(valueOf4) : new String(valueOf3), bundle);
    }
}
