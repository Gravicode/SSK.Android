package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzau;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.zzbfm;
import com.google.android.gms.internal.zzbfp;

public final class zzn extends zzbfm {
    public static final Creator<zzn> CREATOR = new zzo();
    private final String zzflg;
    private final zzh zzflh;
    private final boolean zzfli;

    zzn(String str, IBinder iBinder, boolean z) {
        this.zzflg = str;
        this.zzflh = zzak(iBinder);
        this.zzfli = z;
    }

    zzn(String str, zzh zzh, boolean z) {
        this.zzflg = str;
        this.zzflh = zzh;
        this.zzfli = z;
    }

    private static zzh zzak(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        try {
            IObjectWrapper zzaga = zzau.zzam(iBinder).zzaga();
            byte[] bArr = zzaga == null ? null : (byte[]) com.google.android.gms.dynamic.zzn.zzx(zzaga);
            if (bArr != null) {
                return new zzi(bArr);
            }
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
            return null;
        } catch (RemoteException e) {
            Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", e);
            return null;
        }
    }

    public final void writeToParcel(Parcel parcel, int i) {
        IBinder iBinder;
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 1, this.zzflg, false);
        if (this.zzflh == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            iBinder = null;
        } else {
            iBinder = this.zzflh.asBinder();
        }
        zzbfp.zza(parcel, 2, iBinder, false);
        zzbfp.zza(parcel, 3, this.zzfli);
        zzbfp.zzai(parcel, zze);
    }
}
