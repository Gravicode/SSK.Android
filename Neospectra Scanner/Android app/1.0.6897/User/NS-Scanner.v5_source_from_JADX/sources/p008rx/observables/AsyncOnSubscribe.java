package p008rx.observables;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import p008rx.Observable;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.annotations.Experimental;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Action2;
import p008rx.functions.Action3;
import p008rx.functions.Func0;
import p008rx.functions.Func1;
import p008rx.functions.Func3;
import p008rx.internal.operators.BufferUntilSubscriber;
import p008rx.observers.SerializedObserver;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.CompositeSubscription;

@Experimental
/* renamed from: rx.observables.AsyncOnSubscribe */
public abstract class AsyncOnSubscribe<S, T> implements OnSubscribe<T> {

    /* renamed from: rx.observables.AsyncOnSubscribe$AsyncOnSubscribeImpl */
    static final class AsyncOnSubscribeImpl<S, T> extends AsyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            AsyncOnSubscribe.super.call((Subscriber) x0);
        }

        AsyncOnSubscribeImpl(Func0<? extends S> generator2, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next2, Action1<? super S> onUnsubscribe2) {
            this.generator = generator2;
            this.next = next2;
            this.onUnsubscribe = onUnsubscribe2;
        }

        public AsyncOnSubscribeImpl(Func0<? extends S> generator2, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next2) {
            this(generator2, next2, null);
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> next2, Action1<? super S> onUnsubscribe2) {
            this(null, next2, onUnsubscribe2);
        }

        public AsyncOnSubscribeImpl(Func3<S, Long, Observer<Observable<? extends T>>, S> nextFunc) {
            this(null, nextFunc, null);
        }

        /* access modifiers changed from: protected */
        public S generateState() {
            if (this.generator == null) {
                return null;
            }
            return this.generator.call();
        }

        /* access modifiers changed from: protected */
        public S next(S state, long requested, Observer<Observable<? extends T>> observer) {
            return this.next.call(state, Long.valueOf(requested), observer);
        }

        /* access modifiers changed from: protected */
        public void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe$AsyncOuterManager */
    static final class AsyncOuterManager<S, T> implements Producer, Subscription, Observer<Observable<? extends T>> {
        Producer concatProducer;
        boolean emitting;
        long expectedDelivery;
        private boolean hasTerminated;
        final AtomicBoolean isUnsubscribed;
        private final UnicastSubject<Observable<T>> merger;
        private boolean onNextCalled;
        private final AsyncOnSubscribe<S, T> parent;
        List<Long> requests;
        private final SerializedObserver<Observable<? extends T>> serializedSubscriber;
        private S state;
        final CompositeSubscription subscriptions = new CompositeSubscription();

        public AsyncOuterManager(AsyncOnSubscribe<S, T> parent2, S initialState, UnicastSubject<Observable<T>> merger2) {
            this.parent = parent2;
            this.serializedSubscriber = new SerializedObserver<>(this);
            this.state = initialState;
            this.merger = merger2;
            this.isUnsubscribed = new AtomicBoolean();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0026, code lost:
            cleanup();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void unsubscribe() {
            /*
                r3 = this;
                java.util.concurrent.atomic.AtomicBoolean r0 = r3.isUnsubscribed
                r1 = 1
                r2 = 0
                boolean r0 = r0.compareAndSet(r2, r1)
                if (r0 == 0) goto L_0x002d
                monitor-enter(r3)
                boolean r0 = r3.emitting     // Catch:{ all -> 0x002a }
                if (r0 == 0) goto L_0x0023
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x002a }
                r0.<init>()     // Catch:{ all -> 0x002a }
                r3.requests = r0     // Catch:{ all -> 0x002a }
                java.util.List<java.lang.Long> r0 = r3.requests     // Catch:{ all -> 0x002a }
                r1 = 0
                java.lang.Long r1 = java.lang.Long.valueOf(r1)     // Catch:{ all -> 0x002a }
                r0.add(r1)     // Catch:{ all -> 0x002a }
                monitor-exit(r3)     // Catch:{ all -> 0x002a }
                return
            L_0x0023:
                r3.emitting = r1     // Catch:{ all -> 0x002a }
                monitor-exit(r3)     // Catch:{ all -> 0x002a }
                r3.cleanup()
                goto L_0x002d
            L_0x002a:
                r0 = move-exception
                monitor-exit(r3)     // Catch:{ all -> 0x002a }
                throw r0
            L_0x002d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.observables.AsyncOnSubscribe.AsyncOuterManager.unsubscribe():void");
        }

        /* access modifiers changed from: 0000 */
        public void setConcatProducer(Producer p) {
            if (this.concatProducer != null) {
                throw new IllegalStateException("setConcatProducer may be called at most once!");
            }
            this.concatProducer = p;
        }

        public boolean isUnsubscribed() {
            return this.isUnsubscribed.get();
        }

        public void nextIteration(long requestCount) {
            this.state = this.parent.next(this.state, requestCount, this.serializedSubscriber);
        }

        /* access modifiers changed from: 0000 */
        public void cleanup() {
            this.subscriptions.unsubscribe();
            try {
                this.parent.onUnsubscribe(this.state);
            } catch (Throwable ex) {
                handleThrownError(ex);
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0060, code lost:
            r2 = r1.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0068, code lost:
            if (r2.hasNext() == false) goto L_0x0051;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0078, code lost:
            if (tryEmit(((java.lang.Long) r2.next()).longValue()) == false) goto L_0x0064;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007a, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void request(long r7) {
            /*
                r6 = this;
                r0 = 0
                int r2 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
                if (r2 != 0) goto L_0x0007
                return
            L_0x0007:
                int r0 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
                if (r0 >= 0) goto L_0x0022
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Request can't be negative! "
                r1.append(r2)
                r1.append(r7)
                java.lang.String r1 = r1.toString()
                r0.<init>(r1)
                throw r0
            L_0x0022:
                r0 = 0
                monitor-enter(r6)
                boolean r1 = r6.emitting     // Catch:{ all -> 0x0080 }
                if (r1 == 0) goto L_0x003d
                java.util.List<java.lang.Long> r1 = r6.requests     // Catch:{ all -> 0x0080 }
                if (r1 != 0) goto L_0x0034
                java.util.ArrayList r2 = new java.util.ArrayList     // Catch:{ all -> 0x0080 }
                r2.<init>()     // Catch:{ all -> 0x0080 }
                r1 = r2
                r6.requests = r1     // Catch:{ all -> 0x0080 }
            L_0x0034:
                java.lang.Long r2 = java.lang.Long.valueOf(r7)     // Catch:{ all -> 0x0080 }
                r1.add(r2)     // Catch:{ all -> 0x0080 }
                r0 = 1
                goto L_0x0040
            L_0x003d:
                r1 = 1
                r6.emitting = r1     // Catch:{ all -> 0x0080 }
            L_0x0040:
                monitor-exit(r6)     // Catch:{ all -> 0x0080 }
                rx.Producer r1 = r6.concatProducer
                r1.request(r7)
                if (r0 == 0) goto L_0x0049
                return
            L_0x0049:
                boolean r1 = r6.tryEmit(r7)
                if (r1 == 0) goto L_0x0050
                return
            L_0x0050:
                r1 = r6
            L_0x0051:
                monitor-enter(r6)
                java.util.List<java.lang.Long> r2 = r6.requests     // Catch:{ all -> 0x007d }
                r1 = r2
                if (r1 != 0) goto L_0x005c
                r2 = 0
                r6.emitting = r2     // Catch:{ all -> 0x007d }
                monitor-exit(r6)     // Catch:{ all -> 0x007d }
                return
            L_0x005c:
                r2 = 0
                r6.requests = r2     // Catch:{ all -> 0x007d }
                monitor-exit(r6)     // Catch:{ all -> 0x007d }
                java.util.Iterator r2 = r1.iterator()
            L_0x0064:
                boolean r3 = r2.hasNext()
                if (r3 == 0) goto L_0x007c
                java.lang.Object r3 = r2.next()
                java.lang.Long r3 = (java.lang.Long) r3
                long r3 = r3.longValue()
                boolean r5 = r6.tryEmit(r3)
                if (r5 == 0) goto L_0x007b
                return
            L_0x007b:
                goto L_0x0064
            L_0x007c:
                goto L_0x0051
            L_0x007d:
                r2 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x007d }
                throw r2
            L_0x0080:
                r1 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0080 }
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.observables.AsyncOnSubscribe.AsyncOuterManager.request(long):void");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0044, code lost:
            if (tryEmit(r6) == false) goto L_0x0047;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x0046, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0047, code lost:
            r0 = r5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0048, code lost:
            monitor-enter(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
            r0 = r5.requests;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x004c, code lost:
            if (r0 != null) goto L_0x0053;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x004e, code lost:
            r5.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0051, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x0052, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0053, code lost:
            r5.requests = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0056, code lost:
            monitor-exit(r5);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0057, code lost:
            r1 = r0.iterator();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x005f, code lost:
            if (r1.hasNext() == false) goto L_0x0048;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x006f, code lost:
            if (tryEmit(((java.lang.Long) r1.next()).longValue()) == false) goto L_0x005b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0071, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void requestRemaining(long r6) {
            /*
                r5 = this;
                r0 = 0
                int r2 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r2 != 0) goto L_0x0007
                return
            L_0x0007:
                int r0 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r0 >= 0) goto L_0x0022
                java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "Request can't be negative! "
                r1.append(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                r0.<init>(r1)
                throw r0
            L_0x0022:
                monitor-enter(r5)
                boolean r0 = r5.emitting     // Catch:{ all -> 0x0077 }
                if (r0 == 0) goto L_0x003c
                java.util.List<java.lang.Long> r0 = r5.requests     // Catch:{ all -> 0x0077 }
                if (r0 != 0) goto L_0x0033
                java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x0077 }
                r1.<init>()     // Catch:{ all -> 0x0077 }
                r0 = r1
                r5.requests = r0     // Catch:{ all -> 0x0077 }
            L_0x0033:
                java.lang.Long r1 = java.lang.Long.valueOf(r6)     // Catch:{ all -> 0x0077 }
                r0.add(r1)     // Catch:{ all -> 0x0077 }
                monitor-exit(r5)     // Catch:{ all -> 0x0077 }
                return
            L_0x003c:
                r0 = 1
                r5.emitting = r0     // Catch:{ all -> 0x0077 }
                monitor-exit(r5)     // Catch:{ all -> 0x0077 }
                boolean r0 = r5.tryEmit(r6)
                if (r0 == 0) goto L_0x0047
                return
            L_0x0047:
                r0 = r5
            L_0x0048:
                monitor-enter(r5)
                java.util.List<java.lang.Long> r1 = r5.requests     // Catch:{ all -> 0x0074 }
                r0 = r1
                if (r0 != 0) goto L_0x0053
                r1 = 0
                r5.emitting = r1     // Catch:{ all -> 0x0074 }
                monitor-exit(r5)     // Catch:{ all -> 0x0074 }
                return
            L_0x0053:
                r1 = 0
                r5.requests = r1     // Catch:{ all -> 0x0074 }
                monitor-exit(r5)     // Catch:{ all -> 0x0074 }
                java.util.Iterator r1 = r0.iterator()
            L_0x005b:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x0073
                java.lang.Object r2 = r1.next()
                java.lang.Long r2 = (java.lang.Long) r2
                long r2 = r2.longValue()
                boolean r4 = r5.tryEmit(r2)
                if (r4 == 0) goto L_0x0072
                return
            L_0x0072:
                goto L_0x005b
            L_0x0073:
                goto L_0x0048
            L_0x0074:
                r1 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0074 }
                throw r1
            L_0x0077:
                r0 = move-exception
                monitor-exit(r5)     // Catch:{ all -> 0x0077 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: p008rx.observables.AsyncOnSubscribe.AsyncOuterManager.requestRemaining(long):void");
        }

        /* access modifiers changed from: 0000 */
        public boolean tryEmit(long n) {
            if (isUnsubscribed()) {
                cleanup();
                return true;
            }
            try {
                this.onNextCalled = false;
                this.expectedDelivery = n;
                nextIteration(n);
                if (!this.hasTerminated) {
                    if (!isUnsubscribed()) {
                        if (this.onNextCalled) {
                            return false;
                        }
                        handleThrownError(new IllegalStateException("No events emitted!"));
                        return true;
                    }
                }
                cleanup();
                return true;
            } catch (Throwable ex) {
                handleThrownError(ex);
                return true;
            }
        }

        private void handleThrownError(Throwable ex) {
            if (this.hasTerminated) {
                RxJavaHooks.onError(ex);
                return;
            }
            this.hasTerminated = true;
            this.merger.onError(ex);
            cleanup();
        }

        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onCompleted();
        }

        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            this.merger.onError(e);
        }

        public void onNext(Observable<? extends T> t) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            if (!this.hasTerminated) {
                subscribeBufferToObservable(t);
            }
        }

        private void subscribeBufferToObservable(Observable<? extends T> t) {
            final BufferUntilSubscriber<T> buffer = BufferUntilSubscriber.create();
            final long expected = this.expectedDelivery;
            final Subscriber<T> s = new Subscriber<T>() {
                long remaining = expected;

                public void onNext(T t) {
                    this.remaining--;
                    buffer.onNext(t);
                }

                public void onError(Throwable e) {
                    buffer.onError(e);
                }

                public void onCompleted() {
                    buffer.onCompleted();
                    long r = this.remaining;
                    if (r > 0) {
                        AsyncOuterManager.this.requestRemaining(r);
                    }
                }
            };
            this.subscriptions.add(s);
            t.doOnTerminate(new Action0() {
                public void call() {
                    AsyncOuterManager.this.subscriptions.remove(s);
                }
            }).subscribe(s);
            this.merger.onNext(buffer);
        }
    }

    /* renamed from: rx.observables.AsyncOnSubscribe$UnicastSubject */
    static final class UnicastSubject<T> extends Observable<T> implements Observer<T> {
        private final State<T> state;

        /* renamed from: rx.observables.AsyncOnSubscribe$UnicastSubject$State */
        static final class State<T> implements OnSubscribe<T> {
            Subscriber<? super T> subscriber;

            State() {
            }

            public void call(Subscriber<? super T> s) {
                synchronized (this) {
                    if (this.subscriber == null) {
                        this.subscriber = s;
                    } else {
                        s.onError(new IllegalStateException("There can be only one subscriber"));
                    }
                }
            }
        }

        public static <T> UnicastSubject<T> create() {
            return new UnicastSubject<>(new State());
        }

        protected UnicastSubject(State<T> state2) {
            super(state2);
            this.state = state2;
        }

        public void onCompleted() {
            this.state.subscriber.onCompleted();
        }

        public void onError(Throwable e) {
            this.state.subscriber.onError(e);
        }

        public void onNext(T t) {
            this.state.subscriber.onNext(t);
        }
    }

    /* access modifiers changed from: protected */
    public abstract S generateState();

    /* access modifiers changed from: protected */
    public abstract S next(S s, long j, Observer<Observable<? extends T>> observer);

    /* access modifiers changed from: protected */
    public void onUnsubscribe(S s) {
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action3<? super S, Long, ? super Observer<Observable<? extends T>>> next) {
        return new AsyncOnSubscribeImpl(generator, new Func3<S, Long, Observer<Observable<? extends T>>, S>() {
            public S call(S state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(state, requested, subscriber);
                return state;
            }
        });
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action3<? super S, Long, ? super Observer<Observable<? extends T>>> next, Action1<? super S> onUnsubscribe) {
        return new AsyncOnSubscribeImpl(generator, new Func3<S, Long, Observer<Observable<? extends T>>, S>() {
            public S call(S state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(state, requested, subscriber);
                return state;
            }
        }, onUnsubscribe);
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new AsyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Experimental
    public static <S, T> AsyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func3<? super S, Long, ? super Observer<Observable<? extends T>>, ? extends S> next) {
        return new AsyncOnSubscribeImpl(generator, next);
    }

    @Experimental
    public static <T> AsyncOnSubscribe<Void, T> createStateless(final Action2<Long, ? super Observer<Observable<? extends T>>> next) {
        return new AsyncOnSubscribeImpl(new Func3<Void, Long, Observer<Observable<? extends T>>, Void>() {
            public Void call(Void state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(requested, subscriber);
                return state;
            }
        });
    }

    @Experimental
    public static <T> AsyncOnSubscribe<Void, T> createStateless(final Action2<Long, ? super Observer<Observable<? extends T>>> next, final Action0 onUnsubscribe) {
        return new AsyncOnSubscribeImpl(new Func3<Void, Long, Observer<Observable<? extends T>>, Void>() {
            public Void call(Void state, Long requested, Observer<Observable<? extends T>> subscriber) {
                next.call(requested, subscriber);
                return null;
            }
        }, new Action1<Void>() {
            public void call(Void t) {
                onUnsubscribe.call();
            }
        });
    }

    public final void call(final Subscriber<? super T> actualSubscriber) {
        try {
            S state = generateState();
            UnicastSubject<Observable<T>> subject = UnicastSubject.create();
            final AsyncOuterManager<S, T> outerProducer = new AsyncOuterManager<>(this, state, subject);
            Subscriber<T> concatSubscriber = new Subscriber<T>() {
                public void onNext(T t) {
                    actualSubscriber.onNext(t);
                }

                public void onError(Throwable e) {
                    actualSubscriber.onError(e);
                }

                public void onCompleted() {
                    actualSubscriber.onCompleted();
                }

                public void setProducer(Producer p) {
                    outerProducer.setConcatProducer(p);
                }
            };
            subject.onBackpressureBuffer().concatMap(new Func1<Observable<T>, Observable<T>>() {
                public Observable<T> call(Observable<T> v) {
                    return v.onBackpressureBuffer();
                }
            }).unsafeSubscribe(concatSubscriber);
            actualSubscriber.add(concatSubscriber);
            actualSubscriber.add(outerProducer);
            actualSubscriber.setProducer(outerProducer);
        } catch (Throwable ex) {
            actualSubscriber.onError(ex);
        }
    }
}
