package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.zzc;
import com.google.android.gms.common.zzf;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class zzd<T extends IInterface> {
    private static String[] zzfyy = {"service_esmobile", "service_googleme"};
    private final Context mContext;
    final Handler mHandler;
    private final Object mLock;
    private final Looper zzall;
    private final zzf zzfqc;
    private int zzfyd;
    private long zzfye;
    private long zzfyf;
    private int zzfyg;
    private long zzfyh;
    private zzam zzfyi;
    private final zzag zzfyj;
    /* access modifiers changed from: private */
    public final Object zzfyk;
    /* access modifiers changed from: private */
    public zzay zzfyl;
    protected zzj zzfym;
    private T zzfyn;
    /* access modifiers changed from: private */
    public final ArrayList<zzi<?>> zzfyo;
    private zzl zzfyp;
    private int zzfyq;
    /* access modifiers changed from: private */
    public final zzf zzfyr;
    /* access modifiers changed from: private */
    public final zzg zzfys;
    private final int zzfyt;
    private final String zzfyu;
    /* access modifiers changed from: private */
    public ConnectionResult zzfyv;
    /* access modifiers changed from: private */
    public boolean zzfyw;
    protected AtomicInteger zzfyx;

    protected zzd(Context context, Looper looper, int i, zzf zzf, zzg zzg, String str) {
        this(context, looper, zzag.zzco(context), zzf.zzafy(), i, (zzf) zzbq.checkNotNull(zzf), (zzg) zzbq.checkNotNull(zzg), null);
    }

    protected zzd(Context context, Looper looper, zzag zzag, zzf zzf, int i, zzf zzf2, zzg zzg, String str) {
        this.mLock = new Object();
        this.zzfyk = new Object();
        this.zzfyo = new ArrayList<>();
        this.zzfyq = 1;
        this.zzfyv = null;
        this.zzfyw = false;
        this.zzfyx = new AtomicInteger(0);
        this.mContext = (Context) zzbq.checkNotNull(context, "Context must not be null");
        this.zzall = (Looper) zzbq.checkNotNull(looper, "Looper must not be null");
        this.zzfyj = (zzag) zzbq.checkNotNull(zzag, "Supervisor must not be null");
        this.zzfqc = (zzf) zzbq.checkNotNull(zzf, "API availability must not be null");
        this.mHandler = new zzh(this, looper);
        this.zzfyt = i;
        this.zzfyr = zzf2;
        this.zzfys = zzg;
        this.zzfyu = str;
    }

    /* access modifiers changed from: private */
    public final void zza(int i, T t) {
        boolean z = true;
        if ((i == 4) != (t != null)) {
            z = false;
        }
        zzbq.checkArgument(z);
        synchronized (this.mLock) {
            this.zzfyq = i;
            this.zzfyn = t;
            switch (i) {
                case 1:
                    if (this.zzfyp != null) {
                        this.zzfyj.zza(zzhi(), zzakh(), 129, this.zzfyp, zzaki());
                        this.zzfyp = null;
                        break;
                    }
                    break;
                case 2:
                case 3:
                    if (!(this.zzfyp == null || this.zzfyi == null)) {
                        String zzalo = this.zzfyi.zzalo();
                        String packageName = this.zzfyi.getPackageName();
                        StringBuilder sb = new StringBuilder(String.valueOf(zzalo).length() + 70 + String.valueOf(packageName).length());
                        sb.append("Calling connect() while still connected, missing disconnect() for ");
                        sb.append(zzalo);
                        sb.append(" on ");
                        sb.append(packageName);
                        Log.e("GmsClient", sb.toString());
                        this.zzfyj.zza(this.zzfyi.zzalo(), this.zzfyi.getPackageName(), this.zzfyi.zzalk(), this.zzfyp, zzaki());
                        this.zzfyx.incrementAndGet();
                    }
                    this.zzfyp = new zzl(this, this.zzfyx.get());
                    this.zzfyi = new zzam(zzakh(), zzhi(), false, 129);
                    if (!this.zzfyj.zza(new zzah(this.zzfyi.zzalo(), this.zzfyi.getPackageName(), this.zzfyi.zzalk()), (ServiceConnection) this.zzfyp, zzaki())) {
                        String zzalo2 = this.zzfyi.zzalo();
                        String packageName2 = this.zzfyi.getPackageName();
                        StringBuilder sb2 = new StringBuilder(String.valueOf(zzalo2).length() + 34 + String.valueOf(packageName2).length());
                        sb2.append("unable to connect to service: ");
                        sb2.append(zzalo2);
                        sb2.append(" on ");
                        sb2.append(packageName2);
                        Log.e("GmsClient", sb2.toString());
                        zza(16, (Bundle) null, this.zzfyx.get());
                        break;
                    }
                    break;
                case 4:
                    zza(t);
                    break;
            }
        }
    }

    /* access modifiers changed from: private */
    public final boolean zza(int i, int i2, T t) {
        synchronized (this.mLock) {
            if (this.zzfyq != i) {
                return false;
            }
            zza(i2, t);
            return true;
        }
    }

    @Nullable
    private final String zzaki() {
        return this.zzfyu == null ? this.mContext.getClass().getName() : this.zzfyu;
    }

    private final boolean zzakk() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzfyq == 3;
        }
        return z;
    }

    /* access modifiers changed from: private */
    public final boolean zzakq() {
        if (this.zzfyw || TextUtils.isEmpty(zzhj()) || TextUtils.isEmpty(null)) {
            return false;
        }
        try {
            Class.forName(zzhj());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public final void zzcf(int i) {
        int i2;
        if (zzakk()) {
            i2 = 5;
            this.zzfyw = true;
        } else {
            i2 = 4;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(i2, this.zzfyx.get(), 16));
    }

    public void disconnect() {
        this.zzfyx.incrementAndGet();
        synchronized (this.zzfyo) {
            int size = this.zzfyo.size();
            for (int i = 0; i < size; i++) {
                ((zzi) this.zzfyo.get(i)).removeListener();
            }
            this.zzfyo.clear();
        }
        synchronized (this.zzfyk) {
            this.zzfyl = null;
        }
        zza(1, (T) null);
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        T t;
        zzay zzay;
        String str2;
        String str3;
        synchronized (this.mLock) {
            i = this.zzfyq;
            t = this.zzfyn;
        }
        synchronized (this.zzfyk) {
            zzay = this.zzfyl;
        }
        printWriter.append(str).append("mConnectState=");
        switch (i) {
            case 1:
                str2 = "DISCONNECTED";
                break;
            case 2:
                str2 = "REMOTE_CONNECTING";
                break;
            case 3:
                str2 = "LOCAL_CONNECTING";
                break;
            case 4:
                str2 = "CONNECTED";
                break;
            case 5:
                str2 = "DISCONNECTING";
                break;
            default:
                str2 = "UNKNOWN";
                break;
        }
        printWriter.print(str2);
        printWriter.append(" mService=");
        if (t == null) {
            printWriter.append("null");
        } else {
            printWriter.append(zzhj()).append("@").append(Integer.toHexString(System.identityHashCode(t.asBinder())));
        }
        printWriter.append(" mServiceBroker=");
        if (zzay == null) {
            printWriter.println("null");
        } else {
            printWriter.append("IGmsServiceBroker@").println(Integer.toHexString(System.identityHashCode(zzay.asBinder())));
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
        if (this.zzfyf > 0) {
            PrintWriter append = printWriter.append(str).append("lastConnectedTime=");
            long j = this.zzfyf;
            String format = simpleDateFormat.format(new Date(this.zzfyf));
            StringBuilder sb = new StringBuilder(String.valueOf(format).length() + 21);
            sb.append(j);
            sb.append(" ");
            sb.append(format);
            append.println(sb.toString());
        }
        if (this.zzfye > 0) {
            printWriter.append(str).append("lastSuspendedCause=");
            switch (this.zzfyd) {
                case 1:
                    str3 = "CAUSE_SERVICE_DISCONNECTED";
                    break;
                case 2:
                    str3 = "CAUSE_NETWORK_LOST";
                    break;
                default:
                    str3 = String.valueOf(this.zzfyd);
                    break;
            }
            printWriter.append(str3);
            PrintWriter append2 = printWriter.append(" lastSuspendedTime=");
            long j2 = this.zzfye;
            String format2 = simpleDateFormat.format(new Date(this.zzfye));
            StringBuilder sb2 = new StringBuilder(String.valueOf(format2).length() + 21);
            sb2.append(j2);
            sb2.append(" ");
            sb2.append(format2);
            append2.println(sb2.toString());
        }
        if (this.zzfyh > 0) {
            printWriter.append(str).append("lastFailedStatus=").append(CommonStatusCodes.getStatusCodeString(this.zzfyg));
            PrintWriter append3 = printWriter.append(" lastFailedTime=");
            long j3 = this.zzfyh;
            String format3 = simpleDateFormat.format(new Date(this.zzfyh));
            StringBuilder sb3 = new StringBuilder(String.valueOf(format3).length() + 21);
            sb3.append(j3);
            sb3.append(" ");
            sb3.append(format3);
            append3.println(sb3.toString());
        }
    }

    public Account getAccount() {
        return null;
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzall;
    }

    public Intent getSignInIntent() {
        throw new UnsupportedOperationException("Not a sign in API");
    }

    public final boolean isConnected() {
        boolean z;
        synchronized (this.mLock) {
            z = this.zzfyq == 4;
        }
        return z;
    }

    public final boolean isConnecting() {
        boolean z;
        synchronized (this.mLock) {
            if (this.zzfyq != 2) {
                if (this.zzfyq != 3) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzfyg = connectionResult.getErrorCode();
        this.zzfyh = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void onConnectionSuspended(int i) {
        this.zzfyd = i;
        this.zzfye = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public final void zza(int i, @Nullable Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(7, i2, -1, new zzo(this, i, null)));
    }

    /* access modifiers changed from: protected */
    public void zza(int i, IBinder iBinder, Bundle bundle, int i2) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, i2, -1, new zzn(this, i, iBinder, bundle)));
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void zza(@NonNull T t) {
        this.zzfyf = System.currentTimeMillis();
    }

    @WorkerThread
    public final void zza(zzan zzan, Set<Scope> set) {
        Bundle zzaap = zzaap();
        zzz zzz = new zzz(this.zzfyt);
        zzz.zzfzt = this.mContext.getPackageName();
        zzz.zzfzw = zzaap;
        if (set != null) {
            zzz.zzfzv = (Scope[]) set.toArray(new Scope[set.size()]);
        }
        if (zzaay()) {
            zzz.zzfzx = getAccount() != null ? getAccount() : new Account("<<default account>>", "com.google");
            if (zzan != null) {
                zzz.zzfzu = zzan.asBinder();
            }
        } else if (zzako()) {
            zzz.zzfzx = getAccount();
        }
        zzz.zzfzy = zzakl();
        try {
            synchronized (this.zzfyk) {
                if (this.zzfyl != null) {
                    this.zzfyl.zza(new zzk(this, this.zzfyx.get()), zzz);
                } else {
                    Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                }
            }
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
            zzce(1);
        } catch (SecurityException e2) {
            throw e2;
        } catch (RemoteException | RuntimeException e3) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e3);
            zza(8, (IBinder) null, (Bundle) null, this.zzfyx.get());
        }
    }

    public void zza(@NonNull zzj zzj) {
        this.zzfym = (zzj) zzbq.checkNotNull(zzj, "Connection progress callbacks cannot be null.");
        zza(2, (T) null);
    }

    /* access modifiers changed from: protected */
    public final void zza(@NonNull zzj zzj, int i, @Nullable PendingIntent pendingIntent) {
        this.zzfym = (zzj) zzbq.checkNotNull(zzj, "Connection progress callbacks cannot be null.");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, this.zzfyx.get(), i, pendingIntent));
    }

    public void zza(@NonNull zzp zzp) {
        zzp.zzajf();
    }

    /* access modifiers changed from: protected */
    public Bundle zzaap() {
        return new Bundle();
    }

    public boolean zzaay() {
        return false;
    }

    public boolean zzabj() {
        return false;
    }

    public Bundle zzafi() {
        return null;
    }

    public boolean zzagg() {
        return true;
    }

    @Nullable
    public final IBinder zzagh() {
        synchronized (this.zzfyk) {
            if (this.zzfyl == null) {
                return null;
            }
            IBinder asBinder = this.zzfyl.asBinder();
            return asBinder;
        }
    }

    public final String zzagi() {
        if (isConnected() && this.zzfyi != null) {
            return this.zzfyi.getPackageName();
        }
        throw new RuntimeException("Failed to connect when checking package");
    }

    /* access modifiers changed from: protected */
    public String zzakh() {
        return "com.google.android.gms";
    }

    public final void zzakj() {
        int isGooglePlayServicesAvailable = this.zzfqc.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            zza(1, (T) null);
            zza((zzj) new zzm(this), isGooglePlayServicesAvailable, (PendingIntent) null);
            return;
        }
        zza((zzj) new zzm(this));
    }

    public zzc[] zzakl() {
        return new zzc[0];
    }

    /* access modifiers changed from: protected */
    public final void zzakm() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public final T zzakn() throws DeadObjectException {
        T t;
        synchronized (this.mLock) {
            if (this.zzfyq == 5) {
                throw new DeadObjectException();
            }
            zzakm();
            zzbq.zza(this.zzfyn != null, (Object) "Client is connected but service is null");
            t = this.zzfyn;
        }
        return t;
    }

    public boolean zzako() {
        return false;
    }

    /* access modifiers changed from: protected */
    public Set<Scope> zzakp() {
        return Collections.EMPTY_SET;
    }

    public final void zzce(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(6, this.zzfyx.get(), i));
    }

    /* access modifiers changed from: protected */
    @Nullable
    public abstract T zzd(IBinder iBinder);

    /* access modifiers changed from: protected */
    @NonNull
    public abstract String zzhi();

    /* access modifiers changed from: protected */
    @NonNull
    public abstract String zzhj();
}
