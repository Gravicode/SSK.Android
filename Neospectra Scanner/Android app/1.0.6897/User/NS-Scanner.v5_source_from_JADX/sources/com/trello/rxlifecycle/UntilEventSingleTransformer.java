package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Observable;
import p008rx.Single;
import p008rx.Single.Transformer;

final class UntilEventSingleTransformer<T, R> implements Transformer<T, T> {
    final R event;
    final Observable<R> lifecycle;

    public UntilEventSingleTransformer(@Nonnull Observable<R> lifecycle2, @Nonnull R event2) {
        this.lifecycle = lifecycle2;
        this.event = event2;
    }

    public Single<T> call(Single<T> source) {
        return source.takeUntil(TakeUntilGenerator.takeUntilEvent(this.lifecycle, this.event));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UntilEventSingleTransformer<?, ?> that = (UntilEventSingleTransformer) o;
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
        sb.append("UntilEventSingleTransformer{lifecycle=");
        sb.append(this.lifecycle);
        sb.append(", event=");
        sb.append(this.event);
        sb.append('}');
        return sb.toString();
    }
}
