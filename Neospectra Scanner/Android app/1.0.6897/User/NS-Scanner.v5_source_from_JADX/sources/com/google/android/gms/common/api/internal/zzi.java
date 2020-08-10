package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzbq;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzi extends zzo {
    private final SparseArray<zza> zzfnx = new SparseArray<>();

    class zza implements OnConnectionFailedListener {
        public final int zzfny;
        public final GoogleApiClient zzfnz;
        public final OnConnectionFailedListener zzfoa;

        public zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzfny = i;
            this.zzfnz = googleApiClient;
            this.zzfoa = onConnectionFailedListener;
            googleApiClient.registerConnectionFailedListener(this);
        }

        public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            String valueOf = String.valueOf(connectionResult);
            StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
            sb.append("beginFailureResolution for ");
            sb.append(valueOf);
            Log.d("AutoManageHelper", sb.toString());
            zzi.this.zzb(connectionResult, this.zzfny);
        }
    }

    private zzi(zzcf zzcf) {
        super(zzcf);
        this.zzfud.zza("AutoManageHelper", (LifecycleCallback) this);
    }

    public static zzi zza(zzce zzce) {
        zzcf zzb = zzb(zzce);
        zzi zzi = (zzi) zzb.zza("AutoManageHelper", zzi.class);
        return zzi != null ? zzi : new zzi(zzb);
    }

    @Nullable
    private final zza zzbs(int i) {
        if (this.zzfnx.size() <= i) {
            return null;
        }
        return (zza) this.zzfnx.get(this.zzfnx.keyAt(i));
    }

    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.zzfnx.size(); i++) {
            zza zzbs = zzbs(i);
            if (zzbs != null) {
                printWriter.append(str).append("GoogleApiClient #").print(zzbs.zzfny);
                printWriter.println(":");
                zzbs.zzfnz.dump(String.valueOf(str).concat("  "), fileDescriptor, printWriter, strArr);
            }
        }
    }

    public final void onStart() {
        super.onStart();
        boolean z = this.mStarted;
        String valueOf = String.valueOf(this.zzfnx);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 14);
        sb.append("onStart ");
        sb.append(z);
        sb.append(" ");
        sb.append(valueOf);
        Log.d("AutoManageHelper", sb.toString());
        if (this.zzfol.get() == null) {
            for (int i = 0; i < this.zzfnx.size(); i++) {
                zza zzbs = zzbs(i);
                if (zzbs != null) {
                    zzbs.zzfnz.connect();
                }
            }
        }
    }

    public final void onStop() {
        super.onStop();
        for (int i = 0; i < this.zzfnx.size(); i++) {
            zza zzbs = zzbs(i);
            if (zzbs != null) {
                zzbs.zzfnz.disconnect();
            }
        }
    }

    public final void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzbq.checkNotNull(googleApiClient, "GoogleApiClient instance cannot be null");
        boolean z = this.zzfnx.indexOfKey(i) < 0;
        StringBuilder sb = new StringBuilder(54);
        sb.append("Already managing a GoogleApiClient with id ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        zzp zzp = (zzp) this.zzfol.get();
        boolean z2 = this.mStarted;
        String valueOf = String.valueOf(zzp);
        StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 49);
        sb2.append("starting AutoManage for client ");
        sb2.append(i);
        sb2.append(" ");
        sb2.append(z2);
        sb2.append(" ");
        sb2.append(valueOf);
        Log.d("AutoManageHelper", sb2.toString());
        this.zzfnx.put(i, new zza(i, googleApiClient, onConnectionFailedListener));
        if (this.mStarted && zzp == null) {
            String valueOf2 = String.valueOf(googleApiClient);
            StringBuilder sb3 = new StringBuilder(String.valueOf(valueOf2).length() + 11);
            sb3.append("connecting ");
            sb3.append(valueOf2);
            Log.d("AutoManageHelper", sb3.toString());
            googleApiClient.connect();
        }
    }

    /* access modifiers changed from: protected */
    public final void zza(ConnectionResult connectionResult, int i) {
        Log.w("AutoManageHelper", "Unresolved error while connecting client. Stopping auto-manage.");
        if (i < 0) {
            Log.wtf("AutoManageHelper", "AutoManageLifecycleHelper received onErrorResolutionFailed callback but no failing client ID is set", new Exception());
            return;
        }
        zza zza2 = (zza) this.zzfnx.get(i);
        if (zza2 != null) {
            zzbr(i);
            OnConnectionFailedListener onConnectionFailedListener = zza2.zzfoa;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void zzagz() {
        for (int i = 0; i < this.zzfnx.size(); i++) {
            zza zzbs = zzbs(i);
            if (zzbs != null) {
                zzbs.zzfnz.connect();
            }
        }
    }

    public final void zzbr(int i) {
        zza zza2 = (zza) this.zzfnx.get(i);
        this.zzfnx.remove(i);
        if (zza2 != null) {
            zza2.zzfnz.unregisterConnectionFailedListener(zza2);
            zza2.zzfnz.disconnect();
        }
    }
}
