package p008rx;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Scheduler.Worker;
import p008rx.annotations.Beta;
import p008rx.annotations.Experimental;
import p008rx.exceptions.CompositeException;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Actions;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.functions.Func2;
import p008rx.internal.operators.CompletableFromEmitter;
import p008rx.internal.operators.CompletableOnSubscribeConcat;
import p008rx.internal.operators.CompletableOnSubscribeConcatArray;
import p008rx.internal.operators.CompletableOnSubscribeConcatIterable;
import p008rx.internal.operators.CompletableOnSubscribeMerge;
import p008rx.internal.operators.CompletableOnSubscribeMergeArray;
import p008rx.internal.operators.CompletableOnSubscribeMergeDelayErrorArray;
import p008rx.internal.operators.CompletableOnSubscribeMergeDelayErrorIterable;
import p008rx.internal.operators.CompletableOnSubscribeMergeIterable;
import p008rx.internal.operators.CompletableOnSubscribeTimeout;
import p008rx.internal.util.SubscriptionList;
import p008rx.internal.util.UtilityFunctions;
import p008rx.observers.SafeCompletableSubscriber;
import p008rx.observers.SafeSubscriber;
import p008rx.plugins.RxJavaHooks;
import p008rx.schedulers.Schedulers;
import p008rx.subscriptions.BooleanSubscription;
import p008rx.subscriptions.CompositeSubscription;
import p008rx.subscriptions.MultipleAssignmentSubscription;
import p008rx.subscriptions.SerialSubscription;
import p008rx.subscriptions.Subscriptions;

@Beta
/* renamed from: rx.Completable */
public class Completable {
    static final Completable COMPLETE = new Completable(new OnSubscribe() {
        public void call(CompletableSubscriber s) {
            s.onSubscribe(Subscriptions.unsubscribed());
            s.onCompleted();
        }
    }, false);
    static final Completable NEVER = new Completable(new OnSubscribe() {
        public void call(CompletableSubscriber s) {
            s.onSubscribe(Subscriptions.unsubscribed());
        }
    }, false);
    private final OnSubscribe onSubscribe;

    /* renamed from: rx.Completable$OnSubscribe */
    public interface OnSubscribe extends Action1<CompletableSubscriber> {
    }

    /* renamed from: rx.Completable$Operator */
    public interface Operator extends Func1<CompletableSubscriber, CompletableSubscriber> {
    }

    /* renamed from: rx.Completable$Transformer */
    public interface Transformer extends Func1<Completable, Completable> {
    }

