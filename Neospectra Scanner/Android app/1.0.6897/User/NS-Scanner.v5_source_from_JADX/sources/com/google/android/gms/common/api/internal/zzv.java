package com.google.android.gms.common.api.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.zzf;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.internal.zzcxe;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

final class zzv implements zzcc {
    private final Context mContext;
    private final Looper zzall;
    private final zzba zzfpi;
    /* access modifiers changed from: private */
    public final zzbi zzfpj;
    /* access modifiers changed from: private */
    public final zzbi zzfpk;
    private final Map<zzc<?>, zzbi> zzfpl;
    private final Set<zzcu> zzfpm = Collections.newSetFromMap(new WeakHashMap());
    private final zze zzfpn;
    private Bundle zzfpo;
    /* access modifiers changed from: private */
    public ConnectionResult zzfpp = null;
    /* access modifiers changed from: private */
    public ConnectionResult zzfpq = null;
    /* access modifiers changed from: private */
    public boolean zzfpr = false;
    /* access modifiers changed from: private */
    public final Lock zzfps;
    private int zzfpt = 0;

    private zzv(Context context, zzba zzba, Lock lock, Looper looper, zzf zzf, Map<zzc<?>, zze> map, Map<zzc<?>, zze> map2, zzr zzr, zza<? extends zzcxd, zzcxe> zza, zze zze, ArrayList<zzt> arrayList, ArrayList<zzt> arrayList2, Map<Api<?>, Boolean> map3, Map<Api<?>, Boolean> map4) {
        Context context2 = context;
        this.mContext = context2;
        this.zzfpi = zzba;
        Lock lock2 = lock;
        this.zzfps = lock2;
        Looper looper2 = looper;
        this.zzall = looper2;
        this.zzfpn = zze;
        Context context3 = context2;
        Lock lock3 = lock2;
        zzf zzf2 = zzf;
        zzbi zzbi = r3;
        zzbi zzbi2 = new zzbi(context3, this.zzfpi, lock3, looper2, zzf2, map2, null, map4, null, arrayList2, new zzx(this, null));
        this.zzfpj = zzbi;
        zzbi zzbi3 = new zzbi(context3, this.zzfpi, lock3, looper, zzf2, map, zzr, map3, zza, arrayList, new zzy(this, null));
        this.zzfpk = zzbi3;
        ArrayMap arrayMap = new ArrayMap();
        for (zzc put : map2.keySet()) {
            arrayMap.put(put, this.zzfpj);
        }
        for (zzc put2 : map.keySet()) {
            arrayMap.put(put2, this.zzfpk);
        }
        this.zzfpl = Collections.unmodifiableMap(arrayMap);
    }

