package com.trello.rxlifecycle;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import p008rx.Completable;
import p008rx.Observable.Transformer;
import p008rx.Single;

public interface LifecycleTransformer<T> extends Transformer<T, T> {
    @CheckReturnValue
    @Nonnull
    Completable.Transformer forCompletable();

    @CheckReturnValue
    @Nonnull
    <U> Single.Transformer<U, U> forSingle();
}
