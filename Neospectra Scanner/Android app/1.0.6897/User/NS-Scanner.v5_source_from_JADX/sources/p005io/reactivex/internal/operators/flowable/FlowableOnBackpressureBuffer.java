package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.exceptions.Exceptions;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.functions.Action;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.queue.SpscArrayQueue;
import p005io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p005io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer */
public final class FlowableOnBackpressureBuffer<T> extends AbstractFlowableWithUpstream<T, T> {
    final int bufferSize;
    final boolean delayError;
    final Action onOverflow;
    final boolean unbounded;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer$BackpressureBufferSubscriber */
    static final class BackpressureBufferSubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -2514538129242366402L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final Action onOverflow;
        boolean outputFused;
        final SimplePlainQueue<T> queue;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f175s;

        BackpressureBufferSubscriber(Subscriber<? super T> actual2, int bufferSize, boolean unbounded, boolean delayError2, Action onOverflow2) {
            SimplePlainQueue<T> q;
            this.actual = actual2;
            this.onOverflow = onOverflow2;
            this.delayError = delayError2;
            if (unbounded) {
                q = new SpscLinkedArrayQueue<>(bufferSize);
            } else {
                q = new SpscArrayQueue<>(bufferSize);
            }
            this.queue = q;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f175s, s)) {
                this.f175s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.queue.offer(t)) {
                this.f175s.cancel();
                MissingBackpressureException ex = new MissingBackpressureException("Buffer is full");
                try {
                    this.onOverflow.run();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    ex.initCause(e);
                }
                onError(ex);
                return;
            }
            if (this.outputFused) {
                this.actual.onNext(null);
            } else {
                drain();
            }
        }

        public void onError(Throwable t) {
            this.error = t;
            this.done = true;
            if (this.outputFused) {
                this.actual.onError(t);
            } else {
                drain();
            }
        }

        public void onComplete() {
            this.done = true;
            if (this.outputFused) {
                this.actual.onComplete();
            } else {
                drain();
            }
        }

        public void request(long n) {
            if (!this.outputFused && SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f175s.cancel();
                if (getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                SimplePlainQueue<T> q = this.queue;
                Subscriber<? super T> a = this.actual;
                while (!checkTerminated(this.done, q.isEmpty(), a)) {
                    long r = this.requested.get();
                    long e = 0;
                    while (e != r) {
                        boolean d = this.done;
                        T v = q.poll();
                        boolean empty = v == null;
                        if (!checkTerminated(d, empty, a)) {
                            if (empty) {
                                break;
                            }
                            a.onNext(v);
                            e++;
                        } else {
                            return;
                        }
                    }
                    if (e != r || !checkTerminated(this.done, q.isEmpty(), a)) {
                        if (!(e == 0 || r == Long.MAX_VALUE)) {
                            this.requested.addAndGet(-e);
                        }
                        missed = addAndGet(-missed);
                        if (missed == 0) {
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean d, boolean empty, Subscriber<? super T> a) {
            if (this.cancelled) {
                this.queue.clear();
                return true;
            }
            if (d) {
                if (!this.delayError) {
                    Throwable e = this.error;
                    if (e != null) {
                        this.queue.clear();
                        a.onError(e);
                        return true;
                    } else if (empty) {
                        a.onComplete();
                        return true;
                    }
                } else if (empty) {
                    Throwable e2 = this.error;
                    if (e2 != null) {
                        a.onError(e2);
                    } else {
                        a.onComplete();
                    }
                    return true;
                }
            }
            return false;
        }

        public int requestFusion(int mode) {
            if ((mode & 2) == 0) {
                return 0;
            }
            this.outputFused = true;
            return 2;
        }

        @Nullable
        public T poll() throws Exception {
            return this.queue.poll();
        }

        public void clear() {
            this.queue.clear();
        }

        public boolean isEmpty() {
            return this.queue.isEmpty();
        }
    }

    public FlowableOnBackpressureBuffer(Flowable<T> source, int bufferSize2, boolean unbounded2, boolean delayError2, Action onOverflow2) {
        super(source);
        this.bufferSize = bufferSize2;
        this.unbounded = unbounded2;
        this.delayError = delayError2;
        this.onOverflow = onOverflow2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        Flowable flowable = this.source;
        BackpressureBufferSubscriber backpressureBufferSubscriber = new BackpressureBufferSubscriber(s, this.bufferSize, this.unbounded, this.delayError, this.onOverflow);
        flowable.subscribe((FlowableSubscriber<? super T>) backpressureBufferSubscriber);
    }
}
