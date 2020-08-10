package com.google.firebase.auth.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import java.util.Map;

public final class zzd implements AdditionalUserInfo {
    private final String zzmcu;
    private boolean zzmgk;
    private final Map<String, Object> zzmhe;

    public zzd(@NonNull String str, @NonNull String str2, boolean z) {
        zzbq.zzgm(str);
        zzbq.zzgm(str2);
        this.zzmcu = str;
        this.zzmhe = zzq.zzpd(str2);
        this.zzmgk = z;
    }

    public zzd(boolean z) {
        this.zzmgk = z;
        this.zzmcu = null;
        this.zzmhe = null;
    }

    public final Map<String, Object> getProfile() {
        return this.zzmhe;
    }

    public final String getProviderId() {
        return this.zzmcu;
    }

    public final String getUsername() {
        Map<String, Object> map;
        String str;
        if (GithubAuthProvider.PROVIDER_ID.equals(this.zzmcu)) {
            map = this.zzmhe;
            str = Event.LOGIN;
        } else if (!TwitterAuthProvider.PROVIDER_ID.equals(this.zzmcu)) {
            return null;
        } else {
            map = this.zzmhe;
            str = "screen_name";
        }
        return (String) map.get(str);
    }

    public final boolean isNewUser() {
        return this.zzmgk;
    }
}
