package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Completable;
import p008rx.Observable;
import p008rx.Single.Transformer;

final class UntilEventObservableTransformer<T, R> implements LifecycleTransformer<T> {
    final R event;
    final Observable<R> lifecycle;

    public UntilEventObservableTransformer(@Nonnull Observable<R> lifecycle2, @Nonnull R event2) {
        this.lifecycle = lifecycle2;
        this.event = event2;
    }

    public Observable<T> call(Observable<T> source) {
        return source.takeUntil(TakeUntilGenerator.takeUntilEvent(this.lifecycle, this.event));
    }

    @Nonnull
    public Transformer<T, T> forSingle() {
        return new UntilEventSingleTransformer(this.lifecycle, this.event);
    }

    @Nonnull
    public Completable.Transformer forCompletable() {
        return new UntilEventCompletableTransformer(this.lifecycle, this.event);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UntilEventObservableTransformer<?, ?> that = (UntilEventObservableTransformer) o;
        if (!this.lifecycle.equals(that.lifecycle)) {
            return false;
        }
        return this.event.equals(that.event);
    }

    public int hashCode() {
        return (this.lifecycle.hashCode() * 31) + this.event.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UntilEventObservableTransformer{lifecycle=");
        sb.append(this.lifecycle);
        sb.append(", event=");
        sb.append(this.event);
        sb.append('}');
        return sb.toString();
    }
}
