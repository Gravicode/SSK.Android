package p005io.reactivex.parallel;

import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.Scheduler;
import p005io.reactivex.annotations.BackpressureKind;
import p005io.reactivex.annotations.BackpressureSupport;
import p005io.reactivex.annotations.Beta;
import p005io.reactivex.annotations.CheckReturnValue;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.SchedulerSupport;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Action;
import p005io.reactivex.functions.BiConsumer;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.functions.Consumer;
import p005io.reactivex.functions.Function;
import p005io.reactivex.functions.LongConsumer;
import p005io.reactivex.functions.Predicate;
import p005io.reactivex.internal.functions.Functions;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.operators.parallel.ParallelCollect;
import p005io.reactivex.internal.operators.parallel.ParallelConcatMap;
import p005io.reactivex.internal.operators.parallel.ParallelDoOnNextTry;
import p005io.reactivex.internal.operators.parallel.ParallelFilter;
import p005io.reactivex.internal.operators.parallel.ParallelFilterTry;
import p005io.reactivex.internal.operators.parallel.ParallelFlatMap;
import p005io.reactivex.internal.operators.parallel.ParallelFromArray;
import p005io.reactivex.internal.operators.parallel.ParallelFromPublisher;
import p005io.reactivex.internal.operators.parallel.ParallelJoin;
import p005io.reactivex.internal.operators.parallel.ParallelMap;
import p005io.reactivex.internal.operators.parallel.ParallelMapTry;
import p005io.reactivex.internal.operators.parallel.ParallelPeek;
import p005io.reactivex.internal.operators.parallel.ParallelReduce;
import p005io.reactivex.internal.operators.parallel.ParallelReduceFull;
import p005io.reactivex.internal.operators.parallel.ParallelRunOn;
import p005io.reactivex.internal.operators.parallel.ParallelSortedJoin;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.internal.util.ExceptionHelper;
import p005io.reactivex.internal.util.ListAddBiConsumer;
import p005io.reactivex.internal.util.MergerBiFunction;
import p005io.reactivex.internal.util.SorterFunction;
import p005io.reactivex.plugins.RxJavaPlugins;

@Beta
/* renamed from: io.reactivex.parallel.ParallelFlowable */
public abstract class ParallelFlowable<T> {
    public abstract int parallelism();

    public abstract void subscribe(@NonNull Subscriber<? super T>[] subscriberArr);

    /* access modifiers changed from: protected */
    public final boolean validate(@NonNull Subscriber<?>[] subscribers) {
        int p = parallelism();
        if (subscribers.length == p) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parallelism = ");
        sb.append(p);
        sb.append(", subscribers = ");
        sb.append(subscribers.length);
        Throwable iae = new IllegalArgumentException(sb.toString());
        for (Subscriber<?> s : subscribers) {
            EmptySubscription.error(iae, s);
        }
        return false;
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source) {
        return from(source, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source, int parallelism) {
        return from(source, parallelism, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> source, int parallelism, int prefetch) {
        ObjectHelper.requireNonNull(source, Param.SOURCE);
        ObjectHelper.verifyPositive(parallelism, "parallelism");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFromPublisher<T>(source, parallelism, prefetch));
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    /* renamed from: as */
    public final <R> R mo16282as(@NonNull ParallelFlowableConverter<T, R> converter) {
        return ((ParallelFlowableConverter) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper) {
        ObjectHelper.requireNonNull(mapper, "mapper");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMap<T>(this, mapper));
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper, @NonNull ParallelFailureHandling errorHandler) {
        ObjectHelper.requireNonNull(mapper, "mapper");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMapTry<T>(this, mapper, errorHandler));
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> mapper, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
        ObjectHelper.requireNonNull(mapper, "mapper");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMapTry<T>(this, mapper, errorHandler));
    }

