package com.google.android.gms.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.internal.zzz;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzab;
import com.google.android.gms.common.internal.zzan;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzbr;
import com.google.android.gms.common.internal.zzj;
import com.google.android.gms.common.internal.zzm;
import com.google.android.gms.common.internal.zzr;

public final class zzcxn extends zzab<zzcxl> implements zzcxd {
    private final zzr zzfpx;
    private Integer zzfzj;
    private final boolean zzkbz;
    private final Bundle zzkca;

    private zzcxn(Context context, Looper looper, boolean z, zzr zzr, Bundle bundle, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 44, zzr, connectionCallbacks, onConnectionFailedListener);
        this.zzkbz = true;
        this.zzfpx = zzr;
        this.zzkca = bundle;
        this.zzfzj = zzr.zzalc();
    }

    public zzcxn(Context context, Looper looper, boolean z, zzr zzr, zzcxe zzcxe, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, true, zzr, zza(zzr), connectionCallbacks, onConnectionFailedListener);
    }

    public static Bundle zza(zzr zzr) {
        zzcxe zzalb = zzr.zzalb();
        Integer zzalc = zzr.zzalc();
        Bundle bundle = new Bundle();
        bundle.putParcelable("com.google.android.gms.signin.internal.clientRequestedAccount", zzr.getAccount());
        if (zzalc != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", zzalc.intValue());
        }
        if (zzalb != null) {
            bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", zzalb.zzbdc());
            bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", zzalb.isIdTokenRequested());
            bundle.putString("com.google.android.gms.signin.internal.serverClientId", zzalb.getServerClientId());
            bundle.putBoolean("com.google.android.gms.signin.internal.usePromptModeForAuthCode", true);
            bundle.putBoolean("com.google.android.gms.signin.internal.forceCodeForRefreshToken", zzalb.zzbdd());
            bundle.putString("com.google.android.gms.signin.internal.hostedDomain", zzalb.zzbde());
            bundle.putBoolean("com.google.android.gms.signin.internal.waitForAccessTokenRefresh", zzalb.zzbdf());
            if (zzalb.zzbdg() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.authApiSignInModuleVersion", zzalb.zzbdg().longValue());
            }
            if (zzalb.zzbdh() != null) {
                bundle.putLong("com.google.android.gms.signin.internal.realClientLibraryVersion", zzalb.zzbdh().longValue());
            }
        }
        return bundle;
    }

    public final void connect() {
        zza((zzj) new zzm(this));
    }

    public final void zza(zzan zzan, boolean z) {
        try {
            ((zzcxl) zzakn()).zza(zzan, this.zzfzj.intValue(), z);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    public final void zza(zzcxj zzcxj) {
        zzbq.checkNotNull(zzcxj, "Expecting a valid ISignInCallbacks");
        try {
            Account zzakt = this.zzfpx.zzakt();
            GoogleSignInAccount googleSignInAccount = null;
            if ("<<default account>>".equals(zzakt.name)) {
                googleSignInAccount = zzz.zzbt(getContext()).zzabt();
            }
            ((zzcxl) zzakn()).zza(new zzcxo(new zzbr(zzakt, this.zzfzj.intValue(), googleSignInAccount)), zzcxj);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when signIn is called");
            try {
                zzcxj.zzb(new zzcxq(8));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onSignInComplete should be executed from the same process, unexpected RemoteException.", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final Bundle zzaap() {
        if (!getContext().getPackageName().equals(this.zzfpx.zzaky())) {
            this.zzkca.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzfpx.zzaky());
        }
        return this.zzkca;
    }

    public final boolean zzaay() {
        return this.zzkbz;
    }

    public final void zzbdb() {
        try {
            ((zzcxl) zzakn()).zzeh(this.zzfzj.intValue());
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ IInterface zzd(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.ISignInService");
        return queryLocalInterface instanceof zzcxl ? (zzcxl) queryLocalInterface : new zzcxm(iBinder);
    }

    /* access modifiers changed from: protected */
    public final String zzhi() {
        return "com.google.android.gms.signin.service.START";
    }

    /* access modifiers changed from: protected */
    public final String zzhj() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }
}
