package p005io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;
import p005io.reactivex.internal.util.LinkedArrayList;
import p005io.reactivex.internal.util.NotificationLite;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCache */
public final class FlowableCache<T> extends AbstractFlowableWithUpstream<T, T> {
    final AtomicBoolean once = new AtomicBoolean();
    final CacheState<T> state;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCache$CacheState */
    static final class CacheState<T> extends LinkedArrayList implements FlowableSubscriber<T> {
        static final ReplaySubscription[] EMPTY = new ReplaySubscription[0];
        static final ReplaySubscription[] TERMINATED = new ReplaySubscription[0];
        final AtomicReference<Subscription> connection = new AtomicReference<>();
        volatile boolean isConnected;
        final Flowable<T> source;
        boolean sourceDone;
        final AtomicReference<ReplaySubscription<T>[]> subscribers;

        CacheState(Flowable<T> source2, int capacityHint) {
            super(capacityHint);
            this.source = source2;
            this.subscribers = new AtomicReference<>(EMPTY);
        }

        public boolean addChild(ReplaySubscription<T> p) {
            ReplaySubscription<T>[] a;
            ReplaySubscription<T>[] b;
            do {
                a = (ReplaySubscription[]) this.subscribers.get();
                if (a == TERMINATED) {
                    return false;
                }
                int n = a.length;
                b = new ReplaySubscription[(n + 1)];
                System.arraycopy(a, 0, b, 0, n);
                b[n] = p;
            } while (!this.subscribers.compareAndSet(a, b));
            return true;
        }

        public void removeChild(ReplaySubscription<T> p) {
            ReplaySubscription<T>[] a;
            ReplaySubscription<T>[] b;
            do {
                a = (ReplaySubscription[]) this.subscribers.get();
                int n = a.length;
                if (n != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (a[i].equals(p)) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j >= 0) {
                        if (n == 1) {
                            b = EMPTY;
                        } else {
                            ReplaySubscription<T>[] b2 = new ReplaySubscription[(n - 1)];
                            System.arraycopy(a, 0, b2, 0, j);
                            System.arraycopy(a, j + 1, b2, j, (n - j) - 1);
                            b = b2;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(a, b));
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.setOnce(this.connection, s, Long.MAX_VALUE);
        }

        public void connect() {
            this.source.subscribe((FlowableSubscriber<? super T>) this);
            this.isConnected = true;
        }

        public void onNext(T t) {
            if (!this.sourceDone) {
                add(NotificationLite.next(t));
                for (ReplaySubscription<?> rp : (ReplaySubscription[]) this.subscribers.get()) {
                    rp.replay();
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.error(e));
                SubscriptionHelper.cancel(this.connection);
                for (ReplaySubscription<?> rp : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    rp.replay();
                }
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (!this.sourceDone) {
                this.sourceDone = true;
                add(NotificationLite.complete());
                SubscriptionHelper.cancel(this.connection);
                for (ReplaySubscription<?> rp : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    rp.replay();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCache$ReplaySubscription */
    static final class ReplaySubscription<T> extends AtomicInteger implements Subscription {
        private static final long CANCELLED = Long.MIN_VALUE;
        private static final long serialVersionUID = -2557562030197141021L;
        final Subscriber<? super T> child;
        Object[] currentBuffer;
        int currentIndexInBuffer;
        long emitted;
        int index;
        final AtomicLong requested = new AtomicLong();
        final CacheState<T> state;

        ReplaySubscription(Subscriber<? super T> child2, CacheState<T> state2) {
            this.child = child2;
            this.state = state2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this.requested, n);
                replay();
            }
        }

        public void cancel() {
            if (this.requested.getAndSet(CANCELLED) != CANCELLED) {
                this.state.removeChild(this);
            }
        }

        public void replay() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                Subscriber<? super T> child2 = this.child;
                AtomicLong rq = this.requested;
                long e = this.emitted;
                do {
                    long r = rq.get();
                    if (r != CANCELLED) {
                        int s = this.state.size();
                        if (s != 0) {
                            Object[] b = this.currentBuffer;
                            if (b == null) {
                                b = this.state.head();
                                this.currentBuffer = b;
                            }
                            int n = b.length - 1;
                            int j = this.index;
                            int k = this.currentIndexInBuffer;
                            while (j < s && e != r) {
                                if (rq.get() != CANCELLED) {
                                    if (k == n) {
                                        b = (Object[]) b[n];
                                        k = 0;
                                    }
                                    if (!NotificationLite.accept(b[k], child2)) {
                                        k++;
                                        j++;
                                        e++;
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            if (rq.get() != CANCELLED) {
                                if (r == e) {
                                    Object o = b[k];
                                    if (NotificationLite.isComplete(o)) {
                                        child2.onComplete();
                                        return;
                                    } else if (NotificationLite.isError(o)) {
                                        child2.onError(NotificationLite.getError(o));
                                        return;
                                    }
                                }
                                this.index = j;
                                this.currentIndexInBuffer = k;
                                this.currentBuffer = b;
                            } else {
                                return;
                            }
                        }
                        this.emitted = e;
                        missed = addAndGet(-missed);
                    } else {
                        return;
                    }
                } while (missed != 0);
            }
        }
    }

    public FlowableCache(Flowable<T> source, int capacityHint) {
        super(source);
        this.state = new CacheState<>(source, capacityHint);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> t) {
        ReplaySubscription<T> rp = new ReplaySubscription<>(t, this.state);
        t.onSubscribe(rp);
        boolean doReplay = true;
        if (this.state.addChild(rp) && rp.requested.get() == Long.MIN_VALUE) {
            this.state.removeChild(rp);
            doReplay = false;
        }
        if (!this.once.get() && this.once.compareAndSet(false, true)) {
            this.state.connect();
        }
        if (doReplay) {
            rp.replay();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isConnected() {
        return this.state.isConnected;
    }

    /* access modifiers changed from: 0000 */
    public boolean hasSubscribers() {
        return ((ReplaySubscription[]) this.state.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public int cachedEventCount() {
        return this.state.size();
    }
}
