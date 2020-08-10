package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.zzf;
import com.google.android.gms.internal.zzbha;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.internal.zzcxe;
import com.google.android.gms.tasks.OnCompleteListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public final class zzaa implements zzcc {
    private final Looper zzall;
    private final zzbm zzfmi;
    /* access modifiers changed from: private */
    public final Lock zzfps;
    private final zzr zzfpx;
    /* access modifiers changed from: private */
    public final Map<zzc<?>, zzz<?>> zzfpy = new HashMap();
    /* access modifiers changed from: private */
    public final Map<zzc<?>, zzz<?>> zzfpz = new HashMap();
    private final Map<Api<?>, Boolean> zzfqa;
    /* access modifiers changed from: private */
    public final zzba zzfqb;
    private final zzf zzfqc;
    /* access modifiers changed from: private */
    public final Condition zzfqd;
    private final boolean zzfqe;
    /* access modifiers changed from: private */
    public final boolean zzfqf;
    private final Queue<zzm<?, ?>> zzfqg = new LinkedList();
    /* access modifiers changed from: private */
    public boolean zzfqh;
    /* access modifiers changed from: private */
    public Map<zzh<?>, ConnectionResult> zzfqi;
    /* access modifiers changed from: private */
    public Map<zzh<?>, ConnectionResult> zzfqj;
    private zzad zzfqk;
    /* access modifiers changed from: private */
    public ConnectionResult zzfql;

    public zzaa(Context context, Lock lock, Looper looper, zzf zzf, Map<zzc<?>, zze> map, zzr zzr, Map<Api<?>, Boolean> map2, zza<? extends zzcxd, zzcxe> zza, ArrayList<zzt> arrayList, zzba zzba, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        this.zzfps = lock;
        Looper looper2 = looper;
        this.zzall = looper2;
        this.zzfqd = lock.newCondition();
        this.zzfqc = zzf;
        this.zzfqb = zzba;
        this.zzfqa = map2;
        zzr zzr2 = zzr;
        this.zzfpx = zzr2;
        this.zzfqe = z;
        HashMap hashMap = new HashMap();
        for (Api api : map2.keySet()) {
            hashMap.put(api.zzagf(), api);
        }
        HashMap hashMap2 = new HashMap();
        ArrayList arrayList2 = arrayList;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            zzt zzt = (zzt) obj;
            hashMap2.put(zzt.zzfin, zzt);
        }
        boolean z5 = false;
        boolean z6 = true;
        boolean z7 = false;
        for (Entry entry : map.entrySet()) {
            Api api2 = (Api) hashMap.get(entry.getKey());
            zze zze = (zze) entry.getValue();
            if (zze.zzagg()) {
                if (!((Boolean) this.zzfqa.get(api2)).booleanValue()) {
                    z3 = z6;
                    z4 = true;
                } else {
                    z3 = z6;
                    z4 = z7;
                }
                z2 = true;
            } else {
                z2 = z5;
                z4 = z7;
                z3 = false;
            }
            zzz zzz = r1;
            zze zze2 = zze;
            Entry entry2 = entry;
            zzz zzz2 = new zzz(context, api2, looper2, zze, (zzt) hashMap2.get(api2), zzr2, zza);
            this.zzfpy.put((zzc) entry2.getKey(), zzz);
            if (zze2.zzaay()) {
                this.zzfpz.put((zzc) entry2.getKey(), zzz);
            }
            z7 = z4;
            z6 = z3;
            z5 = z2;
            looper2 = looper;
        }
        this.zzfqf = z5 && !z6 && !z7;
        this.zzfmi = zzbm.zzaiq();
    }

    /* access modifiers changed from: private */
    public final boolean zza(zzz<?> zzz, ConnectionResult connectionResult) {
        return !connectionResult.isSuccess() && !connectionResult.hasResolution() && ((Boolean) this.zzfqa.get(zzz.zzagl())).booleanValue() && zzz.zzahp().zzagg() && this.zzfqc.isUserResolvableError(connectionResult.getErrorCode());
    }

    private final boolean zzahq() {
        this.zzfps.lock();
        try {
            if (this.zzfqh) {
                if (this.zzfqe) {
                    for (zzc zzb : this.zzfpz.keySet()) {
                        ConnectionResult zzb2 = zzb(zzb);
                        if (zzb2 != null) {
                            if (!zzb2.isSuccess()) {
                            }
                        }
                    }
                    this.zzfps.unlock();
                    return true;
                }
            }
            return false;
        } finally {
            this.zzfps.unlock();
        }
    }

    /* access modifiers changed from: private */
    public final void zzahr() {
        if (this.zzfpx == null) {
            this.zzfqb.zzfsc = Collections.emptySet();
            return;
        }
        HashSet hashSet = new HashSet(this.zzfpx.zzakv());
        Map zzakx = this.zzfpx.zzakx();
        for (Api api : zzakx.keySet()) {
            ConnectionResult connectionResult = getConnectionResult(api);
            if (connectionResult != null && connectionResult.isSuccess()) {
                hashSet.addAll(((zzt) zzakx.get(api)).zzehs);
            }
        }
        this.zzfqb.zzfsc = hashSet;
    }

    /* access modifiers changed from: private */
    public final void zzahs() {
        while (!this.zzfqg.isEmpty()) {
            zze((T) (zzm) this.zzfqg.remove());
        }
        this.zzfqb.zzj(null);
    }

    /* access modifiers changed from: private */
    @Nullable
    public final ConnectionResult zzaht() {
        ConnectionResult connectionResult = null;
        ConnectionResult connectionResult2 = null;
        int i = 0;
        int i2 = 0;
        for (zzz zzz : this.zzfpy.values()) {
            Api zzagl = zzz.zzagl();
            ConnectionResult connectionResult3 = (ConnectionResult) this.zzfqi.get(zzz.zzagn());
            if (!connectionResult3.isSuccess() && (!((Boolean) this.zzfqa.get(zzagl)).booleanValue() || connectionResult3.hasResolution() || this.zzfqc.isUserResolvableError(connectionResult3.getErrorCode()))) {
                if (connectionResult3.getErrorCode() != 4 || !this.zzfqe) {
                    int priority = zzagl.zzagd().getPriority();
                    if (connectionResult == null || i > priority) {
                        connectionResult = connectionResult3;
                        i = priority;
                    }
                } else {
                    int priority2 = zzagl.zzagd().getPriority();
                    if (connectionResult2 == null || i2 > priority2) {
                        connectionResult2 = connectionResult3;
                        i2 = priority2;
                    }
                }
            }
        }
        return (connectionResult == null || connectionResult2 == null || i <= i2) ? connectionResult : connectionResult2;
    }

    @Nullable
    private final ConnectionResult zzb(@NonNull zzc<?> zzc) {
        this.zzfps.lock();
        try {
            zzz zzz = (zzz) this.zzfpy.get(zzc);
            if (this.zzfqi != null && zzz != null) {
                return (ConnectionResult) this.zzfqi.get(zzz.zzagn());
            }
            this.zzfps.unlock();
            return null;
        } finally {
            this.zzfps.unlock();
        }
    }

    private final <T extends zzm<? extends Result, ? extends zzb>> boolean zzg(@NonNull T t) {
        zzc zzagf = t.zzagf();
        ConnectionResult zzb = zzb(zzagf);
        if (zzb == null || zzb.getErrorCode() != 4) {
            return false;
        }
        t.zzu(new Status(4, null, this.zzfmi.zza(((zzz) this.zzfpy.get(zzagf)).zzagn(), System.identityHashCode(this.zzfqb))));
        return true;
    }

    public final ConnectionResult blockingConnect() {
        connect();
        while (isConnecting()) {
            try {
                this.zzfqd.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ConnectionResult(15, null);
            }
        }
        return isConnected() ? ConnectionResult.zzfkr : this.zzfql != null ? this.zzfql : new ConnectionResult(13, null);
    }

    public final ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        connect();
        long nanos = timeUnit.toNanos(j);
        while (isConnecting()) {
            if (nanos <= 0) {
                try {
                    disconnect();
                    return new ConnectionResult(14, null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return new ConnectionResult(15, null);
                }
            } else {
                nanos = this.zzfqd.awaitNanos(nanos);
            }
        }
        return isConnected() ? ConnectionResult.zzfkr : this.zzfql != null ? this.zzfql : new ConnectionResult(13, null);
    }

    public final void connect() {
        this.zzfps.lock();
        try {
            if (!this.zzfqh) {
                this.zzfqh = true;
                this.zzfqi = null;
                this.zzfqj = null;
                this.zzfqk = null;
                this.zzfql = null;
                this.zzfmi.zzagz();
                this.zzfmi.zza((Iterable<? extends GoogleApi<?>>) this.zzfpy.values()).addOnCompleteListener((Executor) new zzbha(this.zzall), (OnCompleteListener<TResult>) new zzac<TResult>(this));
            }
        } finally {
            this.zzfps.unlock();
        }
    }

    public final void disconnect() {
        this.zzfps.lock();
        try {
            this.zzfqh = false;
            this.zzfqi = null;
            this.zzfqj = null;
            if (this.zzfqk != null) {
                this.zzfqk.cancel();
                this.zzfqk = null;
            }
            this.zzfql = null;
            while (!this.zzfqg.isEmpty()) {
                zzm zzm = (zzm) this.zzfqg.remove();
                zzm.zza((zzdm) null);
                zzm.cancel();
            }
            this.zzfqd.signalAll();
        } finally {
            this.zzfps.unlock();
        }
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    @Nullable
    public final ConnectionResult getConnectionResult(@NonNull Api<?> api) {
        return zzb(api.zzagf());
    }

    public final boolean isConnected() {
        this.zzfps.lock();
        try {
            return this.zzfqi != null && this.zzfql == null;
        } finally {
            this.zzfps.unlock();
        }
    }

    public final boolean isConnecting() {
        this.zzfps.lock();
        try {
            return this.zzfqi == null && this.zzfqh;
        } finally {
            this.zzfps.unlock();
        }
    }

    /* JADX INFO: finally extract failed */
    public final boolean zza(zzcu zzcu) {
        this.zzfps.lock();
        try {
            if (!this.zzfqh || zzahq()) {
                this.zzfps.unlock();
                return false;
            }
            this.zzfmi.zzagz();
            this.zzfqk = new zzad(this, zzcu);
            this.zzfmi.zza((Iterable<? extends GoogleApi<?>>) this.zzfpz.values()).addOnCompleteListener((Executor) new zzbha(this.zzall), (OnCompleteListener<TResult>) this.zzfqk);
            this.zzfps.unlock();
            return true;
        } catch (Throwable th) {
            this.zzfps.unlock();
            throw th;
        }
    }

    public final void zzags() {
        this.zzfps.lock();
        try {
            this.zzfmi.zzags();
            if (this.zzfqk != null) {
                this.zzfqk.cancel();
                this.zzfqk = null;
            }
            if (this.zzfqj == null) {
                this.zzfqj = new ArrayMap(this.zzfpz.size());
            }
            ConnectionResult connectionResult = new ConnectionResult(4);
            for (zzz zzagn : this.zzfpz.values()) {
                this.zzfqj.put(zzagn.zzagn(), connectionResult);
            }
            if (this.zzfqi != null) {
                this.zzfqi.putAll(this.zzfqj);
            }
        } finally {
            this.zzfps.unlock();
        }
    }

    public final void zzahk() {
    }

    public final <A extends zzb, R extends Result, T extends zzm<R, A>> T zzd(@NonNull T t) {
        if (this.zzfqe && zzg(t)) {
            return t;
        }
        if (!isConnected()) {
            this.zzfqg.add(t);
            return t;
        }
        this.zzfqb.zzfsh.zzb(t);
        return ((zzz) this.zzfpy.get(t.zzagf())).zza(t);
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zze(@NonNull T t) {
        zzc zzagf = t.zzagf();
        if (this.zzfqe && zzg(t)) {
            return t;
        }
        this.zzfqb.zzfsh.zzb(t);
        return ((zzz) this.zzfpy.get(zzagf)).zzb(t);
    }
}
