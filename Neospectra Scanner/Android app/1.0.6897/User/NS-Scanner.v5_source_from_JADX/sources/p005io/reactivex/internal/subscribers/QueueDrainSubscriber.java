package p005io.reactivex.internal.subscribers;

import org.reactivestreams.Subscriber;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.exceptions.MissingBackpressureException;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.QueueDrain;
import p005io.reactivex.internal.util.QueueDrainHelper;

/* renamed from: io.reactivex.internal.subscribers.QueueDrainSubscriber */
public abstract class QueueDrainSubscriber<T, U, V> extends QueueDrainSubscriberPad4 implements FlowableSubscriber<T>, QueueDrain<U, V> {
    protected final Subscriber<? super V> actual;
    /* access modifiers changed from: protected */
    public volatile boolean cancelled;
    protected volatile boolean done;
    protected Throwable error;
    /* access modifiers changed from: protected */
    public final SimplePlainQueue<U> queue;

    public QueueDrainSubscriber(Subscriber<? super V> actual2, SimplePlainQueue<U> queue2) {
        this.actual = actual2;
        this.queue = queue2;
    }

    public final boolean cancelled() {
        return this.cancelled;
    }

    public final boolean done() {
        return this.done;
    }

    public final boolean enter() {
        return this.wip.getAndIncrement() == 0;
    }

    public final boolean fastEnter() {
        return this.wip.get() == 0 && this.wip.compareAndSet(0, 1);
    }

    /* access modifiers changed from: protected */
    public final void fastPathEmitMax(U value, boolean delayError, Disposable dispose) {
        Subscriber<? super V> s = this.actual;
        SimplePlainQueue<U> q = this.queue;
        if (fastEnter()) {
            long r = this.requested.get();
            if (r != 0) {
                if (accept(s, value) && r != Long.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                dispose.dispose();
                s.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            }
        } else {
            q.offer(value);
            if (!enter()) {
                return;
            }
        }
        QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
    }

    /* access modifiers changed from: protected */
    public final void fastPathOrderedEmitMax(U value, boolean delayError, Disposable dispose) {
        Subscriber<? super V> s = this.actual;
        SimplePlainQueue<U> q = this.queue;
        if (fastEnter()) {
            long r = this.requested.get();
            if (r == 0) {
                this.cancelled = true;
                dispose.dispose();
                s.onError(new MissingBackpressureException("Could not emit buffer due to lack of requests"));
                return;
            } else if (q.isEmpty()) {
                if (accept(s, value) && r != Long.MAX_VALUE) {
                    produced(1);
                }
                if (leave(-1) == 0) {
                    return;
                }
            } else {
                q.offer(value);
            }
        } else {
            q.offer(value);
            if (!enter()) {
                return;
            }
        }
        QueueDrainHelper.drainMaxLoop(q, s, delayError, dispose, this);
    }

    public boolean accept(Subscriber<? super V> subscriber, U u) {
        return false;
    }

    public final Throwable error() {
        return this.error;
    }

    public final int leave(int m) {
        return this.wip.addAndGet(m);
    }

    public final long requested() {
        return this.requested.get();
    }

    public final long produced(long n) {
        return this.requested.addAndGet(-n);
    }

    public final void requested(long n) {
        if (SubscriptionHelper.validate(n)) {
            BackpressureHelper.add(this.requested, n);
        }
    }
}
