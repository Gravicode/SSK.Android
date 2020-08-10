package com.google.android.gms.common.api;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.internal.zzah;
import com.google.android.gms.common.api.internal.zzbm;
import com.google.android.gms.common.api.internal.zzbo;
import com.google.android.gms.common.api.internal.zzbw;
import com.google.android.gms.common.api.internal.zzci;
import com.google.android.gms.common.api.internal.zzck;
import com.google.android.gms.common.api.internal.zzcm;
import com.google.android.gms.common.api.internal.zzcq;
import com.google.android.gms.common.api.internal.zzcv;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzdd;
import com.google.android.gms.common.api.internal.zzdn;
import com.google.android.gms.common.api.internal.zzg;
import com.google.android.gms.common.api.internal.zzh;
import com.google.android.gms.common.api.internal.zzm;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

public class GoogleApi<O extends ApiOptions> {
    private final Context mContext;
    private final int mId;
    private final Looper zzall;
    private final Api<O> zzfin;
    private final O zzfme;
    private final zzh<O> zzfmf;
    private final GoogleApiClient zzfmg;
    private final zzcz zzfmh;
    protected final zzbm zzfmi;

    public static class zza {
        public static final zza zzfmj = new zzd().zzagq();
        public final zzcz zzfmk;
        public final Looper zzfml;

        private zza(zzcz zzcz, Account account, Looper looper) {
            this.zzfmk = zzcz;
            this.zzfml = looper;
        }
    }

    @MainThread
    public GoogleApi(@NonNull Activity activity, Api<O> api, O o, zza zza2) {
        zzbq.checkNotNull(activity, "Null activity is not permitted.");
        zzbq.checkNotNull(api, "Api must not be null.");
        zzbq.checkNotNull(zza2, "Settings must not be null; use Settings.DEFAULT_SETTINGS instead.");
        this.mContext = activity.getApplicationContext();
        this.zzfin = api;
        this.zzfme = o;
        this.zzall = zza2.zzfml;
        this.zzfmf = zzh.zza(this.zzfin, this.zzfme);
        this.zzfmg = new zzbw(this);
        this.zzfmi = zzbm.zzcj(this.mContext);
        this.mId = this.zzfmi.zzais();
        this.zzfmh = zza2.zzfmk;
        zzah.zza(activity, this.zzfmi, this.zzfmf);
        this.zzfmi.zza(this);
    }

    @Deprecated
    public GoogleApi(@NonNull Activity activity, Api<O> api, O o, zzcz zzcz) {
        this(activity, api, o, new zzd().zza(zzcz).zza(activity.getMainLooper()).zzagq());
    }

    protected GoogleApi(@NonNull Context context, Api<O> api, Looper looper) {
        zzbq.checkNotNull(context, "Null context is not permitted.");
        zzbq.checkNotNull(api, "Api must not be null.");
        zzbq.checkNotNull(looper, "Looper must not be null.");
        this.mContext = context.getApplicationContext();
        this.zzfin = api;
        this.zzfme = null;
        this.zzall = looper;
        this.zzfmf = zzh.zzb(api);
        this.zzfmg = new zzbw(this);
        this.zzfmi = zzbm.zzcj(this.mContext);
        this.mId = this.zzfmi.zzais();
        this.zzfmh = new zzg();
    }

    @Deprecated
    public GoogleApi(@NonNull Context context, Api<O> api, O o, Looper looper, zzcz zzcz) {
        this(context, api, (O) null, new zzd().zza(looper).zza(zzcz).zzagq());
    }

    public GoogleApi(@NonNull Context context, Api<O> api, O o, zza zza2) {
        zzbq.checkNotNull(context, "Null context is not permitted.");
        zzbq.checkNotNull(api, "Api must not be null.");
        zzbq.checkNotNull(zza2, "Settings must not be null; use Settings.DEFAULT_SETTINGS instead.");
        this.mContext = context.getApplicationContext();
        this.zzfin = api;
        this.zzfme = o;
        this.zzall = zza2.zzfml;
        this.zzfmf = zzh.zza(this.zzfin, this.zzfme);
        this.zzfmg = new zzbw(this);
        this.zzfmi = zzbm.zzcj(this.mContext);
        this.mId = this.zzfmi.zzais();
        this.zzfmh = zza2.zzfmk;
        this.zzfmi.zza(this);
    }

    @Deprecated
    public GoogleApi(@NonNull Context context, Api<O> api, O o, zzcz zzcz) {
        this(context, api, o, new zzd().zza(zzcz).zzagq());
    }

    private final <A extends zzb, T extends zzm<? extends Result, A>> T zza(int i, @NonNull T t) {
        t.zzahi();
        this.zzfmi.zza(this, i, (zzm<? extends Result, zzb>) t);
        return t;
    }