    @CheckReturnValue
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilter<T>(this, predicate));
    }

    @CheckReturnValue
    @Experimental
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull ParallelFailureHandling errorHandler) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilterTry<T>(this, predicate, errorHandler));
    }

    @CheckReturnValue
    @Experimental
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilterTry<T>(this, predicate, errorHandler));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler) {
        return runOn(scheduler, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler, int prefetch) {
        ObjectHelper.requireNonNull(scheduler, "scheduler");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelRunOn<T>(this, scheduler, prefetch));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> reduce(@NonNull BiFunction<T, T, T> reducer) {
        ObjectHelper.requireNonNull(reducer, "reducer");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelReduceFull<T>(this, reducer));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> reduce(@NonNull Callable<R> initialSupplier, @NonNull BiFunction<R, ? super T, R> reducer) {
        ObjectHelper.requireNonNull(initialSupplier, "initialSupplier");
        ObjectHelper.requireNonNull(reducer, "reducer");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelReduce<T>(this, initialSupplier, reducer));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sequential() {
        return sequential(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequential(int prefetch) {
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelJoin<T>(this, prefetch, false));
    }

    @CheckReturnValue
    @Experimental
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequentialDelayError() {
        return sequentialDelayError(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequentialDelayError(int prefetch) {
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelJoin<T>(this, prefetch, true));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator) {
        return sorted(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator, int capacityHint) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(capacityHint, "capacityHint");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelSortedJoin<T>(reduce(Functions.createArrayList((capacityHint / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)), comparator));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator) {
        return toSortedList(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator, int capacityHint) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(capacityHint, "capacityHint");
        return RxJavaPlugins.onAssembly(reduce(Functions.createArrayList((capacityHint / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)).reduce(new MergerBiFunction(comparator)));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, onNext, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext, @NonNull ParallelFailureHandling errorHandler) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelDoOnNextTry<T>(this, onNext, errorHandler));
    }

    @CheckReturnValue
    @Experimental
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> onNext, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> errorHandler) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(errorHandler, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelDoOnNextTry<T>(this, onNext, errorHandler));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doAfterNext(@NonNull Consumer<? super T> onAfterNext) {
        ObjectHelper.requireNonNull(onAfterNext, "onAfterNext is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), onAfterNext, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnError(@NonNull Consumer<Throwable> onError) {
        ObjectHelper.requireNonNull(onError, "onError is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), onError, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnComplete(@NonNull Action onComplete) {
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), onComplete, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doAfterTerminated(@NonNull Action onAfterTerminate) {
        ObjectHelper.requireNonNull(onAfterTerminate, "onAfterTerminate is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, onAfterTerminate, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnSubscribe(@NonNull Consumer<? super Subscription> onSubscribe) {
        ObjectHelper.requireNonNull(onSubscribe, "onSubscribe is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, onSubscribe, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnRequest(@NonNull LongConsumer onRequest) {
        ObjectHelper.requireNonNull(onRequest, "onRequest is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), onRequest, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnCancel(@NonNull Action onCancel) {
        ObjectHelper.requireNonNull(onCancel, "onCancel is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, onCancel);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final <C> ParallelFlowable<C> collect(@NonNull Callable<? extends C> collectionSupplier, @NonNull BiConsumer<? super C, ? super T> collector) {
        ObjectHelper.requireNonNull(collectionSupplier, "collectionSupplier is null");
        ObjectHelper.requireNonNull(collector, "collector is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelCollect<T>(this, collectionSupplier, collector));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ParallelFlowable<T> fromArray(@NonNull Publisher<T>... publishers) {
        if (publishers.length != 0) {
            return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFromArray<T>(publishers));
        }
        throw new IllegalArgumentException("Zero publishers not supported");
    }

    @CheckReturnValue
    @NonNull
    /* renamed from: to */
    public final <U> U mo16319to(@NonNull Function<? super ParallelFlowable<T>, U> converter) {
        try {
            return ((Function) ObjectHelper.requireNonNull(converter, "converter is null")).apply(this);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @CheckReturnValue
    @NonNull
    public final <U> ParallelFlowable<U> compose(@NonNull ParallelTransformer<T, U> composer) {
        return RxJavaPlugins.onAssembly(((ParallelTransformer) ObjectHelper.requireNonNull(composer, "composer is null")).apply(this));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return flatMap(mapper, false, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError) {
        return flatMap(mapper, delayError, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError, int maxConcurrency) {
        return flatMap(mapper, delayError, maxConcurrency, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean delayError, int maxConcurrency, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(maxConcurrency, "maxConcurrency");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        ParallelFlatMap parallelFlatMap = new ParallelFlatMap(this, mapper, delayError, maxConcurrency, prefetch);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelFlatMap);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper) {
        return concatMap(mapper, 2);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelConcatMap<T>(this, mapper, prefetch, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, boolean tillTheEnd) {
        return concatMapDelayError(mapper, 2, tillTheEnd);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> mapper, int prefetch, boolean tillTheEnd) {
        ObjectHelper.requireNonNull(mapper, "mapper is null");
        ObjectHelper.verifyPositive(prefetch, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelConcatMap<T>(this, mapper, prefetch, tillTheEnd ? ErrorMode.END : ErrorMode.BOUNDARY));
    }
}
