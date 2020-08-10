package p008rx.internal.producers;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.Observer;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.internal.operators.BackpressureUtils;
import p008rx.internal.util.atomic.SpscLinkedAtomicQueue;
import p008rx.internal.util.unsafe.SpscLinkedQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;

/* renamed from: rx.internal.producers.QueuedValueProducer */
public final class QueuedValueProducer<T> extends AtomicLong implements Producer {
    static final Object NULL_SENTINEL = new Object();
    private static final long serialVersionUID = 7277121710709137047L;
    final Subscriber<? super T> child;
    final Queue<Object> queue;
    final AtomicInteger wip;

    public QueuedValueProducer(Subscriber<? super T> child2) {
        this(child2, UnsafeAccess.isUnsafeAvailable() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }

    public QueuedValueProducer(Subscriber<? super T> child2, Queue<Object> queue2) {
        this.child = child2;
        this.queue = queue2;
        this.wip = new AtomicInteger();
    }

    public void request(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n >= 0 required");
        } else if (n > 0) {
            BackpressureUtils.getAndAddRequest(this, n);
            drain();
        }
    }

    public boolean offer(T value) {
        if (value == null) {
            if (!this.queue.offer(NULL_SENTINEL)) {
                return false;
            }
        } else if (!this.queue.offer(value)) {
            return false;
        }
        drain();
        return true;
    }

    private void drain() {
        if (this.wip.getAndIncrement() == 0) {
            Subscriber<? super T> c = this.child;
            Queue<Object> q = this.queue;
            while (!c.isUnsubscribed()) {
                this.wip.lazySet(1);
                long r = get();
                long e = 0;
                while (r != 0) {
                    Object poll = q.poll();
                    Object v = poll;
                    if (poll == null) {
                        break;
                    }
                    Object obj = null;
                    try {
                        if (v == NULL_SENTINEL) {
                            c.onNext(null);
                        } else {
                            c.onNext(v);
                        }
                        if (!c.isUnsubscribed()) {
                            r--;
                            e++;
                        } else {
                            return;
                        }
                    } catch (Throwable ex) {
                        if (v != NULL_SENTINEL) {
                            obj = v;
                        }
                        Exceptions.throwOrReport(ex, (Observer<?>) c, obj);
                        return;
                    }
                }
                if (!(e == 0 || get() == Long.MAX_VALUE)) {
                    addAndGet(-e);
                }
                if (this.wip.decrementAndGet() == 0) {
                }
            }
        }
    }
}
