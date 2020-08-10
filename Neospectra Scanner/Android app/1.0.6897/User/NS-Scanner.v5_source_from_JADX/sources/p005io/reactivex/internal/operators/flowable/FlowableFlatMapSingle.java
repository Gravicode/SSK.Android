package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.disposables.CompositeDisposable;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapSingle */
public final class FlowableFlatMapSingle<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final boolean delayErrors;
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final int maxConcurrency;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapSingle$FlatMapSingleSubscriber */
    static final class FlatMapSingleSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 8600231336733376951L;
        final AtomicInteger active = new AtomicInteger(1);
        final Subscriber<? super R> actual;
        volatile boolean cancelled;
        final boolean delayErrors;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;
        final int maxConcurrency;
        final AtomicReference<SpscLinkedArrayQueue<R>> queue = new AtomicReference<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f161s;
        final CompositeDisposable set = new CompositeDisposable();

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableFlatMapSingle$FlatMapSingleSubscriber$InnerObserver */
        final class InnerObserver extends AtomicReference<Disposable> implements SingleObserver<R>, Disposable {
            private static final long serialVersionUID = -502562646270949838L;

            InnerObserver() {
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.setOnce(this, d);
            }

            public void onSuccess(R value) {
                FlatMapSingleSubscriber.this.innerSuccess(this, value);
            }

            public void onError(Throwable e) {
                FlatMapSingleSubscriber.this.innerError(this, e);
            }

            public boolean isDisposed() {
                return DisposableHelper.isDisposed((Disposable) get());
            }

            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        FlatMapSingleSubscriber(Subscriber<? super R> actual2, Function<? super T, ? extends SingleSource<? extends R>> mapper2, boolean delayErrors2, int maxConcurrency2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.delayErrors = delayErrors2;
            this.maxConcurrency = maxConcurrency2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f161s, s)) {
                this.f161s = s;
                this.actual.onSubscribe(this);
                if (this.maxConcurrency == Integer.MAX_VALUE) {
                    s.request(Long.MAX_VALUE);
                } else {
                    s.request((long) this.maxConcurrency);
                }
            }
        }

        public void onNext(T t) {
            try {
                SingleSource<? extends R> ms = (SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null SingleSource");
                this.active.getAndIncrement();
                InnerObserver inner = new InnerObserver<>();
                if (!this.cancelled && this.set.add(inner)) {
                    ms.subscribe(inner);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.f161s.cancel();
                onError(ex);
            }
        }

        public void onError(Throwable t) {
            this.active.decrementAndGet();
            if (this.errors.addThrowable(t)) {
                if (!this.delayErrors) {
                    this.set.dispose();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.active.decrementAndGet();
            drain();
        }

        public void cancel() {
            this.cancelled = true;
            this.f161s.cancel();
            this.set.dispose();
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerSuccess(InnerObserver inner, R value) {
            this.set.delete(inner);
            if (get() == 0) {
                boolean d = true;
                if (compareAndSet(0, 1)) {
                    if (this.active.decrementAndGet() != 0) {
                        d = false;
                    }
                    if (this.requested.get() != 0) {
                        this.actual.onNext(value);
                        SpscLinkedArrayQueue<R> q = (SpscLinkedArrayQueue) this.queue.get();
                        if (!d || (q != null && !q.isEmpty())) {
                            BackpressureHelper.produced(this.requested, 1);
                            if (this.maxConcurrency != Integer.MAX_VALUE) {
                                this.f161s.request(1);
                            }
                        } else {
                            Throwable ex = this.errors.terminate();
                            if (ex != null) {
                                this.actual.onError(ex);
                            } else {
                                this.actual.onComplete();
                            }
                            return;
                        }
                    } else {
                        SpscLinkedArrayQueue<R> q2 = getOrCreateQueue();
                        synchronized (q2) {
                            q2.offer(value);
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                    drainLoop();
                }
            }
            SpscLinkedArrayQueue<R> q3 = getOrCreateQueue();
            synchronized (q3) {
                q3.offer(value);
            }
            this.active.decrementAndGet();
            if (getAndIncrement() != 0) {
                return;
            }
            drainLoop();
        }

        /* access modifiers changed from: 0000 */
        public SpscLinkedArrayQueue<R> getOrCreateQueue() {
            SpscLinkedArrayQueue spscLinkedArrayQueue;
            do {
                SpscLinkedArrayQueue<R> current = (SpscLinkedArrayQueue) this.queue.get();
                if (current != null) {
                    return current;
                }
                spscLinkedArrayQueue = new SpscLinkedArrayQueue(Flowable.bufferSize());
            } while (!this.queue.compareAndSet(null, spscLinkedArrayQueue));
            return spscLinkedArrayQueue;
        }

        /* access modifiers changed from: 0000 */
        public void innerError(InnerObserver inner, Throwable e) {
            this.set.delete(inner);
            if (this.errors.addThrowable(e)) {
                if (!this.delayErrors) {
                    this.f161s.cancel();
                    this.set.dispose();
                } else if (this.maxConcurrency != Integer.MAX_VALUE) {
                    this.f161s.request(1);
                }
                this.active.decrementAndGet();
                drain();
                return;
            }
            RxJavaPlugins.onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                drainLoop();
            }
        }

        /* access modifiers changed from: 0000 */
        public void clear() {
            SpscLinkedArrayQueue<R> q = (SpscLinkedArrayQueue) this.queue.get();
            if (q != null) {
                q.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainLoop() {
            boolean z;
            int missed = 1;
            Subscriber<? super R> a = this.actual;
            AtomicInteger n = this.active;
            AtomicReference<SpscLinkedArrayQueue<R>> qr = this.queue;
            do {
                long r = this.requested.get();
                long e = 0;
                while (true) {
                    z = true;
                    if (e == r) {
                        break;
                    } else if (this.cancelled) {
                        clear();
                        return;
                    } else if (this.delayErrors || ((Throwable) this.errors.get()) == null) {
                        boolean d = n.get() == 0;
                        SpscLinkedArrayQueue<R> q = (SpscLinkedArrayQueue) qr.get();
                        R v = q != null ? q.poll() : null;
                        boolean empty = v == null;
                        if (d && empty) {
                            Throwable ex = this.errors.terminate();
                            if (ex != null) {
                                a.onError(ex);
                            } else {
                                a.onComplete();
                            }
                            return;
                        } else if (empty) {
                            break;
                        } else {
                            a.onNext(v);
                            e++;
                        }
                    } else {
                        Throwable ex2 = this.errors.terminate();
                        clear();
                        a.onError(ex2);
                        return;
                    }
                }
                if (e == r) {
                    if (this.cancelled) {
                        clear();
                        return;
                    } else if (this.delayErrors || ((Throwable) this.errors.get()) == null) {
                        boolean d2 = n.get() == 0;
                        SpscLinkedArrayQueue<R> q2 = (SpscLinkedArrayQueue) qr.get();
                        if (q2 != null && !q2.isEmpty()) {
                            z = false;
                        }
                        boolean empty2 = z;
                        if (d2 && empty2) {
                            Throwable ex3 = this.errors.terminate();
                            if (ex3 != null) {
                                a.onError(ex3);
                            } else {
                                a.onComplete();
                            }
                            return;
                        }
                    } else {
                        Throwable ex4 = this.errors.terminate();
                        clear();
                        a.onError(ex4);
                        return;
                    }
                }
                if (e != 0) {
                    BackpressureHelper.produced(this.requested, e);
                    if (this.maxConcurrency != Integer.MAX_VALUE) {
                        this.f161s.request(e);
                    }
                }
                missed = addAndGet(-missed);
            } while (missed != 0);
        }
    }

    public FlowableFlatMapSingle(Flowable<T> source, Function<? super T, ? extends SingleSource<? extends R>> mapper2, boolean delayError, int maxConcurrency2) {
        super(source);
        this.mapper = mapper2;
        this.delayErrors = delayError;
        this.maxConcurrency = maxConcurrency2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new FlatMapSingleSubscriber<Object>(s, this.mapper, this.delayErrors, this.maxConcurrency));
    }
}
