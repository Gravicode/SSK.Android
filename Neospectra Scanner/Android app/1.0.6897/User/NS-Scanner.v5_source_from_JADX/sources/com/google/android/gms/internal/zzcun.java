package com.google.android.gms.internal;

import android.content.SharedPreferences;
import android.util.Log;

final class zzcun extends zzcui<String> {
    zzcun(zzcup zzcup, String str, String str2) {
        super(zzcup, str, str2, null);
    }

    /* access modifiers changed from: private */
    /* renamed from: zzc */
    public final String zzb(SharedPreferences sharedPreferences) {
        try {
            return sharedPreferences.getString(this.zzjxb, null);
        } catch (ClassCastException e) {
            String str = "PhenotypeFlag";
            String str2 = "Invalid string value in SharedPreferences for ";
            String valueOf = String.valueOf(this.zzjxb);
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e);
            return null;
        }
    }

    public final /* synthetic */ Object zzkt(String str) {
        return str;
    }
}
