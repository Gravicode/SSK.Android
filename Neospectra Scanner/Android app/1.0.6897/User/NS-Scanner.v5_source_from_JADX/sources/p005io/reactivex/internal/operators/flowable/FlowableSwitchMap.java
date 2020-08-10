package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.QueueSubscription;
import p005io.reactivex.internal.fuseable.SimpleQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap */
public final class FlowableSwitchMap<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final int bufferSize;
    final boolean delayErrors;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapInnerSubscriber */
    static final class SwitchMapInnerSubscriber<T, R> extends AtomicReference<Subscription> implements FlowableSubscriber<R> {
        private static final long serialVersionUID = 3837284832786408377L;
        final int bufferSize;
        volatile boolean done;
        int fusionMode;
        final long index;
        final SwitchMapSubscriber<T, R> parent;
        volatile SimpleQueue<R> queue;

        SwitchMapInnerSubscriber(SwitchMapSubscriber<T, R> parent2, long index2, int bufferSize2) {
            this.parent = parent2;
            this.index = index2;
            this.bufferSize = bufferSize2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                if (s instanceof QueueSubscription) {
                    QueueSubscription<R> qs = (QueueSubscription) s;
                    int m = qs.requestFusion(7);
                    if (m == 1) {
                        this.fusionMode = m;
                        this.queue = qs;
                        this.done = true;
                        this.parent.drain();
                        return;
                    } else if (m == 2) {
                        this.fusionMode = m;
                        this.queue = qs;
                        s.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                s.request((long) this.bufferSize);
            }
        }

        public void onNext(R t) {
            SwitchMapSubscriber<T, R> p = this.parent;
            if (this.index == p.unique) {
                if (this.fusionMode != 0 || this.queue.offer(t)) {
                    p.drain();
                } else {
                    onError(new MissingBackpressureException("Queue full?!"));
                }
            }
        }

        public void onError(Throwable t) {
            SwitchMapSubscriber<T, R> p = this.parent;
            if (this.index != p.unique || !p.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (!p.delayErrors) {
                p.f212s.cancel();
            }
            this.done = true;
            p.drain();
        }

        public void onComplete() {
            SwitchMapSubscriber<T, R> p = this.parent;
            if (this.index == p.unique) {
                this.done = true;
                p.drain();
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSwitchMap$SwitchMapSubscriber */
    static final class SwitchMapSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        static final SwitchMapInnerSubscriber<Object, Object> CANCELLED = new SwitchMapInnerSubscriber<>(null, -1, 1);
        private static final long serialVersionUID = -3491074160481096299L;
        final AtomicReference<SwitchMapInnerSubscriber<T, R>> active = new AtomicReference<>();
        final Subscriber<? super R> actual;
        final int bufferSize;
        volatile boolean cancelled;
        final boolean delayErrors;
        volatile boolean done;
        final AtomicThrowable error;
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f212s;
        volatile long unique;

        static {
            CANCELLED.cancel();
        }

        SwitchMapSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends Publisher<? extends R>> mapper2, int bufferSize2, boolean delayErrors2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.delayErrors = delayErrors2;
            this.error = new AtomicThrowable();
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f212s, s)) {
                this.f212s = s;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long c = this.unique + 1;
                this.unique = c;
                SwitchMapInnerSubscriber<T, R> inner = (SwitchMapInnerSubscriber) this.active.get();
                if (inner != null) {
                    inner.cancel();
                }
                try {
                    Publisher<? extends R> p = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The publisher returned is null");
                    SwitchMapInnerSubscriber<T, R> nextInner = new SwitchMapInnerSubscriber<>(this, c, this.bufferSize);
                    while (true) {
                        SwitchMapInnerSubscriber<T, R> inner2 = (SwitchMapInnerSubscriber) this.active.get();
                        if (inner2 != CANCELLED) {
                            if (this.active.compareAndSet(inner2, nextInner)) {
                                p.subscribe(nextInner);
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.f212s.cancel();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done || !this.error.addThrowable(t)) {
                RxJavaPlugins.onError(t);
                return;
            }
            if (!this.delayErrors) {
                disposeInner();
            }
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                if (this.unique == 0) {
                    this.f212s.request(Long.MAX_VALUE);
                } else {
                    drain();
                }
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f212s.cancel();
                disposeInner();
            }
        }

        /* access modifiers changed from: 0000 */
        public void disposeInner() {
            if (((SwitchMapInnerSubscriber) this.active.get()) != CANCELLED) {
                SwitchMapInnerSubscriber<T, R> a = (SwitchMapInnerSubscriber) this.active.getAndSet(CANCELLED);
                if (a != CANCELLED && a != null) {
                    a.cancel();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            Object obj;
            SimpleQueue simpleQueue;
            if (getAndIncrement() == 0) {
                Subscriber<? super R> a = this.actual;
                int missing = 1;
                while (!this.cancelled) {
                    if (this.done) {
                        if (this.delayErrors) {
                            if (this.active.get() == null) {
                                if (((Throwable) this.error.get()) != null) {
                                    a.onError(this.error.terminate());
                                } else {
                                    a.onComplete();
                                }
                                return;
                            }
                        } else if (((Throwable) this.error.get()) != null) {
                            disposeInner();
                            a.onError(this.error.terminate());
                            return;
                        } else if (this.active.get() == null) {
                            a.onComplete();
                            return;
                        }
                    }
                    SwitchMapInnerSubscriber<T, R> inner = (SwitchMapInnerSubscriber) this.active.get();
                    SimpleQueue simpleQueue2 = inner != null ? inner.queue : null;
                    if (simpleQueue2 != null) {
                        if (inner.done) {
                            if (!this.delayErrors) {
                                if (((Throwable) this.error.get()) != null) {
                                    disposeInner();
                                    a.onError(this.error.terminate());
                                    return;
                                } else if (simpleQueue2.isEmpty()) {
                                    this.active.compareAndSet(inner, null);
                                }
                            } else if (simpleQueue2.isEmpty()) {
                                this.active.compareAndSet(inner, null);
                            }
                        }
                        long r = this.requested.get();
                        long e = 0;
                        boolean retry = false;
                        while (true) {
                            if (e == r) {
                                SimpleQueue simpleQueue3 = simpleQueue2;
                                break;
                            } else if (!this.cancelled) {
                                boolean d = inner.done;
                                try {
                                    obj = simpleQueue2.poll();
                                } catch (Throwable th) {
                                    Throwable ex = th;
                                    Exceptions.throwIfFatal(ex);
                                    inner.cancel();
                                    this.error.addThrowable(ex);
                                    d = true;
                                    obj = null;
                                }
                                Object obj2 = obj;
                                boolean empty = obj2 == null;
                                if (inner != this.active.get()) {
                                    retry = true;
                                    SimpleQueue simpleQueue4 = simpleQueue2;
                                    break;
                                }
                                if (d) {
                                    if (this.delayErrors) {
                                        simpleQueue = simpleQueue2;
                                        if (empty) {
                                            this.active.compareAndSet(inner, null);
                                            retry = true;
                                            break;
                                        }
                                    } else if (((Throwable) this.error.get()) != null) {
                                        a.onError(this.error.terminate());
                                        return;
                                    } else if (empty) {
                                        SimpleQueue simpleQueue5 = simpleQueue2;
                                        this.active.compareAndSet(inner, null);
                                        retry = true;
                                        break;
                                    } else {
                                        simpleQueue = simpleQueue2;
                                    }
                                } else {
                                    simpleQueue = simpleQueue2;
                                }
                                if (empty) {
                                    break;
                                }
                                a.onNext(obj2);
                                e++;
                                simpleQueue2 = simpleQueue;
                            } else {
                                return;
                            }
                        }
                        if (e != 0 && !this.cancelled) {
                            if (r != Long.MAX_VALUE) {
                                this.requested.addAndGet(-e);
                            }
                            ((Subscription) inner.get()).request(e);
                        }
                        if (retry) {
                            continue;
                        }
                    } else {
                        SimpleQueue simpleQueue6 = simpleQueue2;
                    }
                    missing = addAndGet(-missing);
                    if (missing == 0) {
                        return;
                    }
                }
                this.active.lazySet(null);
            }
        }
    }

    public FlowableSwitchMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends R>> mapper2, int bufferSize2, boolean delayErrors2) {
        super(source);
        this.mapper = mapper2;
        this.bufferSize = bufferSize2;
        this.delayErrors = delayErrors2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        if (!FlowableScalarXMap.tryScalarXMapSubscribe(this.source, s, this.mapper)) {
            this.source.subscribe((FlowableSubscriber<? super T>) new SwitchMapSubscriber<Object>(s, this.mapper, this.bufferSize, this.delayErrors));
        }
    }
}