    public static Completable amb(final Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                s.onSubscribe(set);
                final AtomicBoolean once = new AtomicBoolean();
                CompletableSubscriber inner = new CompletableSubscriber() {
                    public void onCompleted() {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onCompleted();
                        }
                    }

                    public void onError(Throwable e) {
                        if (once.compareAndSet(false, true)) {
                            set.unsubscribe();
                            s.onError(e);
                            return;
                        }
                        RxJavaHooks.onError(e);
                    }

                    public void onSubscribe(Subscription d) {
                        set.add(d);
                    }
                };
                Completable[] arr$ = sources;
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    Completable c = arr$[i$];
                    if (!set.isUnsubscribed()) {
                        if (c == null) {
                            NullPointerException npe = new NullPointerException("One of the sources is null");
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onError(npe);
                            } else {
                                RxJavaHooks.onError(npe);
                            }
                            return;
                        } else if (!once.get() && !set.isUnsubscribed()) {
                            c.unsafeSubscribe(inner);
                            i$++;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        });
    }

    public static Completable amb(final Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                s.onSubscribe(set);
                try {
                    Iterator<? extends Completable> it = sources.iterator();
                    if (it == null) {
                        s.onError(new NullPointerException("The iterator returned is null"));
                        return;
                    }
                    boolean empty = true;
                    final AtomicBoolean once = new AtomicBoolean();
                    CompletableSubscriber inner = new CompletableSubscriber() {
                        public void onCompleted() {
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onCompleted();
                            }
                        }

                        public void onError(Throwable e) {
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onError(e);
                                return;
                            }
                            RxJavaHooks.onError(e);
                        }

                        public void onSubscribe(Subscription d) {
                            set.add(d);
                        }
                    };
                    while (!once.get() && !set.isUnsubscribed()) {
                        try {
                            if (!it.hasNext()) {
                                if (empty) {
                                    s.onCompleted();
                                }
                                return;
                            }
                            empty = false;
                            if (!once.get() && !set.isUnsubscribed()) {
                                try {
                                    Completable c = (Completable) it.next();
                                    if (c == null) {
                                        NullPointerException npe = new NullPointerException("One of the sources is null");
                                        if (once.compareAndSet(false, true)) {
                                            set.unsubscribe();
                                            s.onError(npe);
                                        } else {
                                            RxJavaHooks.onError(npe);
                                        }
                                        return;
                                    } else if (!once.get() && !set.isUnsubscribed()) {
                                        c.unsafeSubscribe(inner);
                                    } else {
                                        return;
                                    }
                                } catch (Throwable e) {
                                    if (once.compareAndSet(false, true)) {
                                        set.unsubscribe();
                                        s.onError(e);
                                    } else {
                                        RxJavaHooks.onError(e);
                                    }
                                    return;
                                }
                            } else {
                                return;
                            }
                        } catch (Throwable e2) {
                            if (once.compareAndSet(false, true)) {
                                set.unsubscribe();
                                s.onError(e2);
                            } else {
                                RxJavaHooks.onError(e2);
                            }
                            return;
                        }
                    }
                } catch (Throwable e3) {
                    s.onError(e3);
                }
            }
        });
    }

    public static Completable complete() {
        OnSubscribe cos = RxJavaHooks.onCreate(COMPLETE.onSubscribe);
        if (cos == COMPLETE.onSubscribe) {
            return COMPLETE;
        }
        return new Completable(cos, false);
    }

    public static Completable concat(Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new CompletableOnSubscribeConcatArray(sources));
    }

    public static Completable concat(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeConcatIterable(sources));
    }

    public static Completable concat(Observable<? extends Completable> sources) {
        return concat(sources, 2);
    }

    public static Completable concat(Observable<? extends Completable> sources, int prefetch) {
        requireNonNull(sources);
        if (prefetch >= 1) {
            return create(new CompletableOnSubscribeConcat(sources, prefetch));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("prefetch > 0 required but it was ");
        sb.append(prefetch);
        throw new IllegalArgumentException(sb.toString());
    }

    public static Completable create(OnSubscribe onSubscribe2) {
        requireNonNull(onSubscribe2);
        try {
            return new Completable(onSubscribe2);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            RxJavaHooks.onError(ex2);
            throw toNpe(ex2);
        }
    }

    public static Completable defer(final Func0<? extends Completable> completableFunc0) {
        requireNonNull(completableFunc0);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                try {
                    Completable c = (Completable) completableFunc0.call();
                    if (c == null) {
                        s.onSubscribe(Subscriptions.unsubscribed());
                        s.onError(new NullPointerException("The completable returned is null"));
                        return;
                    }
                    c.unsafeSubscribe(s);
                } catch (Throwable e) {
                    s.onSubscribe(Subscriptions.unsubscribed());
                    s.onError(e);
                }
            }
        });
    }

    public static Completable error(final Func0<? extends Throwable> errorFunc0) {
        requireNonNull(errorFunc0);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                s.onSubscribe(Subscriptions.unsubscribed());
                try {
                    e = (Throwable) errorFunc0.call();
                } catch (Throwable th) {
                    e = th;
                }
                if (e == null) {
                    e = new NullPointerException("The error supplied is null");
                }
                s.onError(e);
            }
        });
    }

    public static Completable error(final Throwable error) {
        requireNonNull(error);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                s.onSubscribe(Subscriptions.unsubscribed());
                s.onError(error);
            }
        });
    }

    public static Completable fromAction(final Action0 action) {
        requireNonNull(action);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                BooleanSubscription bs = new BooleanSubscription();
                s.onSubscribe(bs);
                try {
                    action.call();
                    if (!bs.isUnsubscribed()) {
                        s.onCompleted();
                    }
                } catch (Throwable e) {
                    if (!bs.isUnsubscribed()) {
                        s.onError(e);
                    }
                }
            }
        });
    }

    public static Completable fromCallable(final Callable<?> callable) {
        requireNonNull(callable);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                BooleanSubscription bs = new BooleanSubscription();
                s.onSubscribe(bs);
                try {
                    callable.call();
                    if (!bs.isUnsubscribed()) {
                        s.onCompleted();
                    }
                } catch (Throwable e) {
                    if (!bs.isUnsubscribed()) {
                        s.onError(e);
                    }
                }
            }
        });
    }

    @Experimental
    public static Completable fromEmitter(Action1<CompletableEmitter> producer) {
        return create(new CompletableFromEmitter(producer));
    }

    public static Completable fromFuture(Future<?> future) {
        requireNonNull(future);
        return fromObservable(Observable.from(future));
    }

    public static Completable fromObservable(final Observable<?> flowable) {
        requireNonNull(flowable);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber cs) {
                Subscriber<Object> subscriber = new Subscriber<Object>() {
                    public void onCompleted() {
                        cs.onCompleted();
                    }

                    public void onError(Throwable t) {
                        cs.onError(t);
                    }

                    public void onNext(Object t) {
                    }
                };
                cs.onSubscribe(subscriber);
                flowable.unsafeSubscribe(subscriber);
            }
        });
    }

    public static Completable fromSingle(final Single<?> single) {
        requireNonNull(single);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                SingleSubscriber<Object> te = new SingleSubscriber<Object>() {
                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSuccess(Object value) {
                        s.onCompleted();
                    }
                };
                s.onSubscribe(te);
                single.subscribe(te);
            }
        });
    }

    public static Completable merge(Completable... sources) {
        requireNonNull(sources);
        if (sources.length == 0) {
            return complete();
        }
        if (sources.length == 1) {
            return sources[0];
        }
        return create(new CompletableOnSubscribeMergeArray(sources));
    }

    public static Completable merge(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeIterable(sources));
    }

    public static Completable merge(Observable<? extends Completable> sources) {
        return merge0(sources, Integer.MAX_VALUE, false);
    }

    public static Completable merge(Observable<? extends Completable> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, false);
    }

    protected static Completable merge0(Observable<? extends Completable> sources, int maxConcurrency, boolean delayErrors) {
        requireNonNull(sources);
        if (maxConcurrency >= 1) {
            return create(new CompletableOnSubscribeMerge(sources, maxConcurrency, delayErrors));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("maxConcurrency > 0 required but it was ");
        sb.append(maxConcurrency);
        throw new IllegalArgumentException(sb.toString());
    }

    public static Completable mergeDelayError(Completable... sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeDelayErrorArray(sources));
    }

    public static Completable mergeDelayError(Iterable<? extends Completable> sources) {
        requireNonNull(sources);
        return create(new CompletableOnSubscribeMergeDelayErrorIterable(sources));
    }

    public static Completable mergeDelayError(Observable<? extends Completable> sources) {
        return merge0(sources, Integer.MAX_VALUE, true);
    }

    public static Completable mergeDelayError(Observable<? extends Completable> sources, int maxConcurrency) {
        return merge0(sources, maxConcurrency, true);
    }

    public static Completable never() {
        OnSubscribe cos = RxJavaHooks.onCreate(NEVER.onSubscribe);
        if (cos == NEVER.onSubscribe) {
            return NEVER;
        }
        return new Completable(cos, false);
    }

    static <T> T requireNonNull(T o) {
        if (o != null) {
            return o;
        }
        throw new NullPointerException();
    }

    public static Completable timer(long delay, TimeUnit unit) {
        return timer(delay, unit, Schedulers.computation());
    }

    public static Completable timer(final long delay, final TimeUnit unit, final Scheduler scheduler) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
                s.onSubscribe(mad);
                if (!mad.isUnsubscribed()) {
                    final Worker w = scheduler.createWorker();
                    mad.set(w);
                    w.schedule(new Action0() {
                        public void call() {
                            try {
                                s.onCompleted();
                            } finally {
                                w.unsubscribe();
                            }
                        }
                    }, delay, unit);
                }
            }
        });
    }

    static NullPointerException toNpe(Throwable ex) {
        NullPointerException npe = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        npe.initCause(ex);
        return npe;
    }

    public static <R> Completable using(Func0<R> resourceFunc0, Func1<? super R, ? extends Completable> completableFunc1, Action1<? super R> disposer) {
        return using(resourceFunc0, completableFunc1, disposer, true);
    }

    public static <R> Completable using(final Func0<R> resourceFunc0, final Func1<? super R, ? extends Completable> completableFunc1, final Action1<? super R> disposer, final boolean eager) {
        requireNonNull(resourceFunc0);
        requireNonNull(completableFunc1);
        requireNonNull(disposer);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                try {
                    final R resource = resourceFunc0.call();
                    try {
                        Completable cs = (Completable) completableFunc1.call(resource);
                        if (cs == null) {
                            try {
                                disposer.call(resource);
                                s.onSubscribe(Subscriptions.unsubscribed());
                                s.onError(new NullPointerException("The completable supplied is null"));
                            } catch (Throwable ex) {
                                Exceptions.throwIfFatal(ex);
                                s.onSubscribe(Subscriptions.unsubscribed());
                                s.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{new NullPointerException("The completable supplied is null"), ex})));
                            }
                        } else {
                            final AtomicBoolean once = new AtomicBoolean();
                            cs.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {

                                /* renamed from: d */
                                Subscription f893d;

                                /* access modifiers changed from: 0000 */
                                public void dispose() {
                                    this.f893d.unsubscribe();
                                    if (once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            RxJavaHooks.onError(ex);
                                        }
                                    }
                                }

                                public void onCompleted() {
                                    if (eager && once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            s.onError(ex);
                                            return;
                                        }
                                    }
                                    s.onCompleted();
                                    if (!eager) {
                                        dispose();
                                    }
                                }

                                public void onError(Throwable e) {
                                    if (eager && once.compareAndSet(false, true)) {
                                        try {
                                            disposer.call(resource);
                                        } catch (Throwable ex) {
                                            e = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, ex}));
                                        }
                                    }
                                    s.onError(e);
                                    if (!eager) {
                                        dispose();
                                    }
                                }

                                public void onSubscribe(Subscription d) {
                                    this.f893d = d;
                                    s.onSubscribe(Subscriptions.create(new Action0() {
                                        public void call() {
                                            C14321.this.dispose();
                                        }
                                    }));
                                }
                            });
                        }
                    } catch (Throwable ex2) {
                        Exceptions.throwIfFatal(e);
                        Exceptions.throwIfFatal(ex2);
                        s.onSubscribe(Subscriptions.unsubscribed());
                        s.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, ex2})));
                    }
                } catch (Throwable e) {
                    s.onSubscribe(Subscriptions.unsubscribed());
                    s.onError(e);
                }
            }
        });
    }

    protected Completable(OnSubscribe onSubscribe2) {
        this.onSubscribe = RxJavaHooks.onCreate(onSubscribe2);
    }

    protected Completable(OnSubscribe onSubscribe2, boolean useHook) {
        this.onSubscribe = useHook ? RxJavaHooks.onCreate(onSubscribe2) : onSubscribe2;
    }

    public final Completable ambWith(Completable other) {
        requireNonNull(other);
        return amb(this, other);
    }

    public final void await() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            if (err[0] != null) {
                Exceptions.propagate(err[0]);
            }
            return;
        }
        try {
            cdl.await();
            if (err[0] != null) {
                Exceptions.propagate(err[0]);
            }
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final boolean await(long timeout, TimeUnit unit) {
        requireNonNull(unit);
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            if (err[0] != null) {
                Exceptions.propagate(err[0]);
            }
            return true;
        }
        try {
            boolean b = cdl.await(timeout, unit);
            if (b && err[0] != null) {
                Exceptions.propagate(err[0]);
            }
            return b;
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final Completable compose(Transformer transformer) {
        return (Completable) mo24947to(transformer);
    }

    public final <T> Observable<T> andThen(Observable<T> next) {
        requireNonNull(next);
        return next.delaySubscription(toObservable());
    }

    public final <T> Single<T> andThen(Single<T> next) {
        requireNonNull(next);
        return next.delaySubscription(toObservable());
    }

    public final Completable andThen(Completable next) {
        return concatWith(next);
    }

    public final Completable concatWith(Completable other) {
        requireNonNull(other);
        return concat(this, other);
    }

    public final Completable delay(long delay, TimeUnit unit) {
        return delay(delay, unit, Schedulers.computation(), false);
    }

    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler) {
        return delay(delay, unit, scheduler, false);
    }

    public final Completable delay(long delay, TimeUnit unit, Scheduler scheduler, boolean delayError) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        final Scheduler scheduler2 = scheduler;
        final long j = delay;
        final TimeUnit timeUnit = unit;
        final boolean z = delayError;
        C143616 r0 = new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final CompositeSubscription set = new CompositeSubscription();
                final Worker w = scheduler2.createWorker();
                set.add(w);
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        set.add(w.schedule(new Action0() {
                            public void call() {
                                try {
                                    s.onCompleted();
                                } finally {
                                    w.unsubscribe();
                                }
                            }
                        }, j, timeUnit));
                    }

                    public void onError(final Throwable e) {
                        if (z) {
                            set.add(w.schedule(new Action0() {
                                public void call() {
                                    try {
                                        s.onError(e);
                                    } finally {
                                        w.unsubscribe();
                                    }
                                }
                            }, j, timeUnit));
                        } else {
                            s.onError(e);
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        set.add(d);
                        s.onSubscribe(set);
                    }
                });
            }
        };
        return create(r0);
    }

    public final Completable doOnCompleted(Action0 onCompleted) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), onCompleted, Actions.empty(), Actions.empty());
    }

    public final Completable doOnEach(final Action1<Notification<Object>> onNotification) {
        if (onNotification == null) {
            throw new IllegalArgumentException("onNotification is null");
        }
        return doOnLifecycle(Actions.empty(), new Action1<Throwable>() {
            public void call(Throwable throwable) {
                onNotification.call(Notification.createOnError(throwable));
            }
        }, new Action0() {
            public void call() {
                onNotification.call(Notification.createOnCompleted());
            }
        }, Actions.empty(), Actions.empty());
    }

    public final Completable doOnUnsubscribe(Action0 onUnsubscribe) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty(), onUnsubscribe);
    }

    public final Completable doOnError(Action1<? super Throwable> onError) {
        return doOnLifecycle(Actions.empty(), onError, Actions.empty(), Actions.empty(), Actions.empty());
    }

    /* access modifiers changed from: protected */
    public final Completable doOnLifecycle(Action1<? super Subscription> onSubscribe2, Action1<? super Throwable> onError, Action0 onComplete, Action0 onAfterComplete, Action0 onUnsubscribe) {
        requireNonNull(onSubscribe2);
        requireNonNull(onError);
        requireNonNull(onComplete);
        requireNonNull(onAfterComplete);
        requireNonNull(onUnsubscribe);
        final Action0 action0 = onComplete;
        final Action0 action02 = onAfterComplete;
        final Action1<? super Throwable> action1 = onError;
        final Action1<? super Subscription> action12 = onSubscribe2;
        final Action0 action03 = onUnsubscribe;
        C144219 r0 = new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        try {
                            action0.call();
                            s.onCompleted();
                            try {
                                action02.call();
                            } catch (Throwable e) {
                                RxJavaHooks.onError(e);
                            }
                        } catch (Throwable e2) {
                            s.onError(e2);
                        }
                    }

                    public void onError(Throwable e) {
                        try {
                            action1.call(e);
                        } catch (Throwable ex) {
                            e = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, ex}));
                        }
                        s.onError(e);
                    }

                    public void onSubscribe(final Subscription d) {
                        try {
                            action12.call(d);
                            s.onSubscribe(Subscriptions.create(new Action0() {
                                public void call() {
                                    try {
                                        action03.call();
                                    } catch (Throwable e) {
                                        RxJavaHooks.onError(e);
                                    }
                                    d.unsubscribe();
                                }
                            }));
                        } catch (Throwable ex) {
                            d.unsubscribe();
                            s.onSubscribe(Subscriptions.unsubscribed());
                            s.onError(ex);
                        }
                    }
                });
            }
        };
        return create(r0);
    }

    public final Completable doOnSubscribe(Action1<? super Subscription> onSubscribe2) {
        return doOnLifecycle(onSubscribe2, Actions.empty(), Actions.empty(), Actions.empty(), Actions.empty());
    }

    public final Completable doOnTerminate(final Action0 onTerminate) {
        return doOnLifecycle(Actions.empty(), new Action1<Throwable>() {
            public void call(Throwable e) {
                onTerminate.call();
            }
        }, onTerminate, Actions.empty(), Actions.empty());
    }

    public final Completable doAfterTerminate(Action0 onAfterComplete) {
        return doOnLifecycle(Actions.empty(), Actions.empty(), Actions.empty(), onAfterComplete, Actions.empty());
    }

    public final Throwable get() {
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            return err[0];
        }
        try {
            cdl.await();
            return err[0];
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final Throwable get(long timeout, TimeUnit unit) {
        requireNonNull(unit);
        final CountDownLatch cdl = new CountDownLatch(1);
        final Throwable[] err = new Throwable[1];
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                cdl.countDown();
            }

            public void onError(Throwable e) {
                err[0] = e;
                cdl.countDown();
            }

            public void onSubscribe(Subscription d) {
            }
        });
        if (cdl.getCount() == 0) {
            return err[0];
        }
        try {
            if (cdl.await(timeout, unit)) {
                return err[0];
            }
            Exceptions.propagate(new TimeoutException());
            return null;
        } catch (InterruptedException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    public final Completable lift(final Operator onLift) {
        requireNonNull(onLift);
        return create(new OnSubscribe() {
            public void call(CompletableSubscriber s) {
                try {
                    Completable.this.unsafeSubscribe((CompletableSubscriber) RxJavaHooks.onCompletableLift(onLift).call(s));
                } catch (NullPointerException ex) {
                    throw ex;
                } catch (Throwable ex2) {
                    throw Completable.toNpe(ex2);
                }
            }
        });
    }

    public final Completable mergeWith(Completable other) {
        requireNonNull(other);
        return merge(this, other);
    }

    public final Completable observeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final SubscriptionList ad = new SubscriptionList();
                final Worker w = scheduler.createWorker();
                ad.add(w);
                s.onSubscribe(ad);
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        w.schedule(new Action0() {
                            public void call() {
                                try {
                                    s.onCompleted();
                                } finally {
                                    ad.unsubscribe();
                                }
                            }
                        });
                    }

                    public void onError(final Throwable e) {
                        w.schedule(new Action0() {
                            public void call() {
                                try {
                                    s.onError(e);
                                } finally {
                                    ad.unsubscribe();
                                }
                            }
                        });
                    }

                    public void onSubscribe(Subscription d) {
                        ad.add(d);
                    }
                });
            }
        });
    }

    public final Completable onErrorComplete() {
        return onErrorComplete(UtilityFunctions.alwaysTrue());
    }

    public final Completable onErrorComplete(final Func1<? super Throwable, Boolean> predicate) {
        requireNonNull(predicate);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        boolean b;
                        try {
                            b = ((Boolean) predicate.call(e)).booleanValue();
                        } catch (Throwable ex) {
                            Exceptions.throwIfFatal(ex);
                            e = new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, ex}));
                            b = false;
                        }
                        if (b) {
                            s.onCompleted();
                        } else {
                            s.onError(e);
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        s.onSubscribe(d);
                    }
                });
            }
        });
    }

    public final Completable onErrorResumeNext(final Func1<? super Throwable, ? extends Completable> errorMapper) {
        requireNonNull(errorMapper);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final SerialSubscription sd = new SerialSubscription();
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        try {
                            Completable c = (Completable) errorMapper.call(e);
                            if (c == null) {
                                s.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, new NullPointerException("The completable returned is null")})));
                                return;
                            }
                            c.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                                public void onCompleted() {
                                    s.onCompleted();
                                }

                                public void onError(Throwable e) {
                                    s.onError(e);
                                }

                                public void onSubscribe(Subscription d) {
                                    sd.set(d);
                                }
                            });
                        } catch (Throwable ex) {
                            s.onError(new CompositeException((Collection<? extends Throwable>) Arrays.asList(new Throwable[]{e, ex})));
                        }
                    }

                    public void onSubscribe(Subscription d) {
                        sd.set(d);
                    }
                });
            }
        });
    }

    public final Completable repeat() {
        return fromObservable(toObservable().repeat());
    }

    public final Completable repeat(long times) {
        return fromObservable(toObservable().repeat(times));
    }

    public final Completable repeatWhen(Func1<? super Observable<? extends Void>, ? extends Observable<?>> handler) {
        requireNonNull(handler);
        return fromObservable(toObservable().repeatWhen(handler));
    }

    public final Completable retry() {
        return fromObservable(toObservable().retry());
    }

    public final Completable retry(Func2<Integer, Throwable, Boolean> predicate) {
        return fromObservable(toObservable().retry(predicate));
    }

    public final Completable retry(long times) {
        return fromObservable(toObservable().retry(times));
    }

    public final Completable retryWhen(Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> handler) {
        return fromObservable(toObservable().retryWhen(handler));
    }

    public final Completable startWith(Completable other) {
        requireNonNull(other);
        return concat(other, this);
    }

    public final <T> Observable<T> startWith(Observable<T> other) {
        requireNonNull(other);
        return toObservable().startWith(other);
    }

    public final Subscription subscribe() {
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                mad.unsubscribe();
            }

            public void onError(Throwable e) {
                RxJavaHooks.onError(e);
                mad.unsubscribe();
                Completable.deliverUncaughtException(e);
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    public final Subscription subscribe(final Action0 onComplete) {
        requireNonNull(onComplete);
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            boolean done;

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    try {
                        onComplete.call();
                    } catch (Throwable th) {
                        mad.unsubscribe();
                        throw th;
                    }
                    mad.unsubscribe();
                }
            }

            public void onError(Throwable e) {
                RxJavaHooks.onError(e);
                mad.unsubscribe();
                Completable.deliverUncaughtException(e);
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    public final Subscription subscribe(final Action0 onComplete, final Action1<? super Throwable> onError) {
        requireNonNull(onComplete);
        requireNonNull(onError);
        final MultipleAssignmentSubscription mad = new MultipleAssignmentSubscription();
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            boolean done;

            public void onCompleted() {
                if (!this.done) {
                    this.done = true;
                    try {
                        onComplete.call();
                        mad.unsubscribe();
                    } catch (Throwable e) {
                        callOnError(e);
                    }
                }
            }

            public void onError(Throwable e) {
                if (!this.done) {
                    this.done = true;
                    callOnError(e);
                    return;
                }
                RxJavaHooks.onError(e);
                Completable.deliverUncaughtException(e);
            }

            /* access modifiers changed from: 0000 */
            public void callOnError(Throwable e) {
                try {
                    onError.call(e);
                } catch (Throwable th) {
                    mad.unsubscribe();
                    throw th;
                }
                mad.unsubscribe();
            }

            public void onSubscribe(Subscription d) {
                mad.set(d);
            }
        });
        return mad;
    }

    static void deliverUncaughtException(Throwable e) {
        Thread thread = Thread.currentThread();
        thread.getUncaughtExceptionHandler().uncaughtException(thread, e);
    }

    public final void unsafeSubscribe(CompletableSubscriber s) {
        requireNonNull(s);
        try {
            RxJavaHooks.onCompletableStart(this, this.onSubscribe).call(s);
        } catch (NullPointerException ex) {
            throw ex;
        } catch (Throwable ex2) {
            Exceptions.throwIfFatal(ex2);
            Throwable ex3 = RxJavaHooks.onCompletableError(ex2);
            RxJavaHooks.onError(ex3);
            throw toNpe(ex3);
        }
    }

    public final void subscribe(CompletableSubscriber s) {
        if (!(s instanceof SafeCompletableSubscriber)) {
            s = new SafeCompletableSubscriber(s);
        }
        unsafeSubscribe(s);
    }

    public final <T> void unsafeSubscribe(Subscriber<T> s) {
        unsafeSubscribe(s, true);
    }

    private <T> void unsafeSubscribe(final Subscriber<T> s, boolean callOnStart) {
        requireNonNull(s);
        if (callOnStart) {
            try {
                s.onStart();
            } catch (NullPointerException ex) {
                throw ex;
            } catch (Throwable ex2) {
                Exceptions.throwIfFatal(ex2);
                Throwable ex3 = RxJavaHooks.onObservableError(ex2);
                RxJavaHooks.onError(ex3);
                throw toNpe(ex3);
            }
        }
        unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
            public void onCompleted() {
                s.onCompleted();
            }

            public void onError(Throwable e) {
                s.onError(e);
            }

            public void onSubscribe(Subscription d) {
                s.add(d);
            }
        });
        RxJavaHooks.onObservableReturn(s);
    }

    public final <T> void subscribe(Subscriber<T> s) {
        s.onStart();
        if (!(s instanceof SafeSubscriber)) {
            s = new SafeSubscriber<>(s);
        }
        unsafeSubscribe(s, false);
    }

    public final Completable subscribeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                final Worker w = scheduler.createWorker();
                w.schedule(new Action0() {
                    public void call() {
                        try {
                            Completable.this.unsafeSubscribe(s);
                        } finally {
                            w.unsubscribe();
                        }
                    }
                });
            }
        });
    }

    public final Completable timeout(long timeout, TimeUnit unit) {
        return timeout0(timeout, unit, Schedulers.computation(), null);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Completable other) {
        requireNonNull(other);
        return timeout0(timeout, unit, Schedulers.computation(), other);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler) {
        return timeout0(timeout, unit, scheduler, null);
    }

    public final Completable timeout(long timeout, TimeUnit unit, Scheduler scheduler, Completable other) {
        requireNonNull(other);
        return timeout0(timeout, unit, scheduler, other);
    }

    public final Completable timeout0(long timeout, TimeUnit unit, Scheduler scheduler, Completable other) {
        requireNonNull(unit);
        requireNonNull(scheduler);
        CompletableOnSubscribeTimeout completableOnSubscribeTimeout = new CompletableOnSubscribeTimeout(this, timeout, unit, scheduler, other);
        return create(completableOnSubscribeTimeout);
    }

    /* renamed from: to */
    public final <R> R mo24947to(Func1<? super Completable, R> converter) {
        return converter.call(this);
    }

    public final <T> Observable<T> toObservable() {
        return Observable.create((p008rx.Observable.OnSubscribe<T>) new p008rx.Observable.OnSubscribe<T>() {
            public void call(Subscriber<? super T> s) {
                Completable.this.unsafeSubscribe(s);
            }
        });
    }

    public final <T> Single<T> toSingle(final Func0<? extends T> completionValueFunc0) {
        requireNonNull(completionValueFunc0);
        return Single.create(new p008rx.Single.OnSubscribe<T>() {
            public void call(final SingleSubscriber<? super T> s) {
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        try {
                            T v = completionValueFunc0.call();
                            if (v == null) {
                                s.onError(new NullPointerException("The value supplied is null"));
                            } else {
                                s.onSuccess(v);
                            }
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSubscribe(Subscription d) {
                        s.add(d);
                    }
                });
            }
        });
    }

    public final <T> Single<T> toSingleDefault(final T completionValue) {
        requireNonNull(completionValue);
        return toSingle(new Func0<T>() {
            public T call() {
                return completionValue;
            }
        });
    }

    public final Completable unsubscribeOn(final Scheduler scheduler) {
        requireNonNull(scheduler);
        return create(new OnSubscribe() {
            public void call(final CompletableSubscriber s) {
                Completable.this.unsafeSubscribe((CompletableSubscriber) new CompletableSubscriber() {
                    public void onCompleted() {
                        s.onCompleted();
                    }

                    public void onError(Throwable e) {
                        s.onError(e);
                    }

                    public void onSubscribe(final Subscription d) {
                        s.onSubscribe(Subscriptions.create(new Action0() {
                            public void call() {
                                final Worker w = scheduler.createWorker();
                                w.schedule(new Action0() {
                                    public void call() {
                                        try {
                                            d.unsubscribe();
                                        } finally {
                                            w.unsubscribe();
                                        }
                                    }
                                });
                            }
                        }));
                    }
                });
            }
        });
    }
}
