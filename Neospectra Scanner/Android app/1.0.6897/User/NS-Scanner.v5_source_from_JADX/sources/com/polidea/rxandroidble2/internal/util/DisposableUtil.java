package com.polidea.rxandroidble2.internal.util;

import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.Observer;
import p005io.reactivex.SingleEmitter;
import p005io.reactivex.observers.DisposableObserver;
import p005io.reactivex.observers.DisposableSingleObserver;

public class DisposableUtil {
    private DisposableUtil() {
    }

    public static <T> DisposableSingleObserver<T> disposableSingleObserverFromEmitter(final SingleEmitter<T> emitter) {
        return new DisposableSingleObserver<T>() {
            public void onSuccess(T t) {
                emitter.onSuccess(t);
            }

            public void onError(Throwable e) {
                emitter.tryOnError(e);
            }
        };
    }

    public static <T> DisposableObserver<T> disposableObserverFromEmitter(final ObservableEmitter<T> emitter) {
        return new DisposableObserver<T>() {
            public void onNext(T t) {
                emitter.onNext(t);
            }

            public void onError(Throwable e) {
                emitter.tryOnError(e);
            }

            public void onComplete() {
                emitter.onComplete();
            }
        };
    }

    public static <T> DisposableSingleObserver<T> disposableSingleObserverFromEmitter(final ObservableEmitter<T> emitter) {
        return new DisposableSingleObserver<T>() {
            public void onSuccess(T t) {
                emitter.onNext(t);
                emitter.onComplete();
            }

            public void onError(Throwable e) {
                emitter.tryOnError(e);
            }
        };
    }

    public static <T> DisposableObserver<T> disposableObserver(final Observer<T> emitter) {
        return new DisposableObserver<T>() {
            public void onNext(T t) {
                emitter.onNext(t);
            }

            public void onError(Throwable e) {
                emitter.onError(e);
            }

            public void onComplete() {
                emitter.onComplete();
            }
        };
    }
}
