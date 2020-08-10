package p005io.reactivex;

import java.util.NoSuchElementException;
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
import p005io.reactivex.internal.fuseable.FuseToMaybe;
import p005io.reactivex.internal.fuseable.FuseToObservable;
import p005io.reactivex.internal.observers.BiConsumerSingleObserver;
import p005io.reactivex.internal.observers.BlockingMultiObserver;
import p005io.reactivex.internal.observers.ConsumerSingleObserver;
import p005io.reactivex.internal.observers.FutureSingleObserver;
import p005io.reactivex.internal.operators.completable.CompletableFromSingle;
import p005io.reactivex.internal.operators.completable.CompletableToFlowable;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMapPublisher;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMapPublisher;
import p005io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import p005io.reactivex.internal.operators.maybe.MaybeFilterSingle;
import p005io.reactivex.internal.operators.maybe.MaybeFromSingle;
import p005io.reactivex.internal.operators.observable.ObservableConcatMap;
import p005io.reactivex.internal.operators.observable.ObservableSingleSingle;
import p005io.reactivex.internal.operators.single.SingleAmb;
import p005io.reactivex.internal.operators.single.SingleCache;
import p005io.reactivex.internal.operators.single.SingleContains;
import p005io.reactivex.internal.operators.single.SingleCreate;
import p005io.reactivex.internal.operators.single.SingleDefer;
import p005io.reactivex.internal.operators.single.SingleDelay;
import p005io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import p005io.reactivex.internal.operators.single.SingleDelayWithObservable;
import p005io.reactivex.internal.operators.single.SingleDelayWithPublisher;
import p005io.reactivex.internal.operators.single.SingleDelayWithSingle;
import p005io.reactivex.internal.operators.single.SingleDetach;
import p005io.reactivex.internal.operators.single.SingleDoAfterSuccess;
import p005io.reactivex.internal.operators.single.SingleDoAfterTerminate;
import p005io.reactivex.internal.operators.single.SingleDoFinally;
import p005io.reactivex.internal.operators.single.SingleDoOnDispose;
import p005io.reactivex.internal.operators.single.SingleDoOnError;
import p005io.reactivex.internal.operators.single.SingleDoOnEvent;
import p005io.reactivex.internal.operators.single.SingleDoOnSubscribe;
import p005io.reactivex.internal.operators.single.SingleDoOnSuccess;
import p005io.reactivex.internal.operators.single.SingleEquals;
import p005io.reactivex.internal.operators.single.SingleError;
import p005io.reactivex.internal.operators.single.SingleFlatMap;
import p005io.reactivex.internal.operators.single.SingleFlatMapCompletable;
import p005io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable;
import p005io.reactivex.internal.operators.single.SingleFlatMapIterableObservable;
import p005io.reactivex.internal.operators.single.SingleFlatMapMaybe;
import p005io.reactivex.internal.operators.single.SingleFromCallable;
import p005io.reactivex.internal.operators.single.SingleFromPublisher;
import p005io.reactivex.internal.operators.single.SingleFromUnsafeSource;
import p005io.reactivex.internal.operators.single.SingleHide;
import p005io.reactivex.internal.operators.single.SingleInternalHelper;
import p005io.reactivex.internal.operators.single.SingleJust;
import p005io.reactivex.internal.operators.single.SingleLift;
import p005io.reactivex.internal.operators.single.SingleMap;
import p005io.reactivex.internal.operators.single.SingleNever;
import p005io.reactivex.internal.operators.single.SingleObserveOn;
import p005io.reactivex.internal.operators.single.SingleOnErrorReturn;
import p005io.reactivex.internal.operators.single.SingleResumeNext;
import p005io.reactivex.internal.operators.single.SingleSubscribeOn;
import p005io.reactivex.internal.operators.single.SingleTakeUntil;
import p005io.reactivex.internal.operators.single.SingleTimeout;
import p005io.reactivex.internal.operators.single.SingleTimer;
import p005io.reactivex.internal.operators.single.SingleToFlowable;
import p005io.reactivex.internal.operators.single.SingleToObservable;
import p005io.reactivex.internal.operators.single.SingleUnsubscribeOn;
import p005io.reactivex.internal.operators.single.SingleUsing;
import p005io.reactivex.internal.operators.single.SingleZipArray;
import p005io.reactivex.internal.operators.single.SingleZipIterable;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.observers.TestObserver;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.Single */
public abstract class Single<T> implements SingleSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(@NonNull SingleObserver<? super T> singleObserver);

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> amb(Iterable<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleAmb<T>(null, sources));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> ambArray(SingleSource<? extends T>... sources) {
        if (sources.length == 0) {
            return error(SingleInternalHelper.emptyThrower());
        }
        if (sources.length == 1) {
            return wrap(sources[0]);
        }
        return RxJavaPlugins.onAssembly((Single<T>) new SingleAmb<T>(sources, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends SingleSource<? extends T>> sources) {
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromIterable(sources));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(sources, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources) {
        return concat(sources, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> sources, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapPublisher<T>(sources, SingleInternalHelper.toFlowable(), prefetch, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3, source4));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(SingleSource<? extends T>... sources) {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(Flowable.fromArray(sources), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(SingleSource<? extends T>... sources) {
        return Flowable.fromArray(sources).concatMapEager(SingleInternalHelper.toFlowable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends SingleSource<? extends T>> sources) {
        return Flowable.fromPublisher(sources).concatMapEager(SingleInternalHelper.toFlowable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends SingleSource<? extends T>> sources) {
        return Flowable.fromIterable(sources).concatMapEager(SingleInternalHelper.toFlowable());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> create(SingleOnSubscribe<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleCreate<T>(source));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> defer(Callable<? extends SingleSource<? extends T>> singleSupplier) {
        ObjectHelper.requireNonNull(singleSupplier, "singleSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDefer<T>(singleSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> error(Callable<? extends Throwable> errorSupplier) {
        ObjectHelper.requireNonNull(errorSupplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleError<T>(errorSupplier));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> error(Throwable exception) {
        ObjectHelper.requireNonNull(exception, "error is null");
        return error(Functions.justCallable(exception));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromCallable<T>(callable));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future) {
        return toSingle(Flowable.fromFuture(future));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        return toSingle(Flowable.fromFuture(future, timeout, unit));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, timeout, unit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Single<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromPublisher<T>(publisher));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> fromObservable(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observableSource is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(observableSource, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> just(T item) {
        ObjectHelper.requireNonNull(item, "value is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleJust<T>(item));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends SingleSource<? extends T>> sources) {
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromIterable(sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(sources, SingleInternalHelper.toFlowable(), false, Integer.MAX_VALUE, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> merge(SingleSource<? extends SingleSource<? extends T>> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFlatMap<T>(source, Functions.identity()));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3, source4));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends SingleSource<? extends T>> sources) {
        return mergeDelayError((Publisher<? extends SingleSource<? extends T>>) Flowable.fromIterable(sources));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends SingleSource<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        FlowableFlatMapPublisher flowableFlatMapPublisher = new FlowableFlatMapPublisher(sources, SingleInternalHelper.toFlowable(), true, Integer.MAX_VALUE, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMapPublisher);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return mergeDelayError((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return mergeDelayError((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(SingleSource<? extends T> source1, SingleSource<? extends T> source2, SingleSource<? extends T> source3, SingleSource<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return mergeDelayError((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(source1, source2, source3, source4));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> never() {
        return RxJavaPlugins.onAssembly(SingleNever.INSTANCE);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public static Single<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public static Single<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleTimer<T>(delay, unit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<Boolean> equals(SingleSource<? extends T> first, SingleSource<? extends T> second) {
        ObjectHelper.requireNonNull(first, "first is null");
        ObjectHelper.requireNonNull(second, "second is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleEquals<T>(first, second));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> unsafeCreate(SingleSource<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (!(onSubscribe instanceof Single)) {
            return RxJavaPlugins.onAssembly((Single<T>) new SingleFromUnsafeSource<T>(onSubscribe));
        }
        throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer) {
        return using(resourceSupplier, singleFunction, disposer, true);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> resourceSupplier, Function<? super U, ? extends SingleSource<? extends T>> singleFunction, Consumer<? super U> disposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(singleFunction, "singleFunction is null");
        ObjectHelper.requireNonNull(disposer, "disposer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleUsing<T>(resourceSupplier, singleFunction, disposer, eager));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T> Single<T> wrap(SingleSource<T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        if (source instanceof Single) {
            return RxJavaPlugins.onAssembly((Single) source);
        }
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromUnsafeSource<T>(source));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T, R> Single<R> zip(Iterable<? extends SingleSource<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleZipIterable<T>(sources, zipper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), source1, source2, source3, source4, source5);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
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
    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
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
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
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
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(SingleSource<? extends T1> source1, SingleSource<? extends T2> source2, SingleSource<? extends T3> source3, SingleSource<? extends T4> source4, SingleSource<? extends T5> source5, SingleSource<? extends T6> source6, SingleSource<? extends T7> source7, SingleSource<? extends T8> source8, SingleSource<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
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
    public static <T, R> Single<R> zipArray(Function<? super Object[], ? extends R> zipper, SingleSource<? extends T>... sources) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return error((Throwable) new NoSuchElementException());
        }
        return RxJavaPlugins.onAssembly((Single<T>) new SingleZipArray<T>(sources, zipper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> ambWith(SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    /* renamed from: as */
    public final <R> R mo14935as(@NonNull SingleConverter<T, ? extends R> converter) {
        return ((SingleConverter) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> hide() {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleHide<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Single<R> compose(SingleTransformer<? super T, ? extends R> transformer) {
        return wrap(((SingleTransformer) ObjectHelper.requireNonNull(transformer, "transformer is null")).apply(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> cache() {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleCache<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Single<U> cast(Class<? extends U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(SingleSource<? extends T> other) {
        return concat((SingleSource<? extends T>) this, other);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> delay(long time, TimeUnit unit) {
        return delay(time, unit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> delay(long time, TimeUnit unit, boolean delayError) {
        return delay(time, unit, Schedulers.computation(), delayError);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> delay(long time, TimeUnit unit, Scheduler scheduler) {
        return delay(time, unit, scheduler, false);
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Single<T> delay(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleDelay singleDelay = new SingleDelay(this, time, unit, scheduler, delayError);
        return RxJavaPlugins.onAssembly((Single<T>) singleDelay);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> delaySubscription(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithCompletable<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(SingleSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithSingle<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(ObservableSource<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithObservable<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithPublisher<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> delaySubscription(long time, TimeUnit unit) {
        return delaySubscription(time, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> delaySubscription(long time, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription((ObservableSource<U>) Observable.timer(time, unit, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doAfterSuccess(Consumer<? super T> onAfterSuccess) {
        ObjectHelper.requireNonNull(onAfterSuccess, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoAfterSuccess<T>(this, onAfterSuccess));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doAfterTerminate(Action onAfterTerminate) {
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoAfterTerminate<T>(this, onAfterTerminate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doFinally(Action onFinally) {
        ObjectHelper.requireNonNull(onFinally, "onFinally is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoFinally<T>(this, onFinally));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doOnSubscribe(Consumer<? super Disposable> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnSubscribe<T>(this, onSubscribe));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doOnSuccess(Consumer<? super T> onSuccess) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnSuccess<T>(this, onSuccess));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doOnEvent(BiConsumer<? super T, ? super Throwable> onEvent) {
        ObjectHelper.requireNonNull(onEvent, "onEvent is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnEvent<T>(this, onEvent));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doOnError(Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnError<T>(this, onError));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> doOnDispose(Action onDispose) {
        ObjectHelper.requireNonNull(onDispose, "onDispose is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnDispose<T>(this, onDispose));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFilterSingle<T>(this, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Single<R> flatMap(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFlatMap<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new SingleFlatMapMaybe<T>(this, mapper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return toFlowable().flatMap(mapper);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new SingleFlatMapIterableFlowable<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new SingleFlatMapIterableObservable<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> mapper) {
        return toObservable().flatMap(mapper);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new SingleFlatMapCompletable(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver<T> observer = new BlockingMultiObserver<>();
        subscribe((SingleObserver<? super T>) observer);
        return observer.blockingGet();
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Single<R> lift(SingleOperator<? extends R, ? super T> lift) {
        ObjectHelper.requireNonNull(lift, "onLift is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleLift<T>(this, lift));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <R> Single<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleMap<T>(this, mapper));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object value) {
        return contains(value, ObjectHelper.equalsPredicate());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object value, BiPredicate<Object, Object> comparer) {
        ObjectHelper.requireNonNull(value, "value is null");
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleContains<T>(this, value, comparer));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(SingleSource<? extends T> other) {
        return merge(this, other);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleObserveOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> onErrorReturn(Function<Throwable, ? extends T> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleOnErrorReturn<T>(this, resumeFunction, null));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> onErrorReturnItem(T value) {
        ObjectHelper.requireNonNull(value, "value is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleOnErrorReturn<T>(this, null, value));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Single<? extends T> resumeSingleInCaseOfError) {
        ObjectHelper.requireNonNull(resumeSingleInCaseOfError, "resumeSingleInCaseOfError is null");
        return onErrorResumeNext(Functions.justFunction(resumeSingleInCaseOfError));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Function<? super Throwable, ? extends SingleSource<? extends T>> resumeFunctionInCaseOfError) {
        ObjectHelper.requireNonNull(resumeFunctionInCaseOfError, "resumeFunctionInCaseOfError is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleResumeNext<T>(this, resumeFunctionInCaseOfError));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDetach<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return toFlowable().repeat();
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
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
        return toFlowable().repeatWhen(handler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        return toFlowable().repeatUntil(stop);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> retry() {
        return toSingle(toFlowable().retry());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> retry(long times) {
        return toSingle(toFlowable().retry(times));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("none")
    public final Single<T> retry(long times, Predicate<? super Throwable> predicate) {
        return toSingle(toFlowable().retry(times, predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> retry(Predicate<? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        return toSingle(toFlowable().retryWhen(handler));
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(BiConsumer<? super T, ? super Throwable> onCallback) {
        ObjectHelper.requireNonNull(onCallback, "onCallback is null");
        BiConsumerSingleObserver<T> s = new BiConsumerSingleObserver<>(onCallback);
        subscribe((SingleObserver<? super T>) s);
        return s;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess) {
        return subscribe(onSuccess, Functions.ON_ERROR_MISSING);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onSuccess, Consumer<? super Throwable> onError) {
        ObjectHelper.requireNonNull(onSuccess, "onSuccess is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ConsumerSingleObserver<T> s = new ConsumerSingleObserver<>(onSuccess, onError);
        subscribe((SingleObserver<? super T>) s);
        return s;
    }

    @SchedulerSupport("none")
    public final void subscribe(SingleObserver<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        SingleObserver<? super T> subscriber2 = RxJavaPlugins.onSubscribe(this, subscriber);
        ObjectHelper.requireNonNull(subscriber2, "subscriber returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(subscriber2);
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
    @SchedulerSupport("none")
    public final <E extends SingleObserver<? super T>> E subscribeWith(E observer) {
        subscribe((SingleObserver<? super T>) observer);
        return observer;
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleSubscribeOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Single<T> takeUntil(CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return takeUntil((Publisher<E>) new CompletableToFlowable<E>(other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(Publisher<E> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleTakeUntil<T>(this, other));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(SingleSource<? extends E> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return takeUntil((Publisher<E>) new SingleToFlowable<E>(other));
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Single<T> timeout(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, scheduler, other);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long timeout, TimeUnit unit, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    private Single<T> timeout0(long timeout, TimeUnit unit, Scheduler scheduler, SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleTimeout singleTimeout = new SingleTimeout(this, timeout, unit, scheduler, other);
        return RxJavaPlugins.onAssembly((Single<T>) singleTimeout);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo15006to(Function<? super Single<T>, R> convert) {
        try {
            return ((Function) ObjectHelper.requireNonNull(convert, "convert is null")).apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @CheckReturnValue
    @Deprecated
    @SchedulerSupport("none")
    public final Completable toCompletable() {
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromSingle(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElement() {
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromSingle(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new SingleToFlowable<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSingleObserver());
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe) this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromSingle<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new SingleToObservable<T>(this));
    }

    @CheckReturnValue
    @Experimental
    @SchedulerSupport("custom")
    public final Single<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleUnsubscribeOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final <U, R> Single<R> zipWith(SingleSource<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        return zip(this, other, zipper);
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> to = new TestObserver<>();
        subscribe((SingleObserver<? super T>) to);
        return to;
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean cancelled) {
        TestObserver<T> to = new TestObserver<>();
        if (cancelled) {
            to.cancel();
        }
        subscribe((SingleObserver<? super T>) to);
        return to;
    }

    private static <T> Single<T> toSingle(Flowable<T> source) {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(source, null));
    }
}
