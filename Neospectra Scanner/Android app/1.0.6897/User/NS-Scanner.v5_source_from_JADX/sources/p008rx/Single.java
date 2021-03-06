package p008rx;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import p008rx.Observable.Operator;
import p008rx.Scheduler.Worker;
import p008rx.annotations.Beta;
import p008rx.annotations.Experimental;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.OnErrorNotImplementedException;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Actions;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.functions.Func3;
import p008rx.functions.Func4;
import p008rx.functions.Func5;
import p008rx.functions.Func6;
import p008rx.functions.Func7;
import p008rx.functions.Func8;
import p008rx.functions.Func9;
import p008rx.functions.FuncN;
import p008rx.internal.operators.CompletableFlatMapSingleToCompletable;
import p008rx.internal.operators.SingleDelay;
import p008rx.internal.operators.SingleDoAfterTerminate;
import p008rx.internal.operators.SingleDoOnEvent;
import p008rx.internal.operators.SingleDoOnSubscribe;
import p008rx.internal.operators.SingleDoOnUnsubscribe;
import p008rx.internal.operators.SingleFromCallable;
import p008rx.internal.operators.SingleFromFuture;
import p008rx.internal.operators.SingleFromObservable;
import p008rx.internal.operators.SingleLiftObservableOperator;
import p008rx.internal.operators.SingleObserveOn;
import p008rx.internal.operators.SingleOnErrorReturn;
import p008rx.internal.operators.SingleOnSubscribeDelaySubscriptionOther;
import p008rx.internal.operators.SingleOnSubscribeMap;
import p008rx.internal.operators.SingleOnSubscribeUsing;
import p008rx.internal.operators.SingleOperatorOnErrorResumeNext;
import p008rx.internal.operators.SingleOperatorZip;
import p008rx.internal.operators.SingleTakeUntilCompletable;
import p008rx.internal.operators.SingleTakeUntilObservable;
import p008rx.internal.operators.SingleTakeUntilSingle;
import p008rx.internal.operators.SingleTimeout;
import p008rx.internal.operators.SingleToObservable;
import p008rx.internal.util.ScalarSynchronousSingle;
import p008rx.internal.util.UtilityFunctions;
import p008rx.observers.SafeSubscriber;
import p008rx.plugins.RxJavaHooks;
import p008rx.schedulers.Schedulers;
import p008rx.singles.BlockingSingle;

/* renamed from: rx.Single */
public class Single<T> {
    final OnSubscribe<T> onSubscribe;

    /* renamed from: rx.Single$OnSubscribe */
    public interface OnSubscribe<T> extends Action1<SingleSubscriber<? super T>> {
    }

    /* renamed from: rx.Single$Transformer */
    public interface Transformer<T, R> extends Func1<Single<T>, Single<R>> {
    }

    protected Single(OnSubscribe<T> f) {
        this.onSubscribe = RxJavaHooks.onCreate(f);
    }

    @Deprecated
    protected Single(p008rx.Observable.OnSubscribe<T> f) {
        this.onSubscribe = RxJavaHooks.onCreate((OnSubscribe<T>) new SingleFromObservable<T>(f));
    }

    public static <T> Single<T> create(OnSubscribe<T> f) {
        return new Single<>(f);
    }

    @Beta
    public final <R> Single<R> lift(Operator<? extends R, ? super T> lift) {
        return create(new SingleLiftObservableOperator(this.onSubscribe, lift));
    }

    public <R> Single<R> compose(Transformer<? super T, ? extends R> transformer) {
        return (Single) transformer.call(this);
    }

