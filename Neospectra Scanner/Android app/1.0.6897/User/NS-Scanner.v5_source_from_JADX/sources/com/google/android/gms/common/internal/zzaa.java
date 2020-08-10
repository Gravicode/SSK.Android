package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.zzc;
import com.google.android.gms.internal.zzbfn;

public final class zzaa implements Creator<zzz> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        String str = null;
        IBinder iBinder = null;
        Scope[] scopeArr = null;
        Bundle bundle = null;
        Account account = null;
        zzc[] zzcArr = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzd) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 1:
                    i = zzbfn.zzg(parcel, readInt);
                    break;
                case 2:
                    i2 = zzbfn.zzg(parcel, readInt);
                    break;
                case 3:
                    i3 = zzbfn.zzg(parcel, readInt);
                    break;
                case 4:
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 5:
                    iBinder = zzbfn.zzr(parcel, readInt);
                    break;
                case 6:
                    scopeArr = (Scope[]) zzbfn.zzb(parcel, readInt, Scope.CREATOR);
                    break;
                case 7:
                    bundle = zzbfn.zzs(parcel, readInt);
                    break;
                case 8:
                    account = (Account) zzbfn.zza(parcel, readInt, Account.CREATOR);
                    break;
                case 10:
                    zzcArr = (zzc[]) zzbfn.zzb(parcel, readInt, zzc.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        zzz zzz = new zzz(i, i2, i3, str, iBinder, scopeArr, bundle, account, zzcArr);
        return zzz;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzz[i];
    }
}
