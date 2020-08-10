package p005io.reactivex;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.annotations.BackpressureKind;
import p005io.reactivex.annotations.BackpressureSupport;
import p005io.reactivex.annotations.Beta;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.SchedulerSupport;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.flowables.ConnectableFlowable;
import p005io.reactivex.flowables.GroupedFlowable;
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
import p005io.reactivex.functions.LongConsumer;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.ScalarCallable;
import p005io.reactivex.internal.operators.flowable.BlockingFlowableIterable;
import p005io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
import p005io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
import p005io.reactivex.internal.operators.flowable.BlockingFlowableNext;
import p005io.reactivex.internal.operators.flowable.FlowableAllSingle;
import p005io.reactivex.internal.operators.flowable.FlowableAmb;
import p005io.reactivex.internal.operators.flowable.FlowableAnySingle;
import p005io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
import p005io.reactivex.internal.operators.flowable.FlowableBuffer;
import p005io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
import p005io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
import p005io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary;
import p005io.reactivex.internal.operators.flowable.FlowableBufferTimed;
import p005io.reactivex.internal.operators.flowable.FlowableCache;
import p005io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import p005io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import p005io.reactivex.internal.operators.flowable.FlowableConcatArray;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
import p005io.reactivex.internal.operators.flowable.FlowableConcatMapEagerPublisher;
import p005io.reactivex.internal.operators.flowable.FlowableConcatWithCompletable;
import p005io.reactivex.internal.operators.flowable.FlowableConcatWithMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableConcatWithSingle;
import p005io.reactivex.internal.operators.flowable.FlowableCountSingle;
import p005io.reactivex.internal.operators.flowable.FlowableCreate;
import p005io.reactivex.internal.operators.flowable.FlowableDebounce;
import p005io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
import p005io.reactivex.internal.operators.flowable.FlowableDefer;
import p005io.reactivex.internal.operators.flowable.FlowableDelay;
import p005io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import p005io.reactivex.internal.operators.flowable.FlowableDematerialize;
import p005io.reactivex.internal.operators.flowable.FlowableDetach;
import p005io.reactivex.internal.operators.flowable.FlowableDistinct;
import p005io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import p005io.reactivex.internal.operators.flowable.FlowableDoAfterNext;
import p005io.reactivex.internal.operators.flowable.FlowableDoFinally;
import p005io.reactivex.internal.operators.flowable.FlowableDoOnEach;
import p005io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle;
import p005io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
import p005io.reactivex.internal.operators.flowable.FlowableEmpty;
import p005io.reactivex.internal.operators.flowable.FlowableError;
import p005io.reactivex.internal.operators.flowable.FlowableFilter;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMap;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableFlatMapSingle;
import p005io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
import p005io.reactivex.internal.operators.flowable.FlowableFromArray;
import p005io.reactivex.internal.operators.flowable.FlowableFromCallable;
import p005io.reactivex.internal.operators.flowable.FlowableFromFuture;
import p005io.reactivex.internal.operators.flowable.FlowableFromIterable;
import p005io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import p005io.reactivex.internal.operators.flowable.FlowableGenerate;
import p005io.reactivex.internal.operators.flowable.FlowableGroupBy;
import p005io.reactivex.internal.operators.flowable.FlowableGroupJoin;
import p005io.reactivex.internal.operators.flowable.FlowableHide;
import p005io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
import p005io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable;
import p005io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import p005io.reactivex.internal.operators.flowable.FlowableInternalHelper.RequestMax;
import p005io.reactivex.internal.operators.flowable.FlowableInterval;
import p005io.reactivex.internal.operators.flowable.FlowableIntervalRange;
import p005io.reactivex.internal.operators.flowable.FlowableJoin;
import p005io.reactivex.internal.operators.flowable.FlowableJust;
import p005io.reactivex.internal.operators.flowable.FlowableLastMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableLastSingle;
import p005io.reactivex.internal.operators.flowable.FlowableLift;
import p005io.reactivex.internal.operators.flowable.FlowableLimit;
import p005io.reactivex.internal.operators.flowable.FlowableMap;
import p005io.reactivex.internal.operators.flowable.FlowableMapNotification;
import p005io.reactivex.internal.operators.flowable.FlowableMaterialize;
import p005io.reactivex.internal.operators.flowable.FlowableMergeWithCompletable;
import p005io.reactivex.internal.operators.flowable.FlowableMergeWithMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableMergeWithSingle;
import p005io.reactivex.internal.operators.flowable.FlowableNever;
import p005io.reactivex.internal.operators.flowable.FlowableObserveOn;
import p005io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
import p005io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
import p005io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import p005io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import p005io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
import p005io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import p005io.reactivex.internal.operators.flowable.FlowablePublish;
import p005io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
import p005io.reactivex.internal.operators.flowable.FlowableRange;
import p005io.reactivex.internal.operators.flowable.FlowableRangeLong;
import p005io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableReduceSeedSingle;
import p005io.reactivex.internal.operators.flowable.FlowableReduceWithSingle;
import p005io.reactivex.internal.operators.flowable.FlowableRepeat;
import p005io.reactivex.internal.operators.flowable.FlowableRepeatUntil;
import p005io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
import p005io.reactivex.internal.operators.flowable.FlowableReplay;
import p005io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate;
import p005io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
import p005io.reactivex.internal.operators.flowable.FlowableRetryWhen;
import p005io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
import p005io.reactivex.internal.operators.flowable.FlowableSampleTimed;
import p005io.reactivex.internal.operators.flowable.FlowableScalarXMap;
import p005io.reactivex.internal.operators.flowable.FlowableScan;
import p005io.reactivex.internal.operators.flowable.FlowableScanSeed;
import p005io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle;
import p005io.reactivex.internal.operators.flowable.FlowableSerialized;
import p005io.reactivex.internal.operators.flowable.FlowableSingleMaybe;
import p005io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import p005io.reactivex.internal.operators.flowable.FlowableSkip;
import p005io.reactivex.internal.operators.flowable.FlowableSkipLast;
import p005io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
import p005io.reactivex.internal.operators.flowable.FlowableSkipUntil;
import p005io.reactivex.internal.operators.flowable.FlowableSkipWhile;
import p005io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import p005io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty;
import p005io.reactivex.internal.operators.flowable.FlowableSwitchMap;
import p005io.reactivex.internal.operators.flowable.FlowableTake;
import p005io.reactivex.internal.operators.flowable.FlowableTakeLast;
import p005io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
import p005io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
import p005io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import p005io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
import p005io.reactivex.internal.operators.flowable.FlowableTakeWhile;
import p005io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;
import p005io.reactivex.internal.operators.flowable.FlowableThrottleLatest;
import p005io.reactivex.internal.operators.flowable.FlowableTimeInterval;
import p005io.reactivex.internal.operators.flowable.FlowableTimeout;
import p005io.reactivex.internal.operators.flowable.FlowableTimeoutTimed;
import p005io.reactivex.internal.operators.flowable.FlowableTimer;
import p005io.reactivex.internal.operators.flowable.FlowableToListSingle;
import p005io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn;
import p005io.reactivex.internal.operators.flowable.FlowableUsing;
import p005io.reactivex.internal.operators.flowable.FlowableWindow;
import p005io.reactivex.internal.operators.flowable.FlowableWindowBoundary;
import p005io.reactivex.internal.operators.flowable.FlowableWindowBoundarySelector;
import p005io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
import p005io.reactivex.internal.operators.flowable.FlowableWindowTimed;
import p005io.reactivex.internal.operators.flowable.FlowableWithLatestFrom;
import p005io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
import p005io.reactivex.internal.operators.flowable.FlowableZip;
import p005io.reactivex.internal.operators.flowable.FlowableZipIterable;
import p005io.reactivex.internal.operators.mixed.FlowableConcatMapCompletable;
import p005io.reactivex.internal.operators.mixed.FlowableConcatMapMaybe;
import p005io.reactivex.internal.operators.mixed.FlowableConcatMapSingle;
import p005io.reactivex.internal.operators.mixed.FlowableSwitchMapCompletable;
import p005io.reactivex.internal.operators.mixed.FlowableSwitchMapMaybe;
import p005io.reactivex.internal.operators.mixed.FlowableSwitchMapSingle;
import p005io.reactivex.internal.operators.observable.ObservableFromPublisher;
import p005io.reactivex.internal.schedulers.ImmediateThinScheduler;
import p005io.reactivex.internal.subscribers.BlockingFirstSubscriber;
import p005io.reactivex.internal.subscribers.BlockingLastSubscriber;
import p005io.reactivex.internal.subscribers.ForEachWhileSubscriber;
import p005io.reactivex.internal.subscribers.FutureSubscriber;
import p005io.reactivex.internal.subscribers.LambdaSubscriber;
import p005io.reactivex.internal.subscribers.StrictSubscriber;
import p005io.reactivex.internal.util.ArrayListSupplier;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.HashMapSupplier;
import p005io.reactivex.parallel.ParallelFlowable;
import p005io.reactivex.plugins.RxJavaPlugins;
import p005io.reactivex.schedulers.Schedulers;
import p005io.reactivex.schedulers.Timed;
import p005io.reactivex.subscribers.SafeSubscriber;
import p005io.reactivex.subscribers.TestSubscriber;

