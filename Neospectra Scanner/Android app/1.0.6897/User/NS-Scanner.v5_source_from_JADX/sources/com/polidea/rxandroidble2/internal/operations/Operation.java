package com.polidea.rxandroidble2.internal.operations;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import com.polidea.rxandroidble2.internal.Priority;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import p005io.reactivex.Observable;

@RestrictTo({Scope.LIBRARY_GROUP})
public interface Operation<T> extends Comparable<Operation<?>> {
    Priority definedPriority();

    Observable<T> run(QueueReleaseInterface queueReleaseInterface);
}
