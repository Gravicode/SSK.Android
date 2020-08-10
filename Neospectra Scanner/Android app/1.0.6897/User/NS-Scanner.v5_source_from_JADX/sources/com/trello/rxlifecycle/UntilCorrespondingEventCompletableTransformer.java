package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Completable;
import p008rx.Completable.Transformer;
import p008rx.Observable;
import p008rx.functions.Func1;

final class UntilCorrespondingEventCompletableTransformer<T> implements Transformer {
    final Func1<T, T> correspondingEvents;
    final Observable<T> sharedLifecycle;

    public UntilCorrespondingEventCompletableTransformer(@Nonnull Observable<T> sharedLifecycle2, @Nonnull Func1<T, T> correspondingEvents2) {
        this.sharedLifecycle = sharedLifecycle2;
        this.correspondingEvents = correspondingEvents2;
    }

    public Completable call(Completable source) {
        return Completable.amb(source, TakeUntilGenerator.takeUntilCorrespondingEvent(this.sharedLifecycle, this.correspondingEvents).flatMap(Functions.CANCEL_COMPLETABLE).toCompletable());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UntilCorrespondingEventCompletableTransformer<?> that = (UntilCorrespondingEventCompletableTransformer) o;
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
        sb.append("UntilCorrespondingEventCompletableTransformer{sharedLifecycle=");
        sb.append(this.sharedLifecycle);
        sb.append(", correspondingEvents=");
        sb.append(this.correspondingEvents);
        sb.append('}');
        return sb.toString();
    }
}
