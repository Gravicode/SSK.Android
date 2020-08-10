package com.polidea.rxandroidble2.internal.util;

import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import java.util.concurrent.atomic.AtomicBoolean;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.Observer;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.functions.Cancellable;

public class QueueReleasingEmitterWrapper<T> implements Observer<T>, Cancellable {
    private final ObservableEmitter<T> emitter;
    private final AtomicBoolean isEmitterCanceled = new AtomicBoolean(false);
    private final QueueReleaseInterface queueReleaseInterface;

    public QueueReleasingEmitterWrapper(ObservableEmitter<T> emitter2, QueueReleaseInterface queueReleaseInterface2) {
        this.emitter = emitter2;
        this.queueReleaseInterface = queueReleaseInterface2;
        emitter2.setCancellable(this);
    }

    public void onComplete() {
        this.queueReleaseInterface.release();
        this.emitter.onComplete();
    }

    public void onError(Throwable e) {
        this.queueReleaseInterface.release();
        this.emitter.tryOnError(e);
    }

    public void onSubscribe(Disposable d) {
    }

    public void onNext(T t) {
        this.emitter.onNext(t);
    }

    public synchronized void cancel() throws Exception {
        this.isEmitterCanceled.set(true);
    }

    public synchronized boolean isWrappedEmitterUnsubscribed() {
        return this.isEmitterCanceled.get();
    }
}
