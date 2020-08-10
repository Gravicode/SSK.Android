package com.polidea.rxandroidble2.internal;

import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.operations.Operation;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableOnSubscribe;

public abstract class QueueOperation<T> implements Operation<T> {
    /* access modifiers changed from: protected */
    public abstract void protectedRun(ObservableEmitter<T> observableEmitter, QueueReleaseInterface queueReleaseInterface) throws Throwable;

    /* access modifiers changed from: protected */
    public abstract BleException provideException(DeadObjectException deadObjectException);

    public final Observable<T> run(final QueueReleaseInterface queueReleaseInterface) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    QueueOperation.this.protectedRun(emitter, queueReleaseInterface);
                } catch (DeadObjectException deadObjectException) {
                    emitter.tryOnError(QueueOperation.this.provideException(deadObjectException));
                } catch (Throwable throwable) {
                    emitter.tryOnError(throwable);
                }
            }
        });
    }

    public Priority definedPriority() {
        return Priority.NORMAL;
    }

    public int compareTo(@NonNull Operation another) {
        return another.definedPriority().priority - definedPriority().priority;
    }
}
