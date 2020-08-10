package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzan;
import com.google.android.gms.common.internal.zzbt;
import com.google.android.gms.common.internal.zzr;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.zzf;
import com.google.android.gms.internal.zzcxd;
import com.google.android.gms.internal.zzcxe;
import com.google.android.gms.internal.zzcxq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;

public final class zzao implements zzbh {
    /* access modifiers changed from: private */
    public final Context mContext;
    private final zza<? extends zzcxd, zzcxe> zzfmz;
    /* access modifiers changed from: private */
    public final Lock zzfps;
    private final zzr zzfpx;
    private final Map<Api<?>, Boolean> zzfqa;
    /* access modifiers changed from: private */
    public final zzf zzfqc;
    private ConnectionResult zzfql;
    /* access modifiers changed from: private */
    public final zzbi zzfqv;
    private int zzfqy;
    private int zzfqz = 0;
    private int zzfra;
    private final Bundle zzfrb = new Bundle();
    private final Set<zzc> zzfrc = new HashSet();
    /* access modifiers changed from: private */
    public zzcxd zzfrd;
    private boolean zzfre;
    /* access modifiers changed from: private */
    public boolean zzfrf;
    private boolean zzfrg;
    /* access modifiers changed from: private */
    public zzan zzfrh;
    private boolean zzfri;
    private boolean zzfrj;
    private ArrayList<Future<?>> zzfrk = new ArrayList<>();

    public zzao(zzbi zzbi, zzr zzr, Map<Api<?>, Boolean> map, zzf zzf, zza<? extends zzcxd, zzcxe> zza, Lock lock, Context context) {
        this.zzfqv = zzbi;
        this.zzfpx = zzr;
        this.zzfqa = map;
        this.zzfqc = zzf;
        this.zzfmz = zza;
        this.zzfps = lock;
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public final void zza(zzcxq zzcxq) {
        if (zzbt(0)) {
            ConnectionResult zzahf = zzcxq.zzahf();
            if (zzahf.isSuccess()) {
                zzbt zzbdi = zzcxq.zzbdi();
                ConnectionResult zzahf2 = zzbdi.zzahf();
                if (!zzahf2.isSuccess()) {
                    String valueOf = String.valueOf(zzahf2);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 48);
                    sb.append("Sign-in succeeded with resolve account failure: ");
                    sb.append(valueOf);
                    Log.wtf("GoogleApiClientConnecting", sb.toString(), new Exception());
                    zze(zzahf2);
                    return;
                }
                this.zzfrg = true;
                this.zzfrh = zzbdi.zzalp();
                this.zzfri = zzbdi.zzalq();
                this.zzfrj = zzbdi.zzalr();
                zzaid();
            } else if (zzd(zzahf)) {
                zzaif();
                zzaid();
            } else {
                zze(zzahf);
            }
        }
    }

    /* access modifiers changed from: private */
    public final boolean zzaic() {
        ConnectionResult connectionResult;
        this.zzfra--;
        if (this.zzfra > 0) {
            return false;
        }
        if (this.zzfra < 0) {
            Log.w("GoogleApiClientConnecting", this.zzfqv.zzfpi.zzaim());
            Log.wtf("GoogleApiClientConnecting", "GoogleApiClient received too many callbacks for the given step. Clients may be in an unexpected state; GoogleApiClient will now disconnect.", new Exception());
            connectionResult = new ConnectionResult(8, null);
        } else if (this.zzfql == null) {
            return true;
        } else {
            this.zzfqv.zzfst = this.zzfqy;
            connectionResult = this.zzfql;
        }
        zze(connectionResult);
        return false;
    }

    /* access modifiers changed from: private */
    public final void zzaid() {
        if (this.zzfra == 0) {
            if (!this.zzfrf || this.zzfrg) {
                ArrayList arrayList = new ArrayList();
                this.zzfqz = 1;
                this.zzfra = this.zzfqv.zzfsb.size();
                for (zzc zzc : this.zzfqv.zzfsb.keySet()) {
                    if (!this.zzfqv.zzfsq.containsKey(zzc)) {
                        arrayList.add((zze) this.zzfqv.zzfsb.get(zzc));
                    } else if (zzaic()) {
                        zzaie();
                    }
                }
                if (!arrayList.isEmpty()) {
                    this.zzfrk.add(zzbl.zzaip().submit(new zzau(this, arrayList)));
                }
            }
        }
    }

