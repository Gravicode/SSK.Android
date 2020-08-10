package p008rx.internal.operators;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import p008rx.Emitter;
import p008rx.Emitter.BackpressureMode;
import p008rx.Observable.OnSubscribe;
import p008rx.Producer;
import p008rx.Subscriber;
import p008rx.Subscription;
import p008rx.exceptions.Exceptions;
import p008rx.exceptions.MissingBackpressureException;
import p008rx.functions.Action1;
import p008rx.functions.Cancellable;
import p008rx.internal.util.RxRingBuffer;
import p008rx.internal.util.atomic.SpscUnboundedAtomicArrayQueue;
import p008rx.internal.util.unsafe.SpscUnboundedArrayQueue;
import p008rx.internal.util.unsafe.UnsafeAccess;
import p008rx.plugins.RxJavaHooks;
import p008rx.subscriptions.SerialSubscription;

/* renamed from: rx.internal.operators.OnSubscribeFromEmitter */
public final class OnSubscribeFromEmitter<T> implements OnSubscribe<T> {
    final Action1<Emitter<T>> Emitter;
    final BackpressureMode backpressure;

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$BaseEmitter */
    static abstract class BaseEmitter<T> extends AtomicLong implements Emitter<T>, Producer, Subscription {
        private static final long serialVersionUID = 7326289992464377023L;
        final Subscriber<? super T> actual;
        final SerialSubscription serial = new SerialSubscription();

