package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.BiFunction;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.EmptySubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableScanSeed */
public final class FlowableScanSeed<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final BiFunction<R, ? super T, R> accumulator;
    final Callable<R> seedSupplier;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableScanSeed$ScanSeedSubscriber */
    static final class ScanSeedSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -1776795561228106469L;
        final BiFunction<R, ? super T, R> accumulator;
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        int consumed;
        volatile boolean done;
        Throwable error;
        final int limit;
        final int prefetch;
        final SimplePlainQueue<R> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f195s;
        R value;

        ScanSeedSubscriber(Subscriber<? super R> actual2, BiFunction<R, ? super T, R> accumulator2, R value2, int prefetch2) {
            this.actual = actual2;
            this.accumulator = accumulator2;
            this.value = value2;
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            this.queue = new SpscArrayQueue(prefetch2);
            this.queue.offer(value2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f195s, s)) {
                this.f195s = s;
                this.actual.onSubscribe(this);
                s.request((long) (this.prefetch - 1));
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    Object requireNonNull = ObjectHelper.requireNonNull(this.accumulator.apply(this.value, t), "The accumulator returned a null value");
                    this.value = requireNonNull;
                    this.queue.offer(requireNonNull);
                    drain();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.f195s.cancel();
                    onError(ex);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f195s.cancel();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super R> a = this.actual;
                SimplePlainQueue<R> q = this.queue;
                int lim = this.limit;
                int c = this.consumed;
                do {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        if (this.cancelled) {
                            q.clear();
                            return;
                        }
                        boolean d = this.done;
                        if (d) {
                            Throwable ex = this.error;
                            if (ex != null) {
                                q.clear();
                                a.onError(ex);
                                return;
                            }
                        }
                        R v = q.poll();
                        boolean empty = v == null;
                        if (d && empty) {
                            a.onComplete();
                            return;
                        } else if (empty) {
                            break;
                        } else {
                            a.onNext(v);
                            e++;
                            c++;
                            if (c == lim) {
                                c = 0;
                                this.f195s.request((long) lim);
                            }
                        }
                    }
                    if (e == r && this.done) {
                        Throwable ex2 = this.error;
                        if (ex2 != null) {
                            q.clear();
                            a.onError(ex2);
                            return;
                        } else if (q.isEmpty()) {
                            a.onComplete();
                            return;
                        }
                    }
                    if (e != 0) {
                        BackpressureHelper.produced(this.requested, e);
                    }
                    this.consumed = c;
                    missed = addAndGet(-missed);
                } while (missed != 0);
            }
        }
    }

    public FlowableScanSeed(Flowable<T> source, Callable<R> seedSupplier2, BiFunction<R, ? super T, R> accumulator2) {
        super(source);
        this.accumulator = accumulator2;
        this.seedSupplier = seedSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new ScanSeedSubscriber<Object>(s, this.accumulator, ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seed supplied is null"), bufferSize()));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptySubscription.error(e, s);
        }
    }
}
