package p005io.reactivex.internal.operators.mixed;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.SingleObserver;
import p005io.reactivex.SingleSource;
import p005io.reactivex.annotations.Experimental;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Function;
import p005io.reactivex.internal.disposables.DisposableHelper;
import p005io.reactivex.internal.functions.ObjectHelper;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.AtomicThrowable;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.ErrorMode;
import p005io.reactivex.plugins.RxJavaPlugins;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.FlowableConcatMapSingle */
public final class FlowableConcatMapSingle<T, R> extends Flowable<R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final int prefetch;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.mixed.FlowableConcatMapSingle$ConcatMapSingleSubscriber */
    static final class ConcatMapSingleSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
        static final int STATE_ACTIVE = 1;
        static final int STATE_INACTIVE = 0;
        static final int STATE_RESULT_VALUE = 2;
        private static final long serialVersionUID = -9140123220065488293L;
        volatile boolean cancelled;
        int consumed;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        long emitted;
        final ErrorMode errorMode;
        final AtomicThrowable errors = new AtomicThrowable();
        final ConcatMapSingleObserver<R> inner = new ConcatMapSingleObserver<>(this);
        R item;
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;
        final int prefetch;
        final SimplePlainQueue<T> queue;
        final AtomicLong requested = new AtomicLong();
        volatile int state;
        Subscription upstream;

        /* renamed from: io.reactivex.internal.operators.mixed.FlowableConcatMapSingle$ConcatMapSingleSubscriber$ConcatMapSingleObserver */
        static final class ConcatMapSingleObserver<R> extends AtomicReference<Disposable> implements SingleObserver<R> {
            private static final long serialVersionUID = -3051469169682093892L;
            final ConcatMapSingleSubscriber<?, R> parent;

            ConcatMapSingleObserver(ConcatMapSingleSubscriber<?, R> parent2) {
                this.parent = parent2;
            }

            public void onSubscribe(Disposable d) {
                DisposableHelper.replace(this, d);
            }

            public void onSuccess(R t) {
                this.parent.innerSuccess(t);
            }

            public void onError(Throwable e) {
                this.parent.innerError(e);
            }

            /* access modifiers changed from: 0000 */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }

        ConcatMapSingleSubscriber(Subscriber<? super R> downstream2, Function<? super T, ? extends SingleSource<? extends R>> mapper2, int prefetch2, ErrorMode errorMode2) {
            this.downstream = downstream2;
            this.mapper = mapper2;
            this.prefetch = prefetch2;
            this.errorMode = errorMode2;
            this.queue = new SpscArrayQueue(prefetch2);
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.upstream, s)) {
                this.upstream = s;
                this.downstream.onSubscribe(this);
                s.request((long) this.prefetch);
            }
        }

        public void onNext(T t) {
            if (!this.queue.offer(t)) {
                this.upstream.cancel();
                onError(new MissingBackpressureException("queue full?!"));
                return;
            }
            drain();
        }

        public void onError(Throwable t) {
            if (this.errors.addThrowable(t)) {
                if (this.errorMode == ErrorMode.IMMEDIATE) {
                    this.inner.dispose();
                }
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void request(long n) {
            BackpressureHelper.add(this.requested, n);
            drain();
        }

        public void cancel() {
            this.cancelled = true;
            this.upstream.cancel();
            this.inner.dispose();
            if (getAndIncrement() == 0) {
                this.queue.clear();
                this.item = null;
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerSuccess(R item2) {
            this.item = item2;
            this.state = 2;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable ex) {
            if (this.errors.addThrowable(ex)) {
                if (this.errorMode != ErrorMode.END) {
                    this.upstream.cancel();
                }
                this.state = 0;
                drain();
                return;
            }
            RxJavaPlugins.onError(ex);
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super R> downstream2 = this.downstream;
                ErrorMode errorMode2 = this.errorMode;
                SimplePlainQueue<T> queue2 = this.queue;
                AtomicThrowable errors2 = this.errors;
                AtomicLong requested2 = this.requested;
                int i = 1;
                int limit = this.prefetch - (this.prefetch >> 1);
                while (true) {
                    if (this.cancelled) {
                        queue2.clear();
                        this.item = null;
                    }
                    int s = this.state;
                    if (errors2.get() == null || !(errorMode2 == ErrorMode.IMMEDIATE || (errorMode2 == ErrorMode.BOUNDARY && s == 0))) {
                        if (s == 0) {
                            boolean d = this.done;
                            T v = queue2.poll();
                            boolean empty = v == null;
                            if (d && empty) {
                                Throwable ex = errors2.terminate();
                                if (ex == null) {
                                    downstream2.onComplete();
                                } else {
                                    downstream2.onError(ex);
                                }
                                return;
                            } else if (!empty) {
                                int c = this.consumed + i;
                                if (c == limit) {
                                    this.consumed = 0;
                                    boolean z = d;
                                    this.upstream.request((long) limit);
                                } else {
                                    this.consumed = c;
                                }
                                try {
                                    SingleSource<? extends R> ss = (SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(v), "The mapper returned a null SingleSource");
                                    this.state = 1;
                                    ss.subscribe(this.inner);
                                } catch (Throwable th) {
                                    Throwable ex2 = th;
                                    Exceptions.throwIfFatal(ex2);
                                    this.upstream.cancel();
                                    queue2.clear();
                                    errors2.addThrowable(ex2);
                                    downstream2.onError(errors2.terminate());
                                    return;
                                }
                            }
                        } else if (s == 2) {
                            long e = this.emitted;
                            if (e != requested2.get()) {
                                R w = this.item;
                                this.item = null;
                                downstream2.onNext(w);
                                this.emitted = e + 1;
                                this.state = 0;
                                i = 1;
                            }
                        }
                        missed = addAndGet(-missed);
                        if (missed != 0) {
                            i = 1;
                        } else {
                            return;
                        }
                    }
                }
                queue2.clear();
                this.item = null;
                downstream2.onError(errors2.terminate());
            }
        }
    }

    public FlowableConcatMapSingle(Flowable<T> source2, Function<? super T, ? extends SingleSource<? extends R>> mapper2, ErrorMode errorMode2, int prefetch2) {
        this.source = source2;
        this.mapper = mapper2;
        this.errorMode = errorMode2;
        this.prefetch = prefetch2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ConcatMapSingleSubscriber<Object>(s, this.mapper, this.prefetch, this.errorMode));
    }
}
