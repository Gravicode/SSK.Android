package com.google.android.gms.auth.api.signin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.internal.zze;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.zzcz;
import com.google.android.gms.common.api.internal.zzg;
import com.google.android.gms.common.internal.zzbj;
import com.google.android.gms.common.internal.zzbo;
import com.google.android.gms.common.zzf;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.Task;

public class GoogleSignInClient extends GoogleApi<GoogleSignInOptions> {
    private static final zza zzeha = new zza(null);
    private static int zzehb = zzb.zzehd;

    static class zza implements zzbo<GoogleSignInResult, GoogleSignInAccount> {
        private zza() {
        }

        /* synthetic */ zza(zzc zzc) {
            this();
        }

        public final /* synthetic */ Object zzb(Result result) {
            return ((GoogleSignInResult) result).getSignInAccount();
        }
    }

    /* 'enum' access flag removed */
    static final class zzb {
        public static final int zzehd = 1;
        public static final int zzehe = 2;
        public static final int zzehf = 3;
        public static final int zzehg = 4;
        private static final /* synthetic */ int[] zzehh = {zzehd, zzehe, zzehf, zzehg};

        /* renamed from: values$50KLMJ33DTMIUPRFDTJMOP9FC5N68SJFD5I2UPRDECNM2TBKD0NM2S395TPMIPRED5N2UHRFDTJMOPAJD5JMSIBE8DM6IPBEEGI4IRBGDHIMQPBEEHGN8QBFDOTG____0 */
        public static int[] m12xd6228de2() {
            return (int[]) zzehh.clone();
        }
    }

    GoogleSignInClient(@NonNull Activity activity, GoogleSignInOptions googleSignInOptions) {
        super(activity, Auth.GOOGLE_SIGN_IN_API, googleSignInOptions, (zzcz) new zzg());
    }

    GoogleSignInClient(@NonNull Context context, GoogleSignInOptions googleSignInOptions) {
        super(context, Auth.GOOGLE_SIGN_IN_API, googleSignInOptions, (zzcz) new zzg());
    }

    private final synchronized int zzabd() {
        if (zzehb == zzb.zzehd) {
            Context applicationContext = getApplicationContext();
            int isGooglePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(applicationContext);
            int i = isGooglePlayServicesAvailable == 0 ? zzb.zzehg : (zzf.zza(applicationContext, isGooglePlayServicesAvailable, null) != null || DynamiteModule.zzab(applicationContext, "com.google.android.gms.auth.api.fallback") == 0) ? zzb.zzehe : zzb.zzehf;
            zzehb = i;
        }
        return zzehb;
    }

    @NonNull
    public Intent getSignInIntent() {
        Context applicationContext = getApplicationContext();
        switch (zzc.zzehc[zzabd() - 1]) {
            case 1:
                return zze.zzb(applicationContext, (GoogleSignInOptions) zzagm());
            case 2:
                return zze.zza(applicationContext, (GoogleSignInOptions) zzagm());
            default:
                return zze.zzc(applicationContext, (GoogleSignInOptions) zzagm());
        }
    }

    public Task<Void> revokeAccess() {
        return zzbj.zzb(zze.zzb(zzago(), getApplicationContext(), zzabd() == zzb.zzehf));
    }

    public Task<Void> signOut() {
        return zzbj.zzb(zze.zza(zzago(), getApplicationContext(), zzabd() == zzb.zzehf));
    }

    public Task<GoogleSignInAccount> silentSignIn() {
        return zzbj.zza((PendingResult<R>) zze.zza(zzago(), getApplicationContext(), (GoogleSignInOptions) zzagm(), zzabd() == zzb.zzehf), (zzbo<R, T>) zzeha);
    }
}
