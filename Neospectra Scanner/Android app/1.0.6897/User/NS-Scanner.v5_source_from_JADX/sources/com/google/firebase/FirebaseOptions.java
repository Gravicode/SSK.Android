package com.google.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzbg;
import com.google.android.gms.common.internal.zzbq;
import com.google.android.gms.common.internal.zzca;
import com.google.android.gms.common.util.zzu;
import java.util.Arrays;

public final class FirebaseOptions {
    /* access modifiers changed from: private */
    public final String zzenh;
    /* access modifiers changed from: private */
    public final String zzmbb;
    /* access modifiers changed from: private */
    public final String zzmbc;
    /* access modifiers changed from: private */
    public final String zzmbd;
    /* access modifiers changed from: private */
    public final String zzmbe;
    /* access modifiers changed from: private */
    public final String zzmbf;
    /* access modifiers changed from: private */
    public final String zzmbg;

    public static final class Builder {
        private String zzenh;
        private String zzmbb;
        private String zzmbc;
        private String zzmbd;
        private String zzmbe;
        private String zzmbf;
        private String zzmbg;

        public Builder() {
        }

        public Builder(FirebaseOptions firebaseOptions) {
            this.zzenh = firebaseOptions.zzenh;
            this.zzmbb = firebaseOptions.zzmbb;
            this.zzmbc = firebaseOptions.zzmbc;
            this.zzmbd = firebaseOptions.zzmbd;
            this.zzmbe = firebaseOptions.zzmbe;
            this.zzmbf = firebaseOptions.zzmbf;
            this.zzmbg = firebaseOptions.zzmbg;
        }

        public final FirebaseOptions build() {
            FirebaseOptions firebaseOptions = new FirebaseOptions(this.zzenh, this.zzmbb, this.zzmbc, this.zzmbd, this.zzmbe, this.zzmbf, this.zzmbg);
            return firebaseOptions;
        }

        public final Builder setApiKey(@NonNull String str) {
            this.zzmbb = zzbq.zzh(str, "ApiKey must be set.");
            return this;
        }

        public final Builder setApplicationId(@NonNull String str) {
            this.zzenh = zzbq.zzh(str, "ApplicationId must be set.");
            return this;
        }

        public final Builder setDatabaseUrl(@Nullable String str) {
            this.zzmbc = str;
            return this;
        }

        public final Builder setGcmSenderId(@Nullable String str) {
            this.zzmbe = str;
            return this;
        }

        public final Builder setProjectId(@Nullable String str) {
            this.zzmbg = str;
            return this;
        }

        public final Builder setStorageBucket(@Nullable String str) {
            this.zzmbf = str;
            return this;
        }
    }

    private FirebaseOptions(@NonNull String str, @NonNull String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, @Nullable String str6, @Nullable String str7) {
        zzbq.zza(!zzu.zzgs(str), (Object) "ApplicationId must be set.");
        this.zzenh = str;
        this.zzmbb = str2;
        this.zzmbc = str3;
        this.zzmbd = str4;
        this.zzmbe = str5;
        this.zzmbf = str6;
        this.zzmbg = str7;
    }

    public static FirebaseOptions fromResource(Context context) {
        zzca zzca = new zzca(context);
        String string = zzca.getString("google_app_id");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        FirebaseOptions firebaseOptions = new FirebaseOptions(string, zzca.getString("google_api_key"), zzca.getString("firebase_database_url"), zzca.getString("ga_trackingId"), zzca.getString("gcm_defaultSenderId"), zzca.getString("google_storage_bucket"), zzca.getString("project_id"));
        return firebaseOptions;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof FirebaseOptions)) {
            return false;
        }
        FirebaseOptions firebaseOptions = (FirebaseOptions) obj;
        return zzbg.equal(this.zzenh, firebaseOptions.zzenh) && zzbg.equal(this.zzmbb, firebaseOptions.zzmbb) && zzbg.equal(this.zzmbc, firebaseOptions.zzmbc) && zzbg.equal(this.zzmbd, firebaseOptions.zzmbd) && zzbg.equal(this.zzmbe, firebaseOptions.zzmbe) && zzbg.equal(this.zzmbf, firebaseOptions.zzmbf) && zzbg.equal(this.zzmbg, firebaseOptions.zzmbg);
    }

    public final String getApiKey() {
        return this.zzmbb;
    }

    public final String getApplicationId() {
        return this.zzenh;
    }

    public final String getDatabaseUrl() {
        return this.zzmbc;
    }

    public final String getGcmSenderId() {
        return this.zzmbe;
    }

    public final String getProjectId() {
        return this.zzmbg;
    }

    public final String getStorageBucket() {
        return this.zzmbf;
    }

    public final int hashCode() {
        return Arrays.hashCode(new Object[]{this.zzenh, this.zzmbb, this.zzmbc, this.zzmbd, this.zzmbe, this.zzmbf, this.zzmbg});
    }

    public final String toString() {
        return zzbg.zzx(this).zzg("applicationId", this.zzenh).zzg("apiKey", this.zzmbb).zzg("databaseUrl", this.zzmbc).zzg("gcmSenderId", this.zzmbe).zzg("storageBucket", this.zzmbf).zzg("projectId", this.zzmbg).toString();
    }
}
