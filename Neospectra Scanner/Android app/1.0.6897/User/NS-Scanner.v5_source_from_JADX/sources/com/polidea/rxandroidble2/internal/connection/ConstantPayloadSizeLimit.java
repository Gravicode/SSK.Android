package com.polidea.rxandroidble2.internal.connection;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
class ConstantPayloadSizeLimit implements PayloadSizeLimitProvider {
    private final int limit;

    ConstantPayloadSizeLimit(int limit2) {
        this.limit = limit2;
    }

    public int getPayloadSizeLimit() {
        return this.limit;
    }
}
