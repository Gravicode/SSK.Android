package com.google.android.gms.auth.api.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.internal.zzbfm;
import com.google.android.gms.internal.zzbfp;

public final class SignInConfiguration extends zzbfm implements ReflectedParcelable {
    public static final Creator<SignInConfiguration> CREATOR = new zzx();
    private final String zzeil;
    private GoogleSignInOptions zzeim;

    public SignInConfiguration(String str, GoogleSignInOptions googleSignInOptions) {
        this.zzeil = zzbq.zzgm(str);
        this.zzeim = googleSignInOptions;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
        if (r3.zzeim.equals(r4.zzeim) != false) goto L_0x0023;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.internal.SignInConfiguration r4 = (com.google.android.gms.auth.api.signin.internal.SignInConfiguration) r4     // Catch:{ ClassCastException -> 0x0026 }
            java.lang.String r1 = r3.zzeil     // Catch:{ ClassCastException -> 0x0026 }
            java.lang.String r2 = r4.zzeil     // Catch:{ ClassCastException -> 0x0026 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0026 }
            if (r1 == 0) goto L_0x0025
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r1 = r3.zzeim     // Catch:{ ClassCastException -> 0x0026 }
            if (r1 != 0) goto L_0x0019
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = r4.zzeim     // Catch:{ ClassCastException -> 0x0026 }
            if (r4 != 0) goto L_0x0025
            goto L_0x0023
        L_0x0019:
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r1 = r3.zzeim     // Catch:{ ClassCastException -> 0x0026 }
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = r4.zzeim     // Catch:{ ClassCastException -> 0x0026 }
            boolean r4 = r1.equals(r4)     // Catch:{ ClassCastException -> 0x0026 }
            if (r4 == 0) goto L_0x0025
        L_0x0023:
            r4 = 1
            return r4
        L_0x0025:
            return r0
        L_0x0026:
            r4 = move-exception
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.internal.SignInConfiguration.equals(java.lang.Object):boolean");
    }

    public final int hashCode() {
        return new zzp().zzs(this.zzeil).zzs(this.zzeim).zzabn();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int zze = zzbfp.zze(parcel);
        zzbfp.zza(parcel, 2, this.zzeil, false);
        zzbfp.zza(parcel, 5, (Parcelable) this.zzeim, i, false);
        zzbfp.zzai(parcel, zze);
    }

    public final GoogleSignInOptions zzabr() {
        return this.zzeim;
    }
}
