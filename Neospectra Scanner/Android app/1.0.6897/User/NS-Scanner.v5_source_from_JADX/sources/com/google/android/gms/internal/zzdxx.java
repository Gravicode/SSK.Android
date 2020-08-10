package com.google.android.gms.internal;

import android.app.Activity;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.zzcf;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.auth.internal.zzp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

abstract class zzdxx<SuccessT, CallbackT> {
    /* access modifiers changed from: private */
    public boolean zzkuo;
    protected FirebaseApp zzmcx;
    protected String zzmdq;
    protected final int zzmfd;
    protected final zzdya zzmfe = new zzdya(this);
    protected FirebaseUser zzmff;
    protected zzdxp zzmfg;
    protected CallbackT zzmfh;
    protected zzp zzmfi;
    protected zzdxw<SuccessT> zzmfj = this;
    protected final List<OnVerificationStateChangedCallbacks> zzmfk = new ArrayList();
    private Activity zzmfl;
    protected Executor zzmfm;
    protected zzdxz zzmfn = this;
    protected zzdym zzmfo;
    protected zzdyk zzmfp;
    protected zzdyi zzmfq;
    protected zzdys zzmfr;
    protected String zzmfs;
    protected PhoneAuthCredential zzmft;
    boolean zzmfu;
    private SuccessT zzmfv;
    private Status zzmfw;

    static class zza extends LifecycleCallback {
        private List<OnVerificationStateChangedCallbacks> zzmfx;

        private zza(zzcf zzcf, List<OnVerificationStateChangedCallbacks> list) {
            super(zzcf);
            this.zzfud.zza("PhoneAuthActivityStopCallback", (LifecycleCallback) this);
            this.zzmfx = list;
        }

        public static void zza(Activity activity, List<OnVerificationStateChangedCallbacks> list) {
            zzcf zzn = zzn(activity);
            if (((zza) zzn.zza("PhoneAuthActivityStopCallback", zza.class)) == null) {
                new zza(zzn, list);
            }
        }

        @MainThread
        public final void onStop() {
            synchronized (this.zzmfx) {
                this.zzmfx.clear();
            }
        }
    }

    public zzdxx(int i) {
        this.zzmfd = i;
    }

    /* access modifiers changed from: private */
    public final void zzaq(Status status) {
        if (this.zzmfi != null) {
            this.zzmfi.onError(status);
        }
    }

    /* access modifiers changed from: private */
    public final void zzbrs() {
        zzbrl();
        zzbq.zza(this.zzkuo, (Object) "no success or failure set on method implementation");
    }

    /* access modifiers changed from: protected */
    public abstract void dispatch() throws RemoteException;

    public final zzdxx<SuccessT, CallbackT> zza(OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks, Activity activity, Executor executor) {
        synchronized (this.zzmfk) {
            this.zzmfk.add((OnVerificationStateChangedCallbacks) zzbq.checkNotNull(onVerificationStateChangedCallbacks));
        }
        this.zzmfl = activity;
        if (this.zzmfl != null) {
            zza.zza(activity, this.zzmfk);
        }
        this.zzmfm = (Executor) zzbq.checkNotNull(executor);
        return this;
    }

    public final zzdxx<SuccessT, CallbackT> zza(zzp zzp) {
        this.zzmfi = (zzp) zzbq.checkNotNull(zzp, "external failure callback cannot be null");
        return this;
    }

    public final void zzap(Status status) {
        this.zzkuo = true;
        this.zzmfu = false;
        this.zzmfw = status;
        this.zzmfj.zza(null, status);
    }

    public final zzdxx<SuccessT, CallbackT> zzbc(CallbackT callbackt) {
        this.zzmfh = zzbq.checkNotNull(callbackt, "external callback cannot be null");
        return this;
    }

    public final void zzbd(SuccessT successt) {
        this.zzkuo = true;
        this.zzmfu = true;
        this.zzmfv = successt;
        this.zzmfj.zza(successt, null);
    }

    public abstract void zzbrl();

    public final zzdxx<SuccessT, CallbackT> zzc(FirebaseApp firebaseApp) {
        this.zzmcx = (FirebaseApp) zzbq.checkNotNull(firebaseApp, "firebaseApp cannot be null");
        return this;
    }

    public final zzdxx<SuccessT, CallbackT> zze(FirebaseUser firebaseUser) {
        this.zzmff = (FirebaseUser) zzbq.checkNotNull(firebaseUser, "firebaseUser cannot be null");
        return this;
    }
}