        public BaseEmitter(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onCompleted() {
            if (!this.actual.isUnsubscribed()) {
                try {
                    this.actual.onCompleted();
                } finally {
                    this.serial.unsubscribe();
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.actual.isUnsubscribed()) {
                try {
                    this.actual.onError(e);
                } finally {
                    this.serial.unsubscribe();
                }
            }
        }

        public final void unsubscribe() {
            this.serial.unsubscribe();
            onUnsubscribed();
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
        }

        public final boolean isUnsubscribed() {
            return this.serial.isUnsubscribed();
        }

        public final void request(long n) {
            if (BackpressureUtils.validate(n)) {
                BackpressureUtils.getAndAddRequest(this, n);
                onRequested();
            }
        }

        /* access modifiers changed from: 0000 */
        public void onRequested() {
        }

        public final void setSubscription(Subscription s) {
            this.serial.set(s);
        }

        public final void setCancellation(Cancellable c) {
            setSubscription(new CancellableSubscription(c));
        }

        public final long requested() {
            return get();
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$BufferEmitter */
    static final class BufferEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 2427151001689639875L;
        volatile boolean done;
        Throwable error;
        final Queue<Object> queue;
        final AtomicInteger wip;

        public BufferEmitter(Subscriber<? super T> actual, int capacityHint) {
            super(actual);
            this.queue = UnsafeAccess.isUnsafeAvailable() ? new SpscUnboundedArrayQueue<>(capacityHint) : new SpscUnboundedAtomicArrayQueue<>(capacityHint);
            this.wip = new AtomicInteger();
        }

        public void onNext(T t) {
            this.queue.offer(NotificationLite.next(t));
            drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onRequested() {
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
            if (this.wip.getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = this.actual;
                Queue<Object> q = this.queue;
                do {
                    long r = get();
                    long e = 0;
                    while (e != r) {
                        if (a.isUnsubscribed()) {
                            q.clear();
                            return;
                        }
                        boolean d = this.done;
                        Object o = q.poll();
                        boolean empty = o == null;
                        if (d && empty) {
                            Throwable ex = this.error;
                            if (ex != null) {
                                super.onError(ex);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        } else if (empty) {
                            break;
                        } else {
                            a.onNext(NotificationLite.getValue(o));
                            e++;
                        }
                    }
                    if (e == r) {
                        if (a.isUnsubscribed()) {
                            q.clear();
                            return;
                        }
                        boolean d2 = this.done;
                        boolean empty2 = q.isEmpty();
                        if (d2 && empty2) {
                            Throwable ex2 = this.error;
                            if (ex2 != null) {
                                super.onError(ex2);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        }
                    }
                    if (e != 0) {
                        BackpressureUtils.produced(this, e);
                    }
                    missed = this.wip.addAndGet(-missed);
                } while (missed != 0);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$CancellableSubscription */
    static final class CancellableSubscription extends AtomicReference<Cancellable> implements Subscription {
        private static final long serialVersionUID = 5718521705281392066L;

        public CancellableSubscription(Cancellable cancellable) {
            super(cancellable);
        }

        public boolean isUnsubscribed() {
            return get() == null;
        }

        public void unsubscribe() {
            if (get() != null) {
                Cancellable c = (Cancellable) getAndSet(null);
                if (c != null) {
                    try {
                        c.cancel();
                    } catch (Exception ex) {
                        Exceptions.throwIfFatal(ex);
                        RxJavaHooks.onError(ex);
                    }
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$DropEmitter */
    static final class DropEmitter<T> extends NoOverflowBaseEmitter<T> {
        private static final long serialVersionUID = 8360058422307496563L;

        public DropEmitter(Subscriber<? super T> actual) {
            super(actual);
        }

        /* access modifiers changed from: 0000 */
        public void onOverflow() {
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$ErrorEmitter */
    static final class ErrorEmitter<T> extends NoOverflowBaseEmitter<T> {
        private static final long serialVersionUID = 338953216916120960L;
        private boolean done;

        public ErrorEmitter(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onNext(T t) {
            if (!this.done) {
                super.onNext(t);
            }
        }

        public void onCompleted() {
            if (!this.done) {
                this.done = true;
                super.onCompleted();
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaHooks.onError(e);
                return;
            }
            this.done = true;
            super.onError(e);
        }

        /* access modifiers changed from: 0000 */
        public void onOverflow() {
            onError(new MissingBackpressureException("fromEmitter: could not emit value due to lack of requests"));
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$LatestEmitter */
    static final class LatestEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 4023437720691792495L;
        volatile boolean done;
        Throwable error;
        final AtomicReference<Object> queue = new AtomicReference<>();
        final AtomicInteger wip = new AtomicInteger();

        public LatestEmitter(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onNext(T t) {
            this.queue.set(NotificationLite.next(t));
            drain();
        }

        public void onError(Throwable e) {
            this.error = e;
            this.done = true;
            drain();
        }

        public void onCompleted() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onRequested() {
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void onUnsubscribed() {
            if (this.wip.getAndIncrement() == 0) {
                this.queue.lazySet(null);
            }
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            boolean empty;
            if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> a = this.actual;
                AtomicReference<Object> q = this.queue;
                do {
                    long r = get();
                    long e = 0;
                    while (true) {
                        empty = false;
                        if (e == r) {
                            break;
                        } else if (a.isUnsubscribed()) {
                            q.lazySet(null);
                            return;
                        } else {
                            boolean d = this.done;
                            Object o = q.getAndSet(null);
                            boolean empty2 = o == null;
                            if (d && empty2) {
                                Throwable ex = this.error;
                                if (ex != null) {
                                    super.onError(ex);
                                } else {
                                    super.onCompleted();
                                }
                                return;
                            } else if (empty2) {
                                break;
                            } else {
                                a.onNext(NotificationLite.getValue(o));
                                e++;
                            }
                        }
                    }
                    if (e == r) {
                        if (a.isUnsubscribed()) {
                            q.lazySet(null);
                            return;
                        }
                        boolean d2 = this.done;
                        if (q.get() == null) {
                            empty = true;
                        }
                        if (d2 && empty) {
                            Throwable ex2 = this.error;
                            if (ex2 != null) {
                                super.onError(ex2);
                            } else {
                                super.onCompleted();
                            }
                            return;
                        }
                    }
                    if (e != 0) {
                        BackpressureUtils.produced(this, e);
                    }
                    missed = this.wip.addAndGet(-missed);
                } while (missed != 0);
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$NoOverflowBaseEmitter */
    static abstract class NoOverflowBaseEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 4127754106204442833L;

        /* access modifiers changed from: 0000 */
        public abstract void onOverflow();

        public NoOverflowBaseEmitter(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onNext(T t) {
            if (!this.actual.isUnsubscribed()) {
                if (get() != 0) {
                    this.actual.onNext(t);
                    BackpressureUtils.produced(this, 1);
                } else {
                    onOverflow();
                }
            }
        }
    }

    /* renamed from: rx.internal.operators.OnSubscribeFromEmitter$NoneEmitter */
    static final class NoneEmitter<T> extends BaseEmitter<T> {
        private static final long serialVersionUID = 3776720187248809713L;

        public NoneEmitter(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onNext(T t) {
            long r;
            if (!this.actual.isUnsubscribed()) {
                this.actual.onNext(t);
                do {
                    r = get();
                    if (r == 0) {
                        break;
                    }
                } while (!compareAndSet(r, r - 1));
            }
        }
    }

    public OnSubscribeFromEmitter(Action1<Emitter<T>> Emitter2, BackpressureMode backpressure2) {
        this.Emitter = Emitter2;
        this.backpressure = backpressure2;
    }

    public void call(Subscriber<? super T> t) {
        BaseEmitter<T> emitter;
        switch (this.backpressure) {
            case NONE:
                emitter = new NoneEmitter<>(t);
                break;
            case ERROR:
                emitter = new ErrorEmitter<>(t);
                break;
            case DROP:
                emitter = new DropEmitter<>(t);
                break;
            case LATEST:
                emitter = new LatestEmitter<>(t);
                break;
            default:
                emitter = new BufferEmitter<>(t, RxRingBuffer.SIZE);
                break;
        }
        t.add(emitter);
        t.setProducer(emitter);
        this.Emitter.call(emitter);
    }
}
