package com.polidea.rxandroidble2.internal.connection;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface PayloadSizeLimitProvider {
    int getPayloadSizeLimit();
}
