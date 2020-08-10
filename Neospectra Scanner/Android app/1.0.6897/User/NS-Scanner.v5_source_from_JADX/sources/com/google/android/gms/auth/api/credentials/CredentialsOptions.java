package com.google.android.gms.auth.api.credentials;

import com.google.android.gms.auth.api.Auth.AuthCredentialsOptions;

public final class CredentialsOptions extends AuthCredentialsOptions {
    public static final CredentialsOptions DEFAULT = ((CredentialsOptions) new Builder().zzaat());

    public static final class Builder extends com.google.android.gms.auth.api.Auth.AuthCredentialsOptions.Builder {
        /* renamed from: build */
        public final CredentialsOptions zzaat() {
            return new CredentialsOptions(this);
        }

        public final Builder forceEnableSaveDialog() {
            this.zzedh = Boolean.valueOf(true);
            return this;
        }
    }

    private CredentialsOptions(Builder builder) {
        super(builder);
    }
}
