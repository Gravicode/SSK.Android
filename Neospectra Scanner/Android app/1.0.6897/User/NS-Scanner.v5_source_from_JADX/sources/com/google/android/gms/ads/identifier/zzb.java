package com.google.android.gms.ads.identifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.android.gms.common.zzp;

public final class zzb {
    private SharedPreferences zzani;

    public zzb(Context context) {
        try {
            Context remoteContext = zzp.getRemoteContext(context);
            this.zzani = remoteContext == null ? null : remoteContext.getSharedPreferences("google_ads_flags", 0);
        } catch (Throwable th) {
            Log.w("GmscoreFlag", "Error while getting SharedPreferences ", th);
            this.zzani = null;
        }
    }

    public final boolean getBoolean(String str, boolean z) {
        try {
            if (this.zzani == null) {
                return false;
            }
            return this.zzani.getBoolean(str, false);
        } catch (Throwable th) {
            Log.w("GmscoreFlag", "Error while reading from SharedPreferences ", th);
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public final float getFloat(String str, float f) {
        try {
            if (this.zzani == null) {
                return 0.0f;
            }
            return this.zzani.getFloat(str, 0.0f);
        } catch (Throwable th) {
            Log.w("GmscoreFlag", "Error while reading from SharedPreferences ", th);
            return 0.0f;
        }
    }

    /* access modifiers changed from: 0000 */
    public final String getString(String str, String str2) {
        try {
            return this.zzani == null ? str2 : this.zzani.getString(str, str2);
        } catch (Throwable th) {
            Log.w("GmscoreFlag", "Error while reading from SharedPreferences ", th);
            return str2;
        }
    }
}
