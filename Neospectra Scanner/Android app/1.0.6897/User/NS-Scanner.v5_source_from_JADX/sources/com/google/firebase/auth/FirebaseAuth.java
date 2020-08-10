package com.google.firebase.auth;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.util.ArrayMap;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.util.zzq;
import com.google.android.gms.internal.zzdwc;
import com.google.android.gms.internal.zzdxr;
import com.google.android.gms.internal.zzdxu;
import com.google.android.gms.internal.zzdxv;
import com.google.android.gms.internal.zzdym;
import com.google.android.gms.internal.zzdyu;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import com.google.firebase.auth.internal.zze;
import com.google.firebase.auth.internal.zzg;
import com.google.firebase.auth.internal.zzh;
import com.google.firebase.auth.internal.zzo;
import com.google.firebase.auth.internal.zzp;
import com.google.firebase.auth.internal.zzr;
import com.google.firebase.auth.internal.zzs;
import com.google.firebase.auth.internal.zzu;
import com.google.firebase.internal.InternalTokenProvider;
import com.google.firebase.internal.zzc;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class FirebaseAuth implements InternalTokenProvider {
    private static Map<String, FirebaseAuth> zzifg = new ArrayMap();
    private static FirebaseAuth zzmdg;
    /* access modifiers changed from: private */
    public List<IdTokenListener> zzmav;
    /* access modifiers changed from: private */
    public FirebaseApp zzmcx;
    /* access modifiers changed from: private */
    public List<AuthStateListener> zzmcy;
    private zzdwc zzmcz;
    /* access modifiers changed from: private */
    public FirebaseUser zzmda;
    private final Object zzmdb;
    private String zzmdc;
    private zzr zzmdd;
    private zzs zzmde;
    private zzu zzmdf;

    public interface AuthStateListener {
        void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth);
    }

    public interface IdTokenListener {
        void onIdTokenChanged(@NonNull FirebaseAuth firebaseAuth);
    }

    class zza implements com.google.firebase.auth.internal.zza {
        zza() {
        }

        public final void zza(@NonNull zzdym zzdym, @NonNull FirebaseUser firebaseUser) {
            zzbq.checkNotNull(zzdym);
            zzbq.checkNotNull(firebaseUser);
            firebaseUser.zza(zzdym);
            FirebaseAuth.this.zza(firebaseUser, zzdym, true);
        }
    }

    class zzb extends zza implements com.google.firebase.auth.internal.zza, zzp {
        zzb() {
            super();
        }

        public final void onError(Status status) {
            if (status.getStatusCode() == 17011 || status.getStatusCode() == 17021 || status.getStatusCode() == 17005) {
                FirebaseAuth.this.signOut();
            }
        }
    }

    public FirebaseAuth(FirebaseApp firebaseApp) {
        this(firebaseApp, zzdxr.zza(firebaseApp.getApplicationContext(), new zzdxu(firebaseApp.getOptions().getApiKey()).zzbrq()), new zzr(firebaseApp.getApplicationContext(), firebaseApp.zzbqp()));
    }

    private FirebaseAuth(FirebaseApp firebaseApp, zzdwc zzdwc, zzr zzr) {
        this.zzmdb = new Object();
        this.zzmcx = (FirebaseApp) zzbq.checkNotNull(firebaseApp);
        this.zzmcz = (zzdwc) zzbq.checkNotNull(zzdwc);
        this.zzmdd = (zzr) zzbq.checkNotNull(zzr);
        this.zzmav = new CopyOnWriteArrayList();
        this.zzmcy = new CopyOnWriteArrayList();
        this.zzmdf = zzu.zzbsj();
        this.zzmda = this.zzmdd.zzbsh();
        if (this.zzmda != null) {
            zzdym zzg = this.zzmdd.zzg(this.zzmda);
            if (zzg != null) {
                zza(this.zzmda, zzg, false);
            }
        }
    }

    @Keep
    public static FirebaseAuth getInstance() {
        return zzb(FirebaseApp.getInstance());
    }

    @Keep
    public static FirebaseAuth getInstance(@NonNull FirebaseApp firebaseApp) {
        return zzb(firebaseApp);
    }

    private final void zza(@Nullable FirebaseUser firebaseUser) {
        String str;
        String str2;
        if (firebaseUser != null) {
            str = "FirebaseAuth";
            String uid = firebaseUser.getUid();
            StringBuilder sb = new StringBuilder(String.valueOf(uid).length() + 45);
            sb.append("Notifying id token listeners about user ( ");
            sb.append(uid);
            sb.append(" ).");
            str2 = sb.toString();
        } else {
            str = "FirebaseAuth";
            str2 = "Notifying id token listeners about a sign-out event.";
        }
        Log.d(str, str2);
        this.zzmdf.execute(new zzj(this, new zzc(firebaseUser != null ? firebaseUser.zzbrh() : null)));
    }

    private final synchronized void zza(zzs zzs) {
        this.zzmde = zzs;
        this.zzmcx.zza((com.google.firebase.FirebaseApp.zzb) zzs);
    }

    private static synchronized FirebaseAuth zzb(@NonNull FirebaseApp firebaseApp) {
        synchronized (FirebaseAuth.class) {
            FirebaseAuth firebaseAuth = (FirebaseAuth) zzifg.get(firebaseApp.zzbqp());
            if (firebaseAuth != null) {
                return firebaseAuth;
            }
            zzg zzg = new zzg(firebaseApp);
            firebaseApp.zza((InternalTokenProvider) zzg);
            if (zzmdg == null) {
                zzmdg = zzg;
            }
            zzifg.put(firebaseApp.zzbqp(), zzg);
            return zzg;
        }
    }

    private final void zzb(@Nullable FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            String uid = firebaseUser.getUid();
            StringBuilder sb = new StringBuilder(String.valueOf(uid).length() + 47);
            sb.append("Notifying auth state listeners about user ( ");
            sb.append(uid);
            sb.append(" ).");
            Log.d("FirebaseAuth", sb.toString());
        } else {
            Log.d("FirebaseAuth", "Notifying auth state listeners about a sign-out event.");
        }
        this.zzmdf.execute(new zzk(this));
    }

    private final synchronized zzs zzbrc() {
        if (this.zzmde == null) {
            zza(new zzs(this.zzmcx));
        }
        return this.zzmde;
    }

    public void addAuthStateListener(@NonNull AuthStateListener authStateListener) {
        this.zzmcy.add(authStateListener);
        this.zzmdf.execute(new zzi(this, authStateListener));
    }

    public void addIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        this.zzmav.add(idTokenListener);
        this.zzmdf.execute(new zzh(this, idTokenListener));
    }

    @NonNull
    public Task<Void> applyActionCode(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zzc(this.zzmcx, str);
    }

    @NonNull
    public Task<ActionCodeResult> checkActionCode(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zzb(this.zzmcx, str);
    }

    @NonNull
    public Task<Void> confirmPasswordReset(@NonNull String str, @NonNull String str2) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        return this.zzmcz.zza(this.zzmcx, str, str2);
    }

    @NonNull
    public Task<AuthResult> createUserWithEmailAndPassword(@NonNull String str, @NonNull String str2) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        return this.zzmcz.zza(this.zzmcx, str, str2, (com.google.firebase.auth.internal.zza) new zza());
    }

    @NonNull
    public Task<ProviderQueryResult> fetchProvidersForEmail(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zza(this.zzmcx, str);
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return this.zzmda;
    }

    @Nullable
    public String getLanguageCode() {
        String str;
        synchronized (this.zzmdb) {
            str = this.zzmdc;
        }
        return str;
    }

    @Nullable
    public final String getUid() {
        if (this.zzmda == null) {
            return null;
        }
        return this.zzmda.getUid();
    }

    public void removeAuthStateListener(@NonNull AuthStateListener authStateListener) {
        this.zzmcy.remove(authStateListener);
    }

    public void removeIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        this.zzmav.remove(idTokenListener);
    }

    @NonNull
    public Task<Void> sendPasswordResetEmail(@NonNull String str) {
        zzbq.zzgm(str);
        return sendPasswordResetEmail(str, null);
    }

    @NonNull
    public Task<Void> sendPasswordResetEmail(@NonNull String str, @Nullable ActionCodeSettings actionCodeSettings) {
        zzbq.zzgm(str);
        if (actionCodeSettings == null) {
            actionCodeSettings = ActionCodeSettings.newBuilder().build();
        }
        if (this.zzmdc != null) {
            actionCodeSettings.zzou(this.zzmdc);
        }
        actionCodeSettings.zzgl(1);
        return this.zzmcz.zza(this.zzmcx, str, actionCodeSettings);
    }

    public Task<Void> setFirebaseUIVersion(@Nullable String str) {
        return this.zzmcz.setFirebaseUIVersion(str);
    }

    public void setLanguageCode(@NonNull String str) {
        zzbq.zzgm(str);
        synchronized (this.zzmdb) {
            this.zzmdc = str;
        }
    }

    @NonNull
    public Task<AuthResult> signInAnonymously() {
        if (this.zzmda == null || !this.zzmda.isAnonymous()) {
            return this.zzmcz.zza(this.zzmcx, (com.google.firebase.auth.internal.zza) new zza());
        }
        zzh zzh = (zzh) this.zzmda;
        zzh.zzci(false);
        return Tasks.forResult(new zze(zzh));
    }

    @NonNull
    public Task<AuthResult> signInWithCredential(@NonNull AuthCredential authCredential) {
        zzbq.checkNotNull(authCredential);
        if (authCredential instanceof EmailAuthCredential) {
            EmailAuthCredential emailAuthCredential = (EmailAuthCredential) authCredential;
            return this.zzmcz.zzb(this.zzmcx, emailAuthCredential.getEmail(), emailAuthCredential.getPassword(), (com.google.firebase.auth.internal.zza) new zza());
        } else if (!(authCredential instanceof PhoneAuthCredential)) {
            return this.zzmcz.zza(this.zzmcx, authCredential, (com.google.firebase.auth.internal.zza) new zza());
        } else {
            return this.zzmcz.zza(this.zzmcx, (PhoneAuthCredential) authCredential, (com.google.firebase.auth.internal.zza) new zza());
        }
    }

    @NonNull
    public Task<AuthResult> signInWithCustomToken(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zza(this.zzmcx, str, (com.google.firebase.auth.internal.zza) new zza());
    }

    @NonNull
    public Task<AuthResult> signInWithEmailAndPassword(@NonNull String str, @NonNull String str2) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        return this.zzmcz.zzb(this.zzmcx, str, str2, (com.google.firebase.auth.internal.zza) new zza());
    }

    public void signOut() {
        zzbrb();
        if (this.zzmde != null) {
            this.zzmde.cancel();
        }
    }

    public void useAppLanguage() {
        synchronized (this.zzmdb) {
            this.zzmdc = zzdxv.zzbrr();
        }
    }

    @NonNull
    public Task<String> verifyPasswordResetCode(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zzd(this.zzmcx, str);
    }

    @NonNull
    public final Task<Void> zza(@NonNull ActionCodeSettings actionCodeSettings, @NonNull String str) {
        zzbq.zzgm(str);
        if (this.zzmdc != null) {
            if (actionCodeSettings == null) {
                actionCodeSettings = ActionCodeSettings.newBuilder().build();
            }
            actionCodeSettings.zzou(this.zzmdc);
        }
        return this.zzmcz.zza(this.zzmcx, actionCodeSettings, str);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: type inference failed for: r2v1, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: type inference failed for: r5v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 28
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zza(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r7, @android.support.annotation.NonNull com.google.firebase.auth.AuthCredential r8) {
        /*
            r6 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r7)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r8)
            java.lang.Class<com.google.firebase.auth.EmailAuthCredential> r0 = com.google.firebase.auth.EmailAuthCredential.class
            java.lang.Class r1 = r8.getClass()
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 == 0) goto L_0x002b
            com.google.firebase.auth.EmailAuthCredential r8 = (com.google.firebase.auth.EmailAuthCredential) r8
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            java.lang.String r3 = r8.getEmail()
            java.lang.String r4 = r8.getPassword()
            com.google.firebase.auth.FirebaseAuth$zzb r5 = new com.google.firebase.auth.FirebaseAuth$zzb
            r5.<init>()
            r2 = r7
            com.google.android.gms.tasks.Task r7 = r0.zza(r1, r2, r3, r4, r5)
            return r7
        L_0x002b:
            boolean r0 = r8 instanceof com.google.firebase.auth.PhoneAuthCredential
            if (r0 == 0) goto L_0x003f
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            com.google.firebase.auth.PhoneAuthCredential r8 = (com.google.firebase.auth.PhoneAuthCredential) r8
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r7 = r0.zzb(r1, r7, r8, r2)
            return r7
        L_0x003f:
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r7 = r0.zza(r1, r7, r8, r2)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zza(com.google.firebase.auth.FirebaseUser, com.google.firebase.auth.AuthCredential):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zza(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull com.google.firebase.auth.PhoneAuthCredential r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r5)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zza(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zza(com.google.firebase.auth.FirebaseUser, com.google.firebase.auth.PhoneAuthCredential):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zza(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull com.google.firebase.auth.UserProfileChangeRequest r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r5)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zza(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zza(com.google.firebase.auth.FirebaseUser, com.google.firebase.auth.UserProfileChangeRequest):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> zza(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull java.lang.String r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.zzgm(r5)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zzd(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zza(com.google.firebase.auth.FirebaseUser, java.lang.String):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.zzl, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.zzl, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.zzl]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 20
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<com.google.firebase.auth.GetTokenResult> zza(@android.support.annotation.Nullable com.google.firebase.auth.FirebaseUser r4, boolean r5) {
        /*
            r3 = this;
            if (r4 != 0) goto L_0x0012
            com.google.android.gms.common.api.Status r4 = new com.google.android.gms.common.api.Status
            r5 = 17495(0x4457, float:2.4516E-41)
            r4.<init>(r5)
            com.google.firebase.FirebaseException r4 = com.google.android.gms.internal.zzdxm.zzao(r4)
            com.google.android.gms.tasks.Task r4 = com.google.android.gms.tasks.Tasks.forException(r4)
            return r4
        L_0x0012:
            com.google.firebase.auth.FirebaseUser r0 = r3.zzmda
            com.google.android.gms.internal.zzdym r0 = r0.zzbrf()
            boolean r1 = r0.isValid()
            if (r1 == 0) goto L_0x002e
            if (r5 != 0) goto L_0x002e
            com.google.firebase.auth.GetTokenResult r4 = new com.google.firebase.auth.GetTokenResult
            java.lang.String r5 = r0.getAccessToken()
            r4.<init>(r5)
            com.google.android.gms.tasks.Task r4 = com.google.android.gms.tasks.Tasks.forResult(r4)
            return r4
        L_0x002e:
            com.google.android.gms.internal.zzdwc r5 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            java.lang.String r0 = r0.zzbru()
            com.google.firebase.auth.zzl r2 = new com.google.firebase.auth.zzl
            r2.<init>(r3)
            com.google.android.gms.tasks.Task r4 = r5.zza(r1, r4, r0, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zza(com.google.firebase.auth.FirebaseUser, boolean):com.google.android.gms.tasks.Task");
    }

    public final void zza(@NonNull FirebaseUser firebaseUser, @NonNull zzdym zzdym, boolean z) {
        boolean z2;
        zzbq.checkNotNull(firebaseUser);
        zzbq.checkNotNull(zzdym);
        boolean z3 = true;
        if (this.zzmda == null) {
            z2 = true;
        } else {
            boolean z4 = !this.zzmda.zzbrf().getAccessToken().equals(zzdym.getAccessToken());
            boolean equals = this.zzmda.getUid().equals(firebaseUser.getUid());
            z2 = !equals || z4;
            if (equals) {
                z3 = false;
            }
        }
        zzbq.checkNotNull(firebaseUser);
        if (this.zzmda == null) {
            this.zzmda = firebaseUser;
        } else {
            this.zzmda.zzcf(firebaseUser.isAnonymous());
            this.zzmda.zzaq(firebaseUser.getProviderData());
        }
        if (z) {
            this.zzmdd.zzf(this.zzmda);
        }
        if (z2) {
            if (this.zzmda != null) {
                this.zzmda.zza(zzdym);
            }
            zza(this.zzmda);
        }
        if (z3) {
            zzb(this.zzmda);
        }
        if (z) {
            this.zzmdd.zza(firebaseUser, zzdym);
        }
        zzbrc().zzc(this.zzmda.zzbrf());
    }

    @NonNull
    public final void zza(@NonNull String str, long j, TimeUnit timeUnit, @NonNull OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks, @Nullable Activity activity, @NonNull Executor executor, boolean z) {
        String str2;
        long convert;
        String str3;
        Context applicationContext = this.zzmcx.getApplicationContext();
        zzbq.checkNotNull(applicationContext);
        zzbq.zzgm(str);
        TelephonyManager telephonyManager = (TelephonyManager) applicationContext.getSystemService(PhoneAuthProvider.PROVIDER_ID);
        String str4 = null;
        String simCountryIso = telephonyManager != null ? telephonyManager.getSimCountryIso() : null;
        if (TextUtils.isEmpty(simCountryIso)) {
            Locale locale = Locale.getDefault();
            simCountryIso = locale != null ? locale.getCountry() : null;
        }
        String upperCase = TextUtils.isEmpty(simCountryIso) ? "US" : simCountryIso.toUpperCase(Locale.US);
        String stripSeparators = PhoneNumberUtils.stripSeparators(str);
        if (zzq.zzamn()) {
            String formatNumberToE164 = PhoneNumberUtils.formatNumberToE164(stripSeparators, upperCase);
            if (!TextUtils.isEmpty(formatNumberToE164)) {
                str2 = formatNumberToE164;
                convert = TimeUnit.SECONDS.convert(j, timeUnit);
                if (convert >= 0 || convert > 120) {
                    throw new IllegalArgumentException("We only support 0-120 seconds for sms-auto-retrieval timeout");
                }
                zzdyu zzdyu = new zzdyu(str2, convert, z, this.zzmdc);
                this.zzmcz.zza(this.zzmcx, zzdyu, onVerificationStateChangedCallbacks, activity, executor);
                return;
            }
        } else if ("US".equals(upperCase)) {
            if (stripSeparators != null) {
                int length = stripSeparators.length();
                if (!stripSeparators.startsWith("+")) {
                    if (length == 11 && stripSeparators.startsWith("1")) {
                        str3 = "+";
                    } else if (length == 10) {
                        str3 = "+1";
                    }
                    str4 = str3.concat(stripSeparators);
                }
            }
            stripSeparators = str4;
        }
        str2 = stripSeparators;
        convert = TimeUnit.SECONDS.convert(j, timeUnit);
        if (convert >= 0) {
        }
        throw new IllegalArgumentException("We only support 0-120 seconds for sms-auto-retrieval timeout");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: type inference failed for: r2v1, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: type inference failed for: r5v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 27
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> zzb(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r7, @android.support.annotation.NonNull com.google.firebase.auth.AuthCredential r8) {
        /*
            r6 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r7)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r8)
            java.lang.Class<com.google.firebase.auth.EmailAuthCredential> r0 = com.google.firebase.auth.EmailAuthCredential.class
            java.lang.Class r1 = r8.getClass()
            boolean r0 = r0.isAssignableFrom(r1)
            if (r0 == 0) goto L_0x002b
            com.google.firebase.auth.EmailAuthCredential r8 = (com.google.firebase.auth.EmailAuthCredential) r8
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            java.lang.String r3 = r8.getEmail()
            java.lang.String r4 = r8.getPassword()
            com.google.firebase.auth.FirebaseAuth$zzb r5 = new com.google.firebase.auth.FirebaseAuth$zzb
            r5.<init>()
            r2 = r7
            com.google.android.gms.tasks.Task r7 = r0.zzb(r1, r2, r3, r4, r5)
            return r7
        L_0x002b:
            boolean r0 = r8 instanceof com.google.firebase.auth.PhoneAuthCredential
            if (r0 == 0) goto L_0x003d
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r7 = r0.zzc(r1, r7, r8, r2)
            return r7
        L_0x003d:
            com.google.android.gms.internal.zzdwc r0 = r6.zzmcz
            com.google.firebase.FirebaseApp r1 = r6.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r7 = r0.zzb(r1, r7, r8, r2)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zzb(com.google.firebase.auth.FirebaseUser, com.google.firebase.auth.AuthCredential):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zzb(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull java.lang.String r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.common.internal.zzbq.zzgm(r5)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zzb(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zzb(com.google.firebase.auth.FirebaseUser, java.lang.String):com.google.android.gms.tasks.Task");
    }

    public final void zzbrb() {
        if (this.zzmda != null) {
            zzr zzr = this.zzmdd;
            FirebaseUser firebaseUser = this.zzmda;
            zzbq.checkNotNull(firebaseUser);
            zzr.clear(String.format("com.google.firebase.auth.GET_TOKEN_RESPONSE.%s", new Object[]{firebaseUser.getUid()}));
            this.zzmda = null;
        }
        this.zzmdd.clear("com.google.firebase.auth.FIREBASE_USER");
        zza((FirebaseUser) null);
        zzb((FirebaseUser) null);
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 6
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zzc(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zza(r1, r4, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zzc(com.google.firebase.auth.FirebaseUser):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<com.google.firebase.auth.AuthResult> zzc(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull com.google.firebase.auth.AuthCredential r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r5)
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zzd(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zzc(com.google.firebase.auth.FirebaseUser, com.google.firebase.auth.AuthCredential):com.google.android.gms.tasks.Task");
    }

    /* JADX WARNING: type inference failed for: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v0, types: [com.google.firebase.auth.FirebaseAuth$zzb, com.google.firebase.auth.internal.zzv]
      assigns: [com.google.firebase.auth.FirebaseAuth$zzb]
      uses: [com.google.firebase.auth.internal.zzv]
      mth insns count: 7
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.support.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.google.android.gms.tasks.Task<java.lang.Void> zzc(@android.support.annotation.NonNull com.google.firebase.auth.FirebaseUser r4, @android.support.annotation.NonNull java.lang.String r5) {
        /*
            r3 = this;
            com.google.android.gms.common.internal.zzbq.checkNotNull(r4)
            com.google.android.gms.common.internal.zzbq.zzgm(r5)
            com.google.android.gms.internal.zzdwc r0 = r3.zzmcz
            com.google.firebase.FirebaseApp r1 = r3.zzmcx
            com.google.firebase.auth.FirebaseAuth$zzb r2 = new com.google.firebase.auth.FirebaseAuth$zzb
            r2.<init>()
            com.google.android.gms.tasks.Task r4 = r0.zzc(r1, r4, r5, r2)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.auth.FirebaseAuth.zzc(com.google.firebase.auth.FirebaseUser, java.lang.String):com.google.android.gms.tasks.Task");
    }

    @NonNull
    public final Task<GetTokenResult> zzce(boolean z) {
        return zza(this.zzmda, z);
    }

    @NonNull
    public final Task<Void> zzd(@NonNull FirebaseUser firebaseUser) {
        zzbq.checkNotNull(firebaseUser);
        return this.zzmcz.zza(firebaseUser, (zzo) new zzm(this, firebaseUser));
    }

    @NonNull
    public final Task<Void> zzov(@NonNull String str) {
        zzbq.zzgm(str);
        return this.zzmcz.zza(this.zzmcx, (ActionCodeSettings) null, str);
    }
}