    private final void zzaie() {
        this.zzfqv.zzaio();
        zzbl.zzaip().execute(new zzap(this));
        if (this.zzfrd != null) {
            if (this.zzfri) {
                this.zzfrd.zza(this.zzfrh, this.zzfrj);
            }
            zzbg(false);
        }
        for (zzc zzc : this.zzfqv.zzfsq.keySet()) {
            ((zze) this.zzfqv.zzfsb.get(zzc)).disconnect();
        }
        this.zzfqv.zzfsu.zzj(this.zzfrb.isEmpty() ? null : this.zzfrb);
    }

    /* access modifiers changed from: private */
    public final void zzaif() {
        this.zzfrf = false;
        this.zzfqv.zzfpi.zzfsc = Collections.emptySet();
        for (zzc zzc : this.zzfrc) {
            if (!this.zzfqv.zzfsq.containsKey(zzc)) {
                this.zzfqv.zzfsq.put(zzc, new ConnectionResult(17, null));
            }
        }
    }

    private final void zzaig() {
        ArrayList arrayList = this.zzfrk;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((Future) obj).cancel(true);
        }
        this.zzfrk.clear();
    }

    /* access modifiers changed from: private */
    public final Set<Scope> zzaih() {
        if (this.zzfpx == null) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet(this.zzfpx.zzakv());
        Map zzakx = this.zzfpx.zzakx();
        for (Api api : zzakx.keySet()) {
            if (!this.zzfqv.zzfsq.containsKey(api.zzagf())) {
                hashSet.addAll(((zzt) zzakx.get(api)).zzehs);
            }
        }
        return hashSet;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0022, code lost:
        if ((r5.hasResolution() || r4.zzfqc.zzbp(r5.getErrorCode()) != null) != false) goto L_0x0024;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzb(com.google.android.gms.common.ConnectionResult r5, com.google.android.gms.common.api.Api<?> r6, boolean r7) {
        /*
            r4 = this;
            com.google.android.gms.common.api.Api$zzd r0 = r6.zzagd()
            int r0 = r0.getPriority()
            r1 = 0
            r2 = 1
            if (r7 == 0) goto L_0x0024
            boolean r7 = r5.hasResolution()
            if (r7 == 0) goto L_0x0014
        L_0x0012:
            r7 = 1
            goto L_0x0022
        L_0x0014:
            com.google.android.gms.common.zzf r7 = r4.zzfqc
            int r3 = r5.getErrorCode()
            android.content.Intent r7 = r7.zzbp(r3)
            if (r7 == 0) goto L_0x0021
            goto L_0x0012
        L_0x0021:
            r7 = 0
        L_0x0022:
            if (r7 == 0) goto L_0x002d
        L_0x0024:
            com.google.android.gms.common.ConnectionResult r7 = r4.zzfql
            if (r7 == 0) goto L_0x002c
            int r7 = r4.zzfqy
            if (r0 >= r7) goto L_0x002d
        L_0x002c:
            r1 = 1
        L_0x002d:
            if (r1 == 0) goto L_0x0033
            r4.zzfql = r5
            r4.zzfqy = r0
        L_0x0033:
            com.google.android.gms.common.api.internal.zzbi r7 = r4.zzfqv
            java.util.Map<com.google.android.gms.common.api.Api$zzc<?>, com.google.android.gms.common.ConnectionResult> r7 = r7.zzfsq
            com.google.android.gms.common.api.Api$zzc r6 = r6.zzagf()
            r7.put(r6, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.internal.zzao.zzb(com.google.android.gms.common.ConnectionResult, com.google.android.gms.common.api.Api, boolean):void");
    }

    private final void zzbg(boolean z) {
        if (this.zzfrd != null) {
            if (this.zzfrd.isConnected() && z) {
                this.zzfrd.zzbdb();
            }
            this.zzfrd.disconnect();
            this.zzfrh = null;
        }
    }

    /* access modifiers changed from: private */
    public final boolean zzbt(int i) {
        if (this.zzfqz == i) {
            return true;
        }
        Log.w("GoogleApiClientConnecting", this.zzfqv.zzfpi.zzaim());
        String valueOf = String.valueOf(this);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 23);
        sb.append("Unexpected callback in ");
        sb.append(valueOf);
        Log.w("GoogleApiClientConnecting", sb.toString());
        int i2 = this.zzfra;
        StringBuilder sb2 = new StringBuilder(33);
        sb2.append("mRemainingConnections=");
        sb2.append(i2);
        Log.w("GoogleApiClientConnecting", sb2.toString());
        String zzbu = zzbu(this.zzfqz);
        String zzbu2 = zzbu(i);
        StringBuilder sb3 = new StringBuilder(String.valueOf(zzbu).length() + 70 + String.valueOf(zzbu2).length());
        sb3.append("GoogleApiClient connecting is in step ");
        sb3.append(zzbu);
        sb3.append(" but received callback for step ");
        sb3.append(zzbu2);
        Log.wtf("GoogleApiClientConnecting", sb3.toString(), new Exception());
        zze(new ConnectionResult(8, null));
        return false;
    }

    private static String zzbu(int i) {
        switch (i) {
            case 0:
                return "STEP_SERVICE_BINDINGS_AND_SIGN_IN";
            case 1:
                return "STEP_GETTING_REMOTE_SERVICE";
            default:
                return "UNKNOWN";
        }
    }

    /* access modifiers changed from: private */
    public final boolean zzd(ConnectionResult connectionResult) {
        return this.zzfre && !connectionResult.hasResolution();
    }

    /* access modifiers changed from: private */
    public final void zze(ConnectionResult connectionResult) {
        zzaig();
        zzbg(!connectionResult.hasResolution());
        this.zzfqv.zzg(connectionResult);
        this.zzfqv.zzfsu.zzc(connectionResult);
    }

    public final void begin() {
        this.zzfqv.zzfsq.clear();
        this.zzfrf = false;
        this.zzfql = null;
        this.zzfqz = 0;
        this.zzfre = true;
        this.zzfrg = false;
        this.zzfri = false;
        HashMap hashMap = new HashMap();
        boolean z = false;
        for (Api api : this.zzfqa.keySet()) {
            zze zze = (zze) this.zzfqv.zzfsb.get(api.zzagf());
            z |= api.zzagd().getPriority() == 1;
            boolean booleanValue = ((Boolean) this.zzfqa.get(api)).booleanValue();
            if (zze.zzaay()) {
                this.zzfrf = true;
                if (booleanValue) {
                    this.zzfrc.add(api.zzagf());
                } else {
                    this.zzfre = false;
                }
            }
            hashMap.put(zze, new zzaq(this, api, booleanValue));
        }
        if (z) {
            this.zzfrf = false;
        }
        if (this.zzfrf) {
            this.zzfpx.zzc(Integer.valueOf(System.identityHashCode(this.zzfqv.zzfpi)));
            zzax zzax = new zzax(this, null);
            this.zzfrd = (zzcxd) this.zzfmz.zza(this.mContext, this.zzfqv.zzfpi.getLooper(), this.zzfpx, this.zzfpx.zzalb(), zzax, zzax);
        }
        this.zzfra = this.zzfqv.zzfsb.size();
        this.zzfrk.add(zzbl.zzaip().submit(new zzar(this, hashMap)));
    }

    public final void connect() {
    }

    public final boolean disconnect() {
        zzaig();
        zzbg(true);
        this.zzfqv.zzg(null);
        return true;
    }

    public final void onConnected(Bundle bundle) {
        if (zzbt(1)) {
            if (bundle != null) {
                this.zzfrb.putAll(bundle);
            }
            if (zzaic()) {
                zzaie();
            }
        }
    }

    public final void onConnectionSuspended(int i) {
        zze(new ConnectionResult(8, null));
    }

    public final void zza(ConnectionResult connectionResult, Api<?> api, boolean z) {
        if (zzbt(1)) {
            zzb(connectionResult, api, z);
            if (zzaic()) {
                zzaie();
            }
        }
    }

    public final <A extends zzb, R extends Result, T extends zzm<R, A>> T zzd(T t) {
        this.zzfqv.zzfpi.zzfqg.add(t);
        return t;
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zze(T t) {
        throw new IllegalStateException("GoogleApiClient is not connected yet.");
    }
}
