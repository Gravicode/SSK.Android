package p008rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import p008rx.BackpressureOverflow;
import p008rx.BackpressureOverflow.Strategy;
import p008rx.Observable.Operator;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.functions.Action0;
import p008rx.internal.util.BackpressureDrainManager;
import p008rx.internal.util.BackpressureDrainManager.BackpressureQueueCallback;

/* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer */
public class OperatorOnBackpressureBuffer<T> implements Operator<T, T> {
    private final Long capacity;
    private final Action0 onOverflow;
    private final Strategy overflowStrategy;

    /* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer$BufferSubscriber */
    static final class BufferSubscriber<T> extends Subscriber<T> implements BackpressureQueueCallback {
        private final AtomicLong capacity;
        private final Subscriber<? super T> child;
        private final BackpressureDrainManager manager;
        private final Action0 onOverflow;
        private final Strategy overflowStrategy;
        private final ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<>();
        private final AtomicBoolean saturated = new AtomicBoolean(false);

        public BufferSubscriber(Subscriber<? super T> child2, Long capacity2, Action0 onOverflow2, Strategy overflowStrategy2) {
            this.child = child2;
            this.capacity = capacity2 != null ? new AtomicLong(capacity2.longValue()) : null;
            this.onOverflow = onOverflow2;
            this.manager = new BackpressureDrainManager(this);
            this.overflowStrategy = overflowStrategy2;
        }

        public void onStart() {
            request(Long.MAX_VALUE);
        }

        public void onCompleted() {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain();
            }
        }

        public void onError(Throwable e) {
            if (!this.saturated.get()) {
                this.manager.terminateAndDrain(e);
            }
        }

        public void onNext(T t) {
            if (assertCapacity()) {
                this.queue.offer(NotificationLite.next(t));
                this.manager.drain();
            }
        }

        public boolean accept(Object value) {
            return NotificationLite.accept(this.child, value);
        }

        public void complete(Throwable exception) {
            if (exception != null) {
                this.child.onError(exception);
            } else {
                this.child.onCompleted();
            }
        }

        public Object peek() {
            return this.queue.peek();
        }

        public Object poll() {
            Object value = this.queue.poll();
            if (!(this.capacity == null || value == null)) {
                this.capacity.incrementAndGet();
            }
            return value;
        }

        private boolean assertCapacity() {
            long currCapacity;
            if (this.capacity == null) {
                return true;
            }
            do {
                currCapacity = this.capacity.get();
                if (currCapacity <= 0) {
                    boolean hasCapacity = false;
                    try {
                        hasCapacity = this.overflowStrategy.mayAttemptDrop() && poll() != null;
                    } catch (MissingBackpressureException e) {
                        if (this.saturated.compareAndSet(false, true)) {
                            unsubscribe();
                            this.child.onError(e);
                        }
                    }
                    if (this.onOverflow != null) {
                        try {
                            this.onOverflow.call();
                        } catch (Throwable e2) {
                            Exceptions.throwIfFatal(e2);
                            this.manager.terminateAndDrain(e2);
                            return false;
                        }
                    }
                    if (!hasCapacity) {
                        return false;
                    }
                }
            } while (!this.capacity.compareAndSet(currCapacity, currCapacity - 1));
            return true;
        }

        /* access modifiers changed from: protected */
        public Producer manager() {
            return this.manager;
        }
    }

    /* renamed from: rx.internal.operators.OperatorOnBackpressureBuffer$Holder */
    static final class Holder {
        static final OperatorOnBackpressureBuffer<?> INSTANCE = new OperatorOnBackpressureBuffer<>();

        Holder() {
        }
    }

    public static <T> OperatorOnBackpressureBuffer<T> instance() {
        return Holder.INSTANCE;
    }

    OperatorOnBackpressureBuffer() {
        this.capacity = null;
        this.onOverflow = null;
        this.overflowStrategy = BackpressureOverflow.ON_OVERFLOW_DEFAULT;
    }

    public OperatorOnBackpressureBuffer(long capacity2) {
        this(capacity2, null, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long capacity2, Action0 onOverflow2) {
        this(capacity2, onOverflow2, BackpressureOverflow.ON_OVERFLOW_DEFAULT);
    }

    public OperatorOnBackpressureBuffer(long capacity2, Action0 onOverflow2, Strategy overflowStrategy2) {
        if (capacity2 <= 0) {
            throw new IllegalArgumentException("Buffer capacity must be > 0");
        } else if (overflowStrategy2 == null) {
            throw new NullPointerException("The BackpressureOverflow strategy must not be null");
        } else {
            this.capacity = Long.valueOf(capacity2);
            this.onOverflow = onOverflow2;
            this.overflowStrategy = overflowStrategy2;
        }
    }

    public Subscriber<? super T> call(Subscriber<? super T> child) {
        BufferSubscriber<T> parent = new BufferSubscriber<>(child, this.capacity, this.onOverflow, this.overflowStrategy);
        child.add(parent);
        child.setProducer(parent.manager());
        return parent;
    }
}
