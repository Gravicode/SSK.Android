package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzbfn;

public final class zzb implements Creator<AccountChangeEventsRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int zzd = zzbfn.zzd(parcel);
        int i = 0;
        String str = null;
        Account account = null;
        int i2 = 0;
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
                    str = zzbfn.zzq(parcel, readInt);
                    break;
                case 4:
                    account = (Account) zzbfn.zza(parcel, readInt, Account.CREATOR);
                    break;
                default:
                    zzbfn.zzb(parcel, readInt);
                    break;
            }
        }
        zzbfn.zzaf(parcel, zzd);
        return new AccountChangeEventsRequest(i, i2, str, account);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new AccountChangeEventsRequest[i];
    }
}
