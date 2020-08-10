package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.storage.StorageException;
import java.io.InputStream;
import org.json.JSONObject;

public final class zzeyc {
    private Exception zzkuq;
    private zzexx zzomc;
    private int zzomd;
    private Exception zzome;

    public zzeyc(@NonNull zzexx zzexx) {
        this.zzomc = zzexx;
    }

    @Nullable
    public final Exception getException() {
        try {
            return this.zzome != null ? this.zzome : this.zzkuq != null ? this.zzkuq : (Exception) zzn.zzx(this.zzomc.zzcml());
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "getException failed with a RemoteException:", e);
            return null;
        }
    }

    public final int getResultCode() {
        try {
            return this.zzomd != 0 ? this.zzomd : this.zzomc.getResultCode();
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "getResultCode failed with a RemoteException:", e);
            return 0;
        }
    }

    @Nullable
    public final InputStream getStream() {
        try {
            return (InputStream) zzn.zzx(this.zzomc.zzcmi());
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "getStream failed with a RemoteException:", e);
            return null;
        }
    }

    public final void reset() {
        try {
            this.zzomd = 0;
            this.zzome = null;
            this.zzomc.reset();
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "reset failed with a RemoteException:", e);
        }
    }

    public final <TResult> void zza(TaskCompletionSource<TResult> taskCompletionSource, TResult tresult) {
        Exception exception = getException();
        if (!zzcmm() || exception != null) {
            taskCompletionSource.setException(StorageException.fromExceptionAndHttpCode(exception, getResultCode()));
        } else {
            taskCompletionSource.setResult(tresult);
        }
    }

    public final void zzbq(String str, String str2) {
        try {
            this.zzomc.zzbq(str, str2);
        } catch (RemoteException e) {
            String str3 = "NetworkRequestProxy";
            String str4 = "Caught remote exception setting custom header:";
            String valueOf = String.valueOf(str);
            Log.e(str3, valueOf.length() != 0 ? str4.concat(valueOf) : new String(str4), e);
        }
    }

    public final void zzcmh() {
        try {
            if (this.zzomc != null) {
                this.zzomc.zzcmh();
            }
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "performRequestEnd failed with a RemoteException:", e);
        }
    }

    @Nullable
    public final String zzcmk() {
        try {
            this.zzomc.zzcmk();
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "getRawResult failed with a RemoteException:", e);
        }
        return null;
    }

    public final boolean zzcmm() {
        try {
            if (this.zzomd == -2 || this.zzome != null) {
                return false;
            }
            return this.zzomc.zzcmm();
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "isResultSuccess failed with a RemoteException:", e);
            return false;
        }
    }

    public final int zzcmn() {
        try {
            return this.zzomc.zzcmn();
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "getResultingContentLength failed with a RemoteException:", e);
            return 0;
        }
    }

    @NonNull
    public final JSONObject zzcmp() throws RemoteException {
        return (JSONObject) zzn.zzx(this.zzomc.zzcmj());
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0026 A[Catch:{ RemoteException -> 0x002d }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027 A[Catch:{ RemoteException -> 0x002d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zze(@android.support.annotation.Nullable java.lang.String r2, @android.support.annotation.NonNull android.content.Context r3) {
        /*
            r1 = this;
            java.lang.String r0 = "connectivity"
            java.lang.Object r3 = r3.getSystemService(r0)     // Catch:{ RemoteException -> 0x002d }
            android.net.ConnectivityManager r3 = (android.net.ConnectivityManager) r3     // Catch:{ RemoteException -> 0x002d }
            android.net.NetworkInfo r3 = r3.getActiveNetworkInfo()     // Catch:{ RemoteException -> 0x002d }
            if (r3 == 0) goto L_0x0017
            boolean r3 = r3.isConnected()     // Catch:{ RemoteException -> 0x002d }
            if (r3 != 0) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r3 = 1
            goto L_0x0024
        L_0x0017:
            r3 = -2
            r1.zzomd = r3     // Catch:{ RemoteException -> 0x002d }
            java.net.SocketException r3 = new java.net.SocketException     // Catch:{ RemoteException -> 0x002d }
            java.lang.String r0 = "Network subsystem is unavailable"
            r3.<init>(r0)     // Catch:{ RemoteException -> 0x002d }
            r1.zzome = r3     // Catch:{ RemoteException -> 0x002d }
            r3 = 0
        L_0x0024:
            if (r3 != 0) goto L_0x0027
            return
        L_0x0027:
            com.google.android.gms.internal.zzexx r3 = r1.zzomc     // Catch:{ RemoteException -> 0x002d }
            r3.zzsl(r2)     // Catch:{ RemoteException -> 0x002d }
            return
        L_0x002d:
            r2 = move-exception
            r1.zzkuq = r2
            java.lang.String r3 = "NetworkRequestProxy"
            java.lang.String r0 = "performRequest failed with a RemoteException:"
            android.util.Log.e(r3, r0, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzeyc.zze(java.lang.String, android.content.Context):void");
    }

    public final void zzsm(@Nullable String str) {
        try {
            this.zzomc.zzsm(str);
        } catch (RemoteException e) {
            this.zzkuq = e;
            Log.e("NetworkRequestProxy", "performRequestStart failed with a RemoteException:", e);
        }
    }

    @Nullable
    public final String zzsn(String str) {
        try {
            return this.zzomc.zzsn(str);
        } catch (RemoteException e) {
            String str2 = "NetworkRequestProxy";
            String str3 = "getResultString failed with a RemoteException:";
            String valueOf = String.valueOf(str);
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3), e);
            return null;
        }
    }
}