    private final <TResult, A extends zzb> Task<TResult> zza(int i, @NonNull zzdd<A, TResult> zzdd) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzfmi.zza(this, i, zzdd, taskCompletionSource, this.zzfmh);
        return taskCompletionSource.getTask();
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.google.android.gms.common.internal.zzs zzagp() {
        /*
            r2 = this;
            com.google.android.gms.common.internal.zzs r0 = new com.google.android.gms.common.internal.zzs
            r0.<init>()
            O r1 = r2.zzfme
            boolean r1 = r1 instanceof com.google.android.gms.common.api.Api.ApiOptions.HasGoogleSignInAccountOptions
            if (r1 == 0) goto L_0x001a
            O r1 = r2.zzfme
            com.google.android.gms.common.api.Api$ApiOptions$HasGoogleSignInAccountOptions r1 = (com.google.android.gms.common.api.Api.ApiOptions.HasGoogleSignInAccountOptions) r1
            com.google.android.gms.auth.api.signin.GoogleSignInAccount r1 = r1.getGoogleSignInAccount()
            if (r1 == 0) goto L_0x001a
            android.accounts.Account r1 = r1.getAccount()
            goto L_0x002a
        L_0x001a:
            O r1 = r2.zzfme
            boolean r1 = r1 instanceof com.google.android.gms.common.api.Api.ApiOptions.HasAccountOptions
            if (r1 == 0) goto L_0x0029
            O r1 = r2.zzfme
            com.google.android.gms.common.api.Api$ApiOptions$HasAccountOptions r1 = (com.google.android.gms.common.api.Api.ApiOptions.HasAccountOptions) r1
            android.accounts.Account r1 = r1.getAccount()
            goto L_0x002a
        L_0x0029:
            r1 = 0
        L_0x002a:
            com.google.android.gms.common.internal.zzs r0 = r0.zze(r1)
            O r1 = r2.zzfme
            boolean r1 = r1 instanceof com.google.android.gms.common.api.Api.ApiOptions.HasGoogleSignInAccountOptions
            if (r1 == 0) goto L_0x0043
            O r1 = r2.zzfme
            com.google.android.gms.common.api.Api$ApiOptions$HasGoogleSignInAccountOptions r1 = (com.google.android.gms.common.api.Api.ApiOptions.HasGoogleSignInAccountOptions) r1
            com.google.android.gms.auth.api.signin.GoogleSignInAccount r1 = r1.getGoogleSignInAccount()
            if (r1 == 0) goto L_0x0043
            java.util.Set r1 = r1.zzabb()
            goto L_0x0047
        L_0x0043:
            java.util.Set r1 = java.util.Collections.emptySet()
        L_0x0047:
            com.google.android.gms.common.internal.zzs r0 = r0.zze(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.GoogleApi.zzagp():com.google.android.gms.common.internal.zzs");
    }

    public final Context getApplicationContext() {
        return this.mContext;
    }

    public final int getInstanceId() {
        return this.mId;
    }

    public final Looper getLooper() {
        return this.zzall;
    }

    @WorkerThread
    public zze zza(Looper looper, zzbo<O> zzbo) {
        return this.zzfin.zzage().zza(this.mContext, looper, zzagp().zzgf(this.mContext.getPackageName()).zzgg(this.mContext.getClass().getName()).zzald(), this.zzfme, zzbo, zzbo);
    }

    public final <L> zzci<L> zza(@NonNull L l, String str) {
        return zzcm.zzb(l, this.zzall, str);
    }

    public zzcv zza(Context context, Handler handler) {
        return new zzcv(context, handler, zzagp().zzald());
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zza(@NonNull T t) {
        return zza(0, t);
    }

    public final Task<Boolean> zza(@NonNull zzck<?> zzck) {
        zzbq.checkNotNull(zzck, "Listener key cannot be null.");
        return this.zzfmi.zza(this, zzck);
    }

    public final <A extends zzb, T extends zzcq<A, ?>, U extends zzdn<A, ?>> Task<Void> zza(@NonNull T t, U u) {
        zzbq.checkNotNull(t);
        zzbq.checkNotNull(u);
        zzbq.checkNotNull(t.zzajo(), "Listener has already been released.");
        zzbq.checkNotNull(u.zzajo(), "Listener has already been released.");
        zzbq.checkArgument(t.zzajo().equals(u.zzajo()), "Listener registration and unregistration methods must be constructed with the same ListenerHolder.");
        return this.zzfmi.zza(this, (zzcq<zzb, ?>) t, (zzdn<zzb, ?>) u);
    }

    public final <TResult, A extends zzb> Task<TResult> zza(zzdd<A, TResult> zzdd) {
        return zza(0, zzdd);
    }

    public final Api<O> zzagl() {
        return this.zzfin;
    }

    public final O zzagm() {
        return this.zzfme;
    }

    public final zzh<O> zzagn() {
        return this.zzfmf;
    }

    public final GoogleApiClient zzago() {
        return this.zzfmg;
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zzb(@NonNull T t) {
        return zza(1, t);
    }

    public final <TResult, A extends zzb> Task<TResult> zzb(zzdd<A, TResult> zzdd) {
        return zza(1, zzdd);
    }

    public final <A extends zzb, T extends zzm<? extends Result, A>> T zzc(@NonNull T t) {
        return zza(2, t);
    }
}
