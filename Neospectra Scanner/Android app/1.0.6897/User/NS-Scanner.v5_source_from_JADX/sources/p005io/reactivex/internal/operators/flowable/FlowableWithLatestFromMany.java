package p005io.reactivex.internal.operators.flowable;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.NonNull;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.ConditionalSubscriber;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.HalfSerializer;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany */
public final class FlowableWithLatestFromMany<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final Function<? super Object[], R> combiner;
    @Nullable
    final Publisher<?>[] otherArray;
    @Nullable
    final Iterable<? extends Publisher<?>> otherIterable;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$SingletonArrayFunc */
    final class SingletonArrayFunc implements Function<T, R> {
        SingletonArrayFunc() {
        }

        public R apply(T t) throws Exception {
            return ObjectHelper.requireNonNull(FlowableWithLatestFromMany.this.combiner.apply(new Object[]{t}), "The combiner returned a null value");
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestFromSubscriber */
    static final class WithLatestFromSubscriber<T, R> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = 1577321883966341961L;
        final Subscriber<? super R> actual;
        final Function<? super Object[], R> combiner;
        volatile boolean done;
        final AtomicThrowable error;
        final AtomicLong requested;

        /* renamed from: s */
        final AtomicReference<Subscription> f237s;
        final WithLatestInnerSubscriber[] subscribers;
        final AtomicReferenceArray<Object> values;

        WithLatestFromSubscriber(Subscriber<? super R> actual2, Function<? super Object[], R> combiner2, int n) {
            this.actual = actual2;
            this.combiner = combiner2;
            WithLatestInnerSubscriber[] s = new WithLatestInnerSubscriber[n];
            for (int i = 0; i < n; i++) {
                s[i] = new WithLatestInnerSubscriber(this, i);
            }
            this.subscribers = s;
            this.values = new AtomicReferenceArray<>(n);
            this.f237s = new AtomicReference<>();
            this.requested = new AtomicLong();
            this.error = new AtomicThrowable();
        }

        /* access modifiers changed from: 0000 */
        public void subscribe(Publisher<?>[] others, int n) {
            WithLatestInnerSubscriber[] subscribers2 = this.subscribers;
            AtomicReference<Subscription> s = this.f237s;
            for (int i = 0; i < n && !SubscriptionHelper.isCancelled((Subscription) s.get()); i++) {
                others[i].subscribe(subscribers2[i]);
            }
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.f237s, this.requested, s);
        }

        public void onNext(T t) {
            if (!tryOnNext(t) && !this.done) {
                ((Subscription) this.f237s.get()).request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            AtomicReferenceArray<Object> ara = this.values;
            int n = ara.length();
            Object[] objects = new Object[(n + 1)];
            objects[0] = t;
            for (int i = 0; i < n; i++) {
                Object o = ara.get(i);
                if (o == null) {
                    return false;
                }
                objects[i + 1] = o;
            }
            try {
                HalfSerializer.onNext(this.actual, ObjectHelper.requireNonNull(this.combiner.apply(objects), "The combiner returned a null value"), (AtomicInteger) this, this.error);
                return true;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                cancel();
                onError(ex);
                return false;
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            cancelAllBut(-1);
            HalfSerializer.onError(this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                cancelAllBut(-1);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.f237s, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.f237s);
            for (WithLatestInnerSubscriber s : this.subscribers) {
                s.dispose();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerNext(int index, Object o) {
            this.values.set(index, o);
        }

        /* access modifiers changed from: 0000 */
        public void innerError(int index, Throwable t) {
            this.done = true;
            SubscriptionHelper.cancel(this.f237s);
            cancelAllBut(index);
            HalfSerializer.onError(this.actual, t, (AtomicInteger) this, this.error);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete(int index, boolean nonEmpty) {
            if (!nonEmpty) {
                this.done = true;
                SubscriptionHelper.cancel(this.f237s);
                cancelAllBut(index);
                HalfSerializer.onComplete(this.actual, (AtomicInteger) this, this.error);
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAllBut(int index) {
            WithLatestInnerSubscriber[] subscribers2 = this.subscribers;
            for (int i = 0; i < subscribers2.length; i++) {
                if (i != index) {
                    subscribers2[i].dispose();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany$WithLatestInnerSubscriber */
    static final class WithLatestInnerSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
        private static final long serialVersionUID = 3256684027868224024L;
        boolean hasValue;
        final int index;
        final WithLatestFromSubscriber<?, ?> parent;

        WithLatestInnerSubscriber(WithLatestFromSubscriber<?, ?> parent2, int index2) {
            this.parent = parent2;
            this.index = index2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this, s, Long.MAX_VALUE);
        }

        public void onNext(Object t) {
            if (!this.hasValue) {
                this.hasValue = true;
            }
            this.parent.innerNext(this.index, t);
        }

        public void onError(Throwable t) {
            this.parent.innerError(this.index, t);
        }

        public void onComplete() {
            this.parent.innerComplete(this.index, this.hasValue);
        }

        /* access modifiers changed from: 0000 */
        public void dispose() {
            SubscriptionHelper.cancel(this);
        }
    }

    public FlowableWithLatestFromMany(@NonNull Flowable<T> source, @NonNull Publisher<?>[] otherArray2, Function<? super Object[], R> combiner2) {
        super(source);
        this.otherArray = otherArray2;
        this.otherIterable = null;
        this.combiner = combiner2;
    }

    public FlowableWithLatestFromMany(@NonNull Flowable<T> source, @NonNull Iterable<? extends Publisher<?>> otherIterable2, @NonNull Function<? super Object[], R> combiner2) {
        super(source);
        this.otherArray = null;
        this.otherIterable = otherIterable2;
        this.combiner = combiner2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        Publisher<?>[] others = this.otherArray;
        int n = 0;
        if (others == null) {
            others = new Publisher[8];
            try {
                for (Publisher<?> p : this.otherIterable) {
                    if (n == others.length) {
                        others = (Publisher[]) Arrays.copyOf(others, (n >> 1) + n);
                    }
                    int n2 = n + 1;
                    try {
                        others[n] = p;
                        n = n2;
                    } catch (Throwable th) {
                        ex = th;
                        int i = n2;
                        Exceptions.throwIfFatal(ex);
                        EmptySubscription.error(ex, s);
                        return;
                    }
                }
            } catch (Throwable th2) {
                ex = th2;
                Exceptions.throwIfFatal(ex);
                EmptySubscription.error(ex, s);
                return;
            }
        } else {
            n = others.length;
        }
        if (n == 0) {
            new FlowableMap(this.source, new SingletonArrayFunc()).subscribeActual(s);
            return;
        }
        WithLatestFromSubscriber<T, R> parent = new WithLatestFromSubscriber<>(s, this.combiner, n);
        s.onSubscribe(parent);
        parent.subscribe(others, n);
        this.source.subscribe((FlowableSubscriber<? super T>) parent);
    }
}
