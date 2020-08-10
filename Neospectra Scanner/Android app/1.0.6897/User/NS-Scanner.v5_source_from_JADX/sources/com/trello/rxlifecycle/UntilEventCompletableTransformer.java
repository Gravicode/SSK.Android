package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Completable;
import p008rx.Completable.Transformer;
import p008rx.Observable;

final class UntilEventCompletableTransformer<T> implements Transformer {
    final T event;
    final Observable<T> lifecycle;

    public UntilEventCompletableTransformer(@Nonnull Observable<T> lifecycle2, @Nonnull T event2) {
        this.lifecycle = lifecycle2;
        this.event = event2;
    }

    public Completable call(Completable source) {
        return Completable.amb(source, TakeUntilGenerator.takeUntilEvent(this.lifecycle, this.event).flatMap(Functions.CANCEL_COMPLETABLE).toCompletable());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UntilEventCompletableTransformer<?> that = (UntilEventCompletableTransformer) o;
        if (!this.lifecycle.equals(that.lifecycle)) {
            return false;
        }
        return this.event.equals(that.event);
    }

    public int hashCode() {
        return (this.lifecycle.hashCode() * 31) + this.event.hashCode();
    }
}
