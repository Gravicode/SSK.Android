package com.google.firebase.auth.internal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.p001v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.internal.zzbfr;
import com.google.android.gms.internal.zzdyy;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.zzd;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

@KeepName
public class FederatedSignInActivity extends FragmentActivity {
    private static boolean zzmhx = false;

    static class zza extends AsyncTask<Void, Void, Task<AuthResult>> {
        private final zzdyy zzmcw;
        private final WeakReference<FederatedSignInActivity> zzmhy;

        public zza(zzdyy zzdyy, FederatedSignInActivity federatedSignInActivity) {
            this.zzmcw = zzdyy;
            this.zzmhy = new WeakReference<>(federatedSignInActivity);
        }

        /* access modifiers changed from: private */
        /* renamed from: zzc */
        public final Task<AuthResult> doInBackground(Void... voidArr) {
            Task<AuthResult> signInWithCredential = FirebaseAuth.getInstance().signInWithCredential(zzd.zza(this.zzmcw));
            try {
                Tasks.await(signInWithCredential);
                return signInWithCredential;
            } catch (InterruptedException | ExecutionException e) {
                return signInWithCredential;
            }
        }

        private final void zzgo(int i) {
            FederatedSignInActivity federatedSignInActivity = (FederatedSignInActivity) this.zzmhy.get();
            if (federatedSignInActivity != null) {
                federatedSignInActivity.zzgo(i);
            }
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ void onCancelled(Object obj) {
            zzgo(0);
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ void onPostExecute(Object obj) {
            zzgo(-1);
        }
    }

    /* access modifiers changed from: private */
    public final void zzgo(int i) {
        zzmhx = false;
        setResult(i);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        String action = getIntent().getAction();
        if (!action.equals("com.google.firebase.auth.internal.SIGN_IN") && !action.equals("com.google.firebase.auth.internal.GET_CRED")) {
            String str = "IdpSignInActivity";
            String str2 = "Unknown action: ";
            String valueOf = String.valueOf(action);
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (!zzmhx) {
            zzmhx = true;
            if (bundle == null) {
                Intent intent = new Intent("com.google.firebase.auth.api.gms.ui.START_WEB_SIGN_IN");
                intent.setPackage("com.google.android.gms");
                intent.putExtras(getIntent().getExtras());
                try {
                    startActivityForResult(intent, 40963);
                    return;
                } catch (ActivityNotFoundException e) {
                    Log.w("IdpSignInActivity", "Could not launch web sign-in Intent. Google Play service is unavailable");
                    zzgo(0);
                }
            }
            return;
        }
        setResult(0);
        finish();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("com.google.firebase.auth.internal.WEB_SIGN_IN_FAILED".equals(intent.getAction())) {
            Log.e("IdpSignInActivity", "Web sign-in failed, finishing");
            zzgo(0);
            return;
        }
        new zza((zzdyy) zzbfr.zza(intent, "com.google.firebase.auth.internal.VERIFY_ASSERTION_REQUEST", zzdyy.CREATOR), this).execute(new Void[0]);
    }
}