    public static zzv zza(Context context, zzba zzba, Lock lock, Looper looper, zzf zzf, Map<zzc<?>, zze> map, zzr zzr, Map<Api<?>, Boolean> map2, zza<? extends zzcxd, zzcxe> zza, ArrayList<zzt> arrayList) {
        Map<Api<?>, Boolean> map3 = map2;
        ArrayMap arrayMap = new ArrayMap();
        ArrayMap arrayMap2 = new ArrayMap();
        zze zze = null;
        for (Entry entry : map.entrySet()) {
            zze zze2 = (zze) entry.getValue();
            if (zze2.zzabj()) {
                zze = zze2;
            }
            if (zze2.zzaay()) {
                arrayMap.put((zzc) entry.getKey(), zze2);
            } else {
                arrayMap2.put((zzc) entry.getKey(), zze2);
            }
        }
        zzbq.zza(!arrayMap.isEmpty(), (Object) "CompositeGoogleApiClient should not be used without any APIs that require sign-in.");
        ArrayMap arrayMap3 = new ArrayMap();
        ArrayMap arrayMap4 = new ArrayMap();
        for (Api api : map2.keySet()) {
            zzc zzagf = api.zzagf();
            if (arrayMap.containsKey(zzagf)) {
                arrayMap3.put(api, (Boolean) map3.get(api));
            } else if (arrayMap2.containsKey(zzagf)) {
                arrayMap4.put(api, (Boolean) map3.get(api));
            } else {
                throw new IllegalStateException("Each API in the isOptionalMap must have a corresponding client in the clients map.");
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = arrayList;
        int size = arrayList4.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList4.get(i);
            i++;
            zzt zzt = (zzt) obj;
            if (arrayMap3.containsKey(zzt.zzfin)) {
                arrayList2.add(zzt);
            } else if (arrayMap4.containsKey(zzt.zzfin)) {
                arrayList3.add(zzt);
            } else {
                throw new IllegalStateException("Each ClientCallbacks must have a corresponding API in the isOptionalMap");
            }
        }
        zzv zzv = new zzv(context, zzba, lock, looper, zzf, arrayMap, arrayMap2, zzr, zza, zze, arrayList2, arrayList3, arrayMap3, arrayMap4);
        return zzv;
    }

    private final void zza(ConnectionResult connectionResult) {
        switch (this.zzfpt) {
            case 1:
                break;
            case 2:
                this.zzfpi.zzc(connectionResult);
                break;
            default:
                Log.wtf("CompositeGAC", "Attempted to call failure callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new Exception());
                break;
        }
        zzahm();
        this.zzfpt = 0;
    }

    /* access modifiers changed from: private */
    public final void zzahl() {
        if (zzb(this.zzfpp)) {
            if (zzb(this.zzfpq) || zzahn()) {
                switch (this.zzfpt) {
                    case 1:
                        break;
                    case 2:
                        this.zzfpi.zzj(this.zzfpo);
                        break;
                    default:
                        Log.wtf("CompositeGAC", "Attempted to call success callbacks in CONNECTION_MODE_NONE. Callbacks should be disabled via GmsClientSupervisor", new AssertionError());
                        break;
                }
                zzahm();
                this.zzfpt = 0;
            } else if (this.zzfpq != null) {
                if (this.zzfpt == 1) {
                    zzahm();
                    return;
                }
                zza(this.zzfpq);
                this.zzfpj.disconnect();
            }
        } else if (this.zzfpp != null && zzb(this.zzfpq)) {
            this.zzfpk.disconnect();
            zza(this.zzfpp);
        } else if (!(this.zzfpp == null || this.zzfpq == null)) {
            ConnectionResult connectionResult = this.zzfpp;
            if (this.zzfpk.zzfst < this.zzfpj.zzfst) {
                connectionResult = this.zzfpq;
            }
            zza(connectionResult);
        }
    }

    private final void zzahm() {
        for (zzcu zzabi : this.zzfpm) {
            zzabi.zzabi();
        }
        this.zzfpm.clear();
    }

    private final boolean zzahn() {
        return this.zzfpq != null && this.zzfpq.getErrorCode() == 4;
    }

    @Nullable
    private final PendingIntent zzaho() {
        if (this.zzfpn == null) {
            return null;
        }
        return PendingIntent.getActivity(this.mContext, System.identityHashCode(this.zzfpi), this.zzfpn.getSignInIntent(), 134217728);
    }

    private static boolean zzb(ConnectionResult connectionResult) {
        return connectionResult != null && connectionResult.isSuccess();
    }

    /* access modifiers changed from: private */
    public final void zze(int i, boolean z) {
        this.zzfpi.zzf(i, z);
        this.zzfpq = null;
        this.zzfpp = null;
    }

    private final boolean zzf(zzm<? extends Result, ? extends zzb> zzm) {
        zzc zzagf = zzm.zzagf();
        zzbq.checkArgument(this.zzfpl.containsKey(zzagf), "GoogleApiClient is not configured to use the API required for this call.");
        return ((zzbi) this.zzfpl.get(zzagf)).equals(this.zzfpk);
    }

    /* access modifiers changed from: private */
    public final void zzi(Bundle bundle) {
        if (this.zzfpo == null) {
            this.zzfpo = bundle;
            return;
        }
        if (bundle != null) {
            this.zzfpo.putAll(bundle);
        }
    }

    public final ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException();
    }

    public final ConnectionResult blockingConnect(long j, @NonNull TimeUnit timeUnit) {
        throw new UnsupportedOperationException();
    }

    public final void connect() {
        this.zzfpt = 2;
        this.zzfpr = false;
        this.zzfpq = null;
        this.zzfpp = null;
        this.zzfpj.connect();
        this.zzfpk.connect();
    }

    public final void disconnect() {
        this.zzfpq = null;
        this.zzfpp = null;
        this.zzfpt = 0;
        this.zzfpj.disconnect();
        this.zzfpk.disconnect();
        zzahm();
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).append("authClient").println(":");
        this.zzfpk.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
        printWriter.append(str).append("anonClient").println(":");
        this.zzfpj.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
    }

    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return ((zzbi) this.zzfpl.get(api.zzagf())).equals(this.zzfpk) ? zzahn() ? new ConnectionResult(4, zzaho()) : this.zzfpk.getConnectionResult(api) : this.zzfpj.getConnectionResult(api);
    }

    public final boolean isConnected() {
        this.zzfps.lock();
        try {
            boolean z = true;
            if (!this.zzfpj.isConnected() || (!this.zzfpk.isConnected() && !zzahn() && this.zzfpt != 1)) {
                z = false;
            }
            return z;
        } finally {
            this.zzfps.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zzfps.lock();
        try {
            return this.zzfpt == 2;
        } finally {
            this.zzfps.unlock();
        }
    }

    public final boolean zza(zzcu zzcu) {
        this.zzfps.lock();
        try {
            if ((isConnecting() || isConnected()) && !this.zzfpk.isConnected()) {
                this.zzfpm.add(zzcu);
                if (this.zzfpt == 0) {
                    this.zzfpt = 1;
                }
                this.zzfpq = null;
                this.zzfpk.connect();
                return true;
            }
            this.zzfps.unlock();
            return false;
        } finally {
            this.zzfps.unlock();
        }
    }

    public final void zzags() {
        this.zzfps.lock();
        try {
            boolean isConnecting = isConnecting();
            this.zzfpk.disconnect();
            this.zzfpq = new ConnectionResult(4);
            if (isConnecting) {
                new Handler(this.zzall).post(new zzw(this));
            } else {
                zzahm();
            }
        } finally {
            this.zzfps.unlock();
        }
    }

    public final void zzahk() {
        this.zzfpj.zzahk();
        this.zzfpk.zzahk();
    }

    public final <A extends zzb, R extends Result, T extends zzm<R, A>> T zzd(@NonNull T t) {
        if (!zzf((zzm<? extends Result, ? extends zzb>) t)) {
            return this.zzfpj.zzd(t);
        }
        if (!zzahn()) {
            return this.zzfpk.zzd(t);
        }
        t.zzu(new Status(4, null, zzaho()));
        return t;
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zze(@NonNull T t) {
        if (!zzf((zzm<? extends Result, ? extends zzb>) t)) {
            return this.zzfpj.zze(t);
        }
        if (!zzahn()) {
            return this.zzfpk.zze(t);
        }
        t.zzu(new Status(4, null, zzaho()));
        return t;
    }
}
