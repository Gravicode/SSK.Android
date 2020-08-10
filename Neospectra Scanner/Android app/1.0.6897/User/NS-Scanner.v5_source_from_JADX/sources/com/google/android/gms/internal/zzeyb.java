package com.google.android.gms.internal;

import android.content.Context;
import android.net.Uri;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.dynamic.zzn;
import com.google.android.gms.dynamite.DynamiteModule;
import com.google.android.gms.dynamite.DynamiteModule.zzc;
import com.google.android.gms.dynamite.DynamiteModule.zzd;
import com.google.firebase.FirebaseApp;
import org.json.JSONObject;

public final class zzeyb {
    private static zzd zzoly = DynamiteModule.zzgxa;
    private static final Object zzolz = new Object();
    private static volatile zzeyb zzoma;
    private Context mContext;
    private FirebaseApp zzoic;
    private zzexz zzomb;

    private zzeyb(@NonNull FirebaseApp firebaseApp) throws RemoteException {
        zzexz zzexz;
        this.mContext = firebaseApp.getApplicationContext();
        this.zzoic = firebaseApp;
        try {
            IBinder zzhb = DynamiteModule.zza(this.mContext, zzoly, "com.google.android.gms.firebasestorage").zzhb("com.google.firebase.storage.network.NetworkRequestFactoryImpl");
            if (zzhb == null) {
                zzexz = null;
            } else {
                IInterface queryLocalInterface = zzhb.queryLocalInterface("com.google.firebase.storage.network.INetworkRequestFactory");
                zzexz = queryLocalInterface instanceof zzexz ? (zzexz) queryLocalInterface : new zzeya(zzhb);
            }
            this.zzomb = zzexz;
            if (this.zzomb == null) {
                Log.e("NetworkRqFactoryProxy", "Unable to load Firebase Storage Network layer.");
                throw new RemoteException();
            }
        } catch (zzc e) {
            Log.e("NetworkRqFactoryProxy", "NetworkRequestFactoryProxy failed with a RemoteException:", e);
            throw new RemoteException();
        }
    }

    private final zzeyc zze(zzeyc zzeyc) {
        zzeyc.zzbq("x-firebase-gmpid", this.zzoic.getOptions().getApplicationId());
        return zzeyc;
    }

    public static zzeyb zzi(@NonNull FirebaseApp firebaseApp) throws RemoteException {
        if (zzoma == null) {
            synchronized (zzolz) {
                if (zzoma == null) {
                    zzoma = new zzeyb(firebaseApp);
                }
            }
        }
        return zzoma;
    }

    @NonNull
    public final zzeyc zza(Uri uri, long j) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext), j)));
    }

    @Nullable
    public final zzeyc zza(Uri uri, String str) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext), str)));
    }

    @NonNull
    public final zzeyc zza(Uri uri, String str, byte[] bArr, long j, int i, boolean z) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext), str, zzn.zzz(bArr), j, i, z)));
    }

    @NonNull
    public final zzeyc zza(Uri uri, JSONObject jSONObject) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext), zzn.zzz(jSONObject))));
    }

    @NonNull
    public final zzeyc zza(Uri uri, JSONObject jSONObject, String str) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext), zzn.zzz(jSONObject), str)));
    }

    @NonNull
    public final zzeyc zzb(Uri uri, String str) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zzb(uri, zzn.zzz(this.mContext), str)));
    }

    @Nullable
    public final String zzcmo() {
        try {
            return this.zzomb.zzcmo();
        } catch (RemoteException e) {
            Log.e("NetworkRqFactoryProxy", "getBackendAuthority failed with a RemoteException:", e);
            return null;
        }
    }

    @Nullable
    public final String zzu(Uri uri) {
        try {
            return this.zzomb.zzu(uri);
        } catch (RemoteException e) {
            Log.e("NetworkRqFactoryProxy", "getDefaultURL failed with a RemoteException:", e);
            return null;
        }
    }

    @NonNull
    public final zzeyc zzv(Uri uri) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zza(uri, zzn.zzz(this.mContext))));
    }

    @NonNull
    public final zzeyc zzw(Uri uri) throws RemoteException {
        return zze(new zzeyc(this.zzomb.zzb(uri, zzn.zzz(this.mContext))));
    }
}
