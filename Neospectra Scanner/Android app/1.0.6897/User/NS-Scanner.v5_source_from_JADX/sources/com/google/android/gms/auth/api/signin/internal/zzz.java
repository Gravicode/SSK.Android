package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.zzbq;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

public final class zzz {
    private static final Lock zzeiu = new ReentrantLock();
    private static zzz zzeiv;
    private final Lock zzeiw = new ReentrantLock();
    private final SharedPreferences zzeix;

    private zzz(Context context) {
        this.zzeix = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static zzz zzbt(Context context) {
        zzbq.checkNotNull(context);
        zzeiu.lock();
        try {
            if (zzeiv == null) {
                zzeiv = new zzz(context.getApplicationContext());
            }
            return zzeiv;
        } finally {
            zzeiu.unlock();
        }
    }

    private final GoogleSignInAccount zzex(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String zzez = zzez(zzp("googleSignInAccount", str));
        if (zzez != null) {
            try {
                return GoogleSignInAccount.zzeu(zzez);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    private final GoogleSignInOptions zzey(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String zzez = zzez(zzp("googleSignInOptions", str));
        if (zzez != null) {
            try {
                return GoogleSignInOptions.zzev(zzez);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    private final String zzez(String str) {
        this.zzeiw.lock();
        try {
            return this.zzeix.getString(str, null);
        } finally {
            this.zzeiw.unlock();
        }
    }

    private final void zzfa(String str) {
        this.zzeiw.lock();
        try {
            this.zzeix.edit().remove(str).apply();
        } finally {
            this.zzeiw.unlock();
        }
    }

    private static String zzp(String str, String str2) {
        String str3 = ":";
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + String.valueOf(str3).length() + String.valueOf(str2).length());
        sb.append(str);
        sb.append(str3);
        sb.append(str2);
        return sb.toString();
    }

    public final void clear() {
        this.zzeiw.lock();
        try {
            this.zzeix.edit().clear().apply();
        } finally {
            this.zzeiw.unlock();
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(GoogleSignInAccount googleSignInAccount, GoogleSignInOptions googleSignInOptions) {
        zzbq.checkNotNull(googleSignInAccount);
        zzbq.checkNotNull(googleSignInOptions);
        String zzaba = googleSignInAccount.zzaba();
        zzo(zzp("googleSignInAccount", zzaba), googleSignInAccount.zzabc());
        zzo(zzp("googleSignInOptions", zzaba), googleSignInOptions.zzabg());
    }

    public final GoogleSignInAccount zzabt() {
        return zzex(zzez("defaultGoogleSignInAccount"));
    }

    public final GoogleSignInOptions zzabu() {
        return zzey(zzez("defaultGoogleSignInAccount"));
    }

    public final void zzabv() {
        String zzez = zzez("defaultGoogleSignInAccount");
        zzfa("defaultGoogleSignInAccount");
        if (!TextUtils.isEmpty(zzez)) {
            zzfa(zzp("googleSignInAccount", zzez));
            zzfa(zzp("googleSignInOptions", zzez));
        }
    }

    /* access modifiers changed from: protected */
    public final void zzo(String str, String str2) {
        this.zzeiw.lock();
        try {
            this.zzeix.edit().putString(str, str2).apply();
        } finally {
            this.zzeiw.unlock();
        }
    }
}
