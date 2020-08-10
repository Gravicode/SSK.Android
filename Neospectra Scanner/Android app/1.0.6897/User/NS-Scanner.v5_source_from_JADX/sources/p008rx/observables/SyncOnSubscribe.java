package p008rx.observables;

import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observable.OnSubscribe;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.annotations.Beta;
import p008rx.exceptions.Exceptions;
import p008rx.functions.Action0;
import p008rx.functions.Action1;
import p008rx.functions.Action2;
import p008rx.functions.Func0;
import p008rx.functions.Func2;
import p008rx.internal.operators.BackpressureUtils;
import p008rx.plugins.RxJavaHooks;

/* renamed from: rx.observables.SyncOnSubscribe */
public abstract class SyncOnSubscribe<S, T> implements OnSubscribe<T> {

    /* renamed from: rx.observables.SyncOnSubscribe$SubscriptionProducer */
    static final class SubscriptionProducer<S, T> extends AtomicLong implements Producer, Subscription, Observer<T> {
        private static final long serialVersionUID = -3736864024352728072L;
        private final Subscriber<? super T> actualSubscriber;
        private boolean hasTerminated;
        private boolean onNextCalled;
        private final SyncOnSubscribe<S, T> parent;
        private S state;

        SubscriptionProducer(Subscriber<? super T> subscriber, SyncOnSubscribe<S, T> parent2, S state2) {
            this.actualSubscriber = subscriber;
            this.parent = parent2;
            this.state = state2;
        }

        public boolean isUnsubscribed() {
            return get() < 0;
        }

        public void unsubscribe() {
            long requestCount;
            do {
                requestCount = get();
                if (compareAndSet(0, -1)) {
                    doUnsubscribe();
                    return;
                }
            } while (!compareAndSet(requestCount, -2));
        }

        private boolean tryUnsubscribe() {
            if (!this.hasTerminated && get() >= -1) {
                return false;
            }
            set(-1);
            doUnsubscribe();
            return true;
        }

        private void doUnsubscribe() {
            try {
                this.parent.onUnsubscribe(this.state);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaHooks.onError(e);
            }
        }

        public void request(long n) {
            if (n > 0 && BackpressureUtils.getAndAddRequest(this, n) == 0) {
                if (n == Long.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(n);
                }
            }
        }

        private void fastPath() {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            do {
                try {
                    this.onNextCalled = false;
                    nextIteration(p);
                } catch (Throwable ex) {
                    handleThrownError(a, ex);
                    return;
                }
            } while (!tryUnsubscribe());
        }

        private void handleThrownError(Subscriber<? super T> a, Throwable ex) {
            if (this.hasTerminated) {
                RxJavaHooks.onError(ex);
                return;
            }
            this.hasTerminated = true;
            a.onError(ex);
            unsubscribe();
        }

        private void slowPath(long n) {
            SyncOnSubscribe<S, T> p = this.parent;
            Subscriber<? super T> a = this.actualSubscriber;
            long numRequested = n;
            do {
                long numRemaining = numRequested;
                do {
                    try {
                        this.onNextCalled = false;
                        nextIteration(p);
                        if (!tryUnsubscribe()) {
                            if (this.onNextCalled) {
                                numRemaining--;
                            }
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        handleThrownError(a, ex);
                        return;
                    }
                } while (numRemaining != 0);
                numRequested = addAndGet(-numRequested);
            } while (numRequested > 0);
            tryUnsubscribe();
        }

        private void nextIteration(SyncOnSubscribe<S, T> parent2) {
            this.state = parent2.next(this.state, this);
        }

        public void onCompleted() {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (this.hasTerminated) {
                throw new IllegalStateException("Terminal event already emitted.");
            }
            this.hasTerminated = true;
            if (!this.actualSubscriber.isUnsubscribed()) {
                this.actualSubscriber.onError(e);
            }
        }

        public void onNext(T value) {
            if (this.onNextCalled) {
                throw new IllegalStateException("onNext called multiple times!");
            }
            this.onNextCalled = true;
            this.actualSubscriber.onNext(value);
        }
    }

    /* renamed from: rx.observables.SyncOnSubscribe$SyncOnSubscribeImpl */
    static final class SyncOnSubscribeImpl<S, T> extends SyncOnSubscribe<S, T> {
        private final Func0<? extends S> generator;
        private final Func2<? super S, ? super Observer<? super T>, ? extends S> next;
        private final Action1<? super S> onUnsubscribe;

        public /* bridge */ /* synthetic */ void call(Object x0) {
            SyncOnSubscribe.super.call((Subscriber) x0);
        }

        SyncOnSubscribeImpl(Func0<? extends S> generator2, Func2<? super S, ? super Observer<? super T>, ? extends S> next2, Action1<? super S> onUnsubscribe2) {
            this.generator = generator2;
            this.next = next2;
            this.onUnsubscribe = onUnsubscribe2;
        }

        public SyncOnSubscribeImpl(Func0<? extends S> generator2, Func2<? super S, ? super Observer<? super T>, ? extends S> next2) {
            this(generator2, next2, null);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> next2, Action1<? super S> onUnsubscribe2) {
            this(null, next2, onUnsubscribe2);
        }

        public SyncOnSubscribeImpl(Func2<S, Observer<? super T>, S> nextFunc) {
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
        public S next(S state, Observer<? super T> observer) {
            return this.next.call(state, observer);
        }

        /* access modifiers changed from: protected */
        public void onUnsubscribe(S state) {
            if (this.onUnsubscribe != null) {
                this.onUnsubscribe.call(state);
            }
        }
    }

    /* access modifiers changed from: protected */
    public abstract S generateState();

    /* access modifiers changed from: protected */
    public abstract S next(S s, Observer<? super T> observer);

    public final void call(Subscriber<? super T> subscriber) {
        try {
            SubscriptionProducer<S, T> p = new SubscriptionProducer<>(subscriber, this, generateState());
            subscriber.add(p);
            subscriber.setProducer(p);
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            subscriber.onError(e);
        }
    }

    /* access modifiers changed from: protected */
    public void onUnsubscribe(S s) {
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl(generator, new Func2<S, Observer<? super T>, S>() {
            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        });
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createSingleState(Func0<? extends S> generator, final Action2<? super S, ? super Observer<? super T>> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, new Func2<S, Observer<? super T>, S>() {
            public S call(S state, Observer<? super T> subscriber) {
                next.call(state, subscriber);
                return state;
            }
        }, onUnsubscribe);
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next, Action1<? super S> onUnsubscribe) {
        return new SyncOnSubscribeImpl(generator, next, onUnsubscribe);
    }

    @Beta
    public static <S, T> SyncOnSubscribe<S, T> createStateful(Func0<? extends S> generator, Func2<? super S, ? super Observer<? super T>, ? extends S> next) {
        return new SyncOnSubscribeImpl(generator, next);
    }

    @Beta
    public static <T> SyncOnSubscribe<Void, T> createStateless(final Action1<? super Observer<? super T>> next) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return state;
            }
        });
    }

    @Beta
    public static <T> SyncOnSubscribe<Void, T> createStateless(final Action1<? super Observer<? super T>> next, final Action0 onUnsubscribe) {
        return new SyncOnSubscribeImpl(new Func2<Void, Observer<? super T>, Void>() {
            public Void call(Void state, Observer<? super T> subscriber) {
                next.call(subscriber);
                return null;
            }
        }, new Action1<Void>() {
            public void call(Void t) {
                onUnsubscribe.call();
            }
        });
    }
}
