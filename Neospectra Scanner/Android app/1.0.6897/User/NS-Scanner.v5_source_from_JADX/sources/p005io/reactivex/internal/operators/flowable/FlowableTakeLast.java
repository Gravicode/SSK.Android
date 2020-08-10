package p005io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p005io.reactivex.Flowable;
import p005io.reactivex.FlowableSubscriber;
import p005io.reactivex.internal.subscriptions.SubscriptionHelper;
import p005io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLast */
public final class FlowableTakeLast<T> extends AbstractFlowableWithUpstream<T, T> {
    final int count;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLast$TakeLastSubscriber */
    static final class TakeLastSubscriber<T> extends ArrayDeque<T> implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 7240042530241604978L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final int count;
        volatile boolean done;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f213s;
        final AtomicInteger wip = new AtomicInteger();

        TakeLastSubscriber(Subscriber<? super T> actual2, int count2) {
            this.actual = actual2;
            this.count = count2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.validate(this.f213s, s)) {
                this.f213s = s;
                this.actual.onSubscribe(this);
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.count == size()) {
                poll();
            }
            offer(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f213s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super T> a = this.actual;
                long r = this.requested.get();
                while (!this.cancelled) {
                    if (this.done) {
                        long e = 0;
                        while (e != r) {
                            if (!this.cancelled) {
                                T v = poll();
                                if (v == null) {
                                    a.onComplete();
                                    return;
                                } else {
                                    a.onNext(v);
                                    e++;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(e == 0 || r == Long.MAX_VALUE)) {
                            r = this.requested.addAndGet(-e);
                        }
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }
    }

    public FlowableTakeLast(Flowable<T> source, int count2) {
        super(source);
        this.count = count2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe((FlowableSubscriber<? super T>) new TakeLastSubscriber<Object>(s, this.count));
    }
}
