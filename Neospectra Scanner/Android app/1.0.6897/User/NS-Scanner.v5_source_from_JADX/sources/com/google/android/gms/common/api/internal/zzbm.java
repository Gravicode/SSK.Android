package com.google.android.gms.common.api.internal;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArraySet;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class zzbm implements Callback {
    /* access modifiers changed from: private */
    public static final Object sLock = new Object();
    public static final Status zzfsy = new Status(4, "Sign-out occurred while this API call was in progress.");
    /* access modifiers changed from: private */
    public static final Status zzfsz = new Status(4, "The user must be signed in to make this API call.");
    private static zzbm zzftb;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public final GoogleApiAvailability zzfmy;
    /* access modifiers changed from: private */
    public final Map<zzh<?>, zzbo<?>> zzfpy = new ConcurrentHashMap(5, 0.75f, 1);
    /* access modifiers changed from: private */
    public long zzfrx = 120000;
    /* access modifiers changed from: private */
    public long zzfry = 5000;
    /* access modifiers changed from: private */
    public long zzfta = 10000;
    /* access modifiers changed from: private */
    public int zzftc = -1;
    private final AtomicInteger zzftd = new AtomicInteger(1);
    private final AtomicInteger zzfte = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public zzah zzftf = null;
    /* access modifiers changed from: private */
    public final Set<zzh<?>> zzftg = new ArraySet();
    private final Set<zzh<?>> zzfth = new ArraySet();

    private zzbm(Context context, Looper looper, GoogleApiAvailability googleApiAvailability) {
        this.mContext = context;
        this.mHandler = new Handler(looper, this);
        this.zzfmy = googleApiAvailability;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6));
    }

    public static zzbm zzaiq() {
        zzbm zzbm;
        synchronized (sLock) {
            zzbq.checkNotNull(zzftb, "Must guarantee manager is non-null before using getInstance");
            zzbm = zzftb;
        }
        return zzbm;
    }

    public static void zzair() {
        synchronized (sLock) {
            if (zzftb != null) {
                zzbm zzbm = zzftb;
                zzbm.zzfte.incrementAndGet();
                zzbm.mHandler.sendMessageAtFrontOfQueue(zzbm.mHandler.obtainMessage(10));
            }
        }
    }

    @WorkerThread
    private final void zzait() {
        for (zzh remove : this.zzfth) {
            ((zzbo) this.zzfpy.remove(remove)).signOut();
        }
        this.zzfth.clear();
    }

    @WorkerThread
    private final void zzb(GoogleApi<?> googleApi) {
        zzh zzagn = googleApi.zzagn();
        zzbo zzbo = (zzbo) this.zzfpy.get(zzagn);
        if (zzbo == null) {
            zzbo = new zzbo(this, googleApi);
            this.zzfpy.put(zzagn, zzbo);
        }
        if (zzbo.zzaay()) {
            this.zzfth.add(zzagn);
        }
        zzbo.connect();
    }

    public static zzbm zzcj(Context context) {
        zzbm zzbm;
        synchronized (sLock) {
            if (zzftb == null) {
                HandlerThread handlerThread = new HandlerThread("GoogleApiHandler", 9);
                handlerThread.start();
                zzftb = new zzbm(context.getApplicationContext(), handlerThread.getLooper(), GoogleApiAvailability.getInstance());
            }
            zzbm = zzftb;
        }
        return zzbm;
    }

    @WorkerThread
    public final boolean handleMessage(Message message) {
        zzbo zzbo;
        long j = 300000;
        switch (message.what) {
            case 1:
                if (((Boolean) message.obj).booleanValue()) {
                    j = 10000;
                }
                this.zzfta = j;
                this.mHandler.removeMessages(12);
                for (zzh obtainMessage : this.zzfpy.keySet()) {
                    this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(12, obtainMessage), this.zzfta);
                }
                break;
            case 2:
                zzj zzj = (zzj) message.obj;
                for (zzh zzh : zzj.zzaha()) {
                    zzbo zzbo2 = (zzbo) this.zzfpy.get(zzh);
                    if (zzbo2 == null) {
                        zzj.zza(zzh, new ConnectionResult(13), null);
                        return true;
                    } else if (zzbo2.isConnected()) {
                        zzj.zza(zzh, ConnectionResult.zzfkr, zzbo2.zzahp().zzagi());
                    } else if (zzbo2.zzaja() != null) {
                        zzj.zza(zzh, zzbo2.zzaja(), null);
                    } else {
                        zzbo2.zza(zzj);
                    }
                }
                break;
            case 3:
                for (zzbo zzbo3 : this.zzfpy.values()) {
                    zzbo3.zzaiz();
                    zzbo3.connect();
                }
                break;
            case 4:
            case 8:
            case 13:
                zzcp zzcp = (zzcp) message.obj;
                zzbo zzbo4 = (zzbo) this.zzfpy.get(zzcp.zzfur.zzagn());
                if (zzbo4 == null) {
                    zzb(zzcp.zzfur);
                    zzbo4 = (zzbo) this.zzfpy.get(zzcp.zzfur.zzagn());
                }
                if (!zzbo4.zzaay() || this.zzfte.get() == zzcp.zzfuq) {
                    zzbo4.zza(zzcp.zzfup);
                    return true;
                }
                zzcp.zzfup.zzs(zzfsy);
                zzbo4.signOut();
                return true;
            case 5:
                int i = message.arg1;
                ConnectionResult connectionResult = (ConnectionResult) message.obj;
                Iterator it = this.zzfpy.values().iterator();
                while (true) {
                    if (it.hasNext()) {
                        zzbo = (zzbo) it.next();
                        if (zzbo.getInstanceId() == i) {
                        }
                    } else {
                        zzbo = null;
                    }
                }
                if (zzbo != null) {
                    String errorString = this.zzfmy.getErrorString(connectionResult.getErrorCode());
                    String errorMessage = connectionResult.getErrorMessage();
                    StringBuilder sb = new StringBuilder(String.valueOf(errorString).length() + 69 + String.valueOf(errorMessage).length());
                    sb.append("Error resolution was canceled by the user, original error message: ");
                    sb.append(errorString);
                    sb.append(": ");
                    sb.append(errorMessage);
                    zzbo.zzw(new Status(17, sb.toString()));
                    return true;
                }
                StringBuilder sb2 = new StringBuilder(76);
                sb2.append("Could not find API instance ");
                sb2.append(i);
                sb2.append(" while trying to fail enqueued calls.");
                Log.wtf("GoogleApiManager", sb2.toString(), new Exception());
                return true;
            case 6:
                if (this.mContext.getApplicationContext() instanceof Application) {
                    zzk.zza((Application) this.mContext.getApplicationContext());
                    zzk.zzahb().zza((zzl) new zzbn(this));
                    if (!zzk.zzahb().zzbe(true)) {
                        this.zzfta = 300000;
                        return true;
                    }
                }
                break;
            case 7:
                zzb((GoogleApi) message.obj);
                return true;
            case 9:
                if (this.zzfpy.containsKey(message.obj)) {
                    ((zzbo) this.zzfpy.get(message.obj)).resume();
                    return true;
                }
                break;
            case 10:
                zzait();
                return true;
            case 11:
                if (this.zzfpy.containsKey(message.obj)) {
                    ((zzbo) this.zzfpy.get(message.obj)).zzaij();
                    return true;
                }
                break;
            case 12:
                if (this.zzfpy.containsKey(message.obj)) {
                    ((zzbo) this.zzfpy.get(message.obj)).zzajd();
                    return true;
                }
                break;
            default:
                int i2 = message.what;
                StringBuilder sb3 = new StringBuilder(31);
                sb3.append("Unknown message id: ");
                sb3.append(i2);
                Log.w("GoogleApiManager", sb3.toString());
                return false;
        }
        return true;
    }

    /* access modifiers changed from: 0000 */
    public final PendingIntent zza(zzh<?> zzh, int i) {
        zzbo zzbo = (zzbo) this.zzfpy.get(zzh);
        if (zzbo == null) {
            return null;
        }
        zzcxd zzaje = zzbo.zzaje();
        if (zzaje == null) {
            return null;
        }
        return PendingIntent.getActivity(this.mContext, i, zzaje.getSignInIntent(), 134217728);
    }

    public final <O extends ApiOptions> Task<Boolean> zza(@NonNull GoogleApi<O> googleApi, @NonNull zzck<?> zzck) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(13, new zzcp(new zzf(zzck, taskCompletionSource), this.zzfte.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final <O extends ApiOptions> Task<Void> zza(@NonNull GoogleApi<O> googleApi, @NonNull zzcq<zzb, ?> zzcq, @NonNull zzdn<zzb, ?> zzdn) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(8, new zzcp(new zzd(new zzcr(zzcq, zzdn), taskCompletionSource), this.zzfte.get(), googleApi)));
        return taskCompletionSource.getTask();
    }

    public final Task<Map<zzh<?>, String>> zza(Iterable<? extends GoogleApi<?>> iterable) {
        zzj zzj = new zzj(iterable);
        for (GoogleApi googleApi : iterable) {
            zzbo zzbo = (zzbo) this.zzfpy.get(googleApi.zzagn());
            if (zzbo == null || !zzbo.isConnected()) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(2, zzj));
                return zzj.getTask();
            }
            zzj.zza(googleApi.zzagn(), ConnectionResult.zzfkr, zzbo.zzahp().zzagi());
        }
        return zzj.getTask();
    }

    public final void zza(ConnectionResult connectionResult, int i) {
        if (!zzc(connectionResult, i)) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(5, i, 0, connectionResult));
        }
    }

    public final void zza(GoogleApi<?> googleApi) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, googleApi));
    }

    public final <O extends ApiOptions, TResult> void zza(GoogleApi<O> googleApi, int i, zzdd<zzb, TResult> zzdd, TaskCompletionSource<TResult> taskCompletionSource, zzcz zzcz) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, new zzcp(new zze(i, zzdd, taskCompletionSource, zzcz), this.zzfte.get(), googleApi)));
    }

    public final <O extends ApiOptions> void zza(GoogleApi<O> googleApi, int i, zzm<? extends Result, zzb> zzm) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, new zzcp(new zzc(i, zzm), this.zzfte.get(), googleApi)));
    }

    public final void zza(@NonNull zzah zzah) {
        synchronized (sLock) {
            if (this.zzftf != zzah) {
                this.zzftf = zzah;
                this.zzftg.clear();
                this.zzftg.addAll(zzah.zzahx());
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zzags() {
        this.zzfte.incrementAndGet();
        this.mHandler.sendMessage(this.mHandler.obtainMessage(10));
    }

    public final void zzagz() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3));
    }

    public final int zzais() {
        return this.zzftd.getAndIncrement();
    }

    /* access modifiers changed from: 0000 */
    public final void zzb(@NonNull zzah zzah) {
        synchronized (sLock) {
            if (this.zzftf == zzah) {
                this.zzftf = null;
                this.zzftg.clear();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzc(ConnectionResult connectionResult, int i) {
        return this.zzfmy.zza(this.mContext, connectionResult, i);
    }
}
