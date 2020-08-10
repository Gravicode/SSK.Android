package p005io.reactivex;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import p005io.reactivex.annotations.BackpressureKind;
import p005io.reactivex.annotations.BackpressureSupport;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.SchedulerSupport;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.BiPredicate;
import p005io.reactivex.functions.BooleanSupplier;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.Function3;
import p005io.reactivex.functions.Function4;
import p005io.reactivex.functions.Function5;
import p005io.reactivex.functions.Function6;
import p005io.reactivex.functions.Function7;
import p005io.reactivex.functions.Function8;
import p005io.reactivex.functions.Function9;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.FuseToFlowable;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.internal.observers.BlockingMultiObserver;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeAmb;
import p005io.reactivex.internal.operators.maybe.MaybeCache;
import p005io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import p005io.reactivex.internal.operators.maybe.MaybeConcatArray;
import p005io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError;
import p005io.reactivex.internal.operators.maybe.MaybeConcatIterable;
import p005io.reactivex.internal.operators.maybe.MaybeContains;
import p005io.reactivex.internal.operators.maybe.MaybeCount;
import p005io.reactivex.internal.operators.maybe.MaybeCreate;
import p005io.reactivex.internal.operators.maybe.MaybeDefer;
import p005io.reactivex.internal.operators.maybe.MaybeDelay;
import p005io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeDelaySubscriptionOtherPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeDetach;
import p005io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess;
import p005io.reactivex.internal.operators.maybe.MaybeDoFinally;
import p005io.reactivex.internal.operators.maybe.MaybeDoOnEvent;
import p005io.reactivex.internal.operators.maybe.MaybeEmpty;
import p005io.reactivex.internal.operators.maybe.MaybeEqualSingle;
import p005io.reactivex.internal.operators.maybe.MaybeError;
import p005io.reactivex.internal.operators.maybe.MaybeErrorCallable;
import p005io.reactivex.internal.operators.maybe.MaybeFilter;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapCompletable;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapIterableFlowable;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapNotification;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapSingle;
import p005io.reactivex.internal.operators.maybe.MaybeFlatMapSingleElement;
import p005io.reactivex.internal.operators.maybe.MaybeFlatten;
import p005io.reactivex.internal.operators.maybe.MaybeFromAction;
import p005io.reactivex.internal.operators.maybe.MaybeFromCallable;
import p005io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import p005io.reactivex.internal.operators.maybe.MaybeFromFuture;
import p005io.reactivex.internal.operators.maybe.MaybeFromRunnable;
import p005io.reactivex.internal.operators.maybe.MaybeFromSingle;
import p005io.reactivex.internal.operators.maybe.MaybeHide;
import p005io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
import p005io.reactivex.internal.operators.maybe.MaybeIsEmptySingle;
import p005io.reactivex.internal.operators.maybe.MaybeJust;
import p005io.reactivex.internal.operators.maybe.MaybeLift;
import p005io.reactivex.internal.operators.maybe.MaybeMap;
import p005io.reactivex.internal.operators.maybe.MaybeMergeArray;
import p005io.reactivex.internal.operators.maybe.MaybeNever;
import p005io.reactivex.internal.operators.maybe.MaybeObserveOn;
import p005io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;
import p005io.reactivex.internal.operators.maybe.MaybeOnErrorNext;
import p005io.reactivex.internal.operators.maybe.MaybeOnErrorReturn;
import p005io.reactivex.internal.operators.maybe.MaybePeek;
import p005io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
import p005io.reactivex.internal.operators.maybe.MaybeSwitchIfEmpty;
import p005io.reactivex.internal.operators.maybe.MaybeSwitchIfEmptySingle;
import p005io.reactivex.internal.operators.maybe.MaybeTakeUntilMaybe;
import p005io.reactivex.internal.operators.maybe.MaybeTakeUntilPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeTimeoutMaybe;
import p005io.reactivex.internal.operators.maybe.MaybeTimeoutPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeTimer;
import p005io.reactivex.internal.operators.maybe.MaybeToFlowable;
import p005io.reactivex.internal.operators.maybe.MaybeToObservable;
import p005io.reactivex.internal.operators.maybe.MaybeToPublisher;
import p005io.reactivex.internal.operators.maybe.MaybeToSingle;
import p005io.reactivex.internal.operators.maybe.MaybeUnsafeCreate;
import p005io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn;
import p005io.reactivex.internal.operators.maybe.MaybeUsing;
import p005io.reactivex.internal.operators.maybe.MaybeZipArray;
import p005io.reactivex.internal.operators.maybe.MaybeZipIterable;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.observers.TestObserver;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.Maybe */
public abstract class Maybe<T> implements MaybeSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(MaybeObserver<? super T> maybeObserver);

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> amb(Iterable<? extends MaybeSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeAmb<T>(null, sources));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> ambArray(MaybeSource<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeAmb<T>(sources, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends MaybeSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatIterable<T>(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concatArray(source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concatArray(source1, source2, source3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concatArray(source1, source2, source3, source4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources) {
        return concat(sources, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapPublisher<T>(sources, MaybeToPublisher.instance(), prefetch, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(sources[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatArray<T>(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(MaybeSource<? extends T>... sources) {
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(sources[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatArrayDelayError<T>(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(MaybeSource<? extends T>... sources) {
        return Flowable.fromArray(sources).concatMapEager(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Iterable<? extends MaybeSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return Flowable.fromIterable(sources).concatMapDelayError(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends MaybeSource<? extends T>> sources) {
        return Flowable.fromPublisher(sources).concatMapDelayError(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends MaybeSource<? extends T>> sources) {
        return Flowable.fromIterable(sources).concatMapEager(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends MaybeSource<? extends T>> sources) {
        return Flowable.fromPublisher(sources).concatMapEager(MaybeToPublisher.instance());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> create(MaybeOnSubscribe<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeCreate<T>(onSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> defer(Callable<? extends MaybeSource<? extends T>> maybeSupplier) {
        ObjectHelper.requireNonNull(maybeSupplier, "maybeSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDefer<T>(maybeSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> empty() {
        return RxJavaPlugins.onAssembly((Maybe<T>) MaybeEmpty.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Throwable exception) {
        ObjectHelper.requireNonNull(exception, "exception is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeError<T>(exception));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Callable<? extends Throwable> supplier) {
        ObjectHelper.requireNonNull(supplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeErrorCallable<T>(supplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromAction(Action run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromAction<T>(run));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCompletable(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "completableSource is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromCompletable<T>(completableSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromSingle(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "singleSource is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromSingle<T>(singleSource));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCallable(@NonNull Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromCallable<T>(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromFuture<T>(future, 0, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromFuture<T>(future, timeout, unit));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> fromRunnable(Runnable run) {
        ObjectHelper.requireNonNull(run, "run is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromRunnable<T>(run));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> just(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeJust<T>(item));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends MaybeSource<? extends T>> sources) {
        return merge((Publisher<? extends MaybeSource<? extends T>>) Flowable.fromIterable(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources) {
        return merge(sources, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> sources, int maxConcurrency) {
        ObjectHelper.requireNonNull(sources, "source is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(sources, MaybeToPublisher.instance(), false, maxConcurrency, 1);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> merge(MaybeSource<? extends MaybeSource<? extends T>> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(source, Functions.identity()));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return mergeArray(source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return mergeArray(source1, source2, source3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return mergeArray(source1, source2, source3, source4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return Flowable.empty();
        }
        if (sources.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(sources[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeMergeArray<T>(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(MaybeSource<? extends T>... sources) {
        if (sources.length == 0) {
            return Flowable.empty();
        }
        return Flowable.fromArray(sources).flatMap(MaybeToPublisher.instance(), true, sources.length);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends MaybeSource<? extends T>> sources) {
        return Flowable.fromIterable(sources).flatMap(MaybeToPublisher.instance(), true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends MaybeSource<? extends T>> sources) {
        return mergeDelayError(sources, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends MaybeSource<? extends T>> sources, int maxConcurrency) {
        ObjectHelper.requireNonNull(sources, "source is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(sources, MaybeToPublisher.instance(), true, maxConcurrency, 1);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return mergeArrayDelayError(source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return mergeArrayDelayError(source1, source2, source3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, MaybeSource<? extends T> source3, MaybeSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return mergeArrayDelayError(source1, source2, source3, source4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> never() {
        return RxJavaPlugins.onAssembly((Maybe<T>) MaybeNever.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> source1, MaybeSource<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(isEqual, "isEqual is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeEqualSingle<T>(source1, source2, isEqual));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Maybe<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Maybe<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimer<T>(Math.max(0, delay), unit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> unsafeCreate(MaybeSource<T> onSubscribe) {
        if (onSubscribe instanceof Maybe) {
            throw new IllegalArgumentException("unsafeCreate(Maybe) should be upgraded");
        }
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsafeCreate<T>(onSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
        return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends MaybeSource<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
        ObjectHelper.requireNonNull(resourceDisposer, "disposer is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUsing<T>(resourceSupplier, sourceSupplier, resourceDisposer, eager));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Maybe<T> wrap(MaybeSource<T> source) {
        if (source instanceof Maybe) {
            return RxJavaPlugins.onAssembly((Maybe) source);
        }
        ObjectHelper.requireNonNull(source, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsafeCreate<T>(source));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, R> Maybe<R> zip(Iterable<? extends MaybeSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeZipIterable<T>(sources, zipper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Maybe<R> zip(MaybeSource<? extends T1> source1, MaybeSource<? extends T2> source2, MaybeSource<? extends T3> source3, MaybeSource<? extends T4> source4, MaybeSource<? extends T5> source5, MaybeSource<? extends T6> source6, MaybeSource<? extends T7> source7, MaybeSource<? extends T8> source8, MaybeSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, R> Maybe<R> zipArray(Function<? super Object[], ? extends R> zipper, MaybeSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeZipArray<T>(sources, zipper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> ambWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    /* renamed from: as */
    public final <R> R mo14453as(@NonNull MaybeConverter<T, ? extends R> converter) {
        return ((MaybeConverter) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe((MaybeObserver<? super T>) observer);
        return observer.blockingGet();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final T blockingGet(T defaultValue) {
        ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe((MaybeObserver<? super T>) observer);
        return observer.blockingGet(defaultValue);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> cache() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeCache<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Maybe<U> cast(Class<? extends U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> compose(MaybeTransformer<? super T, ? extends R> transformer) {
        return wrap(((MaybeTransformer) ObjectHelper.requireNonNull(transformer, "transformer is null")).apply(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> concatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(this, mapper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concat((MaybeSource<? extends T>) this, other);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeContains<T>(this, item));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeCount<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> defaultIfEmpty(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "item is null");
        return switchIfEmpty((MaybeSource<? extends T>) just(defaultItem));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        MaybeDelay maybeDelay = new MaybeDelay(this, Math.max(0, delay), unit, scheduler);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybeDelay);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U, V> Maybe<T> delay(Publisher<U> delayIndicator) {
        ObjectHelper.requireNonNull(delayIndicator, "delayIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDelayOtherPublisher<T>(this, delayIndicator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> delaySubscription(Publisher<U> subscriptionIndicator) {
        ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDelaySubscriptionOtherPublisher<T>(this, subscriptionIndicator));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(Flowable.timer(delay, unit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doAfterSuccess(Consumer<? super T> onAfterSuccess) {
        ObjectHelper.requireNonNull(onAfterSuccess, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoAfterSuccess<T>(this, onAfterSuccess));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doAfterTerminate(Action onAfterTerminate) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null"), Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doFinally(Action onFinally) {
        ObjectHelper.requireNonNull(onFinally, "onFinally is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoFinally<T>(this, onFinally));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnDispose(Action onDispose) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(onDispose, "onDispose is null"));
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnComplete(Action onComplete) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), (Action) ObjectHelper.requireNonNull(onComplete, "onComplete is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnError(Consumer<? super Throwable> onError) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(onError, "onError is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoOnEvent<T>(this, onEvent));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        MaybePeek maybePeek = new MaybePeek(this, (Consumer) ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null"), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> doOnSuccess(Consumer<? super T> onSuccess) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(onSuccess, "onSubscribe is null"), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFilter<T>(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> onSuccessMapper, Function<? super Throwable, ? extends MaybeSource<? extends R>> onErrorMapper, Callable<? extends MaybeSource<? extends R>> onCompleteSupplier) {
        ObjectHelper.requireNonNull(onSuccessMapper, "onSuccessMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatMapNotification<T>(this, onSuccessMapper, onErrorMapper, onCompleteSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U, R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> resultSelector) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatMapBiSelector<T>(this, mapper, resultSelector));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeFlatMapIterableFlowable<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new MaybeFlatMapIterableObservable<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return toObservable().flatMap(mapper);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return toFlowable().flatMap(mapper);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Single<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeFlatMapSingle<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMapSingleElement(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatMapSingleElement<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new MaybeFlatMapCompletable(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> hide() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeHide<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElement() {
        return RxJavaPlugins.onAssembly((Completable) new MaybeIgnoreElementCompletable(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeIsEmptySingle<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> lift(MaybeOperator<? extends R, ? super T> lift) {
        ObjectHelper.requireNonNull(lift, "onLift is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeLift<T>(this, lift));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeMap<T>(this, mapper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return merge((MaybeSource<? extends T>) this, other);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeObserveOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Maybe<U> ofType(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return filter(Functions.isInstanceOf(clazz)).cast(clazz);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo14539to(Function<? super Maybe<T>, R> convert) {
        try {
            return ((Function) ObjectHelper.requireNonNull(convert, "convert is null")).apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new MaybeToObservable<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> toSingle(T defaultValue) {
        ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeToSingle<T>(this, defaultValue));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> toSingle() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeToSingle<T>(this, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete(Predicate<? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorComplete<T>(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(MaybeSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return onErrorResumeNext(Functions.justFunction(next));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(Function<? super Throwable, ? extends MaybeSource<? extends T>> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorNext<T>(this, resumeFunction, true));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
        ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorReturn<T>(this, valueSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturnItem(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return onErrorReturn(Functions.justFunction(item));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onExceptionResumeNext(MaybeSource<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorNext<T>(this, Functions.justFunction(next), false));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDetach<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return repeat(Long.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long times) {
        return toFlowable().repeat(times);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        return toFlowable().repeatUntil(stop);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
        return toFlowable().repeatWhen(handler);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retry() {
        return retry(Long.MAX_VALUE, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return toFlowable().retry(predicate).singleElement();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retry(long count) {
        return retry(count, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retry(long times, Predicate<? super Throwable> predicate) {
        return toFlowable().retry(times, predicate).singleElement();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retry(Predicate<? super Throwable> predicate) {
        return retry(Long.MAX_VALUE, predicate);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retryUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return retry(Long.MAX_VALUE, Functions.predicateReverseFor(stop));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        return toFlowable().retryWhen(handler).singleElement();
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        return subscribe(onSuccess, onError, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        return (Disposable) subscribeWith(new MaybeCallbackObserver(onSuccess, onError, onComplete));
    }

    @SchedulerSupport("none")
    public final void subscribe(MaybeObserver<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        MaybeObserver<? super T> observer2 = RxJavaPlugins.onSubscribe(this, observer);
        ObjectHelper.requireNonNull(observer2, "observer returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(observer2);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            NullPointerException npe = new NullPointerException("subscribeActual failed");
            npe.initCause(ex2);
            throw npe;
        }
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeSubscribeOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <E extends MaybeObserver<? super T>> E subscribeWith(E observer) {
        subscribe((MaybeObserver<? super T>) observer);
        return observer;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> switchIfEmpty(MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeSwitchIfEmpty<T>(this, other));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single<T> switchIfEmpty(SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeSwitchIfEmptySingle<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(MaybeSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTakeUntilMaybe<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTakeUntilPublisher<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(fallback, "other is null");
        return timeout(timeout, timeUnit, Schedulers.computation(), fallback);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return timeout((MaybeSource<U>) timer(timeout, timeUnit, scheduler), fallback);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout((MaybeSource<U>) timer(timeout, timeUnit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutMaybe<T>(this, timeoutIndicator, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutMaybe<T>(this, timeoutIndicator, fallback));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutPublisher<T>(this, timeoutIndicator, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> timeoutIndicator, MaybeSource<? extends T> fallback) {
        ObjectHelper.requireNonNull(timeoutIndicator, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(fallback, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutPublisher<T>(this, timeoutIndicator, fallback));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Maybe<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsubscribeOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U, R> Maybe<R> zipWith(MaybeSource<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        return zip(this, other, zipper);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> to = new TestObserver<>();
        subscribe((MaybeObserver<? super T>) to);
        return to;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean cancelled) {
        TestObserver<T> to = new TestObserver<>();
        if (cancelled) {
            to.cancel();
        }
        subscribe((MaybeObserver<? super T>) to);
        return to;
    }
}
