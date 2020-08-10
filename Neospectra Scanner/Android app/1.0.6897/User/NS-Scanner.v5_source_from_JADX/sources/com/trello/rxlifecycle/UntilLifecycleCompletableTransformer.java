package com.trello.rxlifecycle;

import javax.annotation.Nonnull;
import p008rx.Completable;
import p008rx.Completable.Transformer;
import p008rx.Observable;

final class UntilLifecycleCompletableTransformer<T> implements Transformer {
    final Observable<T> lifecycle;

    public UntilLifecycleCompletableTransformer(@Nonnull Observable<T> lifecycle2) {
        this.lifecycle = lifecycle2;
    }

    public Completable call(Completable source) {
        return Completable.amb(source, this.lifecycle.flatMap(Functions.CANCEL_COMPLETABLE).toCompletable());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return this.lifecycle.equals(((UntilLifecycleCompletableTransformer) o).lifecycle);
    }

    public int hashCode() {
        return this.lifecycle.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UntilLifecycleCompletableTransformer{lifecycle=");
        sb.append(this.lifecycle);
        sb.append('}');
        return sb.toString();
    }
}
