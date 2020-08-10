package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.auth.PhoneAuthCredential;

final class zzdya extends zzdxo {
    final /* synthetic */ zzdxx zzmfy;

    private zzdya(zzdxx zzdxx) {
        this.zzmfy = zzdxx;
    }

    private final void zza(zzdyg zzdyg) {
        this.zzmfy.zzmfm.execute(new zzdyf(this, zzdyg));
    }

    public final void onFailure(@NonNull Status status) throws RemoteException {
        if (this.zzmfy.zzmfd == 8) {
            this.zzmfy.zzkuo = true;
            this.zzmfy.zzmfu = false;
            zza((zzdyg) new zzdye(this, status));
            return;
        }
        this.zzmfy.zzaq(status);
        this.zzmfy.zzap(status);
    }

    public final void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 8;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzkuo = true;
        this.zzmfy.zzmfu = true;
        zza((zzdyg) new zzdyc(this, phoneAuthCredential));
    }

    public final void zza(@NonNull Status status, @NonNull PhoneAuthCredential phoneAuthCredential) throws RemoteException {
        if (this.zzmfy.zzmfn != null) {
            this.zzmfy.zzkuo = true;
            this.zzmfy.zzmfn.zza(status, phoneAuthCredential);
            return;
        }
        onFailure(status);
    }

    public final void zza(@NonNull zzdyi zzdyi) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 3;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmfq = zzdyi;
        this.zzmfy.zzbrs();
    }

    public final void zza(@NonNull zzdym zzdym, @NonNull zzdyk zzdyk) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 2;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(37);
        sb.append("Unexpected response type: ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmfo = zzdym;
        this.zzmfy.zzmfp = zzdyk;
        this.zzmfy.zzbrs();
    }

    public final void zza(@Nullable zzdys zzdys) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 4;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmfr = zzdys;
        this.zzmfy.zzbrs();
    }

    public final void zzb(@NonNull zzdym zzdym) throws RemoteException {
        boolean z = true;
        if (this.zzmfy.zzmfd != 1) {
            z = false;
        }
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(37);
        sb.append("Unexpected response type: ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmfo = zzdym;
        this.zzmfy.zzbrs();
    }

    public final void zzbrn() throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 5;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzbrs();
    }

    public final void zzbro() throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 6;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzbrs();
    }

    public final void zzbrp() throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 9;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzbrs();
    }

    public final void zzow(@NonNull String str) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 7;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmfs = str;
        this.zzmfy.zzbrs();
    }

    public final void zzox(@NonNull String str) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 8;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmdq = str;
        zza((zzdyg) new zzdyb(this, str));
    }

    public final void zzoy(@NonNull String str) throws RemoteException {
        boolean z = this.zzmfy.zzmfd == 8;
        int i = this.zzmfy.zzmfd;
        StringBuilder sb = new StringBuilder(36);
        sb.append("Unexpected response type ");
        sb.append(i);
        zzbq.zza(z, (Object) sb.toString());
        this.zzmfy.zzmdq = str;
        this.zzmfy.zzkuo = true;
        this.zzmfy.zzmfu = true;
        zza((zzdyg) new zzdyd(this, str));
    }
}
