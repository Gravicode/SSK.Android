package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeResult;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.internal.zza;
import com.google.firebase.auth.internal.zzf;
import com.google.firebase.auth.internal.zzh;
import com.google.firebase.auth.internal.zzi;
import com.google.firebase.auth.internal.zzo;
import com.google.firebase.auth.internal.zzp;
import com.google.firebase.auth.internal.zzv;
import com.google.firebase.zzb;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public final class zzdwc extends zzdvv<zzdxt> {
    zzdwc(@NonNull Context context, @NonNull zzdxt zzdxt) {
        super(context, zzdxr.zzmfc, zzdxt, new zzb(), DynamiteModule.zzac(context, "com.google.android.gms.firebase_auth"), DynamiteModule.zzab(context, "com.google.firebase.auth"), Collections.EMPTY_MAP);
    }

    private static <ResultT, CallbackT> zzdwn<ResultT, CallbackT> zza(zzdxx<ResultT, CallbackT> zzdxx, String str) {
        return new zzdwn<>(zzdxx, str);
    }

    /* access modifiers changed from: private */
    @NonNull
    public static zzh zza(@NonNull FirebaseApp firebaseApp, @NonNull zzdyk zzdyk) {
        return zza(firebaseApp, zzdyk, false);
    }

    /* access modifiers changed from: private */
    @NonNull
    public static zzh zza(@NonNull FirebaseApp firebaseApp, @NonNull zzdyk zzdyk, boolean z) {
        zzbq.checkNotNull(firebaseApp);
        zzbq.checkNotNull(zzdyk);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new zzf(zzdyk, FirebaseAuthProvider.PROVIDER_ID));
        List zzbrt = zzdyk.zzbrt();
        if (zzbrt != null && !zzbrt.isEmpty()) {
            for (int i = 0; i < zzbrt.size(); i++) {
                arrayList.add(new zzf((zzdyo) zzbrt.get(i)));
            }
        }
        zzh zzh = new zzh(firebaseApp, arrayList);
        zzh.zzcf(z);
        zzh.zza((FirebaseUserMetadata) new zzi(zzdyk.getLastSignInTimestamp(), zzdyk.getCreationTimestamp()));
        zzh.zzci(zzdyk.isNewUser());
        return zzh;
    }

    @NonNull
    public final Task<Void> setFirebaseUIVersion(@NonNull String str) {
        return zzb(zza((zzdxx<ResultT, CallbackT>) new zzdww<ResultT,CallbackT>(str), "setFirebaseUIVersion"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull ActionCodeSettings actionCodeSettings, @NonNull String str) {
        return zzb(zza(new zzdwu(str, actionCodeSettings).zzc(firebaseApp), "sendEmailVerification"));
    }

    public final Task<AuthResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull AuthCredential authCredential, @NonNull zza zza) {
        return zzb(zza(new zzdwy(authCredential).zzc(firebaseApp).zzbc(zza), "signInWithCredential"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull AuthCredential authCredential, @NonNull zzv zzv) {
        return zzb(zza(new zzdwo(authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithCredential"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull PhoneAuthCredential phoneAuthCredential, @NonNull zzv zzv) {
        return zzb(zza(new zzdxg(phoneAuthCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "updatePhoneNumber"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull UserProfileChangeRequest userProfileChangeRequest, @NonNull zzv zzv) {
        return zzb(zza(new zzdxh(userProfileChangeRequest).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "updateProfile"));
    }

    @NonNull
    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull zzv zzv) {
        return zza(zza(new zzdwt().zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reload"));
    }

    public final Task<GetTokenResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull zzv zzv) {
        return zza(zza(new zzdwj(str).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "getAccessToken"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull String str2, @NonNull zzv zzv) {
        return zzb(zza(new zzdwq(str, str2).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithEmailPassword"));
    }

    public final Task<AuthResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull PhoneAuthCredential phoneAuthCredential, @NonNull zza zza) {
        return zzb(zza(new zzdxb(phoneAuthCredential).zzc(firebaseApp).zzbc(zza), "signInWithPhoneNumber"));
    }

    public final Task<AuthResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull zza zza) {
        return zzb(zza(new zzdwx().zzc(firebaseApp).zzbc(zza), "signInAnonymously"));
    }

    public final Task<ProviderQueryResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        return zza(zza(new zzdwi(str).zzc(firebaseApp), "fetchProvidersForEmail"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull String str, @NonNull ActionCodeSettings actionCodeSettings) {
        actionCodeSettings.zzgl(1);
        return zzb(zza(new zzdwv(str, actionCodeSettings).zzc(firebaseApp), "sendPasswordResetEmail"));
    }

    public final Task<AuthResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull String str, @NonNull zza zza) {
        return zzb(zza(new zzdwz(str).zzc(firebaseApp).zzbc(zza), "signInWithCustomToken"));
    }

    public final Task<Void> zza(@NonNull FirebaseApp firebaseApp, @NonNull String str, @NonNull String str2) {
        return zzb(zza(new zzdwf(str, str2).zzc(firebaseApp), "confirmPasswordReset"));
    }

    public final Task<AuthResult> zza(@NonNull FirebaseApp firebaseApp, @NonNull String str, @NonNull String str2, @NonNull zza zza) {
        return zzb(zza(new zzdwg(str, str2).zzc(firebaseApp).zzbc(zza), "createUserWithEmailAndPassword"));
    }

    @NonNull
    public final Task<Void> zza(@NonNull FirebaseUser firebaseUser, @NonNull zzo zzo) {
        return zzb(zza(new zzdwh().zze(firebaseUser).zzbc(zzo).zza((zzp) zzo), "delete"));
    }

    public final void zza(@NonNull FirebaseApp firebaseApp, @NonNull zzdyu zzdyu, @NonNull OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks, @Nullable Activity activity, @NonNull Executor executor) {
        zzb(zza(new zzdxj(zzdyu).zzc(firebaseApp).zza(onVerificationStateChangedCallbacks, activity, executor), "verifyPhoneNumber"));
    }

    public final Task<AuthResult> zzb(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull AuthCredential authCredential, @NonNull zzv zzv) {
        return zzb(zza(new zzdwp(authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithCredentialWithData"));
    }

    public final Task<Void> zzb(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull PhoneAuthCredential phoneAuthCredential, @NonNull zzv zzv) {
        return zzb(zza(new zzdws(phoneAuthCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithPhoneCredential"));
    }

    public final Task<Void> zzb(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull zzv zzv) {
        return zzb(zza(new zzdxe(str).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "updateEmail"));
    }

    public final Task<AuthResult> zzb(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull String str2, @NonNull zzv zzv) {
        return zzb(zza(new zzdwr(str, str2).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithEmailPasswordWithData"));
    }

    public final Task<ActionCodeResult> zzb(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        return zzb(zza(new zzdwe(str).zzc(firebaseApp), "checkActionCode"));
    }

    public final Task<AuthResult> zzb(@NonNull FirebaseApp firebaseApp, @NonNull String str, @NonNull String str2, @NonNull zza zza) {
        return zzb(zza(new zzdxa(str, str2).zzc(firebaseApp).zzbc(zza), "signInWithEmailAndPassword"));
    }

    public final Task<AuthResult> zzc(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull AuthCredential authCredential, @NonNull zzv zzv) {
        return zzb(zza(new zzdwp(authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "reauthenticateWithPhoneCredentialWithData"));
    }

    public final Task<Void> zzc(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull zzv zzv) {
        return zzb(zza(new zzdxf(str).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "updatePassword"));
    }

    public final Task<Void> zzc(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        return zzb(zza(new zzdwd(str).zzc(firebaseApp), "applyActionCode"));
    }

    public final Task<AuthResult> zzd(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull AuthCredential authCredential, @NonNull zzv zzv) {
        zzbq.checkNotNull(firebaseApp);
        zzbq.checkNotNull(authCredential);
        zzbq.checkNotNull(firebaseUser);
        zzbq.checkNotNull(zzv);
        List providers = firebaseUser.getProviders();
        if (providers != null && providers.contains(authCredential.getProvider())) {
            return Tasks.forException(zzdxm.zzao(new Status(17015)));
        }
        if (authCredential instanceof EmailAuthCredential) {
            return zzb(zza(new zzdwk((EmailAuthCredential) authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "linkEmailAuthCredential"));
        }
        if (authCredential instanceof PhoneAuthCredential) {
            return zzb(zza(new zzdwm((PhoneAuthCredential) authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "linkPhoneAuthCredential"));
        }
        zzbq.checkNotNull(firebaseApp);
        zzbq.checkNotNull(authCredential);
        zzbq.checkNotNull(firebaseUser);
        zzbq.checkNotNull(zzv);
        return zzb(zza(new zzdwl(authCredential).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "linkFederatedCredential"));
    }

    public final Task<AuthResult> zzd(@NonNull FirebaseApp firebaseApp, @NonNull FirebaseUser firebaseUser, @NonNull String str, @NonNull zzv zzv) {
        zzbq.checkNotNull(firebaseApp);
        zzbq.zzgm(str);
        zzbq.checkNotNull(firebaseUser);
        zzbq.checkNotNull(zzv);
        List providers = firebaseUser.getProviders();
        if ((providers != null && !providers.contains(str)) || firebaseUser.isAnonymous()) {
            return Tasks.forException(zzdxm.zzao(new Status(17016, str)));
        }
        char c = 65535;
        if (str.hashCode() == 1216985755 && str.equals(EmailAuthProvider.PROVIDER_ID)) {
            c = 0;
        }
        return c != 0 ? zzb(zza(new zzdxd(str).zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "unlinkFederatedCredential")) : zzb(zza(new zzdxc().zzc(firebaseApp).zze(firebaseUser).zzbc(zzv).zza((zzp) zzv), "unlinkEmailCredential"));
    }

    public final Task<String> zzd(@NonNull FirebaseApp firebaseApp, @NonNull String str) {
        return zzb(zza(new zzdxi(str).zzc(firebaseApp), "verifyPasswordResetCode"));
    }
}
