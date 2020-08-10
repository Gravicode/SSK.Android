package com.trello.rxlifecycle2;

import com.trello.rxlifecycle2.internal.Preconditions;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Predicate;

public class RxLifecycle {
    private RxLifecycle() {
        throw new AssertionError("No instances");
    }

    @CheckReturnValue
    @Nonnull
    public static <T, R> LifecycleTransformer<T> bindUntilEvent(@Nonnull Observable<R> lifecycle, @Nonnull R event) {
        Preconditions.checkNotNull(lifecycle, "lifecycle == null");
        Preconditions.checkNotNull(event, "event == null");
        return bind(takeUntilEvent(lifecycle, event));
    }

    private static <R> Observable<R> takeUntilEvent(Observable<R> lifecycle, final R event) {
        return lifecycle.filter(new Predicate<R>() {
            public boolean test(R lifecycleEvent) throws Exception {
                return lifecycleEvent.equals(event);
            }
        });
    }

    @CheckReturnValue
    @Nonnull
    public static <T, R> LifecycleTransformer<T> bind(@Nonnull Observable<R> lifecycle) {
        return new LifecycleTransformer<>(lifecycle);
    }

    @CheckReturnValue
    @Nonnull
    public static <T, R> LifecycleTransformer<T> bind(@Nonnull Observable<R> lifecycle, @Nonnull Function<R, R> correspondingEvents) {
        Preconditions.checkNotNull(lifecycle, "lifecycle == null");
        Preconditions.checkNotNull(correspondingEvents, "correspondingEvents == null");
        return bind(takeUntilCorrespondingEvent(lifecycle.share(), correspondingEvents));
    }

    private static <R> Observable<Boolean> takeUntilCorrespondingEvent(Observable<R> lifecycle, Function<R, R> correspondingEvents) {
        return Observable.combineLatest((ObservableSource<? extends T1>) lifecycle.take(1).map(correspondingEvents), (ObservableSource<? extends T2>) lifecycle.skip(1), (BiFunction<? super T1, ? super T2, ? extends R>) new BiFunction<R, R, Boolean>() {
            public Boolean apply(R bindUntilEvent, R lifecycleEvent) throws Exception {
                return Boolean.valueOf(lifecycleEvent.equals(bindUntilEvent));
            }
        }).onErrorReturn(Functions.RESUME_FUNCTION).filter(Functions.SHOULD_COMPLETE);
    }
}