    private static <T> Observable<T> asObservable(Single<T> t) {
        return Observable.create((p008rx.Observable.OnSubscribe<T>) new SingleToObservable<T>(t.onSubscribe));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.concat(asObservable(t1), asObservable(t2));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static <T> Observable<T> concat(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.concat(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static <T> Single<T> error(final Throwable exception) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> te) {
                te.onError(exception);
            }
        });
    }

    public static <T> Single<T> from(Future<? extends T> future) {
        return create(new SingleFromFuture(future, 0, null));
    }

    public static <T> Single<T> from(Future<? extends T> future, long timeout, TimeUnit unit) {
        if (unit != null) {
            return create(new SingleFromFuture(future, timeout, unit));
        }
        throw new NullPointerException("unit is null");
    }

    public static <T> Single<T> from(Future<? extends T> future, Scheduler scheduler) {
        return from(future).subscribeOn(scheduler);
    }

    public static <T> Single<T> fromCallable(Callable<? extends T> func) {
        return create(new SingleFromCallable(func));
    }

    public static <T> Single<T> just(T value) {
        return ScalarSynchronousSingle.create(value);
    }

    public static <T> Single<T> merge(Single<? extends Single<? extends T>> source) {
        if (source instanceof ScalarSynchronousSingle) {
            return ((ScalarSynchronousSingle) source).scalarFlatMap(UtilityFunctions.identity());
        }
        return create(new OnSubscribe<T>(source) {
            final /* synthetic */ Single val$source;

            {
                this.val$source = r1;
            }

            public void call(final SingleSubscriber<? super T> child) {
                SingleSubscriber<Single<? extends T>> parent = new SingleSubscriber<Single<? extends T>>() {
                    public void onSuccess(Single<? extends T> innerSingle) {
                        innerSingle.subscribe(child);
                    }

                    public void onError(Throwable error) {
                        child.onError(error);
                    }
                };
                child.add(parent);
                this.val$source.subscribe(parent);
            }
        });
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2) {
        return Observable.merge(asObservable(t1), asObservable(t2));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8));
    }

    public static <T> Observable<T> merge(Single<? extends T> t1, Single<? extends T> t2, Single<? extends T> t3, Single<? extends T> t4, Single<? extends T> t5, Single<? extends T> t6, Single<? extends T> t7, Single<? extends T> t8, Single<? extends T> t9) {
        return Observable.merge(asObservable(t1), asObservable(t2), asObservable(t3), asObservable(t4), asObservable(t5), asObservable(t6), asObservable(t7), asObservable(t8), asObservable(t9));
    }

    public static <T1, T2, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, final Func2<? super T1, ? super T2, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1]);
            }
        });
    }

    public static <T1, T2, T3, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, final Func3<? super T1, ? super T2, ? super T3, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2]);
            }
        });
    }

    public static <T1, T2, T3, T4, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, final Func4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3]);
            }
        });
    }

    public static <T1, T2, T3, T4, T5, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, Single<? extends T5> s5, final Func5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4, s5}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3], args[4]);
            }
        });
    }

    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, Single<? extends T5> s5, Single<? extends T6> s6, final Func6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4, s5, s6}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3], args[4], args[5]);
            }
        });
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, Single<? extends T5> s5, Single<? extends T6> s6, Single<? extends T7> s7, final Func7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4, s5, s6, s7}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            }
        });
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, Single<? extends T5> s5, Single<? extends T6> s6, Single<? extends T7> s7, Single<? extends T8> s8, final Func8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4, s5, s6, s7, s8}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            }
        });
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(Single<? extends T1> s1, Single<? extends T2> s2, Single<? extends T3> s3, Single<? extends T4> s4, Single<? extends T5> s5, Single<? extends T6> s6, Single<? extends T7> s7, Single<? extends T8> s8, Single<? extends T9> s9, final Func9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> zipFunction) {
        return SingleOperatorZip.zip(new Single[]{s1, s2, s3, s4, s5, s6, s7, s8, s9}, new FuncN<R>() {
            public R call(Object... args) {
                return zipFunction.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            }
        });
    }

    public static <R> Single<R> zip(Iterable<? extends Single<?>> singles, FuncN<? extends R> zipFunction) {
        return SingleOperatorZip.zip(iterableToArray(singles), zipFunction);
    }

    public final Observable<T> concatWith(Single<? extends T> t1) {
        return concat(this, t1);
    }

    public final <R> Single<R> flatMap(Func1<? super T, ? extends Single<? extends R>> func) {
        if (this instanceof ScalarSynchronousSingle) {
            return ((ScalarSynchronousSingle) this).scalarFlatMap(func);
        }
        return merge(map(func));
    }

    public final <R> Observable<R> flatMapObservable(Func1<? super T, ? extends Observable<? extends R>> func) {
        return Observable.merge(asObservable(map(func)));
    }

    @Beta
    public final Completable flatMapCompletable(Func1<? super T, ? extends Completable> func) {
        return Completable.create(new CompletableFlatMapSingleToCompletable(this, func));
    }

    public final <R> Single<R> map(Func1<? super T, ? extends R> func) {
        return create(new SingleOnSubscribeMap(this, func));
    }

    public final Observable<T> mergeWith(Single<? extends T> t1) {
        return merge(this, t1);
    }

    public final Single<T> observeOn(Scheduler scheduler) {
        if (this instanceof ScalarSynchronousSingle) {
            return ((ScalarSynchronousSingle) this).scalarScheduleOn(scheduler);
        }
        if (scheduler != null) {
            return create(new SingleObserveOn(this.onSubscribe, scheduler));
        }
        throw new NullPointerException("scheduler is null");
    }

    public final Single<T> onErrorReturn(Func1<Throwable, ? extends T> resumeFunction) {
        return create(new SingleOnErrorReturn(this.onSubscribe, resumeFunction));
    }

    @Beta
    public final Single<T> onErrorResumeNext(Single<? extends T> resumeSingleInCaseOfError) {
        return new Single<>((OnSubscribe<T>) SingleOperatorOnErrorResumeNext.withOther(this, resumeSingleInCaseOfError));
    }

    @Beta
    public final Single<T> onErrorResumeNext(Func1<Throwable, ? extends Single<? extends T>> resumeFunctionInCaseOfError) {
        return new Single<>((OnSubscribe<T>) SingleOperatorOnErrorResumeNext.withFunction(this, resumeFunctionInCaseOfError));
    }

    public final Subscription subscribe() {
        return subscribe((SingleSubscriber<? super T>) new SingleSubscriber<T>() {
            public final void onError(Throwable e) {
                throw new OnErrorNotImplementedException(e);
            }

            public final void onSuccess(T t) {
            }
        });
    }

    public final Subscription subscribe(final Action1<? super T> onSuccess) {
        if (onSuccess != null) {
            return subscribe((SingleSubscriber<? super T>) new SingleSubscriber<T>() {
                public final void onError(Throwable e) {
                    throw new OnErrorNotImplementedException(e);
                }

                public final void onSuccess(T args) {
                    onSuccess.call(args);
                }
            });
        }
        throw new IllegalArgumentException("onSuccess can not be null");
    }

    public final Subscription subscribe(final Action1<? super T> onSuccess, final Action1<Throwable> onError) {
        if (onSuccess == null) {
            throw new IllegalArgumentException("onSuccess can not be null");
        } else if (onError != null) {
            return subscribe((SingleSubscriber<? super T>) new SingleSubscriber<T>() {
                public final void onError(Throwable e) {
                    onError.call(e);
                }

                public final void onSuccess(T args) {
                    onSuccess.call(args);
                }
            });
        } else {
            throw new IllegalArgumentException("onError can not be null");
        }
    }

    public final Subscription unsafeSubscribe(Subscriber<? super T> subscriber) {
        return unsafeSubscribe(subscriber, true);
    }

    private Subscription unsafeSubscribe(Subscriber<? super T> subscriber, boolean start) {
        if (start) {
            try {
                subscriber.onStart();
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                StringBuilder sb = new StringBuilder();
                sb.append("Error occurred attempting to subscribe [");
                sb.append(e.getMessage());
                sb.append("] and then again while trying to pass to onError.");
                RuntimeException r = new RuntimeException(sb.toString(), e2);
                RxJavaHooks.onSingleError(r);
                throw r;
            }
        }
        RxJavaHooks.onSingleStart(this, this.onSubscribe).call(SingleLiftObservableOperator.wrap(subscriber));
        return RxJavaHooks.onSingleReturn(subscriber);
    }

    public final Subscription subscribe(final Observer<? super T> observer) {
        if (observer != null) {
            return subscribe((SingleSubscriber<? super T>) new SingleSubscriber<T>() {
                public void onSuccess(T value) {
                    observer.onNext(value);
                    observer.onCompleted();
                }

                public void onError(Throwable error) {
                    observer.onError(error);
                }
            });
        }
        throw new NullPointerException("observer is null");
    }

    public final Subscription subscribe(Subscriber<? super T> subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("observer can not be null");
        }
        subscriber.onStart();
        if (!(subscriber instanceof SafeSubscriber)) {
            return unsafeSubscribe(new SafeSubscriber(subscriber), false);
        }
        return unsafeSubscribe(subscriber, true);
    }

    public final Subscription subscribe(SingleSubscriber<? super T> te) {
        if (te == null) {
            throw new IllegalArgumentException("te is null");
        }
        try {
            RxJavaHooks.onSingleStart(this, this.onSubscribe).call(te);
            return RxJavaHooks.onSingleReturn(te);
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            StringBuilder sb = new StringBuilder();
            sb.append("Error occurred attempting to subscribe [");
            sb.append(ex.getMessage());
            sb.append("] and then again while trying to pass to onError.");
            RuntimeException r = new RuntimeException(sb.toString(), e2);
            RxJavaHooks.onSingleError(r);
            throw r;
        }
    }

    public final Single<T> subscribeOn(final Scheduler scheduler) {
        if (this instanceof ScalarSynchronousSingle) {
            return ((ScalarSynchronousSingle) this).scalarScheduleOn(scheduler);
        }
        return create(new OnSubscribe<T>() {
            public void call(final SingleSubscriber<? super T> t) {
                final Worker w = scheduler.createWorker();
                t.add(w);
                w.schedule(new Action0() {
                    public void call() {
                        SingleSubscriber<T> single = new SingleSubscriber<T>() {
                            public void onSuccess(T value) {
                                try {
                                    t.onSuccess(value);
                                } finally {
                                    w.unsubscribe();
                                }
                            }

                            public void onError(Throwable error) {
                                try {
                                    t.onError(error);
                                } finally {
                                    w.unsubscribe();
                                }
                            }
                        };
                        t.add(single);
                        Single.this.subscribe(single);
                    }
                });
            }
        });
    }

    public final Single<T> takeUntil(Completable other) {
        return create(new SingleTakeUntilCompletable(this.onSubscribe, other));
    }

    public final <E> Single<T> takeUntil(Observable<? extends E> other) {
        return create(new SingleTakeUntilObservable(this.onSubscribe, other));
    }

    public final <E> Single<T> takeUntil(Single<? extends E> other) {
        return create(new SingleTakeUntilSingle(this.onSubscribe, other));
    }

    @Experimental
    /* renamed from: to */
    public final <R> R mo25323to(Func1<? super Single<T>, R> converter) {
        return converter.call(this);
    }

    public final Observable<T> toObservable() {
        return asObservable(this);
    }

    @Beta
    public final Completable toCompletable() {
        return Completable.fromSingle(this);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit) {
        return timeout(timeout, timeUnit, null, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout(timeout, timeUnit, null, scheduler);
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other) {
        return timeout(timeout, timeUnit, other, Schedulers.computation());
    }

    public final Single<T> timeout(long timeout, TimeUnit timeUnit, Single<? extends T> other, Scheduler scheduler) {
        if (other == null) {
            other = error(new TimeoutException());
        }
        SingleTimeout singleTimeout = new SingleTimeout(this.onSubscribe, timeout, timeUnit, scheduler, other.onSubscribe);
        return create(singleTimeout);
    }

    @Beta
    public final BlockingSingle<T> toBlocking() {
        return BlockingSingle.from(this);
    }

    public final <T2, R> Single<R> zipWith(Single<? extends T2> other, Func2<? super T, ? super T2, ? extends R> zipFunction) {
        return zip(this, other, zipFunction);
    }

    @Beta
    public final Single<T> doOnError(final Action1<Throwable> onError) {
        if (onError != null) {
            return create(new SingleDoOnEvent(this, Actions.empty(), new Action1<Throwable>() {
                public void call(Throwable throwable) {
                    onError.call(throwable);
                }
            }));
        }
        throw new IllegalArgumentException("onError is null");
    }

    @Experimental
    public final Single<T> doOnEach(final Action1<Notification<? extends T>> onNotification) {
        if (onNotification != null) {
            return create(new SingleDoOnEvent(this, new Action1<T>() {
                public void call(T t) {
                    onNotification.call(Notification.createOnNext(t));
                }
            }, new Action1<Throwable>() {
                public void call(Throwable throwable) {
                    onNotification.call(Notification.createOnError(throwable));
                }
            }));
        }
        throw new IllegalArgumentException("onNotification is null");
    }

    @Experimental
    public final Single<T> doOnSuccess(Action1<? super T> onSuccess) {
        if (onSuccess != null) {
            return create(new SingleDoOnEvent(this, onSuccess, Actions.empty()));
        }
        throw new IllegalArgumentException("onSuccess is null");
    }

    @Beta
    public final Single<T> doOnSubscribe(Action0 subscribe) {
        return create(new SingleDoOnSubscribe(this.onSubscribe, subscribe));
    }

    @Beta
    public final Single<T> delay(long delay, TimeUnit unit, Scheduler scheduler) {
        SingleDelay singleDelay = new SingleDelay(this.onSubscribe, delay, unit, scheduler);
        return create(singleDelay);
    }

    @Beta
    public final Single<T> delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation());
    }

    @Beta
    public static <T> Single<T> defer(final Callable<Single<T>> singleFactory) {
        return create(new OnSubscribe<T>() {
            public void call(SingleSubscriber<? super T> singleSubscriber) {
                try {
                    ((Single) singleFactory.call()).subscribe(singleSubscriber);
                } catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    singleSubscriber.onError(t);
                }
            }
        });
    }

    @Beta
    public final Single<T> doOnUnsubscribe(Action0 action) {
        return create(new SingleDoOnUnsubscribe(this.onSubscribe, action));
    }

    @Beta
    public final Single<T> doAfterTerminate(Action0 action) {
        return create(new SingleDoAfterTerminate(this, action));
    }

    static <T> Single<? extends T>[] iterableToArray(Iterable<? extends Single<? extends T>> singlesIterable) {
        if (singlesIterable instanceof Collection) {
            Collection<? extends Single<? extends T>> list = (Collection) singlesIterable;
            return (Single[]) list.toArray(new Single[list.size()]);
        }
        Single<? extends T>[] tempArray = new Single[8];
        int count = 0;
        for (Single<? extends T> s : singlesIterable) {
            if (count == tempArray.length) {
                Single<? extends T>[] sb = new Single[((count >> 2) + count)];
                System.arraycopy(tempArray, 0, sb, 0, count);
                tempArray = sb;
            }
            tempArray[count] = s;
            count++;
        }
        if (tempArray.length == count) {
            return tempArray;
        }
        Single<? extends T>[] singlesArray = new Single[count];
        System.arraycopy(tempArray, 0, singlesArray, 0, count);
        return singlesArray;
    }

    public final Single<T> retry() {
        return toObservable().retry().toSingle();
    }

    public final Single<T> retry(long count) {
        return toObservable().retry(count).toSingle();
    }

    public final Single<T> retry(Func2<Integer, Throwable, Boolean> predicate) {
        return toObservable().retry(predicate).toSingle();
    }

    public final Single<T> retryWhen(Func1<Observable<? extends Throwable>, ? extends Observable<?>> notificationHandler) {
        return toObservable().retryWhen(notificationHandler).toSingle();
    }

    @Beta
    public static <T, Resource> Single<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Single<? extends T>> singleFactory, Action1<? super Resource> disposeAction) {
        return using(resourceFactory, singleFactory, disposeAction, false);
    }

    @Beta
    public static <T, Resource> Single<T> using(Func0<Resource> resourceFactory, Func1<? super Resource, ? extends Single<? extends T>> singleFactory, Action1<? super Resource> disposeAction, boolean disposeEagerly) {
        if (resourceFactory == null) {
            throw new NullPointerException("resourceFactory is null");
        } else if (singleFactory == null) {
            throw new NullPointerException("singleFactory is null");
        } else if (disposeAction != null) {
            return create(new SingleOnSubscribeUsing(resourceFactory, singleFactory, disposeAction, disposeEagerly));
        } else {
            throw new NullPointerException("disposeAction is null");
        }
    }

    @Beta
    public final Single<T> delaySubscription(Observable<?> other) {
        if (other != null) {
            return create(new SingleOnSubscribeDelaySubscriptionOther(this, other));
        }
        throw new NullPointerException();
    }
}
