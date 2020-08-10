package com.google.android.gms.auth.api.signin.internal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.LoaderManager.LoaderCallbacks;
import android.support.p001v4.content.Loader;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

@KeepName
public class SignInHubActivity extends FragmentActivity {
    private static boolean zzein = false;
    private boolean zzeio = false;
    @VisibleForTesting
    private SignInConfiguration zzeip;
    private boolean zzeiq;
    /* access modifiers changed from: private */
    public int zzeir;
    /* access modifiers changed from: private */
    public Intent zzeis;

    class zza implements LoaderCallbacks<Void> {
        private zza() {
        }

        public final Loader<Void> onCreateLoader(int i, Bundle bundle) {
            return new zzb(SignInHubActivity.this, GoogleApiClient.zzagr());
        }

        public final /* synthetic */ void onLoadFinished(Loader loader, Object obj) {
            SignInHubActivity.this.setResult(SignInHubActivity.this.zzeir, SignInHubActivity.this.zzeis);
            SignInHubActivity.this.finish();
        }

        public final void onLoaderReset(Loader<Void> loader) {
        }
    }

    private final void zzabs() {
        getSupportLoaderManager().initLoader(0, null, new zza());
        zzein = false;
    }

    private final void zzba(int i) {
        Status status = new Status(i);
        Intent intent = new Intent();
        intent.putExtra("googleSignInStatus", status);
        setResult(0, intent);
        finish();
        zzein = false;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (!this.zzeio) {
            setResult(0);
            if (i == 40962) {
                if (intent != null) {
                    SignInAccount signInAccount = (SignInAccount) intent.getParcelableExtra(GoogleSignInApi.EXTRA_SIGN_IN_ACCOUNT);
                    if (signInAccount != null && signInAccount.getGoogleSignInAccount() != null) {
                        GoogleSignInAccount googleSignInAccount = signInAccount.getGoogleSignInAccount();
                        zzo.zzbr(this).zza(this.zzeip.zzabr(), googleSignInAccount);
                        intent.removeExtra(GoogleSignInApi.EXTRA_SIGN_IN_ACCOUNT);
                        intent.putExtra("googleSignInAccount", googleSignInAccount);
                        this.zzeiq = true;
                        this.zzeir = i2;
                        this.zzeis = intent;
                        zzabs();
                        return;
                    } else if (intent.hasExtra("errorCode")) {
                        zzba(intent.getIntExtra("errorCode", 8));
                        return;
                    }
                }
                zzba(8);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        String action = intent.getAction();
        if ("com.google.android.gms.auth.NO_IMPL".equals(action)) {
            zzba(GoogleSignInStatusCodes.SIGN_IN_FAILED);
        } else if (zzein) {
            setResult(0);
            zzba(GoogleSignInStatusCodes.SIGN_IN_CURRENTLY_IN_PROGRESS);
        } else {
            zzein = true;
            if (action.equals("com.google.android.gms.auth.GOOGLE_SIGN_IN") || action.equals("com.google.android.gms.auth.APPAUTH_SIGN_IN")) {
                this.zzeip = (SignInConfiguration) intent.getBundleExtra("config").getParcelable("config");
                if (this.zzeip == null) {
                    Log.e("AuthSignInClient", "Activity started with invalid configuration.");
                    setResult(0);
                    finish();
                } else if (bundle == null) {
                    Intent intent2 = new Intent(action);
                    intent2.setPackage(action.equals("com.google.android.gms.auth.GOOGLE_SIGN_IN") ? "com.google.android.gms" : getPackageName());
                    intent2.putExtra("config", this.zzeip);
                    try {
                        startActivityForResult(intent2, 40962);
                    } catch (ActivityNotFoundException e) {
                        this.zzeio = true;
                        Log.w("AuthSignInClient", "Could not launch sign in Intent. Google Play Service is probably being updated...");
                        zzba(17);
                    }
                } else {
                    this.zzeiq = bundle.getBoolean("signingInGoogleApiClients");
                    if (this.zzeiq) {
                        this.zzeir = bundle.getInt("signInResultCode");
                        this.zzeis = (Intent) bundle.getParcelable("signInResultData");
                        zzabs();
                    }
                }
            } else {
                String str = "AuthSignInClient";
                String str2 = "Unknown action: ";
                String valueOf = String.valueOf(intent.getAction());
                Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("signingInGoogleApiClients", this.zzeiq);
        if (this.zzeiq) {
            bundle.putInt("signInResultCode", this.zzeir);
            bundle.putParcelable("signInResultData", this.zzeis);
        }
    }
}
