package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Observable;
import p008rx.Single;
import p008rx.Single.Transformer;
import p008rx.functions.Func1;

final class UntilCorrespondingEventSingleTransformer<T, R> implements Transformer<T, T> {
    final Func1<R, R> correspondingEvents;
    final Observable<R> sharedLifecycle;

    public UntilCorrespondingEventSingleTransformer(@Nonnull Observable<R> sharedLifecycle2, @Nonnull Func1<R, R> correspondingEvents2) {
        this.sharedLifecycle = sharedLifecycle2;
        this.correspondingEvents = correspondingEvents2;
    }

    public Single<T> call(Single<T> source) {
        return source.takeUntil(TakeUntilGenerator.takeUntilCorrespondingEvent(this.sharedLifecycle, this.correspondingEvents));
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UntilCorrespondingEventSingleTransformer<?, ?> that = (UntilCorrespondingEventSingleTransformer) o;
        if (!this.sharedLifecycle.equals(that.sharedLifecycle)) {
            return false;
        }
        return this.correspondingEvents.equals(that.correspondingEvents);
    }

    public int hashCode() {
        return (this.sharedLifecycle.hashCode() * 31) + this.correspondingEvents.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UntilCorrespondingEventSingleTransformer{sharedLifecycle=");
        sb.append(this.sharedLifecycle);
        sb.append(", correspondingEvents=");
        sb.append(this.correspondingEvents);
        sb.append('}');
        return sb.toString();
    }
}
