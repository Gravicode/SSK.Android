package com.google.android.gms.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

final class zzaup implements Result {
    private final Status mStatus;

    public zzaup(Status status) {
        this.mStatus = status;
    }

    public final Status getStatus() {
        return this.mStatus;
    }
}