/* renamed from: io.reactivex.Flowable */
public abstract class Flowable<T> implements Publisher<T> {
    static final int BUFFER_SIZE = Math.max(1, Integer.getInteger("rx2.buffer-size", 128).intValue());

    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Subscriber<? super T> subscriber);

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> amb(Iterable<? extends Publisher<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAmb<T>(null, sources));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> ambArray(Publisher<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        int len = sources.length;
        if (len == 0) {
            return empty();
        }
        if (len == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAmb<T>(sources, null));
    }

    public static int bufferSize() {
        return BUFFER_SIZE;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(sources, combiner, bufferSize, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatest(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(sources, combiner, bufferSize, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, Publisher<? extends T>... sources) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> combiner, int bufferSize, Publisher<? extends T>... sources) {
        return combineLatestDelayError(sources, combiner, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (sources.length == 0) {
            return empty();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(sources, combiner, bufferSize, true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner) {
        return combineLatestDelayError(sources, combiner, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> combiner, int bufferSize) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(sources, combiner, bufferSize, true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4, source5});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4, source5, source6});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4, source5, source6, source7});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4, source5, source6, source7, source8});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> combineLatest(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return combineLatest(Functions.toFunction(combiner), (Publisher<? extends T>[]) new Publisher[]{source1, source2, source3, source4, source5, source6, source7, source8, source9});
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends Publisher<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return fromIterable(sources).concatMapDelayError(Functions.identity(), 2, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> sources) {
        return concat(sources, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> sources, int prefetch) {
        return fromPublisher(sources).concatMap(Functions.identity(), prefetch);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return concatArray(source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return concatArray(source1, source2, source3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return concatArray(source1, source2, source3, source4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatArray<T>(sources, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        if (sources.length == 1) {
            return fromPublisher(sources[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatArray<T>(sources, true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(Publisher<? extends T>... sources) {
        return concatArrayEager(bufferSize(), bufferSize(), sources);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(int maxConcurrency, int prefetch, Publisher<? extends T>... sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromArray(sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Iterable<? extends Publisher<? extends T>> sources) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        return fromIterable(sources).concatMapDelayError(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return concatDelayError(sources, bufferSize(), true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> sources, int prefetch, boolean tillTheEnd) {
        return fromPublisher(sources).concatMapDelayError(Functions.identity(), prefetch, tillTheEnd);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        FlowableConcatMapEagerPublisher flowableConcatMapEagerPublisher = new FlowableConcatMapEagerPublisher(sources, Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEagerPublisher);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources) {
        return concatEager(sources, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int prefetch) {
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromIterable(sources), Functions.identity(), maxConcurrency, prefetch, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> create(FlowableOnSubscribe<T> source, BackpressureStrategy mode) {
        ObjectHelper.requireNonNull(source, "source is null");
        ObjectHelper.requireNonNull(mode, "mode is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCreate<T>(source, mode));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> defer(Callable<? extends Publisher<? extends T>> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDefer<T>(supplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> empty() {
        return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Callable<? extends Throwable> supplier) {
        ObjectHelper.requireNonNull(supplier, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableError<T>(supplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Throwable throwable) {
        ObjectHelper.requireNonNull(throwable, "throwable is null");
        return error(Functions.justCallable(throwable));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromArray(T... items) {
        ObjectHelper.requireNonNull(items, "items is null");
        if (items.length == 0) {
            return empty();
        }
        if (items.length == 1) {
            return just(items[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromArray<T>(items));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromCallable(Callable<? extends T> supplier) {
        ObjectHelper.requireNonNull(supplier, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromCallable<T>(supplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromFuture<T>(future, 0, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromFuture<T>(future, timeout, unit));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, timeout, unit).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromIterable(Iterable<? extends T> source) {
        ObjectHelper.requireNonNull(source, "source is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromIterable<T>(source));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromPublisher(Publisher<? extends T> source) {
        if (source instanceof Flowable) {
            return RxJavaPlugins.onAssembly((Flowable) source);
        }
        ObjectHelper.requireNonNull(source, "publisher is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromPublisher<T>(source));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> generate(Consumer<Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(Functions.nullSupplier(), FlowableInternalHelper.simpleGenerator(generator), Functions.emptyConsumer());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), Functions.emptyConsumer());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiConsumer<S, Emitter<T>> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(generator, "generator is null");
        return generate(initialState, FlowableInternalHelper.simpleBiGenerator(generator), disposeState);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator) {
        return generate(initialState, generator, Functions.emptyConsumer());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> initialState, BiFunction<S, Emitter<T>, S> generator, Consumer<? super S> disposeState) {
        ObjectHelper.requireNonNull(initialState, "initialState is null");
        ObjectHelper.requireNonNull(generator, "generator is null");
        ObjectHelper.requireNonNull(disposeState, "disposeState is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableGenerate<T>(initialState, generator, disposeState));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit) {
        return interval(initialDelay, period, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableInterval flowableInterval = new FlowableInterval(Math.max(0, initialDelay), Math.max(0, period), unit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableInterval);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long period, TimeUnit unit) {
        return interval(period, period, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long period, TimeUnit unit, Scheduler scheduler) {
        return interval(period, period, unit, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit) {
        return intervalRange(start, count, initialDelay, period, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler) {
        long j = count;
        long j2 = initialDelay;
        TimeUnit timeUnit = unit;
        Scheduler scheduler2 = scheduler;
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (j == 0) {
            return empty().delay(j2, timeUnit, scheduler2);
        } else {
            long end = start + (j - 1);
            if (start <= 0 || end >= 0) {
                ObjectHelper.requireNonNull(timeUnit, "unit is null");
                ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
                FlowableIntervalRange flowableIntervalRange = r0;
                FlowableIntervalRange flowableIntervalRange2 = new FlowableIntervalRange(start, end, Math.max(0, j2), Math.max(0, period), timeUnit, scheduler);
                return RxJavaPlugins.onAssembly((Flowable<T>) flowableIntervalRange);
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableJust<T>(item));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        return fromArray(item1, item2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        return fromArray(item1, item2, item3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        return fromArray(item1, item2, item3, item4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        return fromArray(item1, item2, item3, item4, item5);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        ObjectHelper.requireNonNull(item9, "The ninth is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8, item9);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T item1, T item2, T item3, T item4, T item5, T item6, T item7, T item8, T item9, T item10) {
        ObjectHelper.requireNonNull(item1, "The first item is null");
        ObjectHelper.requireNonNull(item2, "The second item is null");
        ObjectHelper.requireNonNull(item3, "The third item is null");
        ObjectHelper.requireNonNull(item4, "The fourth item is null");
        ObjectHelper.requireNonNull(item5, "The fifth item is null");
        ObjectHelper.requireNonNull(item6, "The sixth item is null");
        ObjectHelper.requireNonNull(item7, "The seventh item is null");
        ObjectHelper.requireNonNull(item8, "The eighth item is null");
        ObjectHelper.requireNonNull(item9, "The ninth item is null");
        ObjectHelper.requireNonNull(item10, "The tenth item is null");
        return fromArray(item1, item2, item3, item4, item5, item6, item7, item8, item9, item10);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int bufferSize) {
        return fromIterable(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), false, maxConcurrency, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources) {
        return fromIterable(sources).flatMap(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency) {
        return fromIterable(sources).flatMap(Functions.identity(), maxConcurrency);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> sources) {
        return merge(sources, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency) {
        return fromPublisher(sources).flatMap(Functions.identity(), maxConcurrency);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), sources.length);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), false, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), false, 3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), false, 4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources) {
        return fromIterable(sources).flatMap(Functions.identity(), true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency, int bufferSize) {
        return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(int maxConcurrency, int bufferSize, Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, maxConcurrency, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> sources, int maxConcurrency) {
        return fromIterable(sources).flatMap(Functions.identity(), true, maxConcurrency);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return mergeDelayError(sources, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> sources, int maxConcurrency) {
        return fromPublisher(sources).flatMap(Functions.identity(), true, maxConcurrency);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(Publisher<? extends T>... sources) {
        return fromArray(sources).flatMap(Functions.identity(), true, sources.length);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return fromArray(source1, source2).flatMap(Functions.identity(), true, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return fromArray(source1, source2, source3).flatMap(Functions.identity(), true, 3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> source1, Publisher<? extends T> source2, Publisher<? extends T> source3, Publisher<? extends T> source4) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return fromArray(source1, source2, source3, source4).flatMap(Functions.identity(), true, 4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> never() {
        return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Integer> range(int start, int count) {
        if (count < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(count);
            throw new IllegalArgumentException(sb.toString());
        } else if (count == 0) {
            return empty();
        } else {
            if (count == 1) {
                return just(Integer.valueOf(start));
            }
            if (((long) start) + ((long) (count - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRange<T>(start, count));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Long> rangeLong(long start, long count) {
        if (count < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(count);
            throw new IllegalArgumentException(sb.toString());
        } else if (count == 0) {
            return empty();
        } else {
            if (count == 1) {
                return just(Long.valueOf(start));
            }
            long end = (count - 1) + start;
            if (start <= 0 || end >= 0) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRangeLong<T>(start, count));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual) {
        return sequenceEqual(source1, source2, isEqual, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, BiPredicate<? super T, ? super T> isEqual, int bufferSize) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(isEqual, "isEqual is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSequenceEqualSingle<T>(source1, source2, isEqual, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> source1, Publisher<? extends T> source2, int bufferSize) {
        return sequenceEqual(source1, source2, ObjectHelper.equalsPredicate(), bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> sources, int bufferSize) {
        return fromPublisher(sources).switchMap(Functions.identity(), bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> sources) {
        return fromPublisher(sources).switchMap(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> sources) {
        return switchOnNextDelayError(sources, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> sources, int prefetch) {
        return fromPublisher(sources).switchMapDelayError(Functions.identity(), prefetch);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> timer(long delay, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimer<T>(Math.max(0, delay), unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public static <T> Flowable<T> unsafeCreate(Publisher<T> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        if (!(onSubscribe instanceof Flowable)) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromPublisher<T>(onSubscribe));
        }
        throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer) {
        return using(resourceSupplier, sourceSupplier, resourceDisposer, true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> resourceSupplier, Function<? super D, ? extends Publisher<? extends T>> sourceSupplier, Consumer<? super D> resourceDisposer, boolean eager) {
        ObjectHelper.requireNonNull(resourceSupplier, "resourceSupplier is null");
        ObjectHelper.requireNonNull(sourceSupplier, "sourceSupplier is null");
        ObjectHelper.requireNonNull(resourceDisposer, "disposer is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableUsing<T>(resourceSupplier, sourceSupplier, resourceDisposer, eager));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        FlowableZip flowableZip = new FlowableZip(null, sources, zipper, bufferSize(), false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Publisher<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return fromPublisher(sources).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(zipper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize(), source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, BiFunction<? super T1, ? super T2, ? extends R> zipper, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return zipArray(Functions.toFunction(zipper), delayError, bufferSize, source1, source2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Function3<? super T1, ? super T2, ? super T3, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> zip(Publisher<? extends T1> source1, Publisher<? extends T2> source2, Publisher<? extends T3> source3, Publisher<? extends T4> source4, Publisher<? extends T5> source5, Publisher<? extends T6> source6, Publisher<? extends T7> source7, Publisher<? extends T8> source8, Publisher<? extends T9> source9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipper) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        ObjectHelper.requireNonNull(source5, "source5 is null");
        ObjectHelper.requireNonNull(source6, "source6 is null");
        ObjectHelper.requireNonNull(source7, "source7 is null");
        ObjectHelper.requireNonNull(source8, "source8 is null");
        ObjectHelper.requireNonNull(source9, "source9 is null");
        return zipArray(Functions.toFunction(zipper), false, bufferSize(), source1, source2, source3, source4, source5, source6, source7, source8, source9);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipArray(Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize, Publisher<? extends T>... sources) {
        if (sources.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(sources, null, zipper, bufferSize, delayError);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipIterable(Iterable<? extends Publisher<? extends T>> sources, Function<? super Object[], ? extends R> zipper, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        ObjectHelper.requireNonNull(sources, "sources is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(null, sources, zipper, bufferSize, delayError);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableAllSingle<T>(this, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> ambWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return ambArray(this, other);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableAnySingle<T>(this, predicate));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    /* renamed from: as */
    public final <R> R mo14076as(@NonNull FlowableConverter<T, ? extends R> converter) {
        return ((FlowableConverter) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst() {
        BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber<>();
        subscribe((FlowableSubscriber<? super T>) s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst(T defaultItem) {
        BlockingFirstSubscriber<T> s = new BlockingFirstSubscriber<>();
        subscribe((FlowableSubscriber<? super T>) s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingForEach(Consumer<? super T> onNext) {
        Iterator<T> it = blockingIterable().iterator();
        while (it.hasNext()) {
            try {
                onNext.accept(it.next());
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                ((Disposable) it).dispose();
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return new BlockingFlowableIterable(this, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast() {
        BlockingLastSubscriber<T> s = new BlockingLastSubscriber<>();
        subscribe((FlowableSubscriber<? super T>) s);
        T v = s.blockingGet();
        if (v != null) {
            return v;
        }
        throw new NoSuchElementException();
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast(T defaultItem) {
        BlockingLastSubscriber<T> s = new BlockingLastSubscriber<>();
        subscribe((FlowableSubscriber<? super T>) s);
        T v = s.blockingGet();
        return v != null ? v : defaultItem;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingLatest() {
        return new BlockingFlowableLatest(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingMostRecent(T initialItem) {
        return new BlockingFlowableMostRecent(this, initialItem);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingNext() {
        return new BlockingFlowableNext(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle() {
        return singleOrError().blockingGet();
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle(T defaultItem) {
        return single(defaultItem).blockingGet();
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSubscriber());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        FlowableBlockingSubscribe.subscribe(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext) {
        FlowableBlockingSubscribe.subscribe(this, onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        FlowableBlockingSubscribe.subscribe(this, onNext, onError, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        FlowableBlockingSubscribe.subscribe(this, onNext, onError, onComplete);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Subscriber<? super T> subscriber) {
        FlowableBlockingSubscribe.subscribe(this, subscriber);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int count) {
        return buffer(count, count);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int count, int skip) {
        return buffer(count, skip, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int count, int skip, Callable<U> bufferSupplier) {
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBuffer<T>(this, count, skip, bufferSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int count, Callable<U> bufferSupplier) {
        return buffer(count, count, bufferSupplier);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit) {
        return buffer(timespan, timeskip, unit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, timeskip, unit, scheduler, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier) {
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable = bufferSupplier;
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, timespan, timeskip, timeUnit, scheduler2, callable, Integer.MAX_VALUE, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableBufferTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit) {
        return buffer(timespan, unit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, int count) {
        return buffer(timespan, unit, Schedulers.computation(), count);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count) {
        return buffer(timespan, unit, scheduler, count, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long timespan, TimeUnit unit, Scheduler scheduler, int count, Callable<U> bufferSupplier, boolean restartTimerOnMaxSize) {
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable = bufferSupplier;
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        int i = count;
        ObjectHelper.verifyPositive(i, "count");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, timespan, timespan, timeUnit, scheduler2, callable, i, restartTimerOnMaxSize);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableBufferTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long timespan, TimeUnit unit, Scheduler scheduler) {
        return buffer(timespan, unit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing> Flowable<List<T>> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator) {
        return buffer(openingIndicator, closingIndicator, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing, U extends Collection<? super T>> Flowable<U> buffer(Flowable<? extends TOpening> openingIndicator, Function<? super TOpening, ? extends Publisher<? extends TClosing>> closingIndicator, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferBoundary<T>(this, openingIndicator, closingIndicator, bufferSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator) {
        return buffer(boundaryIndicator, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> boundaryIndicator, int initialCapacity) {
        ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
        return buffer(boundaryIndicator, Functions.createArrayList(initialCapacity));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Publisher<B> boundaryIndicator, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferExactBoundary<T>(this, boundaryIndicator, bufferSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
        return buffer(boundaryIndicatorSupplier, ArrayListSupplier.asCallable());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, Callable<U> bufferSupplier) {
        ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
        ObjectHelper.requireNonNull(bufferSupplier, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferBoundarySupplier<T>(this, boundaryIndicatorSupplier, bufferSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cache() {
        return cacheWithInitialCapacity(16);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cacheWithInitialCapacity(int initialCapacity) {
        ObjectHelper.verifyPositive(initialCapacity, "initialCapacity");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCache<T>(this, initialCapacity));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> cast(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return map(Functions.castFunction(clazz));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collect(Callable<? extends U> initialItemSupplier, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialItemSupplier, "initialItemSupplier is null");
        ObjectHelper.requireNonNull(collector, "collector is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableCollectSingle<T>(this, initialItemSupplier, collector));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collectInto(U initialItem, BiConsumer<? super U, ? super T> collector) {
        ObjectHelper.requireNonNull(initialItem, "initialItem is null");
        return collect(Functions.justCallable(initialItem), collector);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> compose(FlowableTransformer<? super T, ? extends R> composer) {
        return fromPublisher(((FlowableTransformer) ObjectHelper.requireNonNull(composer, "composer is null")).apply(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMap(mapper, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(this, mapper, prefetch, ErrorMode.IMMEDIATE));
        }
        T v = ((ScalarCallable) this).call();
        if (v == null) {
            return empty();
        }
        return FlowableScalarXMap.scalarXMap(v, mapper);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        return concatMapCompletable(mapper, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Completable concatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Completable) new FlowableConcatMapCompletable(this, mapper, ErrorMode.IMMEDIATE, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper) {
        return concatMapCompletableDelayError(mapper, true, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd) {
        return concatMapCompletableDelayError(mapper, tillTheEnd, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Completable concatMapCompletableDelayError(Function<? super T, ? extends CompletableSource> mapper, boolean tillTheEnd, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Completable) new FlowableConcatMapCompletable(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMapDelayError(mapper, 2, true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, boolean tillTheEnd) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        if (this instanceof ScalarCallable) {
            T v = ((ScalarCallable) this).call();
            if (v == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(v, mapper);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(this, mapper, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMapEager(mapper, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapEagerDelayError(mapper, bufferSize(), bufferSize(), tillTheEnd);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency, int prefetch, boolean tillTheEnd) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, mapper, maxConcurrency, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return concatMapIterable(mapper, 2);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlattenIterable<T>(this, mapper, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        return concatMapMaybe(mapper, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapMaybe<T>(this, mapper, ErrorMode.IMMEDIATE, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        return concatMapMaybeDelayError(mapper, true, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapMaybeDelayError(mapper, tillTheEnd, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapMaybeDelayError(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapMaybe<T>(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        return concatMapSingle(mapper, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapSingle<T>(this, mapper, ErrorMode.IMMEDIATE, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        return concatMapSingleDelayError(mapper, true, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapSingleDelayError(mapper, tillTheEnd, 2);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapSingleDelayError(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean tillTheEnd, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMapSingle<T>(this, mapper, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY, prefetch));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concat((Publisher<? extends T>) this, other);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(@NonNull SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatWithSingle<T>(this, other));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(@NonNull MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatWithMaybe<T>(this, other));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(@NonNull CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatWithCompletable<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return any(Functions.equalsWith(item));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableCountSingle<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> debounce(Function<? super T, ? extends Publisher<U>> debounceIndicator) {
        ObjectHelper.requireNonNull(debounceIndicator, "debounceIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDebounce<T>(this, debounceIndicator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> debounce(long timeout, TimeUnit unit) {
        return debounce(timeout, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> debounce(long timeout, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDebounceTimed flowableDebounceTimed = new FlowableDebounceTimed(this, timeout, unit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDebounceTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> defaultIfEmpty(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "item is null");
        return switchIfEmpty(just(defaultItem));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delay(Function<? super T, ? extends Publisher<U>> itemDelayIndicator) {
        ObjectHelper.requireNonNull(itemDelayIndicator, "itemDelayIndicator is null");
        return flatMap(FlowableInternalHelper.itemDelay(itemDelayIndicator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long delay, TimeUnit unit, boolean delayError) {
        return delay(delay, unit, Schedulers.computation(), delayError);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDelay flowableDelay = new FlowableDelay(this, Math.max(0, delay), unit, scheduler, delayError);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDelay);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> delay(Publisher<U> subscriptionIndicator, Function<? super T, ? extends Publisher<V>> itemDelayIndicator) {
        return delaySubscription(subscriptionIndicator).delay(itemDelayIndicator);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delaySubscription(Publisher<U> subscriptionIndicator) {
        ObjectHelper.requireNonNull(subscriptionIndicator, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDelaySubscriptionOther<T>(this, subscriptionIndicator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delaySubscription(long delay, TimeUnit unit) {
        return delaySubscription(delay, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delaySubscription(long delay, TimeUnit unit, Scheduler scheduler) {
        return delaySubscription(timer(delay, unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T2> Flowable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDematerialize<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> keySelector) {
        return distinct(keySelector, Functions.createHashSet());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> keySelector, Callable<? extends Collection<? super K>> collectionSupplier) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinct<T>(this, keySelector, collectionSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinctUntilChanged(Function<? super T, K> keySelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinctUntilChanged<T>(this, keySelector, ObjectHelper.equalsPredicate()));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> comparer) {
        ObjectHelper.requireNonNull(comparer, "comparer is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinctUntilChanged<T>(this, Functions.identity(), comparer));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doFinally(Action onFinally) {
        ObjectHelper.requireNonNull(onFinally, "onFinally is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoFinally<T>(this, onFinally));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doAfterNext(Consumer<? super T> onAfterNext) {
        ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoAfterNext<T>(this, onAfterNext));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doAfterTerminate(Action onAfterTerminate) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnCancel(Action onCancel) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, onCancel);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnComplete(Action onComplete) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    private Flowable<T> doOnEach(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Action onAfterTerminate) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        FlowableDoOnEach flowableDoOnEach = new FlowableDoOnEach(this, onNext, onError, onComplete, onAfterTerminate);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDoOnEach);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Consumer<? super Notification<T>> onNotification) {
        ObjectHelper.requireNonNull(onNotification, "consumer is null");
        return doOnEach(Functions.notificationOnNext(onNotification), Functions.notificationOnError(onNotification), Functions.notificationOnComplete(onNotification), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Subscriber<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        return doOnEach(FlowableInternalHelper.subscriberOnNext(subscriber), FlowableInternalHelper.subscriberOnError(subscriber), FlowableInternalHelper.subscriberOnComplete(subscriber), Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnError(Consumer<? super Throwable> onError) {
        return doOnEach(Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnLifecycle(Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        ObjectHelper.requireNonNull(onRequest, "onRequest is null");
        ObjectHelper.requireNonNull(onCancel, "onCancel is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoOnLifecycle<T>(this, onSubscribe, onRequest, onCancel));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnNext(Consumer<? super T> onNext) {
        return doOnEach(onNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnRequest(LongConsumer onRequest) {
        return doOnLifecycle(Functions.emptyConsumer(), onRequest, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnSubscribe(Consumer<? super Subscription> onSubscribe) {
        return doOnLifecycle(onSubscribe, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnTerminate(Action onTerminate) {
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(onTerminate), onTerminate, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> elementAt(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableElementAtMaybe<T>(this, index));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(index);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAt(long index, T defaultItem) {
        if (index < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("index >= 0 required but it was ");
            sb.append(index);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableElementAtSingle<T>(this, index, defaultItem));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAtOrError(long index) {
        if (index >= 0) {
            return RxJavaPlugins.onAssembly((Single<T>) new FlowableElementAtSingle<T>(this, index, null));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(index);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFilter<T>(this, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> first(T defaultItem) {
        return elementAt(0, defaultItem);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return flatMap(mapper, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors) {
        return flatMap(mapper, delayErrors, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int maxConcurrency) {
        return flatMap(mapper, false, maxConcurrency, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, delayErrors, maxConcurrency, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (this instanceof ScalarCallable) {
            T v = ((ScalarCallable) this).call();
            if (v == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(v, mapper);
        }
        FlowableFlatMap flowableFlatMap = new FlowableFlatMap(this, mapper, delayErrors, maxConcurrency, bufferSize);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMap);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<? super Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge((Publisher<? extends Publisher<? extends T>>) new FlowableMapNotification<Object>(this, onNextMapper, onErrorMapper, onCompleteSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> onNextMapper, Function<Throwable, ? extends Publisher<? extends R>> onErrorMapper, Callable<? extends Publisher<? extends R>> onCompleteSupplier, int maxConcurrency) {
        ObjectHelper.requireNonNull(onNextMapper, "onNextMapper is null");
        ObjectHelper.requireNonNull(onErrorMapper, "onErrorMapper is null");
        ObjectHelper.requireNonNull(onCompleteSupplier, "onCompleteSupplier is null");
        return merge((Publisher<? extends Publisher<? extends T>>) new FlowableMapNotification<Object>(this, onNextMapper, onErrorMapper, onCompleteSupplier), maxConcurrency);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner) {
        return flatMap(mapper, combiner, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors) {
        return flatMap(mapper, combiner, delayErrors, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency) {
        return flatMap(mapper, combiner, delayErrors, maxConcurrency, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, boolean delayErrors, int maxConcurrency, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return flatMap(FlowableInternalHelper.flatMapWithCombiner(mapper, combiner), delayErrors, maxConcurrency, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends R> combiner, int maxConcurrency) {
        return flatMap(mapper, combiner, false, maxConcurrency, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper) {
        return flatMapCompletable(mapper, false, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Completable) new FlowableFlatMapCompletableCompletable(this, mapper, delayErrors, maxConcurrency));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return flatMapIterable(mapper, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, int bufferSize) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlattenIterable<T>(this, mapper, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> mapper, BiFunction<? super T, ? super U, ? extends V> resultSelector, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(mapper), resultSelector, false, bufferSize(), prefetch);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        return flatMapMaybe(mapper, false, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlatMapMaybe<T>(this, mapper, delayErrors, maxConcurrency));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        return flatMapSingle(mapper, false, Integer.MAX_VALUE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> mapper, boolean delayErrors, int maxConcurrency) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlatMapSingle<T>(this, mapper, delayErrors, maxConcurrency));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEach(Consumer<? super T> onNext) {
        return subscribe(onNext);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext) {
        return forEachWhile(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError) {
        return forEachWhile(onNext, onError, Functions.EMPTY_ACTION);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ForEachWhileSubscriber<T> s = new ForEachWhileSubscriber<>(onNext, onError, onComplete);
        subscribe((FlowableSubscriber<? super T>) s);
        return s;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> keySelector) {
        return groupBy(keySelector, Functions.identity(), false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> keySelector, boolean delayError) {
        return groupBy(keySelector, Functions.identity(), delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return groupBy(keySelector, valueSelector, false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError) {
        return groupBy(keySelector, valueSelector, delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        FlowableGroupBy flowableGroupBy = new FlowableGroupBy(this, keySelector, valueSelector, bufferSize, delayError, null);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableGroupBy);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, boolean delayError, int bufferSize, Function<? super Consumer<Object>, ? extends Map<K, Object>> evictingMapFactory) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(evictingMapFactory, "evictingMapFactory is null");
        FlowableGroupBy flowableGroupBy = new FlowableGroupBy(this, keySelector, valueSelector, bufferSize, delayError, evictingMapFactory);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableGroupBy);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> groupJoin(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super Flowable<TRight>, ? extends R> resultSelector) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
        ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        FlowableGroupJoin flowableGroupJoin = new FlowableGroupJoin(this, other, leftEnd, rightEnd, resultSelector);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableGroupJoin);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> hide() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableHide<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @CheckReturnValue
    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly((Completable) new FlowableIgnoreElementsCompletable(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> join(Publisher<? extends TRight> other, Function<? super T, ? extends Publisher<TLeftEnd>> leftEnd, Function<? super TRight, ? extends Publisher<TRightEnd>> rightEnd, BiFunction<? super T, ? super TRight, ? extends R> resultSelector) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(leftEnd, "leftEnd is null");
        ObjectHelper.requireNonNull(rightEnd, "rightEnd is null");
        ObjectHelper.requireNonNull(resultSelector, "resultSelector is null");
        FlowableJoin flowableJoin = new FlowableJoin(this, other, leftEnd, rightEnd, resultSelector);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableJoin);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableLastMaybe<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> last(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableLastSingle<T>(this, defaultItem));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableLastSingle<T>(this, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> lift(FlowableOperator<? extends R, ? super T> lifter) {
        ObjectHelper.requireNonNull(lifter, "lifter is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableLift<T>(this, lifter));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> limit(long count) {
        if (count >= 0) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableLimit<T>(this, count));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(count);
        throw new IllegalArgumentException(sb.toString());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> map(Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMap<T>(this, mapper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMaterialize<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return merge((Publisher<? extends T>) this, other);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(@NonNull SingleSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMergeWithSingle<T>(this, other));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(@NonNull MaybeSource<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMergeWithMaybe<T>(this, other));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(@NonNull CompletableSource other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMergeWithCompletable<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError) {
        return observeOn(scheduler, delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableObserveOn<T>(this, scheduler, delayError, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> ofType(Class<U> clazz) {
        ObjectHelper.requireNonNull(clazz, "clazz is null");
        return filter(Functions.isInstanceOf(clazz)).cast(clazz);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer() {
        return onBackpressureBuffer(bufferSize(), false, true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(boolean delayError) {
        return onBackpressureBuffer(bufferSize(), delayError, true);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity) {
        return onBackpressureBuffer(capacity, false, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError) {
        return onBackpressureBuffer(capacity, delayError, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded) {
        ObjectHelper.verifyPositive(capacity, "bufferSize");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBuffer);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, boolean delayError, boolean unbounded, Action onOverflow) {
        ObjectHelper.requireNonNull(onOverflow, "onOverflow is null");
        ObjectHelper.verifyPositive(capacity, "capacity");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, capacity, unbounded, delayError, onOverflow);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBuffer);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int capacity, Action onOverflow) {
        return onBackpressureBuffer(capacity, false, false, onOverflow);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(long capacity, Action onOverflow, BackpressureOverflowStrategy overflowStrategy) {
        ObjectHelper.requireNonNull(overflowStrategy, "strategy is null");
        ObjectHelper.verifyPositive(capacity, "capacity");
        FlowableOnBackpressureBufferStrategy flowableOnBackpressureBufferStrategy = new FlowableOnBackpressureBufferStrategy(this, capacity, onOverflow, overflowStrategy);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBufferStrategy);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureDrop<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop(Consumer<? super T> onDrop) {
        ObjectHelper.requireNonNull(onDrop, "onDrop is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureDrop<T>(this, onDrop));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureLatest() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureLatest<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Function<? super Throwable, ? extends Publisher<? extends T>> resumeFunction) {
        ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorNext<T>(this, resumeFunction, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Publisher<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return onErrorResumeNext(Functions.justFunction(next));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturn(Function<? super Throwable, ? extends T> valueSupplier) {
        ObjectHelper.requireNonNull(valueSupplier, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorReturn<T>(this, valueSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturnItem(T item) {
        ObjectHelper.requireNonNull(item, "item is null");
        return onErrorReturn(Functions.justFunction(item));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onExceptionResumeNext(Publisher<? extends T> next) {
        ObjectHelper.requireNonNull(next, "next is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorNext<T>(this, Functions.justFunction(next), true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDetach<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable<T> parallel() {
        return ParallelFlowable.from(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable<T> parallel(int parallelism) {
        ObjectHelper.verifyPositive(parallelism, "parallelism");
        return ParallelFlowable.from(this, parallelism);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @Beta
    @SchedulerSupport("none")
    public final ParallelFlowable<T> parallel(int parallelism, int prefetch) {
        ObjectHelper.verifyPositive(parallelism, "parallelism");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return ParallelFlowable.from(this, parallelism, prefetch);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish() {
        return publish(bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
        return publish(selector, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector, int prefetch) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowablePublishMulticast<T>(this, selector, prefetch, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowablePublish.create(this, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> rebatchRequests(int n) {
        return observeOn(ImmediateThinScheduler.INSTANCE, true, n);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> reduce(BiFunction<T, T, T> reducer) {
        ObjectHelper.requireNonNull(reducer, "reducer is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableReduceMaybe<T>(this, reducer));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduce(R seed, BiFunction<R, ? super T, R> reducer) {
        ObjectHelper.requireNonNull(seed, "seed is null");
        ObjectHelper.requireNonNull(reducer, "reducer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableReduceSeedSingle<T>(this, seed, reducer));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduceWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> reducer) {
        ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
        ObjectHelper.requireNonNull(reducer, "reducer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableReduceWithSingle<T>(this, seedSupplier, reducer));
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
        if (times < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(times);
            throw new IllegalArgumentException(sb.toString());
        } else if (times == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeat<T>(this, times));
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeatUntil<T>(this, stop));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeatWhen<T>(this, handler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay() {
        return FlowableReplay.createFrom(this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), selector);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), selector);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit) {
        return replay(selector, bufferSize, time, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize, time, unit, scheduler), selector);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, int bufferSize, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, bufferSize), FlowableInternalHelper.replayFunction(selector, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit) {
        return replay(selector, time, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, time, unit, scheduler), selector);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> selector, Scheduler scheduler) {
        ObjectHelper.requireNonNull(selector, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), FlowableInternalHelper.replayFunction(selector, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay(int bufferSize) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowableReplay.create(this, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit) {
        return replay(bufferSize, time, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int bufferSize, long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return FlowableReplay.create(this, time, unit, scheduler, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int bufferSize, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(bufferSize), scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(long time, TimeUnit unit) {
        return replay(time, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(long time, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.create(this, time, unit, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(), scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry() {
        return retry(Long.MAX_VALUE, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(BiPredicate<? super Integer, ? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryBiPredicate<T>(this, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long count) {
        return retry(count, Functions.alwaysTrue());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long times, Predicate<? super Throwable> predicate) {
        if (times < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(times);
            throw new IllegalArgumentException(sb.toString());
        }
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryPredicate<T>(this, times, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(Long.MAX_VALUE, predicate);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryUntil(BooleanSupplier stop) {
        ObjectHelper.requireNonNull(stop, "stop is null");
        return retry(Long.MAX_VALUE, Functions.predicateReverseFor(stop));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> handler) {
        ObjectHelper.requireNonNull(handler, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryWhen<T>(this, handler));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final void safeSubscribe(Subscriber<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        if (s instanceof SafeSubscriber) {
            subscribe((FlowableSubscriber<? super T>) (SafeSubscriber) s);
        } else {
            subscribe((FlowableSubscriber<? super T>) new SafeSubscriber<Object>(s));
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> sample(long period, TimeUnit unit) {
        return sample(period, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> sample(long period, TimeUnit unit, boolean emitLast) {
        return sample(period, unit, Schedulers.computation(), emitLast);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> sample(long period, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableSampleTimed flowableSampleTimed = new FlowableSampleTimed(this, period, unit, scheduler, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableSampleTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> sample(long period, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableSampleTimed flowableSampleTimed = new FlowableSampleTimed(this, period, unit, scheduler, emitLast);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableSampleTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> sample(Publisher<U> sampler) {
        ObjectHelper.requireNonNull(sampler, "sampler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSamplePublisher<T>(this, sampler, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> sample(Publisher<U> sampler, boolean emitLast) {
        ObjectHelper.requireNonNull(sampler, "sampler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSamplePublisher<T>(this, sampler, emitLast));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> scan(BiFunction<T, T, T> accumulator) {
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableScan<T>(this, accumulator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scan(R initialValue, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(initialValue, "seed is null");
        return scanWith(Functions.justCallable(initialValue), accumulator);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scanWith(Callable<R> seedSupplier, BiFunction<R, ? super T, R> accumulator) {
        ObjectHelper.requireNonNull(seedSupplier, "seedSupplier is null");
        ObjectHelper.requireNonNull(accumulator, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableScanSeed<T>(this, seedSupplier, accumulator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> serialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSerialized<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> share() {
        return publish().refCount();
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableSingleMaybe<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> single(T defaultItem) {
        ObjectHelper.requireNonNull(defaultItem, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(this, defaultItem));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(this, null));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long count) {
        if (count <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkip<T>(this, count));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long time, TimeUnit unit) {
        return skipUntil(timer(time, unit));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> skip(long time, TimeUnit unit, Scheduler scheduler) {
        return skipUntil(timer(time, unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(int count) {
        if (count < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(count);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipLast<T>(this, count));
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long time, TimeUnit unit) {
        return skipLast(time, unit, Schedulers.computation(), false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long time, TimeUnit unit, boolean delayError) {
        return skipLast(time, unit, Schedulers.computation(), delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler) {
        return skipLast(time, unit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return skipLast(time, unit, scheduler, delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        int i = bufferSize;
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableSkipLastTimed flowableSkipLastTimed = new FlowableSkipLastTimed(this, time, timeUnit, scheduler2, i << 1, delayError);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableSkipLastTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> skipUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipUntil<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipWhile<T>(this, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sorted() {
        return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sorted(Comparator<? super T> sortFunction) {
        ObjectHelper.requireNonNull(sortFunction, "sortFunction");
        return toList().toFlowable().map(Functions.listSorter(sortFunction)).flatMapIterable(Functions.identity());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Iterable<? extends T> items) {
        return concatArray(fromIterable(items), this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return concatArray(other, this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(T value) {
        ObjectHelper.requireNonNull(value, "item is null");
        return concatArray(just(value), this);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWithArray(T... items) {
        Flowable<T> fromArray = fromArray(items);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext) {
        return subscribe(onNext, Functions.ON_ERROR_MISSING, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
        return subscribe(onNext, onError, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        return subscribe(onNext, onError, onComplete, RequestMax.INSTANCE);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Subscription> onSubscribe) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(onNext, onError, onComplete, onSubscribe);
        subscribe((FlowableSubscriber<? super T>) ls);
        return ls;
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void subscribe(Subscriber<? super T> s) {
        if (s instanceof FlowableSubscriber) {
            subscribe((FlowableSubscriber) s);
            return;
        }
        ObjectHelper.requireNonNull(s, "s is null");
        subscribe((FlowableSubscriber<? super T>) new StrictSubscriber<Object>(s));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @Beta
    @SchedulerSupport("none")
    public final void subscribe(FlowableSubscriber<? super T> s) {
        ObjectHelper.requireNonNull(s, "s is null");
        try {
            Subscriber<? super T> z = RxJavaPlugins.onSubscribe(this, (Subscriber<? super T>) s);
            ObjectHelper.requireNonNull(z, "Plugin returned null Subscriber");
            subscribeActual(z);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(e2);
            NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            npe.initCause(e2);
            throw npe;
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <E extends Subscriber<? super T>> E subscribeWith(E subscriber) {
        subscribe((Subscriber<? super T>) subscriber);
        return subscriber;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return subscribeOn(scheduler, !(this instanceof FlowableCreate));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> subscribeOn(@NonNull Scheduler scheduler, boolean requestOn) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSubscribeOn<T>(this, scheduler, requestOn));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> switchIfEmpty(Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchIfEmpty<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return switchMap(mapper, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
        return switchMap0(mapper, bufferSize, false);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable switchMapCompletable(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new FlowableSwitchMapCompletable(this, mapper, false));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable switchMapCompletableDelayError(@NonNull Function<? super T, ? extends CompletableSource> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new FlowableSwitchMapCompletable(this, mapper, true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return switchMapDelayError(mapper, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize) {
        return switchMap0(mapper, bufferSize, true);
    }

    /* access modifiers changed from: 0000 */
    public <R> Flowable<R> switchMap0(Function<? super T, ? extends Publisher<? extends R>> mapper, int bufferSize, boolean delayError) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMap<T>(this, mapper, bufferSize, delayError));
        }
        T v = ((ScalarCallable) this).call();
        if (v == null) {
            return empty();
        }
        return FlowableScalarXMap.scalarXMap(v, mapper);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapMaybe(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMapMaybe<T>(this, mapper, false));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapMaybeDelayError(@NonNull Function<? super T, ? extends MaybeSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMapMaybe<T>(this, mapper, true));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapSingle(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMapSingle<T>(this, mapper, false));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapSingleDelayError(@NonNull Function<? super T, ? extends SingleSource<? extends R>> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMapSingle<T>(this, mapper, true));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> take(long count) {
        if (count >= 0) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTake<T>(this, count));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(count);
        throw new IllegalArgumentException(sb.toString());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> take(long time, TimeUnit unit) {
        return takeUntil((Publisher<U>) timer(time, unit));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> take(long time, TimeUnit unit, Scheduler scheduler) {
        return takeUntil((Publisher<U>) timer(time, unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(int count) {
        if (count < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(count);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (count == 0) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableIgnoreElements<T>(this));
        } else {
            if (count == 1) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeLastOne<T>(this));
            }
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeLast<T>(this, count));
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit) {
        return takeLast(count, time, unit, Schedulers.computation(), false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(count, time, unit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long count, long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        long j = count;
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        int i = bufferSize;
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        FlowableTakeLastTimed flowableTakeLastTimed = new FlowableTakeLastTimed(this, j, time, timeUnit, scheduler2, i, delayError);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableTakeLastTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long time, TimeUnit unit) {
        return takeLast(time, unit, Schedulers.computation(), false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long time, TimeUnit unit, boolean delayError) {
        return takeLast(time, unit, Schedulers.computation(), delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler) {
        return takeLast(time, unit, scheduler, false, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        return takeLast(time, unit, scheduler, delayError, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long time, TimeUnit unit, Scheduler scheduler, boolean delayError, int bufferSize) {
        return takeLast(Long.MAX_VALUE, time, unit, scheduler, delayError, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeUntil(Predicate<? super T> stopPredicate) {
        ObjectHelper.requireNonNull(stopPredicate, "stopPredicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeUntilPredicate<T>(this, stopPredicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<T> takeUntil(Publisher<U> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeUntil<T>(this, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeWhile<T>(this, predicate));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleFirst(long windowDuration, TimeUnit unit) {
        return throttleFirst(windowDuration, unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleFirst(long skipDuration, TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableThrottleFirstTimed flowableThrottleFirstTimed = new FlowableThrottleFirstTimed(this, skipDuration, unit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableThrottleFirstTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit) {
        return sample(intervalDuration, unit);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleLast(long intervalDuration, TimeUnit unit, Scheduler scheduler) {
        return sample(intervalDuration, unit, scheduler);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleLatest(long timeout, TimeUnit unit) {
        return throttleLatest(timeout, unit, Schedulers.computation(), false);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, boolean emitLast) {
        return throttleLatest(timeout, unit, Schedulers.computation(), emitLast);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler) {
        return throttleLatest(timeout, unit, scheduler, false);
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleLatest(long timeout, TimeUnit unit, Scheduler scheduler, boolean emitLast) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableThrottleLatest flowableThrottleLatest = new FlowableThrottleLatest(this, timeout, unit, scheduler, emitLast);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableThrottleLatest);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit) {
        return debounce(timeout, unit);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleWithTimeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return debounce(timeout, unit, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit unit) {
        return timeInterval(unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimeInterval<T>(this, unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator) {
        return timeout0(null, itemTimeoutIndicator, null);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Flowable<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(null, itemTimeoutIndicator, other);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout0(timeout, timeUnit, null, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler, Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(timeout, timeUnit, other, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(timeout, timeUnit, null, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> timeout(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator) {
        ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutIndicator is null");
        return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, null);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> timeout(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(firstTimeoutIndicator, "firstTimeoutSelector is null");
        ObjectHelper.requireNonNull(other, "other is null");
        return timeout0(firstTimeoutIndicator, itemTimeoutIndicator, other);
    }

    private Flowable<T> timeout0(long timeout, TimeUnit timeUnit, Publisher<? extends T> other, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableTimeoutTimed flowableTimeoutTimed = new FlowableTimeoutTimed(this, timeout, timeUnit, scheduler, other);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableTimeoutTimed);
    }

    private <U, V> Flowable<T> timeout0(Publisher<U> firstTimeoutIndicator, Function<? super T, ? extends Publisher<V>> itemTimeoutIndicator, Publisher<? extends T> other) {
        ObjectHelper.requireNonNull(itemTimeoutIndicator, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimeout<T>(this, firstTimeoutIndicator, itemTimeoutIndicator, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit unit) {
        return timestamp(unit, Schedulers.computation());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit unit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(unit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(unit, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo14395to(Function<? super Flowable<T>, R> converter) {
        try {
            return ((Function) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList(int capacityHint) {
        ObjectHelper.verifyPositive(capacityHint, "capacityHint");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this, Functions.createArrayList(capacityHint)));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> collectionSupplier) {
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this, collectionSupplier));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> keySelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(keySelector));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, V>> mapSupplier) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        return collect(mapSupplier, Functions.toMapKeyValueSelector(keySelector, valueSelector));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> keySelector) {
        return toMultimap(keySelector, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector) {
        return toMultimap(keySelector, valueSelector, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<? extends Map<K, Collection<V>>> mapSupplier, Function<? super K, ? extends Collection<? super V>> collectionFactory) {
        ObjectHelper.requireNonNull(keySelector, "keySelector is null");
        ObjectHelper.requireNonNull(valueSelector, "valueSelector is null");
        ObjectHelper.requireNonNull(mapSupplier, "mapSupplier is null");
        ObjectHelper.requireNonNull(collectionFactory, "collectionFactory is null");
        return collect(mapSupplier, Functions.toMultimapKeyValueSelector(keySelector, valueSelector, collectionFactory));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> keySelector, Function<? super T, ? extends V> valueSelector, Callable<Map<K, Collection<V>>> mapSupplier) {
        return toMultimap(keySelector, valueSelector, mapSupplier, ArrayListSupplier.asFunction());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromPublisher<T>(this));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalComparator());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int capacityHint) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(capacityHint).map(Functions.listSorter(comparator));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(int capacityHint) {
        return toSortedList(Functions.naturalComparator(), capacityHint);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableUnsubscribeOn<T>(this, scheduler));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count) {
        return window(count, count, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count, long skip) {
        return window(count, skip, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long count, long skip, int bufferSize) {
        ObjectHelper.verifyPositive(skip, "skip");
        ObjectHelper.verifyPositive(count, "count");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        FlowableWindow flowableWindow = new FlowableWindow(this, count, skip, bufferSize);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindow);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit) {
        return window(timespan, timeskip, unit, Schedulers.computation(), bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, timeskip, unit, scheduler, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        int i = bufferSize;
        ObjectHelper.verifyPositive(i, "bufferSize");
        long j = timespan;
        ObjectHelper.verifyPositive(j, "timespan");
        long j2 = timeskip;
        ObjectHelper.verifyPositive(j2, "timeskip");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, j, j2, timeUnit, scheduler2, Long.MAX_VALUE, i, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindowTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit) {
        return window(timespan, unit, Schedulers.computation(), Long.MAX_VALUE, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count) {
        return window(timespan, unit, Schedulers.computation(), count, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, long count, boolean restart) {
        return window(timespan, unit, Schedulers.computation(), count, restart);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler) {
        return window(timespan, unit, scheduler, Long.MAX_VALUE, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count) {
        return window(timespan, unit, scheduler, count, false);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart) {
        return window(timespan, unit, scheduler, count, restart, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long timespan, TimeUnit unit, Scheduler scheduler, long count, boolean restart, int bufferSize) {
        int i = bufferSize;
        ObjectHelper.verifyPositive(i, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit = unit;
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        long j = count;
        ObjectHelper.verifyPositive(j, "count");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, timespan, timespan, timeUnit, scheduler2, j, i, restart);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindowTimed);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Publisher<B> boundaryIndicator) {
        return window(boundaryIndicator, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Publisher<B> boundaryIndicator, int bufferSize) {
        ObjectHelper.requireNonNull(boundaryIndicator, "boundaryIndicator is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundary<T>(this, boundaryIndicator, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U, V> Flowable<Flowable<T>> window(Publisher<U> openingIndicator, Function<? super U, ? extends Publisher<V>> closingIndicator) {
        return window(openingIndicator, closingIndicator, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U, V> Flowable<Flowable<T>> window(Publisher<U> openingIndicator, Function<? super U, ? extends Publisher<V>> closingIndicator, int bufferSize) {
        ObjectHelper.requireNonNull(openingIndicator, "openingIndicator is null");
        ObjectHelper.requireNonNull(closingIndicator, "closingIndicator is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundarySelector<T>(this, openingIndicator, closingIndicator, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier) {
        return window(boundaryIndicatorSupplier, bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> boundaryIndicatorSupplier, int bufferSize) {
        ObjectHelper.requireNonNull(boundaryIndicatorSupplier, "boundaryIndicatorSupplier is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundarySupplier<T>(this, boundaryIndicatorSupplier, bufferSize));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> withLatestFrom(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> combiner) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFrom<T>(this, combiner, other));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Function3<? super T, ? super T1, ? super T2, R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        return withLatestFrom((Publisher<?>[]) new Publisher[]{source1, source2}, Functions.toFunction(combiner));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        return withLatestFrom((Publisher<?>[]) new Publisher[]{source1, source2, source3}, Functions.toFunction(combiner));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, T4, R> Flowable<R> withLatestFrom(Publisher<T1> source1, Publisher<T2> source2, Publisher<T3> source3, Publisher<T4> source4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> combiner) {
        ObjectHelper.requireNonNull(source1, "source1 is null");
        ObjectHelper.requireNonNull(source2, "source2 is null");
        ObjectHelper.requireNonNull(source3, "source3 is null");
        ObjectHelper.requireNonNull(source4, "source4 is null");
        return withLatestFrom((Publisher<?>[]) new Publisher[]{source1, source2, source3, source4}, Functions.toFunction(combiner));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Publisher<?>[] others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFromMany<T>(this, others, combiner));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Iterable<? extends Publisher<?>> others, Function<? super Object[], R> combiner) {
        ObjectHelper.requireNonNull(others, "others is null");
        ObjectHelper.requireNonNull(combiner, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFromMany<T>(this, others, combiner));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Iterable<U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        ObjectHelper.requireNonNull(zipper, "zipper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableZipIterable<T>(this, other, zipper));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper) {
        ObjectHelper.requireNonNull(other, "other is null");
        return zip(this, other, zipper);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError) {
        return zip((Publisher<? extends T1>) this, other, zipper, delayError);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> other, BiFunction<? super T, ? super U, ? extends R> zipper, boolean delayError, int bufferSize) {
        return zip((Publisher<? extends T1>) this, other, zipper, delayError, bufferSize);
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test() {
        TestSubscriber<T> ts = new TestSubscriber<>();
        subscribe((FlowableSubscriber<? super T>) ts);
        return ts;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long initialRequest) {
        TestSubscriber<T> ts = new TestSubscriber<>(initialRequest);
        subscribe((FlowableSubscriber<? super T>) ts);
        return ts;
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long initialRequest, boolean cancel) {
        TestSubscriber<T> ts = new TestSubscriber<>(initialRequest);
        if (cancel) {
            ts.cancel();
        }
        subscribe((FlowableSubscriber<? super T>) ts);
        return ts;
    }
}
