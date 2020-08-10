package com.trello.rxlifecycle2;

import com.trello.rxlifecycle2.internal.Preconditions;
import javax.annotation.ParametersAreNonnullByDefault;
import org.reactivestreams.Publisher;
import p005io.reactivex.BackpressureStrategy;
import p005io.reactivex.Completable;
import p005io.reactivex.CompletableSource;
import p005io.reactivex.CompletableTransformer;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableTransformer;
import p005io.reactivex.Maybe;
import p005io.reactivex.MaybeSource;
import p005io.reactivex.MaybeTransformer;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.ObservableTransformer;
import p005io.reactivex.Single;
import p005io.reactivex.SingleSource;
import p005io.reactivex.SingleTransformer;

@ParametersAreNonnullByDefault
public final class LifecycleTransformer<T> implements ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {
    final Observable<?> observable;

    LifecycleTransformer(Observable<?> observable2) {
        Preconditions.checkNotNull(observable2, "observable == null");
        this.observable = observable2;
    }

    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil((ObservableSource<U>) this.observable);
    }

    public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil((Publisher<U>) this.observable.toFlowable(BackpressureStrategy.LATEST));
    }

    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil((SingleSource<? extends E>) this.observable.firstOrError());
    }

    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil((MaybeSource<U>) this.observable.firstElement());
    }

    public CompletableSource apply(Completable upstream) {
        return Completable.ambArray(upstream, this.observable.flatMapCompletable(Functions.CANCEL_COMPLETABLE));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.observable.equals(((LifecycleTransformer) o).observable);
    }

    public int hashCode() {
        return this.observable.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LifecycleTransformer{observable=");
        sb.append(this.observable);
        sb.append('}');
        return sb.toString();
    }
}
